package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.InstClaBean;
import com.telcel.gsa.dsaf.bean.InstRegBean;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface InstitucionalDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public InstRegBean consultaInstitucionalRegion(InstRegBean datos)throws CfeException, SQLException;
	public InstRegBean consultaAjInstitucionalRegion(InstRegBean datos) throws CfeException, SQLException;
	public InstRegBean consultaInstitucionalRegNetos(InstRegBean datos) throws CfeException, SQLException;
	public InstClaBean consultaInstitucionalClase(InstClaBean datos) throws CfeException, SQLException;
	public InstClaBean consultaInstitucionalClaNetos(InstClaBean datos) throws CfeException, SQLException;
	public InstClaBean consultaAjInstitucionalClase(InstClaBean datos) throws CfeException, SQLException;
	
}
