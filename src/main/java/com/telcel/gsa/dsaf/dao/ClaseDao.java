package com.telcel.gsa.dsaf.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.telcel.gsa.dsaf.bean.ClaseBean;
import com.telcel.gsa.dsaf.entity.BajasClase;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;

public interface ClaseDao extends AbstractDao<BajasClase, String>, Serializable {
	
	public void insertaClase(ClaseBean inpc, SessionScopeUser sessionData) throws CfeException;
	public void actualizaClase(ClaseBean inpc, SessionScopeUser sessionData) throws CfeException;
	public void eliminaClase(ClaseBean inpc) throws CfeException;
	public List<BajasClase> consultaClase(ClaseBean inpc) throws CfeException;
	public List<BajasClase> consultaClaseValidar(ClaseBean inpc) throws CfeException;
	
	
}
