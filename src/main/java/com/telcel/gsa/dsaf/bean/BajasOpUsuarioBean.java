package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpRol;


public class BajasOpUsuarioBean implements java.io.Serializable {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4534781980877170639L;
	private Integer id;
	private BajasOpRol bajasOpRol;
	private String bajasCatRegion;
	private BajasCatRegion bajasRegion;
	private String claveEmpleado;
	private String claveUniversal;
	private String nombre;
	private String apaterno;
	private String amaterno;
	private String correo;
	private Integer usuarioCreacion;
	private Date fechaCreacion;
	private Integer usuarioModificacion;
	private Date fechaModificacion;
	private boolean estadoRegistro;
	private Integer estatusBaja;
	private String direccion;
	private String subdireccion;
	private String gerencia;
	private String departamento;
	private String puesto;
	private BajasOpUsuarioBean usuarioSel;
	private List<BajasOpUsuarioBean> usuarios;
	
	
	
	public BajasOpUsuarioBean() {
	}

	public BajasOpUsuarioBean(String claveEmpleado) {
		this.claveEmpleado = claveEmpleado;
	}
	
	public BajasOpUsuarioBean(Integer id, String bajasCatRegion, String claveEmpleado,
			Integer usuarioCreacion, Date fechaCreacion, boolean estadoRegistro) {
		this.id = id;
		this.bajasCatRegion = bajasCatRegion;
		this.claveEmpleado = claveEmpleado;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.estadoRegistro = estadoRegistro;
	}

	public BajasOpUsuarioBean(Integer id, BajasOpRol bajasOpRol, String bajasCatRegion, String claveEmpleado,
			String claveUniversal, String nombre, String apaterno, String amaterno, String correo,
			Integer usuarioCreacion, Date fechaCreacion, Integer usuarioModificacion,
			Date fechaModificacion, boolean estadoRegistro, Integer estatusBaja,
			Set<BajasOpRol> bajasOpRolsForRolUsuarioModificacion,
			Set<BajasOpRol> bajaOpRolsForRolUsuarioCreacion) {
		
		this.id = id;
		this.bajasOpRol = bajasOpRol;
		this.bajasCatRegion =bajasCatRegion;
		this.claveEmpleado = claveEmpleado;
		this.claveUniversal = claveUniversal;
		this.nombre = nombre;
		this.apaterno = apaterno;
		this.amaterno = amaterno;
		this.correo = correo;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaModificacion = fechaModificacion;
		this.estadoRegistro = estadoRegistro;
		this.estatusBaja = estatusBaja;
		
		
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BajasOpRol getBajasOpRol() {
		return bajasOpRol;
	}

	public void setBajasOpRol(BajasOpRol bajasOpRol) {
		this.bajasOpRol = bajasOpRol;
	}

	public String getBajasCatRegion() {
		return bajasCatRegion;
	}
	public void setBajasCatRegion(String bajasCatRegion) {
		this.bajasCatRegion = bajasCatRegion;
	}
	public String getClaveEmpleado() {
		return claveEmpleado;
	}
	public void setClaveEmpleado(String claveEmpleado) {
		this.claveEmpleado = claveEmpleado;
	}
	public String getClaveUniversal() {
		return claveUniversal;
	}
	public void setClaveUniversal(String claveUniversal) {
		this.claveUniversal = claveUniversal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApaterno() {
		return apaterno;
	}
	public void setApaterno(String apaterno) {
		this.apaterno = apaterno;
	}
	public String getAmaterno() {
		return amaterno;
	}
	public void setAmaterno(String amaterno) {
		this.amaterno = amaterno;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
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
	public boolean getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public Integer getEstatusBaja() {
		return estatusBaja;
	}
	public void setEstatusBaja(Integer estatusBaja) {
		this.estatusBaja = estatusBaja;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getSubdireccion() {
		return subdireccion;
	}
	public void setSubdireccion(String subdireccion) {
		this.subdireccion = subdireccion;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
//	public BajasOpUsuarioBean getUsuarioSel() {
//		return usuarioSel;
//	}
//	public void setUsuarioSel(BajasOpUsuarioBean usuarioSel) {
//		this.usuarioSel = usuarioSel;
//	}
	public List<BajasOpUsuarioBean> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<BajasOpUsuarioBean> usuarios) {
		this.usuarios = usuarios;
	}

	public BajasCatRegion getBajasRegion() {
		return bajasRegion;
	}

	public void setBajasRegion(BajasCatRegion bajasRegion) {
		this.bajasRegion = bajasRegion;
	}

	public void setUsuarioSel(BajasOpUsuarioBean usuarioSel) {
		this.usuarioSel = usuarioSel;
	}

	@Transient
	public String getNombreCompleto(){
		return this.getNombre() + " " + this.getApaterno() + " " + this.getAmaterno();
	}

	@Transient
	public BajasOpUsuarioBean getUsuarioSel() {
		return usuarioSel;
	}
	
	
	

	
	
	
}
