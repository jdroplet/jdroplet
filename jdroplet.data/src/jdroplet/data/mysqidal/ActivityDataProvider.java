package jdroplet.data.mysqidal;

import jdroplet.data.idal.IActivityDataProvider;
import jdroplet.data.model.Activity;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/24.
 */
public class ActivityDataProvider extends DataProvider implements IActivityDataProvider {

    @Override
    public Entity newEntity() {
        return new Activity();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Activity.DataColumns.fill(rs, (Activity) entity);
    }

    @Override
    public String getTable() {
        return Activity.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Activity.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Activity.DataColumns.getKeyValues((Activity) entity);
    }
}
