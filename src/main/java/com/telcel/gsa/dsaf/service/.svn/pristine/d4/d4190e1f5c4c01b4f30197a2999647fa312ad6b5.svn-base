package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import com.telcel.gsa.dsaf.amov.dao.ReporteDetalladoAmovDao;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasCatEstatusDocBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.bean.BajasOpUsuarioBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoBean;
import com.telcel.gsa.dsaf.bean.TGClaseBean;
import com.telcel.gsa.dsaf.bean.TGRegionBean;
import com.telcel.gsa.dsaf.dao.ReporteDetalladoDao;
import com.telcel.gsa.dsaf.dao.UtileriasCfeDao;
import com.telcel.gsa.dsaf.dipcel.dao.ReporteDetalladoDipcelDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.ReporteDetalladoSercotelDao;
import com.telcel.gsa.dsaf.service.ReporteDetalladoBaService;
import com.telcel.gsa.dsaf.service.ReporteDetalladoService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

@Service("reporteDetalladoBaService")
public class ReporteDetalladoBaServiceImpl implements ReporteDetalladoBaService, Serializable{

	

	@Autowired
	ReporteDetalladoDao reportedetalladoDao;
	@Autowired
	ReporteDetalladoDipcelDao reportedetalladoDipcelDao;
	@Autowired
	ReporteDetalladoSercotelDao reportedetalladoSercotelDao;
	@Autowired
	ReporteDetalladoAmovDao reportedetalladoAmovDao;
	@Autowired
	@Qualifier("utileriasCfeDaoImpl")
	private UtileriasCfeDao utileriasCfeDao;
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public ReporteDetalladoBean datosWhere(ReporteDetalladoBean datos) {
		
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

	
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public HSSFWorkbook generaDocumento(ReporteDetalladoBean datos) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(); 
		HSSFSheet sheet = workbook.createSheet(Constants.TIT_BAJAS_ACTIVO_FIJO);
		
		int rowNum = 0; 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);
		//celdas a combinar
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,11));
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,11));
		sheet.addMergedRegion(new CellRangeAddress(2,2,1,11));
		sheet.addMergedRegion(new CellRangeAddress(3,3,1,11));
		sheet.addMergedRegion(new CellRangeAddress(4,4,1,11));
		sheet.addMergedRegion(new CellRangeAddress(5,5,1,11));
		sheet.addMergedRegion(new CellRangeAddress(6,6,1,11));
		sheet.addMergedRegion(new CellRangeAddress(7,7,1,11));
		sheet.addMergedRegion(new CellRangeAddress(8,8,1,11));
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));
		
		// Create the column headings 
		HSSFRow headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue(
		new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO)); 
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REP_DETALLADO)); 
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(datos.getCalculotxt())); 
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_MES+": "+datos.getMesReptxt()));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_ANIO+": "+datos.getAnio()));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_CLASE+": "+datos.getClaseReptxt()));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_REGION+": "+datos.getRegionReptxt()));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(datos.getTextosTitulos()));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 1).setCellValue( 
		new HSSFRichTextString(Constants.TIT_JUSTES+": "+datos.getAjuste().getDescripcion()));
		headerRow.getCell(1).setCellStyle(headerStyle);

		rowNum++;
		headerRow = sheet.createRow((short) rowNum++); 
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString("FECHA \nCAP. "));
		headerRow.createCell((short) 1).setCellValue( 
				new HSSFRichTextString("TEXTO BAJA "));
		headerRow.createCell((short) 2).setCellValue( 
				new HSSFRichTextString("DENOMINACION BAJA "));
		headerRow.createCell((short) 3).setCellValue( 
				new HSSFRichTextString("NUMERO \nACTIVO"));
		headerRow.createCell((short) 4).setCellValue( 
				new HSSFRichTextString("SUB"));
		headerRow.createCell((short) 5).setCellValue( 
				new HSSFRichTextString("PERIODO \nBAJA "));
		headerRow.createCell((short) 6).setCellValue( 
				new HSSFRichTextString("ADQUISICION \nBAJA "));
		headerRow.createCell((short) 7).setCellValue( 
				new HSSFRichTextString("ACUMULADA \nBAJA "));
		headerRow.createCell((short) 8).setCellValue( 
				new HSSFRichTextString("EJERCICIO \nBAJA "));
		headerRow.createCell((short) 9).setCellValue( 
				new HSSFRichTextString("DEPRECIACION \nTOTAL "));
		headerRow.createCell((short) 10).setCellValue( 
				new HSSFRichTextString("COSTO \nHISTORICO "));
		headerRow.createCell((short) 11).setCellValue( 
				new HSSFRichTextString("INPC \nMP"));
		headerRow.createCell((short) 12).setCellValue( 
				new HSSFRichTextString("INPC "));
		headerRow.createCell((short) 13).setCellValue( 
				new HSSFRichTextString("FACTOR \nACT "));
		headerRow.createCell((short) 14).setCellValue( 
				new HSSFRichTextString("COSTO \nACTUALIZADO "));
		headerRow.createCell((short) 15).setCellValue( 
				new HSSFRichTextString("DEP. AÑO \nACTUALIZADA "));
		
		for(TGRegionBean region: datos.getDetRegiones()){
			
			headerRow = sheet.createRow((short) rowNum++); 
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("REGION: " + region.getNombre()));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,15));
			headerRow.getCell(0).setCellStyle(headerStyle);
			for(TGClaseBean clase: region.getClases()){
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString("CLASE: " + clase.getNombre()));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,15));
				headerRow.getCell(0).setCellStyle(headerStyle);
				for(BajasDosBean texto: clase.getTextos()){
					
					headerRow = sheet.createRow((short) rowNum++);
					headerRow.createCell((short) 0).setCellValue( 
							new HSSFRichTextString(texto.getDia()+"-"+texto.getMesStr()+"-"+texto.getAnio()));
					headerRow.createCell((short) 1).setCellValue( 
							new HSSFRichTextString(texto.getTexto_baja()));
					headerRow.createCell((short) 2).setCellValue( 
							new HSSFRichTextString(texto.getDenom()));
					headerRow.createCell((short) 3).setCellValue( 
							new HSSFRichTextString(texto.getNum_activo()));
					headerRow.createCell((short) 4).setCellValue( 
							new HSSFRichTextString(texto.getSub()));
					headerRow.createCell((short) 5).setCellValue( 
							texto.getPerbajaObj().getId().intValue());
					headerRow.createCell((short) 6).setCellValue( 
							texto.getAdq_baja() != null ? texto.getAdq_baja().doubleValue():0.0);
					headerRow.createCell((short) 7).setCellValue( 
							texto.getAdq_ac_baja() != null ? texto.getAdq_ac_baja().doubleValue():0.0);
					headerRow.createCell((short) 8).setCellValue( 
							texto.getEjercicio_baja() != null ? texto.getEjercicio_baja().doubleValue():0.0);
					headerRow.createCell((short) 9).setCellValue( 
							texto.getDepr_tot() != null ? texto.getDepr_tot().doubleValue():0.0);
					headerRow.createCell((short) 10).setCellValue( 
							texto.getCosto_h() != null ? texto.getCosto_h().doubleValue():0.0);
					headerRow.createCell((short) 11).setCellValue( 
							texto.getCosto_h() != null ? texto.getInpcmp().doubleValue():0.0);
					headerRow.createCell((short) 12).setCellValue( 
							texto.getCosto_h() != null ? texto.getInpc().doubleValue():0.0);
					headerRow.createCell((short) 13).setCellValue( 
							texto.getCosto_h() != null ? texto.getFac_act().doubleValue():0.0);
					headerRow.createCell((short) 14).setCellValue( 
							texto.getCosto_act() != null ? texto.getCosto_act().doubleValue():0.0);
					headerRow.createCell((short) 15).setCellValue( 
							texto.getDepre_anual_act() != null ? texto.getDepre_anual_act().doubleValue():0.0);
					//headerRow.getCell(1).setCellStyle(cellStyle);	        	
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
			
				}
				headerRow = sheet.createRow((short) rowNum++);
				
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString("TOTAL CLASE: " + clase.getNombreCorto()));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));
				
				headerRow.createCell((short) 6).setCellValue( 
						clase.getAll_adq_baja() != null ? clase.getAll_adq_baja().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue( 
						clase.getAll_adq_ac_baja() != null ? clase.getAll_adq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue( 
						clase.getAll_ejercicio_baja() != null ? clase.getAll_ejercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
						clase.getAll_depr_tot() != null ? clase.getAll_depr_tot().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue( 
						clase.getAll_costo_h() != null ? clase.getAll_costo_h().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(" ");
				headerRow.createCell((short) 12).setCellValue(" ");
				headerRow.createCell((short) 13).setCellValue(" ");
				headerRow.createCell((short) 14).setCellValue( 
						clase.getAll_costo_act() != null ? clase.getAll_costo_act().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue( 
						clase.getAll_depre_anual_act() != null ? clase.getAll_depre_anual_act().doubleValue():0.0);
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
			}
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 0).setCellValue( 
					new HSSFRichTextString("TOTAL REGION: " + region.getNombre()));
			sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));

			headerRow.createCell((short) 6).setCellValue( 
					region.getAll_adq_baja() != null ? region.getAll_adq_baja().doubleValue():0.0);
			headerRow.createCell((short) 7).setCellValue( 
					region.getAll_adq_ac_baja() != null ? region.getAll_adq_ac_baja().doubleValue():0.0);
			headerRow.createCell((short) 8).setCellValue( 
					region.getAll_ejercicio_baja() != null ? region.getAll_ejercicio_baja().doubleValue():0.0);
			headerRow.createCell((short) 9).setCellValue( 
					region.getAll_depr_tot() != null ? region.getAll_depr_tot().doubleValue():0.0);
			headerRow.createCell((short) 10).setCellValue( 
					region.getAll_costo_h() != null ? region.getAll_costo_h().doubleValue():0.0);
			headerRow.createCell((short) 11).setCellValue(" ");
			headerRow.createCell((short) 12).setCellValue(" ");
			headerRow.createCell((short) 13).setCellValue(" ");
			headerRow.createCell((short) 14).setCellValue( 
					region.getAll_costo_act() != null ? region.getAll_costo_act().doubleValue():0.0);
			headerRow.createCell((short) 15).setCellValue( 
					region.getAll_depre_anual_act() != null ? region.getAll_depre_anual_act().doubleValue():0.0);
				        		
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
			
		}
		headerRow = sheet.createRow((short) rowNum++);
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString("TOTAL: "));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));
//		headerRow.createCell((short) 1).setCellValue(" ");
//		headerRow.createCell((short) 2).setCellValue(" ");
//		headerRow.createCell((short) 3).setCellValue(" ");
		headerRow.createCell((short) 6).setCellValue( 
				datos.getTotReporteGeneral().getAdq_baja() != null ? datos.getTotReporteGeneral().getAdq_baja().doubleValue():0.0);
		headerRow.createCell((short) 7).setCellValue( 
				datos.getTotReporteGeneral().getAdq_ac_baja() != null ? datos.getTotReporteGeneral().getAdq_ac_baja().doubleValue():0.0);
		headerRow.createCell((short) 8).setCellValue( 
				datos.getTotReporteGeneral().getEjercicio_baja() != null ? datos.getTotReporteGeneral().getEjercicio_baja().doubleValue():0.0);
		headerRow.createCell((short) 9).setCellValue( 
				datos.getTotReporteGeneral().getDepr_tot() != null ? datos.getTotReporteGeneral().getDepr_tot().doubleValue():0.0);
		headerRow.createCell((short) 10).setCellValue( 
				datos.getTotReporteGeneral().getCosto_h() != null ? datos.getTotReporteGeneral().getCosto_h().doubleValue():0.0);
		headerRow.createCell((short) 11).setCellValue(" ");
		headerRow.createCell((short) 12).setCellValue(" ");
		headerRow.createCell((short) 13).setCellValue(" ");
		headerRow.createCell((short) 14).setCellValue( 
				datos.getTotReporteGeneral().getCosto_act() != null ? datos.getTotReporteGeneral().getCosto_act().doubleValue():0.0);
		headerRow.createCell((short) 15).setCellValue( 
				datos.getTotReporteGeneral().getDepre_anual_act() != null ? datos.getTotReporteGeneral().getDepre_anual_act().doubleValue():0.0);
	        		
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
		rowNum++;
		rowNum++;
		
		if (datos.getAjuste().getClave().equals("TA"))
		{
			headerRow = sheet.createRow((short) rowNum++);
			headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_JUSTES_TIPO));
				rowNum++;
				
