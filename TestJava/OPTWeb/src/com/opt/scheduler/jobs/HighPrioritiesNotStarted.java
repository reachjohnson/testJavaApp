package com.opt.scheduler.jobs;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
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

public class HighPrioritiesNotStarted implements Job
{
   Logger objLogger = Logger.getLogger(HighPrioritiesNotStarted.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      if (AppSettings.isHighPriorityNotStarted())
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
            if (AppUtil.isTodayWorkingDay(arlHolidayCalendar))
            {
               SchedulerDAO objSchedulerDAO = new SchedulerDAO();
               ArrayList<String> arlLeave = new ReportsDAO().getCurrentLeave(objDBConnection);
               ArrayList<String> arlAssignees = objSchedulerDAO.getHighPrioritiesNotStartedAssignees(objDBConnection);
               if (arlAssignees != null && !arlAssignees.isEmpty())
               {
                  String strAssignee;
                  String strAssigneeName;
                  ArrayList<String> arlTicketsPriorityETA;
                  String[] strToEmailIds;
                  String strMailSubject = "High Priority Tickets Not Started So far";
                  String[] strMessageHeader;
                  for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                  {
                     strAssigneeName = arlAssignees.get(intCount + 1);
                     strAssignee = arlAssignees.get(intCount);
                     if (arlLeave == null || arlLeave.isEmpty() || !arlLeave.contains(strAssignee))
                     {
                        arlTicketsPriorityETA = objSchedulerDAO.getHighPrioritiesNotStarted(strAssignee, objDBConnection);
                        if (arlTicketsPriorityETA != null && !arlTicketsPriorityETA.isEmpty())
                        {
                           strToEmailIds = new String[]{strAssignee};
                           strMessageHeader = new String[]{strAssigneeName, "Below High Priority Tickets Not Started So far. You have to Close As Per ETA"};
                           String strBackGround;
                           int intSerialNo = 0;
                           int intCounter = 0;
                           String MessageBody = "<table width=\"100%\" cellpadding=2 cellspacing=2 border=1>\n" +
                                   "    <tr bgcolor=\"#B0E0E6\">\n" +
                                   "        <td width=\"3%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">S.No</td>\n" +
                                   "        <td width=\"5%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Category</td>\n" +
                                   "        <td width=\"10%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Ticket ID</td>\n" +
                                   "        <td width=\"63%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Ticket Description</td>\n" +
                                   "        <td width=\"6%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Priority</td>\n" +
                                   "        <td width=\"8%\" align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">ETA</td>\n" +
                                   "        <td width=\"5%\" align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">Ageing</td>\n" +
                                   "    </tr>";

                           for (int intOOSLACount = 0; intOOSLACount < arlTicketsPriorityETA.size(); intOOSLACount += 6)
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
                                      "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + arlTicketsPriorityETA.get(intOOSLACount) + "</td>\n" +
                                      "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlTicketsPriorityETA.get(intOOSLACount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlTicketsPriorityETA.get(intOOSLACount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                      "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlTicketsPriorityETA.get(intOOSLACount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlTicketsPriorityETA.get(intOOSLACount + 2) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                      "            <td style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + arlTicketsPriorityETA.get(intOOSLACount + 3) + "</td>\n" +
                                      "            <td align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + arlTicketsPriorityETA.get(intOOSLACount + 4) + "</td>\n" +
                                      "            <td align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + arlTicketsPriorityETA.get(intOOSLACount + 5) + "</td>\n" +
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
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}