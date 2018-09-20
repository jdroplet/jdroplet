package jdroplet.app.view;

import jdroplet.app.view.api.APIPage;
import jdroplet.bll.Attachments;
import jdroplet.bll.Posts;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.data.model.*;
import jdroplet.exceptions.ApplicationException;
import jdroplet.util.StatusData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuibo on 2017/9/21.
 */
public class FilePage extends APIPage {

    class File {
        public int id;
        public String rawName;
        public String name;
        public String path;
        public long size;

    }

    public void recv() throws UnsupportedEncodingException {
        Integer itemId = null;
        Site site = null;
        User user = null;
        List<Attachment> atts = null;
        List<File> files = null;
        StatusData sd = null;
        String folder = null;
        Cluster cluster = null;

        sd = new StatusData();
        site = SiteManager.getCurrentSite();
        cluster = SiteManager.getClusterBySite(site.getId());
        user = Users.getCurrentUser();
        itemId = request.getIntParameter("itemId");
        folder = request.getParameter("folder");

        try {
            atts = Attachments.addAttachment(cluster.getId(), itemId, user.getId(), folder, request.getFiles());
            files = new ArrayList<File>();

            for(Attachment at:atts) {
                File f = new File();
                int pos = 0;

                pos = at.getDiskFilename().lastIndexOf("/") + 1;
                f.id = at.getId().intValue();
                f.rawName = at.getFilename();
                f.name = at.getDiskFilename().substring(pos, at.getDiskFilename().length());
                f.size = at.getSize().longValue();
                f.path = at.getDiskFilename();
                
                files.add(f);
            }

            sd.setStatus(0);
            sd.setData(files);
        } catch (ApplicationException ex) {
            sd.setStatus(1);
            sd.setMsg(ex.getMessage());
        }

        render(sd);
    }
}