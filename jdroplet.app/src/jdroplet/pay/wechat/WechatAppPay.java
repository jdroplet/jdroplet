package jdroplet.pay.wechat;

import jdroplet.util.JSONUtil;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created by kuibo on 2018/7/26.
 */
public class WechatAppPay extends WechatPay {

    @Override
    public String doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        Map<String, String> jsParams = null;

        jsParams = _doPay(shopId, orderId, totalPrice, redirect);
        if (jsParams == null)
            return null;

        return JSONUtil.toJSONString(jsParams);
    }

    @Override
    public void feedback(Integer shopId, PaySuccessHandle handle) {

    }

    @Override
    public void callback(Integer shopId, String redirect, PaySuccessHandle handle) {

    }
}
