package jdroplet.app.core;

import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.core.SystemConfig;
import jdroplet.enums.ActionStatus;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.Encoding;
import jdroplet.util.JSONUtil;
import jdroplet.util.StatusData;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class JdropletAPIServlet extends JdropletPageServlet {
	
	private static Logger logger = Logger.getLogger(JdropletAPIServlet.class);
	
	@Override
	public void init(final ServletConfig servlet_config) throws ServletException {
		String appPath = null;

		super.init(servlet_config);
		// 初始化配置文件
		appPath = servlet_config.getServletContext().getContextPath();

		SystemConfig.init(servlet_config, appPath);

		// 初始化日志文件
		System.setProperty("log4jdir", SystemConfig.getServerPath());
		DOMConfigurator.configure(SystemConfig.getServerPath() + "/WEB-INF/log4j.xml");

		// 加载插件
		PluginFactory.getInstance().launch();
	}

	@Override
	protected Writer handleErr(HttpContext context, Throwable error) {
		StatusData sd = null;
		OutputStream os = null;
		HttpRequest request = context.request();
		HttpResponse response = context.response();
		
		sd = new StatusData();
		sd.setStatus(ActionStatus.ERROR.getValue());
		sd.setMsg(error.getMessage());
		if (error instanceof AuthorizationException) {
			sd.setStatus(ActionStatus.UNLOGIN.getValue());
		}

		response.setHeader("access-control-allow-credentials", "true");
		response.setHeader("access-control-allow-headers", "Content-Type, Authorization, X-Requested-With");
		response.setHeader("access-control-allow-methods", "PUT,POST,GET,DELETE,OPTIONS");
		if (TextUtils.isEmpty(request.getHeader("Origin"))) {
			response.setHeader("access-control-allow-origin", "*");
		} else {
			response.setHeader("access-control-allow-origin", request.getHeader("Origin"));
		}

		try {
			os = response.getOutputStream();
			os.write(JSONUtil.toJSONString(sd).getBytes("utf-8"));
		} catch (IOException ex) {
			throw new ApplicationException(ex.getMessage());
		} finally {
			if (os != null) {
				try { os.close(); }
				catch (Exception e) {}
			}
		}
		return null;
	}
}
