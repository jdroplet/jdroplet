package jdroplet.app.view.admin;

import jdroplet.bll.Activities;
import jdroplet.bll.Lotteries;
import jdroplet.bll.LotteryUsers;
import jdroplet.bll.Shops;
import jdroplet.data.model.Activity;
import jdroplet.data.model.Lottery;
import jdroplet.data.model.LotteryUser;
import jdroplet.data.model.Shop;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/5/14.
 */
public class ManageLotteryUserPage extends ManageMasterPage {

    public void list() {
        Integer activityId = request.getIntParameter("activityId");
        Integer userId = request.getIntParameter("userId");
        Integer itemId = request.getIntParameter("itemId");
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        DataSet<LotteryUser> datas = LotteryUsers.getLotteryUsers(activityId, userId, itemId, 1, pageIndex, 20);
        Activity act = Activities.getActivity(activityId);
        Shop shop = Shops.getShop(act.getShopId());

        if (itemId != null && itemId != 0)
            addItem("lottery", Lotteries.getLottery(itemId));
        addItem("datas", datas);
        addItem("pageIndex", pageIndex);
        addItem("activityId", activityId);
        addItem("actiivty", act);
        addItem("itemId", itemId);
        addItem("shop", shop);
    }
}
