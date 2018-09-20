package jdroplet.app.view.api;

import jdroplet.bll.*;
import jdroplet.data.model.*;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by kuibo on 2017/12/15.
 */
public class ShopPage extends APIPage {

    public void get() {
        Integer shopId = request.getIntParameter("id");
        Shop shop = Shops.getShop(shopId);

        renderJSON(0, null, shop);
    }

    public void menu() {
        Integer id = request.getIntParameter("id");
        Integer parentId = request.getIntParameter("parentId");
        List<Post> posts = null;
        List<Section> sections = null;

        sections = Sections.getSections(id, parentId, "shop", SortSectionBy.ID, SortOrder.DESC, 1, 20).getObjects();
        for(Section section:sections) {
            posts = Posts.getPosts(parentId,null, null, null, new Integer[]{section.getId()}, "", "shop", Post.APPROVED, null,0, false, 1, Integer.MAX_VALUE).getObjects();
            for(Post post:posts) {
                post.setValue("skus", Skus.getSkus(post.getId(), 0));
            }
            section.setValue("products", posts);
        }
        renderJSON(0, "", sections);
    }

    public void orders() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        Integer status = request.getIntParameter("status");
        Integer userId = Users.getCurrentUser().getId();
        String type = request.getParameter("type");
        DataSet<Order> datas = null;

        datas = Orders.getOrders(userId, null,null, null, null,
                status, null, null, null, null, type, pageIndex, pageSize);
        for(Order order:datas.getObjects()) {
            List<OrderItem> items = null;

            items = Orders.getItems(order.getOrderId());
            order.setItems(items);
        }
        renderJSON(0, "", datas);
    }

    public void order() {
        BigInteger orderId = request.getBigIntegerParameter("orderId");
        Order order = null;

        order = Orders.getOrder(orderId);
        renderJSON(0, "", order);
    }
}
