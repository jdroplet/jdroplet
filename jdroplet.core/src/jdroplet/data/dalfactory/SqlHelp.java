package jdroplet.data.dalfactory;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jdroplet.data.model.Entity;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("unchecked")
public class SqlHelp {
    //private static String sql_cmd_key = "app:sql_cmds";

//	private static Map<String, String> getSqlCommands() {
//		Configuration config = Configuration.config();
//		Properties p = new Properties();
//		FileInputStream input = null;
//		String filePath = null;
//		Map<String, String> sqlCmds = new HashMap<String, String>();
//		
//		filePath = config.getServerPath() + config.getSqlCommandsFile();
//		try {
//			input = new FileInputStream(filePath);
//			p.load(input);
//			
//			for(Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
//				Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
//				sqlCmds.put(entry.getKey(), entry.getValue());
//			}
//		} catch (IOException ex) {
//			throw new SystemException(SystemExceptionType.FileNotFound, filePath);
//		} finally {
//			if (input != null) {
//				try {input.close();}catch(IOException ex) {}
//			}
//		}
//		
//		return sqlCmds;
//	}

//	public static String getSqlCommand(String name) {
//		Map<String, String> sqlCmds = (Map<String, String>)AppCache.get(sql_cmd_key);
//		if (sqlCmds == null) {
//			sqlCmds = getSqlCommands();
//			AppCache.add(sql_cmd_key, sqlCmds, ICache.MAX_FACTOR);
//		}
//		return sqlCmds.get(name);
//	}

//	public static String getSqlCommand(String name, int param) {
//		return getSqlCommand(name + Integer.toString(param));
//	}
//	
//	public static String getSqlCommand(String name, String param) {
//		return getSqlCommand(name + param);
//	}

    private static final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    public static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];

            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }

    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!TextUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    public static String buildQueryString(boolean distinct, String tables,
                                          String[] columns, String where, String groupBy, String having,
                                          String orderBy, String limit) {
        if (TextUtils.isEmpty(groupBy) && !TextUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!TextUtils.isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(128);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append("* ");
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);

        return query.toString();
    }

    public static <T> T getValue(String sql, Class<T> type) {
        SQLDatabase db = null;
        ResultSet rs = null;
        Object resu = null;

        try {
            db = new SQLDatabase();
            rs = db.executeQuery(sql);
            if (rs.next()) {
                resu = rs.getObject(1, type);
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(SqlHelp.class);
            logger.error(ex);
        } finally {
            db.close();
        }

        return (T) resu;
    }

    public interface IReadEntity {
        Entity read(ResultSet rs) throws SQLException;
    }

    public interface IReadMap {
        Map read(ResultSet rs) throws SQLException;
    }

    public static List getList(String sql, IReadEntity ir) {
        SQLDatabase db = null;
        ResultSet rs = null;
        List list = new ArrayList();

        db = new SQLDatabase();
        try {
            rs = db.executeQuery(sql);
            while (rs.next()) {
                if (ir != null) {
                    list.add(ir.read(rs));
                }
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(SqlHelp.class);
            logger.error(ex);
        } finally {
            db.close();
        }

        return list;
    }

    public static List getList(String sql, IReadMap rm) {
        SQLDatabase db = null;
        ResultSet rs = null;
        List list = new ArrayList();

        db = new SQLDatabase();
        try {
            rs = db.executeQuery(sql);
            while (rs.next()) {
                if (rm != null) {
                    list.add(rm.read(rs));
                }
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(SqlHelp.class);
            logger.error(ex);
        } finally {
            db.close();
        }

        return list;
    }

}
