package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import com.telcel.gsa.dsaf.bean.GeneraRespaldoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.RespaldoService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;


@Service("generaRespalAction")
@ViewScoped
public class GenerarRespaldoAction implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2560971369843715665L;
	final static Logger logger = LoggerFactory.getLogger(BitacoraClaseAction.class);
	
	
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	
	@Autowired
	private RespaldoService respaldoService;
	
	public void initFlow(RequestContext ctx){
		GeneraRespaldoBean respaldo = new GeneraRespaldoBean();
		respaldo.setIdSociedad(sessionScopeUser.getSociedad().getId());
		respaldo.setIdUsuario(sessionScopeUser.getUsuarioCfe().getId());
		respaldo.setDisable(false);
		List<String> sshParams = new ArrayList<String>();
		sshParams.add("BACKUP_SERVER");
		sshParams.add("BACKUP_USER");
		sshParams.add("BACKUP_PWD");
		sshParams.add("BACKUP_PORT");
		sshParams.add("BACKUP_CHANNELT");
		sshParams.add("BACKUP_SESIONT");
		respaldo.setSshParamMap(utileriasCfeService.getParams(sshParams));
		ctx.getFlowScope().put("datEnvio", respaldo);
	}
	
	public void respaldo(RequestContext ctx) throws IOException{
		GeneraRespaldoBean respaldo = (GeneraRespaldoBean) ctx.getFlowScope().get("datEnvio");
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
		String fechaHora = sdf.format(utileriasCfeService.obtenerFechaActual());

		try {
			respaldoService.gestionaRespaldo(respaldo, sessionScopeUser);
		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
		}
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", 
			" Se genera la solicitud de Respaldo en breve recibirá un correo con la confirmación, "
			+ "en caso de no recibir el correo favor de contactar al administrador."));
        
        ctx.getFlowScope().put("datEnvio", respaldo);
		
		
	}

}
