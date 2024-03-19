package com.telcel.gsa.dsaf.bean;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import java.util.List;

import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;





public class BajasDepreciadosCienBean implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6720283064725203026L;
	 String numeroActivo;
	 String subnumero;
	 Integer rowNum;
	 Integer idMes;
	 MesBean mes;
	 Integer anio;
	 Date fechaCarga;
	 Integer idUsuario;
	 Integer idSociedad;
	 List<MesBean> meses;
	 Boolean guardar;
	 String msm;
	 List<BajasDepreciadosCien> listOK;
	 List<BajasDepreciadosCien> listUpdt;
	 List<BajasDepreciadosCien> listSave;
	 List<Integer> anios;
	 String aPaterno;
	 String aMaterno;
	 String nombre;
	 String descSociedad;
	 String descMes;
	 String nombreCompleto;
	 boolean cargacumulado;
	 Integer cargaAcumulados;
	 boolean selected;
	 Integer estadoRegistro;
	 
	 
	 
	
	public boolean getCargacumulado() {
		return cargacumulado;
	}

	public void setCargacumulado(boolean cargacumulado) {
		this.cargacumulado = cargacumulado;
	}

	public Integer getCargaAcumulados() {
		return cargaAcumulados;
	}

	public void setCargaAcumulados(Integer cargaAcumulados) {
		this.cargaAcumulados = cargaAcumulados;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDescMes() {
		return descMes;
	}

	public void setDescMes(String descMes) {
		this.descMes = descMes;
	}

	public String getaPaterno() {
		return aPaterno;
	}

	public void setaPaterno(String aPaterno) {
		this.aPaterno = aPaterno;
	}

	public String getaMaterno() {
		return aMaterno;
	}

	public void setaMaterno(String aMaterno) {
		this.aMaterno = aMaterno;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescSociedad() {
		return descSociedad;
	}

	public void setDescSociedad(String descSociedad) {
		this.descSociedad = descSociedad;
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

	public List<BajasDepreciadosCien> getListOK() {
		return listOK;
	}

	public void setListOK(List<BajasDepreciadosCien> listOK) {
		this.listOK = listOK;
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

	public String getSubnumero() {
		return subnumero;
	}

	public void setSubnumero(String subnumero) {
		this.subnumero = subnumero;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anio == null) ? 0 : anio.hashCode());
		result = prime * result + ((idMes == null) ? 0 : idMes.hashCode());
		result = prime * result + ((numeroActivo == null) ? 0 : numeroActivo.hashCode());
		result = prime * result + ((subnumero == null) ? 0 : subnumero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BajasDepreciadosCienBean other = (BajasDepreciadosCienBean) obj;
		if (anio == null)
				return false;
		if (idMes == null)
				return false;
		if (numeroActivo == null)
				return false;
		if (subnumero == null) {
				return false;
		}
		 if(this.anio.equals(other.getAnio()) 
				 && this.getIdMes().equals(other.getIdMes())
				 && this.getNumeroActivo().equals(other.getNumeroActivo()) 
				 && this.getSubnumero().equals(other.getSubnumero())) {
		        return true;
		    }else {
		    	return false;
		    }
	

		}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(Integer estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	public List<BajasDepreciadosCien> getListUpdt() {
		return listUpdt;
	}

	public void setListUpdt(List<BajasDepreciadosCien> listUpdt) {
		this.listUpdt = listUpdt;
	}

	public List<BajasDepreciadosCien> getListSave() {
		return listSave;
	}

	public void setListSave(List<BajasDepreciadosCien> listSave) {
		this.listSave = listSave;
	}
	

}
