package com.opt.scheduler.jobs;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
import com.opt.jms.sender.EmailQSender;
import com.opt.reports.dao.RQAReportsDAO;
import com.opt.scheduler.dao.SchedulerDAO;
import com.opt.session.valobj.EmailData;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.util.ArrayList;

public class RQAETAUpdateReminder implements Job
{
   Logger objLogger = Logger.getLogger(RQAETAUpdateReminder.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      if (AppSettings.isRQAETAReminder())
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
            if (AppUtil.isTodayWorkingDay(arlHolidayCalendar))
            {
               ArrayList<String> arlAssignees = new SchedulerDAO().getRQAOpenTicketsAssignees(objDBConnection);
               if (arlAssignees != null && !arlAssignees.isEmpty())
               {
                  ArrayList<String> arlToMailIds = new ArrayList<>();
                  for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                  {
                     arlToMailIds.add(arlAssignees.get(intCount));
                  }
                  String[] strToEmailIds = arlToMailIds.toArray(new String[arlToMailIds.size()]);
                  String strMailSubject = "RQA Bugs ETA Update Reminder (" + AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY) + ")";
                  String[] strMessageHeader = {"", "Please Update ETA for all Open RQA Bugs before 7.45 PM. Automated RQA Daily Report will be triggered at 8 PM"};
                  new EmailQSender().send(new EmailData(strToEmailIds, null, null, strMailSubject,
                          strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, null, false));
               }
            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new JobExecutionException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}