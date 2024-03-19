package com.telcel.gsa.dsaf.amov.dao;



import java.io.Serializable;

import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.TcddParametro;
import com.telcel.gsa.dsaf.util.CfeException;

public interface TccddParametroAmovDao extends AbstractDao<TcddParametro, String>, Serializable {
	
	
	public void actualizaParametro(TcddParametroBean datos) throws CfeException;
	public TcddParametroBean consultaParametro(TcddParametroBean datos) throws CfeException;
}
