package jdroplet.core;

import javax.servlet.http.Cookie;
import javax.xml.soap.Text;

import jdroplet.security.FormsAuthentication;
import jdroplet.security.FormsAuthenticationTicket;
import jdroplet.security.FormsIdentity;
import jdroplet.security.GenericIdentity;
import jdroplet.security.GenericPrincipal;
import jdroplet.util.TextUtils;


public class FormsAuthenticationModule {
	static GenericIdentity defaultIdentity = new GenericIdentity ("", "");
	private HttpContext context;
	
	public FormsAuthenticationModule(HttpContext context) {
		this.context = context;
	}
	
	void beginAuthenticateRequest() {
		// 是否验证登录在cookie中？
		Cookie cookie = null;
		FormsAuthenticationTicket ticket = null;
		String auth = null;

		cookie = context.request().getCookie(FormsAuthentication.COOKIE_NAME);

		if (cookie == null) {
			auth = context.request().getHeader("Authorization");
		} else {
			auth = cookie.getValue();
		}

		if (!TextUtils.isEmpty(auth))
			ticket = FormsAuthentication.decrypt (auth);

		if (ticket == null || (ticket.isPersistent() && ticket.isExpired())) {
			context.setUser(new GenericPrincipal (defaultIdentity, new String [0]));
		} else {
			context.setUser(new GenericPrincipal (new FormsIdentity (ticket), new String [0]));
		}
	}
}
