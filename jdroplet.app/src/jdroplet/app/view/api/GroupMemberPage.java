package jdroplet.app.view.api;

import jdroplet.bll.GroupMembers;
import jdroplet.bll.Groups;
import jdroplet.bll.Users;
import jdroplet.data.model.GroupMember;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/4/7.
 */
public class GroupMemberPage extends APIPage {

    public void join() {
        Integer groupId = request.getIntParameter("groupId");
        Integer inviterId = request.getIntParameter("inviterId");
        Integer userId = Users.getCurrentUser().getId();

        if (Groups.exists(groupId)) {
            renderJSON(Groups.STATUS_GROUP_NOT_FOUND);
        }
    }

    public void list() {
        Integer groupId = request.getIntParameter("groupId");
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        DataSet<GroupMember> datas = null;

        datas = GroupMembers.getMembers(groupId, pageIndex, pageSize);
        renderJSON(0, "", datas);
    }
}
