package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author VI5XXAA
 *
 */
public class ConsumoFullBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;

	
	private List<MesBean> meses;
	private List<Integer> anios;
	private List<ConsumoFullBean> consumosReg;
	private MesBean mes;
	private Integer anio;
	private Boolean descarga;
	private List<String> informacion=  new ArrayList<String>();
	
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

	public List<ConsumoFullBean> getConsumosReg() {
		return consumosReg;
	}

	public void setConsumosReg(List<ConsumoFullBean> consumosReg) {
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
	public List<String> getInformacion() {
		return informacion;
	}
	public void setInformacion(List<String> informacion) {
		this.informacion = informacion;
	}
	

		
	
}
