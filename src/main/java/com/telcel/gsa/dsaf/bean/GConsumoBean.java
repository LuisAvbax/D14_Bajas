package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author VI5XXAA
 *
 */

public class GConsumoBean  implements Serializable, Comparable<GConsumoBean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3845498565660489049L;

	Integer mes;
	BigDecimal total;
	/**
	 * @return the mes
	 */
	public Integer getMes() {
		return mes;
	}
	/**
	 * @param mes the mes to set
	 */
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	@Override
	public boolean equals(Object other){
		if(this.getMes() == null) return false;
	    if (other == null) return false;
	    if (!(other instanceof GConsumoBean))return false;
	    GConsumoBean otherObj = (GConsumoBean)other;
	    if(this.getMes().equals(otherObj.getMes())){
	    	return true;
	    }
	    return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
		return result;
	}
	
	
	 @Override
     public int compareTo(GConsumoBean o) {
     return this.mes.compareTo(o.mes);
    }





	

	
	
	

	
	
	
}
