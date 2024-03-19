package com.telcel.gsa.dsaf.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoPorTipoBean;
import com.telcel.gsa.dsaf.bean.TGTipoAjusteBean;
import com.telcel.gsa.dsaf.bean.TotalBajasDosBean;
import com.telcel.gsa.dsaf.dao.ReporteResumenAjusteDao;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Repository
public class ReporteResumenAjusteDaoImpl extends AbstractDaoImpl<v_bajas_nvo3, Integer> implements ReporteResumenAjusteDao, Serializable{


	private static final long serialVersionUID = -2930176011149832726L;
	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(ReporteResumenAjusteDaoImpl.class);

	@Autowired
	transient DataSource dataSource;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	

	public ReporteResumenAjusteDaoImpl(){
		super(v_bajas_nvo3.class);
	}


@Override
public ReporteDetalladoPorTipoBean consultaResumenAjuste(ReporteDetalladoPorTipoBean datos) throws CfeException, SQLException {
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
					datos.setTextosTitulos("TEXTOS DE BAJA:" + filterTxt);
				}else if("FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA FISCALES:" + filterTxt);
				}else if("NO FISCALES".equalsIgnoreCase(datos.getTexto())){
					datos.setTextosTitulos("TEXTOS DE BAJA NO FISCALES:"+ filterTxt);
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
		
			StringBuffer selectSQL = new StringBuffer("select 1 as orden, 'ACTIVOS CON VALOR -1.00' as activo, clase, "
					+ " sum(adq_baja) adq_baja, sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act ");
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
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}
					selectSQL.append("group by 1, 2, clase ");
					
					selectSQL.append("union ");
					selectSQL.append("select 2 as orden, 'ACTIVOS CON VALOR -0.01' as activo, clase,  "
							+ " sum(adq_baja) adq_baja, sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act ");
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
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}
					selectSQL.append("group by 1, 2, clase ");
					
					selectSQL.append("union ");
					selectSQL.append("select 3 as orden, 'ACTIVOS 100% DEPRECIADOS' activo, clase, "
							+ "0.0 adq_baja, sum(costo_act) costo_act, sum(depre_anual_act) depre_anual_act");
					selectSQL.append(" from v_bajas_nvo4 ");
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
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}else if(("FISCALES".equalsIgnoreCase(datos.getTexto()) || "NO FISCALES".equalsIgnoreCase(datos.getTexto())) && (datos.getTxtDesc() == null || datos.getTxtDesc().size() == 0) && (datos.getTxtsDesc() != null || datos.getTxtsDesc().size() > 0)){
						selectSQL.append( " AND texto_baja in (");
						selectSQL.append( filterTxt+")");
					}
					selectSQL.append("group by 1, 2, clase ");
					
					selectSQL.append("order by 1,clase");
			logger.info(">>>Query DetalleAjusteTipo: " + selectSQL);
			
			preparedStatement = conn.prepareStatement(selectSQL.toString());
			ResultSet rs = preparedStatement.executeQuery();
			BajasDosBean reporte = null;
			
			
			datos.setListReporte(new ArrayList<BajasDosBean>());
			
			datos.setTotReporteGeneral(new TotalBajasDosBean());
			datos.getTotReporteGeneral().setAdq_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setAdq_ac_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setEjercicio_baja(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepr_tot(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_h(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setCosto_act(BigDecimal.ZERO);
			datos.getTotReporteGeneral().setDepre_anual_act(BigDecimal.ZERO);
			
			while (rs.next()) {
				reporte =  new BajasDosBean();
				reporte.setCvetipoAjuste(rs.getInt("orden"));
				reporte.setTipoAjuste(rs.getString("activo"));
				reporte.setClase(rs.getString("clase"));
				reporte.setAdq_baja(rs.getBigDecimal("adq_baja"));
				reporte.setCosto_act(rs.getBigDecimal("costo_act"));
				reporte.setDepre_anual_act(rs.getBigDecimal("depre_anual_act"));
				
			
				datos.getListReporte().add(reporte);
				datos.getTotReporteGeneral().setAdq_baja(datos.getTotReporteGeneral().getAdq_baja().add(rs.getBigDecimal("adq_baja")!=null?rs.getBigDecimal("adq_baja"): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setCosto_act(datos.getTotReporteGeneral().getCosto_act().add(rs.getBigDecimal("costo_act")!=null?rs.getBigDecimal("costo_act"): BigDecimal.ZERO));
				datos.getTotReporteGeneral().setDepre_anual_act(datos.getTotReporteGeneral().getDepre_anual_act().add(rs.getBigDecimal("depre_anual_act")!=null?rs.getBigDecimal("depre_anual_act"): BigDecimal.ZERO));
			}
			
			List<TGTipoAjusteBean> tipoList = new ArrayList <TGTipoAjusteBean>();
			tipoList.addAll(generaTree(datos.getListReporte()));
			datos.setListReporteDetalladoTip(new ArrayList<TGTipoAjusteBean>()); 
			datos.setListReporteDetalladoTip(tipoList);
	
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


public List<TGTipoAjusteBean> generaTree(List<BajasDosBean> listDatos){
	TGTipoAjusteBean tipoTemp = null;
	
	List<TGTipoAjusteBean> tipoList = new ArrayList<TGTipoAjusteBean>();
	TotalBajasDosBean totalTipo = null;
	
	

	for(BajasDosBean dta: listDatos){
		if(!tipoList.contains(new TGTipoAjusteBean(dta.getCvetipoAjuste()))){
			tipoTemp = new TGTipoAjusteBean();
			totalTipo = new TotalBajasDosBean(); 
			tipoTemp.setDescTipo(dta.getTipoAjuste());
			tipoTemp.setTipo(dta.getCvetipoAjuste());
			tipoTemp.setTextos(new ArrayList<BajasDosBean>());
			
			
			totalTipo.setAdq_baja(BigDecimal.ZERO);
			totalTipo.setCosto_act(BigDecimal.ZERO);
			totalTipo.setDepre_anual_act(BigDecimal.ZERO);
		
			totalTipo.setAdq_baja(totalTipo.getAdq_baja().add(dta.getAdq_baja()!=null?dta.getAdq_baja():BigDecimal.ZERO));
			totalTipo.setCosto_act(totalTipo.getCosto_act().add(dta.getCosto_act()!=null?dta.getCosto_act():BigDecimal.ZERO));
			totalTipo.setDepre_anual_act(totalTipo.getDepre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act():BigDecimal.ZERO));
			
			tipoTemp.getTextos().add(dta);
			tipoTemp.setTotalTipo(totalTipo);
			tipoList.add(tipoTemp);
			
		}else{
			tipoTemp = tipoList.get(tipoList.indexOf(tipoTemp));
			totalTipo.setAdq_baja(totalTipo.getAdq_baja().add(dta.getAdq_baja()!=null?dta.getAdq_baja():BigDecimal.ZERO));
			totalTipo.setCosto_act(totalTipo.getCosto_act().add(dta.getCosto_act()!=null?dta.getCosto_act():BigDecimal.ZERO));
			totalTipo.setDepre_anual_act(totalTipo.getDepre_anual_act().add(dta.getDepre_anual_act()!=null?dta.getDepre_anual_act():BigDecimal.ZERO));
			tipoTemp.getTextos().add(dta);
		}

	}
	
	
	return tipoList;
}



}