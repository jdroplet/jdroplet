package jdroplet.data.mysqidal;

import jdroplet.data.idal.IShopDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Shop;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2017/12/12.
 */
public class ShopDataProvider extends DataProvider implements IShopDataProvider {

    @Override
    public Entity newEntity() {
        return new Shop();
    }

    @Override
    public String getTable() {
        return Shop.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Shop.DataColumns.getColums();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Shop.DataColumns.fill(rs, (Shop) entity);
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Shop.DataColumns.getKeyValues((Shop) entity);
    }

    @Override
    public boolean exists(Integer userId, Integer shopId) {
        return super.exists(getTable(), "id=? AND user_id=?", new Object[]{shopId, userId}) != null;
    }
}
