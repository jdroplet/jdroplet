package jdroplet.app.view;

import jdroplet.bll.Logs;
import jdroplet.bll.Shops;
import jdroplet.bll.Users;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.security.DigestUtil;
import jdroplet.security.FormsAuthentication;
import jdroplet.sns.proxy.Proxy;
import jdroplet.sns.proxy.ProxyFactory;
import jdroplet.sns.proxy.WechatProxy;
import jdroplet.sns.utils.wechat.WeChatHelp;
import jdroplet.sns.wechat.MessageHandler;
import jdroplet.util.Encoding;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SnsProxyPage extends MasterPage {

	private static Logger logger = Logger.getLogger(SnsProxyPage.class);
	
	@Override
	public void show() {
	}
	
	public void oauth() {
		Integer shopId = request.getIntParameter("shopId");
		String scope = request.getParameter("scope", "snsapi_userinfo");
		String name = request.getParameter("name");
		Boolean forwardCode = request.getBooleanParameter("forwardCode");
		String redirect = request.getParameter("redirect", "");
		Proxy proxy = null;
		Cookie cookie = null;
		SystemConfig config = null;

		setReturnPath(redirect);
		proxy = ProxyFactory.getProxy(name);
		redirect =  SystemConfig.getSiteUrls().getUrl("snsproxy.oauth_callback", name, shopId);
		if (forwardCode == true)
			redirect += "?forwardCode=true";

		proxy.setShopId(shopId);
		proxy.requestAuthorizeCode(redirect, scope);
	}
	
	public void oauth_callback() {		
		String name = null;
		String code = null;
		String redirect = null;
		Proxy proxy = null;
		Integer shopId = null;
		String sysToken = null;
		User snsUser = null;
		User sysUser = null;

		shopId = request.getIntParameter("shopId");
		name = request.getParameter("name");
		code = request.getParameter("code");
		if (TextUtils.isEmpty(code))
			throw new ApplicationException(name + " Oauth login error, empty Code!");

		redirect = getReturnPath();
		if (request.getBooleanParameter("forwardCode")) {
			if (redirect.indexOf("?") >= 0)
				redirect += "&code=" + code;
			else
				redirect += "?code=" + code;

			response.setRedirect(redirect);
			return;
		}
		proxy = ProxyFactory.getProxy(name);
		
		proxy.setShopId(shopId);
		proxy.setCode(code);
		
		proxy.requestAccessToken();
		snsUser = proxy.getUser();
		sysUser = Users.getCurrentUser();

		if (TextUtils.isEmpty(snsUser.getUserName()))
			throw new ApplicationException(name + " Oauth login error, empty user name!");

		String userName = null;
		// 是否已经登录
		if (Users.isLoggedIn(sysUser)) {
			if (TextUtils.isEmpty(sysUser.getAvatar())) {
				if (!sysUser.getBool("updated_display_name"))
					Users.updateDisplayName(sysUser.getId(), snsUser.getDisplayName());
				Users.updateAvatar(sysUser.getId(), snsUser.getAvatar());
				Users.updateLastVisit(sysUser.getId());
			}
		} else {
			userName = name + "_" + snsUser.getUserName();
			// 查找系统是否有该用户
			sysUser = Users.getUser(userName);
			if (sysUser == null) {
				Users.createUser(userName, snsUser.getDisplayName(), Long.toString(System.nanoTime()),
						"", snsUser.getAvatar(), 0, new Integer[] {Integer.valueOf(2)});
			} else {
				if (!sysUser.getBool("updated_display_name"))
					Users.updateDisplayName(sysUser.getId(), snsUser.getDisplayName());

				Users.updateAvatar(sysUser.getId(), snsUser.getAvatar());
				Users.updateLastVisit(sysUser.getId());

				if (sysUser.isAdministrator())
					Logs.save("user-login-" + sysUser.getId(), "");
			}
			sysToken = FormsAuthentication.setAuthCookie(userName, true);
		}
		String type = request.getParameter("type");

		if (TextUtils.isEmpty(type)) {
			if (TextUtils.isEmpty(redirect)) {
				redirect = SystemConfig.getSiteUrls().getUrl("user.my");
			}
			response.setRedirect(redirect);
		} else {
			Map<String, Object> map = new HashMap();
			map.put("token", sysToken);
			map.put("openId", snsUser.getUserName());
			map.put("userId", sysUser.getId());
			renderJSON(0, null, map);
		}
	}

	public void wxshare() {
		Integer shopId = request.getIntParameter("shopId");
		String link = request.getParameter("link");
		Map<String, Object> config = null;
		Shop shop = Shops.getShop(shopId);
		String appId = shop.getString(WechatProxy.APP_ID);
		String appSecret = shop.getString(WechatProxy.APP_SECRET);

		link = Encoding.urlDecode(link, "utf-8");
		config = WeChatHelp.getShareConfig(appId, appSecret, link);

		renderJSON(0, "", config);
	}

	public void wechatsubscribe() throws Exception {
		isCustomContent = true;
		if (request.isPostBack()) {
			MessageHandler handler = new MessageHandler();

			//logger.info("@wechatsubscribe-req:" + request.getRawPostData());
			String content = handler.invoke(request.getRawPostData());

			//logger.info("@wechatsubscribe-resp:" + content);
			response.getWriter().write(content);
		} else {
			check();
		}
	}

	public static final String APP_TOKEN = "wechat_sub_token";

	private void check() {
		Integer shopId = request.getIntParameter("shopId");
		String echostr = request.getParameter("echostr");
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String str = null;
		String token = null;
		String[] arr = null;
		Shop shop = null;

		shop = Shops.getShop(shopId);
		token = shop.getString(APP_TOKEN);
		arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);
		str = StringUtils.join(arr, "");
		str = DigestUtil.SHA1(str);

		logger.info("echostr:" + echostr + " signature:" + signature + " nonce:" + nonce + " timestamp:" + timestamp + " str:" + str);
		try {
			if (str.equals(signature)) {
				response.getWriter().print(echostr);
			} else {
				response.getWriter().print("error");
			}
		} catch (IOException e) {
		}
	}

//	public void wechat() {
//		int id = 0;
//		String type = request.getParameter("type");
//		OAuthConsumer oac = null;
//		User user = null;
//		JSONObject jobj = null;
//
//		user = Users.getCurrentUser();
//		id = request.getIntParameter("id");
//		oac = OAuthConsumers.getOAuthConsumer(id);
//		jobj = WeChatHelp.getSubscriber(oac.getAppId(), oac.getAppSecret(), user.getUserName().replace("wechat_", ""));
//
//		if ("info".equals(type)) {
//			renderJSON(0, "", jobj);
//		} else if ("is_subscribe".equals(type)) {
//			renderJSON(0, "", jobj.optInt("subscribe"));
//		}
//	}
}
