package com.telcel.gsa.dsaf.action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.InpcBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasInpc;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.InpcService;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.Predicate;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("inpcAction")
@ViewScoped
public class InpcAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860287028313521018L;
	final static Logger logger = LoggerFactory.getLogger(InpcAction.class);
	@Autowired
	private InpcService inpcService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	
	public void initFlow(RequestContext ctx){
		logger.info("Ingresa a  inpc initflow");
		Calendar calendar = new GregorianCalendar();
		InpcBean inpc = new InpcBean();
		BajasInpc selInpc = new BajasInpc();
		inpc.setInpc(selInpc);
		inpc.setInpcs(new ArrayList<BajasInpc>());	
		inpc.setAnios(sessionScopeUser.getAnios());
		inpc.setMeses(sessionScopeUser.getMeses());
		inpc.setMes(new MesBean());
		inpc.setAnio(calendar.get(Calendar.YEAR));
		ctx.getFlowScope().put("idce", inpc);
	}
	

	public void consulta(RequestContext ctx){
		InpcBean inpc = (InpcBean)ctx.getFlowScope().get("idce");
		
		try {
			inpc.setInpcs(inpcService.consultaInpc(inpc, sessionScopeUser));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("idce", inpc);
	}
	
	public void addInpcPopUp(RequestContext ctx){
		InpcBean inpc = (InpcBean)ctx.getFlowScope().get("idce");
		inpc.setInpc(new BajasInpc());
		ctx.getFlowScope().put("idce", inpc);
	}
	
	public void guardar(RequestContext ctx){
		InpcBean inpc = (InpcBean)ctx.getFlowScope().get("idce");
		try {
			inpcService.insertaInpc(inpc, sessionScopeUser);
			
		}catch(CfeException ex){
			if(ex.getMessage().equals("DUPLICADO")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "EL INPC YA EXISTE"));
			}
			logger.error(ex.getMessage(),ex);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("idce", inpc);
	}
	
	public void actualizar(RequestContext ctx){
		InpcBean inpc = (InpcBean)ctx.getFlowScope().get("idce");
		try {
			inpcService.actualizaInpc(inpc, sessionScopeUser);
		}catch(CfeException ex){
			if(ex.getMessage().equals("DUPLICADO")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "EL INPC YA EXISTE"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("idce", inpc);
	}
	
	
	public void baja(RequestContext ctx){
		InpcBean inpc = (InpcBean)ctx.getFlowScope().get("idce");
		try {
			inpcService.eliminaInpc(inpc, sessionScopeUser);
			inpc.getInpcs().remove(inpc.getInpc());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("idce", inpc);
	}
	
	
	
	
	
	
}
