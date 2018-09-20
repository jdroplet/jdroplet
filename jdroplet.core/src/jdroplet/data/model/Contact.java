package jdroplet.data.model;

import jdroplet.core.HttpRequest;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Contact extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_contacts";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String activityId = "activity_id";
        public static String userId = "user_id";
        public static String userName = "user_name";
        public static String area = "area";
        public static String code = "code";
        public static String phone = "phone";
        public static String address = "address";
        public static String isDefault = "is_default";
        public static String mail = "mail";
        public static String remark = "remark";

        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, Contact ct) throws SQLException {
            ct.id = rs.getInt(id);
            ct.shopId = rs.getInt(shopId);
            ct.activityId = rs.getInt(activityId);
            ct.userId = rs.getInt(userId);
            ct.userName = rs.getString(userName);

            ct.area = rs.getString(area);
            ct.code = rs.getString(code);
            ct.phone = rs.getString(phone);
            ct.address = rs.getString(address);
            ct.isDefault = rs.getBoolean(isDefault);

            ct.mail = rs.getString(mail);
            ct.remark = rs.getString(remark);
        }

        public static void fill(HttpRequest req, Contact ct) {
            ct.id = req.getIntParameter(id);
            ct.shopId = req.getIntParameter(shopId);
            ct.activityId = req.getIntParameter(activityId, 0);
            ct.userId = req.getIntParameter(userId);
            ct.userName = req.getParameter(userName);

            ct.area = StringUtils.join(req.getParameterValues(area), " ");
            ct.code = req.getParameter(code);
            ct.phone = req.getParameter(phone);
            ct.address = req.getParameter(address);
            ct.isDefault = req.getBooleanParameter(isDefault);

            ct.mail = req.getParameter(mail);
            ct.remark = req.getParameter(remark);
        }

        public static Map<String, Object> getKeyValues(Contact ct) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, ct.shopId);
            map.put(activityId, ct.activityId);
            map.put(userId, ct.userId);
            map.put(userName, ct.userName);

            map.put(area, ct.area);
            map.put(code, ct.code);
            map.put(phone, ct.phone);
            map.put(address, ct.address);
            map.put(isDefault, ct.isDefault);
            map.put(mail, ct.mail);
            map.put(remark, ct.remark);

            return map;
        }
    }

    private Integer id;
    private Integer shopId;
    private Integer activityId;
    private Integer userId;
    private Boolean isDefault;
    private String userName;
    private String area = "   ";
    private String code;
    private String phone;
    private String address;
    private String mail;
    private String remark;

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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        isDefault = isDefault;
    }
}
