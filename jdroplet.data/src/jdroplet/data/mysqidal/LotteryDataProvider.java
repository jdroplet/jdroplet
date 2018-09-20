package jdroplet.data.mysqidal;

import jdroplet.data.idal.ILotteryDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Lottery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/23.
 */
public class LotteryDataProvider extends DataProvider implements ILotteryDataProvider {
    @Override
    public Entity newEntity() {
        return new Lottery();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Lottery.DataColumns.fill(rs, (Lottery) entity);
    }

    @Override
    public String getTable() {
        return Lottery.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Lottery.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Lottery.DataColumns.getKeyValues((Lottery) entity);
    }

}