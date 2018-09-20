package jdroplet.data.model;

import jdroplet.core.DateTime;
import jdroplet.core.HttpRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/8/21.
 */
public class Visit extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_visits";

        public static String id = "id";
        public static String user = "user";
        public static String ip = "ip";
        public static String referer = "referer";

        public static String browser = "browser";
        public static String platform = "platform";
        public static String region = "region";
        public static String city = "city";
        public static String resource = "resource";

        public static String userAgent = "user_agent";
        public static String resolution = "resolution";
        public static String depth = "depth";
        public static String createTime = "create_time";

        public static String isp = "isp";


        public static String[] getColums() {
            return new String[]{id, user, ip, referer,
                    browser, platform, region, city, resource,
                    userAgent, resolution, depth, createTime,
                    isp};
        }

        public static void fill(HttpRequest req, Visit visit) {
            visit.id = 0;
            visit.user = "";
            visit.ip = req.getXRemoteAddr();
            visit.referer = req.getHeader("Referer");

            visit.browser = "";
            visit.platform = "";
            visit.region = "";
            visit.city = "";
            visit.resource = req.getParameter(resource);

            visit.userAgent = req.getHeader("User-Agent").toLowerCase();
            visit.resolution = req.getParameter(resolution);
            visit.depth = 0;
            visit.createTime = new Date();

            visit.isp = "";
        }

        public static void fill(ResultSet rs, Visit visit) throws SQLException {
            visit.id = rs.getInt(id);
            visit.user = rs.getString(user);
            visit.ip = rs.getString(ip);
            visit.referer = rs.getString(referer);

            visit.browser = rs.getString(browser);
            visit.platform = rs.getString(platform);
            visit.region = rs.getString(region);
            visit.city = rs.getString(city);
            visit.resource = rs.getString(resource);

            visit.userAgent = rs.getString(userAgent);
            visit.resolution = rs.getString(resolution);
            visit.depth = rs.getInt(depth);
            visit.createTime = new Date(rs.getTimestamp(createTime).getTime());

            visit.isp = rs.getString(isp);
        }

        public static Map<String, Object> getKeyValues(Visit visit) {
            Map<String, Object> map = new HashMap<>();

            map.put(id, visit.id);
            map.put(user, visit.user);
            map.put(ip, visit.ip);
            map.put(referer, visit.referer);

            map.put(browser, visit.browser);
            map.put(platform, visit.platform);
            map.put(region, visit.region);
            map.put(city, visit.city);
            map.put(resource, visit.resource);

            map.put(userAgent, visit.userAgent);
            map.put(resolution, visit.resolution);
            map.put(depth, visit.depth);
            map.put(createTime, new Timestamp(visit.createTime.getTime()));

            map.put(isp, visit.isp);

            return map;
        }
    }

    private Integer id;
    private String user;
    private String ip;
    private String referer;
    private String browser;
    private String platform;
    private String region;
    private String city;
    private String resource;
    private String userAgent;
    private String resolution;
    private String isp;
    private Integer depth;
    private Date createTime;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
