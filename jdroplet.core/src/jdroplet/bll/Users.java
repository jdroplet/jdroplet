package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.core.*;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IUserDataProvider;
import jdroplet.data.model.User;
import jdroplet.enums.CreateUserStatus;
import jdroplet.enums.LoginUserStatus;
import jdroplet.enums.PasswordFormat;
import jdroplet.exceptions.ApplicationException;
import jdroplet.security.DigestUtil;
import jdroplet.util.BASE64Encoder;
import jdroplet.util.DataSet;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

public class Users {

	public static String GROUP_USER = "USERS_GROUPS";
	
	//redis当中用于生成userId的key
	public static String USER_REDIS_TEMP_USER_ID_KEY = "USER_TEMP_USER_ID_KEY";

	public static String KEY_USERS = "USERS_role-{0}_nickname-{1}_status-{2}_pi-{3}_ps-{4}";
	public static String KEY_USERS_COUNT = "USERS_count";
	public static String KEY_USER = "USER_{0}";
	public static String KEY_USER_LOOKUP = "USERS_user_lookup";
	public static String KEY_USER_PROFILE = "USERS_profile_{0}_{1}";
	public static String KEY_TEMP_USER = "TEMP_USER";

	public static User getCurrentUser() {
		User sys_user = null;
		
		try {sys_user = getUser(getLoggedOnUsername());}catch(ApplicationException ex){};
		if (null == sys_user) {
			return new User();
		}
		return sys_user;
	}

	public static User getUser(String name) {
		return getUser(name, false);
	}

	public static User getUser(String name, boolean flush) {
		HttpContext context = HttpContext.current();
		String cacheKey;
		User user;

		if (TextUtils.isEmpty(name))
			return null;
		cacheKey = MessageFormat.format(KEY_USER, name);

		if (flush) {
			context.removeItem(cacheKey);
			RemoteCache.remove(cacheKey);
		}

		if (context.getItem(cacheKey) != null)
			return (User) context.getItem(cacheKey);

		user = (User) RemoteCache.get(cacheKey);
		if (user == null) {
			IUserDataProvider provider = DataAccess.instance().getUserDataProvider();
			user = provider.getUser(null, name);

			if (user != null) {
				context.setItem(cacheKey, user);
				RemoteCache.add(cacheKey, user);
			}
		}

		return user;
	}

	public static User getUser(Integer id) {
		HttpContext context = HttpContext.current();
		String cacheKey;
		User user;

		if (id == null || id <= 0)
			return null;

		cacheKey = MessageFormat.format(KEY_USER, id);

		if (context != null && context.getItem(cacheKey) != null)
			return (User) context.getItem(cacheKey);

		user = (User) RemoteCache.get(cacheKey);
		if (user == null) {
			IUserDataProvider provider = DataAccess.instance().getUserDataProvider();
			user = provider.getUser(id, null);
		}

		if (user == null)
			return null;

		if (context != null) context.setItem(cacheKey, user);
		RemoteCache.add(cacheKey, user);
		return user;
	}


	public static String getLoggedOnUsername() {
		HttpContext context = HttpContext.current();

		if (context == null	|| context.getUser() == null)
			return null;

		return context.getUsername();
	}

	public static LoginUserStatus validUser(User user) {
		User userLookup = null;
		IUserDataProvider provider = null;
		Integer result = null;
		String encrypted_password = null;

		userLookup = getUser(user.getUserName());
		// 如果未找到用户则返回
		if (userLookup == null)
			return LoginUserStatus.InvalidCredentials;

		// 用户状态失效
//		if (userLookup.isBanned())
//			return LoginUserStatus.AccountBanned;
		
//		// 待批准
//		if (userLookup.getAccountStatus() == UserAccountStatus.ApprovalPending) {
//			return LoginUserStatus.AccountPending;
//		}
//		// 未批准
//		if (userLookup.getAccountStatus() == UserAccountStatus.Disapproved) {
//			return LoginUserStatus.AccountDisapproved;
//		}
		encrypted_password = Users.encrypt(userLookup.getPasswordFormat(), user.getPassword(), userLookup.getSalt());
		
		user.setId(userLookup.getId());
		user.setPasswordFormat(userLookup.getPasswordFormat());
		user.setSalt(userLookup.getSalt());
		user.setPassword(encrypted_password);
		
		provider = DataAccess.instance().getUserDataProvider();
		result = provider.validUser(user);
		return LoginUserStatus.get(result);
	}
	
