package jdroplet.bll;

import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IMethodDataProvider;

import java.util.List;


public class Methods<T> {
 
	public static String GROUP_KEY_METHODS = "METHODS_GROUP";
	public static String GROUP_METHODS = "METHODS_GROUP-";
	

	@SuppressWarnings("rawtypes")
	public static List getMethods(String tag) {
		String key = GROUP_KEY_METHODS + tag;
		List methods = (List) AppCache.get(key);
		
		if (methods == null) {
			IMethodDataProvider provider = DataAccess.instance().getMethodDataProvider();
			//methods = provider.getMethods(tag);
			AppCache.add(key, key, methods, ICache.MINUTE_FACTOR);
		}

		return methods;
	}
		
	public static void delete(String tag) {
		IMethodDataProvider provider = DataAccess.instance().getMethodDataProvider();
		String cache_key = GROUP_METHODS + tag;
		
		provider.delete(tag);
		AppCache.remove(cache_key);
	}
	
	public static boolean exists(String tag) {
//		IMethodDataProvider provider = DataAccess.instance().getMethodDataProvider();
//		Boolean exists = null;
//		String cache_key = GROUP_METHODS + tag;
//
//		exists = (Boolean) AppCache.get(cache_key);
//		if (exists == null) {
//			exists = provider.exists(tag);
//			AppCache.add(cache_key, exists, ICache.MINUTE_FACTOR);
//		}
//
//		return exists;

		return false;
	}

//	public static void add(String tag, List<Method> methods) {
//		IMethodDataProvider provider = DataAccess.instance().getMethodDataProvider();
//		provider.add(tag, methods);
//	}
}
