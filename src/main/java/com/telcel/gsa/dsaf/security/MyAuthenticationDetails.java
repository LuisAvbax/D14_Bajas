package com.telcel.gsa.dsaf.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class MyAuthenticationDetails extends WebAuthenticationDetails {
	private static final long serialVersionUID = 1L;
    private final String method;
 
    public MyAuthenticationDetails(final HttpServletRequest request) {
        super(request);
        method = request.getParameter("method");
    }
 
    public String getMethod() {
        return method;
    }
}
