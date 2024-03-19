package com.telcel.gsa.dsaf.entity;
// Generated 31/03/2017 01:34:30 PM by Hibernate Tools 4.0.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.telcel.gsa.dsaf.bean.BajasDosBean;


@Entity
@Table(name = "cat_regiones")
public class BajasCatRegion implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5401218786428499674L;
	private Integer id;
	private String clave;
	private String descripcion;
	
	

	public BajasCatRegion() {
	}

	public BajasCatRegion(Integer id) {
		this.id = id;
	
	}

	public BajasCatRegion(Integer id, 
			String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	
	}
	@Column(name = "c_cve_region", length = 50)
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	@Id

	@Column(name = "c_id_region", unique = true, nullable = false, precision = 4, scale = 0)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name = "c_desc_region", length = 50)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.id == ((BajasCatRegion) obj).getId()) {
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
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "BajasCatRegion [id=" + id + ", clave=" + clave + ", descripcion=" + descripcion + "]";
	}

	
	

}
