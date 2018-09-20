package jdroplet.data.mysqidal;

import jdroplet.data.idal.IAttributeDataProvider;
import jdroplet.data.model.Attribute;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class AttributeDataProvider extends DataProvider implements IAttributeDataProvider {
    @Override
    public Entity newEntity() {
        return new Attribute();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Attribute.DataColumns.fill(rs, (Attribute) entity);
    }

    @Override
    public String getTable() {
        return Attribute.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Attribute.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Attribute.DataColumns.getKeyValues((Attribute) entity);
    }
}