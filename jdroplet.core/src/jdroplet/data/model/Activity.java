package jdroplet.data.model;


import jdroplet.core.HttpRequest;
import jdroplet.sns.proxy.WechatProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/1/24.
 */
public class Activity extends Meta {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_activities";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String status = "status";

        public static String name = "name";
        public static String type = "type";
        public static String description = "description";

        public static String createTime = "create_time";
        public static String startTime = "start_time";
        public static String expired = "expired";


        public static String[] getColums() {
            return new String[]{id, shopId, status,
                    name, type, description,
                    createTime, startTime, expired};
        }

        public static void fill(HttpRequest req, Activity activity) {
            activity.id = req.getIntParameter(id);
            activity.shopId = req.getIntParameter(shopId);
            activity.status = req.getIntParameter(status);

            activity.name = req.getParameter(name);
            activity.type = req.getParameter(type);
            activity.description = req.getParameter(description);

            activity.startTime = req.getDateParameter(startTime);
            activity.expired = req.getDateParameter(expired);

            Integer item_size = req.getIntParameter("item_size");
            for (int idx = 0; idx < item_size; idx++) {
                Integer val = req.getIntParameter("activity_invite_mission_" + idx);

                activity.setValue("activity_invite_mission_" + idx, val);
            }
            activity.setValue("inviteMaxMembers",  req.getIntParameter("activity_invite_maxMembers"));
            activity.setValue("inviteMaxJoinCount", req.getIntParameter("activity_invite_maxJoinCount"));
            activity.setValue("inviteMaxCreateCount", req.getIntParameter("activity_invite_maxCreateCount"));
            activity.setValue("item_size", req.getIntParameter("item_size"));
            activity.setValue("game_count", req.getIntParameter("game_count"));
            activity.setValue("maxIPcount", req.getIntParameter("maxIPcount"));
            activity.setValue("day_reset", req.getIntParameter("day_reset"));

            activity.setValue("page_share_title", req.getParameter("page_share_title"));
            activity.setValue("page_share_desc", req.getParameter("page_share_desc"));
            activity.setValue("page_bg_color", req.getParameter("page_bg_color"));
            activity.setValue("page_bg_img", req.getParameter("page_bg_img"));
        }

        public static void fill(ResultSet rs, Activity activity) throws SQLException {
            activity.id = rs.getInt(id);
            activity.shopId = rs.getInt(shopId);
            activity.status = rs.getInt(status);

            activity.name = rs.getString(name);
            activity.type = rs.getString(type);
            activity.description = rs.getString(description);

            activity.createTime = new Date(rs.getTimestamp(createTime).getTime());
            activity.startTime = new Date(rs.getTimestamp(startTime).getTime());
            activity.expired = new Date(rs.getTimestamp(expired).getTime());
        }

        public static Map<String, Object> getKeyValues(Activity activity) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, activity.shopId);
            map.put(status, activity.status);

            map.put(name, activity.name);
            map.put(type, activity.type);
            map.put(description, activity.description);

            map.put(createTime, new Timestamp(activity.createTime.getTime()));
            map.put(startTime, new Timestamp(activity.startTime.getTime()));
            map.put(expired, new Timestamp(activity.expired.getTime()));

            return map;
        }
    }

    public static Integer ENABLED = 1;
    public static Integer DISABLED = 2;

    private Integer id;
    private Integer shopId;
    private Integer status;

    private String name;
    private String type;
    private String description;

    private Date createTime = new Date();
    private Date startTime;
    private Date expired;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
