package jdroplet.app.view.api;

import jdroplet.bll.MessagesRecipients;
import jdroplet.bll.Posts;
import jdroplet.bll.Users;
import jdroplet.data.model.Message;
import jdroplet.data.model.MessagesRecipient;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/2/13.
 */
public class MessagePage extends APIPage {

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        User user = Users.getCurrentUser();
        DataSet<MessagesRecipient> datas = null;

        datas = MessagesRecipients.getMessagesRecipients(user.getId(), Message.MessageBoxType.Inbox, pageIndex, pageSize);
        renderJSON(0, "", datas);
    }

    public void unreads() {
        User user = Users.getCurrentUser();
        Integer count = MessagesRecipients.getUnReads(user.getId());
        renderJSON(0, "", count);
    }

    public void reset() {
        String[] fields = request.getParameter("fields").split("-");
        for(String fiel:fields) {
            if ("read".equals(fiel)) {
                Integer[] ids = request.getIntParameterValues("id[]");
                boolean isRead = request.getBooleanParameter("read");

                if (ids == null) {
                    Integer id = request.getIntParameter("id");
                    MessagesRecipients.updateRead(id, isRead);
                } else {
                    for(Integer id:ids) {
                        MessagesRecipients.updateRead(id, isRead);
                    }
                }
            }
        }
        renderJSON(0);
    }
}
