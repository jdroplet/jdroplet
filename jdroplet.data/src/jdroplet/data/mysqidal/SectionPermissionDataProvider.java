package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IPermissionDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import jdroplet.security.PermissionBase;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionPermissionDataProvider extends DataProvider implements IPermissionDataProvider {

	@Override
	public Entity newEntity() {
		return null;
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
	}

	@Override
	public String getTable() {
		return null;
	}

	@Override
	public String[] getColums() {
		return null;
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return null;
	}


	public List<PermissionBase> getPermissions(int clusterId, int userId, int parentId, String type) {
		List<PermissionBase> perms = new ArrayList<PermissionBase>();
		PermissionBase pb = null;
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		List<String> clauses_list = new ArrayList<String>();
		List<Object> args_list = new ArrayList<Object>();
		
		if (userId > 0) {
			clauses_list.add("s.user_id=?");
			args_list.add(userId);
		}
		if (parentId > 0) {
			clauses_list.add("s.parent_id=?");
			args_list.add(parentId);
		}
		if (TextUtils.isEmpty(type)) {
			clauses_list.add("s.type=?");
			args_list.add(type);
		}
		clauses_list.add("p.cluster_id=?");
		args_list.add(clusterId);
		
		args = args_list.toArray();
		clause = StringUtils.join(clauses_list, " AND ");
		
		cls = new String[] {"P.cluster_id","P.section_id","R.role_id","allow_mask","deny_mask"};
				
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_sectionpermissions p JOIN jdroplet_roles r ON p.role_id = R.id JOIN jdroplet_sections s ON p.section_id=s.id", cls, clause, args, null, null, null);
			
			while (rs.next()) {
				pb = readPermission(rs);
				perms.add(pb);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return perms;
	}

	public void add(List<PermissionBase> permissions) {		
		SQLDatabase db = null;
		HashMap<String, Object> values = null;
		ArrayList<Map<String, Object>> values_list = null;

		values_list = new ArrayList<Map<String, Object>>();
		for (PermissionBase p : permissions) {
			values = new HashMap<String, Object>();
			values.put("section_id", p.getSectionId());
			values.put("role_id", p.getRoleId());
			values.put("allow_mask", p.getAllowMask());
			values.put("deny_mask", p.getDenyMask());
			values_list.add(values);
		}
		db = new SQLDatabase();
		db.insert("jdroplet_sectionpermissions", null, values_list);
	}
	
	public void update(List<PermissionBase> permissions) {
	}

	public void delete(List<PermissionBase> permissions) {
	}

	public PermissionBase getPermission(int clusterId, int sectionId, int roleId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		PermissionBase pb = null;
		
		cls = new String[] {"section_id","role_id","allow_mask","deny_mask"};
		clause = "section_id=? AND role_id=?";
		args = new Object[] { sectionId, roleId };
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_sectionpermissions", cls, clause, args, null, null, null);
			
			if (rs.next()) {
				pb = readPermission(rs);
			} else {
				throw new ApplicationException("PermissionNotFound");
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return pb;
	}

	@Override
	public PermissionBase getPermission(int sectionId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		PermissionBase pb = null;
		
		cls = new String[] {"cluster_id","section_id","role_id","allow_mask","deny_mask"};
		clause = "section_id=?";
		args = new Object[] { sectionId };
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_sectionpermissions", cls, clause, args, null, null, null);
			
			if (rs.next()) {
				pb = readPermission(rs);
			} else {
				throw new ApplicationException("PermissionNotFound");
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}        
		return pb;
	}
}
