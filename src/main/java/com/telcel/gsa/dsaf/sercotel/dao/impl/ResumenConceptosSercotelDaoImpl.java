package com.telcel.gsa.dsaf.sercotel.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoClasBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.bean.TGClaseBean;
import com.telcel.gsa.dsaf.bean.TGRegionBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.sercotel.dao.ResumenConceptosSercotelDao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import javax.sql.DataSource;
import com.telcel.gsa.dsaf.util.CfeException;

@Repository
public class ResumenConceptosSercotelDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements ResumenConceptosSercotelDao, Serializable{


	private static final long serialVersionUID = -2930176011149832726L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(ResumenConceptosSercotelDaoImpl.class);
	@Autowired
	transient DataSource dataSourceSercotel;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public ResumenConceptosSercotelDaoImpl(){
		super(v_bajas_nvo3.class);
	}


@Override
public ResumenConceptoRegBean consultaConceptoRegion(ResumenConceptoRegBean datos) throws CfeException, SQLException {
	//se trabaja en cambio
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasDosBean> listReporte= new ArrayList<BajasDosBean>();
	List<BajasDosBean> totalGlobalRegLst = new ArrayList<BajasDosBean>();
	datos.setTotReporteGeneral(new TotalBajasDosBean());
	
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
		conn = dataSourceSercotel.getConnection();
			StringBuffer selectSQL = new StringBuffer("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,texto_baja,  "
					+ " per_baja, sum(adq_baja) adq_baja, sum(adq_ac_baja) adq_ac_baja, "
					+ "sum(ejercicio_baja) ejercicio_baja, sum(depr_tot) depr_tot, "
					+"sum(costo_h) costo_h, sum(costo_act) costo_act, " 
					+"sum(depre_anual_act) depre_anual_act "
					+"from v_bajas_nvo3 ");
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
						
			selectSQL.append( " group by region,region_dsc,3,per_baja, texto_baja  ");
			selectSQL.append( " order by region,region_dsc,3 asc,per_baja asc, texto_baja  ");
			logger.info(">>>Query Conceptos Region: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			TotalBajasDosBean datMesRep = new TotalBajasDosBean();
			datMesRep.setPer_baja(0);
			datMesRep.setAdq_baja(BigDecimal.ZERO);
			datMesRep.setAdq_ac_baja(BigDecimal.ZERO);
			datMesRep.setEjercicio_baja(BigDecimal.ZERO);
			datMesRep.setDepr_tot(BigDecimal.ZERO);
			datMesRep.setCosto_h(BigDecimal.ZERO);
			datMesRep.setCosto_act(BigDecimal.ZERO);
			datMesRep.setDepre_anual_act(BigDecimal.ZERO);
			
			TGRegionBean regionTemp = null;
			List<TGRegionBean> regionList = new ArrayList<TGRegionBean>();
			while (rs.next()) {
				BajasDosBean reporte = new BajasDosBean();
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setClase_dsc(rs.getString("clase_dsc"));
				reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
								
//				llenar la lista de textos en vez de esta
				totalGlobalRegLst.add(reporte);
				listReporte.add(reporte);
				
				regionTemp = new TGRegionBean();
				regionTemp.setNombre(reporte.getRegion());
				if(!regionList.contains(regionTemp)){
					regionTemp.setTextos(new ArrayList<BajasDosBean>());
					regionTemp.getTextos().add(reporte);
					regionList.add(regionTemp);
				}else{
					regionTemp = regionList.get(regionList.indexOf(regionTemp));
					regionTemp.getTextos().add(reporte);
				}
				
			}
			
			List<TGRegionBean> regListDef = new ArrayList <TGRegionBean>();
			for(TGRegionBean regionDto: regionList){		
				regListDef.addAll(generateRegionTree(regionDto.getTextos()));	
			}
			

			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			//AGREGAR TOTAL GENRAL
			datos.setDetRegiones(regListDef);
			for(TGRegionBean regionItr: regListDef){
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(regionItr.getAll_adq_baja()!= null ? regionItr.getAll_adq_baja() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().add(regionItr.getAll_adq_ac_baja()!= null ? regionItr.getAll_adq_ac_baja() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().add(regionItr.getAll_ejercicio_baja()!= null ? regionItr.getAll_ejercicio_baja() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().add(regionItr.getAll_depr_tot()!= null ? regionItr.getAll_depr_tot() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setCosto_h(datos.getTotReporteGeneral().getCosto_h().add(regionItr.getAll_costo_h()!= null ? regionItr.getAll_costo_h() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(regionItr.getAll_costo_act()!= null ? regionItr.getAll_costo_act() : BigDecimal.ZERO));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(regionItr.getAll_depre_anual_act()!= null ? regionItr.getAll_depre_anual_act() : BigDecimal.ZERO));
			}
			datos.setListTotalGlobalReg(totalGlobalRegLst);
			datos.setTotReporteGeneral(datMesRep);
			datos.setListReporte(listReporte); 
			
	
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
			logger.error(e.getMessage(),e);
		}
	}
	return datos;
}

@Override
public ResumenConceptoRegBean consultaConceptoNetos(ResumenConceptoRegBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasDosBean> totalGlobalRegLst = new ArrayList<BajasDosBean>();
	List<BajasDosBean> listReporte= new ArrayList<BajasDosBean>();
	datos.setTotReporteGeneral(new TotalBajasDosBean());
	
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
		conn = dataSourceSercotel.getConnection();
			StringBuilder selectSQL = new StringBuilder("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,texto_baja,  "
					+ "per_baja, sum(adq_baja) adq_baja, sum(adq_ac_baja) adq_ac_baja, "
					+ "sum(ejercicio_baja) ejercicio_baja, sum(depr_tot) depr_tot, "
					+"sum(costo_h) costo_h, sum(costo_act) costo_act, " 
					+"sum(depre_anual_act) depre_anual_act "
					+"from v_bajas_nvo3 ");
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
						
			selectSQL.append( " group by region, region_dsc, 3, per_baja, texto_baja ");
			selectSQL.append( " union  ");
			selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,texto_baja,  "
			+ "per_baja, sum(nvl(adq_baja,0))*-1 adq_baja, sum(nvl(adq_ac_baja,0))*-1 adq_ac_baja, "
			+ "sum(nvl(ejercicio_baja,0))*-1 ejercicio_baja, sum(nvl(depr_tot,0))*-1 depr_tot, "
			+"sum(nvl(costo_h,0))*-1 costo_h, sum(nvl(costo_act,0))*-1 costo_act, " 
			+"sum(nvl(depre_anual_act,0))*-1 depre_anual_act "
			+"from v_bajas_nvo4 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append(" and adq_baja in(-1.00,-0.01) ");
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
			selectSQL.append( " group by region, region_dsc, 3, per_baja, texto_baja ");
			selectSQL.append( " union  ");
			selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,texto_baja,  "
			+ "per_baja, 0.00 adq_baja, 0.00 adq_ac_baja, "
			+ "0.00 ejercicio_baja, 0.00 depr_tot, "
			+"0.00 costo_h, sum(nvl(costo_act,0))*-1 costo_act, " 
			+"sum(nvl(depre_anual_act,0))*-1 depre_anual_act "
			+"from v_bajas_nvo4 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append(" and adq_baja not in(-1.00,-0.01) ");
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
				if(filterTxt != null){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
				}
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				if(filterTxt != null){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
				}
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				if(filterTxt != null){
				selectSQL.append( " AND texto_baja in (");
				selectSQL.append( filterTxt+")");
				}
			}
			selectSQL.append( " group by region, region_dsc, 3, per_baja, texto_baja ");
			selectSQL.append( " order by region, region_dsc, 3 asc, per_baja asc, texto_baja ");
			logger.info(">>>Query totalGLobal: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			TotalBajasDosBean datMesRep = new TotalBajasDosBean();
			datMesRep.setAdq_baja(BigDecimal.ZERO);
			datMesRep.setAdq_ac_baja(BigDecimal.ZERO);
			datMesRep.setEjercicio_baja(BigDecimal.ZERO);
			datMesRep.setDepr_tot(BigDecimal.ZERO);
			datMesRep.setCosto_h(BigDecimal.ZERO);
			datMesRep.setCosto_act(BigDecimal.ZERO);
			datMesRep.setDepre_anual_act(BigDecimal.ZERO);
			
			TGRegionBean regionTemp = null;
			List<TGRegionBean> regionList = new ArrayList<TGRegionBean>();
			
			while (rs.next()) {
				BajasDosBean reporte = new BajasDosBean();
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setClase_dsc(rs.getString("clase_dsc"));
				reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
				
				
//				llenar la lista de textos en vez de esta
				totalGlobalRegLst.add(reporte);
				listReporte.add(reporte);
				
				regionTemp = new TGRegionBean();
				regionTemp.setNombre(reporte.getRegion());
				if(!regionList.contains(regionTemp)){
					regionTemp.setTextos(new ArrayList<BajasDosBean>());
					regionTemp.getTextos().add(reporte);
					regionList.add(regionTemp);
				}else{
					regionTemp = regionList.get(regionList.indexOf(regionTemp));
					regionTemp.getTextos().add(reporte);
				}
				
			}
			
			List<TGRegionBean> regListDef = new ArrayList <TGRegionBean>();
			for(TGRegionBean regionDto: regionList){		
				regListDef.addAll(generateRegionTree(regionDto.getTextos()));	
			}
			

			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			//AGREGAR TOTAL GENRAL
			datos.setDetRegiones(regListDef);
			for(TGRegionBean regionItr: regListDef){
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(regionItr.getAll_adq_baja()));
				datos.getTotReporteGeneral().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().add(regionItr.getAll_adq_ac_baja()));
				datos.getTotReporteGeneral().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().add(regionItr.getAll_ejercicio_baja()));
				datos.getTotReporteGeneral().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().add(regionItr.getAll_depr_tot()));
				datos.getTotReporteGeneral().setCosto_h(datos.getTotReporteGeneral().getCosto_h().add(regionItr.getAll_costo_h()));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(regionItr.getAll_costo_act()));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(regionItr.getAll_depre_anual_act()));
			}
			datos.setListTotalGlobalReg(totalGlobalRegLst);
			datos.setTotReporteGeneral(datMesRep);
			datos.setListReporte(listReporte); 
			
	
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
public ResumenConceptoRegBean consultaAjConceptoRegion(ResumenConceptoRegBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
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
		conn = dataSourceSercotel.getConnection();
		
			StringBuilder selectSQL = new StringBuilder("select 1 as orden, 'IMPROCEDENTES CON VALOR -1.00' as activo,   sum(adq_baja) as adq_baja, sum(adq_ac_baja) as adq_ac_baja, sum(ejercicio_baja) as ejercicio_baja,  ");
					selectSQL.append("sum(depr_tot) as depr_tot, sum(costo_h) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act "); 
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja in (-1.00) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					
					selectSQL.append("union ");
					selectSQL.append("select 2 as orden, 'IMPROCEDENTES CON VALOR -0.01' as activo,   sum(adq_baja) as adq_baja, sum(adq_ac_baja) as adq_ac_baja, sum(ejercicio_baja) as ejercicio_baja, ");
					selectSQL.append("sum(depr_tot) as depr_tot, sum(costo_h) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act ");
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja in (-0.01) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					
					selectSQL.append("union ");
					selectSQL.append("select 3 as orden, '100% DEPRECIADOS', sum(0.00) as adq_baja, sum(0.00) as adq_ac_baja, sum(0.00) as ejercicio_baja, ");
					selectSQL.append("sum(0.00) as depr_tot, sum(0.00) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act ");
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja not in (-1.00,-0.01) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					selectSQL.append("order by 1 ");

			logger.info(">>>Query ResConceptoReg: " + selectSQL);
			
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			BajasDosBean reporte = null;
			
			datos.setListTotalGlobalAjReg(new ArrayList<BajasDosBean>());
			datos.setTotReporteAjGeneral(new TotalBajasDosBean());
			datos.getTotReporteAjGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			while (rs.next()) {
				reporte =  new BajasDosBean();
				
				reporte.setTexto_baja(rs.getString("activo"));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datos.getListTotalGlobalAjReg().add(reporte);
				datos.getTotReporteAjGeneral().setAdq_baja(datos.getTotReporteAjGeneral().getAdq_baja().add(rs.getBigDecimal("adq_baja")!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setAdq_ac_baja(datos.getTotReporteAjGeneral().getAdq_ac_baja().add(rs.getBigDecimal("adq_ac_baja")!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setEjercicio_baja(datos.getTotReporteAjGeneral().getEjercicio_baja().add(rs.getBigDecimal("ejercicio_baja")!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setDepr_tot(datos.getTotReporteAjGeneral().getDepr_tot().add(rs.getBigDecimal("depr_tot")!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setCosto_h(datos.getTotReporteAjGeneral().getCosto_h().add(rs.getBigDecimal("costo_h")!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setCosto_act(datos.getTotReporteAjGeneral().getCosto_act().add(rs.getBigDecimal("costo_act")!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setDepre_anual_act(datos.getTotReporteAjGeneral().getDepre_anual_act().add(rs.getBigDecimal("depre_anual_act")!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
			}
			

			datos.setTotalGlobalRegTot(new TotalBajasDosBean());
			datos.getTotalGlobalRegTot().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().subtract(datos.getTotReporteAjGeneral().getAdq_baja()));
			datos.getTotalGlobalRegTot().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().subtract(datos.getTotReporteAjGeneral().getAdq_ac_baja()));
			datos.getTotalGlobalRegTot().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().subtract(datos.getTotReporteAjGeneral().getEjercicio_baja()));
			datos.getTotalGlobalRegTot().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().subtract(datos.getTotReporteAjGeneral().getDepr_tot()));
			datos.getTotalGlobalRegTot().setCosto_h(datos.getTotReporteGeneral().getCosto_h().subtract(datos.getTotReporteAjGeneral().getCosto_h()));
			datos.getTotalGlobalRegTot().setCosto_act(datos.getTotReporteGeneral().getCosto_act().subtract(datos.getTotReporteAjGeneral().getCosto_act()));
			datos.getTotalGlobalRegTot().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().subtract(datos.getTotReporteAjGeneral().getDepre_anual_act()));

	
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

public List<TGRegionBean> generateRegionTree(List<BajasDosBean> listRegCero){
	TGClaseBean claseTemp = null;
	TGRegionBean region = null;
	List<TGClaseBean> claseList = new ArrayList<TGClaseBean>();
	List<TGRegionBean> regionList = new ArrayList <TGRegionBean>();
	
	region = new TGRegionBean();
	region.setAll_adq_baja(BigDecimal.ZERO);
	region.setAll_adq_ac_baja(BigDecimal.ZERO);
	region.setAll_ejercicio_baja(BigDecimal.ZERO);
	region.setAll_depr_tot(BigDecimal.ZERO);
	region.setAll_costo_h(BigDecimal.ZERO);
	region.setAll_costo_act(BigDecimal.ZERO);
	region.setAll_depre_anual_act(BigDecimal.ZERO);

	for(BajasDosBean dta: listRegCero){
		if(!claseList.contains(new TGClaseBean(dta.getClase_dsc()))){
			claseTemp = new TGClaseBean();
			claseTemp.setNombre(dta.getClase_dsc());
			claseTemp.setNombreCorto(dta.getClase_dsc().substring(0,dta.getClase_dsc().indexOf('-')));
			claseTemp.setTextos(new ArrayList<BajasDosBean>());
			claseTemp.setAll_adq_baja(BigDecimal.ZERO);
			claseTemp.setAll_adq_ac_baja(BigDecimal.ZERO);
			claseTemp.setAll_ejercicio_baja(BigDecimal.ZERO);
			claseTemp.setAll_depr_tot(BigDecimal.ZERO);
			claseTemp.setAll_costo_h(BigDecimal.ZERO);
			claseTemp.setAll_costo_act(BigDecimal.ZERO);
			claseTemp.setAll_depre_anual_act(BigDecimal.ZERO);
			claseTemp.getTextos().add(dta);
			claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()!= null ?dta.getAdq_baja():BigDecimal.ZERO));
			claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!=null?dta.getAdq_ac_baja():BigDecimal.ZERO));
			claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!=null?dta.getEjercicio_baja():BigDecimal.ZERO));
			claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()!=null?dta.getDepr_tot():BigDecimal.ZERO));
			claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()!=null?dta.getCosto_h():BigDecimal.ZERO));
			claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()!=null?dta.getCosto_act():BigDecimal.ZERO));
			claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act():BigDecimal.ZERO));
			claseList.add(claseTemp);
			
		}else{
			claseTemp = claseList.get(claseList.indexOf(claseTemp));
			claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()!= null ?dta.getAdq_baja():BigDecimal.ZERO));
			claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!= null ?dta.getAdq_ac_baja():BigDecimal.ZERO));
			claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!= null ?dta.getEjercicio_baja():BigDecimal.ZERO));
			claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()!= null ?dta.getDepr_tot():BigDecimal.ZERO));
			claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()!= null ?dta.getCosto_h():BigDecimal.ZERO));
			claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()!=null?dta.getCosto_act():BigDecimal.ZERO));
			claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()!= null ?dta.getDepre_anual_act():BigDecimal.ZERO));
			claseTemp.getTextos().add(dta);
		}
		region.setAll_adq_baja(region.getAll_adq_baja().add(dta.getAdq_baja()!= null ?dta.getAdq_baja():BigDecimal.ZERO));
		region.setAll_adq_ac_baja(region.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!= null ?dta.getAdq_ac_baja():BigDecimal.ZERO));
		region.setAll_ejercicio_baja(region.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!= null ?dta.getEjercicio_baja():BigDecimal.ZERO));
		region.setAll_depr_tot(region.getAll_depr_tot().add(dta.getDepr_tot()!= null ?dta.getDepr_tot():BigDecimal.ZERO));
		region.setAll_costo_h(region.getAll_costo_h().add(dta.getCosto_h()!= null ?dta.getCosto_h():BigDecimal.ZERO));
		region.setAll_costo_act(region.getAll_costo_act().add(dta.getCosto_act()!= null ?dta.getCosto_act():BigDecimal.ZERO));
		region.setAll_depre_anual_act(region.getAll_depre_anual_act().add(dta.getDepre_anual_act()!= null ?dta.getDepre_anual_act():BigDecimal.ZERO));
	}
	region.setNombre(listRegCero.get(0).getRegion_dsc());
	region.setClases(claseList);
	regionList.add(region);
	return regionList;
}



