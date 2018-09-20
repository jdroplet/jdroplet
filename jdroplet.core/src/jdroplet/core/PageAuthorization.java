package jdroplet.core;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdroplet.bll.SiteManager;
import jdroplet.data.model.UrlPattern;


public class PageAuthorization {
	private Map<String, String[]> allows;
	
	PageAuthorization(Integer clusterId) {
		List<UrlPattern> patterns = SiteManager.getUrlPatterns(clusterId).getObjects();
		String key = null;

		allows = new HashMap<String, String[]>();
		
		for(UrlPattern p:patterns) {
			key = p.getPage().getName() + "." + p.getAction();
			allows.put(key, p.getAllows().split(","));
		}
	}
	
	public boolean validaAccess(String page, String[] roles) {
		// 遍历允许访问的角色
		for(String rstr:allows.get(page)) {
			if("Everyone".equals(rstr))
				return true;

			for(String r:roles) {
				if (r.equals(rstr))
					return true;
			}
		}
		return false;
	}
}
