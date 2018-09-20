package jdroplet.app.view;

/**
 * Created by kuibo on 2018/8/17.
 */
public class QRCodePage extends MasterPage {

    public void show() {
        String key = request.getParameter("key");

        addItem("key", key);
    }
}
