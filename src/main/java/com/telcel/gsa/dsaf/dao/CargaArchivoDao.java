package com.telcel.gsa.dsaf.dao;


import java.io.Serializable;
import java.sql.SQLException;

import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.entity.BajasBitacora;
import com.telcel.gsa.dsaf.util.CfeException;


public interface CargaArchivoDao extends AbstractDao<BajasBitacora, Integer>, Serializable {
	
	public void executeSp(BajasDepreciadosCienBean carga)throws CfeException ,SQLException;
	
}
