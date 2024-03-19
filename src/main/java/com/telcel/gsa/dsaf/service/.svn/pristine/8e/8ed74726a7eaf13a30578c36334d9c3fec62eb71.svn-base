package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface DepreAnioActService extends Serializable{
	
	public DepreActBean datosWhere (DepreActBean datos);
	
	public DepreActBean consultaReg(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaRegAjus (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaRegNetos (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;

	
	public HSSFWorkbook generaDocumento(DepreActBean datos)throws IOException;
	
	public DepreActBean consultaClas (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaClasAjus (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaClasNetos (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;

	
	public HSSFWorkbook generaDocumentoClas(DepreActBean datos)throws IOException;
	
	public DepreActBean consultaText (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaTextAjus (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;
	public DepreActBean consultaTextNetos (DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException;

	
	public HSSFWorkbook generaDocumentoText(DepreActBean datos)throws IOException;
	
}
