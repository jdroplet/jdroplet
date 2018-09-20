package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IAttributeDataProvider;
import jdroplet.data.idal.IAttributeValueDataProvider;
import jdroplet.data.model.Attribute;
import jdroplet.data.model.AttributeValue;
import jdroplet.data.model.Post;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * PostAttributeValues Bll类
 * 2018-08-30 17:04:38 bourne
 */

public class PostAttributeValues {

    public static String KEY_ATTRIBUTE_VALUES = "ATT_VALUES_{0}_{1}_{2}_{3}_{4}";

    public static Integer save(AttributeValue entity) {
        IAttributeValueDataProvider provider = DataAccess.instance().getAttributeValueDataProvider();
        Integer id = null;

        id = provider.save(entity);
        if (entity.getId() == null || entity.getId() == 0)
            entity.setId(id);

        return id;
    }

    public static DataSet<Integer> getItemIds(List<AttributeValue> avs, Integer sectionId, Integer cityId, String type, Integer pageIndex, Integer pageSize) {
        DataSet<Integer> datas = null;
        String key = "";

        List<String> joins = new ArrayList<>();
        for(AttributeValue av:avs) {
            key += av.getAttributeId() + "-" + av.getSlug();
        }
        key = MessageFormat.format(KEY_ATTRIBUTE_VALUES, cityId, sectionId, type, key, pageIndex, pageSize);
        datas = (DataSet<Integer>) RemoteCache.get(key);
        if (datas == null) {
            IAttributeValueDataProvider provider = DataAccess.instance().getAttributeValueDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (sectionId != null) {
                query = new SearchQuery(Post.DataColumns.sectionId, sectionId);
                group_root.addQuery(query);
            }

            if (cityId != null) {
                query = new SearchQuery(Post.DataColumns.cityId, cityId);
                group_root.addQuery(query);
            }

            datas = provider.getItemIds(avs, group_root, type, pageIndex, pageSize);
            if (datas.getObjects().size() != 0)
                RemoteCache.add(key, datas, ICache.DAY_FACTOR);
        }
        return datas;
    }

    public static List<AttributeValue> getAttributeValues(Integer itemId) {
        List<AttributeValue> list = null;
        String key = null;

        key = MessageFormat.format(KEY_ATTRIBUTE_VALUES, itemId);
        list = (List<AttributeValue>) RemoteCache.get(key);
        if (list == null) {
            IAttributeValueDataProvider provider = DataAccess.instance().getAttributeValueDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            query = new SearchQuery(AttributeValue.DataColumns.itemId, itemId);
            group_root.addQuery(query);

            list = (List) provider.search(group_root, AttributeValue.DataColumns.attributeId, SortOrder.ASC);
            if (list.size() != 0) {
                RemoteCache.add(key, list, ICache.DAY_FACTOR);
            }
        }
        return list;
    }
}