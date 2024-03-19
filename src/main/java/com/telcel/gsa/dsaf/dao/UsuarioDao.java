package com.telcel.gsa.dsaf.dao;

import java.io.Serializable;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.util.CfeException;

public interface UsuarioDao extends AbstractDao<BajasOpUsuario, Integer>, Serializable {
	
	public void insertaUsuario(BajasOpUsuarioBean usuario) throws CfeException;
	public void actualizaUsuario(BajasOpUsuarioBean usuario) throws CfeException;
}
