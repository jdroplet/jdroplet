package jdroplet.pay.wechat;

import com.github.wxpay.sdk.*;
import jdroplet.bll.Orders;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.*;
import jdroplet.data.model.Order;
import jdroplet.data.model.Site;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.pay.Pay;
import jdroplet.sns.proxy.WechatProxy;
import jdroplet.util.Encoding;
import jdroplet.util.FileUtil;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WechatPay extends Pay {


    public class WXPayConfigImpl extends WXPayConfig {

        private byte[] certData;

        public WXPayConfigImpl() {
//            String certPath = "/path/to/apiclient_cert.p12";
//            File file = new File(certPath);
//            InputStream certStream = null;
//
//            try {
//                certStream = new FileInputStream(file);
//                this.certData = new byte[(int) file.length()];
//                certStream.read(this.certData);
//                certStream.close();
//            } catch (Exception e) {
//            }

        }

        public String getAppID() {
            return appid;
        }

        public String getMchID() {
            return mchid;
        }

        public String getKey() {
            return key;
        }

        public InputStream getCertStream() {
            return new ByteArrayInputStream(this.certData);
        }

        public int getHttpConnectTimeoutMs() {
            return 8000;
        }

        public int getHttpReadTimeoutMs() {
            return 10000;
        }

        @Override
        public IWXPayDomain getWXPayDomain() {
            IWXPayDomain iwxPayDomain = new IWXPayDomain() {
                @Override
                public void report(String domain, long elapsedTimeMillis, Exception ex) {

                }
                @Override
                public DomainInfo getDomain(WXPayConfig config) {
                    return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
                }
            };
            return iwxPayDomain;
        }


    }

    public static final String APP_ID = "wxpay_app_id";
    public static final String APP_KEY = "wxpay_key";
    public static final String APP_MCHID = "wxpay_mchid";
    public static final String APP_SECRET = "wxpay_secret";

    private static Logger logger = Logger.getLogger(WechatPay.class);

    private String appid;

    private String secret;

    private String mchid;

    private String key;

    private String clientIp;

    private String notify;

    private String openId;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    protected Map<String, String> _doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        Map<String, String> params = new HashMap<String, String>();
        WXPay wxpay = null;
        WXPayConfigImpl config;
        Map<String, String> respData = null;
        Map<String, String> jsParams = null;

        User user = null;
        user = Users.getCurrentUser();
        if (user.getId() != null && user.getUserName().indexOf("wechat_") >= 0) {
            openId = user.getUserName().replace("wechat_", "");
        } else {
            HttpRequest request = HttpContext.current().request();
            String code = request.getParameter("code");

            if (TextUtils.isEmpty(code)) {
                HttpResponse response = HttpContext.current().response();

                String curUrl = "http://" + request.getServerName() + request.getContextPath() + request.getServletPath() + "?" + (request.getQueryString());
                String url = SystemConfig.getSiteUrls().getUrl("snsproxy.oauth", "wechat", shopId) + "?scope=snsapi_base&forwardCode=true&redirect=" + Encoding.urlEncode(curUrl);

                response.setRedirect(url);
                return null;
            } else {
                WechatProxy proxy = new WechatProxy();

                proxy.setShopId(shopId);
                proxy.setCode(code);

                proxy.requestAccessToken();
                openId = proxy.getUserId();
            }
        }

        config = new WXPayConfigImpl();
        //wxpay = new WXPay(config, WXPayConstants.SignType.HMACSHA256, false);
        try {
            wxpay = new WXPay(config, false, false);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

        params.put("attach",  orderId.toString());
        params.put("body",  orderId.toString());
        params.put("out_trade_no", orderId.toString());
        params.put("spbill_create_ip", clientIp);
        params.put("total_fee", Integer.toString(totalPrice));
        params.put("trade_type", "JSAPI");
        params.put("openid", openId);
        params.put("notify_url", getNotify());

        try {
            respData = wxpay.unifiedOrder(params);
        } catch (Exception e) {
            logger.error("doPay:paramsY:" + JSONUtil.toJSONString(params));
            throw new ApplicationException("WechatPay@doPay:" + e.getMessage() + "@" + e.getClass().toString());
        }

        if ("FAIL".equals(respData.get("return_code")))
            throw new ApplicationException("WechatPay@doPay:" + respData.get("return_msg"));

        jsParams = new HashMap<>();
        jsParams.put("appId", appid);
        jsParams.put("timeStamp", Long.toString((new Date()).getTime()));
        jsParams.put("nonceStr", WXPayUtil.generateUUID());
        jsParams.put("package", "prepay_id=" + respData.get("prepay_id"));
        jsParams.put("signType", "HMACSHA256");
        try {
            jsParams.put("paySign", WXPayUtil.generateSignature(jsParams, getKey(), WXPayConstants.SignType.HMACSHA256));
        } catch (Exception e) {
            logger.error("doPay:jsParams:" + JSONUtil.toJSONString(jsParams));
            throw new ApplicationException("WechatPay@doPay:" + e.getMessage());
        }

        return jsParams;
    }

    @Override
    public String doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        Map<String, String> jsParams = null;

        jsParams = _doPay(shopId, orderId, totalPrice, redirect);
        if (jsParams == null)
            return null;

        String content = (String) AppCache.get("wechat_tpl");
        if (content == null) {
            Site site = SiteManager.getCurrentSite();
            String file = null;
            String[] tmp = null;

            tmp = new String[] {"pay", "wechat_create"};
            file = new StringBuilder(SystemConfig.getServerPath())
                    .append(SystemConfig.getTemplatePath())
                    .append("/")
                    .append(site.getTheme())
                    .append("/")
                    .append(StringUtils.join(tmp, "/"))
                    .append(".htm").toString();

            if (FileUtil.exists(file)) {
                content = FileUtil.getFileContent(file);
                AppCache.add("wechat_tpl", content, ICache.DAY_FACTOR);
            }
        }
        content = MessageFormat.format(content,
                JSONUtil.toJSONString(jsParams),
                SystemConfig.getSiteUrls().getUrl("pay.success"),
                SystemConfig.getSiteUrls().getUrl("pay.fail"),
                orderId.toString(),
                Encoding.urlEncode(redirect));
        return content;
    }

    @Override
    public void feedback(Integer shopId, PaySuccessHandle handle) {
        HttpRequest request = HttpContext.current().request();
        Map<String, String> data = null;
        WXPay wxpay = null;
        WXPayConfigImpl config;
        Map<String, String> queryData = null;
        String transId = null;
        BigInteger orderId = null;
        String raw = request.getRawPostData();
        Integer totalAmount = null;
        Order order = null;

        logger.info("reqdata:" +raw);
        try {
            data = WXPayUtil.xmlToMap(raw);
        } catch (Exception ex) {
            logger.error("feedback error:" + ex + " data:" + raw);
        }
        transId = data.get("transaction_id");
        orderId = new BigInteger(data.get("out_trade_no"));
        totalAmount = Integer.parseInt(data.get("total_fee"));

        order = Orders.getOrder(orderId);
        logger.info("feedback:sid:" + transId + " oid:" + orderId + " totalAmount:" + totalAmount + " mtotalAmount:" + order.getPayAmount() + " eq:" + (totalAmount.equals(order.getPayAmount())));
        data = new HashMap<>();
        data.put("transaction_id", transId);

        try {
            config = new WXPayConfigImpl();
            wxpay = new WXPay(config);
            queryData = wxpay.orderQuery(data);
            logger.info("querydata:" + queryData);

            if (handle != null) {
                handle.onSuccess("wechat", shopId,
                        orderId,
                        totalAmount,
                        transId, DateTime.parse(queryData.get("time_end"), "yyyyMMddHHmmss").getDate());
            }
        } catch (Exception e) {
            logger.error("WechatPay@feedback:" + e);

            if (handle != null) {
                handle.onFail("wechat", shopId,
                        new BigInteger(queryData.get("out_trade_no")));
            }
        }
    }

    @Override
    public void callback(Integer shopId, String redirect, PaySuccessHandle handle) {
    }

}
