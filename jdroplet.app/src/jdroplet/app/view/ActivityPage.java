package jdroplet.app.view;

import jdroplet.bll.Activities;
import jdroplet.bll.Lotteries;
import jdroplet.bll.LotteryUsers;
import jdroplet.bll.Users;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Activity;
import jdroplet.data.model.Lottery;
import jdroplet.data.model.LotteryUser;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;

import java.util.List;

/**
 * Created by kuibo on 2018/3/30.
 */
public class ActivityPage extends MasterPage {

    /**
     * 助力游戏
     */
    public void invite() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");
        Integer groupId = request.getIntParameter("groupId", 0);
        User user = Users.getCurrentUser();

        addItem("loginUrl", SystemConfig.getSiteUrls().getUrl("snsproxy.oauth", "wechat", shopId));
        addItem("shopId", shopId);
        addItem("activityId", activityId);
        addItem("groupId", groupId);
        addItem("userId", user.getId());
    }

    /**
     * 转盘
     */
    public void turntable() {

    }

    /**
     * 求签
     */
    public void pray() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");
        User user = Users.getCurrentUser();

        addItem("userId", user.getId());
        addItem("loginUrl", SystemConfig.getSiteUrls().getUrl("snsproxy.oauth", "wechat", shopId));
        addItem("shopId", shopId);
        addItem("activityId", activityId);
    }

    /**
     * 收集
     */
    public void collection() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");
        User user = Users.getCurrentUser();
        Activity act = Activities.getActivity(activityId);

        addItem("loginUrl", SystemConfig.getSiteUrls().getUrl("snsproxy.oauth", "wechat", shopId));
        addItem("shopId", shopId);
        addItem("activityId", activityId);
        addItem("userId", user.getId());
        addItem("url", context.getCurrentUrl());
        addItem("domain", request.getScheme() + "://" + context.getAppRootUrl());
        addItem("activity", act);

        // 当打开的是朋友圈索取福链接的时候
        Integer reqUserId = request.getIntParameter("reqUserId", 0);
        Integer reqItemId = request.getIntParameter("reqItemId", 0);

        addItem("reqUserId", reqUserId);
        addItem("reqItemId", reqItemId);
        addItem("userId", user.getId());

        if (reqItemId != 0) {
            Lottery lot = null;

            try {
                lot = Lotteries.getLottery(reqItemId);
            } catch (Exception ex) {
            }
            if (lot != null) {
                addItem("reqItemName", lot.getName());

                DataSet<LotteryUser> lous = LotteryUsers.getLotteryUsers(activityId, user.getId(), reqItemId, 1, 1, 1);
                addItem("reqMyItemSize", lous.getTotalRecords());
            }

            if (reqUserId != 0) {
                User reqUser = Users.getUser(reqUserId);
                addItem("reqUserName", reqUser.getDisplayName());
                addItem("reqUserAvatar", reqUser.getAvatar());
            }
        }
    }

    /**
     * 挖金矿
     */
    public void lode() {
        Integer shopId = request.getIntParameter("shopId");
        Integer activityId = request.getIntParameter("activityId");

        addItem("shopId", shopId);
        addItem("activityId", activityId);
    }

    /**
     * 红包雨
     */
    public void rain() {
        collection();
    }
}
