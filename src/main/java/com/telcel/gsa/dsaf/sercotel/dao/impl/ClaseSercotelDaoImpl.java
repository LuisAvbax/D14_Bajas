package com.telcel.gsa.dsaf.sercotel.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import org.hibernate.loader.GeneratedCollectionAliases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.amov.dao.ClaseAmovDao;
import com.telcel.gsa.dsaf.bean.ClaseBean;
import com.telcel.gsa.dsaf.dao.ClaseDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.dipcel.dao.ClaseDipcelDao;
import com.telcel.gsa.dsaf.entity.BajasClase;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.ClaseSercotelDao;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

@Repository
public class ClaseSercotelDaoImpl extends AbstractDaoImpl<BajasClase, String> implements ClaseSercotelDao, Serializable{

	@Autowired
	transient DataSource dataSourceSercotel;
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
	public ClaseSercotelDaoImpl(){
		super(BajasClase.class);
	}
	
	
	@Override
	public void insertaClase(ClaseBean clase, SessionScopeUser sessionData) throws CfeException{
		Connection conn = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceSercotel.getConnection();
//			qInsert.append("insert into cat_clases (c_cve_clase, c_desc_clase) "
//					+" values (?,?) ");
//			PreparedStatement preparedStatement = conn.prepareStatement(qInsert.toString());
//			preparedStatement.setString(1, clase.getClaseSel().getClave());
//			preparedStatement.setString(2, clase.getClaseSel().getDescripcion());
			
			qInsert.append("insert into cat_clases (c_cve_clase, c_desc_clase, i_id_usuario_alta, fecha_alta, i_id_usuario_cambio, fecha_cambio) "
					+" values (?,?,?,sysdate,null,null) ");
			PreparedStatement preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setString(1, clase.getClaseSel().getClave());
			preparedStatement.setString(2, clase.getClaseSel().getDescripcion());
			preparedStatement.setInt(3, sessionData.getUsuarioCfe().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			if(e.getMessage().contains("duplicate value")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "EL INPC YA EXISTE"));
				
			}
			throw new CfeException(e.getMessage(),e);
			
		}catch(CfeException e){
			throw e;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		
	}
	
	@Override
	public void actualizaClase(ClaseBean clase, SessionScopeUser sessionData) throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceSercotel.getConnection();
//			qInsert.append("update cat_clases set c_desc_clase=? ");
//			qInsert.append("where c_cve_clase=?");
//			preparedStatement = conn.prepareStatement(qInsert.toString());
//			preparedStatement.setString(1, clase.getClaseSel().getDescripcion());
//			preparedStatement.setString(2, clase.getClaseSel().getClave());
			
			qInsert.append("update cat_clases set c_desc_clase=?, i_id_usuario_cambio=?, fecha_cambio=sysdate ");
			qInsert.append("where c_cve_clase=?");
			preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setString(1, clase.getClaseSel().getDescripcion());
			preparedStatement.setInt(2, sessionData.getUsuarioCfe().getId());
			preparedStatement.setString(3, clase.getClaseSel().getClave());
			
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
	public void eliminaClase(ClaseBean clase) throws CfeException{
		Connection conn = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceSercotel.getConnection();
			qInsert.append("delete from cat_clases "
					+ " where  c_cve_clase=? ");
			
			PreparedStatement preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setString(1, clase.getClaseSel().getClave());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(),e);
		}catch(CfeException e){
			throw new CfeException(e.getMessage(),e);
		
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		
	}
	
	@Override
	
	public List<BajasClase> consultaClase(ClaseBean clase) throws CfeException{
		Connection conn = null;
		List<BajasClase> clases = new ArrayList<BajasClase>();
		BajasClase claseDta = null;
		
		try {
			conn = dataSourceSercotel.getConnection();
		
		StringBuffer selectSQL = new StringBuffer("select c_cve_clase, c_desc_clase "
				+ " from cat_clases where 1 = 1 ");
		if(clase.getClave() != null && !clase.getClave().isEmpty()){
			selectSQL.append(" AND c_cve_clase like '%");
			selectSQL.append( clase.getClave() );
			selectSQL.append("%'");
		}
		if(clase.getDescripcion() != null && !clase.getDescripcion().isEmpty()){
			selectSQL.append( " AND c_desc_clase like '%");
			selectSQL.append( clase.getDescripcion());
			selectSQL.append("%'");
		}

		System.out.println(">>> Query clase: " + selectSQL);
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			claseDta = new BajasClase();
			claseDta.setClave(rs.getString("c_cve_clase"));
			claseDta.setDescripcion(rs.getString("c_desc_clase"));
			clases.add(claseDta);
		}
		
		} catch (SQLException e) {
			throw new CfeException("Error en consulta de inpcs", e);
		}
		finally{
			try {
			
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		return clases;
	}
	
@Override
	
	public List<BajasClase> consultaClaseValidar(ClaseBean clase) throws CfeException{
		Connection conn = null;
		List<BajasClase> clases = new ArrayList<BajasClase>();
		BajasClase claseDta = null;
		
		try {
			conn = dataSourceSercotel.getConnection();
		
		StringBuffer selectSQL = new StringBuffer("select c_cve_clase, c_desc_clase "
				+ " from cat_clases where 1 = 1 ");
		if(clase.getClave() != null && !clase.getClave().isEmpty()){
			selectSQL.append(" AND c_cve_clase = '");
			selectSQL.append( clase.getClave() );
			selectSQL.append("'");
		}

		System.out.println(">>> Query clase: " + selectSQL);
		PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			claseDta = new BajasClase();
			claseDta.setClave(rs.getString("c_cve_clase"));
			claseDta.setDescripcion(rs.getString("c_desc_clase"));
			clases.add(claseDta);
		}
		
		} catch (SQLException e) {
			throw new CfeException("Error en consulta de inpcs", e);
		}
		finally{
			try {
			
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		return clases;
	}
}
