package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "bajas_depreciados_cien")
public class BajasDepreciadosCien implements java.io.Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410413101392276305L;
	private String numeroActivo;
	private Integer mes;
	private Integer anio;
	private Date fechaCarga;
	private Integer idUsuario;
	private Integer idSociedad;
	private String subnumero;
	private Integer estadoRegistro;
	
	

	public BajasDepreciadosCien() {
	}

	public BajasDepreciadosCien(String numeroActivo, Integer mes, Integer anio,
			Date fechaCarga) {
		this.numeroActivo = numeroActivo;
		this.mes = mes;
		this.anio = anio;
		this.fechaCarga = fechaCarga;
	
	}

	public BajasDepreciadosCien(String numeroActivo, Integer mes, Integer anio,
			Date fechaCarga, Integer idUsuario, Integer idSociedad) {
		this.numeroActivo = numeroActivo;
		this.mes = mes;
		this.anio = anio;
		this.fechaCarga = fechaCarga;
		this.idUsuario = idUsuario;
		this.idSociedad = idSociedad;
	}
	
	
	@Id
	@Column(name = "numero_activo", nullable = false, length = 10)
	public String getNumeroActivo() {
		return numeroActivo;
	}

	public void setNumeroActivo(String numeroActivo) {
		this.numeroActivo = numeroActivo;
	}

	@Column(name = "mes", nullable = false, length = 50)
	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	@Column(name = "anio", nullable = false, length = 50)
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_carga", length = 7)
	public Date getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	@Column(name = "id_usuario", nullable = false, length = 50)
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	@Column(name = "id_sociedad", unique = false, nullable = false, precision = 3, scale = 0)
	public Integer getIdSociedad() {
		return idSociedad;
	}

	public void setIdSociedad(Integer idSociedad) {
		this.idSociedad = idSociedad;
	}
	
	@Column(name = "subnumero", nullable = false, length = 5)
	public String getSubnumero() {
		return subnumero;
	}

	public void setSubnumero(String subnumero) {
		this.subnumero = subnumero;
	}
	@Column(name = "estado_registro", unique = false, nullable = false, precision = 1, scale = 0)
	public Integer getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(Integer estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	

}
