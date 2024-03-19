package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author VI5XXAA
 *
 */
public class ConsumoRegionBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	
	private String region;
	private BigDecimal energia;
	private BigDecimal dap;
	private BigDecimal otros;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private BigDecimal total;

	
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
	 * @return the energia
	 */
	public BigDecimal getEnergia() {
		return energia;
	}
	/**
	 * @param energia the energia to set
	 */
	public void setEnergia(BigDecimal energia) {
		this.energia = energia;
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
	/**
	 * @return the otros
	 */
	public BigDecimal getOtros() {
		return otros;
	}
	/**
	 * @param otros the otros to set
	 */
	public void setOtros(BigDecimal otros) {
		this.otros = otros;
	}
	/**
	 * @return the subtotal
	 */
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	/**
	 * @return the iva
	 */
	public BigDecimal getIva() {
		return iva;
	}
	/**
	 * @param iva the iva to set
	 */
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	
			
	
	
}
