package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.CostoActAmovDao;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.CostoBean;
import com.telcel.gsa.dsaf.bean.RepAdqClasBean;
import com.telcel.gsa.dsaf.bean.RepAdqRegBean;
import com.telcel.gsa.dsaf.bean.RepAdqTextoBean;
import com.telcel.gsa.dsaf.dao.CostoActDao;
import com.telcel.gsa.dsaf.dipcel.dao.CostoActDipselDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.CostoActSercotelDao;
import com.telcel.gsa.dsaf.service.CostoService;
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
@Service("costoService")
public class CostoServiceImpl implements CostoService, Serializable{

	
	@Autowired
	@Qualifier("costoActDaoImpl")
	CostoActDao costoActDao;
	@Autowired
	@Qualifier("costoActDipselDaoImpl")
	CostoActDipselDao costoActDipselDao;
	@Autowired
	@Qualifier("costoActSercotelDaoImpl")
	CostoActSercotelDao costoActSercotelDao;
	@Autowired
	@Qualifier("costoActAmovDaoImpl")
	CostoActAmovDao costoActAmovDao;
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public CostoBean DatosWhere(CostoBean datos) {
		
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
		if (datos.getAjuste() != null && datos.getAjuste().getClave() != null)
		{
			
			if(datos.getAjuste().getClave().equals("TA")){
				datos.getAjuste().setDescripcion("TODOS LOS AJUSTES");
			}else if(datos.getAjuste().getClave().equals("N")){
				datos.getAjuste().setDescripcion("NETOS");
			}else{
				datos.getAjuste().setClave("NA");
				datos.getAjuste().setDescripcion("NINGUNO");
			}
		}else{
			datos.setAjuste(new BajasAjustesBean());
			datos.getAjuste().setClave("NA");
			datos.getAjuste().setDescripcion("NINGUNO");
		}
			
		return datos;
	}

		
	@Override
	public CostoBean ConsultaReg(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaRegion(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaRegion(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaRegion(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaRegion(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}
	
	
	
	@Override
	public CostoBean ConsultaRegAjus(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaRegionAjustes(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaRegionAjustes(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaRegionAjustes(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaRegionAjustes(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}

	@Override
	public CostoBean ConsultaRegNetos(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaRegionNetos(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaRegionNetos(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaRegionNetos(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaRegionNetos(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaRegionNetos(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook generaDocumento(CostoBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a conbinar
		sheet.addMergedRegion(new CellRangeAddress(0,0,2,12));
		sheet.addMergedRegion(new CellRangeAddress(1,1,2,12));
		sheet.addMergedRegion(new CellRangeAddress(2,2,2,12));
		sheet.addMergedRegion(new CellRangeAddress(3,3,2,12));
		sheet.addMergedRegion(new CellRangeAddress(4,4,2,12));
		sheet.addMergedRegion(new CellRangeAddress(5,5,2,12));
		sheet.addMergedRegion(new CellRangeAddress(6,6,2,12));
		sheet.addMergedRegion(new CellRangeAddress(7,7,2,12));
		sheet.addMergedRegion(new CellRangeAddress(8,8,2,12));
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));
		cellStyle.setAlignment(headerStyle.ALIGN_RIGHT);
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setAlignment(headerStyle.ALIGN_LEFT);
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_COSTO_REGION)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_MES+": "+datos.getMesSeleccion()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_CLASE+": "+datos.getClaseReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REGION+": "+datos.getRegionReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getTextosTitulos()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_JUSTES+": "+datos.getAjuste().getDescripcion()));
		headerRow.getCell(2).setCellStyle(headerStyle);

		rowNum++;
		//header de tabla
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString(Constants.TIT_REGION));
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ENE)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 2).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_FEB)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 3).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ABR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 5).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAY)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUN)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUL)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 8).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_AGO)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 9).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_SEP)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_OCT)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 11).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_NOV)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 12).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_DIC)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_TOTAL));
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
		
		for (RepAdqRegBean datlist : datos.getListRepAdqReg()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getRegion()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalRegion() != null ? datlist.getTotalRegion().toString():"0.00");
			
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
			}
			
		
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().toString():"0.00");
			
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
			
		if (datos.getAjuste().getClave().equals("TA"))
		{
			rowNum++;
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_JUSTES_COSTO));
			
			for (RepAdqRegBean datlist : datos.getListRepAjAdqReg()) {
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getRegion()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalRegion() != null ? datlist.getTotalRegion().toString():"0.00");
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				}

				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().toString():"0.00");
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				rowNum++;
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_GRAN_TOT_AJUST));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().toString():"0.00");
				
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
		}	
		//IMEGEN
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


	@Override
	public CostoBean ConsultaClas(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaClase(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaClase(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaClase(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaClase(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaClase(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@Override
	public CostoBean ConsultaClasAjus(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaClaseAjustes(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaClaseAjustes(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaClaseAjustes(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaClaseAjustes(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaClaseAjustes(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@Override
	public CostoBean ConsultaClasNetos(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaClaseNetos(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaClaseNetos(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaClaseNetos(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaClaseNetos(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaClaseNetos(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@SuppressWarnings("deprecation")
	@Override
	public HSSFWorkbook generaDocumentoClas(CostoBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a conbinar
		sheet.addMergedRegion(new CellRangeAddress(0,0,2,12));
		sheet.addMergedRegion(new CellRangeAddress(1,1,2,12));
		sheet.addMergedRegion(new CellRangeAddress(2,2,2,12));
		sheet.addMergedRegion(new CellRangeAddress(3,3,2,12));
		sheet.addMergedRegion(new CellRangeAddress(4,4,2,12));
		sheet.addMergedRegion(new CellRangeAddress(5,5,2,12));
		sheet.addMergedRegion(new CellRangeAddress(6,6,2,12));
		sheet.addMergedRegion(new CellRangeAddress(7,7,2,12));
		sheet.addMergedRegion(new CellRangeAddress(8,8,2,12));
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));
		cellStyle.setAlignment(headerStyle.ALIGN_RIGHT);
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setAlignment(headerStyle.ALIGN_LEFT);
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_COSTO_CLASE)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_MES+": "+datos.getMesSeleccion()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_CLASE+": "+datos.getClaseReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REGION+": "+datos.getRegionReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getTextosTitulos()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_JUSTES+": "+datos.getAjuste().getDescripcion()));
		headerRow.getCell(2).setCellStyle(headerStyle);

		rowNum++;
		//header de tabla
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString(datos.getTitReptxt()));
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ENE)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 2).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_FEB)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 3).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ABR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 5).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAY)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUN)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUL)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 8).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_AGO)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 9).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_SEP)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_OCT)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 11).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_NOV)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 12).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_DIC)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_TOTAL));
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
		
		for (RepAdqClasBean datlist : datos.getListRepAdqClas()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getClase()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalClase() != null ? datlist.getTotalClase().toString():"0.00");
			
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
			}
			
		
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().toString():"0.00");
			
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
		if (datos.getAjuste().getClave().equals("TA"))
		{
			rowNum++;
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_JUSTES_COSTO));
											   
