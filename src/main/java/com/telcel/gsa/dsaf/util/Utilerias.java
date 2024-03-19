package com.telcel.gsa.dsaf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utilerias implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1242835114067551516L;

	final static Logger logger = LoggerFactory.getLogger(Utilerias.class);
	public Date formatoFecha(Date fecha){
		 DateFormat  format = new SimpleDateFormat("dd/MM/yyyy");		 
		 Date date = null;
			 String f1 = format.format(fecha);
			 try {
				date = format.parse(f1);
			} catch (ParseException e) {
				logger.info("");
			}
		
		 return date;
	 }
	
	public static String formatoSoloFecha(Date fecha){
		 DateFormat  format = new SimpleDateFormat("dd-MM-yyyy");		 
			 return format.format(fecha);
	 }
	
	public static String formatoBD(Date fecha){
		 DateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 
			 return format.format(fecha);
	 }
	
	public static String agregaZeros(String s, int length) {
	    if (s.length() >= length) return s;
	    else return String.format("%0" + (length-s.length()) + "d%s", 0, s);
	}
	
	public static void createDestinationDataFile(String destinationName, Properties connectProperties)
	{
	    File destCfg = new File(destinationName+".jcoDestination");
	    try
	    {
	        FileOutputStream fos = new FileOutputStream(destCfg, false);
	        connectProperties.store(fos, "for tests only !");
	        fos.close();
	    }
	    catch (Exception e)
	    {
	        throw new RuntimeException("Unable to create the destination files", e);
	    }
	}
	
	public static List<Integer> recuperaAniosAtras(Date fechaActual, Integer cantidad){
		List<Integer> anios = new ArrayList<Integer>();
		Calendar c = Calendar.getInstance();
		Date fechaInicio = null;
		
		c.setTime(fechaActual);
		c.add(Calendar.YEAR, cantidad);
		fechaInicio = c.getTime();
		  c.setTime(fechaInicio);
		while (fechaInicio.before(fechaActual)) {
          
            anios.add(c.get(Calendar.YEAR));
            c.add(Calendar.YEAR, 1);
            fechaInicio = c.getTime();
            
        }
		c.setTime(fechaActual);
		anios.add(c.get(Calendar.YEAR));
		return anios;
	}

}


