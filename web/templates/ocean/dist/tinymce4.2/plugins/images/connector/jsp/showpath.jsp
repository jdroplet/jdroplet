<%
String path = request.getParameter("path");
String year = null;
String month = null;
if ("".equals(path)) {
	year = "2012";
} else {
	String[] info = path.split("/");
	year = info[0];
	if (info.length == 2) {
		month = path.split("/")[1];
	} 
}
%>
<div class="addrItem" path="<%=year%>" pathtype="images" title=""><img src="img/folder_open_image.png" width="16" height="16" alt="¸ùÄ¿Â¼" /></div>
<div class="addrItem" path="<%=year%>" pathtype="images" title=""><div><%=year%></div></div>
<%if (month != null) {%>
<div class="addrItemEnd" path="<%=year%>/<%=month%>" pathtype="images" title=""><div><%=month%></div></div>
<%}%>