	public static String encrypt(PasswordFormat format, String cleanString, String salt) {
		String salt_password = cleanString + salt;

		if (format == PasswordFormat.CLEARTEXT) {
			return cleanString;
		} else if (format == PasswordFormat.SHA1HASH) {
			return DigestUtil.SHA1(salt_password);
		} else {
			return DigestUtil.MD5(salt_password);
		}
	}
	
	public static String createSalt() {
		int t = (int)(Math.random() * 10000);
		
		return BASE64Encoder.encode(Integer.toString(t).getBytes());
	}

//	public static void setPassword(User user, String password) {
//		PasswordFormat format = null;
//		String salt = Users.createSalt();
//
//		format = SystemConfig.getPasswordFormat();
//		password = Users.encrypt(format, password, salt) ;
//
//		user.setPasswordFormat(format);
//		user.setPassword(password);
//		user.setSalt(salt);
//
//		save(user);
//	}

	public static Integer createUser(String userName, String displayName, String password, String email, String avatar, Integer inviter) {
		return createUser(userName, displayName, password, email, avatar, inviter, new Integer[]{Integer.valueOf(1)});
	}

	public static Integer createUser(String userName, String displayName, String password, String email, String avatar,
									 Integer inviter,
									 Integer[] roleIds) {
		User user = null;
		PasswordFormat format = null;
		String salt = Users.createSalt();
		
		user = new User();
		user.setDisplayName(displayName);
		user.setUserName(userName);
		user.setEmail(email);
		user.setPassword(password);
		user.setAvatar(avatar);
		user.setStatus(User.APPROVED);
		user.setInviter(inviter);
		
		format = SystemConfig.getPasswordFormat();
		password = Users.encrypt(format, user.getPassword(), salt) ;
		
		user.setPasswordFormat(format);
		user.setPassword(password);
		user.setSalt(salt);
		user.setRegistered(new Date());
		user.setLastvisit(new Date());
		
		return createUser(user, roleIds);
	}

	public static Integer createUser(User user) {
		return createUser(user, new Integer[]{Integer.valueOf(1)});
	}

	public static Integer createUser(User user, Integer[] roleIds) {
		Integer userId = null;
		IUserDataProvider provider = null;
		
		provider = DataAccess.instance().getUserDataProvider();
		if (provider.exists(user.getUserName()))
			throw new ApplicationException("DuplicateUsername");
		
		userId = provider.save(user);
		user.setId(userId);
		Roles.removeRoles(user.getId());
		for(Integer id:roleIds) {
			Roles.addUserToRole(userId, id);
		}
		RemoteCache.clear(GROUP_USER);

		return userId;
	}
			
	public static DataSet<User> getUsers(Integer pageIndex, Integer pageSize) {
		return Users.getUsers(new Integer[]{}, null, pageIndex, pageSize);
	}
	
	public static DataSet<User> getUsers(Integer[] roleIds, Integer pageIndex, Integer pageSize) {
		return Users.getUsers(roleIds, null, null, pageIndex, pageSize, true);
	}
	
	public static DataSet<User> getUsers(Integer[] roleIds, Integer status, Integer pageIndex, Integer pageSize) {
		return Users.getUsers(roleIds, null, status, pageIndex, pageSize, true);
	}

	public static DataSet<User> getUsers(Integer[] roleIds, String searchTerms, Integer status, Integer pageIndex, Integer pageSize,
			boolean cacheable) {
		DataSet<User> datas = null;
		String key = MessageFormat.format(KEY_USERS, StringUtils.join(roleIds, ","), searchTerms, status, pageIndex, pageSize);

		datas = (DataSet<User>) RemoteCache.get(key);
		if (datas == null) {
			IUserDataProvider provider = DataAccess.instance().getUserDataProvider();
			datas = provider.getUsers(roleIds, searchTerms, status, pageIndex, pageSize);

			if (cacheable && datas.getObjects().size() > 0) {
				RemoteCache.add(key, GROUP_USER, datas, ICache.SECOND_FACTOR);
			}
		}
		return datas;
	}

