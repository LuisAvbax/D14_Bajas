package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;


import com.telcel.gsa.dsaf.bean.ReporteDetalladoPorTipoBean;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface ReporteDetalladoAjusteTipoDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	

	public ReporteDetalladoPorTipoBean consultaReporteDetalladoTipo(ReporteDetalladoPorTipoBean datos)throws CfeException, SQLException;


	
}
