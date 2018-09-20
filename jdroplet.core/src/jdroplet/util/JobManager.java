package jdroplet.util;

import jdroplet.bll.Crons;
import jdroplet.data.model.Cron;
import jdroplet.plugin.PluginFactory;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


/**
 * Created by kuibo on 2017/12/26.
 */
public final class JobManager {

    private static JobManager inst = null;
    private static Logger logger = Logger.getLogger(JobManager.class);

    private SchedulerFactory factory = null;

    private JobManager() {

    }

    public static JobManager getInstance() {
        if (inst == null)
            inst = new JobManager();

        return inst;
    }

    public void star() {
        JobDetail job = null;
        DataSet<Cron> datas = null;
        Scheduler sched = null;
        CronTrigger trigger = null;
        Date ft = null;
        Class<? extends Job> clazz = null;

        logger.info("-------- Cron Scheduler Start --------");

        factory = new StdSchedulerFactory();
        try {
            sched = factory.getScheduler();
        } catch (SchedulerException ex) {
            logger.error("SchedulerException:", ex);

            return;
        }

        datas = Crons.getCrons(1, 1, Integer.MAX_VALUE);
        for(Cron ji:datas.getObjects()) {
            try {
                clazz = (Class<? extends Job>)Class.forName(ji.getRefer());
            } catch (ClassNotFoundException ex1) {
                try {
                    clazz = (Class<? extends Job>) PluginFactory.getInstance().getClassLoader().loadClass(ji.getRefer());
                } catch (ClassNotFoundException ex2) {
                    logger.error("schedule load class error:", ex2);
                }
            }

            if (clazz == null)
                continue;

            try {
                job = newJob(clazz).withIdentity(ji.getName()).build();
                trigger = newTrigger().withIdentity(ji.getName()).withSchedule(cronSchedule(ji.getCron()))
                        .build();

                sched.deleteJob(JobKey.jobKey(ji.getName()));
                ft = sched.scheduleJob(job, trigger);
                logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                        + trigger.getCronExpression());
            } catch (Exception ex) {
                logger.error("job error:", ex);
                logger.info("schedule error:"  + ex.getClass().toString() + "#"+ ex.getMessage());
            }
        }

        try {
            sched.start();
            logger.info("-------- " + datas.getTotalRecords() + " Started Scheduler --------");
        } catch (SchedulerException ex) {
            logger.error("Schedule start error:", ex);
        }
    }

    public void schedule(Integer id) {
        Scheduler sched = null;
        CronTrigger trigger = null;
        Class<? extends Job> clazz = null;
        Cron ji = null;
        JobDetail job = null;
        Date ft = null;

        if (factory == null)
            return;

        try {
            ji = Crons.getCron(id);
            clazz = (Class<? extends Job>)Class.forName(ji.getRefer());
        } catch (ClassNotFoundException ex1) {
            try {
                clazz = (Class<? extends Job>) PluginFactory.getInstance().getClassLoader().loadClass(ji.getRefer());
            } catch (ClassNotFoundException ex2) {
                logger.error("schedule load class error:", ex2);
            }
        }

        if (clazz == null)
            return;

        try {
            sched = factory.getScheduler();
            job = newJob(clazz).withIdentity(ji.getName()).build();
            trigger = newTrigger().withIdentity(ji.getName()).withSchedule(cronSchedule(ji.getCron()))
                    .build();

            sched.deleteJob(JobKey.jobKey(ji.getName()));
            ft = sched.scheduleJob(job, trigger);
            logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                    + trigger.getCronExpression());

            sched.start();
        } catch (Exception ex) {
            logger.error("schedule error:", ex);
            logger.info("schedule error:"  + ex.getClass().toString() + "#"+ ex.getMessage());
        }
    }

    public void reschedule(Integer id) {
        Scheduler sched = null;
        CronTrigger trigger = null;
        TriggerKey tk = null;
        Cron ji = null;

        try {
            ji = Crons.getCron(id);
            tk = TriggerKey.triggerKey(ji.getName());

            sched = factory.getScheduler();
            trigger = (CronTrigger) sched.getTrigger(tk);
            trigger = trigger.getTriggerBuilder().withIdentity(ji.getName())
                    .withSchedule(cronSchedule(ji.getCron())).build();
            sched.rescheduleJob(tk, trigger);
        } catch (SchedulerException ex) {
            logger.error("reschedule error:", ex);
        }
    }

    public void delete(Integer id) {
        Cron ji = null;
        JobKey jk = null;
        Scheduler sched = null;

        if (factory == null)
            return;

        ji = Crons.getCron(id);
        jk = JobKey.jobKey(ji.getName());
        try {
            sched = factory.getScheduler();
            sched.deleteJob(jk);
        } catch (SchedulerException ex) {
            logger.error("delete error:", ex);
        }
    }

    public void destory() {
        Scheduler sched = null;
        try {
            sched = factory.getScheduler();
            sched.shutdown(true);
        } catch (SchedulerException ex) {
            logger.error("shutdown err:", ex);
        }
    }

}
