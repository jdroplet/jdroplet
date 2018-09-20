<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="jdroplet.cache.AppCache" %>
<%@ page import="jdroplet.cache.ICache" %>
<%@ page import="jdroplet.net.WebClient" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%
	String agent = request.getHeader("User-Agent").toLowerCase();
	Pattern pattern = null;
    Matcher matcher = null;
	boolean isMobileDevice = false;
	
    pattern = Pattern.compile("mobile|pda;|avantgo|eudoraweb|minimo|netfront|brew|teleca|lg;|lge |wap;| wap");
    matcher = pattern.matcher(agent);
    if (matcher.find())
        isMobileDevice = true;

    pattern = Pattern.compile("phone|iphone|ipad|itouch|ipod|symbian|android|htc_|htc-|palmos|blackberry|opera mini|iemobile|windows ce|nokia|fennec|hiptop|kindle|mot |mot-|webos\\/|samsung|sonyericsson|^sie-|nintendo");
    matcher = pattern.matcher(agent);
    if (matcher.find())
        isMobileDevice = true;

	String url = null;
	String content = null;
	String domain = null;
	String content_key = null;
		
    domain = request.getServerName();
	content_key = "index_content:" + domain + "#" + isMobileDevice;
	content = (String) AppCache.get(content_key);
	if (content == null) {
		Properties props = null;
		String props_key = null;
		HashMap<String, String> propertys = new HashMap<String, String>();
		
		props_key = "app:domains";
		props = (Properties) AppCache.get(props_key);
		if (props == null) {
			String path = null;
			FileInputStream input = null;

			path = application.getRealPath("/") + "domains.properties";
			input = new FileInputStream(path);

			props = new Properties();
			props.load(input);
			AppCache.add(props_key, props, ICache.MONTH_FACTOR);
		}

		url = (String) props.get(domain);
		if (url != null) {
			WebClient client = new WebClient();

			//client.addProperty("User-Agent", request.getHeader("User-Agent"));
			content = new String(client.get(url), "utf-8");
			AppCache.add(content_key, "index_content", content);
		} else {
			content = "please config your domain map";
		}
	}
	response.setCharacterEncoding("utf-8");
%><%=content%>