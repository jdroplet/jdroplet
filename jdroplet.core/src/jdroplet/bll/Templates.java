package jdroplet.bll;

import jdroplet.core.HttpContext;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Menu;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.SortUtil;
import jdroplet.util.TextUtils;

import java.util.*;

/**
 * Created by kuibo on 2017/12/12.
 */
public class Templates {

    public static Map<String, String> getManageMenuNavGroup() {
        Collection<Menu> menus = getManageMenus();
        Map<String, String> group = new HashMap<String, String>();

        for(Menu menu:menus) {
            group.put(menu.getKey(), menu.getGroup());

            for (String subKey : menu.getSubMenus().keySet()) {
                group.put(subKey, menu.getGroup());
            }
        }

        return group;
    }

    public static Collection<Menu> getManageMenus() {
        Map<String, Menu> menus = null;
        Menu menu = null;
        Menu subMenu = null;
        SiteUrls urls = null;

        urls = SystemConfig.getSiteUrls();

        menus = new HashMap<>();

        menu = new Menu();
        menu.setSort(10);
        menu.setGroup("group_10");
        menu.setName("我的店铺");
        menu.setKey("shop");
        menu.setIcon("fa-building");
        menu.setUrl(urls.getUrl("shop.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("shop", menu);

        menu = new Menu();
        menu.setSort(20);
        menu.setGroup("group_20");
        menu.setName("商品");
        menu.setKey("goods");
        menu.setIcon("fa-gift");
        menu.setUrl(urls.getUrl("goods.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("goods", menu);

        menu = new Menu();
        menu.setSort(30);
        menu.setGroup("group_30");
        menu.setName("分类");
        menu.setKey("section");
        menu.setIcon("fa-list-ul");
        menu.setUrl(urls.getUrl("section.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("section", menu);

        menu = new Menu();
        menu.setSort(40);
        menu.setGroup("group_40");
        menu.setName("文章");
        menu.setKey("post");
        menu.setIcon("fa-file");
        menu.setUrl(urls.getUrl("post.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("post", menu);

        Map<String, Posts.PostTypeTemplate> postTypeTemplates = Posts.getPostTypeTemplates();
        int idx = 0;
        for (String key : postTypeTemplates.keySet()) {
            Posts.PostTypeTemplate ptt = null;

            idx++;
            ptt = postTypeTemplates.get(key);
            menu = new Menu();
            menu.setSort(30 + idx);
            menu.setGroup("group_30" + idx);
            menu.setName(ptt.getTitle());
            menu.setKey(key);
            menu.setIcon(ptt.getIcon());
            menu.setUrl(urls.getUrl("post.list", 1) + "?type=" + key);
            menu.setAtive(isMenuActive(menu));
            menus.put(key, menu);
        }

        menu = new Menu();
        menu.setSort(50);
        menu.setGroup("group_50");
        menu.setName("活动");
        menu.setKey("activity");
        menu.setIcon("fa-gamepad");
        menu.setUrl(urls.getUrl("activity.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("activity", menu);

        menu = new Menu();
        menu.setSort(60);
        menu.setGroup("group_60");
        menu.setName("订单");
        menu.setKey("order");
        menu.setIcon("fa-th-list");
        menu.setUrl(urls.getUrl("order.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("order", menu);

        menu = new Menu();
        menu.setSort(70);
        menu.setGroup("group_70");
        menu.setName("会员");
        menu.setKey("order");
        menu.setIcon("fa-user");
        menu.setUrl(urls.getUrl("user.list", 1));
        menu.setAtive(isMenuActive(menu));
        menus.put("user", menu);

        menu = new Menu();
        menu.setSort(1000);
        menu.setGroup("group_1000");
        menu.setName("设置");
        menu.setKey("");
        menu.setIcon("fa-cog");
        menu.setUrl("#");
        menus.put("settings", menu);

        subMenu = new Menu();
        subMenu.setSort(10);
        subMenu.setName("集群列表");
        subMenu.setKey("cluster");
        subMenu.setAction("list");
        subMenu.setIcon("fa-circle-o");
        subMenu.setUrl(urls.getUrl("cluster.list", 1));
        subMenu.setAtive(isMenuActive(subMenu));
        menu.addMenu("cluster", subMenu);

        subMenu = new Menu();
        subMenu.setSort(30);
        subMenu.setName("计划任务");
        subMenu.setKey("cron");
        subMenu.setAction("list");
        subMenu.setIcon("fa-circle-o");
        subMenu.setUrl(urls.getUrl("cron.list", 1));
        subMenu.setAtive(isMenuActive(subMenu));
        menu.addMenu("cron", subMenu);
        menu.setAtive(isMenuActive(menu));

        menus = (Map) PluginFactory.getInstance().applyFilters("Templates@getManageMenus", menus);

        List<Menu> list = new ArrayList<Menu>(menus.values());
        Collections.sort(list, new Comparator<Menu>() {

            @Override
            public int compare(Menu o1, Menu o2) {
                if (o1.getSort() < o2.getSort())
                    return -1;
                else if (o1.getSort() > o2.getSort())
                    return 1;
                else
                    return o1.getName().compareTo(o2.getName());
            }
        });
        return list;
    }

    public static boolean isMenuActive(Menu menu) {
        for (Iterator<Menu> i = menu.getSubMenus().values().iterator(); i.hasNext(); ) {
            if (isMenuActive(i.next()))
                return true;
        }

        String curUrl = HttpContext.current().getCurrentUrl();
        String menuUrl = menu.getUrl();
        String query = null;

        if (menuUrl.indexOf("?") > 0) {
            query = menuUrl.substring(menuUrl.indexOf("?") + 1);
            menuUrl =  menuUrl.substring(0, menuUrl.indexOf("?"));
        }

        if (menuUrl.equals(curUrl)) {
            String curQuery = HttpContext.current().request().getQueryString();

            if (!TextUtils.isEmpty(curQuery) && !TextUtils.isEmpty(query)) {
                return curQuery.indexOf(query) >= 0;
            } else if (TextUtils.isEmpty(curQuery) && TextUtils.isEmpty(query)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
