package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ILotteryDataProvider;
import jdroplet.data.model.Lottery;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.exceptions.ApplicationException;
import jdroplet.util.IPAddress;
import jdroplet.util.JSONUtil;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kuibo on 2018/3/27.
 */
public class Lotteries {

    public interface IDuplicateUserFilter {
        Lottery doFilter(Integer shopId, Integer activityId, User user, List<Lottery> lotteries, Integer default_lottery);
    }

    public static String GROUP_KEY_LOTTERIES = "GROUP_KEY_LOTTERIES";

    public static String KEY_LOTTERY = "LOTTERY_";

    public static String KEY_LOTTERIES = "LOTTERIES_{0}_{1}";

    public static Lottery getLottery(Integer id) {
        ILotteryDataProvider provider = DataAccess.instance().getLotteryDataProvider();

        return (Lottery) provider.getEntity(id);
    }

    public static Integer save(Lottery lottery) {
        ILotteryDataProvider provider = DataAccess.instance().getLotteryDataProvider();

        Integer id = null;

        id = provider.save(lottery);
        if (lottery.getId() == null || lottery.getId() == 0) {
            lottery.setId(id);
        }
        Metas.save(lottery);
        String key = null;
        key = MessageFormat.format(KEY_LOTTERIES, lottery.getShopId(), lottery.getActivityId());
        RemoteCache.remove(key);

        Logs.save("save", JSONUtil.toJSONString(lottery));
        return id;
    }

    public static Lottery getLottery(int shopId, int activityId, int maxCountItemIndex, int maxIPcount, User user, IDuplicateUserFilter filter) {
        Lottery lottery = null;
        List<Lottery> lots = null;

        lots = Lotteries.getLotteries(shopId, activityId, true, false);
        if (lots != null && lots.size() != 0) {
            // 过滤掉刷量的IP
            if (lottery == null && maxIPcount != Integer.MAX_VALUE) {
                IPAddress ipAddr = null;
                HttpRequest request = null;

                request = HttpContext.current().request();
                ipAddr = IPAddress.parse(request.getXRemoteAddr());

                if (lottery == null && IPS.getIPCount(shopId, activityId, ipAddr.getAddress(), new Date()) >= maxIPcount) {
                    lottery = lots.get(maxCountItemIndex);
                } else {
                    IPS.add(shopId, shopId, ipAddr.getAddress());
                }
            }

            // 过滤用户
            if (lottery == null && filter != null) {
                lottery = filter.doFilter(shopId, activityId, user, lots, maxCountItemIndex);
            }

            // 从奖品池获取奖品
            if (lottery == null) {
                lottery = Lotteries.getLottery(lots, user, maxCountItemIndex);
            }

            // 判断和处理是否可以重复中奖
            if (lottery != null) {
                int type = lottery.getFlag();

                if ((type & Lottery.SINGLE) == Lottery.SINGLE) {
                    if (exists(lottery.getId(), user.getId())) {
                        lottery = lots.get(maxCountItemIndex);
                    }
                }
            }

            if (lottery.getCount() <= 0)
                throw new ApplicationException("out of lottery count");

            Lotteries.updateCount(lottery.getId(), lottery.getCount()-1);
        }
        return lottery;
    }

    public static void updateCount(int id, int count) {
        ILotteryDataProvider provider = null;
        Lottery lottery = getLottery(id);

        provider =  (ILotteryDataProvider)DataAccess.instance().createProvider("LotteryDataProvider");
        provider.update(Lottery.DataColumns.id, id, Lottery.DataColumns.count, count);

        String key = MessageFormat.format(KEY_LOTTERIES, lottery.getShopId(), lottery.getActivityId());
        RemoteCache.remove(key);
    }

    public static List<Lottery> getLotteries(Integer shopId, Integer activityId, boolean cacheable, boolean flush) {
        List<Lottery> list = null;
        String key = null;

        key = MessageFormat.format(KEY_LOTTERIES, shopId, activityId);

        if (flush || !cacheable)
            RemoteCache.remove(key);
        else
            list = (List<Lottery>) RemoteCache.get(key);

        if (list == null) {
            ILotteryDataProvider provider = (ILotteryDataProvider) DataAccess.instance().getLotteryDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (shopId != null) {
                query = new SearchQuery(Lottery.DataColumns.shopId, shopId);
                group_root.addQuery(query);
            }

            if (activityId != null) {
                query = new SearchQuery(Lottery.DataColumns.activityId, activityId);
                group_root.addQuery(query);
            }

            list = (List) provider.search(group_root, Lottery.DataColumns.id, SortOrder.ASC);
            if (cacheable && list.size() != 0) {
                String groupkey = GROUP_KEY_LOTTERIES + shopId + "_" + activityId;
                RemoteCache.add(key, groupkey, list, ICache.DAY_FACTOR);
            }
        }
        return list;
    }

    public static int getRandNum(int size, int rate_base) {
        Integer[] nums = null;
        User user = null;
        int idx = 0;
        long hexIdx = 0;

        nums = new Integer[size];
        user = Users.getCurrentUser();

        // 生成一组随机数
        for (idx = 0; idx < size; idx++) {
            nums[idx] = (int)(Math.random() * rate_base);
        }

        // 根据用户Id取其中的随机数
        hexIdx =  user.getId() % size;
        return nums[(int) hexIdx];
    }

    private static Lottery getLottery(List<Lottery> lots, User user, int maxCountItemIndex) {
        int randNum = 0;
        int boundary_start = 0;
        int boundary_end = 0;
        int max_count = 0;
        int rate_base = 0;
        Lottery lottery = null;
        Lottery temp = null;

        //  获得当前用户的随机数
        for (int idx=0; idx<lots.size(); idx++) {
            rate_base += lots.get(idx).getRate();
        }

        randNum = getRandNum(lots.size(), rate_base);

        for (int idx=0; idx<lots.size(); idx++) {
            boundary_start = boundary_end;
            boundary_end = boundary_start + lots.get(idx).getRate();

            // 计算随机数落在哪个奖品的区间
            if (randNum >= boundary_start && randNum < boundary_end && lots.get(idx).getCount() > 0) {
                lottery = lots.get(idx);
                lottery.setRank(idx);
                break;
            }

            // 获取数量最大的奖品
            if (lots.get(idx).getCount() > max_count) {
                max_count = lots.get(idx).getCount();
                temp = lots.get(idx);
                temp.setRank(idx);
            }
        }

        // 当随机数没有落在奖品区间,就取数量最大的作为奖品返回
        if (lottery == null) {
            lottery = temp;
        } else {
            String userId = user.getId().toString();
            String subId = lottery.getInt("sub_id", 0).toString();

            // 当用户尾号是 指定 尾号的时候, 就只能中奖品数量最大的奖品
            if (!"0".equals(subId) && userId.lastIndexOf(subId) > 0) {
                lottery = lots.get(maxCountItemIndex);
                lottery.setRank(maxCountItemIndex);
            }

            // 当幸运奖品数为0,则返回默认奖品
            if (lottery.getCount() == 0) {
                lottery = lots.get(maxCountItemIndex);
                lottery.setRank(maxCountItemIndex);
            }
        }
        return lottery;
    }

    public static boolean exists(Integer lotteryId, Integer userId) {
        return false;
    }
}
