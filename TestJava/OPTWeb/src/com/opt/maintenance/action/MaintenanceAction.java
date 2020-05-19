package com.opt.maintenance.action;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
import com.opt.exception.TaskException;
import com.opt.jms.sender.EmailQSender;
import com.opt.maintenance.dao.MaintenanceDAO;
import com.opt.session.valobj.EmailData;
import com.opt.session.valobj.SessionInfo;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

public class MaintenanceAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(MaintenanceAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadAddUser() throws Exception
   {
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try
         {
            objLogger.info(AppConstants.PROCESS_STARTED);
            strForward = AppConstants.SUCCESS;
            objLogger.info(AppConstants.PROCESS_FINISHED);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String addUser() throws Exception
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
            String strFirstName = AppUtil.checkNull(objRequest.getParameter("FirstName"));
            String strLastName = AppUtil.checkNull(objRequest.getParameter("LastName"));
            String strAdminFlag = AppUtil.checkNull(objRequest.getParameter("Admin"));
            String strActiveFlag = AppUtil.checkNull(objRequest.getParameter("Active"));
            String strIsAssigneeFlag = AppUtil.checkNull(objRequest.getParameter("IsAssignee"));
            String strTeam = AppUtil.checkNull(objRequest.getParameter("Team"));

            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            if (objMaintenanceDAO.checkUserIdExists(strUserId, objDBConnection))
            {
               objRequest.setAttribute("RESULT", "USERIDEXISTS");
               ArrayList<String> arlUserDetails = new ArrayList<>();
               arlUserDetails.add(strUserId);
               arlUserDetails.add(strFirstName);
               arlUserDetails.add(strLastName);
               arlUserDetails.add(strAdminFlag);
               arlUserDetails.add(strActiveFlag);
               arlUserDetails.add(strIsAssigneeFlag);
               arlUserDetails.add(strTeam);
               objRequest.setAttribute("USERDETAILS", arlUserDetails);
            }
            else
            {
               objMaintenanceDAO.saveUserDetails(strUserId, strFirstName, strLastName, strAdminFlag, strActiveFlag, strIsAssigneeFlag, strTeam, objDBConnection);
               objRequest.setAttribute("RESULT", "ADDED");

               String[] strToEmailIds = {strUserId};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = AppConstants.MAILLOGINADDSUBJECT;
               String[] strMessageHeader = {strFirstName + " " + strLastName, AppConstants.MAILLOGINADDSUBJECT};
               String[] strMessageBody = {
                       "User Id", strUserId,
                       "Password", AppConstants.DEFAULT_PASSWORD
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));
            }

            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String loadModifyUser() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strUserId = AppUtil.checkNull(objRequest.getParameter("UserId"));
            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            if (strUserId.length() > 0)
            {
               ArrayList<String> arlUserDetails = objMaintenanceDAO.getUserDetails(strUserId, objDBConnection);
               objRequest.setAttribute("USERDETAILS", arlUserDetails);
            }
            ArrayList<String> arlUserList = objMaintenanceDAO.getUserList(objDBConnection);
            objRequest.setAttribute("UserId", strUserId);
            objRequest.setAttribute("USERLIST", arlUserList);
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

   public String modifyUser() throws Exception
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
            String strFirstName = AppUtil.checkNull(objRequest.getParameter("FirstName"));
            String strLastName = AppUtil.checkNull(objRequest.getParameter("LastName"));
            String strAdminFlag = AppUtil.checkNull(objRequest.getParameter("Admin"));
            String strActiveFlag = AppUtil.checkNull(objRequest.getParameter("Active"));
            String strOldActiveFlag = AppUtil.checkNull(objRequest.getParameter("OldActiveFlag"));

            String strIsAssigneeFlag = AppUtil.checkNull(objRequest.getParameter("IsAssignee"));
            String strResetPassword = AppUtil.checkNull(objRequest.getParameter("ResetPassword"));
            String strTeam = AppUtil.checkNull(objRequest.getParameter("Team"));
            strResetPassword = (strResetPassword.equalsIgnoreCase("on")) ? "Y" : "N";

            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            objMaintenanceDAO.modifyUserDetails(strUserId, strFirstName, strLastName, strAdminFlag, strActiveFlag, strIsAssigneeFlag, strResetPassword, strTeam, objDBConnection);
            objRequest.setAttribute("RESULT", "MODIFIED");

            if (strResetPassword.equalsIgnoreCase("Y"))
            {
               String[] strToEmailIds = {strUserId};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = AppConstants.MAILPASSWORDRESETSUBJECT;
               String[] strMessageHeader = {strFirstName + " " + strLastName, AppConstants.MAILPASSWORDRESETSUBJECT};
               String[] strMessageBody = {
                       "User Id", strUserId,
                       "Password", AppConstants.DEFAULT_PASSWORD
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));
            }
            if (!strOldActiveFlag.equalsIgnoreCase(strActiveFlag))
            {
               String strSubject;
               String strTempMessage;
               if (strActiveFlag.equalsIgnoreCase("Y"))
               {
                  strSubject = AppConstants.CREDENTIALSACTIVESUBJECT;
                  strTempMessage = "Your UserId has been activated.";
               }
               else
               {
                  strSubject = AppConstants.CREDENTIALSINACTIVESUBJECT;
                  strTempMessage = "Your UserId has been Deactivated.";
               }
               String[] strToEmailIds = {strUserId};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = strSubject;
               String[] strMessageHeader = {strFirstName + " " + strLastName, strTempMessage};
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, null, false));
            }

            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String loadAccessRights() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strUserId = AppUtil.checkNull(objRequest.getParameter("AccessRightsUserId"));
            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();

            if (strUserId.length() > 0)
            {
               ArrayList<String> arlUserAccessRights = objMaintenanceDAO.getUserAccessRights(strUserId, objDBConnection);
               objRequest.setAttribute("UserAccessRights", arlUserAccessRights);
            }
            ArrayList<String> arlUserList = objMaintenanceDAO.getUserListForAccessRights(objDBConnection);
            objRequest.setAttribute("AccessRightsUserId", strUserId);
            objRequest.setAttribute("USERLIST", arlUserList);
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

   public String saveAccessRights() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strUserId = AppUtil.checkNull(objRequest.getParameter("AccessRightsUserId"));

            String writeAccess = AppUtil.checkNull(objRequest.getParameter("WriteAccess")).equalsIgnoreCase("on") ? "Y" : "N";
            String createTask = AppUtil.checkNull(objRequest.getParameter("CreateTask")).equalsIgnoreCase("on") ? "Y" : "N";
            String createBug = AppUtil.checkNull(objRequest.getParameter("CreateBug")).equalsIgnoreCase("on") ? "Y" : "N";
            String createInternalTicket = AppUtil.checkNull(objRequest.getParameter("CreateInternalTicket")).equalsIgnoreCase("on") ? "Y" : "N";
            String modifyCurrentTickets = AppUtil.checkNull(objRequest.getParameter("ModifyCurrentTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String reopenClosedTickets = AppUtil.checkNull(objRequest.getParameter("ReopenClosedTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String updateLeavePlans = AppUtil.checkNull(objRequest.getParameter("UpdateLeavePlans")).equalsIgnoreCase("on") ? "Y" : "N";
            String holidayCalendar = AppUtil.checkNull(objRequest.getParameter("HolidayCalendar")).equalsIgnoreCase("on") ? "Y" : "N";
            String addUser = AppUtil.checkNull(objRequest.getParameter("AddUser")).equalsIgnoreCase("on") ? "Y" : "N";
            String modifyUser = AppUtil.checkNull(objRequest.getParameter("ModifyUser")).equalsIgnoreCase("on") ? "Y" : "N";
            String accessRights = AppUtil.checkNull(objRequest.getParameter("AccessRights")).equalsIgnoreCase("on") ? "Y" : "N";
            String ticketsStatus = AppUtil.checkNull(objRequest.getParameter("TicketsStatus")).equalsIgnoreCase("on") ? "Y" : "N";
            String currentStatus = AppUtil.checkNull(objRequest.getParameter("CurrentStatus")).equalsIgnoreCase("on") ? "Y" : "N";
            String currentTickets = AppUtil.checkNull(objRequest.getParameter("CurrentTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String closedTickets = AppUtil.checkNull(objRequest.getParameter("ClosedTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String SLAMissedTickets = AppUtil.checkNull(objRequest.getParameter("SLAMissedTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String resourceAllocation = AppUtil.checkNull(objRequest.getParameter("ResourceAllocation")).equalsIgnoreCase("on") ? "Y" : "N";
            String SLAReports = AppUtil.checkNull(objRequest.getParameter("SLAReports")).equalsIgnoreCase("on") ? "Y" : "N";
            String SLAMetrics = AppUtil.checkNull(objRequest.getParameter("SLAMetrics")).equalsIgnoreCase("on") ? "Y" : "N";
            String currentInternalTickets = AppUtil.checkNull(objRequest.getParameter("CurrentInternalTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String closedInternalTickets = AppUtil.checkNull(objRequest.getParameter("ClosedInternalTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String viewLeavePlans = AppUtil.checkNull(objRequest.getParameter("ViewLeavePlans")).equalsIgnoreCase("on") ? "Y" : "N";
            String listOfHolidays = AppUtil.checkNull(objRequest.getParameter("ListOfHolidays")).equalsIgnoreCase("on") ? "Y" : "N";
            String teamContacts = AppUtil.checkNull(objRequest.getParameter("TeamContacts")).equalsIgnoreCase("on") ? "Y" : "N";
            String domainPOC = AppUtil.checkNull(objRequest.getParameter("DomainPOC")).equalsIgnoreCase("on") ? "Y" : "N";
            String createRQABug = AppUtil.checkNull(objRequest.getParameter("CreateRQABug")).equalsIgnoreCase("on") ? "Y" : "N";
            String reopenRQABug = AppUtil.checkNull(objRequest.getParameter("ReopenRQABug")).equalsIgnoreCase("on") ? "Y" : "N";
            String modifyRQATickets = AppUtil.checkNull(objRequest.getParameter("ModifyRQATickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQADailyReport = AppUtil.checkNull(objRequest.getParameter("RQADailyReport")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQACurrentStatus = AppUtil.checkNull(objRequest.getParameter("RQACurrentStatus")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQAMetrics = AppUtil.checkNull(objRequest.getParameter("RQAMetrics")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQASettings = AppUtil.checkNull(objRequest.getParameter("RQASettings")).equalsIgnoreCase("on") ? "Y" : "N";
            String ResourceMatrix = AppUtil.checkNull(objRequest.getParameter("ResourceMatrix")).equalsIgnoreCase("on") ? "Y" : "N";
            String InternalTicketAdmin = AppUtil.checkNull(objRequest.getParameter("InternalTicketAdmin")).equalsIgnoreCase("on") ? "Y" : "N";
            String HighPriorityNotStarted = AppUtil.checkNull(objRequest.getParameter("HighPriorityNotStarted")).equalsIgnoreCase("on") ? "Y" : "N";
            String OOSLANotStarted = AppUtil.checkNull(objRequest.getParameter("OOSLANotStarted")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQAETAReminder = AppUtil.checkNull(objRequest.getParameter("RQAETAReminder")).equalsIgnoreCase("on") ? "Y" : "N";
            String StatusUpdateReminder = AppUtil.checkNull(objRequest.getParameter("StatusUpdateReminder")).equalsIgnoreCase("on") ? "Y" : "N";
            String OpenTicketsPriority = AppUtil.checkNull(objRequest.getParameter("OpenTicketsPriority")).equalsIgnoreCase("on") ? "Y" : "N";
            String TicketsProgressCheck = AppUtil.checkNull(objRequest.getParameter("TicketsProgressCheck")).equalsIgnoreCase("on") ? "Y" : "N";
            String CommonSettings = AppUtil.checkNull(objRequest.getParameter("CommonSettings")).equalsIgnoreCase("on") ? "Y" : "N";
            String UpdateClosedTicket = AppUtil.checkNull(objRequest.getParameter("UpdateClosedTicket")).equalsIgnoreCase("on") ? "Y" : "N";
            String TeamFeedback = AppUtil.checkNull(objRequest.getParameter("TeamFeedback")).equalsIgnoreCase("on") ? "Y" : "N";
            String ReopenedTickets = AppUtil.checkNull(objRequest.getParameter("ReopenedTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String CodeFixReleases = AppUtil.checkNull(objRequest.getParameter("CodeFixReleases")).equalsIgnoreCase("on") ? "Y" : "N";

            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            objMaintenanceDAO.saveAccessRights(strUserId, writeAccess, createTask, createBug, createInternalTicket, modifyCurrentTickets, reopenClosedTickets,
                    updateLeavePlans, holidayCalendar, addUser, modifyUser, accessRights, ticketsStatus,
                    currentStatus, currentTickets, closedTickets, SLAMissedTickets, resourceAllocation,
                    SLAReports, SLAMetrics, currentInternalTickets, closedInternalTickets, viewLeavePlans,
                    listOfHolidays, teamContacts, domainPOC, createRQABug, reopenRQABug, modifyRQATickets,
                    RQADailyReport, RQACurrentStatus, RQAMetrics, RQASettings, ResourceMatrix, InternalTicketAdmin,
                    HighPriorityNotStarted, OOSLANotStarted, RQAETAReminder, StatusUpdateReminder, OpenTicketsPriority, TicketsProgressCheck, CommonSettings,
                    UpdateClosedTicket, TeamFeedback, ReopenedTickets, CodeFixReleases, objDBConnection);
            objRequest.setAttribute("RESULT", "MODIFIED");
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String loadRQASettings() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlRQASettings = new MaintenanceDAO().getRQASettings(objDBConnection);
            objRequest.setAttribute("RQASettings", arlRQASettings);
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

   public String updateRQASettings() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String CurrentRQAPhase = AppUtil.checkNull(objRequest.getParameter("CurrentRQAPhase"));
            String AutoEmail = AppUtil.checkNull(objRequest.getParameter("AutoEmail"));
            String EmailCutOffTime = AppUtil.checkNull(objRequest.getParameter("EmailCutOffTime"));
            String EmailToIds = AppUtil.checkNull(objRequest.getParameter("EmailToIds"));
            if (EmailToIds.length() > 499)
            {
               EmailToIds = EmailToIds.substring(0, 488);
            }
            String EmailCCIds = AppUtil.checkNull(objRequest.getParameter("EmailCCIds"));
            if (EmailCCIds.length() > 499)
            {
               EmailCCIds = EmailCCIds.substring(0, 488);
            }
            String EmailSubject = AppUtil.checkNull(objRequest.getParameter("EmailSubject"));
            String EmailHeader = AppUtil.checkNull(objRequest.getParameter("EmailHeader"));
            String BugResolTriaged = AppUtil.checkNull(objRequest.getParameter("BugResolTriaged"));
            String BugResolLegacyBugFixed = AppUtil.checkNull(objRequest.getParameter("BugResolLegacyBugFixed"));
            String BugResolFeatCodeImpactFixed = AppUtil.checkNull(objRequest.getParameter("BugResolFeatCodeImpactFixed"));
            String BugResolNonFeatCodeImpactFixed = AppUtil.checkNull(objRequest.getParameter("BugResolNonFeatCodeImpactFixed"));
            String RQAPOCMailIds = AppUtil.checkNull(objRequest.getParameter("RQAPOCMailIds"));


            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            objMaintenanceDAO.updateRQASettings(CurrentRQAPhase, AutoEmail, EmailCutOffTime, EmailToIds, EmailCCIds, EmailSubject, EmailHeader, BugResolTriaged,
                    BugResolLegacyBugFixed, BugResolFeatCodeImpactFixed, BugResolNonFeatCodeImpactFixed, RQAPOCMailIds, objDBConnection);
            objRequest.setAttribute("RESULT", "UPDATED");
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String loadResourceMatrix() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strResourceMatrixDate = AppUtil.checkNull(objRequest.getParameter("ResourceMatrixDate"));
            if (strResourceMatrixDate.length() == 0)
            {
               strResourceMatrixDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            ArrayList<String> arlResourceMatrix = objMaintenanceDAO.getResourceMatrix(strResourceMatrixDate, objDBConnection);
            ArrayList<String> arlResourceCount = objMaintenanceDAO.getResourceCount(strResourceMatrixDate, objDBConnection);
            objRequest.setAttribute("ResourceMatrix", arlResourceMatrix);
            objRequest.setAttribute("ResourceCount", arlResourceCount);
            objRequest.setAttribute("ResourceMatrixDate", strResourceMatrixDate);
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

   public String saveResourceMatrix() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            MaintenanceDAO objMaintenanceDAO = new MaintenanceDAO();
            String strDate = AppUtil.checkNull(objRequest.getParameter("ResourceMatrixDate"));
            objRequest.setAttribute("ResourceMatrixDate", strDate);
            int intLiveBugsCount = Integer.parseInt(AppUtil.checkNull(objRequest.getParameter("LiveBugsCount")));
            int intRQACount = Integer.parseInt(AppUtil.checkNull(objRequest.getParameter("RQACount")));
            int intFeatureCount = Integer.parseInt(AppUtil.checkNull(objRequest.getParameter("FeatureCount")));
            int intQACount = Integer.parseInt(AppUtil.checkNull(objRequest.getParameter("QACount")));
            objMaintenanceDAO.saveResourceCount(strDate, intLiveBugsCount, intRQACount, intFeatureCount, intQACount, objDBConnection);

            int intResourceMatrixCount = Integer.parseInt(AppUtil.checkNull(objRequest.getParameter("ResourceMatrixCount")));
            if (intResourceMatrixCount > 0)
            {
               String strResourceId;
               String strLiveBugs;
               String strComments;
               for (int iCount = 0; iCount < intResourceMatrixCount; iCount++)
               {
                  strResourceId = AppUtil.checkNull(objRequest.getParameter("ResourceId" + iCount));
                  strLiveBugs = AppUtil.checkNull(objRequest.getParameter("LiveBugsFlag" + iCount)).equalsIgnoreCase("on") ? "Y" : "N";
                  strComments = AppUtil.checkNull(objRequest.getParameter("Comments" + iCount));
                  objMaintenanceDAO.saveResourceMatrix(strResourceId, strDate, strLiveBugs, strComments, objDBConnection);
               }
            }

            objRequest.setAttribute("RESULT", "MODIFIED");
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String loadCommonSettings() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlCommonSettings = new MaintenanceDAO().getCommonSettings(objDBConnection);
            objRequest.setAttribute("CommonSettings", arlCommonSettings);
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

   public String saveCommonSettings() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String OOSLAHighLight = AppUtil.checkNull(objRequest.getParameter("OOSLAHighLight")).equalsIgnoreCase("on") ? "Y" : "N";
            String DBBackup = AppUtil.checkNull(objRequest.getParameter("DBBackup")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETESReminder = AppUtil.checkNull(objRequest.getParameter("ETESReminder")).equalsIgnoreCase("on") ? "Y" : "N";
            String HighPriorityNotStarted = AppUtil.checkNull(objRequest.getParameter("HighPriorityNotStarted")).equalsIgnoreCase("on") ? "Y" : "N";
            String OOSLANotStarted = AppUtil.checkNull(objRequest.getParameter("OOSLANotStarted")).equalsIgnoreCase("on") ? "Y" : "N";
            String RQAETAReminder = AppUtil.checkNull(objRequest.getParameter("RQAETAReminder")).equalsIgnoreCase("on") ? "Y" : "N";
            String StatusUpdateReminder = AppUtil.checkNull(objRequest.getParameter("StatusUpdateReminder")).equalsIgnoreCase("on") ? "Y" : "N";
            String OpenTicketsPriority = AppUtil.checkNull(objRequest.getParameter("OpenTicketsPriority")).equalsIgnoreCase("on") ? "Y" : "N";
            String TicketsProgressCheck = AppUtil.checkNull(objRequest.getParameter("TicketsProgressCheck")).equalsIgnoreCase("on") ? "Y" : "N";

            String ETAForBugs = AppUtil.checkNull(objRequest.getParameter("ETAForBugs")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETAForCriticalTasks = AppUtil.checkNull(objRequest.getParameter("ETAForCriticalTasks")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETAForMajorTasks = AppUtil.checkNull(objRequest.getParameter("ETAForMajorTasks")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETAForTopAgeingTasks = AppUtil.checkNull(objRequest.getParameter("ETAForTopAgeingTasks")).equalsIgnoreCase("on") ? "Y" : "N";
            String RescheduleForCrossedETA = AppUtil.checkNull(objRequest.getParameter("RescheduleForCrossedETA")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETAForInternalTickets = AppUtil.checkNull(objRequest.getParameter("ETAForInternalTickets")).equalsIgnoreCase("on") ? "Y" : "N";
            String ETAForRQABugs = AppUtil.checkNull(objRequest.getParameter("ETAForRQABugs")).equalsIgnoreCase("on") ? "Y" : "N";
            String TopAgeingThreshold = AppUtil.checkNull(objRequest.getParameter("TopAgeingThreshold"));
            String ETARescheduleMax = AppUtil.checkNull(objRequest.getParameter("ETARescheduleMax"));
            String DailyStatus = AppUtil.checkNull(objRequest.getParameter("DailyStatus")).equalsIgnoreCase("on") ? "Y" : "N";

            new MaintenanceDAO().saveCommonSettings(OOSLAHighLight, DBBackup, ETESReminder,
                    HighPriorityNotStarted, OOSLANotStarted, RQAETAReminder, StatusUpdateReminder, OpenTicketsPriority, TicketsProgressCheck,
                    ETAForBugs, ETAForCriticalTasks, ETAForMajorTasks, ETAForTopAgeingTasks, RescheduleForCrossedETA, ETAForInternalTickets, ETAForRQABugs, TopAgeingThreshold,
                    ETARescheduleMax, DailyStatus, objDBConnection);
            objRequest.setAttribute("RESULT", "MODIFIED");

            ArrayList<String> arlStartUpValues = new CommonDAO().getStartUpValues(objDBConnection);
            if (arlStartUpValues != null && !arlStartUpValues.isEmpty())
            {
               AppSettings.setOooslaHighlight(arlStartUpValues.get(0).equalsIgnoreCase("Y"));
               AppSettings.setDBBackup(arlStartUpValues.get(1).equalsIgnoreCase("Y"));
               AppSettings.setETESReminder(arlStartUpValues.get(2).equalsIgnoreCase("Y"));
               AppSettings.setHighPriorityNotStarted(arlStartUpValues.get(3).equalsIgnoreCase("Y"));
               AppSettings.setOOSLANotStarted(arlStartUpValues.get(4).equalsIgnoreCase("Y"));
               AppSettings.setRQAETAReminder(arlStartUpValues.get(5).equalsIgnoreCase("Y"));
               AppSettings.setStatusUpdateReminder(arlStartUpValues.get(6).equalsIgnoreCase("Y"));
               AppSettings.setOpenTicketsPriority(arlStartUpValues.get(7).equalsIgnoreCase("Y"));
               AppSettings.setTicketsProgressCheck(arlStartUpValues.get(8).equalsIgnoreCase("Y"));
               AppSettings.setETABugs(arlStartUpValues.get(9).equalsIgnoreCase("Y"));
               AppSettings.setETACriticalTasks(arlStartUpValues.get(10).equalsIgnoreCase("Y"));
               AppSettings.setETAMajorTasks(arlStartUpValues.get(11).equalsIgnoreCase("Y"));
               AppSettings.setETATopAgeingTasks(arlStartUpValues.get(12).equalsIgnoreCase("Y"));
               AppSettings.setRescheduleCrossedETA(arlStartUpValues.get(13).equalsIgnoreCase("Y"));
               AppSettings.setETAInteranlTickets(arlStartUpValues.get(14).equalsIgnoreCase("Y"));
               AppSettings.setETARQABugs(arlStartUpValues.get(15).equalsIgnoreCase("Y"));
               AppSettings.setTopAgeingThreshold(Integer.parseInt(arlStartUpValues.get(16)));
               AppSettings.setETARescheduleMax(Integer.parseInt(arlStartUpValues.get(17)));
               AppSettings.setDailyStatus(arlStartUpValues.get(18).equalsIgnoreCase("Y"));
            }
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

}