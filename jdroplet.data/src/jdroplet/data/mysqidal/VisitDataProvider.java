package jdroplet.data.mysqidal;

import jdroplet.data.idal.IVisitDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/8/21.
 */
public class VisitDataProvider extends DataProvider implements IVisitDataProvider {

    @Override
    public Entity newEntity() {
        return new Visit();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Visit.DataColumns.fill(rs, (Visit) entity);
    }

    @Override
    public String getTable() {
        return Visit.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Visit.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Visit.DataColumns.getKeyValues((Visit) entity);
    }

    @Override
    public Visit getLast(String user) {
        Visit v = new Visit();

        fillEntity("user=?", new Object[]{user}, "`id` desc", v);
        return v;
    }
}
