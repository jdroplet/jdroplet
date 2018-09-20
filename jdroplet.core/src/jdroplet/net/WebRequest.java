package jdroplet.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import jdroplet.util.Encoding;
import jdroplet.util.FileUtil;

import org.apache.commons.lang.StringUtils;


/**
 * HTTP请求对象
 */
public class WebRequest {
	public static final byte[] FIELD_SEPARATOR = { 0x0d, 0x0a };
	public static final byte[] STREAM_TERMINATOR = { 0x2d, 0x2d };
	public static String BOUNDARY = "";

	private String responseCharset;
	private String contentCharset;
	private RequestMethod method;
	private int timeout = 30000;
	private boolean autoRedirect = false;
	private Proxy proxy;
	
	static {
		for (int idx = 0; idx < 0x20; idx++) {
			BOUNDARY += (char) (97 + Math.random() * 25);
		}
	}
	
	public WebRequest() {
		this.responseCharset = Charset.defaultCharset().name();
		this.contentCharset = Charset.defaultCharset().name();
		this.method = RequestMethod.GET;
	}

	public void setAutoRedirect(boolean r) {
		autoRedirect = r;
	}
	public void setMethod(RequestMethod method) {
		this.method = method;
	}
	public RequestMethod getMethod() {
		return this.method;
	}
	
	public String getResponseCharset() {
		return this.responseCharset;
	}
	
	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
 
