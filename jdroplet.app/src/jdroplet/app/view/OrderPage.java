package jdroplet.app.view;

import jdroplet.app.view.api.APIPage;
import jdroplet.bll.*;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.*;
import jdroplet.util.DataSet;
import jdroplet.util.Encoding;
import jdroplet.util.I18n;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class OrderPage extends APIPage {

	public static final Integer ORDER_IS_COMPLETE = 1011000;

	private static Logger logger = Logger.getLogger(OrderPage.class);

	public void create() {
		Integer shopId = request.getIntParameter("shopId");
		Integer productId = request.getIntParameter("productId");
		Integer itemId = request.getIntParameter("itemId");
		Integer quantity = request.getIntParameter("quantity");
		Integer totalAmount = request.getIntParameter("totalAmount");
		Integer inviter = request.getIntParameter("inviter");
		Integer contactId = request.getIntParameter("contactId");
		String module = request.getParameter("module");
		String remarks = request.getParameter("remarks");
		String type = request.getParameter("type");
		String from = request.getParameter("from");
		String redirect = request.getParameter("redirect");
		String payUrl = null;
		Order order = null;
		User user = null;
		SiteUrls urls = SystemConfig.getSiteUrls();

		if (TextUtils.isEmpty(from)) {
			from = request.getCookieValue("from");
		}
		if (inviter == null) {
			inviter = request.getIntCookieValue("inviter", 0);
		}

		user = Users.getCurrentUser();
		order = new Order();
		order.setShopId(shopId);
		order.setOrderId(null);
		order.setProductId(productId);
		order.setItemId(itemId);
		order.setUserId(user.getId());
		order.setPayAmount(totalAmount);
		order.setTotalAmount(totalAmount);
		order.setCreateTime(new Date());
		order.setQuantity(quantity);
		order.setStatus(Order.ORDER_COMMIT);
		order.setType(type);
		order.setRemarks(remarks);
		order.setComeFrom(from);
		order.setNewUser(Users.isNewUser(user));
		order.setIp(request.getXRemoteAddr());
		order.setInviter(inviter);
		order.setContactId(contactId);
		order.setShippingAmount(0);
		order.setCouponValues(0);

		Orders.create(order);

		if (order.getStatus() == Order.ORDER_PAID) {
			response.setRedirect(redirect);
			return;
		}
		Cart car = Carts.get(shopId, user.getUserName());
		if (car != null) {
			String name = null;
			String icon = null;
			Integer itemAmount = 0;
			Integer payAmount = 0;

			for (Cart.CartItem ci : car.getItems()) {
				if (ci.getSpecId() != 0) {
					Goods goods = GoodsX.getGoods(ci.getItemId());
					GoodsSpec spec = GoodsSpecs.getGoodsSpec(ci.getSpecId());

					itemAmount = spec.getPrice();

					name = goods.getName();
					Section sec = null;
					sec = Sections.getSection(spec.getProperty1(), true);
					if (sec != null)
						name += " " + sec.getName();

					sec = Sections.getSection(spec.getProperty2(), true);
					if (sec != null)
						name += " " + sec.getName();

					sec = Sections.getSection(spec.getProperty3(), true);
					if (sec != null)
						name += " " + sec.getName();

					icon = goods.getPics()[0];
				} else {
					Goods goods = GoodsX.getGoods(ci.getItemId());
					itemAmount = goods.getSalePrice();

					name = goods.getName();
					icon = goods.getPics()[0];
				}
				payAmount += itemAmount;
				Orders.addItem(order.getShopId(), order.getOrderId(), ci.getItemId(), ci.getSpecId(),
						ci.getCount(), name, icon, itemAmount, "");
			}
			Carts.clean(shopId, user.getUserName());

			Orders.updatePayAmount(order.getOrderId(), payAmount);
		}
		super.doAction("OrderPage@created", order);

		if (TextUtils.isEmpty(module))
			payUrl = urls.getUrl("pay.show", shopId) + "?orderId=" + order.getOrderId() + "&redirect=" + redirect;
		else
			payUrl = urls.getUrl("pay.create", shopId) + "?orderId=" + order.getOrderId()
															+ "&module=" + module
															+ "&redirect=" + redirect;
		response.setRedirect(payUrl);
	}

	public void cancel() {
		BigInteger orderId = request.getBigIntegerParameter("orderId");
		Orders.updateStatus(orderId, Order.ORDER_CANCELED);

		renderJSON(0, null);
	}

	public void reset() {
		String[] fields = request.getParameter("fields").split("-");
		BigInteger orderId = request.getBigIntegerParameter("orderId");
		Order order = Orders.getOrder(orderId);

		for (String fiel : fields) {
			if ("status".equals(fiel)) {
				if (order.getStatus().equals(Order.ORDER_COMPLETE)) {
					renderJSON(ORDER_IS_COMPLETE, I18n.getString(Integer.toString(ORDER_IS_COMPLETE)));
					return;
				} else {
					Integer status = request.getIntParameter("status");
					Orders.updateStatus(orderId, status);
				}
			}
		}
		renderJSON(0);
	}

	public void save() {
		Order order = null;
		User user = null;

		user = Users.getCurrentUser();
		order = request.getObjectParameter(Order.class);
		order.setUserId(user.getId());
		order.setIp(request.getXRemoteAddr());
		Orders.create(order);

		renderJSON(0, "", order.getOrderId());
	}

	public void list() {
		Integer pageIndex = request.getIntParameter("pageIndex");
		Integer pageSize = request.getIntParameter("pageSize");
		Integer status = request.getIntParameter("status");
		Integer shopId = request.getIntParameter("shopId");
		Integer userId = 0;
		Integer itemId = request.getIntParameter("itemId");
		Integer productId = request.getIntParameter("productId");
		Integer quantity = request.getIntParameter("quantity");
		Boolean isAdmin = request.getBooleanParameter("isAdmin", false);
		String type = request.getParameter("type");
		String searchTerms = request.getParameter("searchTerms");
		Date createDateFrom = request.getDateParameter("createDateFrom");
		Date createDateTo = request.getDateParameter("createDateTo");
		DataSet<Order> datas = null;
		User user = Users.getCurrentUser();
		List<OrderItem> items = null;

		if (isAdmin == true && user.isAdministrator()) {
			userId = request.getIntParameter("userId");
		} else {
			userId = user.getId();
		}

		datas = Orders.getOrders(userId, productId, itemId, shopId, quantity, status,
				createDateFrom, createDateTo, type, null, searchTerms, pageIndex, pageSize);
		for(Order order:datas.getObjects()) {
			items = Orders.getItems(order.getOrderId());
			order.setItems(items);
		}
		renderJSON(0, "", datas);
	}

	public void count() {
		User user = Users.getCurrentUser();
		Integer status = request.getIntParameter("status");
		String type = request.getParameter("type");
		Integer count = null;

		count = Orders.getCount(user.getId(), status, type);
		renderJSON(0, null, count);
	}
}
