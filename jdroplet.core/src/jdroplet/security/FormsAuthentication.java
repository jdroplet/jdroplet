package jdroplet.security;

import javax.servlet.http.Cookie;

import com.google.gson.reflect.TypeToken;
import jdroplet.bll.Metas;
import jdroplet.bll.Users;
import jdroplet.core.DateTime;
import jdroplet.core.HttpContext;
import jdroplet.data.model.User;
import jdroplet.exceptions.ApplicationException;
import jdroplet.util.HexUtil;


import java.util.*;


public class FormsAuthentication {

	public static final String COOKIE_NAME = "JAUTH";
	static String cookiePath;
	static boolean initialized = false;
	static int timeout;

	public static void initialize () {
		if (initialized)
			return;
		timeout = 72;
		cookiePath = "/";
		initialized = true;
	}

	public static String encrypt (String token, FormsAuthenticationTicket ticket)	{
		if (ticket == null)
			throw new NullPointerException ("ticket");

		initialize();
		StringBuilder allTicket = new StringBuilder (64);
		allTicket.append (ticket.getVersion());
		allTicket.append ('\u0001');
		allTicket.append (ticket.getName());
		allTicket.append ('\u0001');
		allTicket.append (ticket.getIssueDate().getTime());
		allTicket.append ('\u0001');
		allTicket.append (ticket.getExpiration().getTime());
		allTicket.append ('\u0001');
		allTicket.append (ticket.isPersistent() ? '1' : '0');
		allTicket.append ('\u0001');
		allTicket.append (ticket.getUserData());
		allTicket.append ('\u0001');
		allTicket.append (ticket.getSign());
		allTicket.append ('\u0001');
		allTicket.append (token);

		return HexUtil.getHexString (allTicket.toString());
	}

	public static FormsAuthenticationTicket decrypt (String encryptedTicket) {
		if (encryptedTicket == null || encryptedTicket.length() == 0)
			throw new NullPointerException ("Invalid encrypted ticketencryptedTicket");

		byte [] bytes = null;
		String [] values = null;
		String decrypted = null;
		String sign = null;
		String token = null;
		FormsAuthenticationTicket ticket = null;

		bytes = HexUtil.getBytes (encryptedTicket, encryptedTicket.length());
		try {
			decrypted = new String(bytes, "UTF-8");
		} catch (Exception ex) {
		}
		values = decrypted.split("\u0001");
		if (values.length != 8)
			throw new ApplicationException("invalid auth ticket");
		ticket = new FormsAuthenticationTicket (Integer.parseInt(values[0]),
				values [1],
				new DateTime (Long.parseLong(values[2])).getDate(),
				new DateTime (Long.parseLong(values[3])).getDate(),
				(values[4].equals("1")),
				values [5]);
		sign = values[6];
		token = values[7];

		return ticket.getSign().equals(sign) && verify(token, ticket) ? ticket : null;
	}

	public static void signOut() {
		initialize ();

		Cookie cookie = HttpContext.current().request().getCookie(COOKIE_NAME);
		if (cookie == null)
			return;

		cookie.setValue("");
		cookie.setMaxAge(0);
		cookie.setPath(cookiePath);
		cookie.setDomain(HttpContext.current().request().getSimpleServerName());
		HttpContext.current().response().addCookie(cookie);
		HttpContext.current().setUser(new GenericPrincipal (new GenericIdentity ("", ""), new String [0]));
	}

	public static FormsAuthenticationTicket getAuthTicket(String userName, boolean createPersistentCookie) {
		initialize ();

		Date now = new Date();
		Date expiration;
		if (createPersistentCookie)
			expiration = new DateTime(now).addYears(50).getDate();//DateTime.diff(now.addYears (50), DateTime.now());
		else
			expiration = new DateTime(now).addMinutes(timeout).getDate();//DateTime.diff(now.addMinutes (timeout), DateTime.now());

		FormsAuthenticationTicket ticket = new FormsAuthenticationTicket (1,
				userName,
				now,
				expiration,
				createPersistentCookie,
				"");

		return ticket;
	}

