package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class TGRegionBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5502734537936128012L;
	private String nombre;
	private List<TGClaseBean> clases;
	private List<BajasDosBean> textos;
	private BigDecimal all_adq_baja;
	private BigDecimal all_adq_ac_baja;
	private BigDecimal all_ejercicio_baja;
	private BigDecimal all_depr_tot;
	private BigDecimal all_costo_h;
	private BigDecimal all_inpcmp;
	private BigDecimal all_inpc;
	private BigDecimal all_factorAct;
	private BigDecimal all_costo_act;
	private BigDecimal all_depre_anual_act;
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<TGClaseBean> getClases() {
		return clases;
	}
	public void setClases(List<TGClaseBean> clases) {
		this.clases = clases;
	}
	public BigDecimal getAll_adq_baja() {
		return all_adq_baja;
	}
	public void setAll_adq_baja(BigDecimal all_adq_baja) {
		this.all_adq_baja = all_adq_baja;
	}
	public BigDecimal getAll_adq_ac_baja() {
		return all_adq_ac_baja;
	}
	public void setAll_adq_ac_baja(BigDecimal all_adq_ac_baja) {
		this.all_adq_ac_baja = all_adq_ac_baja;
	}
	public BigDecimal getAll_ejercicio_baja() {
		return all_ejercicio_baja;
	}
	public void setAll_ejercicio_baja(BigDecimal all_ejercicio_baja) {
		this.all_ejercicio_baja = all_ejercicio_baja;
	}
	public BigDecimal getAll_depr_tot() {
		return all_depr_tot;
	}
	public void setAll_depr_tot(BigDecimal all_depr_tot) {
		this.all_depr_tot = all_depr_tot;
	}
	public BigDecimal getAll_costo_h() {
		return all_costo_h;
	}
	public void setAll_costo_h(BigDecimal all_costo_h) {
		this.all_costo_h = all_costo_h;
	}
	public BigDecimal getAll_inpcmp() {
		return all_inpcmp;
	}
	public void setAll_inpcmp(BigDecimal all_inpcmp) {
		this.all_inpcmp = all_inpcmp;
	}
	public BigDecimal getAll_inpc() {
		return all_inpc;
	}
	public void setAll_inpc(BigDecimal all_inpc) {
		this.all_inpc = all_inpc;
	}
	public BigDecimal getAll_factorAct() {
		return all_factorAct;
	}
	public void setAll_factorAct(BigDecimal all_factorAct) {
		this.all_factorAct = all_factorAct;
	}
	public BigDecimal getAll_costo_act() {
		return all_costo_act;
	}
	public void setAll_costo_act(BigDecimal all_costo_act) {
		this.all_costo_act = all_costo_act;
	}
	public BigDecimal getAll_depre_anual_act() {
		return all_depre_anual_act;
	}
	public void setAll_depre_anual_act(BigDecimal all_depre_anual_act) {
		this.all_depre_anual_act = all_depre_anual_act;
	}
	
	public List<BajasDosBean> getTextos() {
		return textos;
	}
	public void setTextos(List<BajasDosBean> textos) {
		this.textos = textos;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.nombre.equalsIgnoreCase(((TGRegionBean) obj).getNombre())) {
	        return true;
	    }else {
	        return false;
	    }
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nombre == null) ? 0 : this.nombre.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return "TGRegionBean [nombre=" + nombre + ", clases=" + clases + ", all_adq_baja=" + all_adq_baja
				+ ", all_adq_ac_baja=" + all_adq_ac_baja + ", all_ejercicio_baja=" + all_ejercicio_baja
				+ ", all_depr_tot=" + all_depr_tot + ", all_costo_h=" + all_costo_h + ", all_inpcmp=" + all_inpcmp
				+ ", all_inpc=" + all_inpc + ", all_factorAct=" + all_factorAct + ", all_costo_act=" + all_costo_act
				+ ", all_depre_anual_act=" + all_depre_anual_act + "]";
	}
	
	
}
