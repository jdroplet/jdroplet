package jdroplet.app.view.api.game;

import jdroplet.app.view.api.APIPage;
import jdroplet.bll.*;
import jdroplet.core.DateTime;
import jdroplet.data.model.*;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;

import java.util.*;

/**
 * Created by kuibo on 2018/4/8.
 */
public class InvitePage extends APIPage {

    // 过期
    public static final Integer OUT_OF_DATE = 1001000;
    // 成员数超
    public static final Integer OUT_OF_MEMBERS = 1001001;
    // 加入数超
    public static final Integer OUT_OF_JOINS = 1001002;
    // 创建数数超
    public static final Integer OUT_OF_CREATE = 1001003;
    // 无效的用户所有者
    public static final Integer INVALID_GROUP_OWNER = 1001004;
    // 不符合条件
    public static final Integer UNMATCHED = 1001005;
    //
    public static final Integer NOT_ALLOW_UPDATE_CONTACT = 1001006;

    public void get() {
        Integer activityId = request.getIntParameter("id");
        Integer userId = Users.getCurrentUser().getId();
        DataSet<Group> datas = Groups.getGroups(1, activityId, userId, "game", Group.SortGroupBy.LAST, SortOrder.DESC, 1, 10);
        Activity act = Activities.getActivity(activityId);

        act.getInt("inviteMaxJoinCount");

        act.setValue("groups", datas);
        act.setValue("allowCreateGroup", Groups.getCount(activityId, userId) < act.getInt("inviteMaxCreateCount"));
        act.setValue("userJoinCount", GroupMembers.getGroupCount(userId));

        Integer size = act.getInt("item_size");
        Integer missions[] = new Integer[size];
        for (int i = 0; i < size; i++) {
            missions[i] = act.getInt("activity_invite_mission_" + i);
        }
        act.setValue("missions", missions);
        renderJSON(0, "", act);
    }

    /**
     * 创建一个组
     */
    public void create() {
        Integer activityId = request.getIntParameter("activityId");
        Integer inviterId = request.getIntParameter("inviterId");
        Activity activity = Activities.getActivity(activityId);
        Group group = null;
        User user = Users.getCurrentUser();

        if (DateTime.diff(activity.getExpired(), new Date()) < 0) {
            renderJSON(OUT_OF_DATE);
            return;
        } else if (Groups.getCount(activityId, user.getId()) >= activity.getInt("inviteMaxCreateCount")) {
            DataSet<Group> datas = Groups.getGroups(1, activityId, user.getId(), "game", Group.SortGroupBy.LAST, SortOrder.DESC, 1, 10);
            renderJSON(OUT_OF_CREATE, "", datas);
            return;
        }
        user = Users.getCurrentUser();
        group = new Group();
        group.setActivityId(activityId);
        group.setUserId(user.getId());
        group.setName(user.getDisplayName());
        group.setRank(0);
        Groups.save(group);

        renderJSON(0, "", group);
    }

    public void join() {
        Integer groupId = request.getIntParameter("groupId");
        Integer inviterId = request.getIntParameter("inviterId");
        Group group = Groups.getGroup(groupId);
        Activity activity = Activities.getActivity(group.getActivityId());
        User user = Users.getCurrentUser();

        if (DateTime.diff(activity.getExpired(), new Date()) < 0) {
            renderJSON(OUT_OF_DATE);
            return;
        } else if (activity.getInt("inviteMaxMembers") <= GroupMembers.getMemberCount(group.getId())) {
            // 已经加入的成员 大于 最大允许加入的
            renderJSON(OUT_OF_MEMBERS);
            return;
        } else if (activity.getInt("inviteMaxJoinCount") <= GroupMembers.getGroupCount(user.getId())) {
            // 当前用户已经加入的数量 大于 最大允许加入的数量
            renderJSON(OUT_OF_JOINS);
            return;
        } else if (GroupMembers.exists(groupId, user.getId())) {
            // 已经加入该组
            renderJSON(4);
            return;
        }

        GroupMember gm = new GroupMember();
        gm.setActivityId(activity.getId());
        gm.setGroupId(groupId);
        gm.setUserId(user.getId());
        gm.setAdmin(false);
        gm.setInviterId(inviterId);
        gm.setCreateTime(new Date());
        GroupMembers.save(gm);

        group.setRank(group.getRank() + 1);
        Groups.updateRank(groupId, group.getRank());

        User gmu = Users.getUser(gm.getUserId());
        Map<String, Object> info = new HashMap<>();
        info.put("name", gmu.getDisplayName());
        info.put("avatar", gmu.getAvatar());
        gm.setValue("member", info);
        renderJSON(0, "", gm);
    }

