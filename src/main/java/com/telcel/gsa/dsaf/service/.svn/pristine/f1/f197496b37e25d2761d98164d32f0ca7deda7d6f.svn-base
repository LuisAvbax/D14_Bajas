package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.ClaseBean;
import com.telcel.gsa.dsaf.entity.BajasClase;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
/**
 * 
 * @author VI5XXAA
 *
 */
public interface ClaseService extends Serializable{
	
public void insertaClase(ClaseBean inpc, SessionScopeUser sessionData);
public void actualizaClase(ClaseBean inpc, SessionScopeUser sessionData);
public void eliminaClase(ClaseBean inpc, SessionScopeUser sessionData);
public List<BajasClase> consultaClase(ClaseBean inpc, SessionScopeUser sessionData)throws Exception;
public List<BajasClase> consultaClaseValida(ClaseBean inpc, SessionScopeUser sessionData)throws Exception;


}
