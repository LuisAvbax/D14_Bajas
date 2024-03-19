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
import com.telcel.gsa.dsaf.bean.InstRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.InstitucionalService;
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
@Service("instRegAct")
@ViewScoped
public class InstRegAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(InstRegAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private InstitucionalService instService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		InstRegBean instReg = new InstRegBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		instReg.setAcum(false);
		instReg.setMeses(meses);
		instReg.setAnio(calendar.get(Calendar.YEAR));
		instReg.setAnios(sessionScopeUser.getAnios());
		instReg.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		instReg.setCalculo(new BajasCalculoBean());
		instReg.setClase(new ArrayList<String>());
		instReg.setClases(new ArrayList<BajasClaseBean>());
		instReg.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		instReg.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		instReg.setTextos(Arrays.asList(txts));
		instReg.setTexto("TODOS");

		instReg.setAjuste(new BajasAjustesBean());
		instReg.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		instReg.setRegDisabled(true);
		instReg.setClaseDisabled(true);
		instReg.setTxtDisabled(true);
		instReg.setQueryDisabled(true);
		instReg.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		instReg.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void computeYearVal(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		if(instReg.getMesesselect() == null || instReg.getMesesselect().length == 0){
			instReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		instReg.setClases(utileriasCfeService.getClasesGen(instReg.getRegion(), instReg.getCalculo(),sessionScopeUser));
		instReg.setTxtsDesc(utileriasCfeService.getTxtGen(instReg.getRegion(), instReg.getCalculo(), instReg.getClase(), instReg.getTexto(),sessionScopeUser));
		if(instReg.getCalculo().getId() != 0){
			instReg.setRegDisabled(false);
			instReg.setClaseDisabled(false);
			instReg.setTxtDisabled(false);
		}else{
			instReg.setRegDisabled(true);
			instReg.setClaseDisabled(true);
			instReg.setTxtDisabled(true);
		}
		if(instReg.getMesesselect() == null || instReg.getMesesselect().length == 0){
			instReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void computeRegDependants(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		instReg.setClases(utileriasCfeService.getClasesGen(instReg.getRegion(), instReg.getCalculo(),sessionScopeUser));
		instReg.setTxtsDesc(utileriasCfeService.getTxtGen(instReg.getRegion(), instReg.getCalculo(), instReg.getClase(), instReg.getTexto(),sessionScopeUser));
		if(instReg.getCalculo().getId() != 0){
			instReg.setRegDisabled(false);
			instReg.setClaseDisabled(false);
			instReg.setTxtDisabled(false);
		}else{
			instReg.setRegDisabled(true);
			instReg.setClaseDisabled(true);
			instReg.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void computeClassDependants(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		instReg.setTxtsDesc(utileriasCfeService.getTxtGen(instReg.getRegion(), instReg.getCalculo(), instReg.getClase(), instReg.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void updateTxt(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		instReg.setTxtsDesc(utileriasCfeService.getTxtGen(instReg.getRegion(), instReg.getCalculo(), instReg.getClase(), instReg.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		if(instReg.isAcum()){
			if(instReg.getMesesselect() != null && instReg.getMesesselect().length != 0){
				instReg.setMesesselect(utileriasCfeService.getAcumMonths(instReg.getMesesselect()));
			}else{
				instReg.setMesesselect(new String[]{});
			}
		}else{
			instReg.setMesesselect(new String []{});
		}
		
		if(instReg.getMesesselect() == null || instReg.getMesesselect().length == 0){
			instReg.setQueryDisabled(true);
		}else{
			instReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	public void validaAcumulado(RequestContext ctx){
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		if(instReg.isAcum()){
			instReg.setMesesselect(utileriasCfeService.getAcumMonths(instReg.getMesesselect()));
		}
		if(instReg.getMesesselect() == null || instReg.getMesesselect().length == 0){
			instReg.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			instReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instReg", instReg);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		instReg.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(instReg.isAcum(),instReg.getMesesselect(),instReg.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+instReg.getMesesselect());//ene, feb
		logger.info("mes size: " + instReg.getMesesselect().length);
		if(instReg.getMesesselect().length > 0){
			for(int i = 0; i < instReg.getMesesselect().length; i++){
				logger.info(instReg.getMesesselect()[i]);
			}
		}
		logger.info("año"+instReg.getAnio());
		logger.info("calculo"+instReg.getCalculo().getId());//id
		logger.info("region"+instReg.getRegion());//id
		logger.info("clase"+instReg.getClase());//cve
		logger.info("textos"+instReg.getTexto());//todos
		logger.info("textoSeleccion"+instReg.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		instReg = instService.datosWhere(instReg);

		
		
		char a = Integer.toString(instReg.getAnio()).charAt(2); 
		char aa = Integer.toString(instReg.getAnio()).charAt(3); 
		String an = a+""+aa;
		instReg.setAnioRepCorto(an);
		
		if (instReg.getAjuste() != null && instReg.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(instReg.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			instReg = instService.consultaInstitucionalRegion(instReg, sessionScopeUser);
		}
		if (instReg.getAjuste() != null && instReg.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(instReg.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			instReg = instService.consultaInstitucionalRegion(instReg, sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			instReg = instService.consultaAjInstitucionalRegion(instReg, sessionScopeUser);
		}
		if (instReg.getAjuste() != null && instReg.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(instReg.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			instReg = instService.consultaInstitucionalRegNetos(instReg, sessionScopeUser);
		}
			
		instReg.setAjuste(ajuste);
		instReg.setStrAnio(instReg.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("instReg", instReg);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_INSTREG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = instService.generaDocumento(instReg);
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
		InstRegBean instReg = (InstRegBean)ctx.getFlowScope().get("instReg");
		Connection conn = null;
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
		
		if (instReg.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instReg.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instReg.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (instReg.getListInstReg().isEmpty())
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(instReg.getListInstReg());
			instReg.setListInstReg(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_REGION);
		jParams.put("tipoCalc", instReg.getCalculotxt());//tRegionDesc 
		if(instReg.isAcum()){
			jParams.put("mes", instReg.getMesSeleccion());
		}else{
			jParams.put("mes", instReg.getMesSeleccion());
			jParams.put("anio", "AÑO: "+instReg.getAnio());
			
		}
		jParams.put("clase", "CLASE: "+instReg.getClaseReptxt());
		jParams.put("region", "REGION: "+instReg.getRegionReptxt());
		jParams.put("texto", instReg.getTextosTitulos());
		jParams.put("ajuste", "AJUSTES: "+instReg.getAjuste().getDescripcion());
		jParams.put("basicParam", instReg.getListInstReg());
		jParams.put("AjustesParam", instReg.getListTotalGlobalAjReg());
		
		
		
		if (instReg.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totalAdqBajas", instReg.getTotalGlobalRegTot().getAdq_baja());
			jParams.put("totalAdqAcBajas", instReg.getTotalGlobalRegTot().getAdq_ac_baja());
			jParams.put("totalEjercicio", instReg.getTotalGlobalRegTot().getEjercicio_baja());
			jParams.put("totalDepreciacion", instReg.getTotalGlobalRegTot().getDepr_tot());
			jParams.put("totalCostoH", instReg.getTotalGlobalRegTot().getCosto_h());
			jParams.put("totalCostoAct", instReg.getTotalGlobalRegTot().getCosto_act());
			jParams.put("totalDepreciacionAnual", instReg.getTotalGlobalRegTot().getDepre_anual_act());
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",instReg.getAjuste().getClave());
		
		
		datasource = new JRBeanCollectionDataSource(instReg.getListInstReg(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("instRegion.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstReg");
		

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTREG_FILENAME + Constants.REP_PDF_EXT);
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
