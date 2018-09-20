package jdroplet.app.view.admin;

import jdroplet.bll.SiteManager;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.UrlPattern;
import org.apache.commons.lang.StringUtils;



/**
 * Created by kuibo on 2017/11/2.
 */
public class ManageUrlPage extends ManageMasterPage {

    public void recv() {
        Integer id = request.getIntParameter("id");
        Integer clusterId = request.getIntParameter("clusterId");
        Integer pageId = request.getIntParameter("pageId");
        String action = request.getParameter("action");
        String params = request.getParameter("params");
        String allows[] = request.getParameterValues("allows");
        String description = request.getParameter("description");
        UrlPattern url = null;

        url = new UrlPattern();
        url.setId(id);
        url.setPage(SiteManager.getPage(pageId));
        url.setAction(action);
        url.setCluster(SiteManager.getCluster(clusterId));
        url.setAllows(StringUtils.join(allows, ","));
        url.setParams(params);
        url.setDescription(description);

        SiteManager.saveUrlPattern(url);
        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("url.edit", url.getId()) + "?clusterId=" + clusterId + "&pageId=" + pageId);
    }
}
