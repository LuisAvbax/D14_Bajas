package com.telcel.gsa.dsaf.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.telcel.gsa.dsaf.bean.GeneraRespaldoBean;
import com.telcel.gsa.dsaf.dao.RespaldoDao;
import com.telcel.gsa.dsaf.entity.BajasOpRespaldo;
import com.telcel.gsa.dsaf.security.SessionScopeUser;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.sql.DataSource;



import com.telcel.gsa.dsaf.util.CfeException;

@Repository
public class RespaldoDaoImpl extends AbstractDaoImpl<BajasOpRespaldo, Integer> implements RespaldoDao, Serializable{

	@Autowired
	transient DataSource dataSource;

	
	private static final long serialVersionUID = -4189291921123365148L;

	private static Logger logger = LoggerFactory.getLogger(RespaldoDaoImpl.class);
	

	public RespaldoDaoImpl(){
		super(BajasOpRespaldo.class);
	}

	
	@Transactional(value = "transactionManager")
	@Override	
	public void ejecutaRespaldo(GeneraRespaldoBean respaldo, SessionScopeUser sessionData)throws CfeException ,SQLException {

	 CallableStatement conn = null;
	
	try {
		 
		 
		 String sqlQry ="CALL sp_bajas_admin_respaldo(?,?,?,?)";
		 conn = (CallableStatement) dataSource.getConnection().prepareCall(sqlQry);
		 conn.setString(1,respaldo.getMensaje());
		 conn.setInt(2,respaldo.getIdSociedad());
		 conn.setInt(3,respaldo.getIdUsuario());
		 conn.setInt(4,2);
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