package com.telcel.gsa.dsaf.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.util.CfeException;

public interface CienDepreciadosDao extends AbstractDao<BajasDepreciadosCien, Integer>, Serializable {

	public void guardaCienDpr(BajasDepreciadosCien bitacora) throws CfeException, SQLException;

	public List<BajasDepreciadosCienBean> consultaCienDpr(BajasDprCienFormBean datos, SessionScopeUser sessionData)
			throws CfeException;

	public BajasDepreciadosCienBean consultaCienDprValida(BajasDepreciadosCienBean datos) throws CfeException;

	public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes) throws CfeException;

	public void deleteAssets(List<BajasDepreciadosCienBean> assetList, SessionScopeUser sessionData);

	public void guardaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException;

	public void actualizaCienDprLst(List<BajasDepreciadosCien> dprLst) throws CfeException, SQLException;

	public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
			throws CfeException, SQLException;

	public List<BajasOpDocumentoBean> descargaArchivoBlob() throws CfeException, SQLException;

	public void insertaRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException;

	public void insertaSolicReporte(Integer anio, Integer periodo, Integer sociedad, Integer usuario, String docTipo,
			String docModulo, String docDescripcion, String docNombre) throws CfeException, SQLException;

	public BajasOpDocumentoBean obtenerReporteCienDepre(String docTipo, String docModulo, Integer idSociedad,
			Integer anio, Integer periodo, Integer usuarioCreacion) throws CfeException, SQLException;

	public void spBajasDoc(Integer procesoId, Integer usuarioId, Integer sociedadId) throws CfeException, SQLException;

	public Workbook obtieneReporte(Integer docId) throws CfeException, SQLException, IOException;

	public BajasOpDocumentoBean obtenerReporteCienDepreReciente(String docTipo, String docModulo, Integer idSociedad,
			Integer usuarioCreacion) throws CfeException, SQLException;

}
