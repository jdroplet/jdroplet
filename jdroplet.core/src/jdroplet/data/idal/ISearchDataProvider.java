package jdroplet.data.idal;

import jdroplet.data.model.Post;
import jdroplet.data.model.Section;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.enums.SortUsersBy;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;

public interface ISearchDataProvider {

    DataSet<Post> searchPost(SearchGroup group, SortPostsBy sortBy,
                             SortOrder sortOrder, int pageIndex, int pageSize);

    DataSet<User> searchUser(SearchGroup group, SortUsersBy sortBy,
                             SortOrder sortOrder, int pageIndex, int pageSize);

    DataSet<Section> searchSection(SearchGroup group, int pageIndex, int pageSize);
}
