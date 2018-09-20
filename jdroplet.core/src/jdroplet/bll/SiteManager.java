package jdroplet.bll;

import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.DateTime;
import jdroplet.core.HttpContext;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ISiteManageDataProvider;
import jdroplet.data.model.Cluster;
import jdroplet.data.model.Page;
import jdroplet.data.model.Site;
import jdroplet.data.model.UrlPattern;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SiteManager {	

	public static String GROUP_URL = "site-GROUP_URL";
	public static String GROUP_CLUSTER = "site-GROUP_CLUSTER";
	public static String GROUP_PAGE = "site-GROUP_PAGE";
	public static String GROUP_SITE = "site-GROUP_SITE";

	public static String KEY_URLS = "site-urls_id-{0}_pid-{1}_idx-{2}_size-{3}";
	public static String KEY_URL = "site-url_sid-{0}";
	public static String KEY_CLUSTER = "site-cluster_id{0}";
	public static String KEY_CLUSTERS = "site-clusters_idx-{0}_size-{1}";
	public static String KEY_CLUSTER_SITE = "site-clusters_site-{0}";
	public static String KEY_SITE_URL = "site-site_url-{0}";
	public static String KEY_SITE = "site-site_id-{0}";
	public static String KEY_SITES_CLUSTERS = "site-sites_sid-{0}";
	public static String KEY_SITES = "site-sites_cluster-{0}_idx-{1}_size-{2}";
	public static String KEY_SITES_PAGE = "site-sites-pid-{0}";
	public static String KEY_PAGES = "site-pages-sid-{0}-idx-{1}-siez-{2}";
	public static String KEY_PAGE = "site-pid-{0}";


	public static Page getPage(Integer pageId) {
		String key = MessageFormat.format(KEY_PAGE, pageId);
		Page page = null;

		page = (Page)AppCache.get(key);
		if (page == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			page = provider.getPage(pageId);
			AppCache.add(key, GROUP_PAGE, page);
		}
		return page;
	}

	public static DataSet<Page> getPages(Integer clusterId) {
		return getPages(clusterId, 1, Integer.MAX_VALUE);
	}

	@SuppressWarnings("unchecked")
	public static DataSet<Page> getPages(Integer clusterId, Integer pageIndex, Integer pageSize) {
		String key = MessageFormat.format(KEY_PAGES, clusterId, pageIndex, pageSize);
		DataSet<Page> data = null;

		data = (DataSet<Page>)AppCache.get(key);
		if (data == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			data = provider.getPages(clusterId, pageIndex, pageSize);
			data = (DataSet<Page>) PluginFactory.getInstance().applyFilters("SiteManager@getPages", data, clusterId);
			AppCache.add(key, GROUP_PAGE, data);
		}
		return data;
	}

	public static Map<String, Page> getPageMap(Integer clusterId) {
		Map<String, Page> map = null;
		List<Page> refers = null;

		refers = getPages(clusterId).getObjects();
		map = new Hashtable<String, Page>();
		for (Page pf:refers) {
			map.put(pf.getName(), pf);
		}
		return map;
	}

	public static Page addPage(Integer clusterId, String name, String refer) {
		Page page = new Page();

		page.setName(name);
		page.setRefer(refer);
		page.setUserId(Users.getCurrentUser().getId());
		page.setDateCreated(new Date(DateTime.now().getTime()));
		SiteManager.addPage(page);

		return page;
	}

	public static Integer addPage(Page page) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		Integer result = provider.addPage(page);
		AppCache.clear(GROUP_PAGE);
		return result;
	}

	public static void updatePage(Page page) {
		Page old = getPage(page.getId());
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		//Logs.add(old, LogType.UpdatePage);
		provider.updatePage(page);
		AppCache.clear(GROUP_PAGE);
	}

	public static void deletePage(Page page) {
		//provider.deletePage(page, DataProviderAction.Delete);
		//AppCache.clear(GROUP_PAGE);
	}

	public static Site addSite(Integer clusterId, String appRoot, String name) {
		Site site = null;

		site = new Site();
		site.setAppRoot(appRoot);
		site.setDescription(name);
		site.setClusterId(clusterId);

		site.setUserId(Users.getCurrentUser().getId());
		site.setDateCreated(new Date(DateTime.now().getTime()));
		SiteManager.addSite(site);
		return site;
	}

	public static Integer addSite(Site site) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		Integer id = provider.addSite(site);

		site.setId(id);
		addSiteToCluster(id, site.getClusterId());
		AppCache.clear(GROUP_SITE);
		return id;
	}

	public static void updateSite(Site site) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		provider.updateSite(site);
		AppCache.clear(GROUP_SITE);
	}

	public static void addSiteToCluster(Integer siteId, Integer clusterId) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		provider.addSit2Cluster(siteId, clusterId);
		AppCache.clear(GROUP_SITE);
	}

	public static void updateSiteToCluster(Integer siteId, Integer clusterId) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		provider.updateCluster(siteId, clusterId);
		AppCache.clear(GROUP_SITE);
	}

	public static Site getSite(String siteRoot) {
		String key = MessageFormat.format(KEY_SITE_URL, siteRoot);
		Site site = null;

		site = (Site)AppCache.get(key);
		if (site == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			site = provider.getSite(siteRoot);
			AppCache.add(key, GROUP_SITE, site);
		}
		return site;
	}

	public static Site getSite(Integer siteId) {
		String key = MessageFormat.format(KEY_SITE, siteId);
		Site site = null;

		site = (Site)AppCache.get(key);
		if (site == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			site = provider.getSite(siteId);
			AppCache.add(key, GROUP_SITE, site);
		}
		return site;
	}

	@SuppressWarnings("unchecked")
	public static DataSet<Site> getSites(Integer clusterId, Integer pageIndex, Integer pageSize) {
		String key = MessageFormat.format(KEY_SITES, clusterId, pageIndex, pageSize);
		DataSet<Site> sites = null;

		sites = (DataSet<Site>)AppCache.get(key);
		if (sites == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			sites = provider.getSites(clusterId, pageIndex, pageSize);
			AppCache.add(key, GROUP_SITE, sites, ICache.DAY_FACTOR);
		}
		return sites;
	}

	public static Site getCurrentSite() {
		return getCurrentSite(HttpContext.current().getSiteRootUrl());
	}

	public static Site getCurrentSite(String siteUrl) {
    	String key = MessageFormat.format(KEY_SITE_URL, siteUrl);
        Site site = (Site)AppCache.get(key) ;

        if (site == null) {
        	ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
        	site = provider.getSite(siteUrl);
            AppCache.add(key, GROUP_SITE, site);
        }
        return site;
	}

	public static Integer saveCluster(Cluster cluster) {
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		Integer result = provider.saveCluster(cluster);
		Metas.save(cluster);
		AppCache.clear(GROUP_CLUSTER);
		return result;
	}

	public static DataSet<Cluster> getClusters(Integer pageIndex, Integer pageSize) {
		String key = MessageFormat.format(KEY_CLUSTERS, pageIndex, pageSize);
		DataSet<Cluster> clusters = (DataSet<Cluster>)AppCache.get(key);

		if (clusters == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			clusters = provider.getClusters(pageIndex, pageSize);
			AppCache.add(key, GROUP_CLUSTER, clusters);
		}
		return clusters;
	}

	public static Cluster getCurrentCluster() {
		return getCurrentSite().getCluster();
	}

	public static Cluster getClusterBySite(Integer siteId) {
		String key = MessageFormat.format(KEY_CLUSTER_SITE, siteId);
		Cluster clusters = (Cluster)AppCache.get(key);

		if (clusters == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			clusters = provider.getClusterBySite(siteId);
			AppCache.add(key, GROUP_CLUSTER, clusters);
		}
		return clusters;
	}

	public static Cluster getCluster(Integer clusterId) {
		String key = MessageFormat.format(KEY_CLUSTER, clusterId);
		Cluster clusters = (Cluster)AppCache.get(key);

		if (clusters == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			clusters = provider.getCluster(clusterId);
			AppCache.add(key, GROUP_CLUSTER, clusters);
		}
		return clusters;
	}
	
	
	public static Integer saveUrlPattern(UrlPattern url) {
		Integer id = null;
		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
		id = provider.saveUrlPattern(url);
		if (url.getId() == null || url.getId() <= 0)
			url.setId(id);
		AppCache.clear(GROUP_URL);
		return id;
	}

	public static Integer deleteUrlPattern(UrlPattern url) {
		return null;
	}
	
    public static UrlPattern getUrlPattern(Integer id) {
    	String key = MessageFormat.format(KEY_URL, id);
    	UrlPattern url = (UrlPattern)AppCache.get(key) ;
    	
    	if (url == null) {
    		ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
    		url = provider.getUrlPattern(id);
    		AppCache.add(key, GROUP_URL, url, ICache.MONTH_FACTOR);
    	}
    	return url;
    }
    
    public static DataSet<UrlPattern> getUrlPatterns(Integer clusterId) {
		return getUrlPatterns(clusterId, 1, Integer.MAX_VALUE);
	}
    
    public static DataSet<UrlPattern> getUrlPatterns(Integer clusterId, Integer pageIndex, Integer pageSize) {
		return getUrlPatterns(clusterId, 0, 1, Integer.MAX_VALUE);
	}
    
    @SuppressWarnings("unchecked")
    public static DataSet<UrlPattern> getUrlPatterns(Integer clusterId, Integer pageId, Integer pageIndex, Integer pageSize) {
		String key = MessageFormat.format(KEY_URLS, clusterId, pageId, pageIndex, pageSize);
		DataSet<UrlPattern> datas = (DataSet<UrlPattern>)AppCache.get(key);
		
		if (datas == null) {
			ISiteManageDataProvider provider = DataAccess.instance().getSiteManageDataProvider();
			datas = provider.getUrlPatterns(clusterId, pageId, pageIndex, pageSize);
			datas = (DataSet<UrlPattern>) PluginFactory.getInstance().applyFilters("SiteManager@getUrlPatterns", datas, clusterId, pageId);
			AppCache.add(key, GROUP_URL, datas, ICache.MONTH_FACTOR);
		}
		return datas;
	}
}
