package jdroplet.core;

import jdroplet.annotation.db.DataField;
import jdroplet.annotation.db.DataFieldType;
import jdroplet.core.fileupload.FileItem;
import jdroplet.core.fileupload.PostFile;
import jdroplet.core.fileupload.disk.DiskFileItemFactory;
import jdroplet.core.fileupload.disk.HttpPostedFile;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.fileupload.FileUploadException;
import jdroplet.exceptions.fileupload.MultipartHandlingException;
import jdroplet.util.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HttpRequest extends HttpServletRequestWrapper {

	private static Logger logger = Logger.getLogger(HttpRequest.class);

	private Map<String, Object> params;
	private List<HttpPostedFile> files;
	private String requestUri;
	private String encoding;
	private String pageExtension;
	private String username;
	private String rawPostData;
	private boolean isPostBack;
	private boolean isMultipart;
	private boolean isRawPostParsed;

	public HttpRequest(HttpServletRequest request) throws IOException {
		super(request);
		files = new ArrayList<HttpPostedFile>();
		parse(request);
	}
	
	@SuppressWarnings("unchecked")
	protected void parse(HttpServletRequest request) throws UnsupportedEncodingException {
		//this.request = new ApacheRequest(request);
		this.params = new HashMap<String, Object>();
		// 获取当前请求的虚拟路径
		this.requestUri = this.extractRequestUri(request.getRequestURI(), request.getContextPath());
		this.encoding = SystemConfig.getProperty(SystemConfig.ENCODING);
		this.pageExtension = null;
		// 是否为客户端回发
		this.isPostBack = "post".equals(request.getMethod().toLowerCase());
		// 是否上传数据
		this.isMultipart = false;

		String[] tmp = requestUri.split("\\.");
		this.pageExtension = tmp[tmp.length-1];
		if (isPostBack) {
			isMultipart = PostFile.isMultipartContent(this);
			if (isMultipart) {
				this.parseMultipart(request, encoding);
			}
		} 
		
		if (!isMultipart) {
			// 增加对 XMLHttpRequest 请求
			boolean isXMLHttpRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if (isXMLHttpRequest) {
				request.setCharacterEncoding("utf-8");
			} else {
				request.setCharacterEncoding(encoding);
			}
		}

		request.setCharacterEncoding(encoding);
		this.parseRewrittenUrl();
	}
	
	@SuppressWarnings("unchecked")
	public void addParameter(String key, Object value) {
		if (params.containsKey(key)) {// 处理重复参数的时候,例如 p=1&p=2&p=3
			Object p = this.getObjectParameter(key);
			List l;
			
			if (p instanceof List) {
				l = (List)p;
			} else {
				l = new ArrayList<String>();
				l.add(p);
			}
			l.add(value);
			this.params.put(key, l);
		} else {
			params.put(key, value);
		}
	}

	private void parseRawPostData() {
		String containerEncoding = isPostBack ? encoding : SystemConfig.getProperty(SystemConfig.DEFAULT_CONTAINER_ENCODING);
		String name = null;
		String[] values = null;
		ServletRequest request = this.getRequest();

		// 将URL中的参数保存
		for (Enumeration<String> e = (Enumeration<String>)request.getParameterNames(); e.hasMoreElements();) {
			name = e.nextElement();
			values = request.getParameterValues(name);

			if (values != null || values.length > 0) { // 处理重复参数的时候,例如 p=1&p=2&p=3
				for (String value : values) {
					try {
 						this.addParameter(name, new String(value.getBytes(containerEncoding), encoding));
					} catch (UnsupportedEncodingException e1) {
						throw new ApplicationException(e1.getMessage());
					}
				}
			} else {
				this.addParameter(name, request.getParameter(name));
			}
		}
		isRawPostParsed = true;
		// 如果在url查询串中没有获取到page和action再解析一次url
		if (this.getPage() == null && this.getAction() == null) {
			this.parseRewrittenUrl();
		}
	}

//	public String getScheme() {
//		return "https";
//	}

	private void parseRewrittenUrl() {
		String noExtUri  = this.requestUri.substring(0, this.requestUri.length() - this.pageExtension.length() - 1);
		String[] urlModel = noExtUri.split("/");
		RawUrl url = null;

		int siteIndex = 1;
		int pageIndex = 2;
		int methodIndex = 3;
		int minLen = 4;
		int paramCount = urlModel.length - minLen;
		if (paramCount >= 0) {
			// 获取url重写的相关信息
			StringBuilder sb = new StringBuilder(64)
					.append(urlModel[pageIndex])
					.append(".")
					.append(urlModel[methodIndex]);
			String serverName = getServerName();
			String applicationPath = getContextPath();
			String siteRoot = serverName + applicationPath + "/" + urlModel[siteIndex];

			url = SystemConfig.getSiteUrls().getRawUrl(siteRoot, sb.toString());
		}

		this.removeParameter("_site");
		this.removeParameter("_page");
		this.removeParameter("_action");

		if (url != null ) {
			// 将url重写后的参数保存
			if (url.getSize() >= paramCount) {
				for (int idx=0; idx<paramCount; idx++) {
					this.addParameter(url.getVars()[idx], urlModel[idx + minLen]);
				}
			}

			this.addParameter("_site", urlModel[siteIndex]);
			this.addParameter("_page", urlModel[pageIndex]);
			this.addParameter("_action", urlModel[methodIndex]);
		} else {
			this.addParameter("_site", urlModel[siteIndex]);
		}
	}

	private void readRawPostData() {
		InputStream is = null;
		StringBuffer sb = null;
		String s = null;
		BufferedReader br = null;

		sb = new StringBuffer();
		try {
			is = getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (Exception e) {
		} finally {
			try{br.close();}catch (Exception ex){}
			try{is.close();}catch (Exception ex){}
		}
		isRawPostParsed = true;
		rawPostData = sb.toString();
	}

	public String getRawPostData(){
		if (!isRawPostParsed) {
			this.readRawPostData();
		}
		return rawPostData;
	}
	
	/**
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String key) {
		return getParameter(key, null, false, false);
	}
	
	public String getParameter(String key, boolean stripTag, boolean stripSql) {
		return getParameter(key, null, stripTag, stripSql);
	}
	
	public String getParameter(String key, String default_value) {
		return getParameter(key, default_value, false, false);
	}
		
	@SuppressWarnings("unchecked")
	public String getParameter(String key, String default_value, boolean stripTag, boolean stripSql) {
		Object obj = null;

		obj = this.params.get(key);
		if (obj == null && !isRawPostParsed) {
			this.parseRawPostData();
			obj = this.params.get(key);
		}

		if (obj instanceof ArrayList<?>) {
			ArrayList<String> list = (ArrayList<String>) obj;
			default_value = list.get(0);
		} else {
			if (obj != null)
				default_value = (String) obj;
		}
		if (stripTag && !TextUtils.isEmpty(default_value))
			default_value = Transforms.stripHtmlXmlTags(default_value);

		if (stripSql && !TextUtils.isEmpty(default_value))
			default_value = Transforms.stripSQL(default_value);
		
		return default_value;
	}

	public Date getDateParameter(String key) {
		return getDateParameter(key, "yyyy-MM-dd HH:mm:ss");
	}

	public Date getDateParameter(String key, String pattern) {
		return getDateParameter(key, pattern, null);
	}

	public Date getDateParameter(String key, Date defaultValue) {
		return getDateParameter(key, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	public Date getDateParameter(String key, String pattern, Date defaultValue) {
		String val = null;
		
		val = this.getParameter(key);
		try {
			defaultValue = DateTime.parse(val).getDate();
		} catch (Exception ex) {
		}
		return defaultValue;
	}
	
	public Integer getIntParameter(String key) {
		return getIntParameter(key, null);
	}

	public Integer getIntParameter(String key, Integer default_value) {
		try {
			return Integer.parseInt(this.getParameter(key));
		} catch (Exception ex) {
			return default_value;
		}
	}

	public BigInteger getBigIntegerParameter(String key) {
		try {
			return new BigInteger(this.getParameter(key));
		} catch (Exception ex) {
			return new BigInteger("-1");
		}
	}
	
	public long getLongParameter(String key) {
		try {
			return Long.parseLong(this.getParameter(key));
		} catch (Exception ex) {
			return -1;
		}
	}
	
	public double getDoubleParameter(String key) {
		try {
			return Double.parseDouble(this.getParameter(key));
		} catch (Exception ex) {
			return -1.00;
		}
	}

	public BigDecimal getBigDecimalParameter(String key) {
		try {
			return new BigDecimal(this.getParameter(key));
		} catch (Exception ex) {
			return new BigDecimal(0);
		}
	}
	
	public Boolean getBooleanParameter(String key) {
		return getBooleanParameter(key, false);
	}
	
	public Boolean getBooleanParameter(String key, Boolean defaultValue) {
		String val = this.getParameter(key);
		if (val == null)
			return defaultValue;

		return Boolean.parseBoolean(val);
	}
	
	public JSONObject getJSONParameter(String key) {
		JSONObject obj = null;
		String val = getParameter(key);

		if (!TextUtils.isEmpty(val)) {
			try {
				obj = new JSONObject(val);
			} catch (JSONException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
		return obj;
	}

	public JSONArray getJSONArrayParameter(String key) {
		JSONArray obj = null;
		String val = getParameter(key);

		if (!TextUtils.isEmpty(val)) {
			try {
				obj = new JSONArray(val);
			} catch (JSONException e) {
				throw new ApplicationException(e.getMessage());
			}
		}
		return obj;
	}

	public Integer[] getBigIntegerParameterValues(String key) {
		String[] vals = super.getParameterValues(key);
		Integer[] new_vals = null;

		if (vals != null) {
			new_vals = new Integer[vals.length];

			for(int i=0; i<vals.length; i++) {
				new_vals[i] = new Integer(vals[i]);
			}
			return new_vals;
		}
		return new_vals;
	}

	public Integer[] getIntParameterValues(String key) {
		String[] vals = super.getParameterValues(key);
		Integer[] new_vals = null;
		List<Integer> list = new ArrayList<>();
		
		if (vals != null) {
			for(int i=0; i<vals.length; i++) {
				if (!TextUtils.isEmpty(vals[i]))
					list.add(Integer.parseInt(vals[i]));
			}
			new_vals = new Integer[list.size()];
			new_vals = list.toArray(new_vals);
		}
		return new_vals;
	}
	
	/**
	 * @deprecated inaccurate address 
	 */
	public String getRemoteAddr() {
		return super.getRemoteAddr();
	}
	
	public String getXRemoteAddr() {
		String ip = this.getHeader("x-forwarded-for");

		if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			ip = this.getHeader("Proxy-Client-IP");

		if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			ip = this.getHeader("WL-Proxy-Client-IP");

		if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			ip = this.getRemoteAddr();
		
		String[] ips = ip.split(",");
		ip = ips[0];
		return ip;
	}

	public <T> T getObjectParameter(String key, Class<T> clazz) {
		String str = getParameter(key);
		return JSONUtil.toObject(str, clazz);
	}

	public <T> T getObjectParameter(Class<T> clazz) {
		String rawData = getRawPostData();

		return JSONUtil.toObject(rawData, clazz);
	}

	public <T> T getObjectParameter(Class<T> clazz, Field[] fields) {
		T t = null;

		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}

		DataField df = null;
		String key = null;
		for (Field field : fields) {
			df = field.getAnnotation(DataField.class);
			key = field.getName();

			if (df.type() == DataFieldType.Integer) {
				ReflectUtil.setFieldInt(t, key, getIntParameter(key));
			} else if (df.type() == DataFieldType.BigInt) {
				ReflectUtil.setField(t, key, getBigIntegerParameter(key));
			} else {
				ReflectUtil.setField(t, key, getParameter(key));
			}
		}

		return t;
	}
	
	public Object getObjectParameter(String key) {
		return this.params.get(key);
	}
	
	public void removeParameter(String key) {
		this.params.remove(key);
	}

	public String getRequestUri() {
		return this.requestUri;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public String getPageExtension() {
		return this.pageExtension;
	}

	public String getPage() {
		return this.getParameter("_page");
	}
	
	public String getSite() {
		return this.getParameter("_site");
	}
	
	public String getAction() {
		return this.getParameter("_action");
	}
	
	public boolean isPostBack() {
		return isPostBack;
	}

	public boolean isMultipart() {
		return isMultipart;
	}
		
	public boolean isAuthenticated() {
		HttpContext context = HttpContext.current();
		if (context != null && context.getUser() != null && context.getUser().getIdentity() != null)
			return context.getUser().getIdentity().IsAuthenticated();
		return false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public List<HttpPostedFile> getFiles() {
		return files;
	}
	
	public Cookie getCookie(String name) {
		Cookie[] cookies = getCookies();
		Cookie cookie = null;
		
		if (cookies == null) 
			return null;
		
		for (Cookie c:cookies) {
    		if (c.getName().equals(name)) {
    			cookie = c;
    			break;
    		}
    	}
		return cookie;
	}

	public Integer getIntCookieValue(String name, Integer defaultValue) {
		return CookieUtil.getInt32(getCookies(), name, defaultValue);
	}

	public String getCookieValue(String name) {
		return getCookieValue(name, null);
	}

	public String getCookieValue(String name, String defaultValue) {
		String val = CookieUtil.getString(getCookies(), name);

		if (TextUtils.isEmpty(val))
			val = defaultValue;

		return val;
	}
	
	public String getSimpleServerName() {
		String[] temp = super.getServerName().split("\\.");
		String name = "";
		if (temp.length > 2)
			name = temp[temp.length - 2] + "." + temp[temp.length - 1];
		else
			name = temp[0] + "." + temp[1];

		return name;
	}
	
	private String extractRequestUri(String originRequestUri, String applicationPath) {
		if (applicationPath != null && applicationPath.length() > 0)
			originRequestUri = originRequestUri.substring(
					applicationPath.length(), originRequestUri.length());

		return originRequestUri;
	}

	private void parseMultipart(HttpServletRequest request, String encoding) throws UnsupportedEncodingException {
		String tmpPath = new StringBuffer(256).append(SystemConfig.getTempPath())
											    .toString();
		File tmpDir = new File(tmpPath);
		boolean success = false;

		try {
			success = tmpDir.exists();
			if (!success) {
				tmpDir.mkdirs();
				success = true;
			}
		} catch (Exception e) {
		}
	
		if (!success) {
			tmpPath = System.getProperty("java.io.tmpdir");
			tmpDir = new File(tmpPath);
		}
	
		PostFile upload = new PostFile(new DiskFileItemFactory(1024 * 1024, tmpDir));
		upload.setHeaderEncoding(encoding);
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			for (Iterator<FileItem> iter = items.iterator(); iter.hasNext(); ) {
				FileItem item = (FileItem)iter.next();
				
				if (item.isFile()) 
					files.add(item.getAsPostedFile());
				
				if (item.isFormField()) {
					this.addParameter(item.getFieldName(), item.getString(encoding));
				}
				else {
					if (item.getSize() > 0) {
						// We really don't want to call addParameter(), as 
						// there should not be possible to have multiple
						// values for a InputStream data
						this.params.put(item.getFieldName(), item);
					}
				}
			}
		} catch (FileUploadException e) {
			logger.error("parseMultipart:", e);
			throw new MultipartHandlingException("Error while processing multipart content: " + e);
		}
	}

	public boolean isMobileDevice() {
		String agent = this.getHeader("User-Agent").toLowerCase();
		Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("mobile|pda;|avantgo|eudoraweb|minimo|netfront|brew|teleca|lg;|lge |wap;| wap ");
        matcher = pattern.matcher(agent);
        if (matcher.find())
            return true;

        pattern = Pattern.compile("phone|iphone|ipad|itouch|ipod|symbian|android|htc_|htc-|palmos|blackberry|opera mini|iemobile|windows ce|nokia|fennec|hiptop|kindle|mot |mot-|webos\\/|samsung|sonyericsson|^sie-|nintendo");
        matcher = pattern.matcher(agent);
        if (matcher.find())
            return true;
        
		return false;
	}

	public boolean isWechatBrowser() {
		String agent = this.getHeader("User-Agent").toLowerCase();
		return agent.indexOf("micromessenger") >= 0;
	}

	public boolean isAppleMobileBrowser() {
		String agent = this.getHeader("User-Agent").toLowerCase();
		Pattern pattern = null;
		Matcher matcher = null;

		pattern = Pattern.compile("phone|iphone|ipad|itouch|ipod");
		matcher = pattern.matcher(agent);
		if (matcher.find())
			return true;

		return false;
	}

	public String getUrl() {
		if (TextUtils.isEmpty(super.getQueryString()))
			return super.getRequestURL().toString();
		else
			return super.getRequestURL() + "?" + super.getQueryString();
	}

}
