package jdroplet.app.view.api;

import jdroplet.bll.Logs;
import jdroplet.bll.Users;
import jdroplet.data.model.Log;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/5/9.
 */
public class LogPage extends APIPage {

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        String tag = request.getParameter("tag");
        User user = Users.getCurrentUser();
        DataSet<Log> datas = Logs.getLogs(user.getId(), tag, pageIndex, pageSize);

        renderJSON(0, "", datas);
    }

}
