package com.opt.scheduler.jobs;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
import com.opt.jms.sender.EmailQSender;
import com.opt.reports.dao.RQAReportsDAO;
import com.opt.reports.dao.ReportsDAO;
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

public class TicketsProgressCheck implements Job
{
   Logger objLogger = Logger.getLogger(TicketsProgressCheck.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      if (AppSettings.isTicketsProgressCheck())
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
            if (AppUtil.isTodayWorkingDay(arlHolidayCalendar))
            {
               SchedulerDAO objSchedulerDAO = new SchedulerDAO();
               ArrayList<String> arlLeave = new ReportsDAO().getCurrentLeave(objDBConnection);
               ArrayList<String> arlTicketsProgressAssignees = objSchedulerDAO.getTicketsProgressAssignees(objDBConnection);
               if (arlTicketsProgressAssignees != null && !arlTicketsProgressAssignees.isEmpty())
               {
                  String strAssignee;
                  String strAssigneeName;
                  for (int intCount = 0; intCount < arlTicketsProgressAssignees.size(); intCount += 2)
                  {
                     strAssigneeName = arlTicketsProgressAssignees.get(intCount);
                     strAssignee = arlTicketsProgressAssignees.get(intCount + 1);
                     if (arlLeave == null || arlLeave.isEmpty() || !arlLeave.contains(strAssignee))
                     {
                        if (objSchedulerDAO.getTicketsProgressCheck(strAssignee, objDBConnection))
                        {
                           String[] strToEmailIds = {strAssignee};
                           String strMailSubject = "You Do Not Have Any In Progress Tickets";
                           String[] strMessageHeader = {strAssigneeName, "You Do Not Have Any In Progress Tickets. Please contact your Lead and get Tickets assigned to your name"};
                           new EmailQSender().send(new EmailData(strToEmailIds, null, null, strMailSubject,
                                   strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, null, false));
                        }
                     }
                  }
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