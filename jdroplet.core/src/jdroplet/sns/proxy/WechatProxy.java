package jdroplet.sns.proxy;

import jdroplet.bll.Shops;
import jdroplet.cache.RedisCache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.exceptions.SystemException;
import jdroplet.net.WebClient;
import jdroplet.security.DigestUtil;
import jdroplet.util.Encoding;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.Cookie;


public class WechatProxy extends Proxy {

	public static final String APP_ID = "wechat_appid";
	public static final String APP_SECRET = "wechat_appsecret";

	private static final String FLAG_KEY = "random_flag2";
	private void setFlag() {
		HttpRequest request = HttpContext.current().request();
		HttpResponse response = HttpContext.current().response();

		Cookie cookie = new Cookie(FLAG_KEY, Long.toString(System.nanoTime()));
		cookie.setPath("/");
		cookie.setDomain(request.getSimpleServerName());
		cookie.setMaxAge(10);
		response.addCookie(cookie);
	}

	@Override
	public void setShopId(Integer shopId) {
		Shop shop = Shops.getShop(shopId);

		this.appId = shop.getString(APP_ID);
		this.appSecret = shop.getString(APP_SECRET);
	}

	@Override
	public void requestAuthorizeCode(String redirect, String scope) {
		String authorize_url = null;
		String state = DigestUtil.MD5(Long.toString(System.nanoTime()));
		HttpResponse response = HttpContext.current().response();
		HttpRequest request = HttpContext.current().request();

		authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ super.getAppId()
				+ "&redirect_uri="
				+ Encoding.urlEncode(redirect)
				+ "&response_type=code&scope="
				+ scope
				+ "&state="
				+ state + "#sns_redirect";

		if (TextUtils.isEmpty(request.getCookieValue(FLAG_KEY)))
			setFlag();

		response.set301Redirect(authorize_url);
	}

	@Override
	public void requestAccessToken() {
		String url = null;
		String content = null;
		String flag = null;
		String cacheKey = null;
		JSONObject json = null;
		HttpRequest request = HttpContext.current().request();

		flag = request.getCookieValue(FLAG_KEY);
		cacheKey = "wechat-token@" + flag;
		content = RedisCache.getString(cacheKey);
		if (content == null) {
            WebClient webClient = null;
            boolean result = false;

			webClient = new WebClient();
			url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
					+ super.getAppId() + "&secret=" + super.getAppSecret()
					+ "&code=" + super.getCode() + "&grant_type=authorization_code";
			content = webClient.getString(url);

            Logger logger = Logger.getLogger(this.getClass());
            logger.error("WechatProxy@getToken-" + cacheKey);

            result = RedisCache.setX(cacheKey, content);
            RedisCache.expire(cacheKey, 10);
            if (result == false)
                content = RedisCache.getString(cacheKey);
		}
		try {
			json = new JSONObject(content);
		} catch (JSONException e) {
			throw new SystemException(e.getMessage());
		}

		if (json.has("errcode")) {  //异常情况
			RedisCache.remove(cacheKey);
			throw new AuthorizationException("code:" + json.optInt("errcode") + " msg:" + json.optString("errmsg") + " url:" + url);
		} else {
			super.token = json.optString("access_token");
			super.userId = json.optString("openid");
		}
	}

	@Override
	public User getUser() {
		String url = null;
		WebClient client = null;
		JSONObject json = null;
		User user = null;

		url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + userId;
		client = new WebClient();
		json = client.getJSONObject(url);

		if (json.has("errcode")) {  //异常情况
			if (json.optInt("errcode") == 48001) { // 静默登录会返回这个
				user = new User();
				user.setUserName(userId);
				user.setAvatar("");
				user.setDisplayName("游客");

				return user;
			} else {
				throw new AuthorizationException("code:" + json.optInt("errcode") + " msg:" + json.optString("errmsg") + " url:" + url);
			}
		} else {
			user = new User();

			user.setUserName(userId);
			user.setAvatar(json.optString("headimgurl"));
			user.setDisplayName(json.optString("nickname"));

			return user;
		}
	}
}
