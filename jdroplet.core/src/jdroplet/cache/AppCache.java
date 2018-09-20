package jdroplet.cache;


import java.util.Collection;

import jdroplet.core.HttpRuntime;


public class AppCache {
	private static ICache _cache;
	
	private AppCache() {}
	static {
		_cache = HttpRuntime.cache();
	}
	
	public static void add(String key, Object value) {
		add(key, value, ICache.DAY_FACTOR);
	}
		
	public static void add(String key, Object value, int seconds) {
		_cache.add(key, value, seconds);
	}
	
	public static void add(String key, String group, Object value) {
		add(key, group, value, ICache.DAY_FACTOR);
	}
	
	public static void add(String key, String group, Object value, int seconds) {
		add(key, new String[]{group}, value, seconds);
	}
	
	public static void add(String key, String[] groups, Object value) {
		add(key, groups, value, ICache.DAY_FACTOR);
	}
	public static void add(String key, String[] groups, Object value, int seconds) {
		_cache.add(key, groups, value, seconds);
	}
	
	public static void remove(String key) {
		_cache.remove(key);
	}
	
	public static void clear() {
		_cache.clear();
	}
	
	public static void clear(String group) {
		_cache.clear(group);		
	}
	
	public static Object get(String key) {
		return _cache.get(key);
	}
	
	public static Collection<String> getKeys() {
		return _cache.getKeys();
	}
	
	public static Collection<Object> getValues() {
		return _cache.getValues();
	}
}
