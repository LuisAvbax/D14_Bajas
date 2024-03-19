package com.telcel.gsa.dsaf.dipcel.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.bean.TGAnioBean;
import com.telcel.gsa.dsaf.bean.TGClaseBean;
import com.telcel.gsa.dsaf.bean.TGRegionBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.dipcel.dao.ReporteConcentradoDipcelDao;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ReporteConcentradoDipcelDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements ReporteConcentradoDipcelDao, Serializable{


	private static final long serialVersionUID = -2930176011149832726L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(ReporteConcentradoDipcelDaoImpl.class);
	@Autowired
	transient DataSource dataSourceDipcel;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public ReporteConcentradoDipcelDaoImpl(){
		super(v_bajas_nvo3.class);
	}


@Override
public ReporteConcentradoBean consultaReporteConcentrado(ReporteConcentradoBean datos) throws CfeException, SQLException {
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
		conn = dataSourceDipcel.getConnection();
			StringBuffer selectSQL = new StringBuffer("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,"
					+ " mes,anio,texto_baja, per_baja, nvl(sum(adq_baja),0) adq_baja, nvl(sum(adq_ac_baja),0) adq_ac_baja, "
					+ " nvl(sum(ejercicio_baja),0) ejercicio_baja, nvl(sum(depr_tot),0) depr_tot, nvl(sum(costo_h),0) costo_h, "
					+ " TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc, MAX(fac_act) fac_act, "
					+" nvl(sum(costo_act),0) costo_act, nvl(sum(depre_anual_act),0) depre_anual_act " 
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
						
			selectSQL.append( " group by region,region_dsc,3,anio,mes,texto_baja,per_baja  ");
			selectSQL.append( " order by region,region_dsc,3 asc,anio asc,mes asc,per_baja asc,texto_baja ");
			logger.info(">>>Query Concentrado: " + selectSQL);
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
				reporte.setMes(rs.getInt("mes"));
				reporte.setAnio(rs.getInt("anio"));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				if (reporte.getMes() != null){
					reporte.setMesStr(reporte.getMes()< 10 ? ("0" + reporte.getMes()): reporte.getMes().toString());
				}
				
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setInpcmp(rs.getBigDecimal("inpc_mp"));
				reporte.setInpc(rs.getBigDecimal("inpc"));
				reporte.setFac_act(rs.getBigDecimal("fac_act"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()));
				
				
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
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(regionItr.getAll_adq_baja() != null ? regionItr.getAll_adq_baja(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().add(regionItr.getAll_adq_ac_baja() != null ? regionItr.getAll_adq_ac_baja(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().add(regionItr.getAll_ejercicio_baja() != null ? regionItr.getAll_ejercicio_baja(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().add(regionItr.getAll_depr_tot() != null ? regionItr.getAll_depr_tot(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setCosto_h(datos.getTotReporteGeneral().getCosto_h().add(regionItr.getAll_costo_h() != null ? regionItr.getAll_costo_h(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(regionItr.getAll_costo_act() != null ? regionItr.getAll_costo_act(): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(regionItr.getAll_depre_anual_act() != null ? regionItr.getAll_depre_anual_act(): BigDecimal.ZERO));
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
public ReporteConcentradoBean consultaReporteConcentradoNetos(ReporteConcentradoBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasDosBean> listReporte= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegCero= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegUno= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegDos= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegTres= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegCuatro= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegCinco= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegSeis= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegSiete= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegOcho= new ArrayList<BajasDosBean>();
	List<BajasDosBean> listRegNueve= new ArrayList<BajasDosBean>();
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
		conn = dataSourceDipcel.getConnection();
			StringBuffer selectSQL = new StringBuffer("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,  "
					+ " mes,anio,texto_baja, per_baja, nvl(sum(adq_baja),0) adq_baja, nvl(sum(adq_ac_baja),0) adq_ac_baja, "
					+ " nvl(sum(ejercicio_baja),0) ejercicio_baja, nvl(sum(depr_tot),0) depr_tot, nvl(sum(costo_h),0) costo_h, "
					+ " TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc, MAX(fac_act) fac_act, "
					+" nvl(sum(costo_act),0) costo_act, nvl(sum(depre_anual_act),0) depre_anual_act "
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
						
			selectSQL.append( " group by region,region_dsc,3,anio,mes,texto_baja,per_baja  ");
			
			selectSQL.append( " union  ");
			selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,  "
					+ " mes,anio,texto_baja, per_baja, nvl(sum(adq_baja),0)*-1 adq_baja, nvl(sum(adq_ac_baja),0)*-1 adq_ac_baja, "
					+ " nvl(sum(ejercicio_baja),0)*-1 ejercicio_baja, nvl(sum(depr_tot),0)*-1 depr_tot, nvl(sum(costo_h),0)*-1 costo_h, "
					+ " TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc, MAX(fac_act) fac_act, "
					+" nvl(sum(costo_act),0)*-1 costo_act, nvl(sum(depre_anual_act),0)*-1 depre_anual_act "
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
						
			selectSQL.append( " group by region,region_dsc,3,anio,mes,texto_baja,per_baja  ");
			
			selectSQL.append( " union  ");
			selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,  "
					+ " mes,anio,texto_baja, per_baja, 0.0 adq_baja, 0.0 adq_ac_baja, "
					+ " 0.0 ejercicio_baja, 0.0 depr_tot, 0.0 costo_h, "
					+ " TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc, MAX(fac_act) fac_act, "
					+" nvl(sum(costo_act),0)*-1 costo_act, nvl(sum(depre_anual_act),0)*-1 depre_anual_act "
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
						
			selectSQL.append( " group by region,region_dsc,3,anio,mes,texto_baja,per_baja  ");
			selectSQL.append( " order by region,region_dsc,3 asc,anio asc,mes asc,per_baja asc,texto_baja ");
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
			
			
			while (rs.next()) {
				BajasDosBean reporte = new BajasDosBean();
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setClase_dsc(rs.getString("clase_dsc"));
				reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
				reporte.setMes(rs.getInt("mes"));
				reporte.setAnio(rs.getInt("anio"));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				if (reporte.getMes() != null){
					reporte.setMesStr(reporte.getMes()< 10 ? ("0" + reporte.getMes()): reporte.getMes().toString());
				}
				
				reporte.setPerbaja(rs.getString("per_baja") != null ? Integer.parseInt(rs.getString("per_baja")): 0);
				reporte.setPerbajaObj(utileriasCfeDao.perBajaToObject(reporte.getPerbaja()));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setAdq_ac_baja(rs.getBigDecimal("adq_ac_baja"));
				reporte.setEjercicio_baja(rs.getBigDecimal("ejercicio_baja"));
				reporte.setDepr_tot(rs.getBigDecimal("depr_tot"));
				reporte.setCosto_h(rs.getBigDecimal("costo_h"));
				reporte.setInpcmp(rs.getBigDecimal("inpc_mp"));
				reporte.setInpc(rs.getBigDecimal("inpc"));
				reporte.setFac_act(rs.getBigDecimal("fac_act"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()));
				datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()));
				datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()));
				datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()));
				datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()));
				datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()));
				datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()));
				
				
				totalGlobalRegLst.add(reporte);
				listReporte.add(reporte);
				if(reporte.getRegion().equalsIgnoreCase("MR00")){
					listRegCero.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR01")){
					listRegUno.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR02")){
					listRegDos.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR03")){
					listRegTres.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR04")){
					listRegCuatro.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR05")){
					listRegCinco.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR06")){
					listRegSeis.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR07")){
					listRegSiete.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR08")){
					listRegOcho.add(reporte);
				}else if(reporte.getRegion().equalsIgnoreCase("MR09")){
					listRegNueve.add(reporte);
				}
				
			}
			
			List<TGRegionBean> regionList = new ArrayList <TGRegionBean>();
			if(!listRegCero.isEmpty()){
			regionList.addAll(generateRegionTree(listRegCero));
			}
			if(!listRegUno.isEmpty()){
			regionList.addAll(generateRegionTree(listRegUno));
			}
			if(!listRegDos.isEmpty()){
			regionList.addAll(generateRegionTree(listRegDos));
			}
			if(!listRegTres.isEmpty()){
			regionList.addAll(generateRegionTree(listRegTres));
			}
			if(!listRegCuatro.isEmpty()){
			regionList.addAll(generateRegionTree(listRegCuatro));
			}
			if(!listRegCinco.isEmpty()){
			regionList.addAll(generateRegionTree(listRegCinco));
			}
			if(!listRegSeis.isEmpty()){
			regionList.addAll(generateRegionTree(listRegSeis));
			}
			if(!listRegSiete.isEmpty()){
			regionList.addAll(generateRegionTree(listRegSiete));
			}
			if(!listRegOcho.isEmpty()){
			regionList.addAll(generateRegionTree(listRegOcho));
			}
			if(!listRegNueve.isEmpty()){
			regionList.addAll(generateRegionTree(listRegNueve));
			}
			

			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			//AGREGAR TOTAL GENRAL
			datos.setDetRegiones(regionList);
			for(TGRegionBean regionItr: regionList){
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
public ReporteConcentradoBean consultaReporteConcentradoAjuste(ReporteConcentradoBean datos) throws CfeException, SQLException {
	
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
		conn = dataSourceDipcel.getConnection();
		
			StringBuffer selectSQL = new StringBuffer("select 1 as orden, 'IMPROCEDENTES CON VALOR -1.00' as activo,   "
					+ "nvl(sum(adq_baja),0) as adq_baja, nvl(sum(adq_ac_baja),0) as adq_ac_baja, "
					+ "nvl(sum(ejercicio_baja),0) as ejercicio_baja,  ");
					selectSQL.append("nvl(sum(depr_tot),0) as depr_tot, nvl(sum(costo_h),0) as costo_h, "
							+ "nvl(sum(costo_act),0) as costo_act, nvl(sum(depre_anual_act),0) as depre_anual_act "); 
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
					selectSQL.append("select 2 as orden, 'IMPROCEDENTES CON VALOR -0.01' as activo,   nvl(sum(adq_baja),0) as adq_baja, nvl(sum(adq_ac_baja),0) as adq_ac_baja, "
							+ "nvl(sum(ejercicio_baja),0) as ejercicio_baja, ");
					selectSQL.append("nvl(sum(depr_tot),0) as depr_tot, nvl(sum(costo_h),0) as costo_h, nvl(sum(costo_act),0) as costo_act, "
							+ "nvl(sum(depre_anual_act),0) as depre_anual_act ");
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
					selectSQL.append("select 3 as orden, '100% DEPRECIADOS', 0.00 as adq_baja, "
							+ "0.00 as adq_ac_baja, 0.00 as ejercicio_baja, ");
					selectSQL.append("0.00 as depr_tot, 0.00 as costo_h, "
							+ "nvl(sum(costo_act),0) as costo_act, nvl(sum(depre_anual_act),0) as depre_anual_act ");
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
				datos.getTotReporteAjGeneral().setAdq_baja(datos.getTotReporteAjGeneral().getAdq_baja().add(rs.getBigDecimal("adq_baja")));
				datos.getTotReporteAjGeneral().setAdq_ac_baja(datos.getTotReporteAjGeneral().getAdq_ac_baja().add(rs.getBigDecimal("adq_ac_baja")));
				datos.getTotReporteAjGeneral().setEjercicio_baja(datos.getTotReporteAjGeneral().getEjercicio_baja().add(rs.getBigDecimal("ejercicio_baja")));
				datos.getTotReporteAjGeneral().setDepr_tot(datos.getTotReporteAjGeneral().getDepr_tot().add(rs.getBigDecimal("depr_tot")));
				datos.getTotReporteAjGeneral().setCosto_h(datos.getTotReporteAjGeneral().getCosto_h().add(rs.getBigDecimal("costo_h")));
				datos.getTotReporteAjGeneral().setCosto_act(datos.getTotReporteAjGeneral().getCosto_act().add(rs.getBigDecimal("costo_act")));
				datos.getTotReporteAjGeneral().setDepre_anual_act(datos.getTotReporteAjGeneral().getDepre_anual_act().add(rs.getBigDecimal("depre_anual_act")));
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
	//lista de anio
	List<TGAnioBean> listAnio = null;
	
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
			claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()));
			claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()));
			claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()));
			claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()));
			claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()));
			claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()));
			claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()));
			claseList.add(claseTemp);
			
		}else{
			claseTemp = claseList.get(claseList.indexOf(claseTemp));
			claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()));
			claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()));
			claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()));
			claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()));
			claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()));
			claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()));
			claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()));
			claseTemp.getTextos().add(dta);
		}
		region.setAll_adq_baja(region.getAll_adq_baja().add(dta.getAdq_baja()));
		region.setAll_adq_ac_baja(region.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()));
		region.setAll_ejercicio_baja(region.getAll_ejercicio_baja().add(dta.getEjercicio_baja()));
		region.setAll_depr_tot(region.getAll_depr_tot().add(dta.getDepr_tot()));
		region.setAll_costo_h(region.getAll_costo_h().add(dta.getCosto_h()));
		region.setAll_costo_act(region.getAll_costo_act().add(dta.getCosto_act()));
		region.setAll_depre_anual_act(region.getAll_depre_anual_act().add(dta.getDepre_anual_act()));
	}
	
	if (!listRegCero.isEmpty())
		region.setNombre(listRegCero.get(0).getRegion_dsc());
