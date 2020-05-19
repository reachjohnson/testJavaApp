package com.opt.reports.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.reports.dao.ReportsDAO;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.tickets.util.TicketHistory;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.CalculateBusinessHours;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ReportsAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(ReportsAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String TicketList() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ReportsDAO objReportsDAO = new ReportsDAO();
            ArrayList<String> arlAssignees = objReportsDAO.getOpenTicketsAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strTicketStatus = AppUtil.checkNull(objRequest.getParameter("TicketStatus"));
            String strTicketPriority = AppUtil.checkNull(objRequest.getParameter("TicketPriority"));
            String strTicketModule = AppUtil.checkNull(objRequest.getParameter("TicketModule"));
            if (strAssignee.length() == 0)
            {
               strAssignee = AppConstants.ALL_ASSIGNEES;
            }
            if (strTicketStatus.length() == 0)
            {
               strTicketStatus = AppConstants.ALL_STATUS;
            }
            if (strTicketPriority.length() == 0)
            {
               strTicketPriority = AppConstants.ALL_PRIORITY;
            }
            if (strTicketModule.length() == 0)
            {
               strTicketModule = AppConstants.ALL_MODULE;
            }

            if (strAssignee.length() > 0 && strTicketStatus.length() > 0 && strTicketPriority.length() > 0 && strTicketModule.length() > 0)
            {
               ArrayList<String> arlTasksDetails = objReportsDAO.getTicketList(AppConstants.TASK, strAssignee, strTicketStatus, strTicketPriority, strTicketModule, objDBConnection);
               ArrayList<String> arlBugsDetails = objReportsDAO.getTicketList(AppConstants.BUG, strAssignee, strTicketStatus, strTicketPriority, strTicketModule, objDBConnection);
               ArrayList<String> arlOpenTicketsETAHistory = new CommonDAO().getOpenTicketsETAHistory(AppConstants.ALL_ASSIGNEES, objDBConnection);
               ArrayList<Object> arlPopulatedETAHistory = objWebUtil.populatedETAHistory(arlOpenTicketsETAHistory);

               objRequest.setAttribute("TasksDetails", arlTasksDetails);
               objRequest.setAttribute("BugsDetails", arlBugsDetails);
               String strAssigneeName = "";
               if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
               {
                  strAssigneeName = AppConstants.ALL_ASSIGNEES;
               }
               else
               {
                  strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);
               }
               objRequest.setAttribute("DashBoardName", strAssigneeName);
               objRequest.setAttribute("Assignee", strAssignee);
               objRequest.setAttribute("TicketStatus", strTicketStatus);
               objRequest.setAttribute("TicketPriority", strTicketPriority);
               objRequest.setAttribute("TicketModule", strTicketModule);

               ArrayList<String> arlSLAMissingTicketDetails = new TicketsDAO().getSLAMissingTickets(strAssignee, objDBConnection);
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
               objRequest.setAttribute("OpenTicketsETAHistory", arlPopulatedETAHistory);
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

   public String closedTicketList() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new ReportsDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);

            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("ClosedTicketFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("ClosedTicketToDate"));
            if (strAssignee.length() == 0)
            {
               strAssignee = AppConstants.ALL_ASSIGNEES;
            }
            if (strToDate == null || strToDate.length() == 0)
            {
               strToDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate == null || strFromDate.length() == 0)
            {
               strFromDate = AppUtil.getMondayDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strAssignee.length() > 0 && strFromDate.length() > 0 && strToDate.length() > 0)
            {
               ArrayList<String> arlTicketDetails = new ReportsDAO().getClosedTicketList(strAssignee, strFromDate, strToDate, objDBConnection);
               objRequest.setAttribute("TicketDetails", arlTicketDetails);
               String strAssigneeName = "";
               if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
               {
                  strAssigneeName = AppConstants.ALL_ASSIGNEES;
               }
               else
               {
                  strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);
               }
               objRequest.setAttribute("DashBoardName", strAssigneeName);
               objRequest.setAttribute("Assignee", strAssignee);
               objRequest.setAttribute("FromDate", strFromDate);
               objRequest.setAttribute("ToDate", strToDate);
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

   public String SLAMissedTickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlSLAMissedTickets = new ReportsDAO().getSLAMissedTickets("", objDBConnection);
            objRequest.setAttribute("SLAMissedTickets", arlSLAMissedTickets);
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

   public String ListOfHolidays() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlListOfHolidays = new CommonDAO().getHolidayCalendar(objDBConnection);
            objRequest.setAttribute("LISTOFHOLIDAYS", arlListOfHolidays);
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

   public String ResourceAllocation() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlTriagingResourceAllocation = new ArrayList<>();
            ArrayList<String> arlFixingResourceAllocation = new ArrayList<>();
            ArrayList<String> arlFeatureResourceAllocation = new ArrayList<>();
            ArrayList<String> arlQAResourceAllocation = new ArrayList<>();
            ArrayList<String> arlRQAResourceAllocation = new ArrayList<>();
            ReportsDAO objReportsDAO = new ReportsDAO();
            ArrayList<String> arlAllAssignees = new CommonDAO().getAllAssignees(objDBConnection);
            if (arlAllAssignees != null && !arlAllAssignees.isEmpty())
            {
               ArrayList<Object> arlOpenTicketsCount = new ArrayList<>();

               for (int intCounter = 0; intCounter < arlAllAssignees.size(); intCounter += 2)
               {
                  arlOpenTicketsCount.add(arlAllAssignees.get(intCounter + 1));
                  arlOpenTicketsCount.add(objReportsDAO.getOpenTicketsCount(arlAllAssignees.get(intCounter), objDBConnection));
               }
               objRequest.setAttribute("OpenTicketsCount", arlOpenTicketsCount);
            }
            ArrayList<String> arlTriagingTeam = objReportsDAO.getTeamAllocation(AppConstants.TRIAGING_TEAM, objDBConnection);
            ArrayList<String> arlFixingTeam = objReportsDAO.getTeamAllocation(AppConstants.FIXING_TEAM, objDBConnection);
            ArrayList<String> arlFeatureTeam = objReportsDAO.getTeamAllocation(AppConstants.FEATURE_TEAM, objDBConnection);
            ArrayList<String> arlQATeam = objReportsDAO.getTeamAllocation(AppConstants.QA_TEAM, objDBConnection);
            ArrayList<String> arlRQATeam = objReportsDAO.getTeamAllocation(AppConstants.RQA_TEAM, objDBConnection);
            if (arlAllAssignees != null && !arlAllAssignees.isEmpty())
            {
               ArrayList<String> arlTicketsForResourceAllocation = objReportsDAO.getTicketsForResourceAllocation(objDBConnection);
               if (arlTicketsForResourceAllocation != null && !arlTicketsForResourceAllocation.isEmpty())
               {
                  String strUserId = "";
                  String strAssigneeName = "";
                  String strTicketCategory = "";
                  String strTicketStatus = "";
                  int taskNotStarted = 0;
                  int taskInProgress = 0;
                  int taskOnHold = 0;
                  int taskSLAMissed = 0;
                  int bugNotStarted = 0;
                  int bugInProgress = 0;
                  int bugOnHold = 0;
                  int bugSLAMissed = 0;
                  String strIsLeave = "";
                  ArrayList<String> arlLeave = objReportsDAO.getCurrentLeave(objDBConnection);
                  for (int intFirstCounter = 0; intFirstCounter < arlAllAssignees.size(); intFirstCounter += 2)
                  {
                     strUserId = arlAllAssignees.get(intFirstCounter);
                     strAssigneeName = arlAllAssignees.get(intFirstCounter + 1);
                     taskNotStarted = 0;
                     taskInProgress = 0;
                     taskOnHold = 0;
                     taskSLAMissed = 0;
                     bugNotStarted = 0;
                     bugInProgress = 0;
                     bugOnHold = 0;
                     bugSLAMissed = 0;
                     for (int intSecondCounter = 0; intSecondCounter < arlTicketsForResourceAllocation.size(); intSecondCounter += 4)
                     {
                        if (strUserId.equalsIgnoreCase(arlTicketsForResourceAllocation.get(intSecondCounter)))
                        {
                           if (arlTicketsForResourceAllocation.get(intSecondCounter + 1).equalsIgnoreCase(AppConstants.TASK))
                           {
                              if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.NOT_STARTED))
                                 taskNotStarted++;
                              else if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                                 taskInProgress++;
                              else if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.ON_HOLD))
                                 taskOnHold++;
                              if (arlTicketsForResourceAllocation.get(intSecondCounter + 3).equalsIgnoreCase("1"))
                                 taskSLAMissed++;
                           }
                           else if (arlTicketsForResourceAllocation.get(intSecondCounter + 1).equalsIgnoreCase(AppConstants.BUG))
                           {
                              if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.NOT_STARTED))
                                 bugNotStarted++;
                              else if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                                 bugInProgress++;
                              else if (arlTicketsForResourceAllocation.get(intSecondCounter + 2).equalsIgnoreCase(AppConstants.ON_HOLD))
                                 bugOnHold++;
                              if (arlTicketsForResourceAllocation.get(intSecondCounter + 3).equalsIgnoreCase("1"))
                                 bugSLAMissed++;
                           }
                        }
                     }
                     if (arlLeave != null && !arlLeave.isEmpty() && arlLeave.contains(strUserId))
                     {
                        strIsLeave = "Yes";
                     }
                     else
                     {
                        strIsLeave = "No";
                     }

                     if (arlTriagingTeam.contains(strUserId))
                     {
                        arlTriagingResourceAllocation.add(strAssigneeName);
                        arlTriagingResourceAllocation.add(String.valueOf(taskNotStarted));
                        arlTriagingResourceAllocation.add(String.valueOf(taskInProgress));
                        arlTriagingResourceAllocation.add(String.valueOf(taskOnHold));
                        arlTriagingResourceAllocation.add(String.valueOf(taskSLAMissed));
                        arlTriagingResourceAllocation.add(String.valueOf(bugNotStarted));
                        arlTriagingResourceAllocation.add(String.valueOf(bugInProgress));
                        arlTriagingResourceAllocation.add(String.valueOf(bugOnHold));
                        arlTriagingResourceAllocation.add(String.valueOf(bugSLAMissed));
                        arlTriagingResourceAllocation.add(String.valueOf(strIsLeave));
                        arlTriagingResourceAllocation.add(String.valueOf(taskNotStarted + taskInProgress + taskOnHold + bugNotStarted + bugInProgress + bugOnHold));
                     }
                     else if (arlFixingTeam.contains(strUserId))
                     {
                        arlFixingResourceAllocation.add(strAssigneeName);
                        arlFixingResourceAllocation.add(String.valueOf(taskNotStarted));
                        arlFixingResourceAllocation.add(String.valueOf(taskInProgress));
                        arlFixingResourceAllocation.add(String.valueOf(taskOnHold));
                        arlFixingResourceAllocation.add(String.valueOf(taskSLAMissed));
                        arlFixingResourceAllocation.add(String.valueOf(bugNotStarted));
                        arlFixingResourceAllocation.add(String.valueOf(bugInProgress));
                        arlFixingResourceAllocation.add(String.valueOf(bugOnHold));
                        arlFixingResourceAllocation.add(String.valueOf(bugSLAMissed));
                        arlFixingResourceAllocation.add(String.valueOf(strIsLeave));
                        arlFixingResourceAllocation.add(String.valueOf(taskNotStarted + taskInProgress + taskOnHold + bugNotStarted + bugInProgress + bugOnHold));
                     }
                     else if (arlFeatureTeam.contains(strUserId))
                     {
                        arlFeatureResourceAllocation.add(strAssigneeName);
                        arlFeatureResourceAllocation.add(String.valueOf(taskNotStarted));
                        arlFeatureResourceAllocation.add(String.valueOf(taskInProgress));
                        arlFeatureResourceAllocation.add(String.valueOf(taskOnHold));
                        arlFeatureResourceAllocation.add(String.valueOf(taskSLAMissed));
                        arlFeatureResourceAllocation.add(String.valueOf(bugNotStarted));
                        arlFeatureResourceAllocation.add(String.valueOf(bugInProgress));
                        arlFeatureResourceAllocation.add(String.valueOf(bugOnHold));
                        arlFeatureResourceAllocation.add(String.valueOf(bugSLAMissed));
                        arlFeatureResourceAllocation.add(String.valueOf(strIsLeave));
                        arlFeatureResourceAllocation.add(String.valueOf(taskNotStarted + taskInProgress + taskOnHold + bugNotStarted + bugInProgress + bugOnHold));
                     }
                     else if (arlQATeam.contains(strUserId))
                     {
                        arlQAResourceAllocation.add(strAssigneeName);
                        arlQAResourceAllocation.add(String.valueOf(taskNotStarted));
                        arlQAResourceAllocation.add(String.valueOf(taskInProgress));
                        arlQAResourceAllocation.add(String.valueOf(taskOnHold));
                        arlQAResourceAllocation.add(String.valueOf(taskSLAMissed));
                        arlQAResourceAllocation.add(String.valueOf(bugNotStarted));
                        arlQAResourceAllocation.add(String.valueOf(bugInProgress));
                        arlQAResourceAllocation.add(String.valueOf(bugOnHold));
                        arlQAResourceAllocation.add(String.valueOf(bugSLAMissed));
                        arlQAResourceAllocation.add(String.valueOf(strIsLeave));
                        arlQAResourceAllocation.add(String.valueOf(taskNotStarted + taskInProgress + taskOnHold + bugNotStarted + bugInProgress + bugOnHold));
                     }
                     else if (arlRQATeam.contains(strUserId))
                     {
                        arlRQAResourceAllocation.add(strAssigneeName);
                        arlRQAResourceAllocation.add(String.valueOf(taskNotStarted));
                        arlRQAResourceAllocation.add(String.valueOf(taskInProgress));
                        arlRQAResourceAllocation.add(String.valueOf(taskOnHold));
                        arlRQAResourceAllocation.add(String.valueOf(taskSLAMissed));
                        arlRQAResourceAllocation.add(String.valueOf(bugNotStarted));
                        arlRQAResourceAllocation.add(String.valueOf(bugInProgress));
                        arlRQAResourceAllocation.add(String.valueOf(bugOnHold));
                        arlRQAResourceAllocation.add(String.valueOf(bugSLAMissed));
                        arlRQAResourceAllocation.add(String.valueOf(strIsLeave));
                        arlRQAResourceAllocation.add(String.valueOf(taskNotStarted + taskInProgress + taskOnHold + bugNotStarted + bugInProgress + bugOnHold));
                     }
                  }
                  objRequest.setAttribute("TRIAGINGRESOURCEALLOCATION", arlTriagingResourceAllocation);
                  objRequest.setAttribute("FIXINGRESOURCEALLOCATION", arlFixingResourceAllocation);
                  objRequest.setAttribute("FEATURERESOURCEALLOCATION", arlFeatureResourceAllocation);
                  objRequest.setAttribute("QARESOURCEALLOCATION", arlQAResourceAllocation);
                  objRequest.setAttribute("RQAESOURCEALLOCATION", arlRQAResourceAllocation);
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

   public String viewLeavePlans() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlViewLeavePlans = new ReportsDAO().getLeavePlansList(objDBConnection);
            objRequest.setAttribute("VIEWLEAVEPLANS", arlViewLeavePlans);
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

   public String loadClosedTicketDetails() throws Exception
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

   public String viewClosedTicketDetails() throws Exception
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

   public String loadTicketStatus() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new ReportsDAO().getOpenTicketsAssignees(objDBConnection);
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

   public String TicketStatus() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strAssigneeName = "";
            if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
            {
               strAssigneeName = AppConstants.ALL_ASSIGNEES;
            }
            else
            {
               strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);
            }

            if (strAssignee.length() > 0)
            {
               ReportsDAO objReportsDAO = new ReportsDAO();
               TicketsDAO objTicketsDAO = new TicketsDAO();
               ArrayList<Object> arlTicketsSummary = new ArrayList<>();
               ArrayList<String> arlTicketStatus;
               ArrayList<Object> arlIndividualTickets;
               ArrayList<String> arlCommentsHistory;
               ArrayList<String> arlTicketHistory;

               if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
               {
                  ArrayList<String> arlAssignees = objReportsDAO.getOpenTicketsAssignees(objDBConnection);
                  if (arlAssignees != null && !arlAssignees.isEmpty())
                  {
                     String strTicketAssignee = "";
                     String strTicketAssigneeName = "";

                     for (int intFirstCount = 0; intFirstCount < arlAssignees.size(); intFirstCount += 2)
                     {
                        strTicketAssignee = arlAssignees.get(intFirstCount);
                        strTicketAssigneeName = arlAssignees.get(intFirstCount + 1);
                        arlIndividualTickets = new ArrayList<>();
                        arlTicketStatus = objReportsDAO.getTicketStatus(strTicketAssignee, objDBConnection);
                        if (arlTicketStatus != null && !arlTicketStatus.isEmpty())
                        {
                           String strRefNo = "";
                           String strTicketId = "";
                           String strTicketDesc = "";
                           String strTicketCategory = "";
                           String strTicketStatus = "";
                           String strReceivedDate = "";
                           String strSLAEndDate = "";
                           String strSLAMissed = "";

                           for (int intSecondCount = 0; intSecondCount < arlTicketStatus.size(); intSecondCount += 8)
                           {
                              strRefNo = arlTicketStatus.get(intSecondCount);
                              strTicketId = arlTicketStatus.get(intSecondCount + 1);
                              strTicketDesc = arlTicketStatus.get(intSecondCount + 2);
                              strTicketCategory = arlTicketStatus.get(intSecondCount + 3);
                              strTicketStatus = arlTicketStatus.get(intSecondCount + 4);
                              strReceivedDate = arlTicketStatus.get(intSecondCount + 5);
                              strSLAEndDate = arlTicketStatus.get(intSecondCount + 6);
                              strSLAMissed = arlTicketStatus.get(intSecondCount + 7);

                              arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefNo, strTicketId, objDBConnection);
                              arlTicketHistory = objTicketsDAO.getTicketHistory(strRefNo, strTicketId, objDBConnection);

                              arlIndividualTickets.add(strTicketId);
                              arlIndividualTickets.add(strTicketDesc);
                              arlIndividualTickets.add(strTicketCategory);
                              arlIndividualTickets.add(strTicketStatus);
                              arlIndividualTickets.add(strReceivedDate);
                              arlIndividualTickets.add(strSLAEndDate);
                              arlIndividualTickets.add(strSLAMissed);
                              arlIndividualTickets.add(arlCommentsHistory);
                              arlIndividualTickets.add(arlTicketHistory);
                           }
                        }
                        arlTicketsSummary.add(strTicketAssigneeName);
                        arlTicketsSummary.add(arlIndividualTickets);
                     }
                  }
               }
               else
               {
                  arlIndividualTickets = new ArrayList<>();
                  arlTicketStatus = objReportsDAO.getTicketStatus(strAssignee, objDBConnection);
                  if (arlTicketStatus != null && !arlTicketStatus.isEmpty())
                  {
                     String strRefNo = "";
                     String strTicketId = "";
                     String strTicketDesc = "";
                     String strTicketCategory = "";
                     String strTicketStatus = "";
                     String strReceivedDate = "";
                     String strSLAEndDate = "";
                     String strSLAMissed = "";

                     for (int intSecondCount = 0; intSecondCount < arlTicketStatus.size(); intSecondCount += 8)
                     {
                        strRefNo = arlTicketStatus.get(intSecondCount);
                        strTicketId = arlTicketStatus.get(intSecondCount + 1);
                        strTicketDesc = arlTicketStatus.get(intSecondCount + 2);
                        strTicketCategory = arlTicketStatus.get(intSecondCount + 3);
                        strTicketStatus = arlTicketStatus.get(intSecondCount + 4);
                        strReceivedDate = arlTicketStatus.get(intSecondCount + 5);
                        strSLAEndDate = arlTicketStatus.get(intSecondCount + 6);
                        strSLAMissed = arlTicketStatus.get(intSecondCount + 7);

                        arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefNo, strTicketId, objDBConnection);
                        arlTicketHistory = objTicketsDAO.getTicketHistory(strRefNo, strTicketId, objDBConnection);

                        arlIndividualTickets.add(strTicketId);
                        arlIndividualTickets.add(strTicketDesc);
                        arlIndividualTickets.add(strTicketCategory);
                        arlIndividualTickets.add(strTicketStatus);
                        arlIndividualTickets.add(strReceivedDate);
                        arlIndividualTickets.add(strSLAEndDate);
                        arlIndividualTickets.add(strSLAMissed);
                        arlIndividualTickets.add(arlCommentsHistory);
                        arlIndividualTickets.add(arlTicketHistory);
                     }
                  }
                  arlTicketsSummary.add(strAssigneeName);
                  arlTicketsSummary.add(arlIndividualTickets);
               }

               objRequest.setAttribute("DashBoardName", strAssigneeName);
               objRequest.setAttribute("Assignee", strAssignee);
               objRequest.setAttribute("TicketsSummary", arlTicketsSummary);
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

   public String TicketCurrentStatus() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ReportsDAO objReportsDAO = new ReportsDAO();
            ArrayList<String> arlNotStartedTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.NOT_STARTED, objReportsDAO, objDBConnection);
            ArrayList<String> arlInProgressTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.IN_PROGRESS, objReportsDAO, objDBConnection);
            ArrayList<String> arlOnHoldTicketsCurrentStatus = populateCurrentTicketsStatus(AppConstants.ON_HOLD, objReportsDAO, objDBConnection);
            objRequest.setAttribute("NotStartedTicketsCurrentStatus", arlNotStartedTicketsCurrentStatus);
            objRequest.setAttribute("InProgressTicketsCurrentStatus", arlInProgressTicketsCurrentStatus);
            objRequest.setAttribute("OnHoldTicketsCurrentStatus", arlOnHoldTicketsCurrentStatus);
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

   public String CurrentInternalTickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlInternalTickets = new ReportsDAO().getInternalTickets(objDBConnection);
            objRequest.setAttribute("InternalTickets", arlInternalTickets);
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

   public String ClosedInternalTickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("InternalTicketFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("InternalTicketToDate"));
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
               ArrayList<String> arlClosedInternalTickets = new ReportsDAO().getClosedInternalTickets(strFromDate, strToDate, objDBConnection);
               objRequest.setAttribute("ClosedInternalTickets", arlClosedInternalTickets);
               objRequest.setAttribute("DashBoardName", "Available");
               objRequest.setAttribute("FromDate", strFromDate);
               objRequest.setAttribute("ToDate", strToDate);
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

   public String loadInternalTicketDetailsDummy() throws Exception
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

   public String loadInternalTicketDetails() throws Exception
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


   public String loadSLAMissedTicketDetails() throws Exception
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

   public String viewSLAMissedTicketDetails() throws Exception
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
            ArrayList<String> arlTicketHistoryTemp = objTicketsDAO.getTicketHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlTicketHistory = new ArrayList<>();
            if (arlTicketHistoryTemp != null && !arlTicketHistoryTemp.isEmpty())
            {
               CalculateBusinessHours objCalculateBusinessHours;
               String strStartDate;
               String strEndDate;
               String strTotalHours;
               String strTotalMins;
               int intTotalHours = 0;
               int intTotalMinutes = 0;
               double dblTotalDaysSpent = 0;

               for (int iCount = 0; iCount < arlTicketHistoryTemp.size(); iCount += 5)
               {
                  strTotalHours = "";
                  strTotalMins = "";
                  strStartDate = arlTicketHistoryTemp.get(iCount);
                  strEndDate = arlTicketHistoryTemp.get(iCount + 1);
                  if (strStartDate.length() > 0 && strEndDate.length() > 0)
                  {
                     objCalculateBusinessHours = new CalculateBusinessHours();
                     objCalculateBusinessHours.execute(strStartDate, strEndDate, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
                     intTotalHours += objCalculateBusinessHours.getTotalHours();
                     intTotalMinutes += objCalculateBusinessHours.getTotalMinutes();
                     strTotalHours = String.valueOf(objCalculateBusinessHours.getTotalHours());
                     strTotalMins = String.valueOf(objCalculateBusinessHours.getTotalMinutes());
                  }
                  arlTicketHistory.add(strStartDate);
                  arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 4));
                  arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 2));
                  arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 3));
                  arlTicketHistory.add(strTotalHours);
                  arlTicketHistory.add(strTotalMins);
               }
               intTotalMinutes += intTotalHours * 60;
               dblTotalDaysSpent = Double.valueOf(intTotalMinutes) / Double.valueOf((24 * 60));
               DecimalFormat twoDForm = new DecimalFormat("#0.00");
               objRequest.setAttribute("TotalDaysSpent", twoDForm.format(dblTotalDaysSpent));
            }
            ArrayList<String> arlTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strRefno, strTicketId, objDBConnection);
            ArrayList<String> arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefno, strTicketId, objDBConnection);
            objRequest.setAttribute("ViewTicketDetails", arlViewTicketDetails);
            objRequest.setAttribute("TicketHistory", arlTicketHistory);
            objRequest.setAttribute("TicketActivityHistory", arlTicketActivityHistory);
            objRequest.setAttribute("CommentsHistory", arlCommentsHistory);

            String strTriagingRefNo = objTicketsDAO.getTriagingRefNo(strRefno, strTicketId, objDBConnection);
            if (strTriagingRefNo.length() > 0)
            {
               ArrayList<String> arlTriagingTicketDetails = objTicketsDAO.getTicketDetailsForView(strTriagingRefNo, strTicketId, objDBConnection);
               ArrayList<String> arlTriagingTicketHistoryTemp = objTicketsDAO.getTicketHistory(strTriagingRefNo, strTicketId, objDBConnection);
               ArrayList<String> arlTriagingTicketHistory = new ArrayList<>();
               if (arlTriagingTicketHistoryTemp != null && !arlTriagingTicketHistoryTemp.isEmpty())
               {
                  CalculateBusinessHours objCalculateBusinessHours;
                  String strStartDate;
                  String strEndDate;
                  String strTotalHours;
                  String strTotalMins;
                  int intTotalHours = 0;
                  int intTotalMinutes = 0;
                  double dblTotalDaysSpent = 0;

                  for (int iCount = 0; iCount < arlTriagingTicketHistoryTemp.size(); iCount += 5)
                  {
                     strTotalHours = "";
                     strTotalMins = "";
                     strStartDate = arlTriagingTicketHistoryTemp.get(iCount);
                     strEndDate = arlTriagingTicketHistoryTemp.get(iCount + 1);
                     if (strStartDate.length() > 0 && strEndDate.length() > 0)
                     {
                        objCalculateBusinessHours = new CalculateBusinessHours();
                        objCalculateBusinessHours.execute(strStartDate, strEndDate, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
                        intTotalHours += objCalculateBusinessHours.getTotalHours();
                        intTotalMinutes += objCalculateBusinessHours.getTotalMinutes();
                        strTotalHours = String.valueOf(objCalculateBusinessHours.getTotalHours());
                        strTotalMins = String.valueOf(objCalculateBusinessHours.getTotalMinutes());
                     }
                     arlTriagingTicketHistory.add(strStartDate);
                     arlTriagingTicketHistory.add(arlTriagingTicketHistoryTemp.get(iCount + 4));
                     arlTriagingTicketHistory.add(arlTriagingTicketHistoryTemp.get(iCount + 2));
                     arlTriagingTicketHistory.add(arlTriagingTicketHistoryTemp.get(iCount + 3));
                     arlTriagingTicketHistory.add(strTotalHours);
                     arlTriagingTicketHistory.add(strTotalMins);
                  }
                  intTotalMinutes += intTotalHours * 60;
                  dblTotalDaysSpent = Double.valueOf(intTotalMinutes) / Double.valueOf((24 * 60));
                  DecimalFormat twoDForm = new DecimalFormat("#0.00");
                  objRequest.setAttribute("TriagingTotalDaysSpent", twoDForm.format(dblTotalDaysSpent));
               }
               ArrayList<String> arlTriagingTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strTriagingRefNo, strTicketId, objDBConnection);
               ArrayList<String> arlTriagingCommentsHistory = objTicketsDAO.getCommentsHistory(strTriagingRefNo, strTicketId, objDBConnection);
               objRequest.setAttribute("TriagingTicketDetails", arlTriagingTicketDetails);
               objRequest.setAttribute("TriagingTicketHistory", arlTriagingTicketHistory);
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

   public String loadReopenedTickets() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlAssignees = new ReportsDAO().getAllAssignees(objDBConnection);
            objRequest.setAttribute("Assignees", arlAssignees);

            String strAssignee = AppUtil.checkNull(objRequest.getParameter("Assignee"));
            String strFromDate = AppUtil.checkNull(objRequest.getParameter("ReopenedTicketFromDate"));
            String strToDate = AppUtil.checkNull(objRequest.getParameter("ReopenedTicketToDate"));
            if (strAssignee.length() == 0)
            {
               strAssignee = AppConstants.ALL_ASSIGNEES;
            }
            if (strToDate == null || strToDate.length() == 0)
            {
               strToDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strFromDate == null || strFromDate.length() == 0)
            {
               strFromDate = AppUtil.getMondayDate(strToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            }
            if (strAssignee.length() > 0 && strFromDate.length() > 0 && strToDate.length() > 0)
            {
               ArrayList<String> arlReopenedTicketList = new ReportsDAO().getReopenedTicketList(strAssignee, strFromDate, strToDate, objDBConnection);
               objRequest.setAttribute("ReopenedTicketList", arlReopenedTicketList);
               String strAssigneeName = "";
               if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
               {
                  strAssigneeName = AppConstants.ALL_ASSIGNEES;
               }
               else
               {
                  strAssigneeName = new CommonDAO().getAssigneeName(strAssignee, objDBConnection);
               }
               objRequest.setAttribute("DashBoardName", strAssigneeName);
               objRequest.setAttribute("Assignee", strAssignee);
               objRequest.setAttribute("FromDate", strFromDate);
               objRequest.setAttribute("ToDate", strToDate);
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

   public String loadCodeFixReleases() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            ArrayList<String> arlReleaseVersions = new TicketsDAO().getReleaseVersions(objDBConnection);
            objRequest.setAttribute("ReleaseVersions", arlReleaseVersions);

            String strSelectedReleaseVersion = AppUtil.checkNull(objRequest.getParameter("ReleaseVersion"));

            if (strSelectedReleaseVersion.length() > 0)
            {
               ArrayList<String> arlCodeFixReleases = new ReportsDAO().getCodeFixReleases(strSelectedReleaseVersion, objDBConnection);
               objRequest.setAttribute("CodeFixReleases", arlCodeFixReleases);
               objRequest.setAttribute("SelectedReleaseVersion", strSelectedReleaseVersion);
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
