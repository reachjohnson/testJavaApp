package com.opt.reports.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opt.base.dao.BaseDAO;
import com.opt.charts.BarChart;
import com.opt.charts.LineChart;
import com.opt.charts.LineChartDouble;
import com.opt.charts.PieChart;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.reports.ExcelReports.TicketsCurrentStatusExcel;
import com.opt.reports.PDFReports.SLAMetrics;
import com.opt.reports.PDFReports.SLAReport;
import com.opt.reports.dao.ReportsDAO;
import com.opt.reports.dao.SLAReportsDAO;
import com.opt.session.valobj.SessionInfo;
import com.opt.session.valobj.UserInfo;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.CalculateBusinessHours;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SLAReportsAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(SLAReportsAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String loadSLAReports() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("SLAReportsFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("SLAReportsToDate"));
            if (strToDate == null || strToDate.length() == 0)
            {
               strToDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate == null || strFromDate.length() == 0)
            {
               strFromDate = AppUtil.getMondayDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate.length() > 0 && strToDate.length() > 0)
            {
               SLAReportsDAO objSLAReportsDAO = new SLAReportsDAO();
               int intReceivedTasksCount = objSLAReportsDAO.getReceivedTasksCount(strFromDate, strToDate, objDBConnection);
               int intReceivedBugsCount = objSLAReportsDAO.getReceivedBugsCount(strFromDate, strToDate, objDBConnection);
               int intClosedTasksCount = objSLAReportsDAO.getClosedTasksCount(strFromDate, strToDate, objDBConnection);
               int intClosedBugsCount = objSLAReportsDAO.getClosedBugsCount(strFromDate, strToDate, objDBConnection);
               int intCurrentOpenTasksCount = objSLAReportsDAO.getCurrentOpenTasksCount(strToDate, objDBConnection);
               int intCurrentOpenBugsCount = objSLAReportsDAO.getCurrentOpenBugsCount(strToDate, objDBConnection);

               ArrayList<String> arlReceivedTasks = objSLAReportsDAO.getReceivedTasks(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlReceivedBugs = objSLAReportsDAO.getReceivedBugs(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlClosedTasks = objSLAReportsDAO.getClosedTasks(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlClosedBugs = objSLAReportsDAO.getClosedBugs(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlCurrentOpenTasks = objSLAReportsDAO.getCurrentOpenTasks(strToDate, objDBConnection);
               ArrayList<String> arlCurrentOpenBugs = objSLAReportsDAO.getCurrentOpenBugs(strToDate, objDBConnection);

               ArrayList<String> arlCounts = new ArrayList<>();

               arlCounts.add("Received");
               arlCounts.add(String.valueOf(intReceivedTasksCount));
               arlCounts.add(String.valueOf(intReceivedBugsCount));
               arlCounts.add(String.valueOf(intReceivedTasksCount + intReceivedBugsCount));

               arlCounts.add("Closed");
               arlCounts.add(String.valueOf(intClosedTasksCount));
               arlCounts.add(String.valueOf(intClosedBugsCount));
               arlCounts.add(String.valueOf(intClosedTasksCount + intClosedBugsCount));

               arlCounts.add("Current Open");
               arlCounts.add(String.valueOf(intCurrentOpenTasksCount));
               arlCounts.add(String.valueOf(intCurrentOpenBugsCount));
               arlCounts.add(String.valueOf(intCurrentOpenTasksCount + intCurrentOpenBugsCount));

               objRequest.setAttribute("FromDate", strFromDate);
               objRequest.setAttribute("ToDate", strToDate);
               objRequest.setAttribute("Counts", arlCounts);

               objRequest.setAttribute("ReceivedTasks", arlReceivedTasks);
               objRequest.setAttribute("ReceivedBugs", arlReceivedBugs);
               objRequest.setAttribute("ClosedTasks", arlClosedTasks);
               objRequest.setAttribute("ClosedBugs", arlClosedBugs);
               objRequest.setAttribute("CurrentOpenTasks", arlCurrentOpenTasks);
               objRequest.setAttribute("CurrentOpenBugs", arlCurrentOpenBugs);
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

   public String loadSLAReportsPDF() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, false);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         strForward = AppConstants.SUCCESS;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }

   public void GenerateSLAReportsPDF() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      HttpServletResponse objResponse = ServletActionContext.getResponse();
      objResponse.setContentType("application/pdf");
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI); OutputStream out = objResponse.getOutputStream())
         {
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("SLAReportsFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("SLAReportsToDate"));
            if (strFromDate.length() > 0 && strToDate.length() > 0)
            {
               SLAReportsDAO objSLAReportsDAO = new SLAReportsDAO();
               int intReceivedTasksCount = objSLAReportsDAO.getReceivedTasksCount(strFromDate, strToDate, objDBConnection);
               int intReceivedBugsCount = objSLAReportsDAO.getReceivedBugsCount(strFromDate, strToDate, objDBConnection);
               int intClosedTasksCount = objSLAReportsDAO.getClosedTasksCount(strFromDate, strToDate, objDBConnection);
               int intClosedBugsCount = objSLAReportsDAO.getClosedBugsCount(strFromDate, strToDate, objDBConnection);
               int intCurrentOpenTasksCount = objSLAReportsDAO.getCurrentOpenTasksCount(strToDate, objDBConnection);
               int intCurrentOpenBugsCount = objSLAReportsDAO.getCurrentOpenBugsCount(strToDate, objDBConnection);

               ArrayList<String> arlReceivedTasks = objSLAReportsDAO.getReceivedTasks(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlReceivedBugs = objSLAReportsDAO.getReceivedBugs(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlClosedTasks = objSLAReportsDAO.getClosedTasks(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlClosedBugs = objSLAReportsDAO.getClosedBugs(strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlCurrentOpenTasks = objSLAReportsDAO.getCurrentOpenTasks(strToDate, objDBConnection);
               ArrayList<String> arlCurrentOpenBugs = objSLAReportsDAO.getCurrentOpenBugs(strToDate, objDBConnection);

               ArrayList<String> arlCounts = new ArrayList<>();

               arlCounts.add("Received");
               arlCounts.add(String.valueOf(intReceivedTasksCount));
               arlCounts.add(String.valueOf(intReceivedBugsCount));
               arlCounts.add(String.valueOf(intReceivedTasksCount + intReceivedBugsCount));

               arlCounts.add("Closed");
               arlCounts.add(String.valueOf(intClosedTasksCount));
               arlCounts.add(String.valueOf(intClosedBugsCount));
               arlCounts.add(String.valueOf(intClosedTasksCount + intClosedBugsCount));

               arlCounts.add("Current Open");
               arlCounts.add(String.valueOf(intCurrentOpenTasksCount));
               arlCounts.add(String.valueOf(intCurrentOpenBugsCount));
               arlCounts.add(String.valueOf(intCurrentOpenTasksCount + intCurrentOpenBugsCount));

               int intReportNumbers = 0;
               HashMap<Integer, String> hpReportParameters = new HashMap<>();

               hpReportParameters.put(++intReportNumbers, "Counts");
               hpReportParameters.put(++intReportNumbers, "FixingCounts");

               hpReportParameters.put(++intReportNumbers, "ReceivedTasks");
               hpReportParameters.put(++intReportNumbers, "ReceivedBugs");
               hpReportParameters.put(++intReportNumbers, "ClosedTasks");
               hpReportParameters.put(++intReportNumbers, "ClosedBugs");
               hpReportParameters.put(++intReportNumbers, "CurrentOpenTasks");
               hpReportParameters.put(++intReportNumbers, "CurrentOpenBugs");

               new SLAReport(strFromDate, strToDate, arlCounts, arlReceivedTasks, arlReceivedBugs, arlClosedTasks, arlClosedBugs,
                       arlCurrentOpenTasks, arlCurrentOpenBugs, intReportNumbers, hpReportParameters, out);
            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public String loadTicketCurrentStatusExcel() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, false);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         strForward = AppConstants.SUCCESS;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }

   public String TicketCurrentStatusExcel() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      HttpServletResponse objResponse = ServletActionContext.getResponse();
      objResponse.setContentType("application/pdf");
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            HttpSession objSession = objRequest.getSession(false);
            SessionInfo objSessionInfo = (SessionInfo) objSession.getAttribute(AppConstants.SESSION_DATA);
            String strExcelPath = objRequest.getSession().getServletContext().getRealPath("") + "\\" + AppConstants.EXCEL_FOLDER + "\\" + objSessionInfo.getUserInfo().getUserId() + "_TicketCurrentStatus.xls";
            String strExcelDirectory = objRequest.getSession().getServletContext().getRealPath("") + "\\" + AppConstants.EXCEL_FOLDER;
            File excelDirFile = new File(strExcelDirectory);
            if (!excelDirFile.exists() || !excelDirFile.isDirectory())
            {
               excelDirFile.mkdir();
            }
            File objExcelFile = new File(strExcelPath);
            if (objExcelFile.exists())
            {
               objExcelFile.delete();
            }
            ReportsDAO objReportsDAO = new ReportsDAO();
            ArrayList<String> arlNotStartedTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.NOT_STARTED, objReportsDAO, objDBConnection);
            ArrayList<String> arlInProgressTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.IN_PROGRESS, objReportsDAO, objDBConnection);
            ArrayList<String> arlOnHoldTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.ON_HOLD, objReportsDAO, objDBConnection);

            new TicketsCurrentStatusExcel().exportDataToExcel(strExcelPath, arlNotStartedTicketsCurrentStatus, arlInProgressTicketsCurrentStatus, arlOnHoldTicketsCurrentStatus);
            strExcelPath = strExcelPath.replace('\\', '/');
            objRequest.setAttribute("EXCELFILEPATH", strExcelPath);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }

      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

   public String DownloadExcel() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         String strFilePath = AppUtil.checkNull(objRequest.getParameter("EXCELFILEPATH"));
         objRequest.setAttribute("EXCELFILEPATH", strFilePath);
         strForward = AppConstants.SUCCESS;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }

   public String loadSLAMetrics() throws Exception
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

            String strFromDate = AppUtil.checkNull(objRequest.getParameter("SLAMetricsFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("SLAMetricsToDate"));
            if (strToDate == null || strToDate.length() == 0)
            {
               strToDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate == null || strFromDate.length() == 0)
            {
               strFromDate = AppUtil.getMondayDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate.length() > 0 && strToDate.length() > 0)
            {
               SLAReportsDAO objSLAReportsDAO = new SLAReportsDAO();
               ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
               ArrayList<String> arlCurrentOpenTasksCount = populateCurrentOpenTasks(strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlCurrentOpenBugsCount = populateCurrentOpenBugs(strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlCurrentOpenOOLSATasksCount = populateCurrentOpenOOLSATasks(strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlCurrentOpenOOLSABugsCount = populateCurrentOpenOOLSABugs(strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlTasksStatusCount = populateTasksStatusCount(strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlBugsStatusCount = populateBugsStatusCount(strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlTasksResolutioncount = objSLAReportsDAO.getTaskResolutionTypesCount(AppConstants.TASK, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlConvertedBugsMovedTeamsCount = objSLAReportsDAO.getConvertedBugsMovedTeamsCount(AppConstants.TASK, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlTasksOthersMovedTeamsCount = objSLAReportsDAO.getTasksOthersMovedTeamsCount(AppConstants.TASK, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlBugsResolutioncount = objSLAReportsDAO.getResolutionTypesCount(AppConstants.BUG, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlBugsMovedTeamscount = objSLAReportsDAO.getBugsMovedTeamsCount(AppConstants.BUG, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlTasksSLA = populateTasksSLA(strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection);
               ArrayList<String> arlBugsSLA = populateBugsSLA(strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection);
               ArrayList<String> arlResourceUtilization = populateResourceUtilization(strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection);
               ArrayList<String> arlBugsInflowFromTasks = populateBugsInflowFromTasks(strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<Object> arlTaskResolutionChart = new ArrayList<>();
               ArrayList<Object> arlBugResolutionChart = new ArrayList<>();

               ArrayList<ArrayList<String>> arlWeekAndMonthSummary = populateWeekAndMonthSummary(strFromDate, strToDate, objSLAReportsDAO, objRequest, objSessionInfo.getUserInfo(), objDBConnection);

               if (arlTasksResolutioncount != null && !arlTasksResolutioncount.isEmpty())
               {
                  arlTaskResolutionChart = generateTaskResolutionChart(arlBugsInflowFromTasks, arlTasksResolutioncount, arlTaskResolutionChart, "Tasks Resolution Types");
                  String strTaskResolutionChartPath = new PieChart().drawPieChart("TaskResolution", objSessionInfo.getUserInfo().getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Tasks Resolution", "", "Count", arlTaskResolutionChart);
                  objRequest.setAttribute("TaskResolutionChartPath", strTaskResolutionChartPath);
               }

               if (arlBugsResolutioncount != null && !arlBugsResolutioncount.isEmpty())
               {
                  arlBugResolutionChart = generateBugResolutionChart(arlBugsResolutioncount, arlBugResolutionChart, "Bugs Resolution Types");
                  String strBugResolutionChartPath = new PieChart().drawPieChart("BugResolution", objSessionInfo.getUserInfo().getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Bugs Resolution", "", "Count", arlBugResolutionChart);
                  objRequest.setAttribute("BugResolutionChartPath", strBugResolutionChartPath);
               }

               objRequest.setAttribute("CurrentOpenTasksCount", arlCurrentOpenTasksCount);
               objRequest.setAttribute("CurrentOpenBugsCount", arlCurrentOpenBugsCount);
               objRequest.setAttribute("CurrentOpenOOLSATasksCount", arlCurrentOpenOOLSATasksCount);
               objRequest.setAttribute("CurrentOpenOOLSABugsCount", arlCurrentOpenOOLSABugsCount);
               objRequest.setAttribute("TasksStatusCount", arlTasksStatusCount);
               objRequest.setAttribute("TasksResolutioncount", arlTasksResolutioncount);
               objRequest.setAttribute("ConvertedBugsMovedTeamsCount", arlConvertedBugsMovedTeamsCount);
               objRequest.setAttribute("TasksOthersMovedTeamsCount", arlTasksOthersMovedTeamsCount);
               objRequest.setAttribute("TasksSLA", arlTasksSLA);
               objRequest.setAttribute("BugsStatusCount", arlBugsStatusCount);
               objRequest.setAttribute("BugsResolutioncount", arlBugsResolutioncount);
               objRequest.setAttribute("BugsMovedTeamscount", arlBugsMovedTeamscount);
               objRequest.setAttribute("BugsSLA", arlBugsSLA);
               objRequest.setAttribute("ResourceUtilization", arlResourceUtilization);
               objRequest.setAttribute("BugsInflowFromTasks", arlBugsInflowFromTasks);
               objRequest.setAttribute("FromDate", strFromDate);
               objRequest.setAttribute("ToDate", strToDate);
               objRequest.setAttribute("WeekAndMonthSummary", arlWeekAndMonthSummary);
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

   public String loadSLAMetricsPDF() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, false);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         strForward = AppConstants.SUCCESS;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }

   public void GenerateSLAMetricsPDF() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      HttpServletResponse objResponse = ServletActionContext.getResponse();
      objResponse.setContentType("application/pdf");
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI); OutputStream out = objResponse.getOutputStream())
         {
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("SLAMetricsFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("SLAMetricsToDate"));
            if (strFromDate.length() > 0 && strToDate.length() > 0)
            {
               SLAReportsDAO objSLAReportsDAO = new SLAReportsDAO();
               ArrayList<String> arlHolidayCalendar = new CommonDAO().getHolidayCalendarForCalc(objDBConnection);
               ArrayList<String> arlCurrentOpenTasksCount = populateCurrentOpenTasks(strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlCurrentOpenBugsCount = populateCurrentOpenBugs(strToDate, objSLAReportsDAO, objDBConnection);

               ArrayList<String> arlTasksStatusCount = populateTasksStatusCount(strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlBugsStatusCount = populateBugsStatusCount(strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               ArrayList<String> arlTasksResolutioncount = objSLAReportsDAO.getTaskResolutionTypesCount(AppConstants.TASK, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlConvertedBugsMovedTeamsCount = objSLAReportsDAO.getConvertedBugsMovedTeamsCount(AppConstants.TASK, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlBugsResolutioncount = objSLAReportsDAO.getResolutionTypesCount(AppConstants.BUG, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlBugsMovedTeamscount = objSLAReportsDAO.getBugsMovedTeamsCount(AppConstants.BUG, strFromDate, strToDate, objDBConnection);
               ArrayList<String> arlTasksSLA = populateTasksSLA(strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection);
               ArrayList<String> arlBugsSLA = populateBugsSLA(strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection);


               int intReportNumbers = 0;
               HashMap<Integer, String> hpReportParameters = new HashMap<>();
               hpReportParameters.put(++intReportNumbers, "CurrentOpenTasksCount");
               hpReportParameters.put(++intReportNumbers, "CurrentOpenBugsCount");

               hpReportParameters.put(++intReportNumbers, "TasksStatusCount");
               hpReportParameters.put(++intReportNumbers, "TasksResolutioncount");
               hpReportParameters.put(++intReportNumbers, "TasksMovedTeamscount");
               hpReportParameters.put(++intReportNumbers, "TasksSLA");

               hpReportParameters.put(++intReportNumbers, "BugsStatusCount");
               hpReportParameters.put(++intReportNumbers, "BugsResolutioncount");
               hpReportParameters.put(++intReportNumbers, "BugsMovedTeamscount");
               hpReportParameters.put(++intReportNumbers, "BugsSLA");

               new SLAMetrics(strFromDate, strToDate, arlCurrentOpenTasksCount, arlCurrentOpenBugsCount,
                       arlTasksStatusCount, arlBugsStatusCount, arlTasksResolutioncount, arlConvertedBugsMovedTeamsCount, arlBugsResolutioncount, arlBugsMovedTeamscount,
                       arlTasksSLA, arlBugsSLA, intReportNumbers, hpReportParameters, out);

            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   private ArrayList<String> populateCurrentOpenTasks(String strAsOnDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCurrentOpenTasksCount = new ArrayList<>();
      try
      {
         int intQueryCount;
         int intTotalCount = 0;
         int[] intPriorityTotal = new int[6];
         for (String strModule : AppConstants.TICKET_MODULE)
         {
            intTotalCount = 0;
            arlCurrentOpenTasksCount.add(strModule);
            int intCounter = 0;
            for (String strPriority : AppConstants.TICKET_PRIORITY)
            {
               intQueryCount = objSLAReportsDAO.getCurrentOpenCountByModulePriority(AppConstants.TASK, strModule, strPriority, strAsOnDate, objDBConnection);
               intTotalCount += intQueryCount;
               intPriorityTotal[intCounter] += intQueryCount;
               intCounter++;
               arlCurrentOpenTasksCount.add(String.valueOf(intQueryCount));
            }
            arlCurrentOpenTasksCount.add(String.valueOf(intTotalCount));
            intPriorityTotal[intCounter] += intTotalCount;
         }
         arlCurrentOpenTasksCount.add("Total");
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[0]));
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[1]));
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[2]));
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[3]));
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[4]));
         arlCurrentOpenTasksCount.add(String.valueOf(intPriorityTotal[5]));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenTasksCount;
   }

   private ArrayList<String> populateCurrentOpenBugs(String strAsOnDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCurrentOpenBugsCount = new ArrayList<>();
      try
      {
         int[] intPriorityTotal = new int[6];
         int intQueryCount;
         int intTotalCount = 0;
         for (String strTicketModule : AppConstants.TICKET_MODULE)
         {
            intTotalCount = 0;
            arlCurrentOpenBugsCount.add(strTicketModule);
            int intCounter = 0;
            for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
            {
               intQueryCount = objSLAReportsDAO.getCurrentOpenCountByModulePriority(AppConstants.BUG, strTicketModule, strTicketPriority, strAsOnDate, objDBConnection);
               intTotalCount += intQueryCount;
               intPriorityTotal[intCounter] += intQueryCount;
               intCounter++;
               arlCurrentOpenBugsCount.add(String.valueOf(intQueryCount));
            }
            arlCurrentOpenBugsCount.add(String.valueOf(intTotalCount));
            intPriorityTotal[intCounter] += intTotalCount;
         }
         arlCurrentOpenBugsCount.add("Total");
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[0]));
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[1]));
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[2]));
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[3]));
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[4]));
         arlCurrentOpenBugsCount.add(String.valueOf(intPriorityTotal[5]));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenBugsCount;
   }

   private ArrayList<String> populateCurrentOpenOOLSATasks(String strAsOnDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCurrentOpenOOLSATasksCount = new ArrayList<>();
      try
      {
         int intQueryCount;
         int intTotalCount = 0;
         intTotalCount = 0;
         for (String strPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getCurrentOpenOOSLACountByPriority(AppConstants.TASK, strPriority, strAsOnDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlCurrentOpenOOLSATasksCount.add(String.valueOf(intQueryCount));
         }
         arlCurrentOpenOOLSATasksCount.add(String.valueOf(intTotalCount));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenOOLSATasksCount;
   }

   private ArrayList<String> populateCurrentOpenOOLSABugs(String strAsOnDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCurrentOpenOOLSABugsCount = new ArrayList<>();
      try
      {
         int intQueryCount;
         int intTotalCount = 0;
         intTotalCount = 0;
         for (String strPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getCurrentOpenOOSLACountByPriority(AppConstants.BUG, strPriority, strAsOnDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlCurrentOpenOOLSABugsCount.add(String.valueOf(intQueryCount));
         }
         arlCurrentOpenOOLSABugsCount.add(String.valueOf(intTotalCount));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenOOLSABugsCount;
   }

   private ArrayList<String> populateTasksStatusCount(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTasksStatusCount = new ArrayList<>();
      try
      {
         int intQueryCount;
         int intTotalCount = 0;
         arlTasksStatusCount.add("Received");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getReceivedCountByPriority(AppConstants.TASK, strTicketPriority, strFromDate, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlTasksStatusCount.add(String.valueOf(intQueryCount));
         }
         arlTasksStatusCount.add(String.valueOf(intTotalCount));

         intTotalCount = 0;
         arlTasksStatusCount.add("Closed");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getClosedCountByPriority(AppConstants.TASK, strTicketPriority, strFromDate, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlTasksStatusCount.add(String.valueOf(intQueryCount));
         }
         arlTasksStatusCount.add(String.valueOf(intTotalCount));

         intTotalCount = 0;
         arlTasksStatusCount.add("Current Open");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getCurrentOpenCountByPriority(AppConstants.TASK, strTicketPriority, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlTasksStatusCount.add(String.valueOf(intQueryCount));
         }
         arlTasksStatusCount.add(String.valueOf(intTotalCount));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTasksStatusCount;
   }

   private ArrayList<String> populateBugsStatusCount(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlBugsStatusCount = new ArrayList<>();
      try
      {
         int intQueryCount;
         int intTotalCount = 0;
         arlBugsStatusCount.add("Received");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getReceivedCountByPriority(AppConstants.BUG, strTicketPriority, strFromDate, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlBugsStatusCount.add(String.valueOf(intQueryCount));
         }
         arlBugsStatusCount.add(String.valueOf(intTotalCount));

         intTotalCount = 0;
         arlBugsStatusCount.add("Closed");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getClosedCountByPriority(AppConstants.BUG, strTicketPriority, strFromDate, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlBugsStatusCount.add(String.valueOf(intQueryCount));
         }
         arlBugsStatusCount.add(String.valueOf(intTotalCount));

         intTotalCount = 0;
         arlBugsStatusCount.add("Current Open");
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            intQueryCount = objSLAReportsDAO.getCurrentOpenCountByPriority(AppConstants.BUG, strTicketPriority, strToDate, objDBConnection);
            intTotalCount += intQueryCount;
            arlBugsStatusCount.add(String.valueOf(intQueryCount));
         }
         arlBugsStatusCount.add(String.valueOf(intTotalCount));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugsStatusCount;
   }

   private ArrayList<String> populateTasksSLA(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, ArrayList<String> arlHolidayCalendar, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTasksSLA = new ArrayList<>();
      try
      {
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            arlTasksSLA.add(String.valueOf(strTicketPriority));
            arlTasksSLA.add(objSLAReportsDAO.getInSLACountByPriority(AppConstants.TASK, strTicketPriority, strFromDate, strToDate, objDBConnection));
            arlTasksSLA.add(objSLAReportsDAO.getOutSLACountByPriority(AppConstants.TASK, strTicketPriority, strFromDate, strToDate, objDBConnection));
            arlTasksSLA.add(getResolutionTimeAverage(AppConstants.TASK, strTicketPriority, strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTasksSLA;
   }

   private ArrayList<String> populateBugsSLA(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, ArrayList<String> arlHolidayCalendar, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlBugsSLA = new ArrayList<>();
      try
      {
         for (String strTicketPriority : AppConstants.TICKET_PRIORITY)
         {
            arlBugsSLA.add(String.valueOf(strTicketPriority));
            arlBugsSLA.add(objSLAReportsDAO.getInSLACountByPriority(AppConstants.BUG, strTicketPriority, strFromDate, strToDate, objDBConnection));
            arlBugsSLA.add(objSLAReportsDAO.getOutSLACountByPriority(AppConstants.BUG, strTicketPriority, strFromDate, strToDate, objDBConnection));
            arlBugsSLA.add(getResolutionTimeAverage(AppConstants.BUG, strTicketPriority, strFromDate, strToDate, objSLAReportsDAO, arlHolidayCalendar, objDBConnection));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugsSLA;
   }

   private String getResolutionTimeAverage(String strCategory, String strPriority, String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, ArrayList<String> arlHolidayCalendar, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strTotalDaysSpentAverage;
      try
      {
         ArrayList<String> arlResolutionDates = objSLAReportsDAO.getResolutionDatesByPriority(strCategory, strPriority, strFromDate, strToDate, objDBConnection);
         String strActualEndDate;
         String strStartDate;
         int intTotalHours = 0;
         int intTotalMinutes = 0;
         double dblTotalDaysSpentAverage = 0;
         CalculateBusinessHours objCalculateBusinessHours = new CalculateBusinessHours();
         if (arlResolutionDates != null && !arlResolutionDates.isEmpty())
         {
            for (int intCount = 0; intCount < arlResolutionDates.size(); intCount += 2)
            {
               strStartDate = arlResolutionDates.get(intCount);
               strActualEndDate = arlResolutionDates.get(intCount + 1);
               if (strActualEndDate.length() > 0 && strStartDate.length() > 0)
               {
                  objCalculateBusinessHours.execute(strStartDate, strActualEndDate, arlHolidayCalendar);
                  intTotalHours += objCalculateBusinessHours.getTotalHours();
                  intTotalMinutes += objCalculateBusinessHours.getTotalMinutes();
               }
            }
            intTotalMinutes += intTotalHours * 60;
            dblTotalDaysSpentAverage = (Double.valueOf(intTotalMinutes) / Double.valueOf((24 * 60))) / (arlResolutionDates.size() / 2);
            DecimalFormat twoDForm = new DecimalFormat("#0.00");
            strTotalDaysSpentAverage = twoDForm.format(dblTotalDaysSpentAverage);
         }
         else
         {
            strTotalDaysSpentAverage = "0.00";
         }

      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strTotalDaysSpentAverage;
   }

   private ArrayList<String> populateCurrentTicketsStatus(String strTicketStatus, ReportsDAO objReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCurrentTicketsStatus = new ArrayList<>();
      try
      {
         ArrayList<String> arlCurrentTicketsTemp = objReportsDAO.getCurrentTicketsForStatus(strTicketStatus, objDBConnection);
         if (arlCurrentTicketsTemp != null && !arlCurrentTicketsTemp.isEmpty())
         {
            for (int intCount = 0; intCount < arlCurrentTicketsTemp.size(); intCount += 5)
            {
               arlCurrentTicketsStatus.add(arlCurrentTicketsTemp.get(intCount + 1));
               arlCurrentTicketsStatus.add(arlCurrentTicketsTemp.get(intCount + 2));
               arlCurrentTicketsStatus.add(arlCurrentTicketsTemp.get(intCount + 3));
               arlCurrentTicketsStatus.add(arlCurrentTicketsTemp.get(intCount + 4));

               ArrayList<String> arlCurrentStatusFromCommentsHistory = objReportsDAO.getCurrentStatusFromCommentsHistory(arlCurrentTicketsTemp.get(intCount), arlCurrentTicketsTemp.get(intCount + 1),
                       objDBConnection);
               ArrayList<String> arlCurrentStatusFromTicketHistory = objReportsDAO.getCurrentStatusFromTicketHistory(arlCurrentTicketsTemp.get(intCount), arlCurrentTicketsTemp.get(intCount + 1),
                       objDBConnection);

               String strCHDate = "";
               String strTHDate = "";
               if (arlCurrentStatusFromCommentsHistory != null && !arlCurrentStatusFromCommentsHistory.isEmpty())
               {
                  strCHDate = arlCurrentStatusFromCommentsHistory.get(1);
               }
               if (arlCurrentStatusFromTicketHistory != null && !arlCurrentStatusFromTicketHistory.isEmpty())
               {
                  strTHDate = arlCurrentStatusFromTicketHistory.get(1);
               }

               if (strCHDate.length() > 0 && strTHDate.length() > 0)
               {
                  DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_TIME_FORMAT);
                  Date CHdate = DEFAULT_DATE_FORMAT.parse(strCHDate);
                  Date THdate = DEFAULT_DATE_FORMAT.parse(strTHDate);
                  if (CHdate.compareTo(THdate) == 0 || CHdate.compareTo(THdate) > 0)
                  {
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(0));
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(1));
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(2));
                  }
                  else
                  {
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(0));
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(1));
                     arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(2));
                  }
               }
               else if (strCHDate.length() > 0)
               {
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(0));
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(1));
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromCommentsHistory.get(2));
               }
               else if (strTHDate.length() > 0)
               {
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(0));
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(1));
                  arlCurrentTicketsStatus.add(arlCurrentStatusFromTicketHistory.get(2));
               }
               else
               {
                  arlCurrentTicketsStatus.add("");
                  arlCurrentTicketsStatus.add("");
                  arlCurrentTicketsStatus.add("");
               }
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentTicketsStatus;

   }

   private ArrayList<String> populateOOSLAReasons(String strCategory, String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlOOSLAReasons = new ArrayList<>();
      try
      {
         ArrayList<String> arlOOSLATicketIds = objSLAReportsDAO.getClosedOOSLATicketIds(strCategory, strFromDate, strToDate, objDBConnection);
         if (arlOOSLATicketIds != null && !arlOOSLATicketIds.isEmpty())
         {
            ArrayList<String> arlTicketProgressHistory;
            ArrayList<String> arlTicketCommentsHistory;
            StringBuilder sbTicketComments;
            for (int intCount = 0; intCount < arlOOSLATicketIds.size(); intCount += 8)
            {
               sbTicketComments = new StringBuilder();
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 1));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 2));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 3));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 4));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 5));
               sbTicketComments.append(arlOOSLATicketIds.get(intCount + 6));
               arlTicketCommentsHistory = objSLAReportsDAO.getTicketCommentsHistory(arlOOSLATicketIds.get(intCount), arlOOSLATicketIds.get(intCount + 1), strToDate, objDBConnection);
               if (arlTicketCommentsHistory != null && !arlTicketCommentsHistory.isEmpty())
               {
                  for (String strComments : arlTicketCommentsHistory)
                  {
                     sbTicketComments.append("<br><br>").append(strComments);
                  }
               }
               arlTicketProgressHistory = objSLAReportsDAO.getTicketProgressHistory(arlOOSLATicketIds.get(intCount), arlOOSLATicketIds.get(intCount + 1), objDBConnection);
               if (arlTicketProgressHistory != null && !arlTicketProgressHistory.isEmpty())
               {
                  for (String strComments : arlTicketProgressHistory)
                  {
                     sbTicketComments.append("<br><br>").append(strComments);
                  }
               }
               arlOOSLAReasons.add(sbTicketComments.toString());
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 7));
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOOSLAReasons;
   }

   private ArrayList<String> populateCurrentOpenOOSLAReasons(String strCategory, String strToDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlOOSLAReasons = new ArrayList<>();
      try
      {
         ArrayList<String> arlOOSLATicketIds = objSLAReportsDAO.getCurrentOpenOOSLATicketIds(strCategory, strToDate, objDBConnection);
         if (arlOOSLATicketIds != null && !arlOOSLATicketIds.isEmpty())
         {
            ArrayList<String> arlTicketProgressHistory;
            ArrayList<String> arlTicketCommentsHistory;
            StringBuilder sbTicketComments;
            for (int intCount = 0; intCount < arlOOSLATicketIds.size(); intCount += 8)
            {
               sbTicketComments = new StringBuilder();
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 1));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 2));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 3));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 4));
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 5));
               //sbTicketComments.append(arlOOSLATicketIds.get(intCount + 6)).append("<br><br>");
               arlTicketCommentsHistory = objSLAReportsDAO.getTicketCommentsHistory(arlOOSLATicketIds.get(intCount), arlOOSLATicketIds.get(intCount + 1), strToDate, objDBConnection);
               boolean blnCheck = true;
               if (arlTicketCommentsHistory != null && !arlTicketCommentsHistory.isEmpty())
               {
                  for (String strComments : arlTicketCommentsHistory)
                  {
                     if (blnCheck)
                     {
                        sbTicketComments.append(strComments);
                     }
                     else
                     {
                        sbTicketComments.append("<br><br>").append(strComments);
                     }
                     blnCheck = false;
                  }
               }
               arlTicketProgressHistory = objSLAReportsDAO.getTicketProgressHistory(arlOOSLATicketIds.get(intCount), arlOOSLATicketIds.get(intCount + 1), objDBConnection);
               if (arlTicketProgressHistory != null && !arlTicketProgressHistory.isEmpty())
               {
                  for (String strComments : arlTicketProgressHistory)
                  {
                     sbTicketComments.append("<br><br>").append(strComments);
                  }
               }
               arlOOSLAReasons.add(sbTicketComments.toString());
               arlOOSLAReasons.add(arlOOSLATicketIds.get(intCount + 7));
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOOSLAReasons;
   }

   private ArrayList<String> populateResourceUtilization(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, ArrayList<String> arlHolidayCalendar, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlResourceUtilization = new ArrayList<>();
      try
      {
         ArrayList<String> arlTeam = objSLAReportsDAO.getTeams(objDBConnection);
         ArrayList<String> arlTicketHistoryTemp = objSLAReportsDAO.getTicketHistoryForMetrics(strFromDate, strToDate, objDBConnection);
         if (arlTeam != null && !arlTeam.isEmpty() && arlTicketHistoryTemp != null && !arlTicketHistoryTemp.isEmpty())
         {
            String strStartDate;
            String strEndDate;

            int intTotalTriagingMins;
            int intTriagingHours;
            int intTriagingMins;
            int intTotalFixingMins;
            int intFixingHours;
            int intFixingMins;

            double dblTriagingPercentage;
            double dblFixingPercentage;

            double dblTotalTriagingMins = 0;
            double dblTotalFixingMins = 0;

            double dblOverallTriagingPercentage = 0;
            double dblOverallFixingPercentage = 0;

            Date dtStartDate;
            Date dtEndDate;
            Date dtFromDate;
            Date dtToDate;

            DateFormat dt_DD_MM_YYYY = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            DateFormat dt_DD_MM_YYYY_HH_MM = new SimpleDateFormat(AppConstants.JAVA_DATE_TIME_FORMAT);
            dtFromDate = dt_DD_MM_YYYY.parse(strFromDate);
            dtToDate = dt_DD_MM_YYYY.parse(strToDate);

            DecimalFormat twoDForm = new DecimalFormat("##0.0");

            for (int intFirstCount = 0; intFirstCount < arlTeam.size(); intFirstCount += 2)
            {
               intTriagingHours = 0;
               intTriagingMins = 0;
               intFixingHours = 0;
               intFixingMins = 0;
               CalculateBusinessHours objCalculateBusinessHours;
               for (int intSecondCount = 0; intSecondCount < arlTicketHistoryTemp.size(); intSecondCount += 4)
               {
                  if (arlTeam.get(intFirstCount).equalsIgnoreCase(arlTicketHistoryTemp.get(intSecondCount)))
                  {
                     strStartDate = arlTicketHistoryTemp.get(intSecondCount + 2);
                     strEndDate = arlTicketHistoryTemp.get(intSecondCount + 3);
                     dtStartDate = dt_DD_MM_YYYY_HH_MM.parse(strStartDate);
                     if (dtStartDate.compareTo(dtFromDate) < 0)
                     {
                        strStartDate = AppUtil.convertDate(strFromDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT) + " 12:01 AM";
                     }

                     if ((strEndDate == null || strEndDate.length() == 0))
                     {
                        strEndDate = AppUtil.convertDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT) + " 11:59 PM";
                     }
                     else
                     {
                        dtEndDate = dt_DD_MM_YYYY_HH_MM.parse(strEndDate);
                        if (dtEndDate.compareTo(dtToDate) > 0)
                        {
                           strEndDate = AppUtil.convertDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT) + " 11:59 PM";
                        }
                     }
                     objCalculateBusinessHours = new CalculateBusinessHours();
                     if (strStartDate.length() > 0 && strEndDate.length() > 0)
                     {
                        if (arlTeam.get(intFirstCount).equalsIgnoreCase(arlTicketHistoryTemp.get(intSecondCount)) &&
                                arlTicketHistoryTemp.get(intSecondCount + 1).equalsIgnoreCase(AppConstants.TRIAGING_TEAM))
                        {
                           objCalculateBusinessHours.execute(strStartDate, strEndDate, arlHolidayCalendar);
                           intTriagingHours += objCalculateBusinessHours.getTotalHours();
                           intTriagingMins += objCalculateBusinessHours.getTotalMinutes();
                        }
                        if (arlTeam.get(intFirstCount).equalsIgnoreCase(arlTicketHistoryTemp.get(intSecondCount)) &&
                                arlTicketHistoryTemp.get(intSecondCount + 1).equalsIgnoreCase(AppConstants.FIXING_TEAM))
                        {
                           objCalculateBusinessHours.execute(strStartDate, strEndDate, arlHolidayCalendar);
                           intFixingHours += objCalculateBusinessHours.getTotalHours();
                           intFixingMins += objCalculateBusinessHours.getTotalMinutes();
                        }
                     }
                  }
               }
               intTotalTriagingMins = intTriagingMins + intTriagingHours * 60;
               intTotalFixingMins = intFixingMins + intFixingHours * 60;

               if (intTotalTriagingMins == 0 && intTotalFixingMins == 0)
               {
                  dblTriagingPercentage = 0;
                  dblFixingPercentage = 0;
               }
               else
               {
                  dblTriagingPercentage = (Double.valueOf(intTotalTriagingMins) / Double.valueOf(intTotalTriagingMins + intTotalFixingMins)) * 100;
                  dblFixingPercentage = (Double.valueOf(intTotalFixingMins) / Double.valueOf(intTotalTriagingMins + intTotalFixingMins)) * 100;
               }
               dblTotalTriagingMins += intTotalTriagingMins;
               dblTotalFixingMins += intTotalFixingMins;
               if (dblTriagingPercentage > 0 || dblFixingPercentage > 0)
               {
                  arlResourceUtilization.add(arlTeam.get(intFirstCount + 1));
                  arlResourceUtilization.add(twoDForm.format(dblTriagingPercentage));
                  arlResourceUtilization.add(twoDForm.format(dblFixingPercentage));
               }
            }
            if (dblTotalTriagingMins > 0 || dblTotalFixingMins > 0)
            {
               if (dblTotalTriagingMins > 0)
               {
                  dblOverallTriagingPercentage = (dblTotalTriagingMins / (dblTotalTriagingMins + dblTotalFixingMins)) * 100;
               }
               if (dblTotalFixingMins > 0)
               {
                  dblOverallFixingPercentage = (dblTotalFixingMins / (dblTotalTriagingMins + dblTotalFixingMins)) * 100;
               }

               arlResourceUtilization.add("Total");
               arlResourceUtilization.add(twoDForm.format(dblOverallTriagingPercentage));
               arlResourceUtilization.add(twoDForm.format(dblOverallFixingPercentage));
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResourceUtilization;

   }

   private ArrayList<String> populateBugsInflowFromTasks(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlBugsInflowFromTasks = new ArrayList<>();
      try
      {
         ArrayList<String> arlTemp = objSLAReportsDAO.getBugsInflowFromTasks(strFromDate, strToDate, objDBConnection);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            int intMSTeamCount = 0;
            int intAcquiringCount = 0;
            int intAPCount = 0;
            int intBillingProductCount = 0;
            for (String strMovedDomain : arlTemp)
            {
               if (strMovedDomain.length() == 0)
               {
                  intMSTeamCount++;
               }
               else if (strMovedDomain.equalsIgnoreCase(AppConstants.ACQUIRING))
               {
                  intAcquiringCount++;
               }
               else if (strMovedDomain.equalsIgnoreCase(AppConstants.AP))
               {
                  intAPCount++;
               }
               else if (strMovedDomain.equalsIgnoreCase(AppConstants.BILLING_PRODUCT))
               {
                  intBillingProductCount++;
               }
            }
            arlBugsInflowFromTasks.add(AppConstants.MS_TEAM);
            arlBugsInflowFromTasks.add(String.valueOf(intMSTeamCount));
            arlBugsInflowFromTasks.add(AppConstants.ACQUIRING);
            arlBugsInflowFromTasks.add(String.valueOf(intAcquiringCount));
            arlBugsInflowFromTasks.add(AppConstants.AP);
            arlBugsInflowFromTasks.add(String.valueOf(intAPCount));
            arlBugsInflowFromTasks.add(AppConstants.BILLING_PRODUCT);
            arlBugsInflowFromTasks.add(String.valueOf(intBillingProductCount));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugsInflowFromTasks;
   }

   public String loadOOSLAReasonsDummy() throws Exception
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

   public String loadOOSLAReasons() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            SLAReportsDAO objSLAReportsDAO = new SLAReportsDAO();
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("FromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("ToDate"));
            String strOOSLAReasonsAction = AppUtil.checkNull(objRequest.getParameter("OOSLAReasonsAction"));
            if (strOOSLAReasonsAction.equalsIgnoreCase("OpenTasks"))
            {
               ArrayList<String> arlCurrentOpenTasksOOSLAReasons = populateCurrentOpenOOSLAReasons(AppConstants.TASK, strToDate, objSLAReportsDAO, objDBConnection);
               objRequest.setAttribute("CurrentOpenTasksOOSLAReasons", arlCurrentOpenTasksOOSLAReasons);
               objRequest.setAttribute("MessageToDisplay", "OOSLA Reasons for Open Tasks As On (" + strToDate + ")");
            }
            else if (strOOSLAReasonsAction.equalsIgnoreCase("ClosedTasks"))
            {
               ArrayList<String> arlTasksOOSLAReasons = populateOOSLAReasons(AppConstants.TASK, strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               objRequest.setAttribute("TasksOOSLAReasons", arlTasksOOSLAReasons);
               objRequest.setAttribute("MessageToDisplay", "OOSLA Reasons for Closed Tasks For the Period " + strFromDate + " To " + strToDate);
            }
            else if (strOOSLAReasonsAction.equalsIgnoreCase("OpenBugs"))
            {
               ArrayList<String> arlCurrentOpenBugsOOSLAReasons = populateCurrentOpenOOSLAReasons(AppConstants.BUG, strToDate, objSLAReportsDAO, objDBConnection);
               objRequest.setAttribute("CurrentOpenBugsOOSLAReasons", arlCurrentOpenBugsOOSLAReasons);
               objRequest.setAttribute("MessageToDisplay", "OOSLA Reasons for Open Bugs As On (" + strToDate + ")");
            }
            else if (strOOSLAReasonsAction.equalsIgnoreCase("ClosedBugs"))
            {
               ArrayList<String> arlBugsOOSLAReasons = populateOOSLAReasons(AppConstants.BUG, strFromDate, strToDate, objSLAReportsDAO, objDBConnection);
               objRequest.setAttribute("BugsOOSLAReasons", arlBugsOOSLAReasons);
               objRequest.setAttribute("MessageToDisplay", "OOSLA Reasons for Closed Bugs For the Period " + strFromDate + " To " + strToDate);
            }
            objRequest.setAttribute("OOSLAReasonsAction", strOOSLAReasonsAction);
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

   private ArrayList<ArrayList<String>> populateWeekAndMonthSummary(String strFromDate, String strToDate, SLAReportsDAO objSLAReportsDAO, HttpServletRequest objRequest, UserInfo objUserInfo, Connection objDBConnection) throws Exception
   {
      ArrayList<ArrayList<String>> arlWeekAndMonthSummary = new ArrayList<>();
      ArrayList<String> arlWeekSummary = new ArrayList<>();
      ArrayList<String> arlWeekDisplayDates = new ArrayList<>();
      ArrayList<String> arlWeekResourceSummary = new ArrayList<>();
      ArrayList<String> arlMonthSummary = new ArrayList<>();
      ArrayList<String> arlMonthDisplayDates = new ArrayList<>();
      ArrayList<String> arlMonthResourceSummary = new ArrayList<>();
      ArrayList<Object> arlWeekChartData = new ArrayList<>();
      ArrayList<Object> arlWeekResourceClosureData = new ArrayList<>();
      ArrayList<Object> arlTaskResolutionWeekSummaryChart = new ArrayList<>();
      ArrayList<Object> arlBugResolutionWeekSummaryChart = new ArrayList<>();

      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         String strDateFormat = AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY;
         SimpleDateFormat objDateFormat = new SimpleDateFormat(strDateFormat);
         SimpleDateFormat objDisplayFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
         String strCurrentDateDDMMYYYY = AppUtil.getCurrentDate(strDateFormat);
         Calendar objCalendarCurrentDate = Calendar.getInstance();

         objCalendarCurrentDate.setTime(objDateFormat.parse(strCurrentDateDDMMYYYY));
         Date objCurrentDate = objCalendarCurrentDate.getTime();
         CommonDAO objCommonDAO = new CommonDAO();
         ArrayList<String> arlHolidayCalendar = objCommonDAO.getHolidayCalendarForCalc(objDBConnection);

         ArrayList<Object> arlWeekAndMonthDates = AppUtil.getWeekAndMonthDates(strFromDate, strToDate, strDateFormat);
         Date objFirstWeekDate = (Date) arlWeekAndMonthDates.get(0);
         Date objLastWeekDate = (Date) arlWeekAndMonthDates.get(1);
         ArrayList arlWeekFromToDates = (ArrayList) arlWeekAndMonthDates.get(2);
         Date objFirstMonthDate = (Date) arlWeekAndMonthDates.get(3);
         Date objLastMonthDate = (Date) arlWeekAndMonthDates.get(4);
         ArrayList arlMonthFromToDates = (ArrayList) arlWeekAndMonthDates.get(5);

         ArrayList<String> arlResourceWeekMatrix = objSLAReportsDAO.getResourceMatrix(objDateFormat.format(objFirstWeekDate),
                 objDateFormat.format(objLastWeekDate), objDBConnection);
         ArrayList<String> arlClosedAssigneesForWeeks = objSLAReportsDAO.getActiveAssigneesForGivenDates(objDBConnection);
         ArrayList<String> arlClosedTicketsForWeeks = objSLAReportsDAO.getClosedTicketsForGivenDates(objDateFormat.format(objFirstWeekDate),
                 objDateFormat.format(objLastWeekDate), objDBConnection);
         ArrayList<String> arlReceivedTicketsForWeeks = objSLAReportsDAO.getReceivedTicketsForGivenDates(objDateFormat.format(objFirstWeekDate),
                 objDateFormat.format(objLastWeekDate), objDBConnection);
         ArrayList<String> arlResourceCountForWeeks = objSLAReportsDAO.getResourceCountForGivenDates(objDateFormat.format(objFirstWeekDate),
                 objDateFormat.format(objLastWeekDate), objDBConnection);

         ArrayList<String> arlResourceMonthMatrix = objSLAReportsDAO.getResourceMatrix(objDateFormat.format(objFirstMonthDate),
                 objDateFormat.format(objLastMonthDate), objDBConnection);
         ArrayList<String> arlClosedAssigneesForMonths = objSLAReportsDAO.getActiveAssigneesForGivenDates(objDBConnection);
         ArrayList<String> arlClosedTicketsForMonths = objSLAReportsDAO.getClosedTicketsForGivenDates(objDateFormat.format(objFirstMonthDate),
                 objDateFormat.format(objLastMonthDate), objDBConnection);
         ArrayList<String> arlReceivedTicketsForMonths = objSLAReportsDAO.getReceivedTicketsForGivenDates(objDateFormat.format(objFirstMonthDate),
                 objDateFormat.format(objLastMonthDate), objDBConnection);
         ArrayList<String> arlResourceCountForMonths = objSLAReportsDAO.getResourceCountForGivenDates(objDateFormat.format(objFirstMonthDate),
                 objDateFormat.format(objLastMonthDate), objDBConnection);

         if (arlWeekFromToDates != null && !arlWeekFromToDates.isEmpty())
         {
            DecimalFormat twoDForm = new DecimalFormat("0.00");
            Date objWeekFromDate;
            Date objWeekToDate;
            String strResourceDate;
            int intResourceCount = 0;
            int intWeekDaysCount = 0;
            int intOverallDays = 0;
            int intWeekCount = 0;
            int intReceivedCount = 0;
            int intClosedCount = 0;
            int intOverallReceivedCount = 0;
            int intOverallClosedCount = 0;
            int intOpenCount = 0;
            double dblOverallResourceAverage = 0;
            double dblResourceAverage = 0;
            double dblInflowAverage = 0;
            double dblClosedAverage = 0;
            String strWeekDisplayDate = "";
            String strClosedDate = "";
            String strReceivedDate = "";
            ArrayList<String> arlTasksResolutioncount;
            ArrayList<String> arlBugsResolutioncount;
            ArrayList<String> arlBugsInflowFromTasks;

            for (int iCount1 = 0; iCount1 < arlWeekFromToDates.size(); iCount1 += 2)
            {
               intResourceCount = 0;
               intWeekDaysCount = 0;
               intReceivedCount = 0;
               intClosedCount = 0;
               dblClosedAverage = 0;

               objWeekFromDate = (Date) arlWeekFromToDates.get(iCount1);
               objWeekToDate = (Date) arlWeekFromToDates.get(iCount1 + 1);

               Calendar objTemp = Calendar.getInstance();
               objTemp.setTime(objWeekFromDate);
               objTemp.add(Calendar.DATE, 4);
               strWeekDisplayDate = objDisplayFormat.format(objTemp.getTime());
               strWeekDisplayDate = strWeekDisplayDate.substring(0, 2) + "<br>" + strWeekDisplayDate.substring(3, 6) + "<br>" + strWeekDisplayDate.substring(9, 11);

               Calendar objWeekCalendar = Calendar.getInstance();
               objWeekCalendar.setTime(objWeekFromDate);

               intOpenCount = objCommonDAO.getCurrentOpenCount(objDateFormat.format(objWeekToDate), objDBConnection);

               arlTasksResolutioncount = objSLAReportsDAO.getTaskResolutionTypesCount(AppConstants.TASK, objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objDBConnection);
               arlBugsResolutioncount = objSLAReportsDAO.getResolutionTypesCount(AppConstants.BUG, objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objDBConnection);
               arlBugsInflowFromTasks = populateBugsInflowFromTasks(objDateFormat.format(objWeekFromDate), objDateFormat.format(objWeekToDate), objSLAReportsDAO, objDBConnection);

               arlTaskResolutionWeekSummaryChart = generateTaskResolutionChartSummary(arlBugsInflowFromTasks, arlTasksResolutioncount, arlTaskResolutionWeekSummaryChart, strWeekDisplayDate.replaceAll("<br>", " "));
               arlBugResolutionWeekSummaryChart = generateBugResolutionChartSummary(arlBugsResolutioncount, arlBugResolutionWeekSummaryChart, strWeekDisplayDate.replaceAll("<br>", " "));

               while ((objWeekFromDate.compareTo(objWeekToDate) <= 0) && (objWeekFromDate.compareTo(objCurrentDate) <= 0))
               {
                  if (AppUtil.isWorkingDay(objDateFormat.format(objWeekFromDate), strDateFormat, arlHolidayCalendar))
                  {
                     for (int iCount2 = 0; iCount2 < arlResourceCountForWeeks.size(); iCount2 += 2)
                     {
                        strResourceDate = arlResourceCountForWeeks.get(iCount2);
                        if ((objWeekFromDate.compareTo(objDateFormat.parse(strResourceDate)) == 0))
                        {
                           intResourceCount += Integer.parseInt(arlResourceCountForWeeks.get(iCount2 + 1));
                        }
                     }
                  }
                  if (AppUtil.isWeekDay(objDateFormat.format(objWeekFromDate), strDateFormat))
                  {
                     intWeekDaysCount++;
                  }

                  for (int iCount3 = 0; iCount3 < arlReceivedTicketsForWeeks.size(); iCount3 += 2)
                  {
                     strReceivedDate = arlReceivedTicketsForWeeks.get(iCount3);
                     if (objWeekFromDate.compareTo(objDateFormat.parse(strReceivedDate)) == 0)
                     {
                        intReceivedCount++;
                     }
                  }

                  for (int iCount4 = 0; iCount4 < arlClosedTicketsForWeeks.size(); iCount4 += 2)
                  {
                     strClosedDate = arlClosedTicketsForWeeks.get(iCount4);
                     if (objWeekFromDate.compareTo(objDateFormat.parse(strClosedDate)) == 0)
                     {
                        intClosedCount++;
                     }
                  }

                  objWeekCalendar.add(Calendar.DATE, 1);
                  objWeekFromDate = objWeekCalendar.getTime();

               }

               dblResourceAverage = Double.valueOf(intResourceCount) / Double.valueOf(intWeekDaysCount);
               dblInflowAverage = Double.valueOf(intReceivedCount) / Double.valueOf(intWeekDaysCount);
               if (dblResourceAverage > 0)
               {
                  dblClosedAverage = Double.valueOf(intClosedCount) / Double.valueOf(dblResourceAverage);
               }

               arlWeekSummary.add(strWeekDisplayDate);
               arlWeekSummary.add(String.valueOf(intReceivedCount));
               arlWeekSummary.add(String.valueOf(intClosedCount));
               arlWeekSummary.add(twoDForm.format(dblInflowAverage));
               arlWeekSummary.add(twoDForm.format(dblResourceAverage));
               arlWeekSummary.add(twoDForm.format(dblClosedAverage));
               arlWeekSummary.add(String.valueOf(intOpenCount));

               arlWeekChartData.add(Double.valueOf(intReceivedCount));
               arlWeekChartData.add("Received");
               arlWeekChartData.add(strWeekDisplayDate.replaceAll("<br>", " "));

               arlWeekChartData.add(Double.valueOf(intClosedCount));
               arlWeekChartData.add("Closed");
               arlWeekChartData.add(strWeekDisplayDate.replaceAll("<br>", " "));

               arlWeekChartData.add(Double.valueOf(intOpenCount));
               arlWeekChartData.add("Open");
               arlWeekChartData.add(strWeekDisplayDate.replaceAll("<br>", " "));

               arlWeekResourceClosureData.add(dblResourceAverage);
               arlWeekResourceClosureData.add("Average Resource");
               arlWeekResourceClosureData.add(strWeekDisplayDate.replaceAll("<br>", " "));

               arlWeekResourceClosureData.add(dblClosedAverage);
               arlWeekResourceClosureData.add("Average Closure");
               arlWeekResourceClosureData.add(strWeekDisplayDate.replaceAll("<br>", " "));

               intOverallReceivedCount += intReceivedCount;
               intOverallClosedCount += intClosedCount;
               dblOverallResourceAverage += dblResourceAverage;
               intOverallDays += intWeekDaysCount;
               if (dblOverallResourceAverage > 0)
               {
                  intWeekCount++;
               }
            }

            arlWeekSummary.add("Total");
            arlWeekSummary.add(String.valueOf(intOverallReceivedCount) + "/" + twoDForm.format(Double.valueOf(intOverallReceivedCount) / Double.valueOf(intWeekCount)));
            arlWeekSummary.add(String.valueOf(intOverallClosedCount));
            arlWeekSummary.add(twoDForm.format(Double.valueOf(intOverallReceivedCount) / Double.valueOf(intOverallDays)));
            arlWeekSummary.add(twoDForm.format(dblOverallResourceAverage / Double.valueOf(intWeekCount)));
            arlWeekSummary.add(twoDForm.format(Double.valueOf(intOverallClosedCount) / dblOverallResourceAverage));
            arlWeekSummary.add("NA");

            arlWeekAndMonthSummary.add(arlWeekSummary);

            String strWeekSummaryChartPath = new LineChart().drawLineChart("WeekSummary", objUserInfo.getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Tickets Received vs Closed - Weekly Trend", "Week", "Count", arlWeekChartData);
            objRequest.setAttribute("WeekSummaryChartPath", strWeekSummaryChartPath);

            String strWeekResourceClosureChartPath = new LineChartDouble().drawLineChart("WeekResourcecSummary", objUserInfo.getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Resource vs Closed Average - Weekly Trend", "Week", "Count", arlWeekResourceClosureData);
            objRequest.setAttribute("WeekResourceClosureChartPath", strWeekResourceClosureChartPath);

            String strTaskResolutionWeekSummaryChartPath = new LineChart().drawLineChart("TaskResolutionWeekSummary", objUserInfo.getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Tasks Resolution Types - Weekly Trend", "Week", "Count", arlTaskResolutionWeekSummaryChart);
            objRequest.setAttribute("TaskResolutionWeekSummaryChartPath", strTaskResolutionWeekSummaryChartPath);

            String strBugResolutionWeekSummaryChartPath = new LineChart().drawLineChart("BugResolutionWeekSummary", objUserInfo.getUserId(), objRequest.getSession().getServletContext().getRealPath(""), "Bugs Resolution Types - Weekly Trend", "Week", "Count", arlBugResolutionWeekSummaryChart);
            objRequest.setAttribute("BugResolutionWeekSummaryChartPath", strBugResolutionWeekSummaryChartPath);

         }

         if (arlClosedAssigneesForWeeks != null && !arlClosedAssigneesForWeeks.isEmpty())
         {
            String strAssignee;
            String strAssigneeName;
            double dblResourceAvailableWeekAverage = 0;
            arlWeekDisplayDates.add("Resource Name");
            int intResourceAvailableDays = 0;
            for (int iCount = 0; iCount < arlClosedAssigneesForWeeks.size(); iCount += 2)
            {
               intResourceAvailableDays = 0;
               strAssignee = arlClosedAssigneesForWeeks.get(iCount);
               strAssigneeName = arlClosedAssigneesForWeeks.get(iCount + 1);
               if (objUserInfo.getAdmin() || objUserInfo.getUserId().equalsIgnoreCase(strAssignee))
               {
                  arlWeekResourceSummary.add(strAssigneeName);

                  if (arlWeekFromToDates != null && !arlWeekFromToDates.isEmpty())
                  {
                     DecimalFormat twoDForm = new DecimalFormat("0.00");
                     Date objWeekFromDate;
                     Date objWeekToDate;
                     int intClosedCount = 0;
                     String strClosedDate = "";
                     String strTicketAssignee = "";
                     String strWeekDisplayDate = "";
                     int intOverallClosedCount = 0;
                     int intWeekCount = 0;
                     double dblOverallAverage = 0;
                     int intWeekDaysCount = 0;

                     for (int iCount1 = 0; iCount1 < arlWeekFromToDates.size(); iCount1 += 2)
                     {
                        intClosedCount = 0;

                        objWeekFromDate = (Date) arlWeekFromToDates.get(iCount1);
                        objWeekToDate = (Date) arlWeekFromToDates.get(iCount1 + 1);

                        Calendar objTemp = Calendar.getInstance();
                        objTemp.setTime(objWeekFromDate);
                        objTemp.add(Calendar.DATE, 4);
                        strWeekDisplayDate = objDisplayFormat.format(objTemp.getTime());
                        strWeekDisplayDate = strWeekDisplayDate.substring(0, 2) + "<br>" + strWeekDisplayDate.substring(3, 6) + "<br>" + strWeekDisplayDate.substring(9, 11);
                        if (!arlWeekDisplayDates.contains(strWeekDisplayDate))
                        {
                           arlWeekDisplayDates.add(strWeekDisplayDate);
                        }

                        Calendar objWeekCalendar = Calendar.getInstance();
                        objWeekCalendar.setTime(objWeekFromDate);

                        while ((objWeekFromDate.compareTo(objWeekToDate) <= 0) && (objWeekFromDate.compareTo(objCurrentDate) <= 0))
                        {
                           for (int iCount2 = 0; iCount2 < arlClosedTicketsForWeeks.size(); iCount2 += 2)
                           {
                              strClosedDate = arlClosedTicketsForWeeks.get(iCount2);
                              strTicketAssignee = arlClosedTicketsForWeeks.get(iCount2 + 1);
                              if (objWeekFromDate.compareTo(objDateFormat.parse(strClosedDate)) == 0 && strAssignee.equalsIgnoreCase(strTicketAssignee))
                              {
                                 intClosedCount++;
                              }
                           }
                           if (AppUtil.isWorkingDay(objDateFormat.format(objWeekFromDate), strDateFormat, arlHolidayCalendar))
                           {
                              intWeekDaysCount++;
                              for (int iCount3 = 0; iCount3 < arlResourceWeekMatrix.size(); iCount3 += 3)
                              {
                                 if ((objWeekFromDate.compareTo(objDateFormat.parse(arlResourceWeekMatrix.get(iCount3 + 1))) == 0) &&
                                         strAssignee.equalsIgnoreCase(arlResourceWeekMatrix.get(iCount3)) &&
                                         arlResourceWeekMatrix.get(iCount3 + 2).equalsIgnoreCase("Y"))
                                 {
                                    intResourceAvailableDays++;
                                 }
                              }
                           }

                           objWeekCalendar.add(Calendar.DATE, 1);
                           objWeekFromDate = objWeekCalendar.getTime();

                        }

                        arlWeekResourceSummary.add(String.valueOf(intClosedCount));

                        intOverallClosedCount += intClosedCount;
                        intWeekCount++;
                     }
                     dblResourceAvailableWeekAverage = (Double.valueOf(intResourceAvailableDays) / Double.valueOf(intWeekDaysCount)) * Double.valueOf(intWeekCount);
                     arlWeekResourceSummary.add(String.valueOf(intOverallClosedCount));

                     if (dblResourceAvailableWeekAverage > 0)
                     {
                        dblOverallAverage = Double.valueOf(intOverallClosedCount) / Double.valueOf(dblResourceAvailableWeekAverage);
                     }
                     else
                     {
                        dblOverallAverage = 0;
                     }
                     arlWeekResourceSummary.add(twoDForm.format(dblOverallAverage));
                  }
               }
            }
            arlWeekDisplayDates.add("Total");
            arlWeekDisplayDates.add("Average");
         }

         if (arlMonthFromToDates != null && !arlMonthFromToDates.isEmpty())
         {
            DecimalFormat twoDForm = new DecimalFormat("0.00");
            Date objMonthFromDate;
            Date objMonthToDate;
            String strResourceDate;
            int intResourceCount = 0;
            int intMonthDaysCount = 0;
            int intMonthCount = 0;
            int intOverallDays = 0;
            int intReceivedCount = 0;
            int intClosedCount = 0;
            int intOverallReceivedCount = 0;
            int intOverallClosedCount = 0;
            double dblOverallResourceAverage = 0;
            double dblResourceAverage = 0;
            double dblInflowAverage = 0;
            double dblClosedAverage = 0;
            String strMonthDisplayDate = "";
            String strClosedDate = "";
            String strReceivedDate = "";

            for (int iCount1 = 0; iCount1 < arlMonthFromToDates.size(); iCount1 += 2)
            {
               intResourceCount = 0;
               intMonthDaysCount = 0;
               intReceivedCount = 0;
               intClosedCount = 0;
               dblClosedAverage = 0;
               dblInflowAverage = 0;

               objMonthFromDate = (Date) arlMonthFromToDates.get(iCount1);
               objMonthToDate = (Date) arlMonthFromToDates.get(iCount1 + 1);

               strMonthDisplayDate = objDisplayFormat.format(objMonthFromDate);
               strMonthDisplayDate = strMonthDisplayDate.substring(3, 6) + "<br>" + strMonthDisplayDate.substring(9, 11);
               Calendar objMonthCalendar = Calendar.getInstance();
               objMonthCalendar.setTime(objMonthFromDate);

               while ((objMonthFromDate.compareTo(objMonthToDate) <= 0) && (objMonthFromDate.compareTo(objCurrentDate) <= 0))
               {
                  if (AppUtil.isWorkingDay(objDateFormat.format(objMonthFromDate), strDateFormat, arlHolidayCalendar))
                  {
                     for (int iCount2 = 0; iCount2 < arlResourceCountForMonths.size(); iCount2 += 2)
                     {
                        strResourceDate = arlResourceCountForMonths.get(iCount2);
                        if ((objMonthFromDate.compareTo(objDateFormat.parse(strResourceDate)) == 0))
                        {
                           intResourceCount += Integer.parseInt(arlResourceCountForMonths.get(iCount2 + 1));
                        }
                     }
                  }
                  if (AppUtil.isWeekDay(objDateFormat.format(objMonthFromDate), strDateFormat))
                  {
                     intMonthDaysCount++;
                  }
                  for (int iCount3 = 0; iCount3 < arlReceivedTicketsForMonths.size(); iCount3 += 2)
                  {
                     strReceivedDate = arlReceivedTicketsForMonths.get(iCount3);
                     if (objMonthFromDate.compareTo(objDateFormat.parse(strReceivedDate)) == 0)
                     {
                        intReceivedCount++;
                     }
                  }
                  for (int iCount4 = 0; iCount4 < arlClosedTicketsForMonths.size(); iCount4 += 2)
                  {
                     strClosedDate = arlClosedTicketsForMonths.get(iCount4);
                     if (objMonthFromDate.compareTo(objDateFormat.parse(strClosedDate)) == 0)
                     {
                        intClosedCount++;
                     }
                  }
                  objMonthCalendar.add(Calendar.DATE, 1);
                  objMonthFromDate = objMonthCalendar.getTime();
               }

               dblResourceAverage = Double.valueOf(intResourceCount) / Double.valueOf(intMonthDaysCount);
               dblInflowAverage = Double.valueOf(intReceivedCount) / Double.valueOf(intMonthDaysCount);
               if (dblResourceAverage > 0)
               {
                  dblClosedAverage = Double.valueOf(intClosedCount) / Double.valueOf(dblResourceAverage);
               }

               arlMonthSummary.add(strMonthDisplayDate);
               arlMonthSummary.add(String.valueOf(intReceivedCount));
               arlMonthSummary.add(String.valueOf(intClosedCount));
               arlMonthSummary.add(twoDForm.format(dblInflowAverage));
               arlMonthSummary.add(twoDForm.format(dblResourceAverage));
               arlMonthSummary.add(twoDForm.format(dblClosedAverage));
               arlMonthSummary.add(String.valueOf(objCommonDAO.getCurrentOpenCount(objDateFormat.format(objMonthToDate), objDBConnection)));

               intOverallReceivedCount += intReceivedCount;
               intOverallClosedCount += intClosedCount;
               dblOverallResourceAverage += dblResourceAverage;
               intOverallDays += intMonthDaysCount;
               if (dblOverallResourceAverage > 0)
               {
                  intMonthCount++;
               }

            }

            arlMonthSummary.add("Total");
            arlMonthSummary.add(String.valueOf(intOverallReceivedCount) + "/" + twoDForm.format(Double.valueOf(intOverallReceivedCount) / Double.valueOf(intMonthCount)));
            arlMonthSummary.add(String.valueOf(intOverallClosedCount));
            arlMonthSummary.add(twoDForm.format(Double.valueOf(intOverallReceivedCount) / Double.valueOf(intOverallDays)));
            arlMonthSummary.add(twoDForm.format(dblOverallResourceAverage / Double.valueOf(intMonthCount)));
            arlMonthSummary.add(twoDForm.format(Double.valueOf(intOverallClosedCount) / dblOverallResourceAverage));
            arlMonthSummary.add("NA");

            arlWeekAndMonthSummary.add(arlMonthSummary);
         }

         if (arlClosedAssigneesForMonths != null && !arlClosedAssigneesForMonths.isEmpty())
         {
            String strAssignee;
            String strAssigneeName;
            double dblResourceAvailableMonthAverage = 0;
            arlMonthDisplayDates.add("Resource Name");
            int intResourceAvailableDays = 0;
            for (int iCount = 0; iCount < arlClosedAssigneesForMonths.size(); iCount += 2)
            {
               intResourceAvailableDays = 0;
               strAssignee = arlClosedAssigneesForMonths.get(iCount);
               strAssigneeName = arlClosedAssigneesForMonths.get(iCount + 1);
               //arlMonthResourceSummary.add(strAssignee);
               if (objUserInfo.getAdmin() || objUserInfo.getUserId().equalsIgnoreCase(strAssignee))
               {
                  arlMonthResourceSummary.add(strAssigneeName);

                  if (arlMonthFromToDates != null && !arlMonthFromToDates.isEmpty())
                  {
                     DecimalFormat twoDForm = new DecimalFormat("0.00");
                     Date objMonthFromDate;
                     Date objMonthToDate;
                     int intClosedCount = 0;
                     String strClosedDate = "";
                     String strTicketAssignee = "";
                     String strMonthDisplayDate = "";
                     int intOverallClosedCount = 0;
                     int intMonthCount = 0;
                     double dblOverallAverage = 0;
                     int intMonthDaysCount = 0;

                     for (int iCount1 = 0; iCount1 < arlMonthFromToDates.size(); iCount1 += 2)
                     {
                        intClosedCount = 0;

                        objMonthFromDate = (Date) arlMonthFromToDates.get(iCount1);
                        objMonthToDate = (Date) arlMonthFromToDates.get(iCount1 + 1);

                        Calendar objTemp = Calendar.getInstance();
                        objTemp.setTime(objMonthFromDate);
                        objTemp.add(Calendar.DATE, 4);
                        strMonthDisplayDate = objDisplayFormat.format(objTemp.getTime());
                        strMonthDisplayDate = strMonthDisplayDate.substring(3, 6) + "<br>" + strMonthDisplayDate.substring(9, 11);
                        if (!arlMonthDisplayDates.contains(strMonthDisplayDate))
                        {
                           arlMonthDisplayDates.add(strMonthDisplayDate);
                        }

                        Calendar objMonthCalendar = Calendar.getInstance();
                        objMonthCalendar.setTime(objMonthFromDate);

                        while ((objMonthFromDate.compareTo(objMonthToDate) <= 0) && (objMonthFromDate.compareTo(objCurrentDate) <= 0))
                        {
                           for (int iCount2 = 0; iCount2 < arlClosedTicketsForMonths.size(); iCount2 += 2)
                           {
                              strClosedDate = arlClosedTicketsForMonths.get(iCount2);
                              strTicketAssignee = arlClosedTicketsForMonths.get(iCount2 + 1);
                              if (objMonthFromDate.compareTo(objDateFormat.parse(strClosedDate)) == 0 && strAssignee.equalsIgnoreCase(strTicketAssignee))
                              {
                                 intClosedCount++;
                              }
                           }
                           if (AppUtil.isWorkingDay(objDateFormat.format(objMonthFromDate), strDateFormat, arlHolidayCalendar))
                           {
                              intMonthDaysCount++;
                              for (int iCount3 = 0; iCount3 < arlResourceMonthMatrix.size(); iCount3 += 3)
                              {
                                 if ((objMonthFromDate.compareTo(objDateFormat.parse(arlResourceMonthMatrix.get(iCount3 + 1))) == 0) &&
                                         strAssignee.equalsIgnoreCase(arlResourceMonthMatrix.get(iCount3)) &&
                                         arlResourceMonthMatrix.get(iCount3 + 2).equalsIgnoreCase("Y"))
                                 {
                                    intResourceAvailableDays++;
                                 }
                              }
                           }

                           objMonthCalendar.add(Calendar.DATE, 1);
                           objMonthFromDate = objMonthCalendar.getTime();

                        }

                        arlMonthResourceSummary.add(String.valueOf(intClosedCount));

                        intOverallClosedCount += intClosedCount;
                        intMonthCount++;
                     }
                     dblResourceAvailableMonthAverage = (Double.valueOf(intResourceAvailableDays) / Double.valueOf(intMonthDaysCount)) * Double.valueOf(intMonthCount);
                     arlMonthResourceSummary.add(String.valueOf(intOverallClosedCount));
                     if (dblResourceAvailableMonthAverage > 0)
                     {
                        dblOverallAverage = Double.valueOf(intOverallClosedCount) / Double.valueOf(dblResourceAvailableMonthAverage);
                     }
                     else
                     {
                        dblOverallAverage = 0;
                     }
                     arlMonthResourceSummary.add(twoDForm.format(dblOverallAverage));
                  }
               }
            }
            arlMonthDisplayDates.add("Total");
            arlMonthDisplayDates.add("Average");
         }

         arlWeekAndMonthSummary.add(arlWeekDisplayDates);
         arlWeekAndMonthSummary.add(arlWeekResourceSummary);
         arlWeekAndMonthSummary.add(arlMonthDisplayDates);
         arlWeekAndMonthSummary.add(arlMonthResourceSummary);

      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlWeekAndMonthSummary;
   }

   private ArrayList<Object> generateTaskResolutionChartSummary(ArrayList<String> arlBugsInflowFromTasks, ArrayList<String> arlTasksResolutioncount, ArrayList<Object> arlTaskResolutionWeekSummaryChart, String strWeekDisplayDate) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         int intBugsInflowToSystem = 0;
         int intOtherTeamBugs = 0;
         int intImprovements = 0;
         int intOthers = 0;
         int intDuplicate = 0;
         int intInvalid = 0;
         if (arlBugsInflowFromTasks != null && !arlBugsInflowFromTasks.isEmpty())
         {
            for (int intCount = 0; intCount < arlBugsInflowFromTasks.size(); intCount += 2)
            {
               intBugsInflowToSystem += Integer.parseInt(arlBugsInflowFromTasks.get(intCount + 1));
            }
         }
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intBugsInflowToSystem));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_ACTUAL_BUGS);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);

         for (int intCount = 0; intCount < arlTasksResolutioncount.size(); intCount += 2)
         {
            if (arlTasksResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.TASK_CONVERTED_BUG_MOVED))
            {
               intOtherTeamBugs += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.MOVED_TO_IMPROVEMENT))
            {
               intImprovements += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_INVALID))
            {
               intInvalid += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_DUPLICATE))
            {
               intDuplicate += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else
            {
               intOthers += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
         }
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intOtherTeamBugs));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHER_TEAM_BUGS);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intImprovements));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_IMPROVEMENTS);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intInvalid));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_INVALID);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intDuplicate));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_DUPLICATE);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intOthers));
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHERS);
         arlTaskResolutionWeekSummaryChart.add(strWeekDisplayDate);
      }

      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTaskResolutionWeekSummaryChart;
   }

   private ArrayList<Object> generateTaskResolutionChart(ArrayList<String> arlBugsInflowFromTasks, ArrayList<String> arlTasksResolutioncount, ArrayList<Object> arlTaskResolutionWeekSummaryChart, String strWeekDisplayDate) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         int intBugsInflowToSystem = 0;
         int intOtherTeamBugs = 0;
         int intImprovements = 0;
         int intOthers = 0;
         int intDuplicate = 0;
         int intInvalid = 0;
         if (arlBugsInflowFromTasks != null && !arlBugsInflowFromTasks.isEmpty())
         {
            for (int intCount = 0; intCount < arlBugsInflowFromTasks.size(); intCount += 2)
            {
               intBugsInflowToSystem += Integer.parseInt(arlBugsInflowFromTasks.get(intCount + 1));
            }
         }
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_ACTUAL_BUGS);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intBugsInflowToSystem));

         for (int intCount = 0; intCount < arlTasksResolutioncount.size(); intCount += 2)
         {
            if (arlTasksResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.TASK_CONVERTED_BUG_MOVED))
            {
               intOtherTeamBugs += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.MOVED_TO_IMPROVEMENT))
            {
               intImprovements += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_INVALID))
            {
               intInvalid += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else if (arlTasksResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_DUPLICATE))
            {
               intDuplicate += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
            else
            {
               intOthers += Integer.parseInt(arlTasksResolutioncount.get(intCount + 1));
            }
         }
         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHER_TEAM_BUGS);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intOtherTeamBugs));

         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_IMPROVEMENTS);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intImprovements));

         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_INVALID);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intInvalid));

         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_DUPLICATE);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intDuplicate));

         arlTaskResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHERS);
         arlTaskResolutionWeekSummaryChart.add(Double.valueOf(intOthers));
      }

      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTaskResolutionWeekSummaryChart;
   }


   private ArrayList<Object> generateBugResolutionChartSummary(ArrayList<String> arlBugsResolutioncount, ArrayList<Object> arlBugResolutionWeekSummaryChart, String strWeekDisplayDate) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         int intCodeFix = 0;
         int intOtherTeamBugs = 0;
         int intImprovements = 0;
         int intOthers = 0;
         int intDuplicate = 0;
         int intInvalid = 0;
         for (int intCount = 0; intCount < arlBugsResolutioncount.size(); intCount += 2)
         {
            if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.CODE_FIXED_DELIVERED))
            {
               intCodeFix += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.BUG_MOVED))
            {
               intOtherTeamBugs += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.MOVED_TO_IMPROVEMENT))
            {
               intImprovements += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_INVALID))
            {
               intInvalid += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_DUPLICATE))
            {
               intDuplicate += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else
            {
               intOthers += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
         }
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intCodeFix));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_CODEFIX);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intOtherTeamBugs));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHER_TEAM_BUGS);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intImprovements));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_IMPROVEMENTS);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intInvalid));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_INVALID);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intDuplicate));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_DUPLICATE);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);

         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intOthers));
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHERS);
         arlBugResolutionWeekSummaryChart.add(strWeekDisplayDate);
      }

      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugResolutionWeekSummaryChart;
   }

   private ArrayList<Object> generateBugResolutionChart(ArrayList<String> arlBugsResolutioncount, ArrayList<Object> arlBugResolutionWeekSummaryChart, String strWeekDisplayDate) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         int intCodeFix = 0;
         int intOtherTeamBugs = 0;
         int intImprovements = 0;
         int intOthers = 0;
         int intDuplicate = 0;
         int intInvalid = 0;
         for (int intCount = 0; intCount < arlBugsResolutioncount.size(); intCount += 2)
         {
            if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.CODE_FIXED_DELIVERED))
            {
               intCodeFix += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.BUG_MOVED))
            {
               intOtherTeamBugs += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).equalsIgnoreCase(AppConstants.MOVED_TO_IMPROVEMENT))
            {
               intImprovements += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_INVALID))
            {
               intInvalid += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else if (arlBugsResolutioncount.get(intCount).contains(AppConstants.RESOLUTION_DUPLICATE))
            {
               intDuplicate += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
            else
            {
               intOthers += Integer.parseInt(arlBugsResolutioncount.get(intCount + 1));
            }
         }
         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_CODEFIX);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intCodeFix));

         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHER_TEAM_BUGS);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intOtherTeamBugs));

         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_IMPROVEMENTS);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intImprovements));

         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_INVALID);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intInvalid));

         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_DUPLICATE);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intDuplicate));

         arlBugResolutionWeekSummaryChart.add(AppConstants.RESOL_OTHERS);
         arlBugResolutionWeekSummaryChart.add(Double.valueOf(intOthers));
      }

      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugResolutionWeekSummaryChart;
   }

}
