package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;




//@Entity
@Table(name = "cat_clases")
public class BajasClase implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7529430654320996864L;
	/**
	 * 
	 */
	
	private String clave;
	private String descripcion;
	private int id_usuario_alta;
	private Date fecha_alta;
	private int id_usuario_cambio;
	private Date fecha_cambio;
	

	public BajasClase() {
	}



	public BajasClase(String clave, String descripcion, int id_usuario_alta, Date fecha_alta, int id_usuario_cambio, Date fecha_cambio) {
		super();
		this.clave = clave;
		this.descripcion = descripcion;
		this.id_usuario_alta= id_usuario_alta;
		this.id_usuario_cambio = id_usuario_cambio;
		this.fecha_alta = fecha_alta;
		this.fecha_cambio = fecha_cambio;
	}

	@Column(name = "c_cve_clase", unique = true, nullable = false, precision = 10, scale = 0)
	public String getClave() {
		return clave;
	}



	public void setClave(String clave) {
		this.clave = clave;
	}

	@Column(name = "c_desc_clase", nullable = false, precision = 10, scale = 0)
	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	@Column(name = "i_id_usuario_alta", nullable = true)
	public int getId_usuario_alta() {
		return id_usuario_alta;
	}



	public void setId_usuario_alta(int id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}


	@Column(name = "fecha_alta", nullable = true)
	public Date getFecha_alta() {
		return fecha_alta;
	}



	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}


	@Column(name = "i_id_usuario_cambio", nullable = true)
	public int getId_usuario_cambio() {
		return id_usuario_cambio;
	}



	public void setId_usuario_cambio(int id_usuario_cambio) {
		this.id_usuario_cambio = id_usuario_cambio;
	}


	@Column(name = "fecha_cambio", nullable = true)
	public Date getFecha_cambio() {
		return fecha_cambio;
	}



	public void setFecha_cambio(Date fecha_cambio) {
		this.fecha_cambio = fecha_cambio;
	}
	
	
}
