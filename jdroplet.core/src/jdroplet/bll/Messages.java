package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IMessageDataProvider;
import jdroplet.data.model.Message;
import jdroplet.data.model.MessagesRecipient;

import java.util.List;

/**
 * Created by kuibo on 2018/2/12.
 */
public class Messages {

    public static void send(Message msg, Integer senderId, Integer[] recvUserIds) {
        Integer id = save(msg);
        MessagesRecipient mr = null;

        if (senderId != 0) {
            mr = new MessagesRecipient();
            mr.setMessageId(id);
            mr.setRead(false);
            mr.setSender(true);
            mr.setUserId(senderId);
            mr.setStatus(0);
            MessagesRecipients.save(mr);
        }

        for(Integer userId:recvUserIds) {
            mr = new MessagesRecipient();
            mr.setMessageId(id);
            mr.setRead(false);
            mr.setSender(false);
            mr.setUserId(userId);
            mr.setStatus(0);
            MessagesRecipients.save(mr);
        }
    }

    public static Integer save(Message msg) {
        IMessageDataProvider provider = DataAccess.instance().getMessageDataProvider();
        String key = null;

        Integer id = null;

        id = provider.save(msg);
        if (msg.getId() == null)
            msg.setId(id);

        return id;
    }

    public static Message getMessage(Integer id) {
        IMessageDataProvider provider = DataAccess.instance().getMessageDataProvider();
        return (Message) provider.getEntity(id);
    }
}
