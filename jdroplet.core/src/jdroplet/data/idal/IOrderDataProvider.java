package jdroplet.data.idal;

import jdroplet.data.model.EntityCount;
import jdroplet.data.model.Order;
import jdroplet.data.model.OrderItem;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface IOrderDataProvider extends  IDataProvider {

	Order getOrder(BigInteger orderId);
	Order getOrder(Integer userId, Integer itemId, Integer productId);

	Integer create(Order order);
	void update(BigInteger orderId, String field, Object value);

	void addItem(Integer shopId, BigInteger orderId, Integer productId, Integer skuId, Integer quantity, String name, String icon, Integer price, String description);
	List<OrderItem> getItems(BigInteger orderId);

	void updatePayTime(BigInteger orderId, Date payTime);

	boolean exists(Integer userId, Integer itemId, Integer productId, Integer status);

	Integer getCount(Integer userId, Integer stauts, String type);

	List<EntityCount> getTopItems(Date dateFrom, Date dateTo, Integer count, String type);


	void addCoupon(BigInteger orderId, BigInteger couponId, String type, Integer value);
}
