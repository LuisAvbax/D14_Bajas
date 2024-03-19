package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;

import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.entity.BajasOpRol;


public interface RolDao extends AbstractDao<BajasOpRol, Short>, Serializable {
	
	public boolean guardaRol(RolBean rol);
	
}
