package jdroplet.app.view;

import jdroplet.bll.Orders;
import jdroplet.bll.Shops;
import jdroplet.bll.Users;
import jdroplet.core.HttpContext;
import jdroplet.core.Page;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Order;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.pay.AliPay;
import jdroplet.pay.DemoPay;
import jdroplet.pay.Pay;
import jdroplet.pay.wechat.WechatAppPay;
import jdroplet.pay.wechat.WechatPay;
import jdroplet.pay.wechat.WechatQRCodePay;
import jdroplet.util.Encoding;
import jdroplet.util.TextUtils;
import jdroplet.util.Transforms;
import org.apache.log4j.Logger;

import javax.lang.model.type.NullType;
import java.math.BigInteger;
import java.util.Date;

public class PayPage extends Page {

    static Logger logger = Logger.getLogger(PayPage.class);

    @Override
    public void initial() {
        addFilter("PayPage@getWechatPay", PayPage.class, "getWechatPay");
        addFilter("PayPage@getFeedbackWechatPay", PayPage.class, "getFeedbackWechatPay");

        addFilter("PayPage@getWechatAppPay", PayPage.class, "getWechatAppPay");
        addFilter("PayPage@getFeedbackWechatAppPay", PayPage.class, "getFeedbackWechatAppPay");

        addFilter("PayPage@getWechatQRCodePay", PayPage.class, "getWechatQRCodePay");
        addFilter("PayPage@getFeedbackWechatQRCodePay", PayPage.class, "getFeedbackWechatQRCodePay");

        addFilter("PayPage@getAliPay", PayPage.class, "getAliPay");
        addFilter("PayPage@getFeedbackAliPay", PayPage.class, "getAliPay");

        addFilter("PayPage@getDemoPay", PayPage.class, "getDemoPay");

        Integer shopId  = request.getIntParameter("shopId");
        addItem("shopId", shopId);

        User user = Users.getCurrentUser();
        addItem("isLogin", Users.isLoggedIn(user));
    }

    public static WechatPay getWechatPay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;
        String feedback = null;
        SiteUrls urls = null;

        urls = SystemConfig.getSiteUrls();
        feedback = urls.getUrl("pay.feedback", "wechat", shopId);

        pay = new WechatPay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        pay.setNotify(feedback);
        pay.setClientIp(HttpContext.current().request().getXRemoteAddr());
        //pay.setClientIp("59.50.94.162");

