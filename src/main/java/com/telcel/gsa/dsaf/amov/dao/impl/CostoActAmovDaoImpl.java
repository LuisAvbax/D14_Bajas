package com.telcel.gsa.dsaf.amov.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.amov.dao.CostoActAmovDao;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.RepAdqClasBean;
import com.telcel.gsa.dsaf.bean.RepAdqRegBean;
import com.telcel.gsa.dsaf.bean.RepAdqTextoBean;
import com.telcel.gsa.dsaf.bean.ReporteBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
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

@Repository
public class CostoActAmovDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements CostoActAmovDao, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -600814647102535519L;
	final static Logger logger = LoggerFactory.getLogger(CostoActAmovDaoImpl.class);
	@Autowired
	transient DataSource dataSourceAmov;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public CostoActAmovDaoImpl(){
		super(v_bajas_nvo3.class);
	}


	@Override
	public CostoBean consultaRegion(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
		List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
		List<RepAdqRegBean> repAdqRegBeanLstTot = new ArrayList<RepAdqRegBean>();
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
			
			conn = dataSourceAmov.getConnection();
			
				BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
				BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
				BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
				BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select region,region_dsc,per_baja,"
						+ "sum(nvl(costo_act,0)) deduccion  "
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
				datos.setListReporte(listReporte);
				datos.setListRepAdqReg(repAdqRegBeanLst);
				datos.setListRepAdqRegTot(repAdqRegBeanLstTot);
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
	public  CostoBean consultaRegionAjustes(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
		List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
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
			conn = dataSourceAmov.getConnection();
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select region,region_dsc,per_baja,"
						+ "sum(nvl(costo_act,0)) deduccion  "
						+ " from v_bajas_nvo4 ");
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
				datos.setListReporteAjuste(listReporte);
				datos.setListRepAjAdqReg(repAdqRegBeanLst); // OK
				datos.setTotReporteMesAjusteGeneral(datMesRep); // OK

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
	public CostoBean consultaRegionNetos(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
		List<RepAdqRegBean> repAdqRegBeanLst = new ArrayList<RepAdqRegBean>();
		
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
			
			conn = dataSourceAmov.getConnection();
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
			StringBuffer selectSQL = new StringBuffer("select vbnt.region,vbnt.region_dsc,vbnt.per_baja,"
					+ "sum(nvl(vbnt.costo_act,0)) deduccion  "
					+ " from v_bajas_nvo3 vbnt left outer join v_bajas_nvo4 vbnc "
					+ " ON (vbnt.num_activo = vbnc.num_activo and vbnt.sub = vbnc.sub and vbnt.anio_td = vbnc.anio_td and vbnt.per_baja = vbnc.per_baja) "
					+ " where vbnc.num_activo is null and vbnc.sub is null and vbnc.anio_td is null and vbnc.per_baja is null ");
			selectSQL.append(" and vbnt.d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND vbnt.area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND vbnt.per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and vbnt.adq_baja not in(-1.00,-0.01) ");
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND vbnt.region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND vbnt.clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by vbnt.region,vbnt.region_dsc,vbnt.per_baja ");
			selectSQL.append( " order by vbnt.region,vbnt.region_dsc,vbnt.per_baja ");
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
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
		case 1:{
			tmp.setEnero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 2:{
			tmp.setFebrero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 3:{
			tmp.setMarzo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 4:{
			tmp.setAbril(rs.getBigDecimal("deduccion"));
		}
		break;
		case 5:{
			tmp.setMayo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 6:{
			tmp.setJunio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 7:{
			tmp.setJulio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 8:{
			tmp.setAgosto(rs.getBigDecimal("deduccion"));
		}
		break;
		case 9:{
			tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 10:{
			tmp.setOctubre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 11:{
			tmp.setNoviembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 12:{
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
		case 1:{
			tmp.setEnero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 2:{
			tmp.setFebrero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 3:{
			tmp.setMarzo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 4:{
			tmp.setAbril(rs.getBigDecimal("deduccion"));
		}
		break;
		case 5:{
			tmp.setMayo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 6:{
			tmp.setJunio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 7:{
			tmp.setJulio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 8:{
			tmp.setAgosto(rs.getBigDecimal("deduccion"));
		}
		break;
		case 9:{
			tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 10:{
			tmp.setOctubre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 11:{
			tmp.setNoviembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 12:{
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
		case 1:{
			tmp.setEnero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 2:{
			tmp.setFebrero(rs.getBigDecimal("deduccion"));
		}
		break;
		case 3:{
			tmp.setMarzo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 4:{
			tmp.setAbril(rs.getBigDecimal("deduccion"));
		}
		break;
		case 5:{
			tmp.setMayo(rs.getBigDecimal("deduccion"));
		}
		break;
		case 6:{
			tmp.setJunio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 7:{
			tmp.setJulio(rs.getBigDecimal("deduccion"));
		}
		break;
		case 8:{
			tmp.setAgosto(rs.getBigDecimal("deduccion"));
		}
		break;
		case 9:{
			tmp.setSeptiembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 10:{
			tmp.setOctubre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 11:{
			tmp.setNoviembre(rs.getBigDecimal("deduccion"));
		}
		break;
		case 12:{
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
	public CostoBean consultaTexto(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
		List<RepAdqTextoBean> repAdqRegBeanLst = new ArrayList<RepAdqTextoBean>();
		List<RepAdqTextoBean> repAdqRegBeanLstTot = new ArrayList<RepAdqTextoBean>();
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
			
			conn = dataSourceAmov.getConnection();
			
				BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
				BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
				BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
				BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select texto_baja, per_baja, "
						+ "sum(nvl(costo_act,0)) deduccion   "
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
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
	public CostoBean consultaClase(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<ReporteBean> listReporte= new ArrayList<ReporteBean>();
		List<RepAdqClasBean> repAdqRegBeanLst = new ArrayList<RepAdqClasBean>();
		List<RepAdqClasBean> repAdqRegBeanLstTot = new ArrayList<RepAdqClasBean>();
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
			
			conn = dataSourceAmov.getConnection();
			
				BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
				BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
				BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
				BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc as clase_dsc,per_baja,"
						+ "sum(nvl(costo_act,0)) deduccion   "
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
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
	public CostoBean consultaClaseAjustes(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
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
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc as clase_dsc,per_baja,"
						+ "sum(nvl(costo_act,0)) deduccion "
						+ " from v_bajas_nvo4 ");
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
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
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
	public CostoBean consultaClaseNetos(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
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
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
			StringBuffer selectSQL = new StringBuffer("select vbnt.clase||'-'||vbnt.clase_dsc as clase_dsc,vbnt.per_baja,"
					+ "sum(nvl(vbnt.costo_act,0)) deduccion "
					+ " from v_bajas_nvo3 vbnt left outer join v_bajas_nvo4 vbnc "
					+ " ON (vbnt.num_activo = vbnc.num_activo and vbnt.sub = vbnc.sub and vbnt.anio_td = vbnc.anio_td and vbnt.per_baja = vbnc.per_baja) "
					+ " where vbnc.num_activo is null and vbnc.sub is null and vbnc.anio_td is null and vbnc.per_baja is null ");
			selectSQL.append(" and vbnt.d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND vbnt.area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND vbnt.per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and vbnt.adq_baja not in(-1.00,-0.01) ");
			
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND vbnt.region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND vbnt.clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by vbnt.clase,vbnt.clase_dsc,vbnt.per_baja ");
			selectSQL.append( " order by 1,vbnt.per_baja ");
				logger.info(">>>Query netos clase: " +selectSQL );
				logger.info("Query netos clase"+selectSQL.toString());
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					repAdqRegBeanLst = getDeduccionClase(rs, repAdqRegBeanLst);
					ReporteBean reporte = new ReporteBean();
					reporte.setDescCve(rs.getString("clase_dsc"));
					reporte.setPerBaja(rs.getInt("per_baja"));
					reporte.setDeduccion(rs.getBigDecimal("deduccion"));
					listReporte.add(reporte);
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
//				
				datos.setListRepAdqClas(repAdqRegBeanLst);
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
				throw new CfeException("Error en consulta region netos..", e);
			}
		}
		return datos;
	}


	@Override
	public CostoBean consultaTextoAjustes(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
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

			conn = dataSourceAmov.getConnection();
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
				StringBuffer selectSQL = new StringBuffer("select texto_baja,per_baja,"
						+ "sum(nvl(costo_act,0)) deduccion  "
						+ " from v_bajas_nvo4 ");
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
							
				selectSQL.append( " group by texto_baja,per_baja ");
				selectSQL.append( " order by texto_baja,per_baja ");
				logger.info(">>>Query consultaTextoAjustes: " + selectSQL);
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				ResultSet rs = preparedStatement.executeQuery();
				
				while (rs.next()) {
					repAdqRegBeanLst = getDeduccionTexto(rs, repAdqRegBeanLst);
					ReporteBean reporte = new ReporteBean();
					reporte.setCve(rs.getString("texto_baja"));
					reporte.setPerBaja(rs.getInt("per_baja"));
					reporte.setDeduccion(rs.getBigDecimal("deduccion"));
					
					listReporte.add(reporte);
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
				datos.setListReporteAjuste(listReporte);
				datos.setListRepAjAdqTxt(repAdqRegBeanLst);
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
	public CostoBean consultaTextoNetos(CostoBean datos) throws CfeException, SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
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
			
			conn = dataSourceAmov.getConnection();
			
			BigDecimal ene = new BigDecimal("0.00"); BigDecimal feb = new BigDecimal("0.00"); BigDecimal mar = new BigDecimal("0.00"); 
			BigDecimal abr= new BigDecimal("0.00");  BigDecimal may= new BigDecimal("0.00"); BigDecimal jun= new BigDecimal("0.00"); 
			BigDecimal jul= new BigDecimal("0.00"); BigDecimal agos= new BigDecimal("0.00"); BigDecimal sep= new BigDecimal("0.00"); 
			BigDecimal oct= new BigDecimal("0.00"); BigDecimal nov= new BigDecimal("0.00"); BigDecimal dic = new BigDecimal("0.00");
			StringBuffer selectSQL = new StringBuffer("select vbnt.texto_baja,vbnt.per_baja,"
					+ "sum(nvl(vbnt.costo_act,0)) deduccion   "
					+ " from v_bajas_nvo3 vbnt left outer join v_bajas_nvo4 vbnc "
					+ " ON (vbnt.num_activo = vbnc.num_activo and vbnt.sub = vbnc.sub and vbnt.anio_td = vbnc.anio_td and vbnt.per_baja = vbnc.per_baja)   "
					+ " where vbnc.num_activo is null and vbnc.sub is null and vbnc.anio_td is null and vbnc.per_baja is null ");
			selectSQL.append(" and vbnt.d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND vbnt.area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND vbnt.per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append( " and vbnt.adq_baja not in(-1.00,-0.01) ");
			
			if (datos.getRegion() != null && datos.getRegion().size() > 0)
			{
				selectSQL.append( " AND vbnt.region IN (");
				selectSQL.append( filterRegion+")");
			}
			if(datos.getClase() != null && datos.getClase().size() > 0) 
			{
				selectSQL.append( " AND vbnt.clase IN ( ");
				selectSQL.append( filterClase+")");
			}
			if("TODOS".equalsIgnoreCase(datos.getTexto()) && datos.getTxtDesc() != null && datos.getTxtDesc().size()>0){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() != null && datos.getTxtDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
				selectSQL.append( " AND vbnt.texto_baja in (");
				selectSQL.append( filterTxt+")");
			}
						
			selectSQL.append( " group by vbnt.texto_baja,vbnt.per_baja ");
			selectSQL.append( " order by vbnt.texto_baja,vbnt.per_baja ");
				logger.info(">>>Query txt neto: " + selectSQL);
				preparedStatement = conn.prepareStatement(selectSQL.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					repAdqRegBeanLst = getDeduccionTexto(rs, repAdqRegBeanLst);
					ReporteBean reporte = new ReporteBean();
					reporte.setCve(rs.getString("texto_baja"));
					reporte.setPerBaja(rs.getInt("per_baja"));
					reporte.setDeduccion(rs.getBigDecimal("deduccion"));
					listReporte.add(reporte);
					switch (reporte.getPerBaja())
					{
						case 1:
							ene = ene.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 2:
							feb = feb.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 3:
							mar = mar.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 4:
							abr = abr.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 5:
							may = may.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 6:
							jun = jun.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 7:
							jul = jul.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 8:
							agos = agos.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 9:
							sep = sep.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 10:
							oct = oct.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 11:
							nov = nov.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
						case 12:
							dic = dic.add(reporte.getDeduccion()!= null ? reporte.getDeduccion(): BigDecimal.ZERO);
							break;
					}
				}
				
				
				//se agrega a la lista de total de meses
				TotalBean datMesRep = new TotalBean();
				datMesRep = utileriasCfeDao.inicializaTotales(datMesRep);
				datMesRep.setEnero(ene);
				datMesRep.setFebrero(feb);
				datMesRep.setMarzo(mar);
				datMesRep.setAbril(abr);
				datMesRep.setMayo(may);
				datMesRep.setJunio(jun);
				datMesRep.setJulio(jul);
				datMesRep.setAgosto(agos);
				datMesRep.setSeptiembre(sep);
				datMesRep.setOctubre(oct);
				datMesRep.setNoviembre(nov);
				datMesRep.setDiciembre(dic);
				datMesRep.setTotal(ene.add(feb.add(mar.add(abr.add(may.add(jun.add(jul.add(agos.add(sep.add(oct.add(nov.add(dic))))))))))));
		
				datos.setListRepAdqtxt(repAdqRegBeanLst);
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
				throw new CfeException("Error en consulta region netos..", e);
			}
		}
		return datos;
	}


}