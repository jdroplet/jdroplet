package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/3/16.
 */
public class Log extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_logs";

        public static String id = "id";
        public static String userId = "user_id";
        public static String tag = "tag";
        public static String content = "content";
        public static String createTime = "create_time";


        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, Log log) throws SQLException {
            log.id = rs.getInt(id);
            log.userId = rs.getInt(userId);
            log.tag = rs.getString(tag);
            log.content = rs.getString(content);
            log.createTime = new Date(rs.getTimestamp(createTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Log log) {
            Map<String, Object> map = new HashMap<>();

            map.put(userId, log.userId);
            map.put(tag, log.tag);
            map.put(content, log.content);
            map.put(createTime, new Timestamp(log.createTime.getTime()));

            return map;
        }
    }

    private Integer id;
    private Integer userId;
    private String tag;
    private String content;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
}
