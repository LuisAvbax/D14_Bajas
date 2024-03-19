package com.telcel.gsa.dsaf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.telcel.gsa.dsaf.amov.dao.CargaArchivoAmovDao;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.dao.CargaArchivoDao;
import com.telcel.gsa.dsaf.dipcel.dao.CargaArchivoDipselDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.CargaArchivoSercotelDao;
import com.telcel.gsa.dsaf.service.CargaArchivoService;
import com.telcel.gsa.dsaf.util.CfeException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * 
 * @author VI5XXAA
 *
 */
@Service("cargaArchivoService")
public class CargaArchivoServiceImpl implements CargaArchivoService, Serializable{

	
	@Autowired
	@Qualifier("cargaArchivoDaoImpl")
	CargaArchivoDao cargaArchivoDao;
	@Autowired
	@Qualifier("cargaArchivoDipselDaoImpl")
	CargaArchivoDipselDao cargaArchivoDipselDao;
	@Autowired
	@Qualifier("cargaArchivoSercotelDaoImpl")
	CargaArchivoSercotelDao cargaArchivoSercotelDao;
	@Autowired
	@Qualifier("cargaArchivoAmovDaoImpl")
	CargaArchivoAmovDao cargaArchivoAmovDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;


	
	public void executeSp(BajasDepreciadosCienBean carga, SessionScopeUser sessionData) throws CfeException {
		
		try{
//			cargaArchivoDao.executeSp(carga);
			switch(sessionData.getSociedad().getId()){
			case 1:{
				cargaArchivoDao.executeSp(carga);
			}break;
			case 2:{
				cargaArchivoDipselDao.executeSp(carga);
			}break;
			case 3:{
				cargaArchivoSercotelDao.executeSp(carga);
			}break;
			case 4:{
				cargaArchivoAmovDao.executeSp(carga);
			}break;
		}
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			new CfeException(e.getMessage(), e);
		}
		
	}
	





	


}
