package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IOrderDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.EntityCount;
import jdroplet.data.model.Order;
import jdroplet.data.model.OrderItem;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class OrderDataProvider extends DataProvider implements IOrderDataProvider {

	@Override
	public Entity newEntity() {
		return new Order();
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
		Order.DataColumns.fill(rs, (Order) entity);
	}

	@Override
	public String getTable() {
		return Order.DataColumns.table;
	}

	@Override
	public String[] getColums() {
		return Order.DataColumns.getColums();
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return Order.DataColumns.getKeyValues((Order)entity);
	}

	@Override
	public Integer create(Order order) {
		SQLDatabase db = null;
		Map<String, Object> values = null;

		values = getKeyValues(order);
		db = new SQLDatabase();
		return db.insert("jdroplet_orders", null, values);
	}

	@Override
	public void update(BigInteger orderId, String field, Object value) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		values = new HashMap<String, Object>();

		values.put(field, value);
		db = new SQLDatabase();
		db.update(getTable(), values, "order_id=?", new Object[]{orderId});
	}

	@Override
	public Order getOrder(BigInteger orderId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String limit = null;
		String clause = null;
		String table = null;
		Order order = null;

		table = getTable();
		cls = getColums();
		clause = Order.DataColumns.orderId + "=?";
		args = new Object[] { orderId };
		limit = "1";

		db = new SQLDatabase();
		order = new Order();
		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				fill(rs, order);
			} else {
				throw new ApplicationException("Order " + orderId + " NotFound");
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}

		return order;
	}

	@Override
	public Order getOrder(Integer userId, Integer bookId, Integer chapterId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String limit = null;
		String clause = null;
		String table = null;
		Order order = null;

		table = getTable();
		cls = getColums();
		clause = "user_id=? AND item_id=? AND product_id=?";
		args = new Object[] { userId, bookId, chapterId };
		limit = "1";

		db = new SQLDatabase();
		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				order = new Order();
				fill(rs, order);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}

		return order;
	}

	@Override
	public void addItem(Integer shopId, BigInteger orderId, Integer productId, Integer skuId, Integer quantity, String name, String icon, Integer price, String description) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		values = new HashMap<String, Object>();

		values.put("shop_id", shopId);
		values.put("order_id", orderId);
		values.put("product_id", productId);
		values.put("spec_id", skuId);
		values.put("quantity", quantity);
		values.put("name", name);
		values.put("icon", icon);
		values.put("price", price);
		values.put("description", description);

		db = new SQLDatabase();
		db.insert("jdroplet_order_items", null, values);
	}

	@Override
	public List<OrderItem> getItems(BigInteger orderId) {
		List<OrderItem> entites = null;
		OrderItem item = null;
		SQLDatabase db1 = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String table = null;
		String clause = null;
		String orderBy = null;
		String groupBy = null;
		String limit = null;

		table = OrderItem.DataColumns.table;
		cls = OrderItem.DataColumns.getColums();

		//  构建筛选
		clause = "order_id=?";
		args = new Object[]{orderId};
		orderBy = "id ASC";

		entites = new ArrayList<OrderItem>();

		db1 = new SQLDatabase();
		try {
			rs = db1.query(table, cls, clause, args, groupBy, null, orderBy, limit);
			while (rs.next()) {
				item = new OrderItem();
				entites.add(item);
				OrderItem.DataColumns.fill(rs, item);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}

		return entites;
	}

	@Override
	public void updatePayTime(BigInteger orderId, Date payTime) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		values = new HashMap<String, Object>();

		values.put("pay_time", new Timestamp(payTime.getTime()));
		values.put("status", Order.ORDER_PAID);

		db = new SQLDatabase();
		db.update("jdroplet_orders", values, "order_id=?", new Object[]{orderId});
	}

	@Override
	public boolean exists(Integer userId, Integer itemId, Integer productId, Integer status) {
		if (status == null)
			return super.exists(getTable(), "user_id=? AND item_id=? AND product_id=?", new Object[] {userId, itemId, productId}) != null;
		else
			return super.exists(getTable(), "user_id=? AND item_id=? AND product_id=? AND status=?", new Object[] {userId, itemId, productId, status}) != null;
	}

	@Override
	public Integer getCount(Integer userId, Integer stauts, String type) {
		return super.getCount(getTable(), "user_id=? AND status=? AND type=?", new Object[]{userId, stauts, type});
	}

	@Override
	public List<EntityCount> getTopItems(Date dateFrom, Date dateTo, Integer count, String type) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String table = null;
		String cls[] = null;
		String groupBy = null;
		String orderBy = null;
		String limit = null;
		List<EntityCount> list = null;
		EntityCount ec = null;

		list = new ArrayList<>();
		table = getTable();
		cls = new String[] { "item_id, COUNT(0) as count" };
		groupBy = "item_id";
		orderBy = "count desc";
		limit = count.toString();
		db = new SQLDatabase();
		try {
			Object args[] = null;
			String clause = null;

			clause = "create_time>= ? AND create_time<= ? AND type = ?";
			args = new Object[] { dateFrom, dateTo, type };
			rs = db.query(table, cls, clause, args, groupBy, null, orderBy, limit);
			while (rs.next()) {
				ec = new EntityCount();
				ec.setId(rs.getInt(1));
				ec.setCount(rs.getInt(2));

				list.add(ec);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return list;
	}

	@Override
	public void addCoupon(BigInteger orderId, BigInteger couponId, String type, Integer value) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		values = new HashMap<String, Object>();

		values.put("order_id", orderId);
		values.put("coupon_id", couponId);
		values.put("coupon_type", type);
		values.put("coupon_value", value);

		db = new SQLDatabase();
		db.insert("jdroplet_order_coupons", null, values);
	}
}
