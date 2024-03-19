package com.telcel.gsa.dsaf.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.LoginBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.LoginService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import com.telcel.gsa.dsaf.util.Utilerias;

/**
 * 
 * @author VI5XXAA
 *
 */
@Service("login")
public class LoginAction implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(LoginAction.class);
	private static final long serialVersionUID = -2769470780352964792L;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;

	public void initFlow(RequestContext ctx) {
		LoginBean login = new LoginBean();
		ctx.getFlowScope().put("loginBean", login);
	}

	public String performLogin(RequestContext ctx) {
		String loginVar = "false";
		LoginBean login = (LoginBean) ctx.getFlowScope().get("loginBean");
		if (!StringUtils.isBlank(login.getUsername()) && !StringUtils.isBlank(login.getCode())) {
			List<String> params = new ArrayList<String>();
			params.add(Constants.LOGIN_LDAP_BASE_URL);
			params.add(Constants.LOGIN_LDAP_CLAVE_APP);
			params.add(Constants.LOGIN_LDAP_HOST);
			params.add(Constants.LOGIN_LDAP_ID_APP);
			params.add(Constants.LOGIN_LDAP_ACTIVE);
			params.add(Constants.LOGIN_LDAP_PORT);
			params.add(Constants.LOGIN_LDAP_ENDPOINT);
			params.add(Constants.LOGIN_LDAP_TNS);
			params.add(Constants.LOGIN_LDAP_WSNAME);

			// Se obtienen parametros de conexion LDAP
			Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);

			try {

				SessionBean sessionData = new SessionBean();
				// CARGA PARAMETROS
				sessionData.setParametros(parametros);

				BajasOpUsuarioBean usuarioLogin = loginService.cargaInicial(parametros, login, sessionData, sessionScopeUser);
				if (usuarioLogin != null && (usuarioLogin.getId() != null || usuarioLogin.getId() != 0)) {
					sessionData.setUsuarioCfe(usuarioLogin);
					FacesContext facesContext = FacesContext.getCurrentInstance();
					HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
					session.setAttribute("session", sessionData);
				}else if(usuarioLogin != null && (usuarioLogin.getId() == null || usuarioLogin.getId() == 0)){
					loginVar = "false";
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Credenciales incorrectas o el usuario no se encuentra registrado."));
					ctx.getFlowScope().put("loginRender", login);
				} else {
					loginVar = "false";
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Credenciales incorrectas."));
					ctx.getFlowScope().put("loginRender", login);
				}
			} catch (CfeException e) {
				logger.error(Constants.APP_ID + e.getMessage(), e);
				FacesContext context = FacesContext.getCurrentInstance();
				if (e.getMensaje().contains("WSDL")) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Error de conexión contacte al administrador."));
				} else {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Error, contacte al administrador."));
				}
				return "false";
			}

			return loginVar;
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese sus credeciales."));
			return "false";
		}
	}

	public void logout() {
		this.sessionScopeUser.setUsuarioCfe(null);

	}

	public void getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		
		try {
			if (!(this.sessionScopeUser.getUsuarioCfe() != null)) {
				List<String> params = new ArrayList<String>();
				params.add(Constants.LOGIN_LDAP_BASE_URL);
				params.add(Constants.LOGIN_LDAP_CLAVE_APP);
				params.add(Constants.LOGIN_LDAP_HOST);
				params.add(Constants.LOGIN_LDAP_ID_APP);
				params.add(Constants.LOGIN_LDAP_ACTIVE);
				params.add(Constants.LOGIN_LDAP_PORT);
				params.add(Constants.LOGIN_LDAP_ENDPOINT);
				params.add(Constants.LOGIN_LDAP_TNS);
				params.add(Constants.LOGIN_LDAP_WSNAME);
				params.add(Constants.LOGIN_DATA_MONTHS);
				params.add(Constants.LOGIN_DATA_YEARS_BACK);
				Map<String, BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
				// CARGA PARAMETROS
				this.sessionScopeUser.setParametros(parametros);
				this.sessionScopeUser.setRegionesMap(utileriasCfeService.obtenerRegiones(sessionScopeUser));
				logger.info("Sociedad >>>" + sessionScopeUser.getSociedad());
				this.sessionScopeUser.setMeses(utileriasCfeService.obtenerMeses(parametros));
				this.sessionScopeUser.setAnios(Utilerias.recuperaAniosAtras(utileriasCfeService.obtenerFechaActual(sessionScopeUser),
						Integer.parseInt(parametros.get(Constants.LOGIN_DATA_YEARS_BACK).getValor())));
				this.sessionScopeUser.setUsuarioCfe(
						loginService.obtieneUsuario(new BajasOpUsuarioBean(authentication.getPrincipal().toString()), sessionScopeUser));

			}

		} catch (Exception e) {
			logger.error(Constants.APP_ID + "Error al recuperar un parametro", e);
		}
	}

}
