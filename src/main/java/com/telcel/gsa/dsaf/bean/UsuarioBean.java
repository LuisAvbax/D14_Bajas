package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;

/**
 * 
 * @author VI5XXAA
 *
 */
public class UsuarioBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	private String filtroNumEmpleado;
	private String filtroApellidoPaterno;
	private String filtroApellidoMaterno;
	private String filtroNombre;
	private String filtroPerfil;
	private String numEmpleado;
	private String clave;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombre;
	private String direccion;
	private String subdireccion;
	private String gerencia;
	private String departamento;
	private String puesto;
	private Integer idEmpleado;
	private String claveUniversal;
	private Integer usuarioCreacion;
	private Date fechaCreacion;
	private Integer usuarioModificacion;
	private Date fechaModificacion;
	private Integer estadoRegistro;
	private RolBean rolBean;
	private List<BajasOpUsuarioBean> usuarios;
	private BajasOpUsuarioBean usuarioSel;
	private BajasOpUsuarioBean usuarioAdd;
	private List<BajasCatRegion>regiones;
	private CatRegionBean region;
	private List<RolBean> roles;
	
	

	
	
	/**
	 * @return el filtroNumEmpleado
	 */
	public String getFiltroNumEmpleado() {
		return filtroNumEmpleado;
	}
	/**
	 * @param filtroNumEmpleado el filtroNumEmpleado a establecer
	 */
	public void setFiltroNumEmpleado(String filtroNumEmpleado) {
		this.filtroNumEmpleado = filtroNumEmpleado;
	}
	/**
	 * @return el filtroApellidoPaterno
	 */
	public String getFiltroApellidoPaterno() {
		return filtroApellidoPaterno;
	}
	/**
	 * @param filtroApellidoPaterno el filtroApellidoPaterno a establecer
	 */
	public void setFiltroApellidoPaterno(String filtroApellidoPaterno) {
		this.filtroApellidoPaterno = filtroApellidoPaterno;
	}
	/**
	 * @return el filtroApellidoMaterno
	 */
	public String getFiltroApellidoMaterno() {
		return filtroApellidoMaterno;
	}
	/**
	 * @param filtroApellidoMaterno el filtroApellidoMaterno a establecer
	 */
	public void setFiltroApellidoMaterno(String filtroApellidoMaterno) {
		this.filtroApellidoMaterno = filtroApellidoMaterno;
	}
	/**
	 * @return el filtroNombre
	 */
	public String getFiltroNombre() {
		return filtroNombre;
	}
	/**
	 * @param filtroNombre el filtroNombre a establecer
	 */
	public void setFiltroNombre(String filtroNombre) {
		this.filtroNombre = filtroNombre;
	}
	/**
	 * @return el numEmpleado
	 */
	public String getNumEmpleado() {
		return numEmpleado;
	}
	/**
	 * @param numEmpleado el numEmpleado a establecer
	 */
	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}
	/**
	 * @return el apellidoPaterno
	 */
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	/**
	 * @param apellidoPaterno el apellidoPaterno a establecer
	 */
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	/**
	 * @return el apellidoMaterno
	 */
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	/**
	 * @param apellidoMaterno el apellidoMaterno a establecer
	 */
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
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
	 * @return el direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion el direccion a establecer
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return el subdireccion
	 */
	public String getSubdireccion() {
		return subdireccion;
	}
	/**
	 * @param subdireccion el subdireccion a establecer
	 */
	public void setSubdireccion(String subdireccion) {
		this.subdireccion = subdireccion;
	}
	/**
	 * @return el gerencia
	 */
	public String getGerencia() {
		return gerencia;
	}
	/**
	 * @param gerencia el gerencia a establecer
	 */
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	/**
	 * @return el departamento
	 */
	public String getDepartamento() {
		return departamento;
	}
	/**
	 * @param departamento el departamento a establecer
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	/**
	 * @return el puesto
	 */
	public String getPuesto() {
		return puesto;
	}
	/**
	 * @param puesto el puesto a establecer
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	
	public String getNombreCompleto(){
		return this.getNombre() + " " + this.getApellidoPaterno() + " " + this.getApellidoMaterno();
	}
	/**
	 * @return el clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave el clave a establecer
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return el idEmpleado
	 */
	public Integer getIdEmpleado() {
		return idEmpleado;
	}
	/**
	 * @param idEmpleado el idEmpleado a establecer
	 */
	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	/**
	 * @return el claveUniversal
	 */
	public String getClaveUniversal() {
		return claveUniversal;
	}
	/**
	 * @param claveUniversal el claveUniversal a establecer
	 */
	public void setClaveUniversal(String claveUniversal) {
		this.claveUniversal = claveUniversal;
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
	 * @return el fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	/**
	 * @param fechaCreacion el fechaCreacion a establecer
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	 * @return el filtroPerfil
	 */
	public String getFiltroPerfil() {
		return filtroPerfil;
	}
	/**
	 * @param filtroPerfil el filtroPerfil a establecer
	 */
	public void setFiltroPerfil(String filtroPerfil) {
		this.filtroPerfil = filtroPerfil;
	}

	public List<BajasOpUsuarioBean> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<BajasOpUsuarioBean> usuarios) {
		this.usuarios = usuarios;
	}
	public BajasOpUsuarioBean getUsuarioSel() {
		return usuarioSel;
	}
	public void setUsuarioSel(BajasOpUsuarioBean usuarioSel) {
		this.usuarioSel = usuarioSel;
	}
	public List<BajasCatRegion> getRegiones() {
		return regiones;
	}
	public void setRegiones(List<BajasCatRegion> regiones) {
		this.regiones = regiones;
	}
	/**
	 * @return the region
	 */
	public CatRegionBean getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(CatRegionBean region) {
		this.region = region;
	}
	public RolBean getRolBean() {
		return rolBean;
	}
	public void setRolBean(RolBean rolBean) {
		this.rolBean = rolBean;
	}
	public List<RolBean> getRoles() {
		return roles;
	}
	public void setRoles(List<RolBean> roles) {
		this.roles = roles;
	}
	public BajasOpUsuarioBean getUsuarioAdd() {
		return usuarioAdd;
	}
	public void setUsuarioAdd(BajasOpUsuarioBean usuarioAdd) {
		this.usuarioAdd = usuarioAdd;
	}
	
	
}
