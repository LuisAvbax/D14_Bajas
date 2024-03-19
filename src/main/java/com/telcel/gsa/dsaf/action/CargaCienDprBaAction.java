package com.telcel.gsa.dsaf.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.ReporteCienDprBaService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("repCienDeprBaAct")
@ViewScoped
public class CargaCienDprBaAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(CargaCienDprBaAction.class);
	
	private UploadedFile uploadedFile;
	private Integer anio;
	private Integer mes;
	
	@Autowired
	public SessionScopeUser sessionScopeUser;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	
	@Autowired
	ReporteCienDprBaService cienDprService;
	
	public void initFlow(RequestContext ctx) {
		logger.info(Constants.APP_ID + "Comienza Pantalla");
		BajasDepreciadosCienBean datBean = new BajasDepreciadosCienBean();
		Date fActual = utileriasCfeService.obtenerFechaActual(sessionScopeUser);
		datBean.setFechaCarga(fActual);
		MesBean mes = new MesBean();
		datBean.setMes(mes);
		datBean.setGuardar(true);
		datBean.setMeses(sessionScopeUser.getMeses());
		datBean.setAnios(sessionScopeUser.getAnios());
		List<BajasDepreciadosCien> listOk = new ArrayList<BajasDepreciadosCien>();
		datBean.setListOK(listOk);
		ctx.getFlowScope().put("datCien", datBean);
	}
	
	public void cargaArchivoBlobCien(RequestContext ctx)throws ServletException, IOException{
		BajasDprCienFormBean dprBean = (BajasDprCienFormBean)ctx.getFlowScope().get("datDpr");
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_BITACORA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			HSSFWorkbook workbook = cienDprService.generaDocumento(dprBean.getListBean());
//			out = httpServletResponse.getOutputStream();
			workbook.write(bos);
			//BLOB

			//cienDprService.insertaArchivo(bos.toByteArray(),dprBean);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} catch (CfeException e) {
			// TODO Auto-generated catch block
			throw e;
		} 
		finally {
			bos.close();
		}
	}
	
	public void fileUploadListener(FileUploadEvent event){
		logger.info("comienza");
        uploadedFile=event.getFile();
        logger.info("Archivo: "+uploadedFile.getFileName());
        org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder.getRequestContext();
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
		anio = datos.getAnio();
        mes = datos.getMes().getId();
    }
	
	 public void insert(RequestContext ctx) throws EncryptedDocumentException, InvalidFormatException, IOException{
		 FacesContext context = FacesContext.getCurrentInstance();  
		 if(uploadedFile!=null){
	        	logger.info(Constants.APP_ID + "Metodo de carga de archivo.....");
	    		InputStream input = null;
	    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		try {
	    			Integer sociedad = sessionScopeUser.getSociedad().getId();
	    			Integer usuario = sessionScopeUser.getUsuarioCfe().getId();
	    			input = uploadedFile.getInputstream();
	    			Workbook workbook = WorkbookFactory.create(input); 
	    			workbook.write(bos);
	    			cienDprService.insertaArchivo(bos.toByteArray(),anio,mes,sociedad,usuario);
	    			workbook.close();
	    			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.ADVERTENCIA, "Cuando se termine de cargar tu archivo se te enviara un correo."));
	    			cienDprService.insertarRegistro(sociedad, usuario);
	    		} catch(CfeException e){
	    			
	    		} catch (SQLException e) {
	    			// TODO Auto-generated catch block
	    			throw new CfeException(e.getMessage(), e);
	    		} finally {
	    			bos.close();
	    		}
	            
	        }
	        else{
	            logger.info("El archivo esta nulo.");
	        }
	    }
	
	public void cargaArchivoExcel()throws ServletException, IOException{
		logger.info(Constants.APP_ID + "Metodo de carga de archivo.....");
		//BajasDprCienFormBean datos = (BajasDprCienFormBean)ctx.getFlowScope().get("datCien");
		InputStream input = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		logger.info("continua...");
		try {
			
			//input = uploadedFile.getInputstream();
			logger.info("Archivo: "+uploadedFile);
			logger.info("Input: "+input);
			//HSSFWorkbook workbook = new HSSFWorkbook(input);
			//cienDprService.insertaArchivo(bos.toByteArray(),datos);
			logger.info("continua...");
			//workbook.close();
		} catch(CfeException e){
			
		} finally {
			bos.close();
		}
		
	}
	

	
	public void descargaArchivo(RequestContext ctx)throws ServletException, IOException{
		OutputStream output = null;
		InputStream in = null;
		File fileName = null;
		
		ReporteDetalladoBean reporteDetallado = (ReporteDetalladoBean)ctx.getFlowScope().get("repoDetalle");
		HttpServletRequest request = null;
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_RES_CON_REG_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		try {
			List<BajasOpDocumentoBean> docBeanLst = cienDprService.consultaArchivo();
			/*httpServletResponse.reset();
			fileName = new File("C:/Reporte_Detallados.xls");
			in = new FileInputStream(fileName);
	    	BufferedInputStream bin = new BufferedInputStream(in);
	    	logger.info("res: "+(int) bin.available());
	    	
	    	httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setContentLength((int) fileName.length());
			String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=prueba.xls");
			httpServletResponse.addHeader(headerKey, headerValue);
			output = httpServletResponse.getOutputStream();
			IOUtils.copy(in, output);

	        byte[] buffer = new byte[185344];
	        int bytesRead = -1;
	        
	        while ((bytesRead = in.read()) != -1) {
	        	output.write(0);
            }
	        in.close();
	        output.flush();
	        output.close();
            

            System.out.println("File downloaded at client successfully");
			//output = httpServletResponse.getOutputStream();
			
			/*logger.info("Comienza"); 
			InputStream stream = docBeanLst.get(0).getContenido();
			/*byte[] bull = null;
			bull = new byte[stream.available()];
			ByteArrayInputStream byteArr = new ByteArrayInputStream(bull);
			BufferedInputStream is = new BufferedInputStream(stream);
			String name = docBeanLst.get(0).getNombre();
			int fileLenght = stream.available();
			File file = new File("C:/Reporte_Detallado.xls");*/
			
			//FileOutputStream fos = new FileOutputStream(file);
			/*OutputStream out = httpServletResponse.getOutputStream();
			IOUtils.copy(stream, out);
			
			

			//byte[] buffer = new byte[4096];
            //int bytesRead = 0;
            /*while ((bytesRead = byteArr.read()) != -1) {
               out.write(buffer, 0, bytesRead);
               fos.write(buffer, 0, bytesRead);
            }*/
			//out.flush();
			//out.close();
			//fos.flush();
			//fos.close();
			
			//String val = convert(bull);
			//String pru = convertTo(stream);
			/*byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = stream.read(buffer)) != -1) {
               out.write(buffer, 0, bytesRead);
            }
            stream.close();
            out.close();
			//output.flush();
			//output.close();
			//String fileNamePru = name;
			//FileOutputStream fos = new FileOutputStream(fileNamePru);
			//byte[] buffer = new byte[2048];
			//int r = 0;
			//String str = convert(stream);
			/*File file = new File(fileNamePru);
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(str.getBytes());
			outputStream.close();

			System.out.println("File Generated: " + fileName);*/
			logger.info("Llega aqui:");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CfeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(in);
			if(fileName != null) {
				fileName.delete();
			}
			logger.info("Cerrando servletOutput....");
			
		}
		
		logger.info("Despues de responseCOmplete....");
		FacesContext.getCurrentInstance().responseComplete();
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
}
