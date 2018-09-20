package jdroplet.data.model;

import jdroplet.core.DateTime;
import jdroplet.core.HttpRequest;
import jdroplet.util.DataSet;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post extends Meta {

	public static class DataColumns {
		public static String table = "jdroplet_posts";

		public static String id = "id";
		public static String parentId = "parent_id";
		public static String userId = "user_id";
		public static String views = "views";

		public static String likes = "likes";
		public static String votes = "votes";
		public static String status = "status";
		public static String itemId = "item_id";

		public static String price = "price";
		public static String title = "title";
		public static String content = "content";
		public static String icon = "icon";

		public static String excerpt = "excerpt";
		public static String tags = "tags";
		public static String url = "url";
		public static String type = "type";

		public static String postDate = "post_date";
		public static String modified = "modified";
		public static String shopId = "shop_id";
		public static String valuables = "valuables";

		public static String cityId = "city_id";
		public static String editor = "editor";
		public static String phone = "phone";
		public static String sectionId = "section_id";
		public static String address = "address";

		public static String[] getColums() {
			return new String[]{ table + ".*"};
		}

		public static void fill(HttpRequest req, Post post) {
			post.id = req.getIntParameter(id);
			post.parentId = req.getIntParameter(parentId);
			post.userId = req.getIntParameter(userId);
			post.views = req.getIntParameter(views);

			post.likes = req.getIntParameter(likes);
			post.votes = req.getIntParameter(votes);
			post.status = req.getIntParameter(status);
			post.itemId = req.getIntParameter(itemId);

			post.price = req.getIntParameter(price);;
			post.title = req.getParameter(title);
			post.content = req.getParameter(content);
			post.icon = req.getParameter(icon);

			post.excerpt = req.getParameter(excerpt);
			post.tags = req.getParameter(tags);
			post.url = req.getParameter(url);
			post.type = req.getParameter(type);

			post.postDate = new Date(req.getDateParameter(postDate, new Date()).getTime());
			post.modified = new Date(req.getDateParameter(modified, new Date()).getTime());
			post.shopId = req.getIntParameter(shopId);
			post.valuables = req.getIntParameter(valuables);

			post.cityId = req.getIntParameter(cityId);
			post.editor = req.getParameter(editor);
			post.phone = req.getParameter(phone);
			post.sectionId = req.getIntParameter(sectionId);
			post.address = req.getParameter(address);
		}

		public static void fill(ResultSet rs, Post post) throws SQLException {
			post.setId(rs.getInt(id));
			post.setParentId(rs.getInt(parentId));
			post.setUserId(rs.getInt(userId));
			post.setViews(rs.getInt(views));

			post.setLikes(rs.getInt(likes));
			post.setVotes(rs.getInt(votes));
			post.setStatus(rs.getInt(status));
			post.setItemId(rs.getInt(itemId));

			post.setPrice(rs.getInt(price));
			post.setTitle(rs.getString(title));
			post.setContent(rs.getString(content));
			post.setIcon(rs.getString(icon));

			post.setExcerpt(rs.getString(excerpt));
			post.setTags(rs.getString(tags));
			post.setUrl(rs.getString(url));
			post.setType(rs.getString(type));

			post.postDate = new Date(rs.getTimestamp(postDate).getTime());
			post.modified = new Date(rs.getTimestamp(modified).getTime());
			post.shopId = rs.getInt(shopId);
			post.valuables = rs.getInt(valuables);

			post.cityId = rs.getInt(cityId);
			post.editor = rs.getString(editor);
			post.phone = rs.getString(phone);
			post.sectionId = rs.getInt(sectionId);
			post.address = rs.getString(address);
		}

		public static Map<String, Object> getKeyValues(Post post) {
			Map<String, Object> map = new HashMap<>();

			map.put(parentId, post.parentId);
			map.put(userId, post.userId);
			map.put(views, post.views);

			map.put(likes, post.likes);
			map.put(votes, post.votes);
			map.put(status, post.status);
			map.put(itemId, post.itemId);

			map.put(price, post.price);
			map.put(title, post.title);
			map.put(content, post.content);
			map.put(icon, post.icon);

			map.put(excerpt, post.excerpt);
			map.put(tags, post.tags);
			map.put(url, post.url);
			map.put(type, post.type);

			map.put(postDate, new Timestamp(post.postDate.getTime()));
			map.put(modified, new Timestamp(post.modified.getTime()));
			map.put(shopId, post.shopId);
			map.put(valuables, post.valuables);

			map.put(cityId, post.cityId);
			map.put(editor, post.editor);
			map.put(phone, post.phone);
			map.put(sectionId, post.sectionId);
			map.put(address, post.address);

			return map;
		}
	}

	// status
	public static final int  APPROVED = 1;
	public static final int  APPROVAL_PENDING = 2;
	public static final int  UNAPPROVED = 4;

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer shopId;
	private Integer parentId;
	private Integer userId;
	private Integer itemId;

	private Integer views;
	private Integer likes;
	private Integer valuables;
	private Integer votes;
	private Integer status;
	private Integer cityId;
	private Integer sectionId;
	private Integer price;

	private String title = "";
	private String content = "";
	private String icon = "";
	private String excerpt = "";
	private String tags = "";
	private String url = "";
	private String type = "";
	private String editor = "";
	private String phone = "";
	private String address = "";

	private Date postDate;
	private Date modified;

	private DataSet<Post> childrens;

	public Post() {
		id = 0;
		postDate = new Date(DateTime.now().getTime());
		modified = postDate;
		icon = "";
		excerpt = "";
		address = "";
		status = Post.APPROVED;
		votes = likes = valuables =  views = 0;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public DataSet<Post> getChildrens() {
		return childrens;
	}

	public void setChildrens(DataSet<Post> childrens) {
		this.childrens = childrens;
	}

	public Integer getValuables() {
		return valuables;
	}

	public void setValuables(Integer valuables) {
		this.valuables = valuables;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
