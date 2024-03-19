package com.telcel.gsa.dsaf.action;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.ClaseBean;
import com.telcel.gsa.dsaf.bean.TGTipoAjusteBean;
import com.telcel.gsa.dsaf.entity.BajasClase;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.ClaseService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("claseCargaAction")
@ViewScoped
public class ClaseCargaAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860287028313521018L;
	final static Logger logger = LoggerFactory.getLogger(ClaseCargaAction.class);
	
	@Autowired
	private ClaseService claseService;
	@Autowired
	private SessionScopeUser sessionScopeUser;

	private transient UploadedFile uploadedFile;
	
	@Autowired
	BitacoraService bitacoraService;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	
	public void initFlow(RequestContext ctx){
		logger.info("Ingresa a  inpc initflow");
		ClaseBean clase = new ClaseBean();
		BajasClase selClase = new BajasClase();
		clase.setClaseSel(selClase);
		clase.setClases(new ArrayList<BajasClase>());	
		clase.setGuardar(true);
		ctx.getFlowScope().put("claseCarga", clase);
	}
		
	public void guardar(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("claseCarga");
		for (BajasClase datEnviar : clase.getClases()) {
			try {
											 
				datEnviar.setId_usuario_alta(sessionScopeUser.getUsuarioCfe().getId());
				clase.setClaseSel(datEnviar);
				claseService.insertaClase(clase, sessionScopeUser);
			
			}catch(CfeException ex){
				if(ex.getMessage().equals("DUPLICADO")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "LA CLASE YA EXISTE"));
				}
			} catch (Exception e) {
			logger.error(e.getMessage(),e);
			}
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.BITACORA_WARNING, "SE TERMINA LA CARGA, FAVOR DE VALIDAR EN EL MODULO DE CLASES"));
		ctx.getFlowScope().put("claseCarga", clase);
	}
	
	
	
	public void handleSiteFile(FileUploadEvent event) throws Exception{
		
		logger.info(Constants.APP_ID + "Metodo de carga de archivo.....");
		org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder.getRequestContext();
		ClaseBean datos = (ClaseBean)ctx.getFlowScope().get("claseCarga");
		uploadedFile = event.getFile();
		
		SimpleDateFormat feH = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		SimpleDateFormat FAnio = new SimpleDateFormat(Constants.FORMATOANIO);
		InputStream input = null;
		Integer excelColumns = 1;
		
		try {
			
			//se inserta en la bitacora
			  bitacoraService.limpiaBitacora("CARGA_CLASE",sessionScopeUser);
			  
			input = uploadedFile.getInputstream();
			Workbook workbook = WorkbookFactory.create(input); 
			
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
						
			List<BajasBitacoraBean> bitacoras = new ArrayList<BajasBitacoraBean>();
			BajasBitacoraBean datBit = new BajasBitacoraBean();
			
			BajasClase datExcel = new BajasClase();
			ClaseBean datVal = new ClaseBean();
			Integer contLine = 0;
			
			int datValInc = 0;
			//consulta el maximo de la bitacora 
			Long maxBit = bitacoraService.idMax( sessionScopeUser);
			List <BajasClase> lisOK = new ArrayList<BajasClase>();
			List <ClaseBean> datValidar = new ArrayList<ClaseBean>();
			while (iterator.hasNext()) {
				datExcel = new BajasClase();
				  contLine++;
				  Row currentRow = iterator.next();
				  if(contLine == 1)
				  {
					  currentRow = iterator.next();
					  contLine++;
				  }
				  
				  //pendientes a validar si se utilizan 
//				  Iterator<Cell> cellIterator = currentRow.iterator();
//				  excelColumns = 0;
				  //fin de pendientes
				  datVal = new ClaseBean();
				  String contenidoCeldaCve = null;
				  String contenidoCeldaDesc = null;
				  Cell currentCellCve = currentRow.getCell(0);
				  Cell currentCellDesc = currentRow.getCell(1);
				  //se valida que no sea numerico
				 
				  if (currentCellCve == null)
				  {
					  datVal.setClave("SinDatos");
					  datValInc = datValInc + 1;
					  datBit = new BajasBitacoraBean();
					  maxBit =maxBit + 1;
					  //se agrega en la bitacora
					  datBit.setBitacoraId(maxBit);
					  datBit.setBitacoraLinea(Long.valueOf(contLine));
					  datBit.setBitacoraProceso("CARGA_CLASE");
					  datBit.setBitacoraDescripcion("CAMPO CLAVE ES OBLIGATORIO ");
					  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
					  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
					  datBit.setBitacoraEstadoRegistro(true);
					  bitacoras.add(datBit);
				  }
				  else
				  {
					  
					  //valida que no sean numeros
					  if (currentCellCve.getCellType() != currentCellCve.CELL_TYPE_STRING)
	                  {
						  datValInc = datValInc + 1;
						  datBit = new BajasBitacoraBean();
						  maxBit =maxBit + 1;
						  //se agrega en la bitacora
						  datBit.setBitacoraId(maxBit);
						  datBit.setBitacoraLinea(Long.valueOf(contLine));
						  datBit.setBitacoraProceso("CARGA_CLASE");
						  datBit.setBitacoraDescripcion("EL FORMATO DEL CAMPO CLAVE ES INVALIDO, VERIFICAR QUE EL FORMATO SEA TEXTO");
						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
						  datBit.setBitacoraEstadoRegistro(true);
						  bitacoras.add(datBit);
						  datVal.setClave("SinDatos");
	                  	
	                  }
					  else
					  {
						  contenidoCeldaCve =(currentCellCve.getStringCellValue());
						  datVal.setClave(contenidoCeldaCve);
						  datExcel.setClave(contenidoCeldaCve);
						  datExcel.setId_usuario_alta(sessionScopeUser.getUsuarioCfe().getId());
						  datVal.setId_usuario_alta(sessionScopeUser.getUsuarioCfe().getId());
						  if(datValidar.contains(datVal))
							 {
								 datValInc = datValInc + 1;
								  datBit = new BajasBitacoraBean();
								  maxBit =maxBit + 1;
								  //se agrega en la bitacora
								  datBit.setBitacoraId(maxBit);
								  datBit.setBitacoraLinea(Long.valueOf(contLine));
								  datBit.setBitacoraProceso("CARGA_CLASE");
								  datBit.setBitacoraDescripcion("CLASE "+datVal.getClave()+" ESTA MAS DE UNA VEZ EN EL ARCHIVO ");
								  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
								  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
								  datBit.setBitacoraEstadoRegistro(true);
								  bitacoras.add(datBit);
							 }
						  	datValidar.add(datVal);
					  }
				  }
				  if (currentCellDesc == null)
				  {
					  datVal.setDescripcion("SinDatos");
					  datValInc = datValInc + 1;
					  datBit = new BajasBitacoraBean();
					  maxBit =maxBit + 1;
					  //se agrega en la bitacora
					  datBit.setBitacoraId(maxBit);
					  datBit.setBitacoraLinea(Long.valueOf(contLine));
					  datBit.setBitacoraProceso("CARGA_CLASE");
					  datBit.setBitacoraDescripcion("CAMPO DESCRIPCION DE CLASE ES OBLIGATORIO");
					  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
					  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
					  datBit.setBitacoraEstadoRegistro(true);
					  bitacoras.add(datBit);
				  }
				  else
				  {
					  
					  //valida que no sean numeros
					  if (currentCellDesc.getCellType() != currentCellDesc.CELL_TYPE_STRING)
	                  {
						  datValInc = datValInc + 1;
						  datBit = new BajasBitacoraBean();
						  maxBit =maxBit + 1;
						  //se agrega en la bitacora
						  datBit.setBitacoraId(maxBit);
						  datBit.setBitacoraLinea(Long.valueOf(contLine));
						  datBit.setBitacoraProceso("CARGA_CLASE");
						  datBit.setBitacoraDescripcion("EL FORMATO DEL CAMPO DESCRIPCION ES INVALIDO, VERIFICAR QUE EL FORMATO SEA TEXTO");
						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
						  datBit.setBitacoraEstadoRegistro(true);
						  bitacoras.add(datBit);
	                  	
	                  }
					  else
					  {
						  contenidoCeldaDesc =(currentCellDesc.getStringCellValue());
						  datVal.setDescripcion(contenidoCeldaDesc);
						  datExcel.setDescripcion(contenidoCeldaDesc);
					  }
				  }
				  if (!datVal.getClave().equals("SinDatos"))
				  {
					 
					//validar que la clase no exista
						 List<BajasClase> existe= claseService.consultaClaseValida(datVal, sessionScopeUser);
						 if (!existe.isEmpty())
						 {
							 datValInc = datValInc + 1;
							  datBit = new BajasBitacoraBean();
							  maxBit =maxBit + 1;
							  //se agrega en la bitacora
							  datBit.setBitacoraId(maxBit);
							  datBit.setBitacoraLinea(Long.valueOf(contLine));
							  datBit.setBitacoraProceso("CARGA_CLASE");
							  datBit.setBitacoraDescripcion("CLASE (" +datExcel.getClave()+"-"+datExcel.getDescripcion()+") YA EXISTE");
							  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
							  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
							  datBit.setBitacoraEstadoRegistro(true);
							  bitacoras.add(datBit);
						 }
						 else
						 {
							 lisOK.add(datExcel);
						 }
				  }
				  
				  

//				  datExcel.setClave("SinDatos");
//				  datExcel.setDescripcion("SinDatos");
//				  while (cellIterator.hasNext()) {
//					  excelColumns +=1;
//					  Cell currentCell = cellIterator.next();
//					  String contenidoCelda = null; 
//					  datVal = new ClaseBean();
//                    if (currentCell.getCellType() == currentCell.CELL_TYPE_STRING)
//                    {
//                    	contenidoCelda =(currentCell.getStringCellValue());
//                    	datExcel.setClave("SinDatos");
//      				  	datExcel.setDescripcion("SinDatos");
//                    	
//                    }
//                    else
//                    {
//                    	datValInc = datValInc + 1;
//						  datBit = new BajasBitacoraBean();
//						  maxBit =maxBit + 1;
//						  //se agrega en la bitacora
//						  datBit.setBitacoraId(maxBit);
//						  datBit.setBitacoraLinea(Long.valueOf(contLine));
//						  datBit.setBitacoraProceso("CARGA_CLASE");
//						  datBit.setBitacoraDescripcion("EL FORMATO DE LOS CAMPOS ES INVALIDO, VERIFICAR QUE EL FORMATO SEA TEXTO");
//						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//						  datBit.setBitacoraEstadoRegistro(true);
//						  bitacoras.add(datBit);
//						  contenidoCelda ="numeric@";
//						  datExcel.setClave("numeric@");
//                    	
//                    }
//                    
//                    if(!contenidoCelda.equals("numeric@"))
//                    {
//	                    switch (excelColumns) {
//						case 1:
//							datExcel.setClave(contenidoCelda);
//							datVal.setClave(contenidoCelda);
//							 if(datValidar.contains(datVal))
//							 {
//								 datValInc = datValInc + 1;
//								  datBit = new BajasBitacoraBean();
//								  maxBit =maxBit + 1;
//								  //se agrega en la bitacora
//								  datBit.setBitacoraId(maxBit);
//								  datBit.setBitacoraLinea(Long.valueOf(contLine));
//								  datBit.setBitacoraProceso("CARGA_CLASE");
//								  datBit.setBitacoraDescripcion("CLASE "+datVal.getClave()+" ESTA MAS DE UNA VEZ EN EL ARCHIVO ");
//								  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//								  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//								  datBit.setBitacoraEstadoRegistro(true);
//								  bitacoras.add(datBit);
//							 }
//							 datValidar.add(datVal);
//							break;
//						case 2:
//							datExcel.setDescripcion(contenidoCelda);
//							datVal.setDescripcion(contenidoCelda);
//							if (currentCell.getCellType() == currentCell.CELL_TYPE_NUMERIC)
//		                    {
//		                    	datValInc = datValInc + 1;
//								  datBit = new BajasBitacoraBean();
//								  maxBit =maxBit + 1;
//								  //se agrega en la bitacora
//								  datBit.setBitacoraId(maxBit);
//								  datBit.setBitacoraLinea(Long.valueOf(contLine));
//								  datBit.setBitacoraProceso("CARGA_CLASE");
//								  datBit.setBitacoraDescripcion("CAMPO DESCRIPCION DE CLASE NO PUEDE SER NUMERICO ");
//								  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//								  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//								  datBit.setBitacoraEstadoRegistro(true);
//								  bitacoras.add(datBit);
//		                    }
//							break;
//						
//						}
//                    }
//                    
//                   
//                    
//                    excelColumns++;
//                }
//				 if (!datExcel.getClave().equals("numeric@") )
//				 {
//				  if (datExcel.getClave().equals("SinDatos") || datExcel.getDescripcion().equals("SinDatos"))
//				  {
//					  if (datExcel.getClave().length()>0)
////					  if (datExcel.getClave().equals("SinDatos"))
//					  {
//						  datValInc = datValInc + 1;
//						  datBit = new BajasBitacoraBean();
//						  maxBit =maxBit + 1;
//						  //se agrega en la bitacora
//						  datBit.setBitacoraId(maxBit);
//						  datBit.setBitacoraLinea(Long.valueOf(contLine));
//						  datBit.setBitacoraProceso("CARGA_CLASE");
//						  datBit.setBitacoraDescripcion("CAMPO CLAVE ES OBLIGATORIO ");
//						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//						  datBit.setBitacoraEstadoRegistro(true);
//					  }
//					  if (datExcel.getDescripcion().length()>0)
//					  {
//						  datValInc = datValInc + 1;
//						  datBit = new BajasBitacoraBean();
//						  maxBit =maxBit + 1;
//						  //se agrega en la bitacora
//						  datBit.setBitacoraId(maxBit);
//						  datBit.setBitacoraLinea(Long.valueOf(contLine));
//						  datBit.setBitacoraProceso("CARGA_CLASE");
//						  datBit.setBitacoraDescripcion("CAMPO DESCRIPCION DE CLASE ES OBLIGATORIO");
//						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//						  datBit.setBitacoraEstadoRegistro(true);
//					  }
//					  
//					  bitacoras.add(datBit);
//					  
//				  }
//				  else
//				  {
//					 //validar que la clase no exista
//					 List<BajasClase> existe= claseService.consultaClase(datVal, sessionScopeUser);
//					 if (!existe.isEmpty())
//					 {
//						 datValInc = datValInc + 1;
//						  datBit = new BajasBitacoraBean();
//						  maxBit =maxBit + 1;
//						  //se agrega en la bitacora
//						  datBit.setBitacoraId(maxBit);
//						  datBit.setBitacoraLinea(Long.valueOf(contLine));
//						  datBit.setBitacoraProceso("CARGA_CLASE");
//						  datBit.setBitacoraDescripcion("CLASE (" +datExcel.getClave()+"-"+datExcel.getDescripcion()+") YA EXISTE");
//						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(1);
//						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
//						  datBit.setBitacoraEstadoRegistro(true);
//						  bitacoras.add(datBit);
//					 }
//					 else
//					 {
//						 lisOK.add(datExcel);
//					 }
//					 
//				  }
//				 }
//				 				  
              } //fin while principal
			  if (datValInc > 0)
			  {
//				//se inserta en la bitacora
//				  bitacoraService.limpiaBitacora("CARGA_CLASE",sessionScopeUser);
				  //se inserta en la bitacora
				  if(bitacoras != null && !bitacoras.isEmpty()){
					  bitacoraService.guardaBitacora(bitacoras, sessionScopeUser);
				  }
				  datos.setGuardar(true);
				  FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "SE ENCONTRARON ERRORES EN EL ARCHIVO FAVOR DE VALIDAR LA BITACORA"));
			  }
			  else
			  {
				  datos.setClases(lisOK);
				  datos.setGuardar(false);
			  }
			  
			 ctx.getFlowScope().put("claseCarga", datos);  
           
            logger.info(Constants.APP_ID + "Carga de archivo terminada...");
            
            
		} catch (IOException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (EncryptedDocumentException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (InvalidFormatException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
		finally{try {
			if(input != null){
				input.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}}
		
	}
	
	
	
	
	
}
