package jdroplet.freemarker.core.tmm;

import java.text.MessageFormat;
import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class Formater implements TemplateMethodModel {

	public Object exec(List arguments) throws TemplateModelException {
		if (arguments.size() > 1) {
			String fmt = (String)arguments.remove(0);
			Object[] params = new Object[arguments.size()];
			
			arguments.toArray(params);
			return MessageFormat.format(fmt, params);
		} else {
			throw new TemplateModelException("arguments error");
		}
	}

}
