package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IMessagesRecipientDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Message;
import jdroplet.data.model.MessagesRecipient;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2018/2/24.
 */
public class MessagesRecipientDataProvider extends DataProvider implements IMessagesRecipientDataProvider {

    @Override
    public DataSet<MessagesRecipient> getMessagesRecipients(Integer userId, Message.MessageBoxType type, Integer pageIndex, Integer pageSize) {
        MessagesRecipient mr = null;
        DataSet<MessagesRecipient> datas = new DataSet<MessagesRecipient>();
        ArrayList<MessagesRecipient> msgs = new ArrayList<MessagesRecipient>();
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

        table = "jdroplet_messages_recipients JOIN jdroplet_messages ON jdroplet_messages.id = jdroplet_messages_recipients.message_id";
        cls1 = getColums();
        cls2 = new String[] {"COUNT(0)"};

        //  构建筛选
        if (type == Message.MessageBoxType.Inbox) {
            clauses_list.add("user_id = ? AND is_sender = 0");
            args_list.add(userId);
        } else if (type == Message.MessageBoxType.Sentbox) {
            clauses_list.add("sender_id = ? AND sender_id = user_id AND is_sender = 1");
            args_list.add(userId);
        }

        args = args_list.toArray();
        clause = StringUtils.join(clauses_list, " AND ");

        orderBy = "post_time DESC";
        limit = (pageIndex - 1) * pageSize + "," + pageSize;

        try {
            db1 = new SQLDatabase();
            rs1 = db1.query(table, cls1, clause, args, null, null, orderBy, limit);
            while (rs1.next()) {
                mr = new MessagesRecipient();
                fill(rs1, mr);
                msgs.add(mr);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db1.close();
        }

        try {
            db2 = new SQLDatabase();
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
        datas.setObjects(msgs);
        return datas;
    }

    @Override
    public Integer getUnReads(Integer userId) {
        return super.getCount(getTable(), "user_id = ? AND is_sender = 0 AND is_read = 0", new Object[]{userId});
    }

    @Override
    public Entity newEntity() {
        return new MessagesRecipient();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        MessagesRecipient.DataColumns.fill(rs, (MessagesRecipient) entity);
    }

    @Override
    public String getTable() {
        return MessagesRecipient.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return MessagesRecipient.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return MessagesRecipient.DataColumns.getKeyValues((MessagesRecipient) entity);
    }
}
