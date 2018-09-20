package jdroplet.data.mysqidal;


import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IRoleDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Role;
import jdroplet.exceptions.SystemException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleDataProvider extends DataProvider implements IRoleDataProvider {

	@Override
	public Entity newEntity() {
		return new Role();
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
		Role.DataColumns.fill(rs, (Role) entity);
	}

	@Override
	public String getTable() {
		return Role.DataColumns.table;
	}

	@Override
	public String[] getColums() {
		return Role.DataColumns.getColums();
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return Role.DataColumns.getKeyValues((Role) entity);
	}

	@Override
	public void addUsersToRoles(List<Integer> userIds, Integer[] roleIds) {
		SQLDatabase db = null;
		HashMap<String, Object> values = null;
		ArrayList<Map<String, Object>> values_list = null;
		
		values_list = new ArrayList<Map<String, Object>>();
		for(Integer userId:userIds) {
			for(Integer id:roleIds) {
				values = new HashMap<String, Object>();
				values.put("user_id", userId);
				values.put("role_id", id);			
				values_list.add(values);
			}
		}
		db = new SQLDatabase();
		db.insert("jdroplet_role_relationships", null, values_list);
	}

	@Override
	public boolean exists(Integer userId, Integer roleId) {
		return super.exists("jdroplet_role_relationships", "user_id", "user_id=? AND role_id=?", new Object[]{userId, roleId}) != null;
	}

	@Override
	public List<Role> getRoles(Integer userId) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		ArrayList<Role> roles = null;
		Role r = null;

		cls = new String[] { "r.id", "r.name", "r.description" };

		db = new SQLDatabase();
		roles = new ArrayList<Role>();
		try {
			Object args[] = null;
			String clause = null;

			clause = "ur.user_id=?";
			args = new Object[]{userId};
			rs = db.query("jdroplet_roles r JOIN jdroplet_role_relationships ur ON r.id = ur.role_id", cls, clause, args, null, null, null);
			while (rs.next()) {
				r = new Role();
				fill(rs, r);
				roles.add(r);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return roles;
	}

	@Override
	public List<Role> getRoles() {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		ArrayList<Role> roles = null;
		Role r = null;

		cls = new String[] { "r.id", "r.name", "r.description" };

		db = new SQLDatabase();
		roles = new ArrayList<Role>();
		try {
			Object args[] = null;
			String clause = null;

			rs = db.query("jdroplet_roles r", cls, clause, args, null, null, null);
			while (rs.next()) {
				r = new Role();
				fill(rs, r);
				roles.add(r);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return roles;
	}

	public void removeRoles(Integer userId, Integer roleId) {
		SQLDatabase db = null;
		List<String> clauses_list = new ArrayList<String>();
		List<Object> args_list = new ArrayList<Object>();

		db = new SQLDatabase();

		if (userId == null && roleId == null)
			return;

		if (userId != null) {
			clauses_list.add("user_id=?");
			args_list.add(userId);
		}

		if (roleId != null) {
			clauses_list.add("role_id=?");
			args_list.add(roleId);
		}

		db.delete("jdroplet_role_relationships", StringUtils.join(clauses_list, " AND "), args_list.toArray());
	}

	@Override
	public void remove(Integer id) {
		SQLDatabase db = null;
		Object[] args = null;
		String clause = null;

		clause = "role_id=?";
		args = new Object[] { id };

		db = new SQLDatabase();
		db.delete("jdroplet_role_relationships", clause, args);

		super.remove(id);
	}
}
