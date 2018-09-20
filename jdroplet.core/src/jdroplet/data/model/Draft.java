package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/5/6.
 */
public class Draft extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_drafts";

        public static String id = "id";
        public static String userId = "user_id";
        public static String itemId = "item_id";

        public static String type = "type";
        public static String content = "content";
        public static String createTime = "create_time";
        public static String updateTime = "update_time";


        public static String[] getColums() {
            return new String[]{id, userId, itemId, content,type,
                    createTime, updateTime};
        }

        public static void fill(ResultSet rs, Draft draft) throws SQLException {
            Timestamp ts = null;

            draft.setId(rs.getInt(id));
            draft.setUserId(rs.getInt(userId));
            draft.setItemId(rs.getInt(itemId));
            draft.setType(rs.getString(type));
            draft.setContent(rs.getString(content));

            ts = rs.getTimestamp(createTime);
            if (ts != null) draft.setCreateTime(new Date(ts.getTime()));

            ts = rs.getTimestamp(updateTime);
            if (ts != null) draft.setUpdateTime(new Date(ts.getTime()));
        }

        public static Map<String, Object> getKeyValues(Draft draft) {
            Map<String, Object> map = new HashMap<>();

            map.put(userId, draft.getUserId());
            map.put(itemId, draft.getItemId());
            map.put(content, draft.getContent());
            map.put(type, draft.getType());

            if (draft.getCreateTime() != null) map.put(createTime, new Timestamp(draft.getCreateTime().getTime()));
            if (draft.getUpdateTime() != null) map.put(updateTime, new Timestamp(draft.getUpdateTime().getTime()));

            return map;
        }
    }


    private Integer id;
    private Integer userId;
    private Integer itemId;
    private String type;
    private String content;
    private Date createTime;
    private Date updateTime;

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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
