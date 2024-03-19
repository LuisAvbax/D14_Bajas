package com.telcel.gsa.dsaf.action;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.entity.TcddParametro;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.TccdParametroService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("paramAction")
@ViewScoped
public class TcddParametroAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860287028313521018L;
	final static Logger logger = LoggerFactory.getLogger(TcddParametroAction.class);
	@Autowired
	private TccdParametroService tccdParametroService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	public void initFlow(RequestContext ctx){
		logger.info("Ingresa a parametros");
		TcddParametroBean parametroBean = new TcddParametroBean();
		parametroBean.setNombreParam("AÑO");
		parametroBean.setDato(new TcddParametro());
		try {
			parametroBean=tccdParametroService.consulta(parametroBean, sessionScopeUser);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	
		ctx.getFlowScope().put("paramTdd", parametroBean);
	}
		
	public void actualizar(RequestContext ctx){
		TcddParametroBean datos = (TcddParametroBean)ctx.getFlowScope().get("paramTdd");
		try {
			tccdParametroService.actualiza(datos, sessionScopeUser);
		}catch(CfeException ex){
			
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Constants.BITACORA_WARNING, "ERROR AL GUARDAR"));
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("paramTdd", datos);
	}
	
	
}
