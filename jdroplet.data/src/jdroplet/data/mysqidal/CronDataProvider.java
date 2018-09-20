package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.ICronDataProvider;
import jdroplet.data.model.Cron;
import jdroplet.data.model.Entity;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2017/12/26.
 */
public class CronDataProvider extends DataProvider implements ICronDataProvider  {

    @Override
    public DataSet<Cron> getCrons(Integer status, Integer pageIndex, Integer pageSize) {
        List<Cron> list = new ArrayList<Cron>();
        DataSet<Cron> datas = new DataSet<Cron>();
        Cron c = null;
        SQLDatabase db = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String cls1[] = null;
        String cls2[] = null;
        String clause = null;
        String orderBy = null;
        String limit = null;
        Object args[] = null;
        List<String> clauses_list = new ArrayList<String>();
        List<Object> args_list = new ArrayList<Object>();

        //  构建筛选
        if (status != null) {
            clauses_list.add("status=?");
            args_list.add(status);
        }

        args = args_list.toArray();
        clause = StringUtils.join(clauses_list, " AND ");

        orderBy = "id desc";
        limit = (pageIndex - 1) * pageSize + "," + pageSize;

        cls1 = getColums();
        cls2 = new String[] {"COUNT(0)"};
        db = new SQLDatabase();

        try {
            rs1 = db.query(getTable(), cls1, clause, args, null, null, orderBy, limit);
            while (rs1.next()) {
                c = new Cron();
                list.add(c);
                this.fill(rs1, c);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db.close();
        }
        datas.setObjects(list);

        try {
            db = new SQLDatabase();
            rs2 = db.query(getTable(), cls2, clause, args, null, null, null);
            if (rs2.next()) {
                datas.setTotalRecords(rs2.getInt(1));
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db.close();
        }
        return datas;
    }

    @Override
    public Entity newEntity() {
        return new Cron();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Cron.DataColumns.fill(rs, (Cron) entity);
    }

    @Override
    public String getTable() {
        return Cron.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Cron.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Cron.DataColumns.getKeyValues((Cron) entity);
    }
}
