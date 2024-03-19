package com.telcel.gsa.dsaf.dipcel.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.dipcel.dao.CienDepreciadosDipselDao;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;

import org.apache.poi.EncryptedDocumentException;
import org.hibernate.Session;

import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CienDepreciadosDipselDaoImpl extends AbstractDaoImpl<BajasDepreciadosCien, Integer> implements CienDepreciadosDipselDao, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4189291921123365148L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(CienDepreciadosDipselDaoImpl.class);
	@Autowired
	transient DataSource dataSourceDipcel;

	public CienDepreciadosDipselDaoImpl(){
		super(BajasDepreciadosCien.class);
	}

	
@Override
public void guardaCienDpr(BajasDepreciadosCien dpr) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	Statement statement = null;
	try{
		
		conn = dataSourceDipcel.getConnection();

			String selectSQL = "insert into bajas_depreciados_cien "
					+ "  (mes, anio, fecha_carga, numero_activo,  "+
						" id_usuario, id_sociedad, subnumero, estado_registro) "
			+ " values ( " +
			dpr.getMes() +","+
			dpr.getAnio() +","+
			 "sysdate,'"+
			 dpr.getNumeroActivo()+"',"+
			 dpr.getIdUsuario()+","+
			 dpr.getIdSociedad()+",'"+
			 dpr.getSubnumero()+"',"+
			 dpr.getEstadoRegistro() + ")";
			
			preparedStatement = conn.prepareStatement(selectSQL.toString());
	        
	        int dato = preparedStatement.executeUpdate();
	
	
	}catch(CfeException e){
		throw e;
	}finally{
		
		try {
			if(statement != null) statement.close();
			if(preparedStatement != null){
				preparedStatement.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CfeException("fallo en metodo guardaCienDpr", e);
		}
	}
	
	
	
}


@Override
public void actualizaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException {
	StringBuilder qInsert = new StringBuilder();
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	Statement statement = null;
	try{
		
		conn = dataSourceDipcel.getConnection();
		qInsert.append("update bajas_depreciados_cien set estado_registro = ? where anio =  ? and mes = ? and numero_activo = ? and subnumero = ?");
			
			preparedStatement = conn.prepareStatement(qInsert.toString());
			
			for(BajasDepreciadosCien dpr: dprLst) {
				preparedStatement.setInt(1, Constants.UNO);
				preparedStatement.setInt(2, dpr.getAnio());
				preparedStatement.setInt(3, dpr.getMes());
				preparedStatement.setString(4, dpr.getNumeroActivo());
				preparedStatement.setString(5, dpr.getSubnumero());

				preparedStatement.addBatch();
				
				}
			
			int[] rows = preparedStatement.executeBatch();
			logger.info(">>>Registros actualizados: " + rows.length);
	
	}catch(CfeException e){
		throw e;
	}finally{
		
		try {
			if(statement != null) statement.close();
			if(preparedStatement != null){
				preparedStatement.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CfeException("fallo en metodo guardaCienDpr", e);
		}
	}
	
	
	
}


public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes) throws CfeException {
	BajasDepreciadosCienBean datosDpr = new BajasDepreciadosCienBean();
	Connection conn = null;
	BajasDepreciadosCienBean datoBeans = null;
	PreparedStatement preparedStatement = null;
	List<BajasDepreciadosCienBean> encontrados = null;
		try{	
			conn = dataSourceDipcel.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, "
					+ " c.anio anio,"
					+ " c.numero_activo numeroActivo, c.subnumero, c.estado_registro"
					+ " from bajas_depreciados_cien c "
					+ " inner join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");
					selectSQL.append( " where c.mes = ? ");
					selectSQL.append( " and c.anio = ? ");
					selectSQL.append( " and s.id_sociedad = ? ");
					
					
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			
			preparedStatement.setInt(1, mes);
			preparedStatement.setInt(2, anio);
			preparedStatement.setInt(3, Constants.UNO);
            
            ResultSet rs = preparedStatement.executeQuery();
			encontrados = new LinkedList<BajasDepreciadosCienBean>();
            while (rs.next()) {
            	datoBeans= new BajasDepreciadosCienBean();
            	datoBeans.setIdMes(rs.getInt("mes"));
            	datoBeans.setAnio(rs.getInt("anio"));
            	datoBeans.setNumeroActivo(rs.getString("numeroActivo"));
            	datoBeans.setSubnumero(rs.getString("subnumero"));
            	datoBeans.setEstadoRegistro(rs.getInt("estado_registro"));
            	encontrados.add(datoBeans);
            }
	}catch(Exception e){
		throw new CfeException(e.getMessage(), e);
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
		
		return encontrados;

}


