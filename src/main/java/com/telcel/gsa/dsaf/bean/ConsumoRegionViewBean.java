package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author VI5XXAA
 *
 */
public class ConsumoRegionViewBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	
	private String region;
	private BigDecimal energiaMasOtros;
	private BigDecimal ivaReal;
	private BigDecimal dap;
	private BigDecimal totalReal;
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
	 * @return the energiaMasOtros
	 */
	public BigDecimal getEnergiaMasOtros() {
		return energiaMasOtros;
	}
	/**
	 * @param energiaMasOtros the energiaMasOtros to set
	 */
	public void setEnergiaMasOtros(BigDecimal energiaMasOtros) {
		this.energiaMasOtros = energiaMasOtros;
	}
	/**
	 * @return the ivaReal
	 */
	public BigDecimal getIvaReal() {
		return ivaReal;
	}
	/**
	 * @param ivaReal the ivaReal to set
	 */
	public void setIvaReal(BigDecimal ivaReal) {
		this.ivaReal = ivaReal;
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
	 * @return the totalReal
	 */
	public BigDecimal getTotalReal() {
		return totalReal;
	}
	/**
	 * @param totalReal the totalReal to set
	 */
	public void setTotalReal(BigDecimal totalReal) {
		this.totalReal = totalReal;
	}

	
	
	
	
}
