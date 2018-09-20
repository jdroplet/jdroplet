package jdroplet.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.google.gson.*;
import jdroplet.plugin.PluginFactory;

import jdroplet.util.gson.ArrayDeserializer;
import jdroplet.util.gson.MapDeserializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;

public class JSONUtil {

	/**
	 * object to json string
	 * @param obj
	 * @return
	 */
	public static String toJSONString(Object obj) {
		return toJSONString(obj, false);
	}
	
	/**
	 * object to json string
	 * @param obj
	 * @param inject_class 为true的时候会在json串中注入类信息用于反射
	 * @return
	 */
	public static String toJSONString(Object obj, boolean inject_class) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
//				.registerTypeAdapter(BigDecimal.class, new JsonSerializer<BigDecimal>() {
//
//					@Override
//					public JsonElement serialize(BigDecimal bigDecimal, Type type, JsonSerializationContext jsonSerializationContext) {
//						return new JsonPrimitive(bigDecimal);
//					}
//				})
				.create();
		
		if (inject_class) {
			JsonObject jo = null;
			jo = (JsonObject)gson.toJsonTree(obj);
			jo.addProperty("__class", obj.getClass().getName().toString());
			return gson.toJson(jo);
		} else {
			return gson.toJson(obj);
		}
	}
	
	public static String toJSONString(Object obj, boolean inject_class, String ext_key, Object ext_value) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		
		if (inject_class) {
			JsonObject jo = null;
			jo = (JsonObject)gson.toJsonTree(obj);
			jo.addProperty("__class", obj.getClass().getName().toString());
			jo.add(ext_key, (JsonObject)gson.toJsonTree(ext_value));
			return gson.toJson(jo);
		} else {
			return gson.toJson(obj);
		}
	}

	public static JsonObject toJSONObject(Object obj) {
		return toJSONObject(obj, false);
	}
	
	public static JsonObject toJSONObject(Object obj, boolean inject_class) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		
		if (inject_class) {
			JsonObject jo = null;
			jo = (JsonObject)gson.toJsonTree(obj);
			jo.addProperty("__class", obj.getClass().getName().toString());
			return jo;
		} else {
			return (JsonObject)gson.toJsonTree(obj);
		}
	}

	public static Map<String, Object> toMap(Object obj) {
		return toMap(obj, false);
	}

	public static Map<String, Object> toMap(Object obj, boolean inject_class) {
		JsonObject json =  toJSONObject(obj, inject_class);

		return toMap(json);
	}

	public static Map<String, Object> toMap(JsonObject json) {
		Map<String, Object> map = new HashMap<>();
		Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
		for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
			Map.Entry<String, JsonElement> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof JsonArray)
				map.put(key, toList((JsonArray) value));
			else if (value instanceof JsonObject)
				map.put(key, toMap((JsonObject) value));
			else
				map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JsonArray json) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < json.size(); i++) {
			Object value = json.get(i);
			if (value instanceof JsonArray) {
				list.add(toList((JsonArray) value));
			} else if (value instanceof JsonObject) {
				list.add(toMap((JsonObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

	/**
	 * string to object, json 字符串中必须含有__class类型信息，否则会抛出异常
	 * @param str
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public static Object toObject(String str) {
		JSONObject json = null;
		String name = null;
		Class<? extends Job> clazz = null;
		
		try {
			json = new JSONObject(str);
			name = json.getString("__class");
		} catch (JSONException ex) {
			throw new ClassCastException("__class meta notfound");
		}

		try {
			clazz = (Class<? extends Job>) Class.forName(name);
		} catch (ClassNotFoundException ex) {
			try {
				clazz = (Class<? extends Job>) PluginFactory.getInstance().getClassLoader().loadClass(name);
			} catch (ClassNotFoundException ex2) {
				throw new ClassCastException("class notfound:" + ex.getClass().getName() + "#" + ex.getMessage());
			}
		}
		
		return toObject(str, clazz);
	}

	/**
	 * string to json
	 * @param str 需要转换成特定对象的json字符串
	 * @param clazz 需要转换的类型
	 * @return 转换后的对象
	 */
	public static <T> T toObject(String str, Class<T> clazz) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.registerTypeAdapter(Map.class, new MapDeserializer())
				.registerTypeAdapter(Collection.class, new ArrayDeserializer())
				.registerTypeAdapter(List.class, new ArrayDeserializer())
				.registerTypeAdapter(Number.class, new JsonDeserializer() {

					@Override
					public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
						String str = jsonElement.getAsString();

						try {
							return Integer.parseInt(str);
						} catch (NumberFormatException ex) {
							return Double.parseDouble(str);
						}
					}
				})
				.create();

		return gson.fromJson(str, clazz);
	}

	public static <T> T toObject(String str, Type type) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Map.class, new MapDeserializer()).create();

		return gson.fromJson(str, type);
	}
}
