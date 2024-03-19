package com.telcel.gsa.dsaf.sercotel.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.dao.BitacoraDao;
import com.telcel.gsa.dsaf.entity.BajasBitacora;
import com.telcel.gsa.dsaf.sercotel.dao.BitacoraSercotelDao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class BitacoraSercotelDaoImpl extends AbstractDaoImpl<BajasBitacora, Integer> implements BitacoraSercotelDao, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4189291921123365148L;
	/**
	 * 
	 */

	final static Logger logger = LoggerFactory.getLogger(BitacoraSercotelDaoImpl.class);
	@Autowired
	transient DataSource dataSourceSercotel;

	public BitacoraSercotelDaoImpl(){
		super(BajasBitacora.class);
	}

	
@Override
public void guardaBitacora(List<BajasBitacoraBean> bitacora) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;

	try{
		
		conn = dataSourceSercotel.getConnection();
		
		for (BajasBitacoraBean datos : bitacora) {
			
			StringBuffer selectSQL = new StringBuffer("insert into bajas_op_bitacora "
					+ " ( bitacora_id, bitacora_linea, bitacora_proceso, bitacora_descripcion,  "+
						" bitacora_usuario_creacion, bitacora_fecha_creacion, bitacora_usuario_modificacion, "+
						" bitacora_fecha_modificacion, bitacora_estado_registro )");
			selectSQL.append(" values ( ");

			selectSQL.append( datos.getBitacoraId() +",");
			selectSQL.append( datos.getBitacoraLinea() +",'");
			selectSQL.append( datos.getBitacoraProceso()+"','");
			selectSQL.append( datos.getBitacoraDescripcion()+"',");
			selectSQL.append( datos.getRfcOpUsuarioByBitacoraUsuarioCreacion()+",");
			selectSQL.append( "sysdate,");
			selectSQL.append( datos.getRfcOpUsuarioByBitacoraUsuarioModificacion()+",");
			selectSQL.append( datos.getBitacoraFechaModificacion()+",");
			selectSQL.append( 1);
			selectSQL.append( ")");
			
			
			preparedStatement = conn.prepareStatement(selectSQL.toString());
	        
	        int dato = preparedStatement.executeUpdate();
			
		}
		
		
	
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
			logger.error(e.getMessage(),e);
		}
	}
	
	
	
}


public List<BajasBitacoraBean> consultaBitacora(String tpoOperacion) throws CfeException {
	List<BajasBitacoraBean> errores = new ArrayList<BajasBitacoraBean>();
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	BajasBitacoraBean bitacora;
		
		try{	
			conn = dataSourceSercotel.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select bitacora_id, bitacora_linea, "
					+ " bitacora_proceso, bitacora_descripcion, "
					+ " bitacora_usuario_creacion, bitacora_fecha_creacion, bitacora_usuario_modificacion,"
					+ " bitacora_fecha_modificacion, bitacora_estado_registro, "
					+ " u.usuario_nombre||' '||u.usuario_apaterno||' '||u.usuario_amaterno usuario"
					+ " from bajas_op_bitacora b join bajas_op_usuario u on (u.usuario_id = bitacora_usuario_creacion)"
					+ " where bitacora_estado_registro =1"
					+ " and bitacora_proceso = '" );
			selectSQL.append(tpoOperacion);
			selectSQL.append("'");
			preparedStatement = conn.prepareStatement(selectSQL.toString());
            
            ResultSet rs = preparedStatement.executeQuery();
			
            while (rs.next()) {
            	bitacora= new BajasBitacoraBean();
                bitacora.setBitacoraId(rs.getInt("bitacora_id"));
                bitacora.setBitacoraLinea(rs.getLong("bitacora_linea"));
                bitacora.setBitacoraProceso(rs.getString("bitacora_proceso"));
                bitacora.setBitacoraDescripcion(rs.getString("bitacora_descripcion"));
                bitacora.setRfcOpUsuarioByBitacoraUsuarioCreacion(rs.getInt("bitacora_usuario_creacion"));
                bitacora.setBitacoraFechaCreacion(rs.getDate("bitacora_fecha_creacion"));
                bitacora.setRfcOpUsuarioByBitacoraUsuarioModificacion(rs.getInt("bitacora_usuario_modificacion"));
                bitacora.setBitacoraFechaModificacion(rs.getDate("bitacora_fecha_modificacion"));
                if (rs.getInt("bitacora_estado_registro") == 1)	
                	bitacora.setBitacoraEstadoRegistro(true);
                else
                	bitacora.setBitacoraEstadoRegistro(false);
                bitacora.setNombreUsuario(rs.getString("usuario"));
                errores.add(bitacora);
            }
	}catch(Exception e){
		throw new CfeException(e.getMessage(), e);
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
				throw new CfeException(e.getMessage(),e);
			}
		}
		return errores;

}

//@Override
public void limpiaBitacora(String tpoOperacion) throws SQLException {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	try{	
		conn = dataSourceSercotel.getConnection();
		//Session session = sessionFactory.openSession();
		StringBuffer selectSQL = new StringBuffer("DELETE FROM bajas_op_bitacora "
				+ " WHERE bitacora_proceso ='"
				+ tpoOperacion+"'"
				+ " AND bitacora_estado_registro =1");

		preparedStatement = conn.prepareStatement(selectSQL.toString());
        
        int dato = preparedStatement.executeUpdate();
	
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
			logger.error(e.getMessage(),e);
		}
	}
	
}


//@Override
public  Long idMax() {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	Long max = null ;
	try{	
		conn = dataSourceSercotel.getConnection();
		Session session = sessionFactory.openSession();
		StringBuffer selectSQL = new StringBuffer("select max(bitacora_id) maximo"
				+ " from bajas_op_bitacora ");
		
		preparedStatement = conn.prepareStatement(selectSQL.toString());
        
        ResultSet rs = preparedStatement.executeQuery();
		
        while (rs.next()) {
        	max = rs.getLong("maximo");
        }
        
	}catch(Exception e){
	throw new CfeException(e.getMessage(), e);
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
	return max;
}



}