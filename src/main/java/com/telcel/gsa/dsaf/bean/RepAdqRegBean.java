package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.math.BigDecimal;


public class RepAdqRegBean implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7881967148346800852L;
	/**
	 * 
	 */
	
	private String region;
	private BigDecimal enero;
	private BigDecimal febrero;
	private BigDecimal marzo;
	private BigDecimal abril;
	private BigDecimal mayo;
	private BigDecimal junio;
	private BigDecimal julio;
	private BigDecimal agosto;
	private BigDecimal septiembre;
	private BigDecimal octubre;
	private BigDecimal noviembre;
	private BigDecimal diciembre;	
	private BigDecimal totalRegion;
	

	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public BigDecimal getEnero() {
		return enero;
	}
	public void setEnero(BigDecimal enero) {
		this.enero = enero;
	}
	public BigDecimal getFebrero() {
		return febrero;
	}
	public void setFebrero(BigDecimal febrero) {
		this.febrero = febrero;
	}
	public BigDecimal getMarzo() {
		return marzo;
	}
	public void setMarzo(BigDecimal marzo) {
		this.marzo = marzo;
	}
	public BigDecimal getAbril() {
		return abril;
	}

	public void setAbril(BigDecimal abril) {
		this.abril = abril;
	}
	public BigDecimal getMayo() {
		return mayo;
	}
	public void setMayo(BigDecimal mayo) {
		this.mayo = mayo;
	}
	public BigDecimal getJunio() {
		return junio;
	}
	public void setJunio(BigDecimal junio) {
		this.junio = junio;
	}
	public BigDecimal getJulio() {
		return julio;
	}
	public void setJulio(BigDecimal julio) {
		this.julio = julio;
	}

	public BigDecimal getAgosto() {
		return agosto;
	}
	public void setAgosto(BigDecimal agosto) {
		this.agosto = agosto;
	}
	public BigDecimal getSeptiembre() {
		return septiembre;
	}
	public void setSeptiembre(BigDecimal septiembre) {
		this.septiembre = septiembre;
	}
	public BigDecimal getOctubre() {
		return octubre;
	}
	public void setOctubre(BigDecimal octubre) {
		this.octubre = octubre;
	}
	public BigDecimal getNoviembre() {
		return noviembre;
	}
	public void setNoviembre(BigDecimal noviembre) {
		this.noviembre = noviembre;
	}
	public BigDecimal getDiciembre() {
		return diciembre;
	}
	public void setDiciembre(BigDecimal diciembre) {
		this.diciembre = diciembre;
	}
	
	public BigDecimal getTotalRegion() {
		return totalRegion;
	}
	public void setTotalRegion(BigDecimal totalRegion) {
		this.totalRegion = totalRegion;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.region.equalsIgnoreCase(((RepAdqRegBean) obj).getRegion())) {
	        return true;
	    }else {
	        return false;
	    }
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.region == null) ? 0 : this.region.hashCode());
		return result;
	}

	
	@Override
	public String toString() {
		return "RepAdqRegBean [region=" + region + ", enero=" + enero + ", febrero=" + febrero + ", marzo=" + marzo
				+ ", abril=" + abril + ", mayo=" + mayo + ", junio=" + junio + ", julio=" + julio + ", agosto=" + agosto
				+ ", septiembre=" + septiembre + ", octubre=" + octubre + ", noviembre=" + noviembre + ", diciembre="
				+ diciembre + "]";
	}



		

}
