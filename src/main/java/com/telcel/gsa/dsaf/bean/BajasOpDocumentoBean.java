package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.io.InputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.Workbook;

public class BajasOpDocumentoBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7529430654320996864L;
	/**
	 * 
	 */

	private Integer id;
	private InputStream contenido;
	private String tipo;
	private String modulo;
	private String descripcion;
	private String nombre;
	private BajasCatEstatusDocBean estatus;
	private Date fecha_creacion;
	private BajasOpUsuarioBean usuario_creacion;
	private Integer estado_registro;
	private Workbook iContenido;
	private Integer idSociedad;
	private Integer anio;
	private Integer periodo;
	private String periodoSelect;
	private String periodoSelectIds;
	private Integer acumulado;
	private Integer calculo;
	private String region;
	private String clase;
	private String tipoTxt;
	private String txtfisc;
	private String tipoAjuste;
	private String txtsDesc;
	private boolean descarga;

	public BajasOpDocumentoBean() {
	}

	public BajasOpDocumentoBean(Integer id, String tipo, String modulo, String descripcion, String nombre,
			BajasCatEstatusDocBean estatus, Date fecha_creacion, BajasOpUsuarioBean usuario_creacion,
			Integer estado_registro) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.modulo = modulo;
		this.descripcion = descripcion;
		this.nombre = nombre;
		this.estatus = estatus;
		this.fecha_creacion = fecha_creacion;
		this.usuario_creacion = usuario_creacion;
		this.estado_registro = estado_registro;
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
	 * @return the contenido
	 */
	public InputStream getContenido() {
		return contenido;
	}

	/**
	 * @param contenido the contenido to set
	 */
	public void setContenido(InputStream contenido) {
		this.contenido = contenido;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the modulo
	 */
	public String getModulo() {
		return modulo;
	}

	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the fecha_creacion
	 */
	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	/**
	 * @param fecha_creacion the fecha_creacion to set
	 */
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	/**
	 * @return the estado_registro
	 */
	public Integer getEstado_registro() {
		return estado_registro;
	}

	/**
	 * @param estado_registro the estado_registro to set
	 */
	public void setEstado_registro(Integer estado_registro) {
		this.estado_registro = estado_registro;
	}

	/**
	 * @return the iContenido
	 */
	public Workbook getiContenido() {
		return iContenido;
	}

	/**
	 * @param iContenido the iContenido to set
	 */
	public void setiContenido(Workbook iContenido) {
		this.iContenido = iContenido;
	}

	/**
	 * @return the idSociedad
	 */
	public Integer getIdSociedad() {
		return idSociedad;
	}

	/**
	 * @param idSociedad the idSociedad to set
	 */
	public void setIdSociedad(Integer idSociedad) {
		this.idSociedad = idSociedad;
	}

	/**
	 * @return the anio
	 */
	public Integer getAnio() {
		return anio;
	}

	/**
	 * @param anio the anio to set
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	/**
	 * @return the periodo
	 */
	public Integer getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the acumulado
	 */
	public Integer getAcumulado() {
		return acumulado;
	}

	/**
	 * @param acumulado the acumulado to set
	 */
	public void setAcumulado(Integer acumulado) {
		this.acumulado = acumulado;
	}

	/**
	 * @return the calculo
	 */
	public Integer getCalculo() {
		return calculo;
	}

	/**
	 * @param calculo the calculo to set
	 */
	public void setCalculo(Integer calculo) {
		this.calculo = calculo;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the tipoTxt
	 */
	public String getTipoTxt() {
		return tipoTxt;
	}

	/**
	 * @param tipoTxt the tipoTxt to set
	 */
	public void setTipoTxt(String tipoTxt) {
		this.tipoTxt = tipoTxt;
	}

	/**
	 * @return the txtfisc
	 */
	public String getTxtfisc() {
		return txtfisc;
	}

	/**
	 * @param txtfisc the txtfisc to set
	 */
	public void setTxtfisc(String txtfisc) {
		this.txtfisc = txtfisc;
	}

	/**
	 * @return the tipoAjuste
	 */
	public String getTipoAjuste() {
		return tipoAjuste;
	}

	/**
	 * @param tipoAjuste the tipoAjuste to set
	 */
	public void setTipoAjuste(String tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}

	/**
	 * @return the periodoSelect
	 */
	public String getPeriodoSelect() {
		return periodoSelect;
	}

	/**
	 * @param periodoSelect the periodoSelect to set
	 */
	public void setPeriodoSelect(String periodoSelect) {
		this.periodoSelect = periodoSelect;
	}

	/**
	 * @return the estatus
	 */
	public BajasCatEstatusDocBean getEstatus() {
		return estatus;
	}

	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(BajasCatEstatusDocBean estatus) {
		this.estatus = estatus;
	}

	/**
	 * @return the usuario_creacion
	 */
	public BajasOpUsuarioBean getUsuario_creacion() {
		return usuario_creacion;
	}

	/**
	 * @param usuario_creacion the usuario_creacion to set
	 */
	public void setUsuario_creacion(BajasOpUsuarioBean usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}

	/**
	 * @return the txtsDesc
	 */
	public String getTxtsDesc() {
		return txtsDesc;
	}

	/**
	 * @param txtsDesc the txtsDesc to set
	 */
	public void setTxtsDesc(String txtsDesc) {
		this.txtsDesc = txtsDesc;
	}

	/**
	 * @return the periodoSelectIds
	 */
	public String getPeriodoSelectIds() {
		return periodoSelectIds;
	}

	/**
	 * @param periodoSelectIds the periodoSelectIds to set
	 */
	public void setPeriodoSelectIds(String periodoSelectIds) {
		this.periodoSelectIds = periodoSelectIds;
	}

	/**
	 * @return the descarga
	 */
	public boolean isDescarga() {
		return descarga;
	}

	/**
	 * @param descarga the descarga to set
	 */
	public void setDescarga(boolean descarga) {
		this.descarga = descarga;
	}
	
	

}
