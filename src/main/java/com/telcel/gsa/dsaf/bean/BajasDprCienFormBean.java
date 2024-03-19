package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import java.util.List;

public class BajasDprCienFormBean implements java.io.Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 5443648750995788088L;
	String numeroActivo;
	Integer idMes;
	MesBean mes;
	Integer anio;
	Date fechaCarga;
	Integer idUsuario;
	Integer idSociedad;
	List<MesBean> meses;
	Boolean guardar;
	String msm;
	List<BajasDepreciadosCienBean> listBean;
	List<Integer> anios;
	private Boolean descarga;
	private boolean todos;
	private boolean delDisabled;
	BajasOpDocumentoBean opDocumentoBean;
	String nombreUsuariodDoc;
	String aPaternoUsuarioDoc;
	String aMaternoUsuarioDoc;
	String estatusDes;

	public Boolean getDescarga() {
		return descarga;
	}

	public void setDescarga(Boolean descarga) {
		this.descarga = descarga;
	}

	public List<Integer> getAnios() {
		return anios;
	}

	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}

	public Integer getIdMes() {
		return idMes;
	}

	public void setIdMes(Integer idMes) {
		this.idMes = idMes;
	}

	public List<BajasDepreciadosCienBean> getListBean() {
		return listBean;
	}

	public void setListBean(List<BajasDepreciadosCienBean> listBean) {
		this.listBean = listBean;
	}

	public String getMsm() {
		return msm;
	}

	public void setMsm(String msm) {
		this.msm = msm;
	}

	public Boolean getGuardar() {
		return guardar;
	}

	public void setGuardar(Boolean guardar) {
		this.guardar = guardar;
	}

	public String getNumeroActivo() {
		return numeroActivo;
	}

	public void setNumeroActivo(String numeroActivo) {
		this.numeroActivo = numeroActivo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Date getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdSociedad() {
		return idSociedad;
	}

	public void setIdSociedad(Integer idSociedad) {
		this.idSociedad = idSociedad;
	}

	public MesBean getMes() {
		return mes;
	}

	public void setMes(MesBean mes) {
		this.mes = mes;
	}

	public List<MesBean> getMeses() {
		return meses;
	}

	public void setMeses(List<MesBean> meses) {
		this.meses = meses;
	}

	public boolean isTodos() {
		return todos;
	}

	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	public boolean isDelDisabled() {
		return delDisabled;
	}

	public void setDelDisabled(boolean delDisabled) {
		this.delDisabled = delDisabled;
	}

	public BajasOpDocumentoBean getOpDocumentoBean() {
		return opDocumentoBean;
	}

	public void setOpDocumentoBean(BajasOpDocumentoBean opDocumentoBean) {
		this.opDocumentoBean = opDocumentoBean;
	}

	public String getNombreUsuariodDoc() {
		return nombreUsuariodDoc;
	}

	public void setNombreUsuariodDoc(String nombreUsuariodDoc) {
		this.nombreUsuariodDoc = nombreUsuariodDoc;
	}

	public String getaPaternoUsuarioDoc() {
		return aPaternoUsuarioDoc;
	}

	public void setaPaternoUsuarioDoc(String aPaternoUsuarioDoc) {
		this.aPaternoUsuarioDoc = aPaternoUsuarioDoc;
	}

	public String getaMaternoUsuarioDoc() {
		return aMaternoUsuarioDoc;
	}

	public void setaMaternoUsuarioDoc(String aMaternoUsuarioDoc) {
		this.aMaternoUsuarioDoc = aMaternoUsuarioDoc;
	}

	public String getEstatusDes() {
		return estatusDes;
	}

	public void setEstatusDes(String estatusDes) {
		this.estatusDes = estatusDes;
	}

}
