package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author VI5XXAA
 *
 */
public class ConsumoDetalleBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;
	private Date fechaDocumento;
	private Date fechaContabilizacion;
	private String sociedad;
	private String moneda;
	private Long claveConta;
	private String claseDocumento;
	private BigDecimal debe;
	private BigDecimal dap;
	private String division;
	private String rpu;
	private String centroCosto;
	private String rSocial;
	private String nombreSitio;
	private String texto;
	private String textoCabecera;
	private String numAsignacion;
	private String textoPoliza;
	private String region;
	private Integer prorrateo;
	private BigDecimal importe;
	
	/**
	 * @return the rpu
	 */
	public String getRpu() {
		return rpu;
	}
	/**
	 * @param rpu the rpu to set
	 */
	public void setRpu(String rpu) {
		this.rpu = rpu;
	}
	/**
	 * @return the nombreSitio
	 */
	public String getNombreSitio() {
		return nombreSitio;
	}
	/**
	 * @param nombreSitio the nombreSitio to set
	 */
	public void setNombreSitio(String nombreSitio) {
		this.nombreSitio = nombreSitio;
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
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}
	
	/**
	 * @return the importe
	 */
	public BigDecimal getImporte() {
		return importe;
	}
	/**
	 * @param importe the importe to set
	 */
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	/**
	 * @return the fechaDocumento
	 */
	public Date getFechaDocumento() {
		return fechaDocumento;
	}
	/**
	 * @param fechaDocumento the fechaDocumento to set
	 */
	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}
	/**
	 * @return the fechaContabilizacion
	 */
	public Date getFechaContabilizacion() {
		return fechaContabilizacion;
	}
	/**
	 * @param fechaContabilizacion the fechaContabilizacion to set
	 */
	public void setFechaContabilizacion(Date fechaContabilizacion) {
		this.fechaContabilizacion = fechaContabilizacion;
	}
	/**
	 * @return the sociedad
	 */
	public String getSociedad() {
		return sociedad;
	}
	/**
	 * @param sociedad the sociedad to set
	 */
	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}
	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}
	/**
	 * @param moneda the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	/**
	 * @return the claveConta
	 */
	public Long getClaveConta() {
		return claveConta;
	}
	/**
	 * @param claveConta the claveConta to set
	 */
	public void setClaveConta(Long claveConta) {
		this.claveConta = claveConta;
	}
	/**
	 * @return the debe
	 */
	public BigDecimal getDebe() {
		return debe;
	}
	/**
	 * @param debe the debe to set
	 */
	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}
	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}
	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}
	/**
	 * @return the rSocial
	 */
	public String getrSocial() {
		return rSocial;
	}
	/**
	 * @param rSocial the rSocial to set
	 */
	public void setrSocial(String rSocial) {
		this.rSocial = rSocial;
	}
	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	/**
	 * @return the prorrateo
	 */
	public Integer getProrrateo() {
		return prorrateo;
	}
	/**
	 * @param prorrateo the prorrateo to set
	 */
	public void setProrrateo(Integer prorrateo) {
		this.prorrateo = prorrateo;
	}
	/**
	 * @return the claseDocumento
	 */
	public String getClaseDocumento() {
		return claseDocumento;
	}
	/**
	 * @param claseDocumento the claseDocumento to set
	 */
	public void setClaseDocumento(String claseDocumento) {
		this.claseDocumento = claseDocumento;
	}
	/**
	 * @return the textoCabecera
	 */
	public String getTextoCabecera() {
		return textoCabecera;
	}
	/**
	 * @param textoCabecera the textoCabecera to set
	 */
	public void setTextoCabecera(String textoCabecera) {
		this.textoCabecera = textoCabecera;
	}
	/**
	 * @return the numAsignacion
	 */
	public String getNumAsignacion() {
		return numAsignacion;
	}
	/**
	 * @param numAsignacion the numAsignacion to set
	 */
	public void setNumAsignacion(String numAsignacion) {
		this.numAsignacion = numAsignacion;
	}
	/**
	 * @return the textoPoliza
	 */
	public String getTextoPoliza() {
		return textoPoliza;
	}
	/**
	 * @param textoPoliza the textoPoliza to set
	 */
	public void setTextoPoliza(String textoPoliza) {
		this.textoPoliza = textoPoliza;
	}
	/**
	 * @return the dap
	 */
	public BigDecimal getDap() {
		return dap;
	}
	/**
	 * @param dap the dap to set
	 */
	public void setDap(BigDecimal dap) {
		this.dap = dap;
	}
	
	

	
		
	
			
	
	
}
