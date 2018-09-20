package jdroplet.data.model;

import jdroplet.core.HttpRequest;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/6/21.
 */
public class Goods extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_goods";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String name = "name";
        public static String excerpt = "excerpt";
        public static String description = "description";

        public static String sku = "sku";
        public static String price = "price";
        public static String salePrice = "sale_price";
        public static String cost = "cost";
        public static String status = "status";

        public static String sectionId1 = "section_id1";
        public static String sectionId2 = "section_id2";
        public static String sectionId3 = "section_id3";

        public static String createTime = "create_time";
        public static String stockTime = "stock_time";
        public static String stocks = "stocks";
        public static String tags = "tags";

        public static String propertyName1 = "property_name1";
        public static String propertyName2 = "property_name2";
        public static String propertyName3 = "property_name3";

        public static String pics = "pics";

        public static String[] getColums() {
            return new String[]{id, shopId, name, excerpt, description,
                    sku, price, salePrice, cost, status,
                    sectionId1, sectionId2, sectionId3, createTime, stockTime, tags, stocks,
                    propertyName1, propertyName2, propertyName3,
            pics};
        }

        public static void fill(HttpRequest req, Goods goods) {
            goods.id = req.getIntParameter(id);
            goods.shopId = req.getIntParameter(shopId);
            goods.name = req.getParameter(name);
            goods.excerpt = req.getParameter(excerpt);
            goods.description = req.getParameter(description);

            goods.sku = req.getParameter(sku);
            goods.price = (int) (req.getDoubleParameter(price) * 100);
            goods.salePrice = (int) (req.getDoubleParameter(salePrice) * 100);
            goods.cost = (int) (req.getDoubleParameter(cost) * 100);
            goods.status = req.getIntParameter(status);

            goods.sectionId1 = req.getIntParameter(sectionId1);
            goods.sectionId2 = req.getIntParameter(sectionId2);
            goods.sectionId3 = req.getIntParameter(sectionId3);

            goods.createTime = new Date(req.getDateParameter(createTime, new Date()).getTime());
            goods.stockTime = new Date(req.getDateParameter(stockTime, new Date()).getTime());
            goods.tags = req.getParameter(tags);
            goods.stocks = req.getIntParameter(stocks);

            goods.propertyName1 = req.getParameter(propertyName1);
            goods.propertyName2 = req.getParameter(propertyName2);
            goods.propertyName3 = req.getParameter(propertyName3);

            goods.pics = req.getParameterValues("pics");

            Integer[] sectionIds = req.getIntParameterValues("sectionIds");
            if (sectionIds.length > 0)
                goods.sectionId1 = sectionIds[0];

            if (sectionIds.length > 1)
                goods.sectionId2 = sectionIds[1];

            if (sectionIds.length > 2)
                goods.sectionId3 = sectionIds[2];
        }

        public static void fill(ResultSet rs, Goods goods) throws SQLException {
            goods.id = rs.getInt(id);
            goods.shopId = rs.getInt(shopId);
            goods.name = rs.getString(name);
            goods.excerpt = rs.getString(excerpt);
            goods.description = rs.getString(description);

            goods.sku = rs.getString(sku);
            goods.price = rs.getInt(price);
            goods.salePrice = rs.getInt(salePrice);
            goods.cost = rs.getInt(cost);
            goods.status = rs.getInt(status);

            goods.sectionId1 = rs.getInt(sectionId1);
            goods.sectionId2 = rs.getInt(sectionId2);
            goods.sectionId3 = rs.getInt(sectionId3);

            goods.createTime = new Date(rs.getTimestamp(createTime).getTime());
            goods.stockTime = new Date(rs.getTimestamp(stockTime).getTime());
            goods.tags = rs.getString(tags);
            goods.stocks = rs.getInt(stocks);

            goods.propertyName1 = rs.getString(propertyName1);
            goods.propertyName2 = rs.getString(propertyName2);
            goods.propertyName3 = rs.getString(propertyName3);

            String val = rs.getString(pics);
            if (!TextUtils.isEmpty(val))
                goods.pics = val.split(";");
        }

        public static Map<String, Object> getKeyValues(Goods goods) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, goods.shopId);
            map.put(name, goods.name);
            map.put(excerpt, goods.excerpt);
            map.put(description, goods.description);

            map.put(sku, goods.sku);
            map.put(price, goods.price);
            map.put(salePrice, goods.salePrice);
            map.put(cost, goods.cost);
            map.put(status, goods.status);

            map.put(sectionId1, goods.sectionId1);
            map.put(sectionId2, goods.sectionId2);
            map.put(sectionId3, goods.sectionId3);

            map.put(createTime, new Timestamp(goods.createTime.getTime()));
            map.put(stockTime, new Timestamp(goods.stockTime.getTime()));
            map.put(tags, goods.tags);
            map.put(stocks, goods.stocks);

            map.put(propertyName1, goods.propertyName1);
            map.put(propertyName2, goods.propertyName2);
            map.put(propertyName3, goods.propertyName3);

            String arr = StringUtils.join(goods.pics, ";");
            map.put(pics, arr);
            return map;
        }
    }
    /**
     * 商品id
     */
    private Integer id;

    /**
     * 对应商铺id
     */
    private Integer shopId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 标签(分组)
     */
    private String tags;

    /**
     * 商品简介
     */
    private String excerpt;

    /**
     * 商品介绍
     */
    private String description;

    /**
     * sku
     */
    private String sku;

    /**
     * 商品价格
     */
    private Integer price;

    /**
     * 促销价格
     */
    private Integer salePrice;

    /**
     * 成本价
     */
    private Integer cost;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 商品类目id
     */
    private Integer sectionId1;
    private Integer sectionId2;
    private Integer sectionId3;

    /**
     * 商品创建时间
     */
    private Date createTime;

    /**
     * 商品上架时间
     */
    private Date stockTime;

    /**
     * 商品库存
     */
    private Integer stocks;

    private String propertyName1;
    private String propertyName2;
    private String propertyName3;
    private String[] pics;

    private List<GoodsSpec> specs;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSectionId1() {
        return sectionId1;
    }

    public void setSectionId1(Integer sectionId1) {
        this.sectionId1 = sectionId1;
    }

    public Integer getSectionId2() {
        return sectionId2;
    }

    public void setSectionId2(Integer sectionId2) {
        this.sectionId2 = sectionId2;
    }

    public Integer getSectionId3() {
        return sectionId3;
    }

    public void setSectionId3(Integer sectionId3) {
        this.sectionId3 = sectionId3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStockTime() {
        return stockTime;
    }

    public void setStockTime(Date stockTime) {
        this.stockTime = stockTime;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public String getPropertyName1() {
        return propertyName1;
    }

    public void setPropertyName1(String propertyName1) {
        this.propertyName1 = propertyName1;
    }

    public String getPropertyName2() {
        return propertyName2;
    }

    public void setPropertyName2(String propertyName2) {
        this.propertyName2 = propertyName2;
    }

    public String getPropertyName3() {
        return propertyName3;
    }

    public void setPropertyName3(String propertyName3) {
        this.propertyName3 = propertyName3;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public List<GoodsSpec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<GoodsSpec> specs) {
        this.specs = specs;
    }
}
