package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.DateTime;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IPostDataProvider;
import jdroplet.data.model.Post;
import jdroplet.data.model.Section;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;


public class Posts {

	public static class PostTypeTemplate {
		private String title;
		private String icon;

		public PostTypeTemplate() {

		}

		public PostTypeTemplate(String title, String icon) {
			this.title = title;
			this.icon = icon;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}
	}

	public static final String GROUP_KEY_POSTS = "POSTS_GROUP_s-";

    public static final String KEY_POSTS = "POSTS_sid-{0}-pid-{1}-uid-{2}-itid-{3}-term-{4}-type-{5}-d1-{6}-{7}-s-{8}-o-{9}-s-{10}-pi-{11}-ps-{12}-s{13}_f{14}";

    private static Map<String, PostTypeTemplate> postTypeTemplates = null;

    static {
		postTypeTemplates = new HashMap() {{
			put("page", new PostTypeTemplate("页面", "page"));
			}};
	}

	public static void registerPostType(String type, PostTypeTemplate template) {
		postTypeTemplates.put(type, template);
	}

	public static Map<String, PostTypeTemplate> getPostTypeTemplates() {
    	return postTypeTemplates;
	}

	public static PostTypeTemplate getPostTypeTemplate(String type) {
    	if (TextUtils.isEmpty(type))
    		return null;
    	return postTypeTemplates.get(type);
	}

    public static Integer save(Post post) {
    	IPostDataProvider provider = DataAccess.instance().getPostDataProvider();
    	Integer id = null;

    	id = provider.save(post);
    	if (post.getId() == null || post.getId() == 0)
			post.setId(id);
    	Metas.save(post);

		String groupKey = GROUP_KEY_POSTS + post.getItemId() + "-" + post.getType();
		RemoteCache.clear(groupKey);

		groupKey = GROUP_KEY_POSTS + "null-" + post.getType();
		RemoteCache.clear(groupKey);

		Logs.save("save-post-" + post.getId(), JSONUtil.toJSONString(post));
    	return id;
    }

    public static void delete(Integer id) {
    	Post post = Posts.getPost(id);
		IPostDataProvider provider = DataAccess.instance().getPostDataProvider();

		provider.delete(id);

		String groupKey = GROUP_KEY_POSTS + post.getItemId() + "-" + post.getType();
		RemoteCache.clear(groupKey);

		groupKey = GROUP_KEY_POSTS + "null-" + post.getType();
		RemoteCache.clear(groupKey);
		Logs.save("delete-" + id, "Integer:" + id);
	}

	public static void remove(Integer id) {
		Post post = Posts.getPost(id);
		IPostDataProvider provider = DataAccess.instance().getPostDataProvider();

		provider.remove(id);
		String groupKey = GROUP_KEY_POSTS + post.getItemId() + "-" + post.getType();
		RemoteCache.clear(groupKey);

		groupKey = GROUP_KEY_POSTS + "null-" + post.getType();
		RemoteCache.clear(groupKey);
		Logs.save("delete-" + id, "Integer:" + id);
	}

	private static void update(String field, Integer id, Object value) {
		IPostDataProvider provider = DataAccess.instance().getPostDataProvider();

		provider.update("id", id, field, value);
		provider.update("id", id, "modified", new Date());

		Logs.save("update-post-" + id, "id:" + id + " " + field + ":" + value);

		Post post = Posts.getPost(id);
		String groupKey = GROUP_KEY_POSTS + post.getItemId() + "-" + post.getType();
		RemoteCache.clear(groupKey);

		groupKey = GROUP_KEY_POSTS + "null-" + post.getType();
		RemoteCache.clear(groupKey);
	}

	public static void updateExcerpt(Integer id, String excerpt) {
		update("excerpt", id, excerpt);
	}

	public static void updateStatus(Integer id, Integer status) {
		update("status", id, status);
	}

	public static void updateIcon(Integer id, String icon) {
		update("icon", id, icon);
	}

	public static void updateModified(Integer id, Date modified) {
		update("modified", id, new Timestamp(modified.getTime()));
	}

	public static Post getPost(Integer id) {
    	return getPost(id, true, false);
	}

    public static Post getPost(Integer id, boolean cacheable, boolean flush) {
    	IPostDataProvider provider = DataAccess.instance().getPostDataProvider();
    	
    	return (Post) provider.getEntity(id);
    }

	public static Post getItemPost(Integer itemId, String type) {
    	return getItemPost(itemId, type, false);
	}

