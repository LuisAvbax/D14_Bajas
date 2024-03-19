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
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalClaBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
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
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("tGlobalClaAct")
@ViewScoped
public class TotalGlobalClaAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(TotalGlobalClaAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private TotalGlobalService totalGlobalService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		TotalGlobalClaBean tGlobalClase = new TotalGlobalClaBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		tGlobalClase.setAcum(false);
		tGlobalClase.setMeses(meses);
		tGlobalClase.setAnio(calendar.get(Calendar.YEAR));
		tGlobalClase.setAnios(sessionScopeUser.getAnios());
		tGlobalClase.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		tGlobalClase.setCalculo(new BajasCalculoBean());
		tGlobalClase.setClase(new ArrayList<String>());
		tGlobalClase.setClases(new ArrayList<BajasClaseBean>());
		tGlobalClase.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		tGlobalClase.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		tGlobalClase.setTextos(Arrays.asList(txts));
		tGlobalClase.setTexto("TODOS");

		tGlobalClase.setAjuste(new BajasAjustesBean());
		tGlobalClase.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		tGlobalClase.setRegDisabled(true);
		tGlobalClase.setClaseDisabled(true);
		tGlobalClase.setTxtDisabled(true);
		tGlobalClase.setQueryDisabled(true);
		tGlobalClase.setSociedad(sessionScopeUser.getSociedad().getNombre());
		tGlobalClase.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("tGlobalCla", tGlobalClase);
	}
	
	public void computeYearVal(RequestContext ctx){
		TotalGlobalClaBean tGlobalClase = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		if(tGlobalClase.getMesesselect() == null || tGlobalClase.getMesesselect().length == 0){
			tGlobalClase.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			tGlobalClase.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalCla", tGlobalClase);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		TotalGlobalClaBean tGlobalClase = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		tGlobalClase.setClases(utileriasCfeService.getClasesGen(tGlobalClase.getRegion(), tGlobalClase.getCalculo(),sessionScopeUser));
		tGlobalClase.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalClase.getRegion(), tGlobalClase.getCalculo(), tGlobalClase.getClase(), tGlobalClase.getTexto(),sessionScopeUser));
		if(tGlobalClase.getCalculo().getId() != 0){
			tGlobalClase.setRegDisabled(false);
			tGlobalClase.setClaseDisabled(false);
			tGlobalClase.setTxtDisabled(false);
		}else{
			tGlobalClase.setRegDisabled(true);
			tGlobalClase.setClaseDisabled(true);
			tGlobalClase.setTxtDisabled(true);
		}
		if(tGlobalClase.getMesesselect() == null || tGlobalClase.getMesesselect().length == 0){
			tGlobalClase.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			tGlobalClase.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalCla", tGlobalClase);
	}
	
	public void computeRegDependants(RequestContext ctx){
		TotalGlobalClaBean tGlobalClase = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		tGlobalClase.setClases(utileriasCfeService.getClasesGen(tGlobalClase.getRegion(), tGlobalClase.getCalculo(), sessionScopeUser));
		tGlobalClase.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalClase.getRegion(), tGlobalClase.getCalculo(), tGlobalClase.getClase(), tGlobalClase.getTexto(),sessionScopeUser));
		if(tGlobalClase.getCalculo().getId() != 0){
			tGlobalClase.setRegDisabled(false);
			tGlobalClase.setClaseDisabled(false);
			tGlobalClase.setTxtDisabled(false);
		}else{
			tGlobalClase.setRegDisabled(true);
			tGlobalClase.setClaseDisabled(true);
			tGlobalClase.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("tGlobalCla", tGlobalClase);
	}
	
	public void computeClassDependants(RequestContext ctx){
		TotalGlobalClaBean tGlobalCla = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		tGlobalCla.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalCla.getRegion(), tGlobalCla.getCalculo(), tGlobalCla.getClase(), tGlobalCla.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("tGlobalCla", tGlobalCla);
	}
	
	public void updateTxt(RequestContext ctx){
		TotalGlobalClaBean tGlobalCla = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		tGlobalCla.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalCla.getRegion(), tGlobalCla.getCalculo(), tGlobalCla.getClase(), tGlobalCla.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("tGlobalCla", tGlobalCla);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		TotalGlobalClaBean tGlobalCla = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		if(tGlobalCla.isAcum()){
			if(tGlobalCla.getMesesselect() != null && tGlobalCla.getMesesselect().length != 0){
				tGlobalCla.setMesesselect(utileriasCfeService.getAcumMonths(tGlobalCla.getMesesselect()));
			}else{
				tGlobalCla.setMesesselect(new String[]{});
			}
		}else{
			tGlobalCla.setMesesselect(new String []{});
		}
		
		if(tGlobalCla.getMesesselect() == null || tGlobalCla.getMesesselect().length == 0){
			tGlobalCla.setQueryDisabled(true);
		}else{
			tGlobalCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalCla", tGlobalCla);
	}
	
	public void validaAcumulado(RequestContext ctx){
		TotalGlobalClaBean tGlobalCla = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		if(tGlobalCla.isAcum()){
			tGlobalCla.setMesesselect(utileriasCfeService.getAcumMonths(tGlobalCla.getMesesselect()));
		}
		if(tGlobalCla.getMesesselect() == null || tGlobalCla.getMesesselect().length == 0){
			tGlobalCla.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			tGlobalCla.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalCla", tGlobalCla);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		TotalGlobalClaBean tGlobalClase = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		
		tGlobalClase.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(tGlobalClase.isAcum(),tGlobalClase.getMesesselect(),tGlobalClase.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+tGlobalClase.getMesesselect());//ene, feb
		logger.info("mes size: " + tGlobalClase.getMesesselect().length);
		if(tGlobalClase.getMesesselect().length > 0){
			for(int i = 0; i < tGlobalClase.getMesesselect().length; i++){
				logger.info(tGlobalClase.getMesesselect()[i]);
			}
		}
		logger.info("año"+tGlobalClase.getAnio());
		logger.info("calculo"+tGlobalClase.getCalculo().getId());//id
		logger.info("region"+tGlobalClase.getRegion());//id
		logger.info("clase"+tGlobalClase.getClase());//cve
		logger.info("textos"+tGlobalClase.getTexto());//todos
		logger.info("textoSeleccion"+tGlobalClase.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		tGlobalClase = totalGlobalService.datosWhere(tGlobalClase);

		
		
		char a = Integer.toString(tGlobalClase.getAnio()).charAt(2); 
		char aa = Integer.toString(tGlobalClase.getAnio()).charAt(3); 
		String an = a+""+aa;
		tGlobalClase.setAnioRepCorto(an);
		
		if (tGlobalClase.getAjuste() != null && tGlobalClase.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(tGlobalClase.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			tGlobalClase = totalGlobalService.consultaTotalGlobalClase(tGlobalClase,sessionScopeUser);
		}
		if (tGlobalClase.getAjuste() != null && tGlobalClase.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(tGlobalClase.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			tGlobalClase = totalGlobalService.consultaTotalGlobalClase(tGlobalClase,sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			tGlobalClase = totalGlobalService.consultaAjTotalGlobalClase(tGlobalClase,sessionScopeUser);
			
		
			//gran total ajustado
			TotalBajasDosBean gTotAjustado = new TotalBajasDosBean();
			gTotAjustado.setAdq_baja((tGlobalClase.getTotReporteGeneral().getAdq_baja()).subtract(tGlobalClase.getTotReporteGeneral().getAdq_baja()) );
			gTotAjustado.setAdq_ac_baja((tGlobalClase.getTotReporteGeneral().getAdq_ac_baja()).subtract(tGlobalClase.getTotReporteGeneral().getAdq_ac_baja()));
			gTotAjustado.setEjercicio_baja((tGlobalClase.getTotReporteGeneral().getEjercicio_baja()).subtract(tGlobalClase.getTotReporteGeneral().getEjercicio_baja()));
			gTotAjustado.setDepr_tot((tGlobalClase.getTotReporteGeneral().getDepr_tot()).subtract(tGlobalClase.getTotReporteGeneral().getDepr_tot()));
			gTotAjustado.setCosto_h((tGlobalClase.getTotReporteGeneral().getCosto_h()).subtract(tGlobalClase.getTotReporteGeneral().getCosto_h()));
			gTotAjustado.setCosto_act((tGlobalClase.getTotReporteGeneral().getCosto_act()).subtract(tGlobalClase.getTotReporteGeneral().getCosto_act()));
			gTotAjustado.setDepre_anual_act((tGlobalClase.getTotReporteGeneral().getDepre_anual_act()).subtract(tGlobalClase.getTotReporteGeneral().getDepre_anual_act()));

			tGlobalClase.setTotReporteMesAjusteGran(gTotAjustado);
//			tGlobalClase.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (tGlobalClase.getAjuste() != null && tGlobalClase.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(tGlobalClase.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			tGlobalClase = totalGlobalService.consultaTGlobalClaNetos(tGlobalClase,sessionScopeUser);
		}
			
		tGlobalClase.setAjuste(ajuste);
		tGlobalClase.setStrAnio(tGlobalClase.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("tGlobalCla", tGlobalClase);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		TotalGlobalClaBean tGlobalClase = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_T_GCLA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = totalGlobalService.generaDocumentoClase(tGlobalClase);
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
		TotalGlobalClaBean datos = (TotalGlobalClaBean)ctx.getFlowScope().get("tGlobalCla");
 		
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
		

		if (datos.getSociedad().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (datos.getSociedad().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (datos.getSociedad().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (datos.getListTotalGlobalCla().isEmpty())
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(datos.getListTotalGlobalCla());
			datos.setListTotalGlobalCla(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_GLOB_CLASE);
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
		jParams.put("basicParam", datos.getListTotalGlobalCla());
		jParams.put("AjustesParam", datos.getListTotalGlobalAjCla());
		
		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totalAdqBajas", datos.getTotalGlobalClaTot().getAdq_baja());
			jParams.put("totalAdqAcBajas", datos.getTotalGlobalClaTot().getAdq_ac_baja());
			jParams.put("totalEjercicio", datos.getTotalGlobalClaTot().getEjercicio_baja());
			jParams.put("totalDepreciacion", datos.getTotalGlobalClaTot().getDepr_tot());
			jParams.put("totalCostoH", datos.getTotalGlobalClaTot().getCosto_h());
			jParams.put("totalCostoAct", datos.getTotalGlobalClaTot().getCosto_act());
			jParams.put("totalDepreciacionAnual", datos.getTotalGlobalClaTot().getDepre_anual_act());
		}
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		
		datasource = new JRBeanCollectionDataSource(datos.getListTotalGlobalCla(), true);
		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("globalClase.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		try {
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_T_GCLA_FILENAME + Constants.REP_PDF_EXT);
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
