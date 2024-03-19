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
@Service("depreAnioActReg")
@ViewScoped
public class DepreAnioActRegAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(DepreAnioActRegAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private DepreAnioActService depreAnioActService;
	@Autowired
	UtileriasService utileriasService;

		
	public void initFlow(RequestContext ctx){
	
		DepreActBean depreReg = new DepreActBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		depreReg.setAcum(false);
		depreReg.setMeses(meses);
		depreReg.setAnio(calendar.get(Calendar.YEAR));
		depreReg.setAnios(sessionScopeUser.getAnios());
		depreReg.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		depreReg.setCalculo(new BajasCalculoBean());
		depreReg.setClase(new ArrayList<String>());
		depreReg.setClases(new ArrayList<BajasClaseBean>());
		depreReg.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		depreReg.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		depreReg.setTextos(Arrays.asList(txts));
		depreReg.setTexto("TODOS");

		depreReg.setAjuste(new BajasAjustesBean());
		depreReg.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		depreReg.setRegDisabled(true);
		depreReg.setClaseDisabled(true);
		depreReg.setTxtDisabled(true);
		depreReg.setQueryDisabled(true);
		depreReg.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		depreReg.setSociedad(sessionScopeUser.getSociedad());
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void computeYearVal(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		if(depreReg.getMesesselect() == null || depreReg.getMesesselect().length == 0){
			depreReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			depreReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		depreReg.setClases(utileriasCfeService.getClases(depreReg,sessionScopeUser));
		depreReg.setTxtsDesc(utileriasCfeService.getTxt(depreReg,sessionScopeUser));
		
		if(depreReg.getCalculo().getId() != 0){
			depreReg.setRegDisabled(false);
			depreReg.setClaseDisabled(false);
			depreReg.setTxtDisabled(false);
		}else{
			depreReg.setRegDisabled(true);
			depreReg.setClaseDisabled(true);
			depreReg.setTxtDisabled(true);
		}
		if(depreReg.getMesesselect() == null || depreReg.getMesesselect().length == 0){
			depreReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			depreReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void computeRegDependants(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		depreReg.setClases(utileriasCfeService.getClases(depreReg,sessionScopeUser));
		depreReg.setTxtsDesc(utileriasCfeService.getTxt(depreReg,sessionScopeUser));
		if(depreReg.getCalculo().getId() != 0){
			depreReg.setRegDisabled(false);
			depreReg.setClaseDisabled(false);
			depreReg.setTxtDisabled(false);
		}else{
			depreReg.setRegDisabled(true);
			depreReg.setClaseDisabled(true);
			depreReg.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void computeClassDependants(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		depreReg.setTxtsDesc(utileriasCfeService.getTxt(depreReg,sessionScopeUser));
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void updateTxt(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		depreReg.setTxtsDesc(utileriasCfeService.getTxt(depreReg,sessionScopeUser));
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		if(depreReg.isAcum()){
			if(depreReg.getMesesselect() != null && depreReg.getMesesselect().length != 0){
				depreReg.setMesesselect(utileriasCfeService.getAcumMonths(depreReg.getMesesselect()));
			}else{
				depreReg.setMesesselect(new String[]{});
			}
		}else{
			depreReg.setMesesselect(new String []{});
		}
		
		if(depreReg.getMesesselect() == null || depreReg.getMesesselect().length == 0){
			depreReg.setQueryDisabled(true);
		}else{
			depreReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	public void validaAcumulado(RequestContext ctx){
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		if(depreReg.isAcum()){
		depreReg.setMesesselect(utileriasCfeService.getAcumMonths(depreReg.getMesesselect()));
		}
		if(depreReg.getMesesselect() == null || depreReg.getMesesselect().length == 0){
			depreReg.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			depreReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("depreReg", depreReg);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		List<TotalBean> totReporteMes = null;
		
		depreReg.setMesSeleccion (utileriasCfeService.txtperiodoConsulta(depreReg.isAcum(),depreReg.getMesesselect(),depreReg.getAnio()));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+depreReg.getMesesselect());//ene, feb
		logger.info("mes size: " + depreReg.getMesesselect().length);
		if(depreReg.getMesesselect().length > 0){
			for(int i = 0; i < depreReg.getMesesselect().length; i++){
				logger.info(depreReg.getMesesselect()[i]);
			}
		}
		logger.info("año"+depreReg.getAnio());
		logger.info("calculo"+depreReg.getCalculo().getId());//id
		logger.info("region"+depreReg.getRegion());//id
		logger.info("clase"+depreReg.getClase());//cve
		logger.info("textos"+depreReg.getTexto());//todos
		logger.info("textoSeleccion"+depreReg.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		depreReg = depreAnioActService.datosWhere(depreReg);

		
		
		char a = Integer.toString(depreReg.getAnio()).charAt(2); 
		char aa = Integer.toString(depreReg.getAnio()).charAt(3); 
		String an = a+""+aa;
		depreReg.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (depreReg.getAjuste() != null && depreReg.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(depreReg.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			depreReg = depreAnioActService.consultaReg(depreReg, sessionScopeUser);
			
		}
		if (depreReg.getAjuste() != null && depreReg.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(depreReg.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			depreReg = depreAnioActService.consultaReg(depreReg, sessionScopeUser);
			depreReg = depreAnioActService.consultaRegAjus(depreReg,sessionScopeUser);
			
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((depreReg.getTotReporteMesGeneral().getEnero()).subtract(depreReg.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((depreReg.getTotReporteMesGeneral().getFebrero()).subtract(depreReg.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((depreReg.getTotReporteMesGeneral().getMarzo()).subtract(depreReg.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((depreReg.getTotReporteMesGeneral().getAbril()).subtract(depreReg.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((depreReg.getTotReporteMesGeneral().getMayo()).subtract(depreReg.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((depreReg.getTotReporteMesGeneral().getJunio()).subtract(depreReg.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((depreReg.getTotReporteMesGeneral().getJulio()).subtract(depreReg.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((depreReg.getTotReporteMesGeneral().getAgosto()).subtract(depreReg.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((depreReg.getTotReporteMesGeneral().getSeptiembre()).subtract(depreReg.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((depreReg.getTotReporteMesGeneral().getOctubre()).subtract(depreReg.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((depreReg.getTotReporteMesGeneral().getNoviembre()).subtract(depreReg.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((depreReg.getTotReporteMesGeneral().getDiciembre()).subtract(depreReg.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((depreReg.getTotReporteMesGeneral().getTotal()).subtract(depreReg.getTotReporteMesAjusteGeneral().getTotal()));
			depreReg.setTotReporteMesAjusteGran(gTotAjustado);
			depreReg.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			depreReg.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (depreReg.getAjuste() != null && depreReg.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(depreReg.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			depreReg = depreAnioActService.consultaRegNetos(depreReg, sessionScopeUser);
			
		}
			
		depreReg.setAjuste(ajuste);
		depreReg.setStrAnio(depreReg.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("depreReg", depreReg);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		DepreActBean depreReg = (DepreActBean)ctx.getFlowScope().get("depreReg");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_DPR_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = depreAnioActService.generaDocumento(depreReg);
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
		DepreActBean datos = (DepreActBean)ctx.getFlowScope().get("depreReg");
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
		if (datos.getListRepDepreReg().isEmpty())
			datos = utileriasService.listCeroDepre(datos, "aqdReg");
		Map<String, Object> jParams = new HashMap<String,Object>();
		jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
		jParams.put("titulo2", Constants.TIT_REP_DPR_REGION);
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
		jParams.put("basicParam", datos.getListRepDepreReg());
		jParams.put("AjustesParam", datos.getListRepAjDepreReg());
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
		
		datasource = new JRBeanCollectionDataSource(datos.getListRepDepreReg(), true);
 		
		StringBuffer fullPathRutaArch = new StringBuffer();
		fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
		fullPathRutaArch.append("depreRegion.jasper");//nombre del archivo
	
		File f = new File(fullPathRutaArch.toString());
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
			httpServletResponse = 
					(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			 httpServletResponse.setContentType("application/pdf");
			 httpServletResponse.setHeader("Content-disposition", Constants.REP_DPR_REG_FILENAME + Constants.REP_PDF_EXT);
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
