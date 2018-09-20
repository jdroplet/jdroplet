package jdroplet.data.mysqidal;

import jdroplet.data.idal.IGoodsDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Goods;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/6/28.
 */
public class GoodsDataProvider extends DataProvider implements IGoodsDataProvider {

    @Override
    public Entity newEntity() {
        return new Goods();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Goods.DataColumns.fill(rs, (Goods) entity);
    }

    @Override
    public String getTable() {
        return Goods.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Goods.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Goods.DataColumns.getKeyValues((Goods) entity);
    }
}
