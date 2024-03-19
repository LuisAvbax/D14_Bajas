package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class TGAnioBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5502734537936128012L;
	private Integer anio;
	private List<BajasDosBean> textos;
	private TotalBajasDosBean totalAnio;

	public TGAnioBean(){
		
	}
	
	public TGAnioBean(Integer anio){
	this.anio = anio;	
	}


	public TotalBajasDosBean getTotalAnio() {
		return totalAnio;
	}

	public void setTotalAnio(TotalBajasDosBean totalAnio) {
		this.totalAnio = totalAnio;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public List<BajasDosBean> getTextos() {
		return textos;
	}

	public void setTextos(List<BajasDosBean> textos) {
		this.textos = textos;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.anio.equals((((TGAnioBean) obj).getAnio())) ) {
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
		result = prime * result + ((this.anio == null) ? 0 : this.anio.hashCode());
		return result;
	}
	

	@Override
	public String toString() {
		return "TGAnioBean [anio=" + anio + ", textos=" + textos + "]";
	}
	
}
