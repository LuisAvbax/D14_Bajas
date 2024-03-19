package com.telcel.gsa.dsaf.service.impl;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;

import com.telcel.gsa.dsaf.amov.dao.UtileriasCfeAmovDao;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.UsuarioDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dipcel.dao.UtileriasCfeDipcelDao;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.UtileriasCfeSecortelDao;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.sds.servicios.empleado.cliente.StEmployeesEmpleadoItem;
import com.telcel.sds.servicios.empleado.cliente.StFilterEmployee;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalog;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEE;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEEResponse;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEES;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogGETEMPLOYEESResponse;
import com.telcel.sds.servicios.empleado.cliente.WsEmployeeCatalogSoapPort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4604210464610594065L;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	UtileriasCfeDao utileriasCfeDao;
	@Autowired
	@Qualifier("utileriasCfeDipcelDaoImpl")
	UtileriasCfeDao utileriasCfeDipcelDao;
	@Autowired
	@Qualifier("utileriasCfeSercotelDaoImpl")
	UtileriasCfeDao utileriasCfeSercotelDao;
	@Autowired
	@Qualifier("utileriasCfeAmovDaoImpl")
	UtileriasCfeDao utileriasCfeAmovDao;
	@Autowired
	@Qualifier("usuarioDaoImpl")
	UsuarioDao usuarioDao;
	@Autowired
	@Qualifier("usuarioDaoDipcelImpl")
	UsuarioDao usuarioDipcelDao;
	@Autowired
	@Qualifier("usuarioDaoSercotelImpl")
	UsuarioDao usuarioSercotelDao;
	@Autowired
	@Qualifier("usuarioDaoAmovImpl")
	UsuarioDao usuarioAmovDao;
	
	
	@Override
	public List<BajasOpUsuarioBean> buscaUsuarios(BajasOpUsuarioBean filtro, SessionScopeUser sessionData) {
		List<BajasOpUsuarioBean> usuarios = null;
		StFilterEmployee filterEmployee = new StFilterEmployee();
		filterEmployee.setCNumemp((!StringUtils.isBlank(filtro.getClaveEmpleado()))?filtro.getClaveEmpleado().toUpperCase():null);
		filterEmployee.setCNombre((!StringUtils.isBlank(filtro.getNombre()))?filtro.getNombre().toUpperCase():null);
		filterEmployee.setCApPaterno((!StringUtils.isBlank(filtro.getApaterno()))?filtro.getApaterno().toUpperCase():null);
		filterEmployee.setCApMaterno((!StringUtils.isBlank(filtro.getAmaterno()))?filtro.getAmaterno().toUpperCase():null);
		if(filtro.getBajasCatRegion() != null && !filtro.getBajasCatRegion().isEmpty()){
					filterEmployee.setCRegion(sessionData.getRegionesMap().get(filtro.getBajasCatRegion()).getId());
		}

		WsEmployeeCatalogGETEMPLOYEESResponse resp = consultaWSEmpleados(filterEmployee, sessionData.getParametros());
		
		usuarios = new ArrayList<BajasOpUsuarioBean>();
		for(StEmployeesEmpleadoItem item:  resp.getStemployees().getStEmployeesEmpleadoItem()){
			if(!StringUtils.isBlank(item.getCNumemp()) & item.getClaveError() != 2){
				BajasOpUsuarioBean usuarioBean = new BajasOpUsuarioBean();
				CatRegionBean region = new CatRegionBean();
				usuarioBean.setClaveEmpleado(item.getCNumemp());
				usuarioBean.setApaterno(item.getCApPaterno());
				usuarioBean.setAmaterno(item.getCApMaterno());
				usuarioBean.setNombre(item.getCNombre());
//				region.setId(item.getCRegion());
				usuarioBean.setBajasCatRegion(item.getCRegion());
				usuarioBean.setDireccion(item.getCDireccion());
				usuarioBean.setSubdireccion(item.getCSubdireccion());
				usuarioBean.setGerencia(item.getCGerencia());
				usuarioBean.setDepartamento(item.getCDepartamento());
				usuarioBean.setPuesto(item.getCPuesto());
				usuarioBean.setBajasOpRol(new BajasOpRol());
				usuarioBean.setCorreo(item.getEMail());
				usuarios.add(usuarioBean);
			}
		}
		return usuarios;
	}
	
	
	@Override
	public List<BajasOpUsuarioBean> buscaUsuariosWS(BajasOpUsuarioBean filtro, SessionScopeUser sessionData) {
		List<BajasOpUsuarioBean> usuarios = null;
		StFilterEmployee filterEmployee = new StFilterEmployee();
		filterEmployee.setCNumemp((!StringUtils.isBlank(filtro.getClaveEmpleado()))?filtro.getClaveEmpleado().toUpperCase():null);
		filterEmployee.setCNombre(null);
		filterEmployee.setCApPaterno(null);
		filterEmployee.setCApMaterno(null);
		filterEmployee.setCRegion(null);


		WsEmployeeCatalogGETEMPLOYEEResponse resp = consultaWSEmpleado(filterEmployee, sessionData.getParametros());
		
		usuarios = new ArrayList<BajasOpUsuarioBean>();
		for(StEmployeesEmpleadoItem item:  resp.getStemployees().getStEmployeesEmpleadoItem()){
			if(!StringUtils.isBlank(item.getCNumemp()) & item.getClaveError() != 2){
				BajasOpUsuarioBean usuarioBean = new BajasOpUsuarioBean();
				CatRegionBean region = new CatRegionBean();
				usuarioBean.setClaveEmpleado(item.getCNumemp());
				usuarioBean.setApaterno(item.getCApPaterno());
				usuarioBean.setAmaterno(item.getCApMaterno());
				usuarioBean.setNombre(item.getCNombre());
				usuarioBean.setBajasCatRegion(item.getCRegion());
				usuarioBean.setDireccion(item.getCDireccion());
				usuarioBean.setSubdireccion(item.getCSubdireccion());
				usuarioBean.setGerencia(item.getCGerencia());
				usuarioBean.setDepartamento(item.getCDepartamento());
				usuarioBean.setPuesto(item.getCPuesto());
				usuarioBean.setBajasOpRol(new BajasOpRol());
				usuarios.add(usuarioBean);
			}
		}
		return usuarios;
	}
	
	@Override
	public List<BajasOpUsuarioBean> buscaUsuariosRegistrados(UsuarioBean usuarioParam, SessionScopeUser sessionScopeUser) throws Exception {
		List<BajasOpUsuarioBean> usuarios = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				usuarios = utileriasCfeDao.obtenerUsuariosRegistrados(usuarioParam);
			}break;
			case 2:{
				usuarios = utileriasCfeDipcelDao.obtenerUsuariosRegistrados(usuarioParam);
			}break;
			case 3:{
				usuarios = utileriasCfeSercotelDao.obtenerUsuariosRegistrados(usuarioParam);
			}break;
			case 4:{
				usuarios = utileriasCfeAmovDao.obtenerUsuariosRegistrados(usuarioParam);
			}break;
		}
		return usuarios;
		
	}
	
	
	public WsEmployeeCatalogGETEMPLOYEEResponse consultaWSEmpleado(StFilterEmployee filterEmployee,Map<String,BajasCatParametrosBean> parametros){
		WsEmployeeCatalogGETEMPLOYEE employee  = new WsEmployeeCatalogGETEMPLOYEE();
		employee.setStfilteremployee(filterEmployee);
		URL url = null;
		URL baseUrl = null;
		baseUrl = WsEmployeeCatalog.class.getResource(".");
		WsEmployeeCatalogSoapPort port = null;
		WsEmployeeCatalog service = null;
		 try {
				url = new URL(baseUrl, parametros.get("LDAP_ENDPOINT").getDescripcion());
			
			service = new WsEmployeeCatalog(url, new QName(parametros.get("LDAP_TNS").getValor(), parametros.get("LDAP_WSNAME").getValor()));
			port =	service.getWsEmployeeCatalogSoapPort();
		 } catch (MalformedURLException e) {
				throw new CfeException(e.getMessage(), e);
			}
		 return port.getemployee(employee);
	}
	
	public WsEmployeeCatalogGETEMPLOYEESResponse consultaWSEmpleados(StFilterEmployee filterEmployee,Map<String,BajasCatParametrosBean> parametros){
		WsEmployeeCatalogGETEMPLOYEES employees = new WsEmployeeCatalogGETEMPLOYEES();
		employees.setStfilteremployee(filterEmployee);
		URL url = null;
		URL baseUrl = null;
		baseUrl = WsEmployeeCatalog.class.getResource(".");
		WsEmployeeCatalogSoapPort port = null;
		WsEmployeeCatalog service = null;
		 try {
				url = new URL(baseUrl, parametros.get("LDAP_ENDPOINT").getDescripcion());
			
			service = new WsEmployeeCatalog(url, new QName(parametros.get("LDAP_TNS").getValor(), parametros.get("LDAP_WSNAME").getValor()));
			port =	service.getWsEmployeeCatalogSoapPort();
		 } catch (MalformedURLException e) {
				throw new CfeException(e.getMessage(), e);
			}
		return port.getemployees(employees);
	}
	
	@Override
	public BajasOpUsuarioBean obtieneRolUsuario(BajasOpUsuarioBean usuarioParam, SessionScopeUser sessionScopeUser) throws Exception{
		BajasOpUsuarioBean usuarioBean = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				usuarioBean = utileriasCfeDao.obtenerRolUsuario(usuarioParam);
			}break;
			case 2:{
				usuarioBean = utileriasCfeDipcelDao.obtenerRolUsuario(usuarioParam);
			}break;
			case 3:{
				usuarioBean = utileriasCfeSercotelDao.obtenerRolUsuario(usuarioParam);
			}break;
			case 4:{
				usuarioBean = utileriasCfeAmovDao.obtenerRolUsuario(usuarioParam);
			}break;
		}
		return usuarioBean;
	}
	@Override
	public List<RolBean> obtieneRoles(SessionScopeUser sessionScopeUser){
		List<RolBean> roles = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1:{
			roles = utileriasCfeDao.obtenerRoles();
		}break;
		case 2:{
			roles = utileriasCfeDipcelDao.obtenerRoles();
		}break;
		case 3:{
			roles = utileriasCfeSercotelDao.obtenerRoles();
		}break;
		case 4:{
			roles = utileriasCfeAmovDao.obtenerRoles();
		}break;
	}
		return roles;
	}
	@Override
	public CatRegionBean obtieneRegionUsuario(CatRegionBean region, SessionScopeUser sessionScopeUser){
		CatRegionBean regionBean = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				regionBean =utileriasCfeDao.obtieneRegionUsuario(region);
			}break;
			case 2:{
				regionBean =utileriasCfeDipcelDao.obtieneRegionUsuario(region);
			}break;
			case 3:{
				regionBean =utileriasCfeSercotelDao.obtieneRegionUsuario(region);
			}break;
			case 4:{
				regionBean =utileriasCfeAmovDao.obtieneRegionUsuario(region);
			}break;
		}
		return regionBean;
	}

	@Override
	public void guardaUsuario(BajasOpUsuarioBean usr, SessionScopeUser sessionScopeUser){
		usr.setEstadoRegistro(false);
		BajasOpUsuarioBean usuarioReg = null;
		Date fechaActual = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				usuarioReg = utileriasCfeDao.obtenerUsuarioRegistrado(usr);
				fechaActual = utileriasCfeDao.obtenerFecha();
			}break;
			case 2:{
				usuarioReg = utileriasCfeDipcelDao.obtenerUsuarioRegistrado(usr);
				fechaActual = utileriasCfeDipcelDao.obtenerFecha();
			}break;
			case 3:{
				usuarioReg = utileriasCfeSercotelDao.obtenerUsuarioRegistrado(usr);
				fechaActual = utileriasCfeSercotelDao.obtenerFecha();
			}break;
			case 4:{
				usuarioReg = utileriasCfeAmovDao.obtenerUsuarioRegistrado(usr);
				fechaActual = utileriasCfeAmovDao.obtenerFecha();
			}break;
		}
		
		if(usuarioReg == null){
			usuarioReg = new BajasOpUsuarioBean();
			usuarioReg.setClaveEmpleado(usr.getClaveEmpleado());
			usuarioReg.setClaveUniversal(sessionScopeUser.getUsuarioCfe().getClaveUniversal());
			usuarioReg.setApaterno(usr.getApaterno());
			usuarioReg.setAmaterno(usr.getAmaterno());
			usuarioReg.setNombre(usr.getNombre());
			usuarioReg.setUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
			usuarioReg.setFechaCreacion(fechaActual);
			usuarioReg.setEstadoRegistro(true);
			usuarioReg.setCorreo(usr.getCorreo());
			usuarioReg.setEstatusBaja(0);
			if(usr.getBajasOpRol().getId() == 0){
				usuarioReg.setBajasOpRol(null);
			}else{
				usuarioReg.setBajasOpRol(usr.getBajasOpRol());
			}
			BajasCatRegion catreg = new BajasCatRegion();
			catreg.setId(10);
			usuarioReg.setBajasRegion(catreg);
			switch(sessionScopeUser.getSociedad().getId()){
				case 1:{
					usuarioDao.insertaUsuario(usuarioReg);
				}break;
				case 2:{
					usuarioDipcelDao.insertaUsuario(usuarioReg);
				}break;
				case 3:{
					usuarioSercotelDao.insertaUsuario(usuarioReg);
				}break;
				case 4:{
					usuarioAmovDao.insertaUsuario(usuarioReg);
				}break;
			}
			
		}else{
			if(usr.getBajasOpRol().getId() == 0){
				usr.setBajasOpRol(null);
			}
			if(usr.getBajasCatRegion().equals("0")){
				CatRegionBean datRegion = new CatRegionBean();
				datRegion.setId(usr.getBajasCatRegion());
				switch(sessionScopeUser.getSociedad().getId()){
					case 1:{
						datRegion =  utileriasCfeDao.obtieneRegionUsuario(datRegion);
					}break;
					case 2:{
						datRegion =  utileriasCfeDipcelDao.obtieneRegionUsuario(datRegion);
					}break;
					case 3:{
						datRegion =  utileriasCfeSercotelDao.obtieneRegionUsuario(datRegion);
					}break;
					case 4:{
						datRegion =  utileriasCfeAmovDao.obtieneRegionUsuario(datRegion);
					}break;
				}
				
				usr.setBajasCatRegion(datRegion.getId());
			}
			usr.setUsuarioModificacion(sessionScopeUser.getUsuarioCfe().getId());
			usr.setFechaModificacion(fechaActual);
			usr.setEstadoRegistro(true);
			usr.setEstatusBaja(0);
			
			switch(sessionScopeUser.getSociedad().getId()){
				case 1:{
					usuarioDao.actualizaUsuario(usr);
				}break;
				case 2:{
					usuarioDipcelDao.actualizaUsuario(usr);
				}break;
				case 3:{
					usuarioSercotelDao.actualizaUsuario(usr);
				}break;
				case 4:{
					usuarioAmovDao.actualizaUsuario(usr);
				}break;
			}
			
		}
		
	}
	
	@Override
	public void actualizaUsuario(BajasOpUsuarioBean usr, SessionScopeUser sessionScopeUser){
		
		if(usr.getBajasOpRol().getId() == 0){
			usr.setBajasOpRol(null);
		}
		if(usr.getBajasCatRegion().equals("0")){
			CatRegionBean datRegion = new CatRegionBean();
			datRegion.setId(usr.getBajasCatRegion());	
			
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				datRegion =  utileriasCfeDao.obtieneRegionUsuario(datRegion);
			
			}break;
			case 2:{
				datRegion =  utileriasCfeDipcelDao.obtieneRegionUsuario(datRegion);
			
			}break;
			case 3:{
				datRegion =  utileriasCfeSercotelDao.obtieneRegionUsuario(datRegion);
				
			}break;
			case 4:{
				datRegion =  utileriasCfeAmovDao.obtieneRegionUsuario(datRegion);
				
			}break;
		}
			
			usr.setBajasCatRegion(datRegion.getId());
		}
		usr.setUsuarioModificacion(sessionScopeUser.getUsuarioCfe().getId());
		usr.setEstadoRegistro(true);
		usr.setEstatusBaja(0);
		switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				usuarioDao.actualizaUsuario(usr);
			}break;
			case 2:{
				usuarioDipcelDao.actualizaUsuario(usr);
			}break;
			case 3:{
				usuarioSercotelDao.actualizaUsuario(usr);
			}break;
			case 4:{
				usuarioAmovDao.actualizaUsuario(usr);
			}break;
		}
		
	}
	
	
	
	@Override
	public void bajaUsuario(BajasOpUsuarioBean usr, SessionScopeUser sessionScopeUser){
		
		if(usr.getBajasOpRol().getId() == 0){
			usr.setBajasOpRol(null);
		}
		CatRegionBean datRegion = new CatRegionBean();
		datRegion.setId(usr.getBajasCatRegion());

		
		usr.setBajasCatRegion(datRegion.getId());
		usr.setUsuarioModificacion(sessionScopeUser.getUsuarioCfe().getId());
		usr.setEstadoRegistro(false);
		
		switch(sessionScopeUser.getSociedad().getId()){
		case 1:{
			usuarioDao.actualizaUsuario(usr);
		}break;
		case 2:{
			usuarioDipcelDao.actualizaUsuario(usr);
		}break;
		case 3:{
			usuarioSercotelDao.actualizaUsuario(usr);
		}break;
		case 4:{
			usuarioAmovDao.actualizaUsuario(usr);
		}break;
	}
		
		
	}

}
