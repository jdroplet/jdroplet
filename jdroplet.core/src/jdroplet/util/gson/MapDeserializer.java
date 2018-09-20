package jdroplet.util.gson;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;

/** 
 * @author ghermeto
 */
public class MapDeserializer extends DeserializerHelper
    implements JsonDeserializer<Map<String, Object>> {

    /**
     * 
     * @param element
     * @param typeOfT
     * @param context
     * @return
     * @throws JsonParseException 
     */
    public Map<String, Object> deserialize(JsonElement element, Type typeOfT, 
            JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonNull()) {
            return null;
        } 
        JsonObject json = element.getAsJsonObject();
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            JsonElement jsonValue = entry.getValue();
            result.put(entry.getKey(), getValue(jsonValue, context));
        }
        return result;
    }
}