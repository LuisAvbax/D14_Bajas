package com.telcel.gsa.dsaf.amov.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.amov.dao.ReporteDetalladoAmovDao;
import com.telcel.gsa.dsaf.bean.BajasCatEstatusDocBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoClasBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.bean.TGClaseBean;
import com.telcel.gsa.dsaf.bean.TGRegionBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalRegBean;
import com.telcel.gsa.dsaf.dao.ReporteConcentradoDao;
import com.telcel.gsa.dsaf.dao.ReporteDetalladoDao;
import com.telcel.gsa.dsaf.dao.ResumenConceptosDao;
import com.telcel.gsa.dsaf.dao.TotalGlobalDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.sql.DataSource;
import com.telcel.gsa.dsaf.util.CfeException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ReporteDetalladoAmovDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements ReporteDetalladoAmovDao, Serializable{


	private static final long serialVersionUID = -2930176011149832726L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(ReporteDetalladoAmovDaoImpl.class);
	@Autowired
	transient DataSource dataSourceAmov;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public ReporteDetalladoAmovDaoImpl(){
		super(v_bajas_nvo3.class);
	}


	@Override
	public ReporteDetalladoBean consultaReporteDetallado(ReporteDetalladoBean datos) throws CfeException, SQLException {
		//se trabaja en cambio
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
			conn = dataSourceAmov.getConnection();
				StringBuffer selectSQL = new StringBuffer("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,dia,mes,anio,  "
						+ " texto_baja,denom,num_activo,sub,per_baja,sum(adq_baja) adq_baja,sum(adq_ac_baja) adq_ac_baja, "
						+ " sum(ejercicio_baja) ejercicio_baja, sum(depr_tot) depr_tot,sum(costo_h) costo_h, "
						+" TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc, " 
						+" MAX(fac_act) fac_act,sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act "
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
							
				selectSQL.append( " group by region,region_dsc,3,dia,mes,anio,texto_baja,denom,num_activo,sub,per_baja  ");
				selectSQL.append( " order by region,region_dsc,3 asc,anio asc,mes asc,dia asc,per_baja asc,texto_baja,denom ");
				logger.info(">>>Query Reporte Detallado: " + selectSQL);
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
				SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
			    Date date = null;
				while (rs.next()) {
					BajasDosBean reporte = new BajasDosBean();
					reporte.setRegion(rs.getString("region"));
					reporte.setRegion_dsc(rs.getString("region_dsc"));
					reporte.setClase_dsc(rs.getString("clase_dsc"));
					reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
					reporte.setDia(rs.getInt("dia"));
					reporte.setMes(rs.getInt("mes"));
					if (reporte.getMes() != null){
						reporte.setMesStr(reporte.getMes()< 10 ? ("0" + reporte.getMes()): reporte.getMes().toString());
					}
					reporte.setAnio(rs.getInt("anio"));
					try {
						date = sdf.parse(reporte.getDia() + "-" + reporte.getMes() + "-" + reporte.getAnio());
					} catch (ParseException e) {
						throw new CfeException(e.getMessage(),e);
					}
				
				reporte.setFechaCap(date);
					reporte.setTexto_baja(rs.getString("texto_baja"));
					reporte.setDenom(rs.getString("denom"));
					reporte.setNum_activo(rs.getString("num_activo"));
					reporte.setSub(rs.getString("sub"));
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
					
					datMesRep.setAdq_baja(datMesRep.getAdq_baja().add(reporte.getAdq_baja()!= null ?reporte.getAdq_baja(): BigDecimal.ZERO));
					datMesRep.setAdq_ac_baja(datMesRep.getAdq_ac_baja().add(reporte.getAdq_ac_baja()!= null ?reporte.getAdq_ac_baja(): BigDecimal.ZERO));
					datMesRep.setEjercicio_baja(datMesRep.getEjercicio_baja().add(reporte.getEjercicio_baja()!= null ?reporte.getEjercicio_baja(): BigDecimal.ZERO));
					datMesRep.setDepr_tot(datMesRep.getDepr_tot().add(reporte.getDepr_tot()!= null ?reporte.getDepr_tot(): BigDecimal.ZERO));
					datMesRep.setCosto_h(datMesRep.getCosto_h().add(reporte.getCosto_h()!= null ?reporte.getCosto_h(): BigDecimal.ZERO));
					datMesRep.setCosto_act(datMesRep.getCosto_act().add(reporte.getCosto_act()!= null ?reporte.getCosto_act(): BigDecimal.ZERO));
					datMesRep.setDepre_anual_act(datMesRep.getDepre_anual_act().add(reporte.getDepre_anual_act()!= null ?reporte.getDepre_anual_act(): BigDecimal.ZERO));
					
					
//					llenar la lista de textos en vez de esta
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
				datos.setListReporteDetallado(totalGlobalRegLst);
				//datos.setListTotalGlobalReg(totalGlobalRegLst);
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
	public ReporteDetalladoBean consultaReporteDetalladoNetos(ReporteDetalladoBean datos) throws CfeException, SQLException {
		
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
		List<BajasDosBean> reporteConcentradoLst = new ArrayList<BajasDosBean>();
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
			conn = dataSourceAmov.getConnection();
				StringBuilder selectSQL = new StringBuilder("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,dia,mes,anio,  "
						+ " texto_baja,denom,num_activo,sub,per_baja,sum(adq_baja) adq_baja,sum(adq_ac_baja) adq_ac_baja, "
						+ " sum(ejercicio_baja) ejercicio_baja, sum(depr_tot) depr_tot,sum(costo_h) costo_h, "
						+" TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc,"
						+ " MAX(fac_act) fac_act,sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act " 
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
							
				selectSQL.append( " group by region,region_dsc,3,dia,mes,anio,texto_baja,denom,num_activo,sub,per_baja ");
				selectSQL.append( " union ");
				
				selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,dia,mes,anio,  "
						+ " texto_baja,denom,num_activo,sub,per_baja,sum(adq_baja)*-1 adq_baja,sum(adq_ac_baja)*-1 adq_ac_baja, "
						+ " sum(ejercicio_baja)*-1 ejercicio_baja, sum(depr_tot)*-1 depr_tot,sum(costo_h)*-1 costo_h, "
						+" TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc,"
						+ " MAX(fac_act) fac_act,sum(costo_act)*-1 costo_act, sum(depre_anual_act)*-1 depre_anual_act " 
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
							
				selectSQL.append( " group by region,region_dsc,3,dia,mes,anio,texto_baja,denom,num_activo,sub,per_baja ");
				
				selectSQL.append( " union ");	
				selectSQL.append("select region,region_dsc,clase||'-'||clase_dsc clase_dsc,dia,mes,anio,  "
						+ " texto_baja,denom,num_activo,sub,per_baja,0.0 adq_baja,0.0 adq_ac_baja, "
						+ " 0.0 ejercicio_baja, 0.0 depr_tot,0.0 costo_h, "
						+" TRUNC(MAX(inpc_mp),4) inpc_mp, TRUNC(MAX(inpc),4) inpc,"
						+ " MAX(fac_act) fac_act,sum(costo_act)*-1 costo_act, sum(depre_anual_act)*-1 depre_anual_act " 
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
				selectSQL.append( " group by region,region_dsc,3,dia,mes,anio,texto_baja,denom,num_activo,sub,per_baja ");
				selectSQL.append( " order by region,region_dsc,3 asc,anio asc,mes asc,dia asc,per_baja asc,texto_baja,denom ");
				logger.info(">>>Query ReporteDetalladoNetos: " + selectSQL);
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
			    Date date = null;
				while (rs.next()) {
					BajasDosBean reporte = new BajasDosBean();
					reporte.setRegion(rs.getString("region"));
					reporte.setRegion_dsc(rs.getString("region_dsc"));
					reporte.setClase_dsc(rs.getString("clase_dsc"));
					reporte.setClase(reporte.getClase_dsc().substring(0,reporte.getClase_dsc().indexOf('-')));
					reporte.setDia(rs.getInt("dia"));
					reporte.setMes(rs.getInt("mes"));
					if (reporte.getMes() != null){
						reporte.setMesStr(reporte.getMes()< 10 ? ("0" + reporte.getMes()): reporte.getMes().toString());
					}
					reporte.setAnio(rs.getInt("anio"));
					try {
						date = sdf.parse(reporte.getDia() + "-" + reporte.getMes() + "-" + reporte.getAnio());
					} catch (ParseException e) {
						throw new CfeException(e.getMessage(),e);
					}
				
				reporte.setFechaCap(date);
					reporte.setTexto_baja(rs.getString("texto_baja"));
					reporte.setDenom(rs.getString("denom"));
					reporte.setNum_activo(rs.getString("num_activo"));
					reporte.setSub(rs.getString("sub"));
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
					
					
//					llenar la lista de textos en vez de esta
					reporteConcentradoLst.add(reporte);
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
				datos.setListReporteDetallado(reporteConcentradoLst);
				//datos.setListTotalGlobalReg(totalGlobalRegLst);
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
	public ReporteDetalladoBean consultaReporteDetalladoAjuste(ReporteDetalladoBean datos) throws CfeException, SQLException {
		
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
			conn = dataSourceAmov.getConnection();
			
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
						selectSQL.append("select 2 as orden, 'IMPROCEDENTES CON VALOR -0.01' as activo,   "
								+ "nvl(sum(adq_baja),0) as adq_baja, nvl(sum(adq_ac_baja),0) as adq_ac_baja, "
								+ "nvl(sum(ejercicio_baja),0) as ejercicio_baja, ");
						selectSQL.append("nvl(sum(depr_tot),0) as depr_tot, "
								+ "nvl(sum(costo_h),0) as costo_h, nvl(sum(costo_act),0) as costo_act, "
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
						selectSQL.append("select 3 as orden, '100% DEPRECIADOS', "
								+ "0.00 as adq_baja, 0.00 as adq_ac_baja, "
								+ "0.00 as ejercicio_baja, ");
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
				
				datos.setListReporteDetalladoAjust(new ArrayList<BajasDosBean>());
				//datos.setListTotalGlobalAjReg(new ArrayList<BajasDosBean>());
				datos.setTotReporteDetalladoAjGeneral(new TotalBajasDosBean());
				datos.getTotReporteDetalladoAjGeneral().setAdq_baja(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setAdq_ac_baja(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setEjercicio_baja(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setDepr_tot(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setCosto_h(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setCosto_act(BigDecimal.ZERO);
				datos.getTotReporteDetalladoAjGeneral().setDepre_anual_act(BigDecimal.ZERO);
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
					
					datos.getListReporteDetalladoAjust().add(reporte);
					datos.getTotReporteDetalladoAjGeneral().setAdq_baja(datos.getTotReporteDetalladoAjGeneral().getAdq_baja().add(rs.getBigDecimal("adq_baja")));
					datos.getTotReporteDetalladoAjGeneral().setAdq_ac_baja(datos.getTotReporteDetalladoAjGeneral().getAdq_ac_baja().add(rs.getBigDecimal("adq_ac_baja")));
					datos.getTotReporteDetalladoAjGeneral().setEjercicio_baja(datos.getTotReporteDetalladoAjGeneral().getEjercicio_baja().add(rs.getBigDecimal("ejercicio_baja")));
					datos.getTotReporteDetalladoAjGeneral().setDepr_tot(datos.getTotReporteDetalladoAjGeneral().getDepr_tot().add(rs.getBigDecimal("depr_tot")));
					datos.getTotReporteDetalladoAjGeneral().setCosto_h(datos.getTotReporteDetalladoAjGeneral().getCosto_h().add(rs.getBigDecimal("costo_h")));
					datos.getTotReporteDetalladoAjGeneral().setCosto_act(datos.getTotReporteDetalladoAjGeneral().getCosto_act().add(rs.getBigDecimal("costo_act")));
					datos.getTotReporteDetalladoAjGeneral().setDepre_anual_act(datos.getTotReporteDetalladoAjGeneral().getDepre_anual_act().add(rs.getBigDecimal("depre_anual_act")));
				}
				

				datos.setTotalRepDetTot(new TotalBajasDosBean());
				datos.getTotalRepDetTot().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().subtract(datos.getTotReporteDetalladoAjGeneral().getAdq_baja()));
				datos.getTotalRepDetTot().setAdq_ac_baja(datos.getTotReporteGeneral().getAdq_ac_baja().subtract(datos.getTotReporteDetalladoAjGeneral().getAdq_ac_baja()));
				datos.getTotalRepDetTot().setEjercicio_baja(datos.getTotReporteGeneral().getEjercicio_baja().subtract(datos.getTotReporteDetalladoAjGeneral().getEjercicio_baja()));
				datos.getTotalRepDetTot().setDepr_tot(datos.getTotReporteGeneral().getDepr_tot().subtract(datos.getTotReporteDetalladoAjGeneral().getDepr_tot()));
				datos.getTotalRepDetTot().setCosto_h(datos.getTotReporteGeneral().getCosto_h().subtract(datos.getTotReporteDetalladoAjGeneral().getCosto_h()));
				datos.getTotalRepDetTot().setCosto_act(datos.getTotReporteGeneral().getCosto_act().subtract(datos.getTotReporteDetalladoAjGeneral().getCosto_act()));
				datos.getTotalRepDetTot().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().subtract(datos.getTotReporteDetalladoAjGeneral().getDepre_anual_act()));

		
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
				claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()!=null?dta.getAdq_baja(): BigDecimal.ZERO));
				claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!=null?dta.getAdq_ac_baja(): BigDecimal.ZERO));
				claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!=null?dta.getEjercicio_baja(): BigDecimal.ZERO));
				claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()!=null?dta.getDepr_tot(): BigDecimal.ZERO));
				claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()!=null?dta.getCosto_h(): BigDecimal.ZERO));
				claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()!=null?dta.getCosto_act(): BigDecimal.ZERO));
				claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act(): BigDecimal.ZERO));
				claseList.add(claseTemp);
				
			}else{
				claseTemp = claseList.get(claseList.indexOf(claseTemp));
				claseTemp.setAll_adq_baja(claseTemp.getAll_adq_baja().add(dta.getAdq_baja()!=null?dta.getAdq_baja(): BigDecimal.ZERO));
				claseTemp.setAll_adq_ac_baja(claseTemp.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!=null?dta.getAdq_ac_baja(): BigDecimal.ZERO));
				claseTemp.setAll_ejercicio_baja(claseTemp.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!=null?dta.getEjercicio_baja(): BigDecimal.ZERO));
				claseTemp.setAll_depr_tot(claseTemp.getAll_depr_tot().add(dta.getDepr_tot()!=null?dta.getDepr_tot(): BigDecimal.ZERO));
				claseTemp.setAll_costo_h(claseTemp.getAll_costo_h().add(dta.getCosto_h()!=null?dta.getCosto_h(): BigDecimal.ZERO));
				claseTemp.setAll_costo_act(claseTemp.getAll_costo_act().add(dta.getCosto_act()!=null?dta.getCosto_act(): BigDecimal.ZERO));
				claseTemp.setAll_depre_anual_act(claseTemp.getAll_depre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act(): BigDecimal.ZERO));
				claseTemp.getTextos().add(dta);
			}
			region.setAll_adq_baja(region.getAll_adq_baja().add(dta.getAdq_baja()!=null?dta.getAdq_baja(): BigDecimal.ZERO));
			region.setAll_adq_ac_baja(region.getAll_adq_ac_baja().add(dta.getAdq_ac_baja()!=null?dta.getAdq_ac_baja(): BigDecimal.ZERO));
			region.setAll_ejercicio_baja(region.getAll_ejercicio_baja().add(dta.getEjercicio_baja()!=null?dta.getEjercicio_baja(): BigDecimal.ZERO));
			region.setAll_depr_tot(region.getAll_depr_tot().add(dta.getDepr_tot()!=null?dta.getDepr_tot(): BigDecimal.ZERO));
			region.setAll_costo_h(region.getAll_costo_h().add(dta.getCosto_h()!=null?dta.getCosto_h(): BigDecimal.ZERO));
			region.setAll_costo_act(region.getAll_costo_act().add(dta.getCosto_act()!=null?dta.getCosto_act(): BigDecimal.ZERO));
			region.setAll_depre_anual_act(region.getAll_depre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act(): BigDecimal.ZERO));
		}
		if (!listRegCero.isEmpty())
			region.setNombre(listRegCero.get(0).getRegion_dsc());
		region.setClases(claseList);
		regionList.add(region);
		return regionList;
	}

	public void insertaArchivo(byte[] bytes) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try{
			conn = dataSourceAmov.getConnection();
				StringBuffer selectSQL = new StringBuffer("insert into bajas_op_documento (doc_id, doc_contenido, doc_tipo, doc_modulo, doc_descripcion, doc_nombre, doc_estatus, doc_fecha_creacion, doc_usuario_creacion, doc_estado_registro) values(?,?,?,?,?,?,1,sysdate,1,?)");
				
				
				logger.info(">>>Query insert: " + selectSQL);
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				preparedStatement.setInt(1, 0);
				preparedStatement.setBytes(2, bytes);// BLOB
				preparedStatement.setString(3, "REP");
				preparedStatement.setString(4, "RDETALLADO");
				preparedStatement.setString(5, "REPORTE DETALLADO");
				preparedStatement.setString(6, "REPORTE_DETALLADO_20231010.xls");
				preparedStatement.setInt(7, 1);
				
				preparedStatement.executeUpdate();
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



	public void registraSolicitud(BajasOpDocumentoBean documento) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try{
			conn = dataSourceAmov.getConnection();
//				StringBuffer selectSQL = new StringBuffer("insert into bajas_op_documento (doc_id, doc_contenido, doc_tipo, doc_modulo, doc_descripcion, doc_nombre, doc_estatus, doc_fecha_creacion, doc_usuario_creacion, doc_estado_registro, doc_id_sociedad, doc_p_anio, doc_p_periodos, doc_p_acumulado, doc_p_calculo, doc_p_region, doc_p_clase, doc_p_tipotexto, doc_p_textosfisc, doc_p_tipoajuste) values(?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?)");
				StringBuffer selectSQL = new StringBuffer("insert into bajas_op_documento (doc_id, doc_contenido, doc_tipo, doc_modulo, doc_descripcion, doc_nombre, doc_estatus, doc_fecha_creacion, doc_usuario_creacion, doc_estado_registro, doc_id_sociedad, doc_p_anio, doc_p_periodos, doc_p_acumulado, doc_p_calculo, doc_p_region, doc_p_clase, doc_p_tipotexto, doc_p_textosfisc, doc_p_tipoajuste, doc_p_txtsdesc) values(?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				preparedStatement.setInt(1, documento.getId());
				preparedStatement.setBytes(2, null);// BLOB
				preparedStatement.setString(3, documento.getTipo());
				preparedStatement.setString(4, documento.getModulo());
				preparedStatement.setString(5, documento.getDescripcion());
				preparedStatement.setString(6, documento.getNombre());
				preparedStatement.setInt(7, documento.getEstatus().getId());
				preparedStatement.setInt(8, documento.getUsuario_creacion().getId());
				preparedStatement.setInt(9, documento.getEstado_registro());
				preparedStatement.setInt(10, documento.getIdSociedad());
				preparedStatement.setInt(11, documento.getAnio());
				preparedStatement.setString(12, documento.getPeriodoSelect());
				preparedStatement.setInt(13, documento.getAcumulado());
				preparedStatement.setInt(14, documento.getCalculo());
				preparedStatement.setString(15, documento.getRegion());
				preparedStatement.setString(16, documento.getClase());
				preparedStatement.setString(17, documento.getTipoTxt());
				preparedStatement.setString(18, documento.getTxtfisc());
				preparedStatement.setString(19, documento.getTipoAjuste());
				preparedStatement.setString(20, documento.getTxtsDesc());
				preparedStatement.executeUpdate();
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


	public List<BajasOpDocumentoBean> consultaArchivo(BajasOpDocumentoBean doc) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<BajasOpDocumentoBean> docBeanLst = new ArrayList<BajasOpDocumentoBean>();
		BajasOpDocumentoBean docBean = null;
		try{
			conn = dataSourceAmov.getConnection();
				StringBuffer selectSQL = new StringBuffer("select doc.doc_id, doc.doc_tipo, doc.doc_modulo, doc.doc_nombre, doc.doc_estatus, edoc.estatus_descripcion, ");
				selectSQL.append("doc.doc_fecha_creacion, doc.doc_usuario_creacion,u.usuario_nombre, u.usuario_apaterno, u.usuario_amaterno , doc.doc_p_anio, doc.doc_p_periodos ");
				selectSQL.append("from bajas_op_documento doc ");
				selectSQL.append("inner join bajas_cat_estatus_doc edoc ");
				selectSQL.append("on(doc.doc_estatus = edoc.estatus_id) ");
				selectSQL.append("inner join bajas_op_usuario u ");
				selectSQL.append("on(doc.doc_usuario_creacion = u.usuario_id) ");
				selectSQL.append("where doc.doc_tipo = ? and doc.doc_modulo= ? ");
				selectSQL.append("and doc.doc_estado_registro = ? and doc.doc_usuario_creacion = ?  and doc.doc_id_sociedad = ? ");
				selectSQL.append("order by doc.doc_fecha_creacion desc ");
				
				
				
				logger.info(">>>Query select: " + selectSQL);
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				preparedStatement.setString(1, doc.getTipo());
				preparedStatement.setString(2, doc.getModulo());
				preparedStatement.setInt(3, doc.getEstado_registro());
				preparedStatement.setInt(4, doc.getUsuario_creacion().getId());
				preparedStatement.setInt(5, doc.getIdSociedad());
				
				ResultSet rs = preparedStatement.executeQuery();
				
			
				while (rs.next()) {
					docBean = new BajasOpDocumentoBean();
					docBean.setId(rs.getInt("doc_id"));
//					docBean.setContenido(rs.getBinaryStream("doc_contenido"));
					docBean.setTipo(rs.getString("doc_tipo"));
					docBean.setModulo(rs.getString("doc_modulo"));
					docBean.setNombre(rs.getString("doc_nombre"));
					BajasCatEstatusDocBean estatus = new BajasCatEstatusDocBean();
					estatus.setId(rs.getInt("doc_estatus"));
					estatus.setDescripcion(rs.getString("estatus_descripcion"));
					docBean.setEstatus(estatus);
					docBean.setFecha_creacion(rs.getDate("doc_fecha_creacion"));
					
					BajasOpUsuarioBean usuario =  new BajasOpUsuarioBean();
					usuario.setId(rs.getInt("doc_usuario_creacion"));
					usuario.setApaterno(rs.getString("usuario_apaterno"));
					usuario.setAmaterno(rs.getString("usuario_amaterno"));
					usuario.setNombre(rs.getString("usuario_nombre"));
					docBean.setUsuario_creacion(usuario);
					docBean.setAnio(rs.getInt("doc_p_anio"));
					docBean.setPeriodoSelect(rs.getString("doc_p_periodos"));
					StringTokenizer st = new StringTokenizer(docBean.getPeriodoSelect(), ",");
					String t = null;
					
					List<String> months = new ArrayList<String>();
					while (st.hasMoreTokens()) {
						t = st.nextToken();
						if("ENE".equals(t)) {
							months.add("1");
						}else if("FEB".equals(t)) {
							months.add("2");
						}else if("MZO".equals(t)) {
							months.add("3");
						}else if("ABR".equals(t)) {
							months.add("4");
						}else if("MAY".equals(t)) {
							months.add("5");
						}else if("JUN".equals(t)) {
							months.add("6");
						}else if("JUL".equals(t)) {
							months.add("7");
						}else if("AGO".equals(t)) {
							months.add("8");
						}else if("SEP".equals(t)) {
							months.add("9");
						}else if("OCT".equals(t)) {
							months.add("10");
						}else if("NOV".equals(t)) {
							months.add("11");
						}else if("DIC".equals(t)) {
							months.add("12");
						} 
					}
					Integer countM = 0;
					StringBuilder smonth = new StringBuilder("");
					for(String id : months) {
						smonth.append(id);
						countM++;
						if(countM < months.size()) {
							smonth.append(",");
						}
					}
					docBean.setPeriodoSelectIds(smonth.toString());
					
//					docBean.setiContenido(WorkbookFactory.create(docBean.getContenido()));
					docBeanLst.add(docBean);
				}
		}catch(CfeException e){
			throw e;
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	return docBeanLst;
	}



	public BajasOpDocumentoBean consultaArchivoId(Integer id) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		BajasOpDocumentoBean docBean = null;
		try{
			conn = dataSourceAmov.getConnection();
				StringBuffer selectSQL = new StringBuffer("select doc_id, doc_contenido, doc_tipo, doc_modulo, doc_nombre ");
				selectSQL.append("from bajas_op_documento where doc_id = ? and doc_estado_registro = ? ");
				
				
				logger.info(">>>Query select: " + selectSQL);
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, 1);
				ResultSet rs = preparedStatement.executeQuery();
				
			
				while (rs.next()) {
					docBean = new BajasOpDocumentoBean();
					docBean.setId(rs.getInt("doc_id"));
					docBean.setContenido(rs.getBinaryStream("doc_contenido"));
					docBean.setTipo(rs.getString("doc_tipo"));
					docBean.setModulo(rs.getString("doc_modulo"));
					docBean.setNombre(rs.getString("doc_nombre"));
					docBean.setiContenido(WorkbookFactory.create(docBean.getContenido()));
				
				}
		}catch(CfeException e){
			throw e;
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	return docBean;
	}

	public void generaArchivo(BajasOpDocumentoBean doc) throws CfeException, SQLException {
		Connection conn = null;
		CallableStatement ejecute = null;
		BajasOpDocumentoBean docBean = null;
		try{
			conn = dataSourceAmov.getConnection();
			
			ejecute = conn.prepareCall("call sp_bajas_documento (?,?,?)");
			ejecute.setInt(1, 3); //proceso 
			ejecute.setInt(2, doc.getIdSociedad());  // sociedad
			ejecute.setInt(3, doc.getUsuario_creacion().getId()); // usuario
			ejecute.execute();
			

		} catch (SQLException e) {
			throw new SQLException(e);
		}catch(CfeException e){
			throw e;
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ejecute != null)
					ejecute.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				
				logger.error("Error de BD al cerrar conexion ",
						e);
			
			}
	//return docBean;
	}
	}

}