package com.telcel.gsa.dsaf.amov.dao.impl;

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

import com.telcel.gsa.dsaf.amov.dao.InpcAmovDao;
import com.telcel.gsa.dsaf.bean.InpcBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.BajasInpc;
import com.telcel.gsa.dsaf.util.CfeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class InpcAmovDaoImpl extends AbstractDaoImpl<BajasInpc, Integer> implements InpcAmovDao, Serializable{

	@Autowired
	transient DataSource dataSourceAmov;
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
	final static Logger logger = LoggerFactory.getLogger(InpcAmovDaoImpl.class);
	public InpcAmovDaoImpl(){
		super(BajasInpc.class);
	}
	
	
	@Override
	public void insertaInpc(InpcBean inpc) throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceAmov.getConnection();
			qInsert.append("insert into inpcs (ANIO, MES, INDICE) "
					+" values (?,?,?) ");
			preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setInt(1, inpc.getInpc().getAnio());
			preparedStatement.setInt(2, inpc.getInpc().getMes());
			preparedStatement.setDouble(3, inpc.getInpc().getIndice().doubleValue());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			if(e.getMessage().contains("duplicate value")){
				throw new CfeException("DUPLICADO",e);
			}
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
				throw new CfeException(e.getMessage(),e);
			}
		}
		
	}
	
	@Override
	public void actualizaInpc(InpcBean inpc) throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceAmov.getConnection();
			qInsert.append("update inpcs set indice=:p_indice "
					+ " where  anio=? "
					+ " and  mes=? ");
			
			preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setInt(1, inpc.getInpc().getAnio());
			preparedStatement.setInt(2, inpc.getInpc().getMes());
			preparedStatement.setBigDecimal(3, inpc.getInpc().getIndice());
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
	public void eliminaInpc(InpcBean inpc) throws CfeException{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		StringBuilder qInsert = new StringBuilder();
		try {
			conn = dataSourceAmov.getConnection();
			qInsert.append("delete from inpcs "
					+ " where  anio=? "
					+ " and  mes=? ");
			
			preparedStatement = conn.prepareStatement(qInsert.toString());
			preparedStatement.setInt(1, inpc.getInpc().getAnio());
			preparedStatement.setInt(2, inpc.getInpc().getMes());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(),e);
		}catch(CfeException e){
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
				// TODO Auto-generated catch block
				throw new CfeException(e.getMessage(),e);
			}
		}
		
	}
	
	@Override
	
	public List<BajasInpc> consultaInpc(InpcBean inpc) throws CfeException{
		Connection conn = null;
		List<BajasInpc> inpcs = new ArrayList<BajasInpc>();
		BajasInpc inpcDta = null;
		PreparedStatement preparedStatement = null;
		
		try {
			conn = dataSourceAmov.getConnection();
		
		StringBuffer selectSQL = new StringBuffer("select anio,mes,indice "
				+ " from inpcs where 1 = 1 ");
		if(inpc.getAnio() != null && inpc.getAnio() != 0){
			selectSQL.append(" AND anio= ");
			selectSQL.append( inpc.getAnio() );
		}
		if(inpc.getMes() != null && inpc.getMes().getId() != null && inpc.getMes().getId().intValue() != 0){
			selectSQL.append( " AND mes=");
			selectSQL.append( inpc.getMes().getId());
		}

		logger.info(">>> Query inpc: " + selectSQL);
		preparedStatement = conn.prepareStatement(selectSQL.toString());
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			inpcDta = new BajasInpc();
			inpcDta.setAnio(rs.getInt("anio"));
			inpcDta.setMes(rs.getInt("mes"));
			inpcDta.setIndice(rs.getBigDecimal("indice"));
			inpcDta.setMesTxt(utileriasfeDao.perBajaToObject(inpcDta.getMes()));
			inpcs.add(inpcDta);
		}
		
		} catch (SQLException e) {
			throw new CfeException("Error en consulta de inpcs", e);
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
		return inpcs;
	}
}
