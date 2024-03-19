package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author VI5XXAA
 *
 */
public class ParametroBean implements Serializable{

private static final long serialVersionUID = 673787856785091700L;
private Integer id;
private String nombre;
private String descripcion;
private String valor;
private Date fechaInicio;
private Date fechaFin;
private String unidad;
private Integer usuarioCreacion;
private Date fechaCreación;
private Integer usuarioModificacion;
private Date fechaModificacion;
private Integer estadoRegistro;

/**
 * @return el id
 */
public Integer getId() {
	return id;
}
/**
 * @param id el id a establecer
 */
public void setId(Integer id) {
	this.id = id;
}
/**
 * @return el nombre
 */
public String getNombre() {
	return nombre;
}
/**
 * @param nombre el nombre a establecer
 */
public void setNombre(String nombre) {
	this.nombre = nombre;
}
/**
 * @return el descripcion
 */
public String getDescripcion() {
	return descripcion;
}
/**
 * @param descripcion el descripcion a establecer
 */
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
/**
 * @return el valor
 */
public String getValor() {
	return valor;
}
/**
 * @param valor el valor a establecer
 */
public void setValor(String valor) {
	this.valor = valor;
}
/**
 * @return el fechaInicio
 */
public Date getFechaInicio() {
	return fechaInicio;
}
/**
 * @param fechaInicio el fechaInicio a establecer
 */
public void setFechaInicio(Date fechaInicio) {
	this.fechaInicio = fechaInicio;
}
/**
 * @return el fechaFin
 */
public Date getFechaFin() {
	return fechaFin;
}
/**
 * @param fechaFin el fechaFin a establecer
 */
public void setFechaFin(Date fechaFin) {
	this.fechaFin = fechaFin;
}
/**
 * @return el unidad
 */
public String getUnidad() {
	return unidad;
}
/**
 * @param unidad el unidad a establecer
 */
public void setUnidad(String unidad) {
	this.unidad = unidad;
}
/**
 * @return el usuarioCreacion
 */
public Integer getUsuarioCreacion() {
	return usuarioCreacion;
}
/**
 * @param usuarioCreacion el usuarioCreacion a establecer
 */
public void setUsuarioCreacion(Integer usuarioCreacion) {
	this.usuarioCreacion = usuarioCreacion;
}
/**
 * @return el fechaCreación
 */
public Date getFechaCreación() {
	return fechaCreación;
}
/**
 * @param fechaCreación el fechaCreación a establecer
 */
public void setFechaCreación(Date fechaCreación) {
	this.fechaCreación = fechaCreación;
}
/**
 * @return el usuarioModificacion
 */
public Integer getUsuarioModificacion() {
	return usuarioModificacion;
}
/**
 * @param usuarioModificacion el usuarioModificacion a establecer
 */
public void setUsuarioModificacion(Integer usuarioModificacion) {
	this.usuarioModificacion = usuarioModificacion;
}
/**
 * @return el fechaModificacion
 */
public Date getFechaModificacion() {
	return fechaModificacion;
}
/**
 * @param fechaModificacion el fechaModificacion a establecer
 */
public void setFechaModificacion(Date fechaModificacion) {
	this.fechaModificacion = fechaModificacion;
}
/**
 * @return el estadoRegistro
 */
public Integer getEstadoRegistro() {
	return estadoRegistro;
}
/**
 * @param estadoRegistro el estadoRegistro a establecer
 */
public void setEstadoRegistro(Integer estadoRegistro) {
	this.estadoRegistro = estadoRegistro;
}




}
