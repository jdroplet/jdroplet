package jdroplet.app.view.api;

public class JoinPage extends APIPage {

	@Override
	public void initial() {
	}

	public void show() {
//		Cookie[] cookies = request.getCookies();
//		String username = CookieUtil.getCookieValue(cookies, "user", "w");
//		String key = CookieUtil.getCookieValue(cookies, "temp", "k");
//		int intKey = key.length() == 0 ? 0 : Integer.parseInt(key);
//
//		if (intKey != 0) {
//			User user = new User();
//			user.setUsername(username);
//			
//			CreateUserStatus status = Users.create(user);
//			
//			if (status == CreateUserStatus.Created) {
//				response.setRedirect(config.getAppRoot() + config.getSiteUrls().getRawUrl(request.getSite(), "login.login").rawPath());
//			} else if (status == CreateUserStatus.DuplicateUsername) {
//				throw new ApplicationException(ApplicationExceptionType.UserInvalidCredentials);
//			} else {
//				throw new ApplicationException(ApplicationExceptionType.UnknowError);
//			}
//		} else {
//			throw new ApplicationException(ApplicationExceptionType.UserInvalidCredentials);
//		}
	}
	
	public void recv() {
//		CreateUserStatus creaet_user_status = CreateUserStatus.Deleted;
//		User user = null;
//		int userId = 0;
//		String password = null;
//		String displayName = null;
//		String name = null;
//		String email = null;
//		String[] temp = null;
//		UserAccountStatus user_status = UserAccountStatus.ApprovalPending;
//		List<Integer> roleIds = new ArrayList<Integer>();
//		List<Integer> old_roleIds = new ArrayList<Integer>();
//		List<Role> roles = null;
//
//		userId = request.getIntParameter("member_id");
//		name = request.getParameter("member_name").trim();
//		password = request.getParameter("member_password").trim();
//		displayName = request.getParameter("member_displayName").trim();
//		email = request.getParameter("member_mail").trim();
//		temp = request.getParameter("member_role_ids").split(",");
//		user_status  = UserAccountStatus.get(request.getIntParameter("member_status"));
//
//		user = new User();
//		user.setId(userId);
//		user.setUserName(name);
//		user.setEmail(email);
//		user.setPassword(password);
//		user.setDisplayName(displayName);
//		user.setStatus(user_status);
//
//		for (String rid : temp) {
//			roleIds.add(Integer.parseInt(rid));
//		}
//
//		creaet_user_status = Users.createUser(user);
//		if (creaet_user_status == CreateUserStatus.UserExist) {
//			User sys_user = Users.getCurrentUser();
//			if (user.getId() != sys_user.getId() && !sys_user.isAdministrator()) {
//				renderJSON(-1);
//				return;
//			}
//
//			if (sys_user.isAdministrator()) {
//				roles = Roles.getRoles(userId);
//				for (Role r : roles) {
//					old_roleIds.add(r.getId());
//				}
//
//				Roles.removeRoles(userId, old_roleIds);
//				Roles.addUserToRoles(userId, roleIds);
//			}
//
//			Users.save(user);
//		}
//		renderJSON(creaet_user_status.getValue());
	}
}