    public void rank() {
        Integer activityId = request.getIntParameter("activityId");
        DataSet<Group> datas = Groups.getGroups(1, activityId, null, "game", Group.SortGroupBy.RANK, SortOrder.DESC, 1, 10);

        for(Group g:datas.getObjects()) {
            User gu = Users.getUser(g.getUserId());

            g.setValue("avatar", gu.getAvatar());
        }
        renderJSON(0, "", datas);
    }

    public void group() {
        Integer groupId = request.getIntParameter("groupId");
        Group group = null;

        group = Groups.getGroup(groupId);
        User user = Users.getCurrentUser();
        Activity activity = Activities.getActivity(group.getActivityId());

        // 最大成员数
        Integer maxMembers = activity.getInt("inviteMaxMembers");
        group.setValue("maxMembers", maxMembers);

        // 已经加入的成员数量
        Integer memberCount = GroupMembers.getMemberCount(group.getId());
        group.setValue("memberCount", memberCount);


        // 当前用户是否所有者
        boolean isOwner = group.getUserId().equals(user.getId());
        group.setValue("isOwner", isOwner);
        // 当前用户是否已经达成条件
        if (isOwner) {
            // 是否达成
            group.setValue("isComplete", (memberCount >= maxMembers));
        }

        // 当前组的成员
        List<Map> members = new ArrayList<>();
        Map<String, Object> info = null;
        DataSet<GroupMember> datas = GroupMembers.getMembers(group.getId(), 1, 128);
        for(GroupMember gm:datas.getObjects()) {
            User gmu = Users.getUser(gm.getUserId());

            info = new HashMap<>();
            info.put("name", gmu.getDisplayName());
            info.put("avatar", gmu.getAvatar());

            members.add(info);
        }
        group.setValue("members", members);


        // 最多允许加入的数量
        Integer maxJoinCount = activity.getInt("inviteMaxJoinCount");
        group.setValue("maxJoinCount", maxJoinCount);

        // 用户加入的组数量
        Integer userJoinCount = GroupMembers.getGroupCount(user.getId());
        group.setValue("userJoinCount", userJoinCount);


        // 是否允许加入
        boolean allowJoin = (memberCount < maxMembers) && (userJoinCount < maxJoinCount) && !isOwner;
        group.setValue("allowJoin", allowJoin);

        renderJSON(0, null, group);
    }

    public void save() {
        Integer shopId = null;
        Integer activityId = request.getIntParameter("activityId");
        Integer groupId = request.getIntParameter("groupId");
        Activity act = Activities.getActivity(activityId);
        Group group = Groups.getGroup(groupId);
        User user = Users.getCurrentUser();

        shopId = act.getShopId();
        if (Contacts.exists(shopId, activityId, user.getId())) {
            renderJSON(NOT_ALLOW_UPDATE_CONTACT);
            return;
        }
        if (!group.getUserId().equals(user.getId())) {
            renderJSON(INVALID_GROUP_OWNER);
            return;
        }
        Integer size = act.getInt("item_size");
        Integer members = GroupMembers.getMemberCount(groupId);
        boolean canSave = false;
        for (int i = 0; i < size; i++) {
            if (members > act.getInt("activity_invite_mission_" + i) ) {
                canSave = true;
                break;
            }
        }
        if (canSave) {
            Contact contact = new Contact();
            String userName = request.getParameter("userName");
            String phone = request.getParameter("phone");

            contact.setShopId(shopId);
            contact.setActivityId(activityId);
            contact.setUserId(user.getId());
            contact.setUserName(userName);
            contact.setPhone(phone);
            contact.setRemark(group.getName());
            Contacts.save(contact);

            renderJSON(0);
        } else {
            renderJSON(UNMATCHED);
            return;
        }
    }
}
