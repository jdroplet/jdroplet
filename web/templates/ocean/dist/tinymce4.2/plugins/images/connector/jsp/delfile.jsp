<%@ page import="cn.tianya.irock.bll.Attachments" %>
<%@ page import="cn.tianya.irock.data.model.User" %>
<%@ page import="cn.tianya.framework.util.CookieUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.Integer" %>
<%
String[] params = null;
ArrayList<Integer> attids = null;
User user = null;
Cookie[] cookies = null;
String str_key = null;
String username = null;
int key = 0;

params = request.getParameterValues("attid");
attids = new ArrayList<Integer>();
for(String p:params) {
	attids.add(Integer.parseInt(p));
}

cookies = request.getCookies();
str_key = CookieUtil.getCookieValue(cookies, "user", "id");
key = Integer.parseInt(str_key);
username = CookieUtil.getCookieValue(cookies, "user", "w");
user = new User();
user.setUserId(key);
user.setUsername(username);

Attachments.delete(user,attids);
%>