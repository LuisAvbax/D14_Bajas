package com.telcel.gsa.dsaf.sercotel.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasCatEstatusDocBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.sercotel.dao.ReporteCienDepreciadosSercotelDao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.telcel.gsa.dsaf.util.CfeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ReporteCienDepreciadosSercotelDaoImpl extends AbstractDaoImpl<BajasDepreciadosCien, Integer>
		implements ReporteCienDepreciadosSercotelDao, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(ReporteCienDepreciadosSercotelDaoImpl.class);

	@Autowired
	transient DataSource dataSourceSercotel;

	public ReporteCienDepreciadosSercotelDaoImpl() {
		super(BajasDepreciadosCien.class);
	}

	@Override
	public void insertaSolicReporte(Integer anio, Integer periodo, Integer sociedad, Integer usuario, String docTipo,
			String docModulo, String docDescripcion, String docNombre) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = dataSourceSercotel.getConnection();
			StringBuffer selectSQL = new StringBuffer(
					"INSERT INTO bajas_op_documento (doc_id, doc_contenido, doc_tipo, doc_modulo, doc_descripcion, doc_nombre,"
							+ " doc_estatus, doc_fecha_creacion, doc_usuario_creacion, doc_estado_registro, doc_id_sociedad, "
							+ "doc_p_anio, doc_p_periodo) " + "VALUES(0,?, ?, ?, ? , ? , ?, sysdate, ? , ?, ?, ?, ?) ");

			preparedStatement = conn.prepareStatement(selectSQL.toString());
			preparedStatement.setBytes(1, null);
			preparedStatement.setString(2, docTipo);
			preparedStatement.setString(3, docModulo);
			preparedStatement.setString(4, docDescripcion);
			preparedStatement.setString(5, docNombre);
			preparedStatement.setInt(6, 1);
			preparedStatement.setInt(7, usuario);
			preparedStatement.setInt(8, 1);
			preparedStatement.setInt(9, sociedad);
			preparedStatement.setInt(10, anio);
			preparedStatement.setInt(11, periodo);

			preparedStatement.executeUpdate();
		} catch (CfeException e) {
			throw e;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	@Override
	public BajasOpDocumentoBean obtenerReporteCienDepre(String docTipo, String docModulo, Integer idSociedad,
			Integer anio, Integer periodo, Integer usuarioCreacion) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		BajasOpDocumentoBean documento = null;
		ResultSet rs = null;
		try {
			conn = dataSourceSercotel.getConnection();
			StringBuffer selectSQL = new StringBuffer(
					"SELECT doc_id, doc_tipo, doc_modulo, doc_descripcion, doc_nombre, doc_estatus, doc_fecha_creacion, doc_usuario_creacion,"
							+ "doc_estado_registro, doc_id_sociedad, doc_p_anio, doc_p_periodo ,u.usuario_nombre,u.usuario_apaterno,u.usuario_amaterno ,e.estatus_descripcion "
							+ "FROM bajas_op_documento d "
							+ "inner join bajas_op_usuario u on d.doc_usuario_creacion = u.usuario_id "
							+ "inner join bajas_cat_estatus_doc e on d.doc_estatus  = e.estatus_id where doc_tipo=? and doc_modulo=? and doc_id_sociedad=? and doc_p_anio=? "
							+ "and doc_p_periodo=? and doc_usuario_creacion=?");

			preparedStatement = conn.prepareStatement(selectSQL.toString());
			preparedStatement.setString(1, docTipo);
			preparedStatement.setString(2, docModulo);
			preparedStatement.setInt(3, idSociedad);
			preparedStatement.setInt(4, anio);
			preparedStatement.setInt(5, periodo);
			preparedStatement.setInt(6, usuarioCreacion);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				documento = new BajasOpDocumentoBean();
				documento.setId(rs.getInt("doc_id"));
				documento.setTipo(rs.getString("doc_tipo"));
				documento.setModulo(rs.getString("doc_modulo"));
				documento.setDescripcion(rs.getString("doc_descripcion"));
				documento.setNombre(rs.getString("doc_nombre"));
				BajasCatEstatusDocBean estatus = new BajasCatEstatusDocBean();
				estatus.setId(rs.getInt("doc_estatus"));
				estatus.setDescripcion(rs.getString("estatus_descripcion"));
				BajasOpUsuarioBean usuario = new BajasOpUsuarioBean();
				usuario.setId(rs.getInt("doc_usuario_creacion"));
				usuario.setNombre(rs.getString("usuario_nombre"));
				usuario.setNombre(rs.getString("usuario_nombre"));
				usuario.setAmaterno(rs.getString("usuario_amaterno"));
				usuario.setApaterno(rs.getString("usuario_apaterno"));
				documento.setUsuario_creacion(usuario);
				documento.setEstatus(estatus);
				documento.setFecha_creacion(rs.getTimestamp("doc_fecha_creacion"));
				documento.setIdSociedad(rs.getInt("doc_id_sociedad"));
				documento.setAnio(rs.getInt("doc_p_anio"));
				documento.setPeriodo(rs.getInt("doc_p_periodo"));
			}
		} catch (CfeException e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
		return documento;
	}

	@Override
	public void spBajasDoc(Integer procesoId, Integer usuarioId, Integer sociedadId) throws CfeException, SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		try {
			conn = dataSourceSercotel.getConnection();
			StringBuffer SQL = new StringBuffer("call sp_bajas_documento(?,?,?)");
			cs = conn.prepareCall(SQL.toString());
			cs.setInt(1, procesoId);
			cs.setInt(2, sociedadId);
			cs.setInt(3, usuarioId);
			cs.execute();
			logger.info("Se termina de ejecutar el sp");

		} catch (CfeException e) {
			throw e;
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public Workbook obtieneReporte(Integer docId) throws CfeException, SQLException, IOException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Workbook wb = null;
		try {
			conn = dataSourceSercotel.getConnection();
			StringBuffer selectSQL = new StringBuffer("SELECT doc_contenido FROM bajas_op_documento where doc_id=?");

			preparedStatement = conn.prepareStatement(selectSQL.toString());
			preparedStatement.setInt(1, docId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getBinaryStream("doc_contenido") != null) {
					wb = new XSSFWorkbook(rs.getBinaryStream("doc_contenido"));
				}
			}
		} catch (CfeException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
		return wb;
	}

	@Override
	public BajasOpDocumentoBean obtenerReporteCienDepreReciente(String docTipo, String docModulo, Integer idSociedad,
			Integer usuarioCreacion) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		BajasOpDocumentoBean documento = null;
		ResultSet rs = null;
		try {
			conn = dataSourceSercotel.getConnection();
			StringBuffer selectSQL = new StringBuffer(
					"SELECT FIRST 1 doc_id, doc_tipo, doc_modulo, doc_descripcion, doc_nombre, doc_estatus, doc_fecha_creacion, doc_usuario_creacion,"
							+ "doc_estado_registro, doc_id_sociedad, doc_p_anio, doc_p_periodo ,u.usuario_nombre,u.usuario_apaterno,u.usuario_amaterno ,e.estatus_descripcion "
							+ "FROM bajas_op_documento d "
							+ "inner join bajas_op_usuario u on d.doc_usuario_creacion = u.usuario_id "
							+ "inner join bajas_cat_estatus_doc e on d.doc_estatus  = e.estatus_id where doc_tipo=? and doc_modulo=? "
							+ "and doc_id_sociedad=? and doc_usuario_creacion=? order by doc_id desc ");

			preparedStatement = conn.prepareStatement(selectSQL.toString());
			preparedStatement.setString(1, docTipo);
			preparedStatement.setString(2, docModulo);
			preparedStatement.setInt(3, idSociedad);
			preparedStatement.setInt(4, usuarioCreacion);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				documento = new BajasOpDocumentoBean();
				documento.setId(rs.getInt("doc_id"));
				documento.setTipo(rs.getString("doc_tipo"));
				documento.setModulo(rs.getString("doc_modulo"));
				documento.setDescripcion(rs.getString("doc_descripcion"));
				documento.setNombre(rs.getString("doc_nombre"));
				BajasCatEstatusDocBean estatus = new BajasCatEstatusDocBean();
				estatus.setId(rs.getInt("doc_estatus"));
				estatus.setDescripcion(rs.getString("estatus_descripcion"));
				BajasOpUsuarioBean usuario = new BajasOpUsuarioBean();
				usuario.setId(rs.getInt("doc_usuario_creacion"));
				usuario.setNombre(rs.getString("usuario_nombre"));
				usuario.setNombre(rs.getString("usuario_nombre"));
				usuario.setAmaterno(rs.getString("usuario_amaterno"));
				usuario.setApaterno(rs.getString("usuario_apaterno"));
				documento.setUsuario_creacion(usuario);
				documento.setEstatus(estatus);
				documento.setFecha_creacion(rs.getTimestamp("doc_fecha_creacion"));
				documento.setIdSociedad(rs.getInt("doc_id_sociedad"));
				documento.setAnio(rs.getInt("doc_p_anio"));
				documento.setPeriodo(rs.getInt("doc_p_periodo"));
			}
		} catch (CfeException e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
		return documento;
	}
}