package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IGroupDataProvider;
import jdroplet.data.idal.IGroupMemberDataProvider;
import jdroplet.data.model.Group;
import jdroplet.data.model.GroupMember;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/4/7.
 */
public class GroupMembers {


    public static String GROUP_KEY_GROUP_MEMBERS = "ROUPMEMBERS_GROUPS_MEMBERS_";

    public static String KEY_GROUPMEMBER = "GOUPMEMBERS_";

    public static String KEY_GROUPMEMBERS = "GROUPMEMBERS_{0}_{1}_{2}_{3}";

    public static Integer save(GroupMember gm) {
        IGroupMemberDataProvider provider = DataAccess.instance().getGroupMemberDataProvider();
        Integer id = provider.save(gm);

        String groupkey = GROUP_KEY_GROUP_MEMBERS + gm.getGroupId();
        RemoteCache.clear(groupkey);
        return id;
    }

    public static Integer getMemberCount(Integer groupId) {
        IGroupMemberDataProvider provider = DataAccess.instance().getGroupMemberDataProvider();
        return provider.getMemberCount(groupId);
    }

    public static Integer getGroupCount(Integer userId) {
        IGroupMemberDataProvider provider = DataAccess.instance().getGroupMemberDataProvider();
        return provider.getGroupCount(userId);
    }

    public static boolean exists(Integer groupId, Integer userId) {
        IGroupMemberDataProvider provider = DataAccess.instance().getGroupMemberDataProvider();
        return provider.exists(groupId, userId);
    }

    public static DataSet<GroupMember> getMembers(Integer groupId, Integer pageIndex, Integer pageSize) {
        DataSet<GroupMember> datas = null;
        String key = null;

        key = MessageFormat.format(KEY_GROUPMEMBERS, groupId, pageIndex, pageSize);
        datas = (DataSet<GroupMember>) RemoteCache.get(key);
        if (datas == null) {
            IGroupMemberDataProvider provider = DataAccess.instance().getGroupMemberDataProvider();
            SearchQuery query = null;
            SearchGroup group_root = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (groupId != null) {
                query = new SearchQuery(GroupMember.DataColumns.groupId, groupId);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Group.DataColumns.createTime, SortOrder.DESC, pageIndex, pageSize);
            if (datas.getObjects().size() != 0) {
                String groupkey = GROUP_KEY_GROUP_MEMBERS + groupId;
                RemoteCache.add(key, groupkey, datas, ICache.DAY_FACTOR);
            }
        }
        return datas;
    }
}
