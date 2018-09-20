<%@ page import="cn.tianya.irock.bll.Attachments" %>
<%@ page import="cn.tianya.irock.bll.SiteManager" %>
<%@ page import="cn.tianya.irock.bll.Users" %>
<%@ page import="cn.tianya.irock.data.model.Site" %>
<%@ page import="cn.tianya.irock.data.model.Attachment" %>
<%@ page import="cn.tianya.irock.data.model.User" %>
<%@ page import="cn.tianya.irock.core.DateTime" %>
<%@ page import="cn.tianya.framework.util.CookieUtil" %>
<%@ page import="cn.tianya.irock.core.Configuration" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.Integer" %>
<%
String path = null;
String str_key = null;
String username = null;
int siteId = 0;
int key = 0;
Site site = null;
Cookie[] cookies = null;
Configuration cfg = null;
User user = null;
List<Attachment> attas = null;
DateTime time = null;

cfg = Configuration.config();
path = request.getParameter("path");
if (!"".equals(path)) {
	time = DateTime.parse(path, "yyyy-MM-dd");
} else {
	time = DateTime.now();
}
siteId = Integer.parseInt(request.getParameter("siteId"));
site = SiteManager.getSite(siteId);

cookies = request.getCookies();
str_key = CookieUtil.getCookieValue(cookies, "user", "id");
key = Integer.parseInt(str_key);
username = CookieUtil.getCookieValue(cookies, "user", "w");
user = new User();
user.setUserId(key);
user.setUsername(username);

attas = Attachments.getAttachments(user, site, time);
for (Attachment atta : attas) {
%>
<table class="imageBlock0" cellpadding="0" cellspacing="0" filename="<%=atta.getFilename()%>" fname="<%=atta.getFilename()%>" type="images" ext="<%=atta.getExtension()%>" path="*" linkto="<%=cfg.getAppRoot() + atta.getFullPathname()%>" fsize="<%=atta.getSize()/1024%>" fsizetext="<%=atta.getSize()/1024%>kb" date="23.08.2011 18:16" fwidth="-" fheight="-" md5="bb545c1719f6a0028d66ac89483f075d" attid="<%=atta.getAttachmentId()%>">
  <tr>
    <td valign="bottom" align="center">
      <div class="imageBlock1">
        <div class="imageImage">
          <img src="<%=cfg.getAppRoot() + atta.getFullPathname()%>" width="100" height="100" alt="${atta.filename}" />
        </div>
        <div class="imageName"><%=atta.getFilename()%></div>
      </div>
    </td>
  </tr>
</table>
<%}%>