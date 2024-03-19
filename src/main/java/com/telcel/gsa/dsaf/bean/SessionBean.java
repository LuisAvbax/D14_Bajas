package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;

/**
 * 
 * @author VI5XXAA
 *
 */
public class SessionBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	private UsuarioBean usuario;
	private List<RegionBean> regiones;
	// new
	private BajasOpUsuarioBean usuarioCfe;
	private Map<String, CatRegionBean> regionesMap;
	private Map<String, BajasCatParametrosBean> parametros;
	private List<MesBean> meses;
	private List<Integer> anios;
	
	/**
	 * @return el usuario
	 */
	public UsuarioBean getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario el usuario a establecer
	 */
	public void setUsuario(UsuarioBean usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return el regiones
	 */
	public List<RegionBean> getRegiones() {
		return regiones;
	}
	/**
	 * @param regiones el regiones a establecer
	 */
	public void setRegiones(List<RegionBean> regiones) {
		this.regiones = regiones;
	}
	/**
	 * @return the parametros
	 */
	public Map<String, BajasCatParametrosBean> getParametros() {
		return parametros;
	}
	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(Map<String, BajasCatParametrosBean> parametros) {
		this.parametros = parametros;
	}
	/**
	 * @return the regionesMap
	 */
	public Map<String, CatRegionBean> getRegionesMap() {
		return regionesMap;
	}
	/**
	 * @param regionesMap the regionesMap to set
	 */
	public void setRegionesMap(Map<String, CatRegionBean> regionesMap) {
		this.regionesMap = regionesMap;
	}
	
	/**
	 * @return the usuarioCfe
	 */
	public BajasOpUsuarioBean getUsuarioCfe() {
		return usuarioCfe;
	}
	/**
	 * @param usuarioCfe the usuarioCfe to set
	 */
	public void setUsuarioCfe(BajasOpUsuarioBean usuarioCfe) {
		this.usuarioCfe = usuarioCfe;
	}
	
	/**
	 * @return the meses
	 */
	public List<MesBean> getMeses() {
		return meses;
	}
	/**
	 * @param meses the meses to set
	 */
	public void setMeses(List<MesBean> meses) {
		this.meses = meses;
	}
	/**
	 * @return the anios
	 */
	public List<Integer> getAnios() {
		return anios;
	}
	/**
	 * @param anios the anios to set
	 */
	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}
	
	
	
	
	
}
