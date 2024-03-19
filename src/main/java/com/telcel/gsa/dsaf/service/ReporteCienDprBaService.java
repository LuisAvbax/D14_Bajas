package com.telcel.gsa.dsaf.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.util.CfeException;

public interface ReporteCienDprBaService extends Serializable {

	public HSSFWorkbook generaDocumento(List<BajasDepreciadosCienBean> datos) throws IOException;

	public List<BajasOpDocumentoBean> consultaArchivo() throws CfeException, SQLException;

	public HSSFWorkbook generaDocumentoOp(List<BajasDepreciadosCienBean> datos) throws IOException;

	public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
			throws CfeException, SQLException;

	public void insertarRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException;

	public void insertaSolicitud(BajasDprCienFormBean dprCienFormBean, String nombreRepo)
			throws CfeException, SQLException;

	public BajasOpDocumentoBean obtenerDocumento(BajasDprCienFormBean dprBean) throws CfeException, SQLException;

	public Workbook obtenerReporte(Integer idDoc,Integer sociedadId) throws CfeException, SQLException, IOException;

	public void crearReporte(BajasDprCienFormBean dprBean) throws CfeException, SQLException;

	public BajasOpDocumentoBean obtenerDocumentoReciente(BajasDprCienFormBean dprBean)
			throws CfeException, SQLException;
}
