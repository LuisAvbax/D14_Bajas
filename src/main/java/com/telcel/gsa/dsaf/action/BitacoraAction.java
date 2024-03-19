package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BitacoraBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("bitacoraAction")
@ViewScoped
public class BitacoraAction implements Serializable{

	private static final long serialVersionUID = 1357848192092043408L;

	final static Logger logger = LoggerFactory.getLogger(BitacoraAction.class);
	
	
	@Autowired
	private UtileriasCfeService utileriasCfeService;
	@Autowired
	private BitacoraService bitacoraService;
	@Autowired
	private SessionScopeUser sessionScopeUser;

		
	public void initFlow(RequestContext ctx){
	
		BitacoraBean bitacora = new BitacoraBean();
		List<String> procesos = new ArrayList<String>();
		procesos.add("CARGA_CIEN");
//		procesos.add("NEXT");
		bitacora.setProceso("CARGA_CIEN");
		bitacora.setProcesos(procesos);
		bitacora.setDescarga(false);

		ctx.getFlowScope().put("bitacora", bitacora);
	}
	

	public void consultaBitacora(RequestContext ctx){
		BitacoraBean bitacora = (BitacoraBean)ctx.getFlowScope().get("bitacora");
		List<BajasBitacoraBean> datBitacoras = bitacoraService.consultaBitacora(bitacora.getProceso(), sessionScopeUser);
		bitacora.setListOpBitacora(datBitacoras);
		if(datBitacoras != null && !datBitacoras.isEmpty()){
			bitacora.setDescarga(true);
		}else{
			bitacora.setDescarga(false);
		}
		ctx.getFlowScope().put("bitacora", bitacora);
		
		
	}
	
	public void descargaBitacora(RequestContext ctx){
		BitacoraBean bitacora = (BitacoraBean)ctx.getFlowScope().get("bitacora");
		HttpServletResponse httpServletResponse = 
				(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHAHORA);
		httpServletResponse.addHeader(Constants.REP_RESUMENR_CONTENTDISP, Constants.REP_BITACORA_FILENAME 
				  + sdf.format(utileriasCfeService.obtenerFechaActual(sessionScopeUser)).replaceAll("/", "") 
				  + Constants.REP_EXCELD2003_EXT);	
		httpServletResponse.setContentType("application/vnd.ms-excel");
		
		ServletOutputStream out;
		try {
			HSSFWorkbook workbook = bitacoraService.generaDocumento(bitacora.getListOpBitacora());
			out = httpServletResponse.getOutputStream();
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			throw new CfeException(e.getMessage(), e);
		} 
		FacesContext.getCurrentInstance().responseComplete();
		
	}
	
}
