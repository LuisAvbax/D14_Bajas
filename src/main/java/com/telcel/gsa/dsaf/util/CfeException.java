package com.telcel.gsa.dsaf.util;
/**
 * 
 * @author VI5XXAA
 *
 */
public class CfeException extends RuntimeException{

	private static final long serialVersionUID = 5759633419294742078L;
	private final String mensaje;
	
public CfeException(String mensaje, Throwable e){
	super(e==null?null:e.getMessage(), e);
	this.mensaje = mensaje;
}
/**
 * @return el mensaje
 */
public String getMensaje() {
	return mensaje;
}


}
