package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "baja_cat_parametro")
public class BajasCatParametros implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410413101392276305L;
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

	public BajasCatParametros() {
	}

	public BajasCatParametros(Integer id, String nombre, String valor,
			Integer usuarioCreacion, Date fechaCreacion, boolean estadoRegistro) {
		this.id = id;
		this.nombre = nombre;
		this.valor = valor;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.estadoRegistro = estadoRegistro;
	}

	public BajasCatParametros(Integer id, String nombre, String descripcion,
			String valor, Date fechaInicio, Date fechaFin, String unidad,
			Integer usuarioCreacion, Date fechaCreacion, Integer usuarioModificacion,
			Date fechaModificacion, boolean estadoRegistro) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.valor = valor;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.unidad = unidad;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaModificacion = fechaModificacion;
		this.estadoRegistro = estadoRegistro;
	}

	@Id
	@Column(name = "PARAMETRO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PARAMETRO_NOMBRE", nullable = false, length = 50)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "PARAMETRO_DESCRIPCION", length = 150)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "PARAMETRO_VALOR", nullable = false, length = 50)
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PARAMETRO_FECHA_INICIO", length = 7)
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PARAMETRO_FECHA_FIN", length = 7)
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Column(name = "PARAMETRO_UNIDAD", length = 10)
	public String getUnidad() {
		return this.unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	@Column(name = "PARAMETRO_USUARIO_CREACION", nullable = false, precision = 10, scale = 0)
	public Integer getUsuarioCreacion() {
		return this.usuarioCreacion;
	}

	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PARAMETRO_FECHA_CREACION", nullable = false, length = 7)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "PARAMETRO_USUARIO_MODIFICACION", precision = 10, scale = 0)
	public Integer getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PARAMETRO_FECHA_MODIFICACION", length = 7)
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Column(name = "PARAMETRO_ESTADOREGISTRO", nullable = false, precision = 1, scale = 0)
	public boolean isEstadoRegistro() {
		return this.estadoRegistro;
	}

	public void setEstadoRegistro(boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

}
