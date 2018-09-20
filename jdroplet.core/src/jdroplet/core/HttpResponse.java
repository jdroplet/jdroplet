package jdroplet.core;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpResponse extends HttpServletResponseWrapper {
	private static Logger logger = Logger.getLogger(HttpResponse.class);


	private String redirectUrl;
	
	public HttpResponse(HttpServletResponse response) {
		super(response);
		redirectUrl = null;
	}

	public void set301Redirect(String location) {
		redirectUrl = location;
		setStatus(301, "301 Moved Permanently");
		addHeader("Location", redirectUrl);
		addHeader("Cache-Control", "no-cache");
	}

	public void set302Redirect(String location) {
		redirectUrl = location;
		setStatus(302);
		addHeader("Location", redirectUrl);
	}

	public void setRedirect(String location) {
		sendRedirect(redirectUrl = location);
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void sendRedirect(String location) {
		try {
			super.sendRedirect(location);
		} catch (IOException e) {
			logger.error("sendRedirect" + e);
		}
	}
}
