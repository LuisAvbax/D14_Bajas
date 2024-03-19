package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface BitacoraService extends Serializable{
	
	public HSSFWorkbook generaDocumento(List<BajasBitacoraBean> bitacoras);
	public void guardaBitacora(List<BajasBitacoraBean> bitacora, SessionScopeUser sessionData) throws SQLException;
	public List<BajasBitacoraBean> consultaBitacora (String tipoProc, SessionScopeUser sessionData);
	public void limpiaBitacora(String tipoProc, SessionScopeUser sessionData) throws SQLException;
	public Long idMax(SessionScopeUser sessionData);
	
}
