package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IAttributeValueDataProvider;
import jdroplet.data.model.AttributeValue;
import jdroplet.data.model.Entity;
import jdroplet.exceptions.SystemException;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import sun.plugin2.main.server.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AttributeValueDataProvider extends DataProvider implements IAttributeValueDataProvider {
    @Override
    public Entity newEntity() {
        return new AttributeValue();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        AttributeValue.DataColumns.fill(rs, (AttributeValue) entity);
    }

    @Override
    public String getTable() {
        return AttributeValue.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return AttributeValue.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return AttributeValue.DataColumns.getKeyValues((AttributeValue) entity);
    }

    protected SQLDatabase newSQLDatabase() {
        SQLDatabase db = new SQLDatabase();

        db.setReadUrl("proxool.jdroplet_read_post");
        db.setWriteUrl("proxool.jdroplet_write_post");
        return db;
    }


    //    select p.*
//    from jdroplet_itemIds as p
//    inner join jdroplet_attribute_values as a on p.id = a.item_id and a.attribute_id=1 and a.value='整租'
//    inner join jdroplet_attribute_values as b on p.id = b.item_id and b.attribute_id=2 and b.value='经纪人'
//    inner join jdroplet_attribute_values as c on p.id = c.item_id and c.attribute_id=3 and c.value='11'
//    inner join jdroplet_attribute_values as d on p.id = d.item_id and d.attribute_id=4 and d.value='二室'
//
    
    @Override
    public DataSet<Integer> getItemIds(List<AttributeValue> avs, SearchGroup group, String type, Integer pageIndex, Integer pageSize) {
        Integer itemId = null;
        DataSet<Integer> datas = new DataSet<Integer>();
        ArrayList<Integer> itemIds = new ArrayList<Integer>();
        List<String> clauses_list = new ArrayList<String>();
        List<Object> args_list = new ArrayList<Object>();
        SQLDatabase db1 = null;
        SQLDatabase db2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String cls1[] = null;
        String cls2[] = null;
        Object args[] = null;
        String table = null;
        String clause = null;
        String orderBy = null;
        String limit = null;

        cls1 = new String[] {"tb.*"};
        cls2 = new String[] {"COUNT(0)"};

        //  构建筛选
        table = (String) PluginFactory.getInstance().applyFilters("attributeGet" + type + "Table", "jdroplet_posts");
        table += " as tb";
        List<String> joins = new ArrayList<>();
        for(int i=0; i<avs.size(); i++) {
            AttributeValue av = avs.get(i);
            joins.add(MessageFormat.format(" inner join jdroplet_attribute_values as a{0} on tb.id = a{1}.item_id and a{2}.slug=? ", i, i, i));
            args_list.add(av.getSlug());
        }
        table += StringUtils.join(joins, " ");
        args = args_list.toArray();
        //  构建筛选
        clause = group.toString();

        orderBy = "id desc";
        limit = (pageIndex - 1) * pageSize + "," + pageSize;

        try {
            db1 = newSQLDatabase();
            rs1 = db1.query(table, cls1, clause, args, null, null, orderBy, limit);
            while (rs1.next()) {
                itemId = rs1.getInt(1);
                itemIds.add(itemId);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db1.close();
        }

        try {
            db2 = newSQLDatabase();
            rs2 = db2.query(table, cls2, clause, args, null, null, null);
            if (rs2.next()) {
                datas.setTotalRecords(rs2.getInt(1));
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db2.close();
        }
        datas.setObjects(itemIds);
        return datas;
    }
}