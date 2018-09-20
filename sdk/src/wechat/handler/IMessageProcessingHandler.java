package wechat.handler;

import wechat.message.in.InputMessage;
import wechat.message.out.OutputMessage;

import java.lang.reflect.InvocationTargetException;

/**
 * 消息处理器
 */
public interface IMessageProcessingHandler {

    /**
     * 文字内容的消息处理
     *
     * @param msg
     * @return
     */
    public OutputMessage textMessage(InputMessage msg);

    /**
     * 地理位置类型的消息处理
     *
     * @param msg
     * @return
     */
    public OutputMessage locationMessage(InputMessage msg);

    /**
     * 图片类型的消息处理
     *
     * @param msg
     * @return
     */
    public OutputMessage imageMessage(InputMessage msg);

    /**
     * 链接类型的消息处理
     *
     * @param msg
     * @return
     */
    public OutputMessage linkMessage(InputMessage msg);

    /**
     * 语音类型的消息处理
     *
     * @param msg
     * @return
     */
    public OutputMessage voiceMessage(InputMessage msg);

    /**
     * 事件类型的消息处理。<br/>
     * 在用户首次关注公众账号时，系统将会推送一条subscribe的事件
     *
     * @param msg
     * @return
     */
    public OutputMessage eventMessage(InputMessage msg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    public OutputMessage videoMessage(InputMessage msg);

    public OutputMessage shortvideoMessage(InputMessage msg);
}
