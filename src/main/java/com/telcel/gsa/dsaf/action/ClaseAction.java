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
@Service("claseAction")
@ViewScoped
public class ClaseAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860287028313521018L;
	final static Logger logger = LoggerFactory.getLogger(ClaseAction.class);
	@Autowired
	private ClaseService claseService;
	@Autowired
	private SessionScopeUser sessionScopeUser;

	
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
		ctx.getFlowScope().put("clase", clase);
	}
	

	public void consulta(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("clase");
		
		try {
			
			clase.setClases(claseService.consultaClase(clase, sessionScopeUser));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("clase", clase);
	}
	
	public void addInpcPopUp(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("clase");
		clase.setClaseSel(new BajasClase());
		ctx.getFlowScope().put("clase", clase);
	}
	
	public void guardar(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("clase");
		try {
			//clase.setId_usuario_alta(sessionScopeUser.getUsuarioCfe().getId());
			claseService.insertaClase(clase, sessionScopeUser);
			clase.getClases().add(clase.getClaseSel());
		}catch(CfeException ex){
			if(ex.getMessage().equals("DUPLICADO")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "LA CLASE YA EXISTE"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("clase", clase);
	}
	
	public void actualizar(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("clase");
		try {
			
			claseService.actualizaClase(clase, sessionScopeUser);
		}catch(CfeException ex){
			if(ex.getMessage().equals("DUPLICADO")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "EL INPC YA EXISTE"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("clase", clase);
	}
	
	
	public void baja(RequestContext ctx){
		ClaseBean clase = (ClaseBean)ctx.getFlowScope().get("clase");
		try {
			claseService.eliminaClase(clase, sessionScopeUser);
			clase.getClases().remove(clase.getClaseSel());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("clase", clase);
	}
		
}