    public static Post getItemPost(Integer itemId, String type, Boolean ignoreStatus) {
		DataSet<Post> datas = null;

		if (ignoreStatus == true)
			datas = getPosts(1, null, itemId, type, SortPostsBy.LastModified, SortOrder.ASC, null, 1, 1);
		else
			datas = getPosts(1, null, itemId, type, SortPostsBy.LastModified, SortOrder.ASC, Post.APPROVED, 1, 1);

		if (datas.getObjects().size() > 0)
			return datas.getObjects().get(0);

		return null;
	}


	public static List<Post> getItemPosts(Integer itemId, String type, Boolean ignoreStatus) {
		DataSet<Post> datas = null;

		if (ignoreStatus == true)
			datas = getPosts(1, null, itemId, type, SortPostsBy.LastModified, SortOrder.ASC, null, 1, 1);
		else
			datas = getPosts(1, null, itemId, type, SortPostsBy.LastModified, SortOrder.ASC, Post.APPROVED, 1, 1);

		if (datas.getObjects().size() > 0)
			return datas.getObjects();

		return null;
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer[] sectionIds, String type,
										 SortPostsBy sortBy, SortOrder sortOrder,
										 Integer pageIndex, Integer pageSize) {
		return getPosts(shopId, null, null, null, sectionIds, null, type, sortBy, sortOrder,
				Post.APPROVED, null, 0, false, pageIndex, pageSize);
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer[] sectionIds, Integer itemId, String type,
										 SortPostsBy sortBy, SortOrder sortOrder, Integer status,
										 Integer pageIndex, Integer pageSize) {
		return getPosts(shopId, null, null, itemId, sectionIds, null, type, sortBy, sortOrder,
				status, null, 0, false, pageIndex, pageSize);
	}

    public static DataSet<Post> getPosts(Integer shopId, Integer[] sectionIds, String type,
										 SortPostsBy sortBy, SortOrder sortOrder,
										 Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
		return getPosts(shopId, null, null, null, sectionIds, null, type, sortBy, sortOrder,
				Post.APPROVED, null, 0, false, pageIndex, pageSize, cacheable, flush);
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										 String equalTerm, String type, Integer pageIndex, Integer pageSize) {
		List<SearchQuery> querys = null;

		if (!TextUtils.isEmpty(equalTerm)) {
			querys = new ArrayList<>();
			querys.add(new SearchQuery(Post.DataColumns.title, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.content, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.excerpt, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.tags, equalTerm, SearchQuery.EQ));
		}

		return getPosts(shopId, parentId, userId, itemId, null, querys, type, null,
				null, null, null, SortPostsBy.LastPost,
				SortOrder.DESC, Post.APPROVED, null, null, null, pageIndex, pageSize, true, false);
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										 String equalTerm, String type, SortPostsBy sortBy, SortOrder sortOrder,
										 Integer pageIndex, Integer pageSize) {
		List<SearchQuery> querys = null;

		if (!TextUtils.isEmpty(equalTerm)) {
			querys = new ArrayList<>();
			querys.add(new SearchQuery(Post.DataColumns.title, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.content, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.excerpt, equalTerm, SearchQuery.EQ));
			querys.add(new SearchQuery(Post.DataColumns.tags, equalTerm, SearchQuery.EQ));
		}

		return getPosts(shopId, parentId, userId, itemId, null, querys, type, null,
				null, null, null, sortBy,
				sortOrder, Post.APPROVED, null, null, null, pageIndex, pageSize, true, false);
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										 Integer[] sectionIds, String searchTerm, String type,
										 Integer status, Integer cityId, Integer childrens, Boolean checkVote,
										 Integer pageIndex, Integer pageSize) {
		return getPosts(shopId, parentId, userId, itemId, sectionIds, searchTerm, type,
				SortPostsBy.LastPost, SortOrder.DESC, status, cityId, childrens, checkVote, pageIndex, pageSize);
	}

