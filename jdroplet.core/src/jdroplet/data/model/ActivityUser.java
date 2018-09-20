package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/24.
 */
public class ActivityUser extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_activity_users";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String activityId = "activity_id";
        public static String userId = "user_id";
        public static String count = "count";
        public static String updateTime = "update_time";
        public static String createTime = "create_time";


        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(HttpRequest req, ActivityUser au) {
            au.id = req.getIntParameter(id);
            au.shopId = req.getIntParameter(shopId);
            au.activityId = req.getIntParameter(activityId);

            au.count = req.getIntParameter(count);
        }

        public static void fill(ResultSet rs, ActivityUser au) throws SQLException {
            au.id = rs.getInt(id);
            au.shopId = rs.getInt(shopId);
            au.activityId = rs.getInt(activityId);
            au.userId = rs.getInt(userId);

            au.count = rs.getInt(count);
            au.createTime = new Date(rs.getTimestamp(createTime).getTime());
            au.updateTime = new Date(rs.getTimestamp(updateTime).getTime());
        }

        public static Map<String, Object> getKeyValues(ActivityUser au) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, au.shopId);
            map.put(activityId, au.activityId);
            map.put(userId, au.userId);

            map.put(count, au.count);
            map.put(createTime, new Timestamp(au.createTime.getTime()));
            map.put(updateTime, new Timestamp(au.updateTime.getTime()));
            return map;
        }
    }

    private Integer id;
    private Integer shopId;
    private Integer activityId;
    private Integer userId;
    private Integer count;
    private Date updateTime = new Date();
    private Date createTime = new Date();

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
