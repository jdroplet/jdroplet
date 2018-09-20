package jdroplet.bll;

import jdroplet.cache.RemoteCache;
import jdroplet.core.DateTime;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ICheckInDataProvider;
import jdroplet.data.model.CheckIn;

import java.util.Date;

/**
 * Created by kuibo on 2018/7/3.
 */
public class CheckIns {

    /**
     * 签到
     * @param shopId
     * @param userId
     * @return 如果当天未签到CheckIn的LastDate为空或上一次签到时间,如果已经签过CheckIn的LastDate为当天时间
     */
    public static CheckIn checkin(Integer shopId, Integer userId) {
        DateTime dt = DateTime.now();
        Integer id = null;
        CheckIn ci = null;
        ICheckInDataProvider provider = DataAccess.instance().getCheckInDataProvider();

        dt.setHour(0);
        dt.setMinute(0);
        dt.setSecond(0);
        id = isChecked(shopId, userId, dt.getDate());
        if (id == null) {
            id = isChecked(shopId, userId, dt.addDays(-1).getDate());
            if (id != null) {
                ci = (CheckIn) provider.getEntity(id);
            } else {
                ci = new CheckIn();
                ci.setShopId(shopId);
                ci.setUserId(userId);
                ci.setFirstDate(new Date());
            }
            ci.setLastDate(dt.getDate());
            provider.save(ci);
        } else {
            ci = (CheckIn) provider.getEntity(id);
        }

        return ci;
    }

    public static Integer isChecked(Integer shopId, Integer userId, Date date) {
        String cacheKey = "CHECKINS_s:" + shopId + "-u:" + userId + "-d:" + DateTime.toString(date, "yyyy-MM-dd");
        Integer id = null;

        id = (Integer) RemoteCache.get(cacheKey);
        if (id == null) {
            ICheckInDataProvider provider = DataAccess.instance().getCheckInDataProvider();

            id = provider.isChecked(shopId, userId, date);
            if (id != null)
                RemoteCache.add(cacheKey, id);
        }

        return id;
    }
}
