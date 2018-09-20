package jdroplet.app.view.admin;

import jdroplet.bll.GoodsX;
import jdroplet.bll.Posts;
import jdroplet.bll.Shops;
import jdroplet.bll.Users;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Goods;
import jdroplet.data.model.Post;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by kuibo on 2018/1/20.
 */
public class ManageShopPage extends ManageMasterPage {

     public void recv() {
        Shop shop = null;
        User user = null;

        user = Users.getCurrentUser();

        shop = new Shop();
        Shop.DataColumns.fill(request, shop);
        shop.setUserId(user.getId());
        Shops.save(shop);

        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("shop.edit", shop.getId()));
    }

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        String terms = request.getParameter("terms", "");
        User user = Users.getCurrentUser();
        DataSet<Shop> datas = Shops.getShops(terms, user.getId(), pageIndex, 20);

        addItem("pageIndex", pageIndex);
        addItem("datas", datas);
    }

    private List<Map> toSimpleMap(List<Post> posts) {
        List<Map> list = null;

        list = new ArrayList<>();
        if (posts.size() == 0) {
            list.add(new HashMap());
            list.add(new HashMap());
            list.add(new HashMap());
        } else {
            for(Post post:posts) {
                Map map = new HashMap();

                map.put("id", post.getId());
                map.put("title", post.getTitle());
                map.put("excerpt", post.getExcerpt());
                map.put("itemId", post.getItemId());
                map.put("url", post.getUrl());
                map.put("icon", post.getIcon());
                map.put("price", post.getPrice());
                list.add(map);
            }
        }
        return list;
    }

    public void main() {
        Integer id = request.getIntParameter("id");
        Shop shop = null;

        shop = Shops.getShop(id);
        addItem("shop", shop);

        if (!request.isPostBack()) {
            DataSet<Post> datas = Posts.getPosts(id, null, null, null, null, "shop_main_carousel", SortPostsBy.LastPost, SortOrder.ASC, 1, 3);
            List<Map> shopMainCarousel = toSimpleMap(datas.getObjects());

            addItem("shopMainCarousel", JSONUtil.toJSONString(shopMainCarousel));

            List<Map> shopMainRecommend = null;
            datas = Posts.getPosts(id, null, null, null, null, "shop_main_recommend", SortPostsBy.LastPost, SortOrder.ASC, 1, 3);
            shopMainRecommend = toSimpleMap(datas.getObjects());
            addItem("shopMainRecommend", JSONUtil.toJSONString(shopMainRecommend));
        } else {
            User user = Users.getCurrentUser();
            // 保存跑马灯
            JSONArray jarr = request.getJSONArrayParameter("shopMainCarousel");
            JSONObject jobj = null;

            for (int i = 0; i < jarr.length(); i++) {
                jobj = jarr.optJSONObject(i);

                Post post = new Post();
                post.setId(jobj.optInt("id"));
                post.setShopId(id);
                post.setItemId(jobj.optInt("itemId"));
                post.setUserId(user.getId());
                post.setType("shop_main_carousel");
                post.setUrl(jobj.optString("url"));
                post.setIcon(jobj.optString("icon"));
                post.setPostDate(new Date());
                post.setModified(new Date());
                post.setStatus(Post.APPROVED);
                Posts.save(post);
            }

            jarr = request.getJSONArrayParameter("shopMainRecommend");
            for (int i = 0; i < jarr.length(); i++) {
                jobj = jarr.optJSONObject(i);

                Post post = new Post();
                post.setId(jobj.optInt("id"));
                post.setShopId(id);
                post.setItemId(jobj.optInt("itemId"));
                post.setUserId(user.getId());
                post.setPrice(jobj.optInt("price"));
                post.setType("shop_main_recommend");
                post.setUrl(jobj.optString("url"));
                post.setIcon(jobj.optString("icon"));
                post.setTitle(jobj.optString("title"));
                post.setExcerpt(jobj.optString("excerpt"));
                post.setPostDate(new Date());
                post.setModified(new Date());
                post.setStatus(Post.APPROVED);
                Posts.save(post);
            }

            super.addNotices("更新成功");
            SiteUrls urls = null;
            urls = SystemConfig.getSiteUrls();
            response.setRedirect(urls.getUrl("shop.main", id));
        }
    }
}
