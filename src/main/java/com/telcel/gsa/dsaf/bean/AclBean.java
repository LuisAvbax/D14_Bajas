package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;

public class AclBean implements Serializable {
	private static final long serialVersionUID = -5133352430408880409L;
	
	private String nombreAcl;
	private String idAcl;  
	private String acl;
	private String action;
	private String idAclPadre;
	private String icono;
	private boolean seleccionado;
	private String orden;
	
	public String getNombreAcl() {
		return nombreAcl;
	}
	public void setNombreAcl(String nombreAcl) {
		this.nombreAcl = nombreAcl;
	}
	
	public String getIdAcl() {
		return idAcl;
	}
	public void setIdAcl(String idAcl) {
		this.idAcl = idAcl;
	}
	public String getAcl() {
		return acl;
	}
	public void setAcl(String acl) {
		this.acl = acl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIdAclPadre() {
		return idAclPadre;
	}
	public void setIdAclPadre(String idAclPadre) {
		this.idAclPadre = idAclPadre;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}
	public boolean isSeleccionado() {
		return seleccionado;
	}
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
}
