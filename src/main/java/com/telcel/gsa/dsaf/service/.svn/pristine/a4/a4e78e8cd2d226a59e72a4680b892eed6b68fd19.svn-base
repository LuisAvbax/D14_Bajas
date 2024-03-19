package com.telcel.gsa.dsaf.service.impl;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.springframework.stereotype.Service;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.telcel.gsa.dsaf.bean.AdqBajasBean;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.bean.BajasCalculoBean;
import com.telcel.gsa.dsaf.bean.BajasCatParametrosBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasMesBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.MesBean;
import com.telcel.gsa.dsaf.bean.SociedadBean;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.entity.BajasCatParametros;
import com.telcel.gsa.dsaf.entity.BajasCatRegion;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.service.UtileriasCfeService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

/**
 * 
 * @author VI5XXAA
 *
 */
@Service("utileriaCfeService")
public class UtileriasCfeServiceImpl implements UtileriasCfeService, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6503074069351031461L;
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
	
	@Override
	public Map<String, BajasCatParametrosBean> getParametros(List<String> params,SessionScopeUser sessionScopeUser) {
		Map<String, BajasCatParametrosBean> parametrosMap = null;
		List<BajasCatParametrosBean> parametrosList = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				parametrosList = utileriasCfeDao.getParams(params);
			}break;
			case 2:{
				parametrosList = utileriasCfeDipcelDao.getParams(params);
			}break;
			case 3: {
				parametrosList = utileriasCfeSercotelDao.getParams(params);
			}break;
			case 4: {
				parametrosList = utileriasCfeAmovDao.getParams(params);
			}break;
		}
		
		if(parametrosList != null && !parametrosList.isEmpty()){
			parametrosMap = new LinkedHashMap<String, BajasCatParametrosBean>();
			for(BajasCatParametrosBean p : parametrosList){
				parametrosMap.put(p.getNombre(), p);
			}
		}
		return parametrosMap;
	}
	
	@Override
	public Map<String, BajasCatParametrosBean> obtieneParametros(List<String> params, SessionScopeUser sessionScopeUser) {
		Map<String, BajasCatParametrosBean> parametrosMap = null;
		List<BajasCatParametrosBean> parametrosList = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				parametrosList = utileriasCfeDao.obtenerParametros(params);
			}break;
			case 2:{
				parametrosList = utileriasCfeDipcelDao.obtenerParametros(params);
			}break;
			case 3: {
				parametrosList = utileriasCfeSercotelDao.obtenerParametros(params);
			}break;
			case 4: {
				parametrosList = utileriasCfeAmovDao.obtenerParametros(params);
			}break;
		}
		
		if(parametrosList != null && !parametrosList.isEmpty()){
			parametrosMap = new LinkedHashMap<String, BajasCatParametrosBean>();
			for(BajasCatParametrosBean p : parametrosList){
				parametrosMap.put(p.getNombre(), p);
			}
		}
		return parametrosMap;
	}
	
	

	@Override
	public Map<String, CatRegionBean> obtenerRegiones(SessionScopeUser sessionScopeUser) {
		List <CatRegionBean> lista = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			lista = utileriasCfeDao.obtenerRegiones();
		}break;
		case 2:{
			lista = utileriasCfeDipcelDao.obtenerRegiones();
		}break;
		case 3: {
			lista = utileriasCfeSercotelDao.obtenerRegiones();
		}break;
		case 4: {
			lista = utileriasCfeAmovDao.obtenerRegiones();
		}break;
	}
		
		Map<String, CatRegionBean> map = null;
			if(lista != null && !lista.isEmpty()){
			map = new LinkedHashMap<String, CatRegionBean>();
				for(CatRegionBean r: lista){
					if(r.getDescripcion() != null){
					map.put(r.getDescripcion(), r);
					}
				}
			}
		return map;
	}
	
	
	@Override
	public List<BajasCatRegion> obtenerRegionesWID(SessionScopeUser sessionScopeUser)throws CfeException {
		List <BajasCatRegion> lista = null;
		try{
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			lista = utileriasCfeDao.obtenerRegionesWID();
		}break;
		case 2:{
			lista = utileriasCfeDipcelDao.obtenerRegionesWID();
		}break;
		case 3: {
			lista = utileriasCfeSercotelDao.obtenerRegionesWID();
		}break;
		case 4: {
			lista = utileriasCfeAmovDao.obtenerRegionesWID();
		}break;
		}
		}catch(CfeException e){
			throw e;
		}
		return lista;
	}
	
	

	@Override
	public List<CatRegionBean> obtenerLstRegiones(SessionScopeUser sessionScopeUser) {
		List<CatRegionBean> lista = null;
		switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				lista =  utileriasCfeDao.obtenerRegiones();
			}break;
			case 2:{
				lista =  utileriasCfeDipcelDao.obtenerRegiones();
			}break;
			case 3: {
				lista = utileriasCfeSercotelDao.obtenerRegiones();
			}break;
			case 4: {
				lista = utileriasCfeAmovDao.obtenerRegiones();
			}break;
		}
		return lista;
	}
	

	
	public List<MesBean> obtenerMeses(Map<String,BajasCatParametrosBean> parametros){
		MesBean mb = null;
		List<MesBean> meses = new ArrayList<MesBean>();
		String [] mesesParam = parametros.get(Constants.LOGIN_DATA_MONTHS).getDescripcion().split(Constants.COMMA);
			for(int i = 0; i< mesesParam.length; i++){
				mb = new MesBean();
				mb.setId(i+1);
				mb.setNombre(mesesParam[i]);
				meses.add(mb);
			}
		return meses;
	}
	
	
	public Date obtenerFechaActual(SessionScopeUser sessionScopeUser){
		Date fecha = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			fecha =  utileriasCfeDao.obtenerFecha();
		}break;
		case 2:{
			fecha =  utileriasCfeDipcelDao.obtenerFecha();
		}break;
		case 3: {
			fecha = utileriasCfeSercotelDao.obtenerFecha();
		}break;
		case 4: {
			fecha = utileriasCfeAmovDao.obtenerFecha();
		}break;
	}
		return fecha;
	}
	
	
	@Override
	public MesBean obtieneMesActual(SessionScopeUser sessionScopeUser){
		MesBean mes = new MesBean();
		Date fechaActual = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			fechaActual =  utileriasCfeDao.obtenerFecha();
		}break;
		case 2:{
			fechaActual =  utileriasCfeDipcelDao.obtenerFecha();
		}break;
		case 3: {
			fechaActual = utileriasCfeSercotelDao.obtenerFecha();
		}break;
		case 4: {
			fechaActual = utileriasCfeAmovDao.obtenerFecha();
		}break;
	}
		Calendar c = Calendar.getInstance();
		c.setTime(fechaActual);
		mes.setId(c.get(Calendar.MONTH));
		return mes;
	}
	
	
	
	
	@Override
	public Integer anioActual(SessionScopeUser sessionScopeUser){
		Date fechaActual = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			fechaActual =  utileriasCfeDao.obtenerFecha();
		}break;
		case 2:{
			fechaActual =  utileriasCfeDipcelDao.obtenerFecha();
		}break;
		case 3: {
			fechaActual = utileriasCfeSercotelDao.obtenerFecha();
		}break;
		case 4: {
			fechaActual = utileriasCfeAmovDao.obtenerFecha();
		}break;
	}
		Calendar c = Calendar.getInstance();
		c.setTime(fechaActual);
		return c.get(Calendar.YEAR);
	}
	

	
	
	@Override
	public void agregaEncabezado(HSSFRow row, List<String> titulos , Integer colNum){
		Cell cell =	null;
		for(String t:titulos){
			cell = row.createCell(colNum++);
				cell.setCellValue( 
						new HSSFRichTextString(t)); 
		}
	}
	
	@Override
	public void agregaFooter(HSSFRow row, List<BigDecimal> totales , Integer colNum, HSSFCellStyle style){
		Cell cell =	null;
		cell = row.createCell(colNum++);
		cell.setCellValue(new HSSFRichTextString("TOTAL"));
		for(BigDecimal t:totales){
			cell = row.createCell(colNum++);
				cell.setCellValue(t.toString());
				cell.setCellStyle(style);
				
		}
	}
	@Override
	public Map<Integer, MesBean> mapeaMeses(List<MesBean> meses){
		Map<Integer, MesBean> mesCustMap = new LinkedHashMap<Integer, MesBean>();
		for(MesBean mb: meses){
			switch(mb.getId()){
				case 1:{
					mb.setNombre("ENE");
				}break;
				case 2:{
					mb.setNombre("FEB");
				}break;
				case 3:{
					mb.setNombre("MZO");
				}break;
				case 4:{
					mb.setNombre("ABR");
				}break;
				case 5:{
					mb.setNombre("MAY");
				}break;
				case 6:{
					mb.setNombre("JUN");
				}break;
				case 7:{
					mb.setNombre("JUL");
				}break;
				case 8:{
					mb.setNombre("AGO");
				}break;
				case 9:{
					mb.setNombre("SEP");
				}break;
				case 10:{
					mb.setNombre("OCT");
				}break;
				case 11:{
					mb.setNombre("NOV");
				}break;
				case 12:{
					mb.setNombre("DIC");
				}break;
			}
			mesCustMap.put(mb.getId(), mb);
		}
		return mesCustMap;
	}

	
	@Override
	public List<BajasCalculoBean> getCalculos(SessionScopeUser sessionScopeUser) throws CfeException{
		List<BajasCalculoBean> calculos = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			calculos =  utileriasCfeDao.getCalculos();
		}break;
		case 2:{
			calculos =  utileriasCfeDipcelDao.getCalculos();
		}break;
		case 3: {
			calculos = utileriasCfeSercotelDao.getCalculos();
		}break;
		case 4: {
			calculos = utileriasCfeAmovDao.getCalculos();
		}break;
	}
		return calculos;
	}
	
	
	
	
	@Override
	public List<BajasClaseBean> getClases(AdqBajasBean bajasRegBean, SessionScopeUser sessionScopeUser) throws CfeException{
		List<BajasClaseBean> clases = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			clases =  utileriasCfeDao.getClases(bajasRegBean);
		}break;
		case 2:{
			clases =  utileriasCfeDipcelDao.getClases(bajasRegBean);
		}break;
		case 3: {
			clases = utileriasCfeSercotelDao.getClases(bajasRegBean);
		}break;
		case 4: {
			clases = utileriasCfeAmovDao.getClases(bajasRegBean);
		}break;
	}
		return clases;
	}
	

	
	
	@Override
	public List<BajasClaseBean> getClases(DepreActBean depreRegBean, SessionScopeUser sessionScopeUser) throws CfeException{
		List<BajasClaseBean> clases = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			clases =  utileriasCfeDao.getClases(depreRegBean);
		}break;
		case 2:{
			clases =  utileriasCfeDipcelDao.getClases(depreRegBean);
		}break;
		case 3: {
			clases = utileriasCfeSercotelDao.getClases(depreRegBean);
		}break;
		case 4: {
			clases = utileriasCfeAmovDao.getClases(depreRegBean);
		}break;
	}
		return clases;
	}
	
	
	
	@Override
	public List<BajasClaseBean> getClasesGen(List<String> region, BajasCalculoBean calculo, SessionScopeUser sessionScopeUser) throws CfeException {
		List<BajasClaseBean> clases = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			clases =  utileriasCfeDao.getClasesGen(region, calculo);
		}break;
		case 2:{
			clases =  utileriasCfeDipcelDao.getClasesGen(region, calculo);
		}break;
		case 3: {
			clases = utileriasCfeSercotelDao.getClasesGen(region, calculo);
		}break;
		case 4: {
			clases = utileriasCfeAmovDao.getClasesGen(region, calculo);
		}break;
	}
	return clases;	
	}
	
	
	
	public List<BajasClaseBean> getClasesCosto(CostoBean costoRegBean, SessionScopeUser sessionScopeUser) throws CfeException{
		List<BajasClaseBean> clases = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			clases =  utileriasCfeDao.getClasesCosto(costoRegBean);
		}break;
		case 2:{
			clases =  utileriasCfeDipcelDao.getClasesCosto(costoRegBean);
		}break;
		case 3: {
			clases = utileriasCfeSercotelDao.getClasesCosto(costoRegBean);
		}break;
		case 4: {
			clases = utileriasCfeAmovDao.getClasesCosto(costoRegBean);
		}break;
	}
		return clases;
	}
	
	
	
	public SelectItemGroup getTxtGroup(List<String> txtLst, String groupName){
		SelectItemGroup g1 = new SelectItemGroup(groupName);
//		List<SelectItem> txtsRtn = null;
		SelectItem[] selItemLst = null;
		SelectItem txtRtn = null;
		if(txtLst != null && !txtLst.isEmpty()){
			selItemLst = new SelectItem[txtLst.size()];
			int i = 0;
//			txtsRtn = new ArrayList<SelectItem>();
			for(String txtF: txtLst){
				txtRtn = new SelectItem(txtF,txtF);
				selItemLst[i++] = txtRtn;
//			txtsRtn.add(txtRtn);
			}
			
	        g1.setSelectItems(selItemLst);
		}
		return g1;
	}
	@Override
	public List<String> getTxtGen(List<String> region, BajasCalculoBean calculo, List<String> clase, String texto, SessionScopeUser sessionScopeUser) throws CfeException{
		List<String> txtFiscLst = null;
		try{
			
			switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				txtFiscLst = utileriasCfeDao.getTxtGen(region, calculo, clase, texto);
			}break;
			case 2:{
				txtFiscLst =  utileriasCfeDipcelDao.getTxtGen(region, calculo, clase, texto);
			}break;
			case 3: {
				txtFiscLst = utileriasCfeSercotelDao.getTxtGen(region, calculo, clase, texto);
			}break;
			case 4: {
				txtFiscLst = utileriasCfeAmovDao.getTxtGen(region, calculo, clase, texto);
			}break;
		}
		
		}catch(CfeException e){
			throw e;
		}
		return txtFiscLst ;
	}
	
	
	
	@Override
	public List<String> getTxt(AdqBajasBean bajasRegBean, SessionScopeUser sessionScopeUser) throws CfeException{
		List<String> txtFiscLst = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				txtFiscLst = utileriasCfeDao.getTxt(bajasRegBean);
			}break;
			case 2:{
				txtFiscLst =  utileriasCfeDipcelDao.getTxt(bajasRegBean);
			}break;
			case 3: {
				txtFiscLst = utileriasCfeSercotelDao.getTxt(bajasRegBean);
			}break;
			case 4: {
				txtFiscLst = utileriasCfeAmovDao.getTxt(bajasRegBean);
			}break;
		}
		
		}catch(CfeException e){
			throw e;
		}
		return txtFiscLst ;
	}
	
	
	@Override
	public List<String> getTxt(DepreActBean depreRegBean, SessionScopeUser sessionScopeUser) throws CfeException{
		List<String> txtFiscLst = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1: {
				txtFiscLst = utileriasCfeDao.getTxt(depreRegBean);
			}break;
			case 2:{
				txtFiscLst =  utileriasCfeDipcelDao.getTxt(depreRegBean);
			}break;
			case 3: {
				txtFiscLst = utileriasCfeSercotelDao.getTxt(depreRegBean);
			}break;
			case 4: {
				txtFiscLst = utileriasCfeAmovDao.getTxt(depreRegBean);
			}break;
		}
		}catch(CfeException e){
			throw e;
		}
		return txtFiscLst ;
	}
	

	public List<String> getTxtCosto(CostoBean costoRegBean, SessionScopeUser sessionScopeUser) throws CfeException{

		List<String> txtFiscLst = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			txtFiscLst = utileriasCfeDao.getTxtCosto(costoRegBean);
		}break;
		case 2:{
			txtFiscLst =  utileriasCfeDipcelDao.getTxtCosto(costoRegBean);
		}break;
		case 3: {
			txtFiscLst = utileriasCfeSercotelDao.getTxtCosto(costoRegBean);
		}break;
		case 4: {
			txtFiscLst = utileriasCfeAmovDao.getTxtCosto(costoRegBean);
		}break;
	}	
		return txtFiscLst ;
	}
	
	public List<BajasAjustesBean> getAjustes(SessionScopeUser sessionScopeUser) throws CfeException{
		List<BajasAjustesBean> ajustesLst = null;
		switch(sessionScopeUser.getSociedad().getId()){
		case 1: {
			ajustesLst = utileriasCfeDao.getAjustes();
		}break;
		case 2:{
			ajustesLst =  utileriasCfeDipcelDao.getAjustes();
		}break;
		case 3: {
			ajustesLst = utileriasCfeSercotelDao.getAjustes();
		}break;
		case 4: {
			ajustesLst = utileriasCfeAmovDao.getAjustes();
		}break;
	}	
		return ajustesLst;
	}
	
	public String[] getAcumMonths(String [] monthsSel){
		String [] monthsBack = new String[]{};
		Map <String,BajasMesBean> mesesRef = new HashMap<String, BajasMesBean>();
		BajasMesBean mb = null;
		
		switch(getmaxMonth(monthsSel)){
		case 1:{
			 monthsBack =  new String[]{"ENE"};
		}break;
		case 2:{
			monthsBack = new String[]{"ENE","FEB"};
		}break;
		case 3:{
			monthsBack = new String[]{"ENE","FEB","MZO"};
		}break;
		case 4:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR"};
		}break;
		case 5:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY"};
		}break;
		case 6:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN"};
		}break;
		case 7:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL"};
		}break;
		case 8:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO"};
		}break;
		case 9:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO", "SEP"};
		}break;
		case 10:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO", "SEP", "OCT"};
		}break;
		case 11:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO", "SEP", "OCT", "NOV"};
		}break;
		case 12:{
			monthsBack = new String[]{"ENE","FEB","MZO","ABR","MAY","JUN","JUL","AGO", "SEP", "OCT", "NOV", "DIC"};
		}break;
		default:{
			
		}
		}
		return monthsBack;
		
	}
	
	public Integer getmaxMonth(String [] monthsSel){
		Map <String,BajasMesBean> mesesRef = new HashMap<String, BajasMesBean>();
		BajasMesBean mb = null;
		Integer maxMonth  =0;
		
		mb = new BajasMesBean();
		mb.setMesId(1);
		mb.setMes("ENE");
		mesesRef.put("ENE", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(2);
		mb.setMes("FEB");
		mesesRef.put("FEB", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(3);
		mb.setMes("MZO");
		mesesRef.put("MZO", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(4);
		mb.setMes("ABR");
		mesesRef.put("ABR", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(5);
		mb.setMes("MAY");
		mesesRef.put("MAY", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(6);
		mb.setMes("JUN");
		mesesRef.put("JUN", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(7);
		mb.setMes("JUL");
		mesesRef.put("JUL", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(8);
		mb.setMes("AGO");
		mesesRef.put("AGO", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(9);
		mb.setMes("SEP");
		mesesRef.put("SEP", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(10);
		mb.setMes("OCT");
		mesesRef.put("OCT", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(11);
		mb.setMes("NOV");
		mesesRef.put("NOV", mb);
		
		mb = new BajasMesBean();
		mb.setMesId(12);
		mb.setMes("DIC");
		mesesRef.put("DIC", mb);
		
		
		for(int i = 0; i< monthsSel.length ; i++){
			if(mesesRef.get(monthsSel[i]) != null && mesesRef.get(monthsSel[i]).getMesId() > maxMonth){
				maxMonth = (mesesRef.get(monthsSel[i]).getMesId());
			}
		}
		
		return maxMonth;
	}
	
public String txtperiodoConsulta(AdqBajasBean adqreg){
		
		StringBuilder strMeses = null;
		if(adqreg.isAcum()){
					strMeses = new StringBuilder("ACUMULADO A ");
			switch(getmaxMonth(adqreg.getMesesselect())){
			case 1:{
			strMeses.append("ENERO");
			}
				break;
			case 2:{
			strMeses.append("FEBRERO");	
			}
				break;
			case 3:{
			strMeses.append("MARZO");	
			}
				break;
			case 4:{
			strMeses.append("ABRIL");	
			}
				break;
			case 5:{
				strMeses.append("MAYO");	
			}
				break;
			case 6:{
				strMeses.append("JUNIO");
			}
				break;
			case 7:{
				strMeses.append("JULIO");
			}
				break;
			case 8:{
				strMeses.append("AGOSTO");
			}
				break;
			case 9:{
				strMeses.append("SEPTIEMBRE");
			}
				break;
			case 10:{
				strMeses.append("OCTUBRE");
			}
				break;
			case 11:{
				strMeses.append("NOVIEMBRE");
			}
				break;
			case 12:{
				strMeses.append("DICIEMBRE");
			}
				break;
			}
			strMeses.append(" "+ adqreg.getAnio());
		}else if(!adqreg.isAcum() && adqreg.getMesesselect().length > 1){
			strMeses = new StringBuilder("MESES: " + utileriasCfeDao.joinArray(adqreg.getMesesselect(), "", ",", ""));
		}else if(!adqreg.isAcum() && adqreg.getMesesselect().length == 1){
			strMeses = new StringBuilder("MES: " + utileriasCfeDao.joinArray(adqreg.getMesesselect(), "", ",", ""));
		}
		
		return strMeses.toString() ;
		
	}


public String txtperiodoConsulta(boolean isAcum, String[] mesesSelect, Integer anio){
	
	StringBuilder strMeses = null;
	if(isAcum){
				strMeses = new StringBuilder("ACUMULADO A ");
		switch(getmaxMonth(mesesSelect)){
		case 1:{
		strMeses.append("ENERO");
		}
			break;
		case 2:{
		strMeses.append("FEBRERO");	
		}
			break;
		case 3:{
		strMeses.append("MARZO");	
		}
			break;
		case 4:{
		strMeses.append("ABRIL");	
		}
			break;
		case 5:{
			strMeses.append("MAYO");	
		}
			break;
		case 6:{
			strMeses.append("JUNIO");
		}
			break;
		case 7:{
			strMeses.append("JULIO");
		}
			break;
		case 8:{
			strMeses.append("AGOSTO");
		}
			break;
		case 9:{
			strMeses.append("SEPTIEMBRE");
		}
			break;
		case 10:{
			strMeses.append("OCTUBRE");
		}
			break;
		case 11:{
			strMeses.append("NOVIEMBRE");
		}
			break;
		case 12:{
			strMeses.append("DICIEMBRE");
		}
			break;
		}
		strMeses.append(" "+ anio);
	}else if(!isAcum && mesesSelect.length > 1){
		strMeses = new StringBuilder("MESES: " + utileriasCfeDao.joinArray(mesesSelect, "", ",", ""));
	}else if(!isAcum && mesesSelect.length == 1){
		strMeses = new StringBuilder("MES: " + utileriasCfeDao.joinArray(mesesSelect, "", ",", ""));
	}
	
	return strMeses.toString() ;
	
}

public String txtperiodoConsultaCosto(CostoBean costoReg){
	
	StringBuilder strMeses = null;
	if(costoReg.isAcum()){
				strMeses = new StringBuilder("ACUMULADO A ");
		switch(getmaxMonth(costoReg.getMesesselect())){
		case 1:{
		strMeses.append("ENERO");
		}
			break;
		case 2:{
		strMeses.append("FEBRERO");	
		}
			break;
		case 3:{
		strMeses.append("MARZO");	
		}
			break;
		case 4:{
		strMeses.append("ABRIL");	
		}
			break;
		case 5:{
			strMeses.append("MAYO");	
		}
			break;
		case 6:{
			strMeses.append("JUNIO");
		}
			break;
		case 7:{
			strMeses.append("JULIO");
		}
			break;
		case 8:{
			strMeses.append("AGOSTO");
		}
			break;
		case 9:{
			strMeses.append("SEPTIEMBRE");
		}
			break;
		case 10:{
			strMeses.append("OCTUBRE");
		}
			break;
		case 11:{
			strMeses.append("NOVIEMBRE");
		}
			break;
		case 12:{
			strMeses.append("DICIEMBRE");
		}
			break;
		}
		strMeses.append(" "+ costoReg.getAnio());
	}else if(!costoReg.isAcum() && costoReg.getMesesselect().length > 1){
		strMeses = new StringBuilder("MESES: " + utileriasCfeDao.joinArray(costoReg.getMesesselect(), "", ",", ""));
	}else if(!costoReg.isAcum() && costoReg.getMesesselect().length == 1){
		strMeses = new StringBuilder("MES: " + utileriasCfeDao.joinArray(costoReg.getMesesselect(), "", ",", ""));
	}
	
	return strMeses.toString() ;
	
}

public SociedadBean strutureSoc(Integer id){
	SociedadBean sb = new SociedadBean();
	sb.setId(id);
	switch(id){
		case 1:{
			sb.setNombre("TELCEL");
		}
			break;
		case 2:{
			sb.setNombre("DIPCEL");
		}
			break;
			
		case 3:{
			sb.setNombre("SERCOTEL");
		}
			break;
		case 4:{
			sb.setNombre("AMERICA MOVIL");
		}
			break;
	}
	return sb;
}

public List<BajasBitacoraBean> validaCargaDepreciados(List<BajasDepreciadosCienBean> activos, Integer maxBit, SessionScopeUser sessionScopeUser){
	BajasBitacoraBean datBit = null;
	List<BajasBitacoraBean> bitacoraLst = new LinkedList<BajasBitacoraBean>();
	
	for(BajasDepreciadosCienBean activo:activos) {
		if (activo.getNumeroActivo() == null || activo.getSubnumero() == null) {
			maxBit =maxBit + 1;
			datBit = new BajasBitacoraBean();
			datBit.setBitacoraId(maxBit);
			datBit.setBitacoraLinea(Long.valueOf(activo.getRowNum()));
			datBit.setBitacoraProceso("CARGA_CIEN");
			datBit.setBitacoraDescripcion(activo.getNumeroActivo() == null ? "CAMPO NUMERO DE ACTIVO ES REQUERIDO EN FILA: "+ activo.getRowNum() : activo.getSubnumero() == null ? "CAMPO SUBNUMERO DE ACTIVO ES REQUERIDO EN FILA: "+ activo.getRowNum() :"");
			datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
			datBit.setBitacoraFechaCreacion(obtenerFechaActual(sessionScopeUser));
			datBit.setBitacoraEstadoRegistro(true);
			bitacoraLst.add(datBit);
		}else if((activo.getNumeroActivo() != null) && activo.getSubnumero()!= null) {
			
			if(activo.getNumeroActivo().length() <=11 || activo.getSubnumero().length() < 4){
				maxBit =maxBit + 1;
				datBit = new BajasBitacoraBean();
				datBit.setBitacoraId(maxBit);
				datBit.setBitacoraLinea(Long.valueOf(activo.getRowNum()));
				datBit.setBitacoraProceso("CARGA_CIEN");
				datBit.setBitacoraDescripcion(activo.getNumeroActivo().length() <= 11 ? "VERIFICAR LA LONGITUD DEL NUMERO DE ACTIVO EN LA LINEA  "+activo.getRowNum()+" DEL ARCHIVO" : activo.getSubnumero().length() < 4 ? "VERIFICAR LA LONGITUD DEL SUBNUMERO DE ACTIVO EN LA LINEA  "+activo.getRowNum()+" DEL ARCHIVO" :"");
				datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
				datBit.setBitacoraFechaCreacion(obtenerFechaActual(sessionScopeUser));
				datBit.setBitacoraEstadoRegistro(true);
				bitacoraLst.add(datBit);
			}
			try {
				Integer nactivo = Integer.parseInt(activo.getNumeroActivo());
				Integer nsubnum = Integer.parseInt(activo.getSubnumero());
			}catch(NumberFormatException e) {
				maxBit =maxBit + 1;
				datBit = new BajasBitacoraBean();
				datBit.setBitacoraId(maxBit);
				datBit.setBitacoraLinea(Long.valueOf(activo.getRowNum()));
				datBit.setBitacoraProceso("CARGA_CIEN");
				datBit.setBitacoraDescripcion("SOLO SE PERMITEN NUMEROS, VERIFICAR LINEA  "+activo.getRowNum()+" DEL ARCHIVO");
				datBit.setRfcOpUsuarioByBitacoraUsuarioCreacion(sessionScopeUser.getUsuarioCfe().getId());
				datBit.setBitacoraFechaCreacion(obtenerFechaActual(sessionScopeUser));
				datBit.setBitacoraEstadoRegistro(true);
				bitacoraLst.add(datBit);
			}
		}else{
			
		}
			
			
	}
	return bitacoraLst;
}

public Map<String, BajasCatParametrosBean> getParams(List<String> params) {
	Map<String, BajasCatParametrosBean> parametrosMap = null;
	List<BajasCatParametrosBean> parametrosList = null;
	parametrosList = utileriasCfeDao.getParams(params);
	if (parametrosList != null && !parametrosList.isEmpty()) {
		parametrosMap = new LinkedHashMap<String, BajasCatParametrosBean>();
		for (BajasCatParametrosBean p : parametrosList) {
			parametrosMap.put(p.getNombre(), p);
		}
	}
	return parametrosMap;
}

public Date obtenerFechaActual() {
	return utileriasCfeDao.obtenerFecha();
}

}
