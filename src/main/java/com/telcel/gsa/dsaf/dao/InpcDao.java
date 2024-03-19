package com.telcel.gsa.dsaf.dao;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.bean.InpcBean;
import com.telcel.gsa.dsaf.entity.BajasInpc;
import com.telcel.gsa.dsaf.util.CfeException;

public interface InpcDao extends AbstractDao<BajasInpc, Integer>, Serializable {
	
	public void insertaInpc(InpcBean inpc) throws CfeException;
	public void actualizaInpc(InpcBean inpc) throws CfeException;
	public void eliminaInpc(InpcBean inpc) throws CfeException;
	public List<BajasInpc> consultaInpc(InpcBean inpc) throws CfeException;
}
