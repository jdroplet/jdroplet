package wechat.message.out;


/**
 * 输出文字消息
 * 
 */
public class TextOutputMessage extends OutputMessage {

	private String	MsgType	= "text";
	// 文本消息
	private String	Content;
	
	public TextOutputMessage() {
	}
	
	public TextOutputMessage(String content) {
		Content = content;
	}

	public String getMsgType() {
		return MsgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
