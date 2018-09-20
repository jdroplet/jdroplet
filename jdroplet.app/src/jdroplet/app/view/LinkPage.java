package jdroplet.app.view;

/**
 * Created by kuibo on 2018/3/6.
 */
public class LinkPage extends MasterPage {

    public void show() {
        String link = request.getQueryString();

        response.setRedirect(link);
    }
}
