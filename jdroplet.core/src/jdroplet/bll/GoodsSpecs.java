package jdroplet.bll;

import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IGoodsSpecDataProvider;
import jdroplet.data.model.GoodsSpec;
import jdroplet.data.model.Order;
import jdroplet.enums.SortOrder;
import jdroplet.util.JSONUtil;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.util.List;

/**
 * Created by kuibo on 2018/6/28.
 */
public class GoodsSpecs {

    public static String GROUP_KEY_GOODSSPECS = "GOODSSPECS_GROUPS_";

    public static String KEY_GOODSSPEC = "GOODSSPEC_";

    public static Integer save(GoodsSpec goodsSpec) {
        IGoodsSpecDataProvider provider = DataAccess.instance().getGoodsSpecDataProvider();
        Integer id = null;

        id = provider.save(goodsSpec);
        if (goodsSpec.getId() == null) {
            goodsSpec.setId(id);
        } else {
            String key = KEY_GOODSSPEC + goodsSpec.getId();
            RemoteCache.remove(key);
        }
        String groupKey = GROUP_KEY_GOODSSPECS + goodsSpec.getGoodsId();
        RemoteCache.clear(groupKey);
        Logs.save("save-goodsspec-" + goodsSpec.getId(), JSONUtil.toJSONString(goodsSpec));
        return id;
    }

    public static GoodsSpec getGoodsSpec(Integer id) {
        GoodsSpec goodsSpec = null;
        String key = KEY_GOODSSPEC + id;

        goodsSpec = (GoodsSpec) RemoteCache.get(key);
        if (goodsSpec == null) {
            IGoodsSpecDataProvider provider = DataAccess.instance().getGoodsSpecDataProvider();
            String groupKey = null;

            goodsSpec = (GoodsSpec) provider.getEntity(id);

            groupKey = GROUP_KEY_GOODSSPECS + goodsSpec.getGoodsId();
            RemoteCache.add(key, groupKey, goodsSpec);
        }
        return goodsSpec;
    }

    public static GoodsSpec getGoodsSpec(Integer goodsId, Integer status, Integer property1, Integer property2, Integer property3) {
        if (property1 == null && property2 == null && property3 == null)
            return null;

        String key = KEY_GOODSSPEC + goodsId + "-" + status + "-" + property1 + "-" + property2 + "-" + property3;
        GoodsSpec goodsSpec = null;

        goodsSpec = (GoodsSpec) RemoteCache.get(key);
        if (goodsSpec == null) {
            IGoodsSpecDataProvider provider = DataAccess.instance().getGoodsSpecDataProvider();
            String groupKey = GROUP_KEY_GOODSSPECS + goodsId;

            goodsSpec = provider.getGoodsSpec(goodsId, status, property1, property2, property3);
            RemoteCache.add(key, groupKey, goodsSpec);
        }
        return goodsSpec;
    }

    public static List<GoodsSpec> getGoodsSpecs(Integer shopId, Integer goodsId, String searchTerms, Integer status) {
        List<GoodsSpec> datas = null;
        IGoodsSpecDataProvider provider = DataAccess.instance().getGoodsSpecDataProvider();
        SearchQuery query = null;
        SearchGroup group_root, group = null;

        group_root = new SearchGroup();
        group_root.setTerm(SearchGroup.AND);

        //========================
        group = new SearchGroup();
        group.setTerm(SearchGroup.AND);

        if (shopId != null) {
            query = new SearchQuery(GoodsSpec.DataColumns.shopId, shopId, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (goodsId != null) {
            query = new SearchQuery(GoodsSpec.DataColumns.goodsId, goodsId, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (status != null) {
            query = new SearchQuery(GoodsSpec.DataColumns.status, status, SearchQuery.EQ);
            group.addQuery(query);
        }

        if (!TextUtils.isEmpty(searchTerms)) {
            //========================
            group = new SearchGroup();
            group.setTerm(SearchGroup.OR);

            query = new SearchQuery(GoodsSpec.DataColumns.sku, searchTerms, SearchQuery.LIKE);
            group.addQuery(query);

            group_root.addGroup(group);
        }

        group_root.addGroup(group);

        datas = (List) provider.search(group_root, Order.DataColumns.id, SortOrder.ASC);
        return datas;
    }

    public static void updateStatus(Integer goodsId, Integer status) {
        IGoodsSpecDataProvider provider = DataAccess.instance().getGoodsSpecDataProvider();
        String groupKey = GROUP_KEY_GOODSSPECS + goodsId;

        provider.update("goods_id", goodsId, "status", status);
        RemoteCache.clear(groupKey);
    }
}
