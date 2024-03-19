package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;


/**
 * 
 * @author VI5XXAA
 *
 */
public interface CienDepreciadosService extends Serializable{
	
	public HSSFWorkbook generaDocumento(List<BajasDepreciadosCienBean> datos);
//	public void guardaCienDpr(List<BajasDepreciadosCien> datDpr) throws SQLException;
	public void guardaCienDpr(BajasDepreciadosCien datDpr, SessionScopeUser sessionData) throws SQLException;
	public List<BajasDepreciadosCienBean> consultaCienDpr (BajasDprCienFormBean datos, SessionScopeUser sessionData);
	public BajasDepreciadosCienBean consultaCienDprValida (BajasDepreciadosCienBean datos, SessionScopeUser sessionData);
	public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes, SessionScopeUser sessionData) throws CfeException;
	public List<BajasDepreciadosCienBean> deleteAssets(List<BajasDepreciadosCienBean> assetList, SessionScopeUser sessionData);
	public void guardaCienDprLst(List<BajasDepreciadosCien> datDprLst, SessionScopeUser sessionData) throws CfeException;
	public void actualizaCienDprLst(List<BajasDepreciadosCien> datDprLst, SessionScopeUser sessionData) throws CfeException;
}
