package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author VI5XXAA
 *
 */

public class GMontoTarifaBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2356715085729375023L;
	String tarifa;
	BigDecimal total;
	/**
	 * @return the tarifa
	 */
	public String getTarifa() {
		return tarifa;
	}
	/**
	 * @param tarifa the tarifa to set
	 */
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
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
	
	

	
	






	

	
	
	

	
	
	
}
