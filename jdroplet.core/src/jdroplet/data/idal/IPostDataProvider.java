package jdroplet.data.idal;


import java.util.Date;
import java.util.List;

import jdroplet.data.model.Post;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.util.DataSet;
import jdroplet.util.SearchQuery;

public interface IPostDataProvider extends IDataProvider {

	DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId, Integer[] sectionIds, List<SearchQuery> querys,
						   String type, Date postsNewerThan, Date postsOlderThan, Date modifyNewerThan, Date modifyOlderThan,
						   SortPostsBy sortBy, SortOrder sortOrder, Integer status, Integer cityId, Integer pageIndex, Integer pageSize);

}
