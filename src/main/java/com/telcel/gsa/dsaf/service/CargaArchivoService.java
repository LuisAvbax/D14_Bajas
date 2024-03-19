package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.sql.SQLException;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface CargaArchivoService extends Serializable{
	

	public void executeSp(BajasDepreciadosCienBean carga, SessionScopeUser sessionData) throws SQLException;

	
	
}
