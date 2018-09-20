package jdroplet.data.mysqidal;

import jdroplet.data.idal.IActivityUserDataProvider;
import jdroplet.data.model.ActivityUser;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/24.
 */
public class ActivityUserDataProvider extends DataProvider implements IActivityUserDataProvider {
    @Override
    public Entity newEntity() {
        return new ActivityUser();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        ActivityUser.DataColumns.fill(rs, (ActivityUser) entity);
    }

    @Override
    public String getTable() {
        return ActivityUser.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return ActivityUser.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return ActivityUser.DataColumns.getKeyValues((ActivityUser) entity);
    }

    @Override
    public Integer exists(Integer shopId, Integer activityId, Integer userId) {
        return super.exists(getTable(), "shop_id=? AND activity_id=? AND user_id=?", new Object[]{shopId, activityId, userId});
    }
}
