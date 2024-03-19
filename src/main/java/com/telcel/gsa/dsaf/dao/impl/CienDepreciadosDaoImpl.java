package com.telcel.gsa.dsaf.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasCatEstatusDocBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.dao.CienDepreciadosDao;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CienDepreciadosDaoImpl extends AbstractDaoImpl<BajasDepreciadosCien, Integer>
		implements CienDepreciadosDao, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4189291921123365148L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(CienDepreciadosDaoImpl.class);
	@Autowired
	transient DataSource dataSource;

	public CienDepreciadosDaoImpl() {
		super(BajasDepreciadosCien.class);
	}

	@Override
	public void guardaCienDpr(BajasDepreciadosCien dpr) throws CfeException, SQLException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		try {

			conn = dataSource.getConnection();

			String selectSQL = "insert into bajas_depreciados_cien " + "  (mes, anio, fecha_carga, numero_activo,  "
					+ " id_usuario, id_sociedad, subnumero, estado_registro) " + " values ( " + dpr.getMes() + ","
					+ dpr.getAnio() + "," + "sysdate,'" + dpr.getNumeroActivo() + "'," + dpr.getIdUsuario() + ","
					+ dpr.getIdSociedad() + ",'" + dpr.getSubnumero() + "'," + dpr.getEstadoRegistro() + ")";

			preparedStatement = conn.prepareStatement(selectSQL.toString());

			int dato = preparedStatement.executeUpdate();

		} catch (CfeException e) {
			throw e;
		} finally {

			try {
				if (statement != null)
					statement.close();
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException("fallo en metodo guardaCienDpr", e);
			}
		}

	}

	@Override
	public void guardaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException {
		StringBuilder qInsert = new StringBuilder();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		try {

			conn = dataSource.getConnection();
			qInsert.append("insert into bajas_depreciados_cien (mes,anio,fecha_carga,numero_activo,"
					+ "id_usuario,id_sociedad,subnumero,id_usuariomod, fechamod, estado_registro) "
					+ "values (?,?,sysdate,?,?,?,?,null,null,?)");

			preparedStatement = conn.prepareStatement(qInsert.toString());

			for (BajasDepreciadosCien dpr : dprLst) {
				preparedStatement.setInt(1, dpr.getMes());
				preparedStatement.setInt(2, dpr.getAnio());
				preparedStatement.setString(3, dpr.getNumeroActivo());
				preparedStatement.setInt(4, dpr.getIdUsuario());
				preparedStatement.setInt(5, dpr.getIdSociedad());
				preparedStatement.setString(6, dpr.getSubnumero());
				preparedStatement.setInt(7, Constants.UNO);
				preparedStatement.addBatch();

			}

			int[] rows = preparedStatement.executeBatch();
			logger.info(">>>Registros dados de alta: " + rows.length);

		} catch (CfeException e) {
			throw e;
		} finally {

			try {
				if (statement != null)
					statement.close();
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
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
		try {

			conn = dataSource.getConnection();
			qInsert.append(
					"update bajas_depreciados_cien set estado_registro = ? where anio =  ? and mes = ? and numero_activo = ? and subnumero = ?");

			preparedStatement = conn.prepareStatement(qInsert.toString());

			for (BajasDepreciadosCien dpr : dprLst) {
				preparedStatement.setInt(1, Constants.UNO);
				preparedStatement.setInt(2, dpr.getAnio());
				preparedStatement.setInt(3, dpr.getMes());
				preparedStatement.setString(4, dpr.getNumeroActivo());
				preparedStatement.setString(5, dpr.getSubnumero());

				preparedStatement.addBatch();

			}

			int[] rows = preparedStatement.executeBatch();
			logger.info(">>>Registros actualizados: " + rows.length);

		} catch (CfeException e) {
			throw e;
		} finally {

			try {
				if (statement != null)
					statement.close();
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException("fallo en metodo guardaCienDpr", e);
			}
		}

	}

	public List<BajasDepreciadosCienBean> consultaCienDpr(BajasDprCienFormBean datos, SessionScopeUser sessionData)
			throws CfeException {
		List<BajasDepreciadosCienBean> datosDpr = new ArrayList<BajasDepreciadosCienBean>();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = dataSource.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, c.anio anio, c.fecha_carga fechaCarga, "
					+ " c.numero_activo numeroActivo, u.usuario_apaterno paterno,"
					+ " u.usuario_amaterno materno, u.usuario_nombre nombre, s.desc_sociedad sociedad, c.subnumero subnumero "
					+ " from bajas_depreciados_cien c " + " join bajas_op_usuario u on (u.usuario_id = c.id_usuario) "
					+ " join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");

			selectSQL.append(" where c.id_sociedad =");
			selectSQL.append(sessionData.getSociedad().getId());
			if (!datos.getMes().equals(null))
				selectSQL.append(" and  c.mes =   ");
			selectSQL.append(datos.getMes().getId());
			if (!datos.getAnio().equals(null))
				selectSQL.append(" and c.anio =  ");
			selectSQL.append(datos.getAnio());
			selectSQL.append(" and c.estado_registro =  ");
			selectSQL.append(Constants.UNO);
			preparedStatement = conn.prepareStatement(selectSQL.toString());

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				BajasDepreciadosCienBean datoBeans = new BajasDepreciadosCienBean();
				switch (rs.getInt("mes")) {
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
				datoBeans.setNombreCompleto(
						datoBeans.getaPaterno() + " " + datoBeans.getaMaterno() + " " + datoBeans.getNombre());

				datosDpr.add(datoBeans);
			}
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		}

		return datosDpr;

	}

	public BajasDepreciadosCienBean consultaCienDprValida(BajasDepreciadosCienBean datos) throws CfeException {
		BajasDepreciadosCienBean datosDpr = new BajasDepreciadosCienBean();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = dataSource.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, c.anio anio, c.fecha_carga fechaCarga, "
					+ " c.numero_activo numeroActivo, c.subnumero,  u.usuario_apaterno paterno,"
					+ " u.usuario_amaterno materno, u.usuario_nombre nombre, s.desc_sociedad sociedad "
					+ " from bajas_depreciados_cien c " + " join bajas_op_usuario u on (u.usuario_id = c.id_usuario) "
					+ " join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");

			selectSQL.append(" where c.numero_activo =   ");
			selectSQL.append(datos.getNumeroActivo());
			selectSQL.append(" and c.subnumero =   '");
			selectSQL.append(datos.getSubnumero());
			selectSQL.append("' and c.estado_registro = 1 ");

			preparedStatement = conn.prepareStatement(selectSQL.toString());

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				BajasDepreciadosCienBean datoBeans = new BajasDepreciadosCienBean();
				switch (rs.getInt("mes")) {
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
				datoBeans.setNombreCompleto(
						datoBeans.getaPaterno() + " " + datoBeans.getaMaterno() + " " + datoBeans.getNombre());

				datosDpr = datoBeans;
			}
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		}

		return datosDpr;

	}

	public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes) throws CfeException {
		BajasDepreciadosCienBean datosDpr = new BajasDepreciadosCienBean();
		Connection conn = null;
		BajasDepreciadosCienBean datoBeans = null;
		PreparedStatement preparedStatement = null;
		List<BajasDepreciadosCienBean> encontrados = null;
		try {
			conn = dataSource.getConnection();
			Session session = sessionFactory.openSession();
			StringBuffer selectSQL = new StringBuffer("select c.mes mes, " + " c.anio anio,"
					+ " c.numero_activo numeroActivo, c.subnumero, c.estado_registro"
					+ " from bajas_depreciados_cien c "
					+ " inner join baja_cat_sociedad s on (s.id_sociedad = c.id_sociedad) ");
			selectSQL.append(" where c.mes = ? ");
			selectSQL.append(" and c.anio = ? ");
			selectSQL.append(" and s.id_sociedad = ? ");

			preparedStatement = conn.prepareStatement(selectSQL.toString());

			preparedStatement.setInt(1, mes);
			preparedStatement.setInt(2, anio);
			preparedStatement.setInt(3, Constants.UNO);

			ResultSet rs = preparedStatement.executeQuery();
			encontrados = new LinkedList<BajasDepreciadosCienBean>();
			while (rs.next()) {
				datoBeans = new BajasDepreciadosCienBean();
				datoBeans.setIdMes(rs.getInt("mes"));
				datoBeans.setAnio(rs.getInt("anio"));
				datoBeans.setNumeroActivo(rs.getString("numeroActivo"));
				datoBeans.setSubnumero(rs.getString("subnumero"));
				datoBeans.setEstadoRegistro(rs.getInt("estado_registro"));
				encontrados.add(datoBeans);
			}
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
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
			conn = dataSource.getConnection();

			qInsert.append(
					"update bajas_depreciados_cien set estado_registro = ? where mes = ? and anio  = ? and numero_activo = ? and subnumero = ?");

			preparedStatement = conn.prepareStatement(qInsert.toString());

			for (BajasDepreciadosCienBean asset : assetList) {
				preparedStatement.setInt(1, Constants.CERO);
				preparedStatement.setInt(2, asset.getIdMes());
				preparedStatement.setInt(3, asset.getAnio());
				preparedStatement.setString(4, asset.getNumeroActivo());
				preparedStatement.setString(5, asset.getSubnumero());
				preparedStatement.addBatch();

			}

			int[] rows = preparedStatement.executeBatch();
			logger.info(">>>Registros para baja: " + rows.length);
//		conn.commit();

		} catch (CfeException e) {
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new CfeException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
			throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		// CallableStatement ejecute = null;
		logger.info("bytes: " + bytes);
		try {
			conn = dataSource.getConnection();
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

			/*
			 * ejecute = conn.prepareCall("call sp_bajas_documento (?,?,?)");
			 * ejecute.setInt(1, 1); ejecute.setInt(1, 1); ejecute.setInt(1, 1);
			 * ejecute.execute();
			 */
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
	public List<BajasOpDocumentoBean> descargaArchivoBlob() throws CfeException, SQLException {
		Connection conn = null;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		/*
		 * JFrame frame = new JFrame("Test file chooser"); frame.setVisible(true);
		 * JFileChooser guardar = new JFileChooser();
		 * guardar.setDialogTitle("Especificar Ruta");
		 * guardar.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 */

		PreparedStatement preparedStatement = null;
		List<BajasOpDocumentoBean> docBeanLst = new ArrayList<BajasOpDocumentoBean>();
		BajasOpDocumentoBean docBean = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer selectSQL = new StringBuffer("select * from bajas_op_documento where doc_id = 19");
			HSSFWorkbook excel = new HSSFWorkbook();

			// int result = guardar.showSaveDialog(frame);
			logger.info(">>>Query select: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			File file = new File("C:/Reporte_Cien_dos.xls");

			while (rs.next()) {

				docBean = new BajasOpDocumentoBean();
				logger.info("Sigue");
				docBean.setId(rs.getInt("doc_id"));
				docBean.setContenido(rs.getBinaryStream("doc_contenido"));
				docBean.setTipo(rs.getString("doc_tipo"));
				docBean.setModulo(rs.getString("doc_modulo"));
				docBean.setNombre(rs.getString("doc_nombre"));
				docBeanLst.add(docBean);
				int r = 0;

				logger.info("Sigue");
				try {
					/*
					 * InputStream in = new FileInputStream(file); BufferedInputStream bin = new
					 * BufferedInputStream(in); logger.info("res: "+(int) bin.available());
					 * logger.info("res: "+(int) docBean.getContenido().available());
					 * /*DataInputStream din = new DataInputStream(bin); while(din.available() > 0){
					 * outPrint.print(din.readLine()); outPrint.print("\n"); }
					 */

					/*
					 * response.setContentType("application/vnd.ms-excel");
					 * //response.setContentType("application/force-download");
					 * response.setContentLength(docBean.getContenido().available()); String
					 * headerKey = "Content-Disposition"; String headerValue =
					 * String.format("attachment; filename=Prueba_Rep.xls");
					 * response.setHeader(headerKey, headerValue);
					 * response.setHeader("Content-Transfer-Encoding", "binary"); OutputStream out =
					 * response.getOutputStream();
					 */

					// byte[] buffer = new byte[4096];
					BufferedInputStream is = new BufferedInputStream(docBean.getContenido());
					FileOutputStream fos = new FileOutputStream(file);
					while ((r = is.read()) != -1) {
						fos.write(r);
						// out.write(buffer,0,r);
					}
					// excel.write(fos);
					logger.info("se lee el archivo: " + fos);
					fos.flush();
					fos.close();
					// out.flush();
					is.close();
					// out.close();
					/*
					 * if(guardar.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					 * excel.write(fos); fos.close(); excel.close(); }
					 */

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		}
		return docBeanLst;

	}

	@Override
	public void insertaRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException {
		Connection conn = null;
		CallableStatement ejecute = null;
		try {

			conn = dataSource.getConnection();

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

	@Override
	public void insertaSolicReporte(Integer anio, Integer periodo, Integer sociedad, Integer usuario, String docTipo,
			String docModulo, String docDescripcion, String docNombre) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer selectSQL = new StringBuffer(
					"INSERT INTO bajas_op_documento (doc_id, doc_contenido, doc_tipo, doc_modulo, doc_descripcion, doc_nombre,"
							+ " doc_estatus, doc_fecha_creacion, doc_usuario_creacion, doc_estado_registro, doc_id_sociedad, "
							+ "doc_p_anio, doc_p_periodo) " + "VALUES(0,?, ?, ?, ? , ? , ?, sysdate, ? , ?, ?, ?, ?) ");

			logger.info(">>>Query insert: " + selectSQL);
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
			conn = dataSource.getConnection();
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
			conn = dataSource.getConnection();
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
			conn = dataSource.getConnection();
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
			conn = dataSource.getConnection();
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