package jdroplet.freemarker.core.tmm;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class Or implements TemplateMethodModel {

	@Override
	public Object exec(List args) throws TemplateModelException {
		if (args.size() != 2) {
			throw new TemplateModelException("Wrong arguments");
		}
		
		int v1 = Integer.parseInt((String)args.get(0));
		int v2 = Integer.parseInt((String)args.get(1));
		
		return v1 | v2;
	}
}
