package jdroplet.data.mysqidal;

import jdroplet.data.idal.IGroupMemberDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.GroupMember;
import jdroplet.util.DataSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by kuibo on 2018/4/8.
 */
public class GroupMemberDataProvider extends DataProvider implements IGroupMemberDataProvider {

    @Override
    public Entity newEntity() {
        return new GroupMember();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        GroupMember.DataColumns.fill(rs, (GroupMember) entity);
    }

    @Override
    public String getTable() {
        return GroupMember.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return GroupMember.DataColumns.getColums();
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return GroupMember.DataColumns.getKeyValues((GroupMember) entity);
    }

    @Override
    public Integer getMemberCount(Integer groupId) {
        return super.getCount(getTable(), "group_id=?", new Object[]{groupId});
    }

    @Override
    public boolean exists(Integer groupId, Integer userId) {
        return super.exists(getTable(), "group_id=? AND user_id=?", new Object[]{groupId, userId}) != null;
    }

    @Override
    public Integer getGroupCount(Integer userId) {
        return super.getCount(getTable(), "user_id=?", new Object[]{userId});
    }
}
