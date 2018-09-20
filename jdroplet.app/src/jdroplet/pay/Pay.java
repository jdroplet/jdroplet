package jdroplet.pay;

import java.math.BigInteger;
import java.util.Date;

public abstract class Pay {

	public interface PaySuccessHandle {
		void onSuccess(String payModule, Integer shopId, BigInteger orderId, Integer payAmount, String tradeNo, Date payTime);
		void onFail(String payModule, Integer shopId, BigInteger orderId);
	}

	public abstract String doPay(Integer shopId, BigInteger orderId, Integer payAmount, String redirect);

	public abstract void feedback(Integer shopId, PaySuccessHandle handle);
	
	public abstract void callback(Integer shopId, String redirect, PaySuccessHandle handle);
}
