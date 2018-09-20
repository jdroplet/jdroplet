package jdroplet.data.model;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/6.
 */
public class OrderItem extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_order_items";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String orderId = "order_id";
        public static String quantity = "quantity";
        public static String productId = "product_id";

        public static String specId = "spec_id";
        public static String name = "name";
        public static String price = "price";
        public static String icon = "icon";
        public static String description = "description";

        public static String[] getColums() {
            return new String[]{id, shopId, orderId, quantity, productId,
                    specId, name, price, icon, description};
        }

        public static void fill(ResultSet rs, OrderItem item) throws SQLException {
            item.id = rs.getInt(id);
            item.shopId = rs.getInt(shopId);
            item.orderId = (BigInteger) rs.getObject(orderId);
            item.quantity = rs.getInt(quantity);
            item.productId = rs.getInt(productId);

            item.specId = rs.getInt(specId);
            item.icon = rs.getString(icon);
            item.setName(rs.getString(name));
            item.setPrice(rs.getInt(price));
            item.setDescription(rs.getString(description));
        }

        public static Map<String, Object> getKeyValues(OrderItem item) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, item.shopId);
            map.put(orderId, item.getOrderId());
            map.put(quantity, item.getQuantity());
            map.put(productId, item.getOrderId());

            map.put(specId, item.specId);
            map.put(name, item.name);
            map.put(price, item.price);
            map.put(icon, item.icon);
            map.put(description, item.description);

            return map;
        }
    }

    private Integer id;
    private Integer shopId;
    private BigInteger orderId;
    private Integer productId;
    private Integer specId;
    private Integer quantity;

    private Integer price;
    private String name;
    private String icon;
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
