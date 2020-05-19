package com.opt.scheduler.jobs;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.jms.sender.EmailQSender;
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

public class OOSLANotStarted implements Job
{
   Logger objLogger = Logger.getLogger(OOSLANotStarted.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
      {
         ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
         if (AppUtil.isTodayWorkingDay(arlHolidayCalendar))
         {
            SchedulerDAO objSchedulerDAO = new SchedulerDAO();
            ArrayList<String> arlLeave = new ReportsDAO().getCurrentLeave(objDBConnection);
            ArrayList<String> arlOOSLANotStartedAssignees = objSchedulerDAO.getOOSLANotStartedAssignees(objDBConnection);
            if (arlOOSLANotStartedAssignees != null && !arlOOSLANotStartedAssignees.isEmpty())
            {
               String strAssignee;
               String strAssigneeName;
               ArrayList<String> arlOOSLANotStartedTickets;
               String[] strToEmailIds;
               String strMailSubject = "You have OOSLA Tickets and Not Started So far ???";
               String[] strMessageHeader;
               for (int intCount = 0; intCount < arlOOSLANotStartedAssignees.size(); intCount += 2)
               {
                  strAssigneeName = arlOOSLANotStartedAssignees.get(intCount);
                  strAssignee = arlOOSLANotStartedAssignees.get(intCount + 1);
                  if (arlLeave == null || arlLeave.isEmpty() || !arlLeave.contains(strAssignee))
                  {
                     arlOOSLANotStartedTickets = objSchedulerDAO.getOOSLANotStartedTickets(strAssignee, objDBConnection);
                     if (arlOOSLANotStartedTickets != null && !arlOOSLANotStartedTickets.isEmpty())
                     {
                        strToEmailIds = new String[]{strAssignee};
                        strMessageHeader = new String[]{strAssigneeName, "You have OOSLA tickets and not started so far. Please Prioritize and close these tickets ASAP"};
                        String strBackGround;
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String MessageBody = "<table width=\"100%\" cellpadding=2 cellspacing=2 border=1>\n" +
                                "    <tr bgcolor=\"#B0E0E6\">\n" +
                                "        <td width=\"5%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">S.No</td>\n" +
                                "        <td width=\"15%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Ticket ID</td>\n" +
                                "        <td width=\"80%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Ticket Description</td>\n" +
                                "    </tr>";

                        for (int intOOSLACount = 0; intOOSLACount < arlOOSLANotStartedTickets.size(); intOOSLACount += 2)
                        {
                           if (intCounter % 2 == 0)
                           {
                              strBackGround = "#f2f2f2";
                           }
                           else
                           {
                              strBackGround = "#DCDCDC";
                           }

                           MessageBody += "<tr bgcolor=\"" + strBackGround + "\">\n" +
                                   "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + ++intSerialNo + "</td>\n" +
                                   "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlOOSLANotStartedTickets.get(intOOSLACount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlOOSLANotStartedTickets.get(intOOSLACount) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                   "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlOOSLANotStartedTickets.get(intOOSLACount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlOOSLANotStartedTickets.get(intOOSLACount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                   "         </tr>";
                           intCounter++;
                        }
                        MessageBody += "</table>\n\n";
                        MessageBody += "<br>";
                        String[] strMessageBody = {MessageBody};
                        new EmailQSender().send(new EmailData(strToEmailIds, null, null, strMailSubject,
                                strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, false));
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
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}