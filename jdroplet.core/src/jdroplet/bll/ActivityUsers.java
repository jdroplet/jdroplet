package jdroplet.bll;

import jdroplet.core.DateTime;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IActivityUserDataProvider;
import jdroplet.data.model.Activity;
import jdroplet.data.model.ActivityUser;
import jdroplet.data.model.Entity;

/**
 * Created by kuibo on 2018/4/24.
 */
public class ActivityUsers {

    public static void save(ActivityUser au) {
        IActivityUserDataProvider provider = DataAccess.instance().getActivityUserDataProvider();
        provider.save(au);
    }

    public static ActivityUser getActivityUser(Integer shopId, Integer activityId, Integer userId) {
        IActivityUserDataProvider provider = null;
        ActivityUser au = null;
        Integer auId = null;
        Activity act = null;

        if (userId == null)
            return null;

        provider = DataAccess.instance().getActivityUserDataProvider();
        act = Activities.getActivity(activityId);
        auId = provider.exists(shopId, activityId, userId);
        if (auId == null || auId == 0) {
            au = new ActivityUser();
            au.setUserId(userId);
            au.setShopId(shopId);
            au.setActivityId(activityId);
            au.setShopId(shopId);
            au.setUserId(userId);
            au.setCount(act.getInt("game_count"));

            ActivityUsers.save(au);
        } else {
            au = (ActivityUser) provider.getEntity(auId);

            /// 当第二天访问的时候，初始化信息
            if (act.getBool("day_reset") && DateTime.now().getDay() != new DateTime(au.getUpdateTime()).getDay()) {
                au.setCount(act.getInt("game_count"));

                ActivityUsers.save(au);
            }
        }
        return au;
    }
}
