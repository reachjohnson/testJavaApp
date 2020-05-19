package com.opt.tickets.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.session.valobj.SessionInfo;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.tickets.util.TicketHistory;
import com.opt.util.*;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TicketsAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(TicketsAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadCreateTask() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
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

   public String loadCreateBug() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
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

   public String saveTask() throws Exception
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
            String strTaskId = AppUtil.checkNull(objRequest.getParameter("TaskId"));
            String strTaskDescription = AppUtil.checkNull(objRequest.getParameter("TaskDescription"));
            if (strTaskDescription.length() > 490)
            {
               strTaskDescription = strTaskDescription.substring(0, 490);
            }
            String strTaskPriority = AppUtil.checkNull(objRequest.getParameter("TaskPriority"));
            String strTaskType = AppUtil.checkNull(objRequest.getParameter("TaskType"));
            String strTaskModule = AppUtil.checkNull(objRequest.getParameter("TaskModule"));
            String strTaskRaisedBy = AppUtil.checkNull(objRequest.getParameter("TaskRaisedBy"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strCreateTaskRecDate = AppUtil.checkNull(objRequest.getParameter("CreateTaskRecDate"));
            String strReceivedHour = AppUtil.checkNull(objRequest.getParameter("ReceivedHour"));
            String strReceivedMinute = AppUtil.checkNull(objRequest.getParameter("ReceivedMinute"));
            String strReceived24Hours = AppUtil.checkNull(objRequest.getParameter("Received24Hours"));
            String strTaskCreatedDate = AppUtil.checkNull(objRequest.getParameter("CreatedTaskDate"));
            StringBuilder sbReceivedDate = new StringBuilder(strCreateTaskRecDate);
            sbReceivedDate.append(AppConstants.SPACE).append(strReceivedHour).append(":").append(strReceivedMinute).append(AppConstants.SPACE).append(strReceived24Hours);
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlTaskDetails = new ArrayList<>();
            if (objTicketsDAO.checkTicketExists(strTaskId, objDBConnection))
            {
               arlTaskDetails.add(strTaskId);
               arlTaskDetails.add(strTaskDescription);
               arlTaskDetails.add(strTaskPriority);
               arlTaskDetails.add(strTaskType);
               arlTaskDetails.add(strTaskModule);
               arlTaskDetails.add(strAssignee);
               arlTaskDetails.add(strCreateTaskRecDate);
               arlTaskDetails.add(strReceivedHour);
               arlTaskDetails.add(strReceivedMinute);
               arlTaskDetails.add(strReceived24Hours);
               arlTaskDetails.add(strTaskRaisedBy);
               arlTaskDetails.add(strTaskCreatedDate);

               objRequest.setAttribute("RESULT", "TASKEXISTS");
               objRequest.setAttribute("TaskDetails", arlTaskDetails);
            }
            else
            {
               CommonDAO objCommonDAO = new CommonDAO();
               String strPlannedEndDate = AppUtil.getBusinessDate(sbReceivedDate.toString(), AppConstants.TASK_SLA_DAYS, objCommonDAO.getHolidayCalendarForCalc(objDBConnection));
               String strAssigneeName = objCommonDAO.getAssigneeName(strAssignee, objDBConnection);

               objTicketsDAO.saveTask(strTaskId, strTaskDescription, strTaskPriority, strTaskType,
                       strTaskModule, strAssignee, sbReceivedDate.toString(), strPlannedEndDate, strTaskRaisedBy, strTaskCreatedDate, strAssigneeName, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
               objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

               String[] strToEmailIds = {strAssignee};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = "New Task Created : " + strTaskId;
               String[] strMessageHeader = {strAssigneeName, "New Task Created by " + objSessionInfo.getUserInfo().getUserName()};
               String[] strMessageBody = {
                       objCommonDAO.getLiveTicketRefNo(strTaskId, objDBConnection), strTaskId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
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

   public String saveBug() throws Exception
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
            String strBugId = AppUtil.checkNull(objRequest.getParameter("BugId"));
            String strBugDescription = AppUtil.checkNull(objRequest.getParameter("BugDescription"));
            if (strBugDescription.length() > 490)
            {
               strBugDescription = strBugDescription.substring(0, 490);
            }
            String strBugPriority = AppUtil.checkNull(objRequest.getParameter("BugPriority"));
            String strBugType = AppUtil.checkNull(objRequest.getParameter("BugType"));
            String strBugModule = AppUtil.checkNull(objRequest.getParameter("BugModule"));
            String strBugRaisedBy = AppUtil.checkNull(objRequest.getParameter("BugRaisedBy"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strCreateBugRecDate = AppUtil.checkNull(objRequest.getParameter("CreateBugRecDate"));
            String strReceivedHour = AppUtil.checkNull(objRequest.getParameter("ReceivedHour"));
            String strReceivedMinute = AppUtil.checkNull(objRequest.getParameter("ReceivedMinute"));
            String strReceived24Hours = AppUtil.checkNull(objRequest.getParameter("Received24Hours"));
            String strBugCreatedDate = AppUtil.checkNull(objRequest.getParameter("CreatedBugDate"));
            StringBuilder sbReceivedDate = new StringBuilder(strCreateBugRecDate);
            sbReceivedDate.append(AppConstants.SPACE).append(strReceivedHour).append(":").append(strReceivedMinute).append(AppConstants.SPACE).append(strReceived24Hours);
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlBugDetails = new ArrayList<>();
            if (objTicketsDAO.checkTicketExists(strBugId, objDBConnection))
            {
               arlBugDetails.add(strBugId);
               arlBugDetails.add(strBugDescription);
               arlBugDetails.add(strBugPriority);
               arlBugDetails.add(strBugType);
               arlBugDetails.add(strBugModule);
               arlBugDetails.add(strAssignee);
               arlBugDetails.add(strCreateBugRecDate);
               arlBugDetails.add(strReceivedHour);
               arlBugDetails.add(strReceivedMinute);
               arlBugDetails.add(strReceived24Hours);
               arlBugDetails.add(strBugRaisedBy);
               arlBugDetails.add(strBugCreatedDate);

               objRequest.setAttribute("RESULT", "BUGEXISTS");
               objRequest.setAttribute("BugDetails", arlBugDetails);
            }
            else
            {
               CommonDAO objCommonDAO = new CommonDAO();
               String strPlannedEndDate = AppUtil.getBusinessDate(sbReceivedDate.toString(), AppConstants.EXISTING_BUG_SLA_DAYS, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
               String strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);
               objTicketsDAO.saveBug(strBugId, strBugDescription, strBugPriority, strBugType,
                       strBugModule, strAssignee, sbReceivedDate.toString(), strPlannedEndDate, strBugRaisedBy, strBugCreatedDate, strAssigneeName, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
               objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

               String[] strToEmailIds = {strAssignee};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = "New Bug Created : " + strBugId;
               String[] strMessageHeader = {strAssigneeName, "New Bug Created by " + objSessionInfo.getUserInfo().getUserName()};
               String[] strMessageBody = {
                       objCommonDAO.getLiveTicketRefNo(strBugId, objDBConnection), strBugId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
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

   public String loadUpdateTask() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTaskId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            ArrayList<String> arlTaskDetails = new TicketsDAO().getTicketDetailsForUpdate(strRefno, strTaskId, objDBConnection);
            objRequest.setAttribute("TaskDetails", arlTaskDetails);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
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

   public String loadUpdateBug() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strBugId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            ArrayList<String> arlBugDetails = new TicketsDAO().getTicketDetailsForUpdate(strRefno, strBugId, objDBConnection);
            objRequest.setAttribute("BugDetails", arlBugDetails);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
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

   public String updateTask() throws Exception
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

            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTaskId = AppUtil.checkNull(objRequest.getParameter("hidTaskId"));
            String strTaskDescription = AppUtil.checkNull(objRequest.getParameter("TaskDescription"));
            if (strTaskDescription.length() > 490)
            {
               strTaskDescription = strTaskDescription.substring(0, 490);
            }
            String strTaskPriority = AppUtil.checkNull(objRequest.getParameter("TaskPriority"));
            String strTaskType = AppUtil.checkNull(objRequest.getParameter("TaskType"));
            String strTaskModule = AppUtil.checkNull(objRequest.getParameter("TaskModule"));
            String strTaskRaisedBy = AppUtil.checkNull(objRequest.getParameter("TaskRaisedBy"));
            String strTaskETADate = AppUtil.checkNull(objRequest.getParameter("TaskETADate"));
            String strReasonForUpdate = AppUtil.checkNull(objRequest.getParameter("ReasonForUpdate"));
            if (strReasonForUpdate.length() > 4900)
            {
               strReasonForUpdate = strReasonForUpdate.substring(0, 4900);
            }


            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.updateTask(strRefNo, strTaskId, strTaskId, strTaskDescription, strTaskPriority, strTaskType,
                    strTaskModule, strTaskRaisedBy, strTaskETADate, strReasonForUpdate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
            objRequest.setAttribute("TaskId", strTaskId);

            CommonDAO objCommonDAO = new CommonDAO();
            String strTaskAssignee = objCommonDAO.getTicketAssignee(strRefNo, strTaskId, objDBConnection);

            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefNo, strTaskId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strTaskAssignee};
            String strMailSubject = "Task Updated - " + strTaskId;
            String[] strMessageHeader = {strCreatedByName, "Task Updated By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
               strRefNo, strTaskId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));

            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
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

   public String updateBug() throws Exception
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

            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strBugId = AppUtil.checkNull(objRequest.getParameter("hidBugId"));
            String strBugDescription = AppUtil.checkNull(objRequest.getParameter("BugDescription"));
            if (strBugDescription.length() > 490)
            {
               strBugDescription = strBugDescription.substring(0, 490);
            }
            String strBugPriority = AppUtil.checkNull(objRequest.getParameter("BugPriority"));
            String strBugType = AppUtil.checkNull(objRequest.getParameter("BugType"));
            String strBugModule = AppUtil.checkNull(objRequest.getParameter("BugModule"));
            String strBugRaisedBy = AppUtil.checkNull(objRequest.getParameter("BugRaisedBy"));
            String strBugETADate = AppUtil.checkNull(objRequest.getParameter("BugETADate"));
            String strReasonForUpdate = AppUtil.checkNull(objRequest.getParameter("ReasonForUpdate"));
            if (strReasonForUpdate.length() > 4900)
            {
               strReasonForUpdate = strReasonForUpdate.substring(0, 4900);
            }

            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.updateBug(strRefNo, strBugId, strBugId, strBugDescription, strBugPriority, strBugType,
                    strBugModule, strBugRaisedBy, strBugETADate, strReasonForUpdate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
            objRequest.setAttribute("BugId", strBugId);

            CommonDAO objCommonDAO = new CommonDAO();
            String strBugAssignee = objCommonDAO.getTicketAssignee(strRefNo, strBugId, objDBConnection);

            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefNo, strBugId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strBugAssignee};
            String strMailSubject = "Bug Updated - " + strBugId;
            String[] strMessageHeader = {strCreatedByName, "Bug Updated By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strBugId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));

            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
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

   public String deleteTicket() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));

            CommonDAO objCommonDAO = new CommonDAO();
            String strTicketAssignee = objCommonDAO.getTicketAssignee(strRefno, strTicketId, objDBConnection);
            String strTicketAssigneeName = objCommonDAO.getAssigneeName(strTicketAssignee, objDBConnection);

            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefno, strTicketId, objDBConnection);

            TicketsDAO objTicketsDAO = new TicketsDAO();
            String strTicketDescription = objTicketsDAO.getTicketDesc(strRefno, strTicketId, objDBConnection);
            objTicketsDAO.deleteTicket(strRefno, strTicketId, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", "TICKETDELETED");

            String[] strToEmailIds = {strTicketAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strCreatedBy};
            String strMailSubject = "Ticket Deleted - " + strTicketId;
            String[] strMessageHeader = {strTicketAssigneeName, "Ticket Deleted By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    AppConstants.TICKET_ID_LABEL, strTicketId,
                    AppConstants.TICKET_DESC_LABEL, strTicketDescription
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));

            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            if (strFromPage.equalsIgnoreCase("HomePage"))
            {
               strForward = "homePage";
            }
            else if (strFromPage.equalsIgnoreCase("ModifyCurrentTickets"))
            {
               strForward = "loadModifyCurrentTickets";
            }
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);

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

   public String reAssignTicketDummy() throws Exception
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

   public String loadReAssignTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            ArrayList<String> arlOpenTicketIdDescAssigneeStatus = new TicketsDAO().getOpenTicketIdDescAssigneeStatus(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("OpenTicketIdDescAssigneeStatus", arlOpenTicketIdDescAssigneeStatus);

            objRequest.setAttribute("Assignees", arlAssignees);
            objRequest.setAttribute("FromPage", strFromPage);
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

   public String reAssignTicket() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strNewAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strCommentsForReAssign = AppUtil.checkNull(objRequest.getParameter("commentsforreassign"));
            if (strCommentsForReAssign.length() > 4990)
            {
               strCommentsForReAssign = strCommentsForReAssign.substring(0, 4990);
            }

            CommonDAO objCommonDAO = new CommonDAO();
            String strTicketOriginalAssignee = objCommonDAO.getTicketAssignee(strRefno, strTicketId, objDBConnection);
            String strTicketOriginalAssigneeName = objCommonDAO.getAssigneeName(strTicketOriginalAssignee, objDBConnection);
            String strNewAssigneeName = objCommonDAO.getAssigneeName(strNewAssignee, objDBConnection);
            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefno, strTicketId, objDBConnection);

            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.reAssignTicket(strRefno, strTicketId, strNewAssignee, strCommentsForReAssign, objSessionInfo.getUserInfo().getUserId(), strNewAssigneeName, objDBConnection);
            String strTicketDescription = objTicketsDAO.getTicketDesc(strRefno, strTicketId, objDBConnection);

            String[] strToEmailIds = {strNewAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strTicketOriginalAssignee, strCreatedBy};
            String strMailSubject = "Ticket Re-Assigned To You From " + strTicketOriginalAssigneeName + " : " + strTicketId;
            String[] strMessageHeader = {strNewAssigneeName, "Ticket Re-Assigned To You From " + strTicketOriginalAssigneeName + " By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefno, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));

            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);

            objRequest.setAttribute("RESULT", "TICKETREASSIGNED");
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

   public String startProgressTicket() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            new TicketsDAO().startProgressTicket(strRefno, strTicketId, objSessionInfo.getUserInfo().getUserId(),
                    objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", "TICKETINPROGRESS");
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

   public String stopProgressTicketDummy() throws Exception
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

   public String loadStopProgressTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            ArrayList<String> arlOpenTicketIdDescAssigneeStatus = new TicketsDAO().getOpenTicketIdDescAssigneeStatus(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("OpenTicketIdDescAssigneeStatus", arlOpenTicketIdDescAssigneeStatus);

            objRequest.setAttribute("FromPage", strFromPage);
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


   public String stopProgressTicket() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strCommentsForStopProgress = AppUtil.checkNull(objRequest.getParameter("commentsforstopprogress"));
            if (strCommentsForStopProgress.length() > 4990)
            {
               strCommentsForStopProgress = strCommentsForStopProgress.substring(0, 4990);
            }
            TicketsDAO objTicketsDAO = new TicketsDAO();
            new TicketsDAO().stopProgressTicket(strRefno, strTicketId, strCommentsForStopProgress, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            objRequest.setAttribute("RESULT", "TICKETONHOLD");

            CommonDAO objCommonDAO = new CommonDAO();
            String strTicketAssignee = objCommonDAO.getTicketAssignee(strRefno, strTicketId, objDBConnection);
            if (!strTicketAssignee.equalsIgnoreCase(objSessionInfo.getUserInfo().getUserId()))
            {
               String strTicketDescription = objTicketsDAO.getTicketDesc(strRefno, strTicketId, objDBConnection);
               String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefno, strTicketId, objDBConnection);
               String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

               String[] strToEmailIds = {strCreatedBy};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strTicketAssignee};
               String strMailSubject = "Ticket Progress Stopped : " + strTicketId;
               String[] strMessageHeader = {strCreatedByName, "Ticket Progress Stopped By " + objSessionInfo.getUserInfo().getUserName()};
               String[] strMessageBody = {
                       strRefno, strTicketId,
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
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

   public String loadCloseTask() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTaskId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlDomainNames = objTicketsDAO.getDomainNames(objDBConnection);
            ArrayList<String> arlTaskDetailsForClosing = objTicketsDAO.getTicketDetailsForUpdate(strRefno, strTaskId, objDBConnection);
            objRequest.setAttribute("DomainNames", arlDomainNames);
            objRequest.setAttribute("TaskDetailsForClosing", arlTaskDetailsForClosing);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
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

   public String loadCloseBug() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strBugId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlDomainNames = objTicketsDAO.getDomainNames(objDBConnection);
            ArrayList<String> arlReleaseVersions = objTicketsDAO.getReleaseVersions(objDBConnection);
            ArrayList<String> arlBugDetailsForClosing = objTicketsDAO.getTicketDetailsForUpdate(strRefno, strBugId, objDBConnection);
            objRequest.setAttribute("DomainNames", arlDomainNames);
            objRequest.setAttribute("BugDetailsForClosing", arlBugDetailsForClosing);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            objRequest.setAttribute("ReleaseVersions", arlReleaseVersions);
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

   public String closeTask() throws Exception
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
            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTaskId = AppUtil.checkNull(objRequest.getParameter("hidTaskId"));
            String strExistingJiraBug = AppUtil.checkNull(objRequest.getParameter("existingjirabug"));
            String strTaskResolution = AppUtil.checkNull(objRequest.getParameter("TaskResolution"));
            if (strExistingJiraBug.length() > 0)
            {
               strTaskResolution = strExistingJiraBug + strTaskResolution;
            }

            String strMovedDomain = AppUtil.checkNull(objRequest.getParameter("MovedDomain"));
            String strTaskRootCause = AppUtil.checkNull(objRequest.getParameter("TaskRootCause"));
            String strComments = AppUtil.checkNull(objRequest.getParameter("TaskComments"));
            String strOOSLAJustification = AppUtil.checkNull(objRequest.getParameter("OOSLAJustification"));
            if (strOOSLAJustification.length() > 0)
            {
               strOOSLAJustification = AppConstants.OOSLA_REASON + strOOSLAJustification + "\n";
            }
            String strTaskSeverity = AppUtil.checkNull(objRequest.getParameter("TaskSeverity"));
            String strFixingAssignee = AppUtil.checkNull(objRequest.getParameter("FixingAssignee"));
            strComments = strOOSLAJustification + AppConstants.CLOSING_TICKET_COMMENTS + strComments;
            if (strComments.length() > 4980)
            {
               strComments = strComments.substring(0, 4980);
            }
            String strNewBugRecDate = AppUtil.getCurrentDateDDMMYYYYHHMMAMPM();
            String strNewBugPlannedEndDate = AppUtil.getBusinessDate(strNewBugRecDate, AppConstants.BUG_SLA_DAYS, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            CommonDAO objCommonDAO = new CommonDAO();
            String strFixingAssigneeName = objCommonDAO.getAssigneeName(strFixingAssignee, objDBConnection);
            objRequest.setAttribute("FixingAssigneeName", strFixingAssigneeName);

            objTicketsDAO.closeTask(strRefNo, strTaskId,
                    strTaskResolution, strMovedDomain, strTaskRootCause, strComments, strNewBugRecDate, strNewBugPlannedEndDate, strTaskSeverity, strFixingAssignee,
                    strFixingAssigneeName, objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            String strTicketDescription = objTicketsDAO.getTicketDesc(strRefNo, strTaskId, objDBConnection);
            String strTaskAssignee = objCommonDAO.getTicketAssignee(strRefNo, strTaskId, objDBConnection);
            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefNo, strTaskId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strTaskAssignee};
            String strMailSubject = "Task Closed : " + strTaskId;
            String[] strMessageHeader = {strCreatedByName, "Task Closed By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strTaskId,
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

            if (strTaskResolution.equalsIgnoreCase(AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED))
            {
               objRequest.setAttribute("NEWBUGTOMS", "NEWBUGTOMS");
               strToEmailIds = new String[]{strFixingAssignee};
               strCCMailIds = new String[]{objSessionInfo.getUserInfo().getUserId(), strTaskAssignee};
               strMailSubject = "Task Converted To Bug And Assigned To You For Fixing : " + strTaskId;
               strMessageHeader = new String[]{strFixingAssigneeName, "Task Converted To Bug And Assigned To You For Fixing by " + objSessionInfo.getUserInfo().getUserName()};
               strMessageBody = new String[]{
                       objCommonDAO.getLiveTicketRefNo(strTaskId, objDBConnection), strTaskId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
            }
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);

            String strClosePage = AppUtil.checkNull(objRequest.getParameter("closepage"));
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

   public String closeBug() throws Exception
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
            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strBugId = AppUtil.checkNull(objRequest.getParameter("hidBugId"));
            String strExistingJiraBug = AppUtil.checkNull(objRequest.getParameter("existingjirabug"));
            String strBugResolution = AppUtil.checkNull(objRequest.getParameter("BugResolution"));
            if (strExistingJiraBug.length() > 0)
            {
               strBugResolution = strExistingJiraBug + strBugResolution;
            }

            String strJIRABugId = AppUtil.checkNull(objRequest.getParameter("JIRABugId"));
            String strMovedDomain = AppUtil.checkNull(objRequest.getParameter("MovedDomain"));
            String strBugRootCause = AppUtil.checkNull(objRequest.getParameter("BugRootCause"));
            String strReleaseVersion = AppUtil.checkNull(objRequest.getParameter("ReleaseVersion"));
            String strComments = AppUtil.checkNull(objRequest.getParameter("BugComments"));
            String strOOSLAJustification = AppUtil.checkNull(objRequest.getParameter("OOSLAJustification"));
            if (strOOSLAJustification.length() > 0)
            {
               strOOSLAJustification =  AppConstants.OOSLA_REASON + strOOSLAJustification + "\n";
            }
            String strCodeReviewComments = AppUtil.checkNull(objRequest.getParameter("CodeReviewComments"));
            String strBugSeverity = AppUtil.checkNull(objRequest.getParameter("BugSeverity"));
            String strFixingAssignee = AppUtil.checkNull(objRequest.getParameter("FixingAssignee"));
            strComments = strOOSLAJustification + AppConstants.CLOSING_TICKET_COMMENTS + strComments;
            if (strComments.length() > 4980)
            {
               strComments = strComments.substring(0, 4980);
            }
            String strNewBugRecDate = AppUtil.getCurrentDateDDMMYYYYHHMMAMPM();
            String strNewBugPlannedEndDate = AppUtil.getBusinessDate(strNewBugRecDate.toString(), AppConstants.BUG_SLA_DAYS, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            CommonDAO objCommonDAO = new CommonDAO();
            String strFixingAssigneeName = new CommonDAO().getAssigneeName(strFixingAssignee, objDBConnection);
            objRequest.setAttribute("FixingAssigneeName", strFixingAssigneeName);

            objTicketsDAO.closeBug(strRefNo, strBugId,
                    strBugResolution, strMovedDomain, strBugRootCause, strComments, strNewBugRecDate, strNewBugPlannedEndDate, strCodeReviewComments, strBugSeverity, strFixingAssignee,
                    strFixingAssigneeName, strReleaseVersion, objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            String strTicketDescription = objTicketsDAO.getTicketDesc(strRefNo, strBugId, objDBConnection);
            String strBugAssignee = objCommonDAO.getTicketAssignee(strRefNo, strBugId, objDBConnection);
            String strCreatedBy = objCommonDAO.getTicketCreatedBy(strRefNo, strBugId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strBugAssignee};
            String strMailSubject = "Bug Closed : " + strBugId;
            String[] strMessageHeader = {strCreatedByName, "Bug Closed By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strBugId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

            if (strBugResolution.equalsIgnoreCase(AppConstants.BUG_FIX_EXPECTED))
            {
               objRequest.setAttribute("EXISTINGBUGTOMS", "EXISTINGBUGTOMS");
               strToEmailIds = new String[]{strFixingAssignee};
               strCCMailIds = new String[]{objSessionInfo.getUserInfo().getUserId(), strBugAssignee};
               strMailSubject = "New Bug Created From Triaging Team And Assigned To You For Fixing : " + strBugId;
               strMessageHeader = new String[]{strFixingAssigneeName, "New Bug Created From Triaging Team And Assigned To You For Fixing by " + objSessionInfo.getUserInfo().getUserName()};
               strMessageBody = new String[]{
                       objCommonDAO.getLiveTicketRefNo(strBugId, objDBConnection), strBugId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));
            }
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);

            String strClosePage = AppUtil.checkNull(objRequest.getParameter("closepage"));
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

   public String loadTicketDetails() throws Exception
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

   public String viewTicketDetails() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRefno = AppUtil.checkNull(objRequest.getParameter("RefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("TicketId"));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlViewTicketDetails = objTicketsDAO.getTicketDetailsForView(strRefno, strTicketId, objDBConnection);
            TicketHistory objTicketHistory = new TicketHistory();
            ArrayList<String> arlTicketHistory = objTicketHistory.getTicketHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("ViewTicketDetails", arlViewTicketDetails);
            objRequest.setAttribute("TicketHistory", arlTicketHistory);
            objRequest.setAttribute("TotalDaysSpent", objTicketHistory.getTotalDaysSpent());
            objRequest.setAttribute("TicketActivityHistory", arlTicketActivityHistory);
            objRequest.setAttribute("CommentsHistory", arlCommentsHistory);

            String strTriagingRefNo = objTicketsDAO.getTriagingRefNo(strRefno, strTicketId, objDBConnection);
            if (strTriagingRefNo.length() > 0)
            {
               ArrayList<String> arlTriagingTicketDetails = objTicketsDAO.getTicketDetailsForView(strTriagingRefNo, strTicketId, objDBConnection);
               TicketHistory objTriagingTicketHistory = new TicketHistory();
               ArrayList<String> arlTriagingTicketHistory = objTriagingTicketHistory.getTicketHistory(strTriagingRefNo, strTicketId, objDBConnection);
               ArrayList<String> arlTriagingTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strTriagingRefNo, strTicketId, objDBConnection);
               ArrayList<String> arlTriagingCommentsHistory = objTicketsDAO.getCommentsHistory(strTriagingRefNo, strTicketId, objDBConnection);
               objRequest.setAttribute("TriagingTicketDetails", arlTriagingTicketDetails);
               objRequest.setAttribute("TriagingTicketHistory", arlTriagingTicketHistory);
               objRequest.setAttribute("TriagingTotalDaysSpent", objTriagingTicketHistory.getTotalDaysSpent());
               objRequest.setAttribute("TriagingTicketActivityHistory", arlTriagingTicketActivityHistory);
               objRequest.setAttribute("TriagingCommentsHistory", arlTriagingCommentsHistory);
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

   public String loadModifyCurrentTickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlAssignees = objTicketsDAO.getOpenTicketsAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            ArrayList<String> arlTicketDetails = objTicketsDAO.getTicketDetailsForModifyByAdmin(strAssignee, objDBConnection);
            objRequest.setAttribute("TicketDetails", arlTicketDetails);
            objRequest.setAttribute("Assignee", strAssignee);

            ArrayList<String> arlSLAMissingTicketDetails = objTicketsDAO.getSLAMissingTickets(strAssignee, objDBConnection);
            int intSLAMissedTicketsCount = 0;
            int intNearingSLATicketsCount = 0;
            if (arlSLAMissingTicketDetails != null && !arlSLAMissingTicketDetails.isEmpty())
            {
               ArrayList<String> arlNearingSLATickets = new ArrayList<>();
               ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
               String strTicketId;
               String strPlannedEndDate;
               Date dtPlannedEndDate;
               String strCurrentDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
               SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
               Date dtCurrentDate = sdf.parse(strCurrentDate);
               Calendar objCalendar = Calendar.getInstance();
               objCalendar.setTime(dtCurrentDate);
               Date dtNextWorkingDate = AppUtil.getNextWorkingDay(objCalendar, arlHolidayCalendar);
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

   public String loadReopenTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("TicketId"));
            if (strTicketId.length() > 0)
            {
               ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
               ArrayList<String> arlTicketDetailsForReopen = new TicketsDAO().getTicketDetailsForReopen(strTicketId, objDBConnection);
               objRequest.setAttribute("ReopenTicketId", strTicketId);
               objRequest.setAttribute("Assignees", arlAssignees);
               objRequest.setAttribute("TicketDetailsForReopen", arlTicketDetailsForReopen);
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

   public String ReopenTicket() throws Exception
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
            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strReasonForReopen = AppUtil.checkNull(objRequest.getParameter("ReasonForReopen"));
            String strReopenTicketCategory = AppUtil.checkNull(objRequest.getParameter("ReopenTicketCategory"));
            String strReopenComments = AppUtil.checkNull(objRequest.getParameter("ReopenComments"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));

            String strTeamWorking = "";
            int intSLADays = 0;
            if(strReopenTicketCategory.equalsIgnoreCase(AppConstants.TASK))
            {
               strTeamWorking = AppConstants.TRIAGING_TEAM;
               intSLADays = AppConstants.TASK_SLA_DAYS;
            }
            if(strReopenTicketCategory.equalsIgnoreCase(AppConstants.BUG))
            {
               strTeamWorking = AppConstants.FIXING_TEAM;
               intSLADays = AppConstants.EXISTING_BUG_SLA_DAYS;
            }


            if (strReopenComments.length() > 4980)
            {
               strReopenComments = strReopenComments.substring(0, 4980);
            }
            String strNewBugRecDate = AppUtil.getCurrentDateDDMMYYYYHHMMAMPM();
            String strNewBugPlannedEndDate = AppUtil.getBusinessDate(strNewBugRecDate.toString(), intSLADays, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.reopenTicket(strRefNo, strReopenTicketCategory, strTicketId, strReopenComments, strAssignee, strNewBugRecDate, strNewBugPlannedEndDate, strReasonForReopen, strTeamWorking,
                    objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            CommonDAO objCommonDAO = new CommonDAO();
            String strNewRefNo = objCommonDAO.getTicketRefNoForNotStarted(strTicketId, objDBConnection);

            String strTicketDescription = objTicketsDAO.getTicketDesc(strNewRefNo, strTicketId, objDBConnection);
            String strAssigneeName = objCommonDAO.getAssigneeName(strAssignee, objDBConnection);

            String[] strToEmailIds = {strAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
            String strMailSubject = "Ticket Re-Opened and Assigned to you : " + strTicketId;
            String[] strMessageHeader = {strAssigneeName, "Ticket Re-opened and Assigned to you By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strNewRefNo, strTicketId,
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));

            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
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

   public String loadAddCommentsDummy() throws Exception
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

   public String loadAddComments() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            TicketsDAO objTicketsDAO = new TicketsDAO();
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));

            ArrayList<String> arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlOpenTicketIdDescAssigneeStatus = objTicketsDAO.getOpenTicketIdDescAssigneeStatus(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("OpenTicketIdDescAssigneeStatus", arlOpenTicketIdDescAssigneeStatus);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            objRequest.setAttribute("CommentsHistory", arlCommentsHistory);
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


   public String addComments() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strPreWorkingDay = AppUtil.checkNull(objRequest.getParameter("hidPreWorkingDay"));
            String strCommentsCategory = AppUtil.checkNull(objRequest.getParameter("CommentsCategory"));
            String strCurrentStatus = AppUtil.checkNull(objRequest.getParameter("CurrentStatus"));
            if (strCurrentStatus.length() > 7490)
            {
               strCurrentStatus = strCurrentStatus.substring(0, 7490);
            }

            String strCommentsDate = "";
            if (strPreWorkingDay != null && strPreWorkingDay.length() > 0)
            {
               strCommentsDate = AppUtil.convertDate(strPreWorkingDay, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL) + " 23:59:59";
            }
            else
            {
               strCommentsDate = AppUtil.getCurrentDate_MySql();
            }
            new TicketsDAO().addComments(strRefno, strTicketId, strCommentsCategory, strCurrentStatus, strCommentsDate,
                    objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);

            objRequest.setAttribute("RESULT", "COMMENTSADDED");

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

   public String loadCreateInternalTask() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssigneesForInternalTickets(objDBConnection);
            ArrayList<String> arlOpenTicketIdsAndDesc = new TicketsDAO().getOpenTicketIdsAndDesc(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            objRequest.setAttribute("OpenTicketIdsAndDesc", arlOpenTicketIdsAndDesc);
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

   public String saveInternalTask() throws Exception
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
            String strOpenTicketRefno = AppUtil.checkNull(objRequest.getParameter("OpenTicketRefno"));
            String strOpenTicketId = AppUtil.checkNull(objRequest.getParameter("OpenTicketId"));
            String strOpenTicketDesc = AppUtil.checkNull(objRequest.getParameter("OpenTicketDesc"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strActionRequired = AppUtil.checkNull(objRequest.getParameter("ActionRequired"));
            String strITETADate = AppUtil.checkNull(objRequest.getParameter("ITETADate"));
            if (strActionRequired.length() > 998)
            {
               strActionRequired = strActionRequired.substring(0, 998);
            }

            if (strOpenTicketId.equalsIgnoreCase("Others"))
            {
               strOpenTicketId = AppUtil.checkNull(objRequest.getParameter("ITTicketIdOthers"));
            }

            TicketsDAO objTicketsDAO = new TicketsDAO();
            if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
            {
               ArrayList<String> arlAssignees = new CommonDAO().getAllAssigneesForInternalTickets(objDBConnection);
               String strAssigneeName = "";
               if (arlAssignees != null && !arlAssignees.isEmpty())
               {
                  for (int iCount = 0; iCount < arlAssignees.size(); iCount += 2)
                  {
                     strAssignee = arlAssignees.get(iCount);
                     strAssigneeName = arlAssignees.get(iCount + 1);
                     objTicketsDAO.saveInternalTask(strOpenTicketId, strOpenTicketDesc, strAssignee, strActionRequired, strITETADate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);

                     String[] strToEmailIds = {strAssignee};
                     String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
                     String strMailSubject = "New Internal Ticket Created : " + strOpenTicketId;
                     String[] strMessageHeader = {strAssigneeName, "New Internal Ticket Created by " + objSessionInfo.getUserInfo().getUserName()};
                     String[] strMessageBody = {
                             new CommonDAO().getInternalTicketRefNo(strOpenTicketId, objDBConnection), strOpenTicketId
                     };
                     new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                             strMessageHeader, AppConstants.TICKET_TYPE_INTERNAL, strMessageBody, true));
                  }
               }
            }
            else
            {
               objTicketsDAO.saveInternalTask(strOpenTicketId, strOpenTicketDesc, strAssignee, strActionRequired, strITETADate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
               String strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);

               String[] strToEmailIds = {strAssignee};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = "New Internal Ticket Created : " + strOpenTicketId;
               String[] strMessageHeader = {strAssigneeName, "New Internal Ticket Created by " + objSessionInfo.getUserInfo().getUserName()};
               String[] strMessageBody = {
                       new CommonDAO().getInternalTicketRefNo(strOpenTicketId, objDBConnection), strOpenTicketId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_INTERNAL, strMessageBody, true));
            }
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
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

   public String loadAddITCommentsDummy() throws Exception
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

   public String loadAddITComments() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            TicketsDAO objTicketsDAO = new TicketsDAO();
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            ArrayList<String> arlITCommentsHistory = objTicketsDAO.getITCommentsHistory(strRefno, objDBConnection);
            ArrayList<String> arlInternalTicketDetails = objTicketsDAO.getInternalTaskForUpdate(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("InternalTicketDetails", arlInternalTicketDetails);
            objRequest.setAttribute("ITCommentsHistory", arlITCommentsHistory);
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


   public String addITComments() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strTicketDesc = AppUtil.checkNull(objRequest.getParameter("hidTicketDesc"));
            String strActionRequired = AppUtil.checkNull(objRequest.getParameter("hidActionRequired"));
            String strTicketCreatedBy = AppUtil.checkNull(objRequest.getParameter("CreatedBy"));
            String strCreatedByName = AppUtil.checkNull(objRequest.getParameter("CreatedByName"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strAssigneeName = AppUtil.checkNull(objRequest.getParameter("AssigneeName"));
            String strComments = AppUtil.checkNull(objRequest.getParameter("Comments"));
            if (strComments.length() > 4990)
            {
               strComments = strComments.substring(0, 4990);
            }
            new TicketsDAO().addITComments(strRefno, strTicketId, strComments,
                    objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", "COMMENTSADDED");
            String strTicketCreatedByName = new CommonDAO().getAssigneeName(strTicketCreatedBy, objDBConnection);

            String[] strToEmailIds;
            String[] strCCMailIds;
            String ToIdName = "";

            if(!objSessionInfo.getUserInfo().getUserId().equalsIgnoreCase(strAssignee))
            {
               strToEmailIds = new String[]{strAssignee};
               strCCMailIds = new String[]{objSessionInfo.getUserInfo().getUserId(), strTicketCreatedBy};
               ToIdName = strAssigneeName;
            }
            else
            {
               strToEmailIds = new String[]{strTicketCreatedBy};
               strCCMailIds = new String[]{objSessionInfo.getUserInfo().getUserId(), strAssignee};
               ToIdName = strTicketCreatedByName;
            }

            String strMailSubject = "Internal Ticket Updated : " + strTicketId;
            String[] strMessageHeader = {ToIdName, "Internal Ticket Updated by " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefno, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_INTERNAL, strMessageBody, true));
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

   public String loadCloseInternalTicketDummy() throws Exception
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

   public String loadCloseInternalTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            TicketsDAO objTicketsDAO = new TicketsDAO();
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));

            ArrayList<String> arlITCommentsHistory = objTicketsDAO.getITCommentsHistory(strRefno, objDBConnection);
            ArrayList<String> arlInternalTicketDetails = objTicketsDAO.getInternalTaskForUpdate(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("InternalTicketDetails", arlInternalTicketDetails);
            objRequest.setAttribute("ITCommentsHistory", arlITCommentsHistory);
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


   public String closeInternalTicket() throws Exception
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
            String strTicketRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketCreatedBy = AppUtil.checkNull(objRequest.getParameter("CreatedBy"));
            String strCreatedByName = AppUtil.checkNull(objRequest.getParameter("CreatedByName"));
            String strComments = AppUtil.checkNull(objRequest.getParameter("Comments"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strAssigneeName = AppUtil.checkNull(objRequest.getParameter("AssigneeName"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("TicketId"));
            String strTicketDesc = AppUtil.checkNull(objRequest.getParameter("TicketDesc"));
            if (strComments.length() > 4998)
            {
               strComments = strComments.substring(0, 4998);
            }
            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.closeInternalTicket(strTicketRefno, strTicketId, strComments, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

            String strTicketCreatedByName = new CommonDAO().getAssigneeName(strTicketCreatedBy, objDBConnection);
            String[] strToEmailIds = {strTicketCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strAssignee};
            String strMailSubject = "Internal Ticket Completed : " + strTicketId;
            String[] strMessageHeader = {strTicketCreatedByName, "Internal Ticket Completed by " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strTicketRefno, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_INTERNAL, strMessageBody, true));

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

   public String loadITAdminChanges() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            TicketsDAO objTicketsDAO = new TicketsDAO();
            ArrayList<String> arlITAssignees = objTicketsDAO.getOpenITAssignees(objDBConnection);
            objRequest.setAttribute("ITAssignees", arlITAssignees);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyITAssignee"));
            if (strAssignee.length() == 0)
            {
               strAssignee = AppConstants.ALL_ASSIGNEES;
            }
            ArrayList<String> arlITDetails = objTicketsDAO.getITDetailsForAdmin(strAssignee, objDBConnection);
            objRequest.setAttribute("ITDetails", arlITDetails);
            objRequest.setAttribute("ModifyITAssignee", strAssignee);
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

   public String loadUpdateIT() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new CommonDAO().getAllAssigneesForInternalTickets(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            String strTicketRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            if (strTicketRefno.length() > 0 && strTicketId.length() > 0)
            {
               ArrayList<String> arlITDetails = new TicketsDAO().getInternalTaskForUpdate(strTicketRefno, strTicketId, objDBConnection);
               objRequest.setAttribute("ITDetails", arlITDetails);
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

   public String UpdateIT() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefno"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strOriginalAssignee = AppUtil.checkNull(objRequest.getParameter("hidOriginalAssignee"));
            String strNewAssignee = AppUtil.checkNull(objRequest.getParameter("UpdateITAssignee"));
            String strActionRequired = AppUtil.checkNull(objRequest.getParameter("ActionRequired"));
            String strITETADate = AppUtil.checkNull(objRequest.getParameter("ITETADate"));
            String strTicketDesc = AppUtil.checkNull(objRequest.getParameter("hidTicketDesc"));
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            if (strActionRequired.length() > 998)
            {
               strActionRequired = strActionRequired.substring(0, 998);
            }

            new TicketsDAO().updateInternalTicket(strRefno, strTicketId, strNewAssignee, strActionRequired, strITETADate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            CommonDAO objCommonDAO = new CommonDAO();
            String strNewAssigneeName = objCommonDAO.getAssigneeName(strNewAssignee, objDBConnection);
            String strOriginalAssigneeName = objCommonDAO.getAssigneeName(strOriginalAssignee, objDBConnection);
            String strCreatedBy = objCommonDAO.getITCreatedBy(strRefno, strTicketId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String strToMailId = "";
            String strCCMailId = "";
            String strToMailName = "";

            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

            String[] strCCMailIds;

            if (!strNewAssignee.equalsIgnoreCase(strOriginalAssignee))
            {
               strToMailId = strNewAssignee;
               strToMailName = strNewAssigneeName;
               strCCMailIds = new String[]{strCreatedBy, strOriginalAssignee, objSessionInfo.getUserInfo().getUserId()};
            }
            else
            {
               strToMailId = strCreatedBy;
               strToMailName = strCreatedByName;
               strCCMailIds = new String[]{strOriginalAssignee, objSessionInfo.getUserInfo().getUserId()};
            }

            String[] strToEmailIds = {strToMailId};
            String strMailSubject;
            String[] strMessageHeader;

            if (!strNewAssignee.equalsIgnoreCase(strOriginalAssignee))
            {
               strMailSubject = "Internal Ticket Re-Assigned To You From " + strOriginalAssigneeName + " : " + strTicketId;
               strMessageHeader = new String[]{strToMailName, "Internal Ticket Re-Assigned To You From " + strOriginalAssigneeName + " By " + objSessionInfo.getUserInfo().getUserName()};
            }
            else
            {
               strMailSubject = "Internal Ticket Updated : " + strTicketId;
               strMessageHeader = new String[]{strToMailName, "Internal Ticket Updated by " + objSessionInfo.getUserInfo().getUserName()};
            }
            String[] strMessageBody = {
                    strRefno, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_INTERNAL, strMessageBody, true));
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

   public String loadDeleteIT() throws Exception
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
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strTicketAssignee = AppUtil.checkNull(objRequest.getParameter("hidOriginalAssignee"));
            TicketsDAO objTicketsDAO = new TicketsDAO();
            CommonDAO objCommonDAO = new CommonDAO();

            String strTicketAssigneeName = objCommonDAO.getAssigneeName(strTicketAssignee, objDBConnection);
            String strCreatedBy = objCommonDAO.getITCreatedBy(strRefno, strTicketId, objDBConnection);

            objTicketsDAO.deleteInternalTicket(strRefno, strTicketId, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", "TICKETDELETED");

            String[] strToEmailIds = {strTicketAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strCreatedBy};
            String strMailSubject = "Internal Ticket Deleted : " + strTicketId;
            String[] strMessageHeader = {strTicketAssigneeName, "Internal Ticket Deleted by " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    AppConstants.TICKET_ID_LABEL, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            if (strFromPage.equalsIgnoreCase("HomePage"))
            {
               strForward = "homePage";
            }
            else if (strFromPage.equalsIgnoreCase("MODIFYINTERNALTICKETS"))
            {
               strForward = "ITAdminChanges";
            }
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            objRequest.setAttribute("Assignee", strAssignee);

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

   public String loadUpdateClosedTicket() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("TicketId"));
            if (strTicketId.length() > 0)
            {
               ArrayList<String> arlAssignees = new CommonDAO().getAllAssignees(objDBConnection);
               TicketsDAO objTicketsDAO = new TicketsDAO();
               ArrayList<String> arlDomainNames = objTicketsDAO.getDomainNames(objDBConnection);
               ArrayList<String> arlClosedTicketDetailsForUpdate = objTicketsDAO.getClosedTicketDetailsForUpdate(strTicketId, objDBConnection);
               ArrayList<String> arlReleaseVersions = objTicketsDAO.getReleaseVersions(objDBConnection);
               objRequest.setAttribute("UpdateClosedTicketId", strTicketId);
               objRequest.setAttribute("Assignees", arlAssignees);
               objRequest.setAttribute("DomainNames", arlDomainNames);
               objRequest.setAttribute("ReleaseVersions", arlReleaseVersions);
               objRequest.setAttribute("ClosedTicketDetailsForUpdate", arlClosedTicketDetailsForUpdate);
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

   public String UpdateClosedTicket() throws Exception
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
            String strRefNo = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strTicketCategory = AppUtil.checkNull(objRequest.getParameter("hidTicketCategory"));
            String strTicketDescription = AppUtil.checkNull(objRequest.getParameter("TicketDescription"));
            String strTicketPriority = AppUtil.checkNull(objRequest.getParameter("TicketPriority"));
            String strTicketType = AppUtil.checkNull(objRequest.getParameter("TicketType"));
            String strTicketModule = AppUtil.checkNull(objRequest.getParameter("TicketModule"));
            String strTicketRaisedBy = AppUtil.checkNull(objRequest.getParameter("TicketRaisedBy"));
            String strTicketResolution = AppUtil.checkNull(objRequest.getParameter("TicketResolution"));
            String strMovedDomain = AppUtil.checkNull(objRequest.getParameter("MovedDomain"));
            String strTicketRootCause = AppUtil.checkNull(objRequest.getParameter("TicketRootCause"));
            String strClosingComments = AppUtil.checkNull(objRequest.getParameter("ClosingComments"));
            String strTicketCreatedDate = AppUtil.checkNull(objRequest.getParameter("TicketCreatedDate"));
            String strReleaseVersion = AppUtil.checkNull(objRequest.getParameter("ReleaseVersion"));

            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strAssigneeName = AppUtil.checkNull(objRequest.getParameter("AssigneeName"));

            if (strClosingComments.length() > 4980)
            {
               strClosingComments = strClosingComments.substring(0, 4980);
            }
            TicketsDAO objTicketsDAO = new TicketsDAO();
            objTicketsDAO.UpdateClosedTicket(strRefNo, strTicketId, strTicketDescription, strTicketPriority, strTicketType,
                    strTicketModule, strTicketRaisedBy, strTicketResolution, strMovedDomain, strTicketRootCause,
                    strClosingComments, strTicketCreatedDate, strReleaseVersion,
                    objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            String[] strToEmailIds = {strAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
            String strMailSubject = "Closed Ticket Updated : " + strTicketId;
            String[] strMessageHeader = {strAssigneeName, "Closed Ticket Updated By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_LIVE, strMessageBody, true));

            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
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
