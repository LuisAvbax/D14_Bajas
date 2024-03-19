package com.telcel.gsa.dsaf.action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ViewScoped;

import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.Predicate;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("usuarioAction")
@ViewScoped
public class UsuarioAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3860287028313521018L;
	final static Logger logger = LoggerFactory.getLogger(UsuarioAction.class);
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	
	public void initFlow(RequestContext ctx){
		UsuarioBean usuario = new UsuarioBean();
		usuario.setUsuarioSel(new BajasOpUsuarioBean());
		BajasOpUsuarioBean usuarioSeleccion = new BajasOpUsuarioBean();
		usuarioSeleccion.setBajasOpRol(new BajasOpRol());
		usuarioSeleccion.setBajasRegion(new BajasCatRegion());
		BajasOpUsuarioBean nvoUsuario = new BajasOpUsuarioBean();
		nvoUsuario.setBajasOpRol(new BajasOpRol());
		nvoUsuario.setBajasRegion(new BajasCatRegion());
		usuario.setUsuarioSel(usuarioSeleccion);
		usuario.setUsuarioAdd(nvoUsuario);
		usuario.setUsuarios(new ArrayList<BajasOpUsuarioBean>());
		usuario.setRolBean(new RolBean());
		usuario.setRegion(new CatRegionBean());
		usuario.setRegiones(utileriasCfeService.obtenerRegionesWID(sessionScopeUser));
		usuario.setRoles(usuarioService.obtieneRoles(sessionScopeUser));
		ctx.getFlowScope().put("usuario", usuario);
		ctx.getFlowScope().put("nvousuario", nvoUsuario);
		}
	

	
	public void buscaUsuarios(RequestContext ctx){
		BajasOpUsuarioBean nvoUsuario = (BajasOpUsuarioBean)ctx.getFlowScope().get("nvousuario");
		if(nvoUsuario.getUsuarios() != null && !nvoUsuario.getUsuarios().isEmpty()){
			nvoUsuario.getUsuarios().clear();
		}
		// Busca usuarios LDAP
		nvoUsuario.setUsuarios(usuarioService.buscaUsuarios(nvoUsuario, sessionScopeUser));
		ctx.getFlowScope().put("nvousuario", nvoUsuario);
	}
	

	public void buscaUsuariosRegistrados(RequestContext ctx){
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuario.getUsuarios().clear();
		try {
			usuario.setUsuarios(usuarioService.buscaUsuariosRegistrados(usuario,sessionScopeUser));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
		ctx.getFlowScope().put("usuario", usuario);
	}
	
	public void buscaDetalleUsuario(SelectEvent event){
		org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder
				.getRequestContext();
		BajasOpUsuarioBean usuario = (BajasOpUsuarioBean)ctx.getFlowScope().get("nvousuario");
		
		usuario.setUsuarioSel((BajasOpUsuarioBean) event.getObject());
		List<BajasOpUsuarioBean> uDetailList = usuarioService.buscaUsuariosWS(usuario.getUsuarioSel(), sessionScopeUser);
		
		if(uDetailList != null && !uDetailList.isEmpty()){
			BajasOpUsuarioBean uDetail = uDetailList.get(0);
			usuario.getUsuarioSel().setBajasCatRegion(uDetail.getBajasCatRegion());
			usuario.getUsuarioSel().setDireccion(uDetail.getDireccion());
			usuario.getUsuarioSel().setSubdireccion(uDetail.getSubdireccion());
			usuario.getUsuarioSel().setGerencia(uDetail.getGerencia());
			usuario.getUsuarioSel().setDepartamento(uDetail.getDepartamento());
			usuario.getUsuarioSel().setPuesto(uDetail.getPuesto());
			
		}
		if(usuario.getUsuarioSel().getBajasOpRol() == null){
		}
		ctx.getFlowScope().put("nvousuario", usuario);
	}
	
	public void asignaAlta(SelectEvent event){
		org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder
				.getRequestContext();
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuario.setUsuarioAdd((BajasOpUsuarioBean) event.getObject());
		ctx.getFlowScope().put("usuario", usuario);
	}
	
	public void addUserPopUp(RequestContext ctx){
		BajasOpUsuarioBean nvoUsuario =  new BajasOpUsuarioBean();
//		nvoUsuario.setBajasCatRegion(new BajasCatRegion());
		nvoUsuario.setBajasOpRol(new BajasOpRol());
		ctx.getFlowScope().put("nvousuario", nvoUsuario);
//		org.primefaces.context.RequestContext.getCurrentInstance().execute("addUserDialog.show()");
	}
	
	public void seleccionaUsuario(SelectEvent event){
		org.springframework.webflow.execution.RequestContext ctx = RequestContextHolder
				.getRequestContext();
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		
		usuario.setUsuarioAdd((BajasOpUsuarioBean) event.getObject());
		List<BajasOpUsuarioBean> uDetailList = usuarioService.buscaUsuariosWS(usuario.getUsuarioSel(), sessionScopeUser);
		
		if(uDetailList != null && !uDetailList.isEmpty()){
			BajasOpUsuarioBean uDetail = uDetailList.get(0);
//			usuario.getUsuarioAdd().setBajasCatRegion(usuarioService.obtieneRegionUsuario(uDetail.getBajasCatRegion()));
			usuario.getUsuarioAdd().setBajasCatRegion(uDetail.getBajasCatRegion());
			usuario.getUsuarioAdd().setDireccion(uDetail.getDireccion());
			usuario.getUsuarioAdd().setSubdireccion(uDetail.getSubdireccion());
			usuario.getUsuarioAdd().setGerencia(uDetail.getGerencia());
			usuario.getUsuarioAdd().setDepartamento(uDetail.getDepartamento());
			usuario.getUsuarioAdd().setPuesto(uDetail.getPuesto());
			
		}
		if(usuario.getUsuarioSel().getBajasOpRol() == null){
			usuario.getUsuarioSel().setBajasOpRol(new BajasOpRol());
		}
		ctx.getFlowScope().put("usuario", usuario);
	}
	
	public void guardaRol(RequestContext ctx){
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuarioService.actualizaUsuario(usuario.getUsuarioSel(),sessionScopeUser);
		org.primefaces.context.RequestContext.getCurrentInstance().execute("roleInfoDialog.hide()");
	}
	
	public void guardaUsuario(RequestContext ctx){
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuarioService.actualizaUsuario(usuario.getUsuarioAdd(),sessionScopeUser);
		
	}
	
	public void altaUsuario(RequestContext ctx){
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuarioService.guardaUsuario(usuario.getUsuarioAdd(),sessionScopeUser);
		ctx.getFlowScope().put("usuario", usuario);
		org.primefaces.context.RequestContext.getCurrentInstance().execute("altaUserDialog.hide()");
		org.primefaces.context.RequestContext.getCurrentInstance().execute("addUserDialog.hide()");
		
	}
	
	public void bajaUsuario(RequestContext ctx){
		UsuarioBean usuario = (UsuarioBean)ctx.getFlowScope().get("usuario");
		usuarioService.bajaUsuario(usuario.getUsuarioSel(),sessionScopeUser);
		CollectionUtils.filter(usuario.getUsuarios(), new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				BajasOpUsuarioBean us = (BajasOpUsuarioBean) object;
				if((us.getEstadoRegistro() == Constants.ESTATUS_TRUE)){
					return Constants.ESTATUS_TRUE;
				}
				return Constants.ESTATUS_FALSE;
			}
		});
		org.primefaces.context.RequestContext.getCurrentInstance().execute("roleInfoDialog.hide()");
	}
	
	
	
	
	
	
}
