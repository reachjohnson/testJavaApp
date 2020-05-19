package com.opt.reports.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.reports.dao.RQAReportsDAO;
import com.opt.tickets.dao.RQATicketsDAO;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RQAReportsAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(RQAReportsAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadRQACurrentStatus() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strRQACurrentPhase = new CommonDAO().getRQACurrentPhase(objDBConnection);
            if (strRQACurrentPhase.length() > 0)
            {
               ArrayList<String> arlRQACurrentStatus = new RQAReportsDAO().getRQACurrentStatus(strRQACurrentPhase, objDBConnection);
               objRequest.setAttribute("RQACurrentStatus", arlRQACurrentStatus);
               objRequest.setAttribute("RQACurrentPhase", strRQACurrentPhase);
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

   public String loadRQADailyReport() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            CommonDAO objCommonDAO = new CommonDAO();
            String strRQACurrentPhase = objCommonDAO.getRQACurrentPhase(objDBConnection);
            objRequest.setAttribute("RQACurrentPhase", strRQACurrentPhase);
            if (strRQACurrentPhase.length() > 0)
            {
               RQAReportsDAO objRQAReportsDAO = new RQAReportsDAO();
               String strMailCutOffTime = objCommonDAO.getRQAMailCutOffTime(objDBConnection);
               ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
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
               objRequest.setAttribute("OpeningBalance", String.valueOf(intOpeningBalance));
               objRequest.setAttribute("RQANewBugs", arlRQANewBugs);
               objRequest.setAttribute("RQAResolvedBugs", arlRQAResolvedBugs);
               objRequest.setAttribute("RQACurrentOpenBugs", arlRQACurrentOpenBugs);
               objRequest.setAttribute("RQAResolutionSettings", arlRQAResolutionSettings);
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

   public String loadRQAMetrics() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            RQAReportsDAO objRQAReportsDAO = new RQAReportsDAO();
            ArrayList<String> arlRQAPhases = objRQAReportsDAO.getRQAPhases(objDBConnection);
            objRequest.setAttribute("RQAPhases", arlRQAPhases);
            String strRQAPhase = AppUtil.checkNull(objRequest.getParameter("RQAPhase"));
            objRequest.setAttribute("SelectedRQAPhase", strRQAPhase);
            if (strRQAPhase.length() > 0)
            {
               int intRQAReceivedBugsCount = objRQAReportsDAO.getReceivedRQABugsCount(strRQAPhase, objDBConnection);
               int intRQAClosedBugsCount = objRQAReportsDAO.getClosedRQABugsCount(strRQAPhase, objDBConnection);
               int intRQACurrentOpenBugsCount = objRQAReportsDAO.getCurrentOpenRQABugsCount(strRQAPhase, objDBConnection);
               ArrayList<String> arlRQABugsResolution = objRQAReportsDAO.getRQABugsResolution(strRQAPhase, objDBConnection);
               ArrayList<String> arlRQABugsMovedTeams = objRQAReportsDAO.getRQABugsMovedTeams(strRQAPhase, objDBConnection);
               ArrayList<String> arlRQABugsCount = new ArrayList<>();
               arlRQABugsCount.add(String.valueOf(intRQAReceivedBugsCount));
               arlRQABugsCount.add(String.valueOf(intRQAClosedBugsCount));
               arlRQABugsCount.add(String.valueOf(intRQACurrentOpenBugsCount));
               objRequest.setAttribute("RQABugsCount", arlRQABugsCount);
               objRequest.setAttribute("RQABugsResolution", arlRQABugsResolution);
               objRequest.setAttribute("RQABugsMovedTeams", arlRQABugsMovedTeams);
               ArrayList<String> arlRQAListOfBugs = new RQAReportsDAO().getRQAListOfBugs(strRQAPhase, objDBConnection);
               objRequest.setAttribute("RQAListOfBugs", arlRQAListOfBugs);
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
