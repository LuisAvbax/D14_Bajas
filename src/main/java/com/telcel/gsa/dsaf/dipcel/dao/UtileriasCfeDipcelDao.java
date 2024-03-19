package com.telcel.gsa.dsaf.dipcel.dao;

import java.io.Serializable;
/**
 * @author VI5XXAA
 */
import java.util.Date;
import java.util.List;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.util.CfeException;


public interface UtileriasCfeDipcelDao extends AbstractDao<BajasCatParametros,Integer>, Serializable{
	public List<BajasCatParametrosBean> obtenerParametros(List<String> params) throws CfeException;
	public BajasOpUsuarioBean obtieneUsuario(BajasOpUsuarioBean usuarioParam);
	public List<CatRegionBean> obtenerRegiones();
//	public  Map<String, CatRegionBean> obtenerRegiones();
	public Date obtenerFecha();
	public Integer existeUsuario(String clave);
	public List<RolBean> obtenerRoles();
	public BajasOpUsuarioBean obtenerRolUsuario(BajasOpUsuarioBean usuarioParam) throws Exception;
	public CatRegionBean obtieneRegionUsuario(CatRegionBean regionParam);
	public List<BajasOpUsuarioBean> obtenerUsuariosRegistrados(UsuarioBean usuarioParam) throws Exception ;
	public List<BajasOpUsuarioBean> obtenerUsuariosRegistradosProcesoBaja(Boolean estatus)throws Exception;
//	public CfeCatEstatusCorreo obtenerEstatusCorreo(String clave);
	public BajasOpUsuarioBean obtenerUsuarioRegistrado(BajasOpUsuarioBean usuarioParam);
	
	public List<BajasCatParametrosBean> getParams(List<String> params) throws CfeException;
//	void saveOrUpdate(BajasCatParametrosBean entity) throws CfeException;
//	void save(BajasCatParametrosBean entity) throws CfeException;
//	void update(BajasCatParametrosBean entity) throws CfeException;
	public List<BajasCalculoBean> getCalculos() throws CfeException;
	public List<String> getTxtGen(List<String> region, BajasCalculoBean calculo, List<String> clase, String texto) throws CfeException;
	public List<String> getTxt(AdqBajasBean bajasRegBean) throws CfeException;
	public List<String> getTxt(DepreActBean depreRegBean) throws CfeException;
	public List<String> getTxtCosto(CostoBean bajasRegBean) throws CfeException;	
	public List<BajasClaseBean> getClases(AdqBajasBean bajasRegBean) throws CfeException;
	public List<BajasClaseBean> getClases(DepreActBean depreRegBean) throws CfeException;
	public List<BajasClaseBean> getClasesCosto(CostoBean costoRegBean) throws CfeException;
	public List<BajasAjustesBean> getAjustes() throws CfeException;
	public String joinArray(Object[] arr,String prefix,String strJoin,String sufix);
	public String safeStringVal(String str,String def);
	public String safeStringVal(String str);
	public String getFilterText(String txtType, List<String> txtDesc, List<String> txtsDesc);
	public List<BajasClaseBean> getClasesGen(List<String> region, BajasCalculoBean calculo) throws CfeException;
	public MesBean perBajaToObject(Integer number);
	public StringBuilder buildInClause(List<String> possibleValue) throws CfeException;
	public TotalBean inicializaTotales (TotalBean datMesRep );
		
	
	
	
}
