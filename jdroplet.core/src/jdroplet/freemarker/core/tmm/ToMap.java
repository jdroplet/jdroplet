package jdroplet.freemarker.core.tmm;

import java.util.List;
import java.util.Map;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import jdroplet.util.JSONUtil;
import org.json.JSONObject;

public class ToMap implements TemplateMethodModel {

	@Override
	public Object exec(List args) throws TemplateModelException {
		if (args.size() != 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		
		String v1 = String.valueOf(args.get(0));
		Object jobj = JSONUtil.toObject(v1, Map.class);

		return jobj;
	}

}
