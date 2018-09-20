package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IVisitDataProvider;
import jdroplet.data.model.Visit;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/8/21.
 */
public class Visits {

    public static String KEY_VISITS = "VISITS_{0}_{1}_{2}";
    public static String GROUP_KEY_VISITS = "VISITS_GROUPS_";

    public static void save(Visit visit) {
        IVisitDataProvider provider = DataAccess.instance().getVisitDataProvider();

        provider.save(visit);

        String groupKey = GROUP_KEY_VISITS + visit.getUser();
        RemoteCache.clear(groupKey);
    }

    public static Visit getLast(String user) {
        IVisitDataProvider provider = DataAccess.instance().getVisitDataProvider();

        return provider.getLast(user);
    }

    public static DataSet<Visit> getVisits(String user, Integer pageIndex, Integer pageSize) {
        String key = null;
        DataSet<Visit> datas = null;

        key = MessageFormat.format(KEY_VISITS, user, pageIndex, pageSize);
        datas = (DataSet<Visit>) RemoteCache.get(key);
        if (datas == null) {
            IVisitDataProvider provider = DataAccess.instance().getVisitDataProvider();
            SearchQuery query = null;
            SearchGroup group_root = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (!TextUtils.isEmpty(user)) {
                query = new SearchQuery(Visit.DataColumns.user, user);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root,
                    Visit.DataColumns.id,
                    SortOrder.DESC, pageIndex, pageSize);
            if (datas.getObjects().size() != 0) {
                String groupKey = GROUP_KEY_VISITS + user;
                RemoteCache.add(key, groupKey, datas, ICache.HOUR_FACTOR);
            }
        }

        return datas;
    }
}
