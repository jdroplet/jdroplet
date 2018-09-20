package jdroplet.data.mysqidal;

import jdroplet.data.idal.IDraftDataProvider;
import jdroplet.data.model.Draft;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/5/6.
 */
public class DraftDataProvider extends DataProvider implements IDraftDataProvider {
    @Override
    public Entity newEntity() {
        return new Draft();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Draft.DataColumns.fill(rs, (Draft) entity);
    }

    @Override
    public String getTable() {
        return Draft.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Draft.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Draft.DataColumns.getKeyValues((Draft) entity);
    }

    @Override
    public Integer getDrafts(Integer itemId) {
        return super.getCount(getTable(), "item_id=?", new Object[]{itemId});
    }
}
