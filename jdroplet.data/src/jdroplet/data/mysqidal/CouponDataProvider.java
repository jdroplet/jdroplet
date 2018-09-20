package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.ICouponDataProvider;
import jdroplet.data.model.Coupon;
import jdroplet.data.model.Entity;
import jdroplet.exceptions.SystemException;
import org.apache.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/29.
 */
public class CouponDataProvider extends DataProvider implements ICouponDataProvider {
    @Override
    public Integer getValues(Integer userId, boolean all) {
        SQLDatabase db = null;
        ResultSet rs = null;
        String cls[] = null;
        Object args[] = null;
        String clause = null;
        int sum = 0;

        db = new SQLDatabase();
        cls = new String[] {"SUM(value)"};

        if (all) {
            clause = "user_id = ?";
            args = new Object[] {userId};
        } else {
            clause = "user_id = ? AND expired > now()";
            args = new Object[] {userId};
        }

        try {
            rs = db.query(getTable(), cls, clause, args, null, null, null);
            if (rs.next()) {
                sum = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db.close();
        }

        return sum;
    }

    @Override
    public Entity newEntity() {
        return new Coupon();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Coupon.DataColumns.fill(rs, (Coupon) entity);
    }

    @Override
    public String getTable() {
        return Coupon.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Coupon.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Coupon.DataColumns.getKeyValues((Coupon) entity);
    }

    @Override
    public Integer save(Entity entity) {
        SQLDatabase db = null;
        Map<String, Object> values = null;
        String tableName = null;
        Coupon coupon = (Coupon) entity;

        values = getKeyValues(entity);
        tableName = getTable();
        db = new SQLDatabase();
        if (exists(tableName, "user_id", "id=?", new Object[]{coupon.getBidId()}) == null) {
            return db.insert(tableName, null, values);
        } else {
            db.update(tableName, values, "id=?", new Object[]{((Coupon) entity).getBidId()});

            return entity.getId();
        }
    }
}
