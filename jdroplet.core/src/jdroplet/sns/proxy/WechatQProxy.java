package jdroplet.sns.proxy;

import jdroplet.bll.Shops;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpResponse;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.security.DigestUtil;
import jdroplet.util.Encoding;

/**
 * Created by kuibo on 2018/5/21.
 */
public class WechatQProxy extends WechatProxy {

    public static final String APP_ID = "wechat_q_appid";
    public static final String APP_SECRET = "wechat_q_appsecret";


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

        authorize_url = "https://open.weixin.qq.com/connect/qrconnect?appid="
                + super.getAppId()
                + "&redirect_uri="
                + Encoding.urlEncode(redirect)
                + "&response_type=code&scope=snsapi_login&state="
                + state + "#sns_redirect";

        response.set301Redirect(authorize_url);
    }
}
