package jdroplet.bll;

import jdroplet.cache.RedisCache;
import jdroplet.data.model.Cart;

import java.text.MessageFormat;
import java.util.Iterator;

/**
 * Created by kuibo on 2018/7/6.
 */
public class Carts {

    public static String KEY_CART = "CART_sid:{0}_k:{1}";

    public static Cart get(Integer shopId, String userName) {
        String cacheKey = MessageFormat.format(KEY_CART, shopId, userName);

        return (Cart) RedisCache.get(cacheKey);
    }

    public static void clean(Integer shopId, String userName) {
        String cacheKey = MessageFormat.format(KEY_CART, shopId, userName);

        RedisCache.remove(cacheKey);
    }

    public static void add(Integer shopId, String userName, Integer itemId, Integer specId, Integer count) {
        String cacheKey = MessageFormat.format(KEY_CART, shopId, userName);
        Cart car = null;
        Cart.CartItem item = null;

        car = (Cart) RedisCache.get(cacheKey);
        if (car == null) {
            car = new Cart();
            car.setShopId(shopId);
            car.setKey(userName);
        }

        for(Cart.CartItem ci: car.getItems()) {
            if (ci.getItemId().equals(itemId) && ci.getSpecId().equals(specId)) {
                item = ci;
                break;
            }
        }

        if (item == null) {
            item = new Cart.CartItem();
            item.setCount(count);
            item.setItemId(itemId);
            item.setSpecId(specId);

            car.getItems().add(item);
        } else {
            item.setCount(item.getCount() + count);
        }

        RedisCache.set(cacheKey, car);
    }

    public static void remove(Integer shopId, String userName, Integer itemId, Integer specId) {
        String cacheKey = MessageFormat.format(KEY_CART, shopId, userName);
        Cart car = null;
        Cart.CartItem item = null;

        car = (Cart) RedisCache.get(cacheKey);
        if (car != null) {
            Iterator<Cart.CartItem> it = null;

            it = car.getItems().iterator();
            while (it.hasNext()) {
                item = it.next();
                if (item.getItemId().equals(itemId) && item.getSpecId().equals(specId))
                    it.remove();
            }

            RedisCache.set(cacheKey, car);
        }
    }
}
