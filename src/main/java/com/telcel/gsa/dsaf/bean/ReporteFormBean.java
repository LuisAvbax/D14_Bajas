package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final


import java.util.List;


public class ReporteFormBean implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2690627708643368447L;
	List<ReporteBean> datReporte;
	String mes;
	Integer acumulado;
	Integer anio;
	Integer caulculo; 
	Integer region;
	String cveClase;
	String texto;
	String ajuste;
	Integer dInv;
	Integer areaVal;
	String meses;
	String tipTexto;
	
	
	
	public String getTipTexto() {
		return tipTexto;
	}
	public void setTipTexto(String tipTexto) {
		this.tipTexto = tipTexto;
	}
	public String getMeses() {
		return meses;
	}
	public void setMeses(String meses) {
		this.meses = meses;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Integer getdInv() {
		return dInv;
	}
	public void setdInv(Integer dInv) {
		this.dInv = dInv;
	}
	public Integer getAreaVal() {
		return areaVal;
	}
	public void setAreaVal(Integer areaVal) {
		this.areaVal = areaVal;
	}
	public List<ReporteBean> getDatReporte() {
		return datReporte;
	}
	public void setDatReporte(List<ReporteBean> datReporte) {
		this.datReporte = datReporte;
	}
	
	public Integer getAcumulado() {
		return acumulado;
	}
	public void setAcumulado(Integer acumulado) {
		this.acumulado = acumulado;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getCaulculo() {
		return caulculo;
	}
	public void setCaulculo(Integer caulculo) {
		this.caulculo = caulculo;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public String getCveClase() {
		return cveClase;
	}
	public void setCveClase(String cveClase) {
		this.cveClase = cveClase;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getAjuste() {
		return ajuste;
	}
	public void setAjuste(String ajuste) {
		this.ajuste = ajuste;
	}
	
	
	@Override
	public String toString() {
		return "ReporteFormBean [datReporte=" + datReporte + ", mes=" + mes + ", acumulado=" + acumulado + ", anio="
				+ anio + ", caulculo=" + caulculo + ", region=" + region + ", cveClase=" + cveClase + ", texto=" + texto
				+ ", ajuste=" + ajuste + "]";
	}
	
	
	
	
	
		

}
