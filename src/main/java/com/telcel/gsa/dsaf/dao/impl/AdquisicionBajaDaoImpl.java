package com.telcel.gsa.dsaf.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.RepAdqClasBean;
import com.telcel.gsa.dsaf.bean.RepAdqRegBean;
import com.telcel.gsa.dsaf.bean.RepAdqTextoBean;
import com.telcel.gsa.dsaf.bean.ReporteBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.dao.AdquisicionBajaDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class AdquisicionBajaDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements AdquisicionBajaDao, Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -600814647102535519L;
	final static Logger logger = LoggerFactory.getLogger(AdquisicionBajaDaoImpl.class);
	@Autowired
	transient DataSource dataSource;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public AdquisicionBajaDaoImpl(){
		super(v_bajas_nvo3.class);
	}


@Override
public AdqBajasBean consultaRegion(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
	List<RepAdqRegBean> repAdqRegBeanLstTot = new ArrayList<RepAdqRegBean>();
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		if(!datos.getRegion().isEmpty()){
			filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		
		
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select region,region_dsc,per_baja,sum(adq_baja) deduccion  "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}	
			selectSQL.append( " group by region,region_dsc,per_baja ");
			selectSQL.append( " order by region,region_dsc,per_baja ");
			logger.info(">>> Query listReporte: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("region"));
				reporte.setDescCve(rs.getString("region_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				repAdqRegBeanLst = getDeduccion(rs, repAdqRegBeanLst);
				repAdqRegBeanLstTot = repAdqRegBeanLst;	
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListReporte(listReporte);
			datos.setListRepAdqReg(repAdqRegBeanLst);
			datos.setListRepAdqRegTot(repAdqRegBeanLstTot);
			datos.setTotReporteMesGeneral(datMesRep);
			logger.info("mesesNuevos:?"+datMesRep.toString());

	}catch(CfeException e){
		throw e;
		
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
	return datos;
}


@Override
public AdqBajasBean consultaRegionAjustes(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
			if(filterTxt != null){
				if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
					if( "TODOS".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
					}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
					}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
					}
				}else{
					if( "TODOS".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
					}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
					}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
						datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
					}
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select region,region_dsc,per_baja,sum(adq_baja) deduccion  "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
			selectSQL.append( " group by region,region_dsc,per_baja ");
			selectSQL.append( " order by region,region_dsc,per_baja ");
			logger.info(">>>Query consultaRegionAjustes: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccion(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("region"));
				reporte.setDescCve(rs.getString("region_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListReporteAjuste(listReporte);
			datos.setListRepAjAdqReg(repAdqRegBeanLst); 
			datos.setTotReporteMesAjusteGeneral(datMesRep);

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
	return datos;
}

@Override
public AdqBajasBean consultaRegionNetos(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }

		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
		StringBuffer selectSQL = new StringBuffer("select region,region_dsc,per_baja,sum(adq_baja) deduccion  "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja not in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by region,region_dsc,per_baja ");
			selectSQL.append( " order by region,region_dsc,per_baja ");
			logger.info(">>Query adqneto: >" + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccion(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("region"));
				reporte.setDescCve(rs.getString("region_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListReporte(listReporte);
			
			datos.setTotReporteMesGeneral(datMesRep);
			datos.setListRepAdqReg(repAdqRegBeanLst);
			
	
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
			throw new CfeException("Error en consulta region netos..", e);
		}
	}
	return datos;
}

public List<RepAdqRegBean> getDeduccion(ResultSet rs, List<RepAdqRegBean> lst) throws CfeException{
	RepAdqRegBean tmp = new RepAdqRegBean();
	
	try {
	tmp.setRegion(rs.getString("region_dsc"));
	boolean exist = false;
	
	if(lst.contains(tmp)){
		tmp = lst.get(lst.indexOf(tmp));
		exist = true;
		tmp.setTotalRegion(tmp.getTotalRegion().add(rs.getBigDecimal("deduccion")));
	}
	if(!exist){
		tmp.setTotalRegion(rs.getBigDecimal("deduccion"));
	}
	switch(rs.getInt("per_baja")){
	case Constants.UNO:{
		tmp.setEnero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOS:{
		tmp.setFebrero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.TRES:{
		tmp.setMarzo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.CUATRO:{
		tmp.setAbril(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.CINCO:{
		tmp.setMayo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SEIS:{
		tmp.setJunio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SIETE:{
		tmp.setJulio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.OCHO:{
		tmp.setAgosto(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.NUEVE:{
		tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DIEZ:{
		tmp.setOctubre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.ONCE:{
		tmp.setNoviembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOCE:{
		tmp.setDiciembre(rs.getBigDecimal("deduccion"));
	}
	break;
	default:{
		throw new CfeException("fallo en metodo getDeduccion", null);
	}
	}
	
	if(!exist){
		lst.add(tmp);
	}
	
	} catch (SQLException e) {
		
		throw new CfeException("Error en conversion de cosulta AdqReg", e);
	}
	return lst;
}



public List<RepAdqClasBean> getDeduccionClase(ResultSet rs, List<RepAdqClasBean> lst) throws CfeException{
	RepAdqClasBean tmp = new RepAdqClasBean();
	
	try {
	tmp.setClase(rs.getString("clase_dsc"));
	boolean exist = false;
	
	if(lst.contains(tmp)){
		tmp = lst.get(lst.indexOf(tmp));
		exist = true;
		tmp.setTotalClase(tmp.getTotalClase().add(rs.getBigDecimal("deduccion")));
	}
	if(!exist){
		tmp.setTotalClase(rs.getBigDecimal("deduccion"));
	}
	switch(rs.getInt("per_baja")){
	case Constants.UNO:{
		tmp.setEnero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOS:{
		tmp.setFebrero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.TRES:{
		tmp.setMarzo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.CUATRO:{
		tmp.setAbril(rs.getBigDecimal("deduccion"));
	}
	break;
	case 5:{
		tmp.setMayo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SEIS:{
		tmp.setJunio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SIETE:{
		tmp.setJulio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.OCHO:{
		tmp.setAgosto(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.NUEVE:{
		tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DIEZ:{
		tmp.setOctubre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.ONCE:{
		tmp.setNoviembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOCE:{
		tmp.setDiciembre(rs.getBigDecimal("deduccion"));
	}
	break;
	default:{
		
	}
	}
	
	if(!exist){
		lst.add(tmp);
	}
	
	} catch (SQLException e) {
		
		throw new CfeException("Error en conversion de cosulta AdqReg", e);
	}
	return lst;
}


public List<RepAdqTextoBean> getDeduccionTexto(ResultSet rs, List<RepAdqTextoBean> lst) throws CfeException{
	RepAdqTextoBean tmp = new RepAdqTextoBean();
	
	try {
	tmp.setTexto(rs.getString("texto_baja"));
	boolean exist = false;
	
	if(lst.contains(tmp)){
		tmp = lst.get(lst.indexOf(tmp));
		exist = true;
		tmp.setTotalTexto(tmp.getTotalTexto().add(rs.getBigDecimal("deduccion")));
	}
	if(!exist){
		tmp.setTotalTexto(rs.getBigDecimal("deduccion"));
	}
	switch(rs.getInt("per_baja")){
	case Constants.UNO:{
		tmp.setEnero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOS:{
		tmp.setFebrero(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.TRES:{
		tmp.setMarzo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.CUATRO:{
		tmp.setAbril(rs.getBigDecimal("deduccion"));
	}
	break;
	case 5:{
		tmp.setMayo(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SEIS:{
		tmp.setJunio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.SIETE:{
		tmp.setJulio(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.OCHO:{
		tmp.setAgosto(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.NUEVE:{
		tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DIEZ:{
		tmp.setOctubre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.ONCE:{
		tmp.setNoviembre(rs.getBigDecimal("deduccion"));
	}
	break;
	case Constants.DOCE:{
		tmp.setDiciembre(rs.getBigDecimal("deduccion"));
	}
	break;
	default:{
		
	}
	}
	
	if(!exist){
		lst.add(tmp);
	}
	
	} catch (SQLException e) {
		
		throw new CfeException("Error en conversion de cosulta AdqReg", e);
	}
	return lst;
}

@Override
public AdqBajasBean consultaTexto(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqTextoBean> repAdqRegBeanLst = new ArrayList<RepAdqTextoBean>();
	List<RepAdqTextoBean> repAdqRegBeanLstTot = new ArrayList<RepAdqTextoBean>();
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select texto_baja, per_baja, sum(adq_baja) deduccion   "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase +")");
			}

			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}				
			selectSQL.append( " group by texto_baja,per_baja ");
			selectSQL.append( " order by texto_baja,per_baja ");
			logger.info(">>> Query texto listReporte: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("texto_baja"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));

				repAdqRegBeanLst = getDeduccionTexto(rs, repAdqRegBeanLst);
				repAdqRegBeanLstTot = repAdqRegBeanLst;	
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListReporte(listReporte);
			datos.setListRepAdqtxt(repAdqRegBeanLst);
			datos.setListRepAdqtxtTot(repAdqRegBeanLstTot);
			datos.setTotReporteMesGeneral(datMesRep);
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
	return datos;
}


@Override
public AdqBajasBean consultaClase(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqClasBean> repAdqRegBeanLst = new ArrayList<RepAdqClasBean>();
	List<RepAdqClasBean> repAdqRegBeanLstTot = new ArrayList<RepAdqClasBean>();
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc as clase_dsc,per_baja,sum(adq_baja) deduccion   "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase +")");
			}

			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
			  
			selectSQL.append( " group by clase,clase_dsc,per_baja ");
			selectSQL.append( " order by 1,per_baja ");
			logger.info(">>> Query listReporte: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				ReporteBean reporte = new ReporteBean();
				reporte.setDescCve(rs.getString("clase_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				repAdqRegBeanLst = getDeduccionClase(rs, repAdqRegBeanLst);
				repAdqRegBeanLstTot = repAdqRegBeanLst;	
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListRepAdqClas(repAdqRegBeanLst);
			datos.setListRepAdqClastTot(repAdqRegBeanLstTot);
			datos.setTotReporteMesGeneral(datMesRep);
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
	return datos;
}


@Override
public AdqBajasBean consultaClaseAjustes(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqClasBean> repAdqRegBeanLst = new ArrayList<RepAdqClasBean>();
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	PreparedStatement preparedStatement =null;
	try{
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc as clase_dsc,per_baja,sum(adq_baja) deduccion "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}

			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by clase,clase_dsc,per_baja ");
			selectSQL.append( " order by 1,per_baja ");
			logger.info(">>>Query consultaClaseAjustes: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccionClase(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setDescCve(rs.getString("clase_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListRepAjAdqClas(repAdqRegBeanLst); // ajuste
			datos.setTotReporteMesAjusteGeneral(datMesRep); // total ajuste a la derecha
			
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
	return datos;
}


@Override
public AdqBajasBean consultaClaseNetos(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqClasBean> repAdqRegBeanLst = new ArrayList<RepAdqClasBean>();
	
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	try{
		
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc as clase_dsc,per_baja,sum(adq_baja) deduccion "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja not in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}		
			selectSQL.append( " group by clase,clase_dsc,per_baja ");
			selectSQL.append( " order by 1,per_baja ");
			logger.info(">>>Query netos clase: " +selectSQL );
			
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccionClase(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setDescCve(rs.getString("clase_dsc"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListRepAdqClas(repAdqRegBeanLst);
			datos.setTotReporteMesGeneral(datMesRep);
			
	
	}catch(CfeException e){
		throw e;
	}finally{
		try {
			conn.close();
		} catch (SQLException e) {
			throw new CfeException("Error en consulta region netos..", e);
		}
	}
	return datos;
}


@Override
public AdqBajasBean consultaTextoAjustes(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqTextoBean> repAdqRegBeanLst = new ArrayList<RepAdqTextoBean>();
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	try{
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }
		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select texto_baja,per_baja,sum(adq_baja) deduccion  "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}

			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by texto_baja,per_baja ");
			selectSQL.append( " order by texto_baja,per_baja ");
			logger.info(">>>Query consultaTextoAjustes: " + selectSQL);
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccionTexto(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("texto_baja"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListReporteAjuste(listReporte);
			datos.setListRepAjAdqTxt(repAdqRegBeanLst);
			datos.setTotReporteMesAjusteGeneral(datMesRep);
	}catch(CfeException e){
		throw e;
	}finally{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	}
	return datos;
}


@Override
public AdqBajasBean consultaTextoNetos(AdqBajasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
	List<RepAdqTextoBean> repAdqRegBeanLst = new ArrayList<RepAdqTextoBean>();
	String filterRegion = null;		
	String filterClase = null;
	String filterTxt= null;
	try{
		
		if(!datos.getRegion().isEmpty()){
    		filterRegion = utileriasCfeDao.joinArray(datos.getRegion().toArray(), "'", ",", "'");
    	}		
		if(!datos.getClase().isEmpty()){
    		filterClase = utileriasCfeDao.joinArray(datos.getClase().toArray(), "'", ",", "'");
        }

		filterTxt = utileriasCfeDao.getFilterText(datos.getTexto(), datos.getTxtDesc(), datos.getTxtsDesc());
		if(filterTxt != null){
			if(filterTxt.equalsIgnoreCase(utileriasCfeDao.joinArray(datos.getTxtsDesc().toArray(), "'", ",", "'"))){
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
				}
			}else{
				if( "TODOS".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt.replaceAll("'", ""));
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt.replaceAll("'", ""));
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt.replaceAll("'", ""));
				}
			}
		}else{
			if( "TODOS".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA: TODOS LOS TEXTOS FISCALES Y NO FISCALES");
			}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA FISCALES: TODOS");
			}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
				datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES: TODOS");
			}
		}
		
		conn = dataSource.getConnection();
		TotalBean datMesRep = new TotalBean();
		datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
			StringBuffer selectSQL = new StringBuffer("select texto_baja,per_baja,sum(adq_baja) deduccion   "
					+ " from v_bajas_nvo3 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and adq_baja not in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
			}

						
			selectSQL.append( " group by texto_baja,per_baja ");
			selectSQL.append( " order by texto_baja,per_baja ");
			logger.info(">>>Query txt neto: " + selectSQL);
			PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				repAdqRegBeanLst = getDeduccionTexto(rs, repAdqRegBeanLst);
				ReporteBean reporte = new ReporteBean();
				reporte.setCve(rs.getString("texto_baja"));
				reporte.setPerBaja(rs.getInt("per_baja"));
				reporte.setDeduccion(rs.getBigDecimal("deduccion"));
				listReporte.add(reporte);
				datMesRep = utileriasCfeDao.totalesMes(reporte, datMesRep);
			}
			datos.setListRepAdqtxt(repAdqRegBeanLst);
			datos.setTotReporteMesGeneral(datMesRep);
	}catch(CfeException e){
		throw e;
	}finally{
		try {
			conn.close();
		} catch (SQLException e) {
			throw new CfeException("Error en consulta region netos..", e);
		}
	}
	return datos;
}


}