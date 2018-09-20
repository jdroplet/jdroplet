package jdroplet.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/** 
 * @author ghermeto
 */
public class ArrayDeserializer extends DeserializerHelper
    implements JsonDeserializer<Collection<Object>> {

    /**
     * 
     * @param element
     * @param typeOfT
     * @param context
     * @return
     * @throws JsonParseException 
     */
    public Collection<Object> deserialize(JsonElement element, Type typeOfT, 
            JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonNull()) {
            return null;
        } 
        JsonArray json = element.getAsJsonArray();
        Collection<Object> result = new ArrayList<Object>();
        for (JsonElement jsonValue : json) {
            result.add(getValue(jsonValue, context));
        }
        return result;
    }
}