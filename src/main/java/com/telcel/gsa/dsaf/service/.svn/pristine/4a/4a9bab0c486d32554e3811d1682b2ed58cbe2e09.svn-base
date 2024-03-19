package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface ReporteDetalladoBaService extends Serializable{
	
	public ReporteDetalladoBean datosWhere(ReporteDetalladoBean datos);
	public ReporteDetalladoBean consultaReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteDetalladoBean consultaAjReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ReporteDetalladoBean consultaReporteDetalladoNetos(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(ReporteDetalladoBean datos)throws IOException;
	public void insertaArchivo(SessionScopeUser sessionScopeUser, byte[] bytes) throws CfeException, SQLException;
	public void registraSolicitud(SessionScopeUser sessionScopeUser, ReporteDetalladoBean reporteDetallado, String nombreDoc) throws CfeException, SQLException;
	public List<BajasOpDocumentoBean> consultaArchivo(SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public BajasOpDocumentoBean consultaArchivoId(SessionScopeUser sessionScopeUser, Integer id) throws CfeException, SQLException;
	public void generaArchivo(SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	
}
