package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface DepreAnioActDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	
	public DepreActBean consultaDepreRegion(DepreActBean datos) throws CfeException, SQLException;
	public DepreActBean consultaDepreRegionAjustes(DepreActBean datos) throws CfeException, SQLException;
	public DepreActBean consultaDepreRegionNetos(DepreActBean datos) throws CfeException, SQLException;
	
	public  DepreActBean consultaDepreClase(DepreActBean datos)throws CfeException, SQLException;
	public  DepreActBean consultaDepreClaseAjustes(DepreActBean datos)throws CfeException, SQLException;
	public  DepreActBean consultaDepreClaseNetos(DepreActBean datos)throws CfeException, SQLException;
	
	public  DepreActBean consultaDepreTexto(DepreActBean datos)throws CfeException, SQLException;
	public  DepreActBean consultaDepreTextoAjustes(DepreActBean datos)throws CfeException, SQLException;
	public  DepreActBean consultaDepreTextoNetos(DepreActBean datos)throws CfeException, SQLException;

	
}
