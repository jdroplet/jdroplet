package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jdroplet.sns.proxy.WechatAppProxy;
import jdroplet.sns.proxy.WechatProxy;
import jdroplet.sns.proxy.WechatQProxy;

/**
 * Created by kuibo on 2018/1/20.
 */
public class Shop extends Meta {


    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_shops";

        public static String id = "id";
        public static String name = "name";
        public static String icon = "icon";
        public static String userId = "user_id";
        public static String status = "status";
        public static String type = "type";
        public static String phone = "phone";
        public static String address = "address";
        public static String createTime = "create_time";
        public static String description = "description";

        public static String[] getColums() {
            return new String[]{id, name, status, icon, phone, address,
                    userId, createTime, description, type};
        }

        public static void fill(HttpRequest req, Shop shop) {
            shop.setId(req.getIntParameter(id));
            shop.setStatus(req.getBooleanParameter(status) ? 1 : 0);
            shop.setIcon(req.getParameter(icon));
            shop.setName(req.getParameter(name));
            shop.setDescription(req.getParameter(description));
            shop.type = req.getParameter("type");

            shop.setCreateTime(new Date(req.getDateParameter(createTime, new Date()).getTime()));
            shop.phone = req.getParameter(phone);
            shop.address = req.getParameter(address);

            // set meta
            shop.setValue(WechatProxy.APP_ID, req.getParameter(WechatProxy.APP_ID));
            shop.setValue(WechatProxy.APP_SECRET, req.getParameter(WechatProxy.APP_SECRET));

            shop.setValue(WechatAppProxy.APP_ID, req.getParameter(WechatAppProxy.APP_ID));
            shop.setValue(WechatAppProxy.APP_SECRET, req.getParameter(WechatAppProxy.APP_SECRET));

            shop.setValue(WechatQProxy.APP_ID, req.getParameter(WechatQProxy.APP_ID));
            shop.setValue(WechatQProxy.APP_SECRET, req.getParameter(WechatQProxy.APP_SECRET));

            shop.setValue("weibo_appid", req.getParameter("weibo_appid"));
            shop.setValue("weibo_appsecret", req.getParameter("weibo_appsecret"));

            shop.setValue("qq_appid", req.getParameter("qq_appid"));
            shop.setValue("qq_appsecret", req.getParameter("qq_appsecret"));

            shop.setValue("wechat_sub_token", req.getParameter("wechat_sub_token"));
            // pay
            shop.setValue("wxpay_app_id", req.getParameter("wxpay_app_id"));
            shop.setValue("wxpay_key", req.getParameter("wxpay_key"));
            shop.setValue("wxpay_mchid", req.getParameter("wxpay_mchid"));
            shop.setValue("wxpay_secret", req.getParameter("wxpay_secret"));

            shop.setValue("alipay_id", req.getParameter("alipay_id"));
            shop.setValue("alipay_pubkey", req.getParameter("alipay_pubkey"));
            shop.setValue("alipay_prikey", req.getParameter("alipay_prikey"));
        }

        public static void fill(ResultSet rs, Shop shop) throws SQLException {
            shop.setId(rs.getInt(id));
            shop.setStatus(rs.getInt(status));
            shop.setIcon(rs.getString(icon));
            shop.setName(rs.getString(name));
            shop.setDescription(rs.getString(description));

            shop.userId = rs.getInt(userId);
            shop.type = rs.getString(type);
            shop.setCreateTime(new Date(rs.getTimestamp(createTime).getTime()));
            shop.phone = rs.getString(phone);
            shop.address = rs.getString(address);
        }

        public static Map<String, Object> getKeyValues(Shop shop) {
            Map<String, Object> map = new HashMap<>();

            map.put(name, shop.getName());
            map.put(icon, shop.getIcon());
            map.put(status, shop.getStatus());
            map.put(createTime, new Timestamp(shop.getCreateTime().getTime()));
            map.put(description, shop.getDescription());
            map.put(userId, shop.userId);
            map.put(type, shop.type);
            map.put(phone, shop.phone);
            map.put(address, shop.address);
            return map;
        }
    }

    private Integer id;
    private Integer userId;
    private Integer status;
    private String phone;
    private String address;
    private String name;
    private String icon;
    private String type;
    private String description;
    private Date createTime;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
