package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.TotalGlobalClaBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface TotalGlobalService extends Serializable{
	
	public TotalGlobalRegBean datosWhere(TotalGlobalRegBean datos);
	public TotalGlobalClaBean datosWhere(TotalGlobalClaBean datos);
	public TotalGlobalRegBean consultaTotalGlobalRegion(TotalGlobalRegBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public TotalGlobalRegBean consultaAjTotalGlobalRegion(TotalGlobalRegBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public TotalGlobalRegBean consultaTGlobalRegNetos(TotalGlobalRegBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaTotalGlobalClase(TotalGlobalClaBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaAjTotalGlobalClase(TotalGlobalClaBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaTGlobalClaNetos(TotalGlobalClaBean datos,SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(TotalGlobalRegBean datos)throws IOException;
	public HSSFWorkbook generaDocumentoClase(TotalGlobalClaBean datos)throws IOException;
	
}
