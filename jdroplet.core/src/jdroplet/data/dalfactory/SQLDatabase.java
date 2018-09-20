package jdroplet.data.dalfactory;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdroplet.exceptions.SystemException;
import jdroplet.util.TextUtils;

import org.apache.log4j.Logger;


public class SQLDatabase {
	private Connection m_conn = null;
	private PreparedStatement m_st = null;
	private ResultSet m_rs = null;

	private String mReadUrl = "proxool.jdroplet_read";
	private String mWriteUrl = "proxool.jdroplet_write";

	protected static Logger logger = Logger.getLogger(SQLDatabase.class);
	public static final int CONFLICT_NONE = 0;
	private static final String[] CONFLICT_VALUES = new String[] { "",
			" OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ",
			" OR REPLACE " };

	public String getReadUrl() {
		return mReadUrl;
	}

	public void setReadUrl(String mReadUrl) {
		this.mReadUrl = mReadUrl;
	}

	public String getWriteUrl() {
		return mWriteUrl;
	}

	public void setWriteUrl(String mWriteUrl) {
		this.mWriteUrl = mWriteUrl;
	}

	protected Connection getSqlReadConnection(String url) {
		Connection conn = null;
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			conn = DriverManager.getConnection(url);
		} catch (SQLException ex) {
			logger.error("SQLException", ex);
			throw new SystemException(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			logger.error("SQLException", ex);
			throw new SystemException(ex.getMessage());
		}
		return conn;
	}

	protected Connection getSqlReadConnection() {
		return getSqlReadConnection(mReadUrl);
	}

	protected Connection getSqlWriteConnection() {
		return getSqlReadConnection(mWriteUrl);
	}

	public ResultSet query(String table, String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy) throws SQLException {
		return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
	}

