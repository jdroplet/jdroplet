package jdroplet.util;

import jdroplet.bll.SiteManager;
import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Site;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class I18n {

    private static Logger logger = Logger.getLogger(I18n.class);

    private static final String LAN = "zh_CN";

    public static String getString(String key, Object... params) {
        Site site = null;

        site = SiteManager.getCurrentSite();
        return getString(site.getTheme(), LAN, key);
    }

    public static String getString(String theme, String key, Object... params) {
        String value = null;

        value = getString(theme, LAN, key);
        if (value == null)
            value = key;

        return MessageFormat.format(value, params);
    }

    private static String getString(String theme, String lan, String key) {
        Map<String, String> map = null;
        String cacheKey = "I18n_" + theme + "_" + lan;

        map = (Map<String, String>) AppCache.get(cacheKey);
        if (map == null) {
            String file = null;

            map = new HashMap<>();
            file = new StringBuilder(64)
                    .append(SystemConfig.getServerPath())
                    .append(SystemConfig.getLanguagePath())
                    .append("/" + lan + ".xml")
                    .toString();
            try {
                fill(map, file);
            } catch (DocumentException ex) {
                logger.error(ex);
            }

            file = new StringBuilder(64)
                    .append(SystemConfig.getServerPath())
                    .append(SystemConfig.getTemplatePath())
                    .append("/")
                    .append(theme)
                    .append("/dist/languages/")
                    .append(lan + ".xml")
                    .toString();
            try {
                fill(map, file);
            } catch (DocumentException ex) {
                logger.error(ex);
            }

            AppCache.add(cacheKey, map, ICache.DAY_FACTOR * 30);
        }

        return map.get(key);
    }

    private static void fill(Map<String, String> map, String file) throws DocumentException {
        Document doc = null;
        SAXReader saxReader = null;
        Iterator<Element> iter;
        Element elm;
        String name = null;
        String value = null;

        saxReader = new SAXReader();
        doc = saxReader.read(file);
        iter = doc.selectNodes("//string").iterator();
        while (iter.hasNext()) {
            elm = iter.next();
            name = elm.attributeValue("name");
            value = elm.getText();

            map.put(name, value);
        }
    }
}
