package jdroplet.app.view.admin;

import jdroplet.bll.SiteManager;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Cluster;



/**
 * Created by kuibo on 2017/11/3.
 */
public class ManageClusterPage extends ManageMasterPage {

    public void recv() {
//        Integer id = request.getIntParameter("id");
//        String name = request.getParameter("name");
//        Cluster cluster = null;
//
//        cluster = new Cluster();
//        cluster.setId(id);
//        cluster.setName(name);
//
//        SiteManager.saveCluster(cluster);
//        super.addNotices("更新成功");
//        SiteUrls urls = null;
//        urls = SystemConfig.getSiteUrls();
//        response.setRedirect(urls.getUrl("cluster.edit", id));

        Cluster cluster = null;

        cluster = new Cluster();
        Cluster.DataColumns.fill(request, cluster);
        SiteManager.saveCluster(cluster);

        SiteManager.saveCluster(cluster);
        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("cluster.edit", cluster.getId()));
    }
}
