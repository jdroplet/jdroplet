package jdroplet.data.idal;

import jdroplet.data.model.Message;
import jdroplet.data.model.MessagesRecipient;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/2/12.
 */
public interface IMessagesRecipientDataProvider extends IDataProvider {

    DataSet<MessagesRecipient> getMessagesRecipients(Integer userId, Message.MessageBoxType type, Integer pageIndex, Integer pageSize);

    Integer getUnReads(Integer userId);
}
