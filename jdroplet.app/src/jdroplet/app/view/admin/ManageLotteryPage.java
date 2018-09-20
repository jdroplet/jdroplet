package jdroplet.app.view.admin;

import jdroplet.bll.*;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Activity;
import jdroplet.data.model.Lottery;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;

import java.util.List;

/**
 * Created by kuibo on 2018/4/23.
 */
public class ManageLotteryPage extends ManageMasterPage {

    public void list() {
        Integer activityId = request.getIntParameter("activityId");
        Activity act = Activities.getActivity(activityId);
        List<Lottery> lots = null;
        Integer totalRate = 0;
        Integer itemCount = 0;
        Shop shop = Shops.getShop(act.getShopId());

        lots = Lotteries.getLotteries(act.getShopId(), activityId, true, false);
        for(Lottery lot:lots) {
            totalRate += lot.getRate();
            itemCount += lot.getCount();
        }
        addItem("activityId", activityId);
        addItem("totalRate", totalRate);
        addItem("itemCount", itemCount);
        addItem("act", act);
        addItem("lots", lots);
        addItem("shop", shop);
    }

    public void edit() {
        Integer id = request.getIntParameter("id");
        Integer activityId = request.getIntParameter("activityId");
        Lottery lot = null;
        Activity act = Activities.getActivity(activityId);
        List<Lottery> lots = null;
        Shop shop = Shops.getShop(act.getShopId());

        lots = Lotteries.getLotteries(act.getShopId(), activityId, true, false);
        if (id == null || id == 0) {
            lot = new Lottery();
        } else {
            lot = Lotteries.getLottery(id);
        }
        addItem("id", id);
        addItem("lot", lot);
        addItem("act", act);
        addItem("shop", shop);
        addItem("lots", lots);
    }

    public void recv() {
        Lottery lot = null;
        User user = Users.getCurrentUser();

        lot = new Lottery();
        lot.setUserId(user.getId());
        Lottery.DataColumns.fill(request, lot);
        Lotteries.save(lot);

        // 设置默认奖品
        List<Lottery> lots = null;
        Integer maxCount = 0;
        Integer maxCountItemIndex = 0;
        lots = Lotteries.getLotteries(lot.getShopId(), lot.getActivityId(), true, false);
        for(int i=0; i<lots.size(); i++) {
            if (lots.get(i).getCount() >= maxCount) {
                maxCount = lots.get(i).getCount();
                maxCountItemIndex = i;
            }
        }

        Activity act = Activities.getActivity(lot.getActivityId());
        Metas.save(Activity.class, act.getId(), "maxCountItemIndex", maxCountItemIndex);

        Boolean is_mix_lot = request.getBooleanParameter("is_mix_lot");
        if (is_mix_lot)
            Metas.save(Activity.class, act.getId(), "mix_lot", lot.getId());

        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("lottery.edit", lot.getId()) + "?activityId=" + lot.getActivityId() + "&shopId=" + lot.getShopId());
    }
}
