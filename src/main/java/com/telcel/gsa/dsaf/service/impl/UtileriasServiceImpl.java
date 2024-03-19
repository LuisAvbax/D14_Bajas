package com.telcel.gsa.dsaf.service.impl;


import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasTresBean;
import com.telcel.gsa.dsaf.bean.BajasTresBeanResumen;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.RepAdqClasBean;
import com.telcel.gsa.dsaf.bean.RepAdqRegBean;
import com.telcel.gsa.dsaf.bean.RepAdqTextoBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.BajasCatParametros;

import com.telcel.gsa.dsaf.service.UtileriasService;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("utileriaService")
public class UtileriasServiceImpl implements UtileriasService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5086926505542897883L;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	private UtileriasCfeDao utileriasCfeDao;

	@Override
	public Map<String, BajasCatParametrosBean> obtieneParametros(List<String> params) {
		Map<String, BajasCatParametrosBean> parametrosMap = null;
		List<BajasCatParametrosBean> parametrosList = null;
		parametrosList = utileriasCfeDao.getParams(params);
		if(parametrosList != null && !parametrosList.isEmpty()){
			parametrosMap = new LinkedHashMap<String, BajasCatParametrosBean>();
			for(BajasCatParametrosBean p : parametrosList){
				parametrosMap.put(p.getNombre(), p);
			}
		}
		return parametrosMap;
	}
	
	
	@Override
	public String obtieneRuta(String fileName) {
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "com" +
				File.separator + "telcel" + File.separator + "gsa" + File.separator + "dsaf" + File.separator + "jasper" + File.separator);
		fullPath.append(fileName);
		
		return fullPath.toString();
	}


	@Override
	public AdqBajasBean listCeroAdq(AdqBajasBean datos, String tipoLista) {
		List<RepAdqRegBean> list = new ArrayList<RepAdqRegBean>();
		List<RepAdqClasBean> listClas = new ArrayList<RepAdqClasBean>();
		List<RepAdqTextoBean> listtxt = new ArrayList<RepAdqTextoBean>();
		
		
		if (tipoLista.equals("aqdReg"))
		{
			RepAdqRegBean datosList = new RepAdqRegBean();
			datosList.setRegion("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			list.add(datosList);
			datos.setListRepAdqReg(list);
		}
		if (tipoLista.equals("aqdClas"))
		{
			
			RepAdqClasBean datosList = new RepAdqClasBean();
			datosList.setClase("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listClas.add(datosList);
			datos.setListRepAdqClas(listClas);
		}
		if (tipoLista.equals("aqdtxt"))
		{
			
			RepAdqTextoBean datosList = new RepAdqTextoBean();
			datosList.setTexto("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listtxt.add(datosList);
			datos.setListRepAdqtxt(listtxt);
		}

		return datos;
	}



	public CostoBean listCeroCosto(CostoBean datos, String tipoLista) {
		List<RepAdqRegBean> list = new ArrayList<RepAdqRegBean>();
		List<RepAdqClasBean> listClas = new ArrayList<RepAdqClasBean>();
		List<RepAdqTextoBean> listtxt = new ArrayList<RepAdqTextoBean>();
		
		
		if (tipoLista.equals("aqdReg"))
		{
			RepAdqRegBean datosList = new RepAdqRegBean();
			datosList.setRegion("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			list.add(datosList);
			datos.setListRepAdqReg(list);
		}
		if (tipoLista.equals("aqdClas"))
		{
			
			RepAdqClasBean datosList = new RepAdqClasBean();
			datosList.setClase("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listClas.add(datosList);
			datos.setListRepAdqClas(listClas);
		}
		if (tipoLista.equals("aqdtxt"))
		{
			
			RepAdqTextoBean datosList = new RepAdqTextoBean();
			datosList.setTexto("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listtxt.add(datosList);
			datos.setListRepAdqtxt(listtxt);
		}

		return datos;
	}

	public DepreActBean listCeroDepre(DepreActBean datos, String tipoLista) {
		List<RepAdqRegBean> list = new ArrayList<RepAdqRegBean>();
		List<RepAdqClasBean> listClas = new ArrayList<RepAdqClasBean>();
		List<RepAdqTextoBean> listtxt = new ArrayList<RepAdqTextoBean>();
		
		
		if (tipoLista.equals("aqdReg"))
		{
			RepAdqRegBean datosList = new RepAdqRegBean();
			datosList.setRegion("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			list.add(datosList);
			datos.setListRepDepreReg(list);
		}
		if (tipoLista.equals("aqdClas"))
		{
			
			RepAdqClasBean datosList = new RepAdqClasBean();
			datosList.setClase("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listClas.add(datosList);
			datos.setListRepDepreClas(listClas);
		}
		if (tipoLista.equals("aqdtxt"))
		{
			
			RepAdqTextoBean datosList = new RepAdqTextoBean();
			datosList.setTexto("SIN@INFO");
			datosList.setEnero(BigDecimal.ZERO);
			datosList.setFebrero(BigDecimal.ZERO);
			datosList.setMarzo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setAbril(BigDecimal.ZERO);
			datosList.setMayo(BigDecimal.ZERO);
			datosList.setJunio(BigDecimal.ZERO);
			datosList.setJulio(BigDecimal.ZERO);
			datosList.setAgosto(BigDecimal.ZERO);
			datosList.setSeptiembre(BigDecimal.ZERO);
			datosList.setOctubre(BigDecimal.ZERO);
			datosList.setNoviembre(BigDecimal.ZERO);
			datosList.setDiciembre(BigDecimal.ZERO);
			listtxt.add(datosList);
			datos.setListRepDepretxt(listtxt);
		}

		return datos;
	}
	
	public List<BajasDosBean> listBajasDos(List<BajasDosBean> listdatos) {
		BajasDosBean datos = new BajasDosBean();
		datos.setPerbaja(1);
		datos.setRegion("SIN@INFO");
		datos.setRegion_dsc("");
		datos.setClase("");
		datos.setClase_dsc("");
		datos.setTexto_baja("");
		datos.setAdq_baja(BigDecimal.ZERO);
		datos.setAdq_ac_baja(BigDecimal.ZERO);
		datos.setEjercicio_baja(BigDecimal.ZERO);
		datos.setDepr_tot(BigDecimal.ZERO);
		datos.setCosto_h(BigDecimal.ZERO);
		datos.setInpcmp(BigDecimal.ZERO);
		datos.setInpc(BigDecimal.ZERO);
		datos.setFac_act(BigDecimal.ZERO);
		datos.setFactorAct(BigDecimal.ZERO);
		datos.setCosto_h(BigDecimal.ZERO);
		datos.setDepre_anual_act(BigDecimal.ZERO);
		datos.setDia(0);
		datos.setMes(0);
		datos.setMesStr("");
		datos.setAnio(0);
		datos.setFac_act(BigDecimal.ZERO);
		datos.setDenom("");
		datos.setNum_activo("");
		datos.setSub("");	
		listdatos.add(datos);
		return listdatos;
	}
	
	
	public List<BajasTresBean> listBajasTres(List<BajasTresBean> listdatos) {
		BajasTresBean datos = new BajasTresBean();
		datos.setAdq_baja_r0(BigDecimal.ZERO);
		datos.setAdq_baja_r1(BigDecimal.ZERO);
		datos.setAdq_baja_r2(BigDecimal.ZERO);
		datos.setAdq_baja_r3(BigDecimal.ZERO);
		datos.setAdq_baja_r4(BigDecimal.ZERO);
		datos.setAdq_baja_r5(BigDecimal.ZERO);
		datos.setAdq_baja_r6(BigDecimal.ZERO);
		datos.setAdq_baja_r7(BigDecimal.ZERO);
		datos.setAdq_baja_r8(BigDecimal.ZERO);
		datos.setAdq_baja_r9(BigDecimal.ZERO);
		datos.setAdq_baja_total(BigDecimal.ZERO);
		datos.setCosto_act_r0(BigDecimal.ZERO);
		datos.setCosto_act_r1(BigDecimal.ZERO);
		datos.setCosto_act_r2(BigDecimal.ZERO);
		datos.setCosto_act_r3(BigDecimal.ZERO);
		datos.setCosto_act_r4(BigDecimal.ZERO);
		datos.setCosto_act_r5(BigDecimal.ZERO);
		datos.setCosto_act_r6(BigDecimal.ZERO);
		datos.setCosto_act_r7(BigDecimal.ZERO);
		datos.setCosto_act_r8(BigDecimal.ZERO);
		datos.setCosto_act_r9(BigDecimal.ZERO);
		datos.setCosto_act_total(BigDecimal.ZERO);
		datos.setDepre_anual_act_r0(BigDecimal.ZERO);
		datos.setDepre_anual_act_r1(BigDecimal.ZERO);
		datos.setDepre_anual_act_r2(BigDecimal.ZERO);
		datos.setDepre_anual_act_r3(BigDecimal.ZERO);
		datos.setDepre_anual_act_r4(BigDecimal.ZERO);
		datos.setDepre_anual_act_r5(BigDecimal.ZERO);
		datos.setDepre_anual_act_r6(BigDecimal.ZERO);
		datos.setDepre_anual_act_r7(BigDecimal.ZERO);
		datos.setDepre_anual_act_r8(BigDecimal.ZERO);
		datos.setDepre_anual_act_r9(BigDecimal.ZERO);
		datos.setDepre_anual_act_total(BigDecimal.ZERO);
		datos.setTexto_baja("");
		datos.setClase("");
		datos.setClase_dsc("");	
		datos.setReference("SIN@INFO");
		listdatos.add(datos);
		return listdatos;
	}
	
	
	public List<BajasTresBeanResumen> listBajasTresRes(List<BajasTresBeanResumen> listdatos) {
		BajasTresBeanResumen datos = new BajasTresBeanResumen();
		datos.setAdq_baja_r0(BigDecimal.ZERO);
		datos.setAdq_baja_r1(BigDecimal.ZERO);
		datos.setAdq_baja_r2(BigDecimal.ZERO);
		datos.setAdq_baja_r3(BigDecimal.ZERO);
		datos.setAdq_baja_r4(BigDecimal.ZERO);
		datos.setAdq_baja_r5(BigDecimal.ZERO);
		datos.setAdq_baja_r6(BigDecimal.ZERO);
		datos.setAdq_baja_r7(BigDecimal.ZERO);
		datos.setAdq_baja_r8(BigDecimal.ZERO);
		datos.setAdq_baja_r9(BigDecimal.ZERO);
		datos.setAdq_baja_total(BigDecimal.ZERO);
		datos.setCosto_act_r0(BigDecimal.ZERO);
		datos.setCosto_act_r1(BigDecimal.ZERO);
		datos.setCosto_act_r2(BigDecimal.ZERO);
		datos.setCosto_act_r3(BigDecimal.ZERO);
		datos.setCosto_act_r4(BigDecimal.ZERO);
		datos.setCosto_act_r5(BigDecimal.ZERO);
		datos.setCosto_act_r6(BigDecimal.ZERO);
		datos.setCosto_act_r7(BigDecimal.ZERO);
		datos.setCosto_act_r8(BigDecimal.ZERO);
		datos.setCosto_act_r9(BigDecimal.ZERO);
		datos.setCosto_act_total(BigDecimal.ZERO);
		datos.setDepre_anual_act_r0(BigDecimal.ZERO);
		datos.setDepre_anual_act_r1(BigDecimal.ZERO);
		datos.setDepre_anual_act_r2(BigDecimal.ZERO);
		datos.setDepre_anual_act_r3(BigDecimal.ZERO);
		datos.setDepre_anual_act_r4(BigDecimal.ZERO);
		datos.setDepre_anual_act_r5(BigDecimal.ZERO);
		datos.setDepre_anual_act_r6(BigDecimal.ZERO);
		datos.setDepre_anual_act_r7(BigDecimal.ZERO);
		datos.setDepre_anual_act_r8(BigDecimal.ZERO);
		datos.setDepre_anual_act_r9(BigDecimal.ZERO);
		datos.setDepre_anual_act_total(BigDecimal.ZERO);
		datos.setTexto_baja("");
		datos.setReference("SIN@INFO");
		listdatos.add(datos);
		return listdatos;
	}
	
	public List<BajasDosBean> listBajasDosResumen(List<BajasDosBean> listdatos) {
		BajasDosBean datos = new BajasDosBean();
		datos.setPerbaja(1);
		datos.setRegion("");
		datos.setRegion_dsc("");
		datos.setClase("SIN@INFO");
		datos.setClase_dsc("");
		datos.setTexto_baja("");
		datos.setAdq_baja(BigDecimal.ZERO);
		datos.setAdq_ac_baja(BigDecimal.ZERO);
		datos.setEjercicio_baja(BigDecimal.ZERO);
		datos.setDepr_tot(BigDecimal.ZERO);
		datos.setCosto_h(BigDecimal.ZERO);
		datos.setInpcmp(BigDecimal.ZERO);
		datos.setInpc(BigDecimal.ZERO);
		datos.setFac_act(BigDecimal.ZERO);
		datos.setFactorAct(BigDecimal.ZERO);
		datos.setCosto_h(BigDecimal.ZERO);
		datos.setDepre_anual_act(BigDecimal.ZERO);
		datos.setDia(0);
		datos.setMes(0);
		datos.setMesStr("");
		datos.setAnio(0);
		datos.setFac_act(BigDecimal.ZERO);
		datos.setDenom("");
		datos.setNum_activo("");
		datos.setSub("");	
		listdatos.add(datos);
		return listdatos;
	}
	
}
