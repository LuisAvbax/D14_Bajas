package com.telcel.gsa.dsaf.sercotel.dao.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasInstitucionalAjusteBean;
import com.telcel.gsa.dsaf.bean.BajasTresBean;
import com.telcel.gsa.dsaf.bean.BajasTresBeanResumen;
import com.telcel.gsa.dsaf.bean.InstAjusteBean;

import com.telcel.gsa.dsaf.bean.TGClaseTresBean;

import com.telcel.gsa.dsaf.bean.TotalBajasTresBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.sercotel.dao.InstitucionalAjustesSercotelDao;

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
public class InstitucionalAjustesSercotelDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements InstitucionalAjustesSercotelDao, Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6568379609239969595L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(InstitucionalAjustesSercotelDaoImpl.class);
	@Autowired
	transient DataSource dataSourceSercotel;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public InstitucionalAjustesSercotelDaoImpl(){
		super(v_bajas_nvo3.class);
	}

@Override
public InstAjusteBean consultaInstitucionalAjustes(InstAjusteBean datos) throws CfeException, SQLException {
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	String filterRegion = null;
	String filterClase = null;
	String filterTxt= null;
	
	List<BajasInstitucionalAjusteBean> listReporte= new ArrayList<BajasInstitucionalAjusteBean>();
	List<BajasTresBean> listTextos= new ArrayList<BajasTresBean>();
	List<BajasDosBean> totalGlobalRegLst = new ArrayList<BajasDosBean>();
	TotalBajasTresBean totalGeneral = new TotalBajasTresBean();
	datos.setTotReporteGeneral(new TotalBajasTresBean());
	
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
		StringBuffer selectSQL = new StringBuffer("select clase||'-'||clase_dsc clase_dsc,texto_baja,region,region_dsc, "
				+ "sum(NVL(adq_baja,0)) as adq_baja, sum(NVL(costo_act,0)) as costo_act, sum(NVL(depre_anual_act,0)) as depre_anual_act "
				+ "from v_bajas_nvo4 ");
		
			selectSQL.append(" where d_inv= '");
			selectSQL.append( datos.getdInv() +"'");
			selectSQL.append( " AND area_val='");
			selectSQL.append( datos.getAreaVal()+"'");
			selectSQL.append( " AND per_baja in (");
			selectSQL.append( datos.getMesesConsulta()+")");
			selectSQL.append("AND adq_baja in (-1.00,-0.01) ");
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
			selectSQL.append( " group by 1,texto_baja,region,region_dsc  ");
			selectSQL.append( " union  ");
			selectSQL.append("select clase||'-'||clase_dsc clase_dsc,texto_baja,region,region_dsc, "
					+ "0.0 as adq_baja, sum(NVL(costo_act,0)) as costo_act, sum(NVL(depre_anual_act,0)) as depre_anual_act "
					+ "from v_bajas_nvo4 ");
			
				selectSQL.append(" where d_inv= '");
				selectSQL.append( datos.getdInv() +"'");
				selectSQL.append( " AND area_val='");
				selectSQL.append( datos.getAreaVal()+"'");
				selectSQL.append( " AND per_baja in (");
				selectSQL.append( datos.getMesesConsulta()+")");
				selectSQL.append("AND adq_baja not in (-1.00,-0.01) ");
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
				selectSQL.append( " group by 1,texto_baja,region,region_dsc  ");

			selectSQL.append( " order by 1 asc,texto_baja,region asc  ");
			logger.info(">>>Query IntitucionalRegion: " + selectSQL);
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			TotalBajasTresBean datMesRep = new TotalBajasTresBean();

			List<TGClaseTresBean> classList = new ArrayList<TGClaseTresBean>();
			TGClaseTresBean claseTemp = null;
			
			totalGeneral.setAdq_baja_r0(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r0(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r0(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r1(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r1(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r1(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r2(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r2(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r2(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r3(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r3(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r3(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r4(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r4(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r4(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r5(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r5(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r5(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r6(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r6(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r6(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r7(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r7(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r7(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r8(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r8(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r8(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r9(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r9(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r9(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_total(BigDecimal.ZERO);
			totalGeneral.setCosto_act_total(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_total(BigDecimal.ZERO);
			
			BajasInstitucionalAjusteBean reporte = null;
			BajasTresBean txt = null;
			
			while (rs.next()) {
				
				reporte = new BajasInstitucionalAjusteBean();
				txt = new BajasTresBean();
				
				reporte.setClase(rs.getString("clase_dsc"));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				
				txt.setTexto_baja(reporte.getTexto_baja());
				txt.setClase_dsc(reporte.getClase());
				txt.setClase(reporte.getClase().substring(0,reporte.getClase().indexOf('-')));
				txt.setAdq_baja_r0(BigDecimal.ZERO);
				txt.setAdq_baja_r1(BigDecimal.ZERO);
				txt.setAdq_baja_r2(BigDecimal.ZERO);
				txt.setAdq_baja_r3(BigDecimal.ZERO);
				txt.setAdq_baja_r4(BigDecimal.ZERO);
				txt.setAdq_baja_r5(BigDecimal.ZERO);
				txt.setAdq_baja_r6(BigDecimal.ZERO);
				txt.setAdq_baja_r7(BigDecimal.ZERO);
				txt.setAdq_baja_r8(BigDecimal.ZERO);
				txt.setAdq_baja_r9(BigDecimal.ZERO);
				txt.setAdq_baja_total(BigDecimal.ZERO);
				txt.setCosto_act_r0(BigDecimal.ZERO);
				txt.setCosto_act_r1(BigDecimal.ZERO);
				txt.setCosto_act_r2(BigDecimal.ZERO);
				txt.setCosto_act_r3(BigDecimal.ZERO);
				txt.setCosto_act_r4(BigDecimal.ZERO);
				txt.setCosto_act_r5(BigDecimal.ZERO);
				txt.setCosto_act_r6(BigDecimal.ZERO);
				txt.setCosto_act_r7(BigDecimal.ZERO);
				txt.setCosto_act_r8(BigDecimal.ZERO);
				txt.setCosto_act_r9(BigDecimal.ZERO);
				txt.setCosto_act_total(BigDecimal.ZERO);
				txt.setDepre_anual_act_r0(BigDecimal.ZERO);
				txt.setDepre_anual_act_r1(BigDecimal.ZERO);
				txt.setDepre_anual_act_r2(BigDecimal.ZERO);
				txt.setDepre_anual_act_r3(BigDecimal.ZERO);
				txt.setDepre_anual_act_r4(BigDecimal.ZERO);
				txt.setDepre_anual_act_r5(BigDecimal.ZERO);
				txt.setDepre_anual_act_r6(BigDecimal.ZERO);
				txt.setDepre_anual_act_r7(BigDecimal.ZERO);
				txt.setDepre_anual_act_r8(BigDecimal.ZERO);
				txt.setDepre_anual_act_r9(BigDecimal.ZERO);
				txt.setDepre_anual_act_total(BigDecimal.ZERO);
				
				
				switch(Integer.parseInt(reporte.getRegion().replace("MR", ""))){
					case 0:{
						totalGeneral.setAdq_baja_r0(totalGeneral.getAdq_baja_r0().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r0(totalGeneral.getCosto_act_r0().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r0(totalGeneral.getDepre_anual_act_r0().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r0(reporte.getAdq_baja());
							txt.setCosto_act_r0(reporte.getCosto_act());
							txt.setDepre_anual_act_r0(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r0(txt.getAdq_baja_r0().add(reporte.getAdq_baja()));
							txt.setCosto_act_r0(txt.getCosto_act_r0().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r0(txt.getDepre_anual_act_r0().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 1:{
						totalGeneral.setAdq_baja_r1(totalGeneral.getAdq_baja_r1().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r1(totalGeneral.getCosto_act_r1().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r1(totalGeneral.getDepre_anual_act_r1().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r1(reporte.getAdq_baja());
							txt.setCosto_act_r1(reporte.getCosto_act());
							txt.setDepre_anual_act_r1(reporte.getDepre_anual_act());
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r1(txt.getAdq_baja_r1().add(reporte.getAdq_baja()));
							txt.setCosto_act_r1(txt.getCosto_act_r1().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r1(txt.getDepre_anual_act_r1().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 2:{
						totalGeneral.setAdq_baja_r2(totalGeneral.getAdq_baja_r2().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r2(totalGeneral.getCosto_act_r2().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r2(totalGeneral.getDepre_anual_act_r2().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r2(reporte.getAdq_baja());
							txt.setCosto_act_r2(reporte.getCosto_act());
							txt.setDepre_anual_act_r2(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r2(txt.getAdq_baja_r2().add(reporte.getAdq_baja()));
							txt.setCosto_act_r2(txt.getCosto_act_r2().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r2(txt.getDepre_anual_act_r2().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						}
					}break;
					case 3:{
						totalGeneral.setAdq_baja_r3(totalGeneral.getAdq_baja_r3().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r3(totalGeneral.getCosto_act_r3().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r3(totalGeneral.getDepre_anual_act_r3().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r3(reporte.getAdq_baja());
							txt.setCosto_act_r3(reporte.getCosto_act());
							txt.setDepre_anual_act_r3(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r3(txt.getAdq_baja_r3().add(reporte.getAdq_baja()));
							txt.setCosto_act_r3(txt.getCosto_act_r3().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r3(txt.getDepre_anual_act_r3().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 4:{
						totalGeneral.setAdq_baja_r4(totalGeneral.getAdq_baja_r4().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r4(totalGeneral.getCosto_act_r4().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r4(totalGeneral.getDepre_anual_act_r4().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r4(reporte.getAdq_baja());
							txt.setCosto_act_r4(reporte.getCosto_act());
							txt.setDepre_anual_act_r4(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r4(txt.getAdq_baja_r4().add(reporte.getAdq_baja()));
							txt.setCosto_act_r4(txt.getCosto_act_r4().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r4(txt.getDepre_anual_act_r4().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 5:{
						totalGeneral.setAdq_baja_r5(totalGeneral.getAdq_baja_r5().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r5(totalGeneral.getCosto_act_r5().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r5(totalGeneral.getDepre_anual_act_r5().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r5(reporte.getAdq_baja());
							txt.setCosto_act_r5(reporte.getCosto_act());
							txt.setDepre_anual_act_r5(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r5(txt.getAdq_baja_r5().add(reporte.getAdq_baja()));
							txt.setCosto_act_r5(txt.getCosto_act_r5().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r5(txt.getDepre_anual_act_r5().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 6:{
						totalGeneral.setAdq_baja_r6(totalGeneral.getAdq_baja_r6().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r6(totalGeneral.getCosto_act_r6().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r6(totalGeneral.getDepre_anual_act_r6().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r6(reporte.getAdq_baja());
							txt.setCosto_act_r6(reporte.getCosto_act());
							txt.setDepre_anual_act_r6(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
							
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r6(txt.getAdq_baja_r6().add(reporte.getAdq_baja()));
							txt.setCosto_act_r6(txt.getCosto_act_r6().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r6(txt.getDepre_anual_act_r6().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 7:{
						totalGeneral.setAdq_baja_r7(totalGeneral.getAdq_baja_r7().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r7(totalGeneral.getCosto_act_r7().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r7(totalGeneral.getDepre_anual_act_r7().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r7(reporte.getAdq_baja());
							txt.setCosto_act_r7(reporte.getCosto_act());
							txt.setDepre_anual_act_r7(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r7(txt.getAdq_baja_r7().add(reporte.getAdq_baja()));
							txt.setCosto_act_r7(txt.getCosto_act_r7().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r7(txt.getDepre_anual_act_r7().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
					case 8:{
						totalGeneral.setAdq_baja_r8(totalGeneral.getAdq_baja_r8().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r8(totalGeneral.getCosto_act_r8().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r8(totalGeneral.getDepre_anual_act_r8().add(reporte.getDepre_anual_act()));
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r8(reporte.getAdq_baja());
							txt.setCosto_act_r8(reporte.getCosto_act());
							txt.setDepre_anual_act_r8(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r8(txt.getAdq_baja_r8().add(reporte.getAdq_baja()));
							txt.setCosto_act_r8(txt.getCosto_act_r8().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r8(txt.getDepre_anual_act_r8().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						}
					}break;
					case 9:{
						totalGeneral.setAdq_baja_r9(totalGeneral.getAdq_baja_r9().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_r9(totalGeneral.getCosto_act_r9().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_r9(reporte.getDepre_anual_act());
						totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
						totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
						totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						if(!listTextos.contains(txt)){
							txt.setAdq_baja_r9(reporte.getAdq_baja());
							txt.setCosto_act_r9(reporte.getCosto_act());
							txt.setDepre_anual_act_r9(reporte.getDepre_anual_act());
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
							listTextos.add(txt);
						}else{
							txt = listTextos.get(listTextos.indexOf(txt));
							txt.setAdq_baja_r9(txt.getAdq_baja_r9().add(reporte.getAdq_baja()));
							txt.setCosto_act_r9(txt.getCosto_act_r9().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_r9(txt.getDepre_anual_act_r9().add(reporte.getDepre_anual_act()));
							
							txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
							txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
							txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
							
						}
					}break;
				}
				
			
				//lista para jasper report
				listReporte.add(reporte);

					
			}
			
			for(BajasInstitucionalAjusteBean rep: listReporte){
				claseTemp = new TGClaseTresBean();	
				claseTemp.setNombre(rep.getClase());
				claseTemp.setNombreCorto(rep.getClase().substring(0,rep.getClase().indexOf('-')));
				
				if(!classList.contains(claseTemp)){
					claseTemp.setAll_adq_baja_r0(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r0(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r0(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r1(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r1(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r1(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r2(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r2(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r2(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r3(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r3(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r3(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r4(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r4(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r4(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r5(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r5(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r5(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r6(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r6(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r6(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r7(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r7(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r7(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r8(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r8(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r8(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_r9(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_r9(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_r9(BigDecimal.ZERO);
					claseTemp.setAll_adq_baja_total(BigDecimal.ZERO);
					claseTemp.setAll_costo_act_total(BigDecimal.ZERO);
					claseTemp.setAll_depre_anual_act_total(BigDecimal.ZERO);
//					claseTemp.setAll_adq_baja_total(BigDecimal.ZERO);
//					claseTemp.setAll_costo_act_total(BigDecimal.ZERO);
//					claseTemp.setAll_depre_anual_act_total(BigDecimal.ZERO);
					claseTemp.setTextos(new ArrayList<BajasTresBean>());
					
					claseTemp.setAll_adq_baja_total(claseTemp.getAll_adq_baja_total().add(rep.getAdq_baja()));
					claseTemp.setAll_costo_act_total(claseTemp.getAll_costo_act_total().add(rep.getCosto_act()));
					claseTemp.setAll_depre_anual_act_total(claseTemp.getAll_depre_anual_act_total().add(rep.getDepre_anual_act()));
					
					switch(Integer.parseInt(rep.getRegion().replace("MR", ""))){
					case 0:{
						claseTemp.setAll_adq_baja_r0(claseTemp.getAll_adq_baja_r0().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r0(claseTemp.getAll_costo_act_r0().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r0(claseTemp.getAll_depre_anual_act_r0().add(rep.getDepre_anual_act()));
						
					}break;
					case 1:{
						claseTemp.setAll_adq_baja_r1(claseTemp.getAll_adq_baja_r1().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r1(claseTemp.getAll_costo_act_r1().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r1(claseTemp.getAll_depre_anual_act_r1().add(rep.getDepre_anual_act()));
						
					}break;
					case 2:{
						claseTemp.setAll_adq_baja_r2(claseTemp.getAll_adq_baja_r2().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r2(claseTemp.getAll_costo_act_r2().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r2(claseTemp.getAll_depre_anual_act_r2().add(rep.getDepre_anual_act()));
						
					}break;
					case 3:{
						claseTemp.setAll_adq_baja_r3(claseTemp.getAll_adq_baja_r3().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r3(claseTemp.getAll_costo_act_r3().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r3(claseTemp.getAll_depre_anual_act_r3().add(rep.getDepre_anual_act()));
						
					}break;
					case 4:{
						claseTemp.setAll_adq_baja_r4(claseTemp.getAll_adq_baja_r4().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r4(claseTemp.getAll_costo_act_r4().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r4(claseTemp.getAll_depre_anual_act_r4().add(rep.getDepre_anual_act()));
						
					}break;
					case 5:{
						claseTemp.setAll_adq_baja_r5(claseTemp.getAll_adq_baja_r5().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r5(claseTemp.getAll_costo_act_r5().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r5(claseTemp.getAll_depre_anual_act_r5().add(rep.getDepre_anual_act()));
						
					}break;
					case 6:{
						claseTemp.setAll_adq_baja_r6(claseTemp.getAll_adq_baja_r6().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r6(claseTemp.getAll_costo_act_r6().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r6(claseTemp.getAll_depre_anual_act_r6().add(rep.getDepre_anual_act()));
						
					}break;
					case 7:{
						claseTemp.setAll_adq_baja_r7(claseTemp.getAll_adq_baja_r7().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r7(claseTemp.getAll_costo_act_r7().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r7(claseTemp.getAll_depre_anual_act_r7().add(rep.getDepre_anual_act()));
						
					}break;
					case 8:{
						claseTemp.setAll_adq_baja_r8(claseTemp.getAll_adq_baja_r8().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r8(claseTemp.getAll_costo_act_r8().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r8(claseTemp.getAll_depre_anual_act_r8().add(rep.getDepre_anual_act()));
						
					}break;
					case 9:{
						claseTemp.setAll_adq_baja_r9(claseTemp.getAll_adq_baja_r9().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r9(claseTemp.getAll_costo_act_r9().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r9(claseTemp.getAll_depre_anual_act_r9().add(rep.getDepre_anual_act()));
					}break;
				}
					for(BajasTresBean btb: listTextos){
						if(btb.getClase_dsc().equalsIgnoreCase(claseTemp.getNombre()) && !claseTemp.getTextos().contains(btb)){
							claseTemp.getTextos().add(btb);
						}
					}
					classList.add(claseTemp);
				}else{
					claseTemp = classList.get(classList.indexOf(claseTemp));
					
					claseTemp.setAll_adq_baja_total(claseTemp.getAll_adq_baja_total().add(rep.getAdq_baja()));
					claseTemp.setAll_costo_act_total(claseTemp.getAll_costo_act_total().add(rep.getCosto_act()));
					claseTemp.setAll_depre_anual_act_total(claseTemp.getAll_depre_anual_act_total().add(rep.getDepre_anual_act()));
					
					switch(Integer.parseInt(rep.getRegion().replace("MR", ""))){
					
					case 0:{
						claseTemp.setAll_adq_baja_r0(claseTemp.getAll_adq_baja_r0().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r0(claseTemp.getAll_costo_act_r0().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r0(claseTemp.getAll_depre_anual_act_r0().add(rep.getDepre_anual_act()));
						
					}break;
					case 1:{
						claseTemp.setAll_adq_baja_r1(claseTemp.getAll_adq_baja_r1().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r1(claseTemp.getAll_costo_act_r1().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r1(claseTemp.getAll_depre_anual_act_r1().add(rep.getDepre_anual_act()));
						
					}break;
					case 2:{
						claseTemp.setAll_adq_baja_r2(claseTemp.getAll_adq_baja_r2().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r2(claseTemp.getAll_costo_act_r2().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r2(claseTemp.getAll_depre_anual_act_r2().add(rep.getDepre_anual_act()));
						
					}break;
					case 3:{
						claseTemp.setAll_adq_baja_r3(claseTemp.getAll_adq_baja_r3().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r3(claseTemp.getAll_costo_act_r3().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r3(claseTemp.getAll_depre_anual_act_r3().add(rep.getDepre_anual_act()));
						
					}break;
					case 4:{
						claseTemp.setAll_adq_baja_r4(claseTemp.getAll_adq_baja_r4().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r4(claseTemp.getAll_costo_act_r4().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r4(claseTemp.getAll_depre_anual_act_r4().add(rep.getDepre_anual_act()));
						
					}break;
					case 5:{
						claseTemp.setAll_adq_baja_r5(claseTemp.getAll_adq_baja_r5().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r5(claseTemp.getAll_costo_act_r5().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r5(claseTemp.getAll_depre_anual_act_r5().add(rep.getDepre_anual_act()));
						
					}break;
					case 6:{
						claseTemp.setAll_adq_baja_r6(claseTemp.getAll_adq_baja_r6().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r6(claseTemp.getAll_costo_act_r6().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r6(claseTemp.getAll_depre_anual_act_r6().add(rep.getDepre_anual_act()));
						
					}break;
					case 7:{
						claseTemp.setAll_adq_baja_r7(claseTemp.getAll_adq_baja_r7().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r7(claseTemp.getAll_costo_act_r7().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r7(claseTemp.getAll_depre_anual_act_r7().add(rep.getDepre_anual_act()));
						
					}break;
					case 8:{
						claseTemp.setAll_adq_baja_r8(claseTemp.getAll_adq_baja_r8().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r8(claseTemp.getAll_costo_act_r8().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r8(claseTemp.getAll_depre_anual_act_r8().add(rep.getDepre_anual_act()));
						
					}break;
					case 9:{
						claseTemp.setAll_adq_baja_r9(claseTemp.getAll_adq_baja_r9().add(rep.getAdq_baja()));
						claseTemp.setAll_costo_act_r9(claseTemp.getAll_costo_act_r9().add(rep.getCosto_act()));
						claseTemp.setAll_depre_anual_act_r9(claseTemp.getAll_depre_anual_act_r9().add(rep.getDepre_anual_act()));
						
					}break;
				}
					for(BajasTresBean btb: listTextos){
						if(btb.getClase_dsc().equalsIgnoreCase(claseTemp.getNombre()) && !claseTemp.getTextos().contains(btb)){
							claseTemp.getTextos().add(btb);	
						}
					}
				}
			}//end for
			
//			for(TGClaseTresBean claseItr: classList){		
//				logger.info("Clase: " + claseItr.toString());
//				
//			}
			datos.setDetClases(classList);
			datos.setTotReporteGeneral(totalGeneral);
			datos.setListReporteClase(listReporte);
			datos.setLsttextos(listTextos);
			
			
	
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
	return datos;
}




@Override
public InstAjusteBean consultaAjInstitucionalClase(InstAjusteBean datos) throws CfeException, SQLException {
	
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
		
					StringBuffer selectSQL = new StringBuffer("select texto_baja,region,region_dsc,  ");
					selectSQL.append("sum(NVL(adq_baja,0)) as adq_baja, sum(NVL(costo_act,0)) as costo_act, sum(NVL(depre_anual_act,0)) as depre_anual_act "); 
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja in (-1.00,-0.01) ");
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
					selectSQL.append("group by texto_baja,region,region_dsc ");
					selectSQL.append(" union ");
					selectSQL.append("select texto_baja,region,region_dsc,  ");
					selectSQL.append("0.0 as adq_baja, sum(NVL(costo_act,0)) as costo_act, sum(NVL(depre_anual_act,0)) as depre_anual_act "); 
					selectSQL.append("from v_bajas_nvo4 ");
					selectSQL.append(" where d_inv= '");
					selectSQL.append( datos.getdInv() +"' ");
					selectSQL.append( " AND area_val='");
					selectSQL.append( datos.getAreaVal()+"' ");
					selectSQL.append("AND adq_baja not in (-1.00,-0.01) ");
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
					selectSQL.append("group by texto_baja,region,region_dsc ");
					selectSQL.append("order by texto_baja,region,region_dsc ");

			logger.info(">>>Query AjusteIntitucionalResumen: " + selectSQL);
			
			TotalBajasTresBean totalGeneral = new TotalBajasTresBean();

			totalGeneral.setAdq_baja_r0(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r0(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r0(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r1(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r1(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r1(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r2(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r2(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r2(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r3(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r3(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r3(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r4(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r4(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r4(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r5(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r5(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r5(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r6(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r6(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r6(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r7(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r7(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r7(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r8(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r8(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r8(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_r9(BigDecimal.ZERO);
			totalGeneral.setCosto_act_r9(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_r9(BigDecimal.ZERO);
			
			totalGeneral.setAdq_baja_total(BigDecimal.ZERO);
			totalGeneral.setCosto_act_total(BigDecimal.ZERO);
			totalGeneral.setDepre_anual_act_total(BigDecimal.ZERO);
			
			BajasInstitucionalAjusteBean reporte = null;
			BajasTresBeanResumen txt = null;
			List<BajasTresBeanResumen> listTextos= new ArrayList<BajasTresBeanResumen>();
			List<BajasInstitucionalAjusteBean> listReporte = new ArrayList<BajasInstitucionalAjusteBean>();
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				reporte = new BajasInstitucionalAjusteBean();
				txt = new BajasTresBeanResumen();
				
//				reporte.setClase(rs.getString("clase_dsc"));
				reporte.setTexto_baja(rs.getString("texto_baja"));
				reporte.setRegion(rs.getString("region"));
				reporte.setRegion_dsc(rs.getString("region_dsc"));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
				
				txt.setTexto_baja(reporte.getTexto_baja());
//				txt.setClase(reporte.getClase());
				txt.setAdq_baja_r0(BigDecimal.ZERO);
				txt.setAdq_baja_r1(BigDecimal.ZERO);
				txt.setAdq_baja_r2(BigDecimal.ZERO);
				txt.setAdq_baja_r3(BigDecimal.ZERO);
				txt.setAdq_baja_r4(BigDecimal.ZERO);
				txt.setAdq_baja_r5(BigDecimal.ZERO);
				txt.setAdq_baja_r6(BigDecimal.ZERO);
				txt.setAdq_baja_r7(BigDecimal.ZERO);
				txt.setAdq_baja_r8(BigDecimal.ZERO);
				txt.setAdq_baja_r9(BigDecimal.ZERO);
				txt.setAdq_baja_total(BigDecimal.ZERO);
				txt.setCosto_act_r0(BigDecimal.ZERO);
				txt.setCosto_act_r1(BigDecimal.ZERO);
				txt.setCosto_act_r2(BigDecimal.ZERO);
				txt.setCosto_act_r3(BigDecimal.ZERO);
				txt.setCosto_act_r4(BigDecimal.ZERO);
				txt.setCosto_act_r5(BigDecimal.ZERO);
				txt.setCosto_act_r6(BigDecimal.ZERO);
				txt.setCosto_act_r7(BigDecimal.ZERO);
				txt.setCosto_act_r8(BigDecimal.ZERO);
				txt.setCosto_act_r9(BigDecimal.ZERO);
				txt.setCosto_act_total(BigDecimal.ZERO);
				txt.setDepre_anual_act_r0(BigDecimal.ZERO);
				txt.setDepre_anual_act_r1(BigDecimal.ZERO);
				txt.setDepre_anual_act_r2(BigDecimal.ZERO);
				txt.setDepre_anual_act_r3(BigDecimal.ZERO);
				txt.setDepre_anual_act_r4(BigDecimal.ZERO);
				txt.setDepre_anual_act_r5(BigDecimal.ZERO);
				txt.setDepre_anual_act_r6(BigDecimal.ZERO);
				txt.setDepre_anual_act_r7(BigDecimal.ZERO);
				txt.setDepre_anual_act_r8(BigDecimal.ZERO);
				txt.setDepre_anual_act_r9(BigDecimal.ZERO);
				txt.setDepre_anual_act_total(BigDecimal.ZERO);
				
				
				switch(Integer.parseInt(reporte.getRegion().replace("MR", ""))){
				case 0:{
					totalGeneral.setAdq_baja_r0(totalGeneral.getAdq_baja_r0().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r0(totalGeneral.getCosto_act_r0().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r0(totalGeneral.getDepre_anual_act_r0().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					
					
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r0(reporte.getAdq_baja());
						txt.setCosto_act_r0(reporte.getCosto_act());
						txt.setDepre_anual_act_r0(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r0(txt.getAdq_baja_r0().add(reporte.getAdq_baja()));
						txt.setCosto_act_r0(txt.getCosto_act_r0().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r0(txt.getDepre_anual_act_r0().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 1:{
					totalGeneral.setAdq_baja_r1(totalGeneral.getAdq_baja_r1().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r1(totalGeneral.getCosto_act_r1().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r1(totalGeneral.getDepre_anual_act_r1().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r1(reporte.getAdq_baja());
						txt.setCosto_act_r1(reporte.getCosto_act());
						txt.setDepre_anual_act_r1(reporte.getDepre_anual_act());
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r1(txt.getAdq_baja_r1().add(reporte.getAdq_baja()));
						txt.setCosto_act_r1(txt.getCosto_act_r1().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r1(txt.getDepre_anual_act_r1().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 2:{
					totalGeneral.setAdq_baja_r2(totalGeneral.getAdq_baja_r2().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r2(totalGeneral.getCosto_act_r2().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r2(totalGeneral.getDepre_anual_act_r2().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r2(reporte.getAdq_baja());
						txt.setCosto_act_r2(reporte.getCosto_act());
						txt.setDepre_anual_act_r2(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r2(txt.getAdq_baja_r2().add(reporte.getAdq_baja()));
						txt.setCosto_act_r2(txt.getCosto_act_r2().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r2(txt.getDepre_anual_act_r2().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					}
				}break;
				case 3:{
					totalGeneral.setAdq_baja_r3(totalGeneral.getAdq_baja_r3().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r3(totalGeneral.getCosto_act_r3().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r3(totalGeneral.getDepre_anual_act_r3().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r3(reporte.getAdq_baja());
						txt.setCosto_act_r3(reporte.getCosto_act());
						txt.setDepre_anual_act_r3(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r3(txt.getAdq_baja_r3().add(reporte.getAdq_baja()));
						txt.setCosto_act_r3(txt.getCosto_act_r3().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r3(txt.getDepre_anual_act_r3().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 4:{
					totalGeneral.setAdq_baja_r4(totalGeneral.getAdq_baja_r4().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r4(totalGeneral.getCosto_act_r4().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r4(totalGeneral.getDepre_anual_act_r4().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r4(reporte.getAdq_baja());
						txt.setCosto_act_r4(reporte.getCosto_act());
						txt.setDepre_anual_act_r4(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r4(txt.getAdq_baja_r4().add(reporte.getAdq_baja()));
						txt.setCosto_act_r4(txt.getCosto_act_r4().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r4(txt.getDepre_anual_act_r4().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 5:{
					totalGeneral.setAdq_baja_r5(totalGeneral.getAdq_baja_r5().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r5(totalGeneral.getCosto_act_r5().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r5(totalGeneral.getDepre_anual_act_r5().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r5(reporte.getAdq_baja());
						txt.setCosto_act_r5(reporte.getCosto_act());
						txt.setDepre_anual_act_r5(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r5(txt.getAdq_baja_r5().add(reporte.getAdq_baja()));
						txt.setCosto_act_r5(txt.getCosto_act_r5().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r5(txt.getDepre_anual_act_r5().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 6:{
					totalGeneral.setAdq_baja_r6(totalGeneral.getAdq_baja_r6().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r6(totalGeneral.getCosto_act_r6().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r6(totalGeneral.getDepre_anual_act_r6().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r6(reporte.getAdq_baja());
						txt.setCosto_act_r6(reporte.getCosto_act());
						txt.setDepre_anual_act_r6(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
						
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r6(txt.getAdq_baja_r6().add(reporte.getAdq_baja()));
						txt.setCosto_act_r6(txt.getCosto_act_r6().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r6(txt.getDepre_anual_act_r6().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 7:{
					totalGeneral.setAdq_baja_r7(totalGeneral.getAdq_baja_r7().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r7(totalGeneral.getCosto_act_r7().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r7(totalGeneral.getDepre_anual_act_r7().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r7(reporte.getAdq_baja());
						txt.setCosto_act_r7(reporte.getCosto_act());
						txt.setDepre_anual_act_r7(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r7(txt.getAdq_baja_r7().add(reporte.getAdq_baja()));
						txt.setCosto_act_r7(txt.getCosto_act_r7().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r7(txt.getDepre_anual_act_r7().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
				case 8:{
					totalGeneral.setAdq_baja_r8(totalGeneral.getAdq_baja_r8().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r8(totalGeneral.getCosto_act_r8().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r8(totalGeneral.getDepre_anual_act_r8().add(reporte.getDepre_anual_act()));
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r8(reporte.getAdq_baja());
						txt.setCosto_act_r8(reporte.getCosto_act());
						txt.setDepre_anual_act_r8(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r8(txt.getAdq_baja_r8().add(reporte.getAdq_baja()));
						txt.setCosto_act_r8(txt.getCosto_act_r8().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r8(txt.getDepre_anual_act_r8().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					}
				}break;
				case 9:{
					totalGeneral.setAdq_baja_r9(totalGeneral.getAdq_baja_r9().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_r9(totalGeneral.getCosto_act_r9().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_r9(reporte.getDepre_anual_act());
					totalGeneral.setAdq_baja_total(totalGeneral.getAdq_baja_total().add(reporte.getAdq_baja()));
					totalGeneral.setCosto_act_total(totalGeneral.getCosto_act_total().add(reporte.getCosto_act()));
					totalGeneral.setDepre_anual_act_total(totalGeneral.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
					if(!listTextos.contains(txt)){
						txt.setAdq_baja_r9(reporte.getAdq_baja());
						txt.setCosto_act_r9(reporte.getCosto_act());
						txt.setDepre_anual_act_r9(reporte.getDepre_anual_act());
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
						listTextos.add(txt);
					}else{
						txt = listTextos.get(listTextos.indexOf(txt));
						txt.setAdq_baja_r9(txt.getAdq_baja_r9().add(reporte.getAdq_baja()));
						txt.setCosto_act_r9(txt.getCosto_act_r9().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_r9(txt.getDepre_anual_act_r9().add(reporte.getDepre_anual_act()));
						
						txt.setAdq_baja_total(txt.getAdq_baja_total().add(reporte.getAdq_baja()));
						txt.setCosto_act_total(txt.getCosto_act_total().add(reporte.getCosto_act()));
						txt.setDepre_anual_act_total(txt.getDepre_anual_act_total().add(reporte.getDepre_anual_act()));
						
					}
				}break;
			}
				listReporte.add(reporte);
				
				
			}
			datos.setDetResumen(listTextos);
			datos.setTotResumen(totalGeneral);
			datos.setListReporteResumen(listReporte);
			datos.setLsttextosResumen(listTextos);

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
	return datos;
}
}