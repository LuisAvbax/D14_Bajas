package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
import java.util.List;

public class EstructuraMenuBean implements Serializable {
	private static final long serialVersionUID = -1894345473733331654L;
	
	private AclBean aclPadre;
	private List<AclBean> aclHijos;
	private int sizeAclHijos=0;
	
	public EstructuraMenuBean() {}
	
	public EstructuraMenuBean(AclBean aclPadre) {
		this.aclPadre = aclPadre;
	}
	
	public AclBean getAclPadre() {
		return aclPadre;
	}
	public void setAclPadre(AclBean aclPadre) {
		this.aclPadre = aclPadre;
	}
	public List<AclBean> getAclHijos() {
		return aclHijos;
	}
	public void setAclHijos(List<AclBean> aclHijos) {
		this.aclHijos = aclHijos;
		sizeAclHijos=aclHijos.size();
	}

	public int getSizeAclHijos() {
		return sizeAclHijos;
	}


}
