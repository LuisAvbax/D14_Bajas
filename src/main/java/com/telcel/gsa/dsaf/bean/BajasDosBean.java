package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BajasDosBean  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1640602321956009159L;
	private Integer perbaja;
	private String region;
	private String region_dsc;
	private String clase;
	private String clase_dsc;
	private String texto_baja;
	private BigDecimal adq_baja;
	private BigDecimal adq_ac_baja;
	private BigDecimal ejercicio_baja;
	private BigDecimal depr_tot;
	private BigDecimal costo_h;
	private BigDecimal inpcmp;
	private BigDecimal inpc;
	private BigDecimal factorAct;
	private BigDecimal costo_act;
	private BigDecimal depre_anual_act;
	private MesBean perbajaObj;
	private Integer dia;
	private Integer mes;
	private String mesStr;
	private Integer anio;
	private BigDecimal fac_act;
	private String denom;
	private String num_activo;
	private String sub;
	
	//se ocupa en reportes 3
	private Integer cvetipoAjuste;
	private String tipoAjuste;
	private Date fechaCap;
	
	
	public String getNum_activo() {
		return num_activo;
	}
	public void setNum_activo(String num_activo) {
		this.num_activo = num_activo;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getDenom() {
		return denom;
	}
	public void setDenom(String denom) {
		this.denom = denom;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public BigDecimal getFac_act() {
		return fac_act;
	}
	public void setFac_act(BigDecimal fac_act) {
		this.fac_act = fac_act;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getPerbaja() {
		return perbaja;
	}
	public void setPerbaja(Integer perbaja) {
		this.perbaja = perbaja;
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
	public String getClase_dsc() {
		return clase_dsc;
	}
	public void setClase_dsc(String clase_dsc) {
		this.clase_dsc = clase_dsc;
	}
	public String getTexto_baja() {
		return texto_baja;
	}
	public void setTexto_baja(String texto_baja) {
		this.texto_baja = texto_baja;
	}
	public BigDecimal getAdq_baja() {
		return adq_baja;
	}
	public void setAdq_baja(BigDecimal adq_baja) {
		this.adq_baja = adq_baja;
	}
	public BigDecimal getAdq_ac_baja() {
		return adq_ac_baja;
	}
	public void setAdq_ac_baja(BigDecimal adq_ac_baja) {
		this.adq_ac_baja = adq_ac_baja;
	}
	public BigDecimal getEjercicio_baja() {
		return ejercicio_baja;
	}
	public void setEjercicio_baja(BigDecimal ejercicio_baja) {
		this.ejercicio_baja = ejercicio_baja;
	}
	public BigDecimal getDepr_tot() {
		return depr_tot;
	}
	public void setDepr_tot(BigDecimal depr_tot) {
		this.depr_tot = depr_tot;
	}
	public BigDecimal getCosto_h() {
		return costo_h;
	}
	public void setCosto_h(BigDecimal costo_h) {
		this.costo_h = costo_h;
	}
	public BigDecimal getInpcmp() {
		return inpcmp;
	}
	public void setInpcmp(BigDecimal inpcmp) {
		this.inpcmp = inpcmp;
	}
	public BigDecimal getInpc() {
		return inpc;
	}
	public void setInpc(BigDecimal inpc) {
		this.inpc = inpc;
	}
	public BigDecimal getFactorAct() {
		return factorAct;
	}
	public void setFactorAct(BigDecimal factorAct) {
		this.factorAct = factorAct;
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
	public MesBean getPerbajaObj() {
		return perbajaObj;
	}
	public void setPerbajaObj(MesBean perbajaObj) {
		this.perbajaObj = perbajaObj;
	}
	
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	
	public Integer getCvetipoAjuste() {
		return cvetipoAjuste;
	}
	public void setCvetipoAjuste(Integer cvetipoAjuste) {
		this.cvetipoAjuste = cvetipoAjuste;
	}
	public String getTipoAjuste() {
		return tipoAjuste;
	}
	public void setTipoAjuste(String tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.region.equalsIgnoreCase(((BajasDosBean) obj).getRegion()) &&
	    		this.clase_dsc.equalsIgnoreCase(((BajasDosBean) obj).getClase_dsc()) &&
	    		this.texto_baja.equalsIgnoreCase(((BajasDosBean) obj).getTexto_baja())) {
	        return true;
	    }else {
	        return false;
	    }
		}else{
			return false;
		}
	}
	
	public Date getFechaCap() {
		return fechaCap;
	}
	public void setFechaCap(Date fechaCap) {
		this.fechaCap = fechaCap;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.region == null) ? 0 : this.region.hashCode()) 
				+ ((this.clase_dsc == null) ? 0 : this.clase_dsc.hashCode())
				+ ((this.texto_baja == null) ? 0 : this.texto_baja.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return "BajasDosBean [perbaja=" + perbaja + ", region=" + region + ", region_dsc=" + region_dsc + ", clase_dsc="
				+ clase_dsc + ", texto_baja=" + texto_baja + ", adq_baja=" + adq_baja + ", adq_ac_baja=" + adq_ac_baja
				+ ", ejercicio_baja=" + ejercicio_baja + ", depr_tot=" + depr_tot + ", costo_h=" + costo_h + ", inpcmp="
				+ inpcmp + ", inpc=" + inpc + ", factorAct=" + factorAct + ", costo_act=" + costo_act
				+ ", depre_anual_act=" + depre_anual_act + "]";
	}
	public String getMesStr() {
		return mesStr;
	}
	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}
	
	
	
}
