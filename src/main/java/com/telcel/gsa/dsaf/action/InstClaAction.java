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

import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.InstClaBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalClaBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.InstitucionalService;
import com.telcel.gsa.dsaf.service.TotalGlobalService;
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
@Service("instActCla")
@ViewScoped
public class InstClaAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(InstClaAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private InstitucionalService instService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		InstClaBean instCla = new InstClaBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		instCla.setAcum(false);
		instCla.setMeses(meses);
		instCla.setAnio(calendar.get(Calendar.YEAR));
		instCla.setAnios(sessionScopeUser.getAnios());
		instCla.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		instCla.setCalculo(new BajasCalculoBean());
		instCla.setClase(new ArrayList<String>());
		instCla.setClases(new ArrayList<BajasClaseBean>());
		instCla.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		instCla.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		instCla.setTextos(Arrays.asList(txts));
		instCla.setTexto("TODOS");

		instCla.setAjuste(new BajasAjustesBean());
		instCla.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		instCla.setRegDisabled(true);
		instCla.setClaseDisabled(true);
		instCla.setTxtDisabled(true);
		instCla.setQueryDisabled(true);
		instCla.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		instCla.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void computeYearVal(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		if(instCla.getMesesselect() == null || instCla.getMesesselect().length == 0){
			instCla.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		instCla.setClases(utileriasCfeService.getClasesGen(instCla.getRegion(), instCla.getCalculo(), sessionScopeUser));
		instCla.setTxtsDesc(utileriasCfeService.getTxtGen(instCla.getRegion(), instCla.getCalculo(), instCla.getClase(), instCla.getTexto(),sessionScopeUser));
		if(instCla.getCalculo().getId() != 0){
			instCla.setRegDisabled(false);
			instCla.setClaseDisabled(false);
			instCla.setTxtDisabled(false);
		}else{
			instCla.setRegDisabled(true);
			instCla.setClaseDisabled(true);
			instCla.setTxtDisabled(true);
		}
		if(instCla.getMesesselect() == null || instCla.getMesesselect().length == 0){
			instCla.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			instCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void computeRegDependants(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		instCla.setClases(utileriasCfeService.getClasesGen(instCla.getRegion(), instCla.getCalculo(), sessionScopeUser));
		instCla.setTxtsDesc(utileriasCfeService.getTxtGen(instCla.getRegion(), instCla.getCalculo(), instCla.getClase(), instCla.getTexto(),sessionScopeUser));
		if(instCla.getCalculo().getId() != 0){
			instCla.setRegDisabled(false);
			instCla.setClaseDisabled(false);
			instCla.setTxtDisabled(false);
		}else{
			instCla.setRegDisabled(true);
			instCla.setClaseDisabled(true);
			instCla.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void computeClassDependants(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		instCla.setTxtsDesc(utileriasCfeService.getTxtGen(instCla.getRegion(), instCla.getCalculo(), instCla.getClase(), instCla.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void updateTxt(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		instCla.setTxtsDesc(utileriasCfeService.getTxtGen(instCla.getRegion(), instCla.getCalculo(), instCla.getClase(), instCla.getTexto(), sessionScopeUser));
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		if(instCla.isAcum()){
			if(instCla.getMesesselect() != null && instCla.getMesesselect().length != 0){
				instCla.setMesesselect(utileriasCfeService.getAcumMonths(instCla.getMesesselect()));
			}else{
				instCla.setMesesselect(new String[]{});
			}
		}else{
			instCla.setMesesselect(new String []{});
		}
		
		if(instCla.getMesesselect() == null || instCla.getMesesselect().length == 0){
			instCla.setQueryDisabled(true);
		}else{
			instCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	public void validaAcumulado(RequestContext ctx){
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		if(instCla.isAcum()){
			instCla.setMesesselect(utileriasCfeService.getAcumMonths(instCla.getMesesselect()));
		}
		if(instCla.getMesesselect() == null || instCla.getMesesselect().length == 0){
			instCla.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			instCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("instCla", instCla);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		
		instCla.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(instCla.isAcum(),instCla.getMesesselect(),instCla.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+instCla.getMesesselect());//ene, feb
		logger.info("mes size: " + instCla.getMesesselect().length);
		if(instCla.getMesesselect().length > 0){
			for(int i = 0; i < instCla.getMesesselect().length; i++){
				logger.info(instCla.getMesesselect()[i]);
			}
		}
		logger.info("año"+instCla.getAnio());
		logger.info("calculo"+instCla.getCalculo().getId());//id
		logger.info("region"+instCla.getRegion());//id
		logger.info("clase"+instCla.getClase());//cve
		logger.info("textos"+instCla.getTexto());//todos
		logger.info("textoSeleccion"+instCla.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		instCla = instService.datosWhere(instCla);

		
		
		char a = Integer.toString(instCla.getAnio()).charAt(2); 
		char aa = Integer.toString(instCla.getAnio()).charAt(3); 
		String an = a+""+aa;
		instCla.setAnioRepCorto(an);
		
		if (instCla.getAjuste() != null && instCla.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(instCla.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			instCla = instService.consultaInstitucionalClase(instCla, sessionScopeUser);
		}
		if (instCla.getAjuste() != null && instCla.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(instCla.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			instCla = instService.consultaInstitucionalClase(instCla, sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			instCla = instService.consultaAjInstitucionalClase(instCla, sessionScopeUser);
			
		
			//gran total ajustado
			TotalBajasDosBean gTotAjustado = new TotalBajasDosBean();
			gTotAjustado.setAdq_baja((instCla.getTotReporteGeneral().getAdq_baja()).subtract(instCla.getTotReporteGeneral().getAdq_baja()) );
			gTotAjustado.setAdq_ac_baja((instCla.getTotReporteGeneral().getAdq_ac_baja()).subtract(instCla.getTotReporteGeneral().getAdq_ac_baja()));
			gTotAjustado.setEjercicio_baja((instCla.getTotReporteGeneral().getEjercicio_baja()).subtract(instCla.getTotReporteGeneral().getEjercicio_baja()));
			gTotAjustado.setDepr_tot((instCla.getTotReporteGeneral().getDepr_tot()).subtract(instCla.getTotReporteGeneral().getDepr_tot()));
			gTotAjustado.setCosto_h((instCla.getTotReporteGeneral().getCosto_h()).subtract(instCla.getTotReporteGeneral().getCosto_h()));
			gTotAjustado.setCosto_act((instCla.getTotReporteGeneral().getCosto_act()).subtract(instCla.getTotReporteGeneral().getCosto_act()));
			gTotAjustado.setDepre_anual_act((instCla.getTotReporteGeneral().getDepre_anual_act()).subtract(instCla.getTotReporteGeneral().getDepre_anual_act()));

			instCla.setTotReporteMesAjusteGran(gTotAjustado);
//			tGlobalClase.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (instCla.getAjuste() != null && instCla.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(instCla.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			instCla = instService.consultaInstitucionalClaNetos(instCla, sessionScopeUser);
		}
			
		instCla.setAjuste(ajuste);
		instCla.setStrAnio(instCla.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("instCla", instCla);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_INSTCLA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = instService.generaDocumentoClase(instCla);
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
		InstClaBean instCla = (InstClaBean)ctx.getFlowScope().get("instCla");
 		
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
		
		if (instCla.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (instCla.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (instCla.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (instCla.getListInstCla().isEmpty())
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(instCla.getListInstCla());
			instCla.setListInstCla(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_INST_CLASE);
		jParams.put("tipoCalc", instCla.getCalculotxt());//tRegionDesc 
		if(instCla.isAcum()){
			jParams.put("mes", instCla.getMesSeleccion());
		}else{
			jParams.put("mes", instCla.getMesSeleccion());
			jParams.put("anio", "AÑO: "+instCla.getAnio());
			
		}
		jParams.put("clase", "CLASE: "+instCla.getClaseReptxt());
		jParams.put("region", "REGION: "+instCla.getRegionReptxt());
		jParams.put("texto", instCla.getTextosTitulos());
		jParams.put("ajuste", "AJUSTES: "+instCla.getAjuste().getDescripcion());
		jParams.put("basicParam", instCla.getListInstCla());
		jParams.put("AjustesParam", instCla.getListTotalGlobalAjCla());
		
		
		if (instCla.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totalAdqBajas", instCla.getTotalGlobalClaTot().getAdq_baja());
			jParams.put("totalAdqAcBajas", instCla.getTotalGlobalClaTot().getAdq_ac_baja());
			jParams.put("totalEjercicio", instCla.getTotalGlobalClaTot().getEjercicio_baja());
			jParams.put("totalDepreciacion", instCla.getTotalGlobalClaTot().getDepr_tot());
			jParams.put("totalCostoH", instCla.getTotalGlobalClaTot().getCosto_h());
			jParams.put("totalCostoAct", instCla.getTotalGlobalClaTot().getCosto_act());
			jParams.put("totalDepreciacionAnual", instCla.getTotalGlobalClaTot().getDepre_anual_act());
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",instCla.getAjuste().getClave());

		
		datasource = new JRBeanCollectionDataSource(instCla.getListInstCla(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("instClase.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		StringBuffer fileName = new StringBuffer("InstCla");

		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		 httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, fileName + Constants.REP_PDF_EXT );
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_INSTCLA_FILENAME + Constants.REP_PDF_EXT);
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
