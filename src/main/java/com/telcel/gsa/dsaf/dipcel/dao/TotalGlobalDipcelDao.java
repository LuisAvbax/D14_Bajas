package com.telcel.gsa.dsaf.dipcel.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.TotalGlobalClaBean;
import com.telcel.gsa.dsaf.bean.TotalGlobalRegBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface TotalGlobalDipcelDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public TotalGlobalRegBean consultaTotalGlobalRegion(TotalGlobalRegBean datos)throws CfeException, SQLException;
	public TotalGlobalRegBean consultaAjTotalGlobalRegion(TotalGlobalRegBean datos) throws CfeException, SQLException;
	public TotalGlobalRegBean consultaTGlobalRegNetos(TotalGlobalRegBean datos) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaTotalGlobalClase(TotalGlobalClaBean datos) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaTGlobalClaNetos(TotalGlobalClaBean datos) throws CfeException, SQLException;
	public TotalGlobalClaBean consultaAjTotalGlobalClase(TotalGlobalClaBean datos) throws CfeException, SQLException;
	
}
