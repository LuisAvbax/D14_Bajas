package com.telcel.gsa.dsaf.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telcel.gsa.dsaf.amov.dao.CienDepreciadosAmovDao;
import com.telcel.gsa.dsaf.amov.dao.ReporteCienDepreciadosAmovDao;
import com.telcel.gsa.dsaf.bean.BajasDepreciadosCienBean;
import com.telcel.gsa.dsaf.bean.BajasDprCienFormBean;
import com.telcel.gsa.dsaf.bean.BajasOpDocumentoBean;
import com.telcel.gsa.dsaf.dao.CienDepreciadosDao;
import com.telcel.gsa.dsaf.dipcel.dao.CienDepreciadosDipselDao;
import com.telcel.gsa.dsaf.dipcel.dao.ReporteCienDepreciadosDipselDao;
import com.telcel.gsa.dsaf.entity.BajasDepreciadosCien;
import com.telcel.gsa.dsaf.sercotel.dao.CienDepreciadosSercotelDao;
import com.telcel.gsa.dsaf.sercotel.dao.ReporteCienDepreciadosSercotelDao;
import com.telcel.gsa.dsaf.service.ReporteCienDprBaService;
import com.telcel.gsa.dsaf.util.CfeException;
import com.telcel.gsa.dsaf.util.Constants;

@Service("reporteCienDprBaService")
public class ReporteCienDprBaServiceImpl implements ReporteCienDprBaService, Serializable {

	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(ReporteCienDprBaService.class);

	private static final long serialVersionUID = -290824588675622989L;

	@Autowired
	CienDepreciadosDao ciendDprBaDao;

	@Autowired
	CienDepreciadosAmovDao cienDprAmovDao;

	@Autowired
	CienDepreciadosSercotelDao cienDprSercoDao;

	@Autowired
	@Qualifier("cienDepreciadosDipselImpl")
	CienDepreciadosDipselDao cienDprDipselDao;

	@Autowired
	ReporteCienDepreciadosDipselDao reporteCienDepreciadosDipselDao;

	@Autowired
	ReporteCienDepreciadosSercotelDao reporteCienDepreciadosSercotelDao;

	@Autowired
	ReporteCienDepreciadosAmovDao reporteCienDepreciadosAmovDao;

	@Override
	public void insertaArchivo(byte[] bytes, Integer anio, Integer mes, Integer sociedadId, Integer usuarioId)
			throws CfeException, SQLException {
		switch (sociedadId) {
		case 1: {
			ciendDprBaDao.insertaArchivo(bytes, anio, mes, sociedadId, usuarioId);
		}
			break;
		case 2: {
			cienDprDipselDao.insertaArchivo(bytes, anio, mes, sociedadId, usuarioId);
		}
			break;
		case 3: {
			cienDprSercoDao.insertaArchivo(bytes, anio, mes, sociedadId, usuarioId);
		}
			break;
		case 4: {
			cienDprAmovDao.insertaArchivo(bytes, anio, mes, sociedadId, usuarioId);
		}
			break;
		}
	}

