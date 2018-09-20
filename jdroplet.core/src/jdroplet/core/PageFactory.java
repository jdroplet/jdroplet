package jdroplet.core;


import java.util.Map;

import jdroplet.bll.SiteManager;
import jdroplet.data.model.Site;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.PageNotFoundException;
import jdroplet.plugin.PluginFactory;


public class PageFactory {
	
	private static PageFactory factory = null;
	private PageFactory() {
		factory = this;
	}
	
	public static PageFactory instance() {
		if (factory == null)
			factory = new PageFactory();
		return factory;
	}
	
	private String getQualifiedClassName(String siteRoot, String pagename) {
		Map<String, jdroplet.data.model.Page> pages = null;
		Site site = SiteManager.getSite(siteRoot);
		
		pages = SiteManager.getPageMap(site.getCluster().getId());
		return pages.get(pagename).getRefer();
	}
	
	public Page getPage(String siteRoot, String pagename) {
		Page page = null;
		String refer = null;
		
		if (pagename == null)
			throw new PageNotFoundException();
		
		refer = getQualifiedClassName(siteRoot, pagename);
		try {
			//page = (Page) Class.forName(refer).newInstance();
			page = (Page) PluginFactory.getInstance().getClassLoader().loadClass(refer).newInstance();
		} catch (Exception ex) {
			throw new ApplicationException(ex.getMessage());
		}
		return page;
	}
}
