package jdroplet.app.view.admin;

import jdroplet.bll.*;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Post;
import jdroplet.data.model.GoodsSpec;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2017/12/1.
 */
public class ManagePostPage extends ManageMasterPage {

    @Override
    public void initial() {
        super.initial();

        addItem("Skus", new Skus());
    }

    public void recv() {
        Post post = null;
        Integer[] sectionIds = null;

        sectionIds = request.getIntParameterValues("sectionIds");

        post = new Post();
        Post.DataColumns.fill(request, post);

        post.setUserId(Users.getCurrentUser().getId());
        if (sectionIds != null && sectionIds.length == 1)
            post.setSectionId(sectionIds[0]);

        Posts.save(post);

        if (sectionIds != null) {
            if (post.getId() > 0) {
                Post oldPost = null;
                oldPost = Posts.getPost(post.getId());
                Sections.remove(oldPost.getId(), oldPost.getType());
            }
            Sections.add(post.getId(), post.getType(), sectionIds);
        }

        int shop_sku_atts_item_count = request.getIntParameter("shop_sku_atts_item_count", 0);
        for(int i=0; i<shop_sku_atts_item_count; i++) {
            GoodsSpec goodsSpec = new GoodsSpec();


            Skus.save(goodsSpec);
        }
        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("post.edit", post.getId()));
    }

    public void list() {
        Integer parentId  = request.getIntParameter("parentId", null);
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer shopId = request.getIntParameter("shopId", null);
        Integer itemId = request.getIntParameter("itemId", null);
        Boolean showContent = request.getBooleanParameter("showContent");
        Integer[] sectionIds = request.getIntParameterValues("sectionIds");
        String type  = request.getParameter("type", "");
        DataSet<Post> datas = null;

        datas = Posts.getPosts(shopId, parentId, null, itemId, sectionIds, "", type, null,
                null, 0, false, pageIndex, 20);

        addItem("pageIndex", pageIndex);
        addItem("parentId", parentId);
        addItem("shopId", shopId);
        addItem("itemId", itemId);
        addItem("type", type);
        addItem("showContent", showContent);
        addItem("datas", datas);
    }

    public void edit() {
        Integer id = request.getIntParameter("id", 0);
        Integer parentId = null;
        Integer shopId  = null;
        String type = request.getParameter("type", "");
        Post post = null;

        if (id == 0) {
            parentId = request.getIntParameter("parentId", 0);
            shopId = request.getIntParameter("shopId", 0);
            post = new Post();
        } else {
            post = Posts.getPost(id);
            parentId = post.getParentId();
            shopId = post.getShopId();
            type = post.getType();
        }

        addItem("id", id);
        addItem("type", type);
        addItem("post", post);
        addItem("shopId", shopId);
        addItem("parentId", parentId);
    }
}
