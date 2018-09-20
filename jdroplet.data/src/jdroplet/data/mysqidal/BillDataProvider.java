package jdroplet.data.mysqidal;

import jdroplet.data.idal.IBillDataProvider;
import jdroplet.data.model.Bill;
import jdroplet.data.model.Entity;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/29.
 */
public class BillDataProvider extends DataProvider implements IBillDataProvider {
    @Override
    public Entity newEntity() {
        return new Bill();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Bill.DataColumns.fill(rs, (Bill) entity);
    }

    @Override
    public String getTable() {
        return Bill.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Bill.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Bill.DataColumns.getKeyValues((Bill) entity);
    }

    @Override
    public boolean exists(Integer userId, BigInteger orderId) {
        return super.exists(getTable(), "user_id=? AND order_id=?", new Object[]{userId, orderId}) != null;
    }

    @Override
    public Integer getUserCoins(Integer userId) {
        return super.getSum(getTable(), Bill.DataColumns.fee, "user_id=? AND type<>?", new Object[]{userId, Bill.COUPON});
    }

    @Override
    public Integer getCouponUsedValues(Integer userId, BigInteger couponId) {
        return super.getSum(getTable(), Bill.DataColumns.fee, "user_id=? AND type=? AND item_id=?", new Object[]{userId, Bill.COUPON, couponId});
    }
}
