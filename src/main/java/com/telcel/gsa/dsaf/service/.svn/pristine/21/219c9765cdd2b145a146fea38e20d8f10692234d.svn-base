package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.telcel.gsa.dsaf.bean.ResumenConceptoClasBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface ResumenConceptoService extends Serializable{
	
	public ResumenConceptoRegBean datosWhere(ResumenConceptoRegBean datos);
	public ResumenConceptoClasBean datosWhere(ResumenConceptoClasBean datos);
	public ResumenConceptoRegBean consultaConceptoRegion(ResumenConceptoRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ResumenConceptoRegBean consultaAjConceptoRegion(ResumenConceptoRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ResumenConceptoRegBean consultaConceptoNetos(ResumenConceptoRegBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumento(ResumenConceptoRegBean datos)throws IOException;
	
	public ResumenConceptoClasBean consultaConceptosClase(ResumenConceptoClasBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ResumenConceptoClasBean consultaAjConceptosClase(ResumenConceptoClasBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public ResumenConceptoClasBean consultaConceptosClaseNetos(ResumenConceptoClasBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException;
	public HSSFWorkbook generaDocumentoClase(ResumenConceptoClasBean datos)throws IOException;
	
}
