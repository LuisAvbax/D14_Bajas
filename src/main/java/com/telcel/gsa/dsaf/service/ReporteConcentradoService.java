package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface ReporteConcentradoService extends Serializable{
	
	public ReporteConcentradoBean datosWhere(ReporteConcentradoBean datos);
	public ReporteConcentradoBean consultaReporteConcentrado(ReporteConcentradoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteConcentradoBean consultaAjReporteConcentrado(ReporteConcentradoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteConcentradoBean consultaReporteConcentradoNetos(ReporteConcentradoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(ReporteConcentradoBean datos)throws IOException;
	
	
}
