package jdroplet.bll;

import jdroplet.cache.RedisCache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.DateTime;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ICouponDataProvider;
import jdroplet.data.model.Coupon;
import jdroplet.enums.SortOrder;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

/**
 * Created by kuibo on 2018/1/29.
 */
public class Coupons {
    public static String KEY_COUPONS = "COUPONS_{0}";

    /**
     * 获取用户所有 券
     * @param userId 指定用户id
     * @param all 当为true的时候，返回所有，当为false的只返回有效期内的
     * @return
     */
    public static List<Coupon> getCoupons(Integer userId, boolean all) {
        List<Coupon> list = null;
        //String key = null;

        //key = MessageFormat.format(KEY_COUPONS, userId);
        //list = (List<Coupon>) RemoteCache.get(key);
        if (list == null) {
            ICouponDataProvider provider = null;
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            query = new SearchQuery(Coupon.DataColumns.userId, userId, SearchQuery.EQ);
            group_root.addQuery(query);

            if (all == false) {
                query = new SearchQuery(Coupon.DataColumns.expired, DateTime.now().toString(), SearchQuery.GT);
                group_root.addQuery(query);
            }

            provider = DataAccess.instance().getCouponDataProvider();
            list = (List) provider.search(group_root, Coupon.DataColumns.id, SortOrder.ASC);
//            if (list.size() != 0) {
//                RemoteCache.add(key, list, ICache.HOUR_FACTOR);
//            }
        }
        return list;
    }

    public static Integer getCouponsValues(Integer userId) {
        List<Coupon> coupons = getCoupons(userId, false);
        Integer total = 0;
//        Integer usedVlaues = 0;

        for(Coupon coupon : coupons) {
            //usedVlaues = Bills.getCouponUsedValues(userId, coupon.getBidId());
            //total += coupon.getValue() + usedVlaues;
            total += coupon.getBalance();
        }
        return total > 0 ? total : 0;
    }

    public static Integer getValues(Integer userId, boolean all) {
        ICouponDataProvider provider = DataAccess.instance().getCouponDataProvider();

        return provider.getValues(userId, all);
    }

    public static BigInteger buildId(Integer userId) {
        String userSuffix = null;
        String timeSuffix = null;
        String randSuffix = null;
        long id = 0;
        Formatter fmt = null;

        // id 4
        fmt = new Formatter();
        fmt.format("%04d", userId);
        userSuffix = fmt.toString();
        if (userSuffix.length() > 4) {
            userSuffix = userSuffix.substring(userSuffix.length() - 4);
        }

        // time 3
        timeSuffix = Long.toString(System.nanoTime());
        timeSuffix = timeSuffix.substring(timeSuffix.length() - 3);

        // rand 5
        randSuffix = Integer.toString((int) ((Math.random() * 9 + 1) * 100));

        id = RedisCache.incr("order_id") + 1000;
        return new BigInteger(id + randSuffix + timeSuffix + userSuffix);
    }

    public static Integer save(Integer userId, String name, Integer value, String type, Date createTime, Date expired) {
        ICouponDataProvider provider = DataAccess.instance().getCouponDataProvider();
        Coupon coupon = new Coupon();

        coupon.setBigId(buildId(userId));
        coupon.setUserId(userId);
        coupon.setName(name);
        coupon.setValue(value);
        coupon.setBalance(value);

        coupon.setType(type);
        coupon.setCreateTime(createTime);
        coupon.setExpired(expired);

        String key = null;

        key = MessageFormat.format(KEY_COUPONS, userId);
        RemoteCache.remove(key);
        
        return provider.save(coupon);
    }

    public static Integer update(Coupon coupon) {
        ICouponDataProvider provider = DataAccess.instance().getCouponDataProvider();
        String key = null;

        key = MessageFormat.format(KEY_COUPONS, coupon.getUserId());
        RemoteCache.remove(key);

        return provider.save(coupon);
    }
}
