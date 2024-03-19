package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.telcel.gsa.dsaf.entity.BajasInpc;


/**
 * 
 * @author VI5XXAA
 *
 */
public class InpcBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -603529709155271117L;
	private Integer anio;
	private MesBean mes;
	private BigDecimal valor;
	private BajasInpc inpc;
	private List<BajasInpc> inpcs;
	List<MesBean> meses;
	List <Integer> anios;
	
	
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public MesBean getMes() {
		return mes;
	}
	public void setMes(MesBean mes) {
		this.mes = mes;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BajasInpc getInpc() {
		return inpc;
	}
	public void setInpc(BajasInpc inpc) {
		this.inpc = inpc;
	}
	public List<BajasInpc> getInpcs() {
		return inpcs;
	}
	public void setInpcs(List<BajasInpc> inpcs) {
		this.inpcs = inpcs;
	}
	public List<MesBean> getMeses() {
		return meses;
	}
	public void setMeses(List<MesBean> meses) {
		this.meses = meses;
	}
	public List<Integer> getAnios() {
		return anios;
	}
	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}
	

	
}
