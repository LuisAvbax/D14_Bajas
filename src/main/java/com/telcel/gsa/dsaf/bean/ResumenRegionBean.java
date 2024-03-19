package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 
 * @author VI5XXAA
 *
 */
public class ResumenRegionBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	
	private List<MesBean> meses;
	private List<Integer> anios;
	private List<ConsumoRegionBean> consumosReg;
	private List<ConsumoRegionViewBean> consumosRegCont;

	private MesBean mes;
	private Integer anio;
	private Boolean descarga;
	
	
	
	public Double getEnergiaTotal() {
        Double total = 0d;
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total += con.getEnergia().doubleValue();
	        }
        }
        return total;
    }
	
	public BigDecimal getDapTotal() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total = total.add(con.getDap());
	        }
        }
        return total;
    }
	
	public BigDecimal getOtrosTotal() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total = total.add(con.getOtros());
	        }
        }
        return total;
    }
	
	
	public BigDecimal getSubtotalTotal() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total = total.add(con.getSubtotal());
	        }
        }
        return total;
    }
	
	
	public BigDecimal getIvaTotal() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total = total.add(con.getIva());
	        }
        }
        return total;
    }
	
	
	public Double getEnMasOtTot() {
        Double total = 0d;
        if(getConsumosRegCont() != null && !getConsumosRegCont().isEmpty()){
	        for(ConsumoRegionViewBean con : getConsumosRegCont()) {
	            total += con.getEnergiaMasOtros().doubleValue();
	        }
        }
        return total;
    }
	
	public BigDecimal getIvaRealTot() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosRegCont() != null && !getConsumosRegCont().isEmpty()){
	        for(ConsumoRegionViewBean con : getConsumosRegCont()) {
	            total = total.add(con.getIvaReal());
	        }
        }
        return total;
    }
	
	public BigDecimal getDapCoTot() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosRegCont() != null && !getConsumosRegCont().isEmpty()){
	        for(ConsumoRegionViewBean con : getConsumosRegCont()) {
	            total = total.add(con.getDap());
	        }
        }
        return total;
    }
	
	
	public BigDecimal getTrTot() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosRegCont() != null && !getConsumosRegCont().isEmpty()){
	        for(ConsumoRegionViewBean con : getConsumosRegCont()) {
	            total = total.add(con.getTotalReal());
	        }
        }
        return total;
    }
	
	
	public BigDecimal getIvaTotCont() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosRegCont() != null && !getConsumosRegCont().isEmpty()){
	        for(ConsumoRegionViewBean con : getConsumosRegCont()) {
	            total = total.add(con.getIvaReal());
	        }
        }
        return total;
    }
	
	
	public BigDecimal getTotalTotal() {
        BigDecimal total = new BigDecimal(0);
        if(getConsumosReg() != null && !getConsumosReg().isEmpty()){
	        for(ConsumoRegionBean con : getConsumosReg()) {
	            total = total.add(con.getTotal());
	        }
        }
 
        return total;
    }
	
	
	/**
	 * @return the meses
	 */
	public List<MesBean> getMeses() {
		return meses;
	}
	/**
	 * @param meses the meses to set
	 */
	public void setMeses(List<MesBean> meses) {
		this.meses = meses;
	}
	/**
	 * @return the consumosReg
	 */
	public List<ConsumoRegionBean> getConsumosReg() {
		return consumosReg;
	}
	/**
	 * @param consumosReg the consumosReg to set
	 */
	public void setConsumosReg(List<ConsumoRegionBean> consumosReg) {
		this.consumosReg = consumosReg;
	}
	/**
	 * @return the mes
	 */
	public MesBean getMes() {
		return mes;
	}
	/**
	 * @param mes the mes to set
	 */
	public void setMes(MesBean mes) {
		this.mes = mes;
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
	 * @return the anios
	 */
	public List<Integer> getAnios() {
		return anios;
	}
	/**
	 * @param anios the anios to set
	 */
	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}

	/**
	 * @return the descarga
	 */
	public Boolean getDescarga() {
		return descarga;
	}

	/**
	 * @param descarga the descarga to set
	 */
	public void setDescarga(Boolean descarga) {
		this.descarga = descarga;
	}

	/**
	 * @return the consumosRegCont
	 */
	public List<ConsumoRegionViewBean> getConsumosRegCont() {
		return consumosRegCont;
	}

	/**
	 * @param consumosRegCont the consumosRegCont to set
	 */
	public void setConsumosRegCont(List<ConsumoRegionViewBean> consumosRegCont) {
		this.consumosRegCont = consumosRegCont;
	}

	

	
	

		
	
}
