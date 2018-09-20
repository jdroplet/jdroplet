package jdroplet.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuibo on 2018/7/6.
 */
public class Cart extends Entity {

    public static class CartItem implements Serializable {
        private static final long serialVersionUID = -1l;

        private Integer itemId;
        private Integer specId;
        private Integer count;
        private Object object;

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getSpecId() {
            return specId;
        }

        public void setSpecId(Integer specId) {
            this.specId = specId;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    private Integer id;
    private Integer shopId;
    private String key;
    private List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
