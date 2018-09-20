package jdroplet.core;


import jdroplet.bll.SiteManager;
import jdroplet.cache.AppCache;
import jdroplet.data.model.Site;
import jdroplet.data.model.UrlPattern;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.TextUtils;


import javax.xml.soap.Text;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;


public class SiteUrls {

	protected SiteUrls() {
	}

	protected HashMap<String, RawUrl> getSiteUrls(Integer clusterId) {
		List<UrlPattern> ups;
		HashMap<String, RawUrl> rawUrls;
		String name;
		String params;
		String key;

		key = "siteurls:setingsId:" + clusterId;
		rawUrls = (HashMap<String, RawUrl>) AppCache.get(key);

		if (rawUrls == null) {
			ups = SiteManager.getUrlPatterns(clusterId).getObjects();
			rawUrls = new HashMap<String, RawUrl>();

			for(UrlPattern pattern : ups) {
				name = pattern.getPage().getName() + "." + pattern.getAction();
				params = pattern.getParams();
				rawUrls.put(name, new RawUrl(name, params));
			}

			rawUrls = (HashMap<String, RawUrl>) PluginFactory.getInstance().applyFilters("SiteUrls@getSiteUrls", rawUrls, clusterId);
			AppCache.add(key, rawUrls);
		}
		return rawUrls;
	}

	/// 基于文件的配制
//	protected HashMap<String, RawUrl> getSiteUrls(int siteId) {
//		Configuration config = Configuration.config();
//		Properties p = new Properties();
//		FileInputStream input = null;
//		String filePath = null;
//		rawUrls = new HashMap<String, RawUrl>();
//		
//		filePath = config.getServerPath() + config.getSiteUrlsFile();
//		try {
//			// 加载URL配置文件
//			input = new FileInputStream(filePath);
//			p.load(input);
//			
//			// 迭代URL配置信息
//			for(Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
//				Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
//				rawUrls.put(entry.getKey(), new RawUrl(entry.getKey(), entry.getValue()));
//			}
//		} catch (IOException ex) {
//			throw new ApplicationException(ApplicationExceptionType.FileNotFound, filePath);
//		} finally {
//			if (input != null) {
//				try {input.close();}catch(IOException ex) {}
//			}
//		}
//	}
	
	public RawUrl getRawUrl(Site site, String name) {
		HashMap<String, RawUrl> rawUrls = getSiteUrls(site.getCluster().getId());
		return rawUrls.get(name);
	}
	
	public RawUrl getRawUrl(String siteRoot, String name) {
		Site site = SiteManager.getSite(siteRoot);
		return getRawUrl(site, name);
	}
	
	public String rawPath(String sitename, String name) {
		return getRawUrl(sitename, name).rawPath();
	}
	
//	public String getUrl(String name) {
//		String sitename = HttpContext.current().request().getSite();
//
//		return getUrl(sitename, name);
//	}
	
//	public String getUrl(String sitename, String name) {
//		return Configuration.config().getAppRootUrl() + "/" + sitename + "/" + getRawUrl(sitename, name).rawPath();
//	}
	
	public String getUrl(String name, Object... arr) {
		HttpContext context = HttpContext.current();
		HttpRequest request = context.request();
		String scheme = null;

		// 为了处理 nginx 是https，而 web容器不是http请求的情景
		scheme = SystemConfig.getProperty("app.scheme@" + request.getServerName());
		if (TextUtils.isEmpty(scheme))
			scheme = request.getScheme();

		return scheme + "://" + getSiteUrl(context.getSiteRootUrl(), name, arr);
	}
	
	public String getSiteUrl(String siteRoot, String name, Object... arr) {
		for (int idx=0; idx<arr.length; idx++) {
			arr[idx] = arr[idx].toString();
		}
		return siteRoot + "/" + MessageFormat.format(getRawUrl(siteRoot, name).rawPath(), arr);
	}
}
