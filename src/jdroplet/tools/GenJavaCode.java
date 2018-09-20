package jdroplet.tools;


import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

public class GenJavaCode {

    static class Field {
        String type;
        String name;
        String remark;
    }

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String pwd = "";
    private static final String user = "root";
    private static final String url = "jdbc:mysql://localhost/jdroplet" + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF-8";

    private static String entityPackage = "jdroplet.data.model";//你的实体类所在的包的位置
    private static String viewPackage = "jdroplet.app.view";
    private static String controllPackage = "jdroplet.bll";

    private static String idlPackage = "jdroplet.data.idal";
    private static String mysqlIDLPackage = "jdroplet.data.mysqidal";

    private static String exportTable = "jdroplet_attribute_values";
    private final static String OUT_PUTH         = "D:\\jdroplet\\crc\\";

    public static void main(String[] args) {
        Connection conn = getConnections();
        List<Field> fields = new ArrayList();
        Field f = null;

        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                if(exportTable.equals(tableName)) {
                    ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
                    while (rs.next()) {
                        f = new Field();
                        f.type = sqlType2JavaType(rs.getString("TYPE_NAME"));
                        f.name = rs.getString("COLUMN_NAME");
                        f.remark = rs.getString("REMARKS");

                        fields.add(f);
                    }
                    String entityName = camelCaseName(tableName).replace("jdroplet", "");
                    if (entityName.charAt(entityName.length() - 1) == 's')
                        entityName = entityName.substring(0, entityName.length() - 1);

                    buildEntity(fields, tableName);
                    buildView(tableName);
                    buildControll(tableName);

                    buildIdl(entityName);
                    buildMysqlIDL(entityName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }

    }

    private static void buildEntity(List<Field> fields, String tableName) throws SQLException, IOException {
        File file = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        String entityName = null;
        String filePath = null;
        StringBuilder sb = new StringBuilder();

        entityName = camelCaseName(tableName).replace("jdroplet", "");
        if (entityName.charAt(entityName.length() - 1) == 's')
            entityName = entityName.substring(0, entityName.length() - 1);

        sb.append("package " + entityPackage + ";\r\n");
        sb.append("\r\n");
        sb.append("import jdroplet.core.HttpRequest;\r\n");
        sb.append("import java.util.HashMap;\r\n");
        sb.append("import java.util.Map;\r\n");
        sb.append("import java.sql.ResultSet;\r\n");
        sb.append("import java.sql.SQLException;\r\n\r\n");
        sb.append("   /**\r\n");
        sb.append("    * " + entityName + " 实体类\r\n");
        sb.append("    * " + getDate() + " bourne\r\n");
        sb.append("    */ \r\n");
        sb.append("\r\npublic class " + entityName + " extends Entity {\r\n");
        sb.append(createDataColumns(fields, tableName, entityName));
        for(Field f:fields) {
            sb.append(createProperty(f));
        }
        //提供Get和Set方法
        sb.append("\r\n");
        for(Field f:fields) {
            sb.append(createMethod(f));
        }
        sb.append("}\r\n");

        filePath = OUT_PUTH + entityPackage.replace(".", "\\") + "\\";
        file = new File(filePath);
        file.mkdirs();

        file = new File(filePath + entityName + ".java");
        if (file.exists()) {
        } else {
            file.createNewFile();
        }
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        System.out.println(entityName + "生成成功。");
    }

    private static void buildView(String tableName) throws IOException {
        File file = null;
        FileWriter fw = null;
        PrintWriter pw = null;  
        String viewName = null;
        String filePath = null;
        StringBuilder sb = new StringBuilder();

        viewName = camelCaseName(tableName).replace("jdroplet", "");
        if (viewName.charAt(viewName.length() - 1) == 's')
            viewName = viewName.substring(0, viewName.length() - 1);
        viewName += "Page";

        sb.append("package " + viewPackage + ";\r\n");
        sb.append("\r\n");
        sb.append("   /**\r\n");
        sb.append("    * " + viewName + " View类\r\n");
        sb.append("    * " + getDate() + " bourne\r\n");
        sb.append("    */ \r\n");
        sb.append("\r\npublic class " + viewName + " extends MasterPage {\r\n");
        sb.append("}\r\n");

        filePath = OUT_PUTH + viewPackage.replace(".", "\\") + "\\";
        file = new File(filePath);
        file.mkdirs();

        file = new File(filePath + viewName + ".java");
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        System.out.println(viewName + "生成成功。");
    }

    private static void buildControll(String tableName) throws IOException {
        File file = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        String viewName = null;
        String entityName = null;
        String filePath = null;
        StringBuilder sb = new StringBuilder();

        viewName = camelCaseName(tableName).replace("jdroplet", "");
        entityName = camelCaseName(tableName).replace("jdroplet", "");
        if (entityName.charAt(entityName.length() - 1) == 's')
            entityName = entityName.substring(0, entityName.length() - 1);

        sb.append("package " + controllPackage + ";\r\n");
        sb.append("\r\n");
        sb.append("import jdroplet.data.dalfactory.DataAccess;\r\n");
        sb.append("import jdroplet.data.idal.I" + entityName + "DataProvider;\r\n");
        sb.append("import jdroplet.data.model." + entityName + ";\r\n");
        sb.append("\r\n");
        sb.append("   /**\r\n");
        sb.append("    * " + viewName + " Bll类\r\n");
        sb.append("    * " + getDate() + " bourne\r\n");
        sb.append("    */ \r\n");
        sb.append("\r\npublic class " + viewName + " {\r\n");

        sb.append("    public static Integer save(" + entityName + " entity) {\n" +
                "        I" + entityName +"DataProvider provider = (I" + entityName + "DataProvider) DataAccess.instance().get"+ entityName +"DataProvider();\n" +
                "        Integer id = null;\n" +
                "\n" +
                "        id = provider.save(entity);\n" +
                "        if (entity.getId() == null || entity.getId() == 0)\n" +
                "            entity.setId(id);\n" +
                "\n" +
                "        return id;\n" +
                "    }");

        sb.append("\r\n}");

        filePath = OUT_PUTH + controllPackage.replace(".", "\\") + "\\";
        file = new File(filePath);
        file.mkdirs();

        file = new File(filePath + viewName + ".java");
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        System.out.println(viewName + "生成成功。");
    }

    public static void buildIdl(String entityName) throws IOException {
        File file = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        String viewName = null;
        String filePath = null;
        StringBuilder sb = new StringBuilder();


        sb.append("package " + idlPackage + ";\r\n");
        sb.append("\r\n");
        sb.append("\r\npublic interface I"+entityName+"DataProvider extends IDataProvider{\r\n");
        sb.append("}\r\n");

        filePath = OUT_PUTH + idlPackage.replace(".", "\\") + "\\";
        file = new File(filePath);
        file.mkdirs();

        file = new File(filePath + "I" + entityName + "DataProvider.java");
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        System.out.println("I" + entityName + "DataProvider 生成成功。");
    }

    public static void buildMysqlIDL(String entityName) throws IOException {
        File file = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        String filePath = null;
        StringBuilder sb = new StringBuilder();


        sb.append("package " + mysqlIDLPackage + ";\r\n");
        sb.append("\r\n");
        sb.append("import jdroplet.data.idal.I"+entityName+"DataProvider;\r\n");
        sb.append("import jdroplet.data.model.Entity;\r\n");
        sb.append("import jdroplet.data.model."+entityName+";\r\n");
        sb.append("import java.sql.ResultSet;\r\n");
        sb.append("import java.sql.SQLException;\r\n");
        sb.append("import java.util.Map;\r\n");
        sb.append("\r\n");
        sb.append("\r\npublic class "+entityName+"DataProvider extends DataProvider implements I"+entityName+"DataProvider {\r\n");
        sb.append("    @Override\n" +
                "    public Entity newEntity() {\n" +
                "        return new "+entityName+"();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void fill(ResultSet rs, Entity entity) throws SQLException {\n" +
                "        "+entityName+".DataColumns.fill(rs, ("+entityName+") entity);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getTable() {\n" +
                "        return "+entityName+".DataColumns.table;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String[] getColums() {\n" +
                "        return "+entityName+".DataColumns.getColums();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Map<String, Object> getKeyValues(Entity entity) {\n" +
                "        return "+entityName+".DataColumns.getKeyValues(("+entityName+") entity);\n" +
                "    }");
        sb.append("\r\n}");

        filePath = OUT_PUTH + mysqlIDLPackage.replace(".", "\\") + "\\";
        file = new File(filePath);
        file.mkdirs();

        file = new File(filePath  + entityName + "DataProvider.java");
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        System.out.println(entityName + "DataProvider 生成成功。");
    }

    public static String createDataColumns(List<Field> fields, String tableName, String entityName) {
        StringBuilder sb = new StringBuilder();
        List<String> names = new ArrayList();

        sb.append("\r\n    public static class DataColumns extends Entity.DataColumnsBase {\r\n");
        sb.append("\r\n        public static String table = \""+tableName+"\";\r\n\r\n");
        for(Field f:fields) {
            sb.append("        public static String " + camelCaseName(f.name) + " = \"" + f.name + "\";\r\n");
        }

        sb.append("        public static String[] getColums() {");
        sb.append("\r\n            return new String[]{");
        for(Field f:fields) {
            names.add(camelCaseName(f.name));
        }
        sb.append(StringUtils.join(names, ","));
        sb.append("};");
        sb.append("\r\n        }");


        sb.append("\r\n\r\n        public static void fill(HttpRequest req, " + entityName + " entity) {");
        for(Field f:fields) {
            if (f.type.equals("Integer"))
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = req.getIntParameter(" + camelCaseName(f.name) + ");");
            else if (f.type.equals("Date"))
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = req.getDateParameter(" + camelCaseName(f.name) + ");");
            else
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = req.getParameter(" + camelCaseName(f.name) + ");");
        }
        sb.append("\r\n        }");

        sb.append("\r\n\r\n        public static void fill(ResultSet rs, " + entityName + " entity) throws SQLException {");
        for(Field f:fields) {
            if (f.type.equals("Integer"))
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = rs.getInt(" + camelCaseName(f.name) + ");");
            else if (f.type.equals("Date"))
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = rs.getDate(" + camelCaseName(f.name) + ");");
            else
                sb.append("\r\n            entity." + camelCaseName(f.name) + " = rs.getString(" + camelCaseName(f.name) + ");");
        }
        sb.append("\r\n        }");

        sb.append("\r\n\r\n        public static Map<String, Object> getKeyValues(" + entityName + " entity) {");
        sb.append("\r\n            Map<String, Object> map = new HashMap<>();");
        for(Field f:fields) {
            if (f.type.equals("Date"))
                sb.append("\r\n            map.put(" + camelCaseName(f.name) + ", new Timestamp(entity." + camelCaseName(f.name) +".getTime()));");
            else
                sb.append("\r\n            map.put("+ camelCaseName(f.name) +", entity." + camelCaseName(f.name) +");");
        }
        sb.append("\r\n            return map;");
        sb.append("\r\n        }");

        sb.append("\r\n    }\r\n");

        return sb.toString();
    }

    /**
     * 生成属性
     */
    public static String createProperty(Field f) {
        StringBuilder sb = new StringBuilder();

        if (f.remark != null && !"".equals(f.remark)) {
            sb.append("    /**\r\n");
            sb.append("    *" + f.remark + "\r\n");
            sb.append("    */\r\n");
        } else {
            sb.append("    //" + f.name + "\r\n");
        }
        sb.append("    private " + f.type + " " + camelCaseName(f.name) + ";\r\n\r\n");

        return sb.toString();
    }

    /**
     * 生成方法
     */
    public static String createMethod(Field f) {
        StringBuilder sb = new StringBuilder();

        String name = camelCaseName(f.name);
        sb.append("    public void set" + initcap(name) + "(" + f.type + " " + name + "){\r\n");
        sb.append("        this." + name + " = " + name + ";\r\n");
        sb.append("    }\r\n\r\n");
        sb.append("    public " + f.type + " get" + initcap(name) + "(){\r\n");
        sb.append("        return " + name + ";\r\n");
        sb.append("    }\r\n");
        sb.append("\r\n");

        return sb.toString();
    }


    // 创建数据库连接
    public static Connection getConnections() {
        Connection getConnection = null;
        try {
            Class.forName(driver);
            getConnection = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }

    // 将单词字母首字母改为大写
    private static String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    // 判断属性类型
    public static String sqlType2JavaType(String sqlType) {
        String str = null;
        sqlType = sqlType.replace("UNSIGNED", "").trim();
        if (sqlType.equalsIgnoreCase("bit")) {
            str = "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            str = "Byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            str = "Short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            str = "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            str = "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            str = "Float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            str = "Double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text") || sqlType.equalsIgnoreCase("longtext")) {
            str = "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("timestamp")
                || sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("time")) {
            str = "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            str = "Blod";
        }
        return str;
    }

    // 获取格式化后的时间
    private static String getDate() {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = sdf.format(new Date());
        return time;
    }

}