@Override
public void guardaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException {
	StringBuilder qInsert = new StringBuilder();
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	Statement statement = null;
	try{
		
		conn = dataSourceDipcel.getConnection();
		qInsert.append("insert into bajas_depreciados_cien (mes,anio,fecha_carga,numero_activo,"
				+ "id_usuario,id_sociedad,subnumero,id_usuariomod, fechamod, estado_registro) "
				+ "values (?,?,sysdate,?,?,?,?,null,null,?)");
			
			preparedStatement = conn.prepareStatement(qInsert.toString());
			
			for(BajasDepreciadosCien dpr: dprLst) {
				preparedStatement.setInt(1, dpr.getMes());
				preparedStatement.setInt(2, dpr.getAnio());
				preparedStatement.setString(3, dpr.getNumeroActivo());
				preparedStatement.setInt(4,  dpr.getIdUsuario());
				preparedStatement.setInt(5, dpr.getIdSociedad());
				preparedStatement.setString(6, dpr.getSubnumero());
				preparedStatement.setInt(7, Constants.UNO);
				preparedStatement.addBatch();
				
				}
			
			int[] rows = preparedStatement.executeBatch();
			logger.info(">>>Registros dados de alta: " + rows.length);
	
	}catch(CfeException e){
		throw e;
	}finally{
		
		try {
			if(statement != null) statement.close();
			if(preparedStatement != null){
				preparedStatement.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CfeException("fallo en metodo guardaCienDpr", e);
		}
	}
	
	
	
}

public List<BajasDepreciadosCienBean> consultaCienDpr(BajasDprCienFormBean datos, SessionScopeUser sessionData) throws CfeException {
	List<BajasDepreciadosCienBean> datosDpr = new ArrayList<BajasDepreciadosCienBean>();
	Connection conn = null;
	PreparedStatement preparedStatement = null; 
		try{	
			conn = dataSourceDipcel.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, c.anio anio, c.fecha_carga fechaCarga, "
					+ " c.numero_activo numeroActivo, u.usuario_apaterno paterno,"
					+ " u.usuario_amaterno materno, u.usuario_nombre nombre, s.desc_sociedad sociedad, c.subnumero subnumero "
					+ " from bajas_depreciados_cien c "
					+ " join bajas_op_usuario u on (u.usuario_id = c.id_usuario) "
					+ " join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");
					
					selectSQL.append( " where c.id_sociedad =");
					selectSQL.append(sessionData.getSociedad().getId());
					if (!datos.getMes().equals(null))
					selectSQL.append( " and  c.mes =   ");
					selectSQL.append(datos.getMes().getId());
					if(!datos.getAnio().equals(null))
					selectSQL.append( " and c.anio =  ");
					selectSQL.append(datos.getAnio());
					selectSQL.append( " and c.estado_registro =  ");
					selectSQL.append(Constants.UNO);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
            
            ResultSet rs = preparedStatement.executeQuery();
			
            while (rs.next()) {
            	BajasDepreciadosCienBean datoBeans= new BajasDepreciadosCienBean();
            	switch(rs.getInt("mes"))
            		{
            			case 1:
            				datoBeans.setDescMes("ENERO");
            			break;
            			case 2:
            				datoBeans.setDescMes("FEBRERO");
                		break;
            			case 3:
            				datoBeans.setDescMes("MARZO");
                		break;
            			case 4:
            				datoBeans.setDescMes("ABRIL");
                		break;
            			case 5:
            				datoBeans.setDescMes("MAYO");
                		break;
            			case 6:
            				datoBeans.setDescMes("JUNIO");
                		break;
            			case 7:
            				datoBeans.setDescMes("JULIO");
                		break;
            			case 8:
            				datoBeans.setDescMes("AGOSTO");
                		break;
            			case 9:
            				datoBeans.setDescMes("SEPTIEMBRE");
                		break;
            			case 10:
            				datoBeans.setDescMes("OCTUBRE");
                		break;
            			case 11:
            				datoBeans.setDescMes("NOVIEMBRE");
                		break;
            			case 12:
            				datoBeans.setDescMes("DICIEMBRE");
                		break;
            		}	
            	datoBeans.setIdMes(rs.getInt("mes"));
            	datoBeans.setAnio(rs.getInt("anio"));
            	datoBeans.setFechaCarga(rs.getDate("fechaCarga"));
            	datoBeans.setNumeroActivo(rs.getString("numeroActivo"));
            	datoBeans.setSubnumero(rs.getString("subnumero"));
            	datoBeans.setaPaterno(rs.getString("paterno"));
            	datoBeans.setaMaterno(rs.getString("materno"));
            	datoBeans.setNombre(rs.getString("nombre"));
            	datoBeans.setDescSociedad(rs.getString("sociedad"));
            	datoBeans.setNombreCompleto(datoBeans.getaPaterno()+" "+datoBeans.getaMaterno()+" "+datoBeans.getNombre());
            	
            	datosDpr.add(datoBeans);
            }
	}catch(Exception e){
		throw new CfeException(e.getMessage(), e);
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
		
		return datosDpr;

}

public BajasDepreciadosCienBean consultaCienDprValida(BajasDepreciadosCienBean datos) throws CfeException {
	BajasDepreciadosCienBean datosDpr = new BajasDepreciadosCienBean();
	Connection conn = null;
	PreparedStatement preparedStatement = null; 
		try{	
			conn = dataSourceDipcel.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, c.anio anio, c.fecha_carga fechaCarga, "
					+ " c.numero_activo numeroActivo, c.subnumero,  u.usuario_apaterno paterno,"
					+ " u.usuario_amaterno materno, u.usuario_nombre nombre, s.desc_sociedad sociedad "
					+ " from bajas_depreciados_cien c "
					+ " join bajas_op_usuario u on (u.usuario_id = c.id_usuario) "
					+ " join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");
					
					selectSQL.append( " where c.numero_activo =   ");
					selectSQL.append(datos.getNumeroActivo());
					selectSQL.append( " and c.subnumero =   '");
					selectSQL.append(datos.getSubnumero());
					selectSQL.append( "' and c.estado_registro = 1 ");
					
					
			preparedStatement = conn.prepareStatement(selectSQL.toString());
            
            ResultSet rs = preparedStatement.executeQuery();
			
            while (rs.next()) {
            	BajasDepreciadosCienBean datoBeans= new BajasDepreciadosCienBean();
            	switch(rs.getInt("mes"))
        		{
        			case 1:
        				datoBeans.setDescMes("ENERO");
        			break;
        			case 2:
        				datoBeans.setDescMes("FEBRERO");
            		break;
        			case 3:
        				datoBeans.setDescMes("MARZO");
            		break;
        			case 4:
        				datoBeans.setDescMes("ABRIL");
            		break;
        			case 5:
        				datoBeans.setDescMes("MAYO");
            		break;
        			case 6:
        				datoBeans.setDescMes("JUNIO");
            		break;
        			case 7:
        				datoBeans.setDescMes("JULIO");
            		break;
        			case 8:
        				datoBeans.setDescMes("AGOSTO");
            		break;
        			case 9:
        				datoBeans.setDescMes("SEPTIEMBRE");
            		break;
        			case 10:
        				datoBeans.setDescMes("OCTUBRE");
            		break;
        			case 11:
        				datoBeans.setDescMes("NOVIEMBRE");
            		break;
        			case 12:
        				datoBeans.setDescMes("DICIEMBRE");
            		break;
        		}
            	datoBeans.setIdMes(rs.getInt("mes"));
            	datoBeans.setAnio(rs.getInt("anio"));
            	datoBeans.setFechaCarga(rs.getDate("fechaCarga"));
            	datoBeans.setNumeroActivo(rs.getString("numeroActivo"));
            	datoBeans.setSubnumero(rs.getString("subnumero"));
            	datoBeans.setaPaterno(rs.getString("paterno"));
            	datoBeans.setaMaterno(rs.getString("materno"));
            	datoBeans.setNombre(rs.getString("nombre"));
            	datoBeans.setDescSociedad(rs.getString("sociedad"));
            	datoBeans.setNombreCompleto(datoBeans.getaPaterno()+" "+datoBeans.getaMaterno()+" "+datoBeans.getNombre());
            	
            	datosDpr= datoBeans;
            }
	}catch(Exception e){
		throw new CfeException(e.getMessage(), e);
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
		
		return datosDpr;

}




public List<BajasDepreciadosCienBean> getDepreciados() throws CfeException {
	BajasDepreciadosCienBean datosDpr = new BajasDepreciadosCienBean();
	Connection conn = null;
	BajasDepreciadosCienBean datoBeans = null;
	PreparedStatement preparedStatement = null;
	List<BajasDepreciadosCienBean> encontrados = null;
		try{	
			conn = dataSourceDipcel.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, "
//					+ " case when c.mes=1 then 'ENERO'"
//					+ " when c.mes=2 then 'FEBRERO'"
//					+ " when c.mes=3 then 'MARZO'"
//					+ " when c.mes=4 then 'ABRIL' "
//					+ " when c.mes=5 then 'MAYO'"
//					+ " when c.mes=6 then 'JUNIO'"
//					+ " when c.mes=7 then 'JULIO'"
//					+ " when c.mes=8 then 'AGOSTO'"
//					+ " when c.mes=9 then 'SEPTIEMBRE' "
//					+ " when c.mes=10 then 'OCTUBRE'"
//					+ " when c.mes=11 then 'NOVIEMBRE'"
//					+ " when c.mes=12 then 'DICIEMBRE'"
//					+ " else null end mestxt,"
					+ " c.anio anio,"
//					+ " c.anio anio, c.fecha_carga fechaCarga,"
					+ " c.numero_activo numeroActivo, c.subnumero"
//					+ " c.numero_activo numeroActivo, c.subnumero,  u.usuario_apaterno paterno,"
//					+ " u.usuario_amaterno materno, u.usuario_nombre nombre, s.desc_sociedad sociedad "
					+ " from bajas_depreciados_cien c "
//					+ " inner join bajas_op_usuario u on (u.usuario_id = c.id_usuario) "
					+ " inner join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");
					selectSQL.append( " where c.estado_registro = 1 ");
					selectSQL.append( " and s.id_sociedad = 1 ");
					
					
			preparedStatement = conn.prepareStatement(selectSQL.toString());
            
            ResultSet rs = preparedStatement.executeQuery();
			encontrados = new LinkedList<BajasDepreciadosCienBean>();
            while (rs.next()) {
            	datoBeans= new BajasDepreciadosCienBean();
            	datoBeans.setIdMes(rs.getInt("mes"));
            	datoBeans.setAnio(rs.getInt("anio"));
            	datoBeans.setNumeroActivo(rs.getString("numeroActivo"));
            	datoBeans.setSubnumero(rs.getString("subnumero"));
            	encontrados.add(datoBeans);
            }
	}catch(Exception e){
		throw new CfeException(e.getMessage(), e);
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
		
		return encontrados;

}
@Override
public void deleteAssets(List<BajasDepreciadosCienBean> assetList, SessionScopeUser sessionData) {
	StringBuilder qInsert = new StringBuilder();
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	  try {
            conn = dataSourceDipcel.getConnection();
            
		
		qInsert.append("update bajas_depreciados_cien set estado_registro = ? where mes = ? and anio  = ? and numero_activo = ? and subnumero = ?");
		
		
		preparedStatement = conn.prepareStatement(qInsert.toString());
		
		for(BajasDepreciadosCienBean asset: assetList) {
		preparedStatement.setInt(1, Constants.CERO);
		preparedStatement.setInt(2, asset.getIdMes());
		preparedStatement.setInt(3, asset.getAnio());
		preparedStatement.setString(4, asset.getNumeroActivo());
		preparedStatement.setString(5,  asset.getSubnumero());
		preparedStatement.addBatch();
		
		}
		
		
		int[] rows = preparedStatement.executeBatch();
		logger.info(">>>Registros para baja: " + rows.length);
//		conn.commit();
	
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


@Override
public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
		throws CfeException, SQLException {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	logger.info("bytes: " + bytes);
	try {
		conn = dataSourceDipcel.getConnection();
		StringBuffer selectSQL = new StringBuffer("insert into bajas_op_documento (doc_id," + "doc_contenido,"
				+ "doc_tipo," + "doc_modulo," + "doc_descripcion," + "doc_nombre," + "doc_estatus,"
				+ "doc_fecha_creacion," + "doc_usuario_creacion," + "doc_estado_registro," + "doc_id_sociedad,"
				+ "doc_p_anio," + "doc_p_periodo) " + " values(?,?,?,?,?,?,1,sysdate,?,?,?,?,?)");

		logger.info(">>>Query insert: " + selectSQL);
		preparedStatement = conn.prepareStatement(selectSQL.toString());
		preparedStatement.setInt(1, 0);
		preparedStatement.setBytes(2, bytes);// BLOB
		preparedStatement.setString(3, "CARGA");
		preparedStatement.setString(4, "CARGACIENDEPRE");
		preparedStatement.setString(5, "CARGA CIEN DEPRECIADO");
		preparedStatement.setString(6, "CARGA_CIEN_DEPRE.xls");
		preparedStatement.setInt(7, usuarioId);
		preparedStatement.setInt(8, 1);
		preparedStatement.setInt(9, sociedadId);
		preparedStatement.setInt(10, anio);
		preparedStatement.setInt(11, mes);

		preparedStatement.executeUpdate();

	} catch (CfeException e) {
		throw e;
	} finally {
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
				// ejecute.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
}


@Override
public void insertaRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException {
	Connection conn = null;
	CallableStatement ejecute = null;
	try {

		conn = dataSourceDipcel.getConnection();

		ejecute = conn.prepareCall("call sp_bajas_documento (?,?,?)");
		ejecute.setInt(1, 1);
		ejecute.setInt(2, sociedadId);
		ejecute.setInt(3, usuarioId);
		ejecute.execute();
	} catch (SQLException e) {
		throw new SQLException(e);
	} catch (CfeException e) {

	} catch (EncryptedDocumentException e) {

	} finally {
		try {
			if (ejecute != null) {
				ejecute.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
}

}