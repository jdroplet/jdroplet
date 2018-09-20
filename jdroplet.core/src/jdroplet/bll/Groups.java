package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IGroupDataProvider;
import jdroplet.data.model.Group;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.text.MessageFormat;

public class Groups {

    public static final int STATUS_GROUP_NOT_FOUND = 1000100;

    public static String GROUP_KEY_GROUPS = "GROUPS_GROUPS_";

    public static String KEY_GROUP = "GROUP_";

    public static String KEY_GROUPS = "GROUPS_{0}_{1}_{2}_{3}_{4}_{5}";


    public static boolean exists(Integer id) {
        IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();
        return provider.exists(id);
    }

    public static Integer save(Group group) {
        IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();
        Integer id = null;

        id = provider.save(group);
        if (group.getId() == null || group.getId() == 0)
            group.setId(id);
        return id;
    }

    public static Integer getCount(Integer activityId, Integer userId) {
        IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();
        return provider.getCount(activityId, userId);
    }

    public static Group getGroup(Integer id) {
        IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();
        Group group = (Group) provider.getEntity(id);
        return group;
    }

    public static void updateRank(Integer id, Integer rank) {
        IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();

        provider.update("id", id, "status", rank);
    }

    public static DataSet<Group> getGroups(Integer shopId, Integer activityId, Integer userId, String type, Group.SortGroupBy sortBy, SortOrder sortOrder, Integer pageIndex, Integer pageSize) {
        DataSet<Group> datas = null;
        String key = null;

        key = MessageFormat.format(KEY_GROUPS, activityId, userId, pageIndex, pageSize);
        datas = (DataSet<Group>) RemoteCache.get(key);
        if (datas == null) {
            IGroupDataProvider provider = DataAccess.instance().getGroupDataProvider();
            SearchQuery query = null;
            SearchGroup group_root = null;
            String sortField = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (shopId != null) {
                query = new SearchQuery(Group.DataColumns.shopId, shopId);
                group_root.addQuery(query);
            }

            if (activityId != null) {
                query = new SearchQuery(Group.DataColumns.activityId, activityId);
                group_root.addQuery(query);
            }

            if (userId != null) {
                query = new SearchQuery(Group.DataColumns.userId, userId);
                group_root.addQuery(query);
            }


            if (type != null) {
                query = new SearchQuery(Group.DataColumns.type, type);
                group_root.addQuery(query);
            }

            if (sortBy == Group.SortGroupBy.RANK)
                sortField = Group.DataColumns.rank;
            else
                sortField = Group.DataColumns.id;

            datas = (DataSet) provider.search(group_root, sortField, sortOrder, pageIndex, pageSize);
            if (datas.getObjects().size() != 0) {
                String groupkey = GROUP_KEY_GROUPS + activityId;
                RemoteCache.add(key, groupkey, datas, ICache.HOUR_FACTOR);
            }
        }
        return datas;
    }
}
