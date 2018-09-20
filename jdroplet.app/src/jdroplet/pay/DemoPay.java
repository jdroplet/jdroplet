package jdroplet.pay;

import jdroplet.bll.Orders;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpResponse;
import jdroplet.core.Page;
import jdroplet.core.SystemConfig;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by kuibo on 2018/7/20.
 */
public class DemoPay extends Pay {
    @Override
    public String doPay(Integer shopId, BigInteger orderId, Integer totalPrice, String redirect) {
        Orders.updatePayTime(orderId, new Date(), "", "demo");
        HttpResponse response = HttpContext.current().response();

        response.setRedirect(SystemConfig.getSiteUrls().getUrl("pay.success", shopId) + "?orderId=" + orderId);
        return "";
    }

    @Override
    public void feedback(Integer shopId, PaySuccessHandle handle) {

    }

    @Override
    public void callback(Integer shopId, String redirect, PaySuccessHandle handle) {

    }
}
