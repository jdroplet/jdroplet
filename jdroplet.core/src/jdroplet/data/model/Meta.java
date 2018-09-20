package jdroplet.data.model;

import jdroplet.bll.Metas;
import jdroplet.core.DateTime;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Meta extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> values = new HashMap<String, Object>();

	public void setValue(String key, Object value) {
		values.put(key, value);
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String def) {
		Object val = values.get(key);
		
		if (val == null) {
			val = Metas.getValue(getClass(), getId(), key);
			if (val == null)
				val = def;
			values.put(key, val);
		}
		if (val == null)
			return null;
		else if (val instanceof String)
			return (String) val;
		else
			return JSONUtil.toJSONString(val);

	}

	public Date getDate(String key) {
		Date date = null;
		String val = getString(key, "1999-01-01 00:00:00");

		return DateTime.parse(val).getDate();
	}

	public Integer getInt(String key) {
		 return getInt(key, -1);
	}

	public Integer getInt(String key, int def) {
		String val = getString(key);
		return TextUtils.isEmpty(val) ? def : Integer.parseInt(val);
	}

	public boolean getBool(String key) {
		return getBool(key, false);
	}

	public boolean getBool(String key, boolean def) {
		String val = getString(key);

		if (TextUtils.isEmpty(val)) {
			values.put(key, def);
			return def;
		} else if ("0".equals(val))
			return false;
		else if ("1".equals(val))
			return true;
		else
			return Boolean.parseBoolean(val);
	}

	public long getLong(String key, long def) {
		String val = getString(key);
		return TextUtils.isEmpty(val) ? def : Long.parseLong(val);
	}
	
	public Map<String, Object> getValues() {
		return values;
	}
}
