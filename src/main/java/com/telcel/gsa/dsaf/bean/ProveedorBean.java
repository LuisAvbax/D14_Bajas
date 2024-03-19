package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;

/**
 * 
 * @author VI5XXAA
 *
 */
public class ProveedorBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;
	private Integer codigoProveedor;
	private String claveProveedor;
	private String nombre;
	private RegionBean region;
	private String estatus;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object other){
		if(this.getCodigoProveedor() == 0) return false;
	    if (other == null) return false;
	    if (!(other instanceof ProveedorBean))return false;
	    ProveedorBean otherObj = (ProveedorBean)other;
	    if(this.getCodigoProveedor().equals(otherObj.getCodigoProveedor())){
	    	return true;
	    }
	    return false;
	}
	/**
	 * @return el codigoProveedor
	 */
	public Integer getCodigoProveedor() {
		return codigoProveedor;
	}
	/**
	 * @param codigoProveedor el codigoProveedor a establecer
	 */
	public void setCodigoProveedor(Integer codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}
	/**
	 * @return el nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre el nombre a establecer
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return el region
	 */
	public RegionBean getRegion() {
		return region;
	}
	/**
	 * @param region el region a establecer
	 */
	public void setRegion(RegionBean region) {
		this.region = region;
	}
	/**
	 * @return el estatus
	 */
	public String getEstatus() {
		return estatus;
	}
	/**
	 * @param estatus el estatus a establecer
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	/**
	 * @return el claveProveedor
	 */
	public String getClaveProveedor() {
		return claveProveedor;
	}
	/**
	 * @param claveProveedor el claveProveedor a establecer
	 */
	public void setClaveProveedor(String claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	


	
	
}
