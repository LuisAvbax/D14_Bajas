package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.Map;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.LoginBean;
import com.telcel.gsa.dsaf.bean.SessionBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.sds.servicios.empleado.cliente.StAuthenticateParameters;
import com.telcel.sds.servicios.empleado.cliente.StFilterEmployee;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogDOAUTHENTICATEResponse;

/**
 * 
 * @author VI5XXAA
 *
 */
public interface LoginService extends Serializable{
	public WsEmployeeCatalogDOAUTHENTICATEResponse performLogin(StAuthenticateParameters param, Map<String,BajasCatParametrosBean> parametros)throws CfeException;
	public BajasOpUsuarioBean obtieneDetalleUsuario(StFilterEmployee filterEmployee,Map<String,BajasCatParametrosBean> parametros);
	public BajasOpUsuarioBean cargaInicial(Map<String,BajasCatParametrosBean> parametros, LoginBean login, SessionBean sessionBean, SessionScopeUser sessionScopeUser)throws CfeException;
	public BajasOpUsuarioBean obtieneUsuario(BajasOpUsuarioBean usuarioParam, SessionScopeUser sessionScopeUser);
	
}
