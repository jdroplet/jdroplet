package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IStatisticDataProvider;
import jdroplet.data.model.Statistic;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by kuibo on 2018/3/20.
 */
public class Statistics {

    private static final String KEY_STATS = "STAT_{0}_{1}_{2}_{3}_{4}";

    public static void save(Statistic stat) {
        IStatisticDataProvider provider = DataAccess.instance().getStatisticDataProvider();
        provider.save(stat);
    }

    public static DataSet<Statistic> getStatistics(Integer gruopId, Date dateFrom, Date dateTo, Integer pageIndex, Integer pageSize) {
        DataSet<Statistic> datas = null;
        String key = null;

        key = MessageFormat.format(KEY_STATS, gruopId, dateFrom, dateTo, pageIndex, pageSize);
        if (datas == null) {
            IStatisticDataProvider provider = DataAccess.instance().getStatisticDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (gruopId != null) {
                query = new SearchQuery(Statistic.DataColumns.gruopId, gruopId);
                group_root.addQuery(query);
            }

            if (dateFrom != null) {
                query = new SearchQuery(Statistic.DataColumns.createTime, dateFrom, SearchQuery.GT);
                group_root.addQuery(query);
            }

            if (dateFrom != null) {
                query = new SearchQuery(Statistic.DataColumns.createTime, dateTo, SearchQuery.LT);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Statistic.DataColumns.createTime, SortOrder.DESC, pageIndex, pageSize);
            if (datas.getObjects().size() != 0) {
                RemoteCache.add(key, datas, ICache.DAY_FACTOR);
            }
        }
        return datas;
    }
}