//			for (BajasDosBean datlist : datos.getListReporteDetallado()) {
			for (BajasDosBean datlist : datos.getListReporteDetalladoAjust()) {
				
				
				headerRow = sheet.createRow((short) rowNum++);
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(datlist.getTexto_baja()));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));
//				headerRow.createCell((short) 1).setCellValue(" ");
				headerRow.createCell((short) 6).setCellValue(datlist.getAdq_baja() != null ? datlist.getAdq_baja().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datlist.getAdq_ac_baja() != null ? datlist.getAdq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datlist.getEjercicio_baja() != null ? datlist.getEjercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datlist.getDepr_tot() != null ? datlist.getDepr_tot().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datlist.getCosto_h() != null ? datlist.getCosto_h().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(" ");
				headerRow.createCell((short) 12).setCellValue(" ");
				headerRow.createCell((short) 13).setCellValue(" ");
				headerRow.createCell((short) 14).setCellValue(datlist.getCosto_act() != null ? datlist.getCosto_act().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue(datlist.getDepre_anual_act() != null ? datlist.getDepre_anual_act().doubleValue():0.0);
					        		
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
			}

				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_RE_TOTAL));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));
//				headerRow.createCell((short) 1).setCellValue(" "); 
				headerRow.createCell((short) 6).setCellValue(datos.getTotReporteDetalladoAjGeneral().getAdq_baja() != null ? datos.getTotReporteDetalladoAjGeneral().getAdq_baja().doubleValue():0.0);
				headerRow.createCell((short) 7).setCellValue(datos.getTotReporteDetalladoAjGeneral().getAdq_ac_baja() != null ? datos.getTotReporteDetalladoAjGeneral().getAdq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotReporteDetalladoAjGeneral().getEjercicio_baja() != null ? datos.getTotReporteDetalladoAjGeneral().getEjercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotReporteDetalladoAjGeneral().getDepr_tot() != null ? datos.getTotReporteDetalladoAjGeneral().getDepr_tot().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotReporteDetalladoAjGeneral().getCosto_h() != null ? datos.getTotReporteDetalladoAjGeneral().getCosto_h().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(" ");
				headerRow.createCell((short) 12).setCellValue(" ");
				headerRow.createCell((short) 13).setCellValue(" ");
				headerRow.createCell((short) 14).setCellValue(datos.getTotReporteDetalladoAjGeneral().getCosto_act() != null ? datos.getTotReporteDetalladoAjGeneral().getCosto_act().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue(datos.getTotReporteDetalladoAjGeneral().getDepre_anual_act() != null ? datos.getTotReporteDetalladoAjGeneral().getDepre_anual_act().doubleValue():0.0);
					        		
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
				
				headerRow = sheet.createRow((short) rowNum++); 
				headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString(Constants.TIT_GRAN_TOT_AJUST));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,5));
