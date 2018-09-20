package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2017/11/30.
 */
public class GoodsSpec extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_goods_spec";

        public static String id = "id";
        public static String goodsId = "goods_id";
        public static String sku = "sku";
        public static String price = "price";
        public static String cost = "cost";

        public static String shopId = "shop_id";
        public static String stocks = "stocks";
        public static String status = "status";
        public static String createTime = "create_time";

        public static String property1 = "property1";
        public static String property2 = "property2";
        public static String property3 = "property3";


        public static String[] getColums() {
            return new String[]{id, goodsId, sku, price, cost, shopId,
                    stocks, status, createTime,
                    property1, property2, property3};
        }

        public static void fill(ResultSet rs, GoodsSpec goodsSpec) throws SQLException {
            goodsSpec.id = rs.getInt(id);
            goodsSpec.goodsId = rs.getInt(goodsId);
            goodsSpec.sku = rs.getString(sku);
            goodsSpec.price = rs.getInt(price);
            goodsSpec.cost = rs.getInt(cost);

            goodsSpec.stocks = rs.getInt(stocks);
            goodsSpec.shopId = rs.getInt(shopId);
            goodsSpec.status = rs.getInt(status);
            goodsSpec.createTime = new Date(rs.getTimestamp(createTime).getTime());

            goodsSpec.property1 = rs.getInt(property1);
            goodsSpec.property2 = rs.getInt(property2);
            goodsSpec.property3 = rs.getInt(property3);
        }

        public static Map<String, Object> getKeyValues(GoodsSpec goodsSpec) {
            Map<String, Object> map = new HashMap<>();

            map.put(goodsId, goodsSpec.goodsId);
            map.put(sku, goodsSpec.sku);
            map.put(price, goodsSpec.price);
            map.put(cost, goodsSpec.cost);

            map.put(stocks, goodsSpec.stocks);
            map.put(shopId, goodsSpec.shopId);
            map.put(status, goodsSpec.status);
            if (goodsSpec.id == null || goodsSpec.id == 0)
                map.put(createTime, new Timestamp(goodsSpec.createTime.getTime()));

            map.put(property1, goodsSpec.property1);
            map.put(property2, goodsSpec.property2);
            map.put(property3, goodsSpec.property3);
            return map;
        }
    }

    public static int NORMAL = 1;
    public static int OFFLINE = 3;

    private Integer id;

    /**
     * 所属商店id
     */
    private Integer shopId;
    /**
     * 所属商品id
     */
    private Integer goodsId;

    /**
     * sku
     */
    private String sku;

    /**
     * 售价
     */
    private Integer price;

    /**
     * 成本价
     */
    private Integer cost;

    /**
     * 属性1
     */
    private Integer property1;

    /**
     * 属性2
     */
    private Integer property2;

    /**
     * 属性3
     */
    private Integer property3;

    /**
     * 规格库存
     */
    private Integer stocks;

    private Integer status;

    private Date createTime;

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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getProperty1() {
        return property1;
    }

    public void setProperty1(Integer property1) {
        this.property1 = property1;
    }

    public Integer getProperty2() {
        return property2;
    }

    public void setProperty2(Integer property2) {
        this.property2 = property2;
    }

    public Integer getProperty3() {
        return property3;
    }

    public void setProperty3(Integer property3) {
        this.property3 = property3;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
