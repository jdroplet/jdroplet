package wechat.utils;

import java.io.IOException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import wechat.utils.HttpClientConnectionManager;

/**
 * 微信工具类
 * @author fr
 */
public class WxUtils {

	public static DefaultHttpClient httpclient;
	
	public static String APPID = WeinXinConfig.getValue("APPID");    //测试号-appid
	public static String APPSECRET = WeinXinConfig.getValue("APPSECRET");  //测试号-secret	

	static {
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); // 接受任何证书的浏览器客户端
	}
	
	//获取网络授权用户信息
	public static String getNetBaseAccessToken(String code) throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET+"&code="+code+"&grant_type=authorization_code");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("openid");		
	}

	//获取基本access_token
	public static String getBaseAccessToken() throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET);
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("access_token");		
	}
	
	//获取基本access_token
	public static String getBaseAccessToken(String appId, String appSecret) throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("access_token");		
	}

	/**
	 * 获取用户基本信息
	 * 其中nickname为昵称
	 */
	public static String getWxUserNickname(String openid, String accessToken) throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("nickname");
	}
	
	
	/**
	 * 获取用户基本信息
	 */
	public static JSONObject getWxUserInfo(String openid, String accessToken) throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object;
	}
	
	
	//获取用户基本信息,返回openid
	public static String getWxUserBaseInfo(String openid) throws Exception{
		String access_token = getBaseAccessToken();
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("openid");		
	}

	//获取用户基本信息,返回unionid
	public static String getWxUserUnionid(String openid,String access_token) throws Exception{
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object.getString("openid");		
	}

	//获取用户基本信息，返回JSONObject
	public static JSONObject getWxUserBaseInfoObject(String openid) throws Exception{
		String access_token = getBaseAccessToken();
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object;		
	}
	
	//获取授权用户信息
	public static String getWxUserInfo(String code) throws Exception {
		JSONObject object = getPageAccessToken(code);
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/sns/userinfo?access_token="+object.getString("access_token")+"&openid="+object.getString("openid")+"&lang=zh_CN");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object2 = new JSONObject(jsonStr);
		return object2.getString("openid");		
	}
	
	//获取页面授权access_token
	private static JSONObject getPageAccessToken(String code) throws Exception {
		HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID+"&secret="+APPSECRET+"&code="+code+"&grant_type=authorization_code");
		HttpResponse response = httpclient.execute(get);
		String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
		JSONObject object = new JSONObject(jsonStr);
		return object;		
	}

	/**
	 * 将输入流转为字符串
	 */
	public static final String inputStream2String(InputStream in) throws UnsupportedEncodingException, IOException{
		if(in == null)
			return "";
		
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}

}
