package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;


import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface CostoActDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public  CostoBean consultaClase(CostoBean datos)throws CfeException, SQLException;
	public  CostoBean consultaClaseAjustes(CostoBean datos)throws CfeException, SQLException;
	public  CostoBean consultaClaseNetos(CostoBean datos)throws CfeException, SQLException;
	
	public  CostoBean consultaTexto(CostoBean datos)throws CfeException, SQLException;
	public  CostoBean consultaTextoAjustes(CostoBean datos)throws CfeException, SQLException;
	public  CostoBean consultaTextoNetos(CostoBean datos)throws CfeException, SQLException;
	
	public CostoBean consultaRegion(CostoBean datos) throws CfeException, SQLException;
	public CostoBean consultaRegionAjustes(CostoBean datos) throws CfeException, SQLException;
	public CostoBean consultaRegionNetos(CostoBean datos) throws CfeException, SQLException;

	
	
	
}
