package jdroplet.data.model;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order<T> extends Entity {

	public static class DataColumns extends DataColumnsBase {
		public static String table = "jdroplet_orders";

		public static String id = "id";
		public static String userId = "user_id";
		public static String status = "status";
		public static String orderId = "order_id";
		public static String contactId = "contact_id";

		public static String createTime = "create_time";
		public static String payTime = "pay_time";
		public static String totalAmount = "total_amount";
		public static String payAmount = "pay_amount";
		public static String type = "type";

		public static String shopId = "shop_id";
		public static String shopName = "shop_name";
		public static String remarks = "remarks";
		public static String quantity = "quantity";

		public static String comeFrom = "come_from";
		public static String itemId = "item_id";
		public static String productId = "product_id";
		public static String time = "time";

		public static String newUser = "new_user";
		public static String tradeNo = "trade_no";
		public static String platform = "platform";

		public static String ip = "ip";
		public static String inviter = "inviter";
		public static String shippingAmount = "shipping_amount";
		public static String couponValues = "coupon_values";

		public static String[] getColums() {
			return new String[]{id, userId, status, orderId,
					createTime, payTime, totalAmount, payAmount, type,
					shopId, shopName, remarks, quantity,
					comeFrom, itemId, productId, time,
					newUser, tradeNo, platform,
					ip, contactId, inviter, shippingAmount, couponValues};
		}

		public static void fill(ResultSet rs, Order order) throws SQLException {
			Timestamp ts = null;

			order.id = rs.getInt(id);
			order.userId = rs.getInt(userId);
			order.status = Integer.valueOf(rs.getInt(status));
			order.orderId = (BigInteger) rs.getObject(orderId);
			order.contactId = rs.getInt(contactId);

			order.createTime = new Date(rs.getTimestamp(createTime).getTime());

			ts = rs.getTimestamp(payTime);
			if (ts != null) order.setPayTime(new Date(ts.getTime()));
			order.totalAmount = rs.getInt(totalAmount);
			order.payAmount = rs.getInt(payAmount);
			order.type = rs.getString(type);

			order.shopId = rs.getInt(shopId);
			order.shopName = rs.getString(shopName);
			order.remarks = rs.getString(remarks);
			order.quantity = rs.getInt(quantity);


			order.comeFrom = rs.getString(comeFrom);
			order.itemId = rs.getInt(itemId);
			order.productId = rs.getInt(productId);
			order.time = rs.getInt(time);

			order.newUser = rs.getBoolean(newUser);
			order.tradeNo = rs.getString(tradeNo);
			order.platform = rs.getString(platform);

			order.ip = rs.getString(ip);
			order.inviter = rs.getInt(inviter);
			order.shippingAmount = rs.getInt(shippingAmount);
			order.couponValues = rs.getInt(couponValues);
		}

		public static Map<String, Object> getKeyValues(Order order) {
			Map<String, Object> map = new HashMap<>();

			map.put(userId, order.getUserId());
			map.put(status, order.getStatus());
			map.put(orderId, order.getOrderId());
			map.put(contactId, order.contactId);

			map.put(createTime, new Timestamp(order.getCreateTime().getTime()));
			if (order.getPayTime() != null)
				map.put(payTime, new Timestamp(order.getPayTime().getTime()));
			map.put(totalAmount, order.totalAmount);
			map.put(payAmount, order.payAmount);
			map.put(type, order.getType());

			map.put(shopId, order.shopId);
			map.put(shopName, order.shopName);
			map.put(remarks, order.remarks);
			map.put(quantity, order.quantity);

			map.put(comeFrom, order.comeFrom);
			map.put(itemId, order.itemId);
			map.put(productId, order.productId);
			map.put(time, order.time);

			map.put(newUser, order.newUser);
			map.put(tradeNo, order.tradeNo);
			map.put(platform, order.platform);

			map.put(ip, order.ip);
			map.put(inviter, order.inviter);
			map.put(shippingAmount, order.shippingAmount);
			map.put(couponValues, order.couponValues);

			return map;
		}
	}

	public static final int ORDER_CANCELED = 0; // 取消
	public static final int ORDER_COMMIT = 1; // 已经提交

	public static final int ORDER_PAID = 4;  // 已经支付

	public static final int ORDER_SHIPPING = 5;  // 已发货


	public static final int ORDER_COMPLETE = 9;  // 已完成

	public static final String BOOK = "book";
	public static final String COUPON = "coupon";
	public static final String COIN = "coin";
	public static final String RECHARGE = "recharge";

	private Integer id;
	private Integer userId;
	private BigInteger orderId;
	private Integer itemId;
	private Integer productId;
	private Integer shopId;
	private Integer contactId;

	private Date createTime;
	private Date payTime;
	private Integer totalAmount;
	private Integer payAmount;
	private String type;
	private Integer status;

	private Integer time;
	private Integer quantity;
	private String shopName;
	private String remarks;
	private List<OrderItem> items;

	private String comeFrom;
	private boolean newUser;
	private String tradeNo;
	private String platform;
	private String ip;
	private Integer inviter;

	private Integer shippingAmount;
	private Integer couponValues;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigInteger getOrderId() {
		return orderId;
	}

	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Integer payAmount) {
		this.payAmount = payAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Integer getInviter() {
		return inviter;
	}

	public void setInviter(Integer inviter) {
		this.inviter = inviter;
	}

	public Integer getShippingAmount() {
		return shippingAmount;
	}

	public void setShippingAmount(Integer shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	public Integer getCouponValues() {
		return couponValues;
	}

	public void setCouponValues(Integer couponValues) {
		this.couponValues = couponValues;
	}
}
