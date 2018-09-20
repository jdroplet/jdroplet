package jdroplet.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.core.SystemConfig;
import jdroplet.util.Encoding;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/10.
 */
public class AliPay extends Pay {

    private String appid;
    private String rsaPrivateKey;
    private String aliPubKey;

    private String notifyUrl;
    private String returnUrl;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public String getAliPubKey() {
        return aliPubKey;
    }

    public void setAliPubKey(String aliPubKey) {
        this.aliPubKey = aliPubKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public static final String ALI_PAY_ID = "alipay_id";
    public static final String ALI_PUBKEY = "alipay_pubkey";
    public static final String ALI_RSA_PRI_KEY = "alipay_prikey";

    private static Logger logger = Logger.getLogger(AliPay.class);

    @Override
    public String doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderId.toString();
        // 订单名称，必填
        String subject = orderId.toString();
        // 付款金额，必填
        String total_amount = Double.toString((double) totalPrice / 100);
        // 商品描述，可空
        String body = orderId.toString();
        // 超时时间 可空
        String timeout_express = "2m";
        // 销售产品码 必填
        String product_code = orderId.toString();
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(SystemConfig.getProperty("pay.alipay.url"), appid, rsaPrivateKey, "json", "UTF-8", aliPubKey, "RSA2");
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(notifyUrl);
        // 设置同步地址
        alipay_request.setReturnUrl(returnUrl + "?redirect=" + Encoding.urlEncode(redirect));

        // form表单生产
        String form = null;
        try {
            form = client.pageExecute(alipay_request).getBody();
        } catch (Exception e) {
            logger.error("AliPay@doPay:" + e);
        }
        return form;
    }

    private boolean verify(Integer shopId, PaySuccessHandle handle) {
        Map<String,String> params = new HashMap<String,String>();
        HttpRequest request = HttpContext.current().request();
        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            if ("redirect".equals(name))
                continue;

            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
            }

            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        boolean verify_result = false;

        try {
            verify_result = AlipaySignature.rsaCheckV1(params, aliPubKey, "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            logger.error("rsaCheckV1 error:" + e);
        }

        if (verify_result) {
            String out_trade_no = request.getParameter("out_trade_no");
            String trade_no = request.getParameter("trade_no");
            Date notifyTime = request.getDateParameter("notify_time", "yyyy-MM-dd HH:mm:ss");
            Integer totalFee  = (int) (request.getDoubleParameter("total_amount") * 100);

            if (notifyTime == null)
                notifyTime = request.getDateParameter("timestamp", "yyyy-MM-dd HH:mm:ss");

            if (handle != null)
                handle.onSuccess("alipay", shopId, new BigInteger(out_trade_no), totalFee, trade_no, notifyTime);
        } else {

            String out_trade_no = request.getParameter("out_trade_no");
            if (handle != null)
                handle.onFail("alipay", shopId, new BigInteger(out_trade_no));
        }
        return verify_result;
    }

    @Override
    public void feedback(Integer shopId, PaySuccessHandle handle) {
        boolean verify_result = verify(shopId, handle);
        HttpResponse response = HttpContext.current().response();
        OutputStream os = null;
        String str = verify_result ? "success" : "fail";

        try {
            os = response.getOutputStream();
            os.write(str.getBytes());
        } catch (IOException e) {
            logger.error("feedback@error" + e);
        }  finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void callback(Integer shopId, String redirect, PaySuccessHandle handle) {
        boolean verify_result = verify(shopId, handle);
        HttpResponse response = HttpContext.current().response();
        HttpRequest request = HttpContext.current().request();
        BigInteger orderId = request.getBigIntegerParameter("out_trade_no");

        if (verify_result) {//验证成功
            response.setRedirect(SystemConfig.getSiteUrls().getUrl("pay.success") + "?orderId=" + orderId + "&redirect=" + Encoding.urlEncode(redirect));
        } else {
            response.setRedirect(SystemConfig.getSiteUrls().getUrl("pay.fail") + "?orderId=" + orderId + "&redirect=" + Encoding.urlEncode(redirect));
        }
    }
}
