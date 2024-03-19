package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.telcel.gsa.dsaf.bean.MesBean;



//@Entity
@Table(name = "t_cdd_parametro")
public class TcddParametro implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410413101392276305L;
	private String nombreParam;
	private String valor;
	
	

	public TcddParametro() {
	}

	

	public TcddParametro(String nombreParam, String valor) {
		super();
		this.nombreParam = nombreParam;
		this.valor = valor;
		
	
	}


	@Column(name = "nombre_param", unique = true, nullable = false, length = 30)
	
	public void setNombreParam(String nombreParam) {
		this.nombreParam = nombreParam;
	}

	public String getNombreParam() {
		return nombreParam;
	}


	@Column(name = "valor", nullable = false, length = 50)
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}


	
	
}
