package jdroplet.core;

import jdroplet.data.model.RolesConfiguration;
import jdroplet.enums.PasswordFormat;
import jdroplet.exceptions.SystemException;
import jdroplet.util.TextUtils;

import javax.servlet.ServletConfig;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemConfig {
	// db
	protected static final String DATA_DAL = "db.dal";

	// app dir
	protected static final String PATH_RESOURCE = "path.resource";
	protected static final String PATH_CONFIG = "path.config";
	protected static final String PATH_LANGUAGES = "path.languages";
	protected static final String PATH_TEMPLATES = "path.templates";
	protected static final String PATH_DATA = "path.data";
	protected static final String PATH_TEMP = "path.temp";
	protected static final String PATH_ATTACHMENTS_STORE = "path.attachments.store";
	protected static final String PATH_UPLOAD = "path.upload";
	protected static final String PATH_APP = "path.app";
	protected static final String PATH_PLUGINS = "path.plugins";
	
	
	protected static final String APP_NAME = "app.name";
	protected static final String APP_ROOT = "app.root";
	protected static final String APP_LOGINURL = "app.loginurl";

	// app file
	protected static final String FILE_PAGES_SKIN = "file.pages.skin";
	protected static final String FILE_LANGUAGES = "file.languages";
	protected static final String FILE_SITE_URLS = "file.siteurls";
	protected static final String FILE_AUTHORIZATION = "file.authorization";
	protected static final String FILE_SQL_COMMANDS = "file.sqlcommands";
	protected static final String FILE_PAGES = "file.pages";
	protected static final String FILE_FREEMARKER="file.freemarker";
	
	//app setting
	protected static final String ENABLE_APPLICATION = "app.enable";
	protected static final String ENABLE_JOB = "app.enable.job";
	protected static final String ENABLE_MQ = "app.enable.mq";
	protected static final String ENABLE_DEBUG = "app.enable.debug";
	protected static final String ENABLE_CREATE_THUMB = "app.enable.createthumb";

	protected static final String ENCODING = "app.encoding";
	protected static final String PAGE_EXTENSION = "app.page.extension";
	protected static final String PAGE_API_EXTENSION = "app.page.api.extension";
	protected static final String PASSWORD_FORMAT = "app.password.format";
		
	protected static final String DATA_PAGE_SIZE = "app.data.page.size";
	protected static final String DATA_ACCESS_CLASS = "app.data.access.class";
	
	protected static final String DEFAULT_CONTAINER_ENCODING = "app.default.container.encoding";
	protected static final String DEFAULT_APP = "app.default.app";
	protected static final String DEFAULT_LANGUAGE = "app.default.language";
	
	// cache key
	public static final String CONFIG_CACHE = "app:config_cache";
	public static final String SITE_URLS_CACHE = "app:site_urls_cache";
	public static final String PAGE_AUTH_CACHE = "app:page_auth_cache";
	public static final String ROLE_CONFIG = "app:role_config";
	
	//
	protected static ServletConfig servlet_config;

	//
	private static final String pre = "${";
	private static final String post = "}";
	private static Properties props;
	private static Map<String, String> expProps;
	private static freemarker.template.Configuration fm_config;

	public static void init(ServletConfig config, String appPath) {
		FileInputStream input = null;
		String filePath = null;
		
		servlet_config = config;
		props = new Properties();
		expProps = new HashMap<String, String>();
		
		filePath = getServerPath() + getConfigFile();
		try {
			input = new FileInputStream(filePath);
			props.load(new InputStreamReader(input, "utf-8"));
		} catch (IOException e) {
			throw new SystemException(filePath + " NotFound");
		} finally {
			try {input.close();}catch(IOException ex) {}
		}

		setAppPath(appPath);
		expandVariables();
	}

	@SuppressWarnings("rawtypes")
	protected static void expandVariables() {
		String key = null;
		String value = null;
		for(Map.Entry entry : props.entrySet()) {
			key = (String)entry.getKey();
			value = (String)entry.getValue();
			expProps.put(key, getExpandVariable(value));
		}
	}
	
	protected static String getExpandVariable(String originValue) {
		int sIndex = originValue.indexOf(pre);
		int start = -1;
		int end = -1;
		String variableName = null;
		StringBuilder buffer = new StringBuilder(originValue);
		
		while(sIndex > -1) {
			start = sIndex + pre.length();
			end = buffer.indexOf(post);
			variableName = buffer.substring(start, end);
			end += post.length();
			buffer.replace(sIndex, end, props.getProperty(variableName));
			sIndex = buffer.indexOf(pre);
		}
		return buffer.toString();
	}
	
	private static void setAppPath(String value) {
		expProps.put(PATH_APP, value);
		props.put(PATH_APP, value);
	}

	public static void setFreemarkerConfig(freemarker.template.Configuration config) {
		fm_config = config;
	}

	public static SiteUrls getSiteUrls() {
		SiteUrls siteUrls = (SiteUrls) HttpRuntime.cache().get(SITE_URLS_CACHE);

		if (siteUrls == null) {
			siteUrls = new SiteUrls();
			HttpRuntime.cache().add(SITE_URLS_CACHE, siteUrls);
		}
		return siteUrls;
	}

	public static String getDataDal() {
		return getProperty(DATA_DAL);
	}

	public static freemarker.template.Configuration getFreemarkerConfig() {
		return fm_config;
	}
	
	public static String getProperty(String key) {
		return expProps.get(key);
	}
		
	public static String getConfigFile() {
		return "/WEB-INF/config/system_config.properties";
	}
	
	public static String getSiteUrlsFile() {
		return getProperty(FILE_SITE_URLS);// "/WEB-INF/config/SiteUrls.properties";
	}
	
	public static String getAuthorizationFile() {
		return getProperty(FILE_AUTHORIZATION);
	}
	
	public static String getDefaultApp() {
		return getProperty(DEFAULT_APP);//"default";
	}

	public static String getServerPath() {
		return servlet_config.getServletContext().getRealPath("");
	}
	
	public static PasswordFormat getPasswordFormat() {
		PasswordFormat format = null;
		String val = getProperty(PASSWORD_FORMAT);
		
		if (TextUtils.isEmpty(val))
			format = PasswordFormat.MD5HASH;
		else
			format = PasswordFormat.get(val);
		
		return format;
	}
	
	public static String getResourcePath() {
		return getProperty(PATH_RESOURCE);
	}
	
	public static String getConfigPath() {
		return getProperty(PATH_CONFIG);
	}
	
	public static String getLanguagePath() {
		return getProperty(PATH_LANGUAGES);
	}
	
	public static String getTemplatePath() {
		return getProperty(PATH_TEMPLATES);
	}
	
	public static String getTempPath() {
		return getProperty(PATH_TEMP);
	}
	
	public static String getUploadPath() {
		return getProperty(PATH_UPLOAD);
	}

	public static String getAttachmentsStorePath() {
		return getProperty(PATH_ATTACHMENTS_STORE);
	}

	public static String getPluginPath() {
		return getProperty(PATH_PLUGINS);
	}
	
	public static String getAppPath() {
		return getProperty(PATH_APP);
	}
	
	public static String getPagesFile() {
		return getProperty(FILE_PAGES);
	}
	
	public static String getSqlCommandsFile() {
		return getProperty(FILE_SQL_COMMANDS);
	}
	
	public static String getPagesSkinFile() {
		return getProperty(FILE_PAGES_SKIN);
	}
	
	public static String getLanguagesFile() {
		return getProperty(FILE_LANGUAGES);
	}
	
	public static String getFreemarkerFile() {
		return getProperty(FILE_FREEMARKER);
	}
	
	public static String getDefaultLanguage() {
		return getProperty(DEFAULT_LANGUAGE);
	}
	
	public static String getPageExtension() {
		return getProperty(PAGE_EXTENSION);
	}

	public static String getAPIPageExtension() {
		return getProperty(PAGE_API_EXTENSION);
	}
		
	public static int getDataPageSize() {
		return Integer.parseInt(getProperty(DATA_PAGE_SIZE));
	}
	
	public static String getDataAccessClass() {
		return getProperty(DATA_ACCESS_CLASS);
	}
			
	public static PageAuthorization getPageAuthorization(Integer clusterId) {
		String key = PAGE_AUTH_CACHE + "_site_id:" + clusterId;
		PageAuthorization pa = (PageAuthorization)HttpRuntime.cache().get(key);
		
		if (pa == null) {
			pa = new PageAuthorization(clusterId);
			HttpRuntime.cache().add(key, pa);
		}
		return pa;
	}
	
	public static RolesConfiguration getRolesConfiguration() {
		RolesConfiguration roles = (RolesConfiguration)HttpRuntime.cache().get(ROLE_CONFIG);
		if (roles == null) {
			roles = new RolesConfiguration();
			HttpRuntime.cache().add(ROLE_CONFIG, roles);
		}
		return roles;
	}

	public static String getAppName() {
		return getProperty(APP_NAME);
	}
	
	public static String getAppRoot() {
		return getProperty(APP_ROOT);
	}
		
	public static String getAppLoginUrl() {
		return getProperty(APP_LOGINURL);
	}
	
	public static String getDataPath() {
		return getProperty(PATH_DATA);
	}

	public static boolean enableJob() {
		return "true".equals(getProperty(ENABLE_JOB));
	}

	public static boolean enableCreateThumb() {
		return "true".equals(getProperty(ENABLE_CREATE_THUMB));
	}
	
	public static boolean isDebug() {
		return "true".equals(getProperty(ENABLE_DEBUG));
	}
	
	public static boolean enableMQ() {
		return "true".equals(getProperty(ENABLE_MQ));
	}
	
	public static String getEncoding() {
		return getProperty(ENCODING);
	}
	
	public static boolean enableApplication() {
		return "true".equals(getProperty(ENABLE_APPLICATION));
	}
	
}
