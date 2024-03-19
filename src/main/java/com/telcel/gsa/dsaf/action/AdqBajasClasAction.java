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

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
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
import com.telcel.gsa.dsaf.util.CfeException;
/**
 * 
 * @author VI5XXAA
 *
 */


@Service("adqclasAct")
@ViewScoped
public class AdqBajasClasAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(AdqBajasClasAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private AqdBajasService adqBajasService;


		
	public void initFlow(RequestContext ctx){
	
		AdqBajasBean adqclas = new AdqBajasBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		adqclas.setAcum(false);
		adqclas.setMeses(meses);
		adqclas.setAnio(calendar.get(Calendar.YEAR));
		adqclas.setAnios(sessionScopeUser.getAnios());
		adqclas.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		adqclas.setCalculo(new BajasCalculoBean());
		adqclas.setClase(new ArrayList<String>());
		adqclas.setClases(new ArrayList<BajasClaseBean>());
		adqclas.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		adqclas.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		adqclas.setTextos(Arrays.asList(txts));
		adqclas.setTexto("TODOS");
		adqclas.setAjuste(new BajasAjustesBean());
		adqclas.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		adqclas.setRegDisabled(true);
		adqclas.setClaseDisabled(true);
		adqclas.setTxtDisabled(true);
		adqclas.setQueryDisabled(true);
		adqclas.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		adqclas.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void computeYearVal(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		if(adqclas.getMesesselect() == null || adqclas.getMesesselect().length == 0){
			adqclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		adqclas.setClases(utileriasCfeService.getClases(adqclas,sessionScopeUser));
		adqclas.setTxtsDesc(utileriasCfeService.getTxt(adqclas,sessionScopeUser));
		if(adqclas.getCalculo().getId() != 0){
			adqclas.setRegDisabled(false);
			adqclas.setClaseDisabled(false);
			adqclas.setTxtDisabled(false);
		}else{
			adqclas.setRegDisabled(true);
			adqclas.setClaseDisabled(true);
			adqclas.setTxtDisabled(true);
		}
		if(adqclas.getMesesselect() == null || adqclas.getMesesselect().length == 0){
			adqclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void computeRegDependants(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		adqclas.setClases(utileriasCfeService.getClases(adqclas,sessionScopeUser));
		adqclas.setTxtsDesc(utileriasCfeService.getTxt(adqclas,sessionScopeUser));
		if(adqclas.getCalculo().getId() != 0){
			adqclas.setRegDisabled(false);
			adqclas.setClaseDisabled(false);
			adqclas.setTxtDisabled(false);
		}else{
			adqclas.setRegDisabled(true);
			adqclas.setClaseDisabled(true);
			adqclas.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void computeClassDependants(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		adqclas.setTxtsDesc(utileriasCfeService.getTxt(adqclas,sessionScopeUser));
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void updateTxt(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		adqclas.setTxtsDesc(utileriasCfeService.getTxt(adqclas,sessionScopeUser));
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		if(adqclas.isAcum()){
			if(adqclas.getMesesselect() != null && adqclas.getMesesselect().length != 0){
				adqclas.setMesesselect(utileriasCfeService.getAcumMonths(adqclas.getMesesselect()));
			}else{
				adqclas.setMesesselect(new String[]{});
			}
		}else{
			adqclas.setMesesselect(new String []{});
		}
		
		if(adqclas.getMesesselect() == null || adqclas.getMesesselect().length == 0){
			adqclas.setQueryDisabled(true);
		}else{
			adqclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		if(adqclas.isAcum()){
		adqclas.setMesesselect(utileriasCfeService.getAcumMonths(adqclas.getMesesselect()));
		}
		if(adqclas.getMesesselect() == null || adqclas.getMesesselect().length == 0){
			adqclas.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			adqclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqclas", adqclas);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		List<TotalBean> totReporteMes = null;
		
		adqclas.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(adqclas));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+adqclas.getMesesselect());//ene, feb
		logger.info("mes size: " + adqclas.getMesesselect().length);
		if(adqclas.getMesesselect().length > 0){
			for(int i = 0; i < adqclas.getMesesselect().length; i++){
				logger.info(adqclas.getMesesselect()[i]);
			}
		}
		logger.info("año"+adqclas.getAnio());
		logger.info("calculo"+adqclas.getCalculo().getId());//id
		logger.info("region"+adqclas.getRegion());//id
		logger.info("clase"+adqclas.getClase());//cve
		logger.info("textos"+adqclas.getTexto());//todos
		logger.info("textoSeleccion"+adqclas.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		adqclas = adqBajasService.DatosWhere(adqclas);
		ajuste.setDescripcion("SIN AJUSTES");
		
		adqclas.setTitReptxt("CLASE");
		char a = Integer.toString(adqclas.getAnio()).charAt(2); 
		char aa = Integer.toString(adqclas.getAnio()).charAt(3); 
		String an = a+""+aa;
		adqclas.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (adqclas.getAjuste() != null && adqclas.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(adqclas.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			adqclas = adqBajasService.ConsultaClas(adqclas, sessionScopeUser);
		}
		if (adqclas.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(adqclas.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			adqclas = adqBajasService.ConsultaClas(adqclas, sessionScopeUser);
			adqclas = adqBajasService.ConsultaClasAjus(adqclas, sessionScopeUser);
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((adqclas.getTotReporteMesGeneral().getEnero()).subtract(adqclas.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((adqclas.getTotReporteMesGeneral().getFebrero()).subtract(adqclas.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((adqclas.getTotReporteMesGeneral().getMarzo()).subtract(adqclas.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((adqclas.getTotReporteMesGeneral().getAbril()).subtract(adqclas.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((adqclas.getTotReporteMesGeneral().getMayo()).subtract(adqclas.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((adqclas.getTotReporteMesGeneral().getJunio()).subtract(adqclas.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((adqclas.getTotReporteMesGeneral().getJulio()).subtract(adqclas.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((adqclas.getTotReporteMesGeneral().getAgosto()).subtract(adqclas.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((adqclas.getTotReporteMesGeneral().getSeptiembre()).subtract(adqclas.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((adqclas.getTotReporteMesGeneral().getOctubre()).subtract(adqclas.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((adqclas.getTotReporteMesGeneral().getNoviembre()).subtract(adqclas.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((adqclas.getTotReporteMesGeneral().getDiciembre()).subtract(adqclas.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((adqclas.getTotReporteMesGeneral().getTotal()).subtract(adqclas.getTotReporteMesAjusteGeneral().getTotal()));
			adqclas.setTotReporteMesAjusteGran(gTotAjustado);
			adqclas.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			adqclas.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (adqclas.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(adqclas.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			adqclas = adqBajasService.ConsultaClasNetos(adqclas, sessionScopeUser);
		}

		adqclas.setAjuste(ajuste);
		adqclas.setStrAnio(adqclas.getAnio().toString().substring(2, 4));
		
		ctx.getFlowScope().put("adqclas", adqclas);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		AdqBajasBean adqclas = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_ADQ_CLAS_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = adqBajasService.generaDocumentoClas(adqclas);
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
			AdqBajasBean datos = (AdqBajasBean)ctx.getFlowScope().get("adqclas");
	 		
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
			if (datos.getListRepAdqClas().isEmpty())
				datos = utileriasService.listCeroAdq(datos, "aqdClas");
			Map<String, Object> jParams = new HashMap<String,Object>();
			jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
			jParams.put("titulo2", Constants.TIT_REP_BAJ_CLASE);
			jParams.put("tipoCalc", datos.getCalculotxt());
			if(datos.isAcum()){
				jParams.put("mes", datos.getMesSeleccion());
			}else{
				jParams.put("mes", datos.getMesSeleccion());
				jParams.put("anio", "AÑO: "+datos.getAnio());
				
			}
			jParams.put("anio2", datos.getAnio());
			jParams.put("clase", "CLASE: "+datos.getClaseReptxt());
			jParams.put("region", "REGION: "+datos.getRegionReptxt());
			jParams.put("texto", datos.getTextosTitulos());
			jParams.put("ajuste", "AJUSTES: "+datos.getAjuste().getDescripcion());
			jParams.put("basicParam", datos.getListRepAdqClas());
			jParams.put("AjustesParam", datos.getListRepAjAdqClas());
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
			
			datasource = new JRBeanCollectionDataSource(datos.getListRepAdqClas(), true);
	 		
			StringBuffer fullPathRutaArch = new StringBuffer();
			fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
			fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
			fullPathRutaArch.append("bajasClase.jasper");//nombre del archivo
			
			
			File f = new File(fullPathRutaArch.toString());
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);

			
				 httpServletResponse = 
							(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
					 httpServletResponse.setContentType("application/pdf");
					 httpServletResponse.setHeader("Content-disposition", Constants.REP_ADQ_CLAS_FILENAME + Constants.REP_PDF_EXT);
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
