package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IActivityDataProvider;
import jdroplet.data.model.Activity;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/1/24.
 */
public class Activities {

    public static String GROUP_KEY_ACTIVITYS = "ACTIVITYS_GROUPS_";

    public static String KEY_ACTIVITYS = "ACTIVITY_ACTIVITYS_ui:{0}_s:{1}_t:{2}_s:{3}_pi:{4}_ps:{5}";
    public static String KEY_ACTIVITY = "ACTIVITY_";

    public static Integer save(Activity activity) {
        IActivityDataProvider provider = (IActivityDataProvider) DataAccess.instance().createProvider("ActivityDataProvider");
        Integer id = null;

        id = provider.save(activity);
        if (activity.getId() == null || activity.getId() == 0)
            activity.setId(id);
        Metas.save(activity);

        String groupKey = GROUP_KEY_ACTIVITYS + activity.getShopId();
        RemoteCache.clear(groupKey);
        return id;
    }

    public static Activity getActivity(Integer id) {
        IActivityDataProvider provider = (IActivityDataProvider) DataAccess.instance().createProvider("ActivityDataProvider");
        return (Activity) provider.getEntity(id);
    }

    public static DataSet<Activity> getActivities(Integer shopId, String searchTerms, Integer pageIndex, Integer pageSize) {
        return getActivities(shopId, null, null, searchTerms, pageIndex, pageSize, true, false);
    }

    public static DataSet<Activity> getActivities(Integer shopId, Integer status, String type, String searchTerms,
                                                  Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
        String key = null;
        DataSet<Activity> datas = null;

        key = MessageFormat.format(KEY_ACTIVITYS, shopId, status, type, searchTerms, pageIndex, pageSize);

        if (flush || !cacheable)
            RemoteCache.remove(key);
        else
            datas = (DataSet<Activity>) RemoteCache.get(key);

        if (datas == null) {
            IActivityDataProvider provider = (IActivityDataProvider) DataAccess.instance().createProvider("ActivityDataProvider");
            SearchQuery query = null;
            SearchGroup group_root = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            query = new SearchQuery(Activity.DataColumns.shopId, shopId, SearchQuery.EQ);
            group_root.addQuery(query);

            if (status != null) {
                query = new SearchQuery(Activity.DataColumns.status, status, SearchQuery.AND);
                group_root.addQuery(query);
            }

            if (!TextUtils.isEmpty(type)) {
                query = new SearchQuery(Activity.DataColumns.type, type, SearchQuery.AND);
                group_root.addQuery(query);
            }

            if (!TextUtils.isEmpty(searchTerms)) {
                query = new SearchQuery(Activity.DataColumns.name, searchTerms, SearchQuery.LIKE);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Activity.DataColumns.id, SortOrder.ASC, pageIndex, pageSize);
            if (cacheable && datas.getObjects().size() != 0) {
                String groupKey = GROUP_KEY_ACTIVITYS + shopId;
                RemoteCache.add(key, groupKey, datas, ICache.DAY_FACTOR);
            }
        }
        return datas;
    }
}
