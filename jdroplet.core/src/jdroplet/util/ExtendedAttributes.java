package jdroplet.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExtendedAttributes implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> atts;

	public ExtendedAttributes() {
		atts = new HashMap<String, Object>();
	}

	public double getDouble(String key) {
		return getDouble(key, -1f);
	}

	public double getDouble(String key, double def) {
		if (atts.containsKey(key))
			def = (Double) atts.get(key);

		return def;
	}

	public boolean getBool(String key) {
		return getBool(key, false);
	}

	public boolean getBool(String key, boolean def) {
		if (atts.containsKey(key))
			def = (Boolean) atts.get(key);

		return def;
	}

	public int getInt(String key) {
		return getInt(key, -1);
	}

	public int getInt(String key, int def) {
		if (atts.containsKey(key))
			def = (Integer) atts.get(key);

		return def;
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String def) {
		if (atts.containsKey(key))
			def = String.valueOf(atts.get(key));//(String) atts.get(key);

		return def;
	}

	public void setValue(String key, Object value) {
		atts.put(key, value);
	}

	public int getExtendedAttributesCount() {
		return atts.size();
	}

	public String toString() {
		try {
			return JSONUtil.toJSONString(atts);
		} catch (Exception e) {
			return null;
		}
	}

	public void from(String str) {
		from(str, true);
	}

	@SuppressWarnings("unchecked")
	public void from(String str, boolean ignoreEmpty) {
		Map map = null;

		if (TextUtils.isEmpty(str))
			return;
		
		map = JSONUtil.toObject(str, Map.class);
		if (map != null)
			atts = map;
	}

	public int getSize() {
		return atts.size();
	}

	public Collection<String> getKeys() {
		return atts.keySet();
	}

	public void clearExtendedAttributes() {
		atts.clear();
	}
}
