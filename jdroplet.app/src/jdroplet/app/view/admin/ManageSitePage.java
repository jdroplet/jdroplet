package jdroplet.app.view.admin;

import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Site;

import java.util.Date;

/**
 * Created by kuibo on 2017/11/6.
 */
public class ManageSitePage extends ManageMasterPage {

    public void recv() {
        Integer clusterId = request.getIntParameter("clusterId");
        Integer id = request.getIntParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String appRoot = request.getParameter("appRoot");
        String keywords = request.getParameter("keywords");
        Site site = null;

        if (id == null || id == 0) {
            site = new Site();
        }  else {
            site = SiteManager.getSite(id);
        }

        site.setClusterId(clusterId);
        site.setAppRoot(appRoot);
        site.setTitle(title);
        site.setDescription(description);
        site.setDateCreated(new Date());
        site.setUserId(Users.getCurrentUser().getId());
        site.setKeywords(keywords);

        if (id == null || id == 0) {
            SiteManager.addSite(site);
        }  else {
            SiteManager.updateSite(site);
        }


        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("site.edit", clusterId, site.getId()));
    }
}
