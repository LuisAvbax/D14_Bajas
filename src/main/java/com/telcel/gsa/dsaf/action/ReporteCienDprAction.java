package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.webflow.execution.RequestContext;

import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ReporteCienDprBaService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reporteCienDeprAct")
@ViewScoped
public class ReporteCienDprAction implements Serializable {

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(CargaCienDprBaAction.class);

	@Autowired
	private SessionScopeUser sessionScopeUser;

	@Autowired
	UtileriasCfeService utileriasCfeService;

	@Autowired
	ReporteCienDprBaService cienDprService;

	public void initFlow(RequestContext ctx) {
		BajasDprCienFormBean dprBean = new BajasDprCienFormBean();
		Date fActual = utileriasCfeService.obtenerFechaActual(sessionScopeUser);
		dprBean.setFechaCarga(fActual);
		MesBean mes = new MesBean();
		dprBean.setMes(mes);
		dprBean.setGuardar(true);
		dprBean.setMeses(sessionScopeUser.getMeses());
		dprBean.setAnios(sessionScopeUser.getAnios());
		dprBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		dprBean.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
		dprBean.setDescarga(false);
		BajasOpDocumentoBean documentoBean;
		try {
			documentoBean = cienDprService.obtenerDocumentoReciente(dprBean);
			if (documentoBean != null) {
				dprBean.setOpDocumentoBean(documentoBean);
				if (documentoBean.getEstatus().getId().equals(3)) {
					dprBean.setDescarga(true);
				}
			}
			ctx.getFlowScope().put("datDpr", dprBean);
		} catch (CfeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrio un error al obtener el reporte reciente"));
			e.printStackTrace();
		} catch (SQLException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrio un error al obtener el reporte de la BD"));
			e.printStackTrace();
		}
	}

	public void generaSolicReporte(RequestContext ctx) throws ServletException, IOException {
		BajasDprCienFormBean dprBean = (BajasDprCienFormBean) ctx.getFlowScope().get("datDpr");
		dprBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		dprBean.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		String nombreRepo = "Bitacora"
				+ sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "")
				+ Constants.REP_EXCELU2003_EXT;
		try {
			cienDprService.insertaSolicitud(dprBean, nombreRepo);
			cienDprService.crearReporte(dprBean);
			ctx.getFlowScope().put("datDpr", dprBean);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO",
					"SOLICITUD REGISTRADA, SE NOTIFICARÁ VIA CORREO AL TERMINAR DE GENERAR EL ARCHIVO..."));
		} catch (CfeException e) {
			throw new CfeException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(), e);
		}
	}

	public void consultaSolcitudReporte(RequestContext ctx) throws ServletException, IOException {
		BajasDprCienFormBean dprBean = (BajasDprCienFormBean) ctx.getFlowScope().get("datDpr");
		dprBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		dprBean.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
		dprBean.setDescarga(false);
		dprBean.setOpDocumentoBean(null);
		try {
			BajasOpDocumentoBean documentoBean = cienDprService.obtenerDocumento(dprBean);
			if (documentoBean != null) {
				dprBean.setOpDocumentoBean(documentoBean);
				if (documentoBean.getEstatus().getId().equals(3)) {
					dprBean.setDescarga(true);
				}
			}
			ctx.getFlowScope().put("datDpr", dprBean);
		} catch (CfeException e) {
			throw new CfeException(e.getMessage(), e);
		} catch (SQLException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error al obtener el reporte"));
			e.printStackTrace();
		}
	}

	public void descargaRptProcesado(RequestContext ctx) {
		BajasDprCienFormBean datBean = (BajasDprCienFormBean) ctx.getFlowScope().get("datDpr");

		datBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		datBean.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());

		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();

		ServletOutputStream out;
		try {
			Workbook doc = cienDprService.obtenerReporte(datBean.getOpDocumentoBean().getId(),datBean.getIdSociedad());
			if (doc != null) {
				httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP,
						"attachment; filename=" + datBean.getOpDocumentoBean().getNombre());
				httpServletResponse.setContentType("application/vnd.ms-excel");
				out = httpServletResponse.getOutputStream();
				doc.write(out);
				doc.close();
				FacesContext.getCurrentInstance().responseComplete();
				out.flush();
				out.close();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No hay reporte que descargar"));
			}
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} catch (CfeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error al descargar"));
			e.printStackTrace();
		}

	}

}
