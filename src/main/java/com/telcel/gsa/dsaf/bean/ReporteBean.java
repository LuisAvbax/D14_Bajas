package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.math.BigDecimal;


public class ReporteBean implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -260411594276107704L;
	private String cve;
	private String descCve;
	private Integer perBaja;
	private BigDecimal deduccion;
	
	public String getCve() {
		return cve;
	}
	public void setCve(String cve) {
		this.cve = cve;
	}
	public String getDescCve() {
		return descCve;
	}
	public void setDescCve(String descCve) {
		this.descCve = descCve;
	}
	public Integer getPerBaja() {
		return perBaja;
	}
	public void setPerBaja(Integer perBaja) {
		this.perBaja = perBaja;
	}
	public BigDecimal getDeduccion() {
		return deduccion;
	}
	public void setDeduccion(BigDecimal deduccion) {
		this.deduccion = deduccion;
	}
	
	@Override
	public String toString() {
		return "ReporteBean [cve=" + cve + ", descCve=" + descCve + ", perBaja=" + perBaja + ", deduccion=" + deduccion
				+ "]";
	}
		

}
