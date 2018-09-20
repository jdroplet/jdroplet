package jdroplet.data.mysqidal;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IPostDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Post;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PostDataProvider extends DataProvider implements IPostDataProvider {

    protected SQLDatabase newSQLDatabase() {
        SQLDatabase db = new SQLDatabase();

        db.setReadUrl("proxool.jdroplet_read_post");
        db.setWriteUrl("proxool.jdroplet_write_post");
        return db;
    }

    @Override
    public Entity newEntity() {
        return new Post();
    }

    @Override
    public void fill(ResultSet rs, Entity entity) throws SQLException {
        Post.DataColumns.fill(rs, (Post) entity);
    }

    @Override
    public Map<String, Object> getKeyValues(Entity entity) {
        return Post.DataColumns.getKeyValues((Post) entity);
    }

    @Override
    public String getTable() {
        return Post.DataColumns.table;
    }

    @Override
    public String[] getColums() {
        return Post.DataColumns.getColums();
    }

    private String buildSort(SortPostsBy sortBy, SortOrder sortOrder) {
        String sort_str = null;
        String order_str = null;

        order_str = sortOrder.toString();
        if (sortBy.getValue() == SortPostsBy.LastPost.getValue()) {
            sort_str = "post_date " + order_str;
        } else if(sortBy.getValue() == SortPostsBy.PostAuthor.getValue()) {
            sort_str = "useId " + order_str;
        } else if(sortBy.getValue() == SortPostsBy.TotalReplies.getValue()) {
            sort_str = "replies " + order_str;
        } else if(sortBy.getValue() == SortPostsBy.TotalViews.getValue()) {
            sort_str = "views " + order_str;
        } else if(sortBy.getValue() == SortPostsBy.TotalLikes.getValue()) {
            sort_str = "likes " + order_str;
        } else if(sortBy.getValue() == SortPostsBy.TotalLikes.getValue()) {
            sort_str = "votes " + order_str;
        } else if (sortBy.getValue() == SortPostsBy.TotalValuables.getValue() ) {
            sort_str = "valuables " + order_str + ", post_date desc";
        } else if (sortBy.getValue() == SortPostsBy.LastModified.getValue() ) {
            sort_str = "modified " + order_str;
        } else if (sortBy.getValue() == SortPostsBy.Rand.getValue() ) {
            sort_str = "RAND()";
        } else {
            sort_str = "id " + order_str;
        }

        return sort_str;
    }

    @Override
    public void delete(Integer id) {
        super.update("id", id, Post.DataColumns.status, 1);
    }

    @Override
    public DataSet<Post> getPosts(Integer shopId, Integer parentId, Integer userId, Integer itemId, Integer[] sectionIds,
                                  List<SearchQuery> querys, String type, Date postsNewerThan, Date postsOlderThan,
                                  Date modifyNewerThan, Date modifyOlderThan,
                                  SortPostsBy sortBy, SortOrder sortOrder, Integer status, Integer cityId, Integer pageIndex,
                                  Integer pageSize) {
        Post post = null;
        DataSet<Post> datas = new DataSet<Post>();
        ArrayList<Post> posts = new ArrayList<Post>();
        List<String> clauses_list = new ArrayList<String>();
        List<Object> args_list = new ArrayList<Object>();
        SQLDatabase db1 = null;
        SQLDatabase db2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String cls1[] = null;
        String cls2[] = null;
        Object args[] = null;
        String table = null;
        String clause = null;
        String orderBy = null;
        String limit = null;

        table = getTable();
        cls1 = getColums();
        cls2 = new String[] {"COUNT(0)"};

        //  构建筛选
        if (shopId != null) {
            clauses_list.add("shop_id=?");
            args_list.add(shopId);
        }

        if (sectionIds != null && sectionIds.length > 0) {
            if (sectionIds.length == 1 && !type.equals("recommend")) {
                clauses_list.add("section_id=?");
                args_list.add(sectionIds[0]);
            } else {
                table = "jdroplet_posts join jdroplet_section_objects on jdroplet_section_objects.object_id = jdroplet_posts.id";
                clauses_list.add("jdroplet_section_objects.section_id in(?)");
                args_list.add(StringUtils.join(sectionIds, ","));
            }
        }

        if (parentId != null) {
            clauses_list.add("parent_id=?");
            args_list.add(parentId);
        }

        if (userId != null) {
            clauses_list.add("user_id=?");
            args_list.add(userId);
        }

        if (itemId != null) {
            clauses_list.add("item_id=?");
            args_list.add(itemId);
        }

        if (postsNewerThan != null) {
            clauses_list.add("post_date >= ?");
            args_list.add(postsNewerThan);
        }

        if (postsOlderThan != null) {
            clauses_list.add("post_date <=?");
            args_list.add(postsOlderThan);
        }

        if (modifyNewerThan != null) {
            clauses_list.add("modified >= ?");
            args_list.add(modifyNewerThan);
        }

        if (modifyOlderThan != null) {
            clauses_list.add("modified <=?");
            args_list.add(modifyOlderThan);
        }

        if (!TextUtils.isEmpty(type)) {
            clauses_list.add(getTable() + ".type=?");
            args_list.add(type);
        }

        if (status != null) {
            //clauses_list.add("status & ? = status");
            clauses_list.add("status = ?");
            args_list.add(status);
        }

        if (cityId != null && cityId != 0) {
            clauses_list.add("city_id = ?");
            args_list.add(cityId);
        }

        if (querys != null && querys.size() > 0) {
            List<String> subclauses_list = new ArrayList<>();

            for(SearchQuery query:querys) {
                subclauses_list.add(query.toQueryString());
                args_list.add(query.getValue());
            }
            clauses_list.add("(" + StringUtils.join(subclauses_list, " OR ") + ")");
        }
        args = args_list.toArray();
        clause = StringUtils.join(clauses_list, " AND ");

        orderBy = buildSort(sortBy, sortOrder);
        limit = (pageIndex - 1) * pageSize + "," + pageSize;

        try {
            db1 = new SQLDatabase();
            rs1 = db1.query(table, cls1, clause, args, null, null, orderBy, limit);
            while (rs1.next()) {
                post = new Post();
                fill(rs1, post);
                posts.add(post);
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db1.close();
        }

        try {
            db2 = new SQLDatabase();
            rs2 = db2.query(table, cls2, clause, args, null, null, null);
            if (rs2.next()) {
                datas.setTotalRecords(rs2.getInt(1));
            }
        } catch (SQLException ex) {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
            throw new SystemException(ex.getMessage());
        } finally {
            db2.close();
        }
        datas.setObjects(posts);
        return datas;
    }
}
