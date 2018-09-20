<%@ page import="cn.tianya.irock.core.DateTime" %>
<%@ page import="cn.tianya.irock.bll.Users" %>
<%@ page import="cn.tianya.irock.data.model.User" %>
<%@ page import="cn.tianya.framework.util.CookieUtil" %>

<%
DateTime now = null; 
User user = null;
Cookie[] cookies = null;
String str_key = null;
String username = null;
int key = 0;

now = DateTime.now();
cookies = request.getCookies();
str_key = CookieUtil.getCookieValue(cookies, "user", "id");
key = Integer.parseInt(str_key);
username = CookieUtil.getCookieValue(cookies, "user", "w");
user = new User();
user.setUserId(key);
user.setUsername(username);
%>
<div class="folderImages " path="" pathtype="images"><%=user.getUsername() %> Files</div>
<div class="folderOpenSection" style="display: block;">
  <%for (int year=2012; year<=now.getYear(); year++) {%>
  <div class="folderS" path="<%=year %>-1-1" title="<%=year %>" pathtype="images"><%=year %></div>
  <div class="folderOpenSection">
    <%for (int month=1; month<=12; month++) {%>
    <div class="folderClosed " path="<%=year %>-<%=month %>-1" title="<%=month %>" pathtype="images"><%=month %></div>
    <%} %>
  </div>
  <%}%>

</div>
