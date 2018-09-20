package jdroplet.data.model;

import jdroplet.core.HttpRequest;
import jdroplet.exceptions.ApplicationException;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/29.
 */
public class Coupon extends Entity {

    public static class DataColumns {
        public static String table = "jdroplet_coupons";

        public static String id = "id";
        public static String userId = "user_id";
        public static String value = "value";
        public static String balance = "balance";
        public static String name = "name";

        public static String type = "type";
        public static String createTime = "create_time";
        public static String expired = "expired";

        public static String[] getColums() {
            return new String[]{id, userId, value, name, balance,
                    type, createTime, expired};
        }

        public static void fill(HttpRequest req, Coupon coupon) {
            coupon.id = req.getBigIntegerParameter(id);
            coupon.userId = req.getIntParameter(userId);
            coupon.value = req.getIntParameter(value);
            coupon.balance = req.getIntParameter(balance);
            coupon.name = req.getParameter(name);

            coupon.type = req.getParameter(type);
            coupon.createTime = new Date(req.getDateParameter(createTime, new Date()).getTime());
            coupon.expired = new Date(req.getDateParameter(expired, new Date()).getTime());
        }

        public static void fill(ResultSet rs, Coupon coupon) throws SQLException {
            coupon.id = (BigInteger) rs.getObject(id);
            coupon.userId = rs.getInt(userId);
            coupon.value = rs.getInt(value);
            coupon.balance = rs.getInt(balance);
            coupon.name = rs.getString(name);

            coupon.createTime = new Date(rs.getTimestamp(createTime).getTime());
            coupon.expired = new Date(rs.getTimestamp(expired).getTime());
            coupon.type = rs.getString(type);
        }

        public static Map<String, Object> getKeyValues(Coupon coupon) {
            Map<String, Object> map = new HashMap<>();

            map.put(id, coupon.id);
            map.put(userId, coupon.userId);
            map.put(balance, coupon.balance);
            map.put(value, coupon.value);
            map.put(name, coupon.name);

            map.put(type, coupon.type);
            map.put(createTime, new Timestamp(coupon.createTime.getTime()));
            map.put(expired, new Timestamp(coupon.expired.getTime()));

            return map;
        }
    }

    private BigInteger id;
    private Integer userId;
    private Integer value;
    private Integer balance;

    private String name;
    private String type;
    private Date createTime;
    private Date expired;

    @Override
    @Deprecated
    public Integer getId() {
        return 0;
    }

    public void setId(Integer id) {
        throw new ApplicationException("Coupon@setId Deprecated");
    }

    public BigInteger getBidId() {
        return id;
    }

    public void setBigId(BigInteger id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
