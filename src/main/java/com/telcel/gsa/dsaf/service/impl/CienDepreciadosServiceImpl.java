package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.CienDepreciadosAmovDao;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.dao.CienDepreciadosDao;
import com.telcel.gsa.dsaf.dipcel.dao.CienDepreciadosDipselDao;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.CienDepreciadosSercotelDao;
import com.telcel.gsa.dsaf.service.CienDepreciadosService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("cienDprService")
public class CienDepreciadosServiceImpl implements CienDepreciadosService, Serializable{

	
	@Autowired
	@Qualifier("cienDepreciadosDaoImpl")
	CienDepreciadosDao dprDao;
	@Autowired
	@Qualifier("cienDepreciadosDipselDaoImpl")
	CienDepreciadosDipselDao cienDepreciadosDipselDao;
	@Autowired
	@Qualifier("cienDepreciadosSercotelDaoImpl")
	CienDepreciadosSercotelDao cienDepreciadosSercotelDao;
	@Autowired
	@Qualifier("cienDepreciadosAmovDaoImpl")
	CienDepreciadosAmovDao cienDepreciadosAmovDao;
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public HSSFWorkbook generaDocumento(List<BajasDepreciadosCienBean> datos) {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet("Bitacora");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHA);
		
		int rowNum = 0; 
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum); 
		headerRow.createCell((short) 0).setCellValue( 
		new HSSFRichTextString("Mes")); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString("Año")); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString("Numero Activo")); 
		headerRow.createCell((short) 3).setCellValue( 
		new HSSFRichTextString("Subnumero"));
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString("Usuario"));
		headerRow.createCell((short) 5).setCellValue( 
		new HSSFRichTextString("FECHA"));

		for (BajasDepreciadosCienBean b : datos) {
			// Create a new row in the sheet: 
			HSSFRow row = sheet.createRow((short) ++rowNum); 

			row.createCell((short) 0).setCellValue(b.getDescMes()); 
			row.createCell((short) 1).setCellValue(b.getAnio()); 
			row.createCell((short) 2).setCellValue(b.getNumeroActivo());
			row.createCell((short) 3).setCellValue(b.getSubnumero());
			row.createCell((short) 4).setCellValue(b.getaPaterno()+" "+b.getaMaterno()+" "+b.getNombre());
			row.createCell((short) 5).setCellValue(sdf.format(b.getFechaCarga())); 

		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		
		return workbook;
	}

	@Override
	public void guardaCienDpr(BajasDepreciadosCien datDpr, SessionScopeUser sessionData) throws CfeException {
		
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dprDao.guardaCienDpr(datDpr);
				}break;
				case 2:{
					cienDepreciadosDipselDao.guardaCienDpr(datDpr);
				}break;
				case 3:{
					cienDepreciadosSercotelDao.guardaCienDpr(datDpr);
				}break;
				case 4:{
					cienDepreciadosAmovDao.guardaCienDpr(datDpr);
				}break;
			}
	
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(), e);
			
		}
		
	}
	
	
	@Override
	public void guardaCienDprLst(List<BajasDepreciadosCien> datDprLst, SessionScopeUser sessionData) throws CfeException {
		
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dprDao.guardaCienDprLst(datDprLst);
				}break;
				case 2:{
					cienDepreciadosDipselDao.guardaCienDprLst(datDprLst);
				}break;
				case 3:{
					cienDepreciadosSercotelDao.guardaCienDprLst(datDprLst);
				}break;
				case 4:{
					cienDepreciadosAmovDao.guardaCienDprLst(datDprLst);
				}break;
			}
	
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(), e);
			
		}
		
	}
	
	@Override
	public void actualizaCienDprLst(List<BajasDepreciadosCien> datDprLst, SessionScopeUser sessionData) throws CfeException {
		
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dprDao.actualizaCienDprLst(datDprLst);
				}break;
				case 2:{
					cienDepreciadosDipselDao.actualizaCienDprLst(datDprLst);
				}break;
				case 3:{
					cienDepreciadosSercotelDao.actualizaCienDprLst(datDprLst);
				}break;
				case 4:{
					cienDepreciadosAmovDao.actualizaCienDprLst(datDprLst);
				}break;
			}
	
		}catch(CfeException e){
			throw e;
		} catch (SQLException e) {
			throw new CfeException(e.getMessage(), e);
			
		}
		
	}
	


	@Override
	public List<BajasDepreciadosCienBean> consultaCienDpr(BajasDprCienFormBean datos, SessionScopeUser sessionData) {
		List<BajasDepreciadosCienBean> list = new ArrayList<BajasDepreciadosCienBean>();
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					list = dprDao.consultaCienDpr(datos, sessionData);
				}break;
				case 2:{
					list = cienDepreciadosDipselDao.consultaCienDpr(datos, sessionData);
				}break;
				case 3:{
					list = cienDepreciadosSercotelDao.consultaCienDpr(datos, sessionData);
				}break;
				case 4:{
					list = cienDepreciadosAmovDao.consultaCienDpr(datos, sessionData);
				}break;
			}
			}catch(CfeException e){
			throw e;
		}
		
		return list;
	}
	
	@Override
	public BajasDepreciadosCienBean consultaCienDprValida(BajasDepreciadosCienBean datos, SessionScopeUser sessionData) {
		BajasDepreciadosCienBean dat = new BajasDepreciadosCienBean();
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dat = dprDao.consultaCienDprValida(datos);
				}break;
				case 2:{
					dat = cienDepreciadosDipselDao.consultaCienDprValida(datos);
				}break;
				case 3:{
					dat = cienDepreciadosSercotelDao.consultaCienDprValida(datos);
				}break;
				case 4:{
					dat = cienDepreciadosAmovDao.consultaCienDprValida(datos);
				}break;
			}
			}catch(CfeException e){
			throw e;
		}
		
		return dat;
	}

	@Override
	public List<BajasDepreciadosCienBean> getDepreciados(Integer anio, Integer mes, SessionScopeUser sessionData) throws CfeException{
		List<BajasDepreciadosCienBean> dat = new LinkedList<BajasDepreciadosCienBean>();
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dat = dprDao.getDepreciados(anio, mes);
				}break;
				case 2:{
					dat = cienDepreciadosDipselDao.getDepreciados(anio, mes);
				}break;
				case 3:{
					dat = cienDepreciadosSercotelDao.getDepreciados(anio, mes);
				}break;
				case 4:{
					dat = cienDepreciadosAmovDao.getDepreciados(anio, mes);
				}break;
			}
			}catch(CfeException e){
			throw e;
		}
		return dat;
	}


	
	@Override
	public List<BajasDepreciadosCienBean> deleteAssets(List<BajasDepreciadosCienBean> assetList, SessionScopeUser sessionData) {
		List<BajasDepreciadosCienBean> listToDelete = new ArrayList<BajasDepreciadosCienBean>();
		listToDelete.addAll(assetList);
		try{
			
			
			CollectionUtils.filter(listToDelete, new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					BajasDepreciadosCienBean asset = (BajasDepreciadosCienBean) object;
					if((asset.isSelected() == Constants.ESTATUS_TRUE)){
						return Constants.ESTATUS_TRUE;
					}
					return Constants.ESTATUS_FALSE;
				}
			});
			
			switch(sessionData.getSociedad().getId()){
				case 1:{
					dprDao.deleteAssets(listToDelete, sessionData);
					assetList.removeAll(listToDelete);
				}break;
				case 2:{
					cienDepreciadosDipselDao.deleteAssets(listToDelete, sessionData);
					assetList.removeAll(listToDelete);
				}break;
				case 3:{
					cienDepreciadosSercotelDao.deleteAssets(listToDelete, sessionData);
					assetList.removeAll(listToDelete);
				}break;
				case 4:{
					cienDepreciadosAmovDao.deleteAssets(listToDelete, sessionData);
					assetList.removeAll(listToDelete);
				}break;
			}
			}catch(CfeException e){
			throw e;
		}
		
		return assetList;
	}
	


}
