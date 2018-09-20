package jdroplet.data.model;


import jdroplet.core.HttpRequest;
import jdroplet.sns.proxy.WechatProxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cluster extends Meta {

	public static class DataColumns {
		public static String table = "jdroplet_clusters";

		public static String id = "id";
		public static String name = "name";

		public static String[] getColums() {
			return new String[]{id, name};
		}

		public static void fill(HttpRequest req, Cluster cluster) {
			cluster.setId(req.getIntParameter(id));
			cluster.setName(req.getParameter(name));

			// set meta
			cluster.setValue("EnableWechatUnauthorizeLogin", req.getParameter("EnableWechatUnauthorizeLogin"));
		}

		public static void fill(ResultSet rs, Cluster cluster) throws SQLException {
			cluster.setId(rs.getInt(id));
			cluster.setName(rs.getString(name));
		}

		public static Map<String, Object> getKeyValues(Cluster cluster) {
			Map<String, Object> map = new HashMap<>();

			map.put(name, cluster.getName());
			return map;
		}
	}

	private Integer id = Integer.valueOf(1);
	private int defaultRoleCookieExpiration = 3600;
	private int defaultImageThumbWidth = 320;
	private int defaultImageThumbHeight = 240;
	
	private boolean disabled = false;
	private boolean defaultEnableRoleCookie = true;
	private boolean defaultEnableAttachments = true;
	private boolean defaultEnableWechatUnauthorizeLogin = false;

	private String name;
	private String defaultAllowedAttachmentTypes = "zip;rar;jpg;gif;png;wma;mp3;txt;css;doc;docx;pdf;xps";
	private String defaultMaskImage = "mask.png";


	private long defaultMaxAttachmentSize = 4096; // in Kilobytes


	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}

	public String getMaskImage() {
		String v = getString("MaskImage");

		if (v != null)
			return v;
		else
			return defaultMaskImage;
	}
	
	public String getAllowedAttachmentTypes() {
		String v = getString("AllowedAttachmentTypes");

		if (v != null)
			return v;
		else
			return defaultAllowedAttachmentTypes;
	}
	
	public void setAllowedAttachmentTypes(String value) {
		setValue("AllowedAttachmentTypes", value);
	}

	public int getAuthCookieExpiration() {
		return getInt("AuthCookieExpiration", defaultRoleCookieExpiration);
	}

	public int getImageThumbWidth() {
		return getInt("ImageThumbWidth", defaultImageThumbWidth);
	}
	
	public int getImageThumbHeight() {
		return getInt("ImageThumbHeight", defaultImageThumbHeight);
	}

	public void setRoleCookieExpiration(int value) {
		setValue("RoleCookieExpiration", value);
	}

	public boolean enableRoleCookie() {
		return getBool("EnableRoleCookie", defaultEnableRoleCookie);
	}
	
	public void setEnableRoleCookie(boolean value) {
		setValue("EnableRoleCookie", value);
	}
	
	public boolean enableAttachments() {
		return getBool("EnableAttachments", defaultEnableAttachments);
	}
	
	public void enableAttachments(boolean value) {
		setValue("EnableAttachments", value);
	}
	
	public long getMaxAttachmentSize() {
		return getLong("MaxAttachmentSize", defaultMaxAttachmentSize);
	}
	
	public void setMaxAttachmentSize(long value) {
		setValue("MaxAttachmentSize", value);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer value) {
		id = value;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isEnableWechatUnauthorizeLogin() {
		return getBool("EnableWechatUnauthorizeLogin", defaultEnableWechatUnauthorizeLogin);
	}

	public void setWechatUnauthorizeLogin(boolean value) {
		setValue("EnableWechatUnauthorizeLogin", value);
	}
}
