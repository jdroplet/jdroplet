package jdroplet.data.model;

import jdroplet.bll.Attributes;
import jdroplet.core.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AttributeValue 实体类
 * 2018-08-30 17:04:38 bourne
 */

public class AttributeValue extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {

        public static String table = "jdroplet_attribute_values";

        public static String id = "id";
        public static String attributeId = "attribute_id";
        public static String itemId = "item_id";
        public static String value = "value";
        public static String slug = "slug";

        public static String[] getColums() {
            return new String[]{id, attributeId, itemId, value, slug};
        }

        public static void fill(HttpRequest req, AttributeValue entity) {
            entity.id = req.getIntParameter(id);
            entity.attributeId = req.getIntParameter(attributeId);
            entity.itemId = req.getIntParameter(itemId);
            entity.value = req.getParameter(value);
            entity.slug = req.getParameter(slug);
        }

        public static void fill(ResultSet rs, AttributeValue entity) throws SQLException {
            entity.id = rs.getInt(id);
            entity.attributeId = rs.getInt(attributeId);
            entity.itemId = rs.getInt(itemId);
            entity.value = rs.getString(value);
            entity.slug = rs.getString(slug);

            entity.attribute = Attributes.getAttribute(entity.attributeId);
        }

        public static Map<String, Object> getKeyValues(AttributeValue entity) {
            Map<String, Object> map = new HashMap<>();
            map.put(id, entity.id);
            map.put(attributeId, entity.attributeId);
            map.put(itemId, entity.itemId);
            map.put(value, entity.value);
            map.put(slug, entity.slug);
            return map;
        }
    }

    //id
    private Integer id;

    /**
     * 属性ID
     */
    private Integer attributeId;

    /**
     * 内容ID
     */
    private Integer itemId;

    /**
     * 值
     */
    private String value;

    //slug
    private String slug;

    private Attribute attribute;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}
