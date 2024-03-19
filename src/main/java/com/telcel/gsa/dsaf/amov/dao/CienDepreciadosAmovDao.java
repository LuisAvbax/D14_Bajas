package com.telcel.gsa.dsaf.amov.dao;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.BajasBitacora;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


public interface CienDepreciadosAmovDao extends AbstractDao<BajasDepreciadosCien, Integer>, Serializable {
	
	public void guardaCienDpr(BajasDepreciadosCien bitacora)throws CfeException ,SQLException;
	public void actualizaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException;
	public List<BajasDepreciadosCienBean> consultaCienDpr(BajasDprCienFormBean datos, SessionScopeUser sessionData )throws CfeException;
	public BajasDepreciadosCienBean consultaCienDprValida(BajasDepreciadosCienBean datos )throws CfeException;
	public List<BajasDepreciadosCienBean> getDepreciados() throws CfeException;
	public void deleteAssets(List<BajasDepreciadosCienBean> assetList, SessionScopeUser sessionData);
	public void guardaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException;
	public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes) throws CfeException;
	public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
			throws CfeException, SQLException;
	public void insertaRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException;
}
