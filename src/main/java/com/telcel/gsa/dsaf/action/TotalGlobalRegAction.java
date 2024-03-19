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
import com.telcel.gsa.dsaf.bean.TotalGlobalRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("tGlobalRegAct")
@ViewScoped
public class TotalGlobalRegAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(TotalGlobalRegAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private TotalGlobalService totalGlobalService;
	@Autowired
	private UtileriasService utileriasService;
		
	public void initFlow(RequestContext ctx){
	
		TotalGlobalRegBean tGlobalReg = new TotalGlobalRegBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		tGlobalReg.setAcum(false);
		tGlobalReg.setMeses(meses);
		tGlobalReg.setAnio(calendar.get(Calendar.YEAR));
		tGlobalReg.setAnios(sessionScopeUser.getAnios());
		tGlobalReg.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		tGlobalReg.setCalculo(new BajasCalculoBean());
		tGlobalReg.setClase(new ArrayList<String>());
		tGlobalReg.setClases(new ArrayList<BajasClaseBean>());
		tGlobalReg.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		tGlobalReg.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params,sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		tGlobalReg.setTextos(Arrays.asList(txts));
		tGlobalReg.setTexto("TODOS");

		tGlobalReg.setAjuste(new BajasAjustesBean());
		tGlobalReg.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		tGlobalReg.setRegDisabled(true);
		tGlobalReg.setClaseDisabled(true);
		tGlobalReg.setTxtDisabled(true);
		tGlobalReg.setQueryDisabled(true);
		tGlobalReg.setSociedad(sessionScopeUser.getSociedad().getNombre());
		tGlobalReg.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void computeYearVal(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		if(tGlobalReg.getMesesselect() == null || tGlobalReg.getMesesselect().length == 0){
			tGlobalReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			tGlobalReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		tGlobalReg.setClases(utileriasCfeService.getClasesGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(),sessionScopeUser));
		tGlobalReg.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(), tGlobalReg.getClase(), tGlobalReg.getTexto(),sessionScopeUser));
		if(tGlobalReg.getCalculo().getId() != 0){
			tGlobalReg.setRegDisabled(false);
			tGlobalReg.setClaseDisabled(false);
			tGlobalReg.setTxtDisabled(false);
		}else{
			tGlobalReg.setRegDisabled(true);
			tGlobalReg.setClaseDisabled(true);
			tGlobalReg.setTxtDisabled(true);
		}
		if(tGlobalReg.getMesesselect() == null || tGlobalReg.getMesesselect().length == 0){
			tGlobalReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			tGlobalReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void computeRegDependants(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		tGlobalReg.setClases(utileriasCfeService.getClasesGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(),sessionScopeUser));
		tGlobalReg.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(), tGlobalReg.getClase(), tGlobalReg.getTexto(),sessionScopeUser));
		if(tGlobalReg.getCalculo().getId() != 0){
			tGlobalReg.setRegDisabled(false);
			tGlobalReg.setClaseDisabled(false);
			tGlobalReg.setTxtDisabled(false);
		}else{
			tGlobalReg.setRegDisabled(true);
			tGlobalReg.setClaseDisabled(true);
			tGlobalReg.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void computeClassDependants(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		tGlobalReg.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(), tGlobalReg.getClase(), tGlobalReg.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void updateTxt(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		tGlobalReg.setTxtsDesc(utileriasCfeService.getTxtGen(tGlobalReg.getRegion(), tGlobalReg.getCalculo(), tGlobalReg.getClase(), tGlobalReg.getTexto(),sessionScopeUser));
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		if(tGlobalReg.isAcum()){
			if(tGlobalReg.getMesesselect() != null && tGlobalReg.getMesesselect().length != 0){
				tGlobalReg.setMesesselect(utileriasCfeService.getAcumMonths(tGlobalReg.getMesesselect()));
			}else{
				tGlobalReg.setMesesselect(new String[]{});
			}
		}else{
			tGlobalReg.setMesesselect(new String []{});
		}
		
		if(tGlobalReg.getMesesselect() == null || tGlobalReg.getMesesselect().length == 0){
			tGlobalReg.setQueryDisabled(true);
		}else{
			tGlobalReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	public void validaAcumulado(RequestContext ctx){
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		if(tGlobalReg.isAcum()){
		tGlobalReg.setMesesselect(utileriasCfeService.getAcumMonths(tGlobalReg.getMesesselect()));
		}
		if(tGlobalReg.getMesesselect() == null || tGlobalReg.getMesesselect().length == 0){
			tGlobalReg.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			tGlobalReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		tGlobalReg.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(tGlobalReg.isAcum(),tGlobalReg.getMesesselect(),tGlobalReg.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+tGlobalReg.getMesesselect());//ene, feb
		logger.info("mes size: " + tGlobalReg.getMesesselect().length);
		if(tGlobalReg.getMesesselect().length > 0){
			for(int i = 0; i < tGlobalReg.getMesesselect().length; i++){
				logger.info(tGlobalReg.getMesesselect()[i]);
			}
		}
		logger.info("año"+tGlobalReg.getAnio());
		logger.info("calculo"+tGlobalReg.getCalculo().getId());//id
		logger.info("region"+tGlobalReg.getRegion());//id
		logger.info("clase"+tGlobalReg.getClase());//cve
		logger.info("textos"+tGlobalReg.getTexto());//todos
		logger.info("textoSeleccion"+tGlobalReg.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		tGlobalReg = totalGlobalService.datosWhere(tGlobalReg);

		
		
		char a = Integer.toString(tGlobalReg.getAnio()).charAt(2); 
		char aa = Integer.toString(tGlobalReg.getAnio()).charAt(3); 
		String an = a+""+aa;
		tGlobalReg.setAnioRepCorto(an);
		
		if (tGlobalReg.getAjuste() != null && tGlobalReg.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(tGlobalReg.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			tGlobalReg = totalGlobalService.consultaTotalGlobalRegion(tGlobalReg,sessionScopeUser);
		}
		if (tGlobalReg.getAjuste() != null && tGlobalReg.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(tGlobalReg.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			tGlobalReg = totalGlobalService.consultaTotalGlobalRegion(tGlobalReg,sessionScopeUser);
			//AGREGAR CONSULTA AJUSTES
			tGlobalReg = totalGlobalService.consultaAjTotalGlobalRegion(tGlobalReg,sessionScopeUser);
		}
		if (tGlobalReg.getAjuste() != null && tGlobalReg.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(tGlobalReg.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			
			//AGREGAR CONSULTA NETOS
			tGlobalReg = totalGlobalService.consultaTGlobalRegNetos(tGlobalReg,sessionScopeUser);
		}
			
		tGlobalReg.setAjuste(ajuste);
		tGlobalReg.setStrAnio(tGlobalReg.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("tGlobalReg", tGlobalReg);
		
		
	}	
	
	public void descarga(RequestContext ctx){
		
		TotalGlobalRegBean tGlobalReg = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_T_GREG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = totalGlobalService.generaDocumento(tGlobalReg);
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
		TotalGlobalRegBean datos = (TotalGlobalRegBean)ctx.getFlowScope().get("tGlobalReg");
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

		if (datos.getListTotalGlobalReg().isEmpty())
		{
			List<BajasDosBean> dtList= new ArrayList<BajasDosBean>();
			dtList = utileriasService.listBajasDos(datos.getListTotalGlobalReg());
			datos.setListTotalGlobalReg(dtList);
		}
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_GLOB_REGION);
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
		jParams.put("basicParam", datos.getListTotalGlobalReg());
		jParams.put("AjustesParam", datos.getListTotalGlobalAjReg());

		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totalAdqBajas", datos.getTotalGlobalRegTot().getAdq_baja());
			jParams.put("totalAdqAcBajas", datos.getTotalGlobalRegTot().getAdq_ac_baja());
			jParams.put("totalEjercicio", datos.getTotalGlobalRegTot().getEjercicio_baja());
			jParams.put("totalDepreciacion", datos.getTotalGlobalRegTot().getDepr_tot());
			jParams.put("totalCostoH", datos.getTotalGlobalRegTot().getCosto_h());
			jParams.put("totalCostoAct", datos.getTotalGlobalRegTot().getCosto_act());
			jParams.put("totalDepreciacionAnual", datos.getTotalGlobalRegTot().getDepre_anual_act());
		}
		
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES POR TIPO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		
		datasource = new JRBeanCollectionDataSource(datos.getListTotalGlobalReg(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("globalRegion.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		try {
		
		 httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		 httpServletResponse.setContentType("application/pdf");
		 httpServletResponse.setHeader("Content-disposition", Constants.REP_T_GREG_FILENAME + Constants.REP_PDF_EXT);
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
