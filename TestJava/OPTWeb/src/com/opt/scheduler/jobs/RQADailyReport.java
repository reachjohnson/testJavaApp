package com.opt.scheduler.jobs;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.jms.sender.EmailQSender;
import com.opt.reports.dao.RQAReportsDAO;
import com.opt.session.valobj.EmailData;
import com.opt.tickets.dao.RQATicketsDAO;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RQADailyReport implements Job
{
   Logger objLogger = Logger.getLogger(RQADailyReport.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
      {
         ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
         if (AppUtil.isTodayWorkingDay(arlHolidayCalendar))
         {
            ArrayList<String> arlRQAMailSettings = new CommonDAO().getRQAMailSettings(objDBConnection);
            if (arlRQAMailSettings != null && !arlRQAMailSettings.isEmpty())
            {
               String strMailToTrigger = arlRQAMailSettings.get(0);
               String strMailCutOffTime = arlRQAMailSettings.get(1);
               String strToIds = arlRQAMailSettings.get(2);
               String strCCIds = arlRQAMailSettings.get(3);
               String strMailSubject = arlRQAMailSettings.get(4) + " " + AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
               String strMailHeader = arlRQAMailSettings.get(5);
               String strRQACurrentPhase = arlRQAMailSettings.get(6);
               String strRQAPOCMailIds = arlRQAMailSettings.get(7);
               if (strMailToTrigger.equalsIgnoreCase("Y") && strRQACurrentPhase.length() > 0)
               {
                  String strToday = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
                  RQAReportsDAO objRQAReportsDAO = new RQAReportsDAO();
                  String strWorkingDate = AppUtil.convertDate(AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY) + " " + strMailCutOffTime,
                          AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL);
                  SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.JAVA_DATETIME_FORMAT_MYSQL);
                  Calendar objCalendar = Calendar.getInstance();
                  objCalendar.setTime(dateFormat.parse(strWorkingDate));
                  String strPrevWorkingDate = dateFormat.format(AppUtil.getPreviousWorkingDay(objCalendar, arlHolidayCalendar));
                  int intOpeningBalance = objRQAReportsDAO.getRQAOpeningBalance(strRQACurrentPhase, strPrevWorkingDate, objDBConnection);
                  ArrayList<String> arlRQANewBugs = objRQAReportsDAO.getRQANewBugs(strRQACurrentPhase, strPrevWorkingDate, strWorkingDate, objDBConnection);
                  ArrayList<String> arlRQAResolvedBugs = objRQAReportsDAO.getRQAResolvedBugs(strRQACurrentPhase, strPrevWorkingDate, strWorkingDate, objDBConnection);
                  ArrayList<String> arlRQACurrentOpenBugs = objRQAReportsDAO.getRQACurrentOpenBugs(strRQACurrentPhase, objDBConnection);
                  ArrayList<String> arlRQAResolutionSettings = new RQATicketsDAO().getRQAResolutionSettings(objDBConnection);
                  String strResolutionBugMoved = "";
                  if (arlRQAResolutionSettings != null && !arlRQAResolutionSettings.isEmpty())
                  {
                     strResolutionBugMoved = arlRQAResolutionSettings.get(0);
                  }

                  int intClosingBalance = 0;
                  if (arlRQACurrentOpenBugs != null && !arlRQACurrentOpenBugs.isEmpty())
                  {
                     intClosingBalance = arlRQACurrentOpenBugs.size() / 6;
                  }

                  StringBuilder sbMessage = new StringBuilder();
                  String MessageBody = "<table width=\"100%\" cellpadding=0 cellspacing=0 border=0>\n" +
                          "        <tr>\n" +
                          "            <td width=\"35%\">\n" +
                          "                <table width=\"100%\" cellpadding=1 cellspacing=1 border=1>\n" +
                          "                    <tr>\n" +
                          "                        <td width=\"90%\" style=\"background-color: #0D4F8B;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: #FFFFFF;\">Opening Balance On " + strToday + "</td>\n" +
                          "                        <td width=\"10%\" style=\"background-color: #FFDAB9;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: black;\" align=\"center\">" + intOpeningBalance + "</td>\n" +
                          "                    </tr>\n" +
                          "                </table>\n" +
                          "            </td>\n" +
                          "            <td width=\"75%\"></td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0>\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "\n" +
                          "    <table width=\"100%\" cellpadding=1 cellspacing=1 border=1>\n" +
                          "        <tr style=\"background-color: #0D4F8B;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: #FFFFFF;\">\n" +
                          "            <td width=\"100%\" colspan=\"5\" align=\"center\">New Bugs</td>\n" +
                          "        </tr>\n" +
                          "        <tr style=\"background-color: #B0E0E6;font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #000000;\">\n" +
                          "            <td width=\"5%\">Sl.No</td>\n" +
                          "            <td width=\"10%\">Bug Id</td>\n" +
                          "            <td width=\"58%\">Bug Description</td>\n" +
                          "            <td width=\"7%\">Priority</td>\n" +
                          "            <td width=\"20%\">Assigned To</td>\n" +
                          "        </tr>\n";
                  if (arlRQANewBugs != null && !arlRQANewBugs.isEmpty())
                  {
                     int intSlNo = 1;
                     for (int intCount = 0; intCount < arlRQANewBugs.size(); intCount += 6)
                     {
                        MessageBody += "        <tr style=\"background-color: #f2f2f2;font-family: verdana, arial, helvetica;font-size: 12px;color: black;\">\n" +
                                "            <td>" + intSlNo++ + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQANewBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQANewBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQANewBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQANewBugs.get(intCount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + arlRQANewBugs.get(intCount + 2) + "</td>\n" +
                                "            <td>" + arlRQANewBugs.get(intCount + 3) + "</td>\n" +
                                "        </tr>\n";
                     }
                  }
                  else
                  {
                     MessageBody += "        <tr style=\"background-color: #f2f2f2; font-family: verdana, arial, helvetica; font-size: 12px; color: black;\">\n" +
                             "            <td colspan=\"5\" align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 14px;,font-weight: bold;,color: black;\">No Data Available</td>\n" +
                             "        </tr>\n";
                  }

                  MessageBody += "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "\n" +
                          "    <table width=\"100%\" cellpadding=1 cellspacing=1 border=1>\n" +
                          "        <tr style=\"background-color: #0D4F8B;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: #FFFFFF;\">\n" +
                          "            <td width=\"100%\" colspan=\"6\" align=\"center\">Resolved</td>\n" +
                          "        </tr>\n" +
                          "        <tr style=\"background-color: #B0E0E6;font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #000000;\">\n" +
                          "            <td width=\"5%\">Sl.No</td>\n" +
                          "            <td width=\"10%\">Bug Id</td>\n" +
                          "            <td width=\"45%\">Bug Description</td>\n" +
                          "            <td width=\"7%\">Priority</td>\n" +
                          "            <td width=\"12%\">Assigned To</td>\n" +
                          "            <td width=\"21%\">Resolution</td>\n" +
                          "        </tr>\n";
                  if (arlRQAResolvedBugs != null && !arlRQAResolvedBugs.isEmpty())
                  {
                     int intSlNo = 1;
                     String strTicketResolution;
                     String strMovedDomain;

                     for (int intCount = 0; intCount < arlRQAResolvedBugs.size(); intCount += 6)
                     {
                        strTicketResolution = arlRQAResolvedBugs.get(intCount + 4);
                        strMovedDomain = arlRQAResolvedBugs.get(intCount + 5);
                        if (strTicketResolution.equalsIgnoreCase(strResolutionBugMoved))
                        {
                           strTicketResolution += " - " + strMovedDomain;
                        }


                        MessageBody += "        <tr style=\"background-color: #f2f2f2;font-family: verdana, arial, helvetica;font-size: 12px;color: black;\">\n" +
                                "            <td>" + intSlNo++ + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQAResolvedBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQAResolvedBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQAResolvedBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQAResolvedBugs.get(intCount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + arlRQAResolvedBugs.get(intCount + 2) + "</td>\n" +
                                "            <td>" + arlRQAResolvedBugs.get(intCount + 3) + "</td>\n" +
                                "            <td>" + strTicketResolution + "</td>\n" +
                                "        </tr>\n";
                     }
                  }
                  else
                  {
                     MessageBody += "        <tr style=\"background-color: #f2f2f2; font-family: verdana, arial, helvetica; font-size: 12px; color: black;\">\n" +
                             "            <td colspan=\"6\" align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 14px;,font-weight: bold;,color: black;\">No Data Available</td>\n" +
                             "        </tr>\n";
                  }
                  MessageBody += "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "\n" +
                          "    <table width=\"100%\" cellpadding=1 cellspacing=1 border=1>\n" +
                          "        <tr style=\"background-color: #0D4F8B;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: #FFFFFF;\">\n" +
                          "            <td width=\"100%\" colspan=\"7\" align=\"center\">Current Open Bugs</td>\n" +
                          "        </tr>\n" +
                          "        <tr style=\"background-color: #B0E0E6;font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #000000;\">\n" +
                          "            <td width=\"5%\">Sl.No</td>\n" +
                          "            <td width=\"10%\">Bug Id</td>\n" +
                          "            <td width=\"40%\">Bug Description</td>\n" +
                          "            <td width=\"6%\">Priority</td>\n" +
                          "            <td width=\"15%\">Assigned To</td>\n" +
                          "            <td width=\"14%\">Received</td>\n" +
                          "            <td width=\"10%\">ETA</td>\n" +
                          "        </tr>\n";
                  if (arlRQACurrentOpenBugs != null && !arlRQACurrentOpenBugs.isEmpty())
                  {
                     int intSlNo = 1;
                     String strETA;
                     for (int intCount = 0; intCount < arlRQACurrentOpenBugs.size(); intCount += 6)
                     {
                        if (arlRQACurrentOpenBugs.get(intCount + 5).length() > 0)
                        {
                           strETA = arlRQACurrentOpenBugs.get(intCount + 5);
                        }
                        else
                        {
                           strETA = "Yet To Start";
                        }


                        MessageBody += "        <tr style=\"background-color: #f2f2f2;font-family: verdana, arial, helvetica;font-size: 12px;color: black;\">\n" +
                                "            <td>" + intSlNo++ + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQACurrentOpenBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQACurrentOpenBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlRQACurrentOpenBugs.get(intCount) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlRQACurrentOpenBugs.get(intCount + 1) + AppConstants.MAIL_LBL_JIRA_LINK_END + "</td>\n" +
                                "            <td>" + arlRQACurrentOpenBugs.get(intCount + 2) + "</td>\n" +
                                "            <td>" + arlRQACurrentOpenBugs.get(intCount + 3) + "</td>\n" +
                                "            <td>" + arlRQACurrentOpenBugs.get(intCount + 4) + "</td>\n" +
                                "            <td>" + strETA + "</td>\n" +
                                "        </tr>\n";
                     }
                  }
                  else
                  {
                     MessageBody += "        <tr style=\"background-color: #f2f2f2; font-family: verdana, arial, helvetica; font-size: 12px; color: black;\">\n" +
                             "            <td colspan=\"7\" align=\"center\" style=\"font-family: verdana, arial, helvetica;font-size: 14px;,font-weight: bold;,color: black;\">No Data Available</td>\n" +
                             "        </tr>\n";
                  }
                  MessageBody += "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0>\n" +
                          "        <tr>\n" +
                          "            <td width=\"35%\">\n" +
                          "                <table width=\"100%\" cellpadding=1 cellspacing=1 border=1>\n" +
                          "                    <tr>\n" +
                          "                        <td width=\"90%\" style=\"background-color: #0D4F8B;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: #FFFFFF;\">Closing Balance On " + strToday + "</td>\n" +
                          "                        <td width=\"10%\" style=\"background-color: #FFDAB9;font-family: verdana, arial, helvetica;font-size: 16px;font-weight: bold;color: black;\" align=\"center\">" + intClosingBalance + "</td>\n" +
                          "                    </tr>\n" +
                          "                </table>\n" +
                          "            </td>\n" +
                          "            <td width=\"75%\">&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n" +
                          "<table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"left\">\n" +
                          "    <tr>\n" +
                          "        <td width=\"30%\">\n" +
                          "            <table width=\"100%\" cellpadding=2 cellspacing=1 border=1 align=\"center\">\n" +
                          "                <tr bgcolor=\"#B0E0E6\">\n" +
                          "                    <td width=\"100%\" colspan=\"2\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">" + AppConstants.MAIL_LBL_AUTO_MAIL + "</td>\n" +
                          "                    </td>\n" +
                          "                </tr>\n" +
                          "            </table>\n" +
                          "        </td>\n" +
                          "        <td width=\"70%\">&nbsp;</td>\n" +
                          "    </tr>\n" +
                          "    <tr>\n" +
                          "        <td width=\"60%\">\n" +
                          "            <table width=\"100%\" cellpadding=2 cellspacing=1 border=1 align=\"center\">\n" +
                          "                <tr bgcolor=\"#B0E0E6\">\n" +
                          "                    <td width=\"100%\" colspan=\"2\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">In case of any queries, Please send mail to '" + strRQAPOCMailIds + "'</td>\n" +
                          "                    </td>\n" +
                          "                </tr>\n" +
                          "            </table>\n" +
                          "        </td>\n" +
                          "        <td width=\"70%\">&nbsp;</td>\n" +
                          "    </tr>\n" +
                          "</table>\n" +
                          "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                          "        <tr>\n" +
                          "            <td>&nbsp;</td>\n" +
                          "        </tr>\n" +
                          "    </table>\n";
                  sbMessage.append(MessageBody);
                  sbMessage.append("<span style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #003399;\">").append("Thanks and Regards").append(AppConstants.MAIL_LBL_NEXT_LINE).append("A M Johnson").append(AppConstants.MAIL_LBL_NEXT_LINE);
                  sbMessage.append("testcompany - CSC Managed Services").append("</span>").append(AppConstants.MAIL_LBL_NEXT_LINE);
                  sbMessage.append("<span style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #003399;\">").append("Desk: +91-44-66380370 | Mob: +91 87544 98563").append(AppConstants.MAIL_LBL_NEXT_LINE);
                  sbMessage.append("Email: mariajohnson@testcompany.com | manandaraj@csc.com").append("</span>").append(AppConstants.MAIL_LBL_FONT_END).append(AppConstants.MAIL_LBL_NEXT_LINE);

                  String[] strToEmailIds = {strToIds};
                  String[] strCCMailIds = {strCCIds};
                  String[] strMessageHeader = {"All", strMailHeader + " " + AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY)};
                  String[] strMessageBody = {sbMessage.toString()};
                  new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                          strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, false));
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