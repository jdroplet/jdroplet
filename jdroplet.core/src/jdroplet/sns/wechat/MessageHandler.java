package jdroplet.sns.wechat;

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import wechat.handler.IMessageHandler;
import wechat.handler.IMessageProcessingHandler;
import wechat.message.in.InputMessage;
import wechat.message.out.Item;
import wechat.message.out.OutputMessage;
import wechat.utils.XStreamFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by kuibo on 2018/2/26.
 */
public class MessageHandler implements IMessageHandler {

    private static Logger logger = Logger.getLogger(MessageHandler.class);

    @Override
    public String invoke(String inputXml) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OutputMessage outMessage = null;
        InputMessage inMessage = null;
        XStream xs = null;
        IMessageProcessingHandler handler = null;
        String msgType = null;
        String content = null;
        Method method = null;

        logger.info(inputXml);
        xs = XStreamFactory.init(false);
        xs.alias("xml", InputMessage.class);
        inMessage = (InputMessage) xs.fromXML(inputXml);
        msgType = inMessage.getMsgType();

        handler = new MessageProcessingHandler();
        method = handler.getClass().getMethod(msgType + "Message", InputMessage.class);
        outMessage = (OutputMessage) method.invoke(handler, inMessage);

        outMessage.setCreateTime(new Date().getTime());
        outMessage.setToUserName(inMessage.getFromUserName());
        outMessage.setFromUserName(inMessage.getToUserName());

        xs = XStreamFactory.init(true);
        xs.alias("item", Item.class);
        xs.alias("xml", outMessage.getClass());
        content = xs.toXML(outMessage);
        return content;
    }
}
