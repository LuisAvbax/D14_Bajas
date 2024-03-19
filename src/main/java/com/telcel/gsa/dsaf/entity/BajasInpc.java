package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.telcel.gsa.dsaf.bean.MesBean;



//@Entity
@Table(name = "inpc")
public class BajasInpc implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410413101392276305L;
	private Integer anio;
	private Integer mes;
	private BigDecimal indice;
	private MesBean mesTxt;
	

	public BajasInpc() {
	}

	

	public BajasInpc(Integer anio, Integer mes, BigDecimal indice) {
		super();
		this.anio = anio;
		this.mes = mes;
		this.indice = indice;
	
	}

	@Column(name = "ANIO", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getAnio() {
		return this.anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@Column(name = "MES", nullable = false, precision = 10, scale = 0)
	public Integer getMes() {
		return this.mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@Column(name = "INDICE", length = 150)
	public BigDecimal getIndice() {
		return this.indice;
	}

	public void setIndice(BigDecimal indice) {
		this.indice = indice;
	}
	@Transient
	public MesBean getMesTxt() {
		return mesTxt;
	}

	public void setMesTxt(MesBean mesTxt) {
		this.mesTxt = mesTxt;
	}

}
