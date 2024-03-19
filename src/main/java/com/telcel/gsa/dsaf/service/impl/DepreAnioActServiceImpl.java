package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.DepreAnioActAmovDao;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.DepreActBean;
import com.telcel.gsa.dsaf.bean.RepAdqClasBean;
import com.telcel.gsa.dsaf.bean.RepAdqRegBean;
import com.telcel.gsa.dsaf.bean.RepAdqTextoBean;
import com.telcel.gsa.dsaf.dao.DepreAnioActDao;
import com.telcel.gsa.dsaf.dipcel.dao.DepreAnioActDipcelDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.DepreAnioActSercotelDao;
import com.telcel.gsa.dsaf.service.DepreAnioActService;
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
@Service("depreAnioActService")
public class DepreAnioActServiceImpl implements DepreAnioActService, Serializable{

	
	@Autowired
	@Qualifier("depreAnioActDaoImpl")
	DepreAnioActDao depreAnioActDao;
	@Autowired
	@Qualifier("depreAnioActDipcelDaoImpl")
	DepreAnioActDipcelDao depreAnioActDipcelDao;
	@Autowired
	@Qualifier("depreAnioActSercotelDaoImpl")
	DepreAnioActSercotelDao depreAnioActSercotelDao;
	@Autowired
	@Qualifier("depreAnioActAmovDaoImpl")
	DepreAnioActAmovDao depreAnioActAmovDao;
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public DepreActBean datosWhere(DepreActBean datos) {
		
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
	public DepreActBean consultaReg(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreRegion(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreRegion(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreRegion(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreRegion(datos);
				}break;
				
			}
		}catch(CfeException e){
			throw e;}
		
		return datos;
	}
	
	@Override
	public DepreActBean consultaRegAjus(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreRegionAjustes(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreRegionAjustes(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreRegionAjustes(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreRegionAjustes(datos);
				}break;
			
			}
		}catch(CfeException e){
			throw e;}
		
		return datos;
	}
	
	

	@Override
	public DepreActBean consultaRegNetos(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreRegionNetos(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreRegionNetos(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreRegionNetos(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreRegionNetos(datos);
				}break;
			}
		}catch(CfeException e){
			throw e;}
		return datos;
	}
	
	
	@Override
	public HSSFWorkbook generaDocumento(DepreActBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a convinar
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
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_DPR_REGION)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		if(datos.isAcum()){
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}else{
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 2).setCellValue( 
			new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}
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
		
		for (RepAdqRegBean datlist : datos.getListRepDepreReg()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getRegion()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalRegion() != null ? datlist.getTotalRegion().doubleValue():0.0);
			
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
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().doubleValue():0.0);
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
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_DEPRE_ANUAL));
			
			for (RepAdqRegBean datlist : datos.getListRepAjDepreReg()) {
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getRegion()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalRegion() != null ? datlist.getTotalRegion().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().doubleValue():0.0);
				
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
	public DepreActBean consultaClas(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
			case 1:{
				datos = depreAnioActDao.consultaDepreClase(datos);
			}break;
			case 2:{
				datos = depreAnioActDipcelDao.consultaDepreClase(datos);
			}break;
			case 3:{
				datos = depreAnioActSercotelDao.consultaDepreClase(datos);
			}break;
			case 4:{
				datos = depreAnioActAmovDao.consultaDepreClase(datos);
			}break;
		}
		}catch(CfeException e){
			throw e;}
		
		return datos;
	}
	


	@Override
	public DepreActBean consultaClasAjus(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
		switch(session.getSociedad().getId()){
			case 1:{
				datos = depreAnioActDao.consultaDepreClaseAjustes(datos);
			}break;
			case 2:{
				datos = depreAnioActDipcelDao.consultaDepreClaseAjustes(datos);
			}break;
			case 3:{
				datos = depreAnioActSercotelDao.consultaDepreClaseAjustes(datos);
			}break;
			case 4:{
				datos = depreAnioActAmovDao.consultaDepreClaseAjustes(datos);
			}break;
		}
		}catch(CfeException e){
			throw e;}
		return datos;
	}
	



	@Override
	public DepreActBean consultaClasNetos(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try
		{
			switch(session.getSociedad().getId()){
			case 1:{
				datos = depreAnioActDao.consultaDepreClaseNetos(datos);
			}break;
			case 2:{
				datos = depreAnioActDipcelDao.consultaDepreClaseNetos(datos);
			}break;
			case 3:{
				datos = depreAnioActSercotelDao.consultaDepreClaseNetos(datos);
			}break;
			case 4:{
				datos = depreAnioActAmovDao.consultaDepreClaseNetos(datos);
			}break;
			}
		}
			catch(CfeException e){
				throw e;}
	
		
		return datos;
	}
	



	@Override
	public HSSFWorkbook generaDocumentoClas(DepreActBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a convinar
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
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_DPR_CLASE)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		if(datos.isAcum()){
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}else{
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 2).setCellValue( 
			new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}
		
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
		
		for (RepAdqClasBean datlist : datos.getListRepDepreClas()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getClase()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalClase() != null ? datlist.getTotalClase().doubleValue():0.0);
			
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
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().doubleValue():0.0);
		
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
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_DEPRE_ANUAL));
			
			for (RepAdqClasBean datlist : datos.getListRepAjDepreClas()) {
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getClase()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalClase() != null ? datlist.getTotalClase().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().doubleValue():0.0);
				
				
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
	public DepreActBean consultaText(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try
		{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreTexto(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreTexto(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreTexto(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreTexto(datos);
				}break;
			}
		}catch(CfeException e){
			throw e;}
		return datos;
	}
	
	


	@Override
	public DepreActBean consultaTextAjus(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreTextoAjustes(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreTextoAjustes(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreTextoAjustes(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreTextoAjustes(datos);
				}break;
			}
		}catch(CfeException e){
			throw e;}
		
		return datos;
	}
	
	


	@Override
	public DepreActBean consultaTextNetos(DepreActBean datos, SessionScopeUser session) throws CfeException, SQLException {
		try{
			switch(session.getSociedad().getId()){
				case 1:{
					datos = depreAnioActDao.consultaDepreTextoNetos(datos);
				}break;
				case 2:{
					datos = depreAnioActDipcelDao.consultaDepreTextoNetos(datos);
				}break;
				case 3:{
					datos = depreAnioActSercotelDao.consultaDepreTextoNetos(datos);
				}break;
				case 4:{
					datos = depreAnioActAmovDao.consultaDepreTextoNetos(datos);
				}break;
			}
		}catch(CfeException e){
			throw e;}
		
		return datos;
	}
	



	@Override
	public HSSFWorkbook generaDocumentoText(DepreActBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		//celdas a convinar
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
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_DPR_TEXTO)); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 2).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(2).setCellStyle(headerStyle);
		if(datos.isAcum()){
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}else{
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 2).setCellValue(
			new HSSFRichTextString(datos.getMesSeleccion()));
			headerRow.getCell(2).setCellStyle(headerStyle);
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 2).setCellValue( 
			new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
			headerRow.getCell(2).setCellStyle(headerStyle);
		}
		
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
				new HSSFRichTextString("CLASE"));
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
		
		for (RepAdqTextoBean datlist : datos.getListRepDepretxt()) {
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getTexto()));
			headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datlist.getTotalTexto() != null ? datlist.getTotalTexto().doubleValue():0.0);
			
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
			headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesGeneral().getEnero() != null ? datos.getTotReporteMesGeneral().getEnero().doubleValue():0.0); 
			headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesGeneral().getFebrero() != null ? datos.getTotReporteMesGeneral().getFebrero().doubleValue():0.0);
			headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesGeneral().getMarzo() != null ? datos.getTotReporteMesGeneral().getMarzo().doubleValue():0.0);
			headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesGeneral().getAbril() != null ? datos.getTotReporteMesGeneral().getAbril().doubleValue():0.0);
			headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesGeneral().getMayo() != null ? datos.getTotReporteMesGeneral().getMayo().doubleValue():0.0);
			headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesGeneral().getJunio() != null ? datos.getTotReporteMesGeneral().getJunio().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesGeneral().getJulio() != null ? datos.getTotReporteMesGeneral().getJulio().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesGeneral().getAgosto() != null ? datos.getTotReporteMesGeneral().getAgosto().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesGeneral().getSeptiembre() != null ? datos.getTotReporteMesGeneral().getSeptiembre().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesGeneral().getOctubre() != null ? datos.getTotReporteMesGeneral().getOctubre().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesGeneral().getNoviembre() != null ? datos.getTotReporteMesGeneral().getNoviembre().doubleValue():0.0);
			headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesGeneral().getDiciembre() != null ? datos.getTotReporteMesGeneral().getDiciembre().doubleValue():0.0);
			headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesGeneral().getTotal() != null ? datos.getTotReporteMesGeneral().getTotal().doubleValue():0.0);
			
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
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_DEPRE_ANUAL));
			
			for (RepAdqTextoBean datlist : datos.getListRepAjDepreTxt()) {
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getTexto()));
				headerRow.createCell((short) 1).setCellValue(datlist.getEnero() != null ? datlist.getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datlist.getFebrero() != null ? datlist.getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datlist.getMarzo() != null ? datlist.getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datlist.getAbril() != null ? datlist.getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datlist.getMayo() != null ? datlist.getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datlist.getJunio() != null ? datlist.getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datlist.getJulio() != null ? datlist.getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datlist.getAgosto() != null ? datlist.getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datlist.getSeptiembre() != null ? datlist.getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datlist.getOctubre() != null ? datlist.getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datlist.getNoviembre() != null ? datlist.getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datlist.getDiciembre() != null ? datlist.getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datlist.getTotalTexto() != null ? datlist.getTotalTexto().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGeneral().getEnero() != null ? datos.getTotReporteMesAjusteGeneral().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGeneral().getFebrero() != null ? datos.getTotReporteMesAjusteGeneral().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGeneral().getMarzo() != null ? datos.getTotReporteMesAjusteGeneral().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGeneral().getAbril() != null ? datos.getTotReporteMesAjusteGeneral().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGeneral().getMayo() != null ? datos.getTotReporteMesAjusteGeneral().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGeneral().getJunio() != null ? datos.getTotReporteMesAjusteGeneral().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGeneral().getJulio() != null ? datos.getTotReporteMesAjusteGeneral().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGeneral().getAgosto() != null ? datos.getTotReporteMesAjusteGeneral().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGeneral().getSeptiembre() != null ? datos.getTotReporteMesAjusteGeneral().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGeneral().getOctubre() != null ? datos.getTotReporteMesAjusteGeneral().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGeneral().getNoviembre() != null ? datos.getTotReporteMesAjusteGeneral().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGeneral().getDiciembre() != null ? datos.getTotReporteMesAjusteGeneral().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGeneral().getTotal() != null ? datos.getTotReporteMesAjusteGeneral().getTotal().doubleValue():0.0);
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
				headerRow.createCell((short) 1).setCellValue(datos.getTotReporteMesAjusteGran().getEnero() != null ? datos.getTotReporteMesAjusteGran().getEnero().doubleValue():0.0); 
				headerRow.createCell((short) 2).setCellValue(datos.getTotReporteMesAjusteGran().getFebrero() != null ? datos.getTotReporteMesAjusteGran().getFebrero().doubleValue():0.0);
				headerRow.createCell((short) 3).setCellValue(datos.getTotReporteMesAjusteGran().getMarzo() != null ? datos.getTotReporteMesAjusteGran().getMarzo().doubleValue():0.0);
				headerRow.createCell((short) 4).setCellValue(datos.getTotReporteMesAjusteGran().getAbril() != null ? datos.getTotReporteMesAjusteGran().getAbril().doubleValue():0.0);
				headerRow.createCell((short) 5).setCellValue(datos.getTotReporteMesAjusteGran().getMayo() != null ? datos.getTotReporteMesAjusteGran().getMayo().doubleValue():0.0);
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteMesAjusteGran().getJunio() != null ? datos.getTotReporteMesAjusteGran().getJunio().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteMesAjusteGran().getJulio() != null ? datos.getTotReporteMesAjusteGran().getJulio().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteMesAjusteGran().getAgosto() != null ? datos.getTotReporteMesAjusteGran().getAgosto().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteMesAjusteGran().getSeptiembre() != null ? datos.getTotReporteMesAjusteGran().getSeptiembre().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteMesAjusteGran().getOctubre() != null ? datos.getTotReporteMesAjusteGran().getOctubre().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(datos.getTotReporteMesAjusteGran().getNoviembre() != null ? datos.getTotReporteMesAjusteGran().getNoviembre().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue(datos.getTotReporteMesAjusteGran().getDiciembre() != null ? datos.getTotReporteMesAjusteGran().getDiciembre().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(datos.getTotReporteMesAjusteGran().getTotal() != null ? datos.getTotReporteMesAjusteGran().getTotal().doubleValue():0.0);
				
				
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