@Override
public ResumenConceptoClasBean consultaConceptoClase(ResumenConceptoClasBean datos) throws CfeException, SQLException {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasDosBean> listReporte= new ArrayList<BajasDosBean>();
	List<BajasDosBean> totalGlobalRegLst = new ArrayList<BajasDosBean>();
	datos.setTotReporteGeneral(new TotalBajasDosBean());
	
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
		
		conn = dataSourceSercotel.getConnection();
			StringBuilder selectSQL = new StringBuilder("select clase||'-'||clase_dsc clase_dsc, region,region_dsc, texto_baja,  "
					+ " per_baja, sum(adq_baja) adq_baja, sum(adq_ac_baja) adq_ac_baja, "
					+ "sum(ejercicio_baja) ejercicio_baja, sum(depr_tot) depr_tot, "
					+"sum(costo_h) costo_h, sum(costo_act) costo_act, " 
					+"sum(depre_anual_act) depre_anual_act "
					+"from v_bajas_nvo3 ");
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
						
			selectSQL.append( " group by 1,region,region_dsc,per_baja,texto_baja  ");
			selectSQL.append( " order by 1 asc,region asc,per_baja asc,texto_baja  ");
			logger.info(">>>Query Resumen: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			
			ResultSet rs = preparedStatement.executeQuery();
			TotalBajasDosBean datMesRep = new TotalBajasDosBean();
			datMesRep.setAdq_baja(BigDecimal.ZERO);
			datMesRep.setAdq_ac_baja(BigDecimal.ZERO);
			datMesRep.setEjercicio_baja(BigDecimal.ZERO);
			datMesRep.setDepr_tot(BigDecimal.ZERO);
			datMesRep.setCosto_h(BigDecimal.ZERO);
			datMesRep.setCosto_act(BigDecimal.ZERO);
			datMesRep.setDepre_anual_act(BigDecimal.ZERO);
			List<TGClaseBean> classList = new ArrayList<TGClaseBean>();
			TGClaseBean claseTemp = null;
			while (rs.next()) {
				
				BajasDosBean reporte = new BajasDosBean();
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setClase_dsc(rs.getString("clase_dsc"));
				reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
					claseTemp = new TGClaseBean();
					claseTemp.setNombre(reporte.getClase_dsc());
					if(!classList.contains(claseTemp)){
						claseTemp.setTextos(new ArrayList<BajasDosBean>());
						claseTemp.getTextos().add(reporte);
						classList.add(claseTemp);
					}else{
						claseTemp = classList.get(classList.indexOf(claseTemp));
						claseTemp.getTextos().add(reporte);
					}
				
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
				
				totalGlobalRegLst.add(reporte);
				listReporte.add(reporte);	
			}
			
			List<TGClaseBean> classListDef = new ArrayList <TGClaseBean>();
			for(TGClaseBean claseItr: classList){		
				classListDef.addAll(generateClassTree(claseItr.getTextos()));	
			}
			

			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			//AGREGAR TOTAL GENRAL
			datos.setDetClases(classListDef);
			for(TGClaseBean tClaseItr: classListDef){
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(tClaseItr.getAll_adq_baja()));
				datos.getTotReporteGeneral().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().add(tClaseItr.getAll_adq_ac_baja()));
				datos.getTotReporteGeneral().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().add(tClaseItr.getAll_ejercicio_baja()));
				datos.getTotReporteGeneral().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().add(tClaseItr.getAll_depr_tot()));
				datos.getTotReporteGeneral().setCosto_h(datos.getTotReporteGeneral().getCosto_h().add(tClaseItr.getAll_costo_h()));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(tClaseItr.getAll_costo_act()));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(tClaseItr.getAll_depre_anual_act()));
			}
			datos.setDetClases(classListDef);
			datos.setTotReporteGeneral(datMesRep);
			datos.setListReporte(listReporte);
	
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
public ResumenConceptoClasBean consultaAjConceptoClase(ResumenConceptoClasBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
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
		conn = dataSourceSercotel.getConnection();
		
			StringBuilder selectSQL = new StringBuilder("select 1 as orden, 'IMPROCEDENTES CON VALOR -1.00' as activo,   sum(adq_baja) as adq_baja, sum(adq_ac_baja) as adq_ac_baja, sum(ejercicio_baja) as ejercicio_baja,  ");
					selectSQL.append("sum(depr_tot) as depr_tot, sum(costo_h) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act "); 
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja in (-1.00) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					
					selectSQL.append("union ");
					selectSQL.append("select 2 as orden, 'IMPROCEDENTES CON VALOR -0.01' as activo,   sum(adq_baja) as adq_baja, sum(adq_ac_baja) as adq_ac_baja, sum(ejercicio_baja) as ejercicio_baja, ");
					selectSQL.append("sum(depr_tot) as depr_tot, sum(costo_h) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act ");
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja in (-0.01) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					
					selectSQL.append("union ");
					selectSQL.append("select 3 as orden, '100% DEPRECIADOS', sum(0.00) as adq_baja, sum(0.00) as adq_ac_baja, sum(0.00) as ejercicio_baja, ");
					selectSQL.append("sum(0.00) as depr_tot, sum(0.00) as costo_h, sum(costo_act) as costo_act, sum(depre_anual_act) as depre_anual_act ");
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja not in (-1.00,-0.01) ");
					selectSQL.append( " AND per_baja in (");
					selectSQL.append( datos.getMesesConsulta()+") ");
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
					selectSQL.append("order by 1 ");

			logger.info(">>>Query totalGLobal: " + selectSQL);
			
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			BajasDosBean reporte = null;
			
			datos.setListTotalGlobalAjCla(new ArrayList<BajasDosBean>());
			datos.setTotReporteAjGeneral(new TotalBajasDosBean());
			datos.getTotReporteAjGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteAjGeneral().setDepre_anual_act(BigDecimal.ZERO);
			while (rs.next()) {
				reporte =  new BajasDosBean();
				
				reporte.setTexto_baja(rs.getString("activo"));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datos.getListTotalGlobalAjCla().add(reporte);
				datos.getTotReporteAjGeneral().setAdq_baja(datos.getTotReporteAjGeneral().getAdq_baja().add(rs.getBigDecimal("adq_baja")!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setAdq_ac_baja(datos.getTotReporteAjGeneral().getAdq_ac_baja().add(rs.getBigDecimal("adq_ac_baja")!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setEjercicio_baja(datos.getTotReporteAjGeneral().getEjercicio_baja().add(rs.getBigDecimal("ejercicio_baja")!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setDepr_tot(datos.getTotReporteAjGeneral().getDepr_tot().add(rs.getBigDecimal("depr_tot")!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setCosto_h(datos.getTotReporteAjGeneral().getCosto_h().add(rs.getBigDecimal("costo_h")!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setCosto_act(datos.getTotReporteAjGeneral().getCosto_act().add(rs.getBigDecimal("costo_act")!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datos.getTotReporteAjGeneral().setDepre_anual_act(datos.getTotReporteAjGeneral().getDepre_anual_act().add(rs.getBigDecimal("depre_anual_act")!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
			}
			

			datos.setTotalGlobalClaTot(new TotalBajasDosBean());
			datos.getTotalGlobalClaTot().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().subtract(datos.getTotReporteAjGeneral().getAdq_baja()));
			datos.getTotalGlobalClaTot().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().subtract(datos.getTotReporteAjGeneral().getAdq_ac_baja()));
			datos.getTotalGlobalClaTot().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().subtract(datos.getTotReporteAjGeneral().getEjercicio_baja()));
			datos.getTotalGlobalClaTot().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().subtract(datos.getTotReporteAjGeneral().getDepr_tot()));
			datos.getTotalGlobalClaTot().setCosto_h(datos.getTotReporteGeneral().getCosto_h().subtract(datos.getTotReporteAjGeneral().getCosto_h()));
			datos.getTotalGlobalClaTot().setCosto_act(datos.getTotReporteGeneral().getCosto_act().subtract(datos.getTotReporteAjGeneral().getCosto_act()));
			datos.getTotalGlobalClaTot().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().subtract(datos.getTotReporteAjGeneral().getDepre_anual_act()));

	
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
public ResumenConceptoClasBean consultaConceptoClasNetos(ResumenConceptoClasBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasDosBean> listReporte= new ArrayList<BajasDosBean>();
	List<BajasDosBean> totalGlobalRegLst = new ArrayList<BajasDosBean>();
	datos.setTotReporteGeneral(new TotalBajasDosBean());
	
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
		conn = dataSourceSercotel.getConnection();
		
		
			StringBuilder selectSQL = new StringBuilder("select clase||'-'||clase_dsc clase_dsc, region, region_dsc, texto_baja, "
					+ "per_baja, sum(adq_baja) adq_baja, sum(adq_ac_baja) adq_ac_baja, sum(ejercicio_baja) ejercicio_baja, "
					+ "sum(depr_tot) depr_tot, sum(costo_h) costo_h, sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act "
					+"from v_bajas_nvo3 ");
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
						
			selectSQL.append( " group by 1,region,region_dsc,per_baja,texto_baja  ");
			
			selectSQL.append( " union  ");
			selectSQL.append("select clase||'-'||clase_dsc clase_dsc, region, region_dsc, texto_baja,  "
			+ "per_baja, sum(nvl(adq_baja,0))*-1 adq_baja, sum(nvl(adq_ac_baja,0))*-1 adq_ac_baja, "
			+ "sum(nvl(ejercicio_baja,0))*-1 ejercicio_baja, sum(nvl(depr_tot,0))*-1 depr_tot, "
			+"sum(nvl(costo_h,0))*-1 costo_h, sum(nvl(costo_act,0))*-1 costo_act, " 
			+"sum(nvl(depre_anual_act,0))*-1 depre_anual_act "
			+"from v_bajas_nvo4 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append(" and adq_baja in(-1.00,-0.01) ");
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
			selectSQL.append( " group by 1,region,region_dsc,per_baja,texto_baja  ");
			selectSQL.append( " union  ");
			selectSQL.append("select clase||'-'||clase_dsc clase_dsc, region, region_dsc, texto_baja,  "
			+ "per_baja, 0.00 adq_baja, 0.00 adq_ac_baja, "
			+ "0.00 ejercicio_baja, 0.00 depr_tot, "
			+"0.00 costo_h, sum(nvl(costo_act,0))*-1 costo_act, " 
			+"sum(nvl(depre_anual_act,0))*-1 depre_anual_act "
			+"from v_bajas_nvo4 ");
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append(" and adq_baja not in(-1.00,-0.01) ");
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
			selectSQL.append( " group by 1,region,region_dsc,per_baja,texto_baja  ");
			selectSQL.append( " order by 1 asc,region asc,per_baja asc,texto_baja  ");
			logger.info(">>>Query totalGLobalClase: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			TotalBajasDosBean datMesRep = new TotalBajasDosBean();
			datMesRep.setAdq_baja(BigDecimal.ZERO);
			datMesRep.setAdq_ac_baja(BigDecimal.ZERO);
			datMesRep.setEjercicio_baja(BigDecimal.ZERO);
			datMesRep.setDepr_tot(BigDecimal.ZERO);
			datMesRep.setCosto_h(BigDecimal.ZERO);
			datMesRep.setCosto_act(BigDecimal.ZERO);
			datMesRep.setDepre_anual_act(BigDecimal.ZERO);
			
			List<TGClaseBean> classList = new ArrayList<TGClaseBean>();
			TGClaseBean claseTemp = null;
			while (rs.next()) {
				BajasDosBean reporte = new BajasDosBean();
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setClase_dsc(rs.getString("clase_dsc"));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				claseTemp = new TGClaseBean();
				claseTemp.setNombre(reporte.getClase_dsc());
				if(!classList.contains(claseTemp)){
					claseTemp.setTextos(new ArrayList<BajasDosBean>());
					claseTemp.getTextos().add(reporte);
					classList.add(claseTemp);
				}else{
					claseTemp = classList.get(classList.indexOf(claseTemp));
					claseTemp.getTextos().add(reporte);
				}
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()!= null ? reporte.getAdq_baja() : BigDecimal.ZERO));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()!= null ? reporte.getAdq_ac_baja() : BigDecimal.ZERO));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()!= null ? reporte.getEjercicio_baja() : BigDecimal.ZERO));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()!= null ? reporte.getDepr_tot() : BigDecimal.ZERO));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()!= null ? reporte.getCosto_h() : BigDecimal.ZERO));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()!= null ? reporte.getCosto_act() : BigDecimal.ZERO));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()!= null ? reporte.getDepre_anual_act() : BigDecimal.ZERO));
				
				
//				llenar la lista de textos en vez de esta
				totalGlobalRegLst.add(reporte);
				listReporte.add(reporte);
				
			}
			
			List<TGClaseBean> classListDef = new ArrayList <TGClaseBean>();
			for(TGClaseBean claseItr: classList){		
				classListDef.addAll(generateClassTree(claseItr.getTextos()));	
			}
			

			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			//AGREGAR TOTAL GENRAL
			datos.setDetClases(classListDef);
			for(TGClaseBean tClaseItr: classListDef){
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(tClaseItr.getAll_adq_baja()));
				datos.getTotReporteGeneral().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().add(tClaseItr.getAll_adq_ac_baja()));
				datos.getTotReporteGeneral().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().add(tClaseItr.getAll_ejercicio_baja()));
				datos.getTotReporteGeneral().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().add(tClaseItr.getAll_depr_tot()));
				datos.getTotReporteGeneral().setCosto_h(datos.getTotReporteGeneral().getCosto_h().add(tClaseItr.getAll_costo_h()));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(tClaseItr.getAll_costo_act()));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(tClaseItr.getAll_depre_anual_act()));
			}
			datos.setListTotalGlobalCla(totalGlobalRegLst);
			datos.setTotReporteGeneral(datMesRep);
			datos.setListReporte(listReporte);
	
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

