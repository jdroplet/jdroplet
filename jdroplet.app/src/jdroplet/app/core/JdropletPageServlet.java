package jdroplet.app.core;

import freemarker.template.Configuration;
import jdroplet.app.view.ErrorPage;
import jdroplet.bll.Roles;
import jdroplet.bll.SiteManager;
import jdroplet.bll.Users;
import jdroplet.core.*;
import jdroplet.data.model.Message;
import jdroplet.data.model.Site;
import jdroplet.exceptions.ApplicationTemplateExceptionHandler;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.exceptions.PageNotFoundException;
import jdroplet.freemarker.core.PageTemplateLoader;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.JobManager;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by kuibo on 2017/10/16.
 */
public class JdropletPageServlet extends ApplicationServlet {
    private static Logger logger = Logger.getLogger(JdropletPageServlet.class);

    @Override
    public void init(final ServletConfig servlet_config) throws ServletException {
        String appPath = null;

        super.init(servlet_config);
        // 初始化配置文件
        appPath = servlet_config.getServletContext().getContextPath();

        SystemConfig.init(servlet_config, appPath);

        // 初始化日志文件
        System.setProperty("log4jdir", SystemConfig.getServerPath());
        DOMConfigurator.configure(SystemConfig.getServerPath() + "/WEB-INF/log4j.xml");

        // 加载插件
        PluginFactory.getInstance().launch();


        // 配置Freemarker
        Configuration fmConfig = new Configuration(freemarker.template.Configuration.VERSION_2_3_27);
        fmConfig.setTemplateLoader(new PageTemplateLoader()); // 配置Freemarker文件模板加载器
        fmConfig.setTemplateExceptionHandler(new ApplicationTemplateExceptionHandler());

        // 从配置文件中加载
        Properties props = new Properties();
        FileInputStream file = null;
        String filePath = null;

        filePath = new StringBuilder(64)
                .append(SystemConfig.getServerPath())
                .append(SystemConfig.getFreemarkerFile())
                .toString();

        props = new Properties();
        try {
            file = new FileInputStream(filePath);
            props.load(file);
            fmConfig.setSettings(props);
        } catch (Exception e) {
            logger.error("Exception", e);
        }

        fmConfig.setLocale(new Locale("", "", ""));
        SystemConfig.setFreemarkerConfig(fmConfig);

        if (SystemConfig.enableJob()) {
            JobManager.getInstance().star();
        }

        if (SystemConfig.enableMQ()) {
            MessageQueue.getInstance().start();
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Writer out = null;
        PageFactory fatory = null;
        Page page = null;
        HttpContext context = null;

        try {
            super.service(req, resp);
            context = HttpContext.current();

            if (SystemConfig.enableApplication()) {
                String pagename = context.request().getPage();
                String siteRoot = context.getSiteRootUrl();

                if (TextUtils.isEmpty(pagename))
                    throw new PageNotFoundException();

                this.validaAccess();

                fatory = PageFactory.instance();
                page = fatory.getPage(siteRoot, pagename);
                out = this.handlePage(context, page);
            } else {
                OutputStream os = resp.getOutputStream();
                String msg = "system maintenance";

                os.write(msg.getBytes());
                os.flush();
            }
        } catch (Throwable error) {
            logger.error( req.getRequestURI() + "?" + req.getQueryString() + "@error ", error);
            out = handleErr(context, error);
        } finally {
            if (out != null) out.close();
        }
    }

    protected void validaAccess() {
        Site site = null;
        String[] userRoles = null;
        String action = null;
        String pagename = null;
        String templateName = null;
        HttpContext context = null;

        context = HttpContext.current();
        pagename = context.request().getPage();
        action = context.request().getAction();
        templateName = pagename + "." + action;

        userRoles = context.getUser().getRoles();
        if (userRoles.length == 0) {
            userRoles = Roles.getUserRoleNames(Users.getCurrentUser().getId());
        }


        if (userRoles.length == 0)
            userRoles = new String[]{"Guests"};

        site = SiteManager.getCurrentSite();
        if (!SystemConfig.getPageAuthorization(site.getCluster().getId()).validaAccess(templateName, userRoles)) {
            throw new AuthorizationException("您没有权限访问");
        }
    }

    protected Writer handleErr(HttpContext context, Throwable error) {
        Writer out = null;
        try {
            ErrorPage page = new ErrorPage();
            page.setThrowable(error);
            out = this.handlePage(context, page);
        } catch (Throwable t) {
            logger.error("handleErr", t);
        }
        return out;
    }
}
