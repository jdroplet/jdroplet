package jdroplet.data.mysqidal;

import jdroplet.data.model.Entity;
import jdroplet.yue.data.idl.ILinkDataProvider;
import jdroplet.yue.data.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/6/19.
 */
public class LinkDataProvider extends DataProvider implements ILinkDataProvider {
    @Override
    public Entity newEntity() {
        return new Link();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Link.DataColumns.fill(rs, (Link) entity);
    }

    @Override
    public String getTable() {
        return Link.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Link.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Link.DataColumns.getKeyValues((Link) entity);
    }
}
