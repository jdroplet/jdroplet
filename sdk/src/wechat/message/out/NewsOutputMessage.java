package wechat.message.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuibo on 2018/3/5.
 */
public class NewsOutputMessage extends OutputMessage {

    public NewsOutputMessage() {
        Articles = new ArrayList();
    }

    /**
     * 消息类型:图文消息
     */
    private final String MsgType = "news";
    /**
     * 图文消息个数，限制为10条以内
     */
    private Integer ArticleCount;
    /**
     * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
     */
    private List<Item> Articles;

    public String getMsgType() {
        return MsgType;
    }

    public Integer getArticleCount() {
        return ArticleCount;
    }

    public List<Item> getArticles() {
        return Articles;
    }

    public void addArticle(Item item) {
        Articles.add(item);
        ArticleCount = Articles.size();
    }
}
