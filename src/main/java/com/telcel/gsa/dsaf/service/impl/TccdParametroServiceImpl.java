package com.telcel.gsa.dsaf.service.impl;


import com.telcel.gsa.dsaf.amov.dao.TccddParametroAmovDao;
import com.telcel.gsa.dsaf.bean.TcddParametroBean;
import com.telcel.gsa.dsaf.dao.TccddParametroDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dipcel.dao.TccddParametroDipselDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.TccddParametroSercotelDao;
import com.telcel.gsa.dsaf.service.TccdParametroService;
import com.telcel.gsa.dsaf.util.CfeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("parametroService")
public class TccdParametroServiceImpl implements TccdParametroService{
	
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	@Autowired
	TccddParametroDao tccddParametroDao;
	@Autowired
	TccddParametroDipselDao tccddParametroDipselDao;
	@Autowired
	TccddParametroSercotelDao tccddParametroSercotelDao;
	@Autowired
	TccddParametroAmovDao tccddParametroAmovDao;
	
	
	
	@Override
	public void actualiza(TcddParametroBean datos, SessionScopeUser sessionData){
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					tccddParametroDao.actualizaParametro(datos);
				}break;
				case 2:{
					tccddParametroDipselDao.actualizaParametro(datos);
				}break;
				case 3:{
					tccddParametroSercotelDao.actualizaParametro(datos);
				}break;
				case 4:{
					tccddParametroAmovDao.actualizaParametro(datos);
				}break;
			}
		
		}catch(CfeException e){
			throw e;
		}	
	}
	
	
	@Override
	public TcddParametroBean consulta(TcddParametroBean datos, SessionScopeUser sessionData)throws Exception{
		TcddParametroBean datQry = null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datQry = tccddParametroDao.consultaParametro(datos);
				}break;
				case 2:{
					datQry = tccddParametroDipselDao.consultaParametro(datos);
				}break;
				case 3:{
					datQry = tccddParametroSercotelDao.consultaParametro(datos);
				}break;
				case 4:{
					datQry = tccddParametroAmovDao.consultaParametro(datos);
				}break;
			}
		}catch(CfeException e){
			throw e;
		}
		return datQry;
	}
}