        return pay;
    }

    public static WechatPay getWechatAppPay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;
        String feedback = null;
        SiteUrls urls = null;

        urls = SystemConfig.getSiteUrls();
        feedback = urls.getUrl("pay.feedback", "wechat", shopId);

        pay = new WechatAppPay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        pay.setNotify(feedback);
        pay.setClientIp(HttpContext.current().request().getXRemoteAddr());
        //pay.setClientIp("59.50.94.162");

        return pay;
    }

    public static WechatPay getWechatQRCodePay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;
        String feedback = null;
        SiteUrls urls = null;

        urls = SystemConfig.getSiteUrls();
        feedback = urls.getUrl("pay.feedback", "wechat", shopId);

        pay = new WechatQRCodePay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        pay.setNotify(feedback);
        pay.setClientIp(HttpContext.current().request().getXRemoteAddr());
        //pay.setClientIp("59.50.94.162");

        return pay;
    }

    public static AliPay getAliPay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        AliPay pay = null;

        pay = new AliPay();
        pay.setAppid(shop.getString(AliPay.ALI_PAY_ID));
        pay.setAliPubKey(shop.getString(AliPay.ALI_PUBKEY));
        pay.setRsaPrivateKey(shop.getString(AliPay.ALI_RSA_PRI_KEY));
        pay.setReturnUrl(SystemConfig.getSiteUrls().getUrl("pay.callback", "ali", shopId));
        pay.setNotifyUrl(SystemConfig.getSiteUrls().getUrl("pay.feedback", "ali", shopId));
        return pay;
    }

    public static WechatPay getFeedbackWechatPay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;

        pay = new WechatPay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        return pay;
    }

    public static WechatPay getFeedbackWechatAppPay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;

        pay = new WechatAppPay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        return pay;
    }

    public static WechatPay getFeedbackWechatQRCodePay(NullType nt, Integer shopId) {
        Shop shop = Shops.getShop(shopId);
        WechatPay pay = null;

        pay = new WechatQRCodePay();
        pay.setAppid(shop.getString(WechatPay.APP_ID));
        pay.setKey(shop.getString(WechatPay.APP_KEY));
        pay.setMchid(shop.getString(WechatPay.APP_MCHID));
        pay.setSecret(shop.getString(WechatPay.APP_SECRET));
        return pay;
    }

    public static DemoPay getDemoPay(NullType nt, Integer shopId) {
        DemoPay pay = null;

        pay = new DemoPay();
        return pay;
    }

    public void create() {
        Integer shopId = request.getIntParameter("shopId");
        BigInteger orderId = request.getBigIntegerParameter("orderId");
        Integer payAmount = 0;
        String payModule = request.getParameter("module");
        String redirect = request.getParameter("redirect");
        Pay pay = null;
        String content = null;
        Order order = Orders.getOrder(orderId);

        payAmount = order.getPayAmount();
        pay = (Pay) applyFilters("PayPage@get" + Transforms.toUpperCasePosition(payModule, 0) + "Pay", null, shopId);
        content = pay.doPay(shopId, orderId, payAmount, redirect);
        if (!TextUtils.isEmpty(content))
            render(content);
    }

    public void callback() {
        Integer shopId = request.getIntParameter("shopId");
        Pay pay = null;
        String payModule = null;
        String redirect = null;

        redirect = request.getParameter("redirect");
        payModule = request.getParameter("module");
        pay = (Pay) applyFilters("PayPage@getFeedback" + Transforms.toUpperCasePosition(payModule, 0) + "Pay", null, shopId);
        pay.callback(shopId, redirect, new Pay.PaySuccessHandle(){

            @Override
            public void onSuccess(String payModule, Integer shopId, BigInteger orderId, Integer totalFee, String tradeNo, Date payTime) {
                doAction("PaySuccessHandle@onSuccess", payModule, shopId, orderId, totalFee, tradeNo, payTime);
                Orders.updatePayTime(orderId, payTime, tradeNo, payModule);
                logger.info("PaySuccessHandle@onSucess:module:" + payModule + " shopId:" + shopId + " orderId:" + orderId);
            }

            @Override
            public void onFail(String payModule, Integer shopId, BigInteger orderId) {
                logger.info("PaySuccessHandle@onFail:module:" + payModule + " shopId:" + shopId + " orderId:" + orderId);
            }
        });
    }

    public void feedback() throws Exception {
        Integer shopId = request.getIntParameter("shopId");
        Pay pay = null;
        String payModule = null;

        isCustomContent = true;
        payModule = request.getParameter("module");
        pay = (Pay) applyFilters("PayPage@getFeedback" + Transforms.toUpperCasePosition(payModule, 0) + "Pay", null, shopId);
        pay.feedback(shopId, new Pay.PaySuccessHandle(){
            @Override
            public void onSuccess(String payModule, Integer shopId, BigInteger orderId, Integer totalFee, String tradeNo, Date payTime) {
                doAction("PaySuccessHandle@onSuccess", payModule, shopId, orderId, totalFee, tradeNo, payTime);
                Orders.updatePayTime(orderId, payTime, tradeNo, payModule);
                logger.info("PaySuccessHandle@onSucess:module:" + payModule + " shopId:" + shopId + " orderId:" + orderId);
            }

            @Override
            public void onFail(String payModule, Integer shopId, BigInteger orderId) {
                logger.info("PaySuccessHandle@onFail:module:" + payModule + " shopId:" + shopId + " orderId:" + orderId);
            }
        });
    }

    public void success() {
        String redirect = request.getParameter("redirect");
        Integer shopId = request.getIntParameter("shopId");
        BigInteger orderId = request.getBigIntegerParameter("orderId");

        if ( orderId != null ) {
            Order order = Orders.getOrder(orderId);
            addItem("orderId", orderId);
            addItem("order", order);
            doAction( "PayPage@success", orderId );
        }

        if (TextUtils.isEmpty(redirect))
            redirect = SystemConfig.getSiteUrls().getUrl("user.my");

        addItem("shopId", shopId);
        addItem("redirect", redirect);
    }

    public void fail() {
        String redirect = request.getParameter("redirect");

        if (TextUtils.isEmpty(redirect))
            redirect = SystemConfig.getSiteUrls().getUrl("user.my");

        addItem("redirect", redirect);
    }

    public void show() {
        BigInteger orderId = request.getBigIntegerParameter("orderId");
        String redirect = request.getParameter("redirect", "");
        Order order = Orders.getOrder(orderId);

        addItem("order", order);
        addItem("redirect", Encoding.urlEncode(redirect));

        addItem("page_title", "支付订单");
    }
}
