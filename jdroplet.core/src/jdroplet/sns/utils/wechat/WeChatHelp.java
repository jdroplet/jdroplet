package jdroplet.sns.utils.wechat;

import com.github.wxpay.sdk.WXPayUtil;
import jdroplet.cache.RemoteCache;
import jdroplet.data.model.User;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.exceptions.SystemException;
import jdroplet.net.WebClient;
import jdroplet.security.DigestUtil;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2017/9/18.
 */
public class WeChatHelp {

    public static User getUser(String token, String openId) {
        String url = null;
        WebClient client = null;
        JSONObject json = null;
        User user = null;

        url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId;
        client = new WebClient();
        json = client.getJSONObject(url);

        if (json.has("errcode")) {  //异常情况
            throw new SystemException("code:" + json.optInt("errcode") + " msg:" + json.optString("errmsg"));
        } else {
            user = new User();

            user.setUserName("wechat_" + openId);
            user.setAvatar(json.optString("headimgurl"));
            user.setDisplayName(json.optString("nickname"));
            user.setGender(json.optInt("sex"));
            user.setValue("subscribe", json.optString("subscribe"));
            return user;
        }
    }

    public static String getToken(String appId, String secret) {
        String url = null;
        WebClient client = null;
        JSONObject json = null;
        String cacheKey = "wechat_base_token_" + appId;
        String token = null;

        token = (String) RemoteCache.get(cacheKey);
        if (TextUtils.isEmpty(token)) {
            url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
            client = new WebClient();
            json = client.getJSONObject(url);
            token = json.optString("access_token");

            if (!TextUtils.isEmpty(token))
                RemoteCache.add(cacheKey, token, 7000);
        }

        return token;
    }

    public static Map<String, Object> getShareConfig(String appId, String secret, String link) {
        String signature = null;
        String ticket = null;
        String nonceStr = null;
        String rawString = null;
        String cacheKey = null;
        String timestamp = null;
        Map<String, Object> config = null;

        cacheKey = "wechat_config_" + appId;
        ticket = (String) RemoteCache.get(cacheKey);
        if (TextUtils.isEmpty(ticket)) {
            WebClient client = null;
            JSONObject json = null;
            String token  = getToken(appId, secret);

            client = new WebClient();
            json = client.getJSONObject("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + token);
            if (json.optInt("errcode") != 0) {
                String tokenKey = "wechat_base_token_" + appId;
                RemoteCache.remove(tokenKey);
            }

            ticket = json.optString("ticket");

            RemoteCache.add(cacheKey, ticket, 7000);
        }

        nonceStr = WXPayUtil.generateUUID();
        timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        rawString = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + link;
        signature = DigestUtil.SHA1(rawString);

        config = new HashMap<>();
        config.put("debug", false);
        config.put("appId", appId);
        config.put("nonceStr", nonceStr);
        config.put("timestamp", timestamp);
        config.put("url", link);
        config.put("signature", signature);
        config.put("rawString", rawString);
        config.put("jsApiList", new String[]{"onMenuShareTimeline", "onMenuShareAppMessage"});

        return config;
    }

    /**
     * 获取订阅用户信息
     * @param appId
     * @param secret
     * @param openId
     * @return
     */
    public static JSONObject getSubscriber(String appId, String secret, String openId) {
        String token = null;
        String url = null;
        WebClient client = null;
        JSONObject json = null;

        token = getToken(appId, secret);
        url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId;
        client = new WebClient();
        json = client.getJSONObject(url);

        return json;
    }


    public static final String QR_STR_SCENE = "QR_STR_SCENE";
    public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";

    /**
     *
     * @param appId 微信的AppId
     * @param secret 微信的secret
     * @param type 二维码类型，QR_STR_SCENE， QR_LIMIT_STR_SCENE
     * @param content 微信二维码场景内容，长度不超过64
     * @return
     */
    public static byte[] getQRCode(String appId, String secret, String type, String content) {
        WebClient client = null;
        JSONObject json = null;
        String token  = getToken(appId, secret);
        String ticket = null;
        byte[] bytes = null;
        Map<String, Object> params = null;

        params = new HashMap<>();
        params.put("action_name", type);
        params.put("action_info", new HashMap() {
            {
                put("scene", new HashMap() {{
                    put("expire_seconds", 2592000);
                }});
            }
        });

        if (type.equals(QR_STR_SCENE)) {
            params.put("QR_SCENE", "");
        }

        Map<String, String> postParms = new HashMap<>();
        postParms.put("", JSONUtil.toJSONString(params));

        client = new WebClient();
        json = client.post3("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token, postParms);
        ticket = json.optString("ticket");

        bytes = client.get("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket);
        return bytes;
    }
}
