package com.telcel.gsa.dsaf.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasTresBean;
import com.telcel.gsa.dsaf.bean.BajasTresBeanResumen;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.InstAjusteBean;
import com.telcel.gsa.dsaf.bean.TGClaseTresBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.InstitucionalAjusteService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.service.UtileriasService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
@Service("instActAju")
@ViewScoped
public class InstAjusteAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(InstAjusteAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private InstitucionalAjusteService instAjusteService;
	

		
	public void initFlow(RequestContext ctx){
	
		InstAjusteBean instAj = new InstAjusteBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		instAj.setAcum(false);
		instAj.setMeses(meses);
		instAj.setAnio(calendar.get(Calendar.YEAR));
		instAj.setAnios(sessionScopeUser.getAnios());
		instAj.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		instAj.setCalculo(new BajasCalculoBean());
		instAj.setClase(new ArrayList<String>());
		instAj.setClases(new ArrayList<BajasClaseBean>());
		instAj.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		instAj.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		instAj.setTextos(Arrays.asList(txts));
		instAj.setTexto("TODOS");
		instAj.setRegDisabled(true);
		instAj.setClaseDisabled(true);
		instAj.setTxtDisabled(true);
		instAj.setQueryDisabled(true);
		instAj.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		instAj.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void computeYearVal(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		if(instAj.getMesesselect() == null || instAj.getMesesselect().length == 0){
			instAj.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instAj.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		instAj.setClases(utileriasCfeService.getClasesGen(instAj.getRegion(), instAj.getCalculo(),sessionScopeUser));
		instAj.setTxtsDesc(utileriasCfeService.getTxtGen(instAj.getRegion(), instAj.getCalculo(), instAj.getClase(), instAj.getTexto(),sessionScopeUser));
		if(instAj.getCalculo().getId() != 0){
			instAj.setRegDisabled(false);
			instAj.setClaseDisabled(false);
			instAj.setTxtDisabled(false);
		}else{
			instAj.setRegDisabled(true);
			instAj.setClaseDisabled(true);
			instAj.setTxtDisabled(true);
		}
		if(instAj.getMesesselect() == null || instAj.getMesesselect().length == 0){
			instAj.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instAj.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void computeRegDependants(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		instAj.setClases(utileriasCfeService.getClasesGen(instAj.getRegion(), instAj.getCalculo(),sessionScopeUser));
		instAj.setTxtsDesc(utileriasCfeService.getTxtGen(instAj.getRegion(), instAj.getCalculo(), instAj.getClase(), instAj.getTexto(),sessionScopeUser));
		if(instAj.getCalculo().getId() != 0){
			instAj.setRegDisabled(false);
			instAj.setClaseDisabled(false);
			instAj.setTxtDisabled(false);
		}else{
			instAj.setRegDisabled(true);
			instAj.setClaseDisabled(true);
			instAj.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void computeClassDependants(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		instAj.setTxtsDesc(utileriasCfeService.getTxtGen(instAj.getRegion(), instAj.getCalculo(), instAj.getClase(), instAj.getTexto(), sessionScopeUser));
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void updateTxt(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		instAj.setTxtsDesc(utileriasCfeService.getTxtGen(instAj.getRegion(), instAj.getCalculo(), instAj.getClase(), instAj.getTexto(), sessionScopeUser));
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		if(instAj.isAcum()){
			if(instAj.getMesesselect() != null && instAj.getMesesselect().length != 0){
				instAj.setMesesselect(utileriasCfeService.getAcumMonths(instAj.getMesesselect()));
			}else{
				instAj.setMesesselect(new String[]{});
			}
		}else{
			instAj.setMesesselect(new String []{});
		}
		
		if(instAj.getMesesselect() == null || instAj.getMesesselect().length == 0){
			instAj.setQueryDisabled(true);
		}else{
			instAj.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	public void validaAcumulado(RequestContext ctx){
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		if(instAj.isAcum()){
			instAj.setMesesselect(utileriasCfeService.getAcumMonths(instAj.getMesesselect()));
		}
		if(instAj.getMesesselect() == null || instAj.getMesesselect().length == 0){
			instAj.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			instAj.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instAj", instAj);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		
		instAj.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(instAj.isAcum(),instAj.getMesesselect(),instAj.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+instAj.getMesesselect());//ene, feb
		logger.info("mes size: " + instAj.getMesesselect().length);
		if(instAj.getMesesselect().length > 0){
			for(int i = 0; i < instAj.getMesesselect().length; i++){
				logger.info(instAj.getMesesselect()[i]);
			}
		}
		logger.info("año"+instAj.getAnio());
		logger.info("calculo"+instAj.getCalculo().getId());//id
		logger.info("region"+instAj.getRegion());//id
		logger.info("clase"+instAj.getClase());//cve
		logger.info("textos"+instAj.getTexto());//todos
		logger.info("textoSeleccion"+instAj.getTxtDesc());
		
		instAj = instAjusteService.datosWhere(instAj);

		
		
		char a = Integer.toString(instAj.getAnio()).charAt(2); 
		char aa = Integer.toString(instAj.getAnio()).charAt(3); 
		String an = a+""+aa;
		instAj.setAnioRepCorto(an);
		
		instAj = instAjusteService.consultaInstitucionalClase(instAj, sessionScopeUser);
		System.out.println("=================================================");
		for(TGClaseTresBean clase: instAj.getDetClases()){
			System.out.println("CLASE: " + clase.getNombreCorto() + "|" + clase.getNombre());
			for(BajasTresBean texto: clase.getTextos())
			{		
				System.out.println(texto.getTexto_baja() + " | " + texto.getAdq_baja_r0() + " | " + texto.getAdq_baja_r1() + " | " + texto.getAdq_baja_r2() + " | " + texto.getAdq_baja_r3() + " | " +texto.getAdq_baja_r4()+ " | " +texto.getAdq_baja_r5()+ " | " +
			texto.getAdq_baja_r6()+ " | " + texto.getAdq_baja_r7()+ " | " + texto.getAdq_baja_r8()+ " | " + texto.getAdq_baja_r9() + " | " + texto.getAdq_baja_total());
			}
		}
		
		System.out.println();
		System.out.println("=================================================");
//		System.out.println("=================================================");
//		for(TGClaseTresBean clase: instAj.getDetClases()){
//			System.out.println("CLASE: " + clase.getNombreCorto() + "|" + clase.getNombre());
//			for(BajasTresBean texto: clase.getTextos())
//			{		
//				System.out.println(texto.getTexto_baja() + " | " + texto.getCosto_act_r0() + " | " + texto.getCosto_act_r1() + " | " + texto.getCosto_act_r2() + " | " + texto.getCosto_act_r3() + " | " +texto.getCosto_act_r4()+ " | " +texto.getCosto_act_r5()+ " | " +
//			texto.getCosto_act_r6()+ " | " + texto.getCosto_act_r7()+ " | " + texto.getCosto_act_r8()+ " | " + texto.getCosto_act_r9() + " | " + texto.getCosto_act_total());
//			}
//		}
//		
//		System.out.println();
//		System.out.println("=================================================");
//		System.out.println("=================================================");
//		for(TGClaseTresBean clase: instAj.getDetClases()){
//			System.out.println("CLASE: " + clase.getNombreCorto() + "|" + clase.getNombre());
//			for(BajasTresBean texto: clase.getTextos())
//			{		
//				System.out.println(texto.getTexto_baja() + " | " + texto.getDepre_anual_act_r0() + " | " + texto.getDepre_anual_act_r1() + " | " + texto.getDepre_anual_act_r2() + " | " + texto.getDepre_anual_act_r3() + " | " +texto.getDepre_anual_act_r4()+ " | " +texto.getDepre_anual_act_r5()+ " | " +
//			texto.getDepre_anual_act_r6()+ " | " + texto.getDepre_anual_act_r7()+ " | " + texto.getDepre_anual_act_r8()+ " | " + texto.getDepre_anual_act_r9() + " | " + texto.getDepre_anual_act_total());
//			}
//		}
//		
//		System.out.println();
//		System.out.println("=================================================");
		//AGREGAR CONSULTA AJUSTES
		instAj = instAjusteService.consultaAjInstitucionalClase(instAj, sessionScopeUser);

		ctx.getFlowScope().put("instAj", instAj);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_INSTAJUSTE_FILENAMEXLS 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = instAjusteService.generaDocumentoClase(instAj);
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
		
		
	}	
	
	//PDF
	public void descargaPDF(RequestContext ctx) throws ServletException, IOException{
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
 		
 		HttpServletResponse httpServletResponse = 
		(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		JasperPrint jasperPrint = null;
		JasperReport reporte = null;
		JRDataSource datasource = null;

		StringBuffer fullPathRutaArchJasper = new StringBuffer();
		fullPathRutaArchJasper.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArchJasper.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		
		if (instAj.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instAj.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instAj.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		
		if (instAj.getLsttextos().isEmpty())
		{
			List<BajasTresBean> dtList= new ArrayList<BajasTresBean>();
			dtList = utileriasService.listBajasTres(instAj.getLsttextos());
			instAj.setLsttextos(dtList);
		}
		if (instAj.getLsttextosResumen().isEmpty())
		{
			List<BajasTresBeanResumen> dtList= new ArrayList<BajasTresBeanResumen>();
			dtList = utileriasService.listBajasTresRes(instAj.getLsttextosResumen());
			instAj.setLsttextosResumen(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_AJUSTE);
		jParams.put("tipoCalc", instAj.getCalculotxt());//tRegionDesc 
		if(instAj.isAcum()){
			jParams.put("mes", instAj.getMesSeleccion());
		}else{
			jParams.put("mes", instAj.getMesSeleccion() + " " + instAj.getAnio());	
		}
		jParams.put("clase", "CLASE: "+instAj.getClaseReptxt());
		jParams.put("region", "REGION: "+instAj.getRegionReptxt());
		jParams.put("texto", "TEXTO BAJA: "+instAj.getTexto());
		jParams.put("leyendaAjuste", "RESUMEN POR REGION");
		jParams.put("basicParam", instAj.getLsttextos());
		jParams.put("AjustesParam", instAj.getLsttextosResumen());
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		
		
		jParams.put("regionPos3","CHIHUAHUA");
		jParams.put("regionPos4","MONTERREY");
		jParams.put("regionPos5","GUADALAJARA");

		datasource = new JRBeanCollectionDataSource(instAj.getLsttextos(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("institucionalAjuste.jasper");//nombre del archivo
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstCla");

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTAJUSTE_FILENAME + Constants.REP_PDF_EXT);
		  ServletOutputStream out = httpServletResponse.getOutputStream();
		  reporte = (JasperReport) JRLoader.loadObject(f);
			
			jasperPrint = JasperFillManager.fillReport(reporte, jParams, datasource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		} catch (JRException e) {
			logger.error(e.getMessage(),e);
		}
		
		  FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void descargaPDFCorpo(RequestContext ctx) throws ServletException, IOException{
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
 		
 		HttpServletResponse httpServletResponse = 
		(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		JasperPrint jasperPrint = null;
		JasperReport reporte = null;
		JRDataSource datasource = null;

		StringBuffer fullPathRutaArchJasper = new StringBuffer();
		fullPathRutaArchJasper.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArchJasper.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		
		if (instAj.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instAj.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instAj.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (instAj.getLsttextos().isEmpty())
		{
			List<BajasTresBean> dtList= new ArrayList<BajasTresBean>();
			dtList = utileriasService.listBajasTres(instAj.getLsttextos());
			instAj.setLsttextos(dtList);
		}
		if (instAj.getLsttextosResumen().isEmpty())
		{
			List<BajasTresBeanResumen> dtList= new ArrayList<BajasTresBeanResumen>();
			dtList = utileriasService.listBajasTresRes(instAj.getLsttextosResumen());
			instAj.setLsttextosResumen(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_AJUSTE);
		jParams.put("tipoCalc", instAj.getCalculotxt());//tRegionDesc 
		if(instAj.isAcum()){
			jParams.put("mes", instAj.getMesSeleccion());
		}else{
			jParams.put("mes", instAj.getMesSeleccion() + " " + instAj.getAnio());	
		}
		jParams.put("clase", "CLASE: "+instAj.getClaseReptxt());
		jParams.put("region", "REGION: "+instAj.getRegionReptxt());
		jParams.put("texto", "TEXTO BAJA: "+instAj.getTexto());
		jParams.put("leyendaAjuste", "RESUMEN POR REGION");
		jParams.put("basicParam", instAj.getLsttextos());
		jParams.put("AjustesParam", instAj.getLsttextosResumen());
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("regionPos0","CORPORATIVO");
		jParams.put("regionPos1","TIJUANA");
		jParams.put("regionPos2","HERMOSILLO");
		

		datasource = new JRBeanCollectionDataSource(instAj.getLsttextos(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("institucionalAjusteCorpo.jasper");//nombre del archivo
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstCla");

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTAJUSTE_FILENAMER0 + Constants.REP_PDF_EXT);
		  ServletOutputStream out = httpServletResponse.getOutputStream();
		  reporte = (JasperReport) JRLoader.loadObject(f);
			
			jasperPrint = JasperFillManager.fillReport(reporte, jParams, datasource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		} catch (JRException e) {
			logger.error(e.getMessage(),e);
		}
		
		  FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	public void descargaPDF4a6(RequestContext ctx) throws ServletException, IOException{
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
 		
 		HttpServletResponse httpServletResponse = 
		(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		JasperPrint jasperPrint = null;
		JasperReport reporte = null;
		JRDataSource datasource = null;

		StringBuffer fullPathRutaArchJasper = new StringBuffer();
		fullPathRutaArchJasper.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArchJasper.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		
		if (instAj.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instAj.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instAj.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (instAj.getLsttextos().isEmpty())
		{
			List<BajasTresBean> dtList= new ArrayList<BajasTresBean>();
			dtList = utileriasService.listBajasTres(instAj.getLsttextos());
			instAj.setLsttextos(dtList);
		}
		if (instAj.getLsttextosResumen().isEmpty())
		{
			List<BajasTresBeanResumen> dtList= new ArrayList<BajasTresBeanResumen>();
			dtList = utileriasService.listBajasTresRes(instAj.getLsttextosResumen());
			instAj.setLsttextosResumen(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_AJUSTE);
		jParams.put("tipoCalc", instAj.getCalculotxt());//tRegionDesc 
		if(instAj.isAcum()){
			jParams.put("mes", instAj.getMesSeleccion());
		}else{
			jParams.put("mes", instAj.getMesSeleccion() + " " + instAj.getAnio());	
		}
		jParams.put("clase", "CLASE: "+instAj.getClaseReptxt());
		jParams.put("region", "REGION: "+instAj.getRegionReptxt());
		jParams.put("texto", "TEXTO BAJA: "+instAj.getTexto());
		jParams.put("leyendaAjuste", "RESUMEN POR REGION");
		jParams.put("basicParam", instAj.getLsttextos());
		jParams.put("AjustesParam", instAj.getLsttextosResumen());
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		
		jParams.put("regionPos6","QUERETARO");
		jParams.put("regionPos7","PUEBLA");
		jParams.put("regionPos8","MERIDA");

		datasource = new JRBeanCollectionDataSource(instAj.getLsttextos(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("institucionalAjusteR4a6.jasper");//nombre del archivo
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstCla");

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTAJUSTE_FILENAMER4a6 + Constants.REP_PDF_EXT);
		  ServletOutputStream out = httpServletResponse.getOutputStream();
		  reporte = (JasperReport) JRLoader.loadObject(f);
			
			jasperPrint = JasperFillManager.fillReport(reporte, jParams, datasource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		} catch (JRException e) {
			logger.error(e.getMessage(),e);
		}
		
		  FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	public void descargaPDF7a9(RequestContext ctx) throws ServletException, IOException{
		InstAjusteBean instAj = (InstAjusteBean)ctx.getFlowScope().get("instAj");
 		
 		HttpServletResponse httpServletResponse = 
		(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 		JasperPrint jasperPrint = null;
		JasperReport reporte = null;
		JRDataSource datasource = null;

		StringBuffer fullPathRutaArchJasper = new StringBuffer();
		fullPathRutaArchJasper.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArchJasper.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		
		if (instAj.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instAj.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instAj.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (instAj.getLsttextos().isEmpty())
		{
			List<BajasTresBean> dtList= new ArrayList<BajasTresBean>();
			dtList = utileriasService.listBajasTres(instAj.getLsttextos());
			instAj.setLsttextos(dtList);
		}
		if (instAj.getLsttextosResumen().isEmpty())
		{
			List<BajasTresBeanResumen> dtList= new ArrayList<BajasTresBeanResumen>();
			dtList = utileriasService.listBajasTresRes(instAj.getLsttextosResumen());
			instAj.setLsttextosResumen(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_AJUSTE);
		jParams.put("tipoCalc", instAj.getCalculotxt());//tRegionDesc 
		if(instAj.isAcum()){
			jParams.put("mes", instAj.getMesSeleccion());
		}else{
			jParams.put("mes", instAj.getMesSeleccion() + " " + instAj.getAnio());	
		}
		jParams.put("clase", "CLASE: "+instAj.getClaseReptxt());
		jParams.put("region", "REGION: "+instAj.getRegionReptxt());
		jParams.put("texto", "TEXTO BAJA: "+instAj.getTexto());
		jParams.put("leyendaAjuste", "RESUMEN POR REGION");
		jParams.put("basicParam", instAj.getLsttextos());
		jParams.put("AjustesParam", instAj.getLsttextosResumen());
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		
		jParams.put("regionPos9","METROPOLITANO");
		jParams.put("regionTotal","TOTAL");
		

		datasource = new JRBeanCollectionDataSource(instAj.getLsttextos(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("institucionalAjusteR7a9.jasper");//nombre del archivo
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstCla");

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTAJUSTE_FILENAMER7a9 + Constants.REP_PDF_EXT);
		  ServletOutputStream out = httpServletResponse.getOutputStream();
		  reporte = (JasperReport) JRLoader.loadObject(f);
			
			jasperPrint = JasperFillManager.fillReport(reporte, jParams, datasource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		} catch (JRException e) {
			logger.error(e.getMessage(),e);
		}
		
		  FacesContext.getCurrentInstance().responseComplete();
	}
}
