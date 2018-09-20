package jdroplet.freemarker.core.tmm;

import java.util.List;

import jdroplet.core.HttpContext;
import jdroplet.core.HttpRequest;
import jdroplet.util.TextUtils;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ShowNotices implements TemplateMethodModel {

	@Override
	public Object exec(List arg0) throws TemplateModelException {
		HttpRequest request = HttpContext.current().request();
		String msg = null;
		String type = null;
		String html = "";
		
		
		msg = (String) request.getAttribute("_bk_message");
		if (!TextUtils.isEmpty(msg)) {
			type = (String) request.getAttribute("_bk_message_type");
			if (TextUtils.isEmpty(type))
				type = "update";
			
			html = "<div id=\"ap_notices\" class=\"ap-template-notice alert alert-" + type + "\">" + msg + "</div>";		
		}
		
		return html;
	}

}
