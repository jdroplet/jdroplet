package jdroplet.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.Cookie;

import jdroplet.bll.SiteManager;
import jdroplet.data.model.Site;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.CoreException;
import jdroplet.freemarker.core.tmm.*;
import jdroplet.net.RequestMethod;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.JSONUtil;
import jdroplet.util.StatusData;
import jdroplet.util.TextUtils;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public abstract class Page extends PageBase {
	private static Class<?>[] NO_PARAMS_CLASS = new Class<?>[0];
	private static Object[] NO_PARAMS_OBJECT = new Object[0];
	                                                      
	protected String templateName;
	protected HttpContext context;
	protected HttpRequest request;
	protected HttpResponse response;
	protected SimpleHash variables;
	protected boolean isCustomContent = false;
	private freemarker.template.Configuration fmConfig;
	
	void preRender() throws Throwable {
		// 调用指定方法,若产生NoSuchMethodException,则调用默认的show方法
		String method = this.context.request().getAction();

		if (RequestMethod.OPTIONS.equals(request.getMethod())) {
			handleOptionMethod();
		} else {
			try {
				if (method == null || "show".equals(method))
					this.show();
				else
					this.getClass().getMethod(method, NO_PARAMS_CLASS).invoke(this, NO_PARAMS_OBJECT);
			} catch (NoSuchMethodException ex) {
				this.show();
			} catch (InvocationTargetException ex) {
				handleInvocationException(ex);
			} catch (CoreException ex) {
				throw ex;
			}
		}
	}
	
	void render(Writer out) throws Exception {
		Template t = null;

		t = fmConfig.getTemplate(this.templateName);
		t.process(variables, out);
	}

	void handleInvocationException(InvocationTargetException ite) throws Throwable  {
		throw ite.getTargetException();
	}

	protected void handleOptionMethod() {}

	// 内部块
	protected boolean hasFilters(String tag) {
		return PluginFactory.getInstance().hasFilters(tag);
	}
	
	protected boolean hasActions(String tag) {
		return PluginFactory.getInstance().hasActions(tag);
	}
	
	protected Object applyFilters(String tag, Object result, Object...obj) {
		return PluginFactory.getInstance().applyFilters(tag, result, obj);
	}
	
	protected void doAction(String tag, Object...obj) {
		PluginFactory.getInstance().doAction(tag, obj);
	}

	protected void addFilter(String tag, Class clazz, String method) {
		PluginFactory.getInstance().addFilter(tag, clazz, method, 10);
	}

	protected void addAction(String tag, Class clazz, String method) {
		PluginFactory.getInstance().addAction(tag, clazz, method, 10);
	}

	protected void preInit(HttpContext context) {
		String action = null;
		String page = null;
		String skinname = null;
		Site site = null;
		
		this.context = context;
		this.request = context.request();
		this.response = context.response();
		this.variables = new SimpleHash();
		this.fmConfig = SystemConfig.getFreemarkerConfig();
		
		site = SiteManager.getCurrentSite();
		action = request.getAction();
		if (action == null) {
			action = "show";
		}
		page = request.getPage();
		if (page == null) {
			page = this.getClass().getSimpleName().toLowerCase().replace("page", "");					
		}
		skinname = page + "-" + action;
		
		this.setTemplateName(skinname);

		addItem("context", context);
		addItem("request", context.request());
		addItem("response", context.response());
		addItem("uploadUri", SystemConfig.getUploadPath() + "/" + site.getTheme());
		addItem("themeUri", SystemConfig.getTemplatePath() + "/" + site.getTheme());
		addItem("siteUrls", SystemConfig.getSiteUrls());
		addItem("pageExtension", SystemConfig.getPageExtension());
		addItem("pageSize", SystemConfig.getDataPageSize());
		addItem("site", SiteManager.getCurrentSite());
		
		addItem("Formater", new Formater());
		addItem("ToPinyin", new ToPinyin());
		addItem("SingleLine", new SingleLine());
		addItem("Split", new Split());
		addItem("And", new And());
		addItem("Or", new Or());
		addItem("ToMap", new ToMap());
		addItem("Render", new Render());
		
		// 加载静态类
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel factory = wrapper.getStaticModels();
		TemplateHashModel staticObj;
		try {
			staticObj = (TemplateHashModel)factory.get("jdroplet.core.SystemConfig");
			addItem("SystemConfig", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.util.Formatter");
			addItem("Formatter", staticObj);
			
			staticObj = (TemplateHashModel)factory.get("jdroplet.util.Transforms");
			addItem("Transforms", staticObj);
			
			staticObj = (TemplateHashModel)factory.get("jdroplet.util.I18n");
			addItem("I18n", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.bll.Posts");
			addItem("Posts", staticObj);

			staticObj = (TemplateHashModel) factory.get("jdroplet.enums.SortPostsBy");
			addItem("SortPostsBy", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.bll.Sections");
			addItem("Sections", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.bll.Users");
			addItem("Users", staticObj);
						
			staticObj = (TemplateHashModel)factory.get("jdroplet.core.DateTime");
			addItem("DateTime", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.cache.AppCache");
			addItem("AppCache", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.bll.Templates");
			addItem("Templates", staticObj);

			staticObj = (TemplateHashModel)factory.get("jdroplet.enums.SortOrder");
			addItem("SortOrder", staticObj);
		} catch (TemplateModelException e) {
		}
		
		setContentType("text/html;charset=" + this.context.request().getEncoding());
	}

	protected void render(String str) {
		render(str, SystemConfig.getProperty(SystemConfig.DEFAULT_CONTAINER_ENCODING));
	}
	
	protected void render(String str, String chartset) {
		byte[] bytes = null;
		
		try {
			bytes = str.getBytes(chartset);
		} catch (UnsupportedEncodingException e) {
		}
		render(bytes);
	}

	public boolean isCustomContent() {
		return isCustomContent;
	}

	protected void render(byte[] bytes) {
		OutputStream os = null;

		isCustomContent = true;
		///setContentType("text/plain");
		try {
			os = response.getOutputStream();
			os.write(bytes);
		} catch (IOException ex) {
			throw new ApplicationException(ex.getMessage());
		} finally {			
			if (os != null) {
				try { os.close(); }
				catch (Exception e) {}
			}
		}
	}

	protected void renderJSON(int status) {
		renderJSON(status, "", null);
	}

	protected void renderJSON(int status, String message) {
		renderJSON(status, message, null);
	}

	protected void renderJSON(int status, String message, Object data) {
		StatusData sd = new StatusData();
		
		sd.setStatus(status);
		sd.setMsg(message);
		sd.setData(data);
		render(sd);
	}
	
	protected void render(StatusData sd) {
		renderJSON(JSONUtil.toJSONString(sd));
	}
	
	protected void renderJSON(String content) {
		String type = request.getParameter("t");
		String var = request.getParameter("v");
		
		if (TextUtils.isEmpty(var))
			var = "__page_data";
		
		if (TextUtils.isEmpty(type)) {
			var = request.getParameter("var");
			if (TextUtils.isEmpty(var) == false)
				type = "var";
		}
		
		if ("jsonp".equals(type))
			content = var + "(" + content + ")";
		else if ("var".equals(type))
			content = "var " + var + "=" + content;
		
		render(content);
	}

	protected void setReturnPath(String path) {
		String returnPath = path;

		if (TextUtils.isEmpty(path))
			return;
//		if (returnPath == null || returnPath.equals("")) {
//			returnPath = this.request.getHeader("referer");
//			if (returnPath == null) {
//				returnPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + SystemConfig.getAppRoot() + request.getRequestUri();
//			}
//		}
		Cookie cookie = new Cookie("redirect", returnPath);
		cookie.setPath("/");
		cookie.setDomain(request.getServerName());
		cookie.setMaxAge(360);
		response.addCookie(cookie);
		
		context.setItem("redirect", returnPath);
	}
	
	protected String getReturnPath() {
		Cookie cookie = request.getCookie("redirect");
		String returnPath = null;
		
		if (cookie != null) {
			returnPath = cookie.getValue();
		}
		if (returnPath == null) {
			returnPath = (String)context.getItem("redirect");
		}
		return returnPath;
	}
		
	public String getTemplateName() {
		return templateName;
	}
	
	protected void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public void addItem(String key, Object value) {
		this.variables.put(key, value);
	}
	
	protected void setContentType(String type) {
		this.response.setContentType(type);
	}

	public void show() {
	}
}
