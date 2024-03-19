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
import com.telcel.gsa.dsaf.service.CostoService;
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
@Service("costclasAct")
@ViewScoped
public class CostoClasAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(CostoClasAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private CostoService costoService;
	

		
	public void initFlow(RequestContext ctx){
	
		CostoBean costclas = new CostoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		costclas.setAcum(false);
		costclas.setMeses(meses);
		costclas.setAnio(calendar.get(Calendar.YEAR));
		costclas.setAnios(sessionScopeUser.getAnios());
		costclas.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		costclas.setCalculo(new BajasCalculoBean());
		costclas.setClase(new ArrayList<String>());
		costclas.setClases(new ArrayList<BajasClaseBean>());
		costclas.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		costclas.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		costclas.setTextos(Arrays.asList(txts));
		costclas.setTexto("TODOS");
		costclas.setAjuste(new BajasAjustesBean());
		costclas.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		costclas.setRegDisabled(true);
		costclas.setClaseDisabled(true);
		costclas.setTxtDisabled(true);
		costclas.setQueryDisabled(true);
		costclas.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		costclas.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void computeYearVal(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		if(costclas.getMesesselect() == null || costclas.getMesesselect().length == 0){
			costclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		costclas.setClases(utileriasCfeService.getClasesCosto(costclas,sessionScopeUser));
		costclas.setTxtsDesc(utileriasCfeService.getTxtCosto(costclas,sessionScopeUser));
		if(costclas.getCalculo().getId() != 0){
			costclas.setRegDisabled(false);
			costclas.setClaseDisabled(false);
			costclas.setTxtDisabled(false);
		}else{
			costclas.setRegDisabled(true);
			costclas.setClaseDisabled(true);
			costclas.setTxtDisabled(true);
		}
		if(costclas.getMesesselect() == null || costclas.getMesesselect().length == 0){
			costclas.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void computeRegDependants(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		costclas.setClases(utileriasCfeService.getClasesCosto(costclas,sessionScopeUser));
		costclas.setTxtsDesc(utileriasCfeService.getTxtCosto(costclas,sessionScopeUser));
		if(costclas.getCalculo().getId() != 0){
			costclas.setRegDisabled(false);
			costclas.setClaseDisabled(false);
			costclas.setTxtDisabled(false);
		}else{
			costclas.setRegDisabled(true);
			costclas.setClaseDisabled(true);
			costclas.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void computeClassDependants(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		costclas.setTxtsDesc(utileriasCfeService.getTxtCosto(costclas,sessionScopeUser));
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void updateTxt(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		costclas.setTxtsDesc(utileriasCfeService.getTxtCosto(costclas,sessionScopeUser));
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		if(costclas.isAcum()){
			if(costclas.getMesesselect() != null && costclas.getMesesselect().length != 0){
				costclas.setMesesselect(utileriasCfeService.getAcumMonths(costclas.getMesesselect()));
			}else{
				costclas.setMesesselect(new String[]{});
			}
		}else{
			costclas.setMesesselect(new String []{});
		}
		
		if(costclas.getMesesselect() == null || costclas.getMesesselect().length == 0){
			costclas.setQueryDisabled(true);
		}else{
			costclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		if(costclas.isAcum()){
			costclas.setMesesselect(utileriasCfeService.getAcumMonths(costclas.getMesesselect()));
		}
		if(costclas.getMesesselect() == null || costclas.getMesesselect().length == 0){
			costclas.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			costclas.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costclas", costclas);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		List<TotalBean> totReporteMes = null;
		
		costclas.setMesSeleccion (utileriasCfeService.txtperiodoConsultaCosto(costclas));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+costclas.getMesesselect());//ene, feb
		logger.info("mes size: " + costclas.getMesesselect().length);
		if(costclas.getMesesselect().length > 0){
			for(int i = 0; i < costclas.getMesesselect().length; i++){
				logger.info(costclas.getMesesselect()[i]);
			}
		}
		logger.info("año"+costclas.getAnio());
		logger.info("calculo"+costclas.getCalculo().getId());//id
		logger.info("region"+costclas.getRegion());//id
		logger.info("clase"+costclas.getClase());//cve
		logger.info("textos"+costclas.getTexto());//todos
		logger.info("textoSeleccion"+costclas.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		costclas = costoService.DatosWhere(costclas);
		ajuste.setDescripcion("SIN AJUSTES");
		
		costclas.setTitReptxt("CLASE");
		
		char a = Integer.toString(costclas.getAnio()).charAt(2); 
		char aa = Integer.toString(costclas.getAnio()).charAt(3); 
		String an = a+""+aa;
		costclas.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (costclas.getAjuste() != null && costclas.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(costclas.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			costclas = costoService.ConsultaClas(costclas, sessionScopeUser);
		}
		if (costclas.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(costclas.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			costclas = costoService.ConsultaClas(costclas, sessionScopeUser);
			costclas = costoService.ConsultaClasAjus(costclas, sessionScopeUser);
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((costclas.getTotReporteMesGeneral().getEnero()).subtract(costclas.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((costclas.getTotReporteMesGeneral().getFebrero()).subtract(costclas.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((costclas.getTotReporteMesGeneral().getMarzo()).subtract(costclas.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((costclas.getTotReporteMesGeneral().getAbril()).subtract(costclas.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((costclas.getTotReporteMesGeneral().getMayo()).subtract(costclas.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((costclas.getTotReporteMesGeneral().getJunio()).subtract(costclas.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((costclas.getTotReporteMesGeneral().getJulio()).subtract(costclas.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((costclas.getTotReporteMesGeneral().getAgosto()).subtract(costclas.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((costclas.getTotReporteMesGeneral().getSeptiembre()).subtract(costclas.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((costclas.getTotReporteMesGeneral().getOctubre()).subtract(costclas.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((costclas.getTotReporteMesGeneral().getNoviembre()).subtract(costclas.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((costclas.getTotReporteMesGeneral().getDiciembre()).subtract(costclas.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((costclas.getTotReporteMesGeneral().getTotal()).subtract(costclas.getTotReporteMesAjusteGeneral().getTotal()));
			costclas.setTotReporteMesAjusteGran(gTotAjustado);
			costclas.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			costclas.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (costclas.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(costclas.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			costclas = costoService.ConsultaClasNetos(costclas, sessionScopeUser);
		}

		costclas.setAjuste(ajuste);
		costclas.setStrAnio(costclas.getAnio().toString().substring(2, 4));
		
		
		ctx.getFlowScope().put("costclas", costclas);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		CostoBean costclas = (CostoBean)ctx.getFlowScope().get("costclas");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_COST_CLAS_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = costoService.generaDocumentoClas(costclas);
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
		CostoBean datos = (CostoBean)ctx.getFlowScope().get("costclas");
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
			datos = utileriasService.listCeroCosto(datos, "aqdClas");
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_COSTO_CLASE);
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
		jParams.put("leyendaAjuste","AJUSTES COSTO ACTUALIZADO");
		jParams.put("ajustSoli",datos.getAjuste().getClave());
		jParams.put("anio2", Integer.toString(datos.getAnio()).substring(2));
		datasource = new JRBeanCollectionDataSource(datos.getListRepAdqClas(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("costoClas1.jasper");//nombre del archivo
		File f = new File(fullPathRutaArch.toString());
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			 httpServletResponse = 
						(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				 httpServletResponse.setContentType("application/pdf");
				 httpServletResponse.setHeader("Content-disposition", Constants.REP_COST_CLAS_FILENAME + Constants.REP_PDF_EXT);
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
