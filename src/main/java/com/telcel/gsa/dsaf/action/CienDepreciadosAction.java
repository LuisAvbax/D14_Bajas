package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.BitacoraBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.CienDepreciadosService;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.collections.Predicate;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Service("cienDprAction")
@ViewScoped
public class CienDepreciadosAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2133087251636481367L;

	private static Logger logger = LoggerFactory.getLogger(CienDepreciadosAction.class);
	
	private transient UploadedFile uploadedFile;
	
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	
	@Autowired
	CienDepreciadosService cienDepreciadosService;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	
	@Autowired
	CienDepreciadosService dprCien;

	
	public void initFlow(RequestContext ctx) {
		logger.info(Constants.APP_ID + "Comienza Pantalla");
		BajasDprCienFormBean datBean = new BajasDprCienFormBean();
		Date fActual = utileriasCfeService.obtenerFechaActual(sessionScopeUser);
		datBean.setFechaCarga(fActual);
		MesBean mes = new MesBean();
		datBean.setMes(mes);
		datBean.setGuardar(true);
		datBean.setMeses(sessionScopeUser.getMeses());
		datBean.setAnios(sessionScopeUser.getAnios());
		datBean.setDelDisabled(true);
		List<BajasDepreciadosCienBean> listBean = new ArrayList<BajasDepreciadosCienBean>();
		datBean.setListBean(listBean);
		ctx.getFlowScope().put("datDpr", datBean);
	}
	
	public void consultaDpr(RequestContext ctx){
		BajasDprCienFormBean dpr = (BajasDprCienFormBean)ctx.getFlowScope().get("datDpr");
		dpr.setIdSociedad(sessionScopeUser.getSociedad().getId());
		logger.info("sigue");
		List<BajasDepreciadosCienBean> datDeprecia = cienDepreciadosService.consultaCienDpr(dpr, sessionScopeUser);
		dpr.setListBean(datDeprecia);
		if(datDeprecia != null && !datDeprecia.isEmpty()){
			dpr.setDescarga(true);
			dpr.setDelDisabled(false);
		}else{
			dpr.setDescarga(false);
			dpr.setDelDisabled(true);
		}
		ctx.getFlowScope().put("datDpr", dpr);
		
		
	}
	
	public void descarga(RequestContext ctx){
		BajasDprCienFormBean datBean = (BajasDprCienFormBean)ctx.getFlowScope().get("datDpr");
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_BITACORA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = cienDepreciadosService.generaDocumento(datBean.getListBean());
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
	}
	
	
	public void manageSelectAll(RequestContext ctx) {
		BajasDprCienFormBean datBean = (BajasDprCienFormBean)ctx.getFlowScope().get("datDpr");
		if(datBean.isTodos()) {
			for(BajasDepreciadosCienBean row: datBean.getListBean()) {
				row.setSelected(true);
			}
		}else {
			for(BajasDepreciadosCienBean row: datBean.getListBean()) {
				row.setSelected(false);
			}
		}
		ctx.getFlowScope().put("datDpr", datBean);
	}
	
	public void delAsset(RequestContext ctx) {
		BajasDprCienFormBean datBean = (BajasDprCienFormBean)ctx.getFlowScope().get("datDpr");
		List<BajasDepreciadosCienBean> datDeprecia = cienDepreciadosService.deleteAssets(datBean.getListBean(), sessionScopeUser);
		ctx.getFlowScope().put("datDpr", datBean);
	}

	

}
