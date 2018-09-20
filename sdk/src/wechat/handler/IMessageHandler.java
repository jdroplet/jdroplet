package wechat.handler;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by kuibo on 2018/2/26.
 */
public interface IMessageHandler {

    String invoke(String inputXml) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
