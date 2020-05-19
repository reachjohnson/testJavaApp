package com.opt.calendar.action;

import com.opt.base.dao.BaseDAO;
import com.opt.calendar.dao.CalendarDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.util.*;
import com.opt.session.valobj.SessionInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

public class CalendarAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(CalendarAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadLeavePlans() throws Exception
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
            ArrayList<String> arlLeavePlans = new CalendarDAO().getLeavePlans(objSessionInfo.getUserInfo().getUserId(), objDBConnection);
            objRequest.setAttribute("LEAVEPLANS", arlLeavePlans);
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

   public String saveLeavePlans() throws Exception
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
            String strAction = AppUtil.checkNull(objRequest.getParameter("hidAction"));
            String strLeaveFromDate = AppUtil.checkNull(objRequest.getParameter("LeavePlansFromDate"));
            String strLeaveToDate = AppUtil.checkNull(objRequest.getParameter("LeavePlansToDate"));
            String strLeaveReason = AppUtil.checkNull(objRequest.getParameter("LeaveReason"));
            String strLeaveRefNo = AppUtil.checkNull(objRequest.getParameter("hidLeaveRefNo"));
            CalendarDAO objCalendarDAO = new CalendarDAO();

            String[] strToEmailIds = {AppConstants.MANAGER_ID};
            String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
            String strMailSubject = "";
            String[] strMessageHeader = new String[]{};
            if (strAction.equalsIgnoreCase("Add"))
            {
               strMailSubject = AppConstants.LEAVEPLANSSUBJECT;
               strMessageHeader = new String[]{AppConstants.MANAGER_NAME, "Please find my below Leave Plans"};
            }
            else if (strAction.equalsIgnoreCase("Cancel"))
            {
               strMailSubject = AppConstants.LEAVECANCELPLANSSUBJECT;
               strMessageHeader = new String[]{AppConstants.MANAGER_NAME, "Please find my below Cancelled Leave Plans"};
               ArrayList<String> arlTemp = objCalendarDAO.getLeavePlansForRefNo(strLeaveRefNo, objDBConnection);
               if (arlTemp != null && !arlTemp.isEmpty())
               {
                  strLeaveFromDate = arlTemp.get(0);
                  strLeaveToDate = arlTemp.get(1);
                  strLeaveReason = arlTemp.get(2);
               }
            }
            String[] strMessageBody = {
                    "From Date", strLeaveFromDate,
                    "To Date", strLeaveToDate,
                    "Reason", strLeaveReason
            };

            if (strAction.equalsIgnoreCase("Add"))
            {
               objCalendarDAO.addLeavePlans(objSessionInfo.getUserInfo().getUserId(), strLeaveFromDate, strLeaveToDate, strLeaveReason, objDBConnection);
               objRequest.setAttribute("RESULT", "ADDED");
            }
            else if (strAction.equalsIgnoreCase("Cancel"))
            {
               objCalendarDAO.deleteLeavePlans(strLeaveRefNo, objSessionInfo.getUserInfo().getUserId(), objDBConnection);
               objRequest.setAttribute("RESULT", "Cancelled");
            }
            new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));
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

   public String loadHolidayCalendar() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendar(objDBConnection);
            objRequest.setAttribute("HOLIDAYCALENDAR", arlHolidayCalendar);
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

   public String saveHolidayCalendar() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strAction = AppUtil.checkNull(objRequest.getParameter("hidAction"));
            String strHolidayRefNo = AppUtil.checkNull(objRequest.getParameter("hidHolidayRefNo"));
            String strHolidayDate = AppUtil.checkNull(objRequest.getParameter("HolidayDate"));
            String strHolidayName = AppUtil.checkNull(objRequest.getParameter("HolidayName"));
            CalendarDAO objCalendarDAO = new CalendarDAO();
            if (strAction.equalsIgnoreCase("Add"))
            {
               if (objCalendarDAO.checkHolidayCalendarExists(strHolidayDate, objDBConnection))
               {
                  objRequest.setAttribute("RESULT", "HOLIDAYEXISTS");
                  objRequest.setAttribute("HOLIDAYDATE", strHolidayDate);
                  objRequest.setAttribute("HOLIDAYNAME", strHolidayName);
               }
               else
               {
                  objCalendarDAO.addHolidayCalendar(strHolidayDate, strHolidayName, objDBConnection);
                  objRequest.setAttribute("RESULT", "ADDED");
               }
            }
            else if (strAction.equalsIgnoreCase("Delete"))
            {
               new CalendarDAO().deleteHolidayCalendar(strHolidayRefNo, objDBConnection);
               objRequest.setAttribute("RESULT", "Deleted");
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
}