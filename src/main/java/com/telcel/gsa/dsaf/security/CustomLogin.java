package com.telcel.gsa.dsaf.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.LoginBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
import com.telcel.gsa.dsaf.service.LoginService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.Constants;

@ViewScoped
@Service("autentificadorPersonalizado")
public class CustomLogin implements AuthenticationManager {

	private static Logger logger = LoggerFactory
			.getLogger(CustomLogin.class);

	@Autowired
	private LoginService loginService;
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	
	/**
	 * @return the loginService
	 */
	public LoginService getLoginService() {
		return loginService;
	}



	/**
	 * @param loginService the loginService to set
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}







	/**
	 * @return the utileriasCfeService
	 */
	public UtileriasCfeService getUtileriasCfeService() {
		return utileriasCfeService;
	}



	/**
	 * @param utileriasCfeService the utileriasCfeService to set
	 */
	public void setUtileriasCfeService(UtileriasCfeService utileriasCfeService) {
		this.utileriasCfeService = utileriasCfeService;
	}



	@Override
	public Authentication authenticate(Authentication authentication){

		logger.info(Constants.APP_ID + "Iniciando la autenticacion...");
		
		UsernamePasswordAuthenticationToken authenticationToken = null;
		LoginBean login = new LoginBean();
		login.setUsername(authentication.getName());
		login.setCode(authentication.getCredentials().toString());


		if (login.getUsername() == null || login.getUsername().isEmpty() || login.getCode() == null
				|| login.getCode().isEmpty()) {
			throw new BadCredentialsException("Los campos usuario y contraseña son obligatorios.");
		}

		logger.info(Constants.APP_ID + "Verificando registro del usuario");
			
		SessionBean sessionData = new SessionBean();
		
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
		params.add(Constants.LOGIN_DATA_YEARS_BACK);
		
		// Se obtienen parametros de conexion LDAP
		Map<String,BajasCatParametrosBean> parametros = utileriasCfeService.getParametros(params, sessionScopeUser);
		sessionData.setRegionesMap(utileriasCfeService.obtenerRegiones(sessionScopeUser));
	
		BajasOpUsuarioBean usuarioLogin = loginService.cargaInicial(parametros, login, sessionData, sessionScopeUser);	
		
		if(usuarioLogin != null){
			
			sessionData.setUsuarioCfe(usuarioLogin);
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			authenticationToken = new UsernamePasswordAuthenticationToken(
					authentication.getName(),
					authentication.getCredentials(), authorities);
		}else{
			logger.error(Constants.APP_ID + "El usuario no existe");
			throw new BadCredentialsException(
					"El usuario no esta registrado");
		}

		return authenticationToken;
	}
	
}