public List<TGClaseBean> generateClassTree(List<BajasDosBean> listRegCero){
	TGRegionBean regionTemp = null;
	List<TGRegionBean> regionList = new ArrayList<TGRegionBean>();
	TGClaseBean clase = null;
	List<TGClaseBean> claseList = new ArrayList<TGClaseBean>();
	
	for(BajasDosBean dta: listRegCero){
//		claseTemp.setNombre(dta.getClase_dsc());
		if(!claseList.contains(new TGClaseBean(dta.getClase_dsc()))){
			
			clase = new TGClaseBean();
			clase.setAll_adq_baja(BigDecimal.ZERO);
			clase.setAll_adq_ac_baja(BigDecimal.ZERO);
			clase.setAll_ejercicio_baja(BigDecimal.ZERO);
			clase.setAll_depr_tot(BigDecimal.ZERO);
			clase.setAll_costo_h(BigDecimal.ZERO);
			clase.setAll_costo_act(BigDecimal.ZERO);
			clase.setAll_depre_anual_act(BigDecimal.ZERO);
			
			regionTemp = new TGRegionBean();
			regionTemp.setTextos(new ArrayList<BajasDosBean>());
			regionTemp.setNombre(dta.getRegion_dsc());
			
			if(!regionList.contains(regionTemp)){
				regionTemp.setAll_adq_baja(BigDecimal.ZERO);
				regionTemp.setAll_adq_ac_baja(BigDecimal.ZERO);
				regionTemp.setAll_ejercicio_baja(BigDecimal.ZERO);
				regionTemp.setAll_depr_tot(BigDecimal.ZERO);
				regionTemp.setAll_costo_h(BigDecimal.ZERO);
				regionTemp.setAll_costo_act(BigDecimal.ZERO);
				regionTemp.setAll_depre_anual_act(BigDecimal.ZERO);
				
				regionTemp.setAll_adq_baja(regionTemp.getAll_adq_baja().add(dta.getAdq_baja()!= null ? dta.getAdq_baja(): BigDecimal.ZERO));
				regionTemp.setAll_adq_ac_baja(regionTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja() != null ? dta.getAdq_ac_baja(): BigDecimal.ZERO));
				regionTemp.setAll_ejercicio_baja(regionTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja() != null ? dta.getEjercicio_baja(): BigDecimal.ZERO));
				regionTemp.setAll_depr_tot(regionTemp.getAll_depr_tot().add(dta.getDepr_tot() != null ? dta.getDepr_tot(): BigDecimal.ZERO));
				regionTemp.setAll_costo_h(regionTemp.getAll_costo_h().add(dta.getCosto_h() != null ? dta.getCosto_h(): BigDecimal.ZERO));
				regionTemp.setAll_costo_act(regionTemp.getAll_costo_act().add(dta.getCosto_act() != null ? dta.getCosto_act(): BigDecimal.ZERO));
				regionTemp.setAll_depre_anual_act(regionTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act() != null ? dta.getDepre_anual_act(): BigDecimal.ZERO));
				
				regionTemp.getTextos().add(dta);
				regionList.add(regionTemp);
			}else{
				regionTemp = regionList.get(regionList.indexOf(regionTemp));
			
				regionTemp.setAll_adq_baja(regionTemp.getAll_adq_baja().add(dta.getAdq_baja() != null ? dta.getAdq_baja(): BigDecimal.ZERO));
				regionTemp.setAll_adq_ac_baja(regionTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja() != null ? dta.getAdq_ac_baja(): BigDecimal.ZERO));
				regionTemp.setAll_ejercicio_baja(regionTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja() != null ? dta.getEjercicio_baja(): BigDecimal.ZERO));
				regionTemp.setAll_depr_tot(regionTemp.getAll_depr_tot().add(dta.getDepr_tot() != null ? dta.getDepr_tot(): BigDecimal.ZERO));
				regionTemp.setAll_costo_h(regionTemp.getAll_costo_h().add(dta.getCosto_h() != null ? dta.getCosto_h(): BigDecimal.ZERO));
				regionTemp.setAll_costo_act(regionTemp.getAll_costo_act().add(dta.getCosto_act() != null ? dta.getCosto_act(): BigDecimal.ZERO));
				regionTemp.setAll_depre_anual_act(regionTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act() != null ? dta.getDepre_anual_act(): BigDecimal.ZERO));
				
				
				regionTemp.getTextos().add(dta);
			}
		}else{
			
			clase = claseList.get(claseList.indexOf(new TGClaseBean(dta.getClase_dsc())));
			
			
			if(!regionList.contains(regionTemp)){
				regionTemp.setAll_adq_baja(BigDecimal.ZERO);
				regionTemp.setAll_adq_ac_baja(BigDecimal.ZERO);
				regionTemp.setAll_ejercicio_baja(BigDecimal.ZERO);
				regionTemp.setAll_depr_tot(BigDecimal.ZERO);
				regionTemp.setAll_costo_h(BigDecimal.ZERO);
				regionTemp.setAll_costo_act(BigDecimal.ZERO);
				regionTemp.setAll_depre_anual_act(BigDecimal.ZERO);
				
				regionTemp.setAll_adq_baja(regionTemp.getAll_adq_baja().add(dta.getAdq_baja()!= null ? dta.getAdq_baja(): BigDecimal.ZERO));
				regionTemp.setAll_adq_ac_baja(regionTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja() != null ? dta.getAdq_ac_baja(): BigDecimal.ZERO));
				regionTemp.setAll_ejercicio_baja(regionTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja() != null ? dta.getEjercicio_baja(): BigDecimal.ZERO));
				regionTemp.setAll_depr_tot(regionTemp.getAll_depr_tot().add(dta.getDepr_tot() != null ? dta.getDepr_tot(): BigDecimal.ZERO));
				regionTemp.setAll_costo_h(regionTemp.getAll_costo_h().add(dta.getCosto_h() != null ? dta.getCosto_h(): BigDecimal.ZERO));
				regionTemp.setAll_costo_act(regionTemp.getAll_costo_act().add(dta.getCosto_act() != null ? dta.getCosto_act(): BigDecimal.ZERO));
				regionTemp.setAll_depre_anual_act(regionTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act() != null ? dta.getDepre_anual_act(): BigDecimal.ZERO));
				
				
				regionTemp.getTextos().add(dta);
				regionList.add(regionTemp);
			}else{
				regionTemp = regionList.get(regionList.indexOf(regionTemp));
				
				
				regionTemp.setAll_adq_baja(regionTemp.getAll_adq_baja().add(dta.getAdq_baja()!= null ? dta.getAdq_baja(): BigDecimal.ZERO));
				regionTemp.setAll_adq_ac_baja(regionTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja() != null ? dta.getAdq_ac_baja(): BigDecimal.ZERO));
				regionTemp.setAll_ejercicio_baja(regionTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja() != null ? dta.getEjercicio_baja(): BigDecimal.ZERO));
				regionTemp.setAll_depr_tot(regionTemp.getAll_depr_tot().add(dta.getDepr_tot() != null ? dta.getDepr_tot(): BigDecimal.ZERO));
				regionTemp.setAll_costo_h(regionTemp.getAll_costo_h().add(dta.getCosto_h() != null ? dta.getCosto_h(): BigDecimal.ZERO));
				regionTemp.setAll_costo_act(regionTemp.getAll_costo_act().add(dta.getCosto_act() != null ? dta.getCosto_act(): BigDecimal.ZERO));
				regionTemp.setAll_depre_anual_act(regionTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act() != null ? dta.getDepre_anual_act(): BigDecimal.ZERO));
				
				regionTemp.getTextos().add(dta);
			}


		}

	}
	clase.setNombre(listRegCero.get(0).getClase_dsc());
	clase.setNombreCorto(listRegCero.get(0).getClase_dsc().substring(0,listRegCero.get(0).getClase_dsc().indexOf('-')));
	clase.setRegiones(regionList);
	for(TGRegionBean tgb: regionList){
		clase.setAll_adq_baja(clase.getAll_adq_baja().add(tgb.getAll_adq_baja() != null ? tgb.getAll_adq_baja(): BigDecimal.ZERO));
		clase.setAll_adq_ac_baja(clase.getAll_adq_ac_baja().add(tgb.getAll_adq_ac_baja() != null ? tgb.getAll_adq_ac_baja(): BigDecimal.ZERO));
		clase.setAll_ejercicio_baja(clase.getAll_ejercicio_baja().add(tgb.getAll_ejercicio_baja() != null ? tgb.getAll_ejercicio_baja(): BigDecimal.ZERO));
		clase.setAll_depr_tot(clase.getAll_depr_tot().add(tgb.getAll_depr_tot() != null ? tgb.getAll_depr_tot(): BigDecimal.ZERO));
		clase.setAll_costo_h(clase.getAll_costo_h().add(tgb.getAll_costo_h() != null ? tgb.getAll_costo_h(): BigDecimal.ZERO));
		clase.setAll_costo_act(clase.getAll_costo_act().add(tgb.getAll_costo_act() != null ? tgb.getAll_costo_act(): BigDecimal.ZERO));
		clase.setAll_depre_anual_act(clase.getAll_depre_anual_act().add(tgb.getAll_depre_anual_act() != null ? tgb.getAll_depre_anual_act(): BigDecimal.ZERO));
	}
	claseList.add(clase);
	return claseList;
}
}