//			for (RepAdqClasBean datlist : datos.getListRepAdqClas()) {
			for (RepAdqClasBean datlist : datos.getListRepAjAdqClas()) {	
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getClase()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalClase() != null ? datlist.getTotalClase().toString():"0.00");
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				}

				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().toString():"0.00");
				

				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				rowNum++;
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_GRAN_TOT_AJUST));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().toString():"0.00");
				
				

				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
		}	
		//IMEGEN
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


	@Override
	public CostoBean ConsultaText(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaTexto(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaTexto(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaTexto(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaTexto(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaTexto(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@Override
	public CostoBean ConsultaTextAjus(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaTextoAjustes(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaTextoAjustes(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaTextoAjustes(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaTextoAjustes(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaTextoAjustes(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@Override
	public CostoBean ConsultaTextNetos(CostoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException {
//		datos = costoActDao.consultaTextoNetos(datos);
		try{
			switch(sessionData.getSociedad().getId()){
				case 1:{
					datos = costoActDao.consultaTextoNetos(datos);
				}break;
				case 2:{
					datos = costoActDipselDao.consultaTextoNetos(datos);
				}break;
				case 3:{
					datos = costoActSercotelDao.consultaTextoNetos(datos);
				}break;
				case 4:{
					datos = costoActAmovDao.consultaTextoNetos(datos);
				}break;
			}
			
			
			}catch(CfeException e){
			throw e;
		}
		return datos;
	}


	@Override
	public HSSFWorkbook generaDocumentoText(CostoBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a conbinar
		sheet.addMergedRegion(new CellRangeAddress(0,0,2,12));
		sheet.addMergedRegion(new CellRangeAddress(1,1,2,12));
		sheet.addMergedRegion(new CellRangeAddress(2,2,2,12));
		sheet.addMergedRegion(new CellRangeAddress(3,3,2,12));
		sheet.addMergedRegion(new CellRangeAddress(4,4,2,12));
		sheet.addMergedRegion(new CellRangeAddress(5,5,2,12));
		sheet.addMergedRegion(new CellRangeAddress(6,6,2,12));
		sheet.addMergedRegion(new CellRangeAddress(7,7,2,12));
		sheet.addMergedRegion(new CellRangeAddress(8,8,2,12));
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));
		cellStyle.setAlignment(headerStyle.ALIGN_RIGHT);
		
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setAlignment(headerStyle.ALIGN_LEFT);
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_COSTO_TEXTO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_MES+": "+datos.getMesSeleccion()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_CLASE+": "+datos.getClaseReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REGION+": "+datos.getRegionReptxt()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getTextosTitulos()));
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_JUSTES+": "+datos.getAjuste().getDescripcion()));
		headerRow.getCell(2).setCellStyle(headerStyle);

		rowNum++;
		//header de tabla
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString(datos.getTitReptxt()));
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ENE)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 2).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_FEB)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 3).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_ABR)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 5).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_MAY)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUN)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_JUL)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 8).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_AGO)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 9).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_SEP)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_OCT)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 11).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_NOV)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 12).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_MES_DIC)+"-"+datos.getAnioRepCorto());
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString(Constants.TIT_RE_TOTAL));
		
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
		
		for (RepAdqTextoBean datlist : datos.getListRepAdqtxt()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getTexto()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalTexto() != null ? datlist.getTotalTexto().toString():"0.00");
			
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
			}
			
		
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().toString():"0.00"); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().toString():"0.00");
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().toString():"0.00");
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().toString():"0.00");
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().toString():"0.00");
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().toString():"0.00");
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().toString():"0.00");
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().toString():"0.00");
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().toString():"0.00");
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().toString():"0.00");
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().toString():"0.00");
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().toString():"0.00");
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().toString():"0.00");
		
			headerRow.getCell(0).setCellStyle(cellStyle1);
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
		if (datos.getAjuste().getClave().equals("TA"))
		{
			rowNum++;
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_JUSTES_COSTO));
			
			for (RepAdqTextoBean datlist : datos.getListRepAjAdqTxt()) {
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getTexto()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalTexto() != null ? datlist.getTotalTexto().toString():"0.00");
			
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				}

				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().toString():"0.00");
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
				rowNum++;
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_GRAN_TOT_AJUST));
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().toString():"0.00"); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().toString():"0.00");
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().toString():"0.00");
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().toString():"0.00");
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().toString():"0.00");
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().toString():"0.00");
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().toString():"0.00");
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().toString():"0.00");
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().toString():"0.00");
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().toString():"0.00");
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().toString():"0.00");
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().toString():"0.00");
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().toString():"0.00");
				
				
				headerRow.getCell(0).setCellStyle(cellStyle1);
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
				
		}	
		//IMEGEN
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
