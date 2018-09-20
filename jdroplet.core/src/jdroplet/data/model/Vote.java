package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/26.
 */
public class Vote extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_votes";

        public static String id = "id";
        public static String itemId = "item_id";
        public static String userId = "user_id";
        public static String type = "type";
        public static String createTime = "create_time";

        public static String[] getColums() {
            return new String[]{id, userId, itemId, type,
                    createTime};
        }

        public static void fill(HttpRequest req, Vote vote) {
            vote.id = req.getIntParameter(id);
            vote.userId = req.getIntParameter(userId);
            vote.itemId = req.getIntParameter(itemId);
            vote.type = req.getParameter(type);

            vote.createTime = new Date(req.getDateParameter(createTime, new Date()).getTime());
        }

        public static void fill(ResultSet rs, Vote vote) throws SQLException {
            vote.id = rs.getInt(id);
            vote.userId = rs.getInt(userId);
            vote.itemId = rs.getInt(itemId);
            vote.type = rs.getString(type);

            vote.createTime = new Date(rs.getTimestamp(createTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Vote vote) {
            Map<String, Object> map = new HashMap<>();

            map.put(userId, vote.userId);
            map.put(itemId, vote.itemId);
            map.put(type, vote.type);

            map.put(createTime, new Timestamp(vote.createTime.getTime()));

            return map;
        }
    }

    private Integer id;
    private Integer itemId;
    private Integer userId;
    private String type;
    private Date createTime;

    public Vote() {
    }

    public Vote(Integer userId, Integer itemId, String type) {
        this.userId = userId;
        this.itemId = itemId;
        this.type = type;
        this.createTime = new Date();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
