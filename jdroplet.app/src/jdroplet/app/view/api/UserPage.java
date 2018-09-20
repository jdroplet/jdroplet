package jdroplet.app.view.api;

import jdroplet.bll.Metas;
import jdroplet.bll.Roles;
import jdroplet.bll.Searchs;
import jdroplet.bll.Users;
import jdroplet.data.model.User;
import jdroplet.enums.CreateUserStatus;
import jdroplet.util.*;
import org.andy.sensitivewdfilter.UndueWordFilter;

public class UserPage extends APIPage {

	public static final Integer UNDUE_DISPLAYNAME = 1004000;
	public static final Integer DUPLICATE_DISPLAYNAME = 1004001;
	public static final Integer INVALID_EMAIL = 1004002;
	public static final Integer INVALID_PHONE = 1004003;
	public static final Integer INVALID_ID = 1004004;

	public void create() {
		Integer shopId = request.getIntParameter("shopId");
		String[] ids = request.getParameter("roleId").split(",");
		String name = request.getParameter("userName");
		String password = request.getParameter("password");
		Integer[] roleIds = ArrayUtil.convert2Integer(ids);
		Integer userId = request.getIntParameter("id");

		if (userId == null) {
			Users.createUser(name, name, password, "", "", 0, roleIds);
		} else {
			User user = Users.getUser(userId);

			Roles.removeRoles(user.getId());

			user.setPassword(password);
			Users.save(user);
			Roles.addUserToRoles(userId, roleIds);
		}

		renderJSON(0);
	}

	public void search() {
		String term = request.getParameter("term");
		DataSet<User> datas = null;
		StatusData sd = null;
		
		sd = new StatusData();
		datas = Searchs.searchUser(term);
		sd.setData(datas);
		render(sd);
	}

	public void check() {
		String filed = request.getParameter("field");
		User user = Users.getCurrentUser();

		if (User.DataColumns.displayName.equals(filed)) {
			String val = request.getParameter("val");

			Integer existUserId = Users.exists2(val);
			if (existUserId != null && !user.getId().equals(existUserId)) {
				renderJSON(DUPLICATE_DISPLAYNAME, "", user.getDisplayName());
				return;
			}

			UndueWordFilter wf = new UndueWordFilter(1);
			UndueWordFilter.FilterContent fc = wf.doFilter(user.getDisplayName());
			if (fc.getWords().size() != 0) {
				renderJSON(UNDUE_DISPLAYNAME, "", fc.getWords());
				return;
			}
		}
		renderJSON(0);
	}

	public void reset() {
		String[] fields = request.getParameter("fields").split("-");
		StatusData sd = null;
		User user = Users.getCurrentUser();

		sd = new StatusData();
		for(String field:fields) {
			if ("displayname".equals(field)) {
				String displayName = null;
				if (!user.getBool("updated_display_name")) {
					displayName = request.getParameter("displayName");
					CreateUserStatus status = Users.updateDisplayName(user.getId(), displayName);

					if (status == CreateUserStatus.Updated) {
						user.setValue("updated_display_name", true);
						Metas.save(user);

						sd.setStatus(0);
						sd.setMsg("更新成功");
					} else {
						sd.setStatus(status.getValue());
						sd.setMsg("用户名已经被使用");
					}
				} else {
					sd.setStatus(1);
					sd.setMsg("已经更新过");
				}
			} else if ("role".equals(field)) {
				Integer userId = request.getIntParameter("userId", 0);
				Integer[] role = request.getIntParameterValues("role[]");

				if (userId == 0)
					userId = user.getId();
				Roles.removeRoles(userId);
				Roles.addUserToRoles(userId, role);
			} else {
				user.setValue(field, request.getParameter("value"));
				Metas.save(user);
			}
		}
		render(sd);
	}

