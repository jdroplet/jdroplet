package jdroplet.data.mysqidal;

import jdroplet.data.idal.IStatisticDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Statistic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/3/20.
 */
public class StatisticDataProvider extends DataProvider implements IStatisticDataProvider  {

    @Override
    public Entity newEntity() {
        return new Statistic();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Statistic.DataColumns.fill(rs, (Statistic) entity);
    }

    @Override
    public String getTable() {
        return Statistic.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Statistic.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Statistic.DataColumns.getKeyValues((Statistic) entity);
    }
}
