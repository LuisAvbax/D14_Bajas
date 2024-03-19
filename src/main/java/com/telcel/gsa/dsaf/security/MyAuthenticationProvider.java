package com.telcel.gsa.dsaf.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.LoginBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
import com.telcel.gsa.dsaf.service.LoginService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.Constants;

public class MyAuthenticationProvider implements AuthenticationProvider {

	private static Logger logger = LoggerFactory
			.getLogger(MyAuthenticationProvider.class);
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication res = isValid(authentication);
        if (!res.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }
        return res;
	}
	
	private Authentication isValid(final Authentication authentication) {
        Authentication res = authentication;
        sessionScopeUser.setSociedad(utileriasCfeService.strutureSoc(Integer.parseInt(((MyAuthenticationDetails)authentication.getDetails()).getMethod())));

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
			res = createSuccessAuthentication(authenticationToken);
		}else{
			logger.error(Constants.APP_ID + "Credenciales incorrectas");
			throw new BadCredentialsException(
					"Credenciales incorrectas o usuario no registrado");
		}
        
        
        
//        if ("Admin".equals(authentication.getPrincipal())&&"Password".equals(authentication.getCredentials())) {
//            res = createSuccessAuthentication(authentication);
//        }
        return res;
    }

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	protected Authentication createSuccessAuthentication(final Authentication authentication) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
        result.setDetails(authentication.getDetails());
 
        return result;
    }

}
