package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.RolBean;
import com.telcel.gsa.dsaf.bean.UsuarioBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.entity.BajasOpRol;
import com.telcel.gsa.dsaf.entity.BajasOpUsuario;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.CargaArchivoService;
import com.telcel.gsa.dsaf.service.CienDepreciadosService;
import com.telcel.gsa.dsaf.service.UsuarioService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.Predicate;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Service("cargaBajasAction")
@ViewScoped
public class CargaInsumosBajasAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5347656222555039386L;
	private static Logger logger = LoggerFactory.getLogger(CargaInsumosBajasAction.class);
	
	
	
	@Autowired
	CargaArchivoService cargaArchivoService;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	@Autowired
	private SessionScopeUser sessionScopeUser;
	

	
	public void initFlow(RequestContext ctx) {
		logger.info(Constants.APP_ID + "Comienza Pantalla");
		BajasDepreciadosCienBean datBean = new BajasDepreciadosCienBean();
		datBean.setGuardar(false);
		datBean.setCargacumulado(false);
		ctx.getFlowScope().put("carga", datBean);
	}
	

	public void realizarCarga(RequestContext ctx) throws IOException, SQLException {
		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("carga");
		logger.info("Entrando al metodo ehecutar SP");
		datos.setGuardar(true);
		try
		{
			cargaArchivoService.executeSp(datos, sessionScopeUser);
		}catch(CfeException e)
		{
			logger.info(e.getMensaje(), e);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, Constants.BITACORA_WARNING, "SE PRODUJO UN ERROR FAVOR DE CONTACTAR AL ADMINISTRADOR"));
		}
		datos.setGuardar(false);
		datos.setCargacumulado(false);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Constants.BITACORA_WARNING, "SE TERMINA LA CARGA DE LA INFORMACION"));
		
		ctx.getFlowScope().put("carga", datos);
	}
	
	

	

}
