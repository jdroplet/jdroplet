package jdroplet.app.view.api.game;

import jdroplet.bll.*;
import jdroplet.core.DateTime;
import jdroplet.data.model.*;
import jdroplet.util.I18n;

import java.util.Date;

/**
 * Created by kuibo on 2018/6/13.
 */
public class RainPage extends GameBasePage {

    public static final Integer OUT_OF_DATE = 1009001;
    public static final Integer OUT_OF_COUNT = 1009002;
    public static final Integer TOKEN_ERROR = 1009100;

    protected int getExpire() {
        return 60;
    }

    public void check() {
        User user = Users.getCurrentUser();
        Integer activityId = request.getIntParameter("activityId");
        Activity act = Activities.getActivity(activityId);
        ActivityUser au = ActivityUsers.getActivityUser(act.getShopId(), activityId, user.getId());
        if (DateTime.diff(act.getExpired(), new Date()) < 0) {
            renderJSON(OUT_OF_DATE);
            return;
        } else if (au.getCount() <= 0) {
            renderJSON(OUT_OF_COUNT);
            return;
        }

        renderJSON(0);
    }

    public void start() {
        User user = Users.getCurrentUser();
        Integer activityId = request.getIntParameter("activityId");
        Activity act = Activities.getActivity(activityId);
        ActivityUser au = ActivityUsers.getActivityUser(act.getShopId(), activityId, user.getId());
        long timestamp = request.getLongParameter("timestamp");
        String token = getToken(act.getShopId(), act.getId(), au.getUserId(), timestamp);

        if (DateTime.diff(act.getExpired(), new Date()) < 0) {
            renderJSON(OUT_OF_DATE);
            return;
        } else if (au.getCount() <= 0) {
            renderJSON(OUT_OF_COUNT);
            return;
        }

        au.setCount(au.getCount() - 1 );
        ActivityUsers.save(au);
        renderJSON(0, "", token);
    }

    public void get() {
        Integer activityId = request.getIntParameter("activityId");
        User user = Users.getCurrentUser();
        long timestamp = request.getLongParameter("timestamp");
        Activity act = Activities.getActivity(activityId);
        String tk = request.getParameter("tk");
        Lottery lot = Lotteries.getLottery(act.getShopId(), activityId, act.getInt("maxCountItemIndex"), act.getInt("maxIPcount"), user, null);
        int result = 0;

        result = verifyToken(act.getShopId(), activityId, user.getId(), timestamp, tk);
        if (result != 0) {
            renderJSON(TOKEN_ERROR + result, I18n.getString(Integer.toString(TOKEN_ERROR + result)));
            return;
        }

        if (lot != null) {
            LotteryUser lu = new LotteryUser();
            lu.setActivityId(activityId);
            lu.setItemId(lot.getId());
            lu.setUserId(user.getId());
            lu.setSenderId(0);
            lu.setAge(0);
            lu.setCreateTime(new Date());
            lu.setName(lot.getName());
            lu.setUpdateTime(new Date());
            lu.setStatus(1);
            LotteryUsers.save(lu);



            lot.setCount(0);
            lot.setRate(0);
            lot.setUserId(0);
            renderJSON(0, "", lot);
        } else {
            renderJSON(-1, "Empty");
        }
    }
}
