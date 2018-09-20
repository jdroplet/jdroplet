package jdroplet.quartz;

import jdroplet.core.ApplicationServlet;
import jdroplet.core.SystemConfig;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.quartz.SchedulerConfigException;
import org.quartz.simpl.SimpleThreadPool;


public class QuartzThreadPool extends SimpleThreadPool {

	public void initialize() throws SchedulerConfigException {
		
		super.initialize();
        try {
			JAXPConfigurator.configure(SystemConfig.getServerPath() + "WEB-INF/proxool.xml", false);
		} catch (ProxoolException ex) {
			Logger logger = Logger.getLogger(ApplicationServlet.class);
			
			logger.error("Error", ex);
		} 
    }
}

