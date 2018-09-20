package jdroplet.data.mysqidal;

import jdroplet.data.idal.ILotteryUserDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.LotteryUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/26.
 */
public class LotteryUserDataProvider extends DataProvider implements ILotteryUserDataProvider {

    @Override
    public Entity newEntity() {
        return new LotteryUser();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        LotteryUser.DataColumns.fill(rs, (LotteryUser) entity);
    }

    @Override
    public String getTable() {
        return LotteryUser.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return LotteryUser.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return LotteryUser.DataColumns.getKeyValues((LotteryUser) entity);
    }

    @Override
    public Integer exists(Integer activityId, Integer userId, Integer itemId, Integer status) {
        return super.exists(getTable(), LotteryUser.DataColumns.id, "activity_id=? and user_id=? and item_id=? and status=?",
                new Object[]{activityId, userId, itemId, status});
    }
}
