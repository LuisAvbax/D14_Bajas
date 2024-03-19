package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.util.Date;


public class BajasCalculoBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1291371476316869282L;
	/**
	 * 
	 */
	
	private Integer id;
	private String clave;
	private String descripcion;
	private Date fechaCreacion;
	private Integer usuarioCreacion;
	private Date fechaModificacion;
	private Integer usuarioModificacion;
	private Integer estadoRegistro;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}	
	public Integer getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(Integer estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	
	
	


	
	
	

}
