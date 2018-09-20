package jdroplet.data.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/3/20.
 */
public class Statistic extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {
        public static String table = "jdroplet_statistics";

        public static String id = "id";
        public static String gruopId = "group_id";
        public static String name = "name";
        public static String data = "data";
        public static String itemId = "item_id";
        public static String createTime = "create_time";

        public static String[] getColums() {
            return new String[]{"*"};
        }

        public static void fill(ResultSet rs, Statistic stat) throws SQLException {
            stat.id = rs.getInt(id);
            stat.gruopId = rs.getInt(gruopId);
            stat.name = rs.getString(name);
            stat.data = rs.getString(data);
            stat.itemId = rs.getInt(itemId);
            stat.createTime = new Date(rs.getTimestamp(createTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Statistic stat) {
            Map<String, Object> map = new HashMap<>();

            map.put(gruopId, stat.gruopId);
            map.put(name, stat.name);
            map.put(name, stat.name);
            map.put(data, stat.data);
            map.put(itemId, stat.itemId);
            map.put(createTime, new Timestamp(stat.createTime.getTime()));

            return map;
        }
    }
    
    private Integer id;
    private Integer gruopId;
    private Integer itemId;
    private String name;
    private String type;
    private String data;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGruopId() {
        return gruopId;
    }

    public void setGruopId(Integer gruopId) {
        this.gruopId = gruopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
