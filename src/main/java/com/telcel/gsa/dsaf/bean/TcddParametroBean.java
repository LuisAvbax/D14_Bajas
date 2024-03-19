package com.telcel.gsa.dsaf.bean;
// Generated 12/02/2020 05:14:21 PM by Hibernate Tools 3.5.0.Final

import java.util.Date;
import java.util.List;

import com.telcel.gsa.dsaf.entity.TcddParametro;


public class TcddParametroBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3966503234899332412L;
	/**
	 * 
	 */
	
	private String nombreParam;
	private String valor;
	private List<TcddParametro> datQry;
	private TcddParametro dato;
	
	public String getNombreParam() {
		return nombreParam;
	}
	public void setNombreParam(String nombreParam) {
		this.nombreParam = nombreParam;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public List<TcddParametro> getDatQry() {
		return datQry;
	}
	public void setDatQry(List<TcddParametro> datQry) {
		this.datQry = datQry;
	}
	
	public TcddParametro getDato() {
		return dato;
	}
	public void setDato(TcddParametro dato) {
		this.dato = dato;
	}
	@Override
	public String toString() {
		return "TcddParametroBean [nombreParam=" + nombreParam + ", valor=" + valor + ", datQry=" + datQry + "]";
	}
	

}
