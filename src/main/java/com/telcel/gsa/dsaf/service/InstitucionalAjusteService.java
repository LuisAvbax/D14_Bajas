package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.InstAjusteBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface InstitucionalAjusteService extends Serializable{
	
	public InstAjusteBean datosWhere(InstAjusteBean datos);
	public InstAjusteBean consultaInstitucionalClase(InstAjusteBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstAjusteBean consultaAjInstitucionalClase(InstAjusteBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumentoClase(InstAjusteBean datos)throws IOException;
	
}
