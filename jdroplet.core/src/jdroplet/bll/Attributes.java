package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IAttributeDataProvider;
import jdroplet.data.model.Attribute;
import jdroplet.enums.SortOrder;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Attributes Bll类
 * 2018-08-24 16:19:54 bourne
 */

public class Attributes {

    public static String KEY_ATT = "SECTIONATT_";

    public static String KEY_ATTS = "SECTIONATTS_{0}_{1}_{2}_{3}";


    public static Integer save(Attribute entity) {
        IAttributeDataProvider provider = DataAccess.instance().getAttributeDataProvider();
        Integer id = null;

        id = provider.save(entity);
        if (entity.getId() == null || entity.getId() == 0)
            entity.setId(id);

        return id;
    }

    public static Attribute getAttribute(Integer id) {
        IAttributeDataProvider provider = DataAccess.instance().getAttributeDataProvider();

        return (Attribute) provider.getEntity(id);
    }

    public static List<Attribute> getAttributes(Integer sectionId, Boolean searchAble) {
        List<Attribute> list = null;
        String key = null;

        key = MessageFormat.format(KEY_ATTS, sectionId, searchAble);
        list = (List<Attribute>) RemoteCache.get(key);
        if (list == null) {
            IAttributeDataProvider provider = (IAttributeDataProvider) DataAccess.instance().getAttributeDataProvider();
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);


            query = new SearchQuery(Attribute.DataColumns.sectionId, sectionId);
            group_root.addQuery(query);


            if (searchAble != null) {
                query = new SearchQuery(Attribute.DataColumns.searchAble, searchAble ? 1 : 0);
                group_root.addQuery(query);
            }

            list = (List) provider.search(group_root, Attribute.DataColumns.id, SortOrder.ASC);
            if (list.size() != 0) {
                RemoteCache.add(key, list, ICache.DAY_FACTOR);
            }
        }
        return list;
    }

//    public static Map<String, String> args2Map(String args) {
//        String[] kvs = args.split("\\|");
//        Map<String, String> map = new HashMap();
//        for(String str:kvs) {
//            if (TextUtils.isEmpty(str)) continue;
//
//            String[] kv = str.split(":");
//            String key = kv[0];
//            String val = kv.length == 2 ? kv[1] : "";
//
//            map.put(key, val);
//        }
//
//        return map;
//    }
}