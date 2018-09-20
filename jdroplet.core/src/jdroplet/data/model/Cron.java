package jdroplet.data.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2017/12/26.
 */
public class Cron extends Entity{

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_crons";

        public static String id = "id";
        public static String name = "name";
        public static String description = "description";

        public static String refer = "refer";
        public static String cron = "cron";

        public static String status = "status";


        public static String[] getColums() {
            return new String[]{id, name, description, refer,
                    cron, status};
        }

        public static void fill(ResultSet rs, Cron cron) throws SQLException {
            cron.id = rs.getInt(id);
            cron.setName(rs.getString(name));
            cron.setDescription(rs.getString(description));

            cron.setRefer(rs.getString(refer));
            cron.setCron(rs.getString(DataColumns.cron));

            cron.setStatus(rs.getInt(status));
        }

        public static Map<String, Object> getKeyValues(Cron cron) {
            Map<String, Object> map = new HashMap<>();

            map.put(name, cron.getName());
            map.put(description, cron.getDescription());
            map.put(refer, cron.getRefer());
            map.put(DataColumns.cron, cron.getCron());
            map.put(status, cron.status);
            return map;
        }
    }

    private Integer id;
    private String name;
    private String description;
    private String refer;
    private String cron;
    private Integer status;

    public static final int ENABLED = 1;
    public static final int DISABLE = 0;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
