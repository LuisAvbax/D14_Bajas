package com.telcel.gsa.dsaf.amov.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.telcel.gsa.dsaf.action.CargaInsumosBajasAction;
import com.telcel.gsa.dsaf.amov.dao.CargaArchivoAmovDao;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.dao.CargaArchivoDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.BajasBitacora;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;



import com.telcel.gsa.dsaf.util.CfeException;

@Repository
public class CargaArchivoAmovDaoImpl extends AbstractDaoImpl<BajasBitacora, Integer> implements CargaArchivoAmovDao, Serializable{

	@Autowired
	transient DataSource dataSourceAmov;

	
	private static final long serialVersionUID = -4189291921123365148L;

	private static Logger logger = LoggerFactory.getLogger(CargaArchivoAmovDaoImpl.class);
	

	public CargaArchivoAmovDaoImpl(){
		super(BajasBitacora.class);
	}

	
	@Transactional(value = "transactionManager")
	@Override	
	public void executeSp(BajasDepreciadosCienBean carga) throws CfeException, SQLException {

	 CallableStatement conn = null;
	
	try {
		 
		 if (carga.getCargacumulado()==true)
			 carga.setCargaAcumulados(2);
		 else 
			 carga.setCargaAcumulados(0);
		 
		 String sqlQry ="CALL sp_bajasaf_carga(?)";
		 conn = (CallableStatement) dataSourceAmov.getConnection().prepareCall(sqlQry);
		 conn.setInt(1,carga.getCargaAcumulados());
		 conn.execute();
		
					
	}
	catch (SQLException e) {
		logger.error("SQL_ERROR: "+e.getMessage(),e);
		throw new CfeException("SE PRODUCE ERROR FAVOR DE VALIDAR CON EL ADMINISTRADOR ",e);
	}
	catch(CfeException e){
		logger.error("EXCEP_ERROR: "+e.getMessage(),e);
		throw new CfeException("SE PRODUCE ERROR FAVOR DE VALIDAR CON EL ADMINISTRADOR ",e);
		
		//throw e;
	} catch(Exception e){
		logger.error("EXCEP_ERROR: "+e.getMessage(),e);
		throw new CfeException("SE PRODUCE ERROR FAVOR DE VALIDAR CON EL ADMINISTRADOR ",e);
	}
	finally{
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("SQL_ERROR: "+e.getMessage(),e);
				throw new CfeException("SE PRODUCE ERROR FAVOR DE VALIDAR CON EL ADMINISTRADOR ",e);
			}
	}
		
		
		
	}






}