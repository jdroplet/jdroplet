package jdroplet.app.view;

import jdroplet.bll.Users;
import jdroplet.core.SystemConfig;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.util.Encoding;
import jdroplet.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ErrorPage extends MasterPage {

	public class TitleLink {
		private String title;
		private String link;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}
	}

	private Throwable throwable;

	@Override
	public void initial() {
		response.setHeader("access-control-allow-credentials", "true");
		response.setHeader("access-control-allow-headers", "Content-Type, Authorization, X-Requested-With");
		response.setHeader("access-control-allow-methods", "PUT,POST,GET,DELETE,OPTIONS");

		if (TextUtils.isEmpty(request.getHeader("Origin"))) {
			response.setHeader("access-control-allow-origin", "*");
		} else {
			response.setHeader("access-control-allow-origin", request.getHeader("Origin"));
		}
		this.setTemplateName("error-show");
	}

	@Override
	public void show() {
		String message = null;
		List<TitleLink> links = new ArrayList<TitleLink>();

		if (throwable == null) {
			message = "No Message";
		} else {
			message = throwable.getMessage();
		}
		addItem("message", message);
		addItem("loggedin_user", Users.getCurrentUser());

		if (throwable instanceof AuthorizationException) {
			TitleLink link = null;
			Integer shopId = request.getIntParameter("shopId");

			link = new TitleLink();
			link.title = "登录";
			if (shopId == null)
				link.link = SystemConfig.getSiteUrls().getUrl("login.show") + "?redirect=" + Encoding.urlEncode(request.getRequestURL().toString());
			else
				link.link = SystemConfig.getSiteUrls().getUrl("login.show", shopId) + "?redirect=" + Encoding.urlEncode(request.getRequestURL().toString());
			links.add(link);

			addItem("status", 2);
		} else {
			addItem("status", 1);
		}
		addItem("links", links);
	}
	
	protected void validaAccess() {}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable tr) {
		this.throwable = tr;
	}
}