	public void setProxy(String hostname, int port) {
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port)); 
	}
	/**
	 * 设置默认的响应字符集
	 */
	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}
	
	/**
	 * 发送请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public WebResponse create(String url) throws IOException {
		return this.create(url, this.method, null, null);
	}
 
	/**
	 * 发送请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public WebResponse create(String url, Map<String, String> params) throws IOException {
		return this.create(url, this.method, params, null);
	}

	/**
	 * 发送请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * @return 响应对象
	 * @throws IOException
	 */
	public WebResponse create(String url, Map<String, String> params, Map<String, String> propertys) throws IOException {
		return this.create(url, this.method, params, propertys);
	}
	
	public WebResponse create(String url, Map<String, String> parameters, Map<String, String> propertys, File file) throws IOException {
		byte[] datas = null;
		
		datas = FileUtil.getFileData(file);
		return create(url, parameters, propertys, file.getName(), datas);
	}
	
	public WebResponse create(String url, Map<String, String> parameters, Map<String, String> propertys, String filename, byte[] datas) throws IOException {
		HttpURLConnection conn = null;
		URL uri = null;
		OutputStream os = null;
		
		uri = new URL(url);
		if (proxy != null)
			conn = (HttpURLConnection) uri.openConnection(proxy);
		else
			conn = (HttpURLConnection) uri.openConnection();
		conn.setRequestMethod(this.method.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(this.timeout);
		conn.setReadTimeout(this.timeout);
        
		if (propertys != null) {
			for (String key : propertys.keySet()) {
				conn.addRequestProperty(key, propertys.get(key));
			}
		}
		conn.addRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);
		
		os = conn.getOutputStream();

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				os.write(STREAM_TERMINATOR);
				os.write(BOUNDARY.getBytes(this.contentCharset));
				os.write(FIELD_SEPARATOR);
                os.write(("Content-Disposition: form-data; name=\"" + key + "\"").getBytes(this.contentCharset));
                os.write(FIELD_SEPARATOR);
                os.write(FIELD_SEPARATOR);
                os.write(parameters.get(key).getBytes(this.contentCharset));
                os.write(FIELD_SEPARATOR);
			}
			
			os.write(STREAM_TERMINATOR);
			os.write(BOUNDARY.getBytes(this.contentCharset));
			os.write(FIELD_SEPARATOR);
		}
		
		// 写入上传内容	
        os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"").getBytes(this.contentCharset));
        os.write(FIELD_SEPARATOR);// \r\n
        os.write("Content-Type: application/octet-stream".getBytes(this.contentCharset));
        os.write(FIELD_SEPARATOR);// \r\n
        os.write(FIELD_SEPARATOR);// \r\n
        
        
        os.write(datas);
        os.write(FIELD_SEPARATOR);// \r\n
        os.write(STREAM_TERMINATOR);
        os.write(BOUNDARY.getBytes(this.contentCharset));
                  
        // 写入文件结尾的分割线
        os.write(STREAM_TERMINATOR);// --
        os.write(FIELD_SEPARATOR);// \r\n

		return this.createResponse(url, conn);
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param url_str
	 * @return 响映对象
	 * @throws IOException
	 */
	private WebResponse create(String url_str, RequestMethod method, Map<String, String> parameters, Map<String, String> propertys)
			throws IOException {
		HttpURLConnection conn = null;
		URL url = null;
		
		if (RequestMethod.GET == method && parameters != null) {
			StringBuffer param = new StringBuffer(parameters.size() * 8);
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(Encoding.urlEncode(parameters.get(key), this.contentCharset));
				i++;
			}
			url_str += param;
		}		
		url = new URL(url_str);

		if ("https".equalsIgnoreCase(url.getProtocol())) {
			try{SSLUtils.ignoreSsl();}catch(Exception ex){}
        }
		
		if (proxy != null)
			conn = (HttpURLConnection) url.openConnection(proxy);
		else
			conn = (HttpURLConnection) url.openConnection();
 
		conn.setInstanceFollowRedirects(this.autoRedirect);
		conn.setRequestMethod(method.toString());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(this.timeout);
		conn.setReadTimeout(this.timeout);
		
		if (propertys != null) {
			for (String key : propertys.keySet()) {
				conn.addRequestProperty(key, propertys.get(key));
			}
		}

		conn.connect();
		if (RequestMethod.POST == method && parameters != null) {
			StringBuffer param = new StringBuffer(parameters.size() * 8);
			int idx = 0;
			for (String key : parameters.keySet()) {
				if (key.length() == 0) {
					param.append(parameters.get(key));
				} else {
					if (idx != 0) {
						param.append("&");
					}
					param.append(key).append("=").append(parameters.get(key));
				}
				idx ++;
			}
			conn.getOutputStream().write(param.toString().getBytes(this.contentCharset));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
		}
 
		return this.createResponse(url_str, conn);
	}
 
	/**
	 * 得到响应对象
	 * 
	 * @param conn
	 * @return 响应对象
	 * @throws IOException
	 */
	private WebResponse createResponse(String url_str, HttpURLConnection conn) throws IOException {
		WebResponse resp = null;
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		
		resp = new WebResponse();
		resp.m_conn = conn;
		resp.code = conn.getResponseCode(); 
		resp.host = conn.getURL().getHost();
		resp.protocol = conn.getURL().getProtocol();
		
		// 处理302
		if (resp.code == HttpURLConnection.HTTP_MOVED_TEMP) {
			String location = resp.getHeaderField("Location");
			String server_cookie = null;
			List<String> cookies = resp.getHeaderFields("Set-Cookie");
			HashMap<String, String> propertys = null;
			WebRequest _req = null;
			
			conn.disconnect();
			
			if (location.indexOf("http") < 0)
				url_str = resp.protocol + "://"+ resp.host + location;
			else
				url_str = location;
			
			server_cookie = StringUtils.join(cookies, "; ");
			propertys = new HashMap<String, String>();
			propertys.put("Cookie", server_cookie);
			
			_req = new WebRequest();
			_req.setResponseCharset("UTF-8");
			return _req.create(url_str, null, propertys);
		}
		
		
		resp.urlString = url_str;
		resp.defaultPort = conn.getURL().getDefaultPort();
		resp.file = conn.getURL().getFile();
		resp.path = conn.getURL().getPath();
		resp.port = conn.getURL().getPort();
		resp.query = conn.getURL().getQuery();
		resp.ref = conn.getURL().getRef();
		resp.userInfo = conn.getURL().getUserInfo();
		resp.message = conn.getResponseMessage();
		resp.method = conn.getRequestMethod();
		resp.contentType = conn.getContentType();
		resp.contentEncoding = conn.getContentEncoding();
		resp.connectTimeout = conn.getConnectTimeout();
		resp.readTimeout = conn.getReadTimeout();
		
		if ("".equals(resp.contentType) == false) {
			int char_pos = resp.contentType.toLowerCase().indexOf("charset=");
			if (char_pos > 0) {
				int end_pos = resp.contentType.length();
				resp.charset = resp.contentType.substring(char_pos + 8,
						end_pos);
			} 
		}
		
		if ("gzip".equals(resp.contentEncoding))
			in = new GZIPInputStream(conn.getInputStream());
		else
			in = conn.getInputStream();
		
		baos = new ByteArrayOutputStream();
		bytes = new byte[4096];
		int len = 0;
		
		try {
			while ((len = in.read(bytes)) != -1) {
				baos.write(bytes, 0, len);
			}
			resp.content = baos.toByteArray();
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (baos != null)
				baos.close();
			if (in != null)
				in.close();
			if (conn != null)
				conn.disconnect();
		}
		return resp;
	}
}