package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import com.telcel.gsa.dsaf.bean.ReporteDetalladoPorTipoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface ReporteDetalladoAjusteService extends Serializable{
	
	public ReporteDetalladoPorTipoBean datosWhere(ReporteDetalladoPorTipoBean datos);
	public ReporteDetalladoPorTipoBean consultaReporteDetalladoAjuste(ReporteDetalladoPorTipoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(ReporteDetalladoPorTipoBean datos)throws IOException;
	
	
}
