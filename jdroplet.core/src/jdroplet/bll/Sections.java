package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ISectionDataProvider;
import jdroplet.data.model.Cluster;
import jdroplet.data.model.Section;
import jdroplet.data.model.User;
import jdroplet.enums.Permission;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.exceptions.ApplicationException;
import jdroplet.security.Permissions;
import jdroplet.util.DataSet;
import jdroplet.util.PinyinUtil;
import jdroplet.util.TextUtils;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Sections {
	public static String GROUP_KEY_SECTIONS = "SECTIONS_GROUPS-s:{0}_uid:{1}";
	public static String GROUP_KEY_OBJECT_SECTIONS = "SECTIONS_OBJECT_SECTIONS-obj:{0}_type:{0}";

	public static String KEY_SECTION = "SECTIONS_id:{0}";
	public static String KEY_SECTIONS = "SECTIONS_s:{0}_uid:{1}_pid:{2}_type:{3}_pi:{4}_ps:{5}_ig:{6}_{7}_{8}";
	public static String KEY_OBJECT_SECTIONS = "SECTIONS_object:{0}_type:{1}";

	//这个缓存key仅仅是用于查询使用，没有前段的修改活动的key挂钩，所以缓存的时间，会比较短
	public static String QUERY_KEY_SECTIONS="SECTIONS_ids-{0}";

    public static Integer save(Section section) {
    	String cache_group = null;
    	ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
		Integer id = null;

		if (TextUtils.isEmpty(section.getSlug()))
			section.setSlug(PinyinUtil.toPinYin(section.getName()));

		id = provider.save(section);
		Metas.save(section);
		if (section.getId() == null || section.getId() == 0) {
			section.setId(id);
		} else {
			String key = MessageFormat.format(KEY_SECTION, section.getId());

			RemoteCache.remove(key);
		}
		cache_group = MessageFormat.format(GROUP_KEY_SECTIONS, section.getShopId(), section.getUserId());
		RemoteCache.clear(cache_group);
		return id;
    }

    public static Section getSection (Integer sectionId) {
        return getSection(sectionId, true);
    }

    public static Section getSection (Integer sectionId, boolean ignorePermissions) {
    	return getSection(sectionId, ignorePermissions, true);
    }

    public static Section getSection (Integer sectionId, boolean ignorePermissions, boolean cacheable) {
        return getSection(sectionId, ignorePermissions, cacheable, false);
    }

    public static Section getSection (Integer sectionId, boolean ignorePermissions, boolean cacheable, boolean flush) {
    	if (sectionId == null || sectionId == 0)
    		return null;

    	Section section = null;
    	String group_key = null;
    	String key = MessageFormat.format(KEY_SECTION, sectionId);

    	if (flush || !cacheable)
    		RemoteCache.remove(key);
    	else
    		section = (Section)RemoteCache.get(key);

		if (section == null) {
			ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
			section = (Section) provider.getEntity(sectionId);

			if (cacheable) {
				group_key = MessageFormat.format(GROUP_KEY_SECTIONS, sectionId, section.getUserId());
				RemoteCache.add(key, group_key, section, ICache.DAY_FACTOR);
			}
		}

    	if (section == null)
    		throw new ApplicationException("SectionNotFound s:" + sectionId);

    	if (ignorePermissions) {
    		return section;
    	} else {
    		User user = Users.getCurrentUser();

    		if( Permissions.validatePermissions(section, Permission.View, user) )
				if(user.isAdministrator())
					return section;
    	}

    	throw new ApplicationException("AccessDeny s:" + sectionId);
    }

    public static Section getSection(Integer shopId, Integer userId, String type) {
    	return getSection(shopId, userId, null, type);
    }

    public static Section getSection(Integer shopId, Integer userId, Integer parentId, String type) {
    	return getSection(shopId, userId, parentId, type, false);
    }

    public static Section getSection(Integer shopId, Integer userId, Integer parentId, String type, boolean ignorePermissions) {
    	DataSet<Section> datas = null;
    	Section section = null;

    	datas = getSections(shopId, userId, parentId, type,  SortSectionBy.ID, SortOrder.DESC, 1, 1, ignorePermissions, true, false);
    	if (datas != null && datas.getObjects().size() != 0)
    		section = datas.getObjects().get(0);
    	else
    		throw new ApplicationException("SectionNotFound s:" + shopId + "_u:" + userId + "_p:" + parentId + "_t:" + type + "_p:" + ignorePermissions);

    	return section;
    }

	public static DataSet<Section> getSections(Integer shopId, Integer parentId, String type, Integer pageIndex, Integer pageSize) {
		return getSections(shopId, null, parentId, type, SortSectionBy.ID, SortOrder.ASC, pageIndex, pageSize, true, true, false);
	}

	public static DataSet<Section> getSections(Integer shopId, Integer parentId, String type, SortSectionBy sortBy, SortOrder sortOrder, Integer pageIndex, Integer pageSize) {
		return getSections(shopId, null, parentId, type, sortBy, sortOrder, pageIndex, pageSize, true, true, false);
	}

    public static DataSet<Section> getSections(Integer shopId, Integer userId, Integer parentId, String type,
											   SortSectionBy sortBy, SortOrder sortOrder,
											   Integer pageIndex, Integer pageSize, boolean ignorePermissions, boolean cacheable, boolean flush) {
    	DataSet<Section> unfilteredDatas	= null;
		String cacheKey = MessageFormat.format(KEY_SECTIONS, shopId, userId, parentId, type, pageIndex, pageSize,
				ignorePermissions, sortBy, sortOrder);

		flush = true;
		if (flush)
			RemoteCache.remove(cacheKey);

        // 从系统缓存中取出未筛选过的板块, 并添加到缓存
        unfilteredDatas = (DataSet<Section>) RemoteCache.get(cacheKey);
		if (unfilteredDatas == null) {
        	ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
            unfilteredDatas = provider.getSections(shopId, userId, parentId, type, sortBy, sortOrder, pageIndex, pageSize);
            if (unfilteredDatas.getObjects().size() == 0) {
            	return unfilteredDatas;
            }
			String cacheGroup = MessageFormat.format(GROUP_KEY_SECTIONS, shopId, userId);
            if (cacheable)
            	RemoteCache.add(cacheKey, cacheGroup, unfilteredDatas, ICache.DAY_FACTOR);
        }

        if (ignorePermissions) {
            //当设置忽视权限验证时候
            return unfilteredDatas;
        }

        return unfilteredDatas;
    }
    
    public static List<Section> getSections(Integer objectId, String type) {
		List<Section> sections = null;
		String key = MessageFormat.format(KEY_OBJECT_SECTIONS, objectId, type);

		sections = (List<Section>) RemoteCache.get(key);
		if (sections == null) {
			ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
			List<Integer> ids = provider.getSections(objectId, type);

			if (ids.size() > 0) {
				sections = new ArrayList<>();
				for(Integer id:ids) {
					sections.add(getSection(id, true));
				}

				RemoteCache.add(key, sections, ICache.HOUR_FACTOR);
			}
		}
		return sections;
	}

	public static void add(Integer objectId, String type, List<Integer> sectionIds) {
		Integer[] ids = new Integer[sectionIds.size()];
		for(int i=0; i<ids.length; i++) {
			ids[i] = sectionIds.get(i);
		}
		add(objectId, type, ids);
	}

	public static void add(Integer objectId, String type, Integer[] sectionIds) {
		ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
		String key = MessageFormat.format(KEY_OBJECT_SECTIONS, objectId, type);

		provider.add(objectId, type, sectionIds);
		RemoteCache.remove(key);
	}

	public static void remove(Integer objectId, String type) {
		ISectionDataProvider provider = DataAccess.instance().getSectionProvider();
		String key = MessageFormat.format(KEY_OBJECT_SECTIONS, objectId, type);

		provider.remove(objectId, type);
		RemoteCache.remove(key);
	}

	public static Integer exists(Integer sectionId, Integer objectId, String type) {
		ISectionDataProvider provider = DataAccess.instance().getSectionProvider();

		return provider.exists(sectionId, objectId, type);
	}

	public static Integer getSectionByName(String type, String name) {
		ISectionDataProvider provider = DataAccess.instance().getSectionProvider();

		return provider.nameExists(type, name);
	}

	public static Integer getSectionBySlug(String type, String slug) {
		ISectionDataProvider provider = DataAccess.instance().getSectionProvider();

		return provider.slugExists(type, slug);
	}
}
