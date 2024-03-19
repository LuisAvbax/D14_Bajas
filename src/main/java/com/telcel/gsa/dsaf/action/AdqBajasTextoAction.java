package com.telcel.gsa.dsaf.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasMesBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.AqdBajasService;
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
@Service("adqtxtAct")
@ViewScoped
public class AdqBajasTextoAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(AdqBajasTextoAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private AqdBajasService adqBajasService;

		
	public void initFlow(RequestContext ctx){
	
		AdqBajasBean adqtxt = new AdqBajasBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		adqtxt.setAcum(false);
		adqtxt.setMeses(meses);
		adqtxt.setAnio(calendar.get(Calendar.YEAR));
		adqtxt.setAnios(sessionScopeUser.getAnios());
		adqtxt.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		adqtxt.setCalculo(new BajasCalculoBean());
		adqtxt.setClase(new ArrayList<String>());
		adqtxt.setClases(new ArrayList<BajasClaseBean>());
		adqtxt.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		adqtxt.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		adqtxt.setTextos(Arrays.asList(txts));
		adqtxt.setTexto("TODOS");
		adqtxt.setAjuste(new BajasAjustesBean());
		adqtxt.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		adqtxt.setRegDisabled(true);
		adqtxt.setClaseDisabled(true);
		adqtxt.setTxtDisabled(true);
		adqtxt.setQueryDisabled(true);
		adqtxt.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		adqtxt.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void computeYearVal(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		if(adqtxt.getMesesselect() == null || adqtxt.getMesesselect().length == 0){
			adqtxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		adqtxt.setClases(utileriasCfeService.getClases(adqtxt,sessionScopeUser));
		adqtxt.setTxtsDesc(utileriasCfeService.getTxt(adqtxt,sessionScopeUser));
		if(adqtxt.getCalculo().getId() != 0){
			adqtxt.setRegDisabled(false);
			adqtxt.setClaseDisabled(false);
			adqtxt.setTxtDisabled(false);
		}else{
			adqtxt.setRegDisabled(true);
			adqtxt.setClaseDisabled(true);
			adqtxt.setTxtDisabled(true);
		}
		if(adqtxt.getMesesselect() == null || adqtxt.getMesesselect().length == 0){
			adqtxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void computeRegDependants(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		adqtxt.setClases(utileriasCfeService.getClases(adqtxt,sessionScopeUser));
		adqtxt.setTxtsDesc(utileriasCfeService.getTxt(adqtxt,sessionScopeUser));
		if(adqtxt.getCalculo().getId() != 0){
			adqtxt.setRegDisabled(false);
			adqtxt.setClaseDisabled(false);
			adqtxt.setTxtDisabled(false);
		}else{
			adqtxt.setRegDisabled(true);
			adqtxt.setClaseDisabled(true);
			adqtxt.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void computeClassDependants(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		adqtxt.setTxtsDesc(utileriasCfeService.getTxt(adqtxt,sessionScopeUser));
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void updateTxt(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		adqtxt.setTxtsDesc(utileriasCfeService.getTxt(adqtxt,sessionScopeUser));
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		if(adqtxt.isAcum()){
			if(adqtxt.getMesesselect() != null && adqtxt.getMesesselect().length != 0){
				adqtxt.setMesesselect(utileriasCfeService.getAcumMonths(adqtxt.getMesesselect()));
			}else{
				adqtxt.setMesesselect(new String[]{});
			}
		}else{
			adqtxt.setMesesselect(new String []{});
		}
		
		if(adqtxt.getMesesselect() == null || adqtxt.getMesesselect().length == 0){
			adqtxt.setQueryDisabled(true);
		}else{
			adqtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		if(adqtxt.isAcum()){
		adqtxt.setMesesselect(utileriasCfeService.getAcumMonths(adqtxt.getMesesselect()));
		}
		if(adqtxt.getMesesselect() == null || adqtxt.getMesesselect().length == 0){
			adqtxt.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			adqtxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqtxt", adqtxt);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		List<TotalBean> totReporteMes = null;
		
		adqtxt.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(adqtxt));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+adqtxt.getMesesselect());//ene, feb
		logger.info("mes size: " + adqtxt.getMesesselect().length);
		if(adqtxt.getMesesselect().length > 0){
			for(int i = 0; i < adqtxt.getMesesselect().length; i++){
				logger.info(adqtxt.getMesesselect()[i]);
			}
		}
		logger.info("año"+adqtxt.getAnio());
		logger.info("calculo"+adqtxt.getCalculo().getId());//id
		logger.info("region"+adqtxt.getRegion());//id
		logger.info("clase"+adqtxt.getClase());//cve
		logger.info("textos"+adqtxt.getTexto());//todos
		logger.info("textoSeleccion"+adqtxt.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		adqtxt = adqBajasService.DatosWhere(adqtxt);
		ajuste.setDescripcion("SIN AJUSTES");
		
		adqtxt.setTitReptxt("CLASE");
		
		char a = Integer.toString(adqtxt.getAnio()).charAt(2); 
		char aa = Integer.toString(adqtxt.getAnio()).charAt(3); 
		String an = a+""+aa;
		adqtxt.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (adqtxt.getAjuste() != null && adqtxt.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(adqtxt.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			adqtxt =  adqBajasService.ConsultaText(adqtxt, sessionScopeUser);
		}
		if (adqtxt.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(adqtxt.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			adqtxt = adqBajasService.ConsultaText(adqtxt, sessionScopeUser);
			adqtxt = adqBajasService.ConsultaTextAjus(adqtxt, sessionScopeUser);
			
			
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((adqtxt.getTotReporteMesGeneral().getEnero()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((adqtxt.getTotReporteMesGeneral().getFebrero()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((adqtxt.getTotReporteMesGeneral().getMarzo()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((adqtxt.getTotReporteMesGeneral().getAbril()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((adqtxt.getTotReporteMesGeneral().getMayo()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((adqtxt.getTotReporteMesGeneral().getJunio()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((adqtxt.getTotReporteMesGeneral().getJulio()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((adqtxt.getTotReporteMesGeneral().getAgosto()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((adqtxt.getTotReporteMesGeneral().getSeptiembre()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((adqtxt.getTotReporteMesGeneral().getOctubre()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((adqtxt.getTotReporteMesGeneral().getNoviembre()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((adqtxt.getTotReporteMesGeneral().getDiciembre()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((adqtxt.getTotReporteMesGeneral().getTotal()).subtract(adqtxt.getTotReporteMesAjusteGeneral().getTotal()));
			adqtxt.setTotReporteMesAjusteGran(gTotAjustado);
			adqtxt.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			adqtxt.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (adqtxt.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(adqtxt.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			adqtxt = adqBajasService.ConsultaTextNetos(adqtxt, sessionScopeUser);
		}
			
		adqtxt.setAjuste(ajuste);
		adqtxt.setStrAnio(adqtxt.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("adqtxt", adqtxt);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		AdqBajasBean adqtxt = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_ADQ_TEXTO_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = adqBajasService.generaDocumentoText(adqtxt);
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
		AdqBajasBean datos = (AdqBajasBean)ctx.getFlowScope().get("adqtxt");
	 		
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
			if (datos.getListRepAdqtxt().isEmpty())
				datos = utileriasService.listCeroAdq(datos, "aqdtxt");
			Map<String, Object> jParams = new HashMap<String,Object>();
			jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
			jParams.put("titulo2", Constants.TIT_REP_BAJ_TEXTO);
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
			jParams.put("basicParam", datos.getListRepAdqtxt());
			jParams.put("AjustesParam", datos.getListRepAjAdqTxt());
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
			jParams.put("leyendaAjuste","AJUSTES AL MOI");
			jParams.put("ajustSoli",datos.getAjuste().getClave());
			jParams.put("anio2", Integer.toString(datos.getAnio()).substring(2));
			datasource = new JRBeanCollectionDataSource(datos.getListRepAdqtxt(), true);
	 		
			StringBuffer fullPathRutaArch = new StringBuffer();
			fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
			fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
			fullPathRutaArch.append("bajasTxt.jasper");//nombre del archivo
			
			
			File f = new File(fullPathRutaArch.toString());
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
				httpServletResponse = 
						(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				 httpServletResponse.setContentType("application/pdf");
				 httpServletResponse.setHeader("Content-disposition", Constants.REP_ADQ_TEXTO_FILENAME + Constants.REP_PDF_EXT);
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
