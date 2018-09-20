package jdroplet.app.view.api;

import jdroplet.bll.Sections;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.data.model.Section;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortPostsBy;
import jdroplet.enums.SortSectionBy;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;
import jdroplet.util.SearchQuery;
import jdroplet.util.StatusData;


import java.util.Date;
import java.util.List;
import java.util.Map;

public class SectionPage extends APIPage {
	
	public void save() {
		Section section = request.getObjectParameter(Section.class);
		Boolean isAdmin = request.getBooleanParameter("isAdmin");
		//Sections.save(section);
		renderJSON(0, ""+isAdmin, section);
	}
	
	public void get() {
		Integer id = request.getIntParameter("id");
		Section section = null;
		StatusData sd = new StatusData();

		section = Sections.getSection(id, true, true);
		sd.setData(section);
		render(sd);
	}

	public void delete() {
	}
	
	public void list() {
		Integer pageIndex = request.getIntParameter("pageIndex");
		Integer pageSize = request.getIntParameter("pageSize");
		Integer shopId = request.getIntParameter("shopId");
		Integer parentId = request.getIntParameter("parentId");
		Integer objectId = request.getIntParameter("objectId");
		String type = request.getParameter("type");
		String objectType = request.getParameter("objectType");
		SortSectionBy sortBy = SortSectionBy.get(request.getIntParameter("sortBy", 0));
		SortOrder sortOrder = SortOrder.get(request.getIntParameter("sortOrder", 0));
		DataSet<Section> datas = null;
		List<Section> sections = null;
		StatusData sd = new StatusData();
		
		datas = Sections.getSections(shopId, null, parentId, type, sortBy, sortOrder, pageIndex, pageSize, true, true, false);
		if (datas.getObjects().size() != 0) {
			for(Section sec:datas.getObjects()) {
				sections = Sections.getSections(objectId, objectType);
				if (sections != null) for(Section sec2:sections) {
					if (sec.getId().equals(sec2.getId()))
						sec.setValue("objectId", objectId);
				}
			}
		}
		datas = (DataSet) PluginFactory.getInstance().applyFilters("SectionPage@list", datas);
		sd.setData(datas);
		render(sd);
	}
}
