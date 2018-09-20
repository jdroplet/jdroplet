package jdroplet.data.idal;


import java.util.List;

import jdroplet.data.model.Page;
import jdroplet.data.model.Site;
import jdroplet.data.model.Cluster;
import jdroplet.data.model.UrlPattern;
import jdroplet.util.DataSet;


public interface ISiteManageDataProvider {

	DataSet<Site> getSites(Integer clusterId, Integer pageIndex, Integer pageSize);
	DataSet<Page> getPages(Integer clusterId, Integer pageIndex, Integer pageSize);
	DataSet<UrlPattern> getUrlPatterns(Integer clusterId, Integer pageId, Integer pageIndex, Integer pageSize);
	DataSet<Cluster> getClusters(Integer pageIndex, Integer pageSize);
 
	Site getSite(Integer siteId);
	Site getSite(String siteUrl);

	Page getPage(Integer pageId);
	UrlPattern getUrlPattern(Integer id);

	Integer addSite(Site site);
	Integer addPage(Page page);

	void updateSite(Site site);
	void updatePage(Page page);

	Integer saveUrlPattern(UrlPattern url);
	
	Cluster getClusterBySite(Integer siteId);
	Cluster getCluster(Integer clusterId);
	Integer saveCluster(Cluster cluster);

	void addSit2Cluster(Integer siteId, Integer clusterId);
	void updateCluster(Integer siteId, Integer clusterId);
}
