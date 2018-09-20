package jdroplet.data.mysqidal;

import jdroplet.data.idal.IVoteDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Vote;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/26.
 */
public class VoteDataProvider extends DataProvider implements IVoteDataProvider {
    @Override
    public Integer getVoteCount(Integer userId, Integer itemId, String type, Date dateFrom, Date dateTo) {
        List<String> clauses_list = new ArrayList<String>();
        List<Object> args_list = new ArrayList<Object>();
        String clause = null;

        if (userId != null) {
            clauses_list.add("user_id=?");
            args_list.add(userId);
        }

        if (itemId != null) {
            clauses_list.add("item_id=?");
            args_list.add(itemId);
        }

        if (type != null) {
            clauses_list.add("type=?");
            args_list.add(type);
        }

        if (dateFrom != null) {
            clauses_list.add("create_time<=?");
            args_list.add(dateFrom);
        }

        if (dateTo != null) {
            clauses_list.add("create_time>=?");
            args_list.add(dateTo);
        }

        clause = StringUtils.join(clauses_list, " AND ");
        return super.getCount(getTable(), clause, args_list.toArray());
    }

    @Override
    public Entity newEntity() {
        return new Vote();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Vote.DataColumns.fill(rs, (Vote) entity);
    }

    @Override
    public String getTable() {
        return Vote.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Vote.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Vote.DataColumns.getKeyValues((Vote) entity);
    }
}