	public static Integer save(User user) {
		IUserDataProvider provider = DataAccess.instance().getUserDataProvider();
		Integer id = null;
		
		if (!TextUtils.isEmpty(user.getPassword())) {
			String salt = user.getSalt();
			String password = null;
			PasswordFormat format = null;
			
			if (TextUtils.isEmpty(salt) == true) 
				salt = Users.createSalt();
			
			format = SystemConfig.getPasswordFormat();
			password = Users.encrypt(format, user.getPassword(), salt) ;
			user.setPasswordFormat(format);
			user.setPassword(password);
			user.setSalt(salt);
		}
		id = provider.save(user);
		user.setId(id);
		Metas.save(user);

		String cache = null;

		user = Users.getUser(id);
		cache = MessageFormat.format(KEY_USER, user.getId());
		RemoteCache.remove(cache);

		cache = MessageFormat.format(KEY_USER, user.getUserName());
		RemoteCache.remove(cache);
		
		return id;
	}

	public static boolean isLoggedIn(User user) {
		return user != null && user.getId() != null && user.getId() > 0;
	}

	public static CreateUserStatus updateDisplayName(Integer id, String displayName) {
		IUserDataProvider provider = DataAccess.instance().getUserDataProvider();

		if (provider.exists(displayName))
			return CreateUserStatus.DuplicateUsername;

		provider.update("id", id, User.DataColumns.displayName, displayName);

		User user = null;
		String cache = null;

		user = Users.getUser(id);
		cache = MessageFormat.format(KEY_USER, user.getId());
		RemoteCache.remove(cache);

		cache = MessageFormat.format(KEY_USER, user.getUserName());
		RemoteCache.remove(cache);

		return CreateUserStatus.Updated;
	}

	public static void updateLastVisit(Integer id) {
		IUserDataProvider provider = DataAccess.instance().getUserDataProvider();

		provider.update("id", id, User.DataColumns.lastvisit, new Date());

		User user = null;
		String cache = null;

		user = Users.getUser(id);
		cache = MessageFormat.format(KEY_USER, user.getId());
		RemoteCache.remove(cache);

		cache = MessageFormat.format(KEY_USER, user.getUserName());
		RemoteCache.remove(cache);
	}

	public static void updateAvatar(Integer id, String avatar) {
		IUserDataProvider provider = DataAccess.instance().getUserDataProvider();

		provider.update("id", id, User.DataColumns.avatar, avatar);

		User user = null;
		String cache = null;

		user = Users.getUser(id);
		cache = MessageFormat.format(KEY_USER, user.getId());
		RemoteCache.remove(cache);

		cache = MessageFormat.format(KEY_USER, user.getUserName());
		RemoteCache.remove(cache);
	}

	public static boolean isNewUser(int userId) {
		return isNewUser(getUser(userId));
	}

	public static boolean isNewUser(User user) {
		String now = DateTime.toString(new Date(), "yyyy-MM-dd");
		String pre = DateTime.toString(user.getRegistered(), "yyyy-MM-dd");

		return now.equals(pre);
	}

	public static Integer exists2(String displayName) {
		IUserDataProvider provider = DataAccess.instance().getUserDataProvider();

		return provider.exists2(displayName);
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	}

	public static User getTempUser() {
		User user = null;
		String name = null;
		HttpRequest req = HttpContext.current().request();
		Cookie cookie = null;

		cookie = req.getCookie(KEY_TEMP_USER);

		if (cookie != null)
			name = cookie.getValue();

		if (TextUtils.isEmpty(name)) {
			name = generateUUID();
			HttpResponse resp = HttpContext.current().response();

			cookie = new Cookie(KEY_TEMP_USER, name);
			cookie.setDomain(req.getSimpleServerName());
			cookie.setPath("/");
			cookie.setMaxAge(999999);

			resp.addCookie(cookie);
		}

		user = new User();
		user.setId(0);
		user.setUserName(name);
		return user;
	}
}
