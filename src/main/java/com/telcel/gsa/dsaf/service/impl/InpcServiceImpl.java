package com.telcel.gsa.dsaf.service.impl;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.amov.dao.InpcAmovDao;
import com.telcel.gsa.dsaf.bean.InpcBean;
import com.telcel.gsa.dsaf.dao.InpcDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dipcel.dao.InpcDipcelDao;
import com.telcel.gsa.dsaf.entity.BajasInpc;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.InpcSercotelDao;
import com.telcel.gsa.dsaf.service.InpcService;
import com.telcel.gsa.dsaf.util.CfeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("inpcService")
public class InpcServiceImpl implements InpcService, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7149960121385894290L;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	@Autowired
	InpcDipcelDao inpcDipcelDao;
	@Autowired
	InpcSercotelDao inpcSercotelDao;
	@Autowired
	InpcAmovDao inpcAmovDao;
	@Autowired
	InpcDao inpcDao;
	
	
	@Override
	public void insertaInpc(InpcBean inpc, SessionScopeUser sessionData) {
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					inpcDao.insertaInpc(inpc);
				}break;
				case 2:{
					inpcDipcelDao.insertaInpc(inpc);
				}break;
				case 3:{
					inpcSercotelDao.insertaInpc(inpc);
				}break;
				case 4:{
					inpcAmovDao.insertaInpc(inpc);
				}break;
			}
		BajasInpc binpc = new BajasInpc();
		binpc.setAnio(inpc.getInpc().getAnio());
		binpc.setMes(inpc.getInpc().getMes());
		binpc.setMesTxt(utileriasCfeDao.perBajaToObject(binpc.getMes()));
		binpc.setIndice(inpc.getInpc().getIndice());
		inpc.getInpcs().add(binpc);
		}catch(CfeException e){
			throw e;
		}
	}
	
	
	@Override
	public void actualizaInpc(InpcBean inpc, SessionScopeUser sessionData) {
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					inpcDao.actualizaInpc(inpc);
				}break;
				case 2:{
					inpcDipcelDao.actualizaInpc(inpc);
				}break;
				case 3:{
					inpcSercotelDao.actualizaInpc(inpc);
				}break;
				case 4:{
					inpcAmovDao.actualizaInpc(inpc);
				}break;
			}
		
		}catch(CfeException e){
			throw e;
		}	
	}
	
	
	@Override
	public void eliminaInpc(InpcBean inpc, SessionScopeUser sessionData) {
		try{
		
		switch(sessionData.getSociedad().getId()){
			case 1:{
				inpcDao.eliminaInpc(inpc);
			}break;
			case 2:{
				inpcDipcelDao.eliminaInpc(inpc);
			}break;
			case 3:{
				inpcSercotelDao.eliminaInpc(inpc);
			}break;
			case 4:{
				inpcAmovDao.eliminaInpc(inpc);
			}break;
		}
		}catch(CfeException e){
			throw e;
		}
	}
	
	@Override
	public List<BajasInpc> consultaInpc(InpcBean inpc, SessionScopeUser sessionData) throws Exception {
		List<BajasInpc> bajasList = null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					bajasList = inpcDao.consultaInpc(inpc);
				}break;
				case 2:{
					bajasList = inpcDipcelDao.consultaInpc(inpc);
				}break;
				case 3:{
					bajasList = inpcSercotelDao.consultaInpc(inpc);
				}break;
				case 4:{
					bajasList = inpcAmovDao.consultaInpc(inpc);
				}break;
			}
		}catch(CfeException e){
			throw e;
		}
		return bajasList;
	}
}
