package com.opt.tickets.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.session.valobj.SessionInfo;
import com.opt.tickets.dao.RQATicketsDAO;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

public class RQATicketsAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(RQATicketsAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadCreateRQABug() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            CommonDAO objCommonDAO = new CommonDAO();
            ArrayList<String> arlAssignees = objCommonDAO.getAllAssignees(objDBConnection);
            String strCurrentRQAPhase = objCommonDAO.getRQACurrentPhase(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            objRequest.setAttribute("CurrentRQAPhase", strCurrentRQAPhase);
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

   public String saveRQABug() throws Exception
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
            String strRQAPhase = AppUtil.checkNull(objRequest.getParameter("hidRQAPhase"));
            String strRQACycle = AppUtil.checkNull(objRequest.getParameter("RQACycle"));
            String strBugId = AppUtil.checkNull(objRequest.getParameter("BugId"));
            String strBugDescription = AppUtil.checkNull(objRequest.getParameter("BugDescription"));
            if (strBugDescription.length() > 490)
            {
               strBugDescription = strBugDescription.substring(0, 490);
            }
            String strBugPriority = AppUtil.checkNull(objRequest.getParameter("BugPriority"));
            String strBugModule = AppUtil.checkNull(objRequest.getParameter("BugModule"));
            String strBugRaisedBy = AppUtil.checkNull(objRequest.getParameter("BugRaisedBy"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strCreateBugRecDate = AppUtil.checkNull(objRequest.getParameter("CreateRQABugRecDate"));
            String strReceivedHour = AppUtil.checkNull(objRequest.getParameter("ReceivedHour"));
            String strReceivedMinute = AppUtil.checkNull(objRequest.getParameter("ReceivedMinute"));
            String strReceived24Hours = AppUtil.checkNull(objRequest.getParameter("Received24Hours"));

            StringBuilder sbReceivedDate = new StringBuilder(strCreateBugRecDate);
            sbReceivedDate.append(AppConstants.SPACE).append(strReceivedHour).append(":").append(strReceivedMinute).append(AppConstants.SPACE).append(strReceived24Hours);
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            ArrayList<String> arlBugDetails = new ArrayList<>();
            if (objRQATicketsDAO.checkRQATicketExists(strBugId, objDBConnection))
            {
               arlBugDetails.add(strRQACycle);
               arlBugDetails.add(strBugId);
               arlBugDetails.add(strBugDescription);
               arlBugDetails.add(strBugPriority);
               arlBugDetails.add(strBugModule);
               arlBugDetails.add(strAssignee);
               arlBugDetails.add(strCreateBugRecDate);
               arlBugDetails.add(strReceivedHour);
               arlBugDetails.add(strReceivedMinute);
               arlBugDetails.add(strReceived24Hours);
               arlBugDetails.add(strBugRaisedBy);

               objRequest.setAttribute("RESULT", "BUGEXISTS");
               objRequest.setAttribute("BugDetails", arlBugDetails);
            }
            else
            {
               CommonDAO objCommonDAO = new CommonDAO();
               String strAssigneeName = objCommonDAO.getAssigneeName(strAssignee, objDBConnection);
               objRQATicketsDAO.saveRQABug(strRQAPhase, strRQACycle, strBugId, strBugDescription, strBugPriority,
                       strBugModule, strAssignee, sbReceivedDate.toString(), strBugRaisedBy, strAssigneeName, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
               objRequest.setAttribute("RESULT", AppConstants.SUCCESS);

               String[] strToEmailIds = {strAssignee};
               String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
               String strMailSubject = "New RQA Bug Created : " + strBugId;
               String[] strMessageHeader = {strAssigneeName, "New RQA Bug Created by " + objSessionInfo.getUserInfo().getUserName()};
               String[] strMessageBody = {
                       objCommonDAO.getRQATicketRefNo(strBugId, objDBConnection), strBugId
               };
               new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                       strMessageHeader, AppConstants.TICKET_TYPE_RQA, strMessageBody, true));
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

   public String loadRQATicketDetails() throws Exception
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

   public String viewRQATicketDetails() throws Exception
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
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            ArrayList<String> arlRQATicketDetailsForView = objRQATicketsDAO.getRQATicketDetailsForView(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlRQATicketActivityHistory = objRQATicketsDAO.getRQATicketActivityHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlRQACommentsCategory = objRQATicketsDAO.getRQACommentsHistory(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("RQATicketDetailsForView", arlRQATicketDetailsForView);
            objRequest.setAttribute("RQATicketActivityHistory", arlRQATicketActivityHistory);
            objRequest.setAttribute("RQACommentsCategory", arlRQACommentsCategory);
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

   public String loadRQAUpdateBug() throws Exception
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
            ArrayList<String> arlRQABugDetails = new RQATicketsDAO().getRQATicketDetailsForUpdate(strRefno, strBugId, objDBConnection);
            objRequest.setAttribute("RQABugDetails", arlRQABugDetails);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);
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

   public String updateRQABug() throws Exception
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
            String strBugId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));
            String strBugDescription = AppUtil.checkNull(objRequest.getParameter("BugDescription"));
            if (strBugDescription.length() > 490)
            {
               strBugDescription = strBugDescription.substring(0, 490);
            }
            String strBugPriority = AppUtil.checkNull(objRequest.getParameter("BugPriority"));
            String strBugModule = AppUtil.checkNull(objRequest.getParameter("BugModule"));
            String strBugRaisedBy = AppUtil.checkNull(objRequest.getParameter("BugRaisedBy"));
            String strETADate = AppUtil.checkNull(objRequest.getParameter("UpdateRQABugETADate"));

            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            objRQATicketsDAO.updateRQABug(strRefNo, strBugId, strBugDescription, strBugPriority,
                    strBugModule, strBugRaisedBy, strETADate, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
            objRequest.setAttribute("BugId", strBugId);

            CommonDAO objCommonDAO = new CommonDAO();
            String strCreatedBy = objCommonDAO.getRQABugCreatedBy(strRefNo, strBugId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);
            String strBugAssignee = objCommonDAO.getRQATicketAssignee(strRefNo, strBugId, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strBugAssignee};
            String strMailSubject = "RQA Bug Updated - " + strBugId;
            String[] strMessageHeader = {strCreatedByName, "RQA Bug Updated By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strBugId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_RQA, strMessageBody, true));

            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);
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

   public String loadAddRQACommentsDummy() throws Exception
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

   public String loadAddRQAComments() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            String strRefno = AppUtil.checkNull(objRequest.getParameter("hidRefNo"));
            String strTicketId = AppUtil.checkNull(objRequest.getParameter("hidTicketId"));

            ArrayList<String> arlRQACommentsCategory = objRQATicketsDAO.getRQACommentsHistory(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("RQACommentsCategory", arlRQACommentsCategory);

            ArrayList<String> arlOpenTicketIdDescAssigneeStatus = objRQATicketsDAO.getRQAOpenTicketIdDescAssigneeStatus(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("OpenTicketIdDescAssigneeStatus", arlOpenTicketIdDescAssigneeStatus);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
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

   public String addRQAComments() throws Exception
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

            String strComments = AppUtil.checkNull(objRequest.getParameter("Comments"));
            if (strComments.length() > 4990)
            {
               strComments = strComments.substring(0, 4990);
            }
            new RQATicketsDAO().addRQAComments(strRefno, strTicketId, strComments,
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

   public String ReAssignRQATicketDummy() throws Exception
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

   public String loadReAssignRQATicket() throws Exception
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
            ArrayList<String> arlOpenTicketIdDescAssigneeStatus = new RQATicketsDAO().getRQAOpenTicketIdDescAssigneeStatus(strRefno, strTicketId, objDBConnection);
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

   public String reAssignRQATicket() throws Exception
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
            String strTicketOriginalAssignee = objCommonDAO.getRQATicketAssignee(strRefno, strTicketId, objDBConnection);
            String strTicketOriginalAssigneeName = objCommonDAO.getAssigneeName(strTicketOriginalAssignee, objDBConnection);
            String strNewAssigneeName = objCommonDAO.getAssigneeName(strNewAssignee, objDBConnection);
            String strCreatedBy = objCommonDAO.getRQABugCreatedBy(strRefno, strTicketId, objDBConnection);

            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            objRQATicketsDAO.reAssignRQATicket(strRefno, strTicketId, strNewAssignee, strCommentsForReAssign, objSessionInfo.getUserInfo().getUserId(), strNewAssigneeName, objDBConnection);
            String strTicketDescription = objRQATicketsDAO.getRQATicketDesc(strRefno, strTicketId, objDBConnection);

            String[] strToEmailIds = {strNewAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strTicketOriginalAssignee, strCreatedBy};
            String strMailSubject = "RQA Bug Re-Assigned To You From " + strTicketOriginalAssigneeName + " : " + strTicketId;
            String[] strMessageHeader = {strNewAssigneeName, "RQA Bug Re-Assigned To You From " + strTicketOriginalAssigneeName + " By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefno, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_RQA, strMessageBody, true));
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

   public String deleteRQATicket() throws Exception
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

            String strTicketAssignee = objCommonDAO.getRQATicketAssignee(strRefno, strTicketId, objDBConnection);
            String strTicketAssigneeName = objCommonDAO.getAssigneeName(strTicketAssignee, objDBConnection);
            String strCreatedBy = objCommonDAO.getRQABugCreatedBy(strRefno, strTicketId, objDBConnection);

            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            String strTicketDescription = objRQATicketsDAO.getRQATicketDesc(strRefno, strTicketId, objDBConnection);
            objRQATicketsDAO.deleteRQATicket(strRefno, strTicketId, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("RESULT", "TICKETDELETED");

            String[] strToEmailIds = {strTicketAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strCreatedBy};
            String strMailSubject = "RQA Bug Deleted - " + strTicketId;
            String[] strMessageHeader = {strTicketAssigneeName, "RQA Bug Deleted By " + objSessionInfo.getUserInfo().getUserName()};
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
            else if (strFromPage.equalsIgnoreCase("ModifyRQATickets"))
            {
               strForward = "loadModifyRQATickets";
            }
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);

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

   public String loadCloseRQABug() throws Exception
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
            objRequest.setAttribute("DomainNames", arlDomainNames);
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            ArrayList<String> arlRQABugDetailsForClosing = objRQATicketsDAO.getRQATicketDetailsForUpdate(strRefno, strBugId, objDBConnection);
            objRequest.setAttribute("RQABugDetailsForClosing", arlRQABugDetailsForClosing);
            ArrayList<String> arlRQAResolutionTypes = objRQATicketsDAO.getRQAResolutionTypes(objDBConnection);
            objRequest.setAttribute("RQAResolutionTypes", arlRQAResolutionTypes);
            ArrayList<String> arlRQAResolutionSettings = objRQATicketsDAO.getRQAResolutionSettings(objDBConnection);
            objRequest.setAttribute("RQAResolutionSettings", arlRQAResolutionSettings);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);
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

   public String closeRQABug() throws Exception
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
            String strBugResolution = AppUtil.checkNull(objRequest.getParameter("BugResolution"));
            String strMovedDomain = AppUtil.checkNull(objRequest.getParameter("MovedDomain"));
            String strBugRootCause = AppUtil.checkNull(objRequest.getParameter("BugRootCause"));
            String strComments = AppUtil.checkNull(objRequest.getParameter("BugComments"));
            if (strComments.length() > 4980)
            {
               strComments = strComments.substring(0, 4980);
            }
            strComments = AppConstants.CLOSING_TICKET_COMMENTS + strComments;
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            objRQATicketsDAO.closeRQABug(strRefNo, strBugId,
                    strBugResolution, strMovedDomain, strBugRootCause, strComments, objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            CommonDAO objCommonDAO = new CommonDAO();
            String strTicketDescription = objRQATicketsDAO.getRQATicketDesc(strRefNo, strBugId, objDBConnection);
            String strBugAssignee = objCommonDAO.getRQATicketAssignee(strRefNo, strBugId, objDBConnection);
            String strCreatedBy = objCommonDAO.getRQABugCreatedBy(strRefNo, strBugId, objDBConnection);
            String strCreatedByName = objCommonDAO.getAssigneeName(strCreatedBy, objDBConnection);

            String[] strToEmailIds = {strCreatedBy};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId(), strBugAssignee};
            String strMailSubject = "RQA Bug Closed : " + strBugId;
            String[] strMessageHeader = {strCreatedByName, "RQA Bug Closed By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strRefNo, strBugId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_RQA, strMessageBody, true));

            objRequest.setAttribute("RESULT", AppConstants.SUCCESS);
            String strFromPage = AppUtil.checkNull(objRequest.getParameter("hidFromPage"));
            objRequest.setAttribute("FromPage", strFromPage);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);
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

   public String loadModifyRQATickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            ArrayList<String> arlAssignees = objRQATicketsDAO.getOpenRQATicketsAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("ModifyRQATicketAssignee"));
            ArrayList<String> arlRQATicketDetails = objRQATicketsDAO.getRQATicketDetailsForAdmin(strAssignee, objDBConnection);
            objRequest.setAttribute("RQATicketDetails", arlRQATicketDetails);
            objRequest.setAttribute("ModifyRQATicketAssignee", strAssignee);
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


   public String loadReopenRQATicket() throws Exception
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
               ArrayList<String> arlRQATicketDetailsForReopen = new RQATicketsDAO().getRQATicketDetailsForReopen(strTicketId, objDBConnection);
               objRequest.setAttribute("ReopenTicketId", strTicketId);
               objRequest.setAttribute("Assignees", arlAssignees);
               objRequest.setAttribute("RQATicketDetailsForReopen", arlRQATicketDetailsForReopen);
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

   public String ReopenRQATicket() throws Exception
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
            String strReopenComments = AppUtil.checkNull(objRequest.getParameter("ReopenComments"));
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strRQAPhase = AppUtil.checkNull(objRequest.getParameter("hidRQAPhase"));
            String strRQACycle = AppUtil.checkNull(objRequest.getParameter("hidRQACycle"));

            if (strReopenComments.length() > 4980)
            {
               strReopenComments = strReopenComments.substring(0, 4980);
            }
            String strNewBugRecDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATETIME_FORMAT_MYSQL);
            RQATicketsDAO objRQATicketsDAO = new RQATicketsDAO();
            objRQATicketsDAO.reopenRQATicket(strRefNo, strTicketId, strReopenComments, strAssignee, strNewBugRecDate, strReasonForReopen,
                    strRQAPhase, strRQACycle, objSessionInfo.getUserInfo().getUserId(), objDBConnection);

            CommonDAO objCommonDAO = new CommonDAO();
            String strNewRefNo = objCommonDAO.getRQATicketRefNoForNotStarted(strTicketId, objDBConnection);
            String strTicketDescription = objRQATicketsDAO.getRQATicketDesc(strNewRefNo, strTicketId, objDBConnection);
            String strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);

            String[] strToEmailIds = {strAssignee};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
            String strMailSubject = "RQA Bug Re-Opened and Assigned to you : " + strTicketId;
            String[] strMessageHeader = {strAssigneeName, "RQA Bug Re-opened and Assigned to you By " + objSessionInfo.getUserInfo().getUserName()};
            String[] strMessageBody = {
                    strNewRefNo, strTicketId
            };
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_RQA, strMessageBody, true));
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
