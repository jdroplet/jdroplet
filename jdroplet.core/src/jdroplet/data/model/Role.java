package jdroplet.data.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Role extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_roles";

        public static String id = "id";
        public static String name = "name";
        public static String description = "description";


        public static String[] getColums() {
            return new String[]{id, name, description};
        }

        public static void fill(ResultSet rs, Role r) throws SQLException {
            r.id = rs.getInt(id);
            r.name = rs.getString(name);
            r.description = rs.getString(description);

        }

        public static Map<String, Object> getKeyValues(Role r) {
            Map<String, Object> map = new HashMap<>();

            map.put(name, r.name);
            map.put(description, r.description);

            return map;
        }
    }


    private Integer id;
    private String name;
    private String description;


    public Role() {
        this(Integer.valueOf(0));
    }

    public Role(Integer id) {
        this(id, "");
    }

    public Role(Integer id, String name) {
        this(id, name, "");
    }

    public Role(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

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


}
