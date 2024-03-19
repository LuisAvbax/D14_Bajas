package com.telcel.gsa.dsaf.amov.dao;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.BajasBitacora;
import com.telcel.gsa.dsaf.util.CfeException;


public interface BitacoraAmovDao extends AbstractDao<BajasBitacora, Integer>, Serializable {
	
	public void guardaBitacora(List<BajasBitacoraBean> bitacora)throws CfeException ,SQLException;
	public List<BajasBitacoraBean> consultaBitacora(String tpoOperacion)throws CfeException;
	public void limpiaBitacora(String tpoOperacion) throws SQLException;
	public Long idMax();
	
}
