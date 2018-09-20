package jdroplet.data.mysqidal;

import jdroplet.data.idal.ILogDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/3/16.
 */
public class LogDataProvider extends DataProvider implements ILogDataProvider {

    @Override
    public Entity newEntity() {
        return new Log();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Log.DataColumns.fill(rs, (Log) entity);
    }

    @Override
    public String getTable() {
        return Log.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Log.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Log.DataColumns.getKeyValues((Log) entity);
    }
}
