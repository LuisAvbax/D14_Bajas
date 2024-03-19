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
import com.telcel.gsa.dsaf.bean.ResumenConceptoClasBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ResumenConceptoService;
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
@Service("resConpClaseAct")
@ViewScoped
public class ResumenConceptoClaseAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(ResumenConceptoClaseAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private ResumenConceptoService resumenConceptoService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		ResumenConceptoClasBean resumenConcepto = new ResumenConceptoClasBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		resumenConcepto.setAcum(false);
		resumenConcepto.setMeses(meses);
		resumenConcepto.setAnio(calendar.get(Calendar.YEAR));
		resumenConcepto.setAnios(sessionScopeUser.getAnios());
		resumenConcepto.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		resumenConcepto.setCalculo(new BajasCalculoBean());
		resumenConcepto.setClase(new ArrayList<String>());
		resumenConcepto.setClases(new ArrayList<BajasClaseBean>());
		resumenConcepto.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		resumenConcepto.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		resumenConcepto.setTextos(Arrays.asList(txts));
		resumenConcepto.setTexto("TODOS");

		resumenConcepto.setAjuste(new BajasAjustesBean());
		resumenConcepto.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		resumenConcepto.setRegDisabled(true);
		resumenConcepto.setClaseDisabled(true);
		resumenConcepto.setTxtDisabled(true);
		resumenConcepto.setQueryDisabled(true);
		resumenConcepto.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void computeYearVal(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		if(resumenConcepto.getMesesselect() == null || resumenConcepto.getMesesselect().length == 0){
			resumenConcepto.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			resumenConcepto.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		resumenConcepto.setClases(utileriasCfeService.getClasesGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(),sessionScopeUser));
		resumenConcepto.setTxtsDesc(utileriasCfeService.getTxtGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(), resumenConcepto.getClase(), resumenConcepto.getTexto(),sessionScopeUser));
		if(resumenConcepto.getCalculo().getId() != 0){
			resumenConcepto.setRegDisabled(false);
			resumenConcepto.setClaseDisabled(false);
			resumenConcepto.setTxtDisabled(false);
		}else{
			resumenConcepto.setRegDisabled(true);
			resumenConcepto.setClaseDisabled(true);
			resumenConcepto.setTxtDisabled(true);
		}
		if(resumenConcepto.getMesesselect() == null || resumenConcepto.getMesesselect().length == 0){
			resumenConcepto.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			resumenConcepto.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void computeRegDependants(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		resumenConcepto.setClases(utileriasCfeService.getClasesGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(),sessionScopeUser));
		resumenConcepto.setTxtsDesc(utileriasCfeService.getTxtGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(), resumenConcepto.getClase(), resumenConcepto.getTexto(),sessionScopeUser));
		if(resumenConcepto.getCalculo().getId() != 0){
			resumenConcepto.setRegDisabled(false);
			resumenConcepto.setClaseDisabled(false);
			resumenConcepto.setTxtDisabled(false);
		}else{
			resumenConcepto.setRegDisabled(true);
			resumenConcepto.setClaseDisabled(true);
			resumenConcepto.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void computeClassDependants(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		resumenConcepto.setTxtsDesc(utileriasCfeService.getTxtGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(), resumenConcepto.getClase(), resumenConcepto.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void updateTxt(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		resumenConcepto.setTxtsDesc(utileriasCfeService.getTxtGen(resumenConcepto.getRegion(), resumenConcepto.getCalculo(), resumenConcepto.getClase(), resumenConcepto.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		if(resumenConcepto.isAcum()){
			if(resumenConcepto.getMesesselect() != null && resumenConcepto.getMesesselect().length != 0){
				resumenConcepto.setMesesselect(utileriasCfeService.getAcumMonths(resumenConcepto.getMesesselect()));
			}else{
				resumenConcepto.setMesesselect(new String[]{});
			}
		}else{
			resumenConcepto.setMesesselect(new String []{});
		}
		
		if(resumenConcepto.getMesesselect() == null || resumenConcepto.getMesesselect().length == 0){
			resumenConcepto.setQueryDisabled(true);
		}else{
			resumenConcepto.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	public void validaAcumulado(RequestContext ctx){
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		if(resumenConcepto.isAcum()){
			resumenConcepto.setMesesselect(utileriasCfeService.getAcumMonths(resumenConcepto.getMesesselect()));
		}
		if(resumenConcepto.getMesesselect() == null || resumenConcepto.getMesesselect().length == 0){
			resumenConcepto.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			resumenConcepto.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		
		resumenConcepto.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(resumenConcepto.isAcum(),resumenConcepto.getMesesselect(),resumenConcepto.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+resumenConcepto.getMesesselect());//ene, feb
		logger.info("mes size: " + resumenConcepto.getMesesselect().length);
		if(resumenConcepto.getMesesselect().length > 0){
			for(int i = 0; i < resumenConcepto.getMesesselect().length; i++){
				logger.info(resumenConcepto.getMesesselect()[i]);
			}
		}
		logger.info("año"+resumenConcepto.getAnio());
		logger.info("calculo"+resumenConcepto.getCalculo().getId());//id
		logger.info("region"+resumenConcepto.getRegion());//id
		logger.info("clase"+resumenConcepto.getClase());//cve
		logger.info("textos"+resumenConcepto.getTexto());//todos
		logger.info("textoSeleccion"+resumenConcepto.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		resumenConcepto = resumenConceptoService.datosWhere(resumenConcepto);

		
		
		char a = Integer.toString(resumenConcepto.getAnio()).charAt(2); 
		char aa = Integer.toString(resumenConcepto.getAnio()).charAt(3); 
		String an = a+""+aa;
		resumenConcepto.setAnioRepCorto(an);
		
		if (resumenConcepto.getAjuste() != null && resumenConcepto.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(resumenConcepto.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			resumenConcepto = resumenConceptoService.consultaConceptosClase(resumenConcepto,sessionScopeUser);
		}
		if (resumenConcepto.getAjuste() != null && resumenConcepto.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(resumenConcepto.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			resumenConcepto = resumenConceptoService.consultaConceptosClase(resumenConcepto,sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			resumenConcepto = resumenConceptoService.consultaAjConceptosClase(resumenConcepto,sessionScopeUser);
			
		
			//gran total ajustado
			TotalBajasDosBean gTotAjustado = new TotalBajasDosBean();
			gTotAjustado.setAdq_baja((resumenConcepto.getTotReporteGeneral().getAdq_baja()).subtract(resumenConcepto.getTotReporteGeneral().getAdq_baja()) );
			gTotAjustado.setAdq_ac_baja((resumenConcepto.getTotReporteGeneral().getAdq_ac_baja()).subtract(resumenConcepto.getTotReporteGeneral().getAdq_ac_baja()));
			gTotAjustado.setEjercicio_baja((resumenConcepto.getTotReporteGeneral().getEjercicio_baja()).subtract(resumenConcepto.getTotReporteGeneral().getEjercicio_baja()));
			gTotAjustado.setDepr_tot((resumenConcepto.getTotReporteGeneral().getDepr_tot()).subtract(resumenConcepto.getTotReporteGeneral().getDepr_tot()));
			gTotAjustado.setCosto_h((resumenConcepto.getTotReporteGeneral().getCosto_h()).subtract(resumenConcepto.getTotReporteGeneral().getCosto_h()));
			gTotAjustado.setCosto_act((resumenConcepto.getTotReporteGeneral().getCosto_act()).subtract(resumenConcepto.getTotReporteGeneral().getCosto_act()));
			gTotAjustado.setDepre_anual_act((resumenConcepto.getTotReporteGeneral().getDepre_anual_act()).subtract(resumenConcepto.getTotReporteGeneral().getDepre_anual_act()));

			resumenConcepto.setTotReporteMesAjusteGran(gTotAjustado);
//			tGlobalClase.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (resumenConcepto.getAjuste() != null && resumenConcepto.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(resumenConcepto.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			resumenConcepto = resumenConceptoService.consultaConceptosClaseNetos(resumenConcepto,sessionScopeUser);
		}
			
		resumenConcepto.setAjuste(ajuste);
		resumenConcepto.setStrAnio(resumenConcepto.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("resumenClas", resumenConcepto);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		ResumenConceptoClasBean resumenConcepto = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_T_GCLA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = resumenConceptoService.generaDocumentoClase(resumenConcepto);
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
		ResumenConceptoClasBean datos = (ResumenConceptoClasBean)ctx.getFlowScope().get("resumenClas");
 		
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
		if ((datos.getListReporte().isEmpty())||(datos.getListReporte().equals(null)))
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(datos.getListReporte());
			datos.setListReporte(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_CONCEPTO_CLASE);
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
		jParams.put("AjustesParam", datos.getListTotalGlobalAjCla());
		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totAdqBaja", datos.getTotalGlobalClaTot().getAdq_baja());
			jParams.put("totAdqAcBaja", datos.getTotalGlobalClaTot().getAdq_ac_baja());
			jParams.put("totEjerBaja", datos.getTotalGlobalClaTot().getEjercicio_baja());
			jParams.put("totDepreTot", datos.getTotalGlobalClaTot().getDepr_tot());
			jParams.put("totCostoH", datos.getTotalGlobalClaTot().getCosto_h());
			jParams.put("totCostoAct", datos.getTotalGlobalClaTot().getCosto_act());
			jParams.put("totDepreAnulAct", datos.getTotalGlobalClaTot().getDepre_anual_act());
			jParams.put("totDepreTotal", datos.getTotalGlobalClaTot().getDepr_tot());
			
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		
		datasource = new JRBeanCollectionDataSource(datos.getListReporte(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("conceptoClase.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			 httpServletResponse.setContentType("application/pdf");
			 httpServletResponse.setHeader("Content-disposition", Constants.REP_RES_CON_CLAS_FILENAME + Constants.REP_PDF_EXT);
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
