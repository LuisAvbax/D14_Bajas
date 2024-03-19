package com.telcel.gsa.dsaf.amov.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Workbook;

import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.util.CfeException;

public interface ReporteCienDepreciadosAmovDao extends AbstractDao<BajasDepreciadosCien, Integer>, Serializable {

	public void insertaSolicReporte(Integer anio, Integer periodo, Integer sociedad, Integer usuario, String docTipo,
			String docModulo, String docDescripcion, String docNombre) throws CfeException, SQLException;

	public BajasOpDocumentoBean obtenerReporteCienDepre(String docTipo, String docModulo, Integer idSociedad,
			Integer anio, Integer periodo, Integer usuarioCreacion) throws CfeException, SQLException;

	public void spBajasDoc(Integer procesoId, Integer usuarioId, Integer sociedadId) throws CfeException, SQLException;

	public Workbook obtieneReporte(Integer docId) throws CfeException, SQLException, IOException;

	public BajasOpDocumentoBean obtenerReporteCienDepreReciente(String docTipo, String docModulo, Integer idSociedad,
			Integer usuarioCreacion) throws CfeException, SQLException;
}
