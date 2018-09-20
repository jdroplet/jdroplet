package jdroplet.data.mysqidal;

import jdroplet.data.idal.IMessageDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/13.
 */
public class MessageDataProvider extends DataProvider implements IMessageDataProvider {

    @Override
    public Entity newEntity() {
        return new Message();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Message.DataColumns.fill(rs, (Message) entity);
    }

    @Override
    public String getTable() {
        return Message.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Message.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Message.DataColumns.getKeyValues((Message) entity);
    }
}
