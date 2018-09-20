package jdroplet.data.model;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Attachment extends Entity {

    public static class DataColumns extends DataColumnsBase {
        public static String table = "jdroplet_attachments";

        public static String id = "id";
        public static String userId = "user_id";
        public static String itemId = "item_id";
        public static String shopId = "shop_id";
        public static String totalDownloads = "total_downloads";
        public static String size = "size";

        public static String diskFilename = "disk_filename";
        public static String filename = "filename";
        public static String contentType = "content_type";
        public static String description = "description";
        public static String extension = "extension";
        public static String folder = "folder";
        public static String hasThumb = "has_thumb";
        public static String createTime = "create_time";


        public static String[] getColums() {
            return new String[]{id, userId, itemId, shopId, totalDownloads, size,
                    diskFilename, filename, contentType,
                    description, extension, hasThumb, folder, createTime};
        }

        public static void fill(ResultSet rs, Attachment att) throws SQLException {
            att.id = rs.getInt(id);
            att.userId = rs.getInt(userId);
            att.itemId = rs.getInt(itemId);
            att.shopId = rs.getInt(shopId);
            att.totalDownloads = rs.getInt(totalDownloads);
            att.size = (BigInteger) rs.getObject(size);

            att.diskFilename = rs.getString(diskFilename);
            att.filename = rs.getString(filename);
            att.contentType = rs.getString(contentType);

            att.description = rs.getString(description);
            att.extension = rs.getString(extension);
            att.hasThumb = rs.getBoolean(hasThumb);
            att.folder = rs.getString(folder);

            att.createTime = new Date(rs.getTimestamp(createTime).getTime());
        }

        public static Map<String, Object> getKeyValues(Attachment att) {
            Map<String, Object> map = new HashMap<>();

            map.put(userId, att.userId);
            map.put(itemId, att.itemId);
            map.put(shopId, att.shopId);
            map.put(totalDownloads, att.totalDownloads);
            map.put(size, att.size);

            map.put(diskFilename, att.diskFilename);
            map.put(filename, att.filename);
            map.put(contentType, att.contentType);

            map.put(description, att.description);
            map.put(extension, att.extension);
            map.put(hasThumb, att.hasThumb);
            map.put(folder, att.folder);
            if (att.id == null || att.id == 0)
                map.put(createTime, new Timestamp(att.createTime.getTime()));
            return map;
        }
    }

    
    private Integer id;
    private Integer userId;
    private Integer itemId;
    private Integer shopId;
    private Integer totalDownloads = 0;
    private BigInteger size;

    private String diskFilename;
    private String filename;
    private String contentType;
    private String description;
    private String extension;
    private String folder;
    private boolean hasThumb;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(Integer totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public String getDiskFilename() {
        return diskFilename;
    }

    public void setDiskFilename(String diskFilename) {
        this.diskFilename = diskFilename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isHasThumb() {
        return hasThumb;
    }

    public void setHasThumb(boolean hasThumb) {
        this.hasThumb = hasThumb;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
