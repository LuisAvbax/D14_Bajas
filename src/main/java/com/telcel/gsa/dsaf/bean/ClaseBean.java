package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.telcel.gsa.dsaf.entity.BajasClase;


/**
 * 
 * @author VI5XXAA
 *
 */
public class ClaseBean implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1439045103832407219L;
	private String clave;
	private String descripcion;
	private int id_usuario_alta;
	private Date fecha_alta;
	private int id_usuario_cambio;
	private Date fecha_cambio;
	private List<BajasClase> clases;
	private BajasClase claseSel;
	private Boolean guardar;
	private String msm;
	
	
	
	public String getMsm() {
		return msm;
	}
	public void setMsm(String msm) {
		this.msm = msm;
	}
	public int getId_usuario_alta() {
		return id_usuario_alta;
	}
	public void setId_usuario_alta(int id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public int getId_usuario_cambio() {
		return id_usuario_cambio;
	}
	public void setId_usuario_cambio(int id_usuario_cambio) {
		this.id_usuario_cambio = id_usuario_cambio;
	}
	public Date getFecha_cambio() {
		return fecha_cambio;
	}
	public void setFecha_cambio(Date fecha_cambio) {
		this.fecha_cambio = fecha_cambio;
	}
	public Boolean getGuardar() {
		return guardar;
	}
	public void setGuardar(Boolean guardar) {
		this.guardar = guardar;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<BajasClase> getClases() {
		return clases;
	}
	public void setClases(List<BajasClase> clases) {
		this.clases = clases;
	}
	public BajasClase getClaseSel() {
		return claseSel;
	}
	public void setClaseSel(BajasClase claseSel) {
		this.claseSel = claseSel;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null){
	    if(this.clave.equals((((ClaseBean) obj).getClave())) ) {
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
		result = prime * result + ((this.clave == null) ? 0 : this.clave.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "ClaseBean [clave=" + clave + ", descripcion=" + descripcion + ", id_usuario_alta=" + id_usuario_alta
				+ ", fecha_alta=" + fecha_alta + ", id_usuario_cambio=" + id_usuario_cambio + ", fecha_cambio="
				+ fecha_cambio + ", clases=" + clases + ", claseSel=" + claseSel + ", guardar=" + guardar + ", msm="
				+ msm + "]";
	}
	
	

	
}
