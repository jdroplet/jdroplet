package jdroplet.app.view;

import jdroplet.bll.Posts;
import jdroplet.data.model.Post;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/3/7.
 */
public class PanelPage extends jdroplet.core.Page {

    @Override
    public void initial() {

    }

    public void show() {
        Integer shopId = request.getIntParameter("shopId");
        String name = request.getParameter("name");
        DataSet<Post> datas = Posts.getPosts(shopId, null, null, null, name, "page", 1, 1);
        Post post = null;

        if (datas.getObjects().size() == 0) {
            post = new Post();
            post.setContent(name + " Not Found");
        } else {
            post = datas.getObjects().get(0);
        }
        addItem("post", post);
        addItem("page_title", post.getTitle());
    }
}
