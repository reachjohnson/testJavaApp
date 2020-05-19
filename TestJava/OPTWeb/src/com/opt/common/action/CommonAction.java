package com.opt.common.action;

import com.opt.base.dao.BaseDAO;
import com.opt.charts.SlidingBarChart;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.tickets.util.TicketHistory;
import com.opt.util.AppConstants;
import com.opt.session.valobj.SessionInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opt.util.AppUtil;
import com.opt.util.CalculateBusinessHours;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(CommonAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadTeamContacts() throws Exception
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
            ArrayList<String> arlTeamContacts = new CommonDAO().getTeamContacts(objDBConnection);
            objRequest.setAttribute("TeamContacts", arlTeamContacts);
            strForward = AppConstants.SUCCESS;
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

   public String loadDomainContacts() throws Exception
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
            CommonDAO objCommonDAO = new CommonDAO();
            ArrayList<String> arlDomainPOCNames = objCommonDAO.getDomainPOCNames(objDBConnection);
            ArrayList<String> arlDomainPOCDetailsTemp;
            ArrayList<Object> arlDomainPOCDetails = new ArrayList<>();
            if (arlDomainPOCNames != null && !arlDomainPOCNames.isEmpty())
            {
               for (int intCount = 0; intCount < arlDomainPOCNames.size(); intCount += 2)
               {
                  arlDomainPOCDetailsTemp = objCommonDAO.getDomainPOCDetails(arlDomainPOCNames.get(intCount), objDBConnection);
                  arlDomainPOCDetails.add(arlDomainPOCNames.get(intCount));
                  arlDomainPOCDetails.add(arlDomainPOCDetailsTemp);
                  arlDomainPOCDetails.add(arlDomainPOCNames.get(intCount + 1));
               }
            }
            objRequest.setAttribute("DomainPOCDetails", arlDomainPOCDetails);
            strForward = AppConstants.SUCCESS;
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

   public String loadSearchTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strSearchTicketId = AppUtil.checkNull(objRequest.getParameter("searchticktid"));
            objRequest.setAttribute("SearchTicketId", strSearchTicketId);
            CommonDAO objCommonDAO = new CommonDAO();
            String strRefNo = objCommonDAO.getSearchLiveTicketRefNo(strSearchTicketId, objDBConnection);

            if (strRefNo != null && strRefNo.length() > 0)
            {
               objRequest.setAttribute("TicketDetailsAvailable", "AVAILABLE");
               TicketsDAO objTicketsDAO = new TicketsDAO();
               ArrayList<String> arlViewTicketDetails = objTicketsDAO.getTicketDetailsForView(strRefNo, strSearchTicketId, objDBConnection);
               TicketHistory objTicketHistory = new TicketHistory();
               ArrayList<String> arlTicketHistory = objTicketHistory.getTicketHistory(strRefNo, strSearchTicketId, objDBConnection);
               ArrayList<String> arlTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strRefNo, strSearchTicketId, objDBConnection);
               ArrayList<String> arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefNo, strSearchTicketId, objDBConnection);
               objRequest.setAttribute("ViewTicketDetails", arlViewTicketDetails);
               objRequest.setAttribute("TicketHistory", arlTicketHistory);
               objRequest.setAttribute("TotalDaysSpent", objTicketHistory.getTotalDaysSpent());
               objRequest.setAttribute("TicketActivityHistory", arlTicketActivityHistory);
               objRequest.setAttribute("CommentsHistory", arlCommentsHistory);

               String strTriagingRefNo = objTicketsDAO.getTriagingRefNo(strRefNo, strSearchTicketId, objDBConnection);
               if (strTriagingRefNo.length() > 0)
               {
                  ArrayList<String> arlTriagingTicketDetails = objTicketsDAO.getTicketDetailsForView(strTriagingRefNo, strSearchTicketId, objDBConnection);
                  TicketHistory objTriagingTicketHistory = new TicketHistory();
                  ArrayList<String> arlTriagingTicketHistory = objTriagingTicketHistory.getTicketHistory(strTriagingRefNo, strSearchTicketId, objDBConnection);
                  ArrayList<String> arlTriagingTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strTriagingRefNo, strSearchTicketId, objDBConnection);
                  ArrayList<String> arlTriagingCommentsHistory = objTicketsDAO.getCommentsHistory(strTriagingRefNo, strSearchTicketId, objDBConnection);
                  objRequest.setAttribute("TriagingTicketDetails", arlTriagingTicketDetails);
                  objRequest.setAttribute("TriagingTicketHistory", arlTriagingTicketHistory);
                  objRequest.setAttribute("TriagingTotalDaysSpent", objTriagingTicketHistory.getTotalDaysSpent());
                  objRequest.setAttribute("TriagingTicketActivityHistory", arlTriagingTicketActivityHistory);
                  objRequest.setAttribute("TriagingCommentsHistory", arlTriagingCommentsHistory);
               }
            }
            strForward = AppConstants.SUCCESS;
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

   public String ClosureTrendDummy() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, false);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try
         {
            strForward = AppConstants.SUCCESS;
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

   public String ClosureTrend() throws Exception
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
            String strTeamSummary = AppUtil.checkNull(objRequest.getParameter("hidTeamSummary"));
            SimpleDateFormat objDisplayFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
            SimpleDateFormat objDateFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            String strCurrentDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            Calendar objCalendar = Calendar.getInstance();
            Date objToDate = objDateFormat.parse(strCurrentDate);
            objCalendar.setTime(objToDate);
            objCalendar.add(Calendar.MONTH, -3);
            Date objFromDate = objCalendar.getTime();
            ArrayList<Object> arlWeeklyClosureTrend = new ArrayList<>();
            ArrayList<Object> arlWeekAndMonthDates = AppUtil.getWeekAndMonthDates(objDateFormat.format(objFromDate), objDateFormat.format(objToDate), AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            ArrayList arlWeekFromToDates = (ArrayList) arlWeekAndMonthDates.get(2);
            String strAssignee = "";
            String strAssigneeName = "";
            if (strTeamSummary.equalsIgnoreCase("YES"))
            {
               strAssignee = AppConstants.ALL_ASSIGNEES;
               strAssigneeName = "Team";
            }
            else
            {
               strAssignee = objSessionInfo.getUserInfo().getUserId();
               strAssigneeName = objSessionInfo.getUserInfo().getUserName();
            }
            objRequest.setAttribute("AssigneeName", strAssigneeName);
            if (arlWeekFromToDates != null && !arlWeekFromToDates.isEmpty())
            {
               Date objWeekFromDate;
               Date objWeekToDate;
               int intClosedCount = 0;
               int intClosedRQACount = 0;
               int intClosedITCount = 0;
               CommonDAO objCommonDAO = new CommonDAO();
               String strWeekDisplayDate = "";
               for (int iCount1 = 0; iCount1 < arlWeekFromToDates.size(); iCount1 += 2)
               {
                  objWeekFromDate = (Date) arlWeekFromToDates.get(iCount1);
                  objWeekToDate = (Date) arlWeekFromToDates.get(iCount1 + 1);
                  Calendar objTemp = Calendar.getInstance();
                  objTemp.setTime(objWeekFromDate);
                  objTemp.add(Calendar.DATE, 4);
                  strWeekDisplayDate = objDisplayFormat.format(objTemp.getTime());
                  strWeekDisplayDate = strWeekDisplayDate.substring(0, 2) + "<br>" + strWeekDisplayDate.substring(3, 6) + "<br>" + strWeekDisplayDate.substring(9, 11);
                  intClosedCount = objCommonDAO.getClosedTicketsForGivenDates(strAssignee, objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objDBConnection);
                  intClosedRQACount = objCommonDAO.getClosedRQABugsForGivenDates(strAssignee, objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objDBConnection);
                  intClosedITCount = objCommonDAO.getClosedITForGivenDates(strAssignee, objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objDBConnection);
                  arlWeeklyClosureTrend.add(Double.valueOf(intClosedCount));
                  arlWeeklyClosureTrend.add("Live Tickets");
                  arlWeeklyClosureTrend.add(strWeekDisplayDate.replaceAll("<br>", " "));

                  arlWeeklyClosureTrend.add(Double.valueOf(intClosedRQACount));
                  arlWeeklyClosureTrend.add("RQA Bugs");
                  arlWeeklyClosureTrend.add(strWeekDisplayDate.replaceAll("<br>", " "));

                  arlWeeklyClosureTrend.add(Double.valueOf(intClosedITCount));
                  arlWeeklyClosureTrend.add("Internal Tickets");
                  arlWeeklyClosureTrend.add(strWeekDisplayDate.replaceAll("<br>", " "));
               }
               String strClosureTrendChartPath = new SlidingBarChart().drawSlidingBarChart("ClosureTrend", objSessionInfo.getUserInfo().getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Weekly Closure - 3 Months Trend", "", "Count", arlWeeklyClosureTrend);
               objRequest.setAttribute("ClosureTrendChartPath", strClosureTrendChartPath);

            }
            strForward = AppConstants.SUCCESS;
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

   public String loadSearchCriteria() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try
         {
            HttpSession objSession = objRequest.getSession(false);
            SessionInfo objSessionInfo = (SessionInfo) objSession.getAttribute(AppConstants.SESSION_DATA);
            strForward = AppConstants.SUCCESS;
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

   public String searchLiveTickets() throws Exception
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
            String strTicketDescription = AppUtil.checkNull(objRequest.getParameter("TicketDescription"));
            String strTicketRootCause = AppUtil.checkNull(objRequest.getParameter("TicketRootCause"));
            String strTicketComments = AppUtil.checkNull(objRequest.getParameter("TicketComments"));
            String strTicketResolution = AppUtil.checkNull(objRequest.getParameter("TicketResolution"));
            ArrayList<String> arlSearchedLiveTickets = new CommonDAO().getSearchedLveTickets(strTicketDescription, strTicketRootCause, strTicketComments,
                    strTicketResolution, objDBConnection);
            objRequest.setAttribute("TicketsSearched", "YES");
            objRequest.setAttribute("SearchedLiveTickets", arlSearchedLiveTickets);
            objRequest.setAttribute("TicketDescription", strTicketDescription);
            objRequest.setAttribute("TicketRootCause", strTicketRootCause);
            objRequest.setAttribute("TicketComments", strTicketComments);
            objRequest.setAttribute("TicketResolution", strTicketResolution);
            strForward = AppConstants.SUCCESS;
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

   public String loadTeamFeedback() throws Exception
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
            CommonDAO objCommonDAO = new CommonDAO();
            ArrayList<String> arlFeedbackCategory = objCommonDAO.getFeedbackCategory(objDBConnection);
            ArrayList<String> arlResourceForFeedback = objCommonDAO.getResourceForFeedback(objDBConnection);
            objRequest.setAttribute("FeedbackCategory", arlFeedbackCategory);
            objRequest.setAttribute("ResourceNames", arlResourceForFeedback);
            strForward = AppConstants.SUCCESS;
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

   public String UpdateTeamFeedback() throws Exception
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
            String strUserId = AppUtil.checkNull(objRequest.getParameter("UserId"));
            String strFeedbackDate = AppUtil.checkNull(objRequest.getParameter("FeedbackDate"));
            String strFeedbackCategory = AppUtil.checkNull(objRequest.getParameter("FeedbackCategory"));
            String strFeedbackComments = AppUtil.checkNull(objRequest.getParameter("FeedbackComments"));
            if (strFeedbackComments != null && strFeedbackComments.length() > 2990)
            {
               strFeedbackComments = strFeedbackComments.substring(0, 2990);
            }
            CommonDAO objCommonDAO = new CommonDAO();
            objCommonDAO.UpdateTeamFeedback(strUserId, strFeedbackDate, strFeedbackCategory, strFeedbackComments, objSessionInfo.getUserInfo().getUserId(),
                    objDBConnection);

            String[] strToEmailIds = {strUserId};
            List<String> lstCCMailIds = new ArrayList<>();
            lstCCMailIds.add(objSessionInfo.getUserInfo().getUserId());

            if (strFeedbackCategory.equalsIgnoreCase(AppConstants.FEEDBACK_APPRECIATION))
            {
               lstCCMailIds.add(AppConstants.MSO_TEAM_DL);
            }
            String[] strCCMailIds = lstCCMailIds.toArray(new String[lstCCMailIds.size()]);
            String strMailSubject = "";
            String strMailHeader = "";
            if (strFeedbackCategory.equalsIgnoreCase(AppConstants.FEEDBACK_APPRECIATION))
            {
               strMailSubject = AppConstants.APPRECIATION_EMAIL + " - " + AppUtil.convertDate(strFeedbackDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
               strMailHeader = AppConstants.APPRECIATION_MAIL_HEADER;
            }
            else
            {
               strMailSubject = AppConstants.FEEDBACK_EMAIL + " - " + AppUtil.convertDate(strFeedbackDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
               strMailHeader = AppConstants.FEEDBACK_EMAIL_HEADER;
            }
            String strUserName = objCommonDAO.getResourceName(strUserId, objDBConnection);
            String[] strMessageHeader = {strUserName, strMailHeader};
            String[] strMessageBody = {
                    "Feedback Date", AppUtil.convertDate(strFeedbackDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY),
                    "Feedback Category", strFeedbackCategory,
                    "Feedback Comments", strFeedbackComments,
                    "Feedback By", objSessionInfo.getUserInfo().getUserName()
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));

            objRequest.setAttribute("RESULT", "SUCCESS");
            strForward = AppConstants.SUCCESS;
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

   public String getTeamFeedbackDetails() throws Exception
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
            String strFeedbackFromDate = AppUtil.checkNull(objRequest.getParameter("FeedbackFromDate"));
            String strFeedbackToDate = AppUtil.checkNull(objRequest.getParameter("FeedbackToDate"));
            String strResource = AppUtil.checkNull(objRequest.getParameter("Resource"));
            ArrayList<String> arlTeamFeedbacks = new CommonDAO().getTeamFeedbackDetails(strResource, strFeedbackFromDate, strFeedbackToDate, objDBConnection);
            objRequest.setAttribute("TeamFeedbacks", arlTeamFeedbacks);
            objRequest.setAttribute("FeedbackFromDate", strFeedbackFromDate);
            objRequest.setAttribute("FeedbackToDate", strFeedbackToDate);
            objRequest.setAttribute("Resource", strResource);

            strForward = AppConstants.SUCCESS;
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

}