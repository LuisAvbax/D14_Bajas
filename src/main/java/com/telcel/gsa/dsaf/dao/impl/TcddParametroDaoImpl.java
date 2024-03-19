package com.telcel.gsa.dsaf.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.dao.TccddParametroDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.TcddParametro;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class TcddParametroDaoImpl extends AbstractDaoImpl<TcddParametro, String> implements TccddParametroDao, Serializable{

	final static Logger logger = LoggerFactory.getLogger(TcddParametroDaoImpl.class);
	@Autowired
	transient DataSource dataSource;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasfeDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6575380815105921416L;
	/**
	 * 
	 */
	public TcddParametroDaoImpl(){
		super(TcddParametro.class);
	}
	
	
	@Override
	public void actualizaParametro(TcddParametroBean datos) throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSource.getConnection();
			qInsert.append("update t_cdd_parametro set valor=?"
					+ " where  nombre=?  ");
			
			preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setString(1, datos.getDato().getValor());
			preparedStatement.setString(2, datos.getDato().getNombreParam());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(),e);
			
		}catch(CfeException e){
			throw e;
		}finally{
			try {
				if(preparedStatement != null){
					preparedStatement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		
	}
	
	
	
	@Override
	
	public TcddParametroBean consultaParametro(TcddParametroBean datos)  throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<TcddParametro> datQry =new ArrayList<TcddParametro>();;
		
		try {
			conn = dataSource.getConnection();
		
		StringBuffer selectSQL = new StringBuffer("select nombre, valor "
				+ " from t_cdd_parametro where nombre = '");
			selectSQL.append(datos.getNombreParam()+"'" );
	
		logger.info(">>> Query parametro: " + selectSQL);
		preparedStatement = conn.prepareStatement(selectSQL.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			TcddParametro d= new TcddParametro();
			d.setNombreParam(rs.getString("nombre"));
			d.setValor(rs.getString("valor"));
			datQry.add(d);
			
		}
		datos.setDatQry(datQry);
		
		} catch (SQLException e) {
			throw new CfeException("Error en consulta de tabla TccddParametro", e);
		}
		finally{
			try {
			
				if(preparedStatement != null){
					preparedStatement.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		return datos;
	}
}
