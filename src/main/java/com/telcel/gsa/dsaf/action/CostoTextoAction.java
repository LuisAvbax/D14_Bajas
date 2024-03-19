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
@Service("costotxtAct")
@ViewScoped
public class CostoTextoAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(CostoTextoAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasService utileriasService;
	@Autowired
	private CostoService costoService;

		
	public void initFlow(RequestContext ctx){
	
		CostoBean costotxt = new CostoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		costotxt.setAcum(false);
		costotxt.setMeses(meses);
		costotxt.setAnio(calendar.get(Calendar.YEAR));
		costotxt.setAnios(sessionScopeUser.getAnios());
		costotxt.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		costotxt.setCalculo(new BajasCalculoBean());
		costotxt.setClase(new ArrayList<String>());
		costotxt.setClases(new ArrayList<BajasClaseBean>());
		costotxt.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		costotxt.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		costotxt.setTextos(Arrays.asList(txts));
		costotxt.setTexto("TODOS");
		costotxt.setAjuste(new BajasAjustesBean());
		costotxt.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		costotxt.setRegDisabled(true);
		costotxt.setClaseDisabled(true);
		costotxt.setTxtDisabled(true);
		costotxt.setQueryDisabled(true);
		costotxt.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		costotxt.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void computeYearVal(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		if(costotxt.getMesesselect() == null || costotxt.getMesesselect().length == 0){
			costotxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costotxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		costotxt.setClases(utileriasCfeService.getClasesCosto(costotxt,sessionScopeUser));
		costotxt.setTxtsDesc(utileriasCfeService.getTxtCosto(costotxt,sessionScopeUser));
		if(costotxt.getCalculo().getId() != 0){
			costotxt.setRegDisabled(false);
			costotxt.setClaseDisabled(false);
			costotxt.setTxtDisabled(false);
		}else{
			costotxt.setRegDisabled(true);
			costotxt.setClaseDisabled(true);
			costotxt.setTxtDisabled(true);
		}
		if(costotxt.getMesesselect() == null || costotxt.getMesesselect().length == 0){
			costotxt.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costotxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void computeRegDependants(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		costotxt.setClases(utileriasCfeService.getClasesCosto(costotxt,sessionScopeUser));
		costotxt.setTxtsDesc(utileriasCfeService.getTxtCosto(costotxt,sessionScopeUser));
		if(costotxt.getCalculo().getId() != 0){
			costotxt.setRegDisabled(false);
			costotxt.setClaseDisabled(false);
			costotxt.setTxtDisabled(false);
		}else{
			costotxt.setRegDisabled(true);
			costotxt.setClaseDisabled(true);
			costotxt.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void computeClassDependants(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		costotxt.setTxtsDesc(utileriasCfeService.getTxtCosto(costotxt,sessionScopeUser));
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void updateTxt(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		costotxt.setTxtsDesc(utileriasCfeService.getTxtCosto(costotxt,sessionScopeUser));
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		if(costotxt.isAcum()){
			if(costotxt.getMesesselect() != null && costotxt.getMesesselect().length != 0){
				costotxt.setMesesselect(utileriasCfeService.getAcumMonths(costotxt.getMesesselect()));
			}else{
				costotxt.setMesesselect(new String[]{});
			}
		}else{
			costotxt.setMesesselect(new String []{});
		}
		
		if(costotxt.getMesesselect() == null || costotxt.getMesesselect().length == 0){
			costotxt.setQueryDisabled(true);
		}else{
			costotxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		if(costotxt.isAcum()){
			costotxt.setMesesselect(utileriasCfeService.getAcumMonths(costotxt.getMesesselect()));
		}
		if(costotxt.getMesesselect() == null || costotxt.getMesesselect().length == 0){
			costotxt.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			costotxt.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costotxt", costotxt);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		List<TotalBean> totReporteMes = null;
		
		costotxt.setMesSeleccion (utileriasCfeService.txtperiodoConsultaCosto(costotxt));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+costotxt.getMesesselect());//ene, feb
		logger.info("mes size: " + costotxt.getMesesselect().length);
		if(costotxt.getMesesselect().length > 0){
			for(int i = 0; i < costotxt.getMesesselect().length; i++){
				logger.info(costotxt.getMesesselect()[i]);
			}
		}
		logger.info("año"+costotxt.getAnio());
		logger.info("calculo"+costotxt.getCalculo().getId());//id
		logger.info("region"+costotxt.getRegion());//id
		logger.info("clase"+costotxt.getClase());//cve
		logger.info("textos"+costotxt.getTexto());//todos
		logger.info("textoSeleccion"+costotxt.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		costotxt = costoService.DatosWhere(costotxt);
		ajuste.setDescripcion("SIN AJUSTES");
		
		costotxt.setTitReptxt("CLASE");
		
		char a = Integer.toString(costotxt.getAnio()).charAt(2); 
		char aa = Integer.toString(costotxt.getAnio()).charAt(3); 
		String an = a+""+aa;
		costotxt.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (costotxt.getAjuste() != null && costotxt.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(costotxt.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			costotxt =  costoService.ConsultaText(costotxt, sessionScopeUser);
		}
		if (costotxt.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(costotxt.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			costotxt = costoService.ConsultaText(costotxt, sessionScopeUser);
			costotxt = costoService.ConsultaTextAjus(costotxt, sessionScopeUser);
			
			
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((costotxt.getTotReporteMesGeneral().getEnero()).subtract(costotxt.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((costotxt.getTotReporteMesGeneral().getFebrero()).subtract(costotxt.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((costotxt.getTotReporteMesGeneral().getMarzo()).subtract(costotxt.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((costotxt.getTotReporteMesGeneral().getAbril()).subtract(costotxt.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((costotxt.getTotReporteMesGeneral().getMayo()).subtract(costotxt.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((costotxt.getTotReporteMesGeneral().getJunio()).subtract(costotxt.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((costotxt.getTotReporteMesGeneral().getJulio()).subtract(costotxt.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((costotxt.getTotReporteMesGeneral().getAgosto()).subtract(costotxt.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((costotxt.getTotReporteMesGeneral().getSeptiembre()).subtract(costotxt.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((costotxt.getTotReporteMesGeneral().getOctubre()).subtract(costotxt.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((costotxt.getTotReporteMesGeneral().getNoviembre()).subtract(costotxt.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((costotxt.getTotReporteMesGeneral().getDiciembre()).subtract(costotxt.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((costotxt.getTotReporteMesGeneral().getTotal()).subtract(costotxt.getTotReporteMesAjusteGeneral().getTotal()));
			costotxt.setTotReporteMesAjusteGran(gTotAjustado);
			costotxt.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			costotxt.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (costotxt.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(costotxt.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			costotxt = costoService.ConsultaTextNetos(costotxt, sessionScopeUser);
		}
			
		costotxt.setAjuste(ajuste);
		costotxt.setStrAnio(costotxt.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("costotxt", costotxt);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		CostoBean costotxt = (CostoBean)ctx.getFlowScope().get("costotxt");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_COST_TEXTO_FILENAME
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = costoService.generaDocumentoText(costotxt);
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
				CostoBean datos = (CostoBean)ctx.getFlowScope().get("costotxt");
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
					datos = utileriasService.listCeroCosto(datos, "aqdtxt");
				Map<String, Object> jParams = new HashMap<String,Object>();
				jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
				jParams.put("titulo2", Constants.TIT_REP_COSTO_TEXTO);
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
				jParams.put("leyendaAjuste","AJUSTES COSTO ACTUALIZADO");
				jParams.put("ajustSoli",datos.getAjuste().getClave());
				jParams.put("anio2", Integer.toString(datos.getAnio()).substring(2));
				datasource = new JRBeanCollectionDataSource(datos.getListRepAdqtxt(), true);
		 		
				StringBuffer fullPathRutaArch = new StringBuffer();
				fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
				fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
				fullPathRutaArch.append("costoTxt.jasper");//nombre del archivo
				
				
				File f = new File(fullPathRutaArch.toString());
				
				try {
					
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
					 httpServletResponse = 
								(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
						 httpServletResponse.setContentType("application/pdf");
						 httpServletResponse.setHeader("Content-disposition", Constants.REP_COST_TEXTO_FILENAME+ Constants.REP_PDF_EXT);
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
