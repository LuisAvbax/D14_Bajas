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
@Service("adqRegAct")
@ViewScoped
public class AdqBajasRegAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(AdqBajasRegAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private AqdBajasService adqBajasService;

		
	public void initFlow(RequestContext ctx){
	
		AdqBajasBean adqreg = new AdqBajasBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		adqreg.setAcum(false);
		adqreg.setMeses(meses);
		adqreg.setAnio(calendar.get(Calendar.YEAR));
		adqreg.setAnios(sessionScopeUser.getAnios());
		adqreg.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		adqreg.setCalculo(new BajasCalculoBean());
		adqreg.setClase(new ArrayList<String>());
		adqreg.setClases(new ArrayList<BajasClaseBean>());
		adqreg.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		adqreg.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		adqreg.setTextos(Arrays.asList(txts));
		adqreg.setTexto("TODOS");
//		adqreg.setTxtDesc(new ArrayList<String>());
		adqreg.setAjuste(new BajasAjustesBean());
		adqreg.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		adqreg.setRegDisabled(true);
		adqreg.setClaseDisabled(true);
		adqreg.setTxtDisabled(true);
		adqreg.setQueryDisabled(true);
		adqreg.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		adqreg.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void computeYearVal(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		if(adqreg.getMesesselect() == null || adqreg.getMesesselect().length == 0){
			adqreg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqreg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		adqreg.setClases(utileriasCfeService.getClases(adqreg,sessionScopeUser));
		adqreg.setTxtsDesc(utileriasCfeService.getTxt(adqreg,sessionScopeUser));
		if(adqreg.getCalculo().getId() != 0){
			adqreg.setRegDisabled(false);
			adqreg.setClaseDisabled(false);
			adqreg.setTxtDisabled(false);
		}else{
			adqreg.setRegDisabled(true);
			adqreg.setClaseDisabled(true);
			adqreg.setTxtDisabled(true);
		}
		if(adqreg.getMesesselect() == null || adqreg.getMesesselect().length == 0){
			adqreg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqreg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void computeRegDependants(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		adqreg.setClases(utileriasCfeService.getClases(adqreg,sessionScopeUser));
		adqreg.setTxtsDesc(utileriasCfeService.getTxt(adqreg,sessionScopeUser));
		if(adqreg.getCalculo().getId() != 0){
			adqreg.setRegDisabled(false);
			adqreg.setClaseDisabled(false);
			adqreg.setTxtDisabled(false);
		}else{
			adqreg.setRegDisabled(true);
			adqreg.setClaseDisabled(true);
			adqreg.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void computeClassDependants(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		adqreg.setTxtsDesc(utileriasCfeService.getTxt(adqreg,sessionScopeUser));
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void updateTxt(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		adqreg.setTxtsDesc(utileriasCfeService.getTxt(adqreg,sessionScopeUser));
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		if(adqreg.isAcum()){
			if(adqreg.getMesesselect() != null && adqreg.getMesesselect().length != 0){
				adqreg.setMesesselect(utileriasCfeService.getAcumMonths(adqreg.getMesesselect()));
			}else{
				adqreg.setMesesselect(new String[]{});
			}
		}else{
			adqreg.setMesesselect(new String []{});
		}
		
		if(adqreg.getMesesselect() == null || adqreg.getMesesselect().length == 0){
			adqreg.setQueryDisabled(true);
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			adqreg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		if(adqreg.isAcum()){
		adqreg.setMesesselect(utileriasCfeService.getAcumMonths(adqreg.getMesesselect()));
		}
		if(adqreg.getMesesselect() == null || adqreg.getMesesselect().length == 0){
			adqreg.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			adqreg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("adqreg", adqreg);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		List<TotalBean> totReporteMes = null;
		
		adqreg.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(adqreg));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+adqreg.getMesesselect());//ene, feb
		logger.info("mes size: " + adqreg.getMesesselect().length);
		if(adqreg.getMesesselect().length > 0){
			for(int i = 0; i < adqreg.getMesesselect().length; i++){
				logger.info(adqreg.getMesesselect()[i]);
			}
		}
		logger.info("año"+adqreg.getAnio());
		logger.info("calculo"+adqreg.getCalculo().getId());//id
		logger.info("region"+adqreg.getRegion());//id
		logger.info("clase"+adqreg.getClase());//cve
		logger.info("textos"+adqreg.getTexto());//todos
		logger.info("textoSeleccion"+adqreg.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		adqreg = adqBajasService.DatosWhere(adqreg);
		
		char a = Integer.toString(adqreg.getAnio()).charAt(2); 
		char aa = Integer.toString(adqreg.getAnio()).charAt(3); 
		String an = a+""+aa;
		adqreg.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (adqreg.getAjuste() != null && adqreg.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(adqreg.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			adqreg = adqBajasService.ConsultaReg(adqreg, sessionScopeUser);
		}
		if (adqreg.getAjuste() != null && adqreg.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(adqreg.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			adqreg = adqBajasService.ConsultaReg(adqreg, sessionScopeUser);
			adqreg = adqBajasService.ConsultaRegAjus(adqreg, sessionScopeUser);
			
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero(((adqreg.getTotReporteMesGeneral().getEnero())).subtract((adqreg.getTotReporteMesAjusteGeneral().getEnero())) );
			gTotAjustado.setFebrero(((adqreg.getTotReporteMesGeneral().getFebrero())).subtract((adqreg.getTotReporteMesAjusteGeneral().getFebrero())));
			gTotAjustado.setMarzo(((adqreg.getTotReporteMesGeneral().getMarzo())).subtract((adqreg.getTotReporteMesAjusteGeneral().getMarzo())));
			gTotAjustado.setAbril(((adqreg.getTotReporteMesGeneral().getAbril())).subtract((adqreg.getTotReporteMesAjusteGeneral().getAbril())));
			gTotAjustado.setMayo(((adqreg.getTotReporteMesGeneral().getMayo())).subtract((adqreg.getTotReporteMesAjusteGeneral().getMayo())));
			gTotAjustado.setJunio(((adqreg.getTotReporteMesGeneral().getJunio())).subtract((adqreg.getTotReporteMesAjusteGeneral().getJunio())));
			gTotAjustado.setJulio(((adqreg.getTotReporteMesGeneral().getJulio())).subtract((adqreg.getTotReporteMesAjusteGeneral().getJulio())));
			gTotAjustado.setAgosto(((adqreg.getTotReporteMesGeneral().getAgosto())).subtract((adqreg.getTotReporteMesAjusteGeneral().getAgosto())));
			gTotAjustado.setSeptiembre(((adqreg.getTotReporteMesGeneral().getSeptiembre())).subtract((adqreg.getTotReporteMesAjusteGeneral().getSeptiembre())));
			gTotAjustado.setOctubre(((adqreg.getTotReporteMesGeneral().getOctubre())).subtract((adqreg.getTotReporteMesAjusteGeneral().getOctubre())));
			gTotAjustado.setNoviembre(((adqreg.getTotReporteMesGeneral().getNoviembre())).subtract((adqreg.getTotReporteMesAjusteGeneral().getNoviembre())));
			gTotAjustado.setDiciembre(((adqreg.getTotReporteMesGeneral().getDiciembre())).subtract((adqreg.getTotReporteMesAjusteGeneral().getDiciembre())));
			gTotAjustado.setTotal(((adqreg.getTotReporteMesGeneral().getTotal())).subtract((adqreg.getTotReporteMesAjusteGeneral().getTotal())));
			adqreg.setTotReporteMesAjusteGran(gTotAjustado);
			adqreg.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			adqreg.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (adqreg.getAjuste() != null && adqreg.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(adqreg.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			adqreg = adqBajasService.ConsultaRegNetos(adqreg, sessionScopeUser);
		}
			
		adqreg.setAjuste(ajuste);
		adqreg.setStrAnio(adqreg.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("adqreg", adqreg);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		AdqBajasBean adqreg = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_ADQ_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = adqBajasService.generaDocumento(adqreg);
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
				AdqBajasBean datos = (AdqBajasBean)ctx.getFlowScope().get("adqreg");
		 		
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
				
				if (datos.getListRepAdqReg().isEmpty())
					datos = utileriasService.listCeroAdq(datos, "aqdReg");
				Map<String, Object> jParams = new HashMap<String,Object>();
				jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
				jParams.put("titulo2", Constants.TIT_REP_BAJ_REGION);
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
				jParams.put("basicParam", datos.getListRepAdqReg());
				jParams.put("AjustesParam", datos.getListRepAjAdqReg());
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
				
				
				
				datasource = new JRBeanCollectionDataSource(datos.getListRepAdqReg(), true);
		 		
				StringBuffer fullPathRutaArch = new StringBuffer();
				fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
				fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
				fullPathRutaArch.append("bajasRegion.jasper");//nombre del archivo
				
				File f = new File(fullPathRutaArch.toString());
				
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
					httpServletResponse = 
							(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
					 httpServletResponse.setContentType("application/pdf");
					 httpServletResponse.setHeader("Content-disposition", Constants.REP_ADQ_REG_FILENAME + Constants.REP_PDF_EXT);
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
