package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RedisCache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.DateTime;
import jdroplet.core.SystemConfig;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IOrderDataProvider;
import jdroplet.data.model.EntityCount;
import jdroplet.data.model.Order;
import jdroplet.data.model.OrderItem;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.math.BigInteger;
import java.rmi.Remote;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class Orders {

	public static String GROUP_KEY_ORDERS = "ORDERS_GROUPS";

	public static String KEY_ORDER = "ORDER_";
	
	public static BigInteger buildOrderId(Integer userId) {
		String userSuffix = null;
		String timeSuffix = null;
		String randSuffix = null;
		long id = 0;
		Formatter fmt = null;

		// id 4
		fmt = new Formatter();
		fmt.format("%04d", userId);
		userSuffix = fmt.toString();
		if (userSuffix.length() > 4) {
			userSuffix = userSuffix.substring(userSuffix.length() - 4);
		}

		// time 3
		timeSuffix = Long.toString(System.nanoTime());
		timeSuffix = timeSuffix.substring(timeSuffix.length() - 3);

		// rand 5
		randSuffix = Integer.toString((int) ((Math.random() * 9 + 1) * 100));

		id = RedisCache.incr("order_id") + 1000;
		return new BigInteger(id + timeSuffix + randSuffix + userSuffix);
	}

	public static void addItem(Integer shopId, BigInteger orderId, Integer productId, Integer skuId, Integer count, String name, String icon, Integer price, String description) {
		IOrderDataProvider provider = null;

		provider = DataAccess.instance().getOrderDataProvider();
		provider.addItem(shopId, orderId, productId, skuId, count, name, icon, price, description);

		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static List<OrderItem> getItems(BigInteger orderId) {
		IOrderDataProvider provider = null;

		provider = DataAccess.instance().getOrderDataProvider();
		return provider.getItems(orderId);
	}

	public static void updatePayTime(BigInteger orderId, Date payTime, String tradeNo, String platForm) {
		IOrderDataProvider provider = null;

		provider = DataAccess.instance().getOrderDataProvider();
		provider.updatePayTime(orderId, payTime);

		// 更新支付次数
		Order order = null;

		order = Orders.getOrder(orderId);
		Integer count = null;

		count = provider.getCount(order.getUserId(), order.getStatus(), order.getType());
		provider.update(orderId, "`time`", count);


		provider.update(orderId, "trade_no", tradeNo);
		provider.update(orderId, "platform", platForm);

		// 设置是否新用户
		User user = Users.getCurrentUser();
		if (DateTime.diff(payTime, user.getRegistered()) == 0)
			provider.update(orderId, "new_user", 1);

		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static Order getOrder(BigInteger orderId) {
		IOrderDataProvider provider = null;
		String cacheKey = KEY_ORDER + orderId;
		Order order = null;

		order = (Order) RemoteCache.get(cacheKey);
		if (order == null) {
			OrderItem item = null;

			provider = DataAccess.instance().getOrderDataProvider();
			order = provider.getOrder(orderId);
			order.setItems(provider.getItems(orderId));
			RemoteCache.add(cacheKey, GROUP_KEY_ORDERS, order);
		}
		return order;
	}

	public static DataSet<Order> getOrders(Integer userId, Integer productId, Integer itemId, Integer shopId, Integer quantity,
										   Integer status, Date createDateFrom, Date createDateTo,
										   String type, Integer inviter, String searchTerms, Integer pageIndex, Integer pageSize) {
		DataSet<Order> datas = null;
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();
		SearchQuery query = null;
		SearchGroup group_root, group = null;

		group_root = new SearchGroup();
		group_root.setTerm(SearchGroup.AND);

		//========================
		group = new SearchGroup();
		group.setTerm(SearchGroup.AND);

		if (createDateFrom != null) {
			query = new SearchQuery(Order.DataColumns.createTime, DateTime.toString(createDateFrom), SearchQuery.GET);
			group.addQuery(query);
		}

		if (createDateTo != null) {
			query = new SearchQuery(Order.DataColumns.createTime, DateTime.toString(createDateTo), SearchQuery.LET);
			group.addQuery(query);
		}

		if (userId != null) {
			query = new SearchQuery(Order.DataColumns.userId, userId, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (productId != null) {
			query = new SearchQuery(Order.DataColumns.productId, productId, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (itemId != null) {
			query = new SearchQuery(Order.DataColumns.itemId, itemId, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (shopId != null) {
			query = new SearchQuery(Order.DataColumns.shopId, shopId, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (status != null) {
			query = new SearchQuery(Order.DataColumns.status, status, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (!TextUtils.isEmpty(type)) {
			query = new SearchQuery(Order.DataColumns.type, type, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (quantity != null && quantity != 0) {
			query = new SearchQuery(Order.DataColumns.quantity, quantity, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (inviter != null && inviter != 0) {
			query = new SearchQuery(Order.DataColumns.inviter, inviter, SearchQuery.EQ);
			group.addQuery(query);
		}

		if (!TextUtils.isEmpty(searchTerms)) {
			//========================
			group = new SearchGroup();
			group.setTerm(SearchGroup.OR);

			query = new SearchQuery(Order.DataColumns.tradeNo, searchTerms, SearchQuery.LIKE);
			group.addQuery(query);


			query = new SearchQuery(Order.DataColumns.orderId, searchTerms, SearchQuery.LIKE);
			group.addQuery(query);

			group_root.addGroup(group);
		}
		group_root.addGroup(group);

		datas = (DataSet) provider.search(group_root, Order.DataColumns.id, SortOrder.DESC, pageIndex, pageSize);
		return datas;
	}

	public static Order getOrder(Integer userId, Integer itemId, Integer productId) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		return provider.getOrder(userId, itemId, productId);
	}

	public static void create(Order order) {
		IOrderDataProvider provider = null;

		if ("false".equals(SystemConfig.getProperty("order.allow_duplicate." + order.getType()))
				&& Orders.exists(order.getUserId(), order.getItemId(), order.getProductId())) {
			Order oldOrder = Orders.getOrder(order.getUserId(), order.getItemId(), order.getProductId());
			order.setOrderId(oldOrder.getOrderId());
			order.setStatus(oldOrder.getStatus());
			return;
		}

		order.setOrderId(buildOrderId(order.getUserId()));
		order.setStatus(Order.ORDER_COMMIT);
		order.setCreateTime(new Date());

		provider = DataAccess.instance().getOrderDataProvider();
		provider.create(order);

		List<OrderItem> list = order.getItems();
		if (list != null) for(OrderItem oi:list) {
			addItem(order.getShopId(), order.getOrderId(), oi.getProductId(), oi.getSpecId(),
					oi.getQuantity(), oi.getName(), oi.getIcon(),
					oi.getPrice(), oi.getDescription());
		}
		String key = "KEY_" + order.getUserId() + "_" + order.getItemId() + "_" + order.getProductId();
		RedisCache.set(key, Boolean.TRUE);
		RedisCache.expire(key, 72);
	}

	public static boolean exists(Integer userId, Integer itemId, Integer productId) {
		return exists(userId, itemId, productId, null);
	}

	public static boolean exists(Integer userId, Integer itemId, Integer productId, Integer status) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();
		Boolean exists = null;
		String key = "KEY_" + userId + "_" + itemId + "_" + productId;

		if (status != null)
			key += "_" + status;

		exists = (Boolean) RemoteCache.get(key);
		if (exists == null || exists == false) {
			exists = provider.exists(userId, itemId, productId, status);

			if (exists == true)
				RemoteCache.add(key, exists);
		}

		return exists;
	}

	public static void updateType(BigInteger orderId, String type) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		provider.update(orderId, "type", type);
		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static void updatePayAmount(BigInteger orderId, Integer payAmount) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		provider.update(orderId, "pay_amount", payAmount);
		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static void updateStatus(BigInteger orderId, Integer status) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		provider.update(orderId, "status", status);

		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static void updateCouponValues(BigInteger orderId, Integer values) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		provider.update(orderId, "coupon_values", values);
		String cacheKey = KEY_ORDER + orderId;
		RemoteCache.remove(cacheKey);
	}

	public static Integer getCount(Integer userId, Integer status, String type) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		return provider.getCount(userId, status, type);
	}

	public static List<EntityCount> getTopItems(Date dateFrom, Date dateTo, Integer count, String type) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();
		List<EntityCount> list = null;
		String key = "Order_TOP_" + DateTime.toString(dateFrom) + "_" + DateTime.toString(dateTo) + "_" + count;

		list = (List<EntityCount>) RemoteCache.get(key);
		if (list == null) {
			list = provider.getTopItems(dateFrom, dateTo, count, type);
			list = (List<EntityCount>) PluginFactory.getInstance().applyFilters("Orders@getTopItems", list, dateFrom, dateTo, count);

			if (list.size() >0)
				RemoteCache.add(key, list, ICache.MONTH_FACTOR);
		}
		return list;
	}

	public static void addCoupon(BigInteger orderId, BigInteger couponId, String type, Integer value) {
		IOrderDataProvider provider = DataAccess.instance().getOrderDataProvider();

		provider.addCoupon(orderId, couponId, type, value);
	}
}
