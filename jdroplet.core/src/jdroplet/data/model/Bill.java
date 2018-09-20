package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.math.BigDecimal;

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
public class Bill extends Entity {

    public static class DataColumns {
        public static String table = "jdroplet_bills";

        public static String id = "id";
        public static String userId = "user_id";
        public static String fee = "fee";
        public static String balance = "balance";

        public static String couponValues = "coupon_values";
        public static String type = "type";
        public static String description = "description";
        public static String createTime = "create_time";

        public static String tranNo = "tran_no";
        public static String orderId = "order_id";
        public static String itemId = "item_id";

        public static String[] getColums() {
            return new String[]{id, userId, fee, balance,
                    couponValues, type, description, createTime,
                    tranNo, orderId, itemId};
        }

        public static void fill(HttpRequest req, Bill bill) {
            bill.id = req.getParameter(id);
            bill.userId = req.getIntParameter(userId);
            bill.fee = req.getIntParameter(fee);
            bill.type = req.getIntParameter(type);
            bill.balance = req.getIntParameter(balance);

            bill.couponValues = req.getIntParameter(couponValues);
            bill.description = req.getParameter(description);
            bill.createTime = new Date();

            bill.tranNo = req.getBigIntegerParameter(tranNo);
            bill.orderId = req.getBigIntegerParameter(orderId);
            bill.itemId = req.getBigIntegerParameter(itemId);
        }

        public static void fill(ResultSet rs, Bill bill) throws SQLException {
            bill.id = rs.getString(id);
            bill.userId = rs.getInt(userId);
            bill.fee = rs.getInt(fee);
            bill.type = rs.getInt(type);
            bill.balance = rs.getInt(balance);

            bill.couponValues = rs.getInt(couponValues);
            bill.description = rs.getString(description);
            bill.createTime = new Date(rs.getTimestamp(createTime).getTime());

            bill.tranNo = (BigInteger) rs.getObject(tranNo);
            bill.orderId = (BigInteger) rs.getObject(orderId);
            bill.itemId = (BigInteger) rs.getObject(itemId);
        }

        public static Map<String, Object> getKeyValues(Bill bill) {
            Map<String, Object> map = new HashMap<>();

            map.put(id, bill.id);
            map.put(userId, bill.userId);
            map.put(fee, bill.fee);
            map.put(balance, bill.balance);

            map.put(couponValues, bill.couponValues);
            map.put(type, bill.type);
            map.put(description, bill.description);
            map.put(createTime, new Timestamp(bill.createTime.getTime()));

            map.put(tranNo, bill.tranNo);
            map.put(orderId, bill.orderId);
            map.put(itemId, bill.itemId);

            return map;
        }
    }

    public static final int RECHARGE = 1;
    public static final int COIN = 2;
    public static final int COUPON = 4;

    private String id;
    private Integer userId;
    private Integer fee;
    private Integer type;
    private Integer balance;
    private Integer couponValues;

    private String description;
    private Date createTime;

    private BigInteger tranNo;
    private BigInteger orderId;
    private BigInteger itemId;

    @Override
    public Integer getId() {
        return 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public BigInteger getTranNo() {
        return tranNo;
    }

    public void setTranNo(BigInteger tranNo) {
        this.tranNo = tranNo;
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public BigInteger getItemId() {
        return itemId;
    }

    public void setItemId(BigInteger itemId) {
        this.itemId = itemId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getCouponValues() {
        return couponValues;
    }

    public void setCouponValues(Integer couponValues) {
        this.couponValues = couponValues;
    }
}
