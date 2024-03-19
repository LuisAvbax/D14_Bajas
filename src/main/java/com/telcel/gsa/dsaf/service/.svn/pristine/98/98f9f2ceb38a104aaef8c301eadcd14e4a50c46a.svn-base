package com.telcel.gsa.dsaf.service;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItemGroup;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.SociedadBean;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;




/**
 * 
 * @author VI5XXAA
 *
 */
public interface UtileriasCfeService extends Serializable{
	public Map<String, BajasCatParametrosBean> getParametros(List<String> params,SessionScopeUser sessionScopeUser);
	public Map<String, BajasCatParametrosBean> obtieneParametros(List<String> params, SessionScopeUser sessionScopeUser);
	public Map<String, CatRegionBean> obtenerRegiones(SessionScopeUser sessionScopeUser);
	public List<BajasCatRegion> obtenerRegionesWID(SessionScopeUser sessionScopeUser)throws CfeException;
	public List<CatRegionBean> obtenerLstRegiones(SessionScopeUser sessionScopeUser);
	public Date obtenerFechaActual(SessionScopeUser sessionScopeUser);
	public MesBean obtieneMesActual(SessionScopeUser sessionScopeUser);
	public Integer anioActual(SessionScopeUser sessionScopeUser);
	public void agregaEncabezado(HSSFRow row, List<String> titulos , Integer colNum);
	public void agregaFooter(HSSFRow row, List<BigDecimal> totales , Integer colNum, HSSFCellStyle style);
	public List<BajasCalculoBean> getCalculos(SessionScopeUser sessionScopeUser) throws CfeException;
	public List<BajasClaseBean> getClases(AdqBajasBean bajasRegBean,SessionScopeUser sessionScopeUser) throws CfeException;
	public List<BajasClaseBean> getClases(DepreActBean depreRegBean,SessionScopeUser sessionScopeUser) throws CfeException;
	public List<BajasClaseBean> getClasesGen(List<String> region, BajasCalculoBean calculo, SessionScopeUser sessionScopeUser) throws CfeException;
	public List<BajasClaseBean> getClasesCosto(CostoBean bajasRegBean, SessionScopeUser sessionScopeUser) throws CfeException;
	public List<String> getTxtGen(List<String> region, BajasCalculoBean calculo, List<String> clase, String texto, SessionScopeUser sessionScopeUser) throws CfeException;
	public List<String> getTxt(AdqBajasBean bajasRegBean, SessionScopeUser sessionScopeUser) throws CfeException;
	public List<String> getTxt(DepreActBean depreRegBean, SessionScopeUser sessionScopeUser) throws CfeException; 
	public List<String> getTxtCosto(CostoBean bajasRegBean, SessionScopeUser sessionScopeUser) throws CfeException;
	public List<MesBean> obtenerMeses(Map<String,BajasCatParametrosBean> parametros);
	public Map<Integer, MesBean> mapeaMeses(List<MesBean> meses);
	public SelectItemGroup getTxtGroup(List<String> txtFiscLst, String groupName);
	public List<BajasAjustesBean> getAjustes(SessionScopeUser sessionScopeUser) throws CfeException;
	public String[] getAcumMonths(String [] monthsSel);
	public Integer getmaxMonth(String [] monthsSel);
	public String txtperiodoConsulta(AdqBajasBean adqreg);
	public String txtperiodoConsulta(boolean isAcum, String[] mesesSelect, Integer anio);
	public String txtperiodoConsultaCosto(CostoBean adqreg);
	public SociedadBean strutureSoc(Integer id);
	public List<BajasBitacoraBean> validaCargaDepreciados(List<BajasDepreciadosCienBean> activos, Integer maxBit, SessionScopeUser sessionScopeUser);
	public Map<String, BajasCatParametrosBean> getParams(List<String> params);
	public Date obtenerFechaActual();
	

}
