package jdroplet.data.mysqidal;

import jdroplet.bll.SiteManager;
import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IDataProvider;
import jdroplet.data.model.*;
import jdroplet.enums.SortOrder;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import jdroplet.plugin.PluginFactory;
import jdroplet.security.PermissionBase;
import jdroplet.util.DataSet;
import jdroplet.util.I18n;
import jdroplet.util.SearchGroup;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class DataProvider implements IDataProvider {

	public abstract Entity newEntity();
	public abstract void fill(ResultSet rs, Entity entity) throws SQLException;

	public abstract String getTable();
	public abstract String[] getColums();
	public abstract Map<String, Object> getKeyValues(Entity entity);
	
	protected SQLDatabase newSQLDatabase() {
		return new SQLDatabase();
	}
	
	@Override
	public Entity getEntity(Integer id) {
		Entity entity = newEntity();
		fillEntity(id, entity);
		return entity;
	}

	@Override
	public void remove(Integer id) {
		SQLDatabase db = null;
		Object[] args = null;
		String clause = null;

		clause = "id=?";
		args = new Object[] { id };

		db = newSQLDatabase();
		db.delete(getTable(), clause, args);
	}

	@Override
	public DataSet<Entity> search(SearchGroup group, String sortField, SortOrder sortOrder, Integer pageIndex, Integer pageSize) {
		DataSet<Entity> datas = null;
		List<Entity> entites = null;
		Entity entity = null;
		SQLDatabase db1 = null;
		SQLDatabase db2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		Object args[] = null;
		String table = null;
		String clause = null;
		String orderBy = null;
		String groupBy = null;
		String limit = null;

		table = getTable();
		cls1 = (String[])PluginFactory.getInstance().applyFilters( "search_" + table + "_GetColums", getColums());
		cls2 = new String[] {"COUNT(0)"};

		//  构建筛选
		if (group != null)
			clause = group.toString();

		if (sortOrder.getValue() == SortOrder.ASC.getValue()) {
			orderBy = sortField + " ASC";
		} else {
			orderBy = sortField + " DESC";
		}

		limit = (pageIndex - 1) * pageSize + "," + pageSize;

		datas = new DataSet<Entity>();
		entites = new ArrayList<Entity>();
		datas.setObjects(entites);

		db1 = newSQLDatabase();
		try {
			rs1 = db1.query(table, cls1, clause, null, groupBy, null, orderBy, limit);
			while (rs1.next()) {
				entity = newEntity();
				entites.add(entity);
				fill(rs1, entity);
			}
		} catch (Exception ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}

		db2 = newSQLDatabase();
		try {
			rs2 = db2.query(table, cls2, clause, null, null, null, null);
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

	@Override
	public void delete(Integer id) {

	}

	@Override
	public List<Entity> search(SearchGroup group, String sortField, SortOrder sortOrder) {
		List<Entity> entites = null;
		Entity entity = null;
		SQLDatabase db1 = null;
		ResultSet rs1 = null;
		String cls1[] = null;
		Object args[] = null;
		String table = null;
		String clause = null;
		String orderBy = null;
		String groupBy = null;
		String limit = null;

		table = getTable();
		cls1 = getColums();

		//  构建筛选
		clause = group.toString();

		if (sortOrder.getValue() == SortOrder.ASC.getValue()) {
			orderBy = sortField + " ASC";
		} else {
			orderBy = sortField + " DESC";
		}

		entites = new ArrayList<Entity>();

		db1 = newSQLDatabase();
		try {
			rs1 = db1.query(table, cls1, clause, null, groupBy, null, orderBy, limit);
			while (rs1.next()) {
				entity = newEntity();
				entites.add(entity);
				fill(rs1, entity);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db1.close();
		}

		return entites;
	}

	@Override
	public Integer save(Entity entity) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		String tableName = null;

		values = getKeyValues(entity);
		tableName = getTable();
		db = newSQLDatabase();
		if (entity.getId() == null || entity.getId() <= 0) {
			return db.insert(tableName, null, values);
		} else {
			db.update(tableName, values, "id=?", new Object[]{entity.getId()});

			return entity.getId();
		}
	}

	protected void fillEntity(String clause, Object args[], String orderBy, Entity entity) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		String limit = null;
		String table = null;

		table = getTable();
		cls = getColums();
		limit = "1";

		db = newSQLDatabase();
		try {
			rs = db.query(table, cls, clause, args, null, null, orderBy, limit);
			if (rs.next()) {
				fill(rs, entity);
			} else {
				throw new ApplicationException(I18n.getString(entity.getClass().getSimpleName() + " NotFound"));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
	}

	protected void fillEntity(Integer id, Entity entity) {
		try {
			fillEntity(Entity.DataColumnsBase.id + "=?", new Object[]{id}, null, entity);
		} catch (ApplicationException ae) {
			throw new ApplicationException(I18n.getString(entity.getClass().getSimpleName() + " {0} NotFound", id));
		}
	}

	@Override
	public int update(String clauseFiled, Integer id, String field, Object value) {
		SQLDatabase db = null;
		Map<String, Object> values = null;
		values = new HashMap<String, Object>();

		values.put(field, value);
		db = newSQLDatabase();
		return db.update(getTable(), values, clauseFiled +"=?", new Object[]{id});
	}

	protected void update(String table, Integer id, Map<String, Object> values) {
		String clause = null;
		Object[] args = null;
		SQLDatabase db = null;

		clause = "id=?";
		args = new Object[]{id};

		db = newSQLDatabase();
		db.update(table, values, clause, args);
	}

	protected Integer getSum(String table, String sumField, String clause, Object args[]) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		String limit = null;
		Integer sum = null;

		cls = new String[] {"SUM(" + sumField + ")"};
		limit = "1";

		db = newSQLDatabase();

		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				sum = rs.getInt(1);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		if (sum == null)
			sum = 0;

		return sum;
	}

	protected Integer getCount(String table, String clause, Object args[]) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		String limit = null;

		cls = new String[] {"COUNT(0)"};
		limit = "1";

		db = newSQLDatabase();

		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
	}

	protected Integer exists(String table, String column, String clause, Object args[]) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		String limit = null;

		cls = new String[] {column};
		limit = "1";

		db = newSQLDatabase();

		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return null;
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
	}

	protected Integer exists(String table, String clause, Object args[]) {
		return exists(table, "id", clause, args);
	}

	public PermissionBase readPermission(ResultSet rs) throws SQLException {
		SectionPermission p = new SectionPermission();

		p.setSectionId(rs.getInt("section_id"));
		p.setRoleId(rs.getInt("role_id"));
		p.setAllowMask(rs.getLong("allow_mask"));
		p.setDenyMask(rs.getLong("deny_mask"));
		
		return p;
	}

	public Site readSite(ResultSet rs) throws SQLException {
		Site site = new Site();
		
		site.setId(rs.getInt("id"));
		site.setAppRoot(rs.getString("app_root"));
		site.setTitle(rs.getString("title"));
		site.setDescription(rs.getString("description"));
		site.setUserId(rs.getInt("user_id"));
		site.setDateCreated(new Date(rs.getTimestamp("date_created").getTime()));
		site.setKeywords(rs.getString("keywords"));
		return site;
	}
	
	@SuppressWarnings("unchecked")
	public Cluster readCluster(ResultSet rs) throws SQLException {
		Cluster cluster = null;

		cluster = new Cluster();
		cluster.setId(rs.getInt("id"));
		cluster.setName(rs.getString("name"));
		return cluster;
	}
		
	public UrlPattern readUrlPattern(ResultSet rs) throws SQLException {
		Cluster cluster = null;
		Page page = null;
		
		UrlPattern up = new UrlPattern();

		cluster = SiteManager.getCluster(rs.getInt("cluster_id"));
		page = SiteManager.getPage(rs.getInt("page_id"));

		up.setId(rs.getInt("id"));
		up.setCluster(cluster);
		up.setPage(page);
		up.setAction(rs.getString("action"));
		up.setParams(rs.getString("params"));
		up.setAllows(rs.getString("allows"));
		up.setDescription(rs.getString("description"));
		
		return up;
	}
	
	public Page readPage(ResultSet rs) throws SQLException {
		Page page = new Page();
		
		page.setId(rs.getInt("id"));
		page.setName(rs.getString("name"));
		page.setRefer(rs.getString("refer"));
		page.setUserId(rs.getInt("user_id"));
		page.setDateCreated(new Date(rs.getTimestamp("date_created").getTime()));
		
		return page;
	}

}
