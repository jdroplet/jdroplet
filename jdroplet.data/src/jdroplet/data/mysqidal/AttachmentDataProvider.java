package jdroplet.data.mysqidal;


import jdroplet.data.idal.IAttachmentDataProvider;
import jdroplet.data.model.Attachment;
import jdroplet.data.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class AttachmentDataProvider extends DataProvider implements IAttachmentDataProvider {

    @Override
    public Entity newEntity() {
        return new Attachment();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Attachment.DataColumns.fill(rs, (Attachment) entity);
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Attachment.DataColumns.getKeyValues((Attachment) entity);
    }

    @Override
    public String getTable() {
        return Attachment.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Attachment.DataColumns.getColums();
    }

}
