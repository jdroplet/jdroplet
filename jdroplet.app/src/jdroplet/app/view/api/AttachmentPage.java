package jdroplet.app.view.api;

import jdroplet.bll.Attachments;
import jdroplet.bll.Users;
import jdroplet.data.model.Attachment;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/7/1.
 */
public class AttachmentPage extends APIPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer itemId = request.getIntParameter("itemId");
        Integer userId = Users.getCurrentUser().getId();
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        DataSet<Attachment> datas = null;

        datas = Attachments.getAttachments(shopId, userId, itemId, pageIndex, pageSize);
        renderJSON(0, "", datas);
    }
}
