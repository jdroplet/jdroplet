package jdroplet.app.view.api;

import jdroplet.bll.Posts;
import jdroplet.bll.Users;
import jdroplet.data.model.Post;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;
import jdroplet.util.I18n;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONException;

import java.util.Date;

public class PostPage extends APIPage {

	public static final Integer CHECK_ERROR = 1008000;
	public static final Integer POST_APPROVAL_PENDING = 1008001;

	public void save() {
		Post post = request.getObjectParameter(Post.class);
		User user = Users.getCurrentUser();
		Integer status = 0;

		status = (Integer) PluginFactory.getInstance().applyFilters("Posts@" + post.getType() + "SaveCheck", status, post, user);
		if (status != 0) {
			renderJSON(status, I18n.getString(Integer.toString(status)));
			return;
		}

		if (!user.isAdministrator()) {
			post.setLikes(0);
		}
		post.setUserId(user.getId());
		post.setStatus(Post.APPROVED);
		post.setModified(new Date());
		post = (Post) super.applyFilters( "Posts@" + post.getType() + "SaveBefore", post);
		Posts.save(post);

		post.setValue("userName", Users.getUser(post.getUserId()).getDisplayName());
		post.setValue("avatar", Users.getUser(post.getUserId()).getAvatar());
		post = (Post) super.applyFilters("PostPage@saved", post);

		if (post.getStatus() == Post.APPROVED)
			renderJSON(0, "", post);
		else
			renderJSON(POST_APPROVAL_PENDING, I18n.getString(Integer.toString(POST_APPROVAL_PENDING)));
	}

	public void delete() {
		Integer id = request.getIntParameter("id");

		Posts.delete(id);
		super.renderJSON(0);
	}

	public void reset() {
		String[] fields = request.getParameter("fields").split("-");

		for(String fiel:fields) {
			if ("status".equals(fiel)) {
				Integer[] ids = request.getIntParameterValues("id[]");
				int status = request.getIntParameter("status");

				if (ids == null) {
					Integer id = request.getIntParameter("id");
					Posts.updateStatus(id, status);
				} else {
					for(Integer id:ids) {
						Posts.updateStatus(id, status);
					}
				}
			} else if ("excerpt".equals(fiel)) {
				Integer id = request.getIntParameter("id");
				String excerpt = request.getParameter("excerpt");
				Posts.updateExcerpt(id, excerpt.trim());
			}
		}
		PluginFactory.getInstance().doAction("Post@reset");
		renderJSON(0);
	}
	
	public void get() throws JSONException {
		Integer id = request.getIntParameter("id");
		Post post = null;

		post = Posts.getPost(id, true, true);
		post = (Post) applyFilters("PostPage@" + post.getType() + "Get", post);

		renderJSON(0, "", post);
	}
	
	public void list() {
		Integer userId = request.getIntParameter("userId");
		Integer parentId = request.getIntParameter("parentId");
		Integer status = request.getIntParameter("status");
		Integer cityId = request.getIntParameter("cityId");
		Integer pageIndex = request.getIntParameter("pageIndex", 1);
		Integer pageSize = request.getIntParameter("pageSize", 20);
		Integer itemId = request.getIntParameter("itemId");
		Integer shopId = request.getIntParameter("shopId");
		Integer childrens = request.getIntParameter("childrens");
		SortPostsBy sortBy = SortPostsBy.get(request.getIntParameter("sortBy", 0));
		SortOrder sortOrder = SortOrder.get(request.getIntParameter("sortOrder", 1));
		Integer[] sectionId = request.getIntParameterValues("sectionId[]");
		Integer[] subSectionId = request.getIntParameterValues("subSectionId[]");
		Boolean checkVote = request.getBooleanParameter("checkVote");
		String term = request.getParameter("term");
		Date fromPostDate = request.getDateParameter("fromPostDate");
		Date toPostDate = request.getDateParameter("toPostDate");
		Date modifyFromDate = request.getDateParameter("modifyFromDate");
		Date modifyToDate = request.getDateParameter("modifyToDate");
		String type = request.getParameter("type");
		DataSet<Post> datas = null;

		sectionId = (Integer[]) ArrayUtils.addAll(sectionId, subSectionId);
		datas = Posts.getPosts(shopId, parentId, userId, itemId, sectionId, term,
				type, fromPostDate, toPostDate, modifyFromDate, modifyToDate, sortBy,
				sortOrder, status, cityId, childrens, checkVote, pageIndex, pageSize);
		datas = (DataSet) applyFilters("PostPage@" + type + "List", datas);
		renderJSON(0, "", datas);
	}
}
