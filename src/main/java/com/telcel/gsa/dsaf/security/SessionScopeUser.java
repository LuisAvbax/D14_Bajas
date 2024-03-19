package com.telcel.gsa.dsaf.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.EstructuraMenuBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.SociedadBean;
import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;

@Service
@Qualifier(value="sessionScopeUser")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class SessionScopeUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3415159751154691398L;
	private BajasOpUsuarioBean usuarioCfe;
	private Map<String, CatRegionBean> regionesMap;
	private List<EstructuraMenuBean> menu;
	private Map<String, BajasCatParametrosBean> parametros;
	private SociedadBean sociedad;
	private String traceIp;
	private List<MesBean> meses;
	private List<Integer> anios;
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
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public SociedadBean getSociedad() {
		return sociedad;
	}
	public void setSociedad(SociedadBean sociedad) {
		this.sociedad = sociedad;
	}
	public String getTraceIp() {
		return traceIp;
	}
	public void setTraceIp(String traceIp) {
		this.traceIp = traceIp;
	}
	
}