	public ResultSet query(String table, String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
		return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public ResultSet query(boolean distinct, String table, String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
		return queryWithFactory(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public ResultSet queryWithFactory(boolean distinct, String table, String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
		String sql = SqlHelp.buildQueryString(distinct, table, columns, selection, groupBy, having, orderBy, limit);
		return rawQueryWithFactory(sql, selectionArgs, findEditTable(table));
	}

	public static String findEditTable(String tables) {
		if (!TextUtils.isEmpty(tables)) {
			int spacepos = tables.indexOf(' ');
			int commapos = tables.indexOf(',');

			if (spacepos > 0 && (spacepos < commapos || commapos < 0)) {
				return tables.substring(0, spacepos);
			} else if (commapos > 0 && (commapos < spacepos || spacepos < 0)) {
				return tables.substring(0, commapos);
			}
			return tables;
		} else {
			throw new IllegalStateException("Invalid tables");
		}
	}

	public ResultSet rawQueryWithFactory(String sql, Object[] selectionArgs, String editTable) throws SQLException {
		int numArgs = -1;

		numArgs = selectionArgs == null ? 0 : selectionArgs.length;
		m_conn = getSqlReadConnection();
		m_st = m_conn.prepareStatement(sql);
		for (int idx = 0; idx < numArgs; idx++) {
			m_st.setObject(idx+1, selectionArgs[idx]);
		}
		m_rs = m_st.executeQuery();

		return m_rs;
	}

	public Integer[] insert(String table, String nullColumnHack, List<Map<String, Object>> values) {
		return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
	}

	public Integer insert(String table, String nullColumnHack, Map<String, Object> values) {
		return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
	}

	public Integer[] insertWithOnConflict(String table, String nullColumnHack, List<Map<String, Object>> initialValues, int conflictAlgorithm) {
		StringBuilder sql_buf = null;
		StringBuilder values_buf = null;
		Set<Map.Entry<String, Object>> entrySet = null;
		Map.Entry<String, Object> entry = null;

		sql_buf = new StringBuilder(512);
		sql_buf.append("INSERT");
		sql_buf.append(CONFLICT_VALUES[conflictAlgorithm]);
		sql_buf.append(" INTO ");
		sql_buf.append(table);

		values_buf = new StringBuilder(256);
		if (initialValues != null && initialValues.size() > 0) {
			entrySet = initialValues.get(0).entrySet();
			Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
			sql_buf.append('(');

			boolean needSeparator = false;
			while (entriesIter.hasNext()) {
				if (needSeparator) {
					sql_buf.append(", ");
					values_buf.append(", ");
				}
				needSeparator = true;
				entry = entriesIter.next();
				sql_buf.append(entry.getKey());
				values_buf.append('?');
			}

			sql_buf.append(')');
		} else {
			sql_buf.append("(" + nullColumnHack + ") ");
			values_buf.append("NULL");
		}

		sql_buf.append(" VALUES(");
		sql_buf.append(values_buf);
		sql_buf.append(");");

		Integer[] new_ids = null;

		m_conn = getSqlWriteConnection();
		try {
			m_conn.setAutoCommit(false);
			m_st = m_conn.prepareStatement(sql_buf.toString(), Statement.RETURN_GENERATED_KEYS);

			if (entrySet != null) {
				int size = entrySet.size();
				Iterator<Map.Entry<String, Object>> entriesIter = null;

				for (Map<String, Object> values:initialValues) {
					entriesIter = values.entrySet().iterator();
					for (int idx = 0; idx < size; idx++) {
						entry = entriesIter.next();
						m_st.setObject(idx+1, entry.getValue());
					}
					m_st.addBatch();
				}
			}
			m_st.executeBatch();
			m_conn.commit();
//            m_rs = m_st.getGeneratedKeys();
//            while (m_rs.next()) {
//            	m_rs.getInt(1);
//        	}
		} catch (SQLException ex) {
			logger.error(ex + "#" + m_st.toString());
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st, m_rs);
		}

		return new_ids;
	}

	public Integer insertWithOnConflict(String table, String nullColumnHack, Map<String, Object> initialValues, int conflictAlgorithm) {
		StringBuilder sql = null;
		StringBuilder values = null;
		Set<Map.Entry<String, Object>> entrySet = null;
		Map.Entry<String, Object> entry = null;

		sql = new StringBuilder(512);
		sql.append("INSERT");
		sql.append(CONFLICT_VALUES[conflictAlgorithm]);
		sql.append(" INTO ");
		sql.append(table);

		values = new StringBuilder(256);
		if (initialValues != null && initialValues.size() > 0) {
			entrySet = initialValues.entrySet();
			Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
			sql.append('(');

			boolean needSeparator = false;
			while (entriesIter.hasNext()) {
				if (needSeparator) {
					sql.append(", ");
					values.append(", ");
				}
				needSeparator = true;
				entry = entriesIter.next();
				sql.append(entry.getKey());
				values.append('?');
			}

			sql.append(')');
		} else {
			sql.append("(" + nullColumnHack + ") ");
			values.append("NULL");
		}

		sql.append(" VALUES(");
		sql.append(values);
		sql.append(");");

		Integer new_id = null;

		m_conn = getSqlWriteConnection();
		try {
			m_st = m_conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

			if (entrySet != null) {
				int size = entrySet.size();
				Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
				for (int idx = 0; idx < size; idx++) {
					entry = entriesIter.next();
					m_st.setObject(idx+1, entry.getValue());
				}
			}
			m_st.executeUpdate();
			m_rs = m_st.getGeneratedKeys();
			if (m_rs.next())
				new_id = Integer.valueOf(m_rs.getInt(1));

		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st, m_rs);
		}

		return new_id;
	}

	public void close(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (Exception e) { }
		}

		if (st != null) {
			try {
				st.clearWarnings();
				st.close();
			}
			catch (Exception e) { }
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) { }
		}
	}

	public void close() {
		this.close(m_conn, m_st, m_rs);
	}

	public void close(Connection conn, ResultSet rs) {
		this.close(conn, null, rs);
	}

	public void close(Connection conn, Statement st) {
		this.close(conn, st, null);
	}

	public int update(String table, Map<String, Object> values, String whereClause, Object[] whereArgs) {
		ArrayList<Object[]> wheresArgList = null;
		int[] counts = null;

		wheresArgList = new ArrayList<Object[]>();
		wheresArgList.add(whereArgs);
		counts = updateWithOnConflict(table, values, whereClause, wheresArgList, CONFLICT_NONE);

		if (counts != null && counts.length > 0)
			return counts[0];
		else
			return 0;

	}

	public int[] update(String table, Map<String, Object> values, String whereClause, List<Object[]> whereArgsList) {
		return updateWithOnConflict(table, values, whereClause, whereArgsList, CONFLICT_NONE);
	}

	public int[] updateWithOnConflict(String table, Map<String, Object> values, String whereClause, List<Object[]> whereArgsList, int conflictAlgorithm) {
		StringBuilder sql = null;
		int size = 0;

		sql = new StringBuilder(512);
		sql.append("UPDATE ");
		sql.append(CONFLICT_VALUES[conflictAlgorithm]);
		sql.append(table);
		sql.append(" SET ");

		Set<Map.Entry<String, Object>> entrySet = null;
		Iterator<Map.Entry<String, Object>> entriesIter = null;
		Map.Entry<String, Object> entry = null;

		if (values != null) {
			entrySet = values.entrySet();
			entriesIter = entrySet.iterator();
			while (entriesIter.hasNext()) {
				entry = entriesIter.next();
				sql.append(entry.getKey());
				sql.append("=?");
				if (entriesIter.hasNext()) {
					sql.append(", ");
				}
			}
		}

		if (!TextUtils.isEmpty(whereClause)) {
			sql.append(" WHERE ");
			sql.append(whereClause);
		}

		int[] counts = null;
		m_conn = getSqlWriteConnection();
		try {
			m_conn.setAutoCommit(false);
			m_st = m_conn.prepareStatement(sql.toString());

			if (values != null) {
				size = entrySet.size();
				entriesIter = entrySet.iterator();
				for (int idx = 0; idx < size; idx++) {
					entry = entriesIter.next();
					m_st.setObject(idx+1, entry.getValue());
				}
			}
			if (whereArgsList != null) {
				for (Object[] whereArgs : whereArgsList) {
					for (int idx = 0; idx < whereArgs.length; idx++) {
						m_st.setObject(size + idx + 1, whereArgs[idx]);
					}
					m_st.addBatch();
				}
			}
			counts = m_st.executeBatch();
			m_conn.commit();
		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st);
		}
		return counts;
	}

	public int[] delete(String table, String whereClause, List<Object[]> whereArgsList) {
		int[] counts= null;

		m_conn = getSqlWriteConnection();
		try {
			m_conn.setAutoCommit(false);
			m_st = m_conn.prepareStatement("DELETE FROM " + table + (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : ""));
			if (whereArgsList != null) {
				for (Object[] whereArgs : whereArgsList) {
					for (int idx = 0; idx < whereArgs.length; idx++) {
						m_st.setObject(idx + 1, whereArgs[idx]);
					}
					m_st.addBatch();
				}
			}
			counts = m_st.executeBatch();
			m_conn.commit();
		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st);
		}
		return counts;
	}

	public int delete(String table, String whereClause, Object[] whereArgs) {
		ArrayList<Object[]> wheresArgList = null;
		int[] counts = null;

		wheresArgList = new ArrayList<Object[]>();
		wheresArgList.add(whereArgs);
		counts = delete(table, whereClause, wheresArgList);
		if (counts != null && counts.length > 0) {
			return counts[0];
		} else {
			return 0;
		}
	}

	public int executeUpdate(String sql) {
		int result = -1;

		m_conn = getSqlWriteConnection();
		try {
			m_st = m_conn.prepareStatement(sql);
			result = m_st.executeUpdate();
		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st);
		}
		return result;
	}

	public int executeUpdate(String sql, Object[] selectionArgs)  {
		int result = -1;

		m_conn = getSqlWriteConnection();
		try {
			m_st = m_conn.prepareStatement(sql);
			for (int idx = 0; idx < selectionArgs.length; idx++) {
				m_st.setObject(idx+1, selectionArgs[idx]);
			}
			result = m_st.executeUpdate();
		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			close(m_conn, m_st);
		}

		return result;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		m_conn = getSqlReadConnection();
		m_st = m_conn.prepareStatement(sql);
		m_rs = m_st.executeQuery();

		return m_rs;
	}

	public ResultSet executeQuery(String sql, Object[] selectionArgs) throws SQLException {
		int numArgs = -1;

		m_conn = getSqlReadConnection();
		m_st = m_conn.prepareStatement(sql);

		numArgs = selectionArgs == null ? 0 : selectionArgs.length;
		for (int idx = 0; idx < numArgs; idx++) {
			m_st.setObject(idx+1, selectionArgs[idx]);
		}
		m_rs = m_st.executeQuery();

		return m_rs;
	}
}
