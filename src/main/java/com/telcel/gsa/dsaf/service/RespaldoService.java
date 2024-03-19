package com.telcel.gsa.dsaf.service;
import java.io.Serializable;
import java.sql.SQLException;
import com.telcel.gsa.dsaf.bean.GeneraRespaldoBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
public interface RespaldoService {
	public void gestionaRespaldo(GeneraRespaldoBean respaldo, SessionScopeUser sessionData) throws SQLException;
}
