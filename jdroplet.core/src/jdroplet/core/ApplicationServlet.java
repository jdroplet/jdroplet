package jdroplet.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdroplet.enums.ActionStatus;
import jdroplet.exceptions.ApplicationException;
import jdroplet.util.JSONUtil;
import jdroplet.util.StatusData;
import jdroplet.util.TextUtils;

import org.apache.log4j.Logger;

public class ApplicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ApplicationServlet.class);
	
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		HttpRequest request = null;
		HttpResponse response = null;
		HttpContext context = null;
		FormsAuthenticationModule authModule = null;
		
		request = new HttpRequest(req);
		response = new HttpResponse(resp);

		context = new HttpContext(request, response);
					
		authModule = new FormsAuthenticationModule(context);
		authModule.beginAuthenticateRequest();
	}

	protected Writer handlePage(HttpContext context, Page page) throws Throwable {
		Writer out = null;

		page.preInit(context);
		page.initial();
		page.preRender();
		String encoding = SystemConfig.getProperty(SystemConfig.ENCODING);

		if (context.response().getRedirectUrl() == null) {
			String contentType = context.response().getContentType();
			if (contentType == null) 
				contentType = "text/html; charset=" + encoding;
			
			context.response().setContentType(contentType);
			if (!page.isCustomContent()) {
				out = new BufferedWriter(new OutputStreamWriter(context.response().getOutputStream(), encoding));
				page.render(out);
				out.flush();
			}
		}
		return out;
	}
}
