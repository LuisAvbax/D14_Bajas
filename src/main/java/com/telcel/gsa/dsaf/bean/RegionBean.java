package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
/**
 * 
 * @author VI5XXAA
 *
 */
public class RegionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String claveRegion;
	private String region;
	
	/**
	 * @return el claveRegion
	 */
	public String getClaveRegion() {
		return claveRegion;
	}
	/**
	 * @param claveRegion el claveRegion a establecer
	 */
	public void setClaveRegion(String claveRegion) {
		this.claveRegion = claveRegion;
	}
	/**
	 * @return el region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region el region a establecer
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.claveRegion.equalsIgnoreCase(((RegionBean) obj).getClaveRegion())) {
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
		result = prime * result + ((claveRegion == null) ? 0 : claveRegion.hashCode());
		return result;
	}
	
	

}
