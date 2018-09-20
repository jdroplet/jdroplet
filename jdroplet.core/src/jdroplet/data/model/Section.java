package jdroplet.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import jdroplet.bll.Roles;
import jdroplet.core.DateTime;
import jdroplet.core.HttpRequest;
import jdroplet.security.PermissionBase;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;

public class Section extends Meta implements Comparable<Section>, Serializable {

	public static class DataColumns extends DataColumnsBase {
		public static String table = "jdroplet_sections";

		public static String id = "id";
		public static String shopId = "shop_id";
		public static String parentId = "parent_id";
		public static String userId = "user_id";

		public static String count = "count";
		public static String sortOrder = "sort_order";
		public static String type = "type";
		public static String icon = "icon";

		public static String name = "name";
		public static String slug = "slug";
		public static String keywords = "keywords";
		public static String description = "description";
		public static String url = "url";
		public static String dateCreated = "date_created";


		public static String[] getColums() {
			return new String[]{id, shopId, parentId, userId,
					count, sortOrder, type, icon, keywords,
					name, slug, description, url, dateCreated};
		}

		public static void fill(HttpRequest req, Section section) {
			section.id = req.getIntParameter(id);
			section.shopId = req.getIntParameter(shopId);
			section.parentId = req.getIntParameter(parentId);
			section.userId = req.getIntParameter(userId);

			section.count = req.getIntParameter(count);
			section.sortOrder = req.getIntParameter(sortOrder);
			section.type = req.getParameter(type);
			section.icon = req.getParameter(icon);

			section.name = req.getParameter(name);
			section.slug = req.getParameter(slug);
			section.keywords = req.getParameter(keywords);
			section.description = req.getParameter(description);
			section.url = req.getParameter(url);
			section.icon = req.getParameter(icon);
			section.dateCreated = new Date(req.getDateParameter(dateCreated, new Date()).getTime());
		}

		public static void fill(ResultSet rs, Section section) throws SQLException {
			section.id = rs.getInt(id);
			section.shopId = rs.getInt(shopId);
			section.parentId = rs.getInt(parentId);
			section.userId = rs.getInt(userId);

			section.count = rs.getInt(count);
			section.sortOrder = rs.getInt(sortOrder);
			section.type = rs.getString(type);
			section.icon = rs.getString(icon);

			section.name = rs.getString(name);
			section.slug = rs.getString(slug);
			section.keywords = rs.getString(keywords);
			section.description = rs.getString(description);
			section.url = rs.getString(url);
			section.icon = rs.getString(icon);
			section.dateCreated = new Date(rs.getTimestamp(dateCreated).getTime());
		}

		public static Map<String, Object> getKeyValues(Section section) {
			Map<String, Object> map = new HashMap<>();

			map.put(shopId, section.shopId);
			map.put(parentId, section.parentId);
			map.put(userId, section.userId);

			map.put(count, section.count);
			map.put(sortOrder, section.sortOrder);
			map.put(type, section.type);
			map.put(icon, section.icon);

			map.put(name, section.name);
			map.put(slug, section.slug);
			map.put(keywords, section.keywords);
			map.put(description, section.description);
			map.put(url, section.url);
			map.put(icon, section.icon);

			map.put(dateCreated, new Timestamp(section.dateCreated.getTime()));

			return map;
		}
	}

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer shopId;
	private Integer parentId;
	private Integer userId;
	private Integer count;
	private Integer sortOrder;

	private String type;
	private String icon;
	private String name;
	private String slug;
	private String keywords;
	private String description;
	private String url;

	private Date dateCreated;

	private Hashtable<String, PermissionBase> permissions;

	private List<Section> childrens;

	public Section() {
		icon = url = description = type = name = "";

		this.permissions = new Hashtable<String, PermissionBase>();

		dateCreated = DateTime.now().getDate();
	}

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Hashtable<String, PermissionBase> getPermissions() {
		return permissions;
	}

	public void setPermissions(Hashtable<String, PermissionBase> permissions) {
		this.permissions = permissions;
	}

	public void setPermissionSet(Hashtable<String, PermissionBase> value) {
		this.permissions = value;
	}

	public Hashtable<String, PermissionBase> getPermissionSet() {
		return this.permissions;
	}

	public PermissionBase getResolvePermission(User user) {
		PermissionBase pbMaster = getDefaultRolePermission();

		String[] roles = Roles.getUserRoleNames(user.getId());
		PermissionBase pb = null;
		for (String role : roles) {
			pb = getPermissionSet().get(role);
			if (pb != null)
				pbMaster.merge(pb);
		}

		return pbMaster;
	}

	public int compareTo(Section s) {
		if (s == null)
			return 1;

		return this.sortOrder > s.sortOrder ? -1 : 1;
	}

	public void accessCheck(long permission, User user) {
		accessCheck(permission, user, null);
	}

	public void accessCheck(long permission, User user, Post post) {
		SectionPermission.accessCheck(this, permission, user, post);
	}

	public boolean validate(long permission, User user, Post post) {
		return SectionPermission.validate(this, permission, user, post);
	}

	public PermissionBase getDefaultRolePermission() {
		return new SectionPermission();
	}

	public List<Section> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Section> childrens) {
		this.childrens = childrens;
	}
}
