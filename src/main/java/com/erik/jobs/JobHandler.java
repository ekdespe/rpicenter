package com.erik.jobs;

import com.erik.config.ConfigurationApp;
import com.erik.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class JobHandler {


    private JobHandler() {  }

        public static class Singleton {
        private Singleton(){};

            public static void startJobs(ConfigurationApp properties) {
                log.info(Constants.ANSI_YELLOW + "Starting Job handler" + Constants.ANSI_RESET);
                SchedulerFactory schedFact = new StdSchedulerFactory();
                try {

                    Scheduler sched = schedFact.getScheduler();
                    JobDataMap jobDataMap = new JobDataMap(Collections.singletonMap("config", properties));
                    JobDetail job = JobBuilder.newJob(HeartbeatJob.class).withIdentity("heartbeatJob", "rpicenter").usingJobData(jobDataMap).build();

                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("heartbaatTrigger", "rpicenter").startAt(Date.from(Instant.now().plusSeconds(60))).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();



                    //Trigger triggerA = TriggerBuilder.newTrigger().withIdentity("triggerA", "group2").startNow().withPriority(15).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

                    //Trigger triggerB = TriggerBuilder.newTrigger().withIdentity("triggerB", "group2").startNow().withPriority(10).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();

                    sched.scheduleJob(job, trigger);
                    sched.start();
                    log.info(Constants.ANSI_GREEN + "Success at starting Job Scheduler handler"+ Constants.ANSI_RESET);
                } catch (SchedulerException e) {
                    log.error("Error at start schedule",e);
                }

            }


        }
    }

