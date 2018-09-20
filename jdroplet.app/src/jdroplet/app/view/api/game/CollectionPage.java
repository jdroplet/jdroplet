package jdroplet.app.view.api.game;

import jdroplet.app.view.api.APIPage;
import jdroplet.bll.*;
import jdroplet.core.DateTime;
import jdroplet.data.model.*;
import jdroplet.util.DataSet;

import java.util.*;

/**
 * Created by kuibo on 2018/4/23.
 */
public class CollectionPage extends APIPage {

    //
    public static final Integer LOTTERY_USER_NOT_FOUND = 1002000;

    public static final Integer INVALID_OWNER = 1002001;

    public static final Integer INVALID_RECVER = 1002002;

    // 过期
    public static final Integer OUT_OF_DATE = 1002003;

    //
    public static final Integer OUT_OF_COUNT = 1002004;

    public static final Integer INVALID_REQ = 1002005;

    public void get() {
        Integer activityId = request.getIntParameter("activityId");
        DataSet<LotteryUser> lus = null;
        List<Lottery> lots = null;
        List<Map> cards = null;
        Map<String, Object> card = null;
        User user = Users.getCurrentUser();
        Activity act = Activities.getActivity(activityId);
        ActivityUser au = ActivityUsers.getActivityUser(act.getShopId(), act.getId(), user.getId());

        lus = LotteryUsers.getLotteryUsers(activityId, user.getId(), null, 1, 1, 20);
        lots = Lotteries.getLotteries(act.getShopId(), activityId, true, false);
        cards = new ArrayList<>();
        for(Lottery lot:lots) {
            card = new HashMap<>();
            card.put("name", lot.getName());
            DataSet<LotteryUser> luis = LotteryUsers.getLotteryUsers(activityId, user.getId(), lot.getId(), 1, 1, 1);

            card.put("count", luis.getTotalRecords());
            card.put("id", lot.getId());
            cards.add(card);
        }
        act.setValue("lus", lus);
        act.setValue("cards", cards);
        act.setValue("user", au);
        act.setValue("userName", user.getDisplayName());

        DataSet<LotteryUser> luis = LotteryUsers.getLotteryUsers(activityId, user.getId(), 0, 1, 1, 20);
        act.setValue("lus2", luis);
        renderJSON(0, "", act);
    }

    /**
     * 抽取卡片
     */
    public void lottery() {
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
        } else {
            renderJSON(-1, "Empty");
        }
    }

    /**
     * 转移卡片
     */
    public void transfer() {
        Integer activityId = request.getIntParameter("activityId");
        Integer reqItemId = request.getIntParameter("reqItemId");
        Integer recvId = request.getIntParameter("recvId", 0);
        User user = Users.getCurrentUser();
        User recvUser = Users.getUser(recvId);
        Integer id = LotteryUsers.exists(activityId, user.getId(), reqItemId, 1);
        LotteryUser lu = LotteryUsers.getLotteryUser(id);

        if (lu == null) {
            renderJSON(LOTTERY_USER_NOT_FOUND);
            return;
        } else if (!lu.getUserId().equals(user.getId())) {
            renderJSON(INVALID_OWNER);
            return;
        } else if (recvId.equals(0)) {
            renderJSON(INVALID_RECVER);
            return;
        }
        lu.setSenderId(user.getId());
        lu.setUserId(recvId);
        lu.setAge(lu.getAge() + 1);
        LotteryUsers.save(lu);

        Message msg = new Message();
        msg.setSenderId(user.getId());
        msg.setContent(user.getDisplayName() + " 将 " + lu.getName() + "🎁 送给 " + recvUser.getDisplayName());
        msg.setPostTime(new Date());
        Messages.send(msg, user.getId(), new Integer[]{recvId});

        renderJSON(0);
    }

    public void mix() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");
        List<Lottery> lots = Lotteries.getLotteries(shopId, activityId, true, false);
        User user = Users.getCurrentUser();
        boolean canMix = true;
        List<Integer> ids = new ArrayList<>();

        if (lots.size() == 0)
            canMix = false;

        for(Lottery lot:lots) {
            Integer id = LotteryUsers.exists(activityId, user.getId(), lot.getId(), 1);

            if (id == null) {
                canMix = false;
                break;
            }

            ids.add(id);
        }

        if (canMix) {
            for(Integer id:ids) {
                LotteryUsers.updateStatus(id, 0);
            }
            Activity act = Activities.getActivity(activityId);
            Lottery lot = Lotteries.getLottery(act.getInt("mix_lot"));

            LotteryUser lu = new LotteryUser();
            lu.setActivityId(activityId);
            lu.setItemId(lot.getId());
            lu.setUserId(user.getId());
            lu.setSenderId(0);
            lu.setAge(0);
            lu.setCreateTime(new Date());
            lu.setName(lot.getName());
            lu.setUpdateTime(new Date());
            LotteryUsers.save(lu);

            Message msg = new Message();
            msg.setSenderId(user.getId());
            msg.setContent(lot.getMessage());
            msg.setPostTime(new Date());
            Messages.send(msg, 0, new Integer[]{user.getId()});

            renderJSON(0);
        } else {
            renderJSON(INVALID_REQ);
        }
    }

}
