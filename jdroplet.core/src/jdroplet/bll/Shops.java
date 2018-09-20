package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IShopDataProvider;
import jdroplet.data.model.Shop;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;


import java.text.MessageFormat;
import java.util.List;

/**
 * Created by kuibo on 2017/12/12.
 */
public class Shops {
    public static String GROUP_KEY_SHOPS = "SHOPS_GROUPS";
    
    public static String KEY_SHOPS = "SHOP_SHOPS_st:{0}_pi:{1}_ps:{2}_uid:{3}";
    public static String KEY_SHOP = "SHOP_";
    
    public static DataSet<Shop> getShops(String searchTerms, Integer userId, Integer pageIndex, Integer pageSize) {
        return getShops(searchTerms, userId, pageIndex, pageSize, true, false);
    }

    public static DataSet<Shop> getShops(String searchTerms, Integer userId, Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
        String key = null;
        DataSet<Shop> datas = null;

        key = MessageFormat.format(KEY_SHOPS, searchTerms, userId, pageIndex, pageSize);

        if (flush || !cacheable)
            RemoteCache.remove(key);
        else
            datas = (DataSet<Shop>) RemoteCache.get(key);

        if (datas == null) {
            IShopDataProvider provider = (IShopDataProvider) DataAccess.instance().createProvider("ShopDataProvider");
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            //========================
            group = new SearchGroup();
            group.setTerm(SearchGroup.OR);

            if (userId != null) {
                query = new SearchQuery(Shop.DataColumns.userId, userId, SearchQuery.EQ);
                group.addQuery(query);
            }

            if (!TextUtils.isEmpty(searchTerms)) {
                query = new SearchQuery(Shop.DataColumns.name, searchTerms, SearchQuery.LIKE);
                group.addQuery(query);
            }
            group_root.addGroup(group);

            datas = (DataSet) provider.search(group_root, Shop.DataColumns.id, SortOrder.ASC, pageIndex, pageSize);
            if (cacheable && datas.getObjects().size() != 0) {
                RemoteCache.add(key, KEY_SHOPS, datas, ICache.DAY_FACTOR);
            }
        }
        return datas;
    }

    public static List<Shop> getShops(Integer userId) {
        DataSet<Shop> datas = null;

        datas = Shops.getShops(null, userId, 1, Integer.MAX_VALUE);
        return datas.getObjects();
    }

    public static Shop getShop(Integer id) {
        return getShop(id, true, false);
    }

    public static Shop getShop(Integer id, boolean cacheable, boolean flush) {
        IShopDataProvider provider = null;
        String key = KEY_SHOP + id;
        Shop shop = null;

        if (flush)
            RemoteCache.remove(key);

        shop = (Shop) RemoteCache.get(key);
        if (shop == null) {
            provider = (IShopDataProvider) DataAccess.instance().createProvider("ShopDataProvider");
            shop = (Shop) provider.getEntity(id);

            if (cacheable)
                RemoteCache.add(key, GROUP_KEY_SHOPS, shop);
        }
        return shop;
    }

    public static Integer save(Shop shop) {
        IShopDataProvider provider = DataAccess.instance().getShopDataProvider();
        String key = null;

        Integer id = null;

        id = provider.save(shop);
        if (shop.getId() == null || shop.getId() == 0)
            shop.setId(id);
        Metas.save(shop);

        key = KEY_SHOP + id;
        RemoteCache.remove(key);
        return id;
    }

    public static boolean exists(Integer userId, Integer shopId) {
        IShopDataProvider provider = DataAccess.instance().getShopDataProvider();

        return provider.exists(userId, shopId);
    }
}
