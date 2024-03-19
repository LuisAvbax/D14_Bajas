package com.telcel.gsa.dsaf.bean;

import java.math.BigDecimal;
import java.util.List;

public class TotalBajasDosBean implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6957427516261609536L;
	/**
	 * 
	 */
	private String descripcion;
	private BigDecimal adq_baja;
	private BigDecimal adq_ac_baja;
	private BigDecimal ejercicio_baja;
	private BigDecimal depr_tot;
	private BigDecimal costo_h;
	private BigDecimal costo_act;
	private BigDecimal inpcmp;
	private BigDecimal inpc;
	private BigDecimal factorAct;
	private BigDecimal depre_anual_act;
	private Integer per_baja;
	private Integer anio;
	
//	private List<BajasDosBean> textos;
	
//	public TotalBajasDosBean(){
//		
//	}
//	
//	public TotalBajasDosBean(Integer anio){
//	this.anio = anio;	
//	}
//	
	
//	public List<BajasDosBean> getTextos() {
//		return textos;
//	}
//
//	public void setTextos(List<BajasDosBean> textos) {
//		this.textos = textos;
//	}

	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getPer_baja() {
		return per_baja;
	}
	public void setPer_baja(Integer per_baja) {
		this.per_baja = per_baja;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getAdq_baja() {
		return adq_baja;
	}
	public void setAdq_baja(BigDecimal adq_baja) {
		this.adq_baja = adq_baja;
	}
	public BigDecimal getAdq_ac_baja() {
		return adq_ac_baja;
	}
	public void setAdq_ac_baja(BigDecimal adq_ac_baja) {
		this.adq_ac_baja = adq_ac_baja;
	}
	public BigDecimal getEjercicio_baja() {
		return ejercicio_baja;
	}
	public void setEjercicio_baja(BigDecimal ejercicio_baja) {
		this.ejercicio_baja = ejercicio_baja;
	}
	public BigDecimal getDepr_tot() {
		return depr_tot;
	}
	public void setDepr_tot(BigDecimal depr_tot) {
		this.depr_tot = depr_tot;
	}
	public BigDecimal getCosto_h() {
		return costo_h;
	}
	public void setCosto_h(BigDecimal costo_h) {
		this.costo_h = costo_h;
	}
	public BigDecimal getCosto_act() {
		return costo_act;
	}
	public void setCosto_act(BigDecimal costo_act) {
		this.costo_act = costo_act;
	}
	public BigDecimal getInpcmp() {
		return inpcmp;
	}
	public void setInpcmp(BigDecimal inpcmp) {
		this.inpcmp = inpcmp;
	}
	public BigDecimal getInpc() {
		return inpc;
	}
	public void setInpc(BigDecimal inpc) {
		this.inpc = inpc;
	}
	public BigDecimal getFactorAct() {
		return factorAct;
	}
	public void setFactorAct(BigDecimal factorAct) {
		this.factorAct = factorAct;
	}
	public BigDecimal getDepre_anual_act() {
		return depre_anual_act;
	}
	public void setDepre_anual_act(BigDecimal depre_anual_act) {
		this.depre_anual_act = depre_anual_act;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if(obj != null){
//		    if(this.anio == (((TotalBajasDosBean) obj).getAnio()))
//		    {
//		        return true;
//		    }else {
//		        return false;
//		    }
//		}else{
//			return false;
//		}
//	}
//	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((this.anio == null) ? 0 : this.anio.hashCode());
//		return result;
//	}
	
	@Override
	public String toString() {
		return "TotalBajasDosBean [descripcion=" + descripcion + ", adq_baja=" + adq_baja + ", adq_ac_baja="
				+ adq_ac_baja + ", ejercicio_baja=" + ejercicio_baja + ", depr_tot=" + depr_tot + ", costo_h=" + costo_h
				+ ", costo_act=" + costo_act + ", inpcmp=" + inpcmp + ", inpc=" + inpc + ", factorAct=" + factorAct
				+ ", depre_anual_act=" + depre_anual_act + "]";
	}
	
	
	
	
	
	
	

	
		

}
