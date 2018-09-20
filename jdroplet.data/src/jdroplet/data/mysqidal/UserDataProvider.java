package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IUserDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.User;
import jdroplet.enums.PasswordFormat;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDataProvider extends DataProvider implements IUserDataProvider {

	@Override
	public Entity newEntity() {
		return new User();
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
		User.DataColumns.fill(rs, (User) entity);
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return User.DataColumns.getKeyValues((User)entity);
	}


	@Override
	public String getTable() {
		return "jdroplet_users";
	}

	@Override
	public String[] getColums() {
		return User.DataColumns.getColums();
	}

	@Override
	public User getUser(Integer id, String user_name) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String limit = null;
		String clause = null;
		User user = null;
		
		cls = getColums();
		limit = "1";
		
		if (id != null) {
			clause = "id=?";
			args = new Object[] { id };
		} else if (!TextUtils.isEmpty(user_name)) {
			clause = "user_name=?";
			args = new Object[] { user_name };
		}
		
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_users", cls, clause, args, null, null, null, limit);
			
			if (rs.next()) {
				user = new User();
				fill(rs, user);
				user.setPasswordFormat(PasswordFormat.get(rs.getString("password_format")));
				user.setEmail(rs.getString("email"));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage() + rs.toString());
		} finally {
			db.close();
		}        
		return user;
	}
	
	public boolean exists(String userName) {		
		return super.exists("jdroplet_users", "user_name=?", new Object[]{userName}) != null;
	}

	public Integer exists2(String userName) {
		return super.exists("jdroplet_users", "display_name=?", new Object[]{userName});
	}

	@Override
	public DataSet<User> getUsers(Integer[] roleIds, String searchTerms, Integer status, Integer pageIndex, Integer pageSize) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String table = null;
		String clause = null;
		String limit = null;
		List<User> users = new ArrayList<User>();
		DataSet<User> datas = new DataSet<User>();
		User user = null;
		List<String> clauses_list = new ArrayList<String>();
		List<Object> args_list = new ArrayList<Object>();
		
		cls = getColums();

		if (roleIds == null || roleIds.length == 0) {
			table = "jdroplet_users as u";
		} else {
			table = "jdroplet_users as u join jdroplet_role_relationships uir on u.id = uir.user_id";

			String temp = StringUtils.join(roleIds, ",");
			clauses_list.add("uir.role_id IN (" + temp + ")");
		}
		
		if (!TextUtils.isEmpty(searchTerms)) {
			String arg = "%" + searchTerms + "%";

			clauses_list.add("(u.display_name like ? OR u.id like ?)");
			args_list.add(arg);
			args_list.add(arg);
		}

		if (status != null) {
			clauses_list.add("u.status=?");
			args_list.add(status);
		}
		
		clause = StringUtils.join(clauses_list, " AND ");
		args = args_list.toArray();
		
		limit = (pageIndex - 1) * pageSize + "," + pageSize;

		db = new SQLDatabase();
		try {
			rs = db.query(table, cls, clause, args, null, null, null, limit);
			while (rs.next()) {
				user = new User();
				fill(rs, user);
				users.add(user);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		datas.setObjects(users);

		cls = new String[] {"COUNT(DISTINCT(id))"};
		try {
			db = new SQLDatabase();
			rs = db.query(table, cls, clause, args, null, null, null, null);
			if (rs.next()) {
				datas.setTotalRecords(rs.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return datas;
	}

	@Override
	public Integer validUser(User user) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		Object args[] = null;
		String clause = null;
		String limit = null;
		int status = 1;
		
		cls = new String[] {"status"};
		clause = "id=? AND password=? ";
		limit = "1";
		args = new Object[]{user.getId(), user.getPassword()};
		
		db = new SQLDatabase();
		
		try {
			rs = db.query("jdroplet_users", cls, clause, args, null, null, null, limit);
			if (rs.next()) {
				status = rs.getInt(1);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return status;
	}
}
