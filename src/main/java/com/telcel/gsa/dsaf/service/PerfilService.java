package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.RolBean;




/**
 * 
 * @author VI5XXAA
 *
 */
public interface PerfilService extends Serializable{

	public List<RolBean> obtenerPerfiles(String name);
	public void actualizaPerfil(RolBean rol, BajasOpUsuarioBean usuarioMov);
	public void eliminaPerfil(RolBean rol, BajasOpUsuarioBean usuarioMov);
	public void agregaPerfil(RolBean rol, BajasOpUsuarioBean usuarioMov);
}
