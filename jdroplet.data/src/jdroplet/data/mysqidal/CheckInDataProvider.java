package jdroplet.data.mysqidal;

import jdroplet.core.DateTime;
import jdroplet.data.idal.ICheckInDataProvider;
import jdroplet.data.model.CheckIn;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * Created by kuibo on 2018/7/3.
 */
public class CheckInDataProvider extends DataProvider implements ICheckInDataProvider {

    @Override
    public Entity newEntity() {
        return new CheckIn();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        CheckIn.DataColumns.fill(rs, (CheckIn) entity);
    }

    @Override
    public String getTable() {
        return CheckIn.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return CheckIn.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return CheckIn.DataColumns.getKeyValues((CheckIn) entity);
    }

    @Override
    public Integer isChecked(Integer shopId, Integer userId, Date date) {
        return super.exists(getTable(), "shop_id=? AND user_id=? AND last_date=?", new Object[]{shopId, userId, DateTime.toString(date, "yyyy-MM-dd")});
    }
}
