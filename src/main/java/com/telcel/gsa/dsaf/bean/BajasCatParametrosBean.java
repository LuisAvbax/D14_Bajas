package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



public class BajasCatParametrosBean implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5981334184352054940L;
	private Integer id;
	private String nombre;
	private String descripcion;
	private String valor;
	private Date fechaInicio;
	private Date fechaFin;
	private String unidad;
	private Integer usuarioCreacion;
	private Date fechaCreacion;
	private Integer usuarioModificacion;
	private Date fechaModificacion;
	private boolean estadoRegistro;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public boolean isEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	
	@Override
	public String toString() {
		return "BajasCatParametrosBean [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", valor="
				+ valor + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", unidad=" + unidad
				+ ", usuarioCreacion=" + usuarioCreacion + ", fechaCreacion=" + fechaCreacion + ", usuarioModificacion="
				+ usuarioModificacion + ", fechaModificacion=" + fechaModificacion + ", estadoRegistro="
				+ estadoRegistro + "]";
	}

	

}