	@Override
	public HSSFWorkbook generaDocumento(List<BajasDepreciadosCienBean> datos) throws IOException {
		BajasDepreciadosCienBean datosDpr = (BajasDepreciadosCienBean) datos;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Depreciados");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHA);

		int rowNum = 0;
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);
		// celdas a combinar
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 11));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 11));

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));

		// Create the column headings
		HSSFRow headerRow = sheet.createRow(rowNum++);
		headerRow.createCell((short) 1).setCellValue(new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow(rowNum++);
		headerRow.createCell((short) 1).setCellValue(new HSSFRichTextString(Constants.TIT_REP_DPR));
		headerRow.getCell(1).setCellStyle(headerStyle);

		// Create the column headings
		rowNum++;
		rowNum++;
		rowNum++;
		headerRow = sheet.createRow(rowNum);
		headerRow.createCell((short) 3).setCellValue(new HSSFRichTextString("MES"));
		headerRow.createCell((short) 4).setCellValue(new HSSFRichTextString("AÑO"));
		headerRow.createCell((short) 5).setCellValue(new HSSFRichTextString("NUMERO ACTIVO"));
		headerRow.createCell((short) 6).setCellValue(new HSSFRichTextString("SUBNUMERO"));
		headerRow.createCell((short) 7).setCellValue(new HSSFRichTextString("USUARIO"));
		headerRow.createCell((short) 8).setCellValue(new HSSFRichTextString("FECHA"));

		for (BajasDepreciadosCienBean b : datos) {
			// Create a new row in the sheet:
			HSSFRow row = sheet.createRow(++rowNum);

			row.createCell((short) 3).setCellValue(b.getDescMes());
			row.createCell((short) 4).setCellValue(b.getAnio());
			row.createCell((short) 5).setCellValue(b.getNumeroActivo());
			row.createCell((short) 6).setCellValue(b.getSubnumero());
			row.createCell((short) 7).setCellValue(b.getaPaterno() + " " + b.getaMaterno() + " " + b.getNombre());
			row.createCell((short) 8).setCellValue(sdf.format(b.getFechaCarga()));

		}

		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);

		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		if (datosDpr.getDescSociedad().equals("TELCEL")) {
			fullPath.append("TelceLogo3.gif");
		}
		if (datosDpr.getDescSociedad().equals("SERCOTEL")) {
			fullPath.append("logoSercotel.jpg");
		}
		if (datosDpr.getDescSociedad().equals("AMERICA MOVIL")) {
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
	public List<BajasOpDocumentoBean> consultaArchivo() throws CfeException, SQLException {
		return ciendDprBaDao.descargaArchivoBlob();
	}

	@Override
	public HSSFWorkbook generaDocumentoOp(List<BajasDepreciadosCienBean> datos) throws IOException {
		BajasDepreciadosCienBean datosDpr = (BajasDepreciadosCienBean) datos;
		List<BajasDepreciadosCien> datCienSa = datosDpr.getListSave();
		List<BajasDepreciadosCien> datCienUp = datosDpr.getListUpdt();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Depreciados");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMATOFECHA);

		int rowNum = 0;
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);
		// celdas a combinar
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 11));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 11));

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat hssfDataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00##"));

		// Create the column headings
		HSSFRow headerRow = sheet.createRow(rowNum++);
		headerRow.createCell((short) 1).setCellValue(new HSSFRichTextString(Constants.TIT_BAJAS_ACTIVO_FIJO));
		headerRow.getCell(1).setCellStyle(headerStyle);
		headerRow = sheet.createRow(rowNum++);
		headerRow.createCell((short) 1).setCellValue(new HSSFRichTextString(Constants.TIT_REP_DPR));
		headerRow.getCell(1).setCellStyle(headerStyle);

		// Create the column headings
		rowNum++;
		rowNum++;
		rowNum++;
		headerRow = sheet.createRow(rowNum);
		headerRow.createCell((short) 3).setCellValue(new HSSFRichTextString("MES"));
		headerRow.createCell((short) 4).setCellValue(new HSSFRichTextString("AÑO"));
		headerRow.createCell((short) 5).setCellValue(new HSSFRichTextString("NUMERO ACTIVO"));
		headerRow.createCell((short) 6).setCellValue(new HSSFRichTextString("SUBNUMERO"));
		headerRow.createCell((short) 7).setCellValue(new HSSFRichTextString("USUARIO"));
		headerRow.createCell((short) 8).setCellValue(new HSSFRichTextString("FECHA"));

		for (BajasDepreciadosCienBean b : datos) {
			// Create a new row in the sheet:
			if (datCienSa != null) {
				for (BajasDepreciadosCien dprLst : datCienSa) {
					HSSFRow row = sheet.createRow(++rowNum);

					row.createCell((short) 3).setCellValue(dprLst.getMes());
					row.createCell((short) 4).setCellValue(dprLst.getAnio());
					row.createCell((short) 5).setCellValue(dprLst.getNumeroActivo());
					row.createCell((short) 6).setCellValue(dprLst.getSubnumero());
					row.createCell((short) 7)
							.setCellValue(b.getaPaterno() + " " + b.getaMaterno() + " " + b.getNombre());
					row.createCell((short) 8).setCellValue(sdf.format(b.getFechaCarga()));
				}
			} else {
				for (BajasDepreciadosCien dprLst : datCienUp) {
					HSSFRow row = sheet.createRow(++rowNum);

					row.createCell((short) 3).setCellValue(dprLst.getMes());
					row.createCell((short) 4).setCellValue(dprLst.getAnio());
					row.createCell((short) 5).setCellValue(dprLst.getNumeroActivo());
					row.createCell((short) 6).setCellValue(dprLst.getSubnumero());
					row.createCell((short) 7)
							.setCellValue(b.getaPaterno() + " " + b.getaMaterno() + " " + b.getNombre());
					row.createCell((short) 8).setCellValue(sdf.format(b.getFechaCarga()));
				}
			}
		}

		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);

		StringBuffer fullPath = new StringBuffer();
		fullPath.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""));
		fullPath.append(File.separator + "css" + File.separator + "img" + File.separator);
		if (datosDpr.getDescSociedad().equals("TELCEL")) {
			fullPath.append("TelceLogo3.gif");
		}
		if (datosDpr.getDescSociedad().equals("SERCOTEL")) {
			fullPath.append("logoSercotel.jpg");
		}
		if (datosDpr.getDescSociedad().equals("AMERICA MOVIL")) {
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

	public void insertarRegistro(Integer sociedadId, Integer usuarioId) throws CfeException, SQLException {
		switch (sociedadId) {
		case 1: {
			ciendDprBaDao.insertaRegistro(sociedadId, usuarioId);
		}
			break;
		case 2: {
			cienDprDipselDao.insertaRegistro(sociedadId, usuarioId);
		}
			break;
		case 3: {
			cienDprSercoDao.insertaRegistro(sociedadId, usuarioId);
			;
		}
			break;
		case 4: {
			cienDprAmovDao.insertaRegistro(sociedadId, usuarioId);
			;
		}
			break;
		}
	}

	@Override
	public void insertaSolicitud(BajasDprCienFormBean dprCienFormBean, String nombreRepo)
			throws CfeException, SQLException {
		switch (dprCienFormBean.getIdSociedad()) {
		case 1:// Telcel
			ciendDprBaDao.insertaSolicReporte(dprCienFormBean.getAnio(), dprCienFormBean.getMes().getId(),
					dprCienFormBean.getIdSociedad(), dprCienFormBean.getIdUsuario(), "REP", "REP CIENDEPRE",
					"REPORTE CIEN DEPRE", nombreRepo);
			break;
		case 2:// DIPCEL
			reporteCienDepreciadosDipselDao.insertaSolicReporte(dprCienFormBean.getAnio(),
					dprCienFormBean.getMes().getId(), dprCienFormBean.getIdSociedad(), dprCienFormBean.getIdUsuario(),
					"REP", "REP CIENDEPRE", "REPORTE CIEN DEPRE", nombreRepo);
			break;
		case 3:// SERCOTEL
			reporteCienDepreciadosSercotelDao.insertaSolicReporte(dprCienFormBean.getAnio(),
					dprCienFormBean.getMes().getId(), dprCienFormBean.getIdSociedad(), dprCienFormBean.getIdUsuario(),
					"REP", "REP CIENDEPRE", "REPORTE CIEN DEPRE", nombreRepo);
			break;
		case 4:// A-MOVIL
			reporteCienDepreciadosAmovDao.insertaSolicReporte(dprCienFormBean.getAnio(),
					dprCienFormBean.getMes().getId(), dprCienFormBean.getIdSociedad(), dprCienFormBean.getIdUsuario(),
					"REP", "REP CIENDEPRE", "REPORTE CIEN DEPRE", nombreRepo);
			break;
		default:
		}
	}

	@Override
	public BajasOpDocumentoBean obtenerDocumento(BajasDprCienFormBean dprBean) throws CfeException, SQLException {
		return ciendDprBaDao.obtenerReporteCienDepre("REP", "REP CIENDEPRE", dprBean.getIdSociedad(), dprBean.getAnio(),
				dprBean.getMes().getId(), dprBean.getIdUsuario());
	}

	@Override
	public Workbook obtenerReporte(Integer idDoc, Integer sociedadId) throws CfeException, SQLException, IOException {
		switch (sociedadId) {
		case 1:// Telcel
			return ciendDprBaDao.obtieneReporte(idDoc);
		case 2:// DIPCEL
			return reporteCienDepreciadosDipselDao.obtieneReporte(idDoc);
		case 3:// SERCOTEL
			return reporteCienDepreciadosSercotelDao.obtieneReporte(idDoc);
		case 4:// A-MOVIL
			return reporteCienDepreciadosAmovDao.obtieneReporte(idDoc);
		default:
			return null;
		}
	}

	@Override
	public void crearReporte(BajasDprCienFormBean dprBean) throws CfeException, SQLException {
		switch (dprBean.getIdSociedad()) {
		case 1:// Telcel
			ciendDprBaDao.spBajasDoc(2, dprBean.getIdUsuario(), dprBean.getIdSociedad());
			break;
		case 2:// DIPCEL
			reporteCienDepreciadosDipselDao.spBajasDoc(2, dprBean.getIdUsuario(), dprBean.getIdSociedad());
			break;
		case 3:// SERCOTEL
			reporteCienDepreciadosSercotelDao.spBajasDoc(2, dprBean.getIdUsuario(), dprBean.getIdSociedad());
			break;
		case 4:// A-MOVIL
			reporteCienDepreciadosAmovDao.spBajasDoc(2, dprBean.getIdUsuario(), dprBean.getIdSociedad());
			break;
		default:
			break;
		}
	}

	@Override
	public BajasOpDocumentoBean obtenerDocumentoReciente(BajasDprCienFormBean dprBean)
			throws CfeException, SQLException {

		switch (dprBean.getIdSociedad()) {
		case 1:// Telcel
			return ciendDprBaDao.obtenerReporteCienDepreReciente("REP", "REP CIENDEPRE", dprBean.getIdSociedad(),
					dprBean.getIdUsuario());
		case 2:// DIPCEL
			return reporteCienDepreciadosDipselDao.obtenerReporteCienDepreReciente("REP", "REP CIENDEPRE",
					dprBean.getIdSociedad(), dprBean.getIdUsuario());
		case 3:// SERCOTEL
			return reporteCienDepreciadosSercotelDao.obtenerReporteCienDepreReciente("REP", "REP CIENDEPRE",
					dprBean.getIdSociedad(), dprBean.getIdUsuario());
		case 4:// A-MOVIL
			return reporteCienDepreciadosAmovDao.obtenerReporteCienDepreReciente("REP", "REP CIENDEPRE",
					dprBean.getIdSociedad(), dprBean.getIdUsuario());
		default:
			return null;
		}

	}
}
