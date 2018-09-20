package jdroplet.data.model;

import jdroplet.bll.Messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/12.
 */
public class MessagesRecipient extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_messages_recipients";

        public static String id = "id";
        public static String userId = "user_id";
        public static String messageId = "message_id";
        public static String isRead = "is_read";
        public static String status = "status";
        public static String isSender = "is_sender";


        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, MessagesRecipient mr) throws SQLException {
            mr.id = rs.getInt(id);
            mr.userId = rs.getInt(userId);
            mr.messageId = rs.getInt(messageId);
            mr.isRead = rs.getBoolean(isRead);
            mr.status = rs.getInt(status);
            mr.isSender = rs.getBoolean(isSender);

            mr.message = Messages.getMessage(mr.messageId);
        }

        public static Map<String, Object> getKeyValues(MessagesRecipient mr) {
            Map<String, Object> map = new HashMap<>();

            map.put(userId, mr.userId);
            map.put(messageId, mr.messageId);
            map.put(isRead, mr.isRead);
            map.put(status, mr.status);
            map.put(isSender, mr.isSender);

            return map;
        }
    }

    private Integer id;
    private Integer userId;
    private Integer messageId;
    private Integer status;
    private boolean isRead;
    private boolean isSender;
    private Message message;

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

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public Message getMessage() {
        return message;
    }
}
