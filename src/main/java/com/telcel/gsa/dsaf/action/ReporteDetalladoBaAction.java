package com.telcel.gsa.dsaf.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ReporteConcentradoService;
import com.telcel.gsa.dsaf.service.ReporteDetalladoBaService;
import com.telcel.gsa.dsaf.service.ReporteDetalladoService;
import com.telcel.gsa.dsaf.service.ResumenConceptoService;
import com.telcel.gsa.dsaf.service.TotalGlobalService;
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
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("repDetalladoBaAct")
@ViewScoped
public class ReporteDetalladoBaAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(ReporteDetalladoBaAction.class);
	
	private StreamedContent file;
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private ReporteDetalladoBaService reporteDetalladoBaService;
	@Autowired
	private UtileriasService utileriasService;

		
	public void initFlow(RequestContext ctx){
	
		ReporteDetalladoBean reporteDetallado = new ReporteDetalladoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		reporteDetallado.setAcum(false);
		reporteDetallado.setDescarga(false);
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
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
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
		try {
			List<BajasOpDocumentoBean> documentos = reporteDetalladoBaService.consultaArchivo(sessionScopeUser);
			List<BajasOpDocumentoBean> documentosf = new ArrayList<BajasOpDocumentoBean>();
			if(!documentos.isEmpty()) {
				documentosf.add(documentos.get(0));
				if(documentos.get(0).getEstatus().getId() == 3) {
					reporteDetallado.setDescarga(true);
				}
					
				
				reporteDetallado.setDocs(documentosf);
			}else {
				reporteDetallado.setDocs(new ArrayList<BajasOpDocumentoBean>());
			}
			
		} catch (CfeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void computeYearVal(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		if(reporteDetallado.getMesesselect() == null || reporteDetallado.getMesesselect().length == 0){
			reporteDetallado.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			reporteDetallado.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
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
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void computeRegDependants(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
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
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void computeClassDependants(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void updateTxt(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		reporteDetallado.setTxtsDesc(utileriasCfeService.getTxtGen(reporteDetallado.getRegion(), reporteDetallado.getCalculo(), reporteDetallado.getClase(), reporteDetallado.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
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
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	public void validaAcumulado(RequestContext ctx){
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
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
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		reporteDetallado.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(reporteDetallado.isAcum(),reporteDetallado.getMesesselect(),reporteDetallado.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+reporteDetallado.getMesesselect());//ene, feb
		logger.info("mes size: " + reporteDetallado.getMesesselect().length);
		if(reporteDetallado.getMesesselect().length > 0){
			for(int i = 0; i < reporteDetallado.getMesesselect().length; i++){
				logger.info(reporteDetallado.getMesesselect()[i]);
			}
		}
		logger.info("año"+reporteDetallado.getAnio());
		logger.info("calculo"+reporteDetallado.getCalculo().getId());//id
		logger.info("region"+reporteDetallado.getRegion());//id
		logger.info("clase"+reporteDetallado.getClase());//cve
		logger.info("textos"+reporteDetallado.getTexto());//todos
		logger.info("textoSeleccion"+reporteDetallado.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		reporteDetallado = reporteDetalladoBaService.datosWhere(reporteDetallado);

		
		
		char a = Integer.toString(reporteDetallado.getAnio()).charAt(2); 
		char aa = Integer.toString(reporteDetallado.getAnio()).charAt(3); 
		String an = a+""+aa;
		reporteDetallado.setAnioRepCorto(an);
		
		if (reporteDetallado.getAjuste() != null && reporteDetallado.getAjuste().getClave().equals("NA"))
		{							   
			ajuste.setClave(reporteDetallado.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			reporteDetallado = reporteDetalladoBaService.consultaReporteDetallado(reporteDetallado,sessionScopeUser);
		}
		if (reporteDetallado.getAjuste() != null && reporteDetallado.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(reporteDetallado.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			reporteDetallado = reporteDetalladoBaService.consultaReporteDetallado(reporteDetallado,sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			reporteDetallado = reporteDetalladoBaService.consultaAjReporteDetallado(reporteDetallado,sessionScopeUser);
		}
		if (reporteDetallado.getAjuste() != null && reporteDetallado.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(reporteDetallado.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			reporteDetallado = reporteDetalladoBaService.consultaReporteDetalladoNetos(reporteDetallado,sessionScopeUser);
		}
			
		reporteDetallado.setAjuste(ajuste);
		reporteDetallado.setStrAnio(reporteDetallado.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("repoDetalle", reporteDetallado);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_RES_CON_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = reporteDetalladoBaService.generaDocumento(reporteDetallado);
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
		
		
	}	
	
	//PDF pendiente por realizar
	public void descargaPDF(RequestContext ctx) throws ServletException, IOException{
		ReporteDetalladoBean datos = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
 		
 		HttpServletResponse httpServletResponse = 
 				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		 		@SuppressWarnings("unused")
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
		jParams.put("titulo2", Constants.TIT_REP_DETALLADO);
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
		jParams.put("AjustesParam", datos.getListReporteDetalladoAjust());
		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totAdqBaja", datos.getTotalRepDetTot().getAdq_baja());
			jParams.put("totAdqAcBaja", datos.getTotalRepDetTot().getAdq_ac_baja());
			jParams.put("totEjerBaja", datos.getTotalRepDetTot().getEjercicio_baja());
			jParams.put("totDepreTot", datos.getTotalRepDetTot().getDepr_tot());
			jParams.put("totCostoH", datos.getTotalRepDetTot().getCosto_h());
			jParams.put("totCostoAct", datos.getTotalRepDetTot().getCosto_act());
			jParams.put("totDepreAnulAct", datos.getTotalRepDetTot().getDepre_anual_act());
			jParams.put("totDepreTotal", datos.getTotalRepDetTot().getDepr_tot());
			
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		
		
		datasource = new JRBeanCollectionDataSource(datos.getListReporte(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("reporteDetallado.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			 httpServletResponse.setContentType("application/pdf");
			 httpServletResponse.setHeader("Content-disposition", Constants.REP_RES_DETALLADO_FILENAME + Constants.REP_PDF_EXT);
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
	
	
	public void descargaSFTP(RequestContext ctx) throws ServletException, IOException{
	ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		

		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		try {
			reporteDetalladoBaService.registraSolicitud(sessionScopeUser, reporteDetallado, "ReporteDetallado" 
					  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
					  + Constants.REP_EXCELU2003_EXT);

			reporteDetalladoBaService.generaArchivo(sessionScopeUser);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.BITACORA_WARNING, "SOLICITUD REGISTRADA, SE NOTIFICARÁ VIA CORREO AL TERMINAR DE GENERAR EL ARCHIVO..."));
			
		} catch (CfeException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} 
		finally {
//			bos.close();
		}
 		
	}
	
	
	
	public void consultaArchivoPorId(RequestContext ctx) throws ServletException, IOException{
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		ServletOutputStream out;
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_RES_DETALLADO_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELU2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		try {
			
			BajasOpDocumentoBean docBean = reporteDetalladoBaService.consultaArchivoId(sessionScopeUser, reporteDetallado.getDocs().get(0).getId());
			out = httpServletResponse.getOutputStream();
//			BajasOpDocumentoBean docBean = docBeanLst.get(0);
			docBean.getiContenido().write(out);
			docBean.getiContenido().close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (CfeException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}finally{
			
			logger.info("Cerrando servletOutput....");
		}
		 
		FacesContext.getCurrentInstance().responseComplete();		
	}
	
	 public static byte[] toByteArray(InputStream in) throws IOException
	    {
	        byte[] bytes = IOUtils.toByteArray(in);
	        return bytes;
	    }

	/**
	 * @return the file
	 */
	public StreamedContent getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(StreamedContent file) {
		this.file = file;
	}
	
	
	
}
