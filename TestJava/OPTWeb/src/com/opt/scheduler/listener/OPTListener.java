package com.opt.scheduler.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.opt.scheduler.jobs.ETESReminder;
import com.opt.scheduler.jobs.TicketsProgressCheck;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.opt.scheduler.jobs.StatusUpdateReminder;
import com.opt.scheduler.jobs.OPTDBBackUp;
import com.opt.scheduler.jobs.OOSLANotStarted;
import com.opt.scheduler.jobs.HighPrioritiesNotStarted;
import com.opt.scheduler.jobs.RQADailyReport;
import com.opt.scheduler.jobs.RQAETAUpdateReminder;
import com.opt.scheduler.jobs.TicketsPriorityETA;

public class OPTListener implements ServletContextListener
{

   public void contextDestroyed(ServletContextEvent arg0)
   {
   }

   public void contextInitialized(ServletContextEvent arg0)
   {
      try
      {

         JobDetail StatusUpdateReminderjob = JobBuilder.newJob(StatusUpdateReminder.class)
                 .withIdentity("StatusUpdateReminder", "Reminders").build();

         Trigger StatusUpdateReminderTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("StatusUpdateTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 16,18 ? * MON-FRI"))
                 .build();

         Scheduler StatusUpdateReminderScheduler = new StdSchedulerFactory().getScheduler();
         StatusUpdateReminderScheduler.start();
         StatusUpdateReminderScheduler.scheduleJob(StatusUpdateReminderjob, StatusUpdateReminderTrigger);

         JobDetail TicketsProgressJob = JobBuilder.newJob(TicketsProgressCheck.class)
                 .withIdentity("TicketsProgress", "Reminders").build();

         Trigger TicketsProgressTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("TicketsProgressTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 9,11,13,15,17,19 ? * MON-FRI"))
                 .build();

         Scheduler TicketsProgressScheduler = new StdSchedulerFactory().getScheduler();
         TicketsProgressScheduler.start();
         TicketsProgressScheduler.scheduleJob(TicketsProgressJob, TicketsProgressTrigger);


         JobDetail OPTDBBackupJob = JobBuilder.newJob(OPTDBBackUp.class)
                 .withIdentity("OPTDBBackup", "OPTDBBackupMail").build();

         Trigger OPTDBBackupTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("OPTDBBackupTrigger", "OPTDBBackupMail")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 55 13,23 ? * *"))
                 .build();

         Scheduler OPTDBBackupScheduler = new StdSchedulerFactory().getScheduler();
         OPTDBBackupScheduler.start();
         OPTDBBackupScheduler.scheduleJob(OPTDBBackupJob, OPTDBBackupTrigger);

         /*
         JobDetail OOSLANotStartedJob = JobBuilder.newJob(OOSLANotStarted.class)
                 .withIdentity("OOSLANotStarted", "Reminders").build();

         Trigger OOSLANotStartedTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("OOSLANotStartedTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 10,16 ? * MON-FRI"))
                 .build();

         Scheduler OOSLANotStartedScheduler = new StdSchedulerFactory().getScheduler();
         OOSLANotStartedScheduler.start();
         OOSLANotStartedScheduler.scheduleJob(OOSLANotStartedJob, OOSLANotStartedTrigger);
         */
         JobDetail HighPrioritiesNotStartedJob = JobBuilder.newJob(HighPrioritiesNotStarted.class)
                 .withIdentity("HighPrioritiesNotStarted", "Reminders").build();

         Trigger HighPrioritiesNotStartedTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("HighPrioritiesNotStartedTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 10,16 ? * MON-FRI"))
                 .build();

         Scheduler HighPrioritiesNotStartedScheduler = new StdSchedulerFactory().getScheduler();
         HighPrioritiesNotStartedScheduler.start();
         HighPrioritiesNotStartedScheduler.scheduleJob(HighPrioritiesNotStartedJob, HighPrioritiesNotStartedTrigger);

         JobDetail RQADailyReportjob = JobBuilder.newJob(RQADailyReport.class)
                 .withIdentity("RQADailyReport", "DailyReport").build();

         Trigger RQADailyReportTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("RQADailyReportTrigger", "DailyReport")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 20 ? * MON-FRI"))
                 .build();

         Scheduler RQADailyReportScheduler = new StdSchedulerFactory().getScheduler();
         RQADailyReportScheduler.start();
         RQADailyReportScheduler.scheduleJob(RQADailyReportjob, RQADailyReportTrigger);

         JobDetail RQAETAUpdateReminderjob = JobBuilder.newJob(RQAETAUpdateReminder.class)
                 .withIdentity("RQAETAUpdateReminder", "Reminders").build();

         Trigger RQAETAUpdateReminderTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("RQAETAUpdateReminderTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 17,19 ? * MON-FRI"))
                 .build();

         Scheduler RQAETAUpdateReminderScheduler = new StdSchedulerFactory().getScheduler();
         RQAETAUpdateReminderScheduler.start();
         RQAETAUpdateReminderScheduler.scheduleJob(RQAETAUpdateReminderjob, RQAETAUpdateReminderTrigger);

         JobDetail TicketsPriorityETAjob = JobBuilder.newJob(TicketsPriorityETA.class)
                 .withIdentity("TicketsPriorityETA", "Reminders").build();

         Trigger TicketsPriorityETATrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("TicketsPriorityETATrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 50 15 ? * MON-FRI"))
                 .build();

         Scheduler TicketsPriorityETAScheduler = new StdSchedulerFactory().getScheduler();
         TicketsPriorityETAScheduler.start();
         TicketsPriorityETAScheduler.scheduleJob(TicketsPriorityETAjob, TicketsPriorityETATrigger);

         JobDetail ETESReminderjob = JobBuilder.newJob(ETESReminder.class)
                 .withIdentity("ETESReminder", "Reminders").build();

         Trigger ETESReminderTrigger = TriggerBuilder
                 .newTrigger()
                 .withIdentity("ETESTrigger", "Reminders")
                 .withSchedule(
                         CronScheduleBuilder.cronSchedule("0 0 11 ? * FRI"))
                 .build();

         Scheduler ETESReminderScheduler = new StdSchedulerFactory().getScheduler();
         ETESReminderScheduler.start();
         ETESReminderScheduler.scheduleJob(ETESReminderjob, ETESReminderTrigger);

      }
      catch (SchedulerException e)
      {
         e.printStackTrace();
      }

   }
}