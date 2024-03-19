package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
/**
 * 
 * @author VI5XXAA
 *
 */
public class ReemplazaOpBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1954095177828140913L;
	private String etiqueta;
	private String valor;

	
	
	
	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}




	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}




	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}




	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}




	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.valor.equalsIgnoreCase(((ReemplazaOpBean) obj).getValor())) {
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
		result = prime * result + ((etiqueta == null) ? 0 : etiqueta.hashCode());
		return result;
	}

}
