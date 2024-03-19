package com.telcel.gsa.dsaf.dao;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.entity.BajasOpRol;



public interface PerfilDao extends AbstractDao<BajasOpRol, Integer>, Serializable {
	
	public List<RolBean> obtenerPerfil(String name);
	public RolBean obtenerPerfilDesc(String name);
	public void actualizaPerfil(RolBean rol);
	public void eliminaPerfil(RolBean rol);
	public void agregaPerfil(RolBean rol);
}
