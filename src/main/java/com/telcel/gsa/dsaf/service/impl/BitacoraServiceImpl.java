package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.BitacoraAmovDao;
import com.telcel.gsa.dsaf.bean.BajasBitacoraBean;
import com.telcel.gsa.dsaf.dao.BitacoraDao;
import com.telcel.gsa.dsaf.dipcel.dao.BitacoraDipselDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.BitacoraSercotelDao;
import com.telcel.gsa.dsaf.service.BitacoraService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * 
 * @author VI5XXAA
 *
 */
@Service("bitacoraService")
public class BitacoraServiceImpl implements BitacoraService, Serializable{

	
	@Autowired
	BitacoraDao bitacoraDao;
	
	@Autowired
	@Qualifier("bitacoraDipselDaoImpl")
	BitacoraDipselDao bitacoraDipselDao;
	
	@Autowired
	@Qualifier("bitacoraSercotelDaoImpl")
	BitacoraSercotelDao bitacoraSercotelDao;
	
	@Autowired
	@Qualifier("bitacoraAmovDaoImpl")
	BitacoraAmovDao bitacoraAmovDao;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public HSSFWorkbook generaDocumento(List<BajasBitacoraBean> bitacoras) {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet("Bitacora");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHA);
		int rowNum = 0; 
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum); 
		headerRow.createCell((short) 0).setCellValue( 
		new HSSFRichTextString("TIPO CARGA")); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString("LINEA ERROR")); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString("ERROR")); 
		headerRow.createCell((short) 3).setCellValue( 
		new HSSFRichTextString("USUARIO CARGA"));
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString("FECHA"));

		for (BajasBitacoraBean b : bitacoras) {
			// Create a new row in the sheet: 
			HSSFRow row = sheet.createRow((short) ++rowNum); 

			row.createCell((short) 0).setCellValue(b.getBitacoraProceso()); 
			row.createCell((short) 1).setCellValue(b.getBitacoraLinea()); 
			row.createCell((short) 2).setCellValue(b.getBitacoraDescripcion()); 
			row.createCell((short) 3).setCellValue(b.getNombreUsuario()); 
			row.createCell((short) 4).setCellValue(sdf.format(b.getBitacoraFechaCreacion())); 

		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		
		return workbook;
	}

	@Override
	public void guardaBitacora(List<BajasBitacoraBean> bitacora, SessionScopeUser sessionData) throws SQLException {
//		bitacoraDao.guardaBitacora(bitacora);
		
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					bitacoraDao.guardaBitacora(bitacora);
				}break;
				case 2:{
					bitacoraDipselDao.guardaBitacora(bitacora);
				}break;
				case 3:{
					bitacoraSercotelDao.guardaBitacora(bitacora);
				}break;
				case 4:{
					bitacoraAmovDao.guardaBitacora(bitacora);
				}break;
			}
			
			}catch(CfeException e){
			throw e;
		}
	}
	


	@Override
	public List<BajasBitacoraBean> consultaBitacora(String tipoProc, SessionScopeUser sessionData) {
//		return bitacoraDao.consultaBitacora(tipoProc);
		List<BajasBitacoraBean> datList = null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datList = bitacoraDao.consultaBitacora(tipoProc);
				}break;
				case 2:{
					datList = bitacoraDipselDao.consultaBitacora(tipoProc);
				}break;
				case 3:{
					datList = bitacoraSercotelDao.consultaBitacora(tipoProc);
				}break;
				case 4:{
					datList = bitacoraAmovDao.consultaBitacora(tipoProc);
				}break;
			}
			
			}catch(CfeException e){
			throw e;
		}
		return datList;
	}

	@Override
	public void limpiaBitacora(String tipoProc, SessionScopeUser sessionData) throws SQLException {
//		bitacoraDao.limpiaBitacora(tipoProc);
		
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					bitacoraDao.limpiaBitacora(tipoProc);
				}break;
				case 2:{
					bitacoraDipselDao.limpiaBitacora(tipoProc);
				}break;
				case 3:{
					bitacoraSercotelDao.limpiaBitacora(tipoProc);
				}break;
				case 4:{
					bitacoraAmovDao.limpiaBitacora(tipoProc);
				}break;
			}
			
			}catch(CfeException e){
			throw e;
		}
		
	}

	@Override
	public Long idMax(SessionScopeUser sessionData) {
//		return bitacoraDao.idMax();
		Long id=null;
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					id = bitacoraDao.idMax();
				}break;
				case 2:{
					id = bitacoraDipselDao.idMax();
				}break;
				case 3:{
					id = bitacoraSercotelDao.idMax();
				}break;
				case 4:{
					id = bitacoraAmovDao.idMax();
				}break;
			}
			
			}catch(CfeException e){
			throw e;
		}
		return id;
		
	}
	
	


}
