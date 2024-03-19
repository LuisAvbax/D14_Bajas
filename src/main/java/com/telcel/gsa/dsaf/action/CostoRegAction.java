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
@Service("costoRegAct")
@ViewScoped
public class CostoRegAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(CostoRegAction.class);
	
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	@Autowired
	SessionScopeUser sessionScopeUser;
	@Autowired
	UtileriasService utileriasService;
	@Autowired
	CostoService costoService;

		
	public void initFlow(RequestContext ctx){
	
		CostoBean costReg = new CostoBean();
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String meses[] = {"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
		costReg.setAcum(false);
		costReg.setMeses(meses);
		costReg.setAnio(calendar.get(Calendar.YEAR));
		costReg.setAnios(sessionScopeUser.getAnios());
		costReg.setCalculos(utileriasCfeService.getCalculos(sessionScopeUser));
		costReg.setCalculo(new BajasCalculoBean());
		costReg.setClase(new ArrayList<String>());
		costReg.setClases(new ArrayList<BajasClaseBean>());
		costReg.setRegiones(utileriasCfeService.obtenerLstRegiones(sessionScopeUser));
		CatRegionBean region = new CatRegionBean();
		region.setId(sessionScopeUser.getUsuarioCfe().getBajasCatRegion());
		costReg.setRegion(new ArrayList<String>());
		List<String> params = new ArrayList<String>();
		params.add(Constants.FILTRO_TEXTOS);
		Map<String, BajasCatParametrosBean> parametros = utileriasService.obtieneParametros(params);
		String [] txts = parametros.get(Constants.FILTRO_TEXTOS).getValor().split("\\|");
		costReg.setTextos(Arrays.asList(txts));
		costReg.setTexto("TODOS");
		costReg.setAjuste(new BajasAjustesBean());
		costReg.setAjustes(utileriasCfeService.getAjustes(sessionScopeUser));
		costReg.setRegDisabled(true);
		costReg.setClaseDisabled(true);
		costReg.setTxtDisabled(true);
		costReg.setQueryDisabled(true);
		costReg.setDatSoc(sessionScopeUser.getSociedad().getNombre());
		costReg.setSociedad(sessionScopeUser.getSociedad().getNombre());
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void computeYearVal(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		if(costReg.getMesesselect() == null || costReg.getMesesselect().length == 0){
			costReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void computeCalcDependants(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		costReg.setClases(utileriasCfeService.getClasesCosto(costReg,sessionScopeUser));
		costReg.setTxtsDesc(utileriasCfeService.getTxtCosto(costReg,sessionScopeUser));
		if(costReg.getCalculo().getId() != 0){
			costReg.setRegDisabled(false);
			costReg.setClaseDisabled(false);
			costReg.setTxtDisabled(false);
		}else{
			costReg.setRegDisabled(true);
			costReg.setClaseDisabled(true);
			costReg.setTxtDisabled(true);
		}
		if(costReg.getMesesselect() == null || costReg.getMesesselect().length == 0){
			costReg.setQueryDisabled(true);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void computeRegDependants(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		costReg.setClases(utileriasCfeService.getClasesCosto(costReg,sessionScopeUser));
		costReg.setTxtsDesc(utileriasCfeService.getTxtCosto(costReg,sessionScopeUser));
		if(costReg.getCalculo().getId() != 0){
			costReg.setRegDisabled(false);
			costReg.setClaseDisabled(false);
			costReg.setTxtDisabled(false);
		}else{
			costReg.setRegDisabled(true);
			costReg.setClaseDisabled(true);
			costReg.setTxtDisabled(true);
		}
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void computeClassDependants(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		costReg.setTxtsDesc(utileriasCfeService.getTxtCosto(costReg,sessionScopeUser));
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void updateTxt(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		costReg.setTxtsDesc(utileriasCfeService.getTxtCosto(costReg,sessionScopeUser));
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	public void acumulaMesesAcum(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		if(costReg.isAcum()){
			if(costReg.getMesesselect() != null && costReg.getMesesselect().length != 0){
				costReg.setMesesselect(utileriasCfeService.getAcumMonths(costReg.getMesesselect()));
			}else{
				costReg.setMesesselect(new String[]{});
			}
		}else{
			costReg.setMesesselect(new String []{});
		}
		
		if(costReg.getMesesselect() == null || costReg.getMesesselect().length == 0){
			costReg.setQueryDisabled(true);
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
		}else{
			costReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	
	
	public void validaAcumulado(RequestContext ctx){
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		if(costReg.isAcum()){
			costReg.setMesesselect(utileriasCfeService.getAcumMonths(costReg.getMesesselect()));
		}
		if(costReg.getMesesselect() == null || costReg.getMesesselect().length == 0){
			costReg.setQueryDisabled(true);
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "DEBE SELECCIONAR AL MENOS UN MES"));
			
		}else{
			costReg.setQueryDisabled(false);
		}
		ctx.getFlowScope().put("costreg", costReg);
	}
	
	
	public void consulta(RequestContext ctx) throws CfeException, SQLException{
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		List<TotalBean> totReporteMes = null;
		
		costReg.setMesSeleccion (utileriasCfeService.txtperiodoConsultaCosto(costReg));
		
		logger.info(">>Entrando a consulta de reporte..");
		logger.info("mes"+costReg.getMesesselect());//ene, feb
		logger.info("mes size: " + costReg.getMesesselect().length);
		if(costReg.getMesesselect().length > 0){
			for(int i = 0; i < costReg.getMesesselect().length; i++){
				logger.info(costReg.getMesesselect()[i]);
			}
		}
		logger.info("año"+costReg.getAnio());
		logger.info("calculo"+costReg.getCalculo().getId());//id
		logger.info("region"+costReg.getRegion());//id
		logger.info("clase"+costReg.getClase());//cve
		logger.info("textos"+costReg.getTexto());//todos
		logger.info("textoSeleccion"+costReg.getTxtDesc());
		
		BajasAjustesBean ajuste = new BajasAjustesBean ();
		
		costReg = costoService.DatosWhere(costReg);

		
//		adqreg.setTitReptxt("REGION");
		
		char a = Integer.toString(costReg.getAnio()).charAt(2); 
		char aa = Integer.toString(costReg.getAnio()).charAt(3); 
		String an = a+""+aa;
		costReg.setAnioRepCorto(an);
		totReporteMes = new ArrayList<TotalBean>();
		
		if (costReg.getAjuste() != null && costReg.getAjuste().getClave().equals("NA"))
		{
			ajuste.setClave(costReg.getAjuste().getClave());
			ajuste.setDescripcion("NINGUNO");
			costReg = costoService.ConsultaReg(costReg, sessionScopeUser);
		}
		if (costReg.getAjuste() != null && costReg.getAjuste().getClave().equals("TA"))
		{
			
			ajuste.setClave(costReg.getAjuste().getClave());
			ajuste.setDescripcion("TODOS LOS AJUSTES");
			costReg = costoService.ConsultaReg(costReg, sessionScopeUser);
			costReg = costoService.ConsultaRegAjus(costReg, sessionScopeUser);
			//gran total ajustado
			TotalBean gTotAjustado = new TotalBean();
			gTotAjustado.setEnero((costReg.getTotReporteMesGeneral().getEnero()).subtract(costReg.getTotReporteMesAjusteGeneral().getEnero()) );
			gTotAjustado.setFebrero((costReg.getTotReporteMesGeneral().getFebrero()).subtract(costReg.getTotReporteMesAjusteGeneral().getFebrero()));
			gTotAjustado.setMarzo((costReg.getTotReporteMesGeneral().getMarzo()).subtract(costReg.getTotReporteMesAjusteGeneral().getMarzo()));
			gTotAjustado.setAbril((costReg.getTotReporteMesGeneral().getAbril()).subtract(costReg.getTotReporteMesAjusteGeneral().getAbril()));
			gTotAjustado.setMayo((costReg.getTotReporteMesGeneral().getMayo()).subtract(costReg.getTotReporteMesAjusteGeneral().getMayo()));
			gTotAjustado.setJunio((costReg.getTotReporteMesGeneral().getJunio()).subtract(costReg.getTotReporteMesAjusteGeneral().getJunio()));
			gTotAjustado.setJulio((costReg.getTotReporteMesGeneral().getJulio()).subtract(costReg.getTotReporteMesAjusteGeneral().getJulio()));
			gTotAjustado.setAgosto((costReg.getTotReporteMesGeneral().getAgosto()).subtract(costReg.getTotReporteMesAjusteGeneral().getAgosto()));
			gTotAjustado.setSeptiembre((costReg.getTotReporteMesGeneral().getSeptiembre()).subtract(costReg.getTotReporteMesAjusteGeneral().getSeptiembre()));
			gTotAjustado.setOctubre((costReg.getTotReporteMesGeneral().getOctubre()).subtract(costReg.getTotReporteMesAjusteGeneral().getOctubre()));
			gTotAjustado.setNoviembre((costReg.getTotReporteMesGeneral().getNoviembre()).subtract(costReg.getTotReporteMesAjusteGeneral().getNoviembre()));
			gTotAjustado.setDiciembre((costReg.getTotReporteMesGeneral().getDiciembre()).subtract(costReg.getTotReporteMesAjusteGeneral().getDiciembre()));
			gTotAjustado.setTotal((costReg.getTotReporteMesGeneral().getTotal()).subtract(costReg.getTotReporteMesAjusteGeneral().getTotal()));
			costReg.setTotReporteMesAjusteGran(gTotAjustado);
			costReg.setTotReporteMesAjusteGranLst(new ArrayList<TotalBean>());
			costReg.getTotReporteMesAjusteGranLst().add(gTotAjustado);
		}
		if (costReg.getAjuste() != null && costReg.getAjuste().getClave().equals("N"))
		{
			ajuste.setClave(costReg.getAjuste().getClave());
			ajuste.setDescripcion("NETOS");
			costReg = costoService.ConsultaRegNetos(costReg, sessionScopeUser);
		}
			
		costReg.setAjuste(ajuste);
		costReg.setStrAnio(costReg.getAnio().toString().substring(2, 4));
		ctx.getFlowScope().put("costreg", costReg);
		
		
	}	
	
	@SuppressWarnings("unchecked")
	public void descarga(RequestContext ctx){
		
		CostoBean costReg = (CostoBean)ctx.getFlowScope().get("costreg");
		
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_COST_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = costoService.generaDocumento(costReg);
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
			CostoBean datos = (CostoBean)ctx.getFlowScope().get("costreg");
	 		
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
				datos = utileriasService.listCeroCosto(datos, "aqdReg");
			Map<String, Object> jParams = new HashMap<String,Object>();
			jParams.put("titulo1", Constants.TIT_BAJAS_ACTIVO_FIJO);
			jParams.put("titulo2", Constants.TIT_REP_COSTO_REGION);
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
			jParams.put("leyendaAjuste","AJUSTES COSTO ACTUALIZADO");
			jParams.put("ajustSoli",datos.getAjuste().getClave());
			jParams.put("anio2", Integer.toString(datos.getAnio()).substring(2));
			datasource = new JRBeanCollectionDataSource(datos.getListRepAdqReg(), true);
	 		
			StringBuffer fullPathRutaArch = new StringBuffer();
			fullPathRutaArch.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
			fullPathRutaArch.append(File.separator + "css" + File.separator + "reportes" + File.separator);
			fullPathRutaArch.append("costoRegion.jasper");//nombre del archivo
			
			
			File f = new File(fullPathRutaArch.toString());
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
				 httpServletResponse = 
							(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
					 httpServletResponse.setContentType("application/pdf");
					 httpServletResponse.setHeader("Content-disposition", Constants.REP_COST_REG_FILENAME + Constants.REP_PDF_EXT);
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
