package jdroplet.app.view.admin;

import jdroplet.bll.Contacts;
import jdroplet.bll.Orders;
import jdroplet.bll.Shops;
import jdroplet.data.model.Contact;
import jdroplet.data.model.Order;
import jdroplet.data.model.OrderItem;
import jdroplet.data.model.Shop;
import jdroplet.util.DataSet;

import java.math.BigInteger;
import java.util.List;


/**
 * Created by kuibo on 2018/1/20.
 */
public class ManageOrderPage extends ManageMasterPage {

    public void show() {
        BigInteger orderId = request.getBigIntegerParameter("orderId");
        Order order = null;
        List<OrderItem> items = null;
        Contact contact = null;

        order = Orders.getOrder(orderId);
        items = Orders.getItems(orderId);
        if (order.getContactId() != 0)
            contact = Contacts.getContact(order.getContactId());

        addItem("contact", contact);
        addItem("items", items);
        addItem("order", order);
    }

    public void list() {
        Integer userId = request.getIntParameter("userId");
        Integer productId = request.getIntParameter("productId");
        Integer itemId = request.getIntParameter("itemId");
        Integer status = request.getIntParameter("status", 0);
        Integer inviter = request.getIntParameter("inviter");
        String searchTerms = request.getParameter("searchTerms");
        String type = request.getParameter("type", "");
        Integer pageIndex = request.getIntParameter("pageIndex");
        DataSet<Order> datas = null;
        Shop shop = null;

        if (shopId != null)
            shop = Shops.getShop(shopId);
        else
            shop = new Shop();

        datas = Orders.getOrders(userId, productId, itemId, shopId, null, status,
                null, null, type, inviter, searchTerms, pageIndex, 20);

        addItem("pageIndex", pageIndex);
        addItem("status", status);
        addItem("type", type);
        addItem("shopId", shopId);
        addItem("shop", shop);
        addItem("datas", datas);
        addItem("searchTerms", searchTerms);
    }
}
