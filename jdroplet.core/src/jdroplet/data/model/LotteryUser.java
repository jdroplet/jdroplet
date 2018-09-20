package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/23.
 */
public class LotteryUser extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_lottery_users";

        public static String id = "id";
        public static String activityId = "activity_id";
        public static String senderId = "sender_id";
        public static String userId = "user_id";
        public static String itemId = "item_id";

        public static String name = "name";
        public static String age = "age";
        public static String status = "status";
        public static String createTime = "create_time";
        public static String updateTime = "update_time";

        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, LotteryUser lu) throws SQLException {
            lu.id = rs.getInt(id);
            lu.activityId = rs.getInt(activityId);
            lu.senderId = rs.getInt(senderId);
            lu.userId = rs.getInt(userId);
            lu.itemId = rs.getInt(itemId);

            lu.name = rs.getString(name);
            lu.age = rs.getInt(age);
            lu.status = rs.getInt(status);
            lu.createTime = new Date(rs.getTimestamp(createTime).getTime());
            lu.updateTime = new Date(rs.getTimestamp(updateTime).getTime());
        }

        public static Map<String, Object> getKeyValues(LotteryUser lu) {
            Map<String, Object> map = new HashMap<>();
            map.put(activityId, lu.activityId);
            map.put(senderId, lu.senderId);
            map.put(userId, lu.userId);
            map.put(itemId, lu.itemId);
            map.put(name, lu.name);
            map.put(age, lu.age);
            map.put(status, lu.status);
            map.put(createTime, new Timestamp(lu.createTime.getTime()));
            map.put(updateTime, new Timestamp(lu.updateTime.getTime()));
            return map;
        }
    }

    private Integer id;
    private Integer activityId;
    private Integer senderId;
    private Integer userId;
    private Integer itemId;
    private Integer age;
    private Integer status;
    private String name;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
