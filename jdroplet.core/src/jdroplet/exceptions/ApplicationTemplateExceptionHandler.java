package jdroplet.exceptions;


import java.io.Writer;

import org.apache.log4j.Logger;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ApplicationTemplateExceptionHandler implements TemplateExceptionHandler {
	private static Logger logger = Logger.getLogger(ApplicationTemplateExceptionHandler.class);
	
	public void handleTemplateException(TemplateException te,
			Environment env, Writer out) throws TemplateException {
		logger.error(te); 
	}

}
