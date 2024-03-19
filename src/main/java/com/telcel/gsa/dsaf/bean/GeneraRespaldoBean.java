package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.entity.BajasOpRespaldo;

public class GeneraRespaldoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String mensaje;
	int idSociedad;
	int idUsuario;
	Boolean disable;
	List<BajasOpRespaldo> listRespaldos;
	BajasOpRespaldo datosRespaldos;
	Map<String, BajasCatParametrosBean> sshParamMap;
	
	
	
	public List<BajasOpRespaldo> getListRespaldos() {
		return listRespaldos;
	}
	public void setListRespaldos(List<BajasOpRespaldo> listRespaldos) {
		this.listRespaldos = listRespaldos;
	}
	public BajasOpRespaldo getDatosRespaldos() {
		return datosRespaldos;
	}
	public void setDatosRespaldos(BajasOpRespaldo datosRespaldos) {
		this.datosRespaldos = datosRespaldos;
	}
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public int getIdSociedad() {
		return idSociedad;
	}
	public void setIdSociedad(int idSociedad) {
		this.idSociedad = idSociedad;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	
	
	

	/**
	 * @return the sshParamMap
	 */
	public Map<String, BajasCatParametrosBean> getSshParamMap() {
		return sshParamMap;
	}
	/**
	 * @param sshParamMap the sshParamMap to set
	 */
	public void setSshParamMap(Map<String, BajasCatParametrosBean> sshParamMap) {
		this.sshParamMap = sshParamMap;
	}
	@Override
	public String toString() {
		return "GeneraRespaldoBean [mensaje=" + mensaje + ", idSociedad=" + idSociedad + ", idUsuario=" + idUsuario
				+ ", disable=" + disable + "]";
	}
	
	
	
}
