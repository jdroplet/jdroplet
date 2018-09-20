package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/3/27.
 */
public class Lottery extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_lotteries";

        public static String id = "id";
        public static String shopId = "shop_id";
        public static String activityId = "activity_id";
        public static String userId = "user_id";

        public static String count = "count";
        public static String rate = "rate";
        public static String rank = "rank";
        public static String flag = "flag";
        public static String point = "point";

        public static String name = "name";
        public static String message = "message";
        public static String icon = "icon";

        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(HttpRequest req, Lottery lt) {
            lt.id = req.getIntParameter(id);
            lt.shopId = req.getIntParameter(shopId);
            lt.activityId = req.getIntParameter(activityId);

            lt.count = req.getIntParameter(count);
            lt.rate = req.getIntParameter(rate);
            lt.rank = req.getIntParameter(rank);
            lt.flag = req.getIntParameter(flag);
            lt.point = req.getIntParameter(point);

            lt.name = req.getParameter(name);
            lt.message = req.getParameter(message);
            lt.icon = req.getParameter(icon);

            if (lt.flag == null) {
                int item_type1 = req.getIntParameter("item_type1");//
                int item_type2 = req.getIntParameter("item_type2");//
                int item_type3 = req.getIntParameter("item_type3");//

                lt.flag = item_type1 | item_type2 | item_type3;
            }

            lt.setValue("sub_id", req.getIntParameter("sub_id"));
        }
        
        public static void fill(ResultSet rs, Lottery lt) throws SQLException {
            lt.id = rs.getInt(id);
            lt.shopId = rs.getInt(shopId);
            lt.activityId = rs.getInt(activityId);
            lt.userId = rs.getInt(userId);

            lt.count = rs.getInt(count);
            lt.rate = rs.getInt(rate);
            lt.rank = rs.getInt(rank);
            lt.flag = rs.getInt(flag);
            lt.point = rs.getInt(point);

            lt.name = rs.getString(name);
            lt.message = rs.getString(message);
            lt.icon = rs.getString(icon);
        }

        public static Map<String, Object> getKeyValues(Lottery lt) {
            Map<String, Object> map = new HashMap<>();

            map.put(shopId, lt.shopId);
            map.put(activityId, lt.activityId);
            map.put(userId, lt.userId);

            map.put(count, lt.count);
            map.put(rate, lt.rate);
            map.put(rank, lt.rank);
            map.put(flag, lt.flag);
            map.put(point, lt.point);
            map.put(name, lt.name);
            map.put(message, lt.message);
            map.put(icon, lt.icon);

            return map;
        }
    }

    public final static int SAVE 		= 0x01;
    public final static int POP 		= 0x02;
    public final static int SINGLE 		= 0x04;
    public final static int SHOW_LIST	= 0x08;

    public final static int NORMAL = 0x100;
    public final static int POINT  = 0x200;


    private Integer id;
    private Integer shopId;
    private Integer activityId;
    private Integer userId;
    private Integer count;
    private Integer rate;
    private Integer rank;
    private Integer flag;
    private Integer point;

    private String name;
    private String message;
    private String icon;

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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
