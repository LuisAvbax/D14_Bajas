package com.telcel.gsa.dsaf.dipcel.dao;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.ReporteConcentradoBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.bean.ResumenConceptoRegBean;
import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.entity.v_bajas_nvo3;
import com.telcel.gsa.dsaf.util.CfeException;


public interface ReporteDetalladoDipcelDao extends AbstractDao<v_bajas_nvo3, Integer>, Serializable {
	

	public ReporteDetalladoBean consultaReporteDetallado(ReporteDetalladoBean datos)throws CfeException, SQLException;
	public ReporteDetalladoBean consultaReporteDetalladoAjuste(ReporteDetalladoBean datos) throws CfeException, SQLException;
	public ReporteDetalladoBean consultaReporteDetalladoNetos(ReporteDetalladoBean datos) throws CfeException, SQLException; 
	public void insertaArchivo(byte[] bytes) throws CfeException, SQLException;
	public void registraSolicitud(BajasOpDocumentoBean documento) throws CfeException, SQLException;
	public List<BajasOpDocumentoBean> consultaArchivo(BajasOpDocumentoBean doc) throws CfeException, SQLException ;
	public BajasOpDocumentoBean consultaArchivoId(Integer id) throws CfeException, SQLException;
	public void generaArchivo(BajasOpDocumentoBean doc) throws CfeException, SQLException;
	
}
