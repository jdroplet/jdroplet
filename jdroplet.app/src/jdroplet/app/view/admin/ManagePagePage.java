package jdroplet.app.view.admin;


import java.util.Date;

import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.core.DateTime;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Page;
import jdroplet.data.model.Cluster;
import jdroplet.data.model.UrlPattern;

public class ManagePagePage extends ManageMasterPage {

	public void recv() {
		Integer id = request.getIntParameter("id");
		Integer clusterId = request.getIntParameter("clusterId");
		String name = request.getParameter("name");
		String refer = request.getParameter("refer");
		Page page = new Page();
		
		page.setName(name);
		page.setRefer(refer);
		page.setUserId(Users.getCurrentUser().getId());
		if (id == null || id <= 0) {
			UrlPattern url = null;
			Cluster cluster;
			
			cluster = SiteManager.getCluster(clusterId);
			page.setDateCreated(new Date(DateTime.now().getTime()));
			id = SiteManager.addPage(page);
			
			url = new UrlPattern();
			url.setCluster(cluster);
			url.setPage(page);
			url.setAction("list");
			url.setParams("");
			url.setAllows("");
			url.setDescription("");
			SiteManager.saveUrlPattern(url);
		} else {
			page.setId(id);
			SiteManager.updatePage(page);
		}
		
		super.addNotices("更新成功");
		SiteUrls urls = null;
		urls = SystemConfig.getSiteUrls();
		response.setRedirect(urls.getUrl("page.edit", id));
	}
}
