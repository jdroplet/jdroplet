package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ILogDataProvider;
import jdroplet.data.idal.ILogDataProvider;
import jdroplet.data.model.Log;
import jdroplet.data.model.Log;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.util.Date;

/**
 * Created by kuibo on 2018/3/16.
 */
public class Logs {

    public static void save(String tag, String content) {
        save(Users.getCurrentUser().getId(), tag, content);
    }

    public static void save(Integer userId, String tag, String content) {
        Log log = new Log();

        log.setUserId(userId);
        log.setTag(tag);
        log.setContent(content);
        log.setCreateTime(new Date());

        save(log);
    }

    public static Integer save(Log log) {
        ILogDataProvider provider = DataAccess.instance().getLogDataProvider();
        Integer id = null;

        id = provider.save(log);
        if (log.getId() == null)
            log.setId(id);

        return id;
    }

    public static DataSet<Log> getLogs(Integer userId, String tag, Integer pageIndex, Integer pageSize) {
        DataSet<Log> datas = null;

        if (datas == null) {
            ILogDataProvider provider = (ILogDataProvider) DataAccess.instance().createProvider("LogDataProvider");
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (userId != null && userId != 0) {
                query = new SearchQuery(Log.DataColumns.userId, userId);
                group_root.addQuery(query);
            }

            if (TextUtils.isEmpty(tag)) {
                query = new SearchQuery(Log.DataColumns.tag, tag);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Log.DataColumns.id, SortOrder.DESC, pageIndex, pageSize);
        }
        return datas;
    }
}
