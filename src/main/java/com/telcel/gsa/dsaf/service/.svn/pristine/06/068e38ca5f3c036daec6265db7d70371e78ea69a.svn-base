package com.telcel.gsa.dsaf.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.ReporteDetalladoAjusteTipoAmovDao;
import com.telcel.gsa.dsaf.bean.BajasAjustesBean;
import com.telcel.gsa.dsaf.bean.BajasClaseBean;
import com.telcel.gsa.dsaf.bean.BajasDosBean;
import com.telcel.gsa.dsaf.bean.CatRegionBean;
import com.telcel.gsa.dsaf.bean.ReporteDetalladoPorTipoBean;
import com.telcel.gsa.dsaf.bean.TGTipoAjusteBean;
import com.telcel.gsa.dsaf.dao.AdquisicionBajaDao;
import com.telcel.gsa.dsaf.dao.ReporteDetalladoAjusteTipoDao;
import com.telcel.gsa.dsaf.dipcel.dao.ReporteDetalladoAjusteTipoDipselDao;
import com.telcel.gsa.dsaf.security.SessionScopeUser;
import com.telcel.gsa.dsaf.sercotel.dao.ReporteDetalladoAjusteTipoSercotelDao;
import com.telcel.gsa.dsaf.service.ReporteDetalladoAjusteService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@Service("reporteDetalladoAjusteService")
public class ReporteDetalladoAjusteServiceImpl implements ReporteDetalladoAjusteService, Serializable{

	
	@Autowired
	AdquisicionBajaDao adquisicionBajaDao;
	@Autowired
	ReporteDetalladoAjusteTipoDao reportedetalladoAjusteDao;
	@Autowired
	ReporteDetalladoAjusteTipoDipselDao reporteDetalladoAjusteTipoDipselDao;
	@Autowired
	ReporteDetalladoAjusteTipoSercotelDao reporteDetalladoAjusteTipoSercotelDao;
	@Autowired
	ReporteDetalladoAjusteTipoAmovDao reporteDetalladoAjusteTipoAmovDao;
	/**
	 * 
	 */
	private static final long serialVersionUID = -290824588675622989L;

	@Override
	public ReporteDetalladoPorTipoBean datosWhere(ReporteDetalladoPorTipoBean datos) {
		
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
	public HSSFWorkbook generaDocumento(ReporteDetalladoPorTipoBean datos) throws IOException {
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
		new HSSFRichTextString(Constants.TIT_REP_DETALLADO_AJUSTE)); 
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
		headerRow.createCell((short) 0).setCellValue(new HSSFRichTextString("REGION"));
		headerRow.createCell((short) 1).setCellValue(new HSSFRichTextString("CLASE"));
		headerRow.createCell((short) 2).setCellValue(new HSSFRichTextString("FECHA CAP."));
		headerRow.createCell((short) 3).setCellValue(new HSSFRichTextString("TEXTO BAJA "));
		headerRow.createCell((short) 4).setCellValue(new HSSFRichTextString("DENOMINACION \nBAJA "));
		headerRow.createCell((short) 5).setCellValue(new HSSFRichTextString("NUMERO \nACTIVO"));
		headerRow.createCell((short) 6).setCellValue(new HSSFRichTextString("SUB"));
		headerRow.createCell((short) 7).setCellValue(new HSSFRichTextString("PERIODO \nBAJA "));
		headerRow.createCell((short) 8).setCellValue(new HSSFRichTextString("ADQUISICION \nBAJA "));
		headerRow.createCell((short) 9).setCellValue(new HSSFRichTextString("ACUMULADA \nBAJA "));
		headerRow.createCell((short) 10).setCellValue(new HSSFRichTextString("EJERCICIO \nBAJA "));
		headerRow.createCell((short) 11).setCellValue(new HSSFRichTextString("DEPRECIACION \nTOTAL "));
		headerRow.createCell((short) 12).setCellValue(new HSSFRichTextString("COSTO \nHISTORICO "));
		headerRow.createCell((short) 13).setCellValue(new HSSFRichTextString("INPC \nMP"));
		headerRow.createCell((short) 14).setCellValue(new HSSFRichTextString("INPC "));
		headerRow.createCell((short) 15).setCellValue(new HSSFRichTextString("FACTOR \nACT "));
		headerRow.createCell((short) 16).setCellValue(new HSSFRichTextString("COSTO \nACTUALIZADO "));
		headerRow.createCell((short) 17).setCellValue(new HSSFRichTextString("DEP. AÑO \nACTUALIZADA "));
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
		SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
		for(TGTipoAjusteBean datoT: datos.getListReporteDetalladoTip()){
		
			for(BajasDosBean texto: datoT.getTextos()){
				
				headerRow = sheet.createRow((short) rowNum++);
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString(texto.getRegion()));
				headerRow.createCell((short) 1).setCellValue( 
						new HSSFRichTextString(texto.getClase()));
		
				headerRow.createCell((short) 2).setCellValue( 
						new HSSFRichTextString(sdfOut.format(texto.getFechaCap())));
				headerRow.createCell((short) 3).setCellValue( 
						new HSSFRichTextString(texto.getTexto_baja()));
				headerRow.createCell((short) 4).setCellValue( 
						new HSSFRichTextString(texto.getDenom()));
				headerRow.createCell((short) 5).setCellValue( 
						new HSSFRichTextString(texto.getNum_activo()));
				headerRow.createCell((short) 6).setCellValue( 
						new HSSFRichTextString(texto.getSub()));
				headerRow.createCell((short) 7).setCellValue( 
						texto.getPerbajaObj().getId());
				headerRow.createCell((short) 8).setCellValue( 
						texto.getAdq_baja() != null ? texto.getAdq_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
						texto.getAdq_ac_baja() != null ? texto.getAdq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue( 
						texto.getEjercicio_baja() != null ? texto.getEjercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue( 
						texto.getDepr_tot() != null ? texto.getDepr_tot().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue( 
						texto.getCosto_h() != null ? texto.getCosto_h().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue( 
						texto.getCosto_h() != null ? texto.getInpcmp().doubleValue():0.0);
				headerRow.createCell((short) 14).setCellValue( 
						texto.getCosto_h() != null ? texto.getInpc().doubleValue():0.0);
				headerRow.createCell((short) 15).setCellValue( 
						texto.getCosto_h() != null ? texto.getFac_act().doubleValue():0.0);
				headerRow.createCell((short) 16).setCellValue( 
						texto.getCosto_act() != null ? texto.getCosto_act().doubleValue():0.0);
				headerRow.createCell((short) 17).setCellValue( 
						texto.getDepre_anual_act() != null ? texto.getDepre_anual_act().doubleValue():0.0);
				     	
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
			
		
			}
				headerRow = sheet.createRow((short) rowNum++);
				
				headerRow.createCell((short) 0).setCellValue( 
						new HSSFRichTextString("TOTAL " + datoT.getDescTipo()));
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,7));
	
				headerRow.createCell((short) 8).setCellValue( 
						datoT.getTotalTipo().getAdq_baja() != null ? datoT.getTotalTipo().getAdq_baja().doubleValue():0.0);
				headerRow.createCell((short) 9).setCellValue( 
						datoT.getTotalTipo().getAdq_ac_baja() != null ? datoT.getTotalTipo().getAdq_ac_baja().doubleValue():0.0);
				headerRow.createCell((short) 10).setCellValue( 
						datoT.getTotalTipo().getEjercicio_baja() != null ? datoT.getTotalTipo().getEjercicio_baja().doubleValue():0.0);
				headerRow.createCell((short) 11).setCellValue( 
						datoT.getTotalTipo().getDepr_tot() != null ? datoT.getTotalTipo().getDepr_tot().doubleValue():0.0);
				headerRow.createCell((short) 12).setCellValue( 
						datoT.getTotalTipo().getCosto_h() != null ? datoT.getTotalTipo().getCosto_h().doubleValue():0.0);
				headerRow.createCell((short) 13).setCellValue(" ");
				headerRow.createCell((short) 14).setCellValue(" ");
				headerRow.createCell((short) 15).setCellValue(" ");
				headerRow.createCell((short) 16).setCellValue( 
						datoT.getTotalTipo().getCosto_act() != null ? datoT.getTotalTipo().getCosto_act().doubleValue():0.0);
				headerRow.createCell((short) 17).setCellValue( 
						datoT.getTotalTipo().getDepre_anual_act() != null ? datoT.getTotalTipo().getDepre_anual_act().doubleValue():0.0);
				
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
			}
		
		headerRow = sheet.createRow((short) rowNum++);
		headerRow.createCell((short) 0).setCellValue( 
				new HSSFRichTextString("GRAN TOTAL AJUSTADO: "));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(),headerRow.getRowNum(),0,7));

