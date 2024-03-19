package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface AqdBajasService extends Serializable{
	
	public AdqBajasBean DatosWhere (AdqBajasBean datos);
	public AdqBajasBean ConsultaReg (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaRegAjus (AdqBajasBean datos , SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaRegNetos (AdqBajasBean datos , SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumento(AdqBajasBean datos)throws IOException;
	
	public AdqBajasBean ConsultaClas (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaClasAjus (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaClasNetos (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumentoClas(AdqBajasBean datos)throws IOException;
	
	public AdqBajasBean ConsultaText (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaTextAjus (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	public AdqBajasBean ConsultaTextNetos (AdqBajasBean datos, SessionScopeUser sessionData) throws CfeException, SQLException;
	
	public HSSFWorkbook generaDocumentoText(AdqBajasBean datos)throws IOException;
	
}
