package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class TGTipoAjusteBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5502734537936128012L;
	private Integer tipo;
	private String descTipo;
	private List<BajasDosBean> textos;
	private TotalBajasDosBean totalTipo;

	public TGTipoAjusteBean(){
		
	}
	
	public TGTipoAjusteBean(Integer tipo){
	this.tipo = tipo;	
	}

	public TotalBajasDosBean getTotalTipo() {
		return totalTipo;
	}

	public void setTotalTipo(TotalBajasDosBean totalTipo) {
		this.totalTipo = totalTipo;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public List<BajasDosBean> getTextos() {
		return textos;
	}

	public void setTextos(List<BajasDosBean> textos) {
		this.textos = textos;
	}
	
	
	
	public String getDescTipo() {
		return descTipo;
	}

	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.tipo.equals((((TGTipoAjusteBean) obj).getTipo())) ) {
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
		result = prime * result + ((this.tipo == null) ? 0 : this.tipo.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TGTipoAjusteBean [tipo=" + tipo + ", descTipo=" + descTipo + ", textos=" + textos + ", totalTipo="
				+ totalTipo + "]";
	}

	
	

	
}
