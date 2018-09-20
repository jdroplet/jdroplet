package jdroplet.data.mysqidal;


import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IMetaDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Meta;
import jdroplet.exceptions.SystemException;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MetaDataProvider extends DataProvider implements IMetaDataProvider {

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

    @Override
    public int save(Meta meta) {
        Integer id = null;
        String tableName = null;
        SQLDatabase db = null;
        HashMap<String, Object> values = null;

        tableName = "jdroplet_" + meta.getClass().getSimpleName().toLowerCase() + "meta";

        for (Entry<String, Object> v : meta.getValues().entrySet()) {
            if (v.getValue() == null)
                continue;

            id = super.exists(tableName, "item_id=? AND meta_key=?", new Object[]{meta.getId(), v.getKey()});

            values = new HashMap();
            values.put("item_id", meta.getId());
            values.put("meta_key", v.getKey());
            values.put("meta_value", v.getValue().toString());

            db = new SQLDatabase();
            if (id != null) {
                db.update(tableName, values, "id=?", new Object[]{id});
            } else {
                db.insert(tableName, null, values);
            }
        }

        return 1;
    }

    @Override
    public void save(Class<?> clazz, Integer itemId, String key, String value) {
        String tableName = null;
        SQLDatabase db = null;
        HashMap<String, Object> values = null;
        Object args[] = null;
        Integer id = null;
        String clause = null;

        tableName = "jdroplet_" + clazz.getSimpleName().toLowerCase() + "meta";
        values = new HashMap<String, Object>();
        values.put("item_id", itemId);
        values.put("meta_key", key);
        values.put("meta_value", value);

        clause = "item_id=? AND meta_key=?";
        args = new Object[]{itemId, key};

        id = super.exists(tableName, clause, args);
        db = new SQLDatabase();
        if (id != null) {
            db.update(tableName, values, clause, args);
        } else {
            db.insert(tableName, null, values);
        }
    }

    @Override
    public String getValue(Class<?> clazz, Integer itemId, String key) {
        SQLDatabase db = null;
        ResultSet rs = null;
        String cls[] = null;
        String limit = null;
        String table = null;
        String clause = null;
        Object args[] = null;
        String value = null;

        cls = new String[]{"meta_value"};
        limit = "1";

        clause = "item_id=? AND meta_key=?";
        args = new Object[]{itemId, key};

        db = new SQLDatabase();
        table = "jdroplet_" + clazz.getSimpleName().toLowerCase() + "meta";
        try {
            rs = db.query(table, cls, clause, args, null, null, null, limit);
            if (rs.next()) {
                value = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db.close();
        }
        return value;
    }

    @Override
    public void remove(Class<?> clazz, Integer itemId, String key) {
        SQLDatabase db = null;
        Object[] args = null;
        String table = null;
        String clause = null;

        if (TextUtils.isEmpty(key)) {
            clause = "item_id = ?";
            args = new Object[]{itemId};
        } else {
            clause = "item_id = ? and meta_key = ?";
            args = new Object[]{itemId, key};
        }

        table = "jdroplet_" + clazz.getSimpleName().toLowerCase() + "meta";

        db = new SQLDatabase();
        db.delete(table, clause, args);
    }
}
