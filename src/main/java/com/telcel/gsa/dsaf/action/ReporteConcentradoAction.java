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
import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ReporteConcentradoService;
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
@Service("repConcentradoAct")
@ViewScoped
public class ReporteConcentradoAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(ReporteConcentradoAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private ReporteConcentradoService reporteConcentradoService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		ReporteConcentradoBean reporteConcentrado = new ReporteConcentradoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		reporteConcentrado.setAcum(false);
		reporteConcentrado.setMeses(meses);
		reporteConcentrado.setAnio(calendar.get(Calendar.YEAR));
		reporteConcentrado.setAnios(sessionScopeUser.getAnios());
		reporteConcentrado.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		reporteConcentrado.setCalculo(new BajasCalculoBean());
		reporteConcentrado.setClase(new ArrayList<String>());
		reporteConcentrado.setClases(new ArrayList<BajasClaseBean>());
		reporteConcentrado.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		reporteConcentrado.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		reporteConcentrado.setTextos(Arrays.asList(txts));
		reporteConcentrado.setTexto("TODOS");

		reporteConcentrado.setAjuste(new BajasAjustesBean());
		reporteConcentrado.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		reporteConcentrado.setRegDisabled(true);
		reporteConcentrado.setClaseDisabled(true);
		reporteConcentrado.setTxtDisabled(true);
		reporteConcentrado.setQueryDisabled(true);
		reporteConcentrado.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void computeYearVal(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		if(reporteConcentrado.getMesesselect() == null || reporteConcentrado.getMesesselect().length == 0){
			reporteConcentrado.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			reporteConcentrado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		reporteConcentrado.setClases(utileriasCfeService.getClasesGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(),sessionScopeUser));
		reporteConcentrado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(), reporteConcentrado.getClase(), reporteConcentrado.getTexto(),sessionScopeUser));
		if(reporteConcentrado.getCalculo().getId() != 0){
			reporteConcentrado.setRegDisabled(false);
			reporteConcentrado.setClaseDisabled(false);
			reporteConcentrado.setTxtDisabled(false);
		}else{
			reporteConcentrado.setRegDisabled(true);
			reporteConcentrado.setClaseDisabled(true);
			reporteConcentrado.setTxtDisabled(true);
		}
		if(reporteConcentrado.getMesesselect() == null || reporteConcentrado.getMesesselect().length == 0){
			reporteConcentrado.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			reporteConcentrado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void computeRegDependants(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		reporteConcentrado.setClases(utileriasCfeService.getClasesGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(),sessionScopeUser));
		reporteConcentrado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(), reporteConcentrado.getClase(), reporteConcentrado.getTexto(), sessionScopeUser));
		if(reporteConcentrado.getCalculo().getId() != 0){
			reporteConcentrado.setRegDisabled(false);
			reporteConcentrado.setClaseDisabled(false);
			reporteConcentrado.setTxtDisabled(false);
		}else{
			reporteConcentrado.setRegDisabled(true);
			reporteConcentrado.setClaseDisabled(true);
			reporteConcentrado.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void computeClassDependants(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		reporteConcentrado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(), reporteConcentrado.getClase(), reporteConcentrado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void updateTxt(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		reporteConcentrado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteConcentrado.getRegion(), reporteConcentrado.getCalculo(), reporteConcentrado.getClase(), reporteConcentrado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		if(reporteConcentrado.isAcum()){
			if(reporteConcentrado.getMesesselect() != null && reporteConcentrado.getMesesselect().length != 0){
				reporteConcentrado.setMesesselect(utileriasCfeService.getAcumMonths(reporteConcentrado.getMesesselect()));
			}else{
				reporteConcentrado.setMesesselect(new String[]{});
			}
		}else{
			reporteConcentrado.setMesesselect(new String []{});
		}
		
		if(reporteConcentrado.getMesesselect() == null || reporteConcentrado.getMesesselect().length == 0){
			reporteConcentrado.setQueryDisabled(true);
		}else{
			reporteConcentrado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	public void validaAcumulado(RequestContext ctx){
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		if(reporteConcentrado.isAcum()){
			reporteConcentrado.setMesesselect(utileriasCfeService.getAcumMonths(reporteConcentrado.getMesesselect()));
		}
		if(reporteConcentrado.getMesesselect() == null || reporteConcentrado.getMesesselect().length == 0){
			reporteConcentrado.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			reporteConcentrado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		reporteConcentrado.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(reporteConcentrado.isAcum(),reporteConcentrado.getMesesselect(),reporteConcentrado.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+reporteConcentrado.getMesesselect());//ene, feb
		logger.info("mes size: " + reporteConcentrado.getMesesselect().length);
		if(reporteConcentrado.getMesesselect().length > 0){
			for(int i = 0; i < reporteConcentrado.getMesesselect().length; i++){
				logger.info(reporteConcentrado.getMesesselect()[i]);
			}
		}
		logger.info("año"+reporteConcentrado.getAnio());
		logger.info("calculo"+reporteConcentrado.getCalculo().getId());//id
		logger.info("region"+reporteConcentrado.getRegion());//id
		logger.info("clase"+reporteConcentrado.getClase());//cve
		logger.info("textos"+reporteConcentrado.getTexto());//todos
		logger.info("textoSeleccion"+reporteConcentrado.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		reporteConcentrado = reporteConcentradoService.datosWhere(reporteConcentrado);

		
		
		char a = Integer.toString(reporteConcentrado.getAnio()).charAt(2); 
		char aa = Integer.toString(reporteConcentrado.getAnio()).charAt(3); 
		String an = a+""+aa;
		reporteConcentrado.setAnioRepCorto(an);
		
		if (reporteConcentrado.getAjuste() != null && reporteConcentrado.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(reporteConcentrado.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			reporteConcentrado = reporteConcentradoService.consultaReporteConcentrado(reporteConcentrado,sessionScopeUser);
		}
		if (reporteConcentrado.getAjuste() != null && reporteConcentrado.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(reporteConcentrado.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			reporteConcentrado = reporteConcentradoService.consultaReporteConcentrado(reporteConcentrado,sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			reporteConcentrado = reporteConcentradoService.consultaAjReporteConcentrado(reporteConcentrado,sessionScopeUser);
		}
		if (reporteConcentrado.getAjuste() != null && reporteConcentrado.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(reporteConcentrado.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			reporteConcentrado = reporteConcentradoService.consultaReporteConcentradoNetos(reporteConcentrado,sessionScopeUser);
		}
			
		reporteConcentrado.setAjuste(ajuste);
		reporteConcentrado.setStrAnio(reporteConcentrado.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("resuConcentrado", reporteConcentrado);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		ReporteConcentradoBean reporteConcentrado = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_RES_CON_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = reporteConcentradoService.generaDocumento(reporteConcentrado);
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
		
		
	}	
	
	//PDF pendiente por realizar
	@SuppressWarnings("deprecation")
	public void descargaPDF(RequestContext ctx) throws ServletException, IOException{
		ReporteConcentradoBean datos = (ReporteConcentradoBean)ctx.getFlowScope().get("resuConcentrado");
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
		if ((datos.getListReporte().isEmpty())||(datos.getListReporte().equals(null)))
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(datos.getListReporte());
			datos.setListReporte(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_CONCENTRADO);
		jParams.put("tipoCalc", datos.getCalculotxt());//tRegionDesc 
		if(datos.isAcum()){
			jParams.put("mes", datos.getMesSeleccion());
		}else{
			jParams.put("mes", datos.getMesSeleccion());
			jParams.put("anio", "AÑO: "+datos.getAnio());
			
		}
		jParams.put("clase", "CLASE: "+datos.getClaseReptxt());
		jParams.put("region", "REGION: "+datos.getRegionReptxt());
		jParams.put("texto", datos.getTextosTitulos());
		jParams.put("ajuste", "AJUSTES: "+datos.getAjuste().getDescripcion());
		jParams.put("basicParam", datos.getListReporte());
		jParams.put("AjustesParam", datos.getListTotalGlobalAjReg());
		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totAdqBaja", datos.getTotalGlobalRegTot().getAdq_baja());
			jParams.put("totAdqAcBaja", datos.getTotalGlobalRegTot().getAdq_ac_baja());
			jParams.put("totEjerBaja", datos.getTotalGlobalRegTot().getEjercicio_baja());
			jParams.put("totDepreTot", datos.getTotalGlobalRegTot().getDepr_tot());
			jParams.put("totCostoH", datos.getTotalGlobalRegTot().getCosto_h());
			jParams.put("totCostoAct", datos.getTotalGlobalRegTot().getCosto_act());
			jParams.put("totDepreAnulAct", datos.getTotalGlobalRegTot().getDepre_anual_act());
			jParams.put("totDepreTotal", datos.getTotalGlobalRegTot().getDepr_tot());
			
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		
		
		datasource = new JRBeanCollectionDataSource(datos.getListReporte(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("reporteConcentrado.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			 httpServletResponse.setContentType("application/pdf");
			 httpServletResponse.setHeader("Content-disposition", Constants.REP_RES_CONCENTRADO_FILENAME + Constants.REP_PDF_EXT);
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
