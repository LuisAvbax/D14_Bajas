package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.InstitucionalAjustesAmovDao;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasTresBean;
import com.telcel.gsa.dsaf.bean.BajasTresBeanResumen;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.InstAjusteBean;
import com.telcel.gsa.dsaf.bean.TGClaseTresBean;
import com.telcel.gsa.dsaf.dao.AdquisicionBajaDao;
import com.telcel.gsa.dsaf.dao.InstitucionalAjustesDao;
import com.telcel.gsa.dsaf.dipcel.dao.InstitucionalAjustesDipcelDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.InstitucionalAjustesSercotelDao;
import com.telcel.gsa.dsaf.service.InstitucionalAjusteService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;

import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 
 * @author VI5XXAA
 *
 */
@Service("instAjusteService")
public class InstitucionalAjusteServiceImpl implements InstitucionalAjusteService, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8530679323624973890L;
	@Autowired
	AdquisicionBajaDao adquisicionBajaDao;
	@Autowired
	InstitucionalAjustesDao institucionalAjustesDao;
	@Autowired
	InstitucionalAjustesDipcelDao institucionalAjustesDipcelDao;
	@Autowired
	InstitucionalAjustesSercotelDao institucionalAjustesSercotelDao;
	@Autowired
	InstitucionalAjustesAmovDao institucionalAjustesAmovDao;
	/**
	 * 
	 */
	

	@Override
	public InstAjusteBean datosWhere(InstAjusteBean datos) {
		
		if (datos.getCalculo() != null)
		{
			switch (datos.getCalculo().getId())
			{
				case 1:
					datos.setCalculotxt("NORMAL");
					datos.setdInv(3);
					datos.setAreaVal(10);
					break;
				case 2:
					datos.setCalculotxt("DECRETO 97");
					datos.setdInv(2);
					datos.setAreaVal(10);
					break;
				case 3:
					datos.setCalculotxt("DECRETO 2003");
					datos.setdInv(1);
					datos.setAreaVal(15);
					break;
				case 4:
					datos.setCalculotxt("NO AFECTO DECRETO 2003");
					datos.setdInv(1);
					datos.setAreaVal(10);
					break;
			}
			
		}
		
		if (datos.getMesesselect() != null)
		{
			int cuantos = 1;
			String concatena="";
			String concatenaTxt="";
			for (String datMes : datos.getMesesselect()) {
				if (datMes.equals("ENE"))
				{
					if (cuantos == 1)
					{
						concatena = "1";
						concatenaTxt="ENERO";
						cuantos++;
					}
					else
					{
						concatena += ",1";
						concatenaTxt+=",ENERO";
					}
				}
				if (datMes.equals("FEB"))
				{
					if (cuantos == 1)
					{
						concatena = "2";
						concatenaTxt="FEBRERO";
						cuantos++;
					}
					else{
						concatena += ",2";
						concatenaTxt+=",FEBRERO";
					}
					
				}
				if (datMes.equals("MZO"))
				{
					if (cuantos == 1)
					{
						concatena = "3";
						concatenaTxt+="MARZO";
						cuantos++;
					}
					else
					{
						concatena += ",3";
						concatenaTxt+=",MARZO";
					}
				}
				if (datMes.equals("ABR"))
				{
					if (cuantos == 1)
					{
						concatena = "4";
						concatenaTxt="ABRIL";
						cuantos++;
					}
					else
					{
						concatena += ",4";
						concatenaTxt+=",ABRIL";
					}
				}
				if (datMes.equals("MAY"))
				{
					if (cuantos == 1)
					{
						concatena = "5";
						concatenaTxt="MAYO";
						cuantos++;
					}
					else
					{
						concatena += ",5";
						concatenaTxt+=",MAYO";
					}
				}
				if (datMes.equals("JUN"))
				{
					if (cuantos == 1)
					{
						concatena = "6";
						concatenaTxt="JUNIO";
						cuantos++;
					}
					else
					{
						concatena += ",6";
						concatenaTxt+=",JUNIO";
					}
				}
				if (datMes.equals("JUL"))
				{
					if (cuantos == 1)
					{
						concatena = "7";
						concatenaTxt="JULIO";
						cuantos++;
					}
					else
					{
						concatena += ",7";
						concatenaTxt+=",JULIO";
					}
				}
				if (datMes.equals("AGO"))
				{
					if (cuantos == 1)
					{
						concatena = "8";
						concatenaTxt="AGOSTO";
						cuantos++;
					}
					else
					{
						concatena += ",8";
						concatenaTxt+=",AGOSTO";
					}
				}
				if (datMes.equals("SEP"))
				{
					if (cuantos == 1){
						concatena = "9";
						concatenaTxt="SEPTIEMBRE";
						cuantos++;
					}
					else
					{
						concatena += ",9";
						concatenaTxt+=",SEPTIEMBRE";
					}
				}
				if (datMes.equals("OCT"))
				{
					if (cuantos == 1){
						concatena = "10";
						cuantos++;
						concatenaTxt="OCTUBRE";
					}
					else
					{
						concatena += ",10";
						concatenaTxt+=",OCTUBRE";
					}
				}
				if (datMes.equals("NOV"))
				{
					if (cuantos == 1){
						concatena = "11";
						concatenaTxt="NOVIEMBRE";
						cuantos++;
					}
					else{
						concatena += ",11";
						concatenaTxt+=",NOVIEMBRE";
					}
				}
				if (datMes.equals("DIC"))
				{
					if (cuantos == 1){
						concatena = "12";
						concatenaTxt="DICIEMBRE";
						cuantos++;
					}
					else
					{
						concatena += ",12";
						concatenaTxt+=",DICIEMBRE";
					}
				}
				
			}
			datos.setMesReptxt(concatenaTxt);
			datos.setMesesConsulta(concatena);
		}
		
		if (datos.getClase().size() == 0)
			datos.setClaseReptxt("TODAS");
		else
		{
			String txtClase="TODAS";
			int cuantos=1;
			for (String clas : datos.getClase()) {
				for (BajasClaseBean clases : datos.getClases()) {
					if (clas.equals(clases.getClave()))
					{
						if(cuantos == 1)
						{
							txtClase = clases.getDescripcion();
							cuantos++;
						}
						else
							txtClase += ", "+ clases.getDescripcion();
					}
					
				}
				
			}
			datos.setClaseReptxt(txtClase);
		}
		if(datos.getRegion().size() == 0)
			datos.setRegionReptxt("TODAS");
		else
		{
			String txtReg="TODAS";
			int cuantos=1;
			for (String reg : datos.getRegion()) {
				for (CatRegionBean regiones : datos.getRegiones()) {
					if (reg.equals(regiones.getId()))
					{
						if(cuantos == 1)
						{
							txtReg = regiones.getDescripcion();
							cuantos++;
						}
						else
							txtReg += ", "+ regiones.getDescripcion();
					}
					
				}
				
			}
			datos.setRegionReptxt(txtReg);
		}
		if(datos.getTxtDesc().size() == 0)
		{
			datos.setTextosTitulos("TODOS LOS TEXTOS FISCALES Y NO FISCALES");
		}
			
		return datos;
	}
	
	@Override
	public InstAjusteBean consultaInstitucionalClase(InstAjusteBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException{
		InstAjusteBean inst = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
				case 1:{
					inst = institucionalAjustesDao.consultaInstitucionalAjustes(datos);
				}break;
				case 2:{
					inst = institucionalAjustesDipcelDao.consultaInstitucionalAjustes(datos);
				}break;
				case 3:{
					inst = institucionalAjustesSercotelDao.consultaInstitucionalAjustes(datos);
				}break;
				case 4:{
					inst = institucionalAjustesAmovDao.consultaInstitucionalAjustes(datos);
				}break;
			}
			
		}catch(CfeException e){
			
		}
		return inst;
	}
	
	@Override
	public InstAjusteBean consultaAjInstitucionalClase(InstAjusteBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException{
		InstAjusteBean instClase = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				instClase = institucionalAjustesDao.consultaAjInstitucionalClase(datos);
			}break;
			case 2:{
				instClase = institucionalAjustesDipcelDao.consultaAjInstitucionalClase(datos);
			}break;
			case 3:{
				instClase = institucionalAjustesSercotelDao.consultaAjInstitucionalClase(datos);
			}break;
			case 4:{
				instClase = institucionalAjustesAmovDao.consultaAjInstitucionalClase(datos);
			}break;
		}
			
		}catch(CfeException e){
			
		}
		return instClase;
	}
	
	
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public HSSFWorkbook generaDocumentoClase(InstAjusteBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));
		rowNum = rowNum +2;

				HSSFRow headerRow = sheet.createRow((short) rowNum++); 
				headerRow = sheet.createRow((short) 4);
				headerRow.createCell((short) 6).setCellValue(
				new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
				headerRow.getCell(6).setCellStyle(headerStyle);

				headerRow = sheet.createRow((short) 5); 
				headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_CLASE+": "+datos.getClaseReptxt()));
				headerRow.getCell(1).setCellStyle(headerStyle);

				headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString(Constants.TIT_INST_AJUSTE)); 
				headerRow.getCell(6).setCellStyle(headerStyle);

				headerRow.createCell((short) 16).setCellValue( 
				new HSSFRichTextString(datos.getCalculotxt())); 
				headerRow.getCell(16).setCellStyle(headerStyle);
				
				headerRow = sheet.createRow((short) 6); 
				headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REGION+": "+datos.getRegionReptxt()));
				headerRow.getCell(1).setCellStyle(headerStyle);	

				if(datos.isAcum()){
					headerRow.createCell((short) 6).setCellValue(
					new HSSFRichTextString(datos.getMesSeleccion()));
					headerRow.getCell(6).setCellStyle(headerStyle);
				}else{
					headerRow.createCell((short) 6).setCellValue(
					new HSSFRichTextString(datos.getMesSeleccion() + " " + Constants.TIT_ANIO+": "+datos.getAnio() ));
					headerRow.getCell(6).setCellStyle(headerStyle);
					
				}
				headerRow.createCell((short) 16).setCellValue( 
				new HSSFRichTextString(datos.getTextosTitulos()));
				headerRow.getCell(16).setCellStyle(headerStyle);
				
				sheet.addMergedRegion(new CellRangeAddress(4,4,6,8));
				sheet.addMergedRegion(new CellRangeAddress(5,5,1,3));
				sheet.addMergedRegion(new CellRangeAddress(5,5,6,8));
				sheet.addMergedRegion(new CellRangeAddress(5,5,16,18));
				sheet.addMergedRegion(new CellRangeAddress(6,6,1,3));
				sheet.addMergedRegion(new CellRangeAddress(6,6,6,8));
				sheet.addMergedRegion(new CellRangeAddress(6,6,16,18));
				
				rowNum = rowNum +4;
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString(""));
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString("CORPORATIVO "));
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString("TIJUANA "));
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString("HERMOSILLO "));
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString("CHIHUAHUA "));
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString("MONTERREY "));
		headerRow.createCell((short) 16).setCellValue( 
				new HSSFRichTextString("GUADALAJARA "));
		headerRow.createCell((short) 19).setCellValue( 
				new HSSFRichTextString("QUERETARO "));
		headerRow.createCell((short) 22).setCellValue( 
				new HSSFRichTextString("PUEBLA"));
		headerRow.createCell((short) 25).setCellValue( 
				new HSSFRichTextString("MERIDA"));
		headerRow.createCell((short) 28).setCellValue( 
				new HSSFRichTextString("METROPOLITANO"));
		headerRow.createCell((short) 31).setCellValue( 
				new HSSFRichTextString("TOTAL"));
		
		headerRow.getCell(0).setCellStyle(headerStyle);
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow.getCell(4).setCellStyle(headerStyle);
		headerRow.getCell(7).setCellStyle(headerStyle);
		headerRow.getCell(10).setCellStyle(headerStyle);
		headerRow.getCell(13).setCellStyle(headerStyle);
		headerRow.getCell(16).setCellStyle(headerStyle);
		headerRow.getCell(19).setCellStyle(headerStyle);
		headerRow.getCell(22).setCellStyle(headerStyle);
		headerRow.getCell(25).setCellStyle(headerStyle);
		headerRow.getCell(28).setCellStyle(headerStyle);
		headerRow.getCell(31).setCellStyle(headerStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),1,3));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),4,6));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),7,9));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),10,12));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),13,15));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),16,18));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),19,21));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),22,24));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),25,27));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),28,30));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),31,33));
		
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString("CLASE"));
		//R0
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 2).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 3).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R1
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 5).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R2
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 8).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 9).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R3
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 11).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 12).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R4
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 14).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 15).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R5
		headerRow.createCell((short) 16).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 17).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 18).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R6
		headerRow.createCell((short) 19).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 20).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 21).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R7
		headerRow.createCell((short) 22).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 23).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 24).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R8
		headerRow.createCell((short) 25).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 26).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 27).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R9
		headerRow.createCell((short) 28).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 29).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 30).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
		//R10
		headerRow.createCell((short) 31).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
		headerRow.createCell((short) 32).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_COSTO));
		headerRow.createCell((short) 33).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REP_DEPR));
	
		
		headerRow.getCell(0).setCellStyle(headerStyle);
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow.getCell(3).setCellStyle(headerStyle);
		headerRow.getCell(4).setCellStyle(headerStyle);
		headerRow.getCell(5).setCellStyle(headerStyle);
		headerRow.getCell(6).setCellStyle(headerStyle);
		headerRow.getCell(7).setCellStyle(headerStyle);
		headerRow.getCell(8).setCellStyle(headerStyle);
		headerRow.getCell(9).setCellStyle(headerStyle);
		headerRow.getCell(10).setCellStyle(headerStyle);
		headerRow.getCell(11).setCellStyle(headerStyle);
		headerRow.getCell(12).setCellStyle(headerStyle);
		headerRow.getCell(13).setCellStyle(headerStyle);
		headerRow.getCell(14).setCellStyle(headerStyle);
		headerRow.getCell(15).setCellStyle(headerStyle);
		headerRow.getCell(16).setCellStyle(headerStyle);
		headerRow.getCell(17).setCellStyle(headerStyle);
		headerRow.getCell(18).setCellStyle(headerStyle);
		headerRow.getCell(19).setCellStyle(headerStyle);
		headerRow.getCell(20).setCellStyle(headerStyle);
		headerRow.getCell(21).setCellStyle(headerStyle);
		headerRow.getCell(22).setCellStyle(headerStyle);
		headerRow.getCell(23).setCellStyle(headerStyle);
		headerRow.getCell(24).setCellStyle(headerStyle);
		headerRow.getCell(25).setCellStyle(headerStyle);
		headerRow.getCell(26).setCellStyle(headerStyle);
		headerRow.getCell(27).setCellStyle(headerStyle);
		headerRow.getCell(28).setCellStyle(headerStyle);
		headerRow.getCell(29).setCellStyle(headerStyle);
		headerRow.getCell(30).setCellStyle(headerStyle);
		headerRow.getCell(31).setCellStyle(headerStyle);
		headerRow.getCell(32).setCellStyle(headerStyle);
		headerRow.getCell(33).setCellStyle(headerStyle);
	
		for(TGClaseTresBean clase: datos.getDetClases()){
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("CLASE: " + clase.getNombre()));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,36));
			headerRow.getCell(0).setCellStyle(headerStyle);
			for(BajasTresBean texto: clase.getTextos()){
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString(texto.getTexto_baja()));
				//R00
				headerRow.createCell((short) 1).setCellValue(texto.getAdq_baja_r0() != null ? texto.getAdq_baja_r0().doubleValue():0.0);
				headerRow.createCell((short) 2).setCellValue( 
							texto.getCosto_act_r0() != null ? texto.getCosto_act_r0().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue( 
							texto.getDepre_anual_act_r0() != null ? texto.getDepre_anual_act_r0().doubleValue():0.0);
				//R01
				headerRow.createCell((short) 4).setCellValue(texto.getAdq_baja_r1() != null ? texto.getAdq_baja_r1().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue( 
							texto.getCosto_act_r1() != null ? texto.getCosto_act_r1().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue( 
							texto.getDepre_anual_act_r1() != null ? texto.getDepre_anual_act_r1().doubleValue():0.0);
				
				headerRow.createCell((short) 7).setCellValue(texto.getAdq_baja_r2() != null ? texto.getAdq_baja_r2().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue( 
							texto.getCosto_act_r2() != null ? texto.getCosto_act_r2().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
							texto.getDepre_anual_act_r2() != null ? texto.getDepre_anual_act_r2().doubleValue():0.0);
				
				headerRow.createCell((short) 10).setCellValue(texto.getAdq_baja_r3() != null ? texto.getAdq_baja_r3().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue( 
							texto.getCosto_act_r3() != null ? texto.getCosto_act_r3().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue( 
							texto.getDepre_anual_act_r3() != null ? texto.getDepre_anual_act_r3().doubleValue():0.0);
				
				headerRow.createCell((short) 13).setCellValue(texto.getAdq_baja_r4() != null ? texto.getAdq_baja_r4().doubleValue():0.0);
				headerRow.createCell((short) 14).setCellValue( 
							texto.getCosto_act_r4() != null ? texto.getCosto_act_r4().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue( 
							texto.getDepre_anual_act_r4() != null ? texto.getDepre_anual_act_r4().doubleValue():0.0);
				
				headerRow.createCell((short) 16).setCellValue(texto.getAdq_baja_r5() != null ? texto.getAdq_baja_r5().doubleValue():0.0);
				headerRow.createCell((short) 17).setCellValue( 
							texto.getCosto_act_r5() != null ? texto.getCosto_act_r5().doubleValue():0.0);
				headerRow.createCell((short) 18).setCellValue( 
							texto.getDepre_anual_act_r5() != null ? texto.getDepre_anual_act_r5().doubleValue():0.0);
				
				headerRow.createCell((short) 19).setCellValue(texto.getAdq_baja_r6() != null ? texto.getAdq_baja_r6().doubleValue():0.0);
				headerRow.createCell((short) 20).setCellValue( 
							texto.getCosto_act_r6() != null ? texto.getCosto_act_r6().doubleValue():0.0);
				headerRow.createCell((short) 21).setCellValue( 
							texto.getDepre_anual_act_r6() != null ? texto.getDepre_anual_act_r6().doubleValue():0.0);
				
				headerRow.createCell((short) 22).setCellValue(texto.getAdq_baja_r7() != null ? texto.getAdq_baja_r7().doubleValue():0.0);
				headerRow.createCell((short) 23).setCellValue( 
							texto.getCosto_act_r7() != null ? texto.getCosto_act_r7().doubleValue():0.0);
				headerRow.createCell((short) 24).setCellValue( 
							texto.getDepre_anual_act_r7() != null ? texto.getDepre_anual_act_r7().doubleValue():0.0);
				
				headerRow.createCell((short) 25).setCellValue(texto.getAdq_baja_r8() != null ? texto.getAdq_baja_r8().doubleValue():0.0);
				headerRow.createCell((short) 26).setCellValue( 
							texto.getCosto_act_r8() != null ? texto.getCosto_act_r8().doubleValue():0.0);
				headerRow.createCell((short) 27).setCellValue( 
							texto.getDepre_anual_act_r8() != null ? texto.getDepre_anual_act_r8().doubleValue():0.0);
				
				headerRow.createCell((short) 28).setCellValue(texto.getAdq_baja_r9() != null ? texto.getAdq_baja_r9().doubleValue():0.0);
				headerRow.createCell((short) 29).setCellValue( 
							texto.getCosto_act_r9() != null ? texto.getCosto_act_r9().doubleValue():0.0);
				headerRow.createCell((short) 30).setCellValue( 
							texto.getDepre_anual_act_r9() != null ? texto.getDepre_anual_act_r9().doubleValue():0.0);
				
				headerRow.createCell((short) 31).setCellValue(texto.getAdq_baja_total() != null ? texto.getAdq_baja_total().doubleValue():0.0);
				headerRow.createCell((short) 32).setCellValue( 
							texto.getCosto_act_total() != null ? texto.getCosto_act_total().doubleValue():0.0);
				headerRow.createCell((short) 33).setCellValue( 
							texto.getDepre_anual_act_total() != null ? texto.getDepre_anual_act_total().doubleValue():0.0);
					
					headerRow.getCell(1).setCellStyle(cellStyle);	
					headerRow.getCell(2).setCellStyle(cellStyle);	        	
					headerRow.getCell(3).setCellStyle(cellStyle);	        	
					headerRow.getCell(4).setCellStyle(cellStyle);	        	
					headerRow.getCell(5).setCellStyle(cellStyle);	        		
					headerRow.getCell(6).setCellStyle(cellStyle);	        	
					headerRow.getCell(7).setCellStyle(cellStyle);
					headerRow.getCell(8).setCellStyle(cellStyle);
					headerRow.getCell(9).setCellStyle(cellStyle);
					headerRow.getCell(10).setCellStyle(cellStyle);
					headerRow.getCell(11).setCellStyle(cellStyle);	
					headerRow.getCell(12).setCellStyle(cellStyle);	        	
					headerRow.getCell(13).setCellStyle(cellStyle);	        	
					headerRow.getCell(14).setCellStyle(cellStyle);	        	
					headerRow.getCell(15).setCellStyle(cellStyle);	        		
					headerRow.getCell(16).setCellStyle(cellStyle);	        	
					headerRow.getCell(17).setCellStyle(cellStyle);
					headerRow.getCell(18).setCellStyle(cellStyle);
					headerRow.getCell(19).setCellStyle(cellStyle);
					headerRow.getCell(20).setCellStyle(cellStyle);
					headerRow.getCell(21).setCellStyle(cellStyle);	
					headerRow.getCell(22).setCellStyle(cellStyle);	        	
					headerRow.getCell(23).setCellStyle(cellStyle);	        	
					headerRow.getCell(24).setCellStyle(cellStyle);	        	
					headerRow.getCell(25).setCellStyle(cellStyle);	        		
					headerRow.getCell(26).setCellStyle(cellStyle);	        	
					headerRow.getCell(27).setCellStyle(cellStyle);
					headerRow.getCell(28).setCellStyle(cellStyle);
					headerRow.getCell(29).setCellStyle(cellStyle);
					headerRow.getCell(30).setCellStyle(cellStyle);
					headerRow.getCell(31).setCellStyle(cellStyle);
					headerRow.getCell(32).setCellStyle(cellStyle);
					headerRow.getCell(33).setCellStyle(cellStyle);
			}
				headerRow = sheet.createRow((short) rowNum++);
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString("TOTAL CLASE: " + clase.getNombreCorto()));
				
				//R00
				headerRow.createCell((short) 1).setCellValue( 
						clase.getAll_adq_baja_r0() != null ? clase.getAll_adq_baja_r0().doubleValue():0.0);
				headerRow.createCell((short) 2).setCellValue( 
						clase.getAll_costo_act_r0() != null ? clase.getAll_costo_act_r0().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue( 
						clase.getAll_depre_anual_act_r0() != null ? clase.getAll_depre_anual_act_r0().doubleValue():0.0);
				
				
				headerRow.createCell((short) 4).setCellValue( 
						clase.getAll_adq_baja_r1() != null ? clase.getAll_adq_baja_r1().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue( 
						clase.getAll_costo_act_r1() != null ? clase.getAll_costo_act_r1().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue( 
						clase.getAll_depre_anual_act_r1() != null ? clase.getAll_depre_anual_act_r1().doubleValue():0.0);
				
				headerRow.createCell((short) 7).setCellValue( 
						clase.getAll_adq_baja_r2() != null ? clase.getAll_adq_baja_r2().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue( 
						clase.getAll_costo_act_r2() != null ? clase.getAll_costo_act_r2().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
						clase.getAll_depre_anual_act_r2() != null ? clase.getAll_depre_anual_act_r2().doubleValue():0.0);
				
				
				headerRow.createCell((short) 10).setCellValue( 
						clase.getAll_adq_baja_r3() != null ? clase.getAll_adq_baja_r3().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue( 
						clase.getAll_costo_act_r3() != null ? clase.getAll_costo_act_r3().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue( 
						clase.getAll_depre_anual_act_r3() != null ? clase.getAll_depre_anual_act_r3().doubleValue():0.0);
				
				headerRow.createCell((short) 13).setCellValue( 
						clase.getAll_adq_baja_r4() != null ? clase.getAll_adq_baja_r4().doubleValue():0.0);
				headerRow.createCell((short) 14).setCellValue( 
						clase.getAll_costo_act_r4() != null ? clase.getAll_costo_act_r4().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue( 
						clase.getAll_depre_anual_act_r4() != null ? clase.getAll_depre_anual_act_r4().doubleValue():0.0);
				
				headerRow.createCell((short) 16).setCellValue( 
						clase.getAll_adq_baja_r5() != null ? clase.getAll_adq_baja_r5().doubleValue():0.0);
				headerRow.createCell((short) 17).setCellValue( 
						clase.getAll_costo_act_r5() != null ? clase.getAll_costo_act_r5().doubleValue():0.0);
				headerRow.createCell((short) 18).setCellValue( 
						clase.getAll_depre_anual_act_r5() != null ? clase.getAll_depre_anual_act_r5().doubleValue():0.0);
				
				headerRow.createCell((short) 19).setCellValue( 
						clase.getAll_adq_baja_r6() != null ? clase.getAll_adq_baja_r6().doubleValue():0.0);
				headerRow.createCell((short) 20).setCellValue( 
						clase.getAll_costo_act_r6() != null ? clase.getAll_costo_act_r6().doubleValue():0.0);
				headerRow.createCell((short) 21).setCellValue( 
						clase.getAll_depre_anual_act_r6() != null ? clase.getAll_depre_anual_act_r6().doubleValue():0.0);
				
				headerRow.createCell((short) 22).setCellValue( 
						clase.getAll_adq_baja_r7() != null ? clase.getAll_adq_baja_r7().doubleValue():0.0);
				headerRow.createCell((short) 23).setCellValue( 
						clase.getAll_costo_act_r7() != null ? clase.getAll_costo_act_r7().doubleValue():0.0);
				headerRow.createCell((short) 24).setCellValue( 
						clase.getAll_depre_anual_act_r7() != null ? clase.getAll_depre_anual_act_r7().doubleValue():0.0);
				
				headerRow.createCell((short) 25).setCellValue( 
						clase.getAll_adq_baja_r8() != null ? clase.getAll_adq_baja_r8().doubleValue():0.0);
				headerRow.createCell((short) 26).setCellValue( 
						clase.getAll_costo_act_r8() != null ? clase.getAll_costo_act_r8().doubleValue():0.0);
				headerRow.createCell((short) 27).setCellValue( 
						clase.getAll_depre_anual_act_r8() != null ? clase.getAll_depre_anual_act_r8().doubleValue():0.0);
				
				headerRow.createCell((short) 28).setCellValue( 
						clase.getAll_adq_baja_r9() != null ? clase.getAll_adq_baja_r9().doubleValue():0.0);
				headerRow.createCell((short) 29).setCellValue( 
						clase.getAll_costo_act_r9() != null ? clase.getAll_costo_act_r9().doubleValue():0.0);
				headerRow.createCell((short) 30).setCellValue( 
						clase.getAll_depre_anual_act_r9() != null ? clase.getAll_depre_anual_act_r9().doubleValue():0.0);
				
				headerRow.createCell((short) 31).setCellValue( 
						clase.getAll_adq_baja_total() != null ? clase.getAll_adq_baja_total().doubleValue():0.0);
				headerRow.createCell((short) 32).setCellValue( 
						clase.getAll_costo_act_total() != null ? clase.getAll_costo_act_total().doubleValue():0.0);
				headerRow.createCell((short) 33).setCellValue( 
						clase.getAll_depre_anual_act_total() != null ? clase.getAll_depre_anual_act_total().doubleValue():0.0);
				
				headerRow.getCell(1).setCellStyle(cellStyle);
				headerRow.getCell(2).setCellStyle(cellStyle);	        	
				headerRow.getCell(3).setCellStyle(cellStyle);	        	
				headerRow.getCell(4).setCellStyle(cellStyle);	        	
				headerRow.getCell(5).setCellStyle(cellStyle);	        	
				headerRow.getCell(6).setCellStyle(cellStyle);	        		
				headerRow.getCell(7).setCellStyle(cellStyle);	        	
				headerRow.getCell(8).setCellStyle(cellStyle);
				headerRow.getCell(9).setCellStyle(cellStyle);
				headerRow.getCell(10).setCellStyle(cellStyle);
				headerRow.getCell(11).setCellStyle(cellStyle);
				headerRow.getCell(12).setCellStyle(cellStyle);	        	
				headerRow.getCell(13).setCellStyle(cellStyle);	        	
				headerRow.getCell(14).setCellStyle(cellStyle);	        	
				headerRow.getCell(15).setCellStyle(cellStyle);	        	
				headerRow.getCell(16).setCellStyle(cellStyle);	        		
				headerRow.getCell(17).setCellStyle(cellStyle);	        	
				headerRow.getCell(18).setCellStyle(cellStyle);
				headerRow.getCell(19).setCellStyle(cellStyle);
				headerRow.getCell(20).setCellStyle(cellStyle);
				headerRow.getCell(21).setCellStyle(cellStyle);
				headerRow.getCell(22).setCellStyle(cellStyle);	        	
				headerRow.getCell(23).setCellStyle(cellStyle);	        	
				headerRow.getCell(24).setCellStyle(cellStyle);	        	
				headerRow.getCell(25).setCellStyle(cellStyle);	        	
				headerRow.getCell(26).setCellStyle(cellStyle);	        		
				headerRow.getCell(27).setCellStyle(cellStyle);	        	
				headerRow.getCell(28).setCellStyle(cellStyle);
				headerRow.getCell(29).setCellStyle(cellStyle);
				headerRow.getCell(30).setCellStyle(cellStyle);
				headerRow.getCell(31).setCellStyle(cellStyle);
				headerRow.getCell(32).setCellStyle(cellStyle);
				headerRow.getCell(33).setCellStyle(cellStyle);
				
		}
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("TOTAL"));//totReporteGeneral
			headerRow.createCell((short) 1).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r0() != null ? datos.getTotReporteGeneral().getAdq_baja_r0().doubleValue():0.0);
			headerRow.createCell((short) 2).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r0() != null ? datos.getTotReporteGeneral().getCosto_act_r0().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r0() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r0().doubleValue():0.0);
			
			headerRow.createCell((short) 4).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r1() != null ? datos.getTotReporteGeneral().getAdq_baja_r1().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r1() != null ? datos.getTotReporteGeneral().getCosto_act_r1().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r1() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r1().doubleValue():0.0);
			
			headerRow.createCell((short) 7).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r2() != null ? datos.getTotReporteGeneral().getAdq_baja_r2().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r2() != null ? datos.getTotReporteGeneral().getCosto_act_r2().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r2() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r2().doubleValue():0.0);
			
			headerRow.createCell((short) 10).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r3() != null ? datos.getTotReporteGeneral().getAdq_baja_r3().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r3() != null ? datos.getTotReporteGeneral().getCosto_act_r3().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r3() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r3().doubleValue():0.0);
			
			headerRow.createCell((short) 13).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r4() != null ? datos.getTotReporteGeneral().getAdq_baja_r4().doubleValue():0.0);
			headerRow.createCell((short) 14).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r4() != null ? datos.getTotReporteGeneral().getCosto_act_r4().doubleValue():0.0);
			headerRow.createCell((short) 15).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r4() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r4().doubleValue():0.0);
			
			headerRow.createCell((short) 16).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r5() != null ? datos.getTotReporteGeneral().getAdq_baja_r5().doubleValue():0.0);
			headerRow.createCell((short) 17).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r5() != null ? datos.getTotReporteGeneral().getCosto_act_r5().doubleValue():0.0);
			headerRow.createCell((short) 18).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r5() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r5().doubleValue():0.0);
			
			headerRow.createCell((short) 19).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r6() != null ? datos.getTotReporteGeneral().getAdq_baja_r6().doubleValue():0.0);
			headerRow.createCell((short) 20).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r6() != null ? datos.getTotReporteGeneral().getCosto_act_r6().doubleValue():0.0);
			headerRow.createCell((short) 21).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r6() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r6().doubleValue():0.0);
			
			headerRow.createCell((short)22).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r7() != null ? datos.getTotReporteGeneral().getAdq_baja_r7().doubleValue():0.0);
			headerRow.createCell((short) 23).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r7() != null ? datos.getTotReporteGeneral().getCosto_act_r7().doubleValue():0.0);
			headerRow.createCell((short) 24).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r7() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r7().doubleValue():0.0);
			
			headerRow.createCell((short) 25).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r8() != null ? datos.getTotReporteGeneral().getAdq_baja_r8().doubleValue():0.0);
			headerRow.createCell((short) 26).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r8() != null ? datos.getTotReporteGeneral().getCosto_act_r8().doubleValue():0.0);
			headerRow.createCell((short) 27).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r8() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r8().doubleValue():0.0);
			
			headerRow.createCell((short) 28).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_r9() != null ? datos.getTotReporteGeneral().getAdq_baja_r9().doubleValue():0.0);
			headerRow.createCell((short) 29).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_r9() != null ? datos.getTotReporteGeneral().getCosto_act_r9().doubleValue():0.0);
			headerRow.createCell((short) 30).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_r9() != null ? datos.getTotReporteGeneral().getDepre_anual_act_r9().doubleValue():0.0);
			
			headerRow.createCell((short) 31).setCellValue( 
					datos.getTotReporteGeneral().getAdq_baja_total() != null ? datos.getTotReporteGeneral().getAdq_baja_total().doubleValue():0.0);
			headerRow.createCell((short) 32).setCellValue( 
					datos.getTotReporteGeneral().getCosto_act_total() != null ? datos.getTotReporteGeneral().getCosto_act_total().doubleValue():0.0);
			headerRow.createCell((short) 33).setCellValue( 
					datos.getTotReporteGeneral().getDepre_anual_act_total() != null ? datos.getTotReporteGeneral().getDepre_anual_act_total().doubleValue():0.0);
			
			headerRow.getCell(1).setCellStyle(cellStyle);
			headerRow.getCell(2).setCellStyle(cellStyle);	        	
			headerRow.getCell(3).setCellStyle(cellStyle);	        	
			headerRow.getCell(4).setCellStyle(cellStyle);	        	
			headerRow.getCell(5).setCellStyle(cellStyle);	        	
			headerRow.getCell(6).setCellStyle(cellStyle);	        		
			headerRow.getCell(7).setCellStyle(cellStyle);	        	
			headerRow.getCell(8).setCellStyle(cellStyle);
			headerRow.getCell(9).setCellStyle(cellStyle);
			headerRow.getCell(10).setCellStyle(cellStyle);
			headerRow.getCell(11).setCellStyle(cellStyle);
			headerRow.getCell(12).setCellStyle(cellStyle);	        	
			headerRow.getCell(13).setCellStyle(cellStyle);	        	
			headerRow.getCell(14).setCellStyle(cellStyle);	        	
			headerRow.getCell(15).setCellStyle(cellStyle);	        	
			headerRow.getCell(16).setCellStyle(cellStyle);	        		
			headerRow.getCell(17).setCellStyle(cellStyle);	        	
			headerRow.getCell(18).setCellStyle(cellStyle);
			headerRow.getCell(19).setCellStyle(cellStyle);
			headerRow.getCell(20).setCellStyle(cellStyle);
			headerRow.getCell(21).setCellStyle(cellStyle);
			headerRow.getCell(22).setCellStyle(cellStyle);	        	
			headerRow.getCell(23).setCellStyle(cellStyle);	        	
			headerRow.getCell(24).setCellStyle(cellStyle);	        	
			headerRow.getCell(25).setCellStyle(cellStyle);	        	
			headerRow.getCell(26).setCellStyle(cellStyle);	        		
			headerRow.getCell(27).setCellStyle(cellStyle);	        	
			headerRow.getCell(28).setCellStyle(cellStyle);
			headerRow.getCell(29).setCellStyle(cellStyle);
			headerRow.getCell(30).setCellStyle(cellStyle);
			headerRow.getCell(31).setCellStyle(cellStyle);
			headerRow.getCell(32).setCellStyle(cellStyle);
			headerRow.getCell(33).setCellStyle(cellStyle);

			rowNum++;
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_REGION));
			rowNum++;
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString(""));
			headerRow.createCell((short) 1).setCellValue( 
					new HSSFRichTextString("CORPORATIVO "));
			headerRow.createCell((short) 4).setCellValue( 
					new HSSFRichTextString("TIJUANA "));
			headerRow.createCell((short) 7).setCellValue( 
					new HSSFRichTextString("HERMOSILLO "));
			headerRow.createCell((short) 10).setCellValue( 
					new HSSFRichTextString("CHIHUAHUA "));
			headerRow.createCell((short) 13).setCellValue( 
					new HSSFRichTextString("MONTERREY "));
			headerRow.createCell((short) 16).setCellValue( 
					new HSSFRichTextString("GUADALAJARA "));
			headerRow.createCell((short) 19).setCellValue( 
					new HSSFRichTextString("QUERETARO "));
			headerRow.createCell((short) 22).setCellValue( 
					new HSSFRichTextString("PUEBLA"));
			headerRow.createCell((short) 25).setCellValue( 
					new HSSFRichTextString("MERIDA"));
			headerRow.createCell((short) 28).setCellValue( 
					new HSSFRichTextString("METROPOLITANO"));
			headerRow.createCell((short) 31).setCellValue( 
					new HSSFRichTextString("TOTAL"));
			
			headerRow.getCell(0).setCellStyle(headerStyle);
			headerRow.getCell(1).setCellStyle(headerStyle);
			headerRow.getCell(4).setCellStyle(headerStyle);
			headerRow.getCell(7).setCellStyle(headerStyle);
			headerRow.getCell(10).setCellStyle(headerStyle);
			headerRow.getCell(13).setCellStyle(headerStyle);
			headerRow.getCell(16).setCellStyle(headerStyle);
			headerRow.getCell(19).setCellStyle(headerStyle);
			headerRow.getCell(22).setCellStyle(headerStyle);
			headerRow.getCell(25).setCellStyle(headerStyle);
			headerRow.getCell(28).setCellStyle(headerStyle);
			headerRow.getCell(31).setCellStyle(headerStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),1,3));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),4,6));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),7,9));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),10,12));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),13,15));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),16,18));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),19,21));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),22,24));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),25,27));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),28,30));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),31,33));
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("TEXTO"));
			//R0
			headerRow.createCell((short) 1).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 2).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 3).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R1
			headerRow.createCell((short) 4).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 5).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 6).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R2
			headerRow.createCell((short) 7).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 8).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 9).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R3
			headerRow.createCell((short) 10).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 11).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 12).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R4
			headerRow.createCell((short) 13).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 14).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 15).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R5
			headerRow.createCell((short) 16).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 17).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 18).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R6
			headerRow.createCell((short) 19).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 20).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 21).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R7
			headerRow.createCell((short) 22).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 23).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 24).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R8
			headerRow.createCell((short) 25).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 26).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 27).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R9
			headerRow.createCell((short) 28).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 29).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 30).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
			//R10
			headerRow.createCell((short) 31).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_ADQ_BAJA));
			headerRow.createCell((short) 32).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_COSTO));
			headerRow.createCell((short) 33).setCellValue( 
					new HSSFRichTextString(Constants.TIT_REP_DEPR));
		
			
			headerRow.getCell(0).setCellStyle(headerStyle);
			headerRow.getCell(1).setCellStyle(headerStyle);
			headerRow.getCell(2).setCellStyle(headerStyle);
			headerRow.getCell(3).setCellStyle(headerStyle);
			headerRow.getCell(4).setCellStyle(headerStyle);
			headerRow.getCell(5).setCellStyle(headerStyle);
			headerRow.getCell(6).setCellStyle(headerStyle);
			headerRow.getCell(7).setCellStyle(headerStyle);
			headerRow.getCell(8).setCellStyle(headerStyle);
			headerRow.getCell(9).setCellStyle(headerStyle);
			headerRow.getCell(10).setCellStyle(headerStyle);
			headerRow.getCell(11).setCellStyle(headerStyle);
			headerRow.getCell(12).setCellStyle(headerStyle);
			headerRow.getCell(13).setCellStyle(headerStyle);
			headerRow.getCell(14).setCellStyle(headerStyle);
			headerRow.getCell(15).setCellStyle(headerStyle);
			headerRow.getCell(16).setCellStyle(headerStyle);
			headerRow.getCell(17).setCellStyle(headerStyle);
			headerRow.getCell(18).setCellStyle(headerStyle);
			headerRow.getCell(19).setCellStyle(headerStyle);
			headerRow.getCell(20).setCellStyle(headerStyle);
			headerRow.getCell(21).setCellStyle(headerStyle);
			headerRow.getCell(22).setCellStyle(headerStyle);
			headerRow.getCell(23).setCellStyle(headerStyle);
			headerRow.getCell(24).setCellStyle(headerStyle);
			headerRow.getCell(25).setCellStyle(headerStyle);
			headerRow.getCell(26).setCellStyle(headerStyle);
			headerRow.getCell(27).setCellStyle(headerStyle);
			headerRow.getCell(28).setCellStyle(headerStyle);
			headerRow.getCell(29).setCellStyle(headerStyle);
			headerRow.getCell(30).setCellStyle(headerStyle);
			headerRow.getCell(31).setCellStyle(headerStyle);
			headerRow.getCell(32).setCellStyle(headerStyle);
			headerRow.getCell(33).setCellStyle(headerStyle);
		
			

			for(BajasTresBeanResumen texto: datos.getDetResumen()){
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString(texto.getTexto_baja()));
				//R00
				headerRow.createCell((short) 1).setCellValue(texto.getAdq_baja_r0() != null ? texto.getAdq_baja_r0().doubleValue():0.0);
				headerRow.createCell((short) 2).setCellValue( 
							texto.getCosto_act_r0() != null ? texto.getCosto_act_r0().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue( 
							texto.getDepre_anual_act_r0() != null ? texto.getDepre_anual_act_r0().doubleValue():0.0);
				//R01
				headerRow.createCell((short) 4).setCellValue(texto.getAdq_baja_r1() != null ? texto.getAdq_baja_r1().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue( 
							texto.getCosto_act_r1() != null ? texto.getCosto_act_r1().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue( 
							texto.getDepre_anual_act_r1() != null ? texto.getDepre_anual_act_r1().doubleValue():0.0);
				
				headerRow.createCell((short) 7).setCellValue(texto.getAdq_baja_r2() != null ? texto.getAdq_baja_r2().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue( 
							texto.getCosto_act_r2() != null ? texto.getCosto_act_r2().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
							texto.getDepre_anual_act_r2() != null ? texto.getDepre_anual_act_r2().doubleValue():0.0);
				
				headerRow.createCell((short) 10).setCellValue(texto.getAdq_baja_r3() != null ? texto.getAdq_baja_r3().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue( 
							texto.getCosto_act_r3() != null ? texto.getCosto_act_r3().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue( 
							texto.getDepre_anual_act_r3() != null ? texto.getDepre_anual_act_r3().doubleValue():0.0);
				
				headerRow.createCell((short) 13).setCellValue(texto.getAdq_baja_r4() != null ? texto.getAdq_baja_r4().doubleValue():0.0);
				headerRow.createCell((short) 14).setCellValue( 
							texto.getCosto_act_r4() != null ? texto.getCosto_act_r4().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue( 
							texto.getDepre_anual_act_r4() != null ? texto.getDepre_anual_act_r4().doubleValue():0.0);
				
				headerRow.createCell((short) 16).setCellValue(texto.getAdq_baja_r5() != null ? texto.getAdq_baja_r5().doubleValue():0.0);
				headerRow.createCell((short) 17).setCellValue( 
							texto.getCosto_act_r5() != null ? texto.getCosto_act_r5().doubleValue():0.0);
				headerRow.createCell((short) 18).setCellValue( 
							texto.getDepre_anual_act_r5() != null ? texto.getDepre_anual_act_r5().doubleValue():0.0);
				
				headerRow.createCell((short) 19).setCellValue(texto.getAdq_baja_r6() != null ? texto.getAdq_baja_r6().doubleValue():0.0);
				headerRow.createCell((short) 20).setCellValue( 
							texto.getCosto_act_r6() != null ? texto.getCosto_act_r6().doubleValue():0.0);
				headerRow.createCell((short) 21).setCellValue( 
							texto.getDepre_anual_act_r6() != null ? texto.getDepre_anual_act_r6().doubleValue():0.0);
				
				headerRow.createCell((short) 22).setCellValue(texto.getAdq_baja_r7() != null ? texto.getAdq_baja_r7().doubleValue():0.0);
				headerRow.createCell((short) 23).setCellValue( 
							texto.getCosto_act_r7() != null ? texto.getCosto_act_r7().doubleValue():0.0);
				headerRow.createCell((short) 24).setCellValue( 
							texto.getDepre_anual_act_r7() != null ? texto.getDepre_anual_act_r7().doubleValue():0.0);
				
				headerRow.createCell((short) 25).setCellValue(texto.getAdq_baja_r8() != null ? texto.getAdq_baja_r8().doubleValue():0.0);
				headerRow.createCell((short) 26).setCellValue( 
							texto.getCosto_act_r8() != null ? texto.getCosto_act_r8().doubleValue():0.0);
				headerRow.createCell((short) 27).setCellValue( 
							texto.getDepre_anual_act_r8() != null ? texto.getDepre_anual_act_r8().doubleValue():0.0);
				
				headerRow.createCell((short) 28).setCellValue(texto.getAdq_baja_r9() != null ? texto.getAdq_baja_r9().doubleValue():0.0);
				headerRow.createCell((short) 29).setCellValue( 
							texto.getCosto_act_r9() != null ? texto.getCosto_act_r9().doubleValue():0.0);
				headerRow.createCell((short) 30).setCellValue( 
							texto.getDepre_anual_act_r9() != null ? texto.getDepre_anual_act_r9().doubleValue():0.0);
				
				headerRow.createCell((short) 31).setCellValue(texto.getAdq_baja_total() != null ? texto.getAdq_baja_total().doubleValue():0.0);
				headerRow.createCell((short) 32).setCellValue( 
							texto.getCosto_act_total() != null ? texto.getCosto_act_total().doubleValue():0.0);
				headerRow.createCell((short) 33).setCellValue( 
							texto.getDepre_anual_act_total() != null ? texto.getDepre_anual_act_total().doubleValue():0.0);
					
					headerRow.getCell(1).setCellStyle(cellStyle);	
					headerRow.getCell(2).setCellStyle(cellStyle);	        	
					headerRow.getCell(3).setCellStyle(cellStyle);	        	
					headerRow.getCell(4).setCellStyle(cellStyle);	        	
					headerRow.getCell(5).setCellStyle(cellStyle);	        		
					headerRow.getCell(6).setCellStyle(cellStyle);	        	
					headerRow.getCell(7).setCellStyle(cellStyle);
					headerRow.getCell(8).setCellStyle(cellStyle);
					headerRow.getCell(9).setCellStyle(cellStyle);
					headerRow.getCell(10).setCellStyle(cellStyle);
					headerRow.getCell(11).setCellStyle(cellStyle);	
					headerRow.getCell(12).setCellStyle(cellStyle);	        	
					headerRow.getCell(13).setCellStyle(cellStyle);	        	
					headerRow.getCell(14).setCellStyle(cellStyle);	        	
					headerRow.getCell(15).setCellStyle(cellStyle);	        		
					headerRow.getCell(16).setCellStyle(cellStyle);	        	
					headerRow.getCell(17).setCellStyle(cellStyle);
					headerRow.getCell(18).setCellStyle(cellStyle);
					headerRow.getCell(19).setCellStyle(cellStyle);
					headerRow.getCell(20).setCellStyle(cellStyle);
					headerRow.getCell(21).setCellStyle(cellStyle);	
					headerRow.getCell(22).setCellStyle(cellStyle);	        	
					headerRow.getCell(23).setCellStyle(cellStyle);	        	
					headerRow.getCell(24).setCellStyle(cellStyle);	        	
					headerRow.getCell(25).setCellStyle(cellStyle);	        		
					headerRow.getCell(26).setCellStyle(cellStyle);	        	
					headerRow.getCell(27).setCellStyle(cellStyle);
					headerRow.getCell(28).setCellStyle(cellStyle);
					headerRow.getCell(29).setCellStyle(cellStyle);
					headerRow.getCell(30).setCellStyle(cellStyle);
					headerRow.getCell(31).setCellStyle(cellStyle);
					headerRow.getCell(32).setCellStyle(cellStyle);
					headerRow.getCell(33).setCellStyle(cellStyle);
			}
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("TOTAL"));
			headerRow.createCell((short) 1).setCellValue( 
					datos.getTotResumen().getAdq_baja_r0() != null ? datos.getTotResumen().getAdq_baja_r0().doubleValue():0.0);
			headerRow.createCell((short) 2).setCellValue( 
					datos.getTotResumen().getCosto_act_r0() != null ? datos.getTotResumen().getCosto_act_r0().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r0() != null ? datos.getTotResumen().getDepre_anual_act_r0().doubleValue():0.0);
			
			headerRow.createCell((short) 4).setCellValue( 
					datos.getTotResumen().getAdq_baja_r1() != null ? datos.getTotResumen().getAdq_baja_r1().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue( 
					datos.getTotResumen().getCosto_act_r1() != null ? datos.getTotResumen().getCosto_act_r1().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r1() != null ? datos.getTotResumen().getDepre_anual_act_r1().doubleValue():0.0);
			
			headerRow.createCell((short) 7).setCellValue( 
					datos.getTotResumen().getAdq_baja_r2() != null ? datos.getTotResumen().getAdq_baja_r2().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue( 
					datos.getTotResumen().getCosto_act_r2() != null ? datos.getTotResumen().getCosto_act_r2().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r2() != null ? datos.getTotResumen().getDepre_anual_act_r2().doubleValue():0.0);
			
			headerRow.createCell((short) 10).setCellValue( 
					datos.getTotResumen().getAdq_baja_r3() != null ? datos.getTotResumen().getAdq_baja_r3().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue( 
					datos.getTotResumen().getCosto_act_r3() != null ? datos.getTotResumen().getCosto_act_r3().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r3() != null ? datos.getTotResumen().getDepre_anual_act_r3().doubleValue():0.0);
			
			headerRow.createCell((short) 13).setCellValue( 
					datos.getTotResumen().getAdq_baja_r4() != null ? datos.getTotResumen().getAdq_baja_r4().doubleValue():0.0);
			headerRow.createCell((short) 14).setCellValue( 
					datos.getTotResumen().getCosto_act_r4() != null ? datos.getTotResumen().getCosto_act_r4().doubleValue():0.0);
			headerRow.createCell((short) 15).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r4() != null ? datos.getTotResumen().getDepre_anual_act_r4().doubleValue():0.0);
			
			headerRow.createCell((short) 16).setCellValue( 
					datos.getTotResumen().getAdq_baja_r5() != null ? datos.getTotResumen().getAdq_baja_r5().doubleValue():0.0);
			headerRow.createCell((short) 17).setCellValue( 
					datos.getTotResumen().getCosto_act_r5() != null ? datos.getTotResumen().getCosto_act_r5().doubleValue():0.0);
			headerRow.createCell((short) 18).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r5() != null ? datos.getTotResumen().getDepre_anual_act_r5().doubleValue():0.0);
			
			headerRow.createCell((short) 19).setCellValue( 
					datos.getTotResumen().getAdq_baja_r6() != null ? datos.getTotResumen().getAdq_baja_r6().doubleValue():0.0);
			headerRow.createCell((short) 20).setCellValue( 
					datos.getTotResumen().getCosto_act_r6() != null ? datos.getTotResumen().getCosto_act_r6().doubleValue():0.0);
			headerRow.createCell((short) 21).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r6() != null ? datos.getTotResumen().getDepre_anual_act_r6().doubleValue():0.0);
			
			headerRow.createCell((short)22).setCellValue( 
					datos.getTotResumen().getAdq_baja_r7() != null ? datos.getTotResumen().getAdq_baja_r7().doubleValue():0.0);
			headerRow.createCell((short) 23).setCellValue( 
					datos.getTotResumen().getCosto_act_r7() != null ? datos.getTotResumen().getCosto_act_r7().doubleValue():0.0);
			headerRow.createCell((short) 24).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r7() != null ? datos.getTotResumen().getDepre_anual_act_r7().doubleValue():0.0);
			
			headerRow.createCell((short) 25).setCellValue( 
					datos.getTotResumen().getAdq_baja_r8() != null ? datos.getTotResumen().getAdq_baja_r8().doubleValue():0.0);
			headerRow.createCell((short) 26).setCellValue( 
					datos.getTotResumen().getCosto_act_r8() != null ? datos.getTotResumen().getCosto_act_r8().doubleValue():0.0);
			headerRow.createCell((short) 27).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r8() != null ? datos.getTotResumen().getDepre_anual_act_r8().doubleValue():0.0);
			
			headerRow.createCell((short) 28).setCellValue( 
					datos.getTotResumen().getAdq_baja_r9() != null ? datos.getTotResumen().getAdq_baja_r9().doubleValue():0.0);
			headerRow.createCell((short) 29).setCellValue( 
					datos.getTotResumen().getCosto_act_r9() != null ? datos.getTotResumen().getCosto_act_r9().doubleValue():0.0);
			headerRow.createCell((short) 30).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_r9() != null ? datos.getTotResumen().getDepre_anual_act_r9().doubleValue():0.0);
			
			headerRow.createCell((short) 31).setCellValue( 
					datos.getTotResumen().getAdq_baja_total() != null ? datos.getTotResumen().getAdq_baja_total().doubleValue():0.0);
			headerRow.createCell((short) 32).setCellValue( 
					datos.getTotResumen().getCosto_act_total() != null ? datos.getTotResumen().getCosto_act_total().doubleValue():0.0);
			headerRow.createCell((short) 33).setCellValue( 
					datos.getTotResumen().getDepre_anual_act_total() != null ? datos.getTotResumen().getDepre_anual_act_total().doubleValue():0.0);
			
			headerRow.getCell(1).setCellStyle(cellStyle);
			headerRow.getCell(2).setCellStyle(cellStyle);	        	
			headerRow.getCell(3).setCellStyle(cellStyle);	        	
			headerRow.getCell(4).setCellStyle(cellStyle);	        	
			headerRow.getCell(5).setCellStyle(cellStyle);	        	
			headerRow.getCell(6).setCellStyle(cellStyle);	        		
			headerRow.getCell(7).setCellStyle(cellStyle);	        	
			headerRow.getCell(8).setCellStyle(cellStyle);
			headerRow.getCell(9).setCellStyle(cellStyle);
			headerRow.getCell(10).setCellStyle(cellStyle);
			headerRow.getCell(11).setCellStyle(cellStyle);
			headerRow.getCell(12).setCellStyle(cellStyle);	        	
			headerRow.getCell(13).setCellStyle(cellStyle);	        	
			headerRow.getCell(14).setCellStyle(cellStyle);	        	
			headerRow.getCell(15).setCellStyle(cellStyle);	        	
			headerRow.getCell(16).setCellStyle(cellStyle);	        		
			headerRow.getCell(17).setCellStyle(cellStyle);	        	
			headerRow.getCell(18).setCellStyle(cellStyle);
			headerRow.getCell(19).setCellStyle(cellStyle);
			headerRow.getCell(20).setCellStyle(cellStyle);
			headerRow.getCell(21).setCellStyle(cellStyle);
			headerRow.getCell(22).setCellStyle(cellStyle);	        	
			headerRow.getCell(23).setCellStyle(cellStyle);	        	
			headerRow.getCell(24).setCellStyle(cellStyle);	        	
			headerRow.getCell(25).setCellStyle(cellStyle);	        	
			headerRow.getCell(26).setCellStyle(cellStyle);	        		
			headerRow.getCell(27).setCellStyle(cellStyle);	        	
			headerRow.getCell(28).setCellStyle(cellStyle);
			headerRow.getCell(29).setCellStyle(cellStyle);
			headerRow.getCell(30).setCellStyle(cellStyle);
			headerRow.getCell(31).setCellStyle(cellStyle);
			headerRow.getCell(32).setCellStyle(cellStyle);
			headerRow.getCell(33).setCellStyle(cellStyle);

			
		//IMAGEN
        StringBuffer fullPath = new StringBuffer();
        fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
        fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		 if (datos.getDatSoc().equals("TELCEL"))
	        {
	        	fullPath.append("TelceLogo3.gif");
	        }
	        if (datos.getDatSoc().equals("SERCOTEL"))
	        {
	        	fullPath.append("logoSercotel.jpg");
	        }
	        if (datos.getDatSoc().equals("AMERICA MOVIL"))
	        {
	        	fullPath.append("logoAM.png");
	        }
			
	        InputStream inputStream = new FileInputStream(fullPath.toString());
	        
	        byte[] bytes = IOUtils.toByteArray(inputStream);
	        int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
	        inputStream.close();
	        
	        CreationHelper helper = workbook.getCreationHelper();
	        HSSFPatriarch drawing = sheet.createDrawingPatriarch();
	        ClientAnchor anchor = helper.createClientAnchor();
	        anchor.setCol1(0);
	        anchor.setRow1(1);
	        Picture pict = drawing.createPicture(anchor, pictureIdx);
	        pict.resize();
			return workbook;
	}

}