	public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										 Integer[] sectionIds, String searchTerm, String type,
										 SortPostsBy sortBy, SortOrder sortOrder,
										 Integer status, Integer cityId, Integer childrens, Boolean checkVote,
										 Integer pageIndex, Integer pageSize) {

		return getPosts(shopId, parentId, userId, itemId,
				sectionIds, searchTerm, type, sortBy, sortOrder,
				status, cityId, childrens, checkVote,
				pageIndex, pageSize, true, false);
	}

    public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										 Integer[] sectionIds, String searchTerm, String type,
										 SortPostsBy sortBy, SortOrder sortOrder,
										 Integer status, Integer cityId, Integer childrens, Boolean checkVote,
										 Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
		List<SearchQuery> querys = null;

		if (!TextUtils.isEmpty(searchTerm)) {
			querys = new ArrayList<>();
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.userId, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.itemId, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.title, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.content, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.excerpt, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.tags, searchTerm, SearchQuery.LIKE));
		}

    	return getPosts(shopId, parentId, userId, itemId, sectionIds, querys, type, null,
				null, null, null, sortBy,
				sortOrder, status, cityId, childrens, checkVote, pageIndex, pageSize, cacheable, flush);
    }

	public static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										  Integer[] sectionIds, String searchTerm, String type,
										  Date postsNewerThan, Date postsOlderThan,
										  Date modifyNewerThan, Date modifyOlderThan,
										  SortPostsBy sortBy, SortOrder sortOrder,
										  Integer status, Integer cityId, Integer childrens, Boolean checkVote,
										  Integer pageIndex, Integer pageSize) {
		List<SearchQuery> querys = null;

		if (!TextUtils.isEmpty(searchTerm)) {
			querys = new ArrayList<>();
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.userId, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.itemId, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.title, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.content, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.excerpt, searchTerm, SearchQuery.LIKE));
			querys.add(new SearchQuery(Post.DataColumns.table + "." + Post.DataColumns.tags, searchTerm, SearchQuery.LIKE));
		}

		return getPosts(shopId, parentId, userId, itemId, sectionIds, querys, type, postsNewerThan,
				postsOlderThan, modifyNewerThan, modifyOlderThan, sortBy,
				sortOrder, status, cityId, childrens, checkVote, pageIndex, pageSize, true, false);
	}
    
	private static DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId,
										  Integer[] sectionIds, List<SearchQuery> querys, String type,
										  Date postsNewerThan, Date postsOlderThan,
										  Date modifyNewerThan, Date modifyOlderThan,
										  SortPostsBy sortBy, SortOrder sortOrder,
										  Integer status, Integer cityId, Integer childrens, Boolean checkVote,
										  Integer pageIndex, Integer pageSize, boolean cacheable, boolean flush) {
		DataSet<Post> datas = null;
       	String group = null;
        String key = null;
        String date_start_key = null;
        String date_end_key = null;
        String queryKey = null;

        date_start_key = postsNewerThan == null ? "" : DateTime.toString(postsNewerThan, "yyyy-MM-dd");
        date_end_key = postsOlderThan == null ? "" : DateTime.toString(postsOlderThan, "yyyy-MM-dd");

		queryKey = "";
		if (querys != null) {
			for(SearchQuery query:querys) {
				queryKey += query.toString();
			}
		}
		key = MessageFormat.format(KEY_POSTS, StringUtils.join(sectionIds, "-"), shopId, parentId, userId, itemId, queryKey,
				type, date_start_key, date_end_key, sortBy, sortOrder, status, cityId, pageIndex, pageSize);
        if (flush || !cacheable) 
        	RemoteCache.remove(key);
        else
        	datas = (DataSet<Post>)RemoteCache.get(key);
        
        if (datas == null) {
        	IPostDataProvider provider = DataAccess.instance().getPostDataProvider();
        	datas = provider.getPosts(shopId, parentId, userId, itemId, sectionIds, querys, type, postsNewerThan, postsOlderThan,
        			modifyNewerThan, modifyOlderThan, sortBy, sortOrder, status, cityId, pageIndex, pageSize);

			for(Post post:datas.getObjects()) {
				post.setValue("userName", PluginFactory.getInstance().applyFilters("Posts@getPostUserName", "", post));
				post.setValue("avatar", PluginFactory.getInstance().applyFilters("Posts@getPostUserAvatar", "", post));

				if (childrens != null) {
					DataSet<Post> cDatas = Posts.getPosts(shopId, post.getId(), null, itemId, null, null, type, null, null,
							null, null, SortPostsBy.LastPost, SortOrder.ASC, status, cityId, 0, false, pageIndex, childrens,
							true, false);
					for(Post post2:cDatas.getObjects()) {
						if (checkVote) {
							User user = Users.getCurrentUser();
							post2.setValue("voted", Votes.isVoted(user.getId(), post2.getId(), post2.getType()));
						}
					}
					post.setChildrens(cDatas);
				}

				if (checkVote != null && checkVote == true) {
					User user = Users.getCurrentUser();
					post.setValue("voted", Votes.isVoted(user.getId(), post.getId(), post.getType()));
				}
			}

			if (datas.getObjects().size() > 0) {
				String groupKey = GROUP_KEY_POSTS + itemId + "-" + type;
				RemoteCache.add(key, groupKey, datas, ICache.HOUR_FACTOR);
			}

        }
        return datas;
	}
}
