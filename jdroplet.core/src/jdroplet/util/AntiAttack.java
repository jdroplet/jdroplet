package jdroplet.util;

import jdroplet.cache.RemoteCache;
import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.exceptions.ApplicationException;

import org.apache.log4j.Logger;


public class AntiAttack {

	private int maxRequestCount = 2;
	private int interval = 4;
	private boolean antiMultiIPAddress = false;
	
	public int getMaxRequestCount() {
		return maxRequestCount;
	}

	public void setMaxRequestCount(int maxRequestCount) {
		this.maxRequestCount = maxRequestCount;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isAntiMultiIPAddress() {
		return antiMultiIPAddress;
	}

	public void setAntiMultiIPAddress(boolean antiMultiIPAddress) {
		this.antiMultiIPAddress = antiMultiIPAddress;
	}

	public void start() {
		HttpContext context = null;
		HttpRequest request = null;
		String count_key = null;
		Integer count = null;
		Logger logger = Logger.getLogger(this.getClass());
		
		context = HttpContext.current();
		request = context.request();
		
		if (antiMultiIPAddress)
			count_key = "attack_" + request.getRequestUri();
		else
			count_key = "attack_" + request.getRequestUri() + "_"
					+ request.getXRemoteAddr();
		
		
		count = (Integer)RemoteCache.get(count_key);
		if (count == null)
			count = 1;
		else
			count++;

		
		if (count > maxRequestCount) {
			if (antiMultiIPAddress)
				logger.error(request.getRequestUri());
			else
				logger.error(request.getRequestUri() + "_"
						+ request.getXRemoteAddr());
			throw new ApplicationException("ServiceDenial");
		}

		RemoteCache.add(count_key, count, interval);
	}
}
