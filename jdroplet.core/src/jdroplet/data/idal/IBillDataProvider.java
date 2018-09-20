package jdroplet.data.idal;

import java.math.BigInteger;


/**
 * Created by kuibo on 2018/1/29.
 */
public interface IBillDataProvider extends IDataProvider {

    boolean exists(Integer userId, BigInteger orderId);

    Integer getUserCoins(Integer userId);

    Integer getCouponUsedValues(Integer userId, BigInteger couponId);
}
