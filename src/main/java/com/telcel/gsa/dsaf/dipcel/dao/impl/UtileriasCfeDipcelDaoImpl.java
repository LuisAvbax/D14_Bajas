package com.telcel.gsa.dsaf.dipcel.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.ReporteBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.TotalBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dao.impl.AbstractDaoImpl;
import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA (phantom)
 *
 */
@Repository
public class UtileriasCfeDipcelDaoImpl extends AbstractDaoImpl<BajasCatParametros, Integer> implements UtileriasCfeDao, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1669481400982771068L;
	final static Logger logger = LoggerFactory.getLogger(UtileriasCfeDipcelDaoImpl.class);
	
	@Autowired
	transient DataSource dataSourceDipcel;
	
	
	protected UtileriasCfeDipcelDaoImpl() {
		super(BajasCatParametros.class);
	}
		
	
	public Date obtenerFecha(){
		Date fechaActual = null;
		Connection conn = null;
		  try {
	            conn = dataSourceDipcel.getConnection();
                
		StringBuilder queryString;
		queryString = new StringBuilder("SELECT sysdate as fecha from baja_cat_parametro ");
		PreparedStatement preparedStatement = conn.prepareStatement(queryString.toString());
        
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            fechaActual = rs.getDate("fecha"); 
        }
		  } catch (SQLException e) {
	            // TODO Bloque catch generado automáticamente
			  logger.error(e.getMessage(),e);
	       
	        }finally{
	            try{
	                if(conn != null){
	                    conn.close();
	                }
	            }catch(SQLException e){
	                try {
						throw new Exception (e.getMessage(),e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }catch(Exception e){
	                try {
						throw new Exception("Ocurrio un error al consultar fecha",e);
					} catch (Exception e1) {
						throw new CfeException(e.getMessage(),e);
					}
	            }
	        }
		return fechaActual;
	}
	
	public  List<CatRegionBean> obtenerRegiones() {
		Connection conn = null;
		List<CatRegionBean> parametros = null;  
        try {
            conn = dataSourceDipcel.getConnection();
            
            if(!conn.isClosed()){
            	
                logger.info("Dabase connection OK");
               
                StringBuffer selectSQL = new StringBuffer(" select c_id_region, c_cve_region, c_cve_region || ':' || c_desc_region as c_desc_region  ");
                selectSQL.append(" from cat_regiones ");
                selectSQL.append(" where c_desc_region not like '%TODAS%' ");
                selectSQL.append(" order by c_cve_region ");
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
                ResultSet rs = preparedStatement.executeQuery();
               parametros = new  ArrayList <CatRegionBean>();
                
                while (rs.next()) {
                    CatRegionBean datCatRegion= new CatRegionBean();
                   
                    datCatRegion.setId(rs.getString("c_cve_region"));
                    datCatRegion.setDescripcion(rs.getString("c_desc_region"));
                    
                    parametros.add(datCatRegion);
                }
            }
        } catch (SQLException e) {
            // TODO Bloque catch generado automáticamente
        	logger.error(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                try {
					throw new Exception (e.getMessage(),e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }catch(Exception e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }
        }
		return parametros;
	}
	
	public  List<BajasCatRegion> obtenerRegionesWID()throws CfeException{
		Connection conn = null;
		List<BajasCatRegion> parametros = null;  
        try {
            conn = dataSourceDipcel.getConnection();
            
            if(!conn.isClosed()){
            	
                logger.info("Dabase connection OK");
               
                StringBuffer selectSQL = new StringBuffer(" select c_id_region, c_cve_region, c_cve_region || ':' || c_desc_region as c_desc_region  ");
                selectSQL.append(" from cat_regiones ");
                selectSQL.append(" where c_desc_region not like '%TODAS%' ");
                selectSQL.append(" order by c_cve_region ");
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
                ResultSet rs = preparedStatement.executeQuery();
               parametros = new  ArrayList <BajasCatRegion>();
                
                while (rs.next()) {
                    BajasCatRegion datCatRegion= new BajasCatRegion();
                    datCatRegion.setId(rs.getInt("c_id_region"));
                    datCatRegion.setClave(rs.getString("c_cve_region"));
                    datCatRegion.setDescripcion(rs.getString("c_desc_region"));
                    
                    parametros.add(datCatRegion);
                }
            }
        } catch (SQLException e) {
            throw new CfeException(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                try {
					throw new Exception (e.getMessage(),e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }catch(Exception e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }
        }
		return parametros;
	}
	
	
	public BajasOpUsuarioBean obtieneUsuario(BajasOpUsuarioBean usuarioParam) {
				
		Connection conn = null;
		BajasOpUsuarioBean datBean = new BajasOpUsuarioBean();
	       
        try {
            conn = dataSourceDipcel.getConnection();
            
            if(!conn.isClosed()){
                logger.info("Dabase connection OK");
                
                String claveEmpleado = usuarioParam.getClaveEmpleado();
                
				StringBuffer selectSQL = new StringBuffer("select usuario_id, usuario_clave_empleado,  ");
                selectSQL.append("usuario_clave_universal, usuario_rol_id, usuario_region_id, ");
				selectSQL.append("usuario_nombre, usuario_apaterno, usuario_amaterno, ");
				selectSQL.append(" usuario_usuario_creacion, usuario_fecha_creacion, ");
				selectSQL.append(" usuario_usuario_modificacion, usuario_fecha_modificacion, ");
				selectSQL.append(" usuario_estado_registro, usuario_correo ");
				selectSQL.append(" from bajas_op_usuario ");
                selectSQL.append(" WHERE usuario_estado_registro = 1 ");
//                selectSQL.append(estadoRegistro);
                selectSQL.append(" and usuario_clave_empleado = ");
				selectSQL.append(claveEmpleado);
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
                ResultSet rs = preparedStatement.executeQuery();
                BajasOpRol datRol= new BajasOpRol();
                while (rs.next()) {
					
                    
                   
                    datBean.setId(rs.getInt("usuario_id"));
                    datBean.setNombre(rs.getString("usuario_clave_empleado"));
                    datBean.setClaveUniversal(rs.getString("usuario_clave_universal"));
                    datBean.setBajasOpRol(datRol);
                    datBean.setBajasCatRegion(rs.getString("usuario_region_id"));
                    datBean.setNombre(rs.getString("usuario_nombre"));
                    datBean.setApaterno(rs.getString("usuario_apaterno"));
                    datBean.setAmaterno(rs.getString("usuario_amaterno"));
					datBean.setUsuarioCreacion(rs.getInt("usuario_usuario_creacion"));	
					datBean.setFechaCreacion(rs.getDate("usuario_fecha_creacion")); 
					datBean.setUsuarioModificacion(rs.getInt("usuario_usuario_modificacion")); 
					datBean.setFechaModificacion(rs.getDate("usuario_fecha_modificacion")); 
					datBean.setBajasOpRol(new BajasOpRol());
					datBean.getBajasOpRol().setId(rs.getInt("usuario_rol_id"));
					
					if(rs.getInt("usuario_estado_registro") == 1)
						datBean.setEstadoRegistro(true);
	                else 
	                	datBean.setEstadoRegistro(false);
					
					datBean.setCorreo(rs.getString("usuario_correo"));  
                    
					
					if (datBean != null && datBean.getBajasOpRol() != null)
					{
						selectSQL.delete(0, selectSQL.length());
		
						selectSQL.append("select rol_id, rol_nombre, rol_descripcion, rol_fecha_creacion, ");
						selectSQL.append("rol_usuario_modificacion, rol_fecha_modificacion, ");
						selectSQL.append("rol_estado_registro,rol_usuario_creacion ");
						selectSQL.append("from bajas_op_rol ");
						selectSQL.append("where rol_id = " + datBean.getBajasOpRol().getId() + " ");
						selectSQL.append("and rol_estado_registro = 1");
						preparedStatement = conn.prepareStatement(selectSQL.toString());
						 rs = preparedStatement.executeQuery();
						BajasOpUsuario usuCrea = null;
						BajasOpUsuario usuMod = null;
						while (rs.next()) {
							
                   
							datRol.setId(rs.getInt("rol_id"));
							datRol.setNombre(rs.getString("rol_nombre"));
							datRol.setDescripcion(rs.getString("rol_descripcion"));
		                	usuMod= new BajasOpUsuario();
		                	usuMod.setId(rs.getInt("rol_usuario_modificacion"));
							datRol.setUsuarioModificacion(usuMod);
							datRol.setFechaModificacion(rs.getDate("rol_fecha_modificacion"));
							datRol.setEstadoRegistro(rs.getInt("rol_estado_registro") == 1 ? true: false);
							usuCrea= new BajasOpUsuario();
		                	usuCrea.setId(rs.getInt("rol_usuario_creacion"));
		                	datRol.setUsuarioCreacion(usuCrea);
							
							
						}
						if(datRol != null){
							datBean.setBajasOpRol(datRol);
							
						}
						
						
					}
					
                }
            }
        } catch (SQLException e) {
        	throw new CfeException(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                try {
					throw new Exception (e.getMessage(),e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }catch(Exception e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }
        }	
		return datBean;
	}
	
	
	public Integer existeUsuario(String clave){
		Long count= 0L;
		Connection conn = null;
	       
	        try {
	            conn = dataSourceDipcel.getConnection();
	            if(!conn.isClosed()){
	                logger.info("Dabase connection OK");
	               
					int estadoRegistro = 1;

	                StringBuffer selectSQL = new StringBuffer("select count(*) ");
	                selectSQL.append("from bajas_op_usuario ");
	                selectSQL.append("where usuario_estado_registro =");
	                selectSQL.append(estadoRegistro);
	                selectSQL.append(" and usuario_clave_empleado= ");
					selectSQL.append(clave);
	                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	               
	                ResultSet rs = preparedStatement.executeQuery();
	                
	                while (rs.next()) {
	                    count = (long) rs.getInt(1);
	                   
	                }
	            }
	        } catch (SQLException e) {
	            throw new CfeException(e.getMessage(),e);
	       
	        }finally{
	            try{
	                if(conn != null){
	                    conn.close();
	                }
	            }catch(SQLException e){
	                try {
						throw new Exception (e.getMessage(),e);
					} catch (Exception e1) {
						throw new CfeException(e.getMessage(),e);
					}
	            }catch(Exception e){
	                try {
						throw new Exception("Ocurrio un error al consultar proveedores",e);
					} catch (Exception e1) {
						throw new CfeException(e.getMessage(),e);
					}
	            }
	        }
		return count.intValue();
	}
	
	
	public List<RolBean> obtenerRoles() {
		List<RolBean> roles = new ArrayList<RolBean>();
		
		
			Connection conn = null;
		       
	        try {
	            conn = dataSourceDipcel.getConnection();
	            if(!conn.isClosed()){
	                logger.info("Dabase connection OK");
	                
	                StringBuffer selectSQL = new StringBuffer("select rol_id, rol_nombre, rol_descripcion, rol_fecha_creacion,  ");
	                selectSQL.append("rol_usuario_modificacion, rol_fecha_modificacion, rol_estado_registro,  ");
	                selectSQL.append("rol_usuario_creacion ");
					selectSQL.append("from bajas_op_rol ");
	                selectSQL.append("where  rol_estado_registro = 1 ");
	                selectSQL.append("order by rol_id");
	                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	               
	                ResultSet rs = preparedStatement.executeQuery();
	                
	                while (rs.next()) {
	                	RolBean rol= new RolBean();
	                   
	                	rol.setIdRol(rs.getInt("rol_id"));
	                	rol.setNombre(rs.getString("rol_nombre"));
	                	rol.setDescripcion(rs.getString("rol_descripcion"));
	                	rol.setFechaCreacion(rs.getDate("rol_fecha_creacion"));
	                	rol.setUsuarioModificacion(rs.getInt("rol_usuario_modificacion"));
	                	rol.setFechaModificacion(rs.getDate("rol_fecha_modificacion"));
	                	rol.setEstadoRegistro(rs.getInt("rol_estado_registro"));
	                	rol.setUsuarioCreacion(rs.getInt("rol_usuario_creacion"));
	                	roles.add(rol);
	                    
	                }
	            }
	        }  catch (SQLException e) {
	            // TODO Bloque catch generado automáticamente
	        	logger.error(e.getMessage(),e);
	       
	        }finally{
	            try{
	                if(conn != null){
	                    conn.close();
	                }
	            }catch(SQLException e){
	                try {
						throw new Exception("Ocurrio un error al consultar proveedores",e);
					} catch (Exception e1) {
						throw new CfeException(e.getMessage(),e);
					}
	            }catch(Exception e){
	                try {
						throw new Exception("Ocurrio un error al consultar proveedores",e);
					} catch (Exception e1) {
						throw new CfeException(e.getMessage(),e);
					}
	            }
	            finally{
	            	if(conn != null){
	            		try {
	    					conn.close();
	    				} catch (SQLException e) {
	    					throw new CfeException(e.getMessage(),e);
	    				}
	            	}
	            }
	        
	        }
		return roles;
	}

	
	
	public BajasOpUsuarioBean obtenerRolUsuario(BajasOpUsuarioBean usuarioParam) throws Exception {
		BajasOpUsuarioBean usuario = new BajasOpUsuarioBean();

		
			Connection conn = null;
		       
	        try {
	            conn = dataSourceDipcel.getConnection();
	            if(!conn.isClosed()){
	                logger.info("Dabase connection OK");
	                String estadoRegistro = "1";
	                if(usuarioParam.getEstadoRegistro() == true)
	                	estadoRegistro = "1";
	                else 
	                	estadoRegistro = "0";
	                String claveEmpleado = usuarioParam.getClaveEmpleado();
					
	                StringBuffer selectSQL = new StringBuffer("select usuario_id, usuario_clave_empleado,  ");
	                selectSQL.append("usuario_clave_universal, usuario_rol_id, usuario_region_id, ");
					selectSQL.append("usuario_nombre, usuario_apaterno, usuario_amaterno, ");
					selectSQL.append(" usuario_usuario_creacion, usuario_fecha_creacion, ");
					selectSQL.append(" usuario_usuario_modificacion, usuario_fecha_modificacion, ");
					selectSQL.append(" usuario_estado_registro, usuario_correo ");
					selectSQL.append(" from bajas_op_usuario ");
	                selectSQL.append(" WHERE usuario_estado_registro = ");
	                selectSQL.append(estadoRegistro);
	                selectSQL.append(" and usuario_clave_empleado = ");
					selectSQL.append(claveEmpleado);
	                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	               
	               
	                ResultSet rs = preparedStatement.executeQuery();
	                
	                while (rs.next()) {
	                    BajasOpRol datRol = new BajasOpRol();
	                    datRol.setId(rs.getInt("usuario_rol_id"));
	                   
	                	usuario.setId(rs.getInt("usuario_id"));
	                	usuario.setNombre(rs.getString("usuario_clave_empleado"));
	                	usuario.setClaveUniversal(rs.getString("usuario_clave_universal"));
	                	usuario.setBajasOpRol(datRol);
	                	usuario.setBajasCatRegion(rs.getString("usuario_region_id"));
	                	usuario.setNombre(rs.getString("usuario_nombre"));
	                	usuario.setApaterno(rs.getString("usuario_apaterno"));
	                	usuario.setAmaterno(rs.getString("usuario_amaterno"));
	                	usuario.setUsuarioCreacion(rs.getInt("usuario_usuario_creacion"));	
	                	usuario.setFechaCreacion(rs.getDate("usuario_fecha_creacion")); 
	                	usuario.setUsuarioModificacion(rs.getInt("usuario_usuario_modificacion")); 
	                	usuario.setFechaModificacion(rs.getDate("usuario_fecha_modificacion")); 
	                }
	            }
	        }  catch (SQLException e) {
	        	throw new CfeException(e.getMessage(),e);
	       
	        }finally{
	            try{
	                if(conn != null){
	                    conn.close();
	                }
	            }catch(SQLException e){
	                throw new Exception("Ocurrio un error al consultar proveedores",e);
	            }catch(Exception e){
	                throw new Exception("Ocurrio un error al consultar proveedores",e);
	            }
	            finally{
	            	if(conn != null){
	            		try {
	    					conn.close();
	    				} catch (SQLException e) {
	    					throw new CfeException(e.getMessage(),e);
	    				}
	            	}
	            }
	        }
	
		
		return usuario;
	}
	
	
	
	public List<BajasOpUsuarioBean> obtenerUsuariosRegistrados(UsuarioBean usuarioParam) throws Exception {
		List<BajasOpUsuarioBean> usuarios= new ArrayList<BajasOpUsuarioBean>();
		Connection conn = null;
	       
        try {
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
                logger.info("Dabase connection OK");
                
				
                StringBuffer selectSQL = new StringBuffer("select us.usuario_id, us.usuario_clave_empleado, us.usuario_clave_universal, us.usuario_rol_id,  ");
                selectSQL.append(" us.usuario_region_id, us.usuario_nombre, us.usuario_apaterno, us.usuario_amaterno, ");
				selectSQL.append(" us.usuario_usuario_creacion, us.usuario_fecha_creacion, ");
				selectSQL.append(" us.usuario_usuario_creacion, us.usuario_fecha_modificacion, us.usuario_estado_registro, ");
				selectSQL.append(" us.usuario_correo,  rol.rol_id, rol.rol_nombre, rol.rol_descripcion, ");
				selectSQL.append(" rol.rol_fecha_creacion, rol.rol_usuario_modificacion, rol.rol_fecha_modificacion, ");
				selectSQL.append(" rol.rol_estado_registro, rol.rol_usuario_creacion, reg.c_cve_region, reg.c_desc_region ");
				selectSQL.append(" from bajas_op_usuario us ");
				selectSQL.append(" left join bajas_op_rol rol on (us.usuario_rol_id = rol.rol_id) ");
				selectSQL.append(" left join cat_regiones reg on (us.usuario_region_id = reg.c_id_region) ");
                selectSQL.append(" where us.usuario_estado_registro = 1 ");
                
                if(usuarioParam != null &&  usuarioParam.getFiltroNumEmpleado() != null && !usuarioParam.getFiltroNumEmpleado().trim().isEmpty()){
				selectSQL.append(" and us.usuario_clave_empleado like ('%");
				selectSQL.append(usuarioParam.getFiltroNumEmpleado());
				selectSQL.append("%') "); 
                }
                
                if(usuarioParam != null &&  usuarioParam.getFiltroNombre() != null &&  !usuarioParam.getFiltroNombre().trim().isEmpty()){
				selectSQL.append("and us.usuario_nombre like ('%");
				selectSQL.append(usuarioParam.getFiltroNombre());
				selectSQL.append("%') "); 
                }
                if(usuarioParam != null &&  usuarioParam.getFiltroApellidoPaterno() != null  && !usuarioParam.getFiltroApellidoPaterno().trim().isEmpty()){
				selectSQL.append("and us.usuario_apaterno like ('%");
				selectSQL.append(usuarioParam.getFiltroApellidoPaterno());
				selectSQL.append("%') "); 
                }
                if(usuarioParam != null &&  usuarioParam.getFiltroApellidoMaterno() != null && !usuarioParam.getFiltroApellidoPaterno().trim().isEmpty()){
				selectSQL.append("and us.usuario_amaterno  like ('%"); 
				selectSQL.append(usuarioParam.getFiltroApellidoMaterno());
				selectSQL.append("%') "); 
                }
             
				logger.info("QUERY USER REGISTERED>>>" + selectSQL);
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
                
                ResultSet rs = preparedStatement.executeQuery();
                BajasOpUsuario usuMod = null;
                BajasOpUsuario usuCrea = null;
                while (rs.next()) {
                	BajasOpUsuarioBean datBean = new BajasOpUsuarioBean();
                	BajasOpRol datRol = new BajasOpRol();
                   
                	datRol.setId(rs.getInt("rol_id"));
                	datRol.setNombre(rs.getString("rol_nombre"));
                	datRol.setDescripcion(rs.getString("rol_descripcion"));
                	datRol.setFechaCreacion(rs.getDate("rol_fecha_creacion"));
                	usuMod= new BajasOpUsuario();
                	usuMod.setId(rs.getInt("rol_usuario_modificacion"));
                	datRol.setUsuarioModificacion(usuMod);
                	datRol.setFechaModificacion(rs.getDate("rol_fecha_modificacion"));
                	datRol.setEstadoRegistro(rs.getInt("rol_estado_registro")== 1 ? true: false); 
                	usuCrea= new BajasOpUsuario();
                	usuCrea.setId(rs.getInt("rol_usuario_creacion"));
                	datRol.setUsuarioCreacion(usuCrea);
                
                    datBean.setId(rs.getInt("usuario_id"));
                    datBean.setClaveEmpleado(rs.getString("usuario_clave_empleado"));
                    datBean.setClaveUniversal(rs.getString("usuario_clave_universal"));
                    datBean.setBajasOpRol(datRol);
                    datBean.setBajasCatRegion(rs.getString("usuario_region_id"));
                    datBean.setNombre(rs.getString("usuario_nombre"));
                    datBean.setApaterno(rs.getString("usuario_apaterno"));
                    datBean.setAmaterno(rs.getString("usuario_amaterno"));
					datBean.setUsuarioCreacion(rs.getInt("usuario_usuario_creacion"));	
					datBean.setFechaCreacion(rs.getDate("usuario_fecha_creacion"));  
					datBean.setFechaModificacion(rs.getDate("usuario_fecha_modificacion")); 
					
					datBean.setBajasRegion(new BajasCatRegion());
					datBean.getBajasRegion().setId(rs.getInt("usuario_region_id"));
					Map<Integer, BajasCatRegion> mapregiones = new HashMap<Integer, BajasCatRegion>();
					for(BajasCatRegion bcr: usuarioParam.getRegiones()){
						mapregiones.put(bcr.getId(), bcr);
					}
					datBean.getBajasRegion().setClave(mapregiones.get(datBean.getBajasRegion().getId()).getClave());
					datBean.getBajasRegion().setDescripcion(mapregiones.get(datBean.getBajasRegion().getId()).getDescripcion());
					usuarios.add(datBean);
                }
            }
        }  catch (SQLException e) {
        	throw new CfeException(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                throw new Exception("Ocurrio un error al consultar proveedores",e);
            }catch(Exception e){
                throw new Exception("Ocurrio un error al consultar proveedores",e);
            }
            finally{
            	if(conn != null){
            		try {
    					conn.close();
    				} catch (SQLException e) {
    					throw new CfeException(e.getMessage(),e);
    				}
            	}
            }
        }
		return usuarios;
	}
	

	
	public List<BajasOpUsuarioBean> obtenerUsuariosRegistradosProcesoBaja(Boolean estatus) throws Exception {
		List<BajasOpUsuarioBean> usuarios= null;
		Connection conn = null;
	       
        try {
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
                logger.info("Dabase connection OK");
                
				
                StringBuffer selectSQL = new StringBuffer("select us.usuario_id, us.usuario_clave_empleado, us.usuario_clave_universal, us.usuario_rol_id,  ");
                selectSQL.append(" us.usuario_region_id, us.usuario_nombre, us.usuario_apaterno, us.usuario_amaterno, ");
				selectSQL.append(" us.usuario_usuario_creacion, us.usuario_fecha_creacion, ");
				selectSQL.append("  us.usuario_usuario_creacion, us.usuario_fecha_modificacion, us.usuario_estado_registro, ");
				selectSQL.append("  us.usuario_correo,  rol.rol_id, rol.rol_nombre, rol.rol_descripcion,");
				selectSQL.append("  rol.rol_fecha_creacion, rol.rol_usuario_modificacion, rol.rol_fecha_modificacion,");
				selectSQL.append("  rol.rol_estado_registro, rol.rol_usuario_creacion, reg.c_cve_region, reg.c_desc_region ");
				selectSQL.append("  from bajas_op_usuario us ");
				selectSQL.append("  left join bajas_op_rol rol on (us.usuario_rol_id = rol.rol_id) ");
				selectSQL.append(" left join cat_regiones reg on (us.usuario_region_id = reg.c_cve_region) ");
                selectSQL.append(" where us.usuario_estado_registro = 1");
				
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
               
                ResultSet rs = preparedStatement.executeQuery();
                
                while (rs.next()) {
                	BajasOpUsuarioBean datBean = new BajasOpUsuarioBean();
                	BajasOpRol datRol = new BajasOpRol();
                   
                	datRol.setId(rs.getInt("rol.rol_id"));
                	datRol.setNombre(rs.getString("rol.rol_nombre"));
                	datRol.setDescripcion(rs.getString("rol.rol_descripcion"));
                	datRol.setFechaCreacion(rs.getDate("rol.rol_fecha_creacion"));
//                	datRol.setUsuarioModificacion(rs.getInt("rol.rol_usuario_modificacion"));
                	datRol.setFechaModificacion(rs.getDate("rol.rol_fecha_modificacion"));
                	datRol.setEstadoRegistro(rs.getInt("rol.rol_estado_registro") ==1 ? true: false); 
//                	datRol.setUsuarioCreacion(rs.getInt("rol.rol_usuario_creacion"));
                
                    datBean.setId(rs.getInt("usuario_id"));
                    datBean.setNombre(rs.getString("usuario_clave_empleado"));
                    datBean.setClaveUniversal(rs.getString("usuario_clave_universal"));
                    datBean.setBajasOpRol(datRol);
                    datBean.setBajasCatRegion(rs.getString("usuario_region_id"));
                    datBean.setNombre(rs.getString("usuario_nombre"));
                    datBean.setApaterno(rs.getString("usuario_apaterno"));
                    datBean.setAmaterno(rs.getString("usuario_amaterno"));
					datBean.setUsuarioCreacion(rs.getInt("usuario_usuario_creacion"));	
					datBean.setFechaCreacion(rs.getDate("usuario_fecha_creacion")); 
					datBean.setUsuarioModificacion(rs.getInt("usuario_usuario_modificacion")); 
					datBean.setFechaModificacion(rs.getDate("usuario_fecha_modificacion")); 
                }
            }
        }  catch (SQLException e) {
        	throw new CfeException(e.getMessage(),e);
        }finally{
            	if(conn != null){
            		try {
    					conn.close();
    				} catch (SQLException e) {
    					throw new CfeException(e.getMessage(),e);
    				}
            	}
            }
        
		return usuarios;
	}
	

	public BajasOpUsuarioBean obtenerUsuarioRegistrado(BajasOpUsuarioBean usuarioParam) {
		BajasOpUsuarioBean usuario= null;
		Connection conn = null;
		try {
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
                logger.info("Dabase connection OK");
                
				
                StringBuffer selectSQL = new StringBuffer("select us.usuario_id, us.usuario_clave_empleado, us.usuario_clave_universal, us.usuario_rol_id,  ");
                selectSQL.append(" us.usuario_region_id, us.usuario_nombre, us.usuario_apaterno, us.usuario_amaterno, ");
				selectSQL.append(" us.usuario_usuario_creacion, us.usuario_fecha_creacion, ");
				selectSQL.append("  us.usuario_usuario_creacion, us.usuario_fecha_modificacion, us.usuario_estado_registro, ");
				selectSQL.append("  us.usuario_correo,  rol.rol_id, rol.rol_nombre, rol.rol_descripcion,");
				selectSQL.append("  rol.rol_fecha_creacion, rol.rol_usuario_modificacion, rol.rol_fecha_modificacion,");
				selectSQL.append("  rol.rol_estado_registro, rol.rol_usuario_creacion, reg.c_cve_region, reg.c_desc_region ");
				selectSQL.append("  from bajas_op_usuario us ");
				selectSQL.append("  left join bajas_op_rol rol on (us.usuario_rol_id = rol.rol_id) ");
				selectSQL.append(" left join cat_regiones reg on (us.usuario_region_id = reg.c_id_region) ");
                selectSQL.append(" where us.usuario_estado_registro = 1");
				selectSQL.append(" and us.usuario_clave_empleado like ('");
				selectSQL.append(usuarioParam.getClaveEmpleado());
				selectSQL.append("')");
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
               
                ResultSet rs = preparedStatement.executeQuery();
                BajasOpUsuario usuCrea = null;
                BajasOpUsuario usuMod = null;
                while (rs.next()) {
                	BajasOpUsuarioBean datBean = new BajasOpUsuarioBean();
                	BajasOpRol datRol = new BajasOpRol();
                   
                	datRol.setId(rs.getInt("rol.rol_id"));
                	datRol.setNombre(rs.getString("rol.rol_nombre"));
                	datRol.setDescripcion(rs.getString("rol.rol_descripcion"));
                	datRol.setFechaCreacion(rs.getDate("rol.rol_fecha_creacion"));
                	usuMod= new BajasOpUsuario();
                	usuMod.setId(rs.getInt("rol_usuario_modificacion"));
                	datRol.setUsuarioModificacion(usuMod);
                	datRol.setFechaModificacion(rs.getDate("rol.rol_fecha_modificacion"));
                	datRol.setEstadoRegistro(rs.getInt("rol.rol_estado_registro") == 1 ? true: false); 
                	usuCrea= new BajasOpUsuario();
                	usuCrea.setId(rs.getInt("rol_usuario_creacion"));
                	datRol.setUsuarioCreacion(usuCrea);
                
                    datBean.setId(rs.getInt("usuario_id"));
                    datBean.setNombre(rs.getString("usuario_clave_empleado"));
                    datBean.setClaveUniversal(rs.getString("usuario_clave_universal"));
                    datBean.setBajasOpRol(datRol);
                    datBean.setBajasCatRegion(rs.getString("usuario_region_id"));
                    datBean.setNombre(rs.getString("usuario_nombre"));
                    datBean.setApaterno(rs.getString("usuario_apaterno"));
                    datBean.setAmaterno(rs.getString("usuario_amaterno"));
					datBean.setUsuarioCreacion(rs.getInt("usuario_usuario_creacion"));	
					datBean.setFechaCreacion(rs.getDate("usuario_fecha_creacion")); 
					datBean.setUsuarioModificacion(rs.getInt("usuario_usuario_modificacion")); 
					datBean.setFechaModificacion(rs.getDate("usuario_fecha_modificacion")); 
                }
            }
        }  catch (SQLException e) {
        	throw new CfeException(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }catch(Exception e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }
            finally{
            	if(conn != null){
            		try {
    					conn.close();
    				} catch (SQLException e) {
    					throw new CfeException(e.getMessage(),e);
    				}
            	}
            }
        }
		return usuario;
	}
	

	
	public CatRegionBean obtieneRegionUsuario(CatRegionBean regionParam) {
		CatRegionBean region = null;
		Connection conn = null;
		try{
			
			conn = dataSourceDipcel.getConnection();
			
            if(!conn.isClosed()){
				StringBuffer selectSQL = new StringBuffer("select c_cve_region, c_desc_region  ");
	            selectSQL.append(" from cat_regiones ");
				
				if (regionParam.getId()  != null || regionParam.getDescripcion() != null)
					selectSQL.append(" where  ");
				if(regionParam.getId()  != null){
					selectSQL.append(" c_cve_region = " + regionParam.getId());
				}else if(regionParam.getDescripcion() != null && regionParam.getId()  != null){
					selectSQL.append(" c_desc_region = " + regionParam.getDescripcion());	
				}
				
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	                
	               
	            	region.setId(rs.getString("c_cve_region"));
	            	region.setDescripcion(rs.getString("c_desc_region"));
	                
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
		finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return region;
	}



//	@Override
	public List<BajasCatParametrosBean> getParams(List<String> params) throws CfeException {
		List<BajasCatParametrosBean> parametros = new ArrayList<BajasCatParametrosBean>();
		Connection conn = null;
	       
        try {
        	String nombre = "";
        	int i = 0;
        	for (String dta : params) {
        		if (nombre.isEmpty())
        		{
        			nombre = "'" + dta + "'";
        		}
        		else{
        			nombre += "," + "'" + dta + "'";
        		}
        		
			}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	
            	StringBuffer selectSQL = new StringBuffer("select parametro_id, parametro_nombre, "
            			+ "parametro_descripcion, parametro_valor, "
            			+ "parametro_fecha_inicio, parametro_fecha_fin, parametro_unidad,"
            			+ " parametro_usuario_creacion, parametro_fecha_creacion, parametro_usuario_modificacion,"
            			+ "  parametro_fecha_modificacion, parametro_estadoregistro");
                selectSQL.append("  from baja_cat_parametro ");
                selectSQL.append("  where parametro_estadoregistro =1 "
                		+ "and parametro_nombre in ( ");
                selectSQL.append(nombre + ")");
                logger.info("PARAMETROS: " + selectSQL);
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasCatParametrosBean param = new BajasCatParametrosBean();
	            	param.setId(rs.getInt("parametro_id"));
	            	param.setNombre(rs.getString("parametro_nombre"));
	            	param.setDescripcion(rs.getString("parametro_descripcion"));
	            	param.setValor(rs.getString("parametro_valor"));
	            	param.setFechaInicio(rs.getDate("parametro_fecha_inicio"));
	            	param.setFechaFin(rs.getDate("parametro_fecha_fin"));
	            	param.setUnidad(rs.getString("parametro_unidad"));
	            	param.setUsuarioCreacion(rs.getInt("parametro_usuario_creacion"));
	            	param.setFechaCreacion(rs.getDate("parametro_fecha_creacion"));
	            	param.setUsuarioModificacion(rs.getInt("parametro_usuario_modificacion"));
	            	param.setFechaModificacion(rs.getDate("parametro_fecha_modificacion"));
	            	Boolean estReg = true;
	            	if (rs.getInt("parametro_estadoregistro") == 1)
	            		estReg = true;
	            	else
	            		estReg = false;
	            	param.setEstadoRegistro(estReg);
	            	
	            	parametros.add(param);
	                
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return parametros;
	}

//	@Override
	public List<BajasCatParametrosBean> obtenerParametros(List<String> params) throws CfeException {
		List<BajasCatParametrosBean> parametros = new ArrayList<BajasCatParametrosBean>();
		
		Connection conn = null;
	       
        try {
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
                logger.info("Dabase connection OK");
				
                StringBuffer selectSQL = new StringBuffer("select parametro_id, parametro_nombre, "
                		+ "parametro_descripcion, parametro_valor,  ");
                selectSQL.append(" parametro_fecha_inicio, parametro_fecha_fin, parametro_unidad, parametro_usuario_creacion,  ");
				selectSQL.append(" parametro_fecha_creacion, parametro_usuario_modificacion, parametro_fecha_modificacion, ");
				selectSQL.append("  parametro_estadoregistro ");
				selectSQL.append("  from baja_cat_parametro");
				selectSQL.append("  parametro_estadoregistro = 1");
			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
               
               
                ResultSet rs = preparedStatement.executeQuery();
                
                while (rs.next()) {
                	BajasCatParametrosBean datBean = new BajasCatParametrosBean();
                   
                    datBean.setId(rs.getInt("parametro_id"));
                    datBean.setNombre(rs.getString("parametro_nombre"));
                    datBean.setValor(rs.getString("parametro_valor"));
                    datBean.setFechaInicio(rs.getDate("parametro_fecha_inicio"));
                    datBean.setFechaFin(rs.getDate("parametro_fecha_fin"));
                    datBean.setUnidad(rs.getString("parametro_unidad"));
                    datBean.setUsuarioCreacion(rs.getInt("parametro_usuario_creacion"));
                    datBean.setFechaCreacion(rs.getDate("parametro_fecha_creacion"));
					datBean.setUsuarioModificacion(rs.getInt("parametro_usuario_modificacion"));	
					datBean.setFechaModificacion(rs.getDate("parametro_fecha_modificacion")); 
					datBean.setUsuarioModificacion(rs.getInt("parametro_estadoregistro")); 
					parametros.add(datBean);
                }
            }
        }  catch (SQLException e) {
        	throw new CfeException(e.getMessage(),e);
       
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }catch(Exception e){
                try {
					throw new Exception("Ocurrio un error al consultar proveedores",e);
				} catch (Exception e1) {
					throw new CfeException(e.getMessage(),e);
				}
            }
        }
		
		return null;
	}



	@Override
	public List<BajasCalculoBean> getCalculos() throws CfeException {
		
		List<BajasCalculoBean> calculos = new ArrayList<BajasCalculoBean>();
		Connection conn = null;
	       
        try {
        	String nombre = "";
        	int i = 0;
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	
            			
            			
            	StringBuffer selectSQL = new StringBuffer("select calc_id, calc_clave, calc_descripcion, calc_fecha_creacion, calc_usuario_creacion, "
            			+ "calc_fecha_modificacion, calc_usuario_modificacion, calc_estado_registro "
            			+ "from bajas_cat_calculo "
            			+ "where calc_estado_registro = 1 ");
            			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasCalculoBean baja = new BajasCalculoBean();
	            	baja.setId(rs.getInt("calc_id"));
	            	baja.setClave(rs.getString("calc_clave"));
	            	baja.setDescripcion(rs.getString("calc_descripcion"));
	            	baja.setFechaCreacion(rs.getDate("calc_fecha_creacion"));
	            	baja.setUsuarioCreacion(rs.getInt("calc_usuario_creacion"));
	            	baja.setFechaModificacion(rs.getDate("calc_fecha_modificacion"));
	            	baja.setUsuarioModificacion(rs.getInt("calc_usuario_modificacion"));
	            	baja.setEstadoRegistro(rs.getInt("calc_estado_registro"));
	            	
	            	calculos.add(baja);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return calculos;
	}
	
	
	
	
	@Override
	public List<BajasClaseBean> getClasesGen(List<String> region, BajasCalculoBean calculo) throws CfeException {
		
		List<BajasClaseBean> clases = new ArrayList<BajasClaseBean>();
		Connection conn = null;
		String filterRegion = null;
		
        try {
        	if(!region.isEmpty()){
        		filterRegion = joinArray(region.toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct TRIM(clase) clase,TRIM(clase_dsc) clase_dsc from v_bajas_nvo ");
            	
            			if(calculo.getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(calculo.getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(calculo.getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(calculo.getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            			
            			if(region != null  && region.size() > 0){
//                    		adqreg.region.id
                    		selectSQL.append("AND region IN (" + filterRegion + ") ");
                    	}
            			selectSQL.append("order by 1 asc ");
            			logger.info(">>> SQL GETCLASSES: " + selectSQL);         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasClaseBean clase = new BajasClaseBean();
	            	clase.setClave(rs.getString("clase"));
	            	clase.setDescripcion(rs.getString("clase")+ ": " + rs.getString("clase_dsc"));
	            	clases.add(clase);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return clases;
	}
	
	
	@Override
	public List<BajasClaseBean> getClases(AdqBajasBean bajasRegBean) throws CfeException {
		
		List<BajasClaseBean> clases = new ArrayList<BajasClaseBean>();
		Connection conn = null;
		String filterRegion = null;
		
        try {
        	if(!bajasRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(bajasRegBean.getRegion().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct TRIM(clase) clase,TRIM(clase_dsc) clase_dsc from v_bajas_nvo ");
            	
            			if(bajasRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(bajasRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(bajasRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(bajasRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            			
            			if(bajasRegBean.getRegion() != null  && bajasRegBean.getRegion().size() > 0){
//                    		adqreg.region.id
                    		selectSQL.append("AND region IN (" + filterRegion + ") ");
                    	}
            			selectSQL.append("order by 1 asc ");
            			logger.info(">>> SQL GETCLASSES: " + selectSQL);         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasClaseBean clase = new BajasClaseBean();
	            	clase.setClave(rs.getString("clase"));
	            	clase.setDescripcion(rs.getString("clase")+ ": " + rs.getString("clase_dsc"));
	            	clases.add(clase);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return clases;
	}
	
	@Override
	public List<BajasClaseBean> getClases(DepreActBean depreRegBean) throws CfeException {
		
		List<BajasClaseBean> clases = new ArrayList<BajasClaseBean>();
		Connection conn = null;
		String filterRegion = null;
		
        try {
        	if(!depreRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(depreRegBean.getRegion().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct TRIM(clase) clase,TRIM(clase_dsc) clase_dsc from v_bajas_nvo ");
            	
            			if(depreRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(depreRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(depreRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(depreRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            			
            			if(depreRegBean.getRegion() != null  && depreRegBean.getRegion().size() > 0){
                    		selectSQL.append("AND region IN (" + filterRegion + ") ");
                    	}
            			selectSQL.append("order by 1 asc ");
            			logger.info(">>> SQL GETCLASSES: " + selectSQL);         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasClaseBean clase = new BajasClaseBean();
	            	
	            	clase.setClave(rs.getString("clase"));
	            	clase.setDescripcion(rs.getString("clase")+ ": " + rs.getString("clase_dsc"));
	            	
	            	
	            	clases.add(clase);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return clases;
	}

	@Override
	public List<BajasClaseBean> getClasesCosto(CostoBean costoRegBean) throws CfeException {
		
		List<BajasClaseBean> clases = new ArrayList<BajasClaseBean>();
		Connection conn = null;
		String filterRegion = null;
		
        try {
        	if(!costoRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(costoRegBean.getRegion().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct TRIM(clase) clase,TRIM(clase_dsc) clase_dsc from v_bajas_nvo ");
            	
            			if(costoRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(costoRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(costoRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(costoRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            			
            			if(costoRegBean.getRegion() != null  && costoRegBean.getRegion().size() > 0){
//                    		adqreg.region.id
                    		selectSQL.append("AND region IN (" + filterRegion + ") ");
                    	}
            			selectSQL.append("order by 1 asc ");
            			logger.info(">>> SQL GETCLASSES: " + selectSQL);         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	BajasClaseBean clase = new BajasClaseBean();
	            	clase.setClave(rs.getString("clase"));
	            	clase.setDescripcion(rs.getString("clase")+ ": " + rs.getString("clase_dsc"));
	            	clases.add(clase);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return clases;
	}

	
	@Override
	public List<String> getTxtGen(List<String> region, BajasCalculoBean calculo, List<String> clase, String texto) throws CfeException {
		List<String> textos = new ArrayList<String>();
		Connection conn = null;
		String filterRegion = null;
		String filterClase = null;
		
        try {
        	if(!region.isEmpty()){
        		filterRegion = joinArray(region.toArray(), "'", ",", "'");
        	}
        	if(!clase.isEmpty()){
    		filterClase = joinArray(clase.toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct texto_baja from v_bajas_nvo ");
            		
            	if(calculo.getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(calculo.getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(calculo.getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(calculo.getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            	
            	if(region != null && region.size() > 0){
//            		adqreg.region.id
            		selectSQL.append("AND region IN (" + filterRegion + ") ");
            	}
        		if(clase != null && clase != null && clase.size() > 0){
        			selectSQL.append("AND clase IN (" + filterClase + ") ");
        		}
        		
        		if("NO FISCALES".equalsIgnoreCase(texto)){
        			selectSQL.append("AND texto_baja NOT IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}else if("FISCALES".equalsIgnoreCase(texto)){
        			selectSQL.append("AND texto_baja IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}
        		
            	selectSQL.append("order by 1 asc ");
            	logger.info(">>> SQL GETTXT: " + selectSQL); 		         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	String txt = new String();
	            	
	            	
	            	txt = rs.getString("texto_baja");
	            	
	            	
	            	textos.add(txt);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return textos;
	}
	
	
	@Override
	public List<String> getTxt(AdqBajasBean bajasRegBean) throws CfeException {
		List<String> textos = new ArrayList<String>();
		Connection conn = null;
		String filterRegion = null;
		String filterClase = null;
		
        try {
        	if(!bajasRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(bajasRegBean.getRegion().toArray(), "'", ",", "'");
        	}
        	if(!bajasRegBean.getClase().isEmpty()){
    		filterClase = joinArray(bajasRegBean.getClase().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct texto_baja from v_bajas_nvo ");
            		
            	if(bajasRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(bajasRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(bajasRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(bajasRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            	
            	if(bajasRegBean.getRegion() != null && bajasRegBean.getRegion().size() > 0){
//            		adqreg.region.id
            		selectSQL.append("AND region IN (" + filterRegion + ") ");
            	}
        		if(bajasRegBean.getClase() != null && bajasRegBean.getClase() != null && bajasRegBean.getClase().size() > 0){
        			selectSQL.append("AND clase IN (" + filterClase + ") ");
        		}
        		
        		if("NO FISCALES".equalsIgnoreCase(bajasRegBean.getTexto())){
        			selectSQL.append("AND texto_baja NOT IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}else if("FISCALES".equalsIgnoreCase(bajasRegBean.getTexto())){
        			selectSQL.append("AND texto_baja IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}
        		
            	selectSQL.append("order by 1 asc ");
            	logger.info(">>> SQL GETTXT: " + selectSQL); 		         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	String texto = new String();
	            	
	            	
	            	texto = rs.getString("texto_baja");
	            	
	            	
	            	textos.add(texto);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return textos;
	}
	
	@Override
	public List<String> getTxtCosto(CostoBean costoRegBean) throws CfeException {
		List<String> textos = new ArrayList<String>();
		Connection conn = null;
		String filterRegion = null;
		String filterClase = null;
		
        try {
        	if(!costoRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(costoRegBean.getRegion().toArray(), "'", ",", "'");
        	}
        	if(!costoRegBean.getClase().isEmpty()){
    		filterClase = joinArray(costoRegBean.getClase().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct texto_baja from v_bajas_nvo ");
            		
            	if(costoRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(costoRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(costoRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(costoRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            	
            	if(costoRegBean.getRegion() != null && costoRegBean.getRegion().size() > 0){
            		selectSQL.append("AND region IN (" + filterRegion + ") ");
            	}
        		if(costoRegBean.getClase() != null && costoRegBean.getClase() != null && costoRegBean.getClase().size() > 0){
        			selectSQL.append("AND clase IN (" + filterClase + ") ");
        		}
        		
        		if("NO FISCALES".equalsIgnoreCase(costoRegBean.getTexto())){
        			selectSQL.append("AND texto_baja NOT IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}else if("FISCALES".equalsIgnoreCase(costoRegBean.getTexto())){
        			selectSQL.append("AND texto_baja IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}
        		
            	selectSQL.append("order by 1 asc ");
            	logger.info(">>> SQL GETTXT: " + selectSQL); 		         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	String texto = new String();
	            	
	            	
	            	texto = rs.getString("texto_baja");
	            	
	            	
	            	textos.add(texto);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return textos;
	}

	
	
	@Override
	public List<String> getTxt(DepreActBean depreRegBean) throws CfeException {
		List<String> textos = new ArrayList<String>();
		Connection conn = null;
		String filterRegion = null;
		String filterClase = null;
		
        try {
        	if(!depreRegBean.getRegion().isEmpty()){
        		filterRegion = joinArray(depreRegBean.getRegion().toArray(), "'", ",", "'");
        	}
        	if(!depreRegBean.getClase().isEmpty()){
    		filterClase = joinArray(depreRegBean.getClase().toArray(), "'", ",", "'");
        	}
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select distinct texto_baja from v_bajas_nvo ");
            		
            	if(depreRegBean.getCalculo().getId() == 1){
            				
            				selectSQL.append("where d_inv='3' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(depreRegBean.getCalculo().getId() == 2){
            			
            				selectSQL.append("where d_inv='2' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}else if(depreRegBean.getCalculo().getId() == 3){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='15' ");
            			}else if(depreRegBean.getCalculo().getId() == 4){
            				selectSQL.append("where d_inv='1' ");
            				selectSQL.append("AND ");
            				selectSQL.append("area_val='10' ");
            			}
            	
            	if(depreRegBean.getRegion() != null && depreRegBean.getRegion().size() > 0){
//            		adqreg.region.id
            		selectSQL.append("AND region IN (" + filterRegion + ") ");
            	}
        		if(depreRegBean.getClase() != null && depreRegBean.getClase() != null && depreRegBean.getClase().size() > 0){
        			selectSQL.append("AND clase IN (" + filterClase + ") ");
        		}
        		
        		if("NO FISCALES".equalsIgnoreCase(depreRegBean.getTexto())){
        			selectSQL.append("AND texto_baja NOT IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}else if("FISCALES".equalsIgnoreCase(depreRegBean.getTexto())){
        			selectSQL.append("AND texto_baja IN (select valor from t_parametro where nombre_param matches 'TEXTO FISCAL*') ");
        		}
        		
            	selectSQL.append("order by 1 asc ");
            	logger.info(">>> SQL GETTXT: " + selectSQL); 		         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	String texto = new String();
	            	
	            	
	            	texto = rs.getString("texto_baja");
	            	
	            	
	            	textos.add(texto);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}finally{
	        	if(conn != null){
	        		try {
						conn.close();
					} catch (SQLException e) {
						throw new CfeException(e.getMessage(),e);
					}
	        	}
	        }
		return textos;
	}
	
	@Override
	public List<BajasAjustesBean> getAjustes() throws CfeException {
		List<BajasAjustesBean> ajustes = new ArrayList<BajasAjustesBean>();
		Connection conn = null;
		BajasAjustesBean ajuste = null;
	       
        try {
            conn = dataSourceDipcel.getConnection();
            if(!conn.isClosed()){
            	
            	StringBuffer selectSQL = new StringBuffer("select ajuste_id, ajuste_clave, ajuste_descripcion, ajuste_fecha_creacion, ajuste_usuario_creacion, ");
            				selectSQL.append("ajuste_fecha_modificacion, ajuste_usuario_modificacion, ajuste_estado_registro  ");
            				selectSQL.append("from bajas_cat_ajuste ");
            				selectSQL.append("where ajuste_estado_registro = 1 ");
            				selectSQL.append("order by ajuste_id ");
            	
            	logger.info(selectSQL.toString());		         			
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL.toString());
	            
	            ResultSet rs = preparedStatement.executeQuery();
	            
	            while (rs.next()) {
	            	ajuste = new BajasAjustesBean();
	            	ajuste.setId(rs.getInt("ajuste_id"));
	            	ajuste.setClave(rs.getString("ajuste_clave"));
	            	ajuste.setDescripcion(rs.getString("ajuste_descripcion"));
	            	ajuste.setFechaCreacion(rs.getDate("ajuste_fecha_creacion"));
	            	ajuste.setUsuarioCreacion(rs.getInt("ajuste_usuario_creacion"));
	            	ajuste.setFechaModificacion(rs.getDate("ajuste_fecha_modificacion"));
	            	ajuste.setUsuarioModificacion(rs.getInt("ajuste_usuario_modificacion"));
	            	ajuste.setEstadoRegistro(rs.getInt("ajuste_estado_registro"));
	            	ajustes.add(ajuste);
	            }
            }
		}catch(Exception e){
			throw new CfeException(e.getMessage(), e);
		}
        finally{
        	if(conn != null){
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new CfeException(e.getMessage(),e);
				}
        	}
        }
		return ajustes;
	}

	
	public String joinArray(Object[] arr,String prefix,String strJoin,String sufix){
		 return joinCollection((arr!=null&&arr.length>0)?Arrays.asList(arr):null, prefix, strJoin, sufix);	
		}
		
	public String joinCollection(Iterable<? extends Object> iterable,String prefix,String strJoin,String sufix)
	{	StringBuffer strB = new StringBuffer();
	if(iterable != null){
		Iterator<? extends Object> iterator = iterable.iterator();
	
		while(iterator.hasNext())
		{
			Object obj = iterator.next();
			String strObj = obj.toString().trim();
			if(strObj.length()==0)
				continue;
						
			if(prefix!=null)
			{ strB.append(prefix); }
			
			strB.append(obj);
			
			if(sufix!=null)
			{ strB.append(sufix); }
			
			if(strB.length()>0 && iterator.hasNext())
			{ strB.append(strJoin); }
		}
		
	}
		return safeStringVal(strB.toString());
	}
		
		
		
		public String safeStringVal(String str,String def)
		{
			if(str==null||str.trim().length()==0)
				return def;
			
			return str.trim();
		}

		public String safeStringVal(String str)
		{ 
			return safeStringVal(str,null); 
		}
		@Override
		public String getFilterText(String txtType, List<String> txtDesc, List<String> txtsDesc){
			String filter = null;
			if("TODOS".equalsIgnoreCase(txtType) && !txtDesc.isEmpty()){
	    		filter= joinArray(txtDesc.toArray(), "'", ",", "'");
//	    		datos.setTextosTitulos(filterTxt);//los textos que se selccionaron y se muestran en los reportes
	        }else if (("FISCALES".equalsIgnoreCase(txtType) || "NO FISCALES".equalsIgnoreCase(txtType)) && txtDesc.isEmpty()){
	        	filter = joinArray(txtsDesc.toArray(), "'", ",", "'");
	        }else if(("FISCALES".equalsIgnoreCase(txtType) || "NO FISCALES".equalsIgnoreCase(txtType)) && !txtDesc.isEmpty()){
	        	filter = joinArray(txtDesc.toArray(), "'", ",", "'");
	        }
			return filter;
		}
		@Override
		public MesBean perBajaToObject(Integer number) {
			MesBean mesBean = new MesBean();
			switch (number) {
			case 1: {
				mesBean.setId(number);
				mesBean.setNombre("ENE");
			}
				break;
			case 2: {
				mesBean.setId(number);
				mesBean.setNombre("FEB");
			}
				break;
			case 3: {
				mesBean.setId(number);
				mesBean.setNombre("MZO");
			}
				break;
			case 4: {
				mesBean.setId(number);
				mesBean.setNombre("ABR");
			}
				break;
			case 5: {
				mesBean.setId(number);
				mesBean.setNombre("MAY");
			}
				break;
			case 6: {
				mesBean.setId(number);
				mesBean.setNombre("JUN");
			}
				break;
			case 7: {
				mesBean.setId(number);
				mesBean.setNombre("JUL");
			}
				break;
			case 8: {
				mesBean.setId(number);
				mesBean.setNombre("AGO");
			}
				break;
			case 9: {
				mesBean.setId(number);
				mesBean.setNombre("SEP");
			}
				break;
			case 10: {
				mesBean.setId(number);
				mesBean.setNombre("OCT");
			}
				break;
			case 11: {
				mesBean.setId(number);
				mesBean.setNombre("NOV");
			}
				break;
			case 12: {
				mesBean.setId(number);
				mesBean.setNombre("DIC");
			}
				break;
			default: {
				mesBean.setId(0);
				mesBean.setNombre("");
			}
			}
			return mesBean;
		}


		@Override
		public TotalBean totalesMes(ReporteBean datos, TotalBean datMes) throws CfeException {
			switch (datos.getPerBaja())
			{
			case Constants.UNO:
				datMes.setEnero(datMes.getEnero().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.DOS:
				datMes.setFebrero(datMes.getFebrero().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.TRES:
				datMes.setMarzo(datMes.getMarzo().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.CUATRO:
				datMes.setAbril(datMes.getAbril().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.CINCO:
				datMes.setMayo(datMes.getMayo().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.SEIS:
				datMes.setJunio(datMes.getJunio().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.SIETE:
				datMes.setJulio(datMes.getJulio().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.OCHO:
				datMes.setAgosto(datMes.getAgosto().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.NUEVE:
				datMes.setSeptiembre(datMes.getSeptiembre().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.DIEZ:
				datMes.setOctubre(datMes.getOctubre().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.ONCE:
				datMes.setNoviembre(datMes.getNoviembre().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
			case Constants.DOCE:
				datMes.setDiciembre(datMes.getDiciembre().add(datos.getDeduccion()!=null?datos.getDeduccion():BigDecimal.ZERO));
				break;
				default:
					throw new CfeException("Fallo la suma de meses", null);
					
			}

			datMes.setTotal(datMes.getEnero().add(datMes.getFebrero().add(datMes.getMarzo().add(datMes.getAbril().add(datMes.getMayo().add(datMes.getJunio().add(datMes.getJulio().add(datMes.getAgosto().add(datMes.getSeptiembre().add(datMes.getOctubre().add(datMes.getNoviembre().add(datMes.getDiciembre()))))))))))));
			return datMes;
		
		}
		
		public StringBuilder buildInClause(List<String> possibleValue) throws CfeException{
			StringBuilder builder = new StringBuilder();

			for( int i = 0 ; i < possibleValue.size(); i++ ) {
			    builder.append("?,");
			}
			return builder;
		}
		
		public String joinArray(Object[] arr,String strJoin){
			 return joinCollection((arr!=null&&arr.length>0)?Arrays.asList(arr):null, strJoin);	
			}
			
			public String joinCollection(Iterable<? extends Object> iterable,String strJoin)
			{	StringBuffer strB = new StringBuffer();

				Iterator<? extends Object> iterator = iterable.iterator();
				while(iterator.hasNext())
				{
					Object obj = iterator.next();
					String strObj = obj.toString().trim();
					if(strObj.length()==0)
						continue;
					
					strB.append(obj);
					
					if(strB.length()>0 && iterator.hasNext())
					{ strB.append(strJoin); }
				}
				
				return safeStringVal(strB.toString());
			}
			
			public TotalBean inicializaTotales (TotalBean datMesRep ){
				datMesRep.setEnero(new BigDecimal("0.00"));
				datMesRep.setFebrero(new BigDecimal("0.00"));
				datMesRep.setMarzo(new BigDecimal("0.00"));
				datMesRep.setAbril(new BigDecimal("0.00"));
				datMesRep.setMayo(new BigDecimal("0.00"));
				datMesRep.setJunio(new BigDecimal("0.00"));
				datMesRep.setJulio(new BigDecimal("0.00"));
				datMesRep.setAgosto(new BigDecimal("0.00"));
				datMesRep.setSeptiembre(new BigDecimal("0.00"));
				datMesRep.setOctubre(new BigDecimal("0.00"));
				datMesRep.setNoviembre(new BigDecimal("0.00"));
				datMesRep.setDiciembre(new BigDecimal("0.00"));
				datMesRep.setTotal(new BigDecimal("0.00"));
				return datMesRep;
			}
}
