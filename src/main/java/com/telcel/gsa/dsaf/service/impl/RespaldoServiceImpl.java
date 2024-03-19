package com.telcel.gsa.dsaf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.telcel.gsa.dsaf.bean.GeneraRespaldoBean;
import com.telcel.gsa.dsaf.dao.CargaArchivoDao;
import com.telcel.gsa.dsaf.dao.RespaldoDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.RespaldoService;
import com.telcel.gsa.dsaf.util.CfeException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * 
 * @author VI5XXAA
 *
 */
@Service("respaldoService")
public class RespaldoServiceImpl implements RespaldoService, Serializable{

	
	@Autowired
	@Qualifier("respaldoDaoImpl")
	RespaldoDao respaldoDao;
//	@Autowired
//	@Qualifier("cargaArchivoDipselDaoImpl")
//	CargaArchivoDipselDao cargaArchivoDipselDao;
//	@Autowired
//	@Qualifier("cargaArchivoSercotelDaoImpl")
//	CargaArchivoSercotelDao cargaArchivoSercotelDao;
//	@Autowired
//	@Qualifier("cargaArchivoAmovDaoImpl")
//	CargaArchivoAmovDao cargaArchivoAmovDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	public void gestionaRespaldo(GeneraRespaldoBean respaldo, SessionScopeUser sessionData) throws SQLException{
		try {
			switch(sessionData.getSociedad().getId()){
			case 1:{
				respaldoDao.ejecutaRespaldo(respaldo, sessionData);
			}break;
			case 2:{
//				cargaArchivoDipselDao.executeSp(carga);
			}break;
			case 3:{
//				cargaArchivoSercotelDao.executeSp(carga);
			}break;
			case 4:{
//				cargaArchivoAmovDao.executeSp(carga);
			}break;
		}
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			new CfeException(e.getMessage(), e);
		}
	}
	
	
	





	


}
