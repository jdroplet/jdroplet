package jdroplet.util.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.net.HttpCookie;

/**
 *
 * @author ghermeto
 */
public class HttpCookieSerializer implements JsonSerializer<HttpCookie> {

    public JsonElement serialize(HttpCookie src, Type typeOfSrc, 
            JsonSerializationContext context) {

        StringBuilder builder = new StringBuilder();
        builder.append(src.getName()).append("=").append(src.getValue());
        String cookie = builder.toString();
        JsonPrimitive primitive =  new JsonPrimitive(cookie);
        return primitive;
    }
}
