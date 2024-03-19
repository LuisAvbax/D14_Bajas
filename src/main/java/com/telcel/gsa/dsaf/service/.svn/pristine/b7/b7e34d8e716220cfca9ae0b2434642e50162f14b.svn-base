package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface CostoService extends Serializable{
	
	public CostoBean DatosWhere (CostoBean datos);
	public CostoBean ConsultaReg (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaRegAjus (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaRegNetos (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumento(CostoBean datos)throws IOException;
	
	public CostoBean ConsultaClas (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaClasAjus (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaClasNetos (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumentoClas(CostoBean datos)throws IOException;
	
	public CostoBean ConsultaText (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaTextAjus (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public CostoBean ConsultaTextNetos (CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumentoText(CostoBean datos)throws IOException;
	
}
