package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.GeneraRespaldoBean;
import com.telcel.gsa.dsaf.entity.BajasOpRespaldo;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


public interface RespaldoDao extends AbstractDao<BajasOpRespaldo, Integer>, Serializable {
	
	public void ejecutaRespaldo(GeneraRespaldoBean respaldo, SessionScopeUser sessionData)throws CfeException ,SQLException;
	
}
