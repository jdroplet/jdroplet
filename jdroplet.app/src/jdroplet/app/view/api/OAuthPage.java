package jdroplet.app.view.api;

import java.util.HashMap;
import java.util.Map;

import jdroplet.bll.Users;
import jdroplet.data.model.User;
import jdroplet.enums.LoginUserStatus;
import jdroplet.security.FormsAuthentication;

public class OAuthPage extends APIPage {

	public final static String PASSWORD = "password";


	@Override
	public void show() {
	}

	public void authorize() {
		String scope = request.getParameter("scope");
		String grantType = request.getParameter("grant_type");

		if (PASSWORD.equals(grantType)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			Map<String, Object> result = new HashMap<String, Object>();
			
			User userToLogin = new User();
			LoginUserStatus status = null;
			
			userToLogin.setUserName(username);
			userToLogin.setPassword(password);
			
			status = Users.validUser(userToLogin);		
			if (status == LoginUserStatus.Success) {
				String accessToken = FormsAuthentication.getAushToken(username);
				
				result.put("code", 0);
				result.put("access_token", accessToken);
				result.put("token_type", "bearer");
				result.put("refresh_token", "");
				result.put("scope", scope);
				result.put("expires_in", 7200);
				
				FormsAuthentication.setAuthCookie(username, false);
			} else {
				result.put("code", status.getValue());
				result.put("error", status);
			}
		} else {
			String clientId = request.getParameter("client_id");
			String redirectUri = request.getParameter("redirect_uri");
			String responseType = request.getParameter("response_type");
		}
	}
	
	public void access_token() {
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		String grantType = request.getParameter("grant_type");
		String code = request.getParameter("code");
		String redirectUri = request.getParameter("redirect_uri");

		
	}
}