	public static Cookie getAuthCookie(String token, FormsAuthenticationTicket ticket, String strCookiePath) {
		if (strCookiePath == null || strCookiePath.length() == 0)
			strCookiePath = cookiePath;

		Cookie cookie = new Cookie(COOKIE_NAME, encrypt(token, ticket));
		cookie.setPath(strCookiePath);
		if (ticket.isPersistent()) {
			int age = (int) DateTime.diff(ticket.getExpiration(), new Date());
			cookie.setMaxAge(age);
		}
		return cookie;
	}

	public static String setAuthCookie (String userName, boolean createPersistentCookie) {
		initialize ();
		return setAuthCookie (userName, createPersistentCookie, cookiePath);
	}

	public static String getAushToken(String userName) {
		FormsAuthenticationTicket ticket = null;
		Cookie cookie = null;
		HttpContext context = null;
		String token = null;

		ticket = getAuthTicket(userName, false);
		token = getGeneratePassword();
		update(userName, token, ticket);

		return token;
	}

	public static String setAuthCookie (String userName, boolean createPersistentCookie, String strCookiePath) {
		FormsAuthenticationTicket ticket = null;
		Cookie cookie = null;
		HttpContext context = null;
		String token = null;

		ticket = getAuthTicket(userName, createPersistentCookie);
		token = getGeneratePassword();
		update(userName, token, ticket);

		cookie = getAuthCookie(token, ticket, strCookiePath);
		context = HttpContext.current();
		cookie.setDomain(context.request().getSimpleServerName());
		context.setUser(new GenericPrincipal (new FormsIdentity (ticket), new String [0]));
		context.response().addCookie(cookie);

		return cookie.getValue();
	}

	private static void update(String userName, String token, FormsAuthenticationTicket ticket) {
		String verifier = DigestUtil.MD5(token);

		updateTickets(userName, verifier, ticket);
	}

	private static void updateTickets(String userName, String verifie, FormsAuthenticationTicket ticket) {
		Map<String, FormsAuthenticationTicket> tickets = null;
		Integer userId = null;

		userId = Users.getUser(userName).getId();
		tickets = (Map<String, FormsAuthenticationTicket>) Metas.getValue(User.class, userId, "session_tokens",
				new TypeToken<HashMap<String, FormsAuthenticationTicket>>() {}.getType());
		if (tickets == null) {
			tickets = new HashMap<String, FormsAuthenticationTicket>();
		} else {
			Iterator<Map.Entry<String, FormsAuthenticationTicket>> it = tickets.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, FormsAuthenticationTicket> entry = it.next();
				if (entry.getValue().isExpired())
					it.remove();
			}
		}

		if (ticket != null) {
			tickets.put(verifie, ticket);
		} else {
			tickets.remove(verifie);
		}

		Metas.save(User.class, userId, "session_tokens", tickets);
	}

	public static String getGeneratePassword() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	}

	public static boolean verify(String token, FormsAuthenticationTicket ticket) {
		String userName = null;
		String verifier = null;
		Integer userId = null;
		Map<String, FormsAuthenticationTicket> tickets = null;

		userName = ticket.getName();
		userId = Users.getUser(userName).getId();
		tickets = (Map<String, FormsAuthenticationTicket>) Metas.getValue(User.class, userId, "session_tokens",
				new TypeToken<HashMap<String, FormsAuthenticationTicket>>() {}.getType());

		verifier = DigestUtil.MD5(token);

		if (tickets == null)
			return false;

		if (!tickets.containsKey(verifier))
			return false;

		FormsAuthenticationTicket dbTicket = tickets.get(verifier);
		return ticket.getSign().equals(dbTicket.getSign());
	}
}
