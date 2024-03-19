package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface ReporteConcentradoDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public ReporteConcentradoBean consultaReporteConcentrado(ReporteConcentradoBean datos)throws CfeException, SQLException;
	public ReporteConcentradoBean consultaReporteConcentradoAjuste(ReporteConcentradoBean datos) throws CfeException, SQLException;
	public ReporteConcentradoBean consultaReporteConcentradoNetos(ReporteConcentradoBean datos) throws CfeException, SQLException; 

	
}
