package jdroplet.app.view.admin;

import jdroplet.bll.Activities;
import jdroplet.bll.Shops;
import jdroplet.bll.Users;
import jdroplet.core.DateTime;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Activity;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;
import jdroplet.util.TextUtils;

import java.util.Date;

/**
 * Created by kuibo on 2018/1/24.
 */
public class ManageActivityPage extends ManageMasterPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer status = request.getIntParameter("status", 0);
        String type = request.getParameter("type", "");
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        DataSet<Activity> datas = null;
        Shop shop = null;

        if (shopId != null)
            shop = Shops.getShop(shopId);
        else
            shop = new Shop();

        datas = Activities.getActivities(shopId, null, pageIndex, 20);
        addItem("pageIndex", pageIndex);
        addItem("status", status);
        addItem("type", type);
        addItem("shopId", shopId);
        addItem("shop", shop);
        addItem("datas", datas);
    }

    public void edit() {
        Integer id = request.getIntParameter("id");
        Integer shopId = request.getIntParameter("shopId");
        Activity activity = null;
        Shop shop = null;

        if (id == null || id == 0) {
            activity = new Activity();
        } else {
            activity = Activities.getActivity(id);
            shopId = activity.getShopId();
        }
        shop = Shops.getShop(shopId);

        addItem("id", id);
        addItem("shopId", shopId);
        addItem("shop", shop);
        addItem("activity", activity);
    }

    public void recv() {
        Activity activity = null;
        User user = null;
        String activity_daterange = request.getParameter("daterange");

        activity = new Activity();
        Activity.DataColumns.fill(request, activity);

        //处理时间字符串
        if (!TextUtils.isEmpty(activity_daterange)
                && activity_daterange.indexOf(" - ") != -1
                && activity_daterange.split(" - ").length == 2) {

            String start = activity_daterange.split(" - ")[0];
            String end = activity_daterange.split(" - ")[1];

            Date startTime = DateTime.parse(start).getDate();
            Date expired = DateTime.parse(end).getDate();

            activity.setStartTime(startTime);
            activity.setExpired(expired);
        }
        Activities.save(activity);

        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("activity.edit", activity.getId()) + "?shopId=" + activity.getShopId());
    }
}
