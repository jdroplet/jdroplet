package jdroplet.sns.proxy;

import jdroplet.core.SystemConfig;
import org.apache.log4j.Logger;

public class ProxyFactory {
	
	public static Proxy getProxy(String name) {
		Proxy proxy = null;
		String clazz = null;

		clazz = SystemConfig.getProperty("sns.proxy." + name);
		try {
			proxy = (Proxy)Class.forName(clazz).newInstance();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(ProxyFactory.class);
			logger.error("ProxyFactory@Proxy:", e);
		}
		return proxy;
	}
}