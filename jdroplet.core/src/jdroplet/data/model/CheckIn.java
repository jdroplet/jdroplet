package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/7/3.
 */
public class CheckIn extends Entity {

    public static class DataColumns {
        public static String table = "jdroplet_checkins";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String userId = "user_id";
        public static String firstDate = "first_date";
        public static String lastDate = "last_date";

        public static String[] getColums() {
            return new String[]{id, shopId, userId, firstDate,
                    lastDate};
        }

        public static void fill(ResultSet rs, CheckIn ci) throws SQLException {
            ci.id = rs.getInt(id);
            ci.shopId = rs.getInt(shopId);
            ci.userId = rs.getInt(userId);
            ci.firstDate = new Date(rs.getTimestamp(firstDate).getTime());
            ci.lastDate = new Date(rs.getTimestamp(lastDate).getTime());
        }

        public static Map<String, Object> getKeyValues(CheckIn ci) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, ci.shopId);
            map.put(userId, ci.userId);
            map.put(userId, ci.userId);

            map.put(firstDate, new Timestamp(ci.firstDate.getTime()));
            map.put(lastDate, new Timestamp(ci.lastDate.getTime()));

            return map;
        }
    }

    private Integer id;
    private Integer shopId;
    private Integer userId;
    private Date firstDate;
    private Date lastDate;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}
