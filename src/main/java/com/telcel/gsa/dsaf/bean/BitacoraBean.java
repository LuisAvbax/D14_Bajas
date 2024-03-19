package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.List;

public class BitacoraBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494721671671221935L;
	String  proceso;
	List<String> procesos;
	List<BajasBitacoraBean> listOpBitacora;
	BajasBitacoraBean opBitacora;
	private Boolean descarga;
	
	
	public Boolean getDescarga() {
		return descarga;
	}
	public void setDescarga(Boolean descarga) {
		this.descarga = descarga;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public List<String> getProcesos() {
		return procesos;
	}
	public void setProcesos(List<String> procesos) {
		this.procesos = procesos;
	}
	public List<BajasBitacoraBean> getListOpBitacora() {
		return listOpBitacora;
	}
	public void setListOpBitacora(List<BajasBitacoraBean> listOpBitacora) {
		this.listOpBitacora = listOpBitacora;
	}
	public BajasBitacoraBean getOpBitacora() {
		return opBitacora;
	}
	public void setOpBitacora(BajasBitacoraBean opBitacora) {
		this.opBitacora = opBitacora;
	}
	@Override
	public String toString() {
		return "BitacoraBean [procesos=" + procesos + ", listOpBitacora=" + listOpBitacora + ", opBitacora="
				+ opBitacora + "]";
	}
	
}
