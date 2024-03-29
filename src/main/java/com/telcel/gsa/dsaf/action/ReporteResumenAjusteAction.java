package com.telcel.gsa.dsaf.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;

import com.telcel.gsa.dsaf.bean.ReporteDetalladoPorTipoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ReporteResumenAjusteService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.service.UtileriasService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("repResumenAjustAct")
@ViewScoped
public class ReporteResumenAjusteAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(ReporteResumenAjusteAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private ReporteResumenAjusteService reporteResumenAjusteService;

		
	public void initFlow(RequestContext ctx){
	
		ReporteDetalladoPorTipoBean reporteDetallado = new ReporteDetalladoPorTipoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		reporteDetallado.setAcum(false);
		reporteDetallado.setMeses(meses);
		reporteDetallado.setAnio(calendar.get(Calendar.YEAR));
		reporteDetallado.setAnios(sessionScopeUser.getAnios());
		reporteDetallado.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		reporteDetallado.setCalculo(new BajasCalculoBean());
		reporteDetallado.setClase(new ArrayList<String>());
		reporteDetallado.setClases(new ArrayList<BajasClaseBean>());
		reporteDetallado.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		reporteDetallado.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		reporteDetallado.setTextos(Arrays.asList(txts));
		reporteDetallado.setTexto("TODOS");

		reporteDetallado.setAjuste(new BajasAjustesBean());
		reporteDetallado.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		reporteDetallado.setRegDisabled(true);
		reporteDetallado.setClaseDisabled(true);
		reporteDetallado.setTxtDisabled(true);
		reporteDetallado.setQueryDisabled(true);
		reporteDetallado.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void computeYearVal(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		if(reporteDetallado.getMesesselect() == null || reporteDetallado.getMesesselect().length == 0){
			reporteDetallado.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			reporteDetallado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		reporteDetallado.setClases(utileriasCfeService.getClasesGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(),sessionScopeUser));
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		if(reporteDetallado.getCalculo().getId() != 0){
			reporteDetallado.setRegDisabled(false);
			reporteDetallado.setClaseDisabled(false);
			reporteDetallado.setTxtDisabled(false);
		}else{
			reporteDetallado.setRegDisabled(true);
			reporteDetallado.setClaseDisabled(true);
			reporteDetallado.setTxtDisabled(true);
		}
		if(reporteDetallado.getMesesselect() == null || reporteDetallado.getMesesselect().length == 0){
			reporteDetallado.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			reporteDetallado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void computeRegDependants(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		reporteDetallado.setClases(utileriasCfeService.getClasesGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(),sessionScopeUser));
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		if(reporteDetallado.getCalculo().getId() != 0){
			reporteDetallado.setRegDisabled(false);
			reporteDetallado.setClaseDisabled(false);
			reporteDetallado.setTxtDisabled(false);
		}else{
			reporteDetallado.setRegDisabled(true);
			reporteDetallado.setClaseDisabled(true);
			reporteDetallado.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void computeClassDependants(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void updateTxt(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		if(reporteDetallado.isAcum()){
			if(reporteDetallado.getMesesselect() != null && reporteDetallado.getMesesselect().length != 0){
				reporteDetallado.setMesesselect(utileriasCfeService.getAcumMonths(reporteDetallado.getMesesselect()));
			}else{
				reporteDetallado.setMesesselect(new String[]{});
			}
		}else{
			reporteDetallado.setMesesselect(new String []{});
		}
		
		if(reporteDetallado.getMesesselect() == null || reporteDetallado.getMesesselect().length == 0){
			reporteDetallado.setQueryDisabled(true);
		}else{
			reporteDetallado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	public void validaAcumulado(RequestContext ctx){
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		if(reporteDetallado.isAcum()){
			reporteDetallado.setMesesselect(utileriasCfeService.getAcumMonths(reporteDetallado.getMesesselect()));
		}
		if(reporteDetallado.getMesesselect() == null || reporteDetallado.getMesesselect().length == 0){
			reporteDetallado.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			reporteDetallado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		reporteDetallado.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(reporteDetallado.isAcum(),reporteDetallado.getMesesselect(),reporteDetallado.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+reporteDetallado.getMesesselect());//ene, feb
		logger.info("mes size: " + reporteDetallado.getMesesselect().length);
		if(reporteDetallado.getMesesselect().length > 0){
			for(int i = 0; i < reporteDetallado.getMesesselect().length; i++){
				logger.info(reporteDetallado.getMesesselect()[i]);
			}
		}
		logger.info("a�o"+reporteDetallado.getAnio());
		logger.info("calculo"+reporteDetallado.getCalculo().getId());//id
		logger.info("region"+reporteDetallado.getRegion());//id
		logger.info("clase"+reporteDetallado.getClase());//cve
		logger.info("textos"+reporteDetallado.getTexto());//todos
		logger.info("textoSeleccion"+reporteDetallado.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		reporteDetallado = reporteResumenAjusteService.datosWhere(reporteDetallado);

		
		
		char a = Integer.toString(reporteDetallado.getAnio()).charAt(2); 
		char aa = Integer.toString(reporteDetallado.getAnio()).charAt(3); 
		String an = a+""+aa;
		reporteDetallado.setAnioRepCorto(an);

			
			ajuste.setClave(reporteDetallado.getAjuste().getClave());
			reporteDetallado = reporteResumenAjusteService.consultaResumenAjuste(reporteDetallado, sessionScopeUser);
		
		reporteDetallado.setAjuste(ajuste);
		reporteDetallado.setStrAnio(reporteDetallado.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("resumenAjust", reporteDetallado);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		ReporteDetalladoPorTipoBean reporteDetallado = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_RES_CON_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = reporteResumenAjusteService.generaDocumento(reporteDetallado);
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
		
		
	}	
	
	//PDF pendiente por realizar
	public void descargaPDF(RequestContext ctx) throws ServletException, IOException{
		ReporteDetalladoPorTipoBean datos = (ReporteDetalladoPorTipoBean)ctx.getFlowScope().get("resumenAjust");
 		
		Connection conn = null;
 		HttpServletResponse httpServletResponse = 
 				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		 		ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
 		JasperPrint jasperPrint = null;
		JasperReport reporte = null;
		JRDataSource datasource = null;
		StringBuffer fullPathRutaArchJasper = new StringBuffer();
		fullPathRutaArchJasper.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArchJasper.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);

		if (datos.getDatSoc().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (datos.getDatSoc().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (datos.getDatSoc().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if ((datos.getListReporte().isEmpty())||datos.getListReporte().equals(null))
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDosResumen(datos.getListReporte());
			datos.setListReporte(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_RES_AJUSTES);
		jParams.put("tipoCalc", datos.getCalculotxt());//tRegionDesc 
		if(datos.isAcum()){
			jParams.put("mes", datos.getMesSeleccion());
		}else{
			jParams.put("mes", datos.getMesSeleccion());
			jParams.put("anio", "A�O: "+datos.getAnio());
			
		}
		jParams.put("clase", "CLASE: "+datos.getClaseReptxt());
		jParams.put("region", "REGION: "+datos.getRegionReptxt());
		jParams.put("texto", "TEXTO BAJA: "+datos.getTexto());
		jParams.put("ajuste", "AJUSTES: "+datos.getAjuste().getDescripcion());
		jParams.put("basicParam", datos.getListReporte());
		
		jParams.put("rutImagen", fullPath.toString());
		

		datasource = new JRBeanCollectionDataSource(datos.getListReporte(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("resumenAjustes.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
					 httpServletResponse.setContentType("application/pdf");
					 httpServletResponse.setHeader("Content-disposition", Constants.REP_RESUSMEN_AJUSTE_FILENAME + Constants.REP_PDF_EXT);
					  ServletOutputStream out = httpServletResponse.getOutputStream();
					  reporte = (JasperReport) JRLoader.loadObject(f);
				
					jasperPrint = JasperFillManager.fillReport(reporte, jParams, datasource);
					JasperExportManager.exportReportToPdfStream(jasperPrint, out);
		  
		  
		
		} catch (JRException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
		
		  FacesContext.getCurrentInstance().responseComplete();
	}
}
