package com.telcel.gsa.dsaf.sercotel.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface AdquisicionBajaSercotelDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public  AdqBajasBean consultaClase(AdqBajasBean datos)throws CfeException, SQLException;
	public  AdqBajasBean consultaClaseAjustes(AdqBajasBean datos)throws CfeException, SQLException;
	public  AdqBajasBean consultaClaseNetos(AdqBajasBean datos)throws CfeException, SQLException;
	
	public  AdqBajasBean consultaTexto(AdqBajasBean datos)throws CfeException, SQLException;
	public  AdqBajasBean consultaTextoAjustes(AdqBajasBean datos)throws CfeException, SQLException;
	public  AdqBajasBean consultaTextoNetos(AdqBajasBean datos)throws CfeException, SQLException;
	
	public AdqBajasBean consultaRegion(AdqBajasBean datos) throws CfeException, SQLException;
	public AdqBajasBean consultaRegionAjustes(AdqBajasBean datos) throws CfeException, SQLException;
	public AdqBajasBean consultaRegionNetos(AdqBajasBean datos) throws CfeException, SQLException;

	
	
	
}
