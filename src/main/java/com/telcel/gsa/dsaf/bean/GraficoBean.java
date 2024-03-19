package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 * 
 * @author VI5XXAA
 *
 */

public class GraficoBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5163481706668930005L;

	
	BigDecimal montoTotal;
	BigDecimal montoTotalAnt;
	BigDecimal consTotal;
	BigDecimal consTotalAnt;
	List<GConsumoBean> consumoBean;
	List<GConsumoBean> consumoBeanAnt;
	List<GMontoTarifaBean> consumoTarifa;
	List<GMontoTarifaBean> sitioTarifa;
	
	Integer anio;
	List<Integer> anios;
	MesBean month;
	List<MesBean> months;
	CartesianChartModel model;
	PieChartModel piemodel;
	
	String activaTotalM;
	String activaTotalC;
	String tituloGrafico;
	
	
	
	/**
	 * @return the consumoBean
	 */
	public List<GConsumoBean> getConsumoBean() {
		return consumoBean;
	}
	/**
	 * @param consumoBean the consumoBean to set
	 */
	public void setConsumoBean(List<GConsumoBean> consumoBean) {
		this.consumoBean = consumoBean;
	}
	/**
	 * @return the consumoBeanAnt
	 */
	public List<GConsumoBean> getConsumoBeanAnt() {
		return consumoBeanAnt;
	}
	/**
	 * @param consumoBeanAnt the consumoBeanAnt to set
	 */
	public void setConsumoBeanAnt(List<GConsumoBean> consumoBeanAnt) {
		this.consumoBeanAnt = consumoBeanAnt;
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
	 * @return the month
	 */
	public MesBean getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(MesBean month) {
		this.month = month;
	}
	/**
	 * @return the model
	 */
	public CartesianChartModel getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(CartesianChartModel model) {
		this.model = model;
	}
	/**
	 * @return the months
	 */
	public List<MesBean> getMonths() {
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(List<MesBean> months) {
		this.months = months;
	}

	/**
	 * @return the montoTotal
	 */
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	/**
	 * @param montoTotal the montoTotal to set
	 */
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	/**
	 * @return the montoTotalAnt
	 */
	public BigDecimal getMontoTotalAnt() {
		return montoTotalAnt;
	}
	/**
	 * @param montoTotalAnt the montoTotalAnt to set
	 */
	public void setMontoTotalAnt(BigDecimal montoTotalAnt) {
		this.montoTotalAnt = montoTotalAnt;
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
	 * @return the consTotal
	 */
	public BigDecimal getConsTotal() {
		return consTotal;
	}
	/**
	 * @param consTotal the consTotal to set
	 */
	public void setConsTotal(BigDecimal consTotal) {
		this.consTotal = consTotal;
	}
	/**
	 * @return the consTotalAnt
	 */
	public BigDecimal getConsTotalAnt() {
		return consTotalAnt;
	}
	/**
	 * @param consTotalAnt the consTotalAnt to set
	 */
	public void setConsTotalAnt(BigDecimal consTotalAnt) {
		this.consTotalAnt = consTotalAnt;
	}
	/**
	 * @return the activaTotalM
	 */
	public String getActivaTotalM() {
		return activaTotalM;
	}
	/**
	 * @param activaTotalM the activaTotalM to set
	 */
	public void setActivaTotalM(String activaTotalM) {
		this.activaTotalM = activaTotalM;
	}
	/**
	 * @return the activaTotalC
	 */
	public String getActivaTotalC() {
		return activaTotalC;
	}
	/**
	 * @param activaTotalC the activaTotalC to set
	 */
	public void setActivaTotalC(String activaTotalC) {
		this.activaTotalC = activaTotalC;
	}
	
	/**
	 * @return the consumoTarifa
	 */
	public List<GMontoTarifaBean> getConsumoTarifa() {
		return consumoTarifa;
	}
	/**
	 * @param consumoTarifa the consumoTarifa to set
	 */
	public void setConsumoTarifa(List<GMontoTarifaBean> consumoTarifa) {
		this.consumoTarifa = consumoTarifa;
	}
	/**
	 * @return the sitioTarifa
	 */
	public List<GMontoTarifaBean> getSitioTarifa() {
		return sitioTarifa;
	}
	/**
	 * @param sitioTarifa the sitioTarifa to set
	 */
	public void setSitioTarifa(List<GMontoTarifaBean> sitioTarifa) {
		this.sitioTarifa = sitioTarifa;
	}
	/**
	 * @return the piemodel
	 */
	public PieChartModel getPiemodel() {
		return piemodel;
	}
	/**
	 * @param piemodel the piemodel to set
	 */
	public void setPiemodel(PieChartModel piemodel) {
		this.piemodel = piemodel;
	}
	/**
	 * @return the tituloGrafico
	 */
	public String getTituloGrafico() {
		return tituloGrafico;
	}
	/**
	 * @param tituloGrafico the tituloGrafico to set
	 */
	public void setTituloGrafico(String tituloGrafico) {
		this.tituloGrafico = tituloGrafico;
	}


	
	
	
	
	
}