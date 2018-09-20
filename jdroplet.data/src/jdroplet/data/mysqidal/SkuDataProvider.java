package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.ISkuDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.GoodsSpec;
import jdroplet.data.model.ProductAtt;
import jdroplet.exceptions.SystemException;
import org.apache.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/20.
 */
public class SkuDataProvider extends DataProvider implements ISkuDataProvider {

    @Override
    public Entity newEntity() {
        return new GoodsSpec();
    }

    @Override
    public String getTable() {
        return "jdroplet_shop_skus";
    }

    @Override
    public String[] getColums() {
        return GoodsSpec.DataColumns.getColums();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        GoodsSpec.DataColumns.fill(rs, (GoodsSpec)entity);
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return GoodsSpec.DataColumns.getKeyValues((GoodsSpec) entity);
    }

    @Override
    public List<ProductAtt> getAtts(Integer productId, Integer skuId) {
        SQLDatabase db = null;
        ResultSet rs = null;
        String cls[] = null;
        Object args[] = null;
        String table = null;
        String clause = null;
        String orderBy = null;
        String groupBy = null;
        String limit = null;
        int attId = -1;
        ProductAtt att = null;
        List<ProductAtt> atts = null;

        table = "jdroplet_shop_sku_atts ssa join jdroplet_shop_skus ss on ss.id = ssa.att_id";
        cls = new String[]{"ss.*"};
        clause = "ssa.product_id=? AND ssa.sku_id=?";
        args = new Object[]{productId, skuId};
        orderBy = "ssa.id asc";

        db = new SQLDatabase();
        atts = new ArrayList<>();
        try {
            rs = db.query(table, cls, clause, null, groupBy, null, orderBy, limit);
            while (rs.next()) {
                att = new ProductAtt();
                att.setId(rs.getInt("id"));
                att.setName(rs.getString("name"));
                att.setSectionId(rs.getInt("section_id"));
                atts.add(att);
            }
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db.close();
        }
        return atts;
    }
}
