package com.telcel.gsa.dsaf.bean;

import java.io.Serializable;
/**
 * 
 * @author VI5XXAA
 *
 */
public class LoginBean implements Serializable{
	private static final long serialVersionUID = 6977611649346362410L;

	private String username;
	private String code;
	/**
	 * @return el username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username el username a establecer
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return el code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code el code a establecer
	 */
	public void setCode(String code) {
		this.code = code;
	}
	 @Override
	 
	         public String toString()
	 
	         {
	 
	                 return "LoginBean [userName=" + username + ", password=" + code + "]";
	 
	         }

}
