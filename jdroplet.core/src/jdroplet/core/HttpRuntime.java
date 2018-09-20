package jdroplet.core;

import jdroplet.cache.GuavaCache;
import jdroplet.cache.ICache;

public class HttpRuntime {
	private static HttpRuntime runtime;
	
	private ICache cache;
	
	static {
		runtime = new HttpRuntime();//this;
	}
	
	private HttpRuntime() {
		cache = new GuavaCache();
	}
	
	public static ICache cache() {
		return runtime.cache;
	}
}
