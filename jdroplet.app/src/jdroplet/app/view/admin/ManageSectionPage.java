package jdroplet.app.view.admin;

import jdroplet.bll.Attributes;
import jdroplet.bll.Sections;
import jdroplet.bll.Users;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Section;
import jdroplet.data.model.Attribute;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kuibo on 2017/11/30.
 */
public class ManageSectionPage extends ManageMasterPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer parentId = request.getIntParameter("parentId", 0);
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        SortSectionBy sortBy = SortSectionBy.get(request.getIntParameter("sortBy", 0));
        SortOrder sortOrder = SortOrder.get(request.getIntParameter("sortOrder", 0));
        String type = request.getParameter("type");
        DataSet<Section> datas = null;
        Section parent = null;

        addItem("shopId", shopId);
        addItem("parentId", parentId);
        addItem("pageIndex", pageIndex);
        addItem("type", type);

        datas = Sections.getSections(shopId, null, parentId, type, sortBy, sortOrder, pageIndex, 20, true, true, false);
        addItem("datas", datas);

        parent = Sections.getSection(parentId, true);
        addItem("parent", parent);
    }

    public void atts() {
        Integer sectionId = request.getIntParameter("id");
        List<Attribute> list = Attributes.getAttributes(sectionId, null);
        Section sec = Sections.getSection(sectionId, true);

        addItem("list", list);
        addItem("sectionId", sectionId);
        addItem("section", sec);
    }

    public void att() {
        Integer id = request.getIntParameter("id", 0);
        Attribute att = null;

        if (request.isPostBack()) {
            att = new Attribute();

            Attribute.DataColumns.fill(request, att);
            id = Attributes.save(att);

            super.addNotices("更新成功");
            SiteUrls urls = null;
            urls = SystemConfig.getSiteUrls();
            response.setRedirect(urls.getUrl("section.att", id));
        } else {
            Integer sectionId = 0;
            if (id == 0) {
                sectionId = request.getIntParameter("sectionId");
                att = Attributes.getAttribute(1);
            } else {
                att = Attributes.getAttribute(id);
                sectionId = att.getSectionId();
            }

            addItem("att", att);
            addItem("id", id);
            addItem("sectionId", sectionId);
        }
    }

    public void edit() {
        Integer id = request.getIntParameter("id");
        Integer shopId = request.getIntParameter("shopId");
        Integer parentId = request.getIntParameter("parentId", 0);
        String type = request.getParameter("type");
        Section section = null;
        Section parent = null;

        if (id != 0) {
            section = Sections.getSection(id, true);
            type = section.getType();
            shopId = section.getShopId();
            parentId = section.getParentId();
        } else {
            section = new Section();
        }

        addItem("id", id);
        addItem("shopId", shopId);
        addItem("parentId", parentId);
        addItem("id", id);
        addItem("type", type);
        addItem("section", section);
    }

    public void recv() {
        Integer id = request.getIntParameter("id");
        Integer shopId = request.getIntParameter("shopId");
        Integer parentId = request.getIntParameter("parentId");
        String name = request.getParameter("name");
        String desc = request.getParameter("desc");
        String type = request.getParameter("type");
        String icon = request.getParameter("icon");
        String keywords = request.getParameter("keywords");
        Section section = null;

        section = new Section();
        section.setId(id);
        section.setShopId(shopId);
        section.setParentId(parentId);
        section.setUserId(Users.getCurrentUser().getId());

        section.setName(name);
        section.setDescription(desc);
        section.setType(type);
        section.setIcon(icon);
        section.setKeywords(keywords);

        // meta value
        section.setValue("title_title", request.getParameter("title_title"));

        section.setValue("price_title", request.getParameter("price_title"));
        section.setValue("price_postfix", request.getParameter("price_postfix"));

        section.setValue("content_title", request.getParameter("content_title"));


        Sections.save(section);

        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("section.edit", section.getId()));
    }
}
