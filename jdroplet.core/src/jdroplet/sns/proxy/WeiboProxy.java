package jdroplet.sns.proxy;

import jdroplet.bll.Shops;
import jdroplet.core.HttpContext;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.net.RequestMethod;
import jdroplet.net.WebClient;
import jdroplet.net.WebRequest;
import jdroplet.net.WebResponse;
import jdroplet.security.DigestUtil;
import jdroplet.util.Encoding;
import jdroplet.util.TextUtils;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/6.
 */
public class WeiboProxy extends Proxy {

    public static final String APP_ID = "weibo_appid";
    public static final String APP_SECRET = "weibo_appsecret";

    @Override
    public void setShopId(Integer shopId) {
        Shop shop = Shops.getShop(shopId);

        this.shopId = shopId;
        this.appId = shop.getString(APP_ID);
        this.appSecret = shop.getString(APP_SECRET);
    }

    @Override
    public void requestAccessToken() {
        WebClient webClient = null;
        String url = "https://api.weibo.com/oauth2/access_token";
        String redirect_uri = null;
        Map<String, String> params = null;
        JSONObject json = null;

        redirect_uri = SystemConfig.getSiteUrls().getUrl("snsproxy.oauth_callback", "weibo", shopId);
        params = new HashMap<>();
        params.put("client_id", appId);
        params.put("client_secret", appSecret);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", redirect_uri);

        webClient = new WebClient();
        json = webClient.post3(url, params);
        if (json == null)
            throw new ApplicationException("Weibo OAuth Error");

        if (json.has("error"))
            throw new ApplicationException("error:" + json.optString("error") + " code:" + json.optInt("error_code") + " msg:" + json.optString("error_description"));

        super.token = json.optString("access_token");
        super.userId = json.optString("uid");

        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(userId))
            throw new ApplicationException("Weibo OAuth Error");
    }

    @Override
    public void requestAuthorizeCode(String redirect, String scope) {
        String authorize_url = null;

        authorize_url = "https://api.weibo.com/oauth2/authorize?client_id=" + super.getAppId()
                + "&redirect_uri=" + Encoding.urlEncode(redirect)
                + "&response_type=code";

        HttpContext.current().response().setRedirect(authorize_url);
    }

    @Override
    public User getUser() {
        WebClient webClient = null;
        String url = null;
        JSONObject json = null;
        User user = null;

        url = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + userId;
        webClient = new WebClient();
        json = webClient.getJSONObject(url);

        if (json == null)
            throw new ApplicationException("Weibo Oauth getCurrentUser Error");

        if (json.has("error"))
            throw new ApplicationException("Weibo getCurrentUser Error:" + json.optString("error") + " code:" + json.optInt("error_code") + " msg:" + json.optString("error_description"));

        user = new User();
        user.setUserName(json.optString("id"));
        user.setDisplayName(json.optString("name"));
        user.setAvatar(json.optString("profile_image_url"));

        return user;
    }
}
