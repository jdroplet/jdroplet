package jdroplet.app.view.api;

import jdroplet.bll.*;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/7/24.
 */
public class CartPage extends APIPage {

    public void create() {
        List<Map<String, Object>> items = request.getObjectParameter(List.class);
        User user = Users.getCurrentUser();
        String userName = null;

        if (Users.isLoggedIn(user))
            userName = user.getUserName();
        else
            userName = Users.getTempUser().getUserName();


        for (Map<String, Object> item : items) {
            Carts.add((Integer) item.get("shopId"), userName,
                    (Integer) item.get("itemId"), (Integer) item.get("specId"), (Integer) item.get("count"));
        }
        renderJSON(0);
    }

    public void clean() {
        Integer shopId = request.getIntParameter("shopId");
        User user = Users.getCurrentUser();
        String userName = null;

        if (Users.isLoggedIn(user))
            userName = user.getUserName();
        else
            userName = Users.getTempUser().getUserName();

        Carts.clean(shopId, userName);
        renderJSON(0);
    }

    public void remove() {
        Integer goodsId = request.getIntParameter("goodsId");
        Integer specId = request.getIntParameter("specId");
        Goods goods = GoodsX.getGoods(goodsId);
        User user = Users.getCurrentUser();
        String userName = null;

        if (Users.isLoggedIn(user))
            userName = user.getUserName();
        else
            userName = Users.getTempUser().getUserName();

        Carts.remove(goods.getShopId(), userName, goodsId, specId);
        renderJSON(0);
    }

    public void add() {
        Integer count = request.getIntParameter("count");
        String params = request.getParameter("id");
        String[] ids = params.split("-");
        Integer goodsId = null;
        Integer specId = null;
        Goods goods = null;
        GoodsSpec goodsSpec = null;

        goodsId = Integer.parseInt(ids[0]);
        goods = GoodsX.getGoods(goodsId);

        if (ids.length == 2) {
            specId = Integer.parseInt(ids[1]);
        }

        User user = Users.getCurrentUser();
        String userName = null;

        if (Users.isLoggedIn(user))
            userName = user.getUserName();
        else
            userName = Users.getTempUser().getUserName();

        if (ids.length == 2) {
            Carts.add(goods.getShopId(), userName, goodsId, specId, count);
        } else {
            Carts.add(goods.getShopId(), userName, goodsId, 0, count);
        }
        renderJSON(0);
    }


    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Cart cart = null;
        User user = null;
        Integer totalAmount = 0;
        Integer itemPrice = 0;
        Integer itemCount = 0;

        user = Users.getCurrentUser();
        if (!Users.isLoggedIn(user))
            user = Users.getTempUser();

        cart = Carts.get(shopId, user.getUserName());
        if (cart != null) for(Cart.CartItem ci: cart.getItems()) {
            Map map = new HashMap();
            Goods goods = GoodsX.getGoods(ci.getItemId());
            GoodsSpec spec = null;

            itemPrice = goods.getSalePrice();
            if (ci.getSpecId() != 0) {
                spec = GoodsSpecs.getGoodsSpec(ci.getSpecId());

                Section sec = null;
                sec = Sections.getSection(spec.getProperty1(), true);
                if (sec == null)
                    map.put("property0", "");
                else
                    map.put("property0", sec.getName());

                sec = Sections.getSection(spec.getProperty2(), true);
                if (sec == null)
                    map.put("property1", "");
                else
                    map.put("property1", sec.getName());

                sec = Sections.getSection(spec.getProperty3(), true);
                if (sec == null)
                    map.put("property2", "");
                else
                    map.put("property2", sec.getName());

                itemPrice = spec.getPrice();
            }
            map.put("goods", goods);
            map.put("spec", spec);
            map.put("price", itemPrice);
            ci.setObject(map);

            totalAmount += (itemPrice * ci.getCount());
            itemCount += ci.getCount();
        }
        renderJSON(0, null, cart);
    }
}
