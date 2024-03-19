package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
/**
 * 
 * @author VI5XXAA
 *
 */
public class MesBean implements Serializable{

	private static final long serialVersionUID = 8441453171763616102L;
	
	private Integer id;
	private String nombre;
	
	public MesBean(){
		
	}
	
	public MesBean(Integer id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}
	
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.nombre.equalsIgnoreCase(((MesBean) obj).getNombre())) {
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
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	
	
	
			
	
	
}
