package jdroplet.sns.proxy;

import jdroplet.bll.Shops;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpResponse;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.net.WebClient;
import jdroplet.util.TextUtils;
import org.json.JSONObject;

/**
 * Created by kuibo on 2018/7/24.
 */
public class WechatAppProxy extends WechatProxy {

    public static final String APP_ID = "wechat_app_appid";
    public static final String APP_SECRET = "wechat_app_appsecret";

    @Override
    public void setShopId(Integer shopId) {
        Shop shop = Shops.getShop(shopId);

        this.appId = shop.getString(APP_ID);
        this.appSecret = shop.getString(APP_SECRET);
    }

    @Override
    public void requestAccessToken() {
        String url = null;
        HttpResponse response = HttpContext.current().response();
        WebClient webClient = null;
        JSONObject json = null;

        url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + super.getAppId() + "&secret=" + super.getAppSecret()
                + "&code=" + super.getCode() + "&grant_type=authorization_code&js_code=" + super.getCode();
        webClient = new WebClient();
        json = webClient.getJSONObject(url);
        if (json == null)
            throw new ApplicationException("WechatApp OAuth Error");

        if (json.has("error"))
            throw new ApplicationException("error:" + json.optString("error") + " code:" + json.optInt("error_code") + " msg:" + json.optString("error_description"));

        super.userId = json.optString("openid");

        if (TextUtils.isEmpty(userId))
            throw new ApplicationException("Weibo OAuth Error");
    }

    @Override
    public User getUser() {
        User user = new User();

        user.setUserName(userId);
        user.setDisplayName(userId);
        user.setAvatar("");
        return user;
    }
}
