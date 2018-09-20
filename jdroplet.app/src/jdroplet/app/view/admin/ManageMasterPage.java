package jdroplet.app.view.admin;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import jdroplet.app.view.MasterPage;
import jdroplet.bll.*;
import jdroplet.core.DateTime;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Shop;
import jdroplet.data.model.User;
import jdroplet.enums.NoticesType;
import jdroplet.freemarker.core.tmm.ShowNotices;
import jdroplet.util.Encoding;
import jdroplet.util.Formatter;

import javax.servlet.http.Cookie;
import java.util.List;

public class ManageMasterPage extends MasterPage {

	protected Integer shopId = 0;

	protected void addNotices(String msg) {
		addNotices(msg, NoticesType.SUCCESS);
	}
	
	protected void addNotices(String msg, NoticesType type) {
		Cookie cookie = null;
   
		cookie = new Cookie("_bk_message", Encoding.urlEncode(msg));
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		response.addCookie(cookie);
		
		cookie = new Cookie("_bk_message_type", type.toString().toLowerCase());
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		response.addCookie(cookie);		
	}
	
	private void cleanNotices() {
		Cookie cookie = null;
		
		cookie = request.getCookie("_bk_message");
		if (cookie != null) {
			request.setAttribute("_bk_message", Encoding.urlDecode(cookie.getValue(), "utf-8"));
			
			cookie.setMaxAge(1);
			cookie.setDomain(request.getServerName());
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		
		cookie = request.getCookie("_bk_message_type");
		if (cookie != null) {
			request.setAttribute("_bk_message_type", cookie.getValue());
			
			cookie.setMaxAge(1);
			cookie.setDomain(request.getServerName());
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	@Override
	public void initial() {
		addItem("Posts", new Posts());
		addItem("Activities", new Activities());
		addItem("Shops", new Shops());
		addItem("Orders", new Orders());
		addItem("Sections", new Sections());
		addItem("Attachments", new Attachments());
		addItem("SiteManager", new SiteManager());
		addItem("Roles", new Roles());
		addItem("Searchs", new Searchs());
		addItem("DateTime", new DateTime());
		addItem("Users", new Users());
		addItem("Formatter", new Formatter());
		addItem("Works", new Works());
		addItem("ShowNotices", new ShowNotices());
		addItem("attaPath", SystemConfig.getAttachmentsStorePath());
		
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel enumModels = wrapper.getEnumModels();
		TemplateHashModel roundingModeEnums;
		TemplateHashModel factory = wrapper.getStaticModels();
		TemplateHashModel staticObj;
		try {			
			roundingModeEnums = (TemplateHashModel) enumModels.get("jdroplet.enums.SortPostsBy");
			addItem("SortPostsBy", roundingModeEnums); 
			
			roundingModeEnums = (TemplateHashModel) enumModels.get("jdroplet.enums.SortOrder");
			addItem("SortOrder", roundingModeEnums); 
			
			roundingModeEnums = (TemplateHashModel) enumModels.get("jdroplet.enums.DataProviderAction");
			addItem("DataProviderAction", roundingModeEnums); 
			
			roundingModeEnums = (TemplateHashModel) enumModels.get("jdroplet.enums.SortUserProfileBy");
			addItem("SortUserProfileBy", roundingModeEnums); 
			
			staticObj = (TemplateHashModel) factory.get("jdroplet.util.IPAddress");
			addItem("IPAddress", staticObj); 
			
			staticObj = (TemplateHashModel) factory.get("jdroplet.enums.ApplicationType");
			addItem("ApplicationType", staticObj); 
		} catch (TemplateModelException e) {
		}
				
		this.cleanNotices();

		User user = Users.getCurrentUser();
		shopId = request.getIntParameter("shopId");
		if (shopId == null || shopId == 0) {
			List<Shop> shops = Shops.getShops(user.getId());
			if (shops.size() > 0)
				shopId = shops.get(0).getId();
		}
	}
	
	public void list() {

	}
	
	public void edit() {

	}
	
	@Override
	public void show() {

	}
}