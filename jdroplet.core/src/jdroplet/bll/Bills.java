package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IBillDataProvider;
import jdroplet.data.model.Bill;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.math.BigInteger;
import java.text.MessageFormat;

/**
 * Created by kuibo on 2018/1/29.
 */
public class Bills {
    public static String GROUP_KEY_BOOKS = "BOOKS_GROUPS";
    public static String KEY_BILLS = "BILLS_{0}_{1}_{2}_{3}";

    public static boolean exists(Integer userId, BigInteger orderId) {
        IBillDataProvider provider = (IBillDataProvider) DataAccess.instance().createProvider("BillDataProvider");

        return provider.exists(userId, orderId);
    }

    public static DataSet<Bill> getBills(Integer userId, Integer type, Integer pageIndex, Integer pageSize) {
        String key = null;
        DataSet<Bill> datas = null;

        key = MessageFormat.format(KEY_BILLS, userId, type, pageIndex, pageSize);

        if (datas == null) {
            IBillDataProvider provider = (IBillDataProvider) DataAccess.instance().createProvider("BillDataProvider");
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (userId != null) {
                query = new SearchQuery(Bill.DataColumns.userId, userId, SearchQuery.EQ);
                group_root.addQuery(query);
            }

            if (type != null) {
                query = new SearchQuery(Bill.DataColumns.type, type, SearchQuery.OR);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Bill.DataColumns.createTime, SortOrder.DESC, pageIndex, pageSize);
            if (datas.getObjects().size() != 0) {
                RemoteCache.add(key, GROUP_KEY_BOOKS, datas, ICache.DAY_FACTOR);
            }
        }
        return datas;
    }

    /**
     * 获取用户总金币数量
     * @param userId
     * @return
     */
    public static Integer getUserCoins(Integer userId) {
        IBillDataProvider provider = (IBillDataProvider) DataAccess.instance().createProvider("BillDataProvider");
        Integer coins = provider.getUserCoins(userId);
        return coins;
    }

    /**
     * 获取已经使用的值
     * @param userId
     * @param couponId
     * @return
     */
    public static Integer getCouponUsedValues(Integer userId, BigInteger couponId) {
        IBillDataProvider provider = (IBillDataProvider) DataAccess.instance().createProvider("BillDataProvider");

        return provider.getCouponUsedValues(userId, couponId);
    }

    public static Integer save(Bill bill) {
        IBillDataProvider provider = (IBillDataProvider) DataAccess.instance().createProvider("BillDataProvider");
        return provider.save(bill);
    }
}
