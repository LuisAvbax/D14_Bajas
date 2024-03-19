package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface ReporteDetalladoService extends Serializable{
	
	public ReporteDetalladoBean datosWhere(ReporteDetalladoBean datos);
	public ReporteDetalladoBean consultaReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteDetalladoBean consultaAjReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteDetalladoBean consultaReporteDetalladoNetos(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(ReporteDetalladoBean datos)throws IOException;
	
	
}
