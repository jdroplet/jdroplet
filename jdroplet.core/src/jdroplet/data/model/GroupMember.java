package jdroplet.data.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupMember extends Meta {

	public static class DataColumns extends DataColumnsBase {
		public static String table = "jdroplet_groups_members";

		public static String id = "id";
		public static String activityId = "activity_id";
		public static String groupId = "group_id";
		public static String userId = "user_id";
		public static String inviterId = "inviter_id";
		public static String isAdmin = "is_admin";
		public static String createTime = "create_time";

		public static String[] getColums() {
			return new String[]{"*"};
		}

		public static void fill(ResultSet rs, GroupMember gm) throws SQLException {
			gm.id = rs.getInt(id);
			gm.activityId = rs.getInt(userId);
			gm.userId = rs.getInt(userId);
			gm.groupId = rs.getInt(groupId);
			gm.inviterId = rs.getInt(inviterId);
			gm.isAdmin = rs.getBoolean(isAdmin);

			gm.createTime = new Date(rs.getTimestamp(createTime).getTime());
		}

		public static Map<String, Object> getKeyValues(GroupMember gm) {
			Map<String, Object> map = new HashMap<>();

			map.put(activityId, gm.activityId);
			map.put(userId, gm.userId);
			map.put(groupId, gm.groupId);
			map.put(inviterId, gm.inviterId);
			map.put(isAdmin, gm.isAdmin);

			map.put(createTime, new Timestamp(gm.createTime.getTime()));

			return map;
		}
	}

	private Integer id;
	private Integer activityId;
	private Integer groupId;
	private Integer userId;
	private Integer inviterId;
	private boolean isAdmin;
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getInviterId() {
		return inviterId;
	}

	public void setInviterId(Integer inviterId) {
		this.inviterId = inviterId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
