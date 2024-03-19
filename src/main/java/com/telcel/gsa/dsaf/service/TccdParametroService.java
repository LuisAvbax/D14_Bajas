package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
/**
 * 
 * @author VI5XXAA
 *
 */
public interface TccdParametroService extends Serializable{
	
public void actualiza(TcddParametroBean datos, SessionScopeUser sessionData);
public TcddParametroBean consulta(TcddParametroBean datos, SessionScopeUser sessionData)throws Exception;

}