//se agregan las clase list a la region
	region.setClases(claseList);
	//divi de año y texto
	TGAnioBean anioTemp= null;
	TotalBajasDosBean totalAnio = null;
	for (TGClaseBean tgClasBean : claseList) {
		listAnio = new ArrayList<TGAnioBean>();
		for (BajasDosBean tgTextoBean : tgClasBean.getTextos()) {
			anioTemp = new TGAnioBean(tgTextoBean.getAnio());
			if (!listAnio.contains(anioTemp)){
				//total de año
				totalAnio = new TotalBajasDosBean(); //nuevo totalizador para nuevo año
				totalAnio.setAnio(tgTextoBean.getAnio());
				totalAnio.setAdq_baja(tgTextoBean.getAdq_baja());
				totalAnio.setAdq_ac_baja(tgTextoBean.getAdq_ac_baja());
				totalAnio.setEjercicio_baja(tgTextoBean.getEjercicio_baja());
				totalAnio.setDepr_tot(tgTextoBean.getDepr_tot());
				totalAnio.setCosto_h(tgTextoBean.getCosto_h());
				totalAnio.setCosto_act(tgTextoBean.getCosto_act());
				totalAnio.setDepre_anual_act(tgTextoBean.getDepre_anual_act());
				anioTemp.setTotalAnio(totalAnio);
				anioTemp.setTextos(new ArrayList<BajasDosBean>());
				anioTemp.getTextos().add(tgTextoBean);
				listAnio.add(anioTemp);
			}
			else
			{
				totalAnio.setAdq_baja(totalAnio.getAdq_baja().add(tgTextoBean.getAdq_baja()));
				totalAnio.setAdq_ac_baja(totalAnio.getAdq_ac_baja().add(tgTextoBean.getAdq_ac_baja()));
				totalAnio.setEjercicio_baja(totalAnio.getEjercicio_baja().add(tgTextoBean.getEjercicio_baja()));
				totalAnio.setDepr_tot(totalAnio.getDepr_tot().add(tgTextoBean.getDepr_tot()));
				totalAnio.setCosto_h(totalAnio.getCosto_h().add(tgTextoBean.getCosto_h()));
				totalAnio.setCosto_act(totalAnio.getCosto_act().add(tgTextoBean.getCosto_act()));
				totalAnio.setDepre_anual_act(totalAnio.getDepre_anual_act().add(tgTextoBean.getDepre_anual_act()));
				anioTemp.setTotalAnio(totalAnio);
				anioTemp = listAnio.get(listAnio.indexOf(anioTemp));
				anioTemp.getTextos().add(tgTextoBean);
			}
		}
		tgClasBean.setAnio(listAnio);
	}
	regionList.add(region);
	return regionList;
}

}