	/**
	 * 更新当前用户信息
	 */
	public void update() {
		String displayName = request.getParameter("displayName");
		String phone = request.getParameter("phone");
		String idCard = request.getParameter("idCard");
		User user = null;
		StatusData sd = null;
		
		sd = new StatusData();
		if (TextUtils.isEmpty(displayName)) {
			sd.setStatus(1);
			sd.setMsg("");
			
			render(sd);
			return;
		} else if (TextUtils.isEmpty(phone)) {
			sd.setStatus(2);
			sd.setMsg("phone is empty");
			
			render(sd);
			return;
		}
		user = Users.getCurrentUser();
		user.setDisplayName(displayName);
		user.setPhone(phone);
		if (!TextUtils.isEmpty(idCard)) {
			user.setValue("IDCard", idCard);
		}
		user.setValue("updated_display_name", true);

		if (user.getBool("updated_display_name")) {
			User oldUser = Users.getUser(user.getId());

			user.setDisplayName(oldUser.getDisplayName());
		}
		Users.save(user);
		
		render(sd);
	}

	public void list() {
		Integer shopId = request.getIntParameter("shopId", 1);
		Integer pageIndex = request.getIntParameter("pageIndex", 1);
		Integer pageSize = request.getIntParameter("pageSize", 20);
		Integer[] roleIds = request.getIntParameterValues("roleId");
		String terms = request.getParameter("terms");
		DataSet<User> datas = null;

		datas = Users.getUsers(roleIds, terms, null, pageIndex, pageSize, true);
		renderJSON(0, "", datas);
	}

	public void get() {
		User user = null;
		Boolean isAdmin = request.getBooleanParameter("isAdmin");

		if (isAdmin == true && Users.getCurrentUser().isAdministrator()) {
			Integer userId = request.getIntParameter("id", 0);

			if (userId == 0)
				user = Users.getCurrentUser();
			else
				user = Users.getUser(userId);
		} else {
			user = Users.getCurrentUser();
		}

		if (user.getId() != null) {
			user.setValue("isEditors", user.isRole("Editors"));
			user.setPassword("");
			user.setSalt("");
			user.getString("qq", "");
			user.getString("ID", "");
			user.getBool("updated_display_name", false);
			renderJSON(0, "", user);
			return;
		}
		renderJSON(0, "", null);
	}

	public void save(){
		User user = request.getObjectParameter(User.class);
		User oldUser = null;
		UndueWordFilter wf = new UndueWordFilter(1);

		if(!TextUtils.isEmpty(user.getEmail()) && !Regex.isMail(user.getEmail())) {
			renderJSON(INVALID_EMAIL, "无效的邮箱地址", user.getEmail());
			return;
		}

		if(!TextUtils.isEmpty(user.getPhone()) && !Regex.isMobile(user.getPhone())) {
			renderJSON(INVALID_PHONE, "无效的手机号码", user.getPhone());
			return;
		}

		if(!TextUtils.isEmpty(user.getString("ID")) && !Regex.isID(user.getString("ID"))) {
			renderJSON(INVALID_ID, "无效的身份证", user.getString("ID"));
			return;
		}

		Integer existUserId = Users.exists2(user.getDisplayName());
		if (existUserId != null && !user.getId().equals(existUserId)) {
			renderJSON(DUPLICATE_DISPLAYNAME, "", user.getDisplayName());
			return;
		}

		UndueWordFilter.FilterContent fc = wf.doFilter(user.getDisplayName());
		if (fc.getWords().size() != 0) {
			renderJSON(UNDUE_DISPLAYNAME, "", fc.getWords());
			return;
		}

		oldUser = Users.getUser(user.getId());
		user.setPassword("");
		user.setSalt(oldUser.getSalt());
		user.setPasswordFormat(oldUser.getPasswordFormat());
		user.setRegistered(oldUser.getRegistered());
		user.setLastvisit(oldUser.getLastvisit());
		user.setUserName(oldUser.getUserName());
		user.setComeFrom(oldUser.getComeFrom());
		user.setValue("updated_display_name", true);

		Users.save(user);
		if (!user.getBool("isEditors") && !Roles.exists(user.getId(), 5)) {
			Roles.addUserToRole(user.getId(), 5);
		}
		renderJSON(0, "", user);
	}
}
