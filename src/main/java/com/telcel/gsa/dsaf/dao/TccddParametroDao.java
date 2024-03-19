package com.telcel.gsa.dsaf.dao;



import java.io.Serializable;

import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.entity.TcddParametro;
import com.telcel.gsa.dsaf.util.CfeException;

public interface TccddParametroDao extends AbstractDao<TcddParametro, String>, Serializable {
	
	
	public void actualizaParametro(TcddParametroBean datos) throws CfeException;
	public TcddParametroBean consultaParametro(TcddParametroBean datos) throws CfeException;
}
