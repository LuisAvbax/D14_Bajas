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
import javax.sql.DataSource;

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
@Service("depreAnioClasAct")
@ViewScoped
public class DepreAnioActClasAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(DepreAnioActClasAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private DepreAnioActService depreAnioActService;
	@Autowired
	UtileriasService utileriasService;

		
	public void initFlow(RequestContext ctx){
	
		DepreActBean dprclas = new DepreActBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		dprclas.setAcum(false);
		dprclas.setMeses(meses);
		dprclas.setAnio(calendar.get(Calendar.YEAR));
		dprclas.setAnios(sessionScopeUser.getAnios());
		dprclas.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		dprclas.setCalculo(new BajasCalculoBean());
		dprclas.setClase(new ArrayList<String>());
		dprclas.setClases(new ArrayList<BajasClaseBean>());
		dprclas.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		dprclas.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		dprclas.setTextos(Arrays.asList(txts));
		dprclas.setTexto("TODOS");
		dprclas.setAjuste(new BajasAjustesBean());
		dprclas.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		dprclas.setRegDisabled(true);
		dprclas.setClaseDisabled(true);
		dprclas.setTxtDisabled(true);
		dprclas.setQueryDisabled(true);
		dprclas.setSociedad(sessionScopeUser.getSociedad());
		dprclas.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("dprclas", dprclas);
	}
	
	public void computeYearVal(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		if(dprclas.getMesesselect() == null || dprclas.getMesesselect().length == 0){
			dprclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			dprclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqclas", dprclas);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		dprclas.setClases(utileriasCfeService.getClases(dprclas,sessionScopeUser));
		dprclas.setTxtsDesc(utileriasCfeService.getTxt(dprclas,sessionScopeUser));
		
		if(dprclas.getCalculo().getId() != 0){
			dprclas.setRegDisabled(false);
			dprclas.setClaseDisabled(false);
			dprclas.setTxtDisabled(false);
		}else{
			dprclas.setRegDisabled(true);
			dprclas.setClaseDisabled(true);
			dprclas.setTxtDisabled(true);
		}
		if(dprclas.getMesesselect() == null || dprclas.getMesesselect().length == 0){
			dprclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			dprclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("dprclas", dprclas);
	}
	
	public void computeRegDependants(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		dprclas.setClases(utileriasCfeService.getClases(dprclas,sessionScopeUser));
		dprclas.setTxtsDesc(utileriasCfeService.getTxt(dprclas,sessionScopeUser));
		
		if(dprclas.getCalculo().getId() != 0){
			dprclas.setRegDisabled(false);
			dprclas.setClaseDisabled(false);
			dprclas.setTxtDisabled(false);
		}else{
			dprclas.setRegDisabled(true);
			dprclas.setClaseDisabled(true);
			dprclas.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("dprclas", dprclas);
	}
	
	public void computeClassDependants(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		dprclas.setTxtsDesc(utileriasCfeService.getTxt(dprclas,sessionScopeUser));
		
		ctx.getFlowScope().put("adqclas", dprclas);
	}
	
	public void updateTxt(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		dprclas.setTxtsDesc(utileriasCfeService.getTxt(dprclas,sessionScopeUser));
		ctx.getFlowScope().put("adqclas", dprclas);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		if(dprclas.isAcum()){
			if(dprclas.getMesesselect() != null && dprclas.getMesesselect().length != 0){
				dprclas.setMesesselect(utileriasCfeService.getAcumMonths(dprclas.getMesesselect()));
			}else{
				dprclas.setMesesselect(new String[]{});
			}
		}else{
			dprclas.setMesesselect(new String []{});
		}
		
		if(dprclas.getMesesselect() == null || dprclas.getMesesselect().length == 0){
			dprclas.setQueryDisabled(true);
		}else{
			dprclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("dprclas", dprclas);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		if(dprclas.isAcum()){
		dprclas.setMesesselect(utileriasCfeService.getAcumMonths(dprclas.getMesesselect()));
		}
		if(dprclas.getMesesselect() == null || dprclas.getMesesselect().length == 0){
			dprclas.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			dprclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("dprclas", dprclas);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		DepreActBean dprclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		List<TotalBean> totReporteMes = null;
		
		dprclas.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(dprclas.isAcum(), dprclas.getMesesselect(), dprclas.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+dprclas.getMesesselect());//ene, feb
		logger.info("mes size: " + dprclas.getMesesselect().length);
		if(dprclas.getMesesselect().length > 0){
			for(int i = 0; i < dprclas.getMesesselect().length; i++){
				logger.info(dprclas.getMesesselect()[i]);
			}
		}
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		dprclas = depreAnioActService.datosWhere(dprclas);
		ajuste.setDescripcion("SIN AJUSTES");
		dprclas.setTitReptxt("CLASE");
		char a = Integer.toString(dprclas.getAnio()).charAt(2); 
		char aa = Integer.toString(dprclas.getAnio()).charAt(3); 
		String an = a+""+aa;
		dprclas.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (dprclas.getAjuste() != null && dprclas.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(dprclas.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			dprclas = depreAnioActService.consultaClas(dprclas, sessionScopeUser);
		}
		if (dprclas.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(dprclas.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			dprclas = depreAnioActService.consultaClas(dprclas, sessionScopeUser);
			dprclas = depreAnioActService.consultaClasAjus(dprclas, sessionScopeUser);
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((dprclas.getTotReporteMesGeneral().getEnero()).subtract(dprclas.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((dprclas.getTotReporteMesGeneral().getFebrero()).subtract(dprclas.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((dprclas.getTotReporteMesGeneral().getMarzo()).subtract(dprclas.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((dprclas.getTotReporteMesGeneral().getAbril()).subtract(dprclas.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((dprclas.getTotReporteMesGeneral().getMayo()).subtract(dprclas.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((dprclas.getTotReporteMesGeneral().getJunio()).subtract(dprclas.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((dprclas.getTotReporteMesGeneral().getJulio()).subtract(dprclas.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((dprclas.getTotReporteMesGeneral().getAgosto()).subtract(dprclas.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((dprclas.getTotReporteMesGeneral().getSeptiembre()).subtract(dprclas.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((dprclas.getTotReporteMesGeneral().getOctubre()).subtract(dprclas.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((dprclas.getTotReporteMesGeneral().getNoviembre()).subtract(dprclas.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((dprclas.getTotReporteMesGeneral().getDiciembre()).subtract(dprclas.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((dprclas.getTotReporteMesGeneral().getTotal()).subtract(dprclas.getTotReporteMesAjusteGeneral().getTotal()));
			dprclas.setTotReporteMesAjusteGran(gTotAjustado);
			dprclas.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			dprclas.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (dprclas.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(dprclas.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			dprclas = depreAnioActService.consultaClasNetos(dprclas, sessionScopeUser);
			
		}

		dprclas.setAjuste(ajuste);
		dprclas.setStrAnio(dprclas.getAnio().toString().substring(2, 4));
		
	
		ctx.getFlowScope().put("dprclas", dprclas);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		DepreActBean adqclas = (DepreActBean)ctx.getFlowScope().get("dprclas");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_DPR_CLAS_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = depreAnioActService.generaDocumentoClas(adqclas);
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();	
	}	
	
	//PDF
	@SuppressWarnings("deprecation")
	public void descargaPDF(RequestContext ctx) throws ServletException, IOException{
		DepreActBean datos = (DepreActBean)ctx.getFlowScope().get("dprclas");
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
		if (datos.getListRepDepreClas().isEmpty())
			datos = utileriasService.listCeroDepre(datos, "aqdClas");
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_DPR_CLASE);
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
		jParams.put("basicParam", datos.getListRepDepreClas()); 
		jParams.put("AjustesParam", datos.getListRepAjDepreClas());
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
		datasource = new JRBeanCollectionDataSource(datos.getListRepDepreClas(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("depreClas.jasper");//nombre del archivo
		
		
		File f = new File(fullPathRutaArch.toString());
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			 httpServletResponse = 
						(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				 httpServletResponse.setContentType("application/pdf");
				 httpServletResponse.setHeader("Content-disposition", Constants.REP_DPR_CLAS_FILENAME + Constants.REP_PDF_EXT);
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
