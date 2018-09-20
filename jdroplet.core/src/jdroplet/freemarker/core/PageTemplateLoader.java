package jdroplet.freemarker.core;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.MessageFormat;

import jdroplet.bll.SiteManager;
import jdroplet.cache.AppCache;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Site;
import freemarker.cache.TemplateLoader;
import jdroplet.util.FileUtil;
import org.apache.commons.lang.StringUtils;

public class PageTemplateLoader implements TemplateLoader {
	
	public void closeTemplateSource(Object templateSource) throws IOException {
	}

	public Object findTemplateSource(String name) throws IOException {
		Site site = SiteManager.getCurrentSite();
		String key = "FM_TEMPLATE_" + site.getTheme() + "-" + name;
		String content = (String) AppCache.get(key);

		if (content == null) {
			String file = null;
			String[] tmp = null;

			tmp = name.split("-");

			file = new StringBuilder(SystemConfig.getServerPath())
					.append(SystemConfig.getTemplatePath())
					.append("/")
					.append(site.getTheme())
					.append("/")
					.append(StringUtils.join(tmp, "/"))
					.append(".htm").toString();

			if (FileUtil.exists(file))
				content = FileUtil.getFileContent(file);
			//AppCache.add(key, GROUP_TEMPLATE, content, ICache.DAY_FACTOR);
		}
		return content;
	}

	public long getLastModified(Object templateSource) {
		return -1l;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader((String) templateSource);
	}

}
