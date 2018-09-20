package jdroplet.bll;

import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IGoodsDataProvider;
import jdroplet.data.idal.IOrderDataProvider;
import jdroplet.data.model.Goods;
import jdroplet.data.model.Order;
import jdroplet.enums.SortOrder;
import jdroplet.util.*;

/**
 * Created by kuibo on 2018/6/25.
 */
public class GoodsX {
    public static String GROUP_KEY_GOODSS = "GOODSS_GROUPS";

    public static String KEY_GOODS = "GOODS_";

    public static String KEY_GOODSS = "GOODSS_{0}_{1}";

    public static Integer save(Goods goods) {
        IGoodsDataProvider provider = DataAccess.instance().getGoodsDataProvider();
        Integer id = null;

        id = provider.save(goods);
        if (goods.getId() == null) {
            goods.setId(id);
        } else {
            String key = KEY_GOODS + goods.getId();
            RemoteCache.remove(key);
        }
        RemoteCache.clear(KEY_GOODSS);

        Logs.save("save-goos-" + goods.getId(), JSONUtil.toJSONString(goods));
        return id;
    }

    public static Goods getGoods(Integer id) {
        Goods goods = null;
        String key = KEY_GOODS + id;

        goods = (Goods) RemoteCache.get(key);
        if (goods == null) {
            IGoodsDataProvider provider = DataAccess.instance().getGoodsDataProvider();

            goods = (Goods) provider.getEntity(id);
            RemoteCache.add(key, goods);
        }
        return goods;
    }

    public static DataSet<Goods> getGoods(Integer shopId, Integer sectionId1, Integer sectionId2, Integer sectionId3,
                                          String keyword, Integer status, Integer pageIndex, Integer pageSize) {
        DataSet<Goods> datas = null;
        IGoodsDataProvider provider = DataAccess.instance().getGoodsDataProvider();
        SearchQuery query = null;
        SearchGroup group_root, group = null;

        group_root = new SearchGroup();
        group_root.setTerm(SearchGroup.AND);

        //========================
        group = new SearchGroup();
        group.setTerm(SearchGroup.AND);

        if (shopId != null) {
            query = new SearchQuery(Goods.DataColumns.shopId, shopId, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (sectionId1 != null) {
            query = new SearchQuery(Goods.DataColumns.sectionId1, sectionId1, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (sectionId2 != null) {
            query = new SearchQuery(Goods.DataColumns.sectionId2, sectionId2, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (sectionId3 != null) {
            query = new SearchQuery(Goods.DataColumns.sectionId3, sectionId3, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (status != null) {
            query = new SearchQuery(Goods.DataColumns.status, status, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (!TextUtils.isEmpty(keyword)) {
            //========================
            group = new SearchGroup();
            group.setTerm(SearchGroup.OR);

            query = new SearchQuery(Goods.DataColumns.name, keyword, SearchQuery.LIKE);
            group.addQuery(query);

            group_root.addGroup(group);
        }
        group_root.addGroup(group);

        datas = (DataSet) provider.search(group_root, Order.DataColumns.id, SortOrder.DESC, pageIndex, pageSize);
        return datas;
    }
}
