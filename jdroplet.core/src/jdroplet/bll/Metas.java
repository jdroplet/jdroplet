package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IMetaDataProvider;
import jdroplet.data.model.Meta;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;

import java.lang.reflect.Type;


public class Metas {

	public static void save(Meta meta) {
		IMetaDataProvider provider = DataAccess.instance().getMetaDataProvider();
		provider.save(meta);
	}

	public static void save(Class<?> clazz, Integer itemId, String key, Object value) {
		IMetaDataProvider provider = null;
		String str = null;

		if (itemId == null || itemId == 0 || TextUtils.isEmpty(key) || value == null)
			return;

		str = JSONUtil.toJSONString(value);
		provider = DataAccess.instance().getMetaDataProvider();
		provider.save(clazz, itemId, key, str);
	}

	public static String getValue(Class<?> clazz, Integer itemId, String key) {
		IMetaDataProvider provider = null;
		provider = DataAccess.instance().getMetaDataProvider();
		return provider.getValue(clazz, itemId, key);
	}

	public static Object getValue(Class<?> clazz, Integer itemId, String key, Type type) {
		IMetaDataProvider provider = null;
		String str = null;

		provider = DataAccess.instance().getMetaDataProvider();
		str = provider.getValue(clazz, itemId, key);
		return JSONUtil.toObject(str, type);
	}

	public static void remove(Class<?> clazz, Integer itemId) {
 		remove(clazz, itemId, null);
	}

	public static void remove(Class<?> clazz, Integer itemId, String key) {
		if (itemId == null || itemId == 0)
			return;

		if (TextUtils.isEmpty(key))
			return;

		IMetaDataProvider provider = null;
		provider = DataAccess.instance().getMetaDataProvider();
		provider.remove(clazz, itemId, key);
	}
}
