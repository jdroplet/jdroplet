package jdroplet.bll;

import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.SystemConfig;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ISearchDataProvider;
import jdroplet.data.model.Post;
import jdroplet.data.model.Section;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.enums.SortUsersBy;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;



public class Searchs {
       
    public static DataSet<Post> searchPost(int sectionId, int threadId, String word) {
    	return Searchs.searchPost(sectionId, threadId, word, SortPostsBy.LastPost, SortOrder.DESC, 1, SystemConfig.getDataPageSize(), true);
    }
    
    public static DataSet<Post> searchPost(int sectionId, int threadId, int userId) {
    	SearchQuery query = null;
    	SearchGroup group_root = null;

    	group_root = new SearchGroup();
    	group_root.setTerm(SearchGroup.AND);
			
    	if (sectionId > 0) {
	    	query = new SearchQuery("sectionId", sectionId, SearchQuery.EQ);
	    	group_root.addQuery(query);
		}
    	
		if (threadId > 0) {
	    	query = new SearchQuery("threadId", threadId, SearchQuery.EQ);
	    	group_root.addQuery(query);
		}
		
		if (userId > 0) {
	    	query = new SearchQuery("userId", userId, SearchQuery.EQ);
	    	group_root.addQuery(query);
		}
    	
		return searchPost(group_root, SortPostsBy.LastPost, SortOrder.DESC, 1, SystemConfig.getDataPageSize(), true);
    }
    
    public static DataSet<Post> searchPost(int sectionId, int threadId, String word, int pageIndex, int pageSize, boolean cacheable) {    	
    	return searchPost(sectionId, threadId, word, SortPostsBy.LastPost, SortOrder.DESC, pageIndex, pageSize, cacheable);
    }
    
    public static DataSet<Post> searchPost(int sectionId, int threadId, String word, SortPostsBy sortBy, SortOrder sortOrder, int pageIndex, int pageSize, boolean cacheable) {    	
    	SearchQuery query = null;
    	SearchGroup group_root, group = null;
    	
    	group_root = new SearchGroup();
    	group_root.setTerm(SearchGroup.AND);
    	
    	group = new SearchGroup();
    	group.setTerm(SearchGroup.OR);
    	
    	query = new SearchQuery("subject", word);
    	group.addQuery(query);
				
		query = new SearchQuery("body", word);
		group.addQuery(query);
	
		group_root.addGroup(group);

		group = new SearchGroup();
		group.setTerm(SearchGroup.AND);
			
		if (sectionId > 0) {
	    	query = new SearchQuery("sectionId", sectionId, SearchQuery.EQ);
	    	group_root.addQuery(query);
		}
		
		if (threadId > 0) {
	    	query = new SearchQuery("threadId", threadId, SearchQuery.EQ);
	    	group.addQuery(query);
		}
		
    	group_root.addGroup(group);
    	
		return searchPost(group_root, sortBy, sortOrder, pageIndex, pageSize, cacheable);
    }
    
    @SuppressWarnings("unchecked")
	public static DataSet<Post> searchPost(SearchGroup group, SortPostsBy sortBy, SortOrder sortOrder, int pageIndex, int pageSize, boolean cacheable) {
		String key = null;
		DataSet<Post> datas = null;
		
		if (cacheable) {
			key = "search:posts_" + group.toString() + "_" + sortBy + "_" + sortOrder + "_" + pageIndex + "_" + pageSize;
			datas = (DataSet<Post>)AppCache.get(key);		
		}
		
		if (datas == null) {
			ISearchDataProvider provider = DataAccess.instance().getSearchDataProvider();
			
			datas = provider.searchPost(group, sortBy, sortOrder, pageIndex, pageSize);
			if (cacheable) {
				AppCache.add(key, datas, ICache.SECOND_FACTOR);
			}
		}
		return datas;
	}
    
    public static DataSet<User> searchUser(String term) {
    	return searchUser(term, true);
    }
    
    public static DataSet<User> searchUser(String term, boolean cacheable) {
    	return searchUser(term, SortUsersBy.LastUser, SortOrder.DESC, 1, SystemConfig.getDataPageSize(), true);
    }
    
    public static DataSet<User> searchUser(String term, SortUsersBy sortBy, SortOrder sortOrder, int pageIndex, int pageSize, boolean cacheable) {
    	SearchQuery query = null;
    	SearchGroup group = null;
     	
    	group = new SearchGroup();
    	group.setTerm(SearchGroup.OR);
    	
		query = new SearchQuery("display_name", term);		
		group.addQuery(query);
		
		query = new SearchQuery("phone", term);		
		group.addQuery(query);
				
    	return searchUser(group, sortBy, SortOrder.DESC, pageIndex, pageSize, true);
    }

	protected static DataSet<User> searchUser(SearchGroup group, SortUsersBy sortBy, SortOrder sortOrder, int pageIndex, int pageSize, boolean cacheable) {
    	String key = "search:users_" + sortBy + "_" + sortOrder + "_p:" + pageIndex + "_s:" + pageSize;
		DataSet<User> datas = null;
		
		if (cacheable) {
			key += group.toString();
			datas = (DataSet<User>)AppCache.get(key);
		}
		
		if (datas == null) {
			ISearchDataProvider provider = DataAccess.instance().getSearchDataProvider();
			
			datas = provider.searchUser(group, sortBy, sortOrder, pageIndex, pageSize);
			if (cacheable) {
				AppCache.add(key, datas, ICache.SECOND_FACTOR);
			}
		}
		return datas;
    }

    public static DataSet<Section> searchSection(String term) {
		SearchQuery query = null;
		SearchGroup group = null;

		group = new SearchGroup();
		group.setTerm(SearchGroup.OR);

		query = new SearchQuery("name", term);
		group.addQuery(query);

		return searchSection(group, 1, SystemConfig.getDataPageSize(), true);
	}

	protected static DataSet<Section> searchSection(SearchGroup group, int pageIndex, int pageSize, boolean cacheable) {
		String key = "search:section_p:" + pageIndex + "_s:" + pageSize;
		DataSet<Section> datas = null;

		if (cacheable) {
			key += group.toString();
			datas = (DataSet<Section>)AppCache.get(key);
		}

		if (datas == null) {
			ISearchDataProvider provider = DataAccess.instance().getSearchDataProvider();

			datas = provider.searchSection(group, pageIndex, pageSize);
			if (cacheable) {
				AppCache.add(key, datas, ICache.SECOND_FACTOR);
			}
		}
		return datas;
	}
}
