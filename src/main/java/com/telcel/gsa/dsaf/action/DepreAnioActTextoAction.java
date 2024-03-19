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
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.DepreAnioActService;
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
@SuppressWarnings("deprecation")
@Service("depreAnioTxtAct")
@ViewScoped
public class DepreAnioActTextoAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(DepreAnioActTextoAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private DepreAnioActService depreAnioActService;
	

		
	public void initFlow(RequestContext ctx){
	
		DepreActBean dprtxt = new DepreActBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		dprtxt.setAcum(false);
		dprtxt.setMeses(meses);
		dprtxt.setAnio(calendar.get(Calendar.YEAR));
		dprtxt.setAnios(sessionScopeUser.getAnios());
		dprtxt.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		dprtxt.setCalculo(new BajasCalculoBean());
		dprtxt.setClase(new ArrayList<String>());
		dprtxt.setClases(new ArrayList<BajasClaseBean>());
		dprtxt.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		dprtxt.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		dprtxt.setTextos(Arrays.asList(txts));
		dprtxt.setTexto("TODOS");
		dprtxt.setAjuste(new BajasAjustesBean());
		dprtxt.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		dprtxt.setRegDisabled(true);
		dprtxt.setClaseDisabled(true);
		dprtxt.setTxtDisabled(true);
		dprtxt.setQueryDisabled(true);
		dprtxt.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		dprtxt.setSociedad(sessionScopeUser.getSociedad());
		ctx.getFlowScope().put("dprtxt", dprtxt);
	}
	
	public void computeYearVal(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		if(dprtxt.getMesesselect() == null || dprtxt.getMesesselect().length == 0){
			dprtxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			dprtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", dprtxt);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		dprtxt.setClases(utileriasCfeService.getClases(dprtxt, sessionScopeUser));
		dprtxt.setTxtsDesc(utileriasCfeService.getTxt(dprtxt, sessionScopeUser));
		
		if(dprtxt.getCalculo().getId() != 0){
			dprtxt.setRegDisabled(false);
			dprtxt.setClaseDisabled(false);
			dprtxt.setTxtDisabled(false);
		}else{
			dprtxt.setRegDisabled(true);
			dprtxt.setClaseDisabled(true);
			dprtxt.setTxtDisabled(true);
		}
		if(dprtxt.getMesesselect() == null || dprtxt.getMesesselect().length == 0){
			dprtxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			dprtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", dprtxt);
	}
	
	public void computeRegDependants(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		dprtxt.setClases(utileriasCfeService.getClases(dprtxt,sessionScopeUser));
		dprtxt.setTxtsDesc(utileriasCfeService.getTxt(dprtxt, sessionScopeUser));
		
		if(dprtxt.getCalculo().getId() != 0){
			dprtxt.setRegDisabled(false);
			dprtxt.setClaseDisabled(false);
			dprtxt.setTxtDisabled(false);
		}else{
			dprtxt.setRegDisabled(true);
			dprtxt.setClaseDisabled(true);
			dprtxt.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("adqtxt", dprtxt);
	}
	
	public void computeClassDependants(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		dprtxt.setTxtsDesc(utileriasCfeService.getTxt(dprtxt, sessionScopeUser));
		ctx.getFlowScope().put("dprtxt", dprtxt);
	}
	
	public void updateTxt(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		dprtxt.setTxtsDesc(utileriasCfeService.getTxt(dprtxt,sessionScopeUser));
		ctx.getFlowScope().put("dprtxt", dprtxt);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		if(dprtxt.isAcum()){
			if(dprtxt.getMesesselect() != null && dprtxt.getMesesselect().length != 0){
				dprtxt.setMesesselect(utileriasCfeService.getAcumMonths(dprtxt.getMesesselect()));
			}else{
				dprtxt.setMesesselect(new String[]{});
			}
		}else{
			dprtxt.setMesesselect(new String []{});
		}
		
		if(dprtxt.getMesesselect() == null || dprtxt.getMesesselect().length == 0){
			dprtxt.setQueryDisabled(true);
		}else{
			dprtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("dprtxt", dprtxt);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		if(dprtxt.isAcum()){
		dprtxt.setMesesselect(utileriasCfeService.getAcumMonths(dprtxt.getMesesselect()));
		}
		if(dprtxt.getMesesselect() == null || dprtxt.getMesesselect().length == 0){
			dprtxt.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			dprtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("dprtxt", dprtxt);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		List<TotalBean> totReporteMes = null;
		
		dprtxt.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(dprtxt.isAcum(), dprtxt.getMesesselect(),dprtxt.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+dprtxt.getMesesselect());//ene, feb
		logger.info("mes size: " + dprtxt.getMesesselect().length);
		if(dprtxt.getMesesselect().length > 0){
			for(int i = 0; i < dprtxt.getMesesselect().length; i++){
				logger.info(dprtxt.getMesesselect()[i]);
			}
		}
		logger.info("año"+dprtxt.getAnio());
		logger.info("calculo"+dprtxt.getCalculo().getId());//id
		logger.info("region"+dprtxt.getRegion());//id
		logger.info("clase"+dprtxt.getClase());//cve
		logger.info("textos"+dprtxt.getTexto());//todos
		logger.info("textoSeleccion"+dprtxt.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		dprtxt = depreAnioActService.datosWhere(dprtxt);
		ajuste.setDescripcion("SIN AJUSTES");
		
		dprtxt.setTitReptxt("CLASE");
		
		char a = Integer.toString(dprtxt.getAnio()).charAt(2); 
		char aa = Integer.toString(dprtxt.getAnio()).charAt(3); 
		String an = a+""+aa;
		dprtxt.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (dprtxt.getAjuste() != null && dprtxt.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(dprtxt.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			dprtxt =  depreAnioActService.consultaText(dprtxt, sessionScopeUser);
			
		}
		if (dprtxt.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(dprtxt.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			dprtxt = depreAnioActService.consultaText(dprtxt, sessionScopeUser);
			dprtxt = depreAnioActService.consultaTextAjus(dprtxt, sessionScopeUser);

			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((dprtxt.getTotReporteMesGeneral().getEnero()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((dprtxt.getTotReporteMesGeneral().getFebrero()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((dprtxt.getTotReporteMesGeneral().getMarzo()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((dprtxt.getTotReporteMesGeneral().getAbril()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((dprtxt.getTotReporteMesGeneral().getMayo()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((dprtxt.getTotReporteMesGeneral().getJunio()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((dprtxt.getTotReporteMesGeneral().getJulio()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((dprtxt.getTotReporteMesGeneral().getAgosto()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((dprtxt.getTotReporteMesGeneral().getSeptiembre()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((dprtxt.getTotReporteMesGeneral().getOctubre()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((dprtxt.getTotReporteMesGeneral().getNoviembre()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((dprtxt.getTotReporteMesGeneral().getDiciembre()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((dprtxt.getTotReporteMesGeneral().getTotal()).subtract(dprtxt.getTotReporteMesAjusteGeneral().getTotal()));
			dprtxt.setTotReporteMesAjusteGran(gTotAjustado);
			dprtxt.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			dprtxt.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (dprtxt.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(dprtxt.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			dprtxt = depreAnioActService.consultaTextNetos(dprtxt, sessionScopeUser);
		}
			
		dprtxt.setAjuste(ajuste);
		dprtxt.setStrAnio(dprtxt.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("dprtxt",dprtxt);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		DepreActBean dprtxt = (DepreActBean)ctx.getFlowScope().get("dprtxt");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_DPR_TEXTO_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = depreAnioActService.generaDocumentoText(dprtxt);
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
		DepreActBean datos = (DepreActBean)ctx.getFlowScope().get("dprtxt");
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
		if (datos.getSociedad().getNombre().equals("TELCEL"))
		{
			fullPath.append("TelceLogo3.gif");
		}
		if (datos.getSociedad().getNombre().equals("SERCOTEL"))
		{
			fullPath.append("logoSercotel.jpg");
		}
		if (datos.getSociedad().getNombre().equals("AMERICA MOVIL"))
		{
			fullPath.append("logoAM.png");
		}
		if (datos.getListRepDepretxt().isEmpty())
			datos = utileriasService.listCeroDepre(datos, "aqdtxt");
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_DPR_TEXTO);
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
		jParams.put("basicParam", datos.getListRepDepretxt());
		jParams.put("AjustesParam", datos.getListRepAjDepreTxt());
		if (datos.getAjuste().getClave().equals("TA"))
		{
			jParams.put("totalEn", datos.getTotReporteMesAjusteGran().getEnero());
			jParams.put("totalFeb", datos.getTotReporteMesAjusteGran().getFebrero());
			jParams.put("totalMa", datos.getTotReporteMesAjusteGran().getMarzo());
			jParams.put("totalAb", datos.getTotReporteMesAjusteGran().getAbril());
			jParams.put("totalMay", datos.getTotReporteMesAjusteGran().getMayo());
			jParams.put("totalJun", datos.getTotReporteMesAjusteGran().getJunio());
			jParams.put("totalJul", datos.getTotReporteMesAjusteGran().getJulio());
			jParams.put("totalAg", datos.getTotReporteMesAjusteGran().getAgosto());
			jParams.put("totalSep", datos.getTotReporteMesAjusteGran().getSeptiembre());
			jParams.put("totalOct", datos.getTotReporteMesAjusteGran().getOctubre());
			jParams.put("totalNov", datos.getTotReporteMesAjusteGran().getNoviembre());
			jParams.put("totalDic", datos.getTotReporteMesAjusteGran().getDiciembre());
			jParams.put("totalTot", datos.getTotReporteMesAjusteGran().getTotal());
		}
		jParams.put("rutImagen", fullPath.toString());
		jParams.put("SUBREPORT_DIR",fullPathRutaArchJasper);
		jParams.put("leyendaAjuste","AJUSTES DEP. AÑO ACTUALIZADA");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		jParams.put("anio2", Integer.toString(datos.getAnio()).substring(2));
		datasource = new JRBeanCollectionDataSource(datos.getListRepDepretxt(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("depreTxt.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			 httpServletResponse.setContentType("application/pdf");
			 httpServletResponse.setHeader("Content-disposition", Constants.REP_DPR_TEXTO_FILENAME + Constants.REP_PDF_EXT);
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
