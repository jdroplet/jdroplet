package jdroplet.data.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Group extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_groups";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String activityId = "activity_id";
        public static String userId = "user_id";
        public static String rank = "rank";
        public static String name = "name";
        public static String description = "description";
        public static String type = "type";
        public static String createTime = "create_time";

        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, Group group) throws SQLException {
            group.id = rs.getInt(id);
            group.activityId = rs.getInt(activityId);
            group.userId = rs.getInt(userId);
            group.shopId = rs.getInt(shopId);
            group.rank = rs.getInt(rank);

            group.name = rs.getString(name);
            group.description = rs.getString(description);
            group.type = rs.getString(type);
            group.createTime = new Date(rs.getTimestamp(createTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Group group) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, group.shopId);
            map.put(activityId, group.activityId);
            map.put(userId, group.userId);
            map.put(name, group.name);
            map.put(rank, group.rank);
            map.put(description, group.description);
            map.put(type, group.type);
            map.put(createTime, new Timestamp(group.createTime.getTime()));

            return map;
        }
    }

    public enum SortGroupBy {
        LAST (0),
        RANK (1);

        private int value;
        SortGroupBy(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SortGroupBy get(int value) {
            return SortGroupBy.values()[value];
        }
    }
    private Integer id;
    private Integer shopId;
    private Integer activityId;
    private Integer userId;
    private Integer rank;

    private String name;
    private String description;
    private String type;
    private Date createTime = new Date();

    @Override
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
