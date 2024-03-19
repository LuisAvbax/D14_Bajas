package com.telcel.gsa.dsaf.service.impl;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.amov.dao.ClaseAmovDao;
import com.telcel.gsa.dsaf.bean.ClaseBean;
import com.telcel.gsa.dsaf.dao.ClaseDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dipcel.dao.ClaseDipcelDao;
import com.telcel.gsa.dsaf.entity.BajasClase;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.ClaseSercotelDao;
import com.telcel.gsa.dsaf.service.ClaseService;
import com.telcel.gsa.dsaf.util.CfeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("claseService")
public class ClaseServiceImpl implements ClaseService, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1522354171162282032L;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	@Autowired
	@Qualifier("claseDipcelDaoImpl")
	ClaseDipcelDao claseDipcelDao;
	@Autowired
	@Qualifier("claseSercotelDaoImpl")
	ClaseSercotelDao claseSercotelDao;
	@Autowired
	@Qualifier("claseAmovDaoImpl")
	ClaseAmovDao claseAmovDao;
	@Autowired
	@Qualifier("claseDaoImpl")
	ClaseDao claseDao;
	
	
	@Override
	public void insertaClase(ClaseBean clase, SessionScopeUser sessionData) {
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					claseDao.insertaClase(clase,sessionData);
					
				}break;
				case 2:{
					claseDipcelDao.insertaClase(clase, sessionData);
				}break;
				case 3:{
					claseSercotelDao.insertaClase(clase, sessionData);
				}break;
				case 4:{
					claseAmovDao.insertaClase(clase, sessionData);
				}break;
			}
	
		}catch(CfeException e){
			throw e;
		}
	}
	
	
	@Override
	public void actualizaClase(ClaseBean clase, SessionScopeUser sessionData) {
		try{
			
			switch(sessionData.getSociedad().getId()){
				case 1:{
					claseDao.actualizaClase(clase, sessionData);
				}break;
				case 2:{
					claseDipcelDao.actualizaClase(clase, sessionData);
				}break;
				case 3:{
					claseSercotelDao.actualizaClase(clase, sessionData);
				}break;
				case 4:{
					claseAmovDao.actualizaClase(clase, sessionData);
				}break;
			}
		
		}catch(CfeException e){
			throw e;
		}	
	}
	
	
	@Override
	public void eliminaClase(ClaseBean clase, SessionScopeUser sessionData) {
		try{
		
		switch(sessionData.getSociedad().getId()){
			case 1:{
				claseDao.eliminaClase(clase);
			}break;
			case 2:{
				claseDipcelDao.eliminaClase(clase);
			}break;
			case 3:{
				claseSercotelDao.eliminaClase(clase);
			}break;
			case 4:{
				claseAmovDao.eliminaClase(clase);
			}break;
		}
		}catch(CfeException e){
			throw e;
		}
	}
	
	@Override
	public List<BajasClase> consultaClase(ClaseBean clase, SessionScopeUser sessionData) throws Exception {
		List<BajasClase> bajasList = null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					bajasList = claseDao.consultaClase(clase);
				}break;
				case 2:{
					bajasList = claseDipcelDao.consultaClase(clase);
				}break;
				case 3:{
					bajasList = claseSercotelDao.consultaClase(clase);
				}break;
				case 4:{
					bajasList = claseAmovDao.consultaClase(clase);
				}break;
			}
		}catch(CfeException e){
			throw e;
		}
		return bajasList;
	}
	
	@Override
	public List<BajasClase> consultaClaseValida(ClaseBean clase, SessionScopeUser sessionData) throws Exception {
		List<BajasClase> bajasList = null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					bajasList = claseDao.consultaClaseValidar(clase);
				}break;
				case 2:{
					bajasList = claseDipcelDao.consultaClaseValidar(clase);
				}break;
				case 3:{
					bajasList = claseSercotelDao.consultaClaseValidar(clase);
				}break;
				case 4:{
					bajasList = claseAmovDao.consultaClaseValidar(clase);
				}break;
			}
		}catch(CfeException e){
			throw e;
		}
		return bajasList;
	}
}
