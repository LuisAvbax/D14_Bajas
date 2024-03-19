package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


public class BajasBitacoraBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2298259371142006742L;
	private long bitacoraId;
	private Integer rfcOpUsuarioByBitacoraUsuarioCreacion;
	private Integer rfcOpUsuarioByBitacoraUsuarioModificacion;
	private Long bitacoraControlId;
	private Long bitacoraLinea;
	private String bitacoraProceso;
	private Date bitacoraFechaCreacion;
	private Date bitacoraFechaModificacion;
	private boolean bitacoraEstadoRegistro;
	private String bitacoraDescripcion;
	private String nombreUsuario;
	
	
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public long getBitacoraId() {
		return bitacoraId;
	}
	public void setBitacoraId(long bitacoraId) {
		this.bitacoraId = bitacoraId;
	}
	public Integer getRfcOpUsuarioByBitacoraUsuarioCreacion() {
		return rfcOpUsuarioByBitacoraUsuarioCreacion;
	}
	public void setRfcOpUsuarioByBitacoraUsuarioCreacion(Integer rfcOpUsuarioByBitacoraUsuarioCreacion) {
		this.rfcOpUsuarioByBitacoraUsuarioCreacion = rfcOpUsuarioByBitacoraUsuarioCreacion;
	}
	public Integer getRfcOpUsuarioByBitacoraUsuarioModificacion() {
		return rfcOpUsuarioByBitacoraUsuarioModificacion;
	}
	public void setRfcOpUsuarioByBitacoraUsuarioModificacion(Integer rfcOpUsuarioByBitacoraUsuarioModificacion) {
		this.rfcOpUsuarioByBitacoraUsuarioModificacion = rfcOpUsuarioByBitacoraUsuarioModificacion;
	}
	public Long getBitacoraControlId() {
		return bitacoraControlId;
	}
	public void setBitacoraControlId(Long bitacoraControlId) {
		this.bitacoraControlId = bitacoraControlId;
	}
	public Long getBitacoraLinea() {
		return bitacoraLinea;
	}
	public void setBitacoraLinea(Long bitacoraLinea) {
		this.bitacoraLinea = bitacoraLinea;
	}
	public String getBitacoraProceso() {
		return bitacoraProceso;
	}
	public void setBitacoraProceso(String bitacoraProceso) {
		this.bitacoraProceso = bitacoraProceso;
	}
	public Date getBitacoraFechaCreacion() {
		return bitacoraFechaCreacion;
	}
	public void setBitacoraFechaCreacion(Date bitacoraFechaCreacion) {
		this.bitacoraFechaCreacion = bitacoraFechaCreacion;
	}
	public Date getBitacoraFechaModificacion() {
		return bitacoraFechaModificacion;
	}
	public void setBitacoraFechaModificacion(Date bitacoraFechaModificacion) {
		this.bitacoraFechaModificacion = bitacoraFechaModificacion;
	}
	public boolean isBitacoraEstadoRegistro() {
		return bitacoraEstadoRegistro;
	}
	public void setBitacoraEstadoRegistro(boolean bitacoraEstadoRegistro) {
		this.bitacoraEstadoRegistro = bitacoraEstadoRegistro;
	}
	public String getBitacoraDescripcion() {
		return bitacoraDescripcion;
	}
	public void setBitacoraDescripcion(String bitacoraDescripcion) {
		this.bitacoraDescripcion = bitacoraDescripcion;
	}


	
	
	

}
