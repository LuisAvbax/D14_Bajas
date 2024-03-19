package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.InstClaBean;
import com.telcel.gsa.dsaf.bean.InstRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface InstitucionalService extends Serializable{
	
	public InstRegBean datosWhere(InstRegBean datos);
	public InstClaBean datosWhere(InstClaBean datos);
	public InstRegBean consultaInstitucionalRegion(InstRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstRegBean consultaAjInstitucionalRegion(InstRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstRegBean consultaInstitucionalRegNetos(InstRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstClaBean consultaInstitucionalClase(InstClaBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstClaBean consultaAjInstitucionalClase(InstClaBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public InstClaBean consultaInstitucionalClaNetos(InstClaBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(InstRegBean datos)throws IOException;
	public HSSFWorkbook generaDocumentoClase(InstClaBean datos)throws IOException;
	
}
