package jdroplet.app.view.api.game;

import jdroplet.app.view.api.APIPage;
import jdroplet.bll.*;
import jdroplet.core.DateTime;
import jdroplet.data.model.*;

import java.util.Date;

/**
 * Created by kuibo on 2018/5/24.
 */
public class LotteryPage extends APIPage {

    // 过期
    public static final Integer OUT_OF_DATE = 1007000;

    //
    public static final Integer OUT_OF_COUNT = 1007001;

    public static final Integer NOT_ALLOW_UPDATE_CONTACT = 1007002;

    public static final Integer INVALID_LOTTERY_USER  = 1007003;

    /**
     * 抽奖
     */
    public void get() {
        Integer activityId = request.getIntParameter("activityId");
        User user = Users.getCurrentUser();
        Activity act = Activities.getActivity(activityId);
        ActivityUser au = ActivityUsers.getActivityUser(act.getShopId(), activityId, user.getId());

        if (DateTime.diff(act.getExpired(), new Date()) < 0) {
            renderJSON(OUT_OF_DATE);
            return;
        } else if (au.getCount() <= 0) {
            renderJSON(OUT_OF_COUNT);
            return;
        }
        Lottery lot = Lotteries.getLottery(act.getShopId(), activityId, act.getInt("maxCountItemIndex"), act.getInt("maxIPcount"), user, null);

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

            au.setCount(au.getCount() - 1 );
            ActivityUsers.save(au);

            lot.setCount(0);
            lot.setRate(0);
            lot.setUserId(0);
            renderJSON(0, "", lot);
        }
    }

    public void save() {
        Integer shopId = null;
        Integer activityId = request.getIntParameter("activityId");
        Integer lotteryId = request.getIntParameter("lotteryId");
        Activity act = Activities.getActivity(activityId);
        User user = Users.getCurrentUser();

        shopId = act.getShopId();
        if (Contacts.exists(shopId, activityId, user.getId())) {
            renderJSON(NOT_ALLOW_UPDATE_CONTACT);
            return;
        }
        if (LotteryUsers.exists(activityId, user.getId(), lotteryId, 1) == null) {
            renderJSON(INVALID_LOTTERY_USER);
            return;
        }

        Contact contact = new Contact();
        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        contact.setShopId(shopId);
        contact.setActivityId(activityId);
        contact.setUserId(user.getId());
        contact.setUserName(userName);
        contact.setPhone(phone);
        contact.setRemark("");
        contact.setAddress(address);
        Contacts.save(contact);

        renderJSON(0);

    }
}
