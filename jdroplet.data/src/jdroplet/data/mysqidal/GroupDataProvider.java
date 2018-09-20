package jdroplet.data.mysqidal;

import jdroplet.data.idal.IGroupDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Group;
import jdroplet.util.DataSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/8.
 */
public class GroupDataProvider extends DataProvider implements IGroupDataProvider {

    @Override
    public Entity newEntity() {
        return new Group();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Group.DataColumns.fill(rs, (Group) entity);
    }

    @Override
    public String getTable() {
        return Group.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Group.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Group.DataColumns.getKeyValues((Group) entity);
    }

    @Override
    public boolean exists(Integer id) {
        return super.exists(getTable(), "id=?", new Object[] {id}) != null;
    }

    @Override
    public Integer getCount(Integer activityId, Integer userId) {
        return super.getCount(getTable(), "activity_id=? AND user_id=?", new Object[]{activityId, userId});
    }
}
