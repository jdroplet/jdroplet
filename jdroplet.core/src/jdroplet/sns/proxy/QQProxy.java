package jdroplet.sns.proxy;

import jdroplet.bll.Shops;
import jdroplet.core.HttpContext;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import jdroplet.net.RequestMethod;
import jdroplet.net.WebClient;
import jdroplet.net.WebRequest;
import jdroplet.net.WebResponse;
import jdroplet.security.DigestUtil;
import jdroplet.util.Encoding;
import jdroplet.util.QueryStringParser;
import jdroplet.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by kuibo on 2018/2/6.
 */
public class QQProxy extends Proxy {

    public static final String APP_ID = "qq_appid";
    public static final String APP_SECRET = "qq_appsecret";

    @Override
    public void setShopId(Integer shopId) {
        Shop shop = Shops.getShop(shopId);

        this.shopId = shopId;
        this.appId = shop.getString(APP_ID);
        this.appSecret = shop.getString(APP_SECRET);
    }

    @Override
    public void requestAccessToken() {
        String url = null;
        String redirect_uri = null;
        WebRequest req = null;
        WebResponse resp = null;
        String content = null;
        JSONObject json = null;

        redirect_uri = SystemConfig.getSiteUrls().getUrl("snsproxy.oauth_callback", "qq", shopId);
        url = "https://graph.qq.com/oauth2.0/token?client_id=" + super.getAppId()
                + "&client_secret=" + super.getAppSecret()
                + "&code=" + super.getCode()
                + "&redirect_uri=" + redirect_uri + "&grant_type=authorization_code";

        req = new WebRequest();
        req.setResponseCharset("utf-8");
        req.setContentCharset("utf-8");
        req.setMethod(RequestMethod.GET);
        req.setAutoRedirect(false);

        try {
            resp = req.create(url);
            content = new String(resp.getContent(), "utf-8");
            if (content.indexOf("callback") >= 0) {
                int lpos = content.indexOf("(");
                int rpos = content.indexOf(")");
                content = content.substring(lpos + 1, rpos).trim();
                json = new JSONObject(content);

                throw new ApplicationException("code:" + json.optInt("error") + " msg:" + json.optString("error_description"));
            }
        } catch (IOException e) {
            throw new SystemException("QQ Oauth error:" + e.getMessage());
        } catch (JSONException e) {
            throw new SystemException("QQ Oauth error:" + e.getMessage());
        }

        QueryStringParser qsp = new QueryStringParser(content);
        super.token = qsp.getParameterValue("access_token");
        if (TextUtils.isEmpty(super.token))
            throw new ApplicationException("QQ OAuth AccessToken empty!");
    }

    @Override
    public void requestAuthorizeCode(String redirect, String scope) {
        String authorize_url = null;
        String state = DigestUtil.MD5(Long.toString(System.nanoTime()));

        authorize_url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + super.getAppId()
                + "&redirect_uri=" + Encoding.urlEncode(redirect)
                + "&scope=" + scope
                + "&state=" + state;

        HttpContext.current().response().setRedirect(authorize_url);
    }

    @Override
    public User getUser() {
        String url = null;
        String content = null;
        WebClient client = null;
        JSONObject json = null;
        User user = null;

        url = "https://graph.qq.com/oauth2.0/me?access_token=" + token;
        client = new WebClient();
        content = client.getString(url);

        if (content.indexOf("callback") >= 0) {
            int lpos = content.indexOf("(");
            int rpos = content.indexOf(")");
            content = content.substring(lpos + 1, rpos).trim();

            try {
                json = new JSONObject(content);
            } catch (JSONException e) {
                throw new SystemException("QQ Oauth error:" + e.getMessage());
            }
        }

        if (json.has("error")) {  //异常情况
            throw new ApplicationException("code:" + json.optInt("error") + " msg:" + json.optString("error_description"));
        } else {
            String openid = json.optString("openid");

            if (TextUtils.isEmpty(openid))
                throw new ApplicationException("QQ openid empty!");

            url = "https://graph.qq.com/user/get_user_info?access_token=" + token+ "&oauth_consumer_key=" + appId + "&openid=" + openid;
            json = client.getJSONObject(url);

            if (json == null)   //异常情况
                throw new ApplicationException("QQ Oauth getCurrentUser error");

            if (json.has("error"))   //异常情况
                throw new ApplicationException("code:" + json.optInt("error") + " msg:" + json.optString("error_description"));

            user = new User();
            user.setUserName(openid);
            user.setDisplayName(json.optString("nickname"));
            if (TextUtils.isEmpty(json.optString("figureurl_qq_2"))) {
                user.setAvatar(json.optString("figureurl_qq_1"));
            } else {
                user.setAvatar(json.optString("figureurl_qq_2"));
            }
            if ("男".equals(json.optString("gender")))
                user.setGender(User.MALE);

            if ("女".equals(json.optString("gender")))
                user.setGender(User.FEMALE);

            return user;
        }
    }
}
