package com.telcel.gsa.dsaf.amov.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.InstAjusteBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface InstitucionalAjustesAmovDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {

	public InstAjusteBean consultaInstitucionalAjustes(InstAjusteBean datos) throws CfeException, SQLException;
	public InstAjusteBean consultaAjInstitucionalClase(InstAjusteBean datos) throws CfeException, SQLException;
	
}
