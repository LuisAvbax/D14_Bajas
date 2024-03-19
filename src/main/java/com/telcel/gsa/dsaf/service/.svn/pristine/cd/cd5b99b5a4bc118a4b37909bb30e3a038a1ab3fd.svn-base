package com.telcel.gsa.dsaf.service.impl;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.LoginBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
import com.telcel.gsa.dsaf.dao.UsuarioDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.sds.servicios.empleado.cliente.StAuthenticateParameters;
import com.telcel.sds.servicios.empleado.cliente.StEmployeesEmpleadoItem;
import com.telcel.sds.servicios.empleado.cliente.StFilterEmployee;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalog;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogDOAUTHENTICATE;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogDOAUTHENTICATEResponse;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEE;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEEResponse;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogSoapPort;
import com.telcel.security.Crypt;
import com.telcel.gsa.dsaf.service.LoginService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2516958211455856334L;
	private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	@Autowired
	@Qualifier("utileriasCfeDipcelDaoImpl")
	UtileriasCfeDao utileriasCfeDipcelDao;
	@Autowired
	@Qualifier("utileriasCfeSercotelDaoImpl")
	UtileriasCfeDao utileriasCfeSercotelDao;
	@Autowired
	@Qualifier("utileriasCfeAmovDaoImpl")
	UtileriasCfeDao utileriasCfeAmovDao;
	@Autowired
	@Qualifier("usuarioDaoImpl")
	UsuarioDao usuarioDao;
	@Autowired
	@Qualifier("usuarioDaoDipcelImpl")
	UsuarioDao usuarioDipcelDao;
	@Autowired
	@Qualifier("usuarioDaoSercotelImpl")
	UsuarioDao usuarioSercotelDao;
	@Autowired
	@Qualifier("usuarioDaoAmovImpl")
	UsuarioDao usuarioAmovDao;
	
	@Override
	public WsEmployeeCatalogDOAUTHENTICATEResponse performLogin(
			StAuthenticateParameters param, Map<String,BajasCatParametrosBean> parametros)throws CfeException {
		
		logger.info(Constants.APP_ID +  "##############INICIA PERFORM LOGIN##############");
		WsEmployeeCatalogDOAUTHENTICATE auth = null;
		WsEmployeeCatalogSoapPort port = null;
		WsEmployeeCatalog service = null;
		try{
		auth = new WsEmployeeCatalogDOAUTHENTICATE();
		auth.setStauthenticateparameters(param);
		URL url = null;
		URL baseUrl;
	     baseUrl = WsEmployeeCatalog.class.getResource(".");
	     logger.info(Constants.APP_ID + "##############SETEANDO URL##############");
	     url = new URL(baseUrl, parametros.get(Constants.LOGIN_LDAP_ENDPOINT).getDescripcion());
	    service = new WsEmployeeCatalog(url, new QName(parametros.get(Constants.LOGIN_LDAP_TNS).getValor(), parametros.get(Constants.LOGIN_LDAP_WSNAME).getValor()));
	    logger.info(Constants.APP_ID + "##############SETEANDO PORT##############");
	    port = service.getWsEmployeeCatalogSoapPort();
		}catch(Exception e){
			logger.error(Constants.APP_ID + e.getMessage(), e);
			throw new CfeException(Constants.LOGIN_LDAP_ERROR, e);
		}
		return port.doauthenticate(auth);
	}
	@Override
	public BajasOpUsuarioBean obtieneDetalleUsuario(StFilterEmployee filterEmployee,Map<String,BajasCatParametrosBean> parametros){
		WsEmployeeCatalog service = null;
		WsEmployeeCatalogGETEMPLOYEE employee = new WsEmployeeCatalogGETEMPLOYEE();
		employee.setStfilteremployee(filterEmployee);
		URL url = null;
		URL baseUrl = null;
		BajasOpUsuarioBean usuario = null;
	     baseUrl = WsEmployeeCatalog.class.getResource(".");
	     try {
			url = new URL(baseUrl, parametros.get("LDAP_ENDPOINT").getDescripcion());
		
		service = new WsEmployeeCatalog(url, new QName(parametros.get("LDAP_TNS").getValor(), parametros.get("LDAP_WSNAME").getValor()));
		WsEmployeeCatalogSoapPort port = service.getWsEmployeeCatalogSoapPort();
		WsEmployeeCatalogGETEMPLOYEEResponse response = port.getemployee(employee);
		
		for(StEmployeesEmpleadoItem item:  response.getStemployees().getStEmployeesEmpleadoItem()){
			if(!StringUtils.isBlank(item.getCNumemp()) & item.getClaveError() != 2){
				usuario = new BajasOpUsuarioBean();
				CatRegionBean region = new CatRegionBean();
				usuario.setClaveEmpleado(item.getCNumemp());
				usuario.setApaterno(item.getCApPaterno());
				usuario.setAmaterno(item.getCApMaterno());
				usuario.setNombre(item.getCNombre());
//				region.setId(item.getCRegion());
				usuario.setBajasCatRegion(item.getCRegion());
				usuario.setDireccion(item.getCDireccion());
				usuario.setSubdireccion(item.getCSubdireccion());
				usuario.setGerencia(item.getCGerencia());
				usuario.setDepartamento(item.getCDepartamento());
				usuario.setPuesto(item.getCPuesto());	
			}else if(StringUtils.isBlank(item.getCNumemp()) & item.getClaveError() != 2){
				
			}
			
		
		}
	     } catch (MalformedURLException e) {
				throw new CfeException(e.getMessage(),e);
			}
		return usuario;
	}
	@Override
	public BajasOpUsuarioBean cargaInicial(Map<String,BajasCatParametrosBean> parametros, LoginBean login, SessionBean sessionBean, SessionScopeUser sessionScopeUser)throws CfeException {
		logger.info(Constants.APP_ID + "##########ENTRA A CARGA INICIAL############");
		//CONSULTA DE PARAMETROS LDAP
		WsEmployeeCatalogDOAUTHENTICATEResponse resp;
		StFilterEmployee filterEmployee = new StFilterEmployee();
		filterEmployee.setCNumemp(login.getUsername());
		logger.info(Constants.APP_ID + "username: " + login.getUsername());
		// Cuando la bandera QA esta activa se consulta el servicio web
		if(parametros.get(Constants.LOGIN_LDAP_ACTIVE).getValor().equalsIgnoreCase("TRUE")){
			logger.info(Constants.APP_ID + "##########LDAP ESTA ACTIVO#############");
			Crypt miCryipt = new Crypt(parametros.get(Constants.LOGIN_LDAP_ID_APP).getValor());
			StAuthenticateParameters param = new StAuthenticateParameters();
			param.setBaseUrl(parametros.get(Constants.LOGIN_LDAP_BASE_URL).getValor());
			param.setClaveApp(parametros.get(Constants.LOGIN_LDAP_CLAVE_APP).getValor());
			param.setHost(parametros.get(Constants.LOGIN_LDAP_HOST).getValor());
			param.setIdApp(parametros.get(Constants.LOGIN_LDAP_ID_APP).getValor());
			param.setNoEmpleado(login.getUsername());
			param.setPasswordEmpleado(miCryipt.encrypt(login.getCode().getBytes()));
			param.setPort(Integer.parseInt(parametros.get(Constants.LOGIN_LDAP_PORT).getValor()));
			try{
				logger.info(Constants.APP_ID + "##########ANTES DE INGRESAR A PERFORM LOGIN#############");
				resp = performLogin(param, parametros);
				logger.info(Constants.APP_ID + "######### Codigo LDAP: " + resp.getStauthenticate().getCodigo() +"##########");
			}catch(CfeException e){
			
				throw new BadCredentialsException(Constants.LOGIN_LDAP_ERROR);
			}
	
			if(resp.getStauthenticate().getCodigo().equals(Constants.S_UNO)){
				
				BajasOpUsuarioBean usuario = obtieneDetalleUsuario(filterEmployee, parametros);
				
				if(usuario == null){
					usuario = new BajasOpUsuarioBean();
					usuario.setClaveEmpleado(login.getUsername());
					usuario.setClaveUniversal(resp.getStauthenticate().getDescripcion());
					CatRegionBean region = new CatRegionBean();
//					region.setId("R00");
					usuario.setNombre("EXTERNO");
					usuario.setApaterno("");
					usuario.setAmaterno("");
					usuario.setBajasCatRegion("R00");
				}else{
					usuario.setClaveUniversal(resp.getStauthenticate().getDescripcion());
				}
				logger.info("Antes de validar si el usuario se encuentra registrado");
				
				Integer existeUsrCount = 0;
				switch(sessionScopeUser.getSociedad().getId()){
					case 1:{
						existeUsrCount = utileriasCfeDao.existeUsuario(usuario.getClaveEmpleado());
					}break;
					case 2:{
						existeUsrCount = utileriasCfeDipcelDao.existeUsuario(usuario.getClaveEmpleado());
					}break;
					case 3:{
						existeUsrCount = utileriasCfeSercotelDao.existeUsuario(usuario.getClaveEmpleado());
					}break;
					case 4:{
						existeUsrCount = utileriasCfeAmovDao.existeUsuario(usuario.getClaveEmpleado());
					}break;
				}
				if(existeUsrCount <= 0){
					throw new BadCredentialsException("El usuario no se encuentra registrado");
				
				}
				
				switch(sessionScopeUser.getSociedad().getId()){
					case 1:{
						usuario = utileriasCfeDao.obtieneUsuario(usuario);
					}break;
					case 2:{
						usuario = utileriasCfeDipcelDao.obtieneUsuario(usuario);
					}break;
					case 3:{
						usuario = utileriasCfeSercotelDao.obtieneUsuario(usuario);
					}break;
					case 4:{
						usuario = utileriasCfeAmovDao.obtieneUsuario(usuario);
					}break;
				}
				
				if(usuario.getId() != null && usuario.getId() != 0){
					
					switch(sessionScopeUser.getSociedad().getId()){
						case 1:{
							usuario.setFechaModificacion(utileriasCfeDao.obtenerFecha());
							usuarioDao.actualizaUsuario(usuario);
						}break;
						case 2:{
							usuario.setFechaModificacion(utileriasCfeDipcelDao.obtenerFecha());
							usuarioDipcelDao.actualizaUsuario(usuario);
						}break;
						case 3:{
							usuario.setFechaModificacion(utileriasCfeSercotelDao.obtenerFecha());
							usuarioSercotelDao.actualizaUsuario(usuario);
						}break;
						case 4:{
							usuario.setFechaModificacion(utileriasCfeAmovDao.obtenerFecha());
							usuarioAmovDao.actualizaUsuario(usuario);
						}break;
					}
					
				}
					return usuario;
				
			}else{
				
				throw new BadCredentialsException("Las credeciales de acceso son incorrectas.");
			}
		}else{
			BajasOpUsuarioBean usuario = new BajasOpUsuarioBean();
			usuario.setClaveEmpleado(login.getUsername());
			
			switch(sessionScopeUser.getSociedad().getId()){
				case 1:{
					usuario = utileriasCfeDao.obtieneUsuario(usuario);
				}break;
				case 2:{
					usuario = utileriasCfeDipcelDao.obtieneUsuario(usuario);
				}break;
				case 3:{
					usuario = utileriasCfeSercotelDao.obtieneUsuario(usuario);
				}break;
				case 4:{
					usuario = utileriasCfeAmovDao.obtieneUsuario(usuario);
				}break;
			}
			
			
			if(usuario.getId() != null && usuario.getId() != 0){
				
				switch(sessionScopeUser.getSociedad().getId()){
					case 1:{
						usuario.setFechaModificacion(utileriasCfeDao.obtenerFecha());
						///REVISAR
//						usuarioDao.actualizaUsuario(usuario);	
					}break;
					case 2:{
						usuario.setFechaModificacion(utileriasCfeDipcelDao.obtenerFecha());
						///REVISAR
//						usuarioDao.actualizaUsuario(usuario);	
					}break;
					case 3:{
						usuario.setFechaModificacion(utileriasCfeSercotelDao.obtenerFecha());
						///REVISAR
//						usuarioDao.actualizaUsuario(usuario);	
					}break;
					case 4:{
						usuario.setFechaModificacion(utileriasCfeAmovDao.obtenerFecha());
						///REVISAR
//						usuarioDao.actualizaUsuario(usuario);	
					}break;
				}
				
			}else{
				usuario = null;	
			}
			return usuario;
		}
	}
	
	@Override
	public BajasOpUsuarioBean obtieneUsuario(BajasOpUsuarioBean usuarioParam, SessionScopeUser sessionScopeUser){
		BajasOpUsuarioBean bajasOpUsuario = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				bajasOpUsuario = utileriasCfeDao.obtieneUsuario(usuarioParam);
			}break;
			case 2:{
				bajasOpUsuario = utileriasCfeDipcelDao.obtieneUsuario(usuarioParam);
			}break;
			case 3:{
				bajasOpUsuario = utileriasCfeSercotelDao.obtieneUsuario(usuarioParam);
			}break;
			case 4:{
				bajasOpUsuario = utileriasCfeAmovDao.obtieneUsuario(usuarioParam);
			}break;
	}
		return bajasOpUsuario;
	}

}
