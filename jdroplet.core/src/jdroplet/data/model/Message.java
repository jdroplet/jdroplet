package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/12.
 */
public class Message extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_messages";

        public static String id = "id";
        public static String senderId = "sender_id";
        public static String content = "content";
        public static String postTime = "post_time";


        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, Message msg) throws SQLException {
            msg.id = rs.getInt(id);
            msg.senderId = rs.getInt(senderId);
            msg.content = rs.getString(content);
            msg.postTime = new Date(rs.getTimestamp(postTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Message msg) {
            Map<String, Object> map = new HashMap<>();

            map.put(senderId, msg.senderId);
            map.put(content, msg.content);
            map.put(postTime, new Timestamp(msg.postTime.getTime()));

            return map;
        }
    }

    public enum MessageBoxType {
        Inbox,
        Sentbox
    }

    private Integer id;
    private Integer senderId;
    private String content;
    private Date postTime;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}
