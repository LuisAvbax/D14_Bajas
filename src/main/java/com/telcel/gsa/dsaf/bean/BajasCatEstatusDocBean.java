package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.io.InputStream;
import java.util.Date;


public class BajasCatEstatusDocBean implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4721208952136050274L;
	
	
	private	Integer id;
	private	InputStream clave;
	private	String descripcion;
	private	String fechaCreacion;
	private	String usuarioCreacion;
	private	String fechaModificacion;
	private	Integer usuarioModificacion;
	private	Date estadoRegistro;
	

	public BajasCatEstatusDocBean() {
	}



	public BajasCatEstatusDocBean(Integer id, InputStream clave, String descripcion, String fechaCreacion,
			String usuarioCreacion, String fechaModificacion, Integer usuarioModificacion, Date estadoRegistro) {
		super();
		this.id = id;
		this.clave = clave;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaModificacion = fechaModificacion;
		this.usuarioModificacion = usuarioModificacion;
		this.estadoRegistro = estadoRegistro;
	}



	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}



	/**
	 * @return the clave
	 */
	public InputStream getClave() {
		return clave;
	}



	/**
	 * @param clave the clave to set
	 */
	public void setClave(InputStream clave) {
		this.clave = clave;
	}



	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}



	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	/**
	 * @return the fechaCreacion
	 */
	public String getFechaCreacion() {
		return fechaCreacion;
	}



	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}



	/**
	 * @return the usuarioCreacion
	 */
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}



	/**
	 * @param usuarioCreacion the usuarioCreacion to set
	 */
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}



	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}



	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}



	/**
	 * @return the usuarioModificacion
	 */
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}



	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}



	/**
	 * @return the estadoRegistro
	 */
	public Date getEstadoRegistro() {
		return estadoRegistro;
	}



	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(Date estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	
}
