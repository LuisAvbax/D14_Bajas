package com.telcel.gsa.dsaf.dipcel.dao;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.InpcBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.BajasInpc;
import com.telcel.gsa.dsaf.util.CfeException;

public interface InpcDipcelDao extends AbstractDao<BajasInpc, Integer>, Serializable {
	
	public void insertaInpc(InpcBean inpc) throws CfeException;
	public void actualizaInpc(InpcBean inpc) throws CfeException;
	public void eliminaInpc(InpcBean inpc) throws CfeException;
	public List<BajasInpc> consultaInpc(InpcBean inpc) throws CfeException;
}
