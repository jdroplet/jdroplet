package jdroplet.sns.wechat;

import jdroplet.plugin.PluginFactory;
import org.apache.log4j.Logger;
import wechat.handler.IMessageProcessingHandler;
import wechat.message.in.InputMessage;
import wechat.message.out.OutputMessage;
import wechat.message.out.TextOutputMessage;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by kuibo on 2018/2/26.
 */
public class MessageProcessingHandler implements IMessageProcessingHandler {

    private static Logger logger = Logger.getLogger(MessageProcessingHandler.class);

    private OutputMessage allType(InputMessage msg){
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("已经收到您的消息！");
        return out;
    }

    @Override
    public OutputMessage textMessage(InputMessage msg) {
        return (OutputMessage) PluginFactory.getInstance().applyFilters("WechatTextMessage@" + msg.getToUserName(), allType(msg), msg);
    }

    @Override
    public OutputMessage locationMessage(InputMessage msg) {
        return allType(msg);
    }

    @Override
    public OutputMessage imageMessage(InputMessage msg) {
        return allType(msg);
    }

    @Override
    public OutputMessage linkMessage(InputMessage msg) {
        return allType(msg);
    }

    @Override
    public OutputMessage voiceMessage(InputMessage msg) {
        return allType(msg);
    }

    @Override
    public OutputMessage eventMessage(InputMessage msg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OutputMessage out = (OutputMessage) PluginFactory.getInstance().applyFilters("WechatEventMessage@" + msg.getToUserName(), allType(msg), msg);
        return out;
    }

    @Override
    public OutputMessage videoMessage(InputMessage msg) {
        return allType(msg);
    }

    @Override
    public OutputMessage shortvideoMessage(InputMessage msg) {
        return allType(msg);
    }
}