//				headerRow.createCell((short) 1).setCellValue(" ");
				headerRow.createCell((short) 6).setCellValue(datos.getTotalRepDetTot().getAdq_baja() != null ? datos.getTotalRepDetTot().getAdq_baja().doubleValue():0.0); 
				headerRow.createCell((short) 7).setCellValue(datos.getTotalRepDetTot().getAdq_ac_baja() != null ? datos.getTotalRepDetTot().getAdq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 8).setCellValue(datos.getTotalRepDetTot().getEjercicio_baja() != null ? datos.getTotalRepDetTot().getEjercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue(datos.getTotalRepDetTot().getDepr_tot() != null ? datos.getTotalRepDetTot().getDepr_tot().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue(datos.getTotalRepDetTot().getCosto_h() != null ? datos.getTotalRepDetTot().getCosto_h().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue(" ");
				headerRow.createCell((short) 12).setCellValue(" ");
				headerRow.createCell((short) 13).setCellValue(" ");
				headerRow.createCell((short) 14).setCellValue(datos.getTotalRepDetTot().getCosto_act() != null ? datos.getTotalRepDetTot().getCosto_act().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue(datos.getTotalRepDetTot().getDepre_anual_act() != null ? datos.getTotalRepDetTot().getDepre_anual_act().doubleValue():0.0);
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
//	        return null;
	}


	
	@Override
	public ReporteDetalladoBean consultaReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException{
		ReporteDetalladoBean reporte = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
				case 1:{
					reporte = reportedetalladoDao.consultaReporteDetallado(datos);
				}break;
				case 2:{
					reporte = reportedetalladoDipcelDao.consultaReporteDetallado(datos);
				}break;
				case 3:{
					reporte = reportedetalladoSercotelDao.consultaReporteDetallado(datos);
				}break;
				case 4:{
					reporte = reportedetalladoAmovDao.consultaReporteDetallado(datos);
				}break;
			}
			
		}catch(CfeException e){
			
		}
		return reporte;
	}
	
	
	
	@Override
	public ReporteDetalladoBean consultaReporteDetalladoNetos(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException{
		ReporteDetalladoBean reporte = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				reporte = reportedetalladoDao.consultaReporteDetalladoNetos(datos);
			}break;
			case 2:{
				reporte = reportedetalladoDipcelDao.consultaReporteDetalladoNetos(datos);
			}break;
			case 3:{
				reporte = reportedetalladoSercotelDao.consultaReporteDetalladoNetos(datos);
			}break;
			case 4:{
				reporte = reportedetalladoAmovDao.consultaReporteDetalladoNetos(datos);
			}break;
		}
			
		}catch(CfeException e){
			
		}
		return reporte;
	}
	
	@Override
	public ReporteDetalladoBean consultaAjReporteDetallado(ReporteDetalladoBean datos, SessionScopeUser sessionScopeUser) throws CfeException, SQLException{
		ReporteDetalladoBean reporte = null;
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				reporte = reportedetalladoDao.consultaReporteDetalladoAjuste(datos);
			}break;
			case 2:{
				reporte = reportedetalladoDipcelDao.consultaReporteDetalladoAjuste(datos);
			}break;
			case 3:{
				reporte = reportedetalladoSercotelDao.consultaReporteDetalladoAjuste(datos);
			}break;
			case 4:{
				reporte = reportedetalladoAmovDao.consultaReporteDetalladoAjuste(datos);
			}break;
		}
			
		}catch(CfeException e){
			throw e;
		}
		return reporte;
	}



	    
	public void insertaArchivo(SessionScopeUser sessionScopeUser, byte[] bytes) throws CfeException, SQLException {
		
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				reportedetalladoDao.insertaArchivo(bytes);
			}break;
			case 2:{
				reportedetalladoDipcelDao.insertaArchivo(bytes);
			}break;
			case 3:{
				reportedetalladoSercotelDao.insertaArchivo(bytes);
			}break;
			case 4:{
				reportedetalladoAmovDao.insertaArchivo(bytes);
			}break;
		}
			
		}catch(CfeException e){
			throw e;
		}
		
	}
	
	public void registraSolicitud(SessionScopeUser sessionScopeUser, ReporteDetalladoBean reporteDetallado, String nombreDoc) throws CfeException, SQLException{
		BajasOpDocumentoBean documento = new BajasOpDocumentoBean();
		BajasOpUsuarioBean usuario = new BajasOpUsuarioBean();
		BajasCatEstatusDocBean estatusDoc = new BajasCatEstatusDocBean();
		documento.setId(0);
		documento.setTipo("REP");
		documento.setModulo("RDETALLADO");
		documento.setDescripcion("REPORTE DETALLADO");
		documento.setNombre(nombreDoc);
		estatusDoc.setId(1);
		documento.setEstatus(estatusDoc);
		documento.setFecha_creacion(utileriasCfeDao.obtenerFecha());
		usuario.setId(sessionScopeUser.getUsuarioCfe().getId());
		documento.setUsuario_creacion(usuario);
		documento.setEstado_registro(1);
		documento.setIdSociedad(sessionScopeUser.getSociedad().getId());
		documento.setAnio(reporteDetallado.getAnio());
		StringBuilder m = new StringBuilder("");
		Integer cMonth = 0;
		for(String month: reporteDetallado.getMesesselect()){
		m.append(month);
		cMonth++;
		if(cMonth < reporteDetallado.getMesesselect().length)
			m.append(",");
		}
		documento.setPeriodoSelect(m.toString());
		documento.setAcumulado(reporteDetallado.isAcum() ? 1: 0);
		documento.setCalculo(reporteDetallado.getCalculo().getId());
		StringBuilder reg = new StringBuilder("");
		Integer cReg = 0;
		for(String r: reporteDetallado.getRegion()){
		reg.append(r);
		cReg++;
		if(cReg < reporteDetallado.getRegion().size())
			reg.append(",");
		}
		
		documento.setRegion(reg.toString());
		
		
		StringBuilder clase = new StringBuilder("");
		Integer cClase = 0;
		for(String c: reporteDetallado.getClase()){
		clase.append(c);
		cClase++;
		if(cClase < reporteDetallado.getClase().size())
			clase.append(",");
		}
		
		documento.setClase(clase.toString());
		
		StringBuilder txt = new StringBuilder("");
		Integer cTxt = 0;
		for(String t: reporteDetallado.getTxtDesc()){
		txt.append(t);
		cTxt++;
		if(cTxt < reporteDetallado.getTxtDesc().size())
			txt.append(",");
		}
		
		documento.setTxtfisc(txt.toString());
		
		StringBuilder txtDesc = new StringBuilder("");
		Integer cTxtDesc = 0;
		for(String td: reporteDetallado.getTxtsDesc()){
			txtDesc.append(td);
		cTxtDesc++;
		if(cTxtDesc < reporteDetallado.getTxtsDesc().size())
			txtDesc.append(",");
		}
		documento.setTxtsDesc(txtDesc.toString());
		
		documento.setTipoTxt(reporteDetallado.getTexto());
		documento.setTipoAjuste(reporteDetallado.getAjuste().getClave());
		
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				reportedetalladoDao.registraSolicitud(documento);
			}break;
			case 2:{
				reportedetalladoDipcelDao.registraSolicitud(documento);
			}break;
			case 3:{
				reportedetalladoSercotelDao.registraSolicitud(documento);
			}break;
			case 4:{
				reportedetalladoAmovDao.registraSolicitud(documento);
			}break;
		}
			
		}catch(CfeException e){
			
		}
		
		
	}
	
	public List<BajasOpDocumentoBean> consultaArchivo(SessionScopeUser sessionScopeUser) throws CfeException, SQLException {
		List<BajasOpDocumentoBean> archivos = new ArrayList<BajasOpDocumentoBean>();
		BajasOpDocumentoBean docBean = new BajasOpDocumentoBean();
		BajasCatEstatusDocBean estatus = new BajasCatEstatusDocBean();
		BajasOpUsuarioBean usuario =  new BajasOpUsuarioBean();
		
		docBean.setId(0);
		docBean.setTipo("REP");
		docBean.setModulo("RDETALLADO");
		estatus.setId(3);
		docBean.setEstatus(estatus);
		docBean.setEstado_registro(1);
		usuario.setId(sessionScopeUser.getUsuarioCfe().getId());
		docBean.setUsuario_creacion(usuario);
		docBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				archivos =  reportedetalladoDao.consultaArchivo(docBean);
			}break;
			case 2:{
				archivos =  reportedetalladoDipcelDao.consultaArchivo(docBean);
			}break;
			case 3:{
				archivos =  reportedetalladoSercotelDao.consultaArchivo(docBean);
			}break;
			case 4:{
				archivos =  reportedetalladoAmovDao.consultaArchivo(docBean);
			}break;
		}
			
		}catch(CfeException e){
			throw e;
		}
			return archivos;
		
	}
	
	
	public BajasOpDocumentoBean consultaArchivoId(SessionScopeUser sessionScopeUser, Integer id) throws CfeException, SQLException {
		BajasOpDocumentoBean doc = new BajasOpDocumentoBean();
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				doc = reportedetalladoDao.consultaArchivoId(id);
			}break;
			case 2:{
				doc = reportedetalladoDipcelDao.consultaArchivoId(id);
			}break;
			case 3:{
				doc =  reportedetalladoSercotelDao.consultaArchivoId(id);
			}break;
			case 4:{
				doc =  reportedetalladoAmovDao.consultaArchivoId(id);
			}break;
		}
			
		}catch(CfeException e){
			throw e;
		}
		return doc;
	}
	
public void generaArchivo(SessionScopeUser sessionScopeUser) throws CfeException, SQLException {
		
		BajasOpDocumentoBean docBean = new BajasOpDocumentoBean();
		BajasOpUsuarioBean usuario =  new BajasOpUsuarioBean();
		
		
		docBean.setId(0);
		docBean.setTipo("REP");
		docBean.setModulo("RDETALLADO");
		docBean.setEstado_registro(1);
		usuario.setId(sessionScopeUser.getUsuarioCfe().getId());
		docBean.setUsuario_creacion(usuario);
		docBean.setIdSociedad(sessionScopeUser.getSociedad().getId());
		
		
		try{
			switch(sessionScopeUser.getSociedad().getId()){
			case 1:{
				reportedetalladoDao.generaArchivo(docBean);
			}break;
			case 2:{
				reportedetalladoDipcelDao.generaArchivo(docBean);
			}break;
			case 3:{
				reportedetalladoSercotelDao.generaArchivo(docBean);
			}break;
			case 4:{
				reportedetalladoAmovDao.generaArchivo(docBean);
			}break;
		}
			
		}catch(CfeException e){
			throw e;
		}
		
		
	}
}
;