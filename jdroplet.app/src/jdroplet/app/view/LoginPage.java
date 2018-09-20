package jdroplet.app.view;

import jdroplet.bll.Roles;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.User;
import jdroplet.enums.LoginUserStatus;
import jdroplet.security.FormsAuthentication;

public class LoginPage extends MasterPage {

	public void show() {
		Integer shopId = request.getIntParameter("shopId");
		Boolean signUp = request.getBooleanParameter("signUp");
		if (!signUp && Users.isLoggedIn(Users.getCurrentUser())) {
			if (shopId == null)
				response.setRedirect(SystemConfig.getSiteUrls().getUrl("user.my"));
			else
				response.setRedirect(SystemConfig.getSiteUrls().getUrl("user.my", shopId));
			return;
		}

		String redirect = request.getParameter("redirect", "");
		addItem("redirect", redirect);
		addItem("shopId", shopId);
	}

	public void recv() {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User userToLogin = new User();
		LoginUserStatus status = null;
		String[] msg = new String[] {"", "密码错误"};
		userToLogin.setUserName(username);
		userToLogin.setPassword(password);
		
		status = Users.validUser(userToLogin);		
		if (status == LoginUserStatus.Success) {
			FormsAuthentication.setAuthCookie(username, false);
		}
		
		renderJSON(status.getValue(), msg[status.getValue()]);
	}

	public void logout() {
		FormsAuthentication.signOut();

		response.setRedirect("/");
	}

	public void signup() {
		User user = request.getObjectParameter(User.class);
		Users.save(user);

		Integer roleId = user.getInt("roleId");
		if (!Roles.exists(user.getId(), roleId))
			Roles.addUserToRoles(user.getId(), new Integer[] {roleId});

		renderJSON(0);
	}
}
