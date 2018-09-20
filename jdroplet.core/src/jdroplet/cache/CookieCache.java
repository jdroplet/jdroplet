package jdroplet.cache;

import javax.servlet.http.Cookie;

import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.util.TextUtils;


public class CookieCache {

	public static void add(String key, int value) {
		add(key, Integer.toString(value));
	}
	
	public static void add(String key, String value) {
		add(key, value, 0);
	}
		
	public static void add(String key, String value, int sections) {
		Cookie cookie = null;
		HttpRequest request = HttpContext.current().request();
		HttpResponse response = HttpContext.current().response();
		
		cookie = new Cookie(key, value);
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		
		response.addCookie(cookie);
	}
	
	public static String get(String key) {
		Cookie cookie = null;
		String value = null;
		HttpRequest request = HttpContext.current().request();
		
		cookie = request.getCookie(key);
		if (cookie != null)
			value = cookie.getValue();
		
		return value;
	}
	
	public static int getInt(String key) {
		return getInt(key, -1);
	}
	
	public static int getInt(String key, int def) {
		String value = null;
		
		value = get(key);
		if (!TextUtils.isEmpty(value))
			return Integer.parseInt(value);
		
		return def;
	}
}
