package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class BajasInstitucionalAjusteBean  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1640602321956009159L;
	private String clase;
	private String texto_baja;
	private String region;
	private String region_dsc;
	private BigDecimal adq_baja;
	private BigDecimal costo_act;
	private BigDecimal depre_anual_act;
	
	
	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getTexto_baja() {
		return texto_baja;
	}

	public void setTexto_baja(String texto_baja) {
		this.texto_baja = texto_baja;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion_dsc() {
		return region_dsc;
	}

	public void setRegion_dsc(String region_dsc) {
		this.region_dsc = region_dsc;
	}

	public BigDecimal getAdq_baja() {
		return adq_baja;
	}

	public void setAdq_baja(BigDecimal adq_baja) {
		this.adq_baja = adq_baja;
	}

	public BigDecimal getCosto_act() {
		return costo_act;
	}

	public void setCosto_act(BigDecimal costo_act) {
		this.costo_act = costo_act;
	}

	public BigDecimal getDepre_anual_act() {
		return depre_anual_act;
	}

	public void setDepre_anual_act(BigDecimal depre_anual_act) {
		this.depre_anual_act = depre_anual_act;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.texto_baja.equalsIgnoreCase(((BajasInstitucionalAjusteBean) obj).getTexto_baja()) &&
	    		this.clase.equalsIgnoreCase(((BajasInstitucionalAjusteBean) obj).getClase())) {
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
		result = prime * result + ((this.texto_baja == null) ? 0 : this.texto_baja.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "BajasIntitucionalAjusteBean [clase=" + clase + ", texto_baja=" + texto_baja + ", region=" + region
				+ ", region_dsc=" + region_dsc + ", adq_baja=" + adq_baja + ", costo_act=" + costo_act
				+ ", depre_anual_act=" + depre_anual_act + "]";
	}
	
	
	
	
	
}
