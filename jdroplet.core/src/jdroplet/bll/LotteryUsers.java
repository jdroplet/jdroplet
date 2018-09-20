package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ILotteryUserDataProvider;
import jdroplet.data.model.Lottery;
import jdroplet.data.model.LotteryUser;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/4/23.
 */
public class LotteryUsers {
    public static String GROUP_KEY_LOTTERY_USERS = "LOTTERY_USERS_{0}_{1}";

    public static String KEY_LOTTERY_USERS = "LOTTERY_USERS_{0}_{1}_{2}_{3}_{4}";

    public static void save(LotteryUser lu) {
        ILotteryUserDataProvider provider = DataAccess.instance().getLotteryUserDataProvider();
        provider.save(lu);

        String groupkey = MessageFormat.format(GROUP_KEY_LOTTERY_USERS, lu.getActivityId(), lu.getUserId());
        RemoteCache.clear(groupkey);
    }

    public static LotteryUser getLotteryUser(Integer id) {
        ILotteryUserDataProvider provider = DataAccess.instance().getLotteryUserDataProvider();

        return (LotteryUser) provider.getEntity(id);
    }

    public static DataSet<LotteryUser> getLotteryUsers(Integer activityId, Integer userId, Integer itemId, Integer status,
                                                       Integer pageIndex, Integer pageSize) {
        DataSet<LotteryUser> datas = null;
        String key = null;

        key = MessageFormat.format(KEY_LOTTERY_USERS, activityId, userId, itemId, status, pageIndex, pageSize);
        datas = (DataSet<LotteryUser>) RemoteCache.get(key);
        if (datas == null) {
            ILotteryUserDataProvider provider = DataAccess.instance().getLotteryUserDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (activityId != null) {
                query = new SearchQuery(LotteryUser.DataColumns.activityId, activityId);
                group_root.addQuery(query);
            }

            if (userId != null) {
                query = new SearchQuery(LotteryUser.DataColumns.userId, userId);
                group_root.addQuery(query);
            }

            if (itemId != null) {
                query = new SearchQuery(LotteryUser.DataColumns.itemId, itemId);
                group_root.addQuery(query);
            }

            if (status != null) {
                query = new SearchQuery(LotteryUser.DataColumns.status, status);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Lottery.DataColumns.id, SortOrder.DESC, pageIndex,pageSize);
            if (datas.getObjects().size() != 0) {
                String groupkey = MessageFormat.format(GROUP_KEY_LOTTERY_USERS, activityId, userId);
                RemoteCache.add(key, groupkey, datas, ICache.HOUR_FACTOR);
            }
        }
        return datas;
    }

    public static Integer exists(Integer activityId, Integer userId, Integer itemId, Integer status) {
        ILotteryUserDataProvider provider = DataAccess.instance().getLotteryUserDataProvider();
        return provider.exists(activityId, userId, itemId, status);
    }

    public static void updateStatus(Integer id, Integer status) {
        ILotteryUserDataProvider provider = DataAccess.instance().getLotteryUserDataProvider();

        provider.update("id", id, "status", status);
        Logs.save("update-lu-" + id, "lu:" + id + " status:" + status);
    }
}
