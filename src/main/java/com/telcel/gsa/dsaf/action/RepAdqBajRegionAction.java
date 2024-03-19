package com.telcel.gsa.dsaf.action;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.faces.bean.ViewScoped;



import com.telcel.gsa.dsaf.bean.ReporteFormBean;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.service.CienDepreciadosService;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



@Service("repAdqBajRegion")
@ViewScoped
public class RepAdqBajRegionAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5347656222555039386L;
	private static Logger logger = LoggerFactory.getLogger(RepAdqBajRegionAction.class);
	
	
	
	@Autowired
	private SessionScopeUser sessionScopeUser;
	
	
	@Autowired
	BitacoraService bitacoraService;
	
	@Autowired
	UtileriasCfeService utileriasCfeService;
	
	@Autowired
	CienDepreciadosService dprCien;

	
	public void initFlow(RequestContext ctx) {
		logger.info(Constants.APP_ID + "Comienza Pantalla");
		ReporteFormBean datBean = new ReporteFormBean();
		
		ctx.getFlowScope().put("reporteBR", datBean);
	}
	
//	public void updateMonth(RequestContext ctx) {
//		BajasDepreciadosCienBean datos = (BajasDepreciadosCienBean)ctx.getFlowScope().get("datCien");
//		datos.setMes(datos.getMes());
//		ctx.getFlowScope().put("datCien", datos);
//		
//	}
	
	
	public void consultarInfo(RequestContext ctx) throws IOException, SQLException {
		ReporteFormBean datos = (ReporteFormBean)ctx.getFlowScope().get("reporteBR");
		logger.info("consultarInfo");
		
		ctx.getFlowScope().put("reporteBR", datos);
	}
	
	

	

}
