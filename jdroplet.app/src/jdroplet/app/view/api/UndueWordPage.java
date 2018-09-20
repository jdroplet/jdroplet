package jdroplet.app.view.api;

import org.andy.sensitivewdfilter.UndueWordFilter;

/**
 * Created by kuibo on 2018/5/22.
 */
public class UndueWordPage extends APIPage {

    public static final Integer UNDUE_WORD = 1006000;

    public void check() {
        String content = request.getParameter("content");
        Integer level = request.getIntParameter("level", 1);
        UndueWordFilter uwf = new UndueWordFilter(level);

        UndueWordFilter.FilterContent fc = uwf.doFilter(content);
        if (fc.getWords().size() != 0) {
            renderJSON(UNDUE_WORD, "内容含有违规词", fc.getWords());
            return;
        }

        renderJSON(0);
    }
}
