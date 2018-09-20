package jdroplet.app.view.api;

import jdroplet.bll.Groups;
import jdroplet.data.model.Group;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/4/7.
 */
public class GroupPage extends APIPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        String type = request.getParameter("type");
        DataSet<Group> datas = null;

        datas = Groups.getGroups(shopId, activityId, null, type, Group.SortGroupBy.LAST, SortOrder.ASC, pageIndex, pageSize);
        renderJSON(0, null, datas);
    }
}
