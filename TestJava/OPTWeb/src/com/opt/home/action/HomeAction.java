package com.opt.home.action;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
import com.opt.exception.TaskException;
import com.opt.home.dao.HomeDAO;
import com.opt.util.AppConstants;
import com.opt.session.valobj.SessionInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opt.util.AppUtil;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(HomeAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String homePage() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            HttpSession objSession = objRequest.getSession(false);
            SessionInfo objSessionInfo = (SessionInfo) objSession.getAttribute(AppConstants.SESSION_DATA);
            HomeDAO objHomeDAO = new HomeDAO();
            ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
            String strWorkingDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            Calendar objCalendar = Calendar.getInstance();
            objCalendar.setTime(dateFormat.parse(strWorkingDate));
            String strPrevWorkingDate = dateFormat.format(AppUtil.getPreviousWorkingDay(objCalendar, arlHolidayCalendar));

            String strTeamSummary = "";

            if (AppUtil.checkNull(objRequest.getParameter("FROMLOGIN")).equalsIgnoreCase("FROMLOGIN") && objSessionInfo.getUserInfo().getTeam().equalsIgnoreCase(AppConstants.MGMT_TEAM))
            {
               strTeamSummary = "YES";
            }
            else
            {
               strTeamSummary = AppUtil.checkNull(objRequest.getParameter("hidTeamSummary"));
            }

            ArrayList<String> arlTicketsForETAUpdate = objHomeDAO.getTicketsForETAUpdate(objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            ArrayList<String> arlTicketsForCommentsUpdate = null;
            ArrayList<String> arlInternalTicketsForETAUpdate = null;
            ArrayList<String> arlRQATicketsForETAUpdate = null;

            ArrayList<String> arlBugsForETAUpdate = new ArrayList<String>();
            ArrayList<String> arlCriticalTasksForETAUpdate = new ArrayList<>();
            ArrayList<String> arlMajorTasksForETAUpdate = new ArrayList<String>();
            ArrayList<String> arlTopAgeingTasksForETAUpdate = new ArrayList<String>();
            ArrayList<String> arlETACrossedTickets = new ArrayList<String>();

            if (arlTicketsForETAUpdate != null && !arlTicketsForETAUpdate.isEmpty())
            {
               String strRefNo = "";
               String strTicketCategory = "";
               String strTicketId = "";
               String strTicketDesc = "";
               String strTicketPriority = "";
               String strETA = "";
               String strAgeing;
               for (int iCount = 0; iCount < arlTicketsForETAUpdate.size(); iCount += 7)
               {
                  strRefNo = arlTicketsForETAUpdate.get(iCount);
                  strTicketCategory = arlTicketsForETAUpdate.get(iCount + 1);
                  strTicketId = arlTicketsForETAUpdate.get(iCount + 2);
                  strTicketDesc = arlTicketsForETAUpdate.get(iCount + 3);
                  strTicketPriority = arlTicketsForETAUpdate.get(iCount + 4);
                  strETA = arlTicketsForETAUpdate.get(iCount + 5);
                  strAgeing = arlTicketsForETAUpdate.get(iCount + 6);
                  ;
                  if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG) && (strETA == null || strETA.isEmpty()))
                  {
                     arlBugsForETAUpdate.add(strRefNo);
                     arlBugsForETAUpdate.add(strTicketCategory);
                     arlBugsForETAUpdate.add(strTicketId);
                     arlBugsForETAUpdate.add(strTicketDesc);
                     arlBugsForETAUpdate.add(strTicketPriority);
                     arlBugsForETAUpdate.add(strETA);
                     arlBugsForETAUpdate.add(strAgeing);
                  }
                  else if (strTicketCategory.equalsIgnoreCase(AppConstants.TASK) && strTicketPriority.equalsIgnoreCase("Major") && (strETA == null || strETA.isEmpty()))
                  {
                     arlMajorTasksForETAUpdate.add(strRefNo);
                     arlMajorTasksForETAUpdate.add(strTicketCategory);
                     arlMajorTasksForETAUpdate.add(strTicketId);
                     arlMajorTasksForETAUpdate.add(strTicketDesc);
                     arlMajorTasksForETAUpdate.add(strTicketPriority);
                     arlMajorTasksForETAUpdate.add(strETA);
                     arlMajorTasksForETAUpdate.add(strAgeing);
                  }
                  else if (strTicketCategory.equalsIgnoreCase(AppConstants.TASK) && strTicketPriority.equalsIgnoreCase("Critical") && (strETA == null || strETA.isEmpty()))
                  {
                     arlCriticalTasksForETAUpdate.add(strRefNo);
                     arlCriticalTasksForETAUpdate.add(strTicketCategory);
                     arlCriticalTasksForETAUpdate.add(strTicketId);
                     arlCriticalTasksForETAUpdate.add(strTicketDesc);
                     arlCriticalTasksForETAUpdate.add(strTicketPriority);
                     arlCriticalTasksForETAUpdate.add(strETA);
                     arlCriticalTasksForETAUpdate.add(strAgeing);
                  }
                  else if (strTicketCategory.equalsIgnoreCase(AppConstants.TASK) && (strTicketPriority.equalsIgnoreCase("Normal") ||
                          strTicketPriority.equalsIgnoreCase("Minor") || strTicketPriority.equalsIgnoreCase("Trivial")) && Integer.parseInt(strAgeing) < AppSettings.getTopAgeingThreshold() && (strETA == null || strETA.isEmpty()))
                  {
                     arlTopAgeingTasksForETAUpdate.add(strRefNo);
                     arlTopAgeingTasksForETAUpdate.add(strTicketCategory);
                     arlTopAgeingTasksForETAUpdate.add(strTicketId);
                     arlTopAgeingTasksForETAUpdate.add(strTicketDesc);
                     arlTopAgeingTasksForETAUpdate.add(strTicketPriority);
                     arlTopAgeingTasksForETAUpdate.add(strETA);
                     arlTopAgeingTasksForETAUpdate.add(strAgeing);
                  }
                  else if ((strTicketCategory.equalsIgnoreCase(AppConstants.TASK) || strTicketCategory.equalsIgnoreCase(AppConstants.BUG)) && (strETA != null || !strETA.isEmpty()))
                  {
                     arlETACrossedTickets.add(strRefNo);
                     arlETACrossedTickets.add(strTicketCategory);
                     arlETACrossedTickets.add(strTicketId);
                     arlETACrossedTickets.add(strTicketDesc);
                     arlETACrossedTickets.add(strTicketPriority);
                     arlETACrossedTickets.add(strETA);
                     arlETACrossedTickets.add(strAgeing);
                  }

               }
            }

            if(!AppSettings.isETABugs())
            {
               arlBugsForETAUpdate = null;
            }
            if(!AppSettings.isETACriticalTasks())
            {
               arlCriticalTasksForETAUpdate = null;
            }
            if(!AppSettings.isETAMajorTasks())
            {
               arlMajorTasksForETAUpdate = null;
            }
            if(!AppSettings.isETATopAgeingTasks())
            {
               arlTopAgeingTasksForETAUpdate = null;
            }
            if(!AppSettings.isRescheduleCrossedETA())
            {
               arlETACrossedTickets = null;
            }
            if(AppSettings.isETAInteranlTickets())
            {
               arlInternalTicketsForETAUpdate = objHomeDAO.getInternalTicketsForETAUpdate(objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            }
            if(AppSettings.isETARQABugs())
            {
               arlRQATicketsForETAUpdate = objHomeDAO.getRQATicketsForETAUpdate(objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            }
            if(AppSettings.isDailyStatus())
            {
               arlTicketsForCommentsUpdate = populateTicketsForCommentsUpdate(objSessionInfo.getUserInfo().getUserId(), strWorkingDate, strPrevWorkingDate, objDBConnection, objHomeDAO);
            }

            if ((arlBugsForETAUpdate != null && !arlBugsForETAUpdate.isEmpty()) ||
                    (arlCriticalTasksForETAUpdate != null && !arlCriticalTasksForETAUpdate.isEmpty()) ||
                    (arlTicketsForCommentsUpdate != null && !arlTicketsForCommentsUpdate.isEmpty()) ||
                    (arlMajorTasksForETAUpdate != null && !arlMajorTasksForETAUpdate.isEmpty()) ||
                    (arlTopAgeingTasksForETAUpdate != null && !arlTopAgeingTasksForETAUpdate.isEmpty()) ||
                    (arlETACrossedTickets != null && !arlETACrossedTickets.isEmpty()) ||
                    (arlInternalTicketsForETAUpdate != null && !arlInternalTicketsForETAUpdate.isEmpty()) ||
                    (arlRQATicketsForETAUpdate != null && !arlRQATicketsForETAUpdate.isEmpty()))
            {
               if(arlETACrossedTickets != null && !arlETACrossedTickets.isEmpty())
               {
                  ArrayList<String> arlOpenTicketsETAHistory = new CommonDAO().getOpenTicketsETAHistory(objSessionInfo.getUserInfo().getUserId(), objDBConnection);
                  ArrayList<Object> arlPopulatedETAHistory = objWebUtil.populatedETAHistory(arlOpenTicketsETAHistory);
                  objRequest.setAttribute("OpenTicketsETAHistory", arlPopulatedETAHistory);
               }
               strForward = "PrioritizeTickets";
               objSessionInfo.getUserInfo().setBlnAccessRestricted(true);
               objRequest.setAttribute("BugsForETAUpdate", arlBugsForETAUpdate);
               objRequest.setAttribute("CriticalTasksForETAUpdate", arlCriticalTasksForETAUpdate);
               objRequest.setAttribute("MajorTasksForETAUpdate", arlMajorTasksForETAUpdate);
               objRequest.setAttribute("TopAgeingTasksForETAUpdate", arlTopAgeingTasksForETAUpdate);
               objRequest.setAttribute("ETACrossedTickets", arlETACrossedTickets);
               objRequest.setAttribute("TicketsForCommentsUpdate", arlTicketsForCommentsUpdate);
               objRequest.setAttribute("InternalTicketsForETAUpdate", arlInternalTicketsForETAUpdate);
               objRequest.setAttribute("RQATicketsForETAUpdate", arlRQATicketsForETAUpdate);
               objRequest.setAttribute("CommentsDate", strPrevWorkingDate);
            }
            else
            {
               objSessionInfo.getUserInfo().setBlnAccessRestricted(false);
               String strUserId = null;
               if (strTeamSummary.equalsIgnoreCase("YES"))
               {
                  strUserId = AppConstants.ALL_ASSIGNEES;
                  objRequest.setAttribute("TeamSummary", strTeamSummary);
               }
               else
               {
                  strUserId = objSessionInfo.getUserInfo().getUserId();
               }

               ArrayList<String> arlSLAMissingTicketDetails = objHomeDAO.getSLAMissingTickets(strUserId, objDBConnection);
               ArrayList<String> arlOpenTicketsCount = objHomeDAO.getOpenTicketsCount(strUserId, objDBConnection);
               ArrayList<String> arlClosedTasksBugsCountsCurrentMonth = objHomeDAO.getClosedTasksBugsCountsCurrentMonth(strUserId, objDBConnection);
               ArrayList<String> arlClosedTasksBugsCountsCurrentWeek = objHomeDAO.getClosedTasksBugsCountsCurrentWeek(strUserId, objDBConnection);
               String strRQACurrentPhase = new CommonDAO().getRQACurrentPhase(objDBConnection);
               if (strRQACurrentPhase != null && strRQACurrentPhase.length() > 0)
               {
                  ArrayList<String> arlRQAClosedBugsCounts = objHomeDAO.getRQAClosedBugsCounts(strRQACurrentPhase, strUserId, objDBConnection);
                  objRequest.setAttribute("RQAClosedBugsCounts", arlRQAClosedBugsCounts);
                  objRequest.setAttribute("RQACurrentPhase", strRQACurrentPhase);
               }
               int intSLAMissedTicketsCount = 0;
               int intNearingSLATicketsCount = 0;
               if (arlSLAMissingTicketDetails != null && !arlSLAMissingTicketDetails.isEmpty())
               {
                  ArrayList<String> arlNearingSLATickets = new ArrayList<>();
                  String strTicketId;
                  String strPlannedEndDate;
                  Date dtPlannedEndDate;
                  String strCurrentDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
                  SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
                  Date dtCurrentDate = sdf.parse(strCurrentDate);
                  Calendar objCalendarNxtWD = Calendar.getInstance();
                  objCalendarNxtWD.setTime(dtCurrentDate);
                  Date dtNextWorkingDate = AppUtil.getNextWorkingDay(objCalendarNxtWD, arlHolidayCalendar);
                  for (int intCount = 0; intCount < arlSLAMissingTicketDetails.size(); intCount += 2)
                  {
                     strTicketId = arlSLAMissingTicketDetails.get(intCount);
                     strPlannedEndDate = arlSLAMissingTicketDetails.get(intCount + 1);
                     dtPlannedEndDate = sdf.parse(strPlannedEndDate);
                     if (dtPlannedEndDate.compareTo(dtCurrentDate) < 0)
                     {
                        intSLAMissedTicketsCount++;
                     }
                     else
                     {
                        if (dtPlannedEndDate.compareTo(dtCurrentDate) == 0 || dtPlannedEndDate.compareTo(dtNextWorkingDate) == 0)
                        {
                           intNearingSLATicketsCount++;
                           arlNearingSLATickets.add(strTicketId);
                        }
                     }
                  }
                  objRequest.setAttribute("NearingSLATickets", arlNearingSLATickets);
               }
               objRequest.setAttribute("SLAMissedTicketsCount", String.valueOf(intSLAMissedTicketsCount));
               objRequest.setAttribute("NearingSLATicketsCount", String.valueOf(intNearingSLATicketsCount));
               if (AppUtil.checkNull(objRequest.getParameter("FROMLOGIN")).equalsIgnoreCase("FROMLOGIN"))
               {
                  if (intSLAMissedTicketsCount > 0)
                  {
                     objRequest.setAttribute("SLAMissedTicketsAvailable", "Available");
                  }
               }
               ArrayList<String> arlInternalTicketDetails = objHomeDAO.getInternalTicketDetails(strUserId, objDBConnection);
               ArrayList<String> arlInternalTicketDetailsCreatedByYou = objHomeDAO.getInternalTicketDetailsCreatedByYou(strUserId, objDBConnection);
               objRequest.setAttribute("InternalTicketDetails", arlInternalTicketDetails);
               objRequest.setAttribute("InternalTicketDetailsCreatedByYou", arlInternalTicketDetailsCreatedByYou);
               ArrayList<String> arlTicketDetails = objHomeDAO.getTicketDetails(strUserId, objDBConnection);
               objRequest.setAttribute("TicketDetails", arlTicketDetails);
               ArrayList<String> arlRQATicketDetails = objHomeDAO.getRQATicketDetails(strUserId, objDBConnection);
               objRequest.setAttribute("RQATicketDetails", arlRQATicketDetails);
               objRequest.setAttribute("OpenTicketsCount", arlOpenTicketsCount);
               objRequest.setAttribute("ClosedTasksBugsCountsCurrentMonth", arlClosedTasksBugsCountsCurrentMonth);
               objRequest.setAttribute("ClosedTasksBugsCountsCurrentWeek", arlClosedTasksBugsCountsCurrentWeek);
               ArrayList<String> arlOpenTicketsETAHistory = new CommonDAO().getOpenTicketsETAHistory(strUserId, objDBConnection);
               ArrayList<Object> arlPopulatedETAHistory = objWebUtil.populatedETAHistory(arlOpenTicketsETAHistory);
               objRequest.setAttribute("OpenTicketsETAHistory", arlPopulatedETAHistory);
               int intReopenTicketsCount = objHomeDAO.getReopenTicketsCount(strUserId, objDBConnection);
               objRequest.setAttribute("ReopenTicketsCount", ""+intReopenTicketsCount);

               strForward = AppConstants.SUCCESS;
            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;

   }

   private ArrayList<String> populateTicketsForCommentsUpdate(String strAssignee, String strCurrentWorkingDate, String strPrevWorkingDate, Connection objDBConnection, HomeDAO objHomeDAO) throws Exception
   {
      ArrayList<String> arlTicketsForCommentsUpdate;
      ArrayList<String> arlTicketsForCommentsUpdateActuals = new ArrayList<>();
      ;
      try
      {
         arlTicketsForCommentsUpdate = objHomeDAO.getInProgressOnHoldTickets(strAssignee, strPrevWorkingDate, objDBConnection);
         if (arlTicketsForCommentsUpdate != null && !arlTicketsForCommentsUpdate.isEmpty())
         {
            for (int iCount = 0; iCount < arlTicketsForCommentsUpdate.size(); iCount += 7)
            {
               if (objHomeDAO.isLatestCommentsNotAvailable(arlTicketsForCommentsUpdate.get(iCount),
                       arlTicketsForCommentsUpdate.get(iCount + 2), strCurrentWorkingDate, strPrevWorkingDate, strAssignee, objDBConnection))
               {
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 1));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 2));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 3));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 4));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 5));
                  arlTicketsForCommentsUpdateActuals.add(arlTicketsForCommentsUpdate.get(iCount + 6));
               }
            }
         }

      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      return arlTicketsForCommentsUpdateActuals;
   }
}