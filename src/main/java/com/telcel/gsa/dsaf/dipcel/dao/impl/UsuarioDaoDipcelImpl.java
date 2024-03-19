package com.telcel.gsa.dsaf.dipcel.dao.impl;



import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.dao.UsuarioDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.util.CfeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class UsuarioDaoDipcelImpl extends AbstractDaoImpl<BajasOpUsuario, Integer> implements UsuarioDao, Serializable{
	final static Logger logger = LoggerFactory.getLogger(UsuarioDaoDipcelImpl.class);
	@Autowired
	transient DataSource dataSourceDipcel;
	private static final long serialVersionUID = -6575380815105921416L;
	/**
	 * 
	 */
	public UsuarioDaoDipcelImpl(){
		super(BajasOpUsuario.class);
	}
	

	@Transactional(value = "transactionManager")
	@Override
	public void insertaUsuario(BajasOpUsuarioBean usuario) throws CfeException{
	
		StringBuilder qInsert = new StringBuilder();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		  try {
	            conn = dataSourceDipcel.getConnection();
              
			qInsert.append("insert into bajas_op_usuario (USUARIO_ID, USUARIO_CLAVE_EMPLEADO, USUARIO_CLAVE_UNIVERSAL, "
					+ "USUARIO_ROL_ID,  USUARIO_REGION_ID, USUARIO_NOMBRE, USUARIO_APATERNO, "
					+ "USUARIO_AMATERNO, USUARIO_USUARIO_CREACION, USUARIO_FECHA_CREACION, "
					+ "USUARIO_USUARIO_MODIFICACION, "
					+ "USUARIO_FECHA_MODIFICACION, USUARIO_ESTADO_REGISTRO, USUARIO_CORREO  ) "
					+" values ((select max(usuario_id)+1 from bajas_op_usuario),?,?,?,?,?,?,"
					+ "?,?,sysdate,null,null,1,"
					+ "?)");
	
			preparedStatement = conn.prepareStatement(qInsert.toString());
//			preparedStatement.setInt(1,usuario.getId());
			preparedStatement.setString(1, usuario.getClaveEmpleado());
			preparedStatement.setString(2, usuario.getClaveUniversal());
			preparedStatement.setInt(3,usuario.getBajasOpRol().getId());
			preparedStatement.setInt(4,usuario.getBajasRegion().getId());
			preparedStatement.setString(5, usuario.getNombre());
			preparedStatement.setString(6, usuario.getApaterno());
			preparedStatement.setString(7, usuario.getAmaterno());
			preparedStatement.setInt(8,usuario.getUsuarioCreacion());
			preparedStatement.setString(9, usuario.getCorreo());
			
			 preparedStatement.executeUpdate();
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(),e);
		}finally{
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
		
	}
	
	

	@Transactional(value = "transactionManager")
	@Override
	public void actualizaUsuario(BajasOpUsuarioBean usuario) throws CfeException{
		
		Query query = null;
		StringBuilder qInsert = new StringBuilder();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		  try {
	            conn = dataSourceDipcel.getConnection();
	            
			qInsert.append("update bajas_op_usuario set   USUARIO_CLAVE_EMPLEADO=? "
					+ ",USUARIO_CLAVE_UNIVERSAL=? ,USUARIO_ROL_ID=? "
					+ ",USUARIO_REGION_ID=:? ,USUARIO_NOMBRE=? "
					+ ",USUARIO_APATERNO=? ,USUARIO_AMATERNO=? "
					+ ",USUARIO_USUARIO_CREACION=:? ,USUARIO_FECHA_CREACION=sysdate "
					+ ",USUARIO_USUARIO_MODIFICACION=? "
					+ ",USUARIO_FECHA_MODIFICACION=sysdate ,USUARIO_ESTADO_REGISTRO=? "
					+ ",USUARIO_CORREO=?  "
					+ " where  USUARIO_ID=?");
			
			
			preparedStatement = conn.prepareStatement(qInsert.toString());

			preparedStatement.setString(1, usuario.getClaveEmpleado());
			preparedStatement.setString(2, usuario.getClaveUniversal());
			preparedStatement.setInt(3,usuario.getBajasOpRol().getId());
			preparedStatement.setInt(4,Integer.parseInt(usuario.getBajasCatRegion()));
			preparedStatement.setString(5, usuario.getNombre());
			preparedStatement.setString(6, usuario.getApaterno());
			preparedStatement.setString(7, usuario.getAmaterno());
			preparedStatement.setInt(8,usuario.getUsuarioCreacion());
			preparedStatement.setInt(9,usuario.getUsuarioModificacion());
			preparedStatement.setInt(10,usuario.getEstadoRegistro() == true? 1:0);
			preparedStatement.setString(11, usuario.getCorreo());
			preparedStatement.setInt(12,usuario.getId());			

			logger.info(">>>actualiza usuario: " + qInsert.toString());

			preparedStatement.executeUpdate();
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(),e);
		}finally{
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
		
	}
}
