package jdroplet.data.mysqidal;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdroplet.core.DateTime;
import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.ISiteManageDataProvider;
import jdroplet.data.model.*;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;

import org.apache.log4j.Logger;

public class SiteManageDataProvider extends DataProvider implements ISiteManageDataProvider {

	@Override
	public Entity newEntity() {
		return null;
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
	}

	@Override
	public String getTable() {
		return "jdroplet_books";
	}

	@Override
	public String[] getColums() {
		return null;
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return null;
	}


	public Cluster getClusterBySite(Integer siteId) {
		Cluster cluster = null;
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		
		cls = new String[] {"ss.id,ss.name"};
		clause = "s.id=?";
		args = new Object[] { siteId };
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_clusters ss JOIN jdroplet_sitemappings sm ON sm.cluster_id = ss.id JOIN jdroplet_sites s ON s.id = sm.site_id", cls, clause, args, null, null, null);
			
			if (rs.next()) {
				cluster = readCluster(rs);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return cluster;
	}
	
	@Override
	public Cluster getCluster(Integer id) {
		Cluster cluster = null;
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		
		cls = new String[] {"ss.id,ss.name"};
		clause = "ss.id=?";
		args = new Object[] { id };
		
		db = new SQLDatabase();
		
		try {
			rs = db.query("jdroplet_clusters ss", cls, clause, args, null, null, null);
			if (rs.next()) {
				cluster = readCluster(rs);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return cluster;
	}
		
	public DataSet<Cluster> getClusters(Integer pageIndex, Integer pageSize) {
		DataSet<Cluster> datas = new DataSet<Cluster>();
		ArrayList<Cluster> list = new ArrayList<Cluster>();
		SQLDatabase db1 = null;
		SQLDatabase db2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		String limit = null;
		
		cls1 = new String[] {"id,name"};
		cls2 = new String[] {"COUNT(0)"};
		
		limit = (pageIndex - 1) * pageSize + "," + pageSize;
		
		datas.setObjects(list);
		
		db1 = new SQLDatabase();
		try {
			rs1 = db1.query("jdroplet_clusters", cls1, null, null, null, null, null, limit);
			while (rs1.next()) {
				list.add(readCluster(rs1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}
		
		db2 = new SQLDatabase();
		try {
			rs2 = db2.query("jdroplet_clusters", cls2, null, null, null, null, null);
			if (rs2.next()) {
				datas.setTotalRecords(rs2.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db2.close();
		}
		
		return datas;
	}

	public Page getPage(Integer id) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		Page page = null;
		
		cls = new String[] { "id,`name`,refer,user_id,date_created" };
		clause = "id=?";
		args = new Object[] { id };
		
		db = new SQLDatabase();
		
		try {
			rs = db.query("jdroplet_page_pages", cls, clause, args, null, null, null);
			if (rs.next()) {
				page = readPage(rs);
			} else {
				throw new ApplicationException("PageNotFound");
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}      
		return page;
	}
	
	public DataSet<Page> getPages(Integer clusterId, Integer pageIndex, Integer pageSize) {
		DataSet<Page> datas = new DataSet<Page>();
		ArrayList<Page> pages = new ArrayList<Page>();
		SQLDatabase db1 = null;
		SQLDatabase db2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		Object args[] = null;
		String clause = null;
		String limit = null;
		String table = null;
		
		cls1 = new String[] {"DISTINCT(name),p.id,refer,user_id,date_created"};
		cls2 = new String[] {"COUNT(0)"};
		
		if (clusterId != null) {
			clause = "pu.cluster_id=?";
			args = new Object[] { clusterId };
			table = "jdroplet_page_pages p join jdroplet_page_urlpatterns pu on p.id = pu.page_id";
		} else {
			table = "jdroplet_page_pages p";
		}
		limit = (pageIndex - 1) * pageSize + "," + pageSize;
		
		db1 = new SQLDatabase();
		try {
			rs1 = db1.query(table, cls1, clause, args, null, null, null, limit);
			while (rs1.next()) {
				pages.add(readPage(rs1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}
		
		db2 = new SQLDatabase();
		try {
			rs2 = db2.query(table, cls2, clause, args, null, null, null);
			if (rs2.next()) {
				datas.setTotalRecords(rs2.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db2.close();
		}
		datas.setObjects(pages);
		return datas;
	}

	public List<Site> getSites(Integer clusterId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		ArrayList<Site> sites = null;
		
		sites = new ArrayList<Site>();
		cls = new String[] {"s.*"};
		clause = "sm.clusterId=?";
		args = new Object[] {clusterId};
		
		db = new SQLDatabase();
	
		try {
			rs = db.query("jdroplet_sites s JOIN jdroplet_sitemappings sm ON sm.site_id = s.site_id", cls, clause, args, null, null, null);
			while (rs.next()) {
				sites.add(readSite(rs));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		
		return sites;
	}
	
	public DataSet<Site> getSites(Integer clusterId, Integer pageIndex, Integer pageSize) {
		SQLDatabase db1 = null;
		SQLDatabase db2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		Object args[] = null;
		String orderBy = null;
		String limit=  null;
		String clause = null;
		DataSet<Site> datas = null;
		ArrayList<Site> sites = null;
		
		datas = new DataSet<Site>();
		sites = new ArrayList<Site>();
		datas.setObjects(sites);
		
		cls1 = new String[] { "id", "app_root", "title", "description", "date_created", "user_id", "keywords"};
		cls2 = new String[] {"COUNT(0)"};

		clause = "sm.cluster_id=?";
		args = new Object[] {clusterId};
		orderBy = "s.id DESC";
		limit = (pageIndex - 1) * pageSize + "," + pageSize;
		
		db1 = new SQLDatabase();
		try {
			rs1 = db1.query("jdroplet_sites s JOIN jdroplet_sitemappings sm ON sm.site_id = s.id", cls1, clause, args, null, null, orderBy, limit);
			while(rs1.next()) {
				sites.add(readSite(rs1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}
		
		db2 = new SQLDatabase();
		try {
			rs2 = db2.query("jdroplet_sites s JOIN jdroplet_sitemappings sm ON sm.site_id = s.id", cls2, clause, args, null, null, null, null);
			if (rs2.next()) {
				datas.setTotalRecords(rs2.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db2.close();
		}
		return datas;
	}
	
	public Site getSite(String url) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		Site site = null;
		
		cls = new String[] {"id","app_root","title","description","date_created","user_id", "keywords"};
		clause = "app_root=?";
		args = new Object[]{url};
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_sites", cls, clause, args, null, null, null);
			if (rs.next()) {
				site = readSite(rs);
			} else {
				throw new ApplicationException("SiteNotFound " + url);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return site;
	}
	
	public Site getSite(Integer id) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		Site site = null;
		
		cls = new String[] {"id","app_root","title","description","date_created","user_id", "keywords"};
		clause = "id=?";
		args = new Object[]{id};
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_sites", cls, clause, args, null, null, null);
			if (rs.next()) {
				site = readSite(rs);
			} else {
				throw new ApplicationException("SiteNotFound");
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		
		return site;
	}

	public Integer addSite(Site site) {
		Integer site_id = null;
		SQLDatabase db = null;
		HashMap<String, Object>values = null;

		values = new HashMap<String, Object>();
		values.put("title", site.getDescription());
		values.put("description", site.getDescription());
		values.put("keywords", site.getKeywords());
		values.put("app_root", site.getAppRoot());
		values.put("user_id", site.getUserId());
		values.put("date_created", DateTime.toString(site.getDateCreated()));
				
		db = new SQLDatabase();
		site_id = db.insert("jdroplet_sites", null, values);
		site.setId(site_id);
		return site_id;
	}
	
	public void updateSite(Site site) {
		String clause = null;
		Object[] args = null;
		SQLDatabase db = null;
		Map<String, Object> values = null;
		
		clause = "id=?";
		args = new Object[] { site.getId() };

		values = new HashMap<String, Object>();
		values.put("title", site.getTitle());
		values.put("description", site.getDescription());
		values.put("app_root", site.getAppRoot());
		values.put("user_id", site.getUserId());
		values.put("keywords", site.getKeywords());

		db = new SQLDatabase();
		db.update("jdroplet_sites", values, clause, args);
	}

	public Integer addPage(Page page) {
		Integer pageId = null;
		SQLDatabase db = null;
		HashMap<String, Object>values = null;
		
		values = new HashMap<String, Object>();
		values.put("name", page.getName());
		values.put("refer", page.getRefer());
		values.put("user_id", page.getUserId());
		values.put("date_created", DateTime.toString(page.getDateCreated()));
				
		db = new SQLDatabase();
		pageId = db.insert("jdroplet_page_pages", null, values);
		page.setId(pageId);
		return pageId;
	}
	
	public void updatePage(Page page) {
		String clause = null;
		Object[] args = null;
		SQLDatabase db = null;
		Map<String, Object> values = null;
		
		clause = "id=?";
		args = new Object[]{page.getId()};
		
		values = new HashMap<String, Object>();
		values.put("name", page.getName());
		values.put("refer", page.getRefer());
		values.put("user_id", page.getUserId());
		values.put("date_created", DateTime.toString(page.getDateCreated()));
		
		db = new SQLDatabase();
		db.update("jdroplet_page_pages", values, clause, args);
	}

	public Integer saveCluster(Cluster cluster) {
		SQLDatabase db = null;
		Map<String, Object> values = null;

		values = new HashMap<String, Object>();
		values.put("name", cluster.getName());

		if (cluster.getId() == null)
			values.put("id", cluster.getId());

		db = new SQLDatabase();
		if (cluster.getId() == null) {
			return db.insert("jdroplet_clusters", null, values);
		} else {
			db.update("jdroplet_clusters", values, "id=?", new Object[]{cluster.getId()});
			return cluster.getId();
		}
	}

	public void updateCluster(Integer siteId, Integer clusterId) {
		String clause = null;
		Object[] args = null;
		SQLDatabase db = null;
		Map<String, Object> values = null;

		clause = "site_id=?";
		args = new Object[]{siteId};

		values = new HashMap<String, Object>();
		values.put("clusterId", clusterId);

		db = new SQLDatabase();
		db.update("jdroplet_sitemappings", values, clause, args);
	}

	public void addSit2Cluster(Integer siteId, Integer clusterId) {
		SQLDatabase db = null;
		HashMap<String, Object>values = null;
		
		values = new HashMap<String, Object>();
		values.put("site_id", siteId);
		values.put("cluster_id", clusterId);

		db = new SQLDatabase();
		db.insert("jdroplet_sitemappings", null, values);
	}

	public UrlPattern getUrlPattern(Integer id) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		UrlPattern url = null;
		
		cls = new String[] {"up.*"};
		clause = "up.id=?";
		args = new Object[] { id };
		
		db = new SQLDatabase();
		
		try {
			rs = db.query("jdroplet_page_urlpatterns up", cls, clause, args, null, null, null);
			if (rs.next()) {
				url = readUrlPattern(rs);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return url;
	}
	
	public DataSet<UrlPattern> getUrlPatterns(Integer clusterId, Integer pageId, Integer pageIndex, Integer pageSize) {
		SQLDatabase db1 = null;
		SQLDatabase db2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		Object args[] = null;
		String clause = null;
		String orderBy = null;
		String limit = null;
		String table = null;
		
		ArrayList<UrlPattern> urls = new ArrayList<UrlPattern>();
		DataSet<UrlPattern> datas = new DataSet<UrlPattern>();
		
		cls1 = new String[] { "up.*" };
		cls2 = new String[] { "COUNT(0)" };
		table = "jdroplet_page_urlpatterns up JOIN jdroplet_clusters s ON up.cluster_id=s.id JOIN jdroplet_page_pages p ON up.page_id=p.id";
		if (pageId == null || pageId == 0) {
			clause = "up.cluster_id=?";
			args = new Object[] { clusterId };
		} else {
			clause = "up.cluster_id=? AND up.page_id=?";
			args = new Object[] { clusterId, pageId };
		}
		
		limit = (pageIndex - 1) * pageSize + "," + pageSize;
		
		db1 = new SQLDatabase();
		try {
			rs1 = db1.query(table, cls1, clause, args, null, null, orderBy, limit);
			while (rs1.next()) {
				urls.add(readUrlPattern(rs1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}
		
		db2 = new SQLDatabase();
		try {
			rs2 = db2.query(table, cls2, clause, args, null, null, null);
			if (rs2.next()) {
				datas.setTotalRecords(rs2.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db2.close();
		}
		datas.setObjects(urls);
		return datas;
	}
	
	public Integer saveUrlPattern(UrlPattern url) {
		SQLDatabase db = null;
		HashMap<String, Object>values = null;
		String clause = null;
		Object[] args = null;

		values = new HashMap<String, Object>();
		values.put("cluster_id", url.getCluster().getId());
		values.put("page_id", url.getPage().getId());
		values.put("action", url.getAction());
		values.put("params", url.getParams());
		values.put("allows", url.getAllows());
		values.put("description", url.getDescription());

		clause = "id=?";
		args = new Object[] { url.getId() };
		db = new SQLDatabase();
		if (url.getId() == null || url.getId() <= 0) {
			return db.insert("jdroplet_page_urlpatterns", null, values);
		} else {
			db.update("jdroplet_page_urlpatterns", values, clause, args);
			return url.getId();
		}
	}
}
