package jdroplet.net;

import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
 
/**
 * 响应对象
 */
public class WebResponse {
 
	String urlString;
	String file;
	String host;
	String path;
	String protocol;
	String query;
	String ref;
	String userInfo;
	String contentEncoding;
	String contentType;
	String charset;
	String message;
	String method;
	
	int port;
	int defaultPort;
	int code;
	int connectTimeout;
	int readTimeout;
	byte[] content;
	
	Vector<String> contentCollection;

	URLConnection m_conn;
	
	public byte[] getContent() {
		return content;
	}
 
	public String getContentType() {
		return contentType;
	}
 
	public int getCode() {
		return code;
	}
 
	public String getMessage() {
		return message;
	}
 
	public Vector<String> getContentCollection() {
		return contentCollection;
	}
	 
	public String getContentEncoding() {
		return contentEncoding;
	}
	
	public String getCharset() {
		return charset;
	}
 
	public String getMethod() {
		return method;
	}
 
	public int getConnectTimeout() {
		return connectTimeout;
	}
 
	public int getReadTimeout() {
		return readTimeout;
	}
 
	public String getUrlString() {
		return urlString;
	}
 
	public int getDefaultPort() {
		return defaultPort;
	}
 
	public String getFile() {
		return file;
	}
 
	public String getHost() {
		return host;
	}
 
	public String getPath() {
		return path;
	}
 
	public int getPort() {
		return port;
	}
 
	public String getProtocol() {
		return protocol;
	}
 
	public String getQuery() {
		return query;
	}
 
	public String getRef() {
		return ref;
	}
 
	public String getUserInfo() {
		return userInfo;
	}
	
	public Map<String, List<String>> getHeaderFields() {
		return m_conn == null ? null : m_conn.getHeaderFields();
	}
	
	public List<String> getHeaderFields(String name) {
		Map<String, List<String>> map = null;
		
		map = getHeaderFields();
		return map == null ? null : map.get(name);
	}
	
	public String getHeaderField(String name) {
		return m_conn == null ? null : m_conn.getHeaderField(name);
	}
 
}