package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.entity.TcddParametro;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.CienDepreciadosService;
import com.telcel.gsa.dsaf.service.TccdParametroService;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.Predicate;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Service("cargaMasAction")
@ViewScoped
public class CargaCienDepreciadosAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5347656222555039386L;
	private static Logger logger = LoggerFactory.getLogger(CargaCienDepreciadosAction.class);
	
	private transient UploadedFile uploadedFile;
	
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	@Autowired
	private TccdParametroService tccdParametroService;
	
	@Autowired
	BitacoraService bitacoraService;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	
	@Autowired
	CienDepreciadosService dprCien;
	

	
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
	
	public void updateMonth(RequestContext ctx) {
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
		datos.setMes(datos.getMes());
		ctx.getFlowScope().put("datCien", datos);
		
	}
	
	public void updateYear(RequestContext ctx) {
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
		datos.setAnio(datos.getAnio());
		ctx.getFlowScope().put("datCien", datos);
		
	}
	
	
	
	public void handleSiteFile(FileUploadEvent event){
		
		logger.info(Constants.APP_ID + "Metodo de carga de archivo.....");
		org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder.getRequestContext();
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
		uploadedFile = event.getFile();
		FacesContext context = FacesContext.getCurrentInstance();
		SimpleDateFormat feH = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		SimpleDateFormat FAnio = new SimpleDateFormat(Constants.FORMATOANIO);
		InputStream input = null;
		logger.info("Ingresa a parametros");
		TcddParametroBean parametroBean = new TcddParametroBean();
		parametroBean.setNombreParam("AÑO");
		parametroBean.setDato(new TcddParametro());
		BajasBitacoraBean datBit = new BajasBitacoraBean();
		int datValInc = 0;
		Long maxBit = bitacoraService.idMax( sessionScopeUser);
		BajasDepreciadosCienBean datExcel = new BajasDepreciadosCienBean();
		List<BajasDepreciadosCienBean> datCorrectos = new ArrayList<BajasDepreciadosCienBean>();
		List<BajasBitacoraBean> bitacoras = new ArrayList<BajasBitacoraBean>();
		List <BajasDepreciadosCien> lisOK = new ArrayList<BajasDepreciadosCien>();
		List <BajasDepreciadosCien> lisUpdt = new ArrayList<BajasDepreciadosCien>();
		List <BajasDepreciadosCien> lisSave = new ArrayList<BajasDepreciadosCien>();
		try {
			parametroBean=tccdParametroService.consulta(parametroBean, sessionScopeUser);
			
			if(datos.getAnio().equals(Integer.parseInt(parametroBean.getDatQry().get(0).getValor()))) {
			input = uploadedFile.getInputstream();
			Workbook workbook = WorkbookFactory.create(input); 
			
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			
			List<BajasDepreciadosCienBean> datExcelLst = new LinkedList<BajasDepreciadosCienBean>();
			
			
			//limpia bitacora carga CIEN
			bitacoraService.limpiaBitacora("CARGA_CIEN",sessionScopeUser);
			//consulta el maximo de la bitacora 
			
			
			List<BajasDepreciadosCienBean> fassets = dprCien.getDepreciados(datos.getAnio(), datos.getMes().getId(), sessionScopeUser);
			while (iterator.hasNext()) {
				  boolean bitaError = false;// no insertar bitacora
				 
				  Row currentRow = iterator.next();
				  if(currentRow.getRowNum() == 0)
				  {
					  currentRow = iterator.next(); 
				  }
				  Iterator<Cell> cellIterator = currentRow.cellIterator();

//				  datosDpr.setAnio(datos.getAnio());
//				  datosDpr.setMes(datos.getMes().getId());
				  datExcel.setAnio(datos.getAnio());
				  datExcel.setMes(datos.getMes());
				  datExcel.setIdMes(datos.getMes().getId());
				  while (cellIterator.hasNext()) {
					  Cell currentCell = cellIterator.next();
					  datExcel.setRowNum(currentRow.getRowNum()+1);
					  
		                switch(currentCell.getColumnIndex()) {
			                case 0:
			                	datExcel.setNumeroActivo(currentCell.getStringCellValue());
			                
			                break;
			                case 1:
			                	datExcel.setSubnumero(currentCell.getStringCellValue());
			                
			                break;
			                default:
			                
		                }
                    
                }
				  
				  if (datExcel.getNumeroActivo() == null)
				  {
					  datValInc = datValInc + 1;
					  datBit = new BajasBitacoraBean();
					  maxBit =maxBit + 1;
					  //se agrega en la bitacora
					  datBit.setBitacoraId(maxBit);
					  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
					  datBit.setBitacoraProceso("CARGA_CIEN");
					  datBit.setBitacoraDescripcion("CAMPO NUMERO DE ACTIVO ES REQUERIDO EN FILA: "+datExcel.getRowNum());
					  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
					  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
					  datBit.setBitacoraEstadoRegistro(true);
					  bitacoras.add(datBit);
					  
				  }else if(datExcel.getSubnumero() == null) {
					  datValInc = datValInc + 1;
					  datBit = new BajasBitacoraBean();
					  maxBit =maxBit + 1;
					  //se agrega en la bitacora
					  datBit.setBitacoraId(maxBit);
					  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
					  datBit.setBitacoraProceso("CARGA_CIEN");
					  datBit.setBitacoraDescripcion("CAMPO SUBNUMERO ES REQUERIDO EN FILA: "+datExcel.getRowNum());
					  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
					  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
					  datBit.setBitacoraEstadoRegistro(true);
					  bitacoras.add(datBit);
				  }
				  else
				  {
					  if(datExcel.getNumeroActivo().length() <=11)
					  {
						  datValInc = datValInc +1;
						  datBit = new BajasBitacoraBean();
						  //se agrega en la bitacora
						  maxBit =maxBit +1;
						  datBit.setBitacoraId(maxBit);
						  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
						  datBit.setBitacoraProceso("CARGA_CIEN");
						  datBit.setBitacoraDescripcion("VERIFICAR LA LONGITUD DEL NUMERO DE ACTIVO EN LA LINEA  "+datExcel.getRowNum()+" DEL ARCHIVO");
						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
						  datBit.setBitacoraEstadoRegistro(true);
						  bitacoras.add(datBit);
					  }else if(datExcel.getSubnumero().length() != 4) {
						  datValInc = datValInc +1;
						  datBit = new BajasBitacoraBean();
						  //se agrega en la bitacora
						  maxBit =maxBit +1;
						  datBit.setBitacoraId(maxBit);
						  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
						  datBit.setBitacoraProceso("CARGA_CIEN");
						  datBit.setBitacoraDescripcion("VERIFICAR LA LONGITUD DEL SUBNUMERO DE ACTIVO EN LA LINEA  "+datExcel.getRowNum()+" DEL ARCHIVO (4 DIGITOS)");
						  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
						  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
						  datBit.setBitacoraEstadoRegistro(true);
						  bitacoras.add(datBit);
					  }
					  else
					  { 
//						  BajasDprCienFormBean datEnviar =new BajasDprCienFormBean();
//						  datEnviar.setNumeroActivo(datExcel.getNumeroActivo());
						  //validar que no se haya echo la carga en algun mes y año anterior
						  BajasDepreciadosCienBean datValidar = new BajasDepreciadosCienBean();
						
							  if(fassets.contains(datExcel)) {
								  
								 if(fassets.get(fassets.indexOf(datExcel)).getEstadoRegistro().equals(1)) {
									  datValInc = datValInc +1;
									  datBit = new BajasBitacoraBean();
									  //se agrega en la bitacora
									  maxBit =maxBit +1;
									  datBit.setBitacoraId(maxBit);
									  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
									  datBit.setBitacoraProceso("CARGA_CIEN");
									  datBit.setBitacoraDescripcion("NUMERO DE ACTIVO; "+datExcel.getNumeroActivo()+ ", SUBNUMERO: " + datExcel.getSubnumero() + ", YA EXISTE EN EL AÑO: "+ datExcel.getAnio()+", MES: "+ datExcel.getMes().getId()+"");
									  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
									  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
									  datBit.setBitacoraEstadoRegistro(true);
									  bitacoras.add(datBit);
								 }else {
									//Cambiar estado registro a 1.
									  BajasDepreciadosCien datosDpr = new BajasDepreciadosCien();
									  datosDpr.setNumeroActivo(datExcel.getNumeroActivo());
									  datosDpr.setSubnumero(datExcel.getSubnumero());
									  datosDpr.setAnio(datExcel.getAnio());
									  datosDpr.setMes(datExcel.getMes().getId());
									  datosDpr.setFechaCarga(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
									  datosDpr.setIdSociedad(sessionScopeUser.getSociedad().getId());
									  datosDpr.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
									  datosDpr.setEstadoRegistro(1);
									  lisUpdt.add(datosDpr);
								 }
								  
								  
							  }else{
								  ///todos los datos son correctos.
								  BajasDepreciadosCien datosDpr = new BajasDepreciadosCien();
								  datosDpr.setNumeroActivo(datExcel.getNumeroActivo());
								  datosDpr.setSubnumero(datExcel.getSubnumero());
								  datosDpr.setAnio(datExcel.getAnio());
								  datosDpr.setMes(datExcel.getMes().getId());
								  datosDpr.setFechaCarga(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
								  datosDpr.setIdSociedad(sessionScopeUser.getSociedad().getId());
								  datosDpr.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
								  datosDpr.setEstadoRegistro(1);
//								  lisOK.add(datosDpr);
								  lisSave.add(datosDpr);
								  
								 
							  } 
						  
					  }
				  }
				 datExcelLst.add(datExcel);
				 				  
              } //fin while 
			
			 
			  
			  
			  
            
		}else{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "EL AÑO DE TRABAJO NO CORRESPONDE CON EL AÑO DE CARGA DE ACTIVOS, VALIDE LA SECCION DE ADMINISTRACION > PARAMETROS"));
		}
		} catch (IOException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (EncryptedDocumentException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (InvalidFormatException e) {
			logger.error(Constants.APP_ID + e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
			//Cannot get a STRING value from a NUMERIC cell
			if(e.getMessage() != null && e.getMessage().toUpperCase().contains("CANNOT GET A STRING VALUE FROM A NUMERIC CELL")) {
				datValInc = datValInc +1;
				  datBit = new BajasBitacoraBean();
				  //se agrega en la bitacora
				  maxBit =maxBit +1;
				  datBit.setBitacoraId(maxBit);
				  datBit.setBitacoraLinea(Long.valueOf(datExcel.getRowNum()));
				  datBit.setBitacoraProceso("CARGA_CIEN");
				  datBit.setBitacoraDescripcion("VERIFICAR QUE EL FORMATO DE LAS COLUMASN SEA TEXTO");
				  datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
				  datBit.setBitacoraFechaCreacion(utileriasCfeService.obtenerFechaActual(sessionScopeUser));
				  datBit.setBitacoraEstadoRegistro(true);
				  bitacoras.add(datBit);
			}
		}
		finally{try {
			if(input != null){
				input.close();
				if (datValInc > 0)
				  {
					  //se inserta en la bitacora
					  if(bitacoras != null && !bitacoras.isEmpty()){
						  bitacoraService.guardaBitacora(bitacoras, sessionScopeUser);
					  }
					  datos.setGuardar(true);
					  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "SE ENCONTRARON ERRORES EN EL ARCHIVO FAVOR DE VALIDAR LA BITACORA"));
				  }
				  else
				  {
					  datos.getListOK().addAll(lisSave);
					  datos.getListOK().addAll(lisUpdt);
					  datos.setListUpdt(lisUpdt);
					  datos.setListSave(lisSave);
					  datos.setGuardar(false);
					 
					  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.BITACORA_WARNING, "ARCHIVO CORRECTO, PRESIONE GUARDAR PARA ALMACENAR ACTIVOS"));
				  }
				  
				 ctx.getFlowScope().put("datCien", datos);  
	           
	            logger.info(Constants.APP_ID + "Carga de archivo terminada...");
			}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(),e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(),e);
			}
		}
		
	}
	
	public void guardarInfo(RequestContext ctx) throws IOException, SQLException {
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
		
//			for (BajasDepreciadosCien bajasDepreciadosCien : datos.getListOK()) {
//				logger.info("Guardando objeto: " + bajasDepreciadosCien.getNumeroActivo() + "#" + bajasDepreciadosCien.getSubnumero());
//				dprCien.guardaCienDpr(bajasDepreciadosCien, sessionScopeUser);
//			
//			}
			if(datos.getListOK() != null && !datos.getListOK().isEmpty()) {
				dprCien.guardaCienDprLst(datos.getListSave(), sessionScopeUser);
			}
			if(datos.getListUpdt() != null && !datos.getListUpdt().isEmpty()) {
				dprCien.actualizaCienDprLst(datos.getListUpdt(), sessionScopeUser);
			}
			
		datos.setGuardar(true);
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.BITACORA_WARNING, "ACTIVOS GUARDADOS EXITOSAMENTE!"));
		
		}catch(CfeException e){
			logger.info(e.getMessage(), e);
		}catch(Exception e) {
			logger.info(e.getMessage(), e);
		}
		
		ctx.getFlowScope().put("datCien", datos);
	}
	
	

	

}
