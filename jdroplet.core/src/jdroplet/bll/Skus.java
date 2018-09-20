package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ISkuDataProvider;
import jdroplet.data.model.GoodsSpec;
import jdroplet.data.model.ProductAtt;
import jdroplet.enums.SortOrder;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;


import java.text.MessageFormat;
import java.util.List;

/**
 * Created by kuibo on 2018/1/20.
 */
public class Skus {

    public static String KEY_SKUS = "SHOP_SKUS_{0}";
    public static String KEY_SKU_ATTS = "SHOP_ATTS_{0}_{1}";
    
    public static GoodsSpec getSku(Integer id) {
        ISkuDataProvider provider = null;

        provider = DataAccess.instance().getSkuDataProvider();
        return (GoodsSpec) provider.getEntity(id);
    }

    public static Integer save(GoodsSpec goodsSpec) {
        ISkuDataProvider provider = DataAccess.instance().getSkuDataProvider();
        String key = null;

        Integer id = Integer.valueOf(0);

        id = provider.save(goodsSpec);
        if (goodsSpec.getId() == null)
            goodsSpec.setId(id);
        Metas.save(goodsSpec);

        //key = MessageFormat.format(KEY_SKUS, goodsSpec.getProductId());
        RemoteCache.remove(key);
        return id;
    }

    public static List<GoodsSpec> getSkus(Integer productId, Integer status) {
        List<GoodsSpec> list = null;
        String key = null;

        key = MessageFormat.format(KEY_SKUS, productId);
        list = (List<GoodsSpec>) RemoteCache.get(key);
        if (list == null) {
            ISkuDataProvider provider = null;
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            //query = new SearchQuery(GoodsSpec.DataColumns.productId, productId, SearchQuery.LIKE);
            group_root.addQuery(query);

 

            provider = DataAccess.instance().getSkuDataProvider();
            list = (List) provider.search(group_root, GoodsSpec.DataColumns.id, SortOrder.ASC);
            if (list.size() != 0) {
                for(GoodsSpec goodsSpec :list) {
                    List<ProductAtt> atts = null;
                    //goodsSpec.setAtts(atts);
                }
                RemoteCache.add(key, list, ICache.DAY_FACTOR);
            }
        }
        return list;
    }

    public static List<ProductAtt> getAtts(Integer productId, Integer skuId) {
        List<ProductAtt> atts = null;
        String cacheKey = null;

        cacheKey = MessageFormat.format(KEY_SKU_ATTS, productId, skuId);
        atts = (List<ProductAtt>) RemoteCache.get(cacheKey);
        if (atts == null) {
            ISkuDataProvider provider = DataAccess.instance().getSkuDataProvider();

            atts = provider.getAtts(productId, skuId);
            RemoteCache.add(cacheKey, atts);
        }
        return atts;
    }
}
