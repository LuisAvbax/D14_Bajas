package com.telcel.gsa.dsaf.dipcel.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.ResumenConceptoClasBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface ResumenConceptosDipcelDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public ResumenConceptoRegBean consultaConceptoRegion(ResumenConceptoRegBean datos)throws CfeException, SQLException;
	public ResumenConceptoRegBean consultaAjConceptoRegion(ResumenConceptoRegBean datos) throws CfeException, SQLException;
	public ResumenConceptoRegBean consultaConceptoNetos(ResumenConceptoRegBean datos) throws CfeException, SQLException; 
	
	public ResumenConceptoClasBean consultaConceptoClase(ResumenConceptoClasBean datos)throws CfeException, SQLException;
	public ResumenConceptoClasBean consultaAjConceptoClase(ResumenConceptoClasBean datos) throws CfeException, SQLException;
	public ResumenConceptoClasBean consultaConceptoClasNetos(ResumenConceptoClasBean datos) throws CfeException, SQLException; 

	
}
