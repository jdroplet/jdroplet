/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jdroplet.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author ghermeto
 */
public class DeserializerHelper {
    
    /**
     * 
     * @param primitive
     * @return 
     */
    protected Type getPrimitiveType(JsonPrimitive primitive) {
        if(primitive.isBoolean()) {
            return Boolean.class;
        }
        else if(primitive.isNumber()) {
            return Number.class;
        }
        return String.class;
    }
    
    /**
     * 
     * @param element
     * @param context
     * @return 
     */
    protected Object getValue(JsonElement element, JsonDeserializationContext context) {
        Object value = null;
        if(element.isJsonObject()) {
            value = context.deserialize(element, Map.class);
        }
        else if (element.isJsonArray()) {
            value = context.deserialize(element, Collection.class);
        }
        else {
            value = context.deserialize(element, getPrimitiveType(element.getAsJsonPrimitive()));
        }
        return value;
    }
}
