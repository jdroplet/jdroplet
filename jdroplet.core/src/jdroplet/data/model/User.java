package jdroplet.data.model;

import jdroplet.annotation.db.DataTable;
import jdroplet.bll.Roles;
import jdroplet.core.SystemConfig;
import jdroplet.enums.PasswordFormat;
import jdroplet.util.TextUtils;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@DataTable(name="jdroplet_users")
public class User extends Meta {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_users";

        public static String id = "id";
        public static String status = "status";
        public static String userName = "user_name";

        public static String displayName = "display_name";
        public static String password = "password";
        public static String email = "email";
        public static String salt = "salt";

        public static String avatar = "avatar";
        public static String phone = "phone";
        public static String comeFrom = "come_from";
        public static String passwordFormat = "password_format";

        public static String registered = "registered";
        public static String lastvisit = "lastvisit";
        public static String gender = "gender";
        public static String inviter = "inviter";

        public static String[] getColums() {
            return new String[]{id, status, userName,
                    displayName, password, email, salt,
                    avatar, phone, comeFrom, passwordFormat,
                    registered, lastvisit, gender, inviter};
        }

        public static void fill(ResultSet rs, User user) throws SQLException {
            Timestamp ts = null;

            user.id = rs.getInt(id);
            user.status = rs.getInt(status);
            user.userName = rs.getString(userName);

            user.displayName = rs.getString(displayName);
            user.password = rs.getString(password);
            user.email = rs.getString(email);
            user.salt = rs.getString(salt);

            user.avatar = rs.getString(avatar);
            user.phone = rs.getString(phone);
            user.comeFrom = rs.getString(comeFrom);
            user.passwordFormat = PasswordFormat.get(rs.getString(passwordFormat));

            user.registered = new Date(rs.getTimestamp(registered).getTime());
            ts = rs.getTimestamp(lastvisit);
            if (ts != null)
                user.lastvisit = new Date(ts.getTime());
            user.gender = rs.getInt(gender);
            user.inviter = rs.getInt(inviter);
        }

        public static Map<String, Object> getKeyValues(User user) {
            Map<String, Object> map = new HashMap<>();

            map.put(status, user.status);
            map.put(userName, user.userName);

            map.put(displayName, user.displayName);
            if (!TextUtils.isEmpty(user.password))
                map.put(password, user.password);
            map.put(email, user.email);
            map.put(salt, user.salt);

            map.put(avatar, user.avatar);
            map.put(phone, user.phone);
            map.put(comeFrom, user.comeFrom);
            map.put(passwordFormat, user.passwordFormat.toString());

            map.put(registered, new Timestamp(user.registered.getTime()));
            map.put(lastvisit, new Timestamp(user.lastvisit.getTime()));
            map.put(gender, user.gender);
            map.put(inviter, user.inviter);
            return map;
        }
    }
    
    private static final long serialVersionUID = 1L;

    // 已批准
    public static int APPROVED = 0;
    // 封禁中
    public static int BANNED = 1;
    // 待批准
    public static int APPROVALPENDING = 2;
    // 未批准
    public static int DISAPPROVED = 3;

    public static int FEMALE = 0;
    public static int MALE = 1;
    public static int UNKNOW = 2;

    private Integer id;
    private Integer status;
    private Integer gender;
    private Integer inviter;
    private String userName;
    private String displayName;
    private String password;
    private String email;
    private String salt;
    private String avatar;
    private String phone;
    private String comeFrom;
    private PasswordFormat passwordFormat;
    private Date registered;
    private Date lastvisit;

    public User() {
        email = avatar = phone = comeFrom= "";
        registered = lastvisit = new Date();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PasswordFormat getPasswordFormat() {
        return passwordFormat;
    }

    public void setPasswordFormat(PasswordFormat passwordFormat) {
        this.passwordFormat = passwordFormat;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Date getLastvisit() {
        return lastvisit;
    }

    public void setLastvisit(Date lastvisit) {
        this.lastvisit = lastvisit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLanguage() {
        return "zh-cn";
    }

    public Integer getGender() {
        return gender;
    }

    public Integer getInviter() {
        return inviter;
    }

    public void setInviter(Integer inviter) {
        this.inviter = inviter;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public boolean isRole(String roleName) {
        return isInRoles(new String[]{roleName});
    }

    public boolean isInRoles(String[] roleNames) {
        String[] userRoles = Roles.getUserRoleNames(id);
        for (String userRole : userRoles) {
            for (String roleName : roleNames) {
                if (roleName.equals(userRole))
                    return true;
            }
        }
        return false;
    }

    public boolean isAdministrator() {
        RolesConfiguration roles = SystemConfig.getRolesConfiguration();
        return isInRoles(new String[]{roles.getAdministrators()});
    }

    public boolean isModerators() {
        RolesConfiguration roles = SystemConfig.getRolesConfiguration();
        return isInRoles(new String[]{roles.getModerators()});
    }

    public boolean isEditors() {
        RolesConfiguration roles = SystemConfig.getRolesConfiguration();
        return isInRoles(new String[]{roles.getEditors()});
    }
}
