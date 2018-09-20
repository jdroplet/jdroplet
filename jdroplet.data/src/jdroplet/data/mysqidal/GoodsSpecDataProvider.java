package jdroplet.data.mysqidal;

import jdroplet.data.idal.IGoodsSpecDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.GoodsSpec;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/6/28.
 */
public class GoodsSpecDataProvider extends DataProvider implements IGoodsSpecDataProvider {
    @Override
    public Entity newEntity() {
        return new GoodsSpec();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        GoodsSpec.DataColumns.fill(rs, (GoodsSpec) entity);
    }

    @Override
    public String getTable() {
        return GoodsSpec.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return GoodsSpec.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return GoodsSpec.DataColumns.getKeyValues((GoodsSpec) entity);
    }

    @Override
    public GoodsSpec getGoodsSpec(Integer goodsId, Integer status, Integer property1, Integer property2, Integer property3) {
        GoodsSpec goodsSpec = new GoodsSpec();
        List<String> clauses_list = new ArrayList<String>();
        List<Object> args_list = new ArrayList<Object>();

        if (goodsId != null && goodsId != 0) {
            clauses_list.add("goods_id=?");
            args_list.add(goodsId);
        }

        if (status != null && status != 0) {
            clauses_list.add("status=?");
            args_list.add(status);
        }

        if (property1 != null && property1 != 0) {
            clauses_list.add("property1=?");
            args_list.add(property1);
        }

        if (property2 != null && property2 != 0) {
            clauses_list.add("property2=?");
            args_list.add(property2);
        }

        if (property3 != null && property3 != 0) {
            clauses_list.add("property3=?");
            args_list.add(property3);
        }

        try {
            fillEntity(StringUtils.join(clauses_list, " AND "), args_list.toArray(), null, goodsSpec);
        } catch (ApplicationException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            goodsSpec = null;
        }
        return goodsSpec;
    }
}