		headerRow.createCell((short) 8).setCellValue( 
				datos.getTotReporteGeneral().getAdq_baja() != null ? datos.getTotReporteGeneral().getAdq_baja().doubleValue():0.0);
		headerRow.createCell((short) 9).setCellValue( 
				datos.getTotReporteGeneral().getAdq_ac_baja() != null ? datos.getTotReporteGeneral().getAdq_ac_baja().doubleValue():0.0);
		headerRow.createCell((short) 10).setCellValue( 
				datos.getTotReporteGeneral().getEjercicio_baja() != null ? datos.getTotReporteGeneral().getEjercicio_baja().doubleValue():0.0);
		headerRow.createCell((short) 11).setCellValue( 
				datos.getTotReporteGeneral().getDepr_tot() != null ? datos.getTotReporteGeneral().getDepr_tot().doubleValue():0.0);
		headerRow.createCell((short) 12).setCellValue( 
				datos.getTotReporteGeneral().getCosto_h() != null ? datos.getTotReporteGeneral().getCosto_h().doubleValue():0.0);
		headerRow.createCell((short) 13).setCellValue(" ");
		headerRow.createCell((short) 14).setCellValue(" ");
		headerRow.createCell((short) 15).setCellValue(" ");
		headerRow.createCell((short) 16).setCellValue( 
				datos.getTotReporteGeneral().getCosto_act() != null ? datos.getTotReporteGeneral().getCosto_act().doubleValue():0.0);
		headerRow.createCell((short) 17).setCellValue( 
				datos.getTotReporteGeneral().getDepre_anual_act() != null ? datos.getTotReporteGeneral().getDepre_anual_act().doubleValue():0.0);
	        		
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
		rowNum++;
		rowNum++;
				
		 	
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
	public ReporteDetalladoPorTipoBean consultaReporteDetalladoAjuste(ReporteDetalladoPorTipoBean datos, SessionScopeUser sessionData) throws CfeException, SQLException{
		ReporteDetalladoPorTipoBean reporte = null;
		try{
			switch(sessionData.getSociedad().getId()){
			case 1:{
				reporte = reportedetalladoAjusteDao.consultaReporteDetalladoTipo(datos);
			}break;
			case 2:{
				reporte = reporteDetalladoAjusteTipoDipselDao.consultaReporteDetalladoTipo(datos);
			}break;
			case 3:{
				reporte = reporteDetalladoAjusteTipoSercotelDao.consultaReporteDetalladoTipo(datos);
			}break;
			case 4:{
				reporte = reporteDetalladoAjusteTipoAmovDao.consultaReporteDetalladoTipo(datos);
			}break;
		}
		}catch(CfeException e){
			
		}
		return reporte;
	}
	
	

	
}
