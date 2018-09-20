package jdroplet.pay.wechat;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.sns.proxy.WechatProxy;
import jdroplet.util.Encoding;
import jdroplet.util.JSONUtil;
import jdroplet.util.QRCodeUtil;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/8/17.
 */
public class WechatQRCodePay extends WechatPay {

    private static Logger logger = Logger.getLogger(WechatQRCodePay.class);

    protected Map<String, String> _doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        Map<String, String> params = new HashMap<String, String>();
        WXPay wxpay = null;
        WXPayConfigImpl config;
        Map<String, String> respData = null;
        Map<String, String> jsParams = null;

        config = new WXPayConfigImpl();
        try {
            wxpay = new WXPay(config, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("appid", getAppid());//公众账号ID
        params.put("mch_id", getMchid());//商户号
        params.put("body", orderId.toString());//商品描述
        params.put("nonce_str", WXPayUtil.generateUUID());
        params.put("notify_url", getNotify());//通知地址
        params.put("out_trade_no", orderId.toString());//订单号
        params.put("spbill_create_ip", getClientIp());//终端ip
        params.put("trade_type", "NATIVE");//交易类型
        params.put("total_fee", String.valueOf(totalPrice));//总金额

        try {
            respData = wxpay.unifiedOrder(params);
        } catch (Exception e) {
            logger.error("doPay:paramsY:" + JSONUtil.toJSONString(params));
            throw new ApplicationException("WechatPay@doPay:" + e.getMessage() + "@" + e.getClass().toString());
        }

        if ("FAIL".equals(respData.get("return_code")))
            throw new ApplicationException("WechatPay@doPay:" + respData.get("return_msg"));

        HttpResponse response = HttpContext.current().response();
        String codeUrl = respData.get("code_url");
        String path = SystemConfig.getServerPath() + SystemConfig.getAttachmentsStorePath() + "/" + SiteManager.getCurrentSite().getName() + "/qrcode/";
        String key = null;

        try {
            key = QRCodeUtil.encode(codeUrl, path, true);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

        String url = SystemConfig.getSiteUrls().getUrl("qrcode.show") + "?key=" + key;
        response.setRedirect(url);
        return null;
    }
}
