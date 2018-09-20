package jdroplet.bll;

import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IMessagesRecipientDataProvider;
import jdroplet.data.model.Message;
import jdroplet.data.model.MessagesRecipient;
import jdroplet.util.DataSet;

import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/2/12.
 */
public class MessagesRecipients {

    public static String KEY_MESSAGES = "MESSAGE_u:{0}_t:{1}_pi:{2}_ps:{3}";


    public static Integer save(MessagesRecipient mr) {
        IMessagesRecipientDataProvider provider = DataAccess.instance().getMessagesRecipientDataProvider();
        String key = null;

        Integer id = null;

        id = provider.save(mr);
        if (mr.getId() == null)
            mr.setId(id);

        return id;
    }

    public static Integer getUnReads(Integer userId) {
        IMessagesRecipientDataProvider provider = DataAccess.instance().getMessagesRecipientDataProvider();

        return provider.getUnReads(userId);
    }

    public static DataSet<MessagesRecipient> getMessagesRecipients(Integer userId, Message.MessageBoxType type, Integer pageIndex, Integer pageSize) {
        return getMessagesRecipients(userId, type, pageIndex, pageSize, true, false);
    }

    public static DataSet<MessagesRecipient> getMessagesRecipients(Integer userId, Message.MessageBoxType type, Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
        DataSet<MessagesRecipient> datas = null;
        String key = null;

        key = MessageFormat.format(KEY_MESSAGES, userId, type, pageIndex, pageSize);

        if (flush || !cacheable)
            RemoteCache.remove(key);
        else
            datas = (DataSet<MessagesRecipient>) RemoteCache.get(key);

        if (datas == null) {
            IMessagesRecipientDataProvider provider = DataAccess.instance().getMessagesRecipientDataProvider();

            datas = provider.getMessagesRecipients(userId, type, pageIndex, pageSize);
        }
        return datas;
    }

    public static void updateRead(Integer id, boolean readed) {
        IMessagesRecipientDataProvider provider = DataAccess.instance().getMessagesRecipientDataProvider();

        provider.update("id", id, "is_read", readed);
    }
}
