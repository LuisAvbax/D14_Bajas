package com.telcel.gsa.dsaf.service;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.telcel.gsa.dsaf.bean.ConsumoRegionBean;
import com.telcel.gsa.dsaf.bean.ResumenRegionBean;

/**
 * 
 * @author VI5XXAA
 *
 */
public interface ResumenRegService extends Serializable{
	public List<ConsumoRegionBean> resumenPorRegion(ResumenRegionBean resumenRegionBean);
	public void agregaColumna(HSSFRow row, ConsumoRegionBean crBean, Integer colNum, HSSFCellStyle style);
	public void agregaColumnaCont(HSSFRow row, ConsumoRegionBean crBean, Integer colNum, HSSFCellStyle style);
	
}
