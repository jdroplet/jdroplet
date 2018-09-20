package wechat.message.out;

//返回给用户的消息
public class OutputMessage {

    private String ToUserName;//微信公众号标识
    private String FromUserName;//用户openid
    private Long CreateTime;


    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }
}