package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;


import com.telcel.gsa.dsaf.security.SessionScopeUser;
/**
 * 
 * @author VI5XXAA
 *
 */
public interface UsuarioService extends Serializable{
public List<BajasOpUsuarioBean> buscaUsuarios(BajasOpUsuarioBean filtro, SessionScopeUser sessionData);
public BajasOpUsuarioBean obtieneRolUsuario(BajasOpUsuarioBean usuarioParam, SessionScopeUser sessionScopeUser)throws Exception;
public List<RolBean> obtieneRoles(SessionScopeUser sessionScopeUser);
public void guardaUsuario(BajasOpUsuarioBean usr,SessionScopeUser sessionScopeUser);
public void actualizaUsuario(BajasOpUsuarioBean usr, SessionScopeUser sessionScopeUser);
public CatRegionBean obtieneRegionUsuario(CatRegionBean region,SessionScopeUser sessionScopeUser);
public List<BajasOpUsuarioBean> buscaUsuariosRegistrados(UsuarioBean usuarioParam,SessionScopeUser sessionScopeUser) throws Exception;
public List<BajasOpUsuarioBean> buscaUsuariosWS(BajasOpUsuarioBean filtro, SessionScopeUser sessionData);
public void bajaUsuario(BajasOpUsuarioBean usr, SessionScopeUser sessionScopeUser);

}
