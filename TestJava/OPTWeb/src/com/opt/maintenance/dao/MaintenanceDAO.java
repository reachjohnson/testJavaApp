package com.opt.maintenance.dao;

import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.Connection;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;

public class MaintenanceDAO
{
   Logger objLogger = Logger.getLogger(MaintenanceDAO.class.getName());

   public boolean checkUserIdExists(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      boolean blnLoginIdExists = false;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
      sbSqlQuery.append(" FROM USER_DETAILS");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arltemp != null && !arltemp.isEmpty())
         {
            int intCount = Integer.parseInt(arltemp.get(0));
            blnLoginIdExists = (intCount > 0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return blnLoginIdExists;
   }

   public void saveUserDetails(String strUserId, String strFirstName, String strLastName,
                               String strAdminFlag, String strActiveFlag, String strAssigneeFlag, String strTeam, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO USER_DETAILS ");
      sbSqlQuery.append("(USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, ADMIN_FLAG, ACTIVE_FLAG, ASSIGNEE_FLAG, TEAM) VALUES ");
      sbSqlQuery.append("(?, ?, ?, ?, ?, ?, ?, ?)");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         objPreparedStatement.setString(++intParam, AppUtil.getEncriptedString(AppConstants.DEFAULT_PASSWORD, 5));
         objPreparedStatement.setString(++intParam, strFirstName);
         objPreparedStatement.setString(++intParam, strLastName);
         objPreparedStatement.setString(++intParam, strAdminFlag);
         objPreparedStatement.setString(++intParam, strActiveFlag);
         objPreparedStatement.setString(++intParam, strAssigneeFlag);
         objPreparedStatement.setString(++intParam, strTeam);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      intParam = 0;
      sbSqlQuery = new StringBuilder("INSERT INTO USER_ACCESS_RIGHTS ");
      sbSqlQuery.append("(USER_ID) VALUES ");
      sbSqlQuery.append("(?)");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      BaseDAO.connCommit(objDBConnection);
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public String modifyUserDetails(String strUserId, String strFirstName, String strLastName,
                                   String strAdminFlag, String strActiveFlag, String strAssigneeFlag,
                                   String strPasswordResetFlag, String strTeam, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE USER_DETAILS SET");
      sbSqlQuery.append(" FIRST_NAME = ?, ");
      sbSqlQuery.append(" LAST_NAME = ?, ");
      sbSqlQuery.append(" ADMIN_FLAG = ?, ");
      sbSqlQuery.append(" ACTIVE_FLAG = ?, ");
      sbSqlQuery.append(" ASSIGNEE_FLAG = ?, ");
      sbSqlQuery.append(" TEAM = ? ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFirstName);
         objPreparedStatement.setString(++intParam, strLastName);
         objPreparedStatement.setString(++intParam, strAdminFlag);
         objPreparedStatement.setString(++intParam, strActiveFlag);
         objPreparedStatement.setString(++intParam, strAssigneeFlag);
         objPreparedStatement.setString(++intParam, strTeam);
         objPreparedStatement.setString(++intParam, strUserId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      if (strPasswordResetFlag.equalsIgnoreCase("Y"))
      {
         intParam = 0;
         sbSqlQuery = new StringBuilder("UPDATE USER_DETAILS SET");
         sbSqlQuery.append(" PASSWORD = ? ");
         sbSqlQuery.append(" WHERE USER_ID = ? ");
         try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
         {
            objPreparedStatement.setString(++intParam, AppUtil.getEncriptedString(AppConstants.DEFAULT_PASSWORD, 5));
            objPreparedStatement.setString(++intParam, strUserId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         }
         catch (Exception objException)
         {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
         }
      }
      BaseDAO.connCommit(objDBConnection);
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

   public String saveAccessRights(String strUserId, String writeAccess, String createTask, String createBug, String createInternalTicket, String modifyCurrentTickets, String reopenClosedTickets,
                                  String updateLeavePlans, String holidayCalendar, String addUser, String modifyUser, String accessRights, String ticketsStatus,
                                  String currentStatus, String currentTickets, String closedTickets, String SLAMissedTickets, String resourceAllocation,
                                  String SLAReports, String SLAMetrics, String currentInternalTickets, String closedInternalTickets, String viewLeavePlans,
                                  String listOfHolidays, String teamContacts, String domainPOC, String createRQABug, String reopenRQABug, String modifyRQATickets,
                                  String RQADailyReport, String RQACurrentStatus, String RQAMetrics, String RQASettings, String ResourceMatrix, String InternalTicketAdmin,
                                  String HighPriorityNotStarted, String OOSLANotStarted, String RQAETAReminder, String StatusUpdateReminder, String OpenTicketsPriority, String TicketsProgressCheck,
                                  String CommonSettings, String UpdateClosedTicket, String TeamFeedback, String ReopenedTickets, String CodeFixReleases, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);

      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE USER_ACCESS_RIGHTS SET");
      sbSqlQuery.append(" WRITE_ACCESS = ?, ");
      sbSqlQuery.append(" CREATE_TASK = ?, ");
      sbSqlQuery.append(" CREATE_BUG = ?, ");
      sbSqlQuery.append(" CREATE_INTERNAL_TICKET = ?, ");
      sbSqlQuery.append(" MODIFY_CURRENT_TICKETS = ?, ");
      sbSqlQuery.append(" REOPEN_CLOSED_TICKETS = ?, ");
      sbSqlQuery.append(" UPDATE_LEAVE_PLANS = ?, ");
      sbSqlQuery.append(" HOLIDAY_CALENDAR = ?, ");
      sbSqlQuery.append(" ADD_USER = ?, ");
      sbSqlQuery.append(" MODIFY_USER = ?, ");
      sbSqlQuery.append(" ACCESS_RIGHTS = ?, ");
      sbSqlQuery.append(" TICKETS_STATUS = ?, ");
      sbSqlQuery.append(" CURRENT_STATUS = ?, ");
      sbSqlQuery.append(" CURRENT_TICKETS = ?, ");
      sbSqlQuery.append(" CLOSED_TICKETS = ?, ");
      sbSqlQuery.append(" SLA_MISSED_TICKETS = ?, ");
      sbSqlQuery.append(" RESOURCE_ALLOCATION = ?, ");
      sbSqlQuery.append(" SLA_REPORTS = ?, ");
      sbSqlQuery.append(" SLA_METRICS = ?, ");
      sbSqlQuery.append(" CURRENT_INTERNAL_TICKETS = ?, ");
      sbSqlQuery.append(" CLOSED_INTERNAL_TICKETS = ?, ");
      sbSqlQuery.append(" VIEW_LEAVE_PLANS = ?, ");
      sbSqlQuery.append(" LIST_OF_HOLIDAYS = ?, ");
      sbSqlQuery.append(" TEAM_CONTACTS = ?, ");
      sbSqlQuery.append(" DOMAIN_POC = ?, ");
      sbSqlQuery.append(" CREATE_RQA_BUG = ?, ");
      sbSqlQuery.append(" REOPEN_RQA_BUG = ?, ");
      sbSqlQuery.append(" MODIFY_RQA_TICKETS = ?, ");
      sbSqlQuery.append(" RQA_DAILY_REPORT = ?, ");
      sbSqlQuery.append(" RQA_CURRENT_STATUS = ?, ");
      sbSqlQuery.append(" RQA_METRICS = ?, ");
      sbSqlQuery.append(" RQA_SETTINGS = ?, ");
      sbSqlQuery.append(" RESOURCE_MATRIX = ?, ");
      sbSqlQuery.append(" IT_ADMIN_CHANGES = ?, ");
      sbSqlQuery.append(" HIGH_PRIORITY_NOT_STARTED = ?, ");
      sbSqlQuery.append(" OOSLA_NOT_STARTED = ?, ");
      sbSqlQuery.append(" RQA_ETA_REMINDER = ?, ");
      sbSqlQuery.append(" STATUS_UPDATE_REMINDER = ?, ");
      sbSqlQuery.append(" OPEN_TICKETS_PRIORITY = ?, ");
      sbSqlQuery.append(" TICKETS_PROGRESS_CHECK = ?, ");
      sbSqlQuery.append(" COMMON_SETTINGS = ?, ");
      sbSqlQuery.append(" UPDATE_CLOSED_TICKET = ?, ");
      sbSqlQuery.append(" TEAM_FEEDBACK = ?, ");
      sbSqlQuery.append(" REOPENED_TICKETS = ?, ");
      sbSqlQuery.append(" CODE_FIX_RELEASES = ? ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, writeAccess);
         objPreparedStatement.setString(++intParam, createTask);
         objPreparedStatement.setString(++intParam, createBug);
         objPreparedStatement.setString(++intParam, createInternalTicket);
         objPreparedStatement.setString(++intParam, modifyCurrentTickets);
         objPreparedStatement.setString(++intParam, reopenClosedTickets);
         objPreparedStatement.setString(++intParam, updateLeavePlans);
         objPreparedStatement.setString(++intParam, holidayCalendar);
         objPreparedStatement.setString(++intParam, addUser);
         objPreparedStatement.setString(++intParam, modifyUser);
         objPreparedStatement.setString(++intParam, accessRights);
         objPreparedStatement.setString(++intParam, ticketsStatus);
         objPreparedStatement.setString(++intParam, currentStatus);
         objPreparedStatement.setString(++intParam, currentTickets);
         objPreparedStatement.setString(++intParam, closedTickets);
         objPreparedStatement.setString(++intParam, SLAMissedTickets);
         objPreparedStatement.setString(++intParam, resourceAllocation);
         objPreparedStatement.setString(++intParam, SLAReports);
         objPreparedStatement.setString(++intParam, SLAMetrics);
         objPreparedStatement.setString(++intParam, currentInternalTickets);
         objPreparedStatement.setString(++intParam, closedInternalTickets);
         objPreparedStatement.setString(++intParam, viewLeavePlans);
         objPreparedStatement.setString(++intParam, listOfHolidays);
         objPreparedStatement.setString(++intParam, teamContacts);
         objPreparedStatement.setString(++intParam, domainPOC);
         objPreparedStatement.setString(++intParam, createRQABug);
         objPreparedStatement.setString(++intParam, reopenRQABug);
         objPreparedStatement.setString(++intParam, modifyRQATickets);
         objPreparedStatement.setString(++intParam, RQADailyReport);
         objPreparedStatement.setString(++intParam, RQACurrentStatus);
         objPreparedStatement.setString(++intParam, RQAMetrics);
         objPreparedStatement.setString(++intParam, RQASettings);
         objPreparedStatement.setString(++intParam, ResourceMatrix);
         objPreparedStatement.setString(++intParam, InternalTicketAdmin);
         objPreparedStatement.setString(++intParam, HighPriorityNotStarted);
         objPreparedStatement.setString(++intParam, OOSLANotStarted);
         objPreparedStatement.setString(++intParam, RQAETAReminder);
         objPreparedStatement.setString(++intParam, StatusUpdateReminder);
         objPreparedStatement.setString(++intParam, OpenTicketsPriority);
         objPreparedStatement.setString(++intParam, TicketsProgressCheck);
         objPreparedStatement.setString(++intParam, CommonSettings);
         objPreparedStatement.setString(++intParam, UpdateClosedTicket);
         objPreparedStatement.setString(++intParam, TeamFeedback);
         objPreparedStatement.setString(++intParam, ReopenedTickets);
         objPreparedStatement.setString(++intParam, CodeFixReleases);
         objPreparedStatement.setString(++intParam, strUserId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         BaseDAO.connCommit(objDBConnection);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

   public void updateRQASettings(String CurrentRQAPhase, String AutoEmail, String EmailCutOffTime, String EmailToIds, String EmailCCIds, String EmailSubject, String EmailHeader, String BugResolTriaged,
                                 String BugResolLegacyBugFixed, String BugResolFeatCodeImpactFixed, String BugResolNonFeatCodeImpactFixed, String RQAPOCMailIds,
                                 Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE RQA_SETTINGS SET");
      sbSqlQuery.append(" CURRENT_PHASE = ?, ");
      sbSqlQuery.append(" MAIL_TO_TRIGGER = ?, ");
      sbSqlQuery.append(" MAIL_CUTOFF_TIME = ?, ");
      sbSqlQuery.append(" MAIL_TO_IDS = ?, ");
      sbSqlQuery.append(" MAIL_CC_IDS = ?, ");
      sbSqlQuery.append(" MAIL_SUBJECT = ?, ");
      sbSqlQuery.append(" MAIL_HEADER = ?, ");
      sbSqlQuery.append(" RESOL_TRIAGED = ?, ");
      sbSqlQuery.append(" RESOL_LEGACY_FIXED = ?, ");
      sbSqlQuery.append(" RESOL_FEAT_CODE_IMP_FIXED = ?, ");
      sbSqlQuery.append(" RESOL_NONFEAT_CODE_IMP_FIXED = ?, ");
      sbSqlQuery.append(" RQA_POC_MAIL_IDS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, CurrentRQAPhase);
         objPreparedStatement.setString(++intParam, AutoEmail);
         objPreparedStatement.setString(++intParam, EmailCutOffTime);
         objPreparedStatement.setString(++intParam, EmailToIds);
         objPreparedStatement.setString(++intParam, EmailCCIds);
         objPreparedStatement.setString(++intParam, EmailSubject);
         objPreparedStatement.setString(++intParam, EmailHeader);
         objPreparedStatement.setString(++intParam, BugResolTriaged);
         objPreparedStatement.setString(++intParam, BugResolLegacyBugFixed);
         objPreparedStatement.setString(++intParam, BugResolFeatCodeImpactFixed);
         objPreparedStatement.setString(++intParam, BugResolNonFeatCodeImpactFixed);
         objPreparedStatement.setString(++intParam, RQAPOCMailIds);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         BaseDAO.connCommit(objDBConnection);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public ArrayList<String> getUserDetails(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID, FIRST_NAME, LAST_NAME, ACTIVE_FLAG, ADMIN_FLAG, ASSIGNEE_FLAG, TEAM FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlAssignees;
   }

   public ArrayList<String> getUserList(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlAssignees;
      String strSqlQuery = "SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) FROM USER_DETAILS ";
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(strSqlQuery.toString()))
      {
         arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlAssignees;
   }

   public ArrayList<String> getUserListForAccessRights(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE ACTIVE_FLAG = 'Y' ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlAssignees;
   }

   public ArrayList<String> getUserAccessRights(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlUserAccessRights;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT WRITE_ACCESS,CREATE_TASK,CREATE_BUG,CREATE_INTERNAL_TICKET,MODIFY_CURRENT_TICKETS,REOPEN_CLOSED_TICKETS,");
      sbSqlQuery.append("UPDATE_LEAVE_PLANS,HOLIDAY_CALENDAR,ADD_USER,MODIFY_USER,ACCESS_RIGHTS, TICKETS_STATUS,CURRENT_STATUS,CURRENT_TICKETS,CLOSED_TICKETS,SLA_MISSED_TICKETS,RESOURCE_ALLOCATION,");
      sbSqlQuery.append("SLA_REPORTS,SLA_METRICS,CURRENT_INTERNAL_TICKETS,CLOSED_INTERNAL_TICKETS,VIEW_LEAVE_PLANS,LIST_OF_HOLIDAYS,TEAM_CONTACTS,DOMAIN_POC, ");
      sbSqlQuery.append(" CREATE_RQA_BUG, REOPEN_RQA_BUG, MODIFY_RQA_TICKETS, RQA_DAILY_REPORT, RQA_CURRENT_STATUS, RQA_METRICS, RQA_SETTINGS, RESOURCE_MATRIX, IT_ADMIN_CHANGES, ");
      sbSqlQuery.append(" HIGH_PRIORITY_NOT_STARTED, OOSLA_NOT_STARTED, RQA_ETA_REMINDER, STATUS_UPDATE_REMINDER, OPEN_TICKETS_PRIORITY, TICKETS_PROGRESS_CHECK, COMMON_SETTINGS, ");
      sbSqlQuery.append(" UPDATE_CLOSED_TICKET, TEAM_FEEDBACK, REOPENED_TICKETS, CODE_FIX_RELEASES ");
      sbSqlQuery.append(" FROM USER_ACCESS_RIGHTS ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         arlUserAccessRights = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlUserAccessRights;
   }

   public ArrayList<String> getRQASettings(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQASettings;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT  CURRENT_PHASE, MAIL_TO_TRIGGER, MAIL_CUTOFF_TIME, MAIL_TO_IDS, MAIL_CC_IDS, MAIL_SUBJECT, MAIL_HEADER, RESOL_TRIAGED,");
      sbSqlQuery.append(" RESOL_LEGACY_FIXED, RESOL_FEAT_CODE_IMP_FIXED, RESOL_NONFEAT_CODE_IMP_FIXED, RQA_POC_MAIL_IDS ");
      sbSqlQuery.append(" FROM RQA_SETTINGS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlRQASettings = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQASettings;
   }

   public ArrayList<String> getResourceMatrix(String strDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResourceMatrix;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', LAST_NAME), ");
      sbSqlQuery.append(" RM.LIVE_BUGS, RM.COMMENTS, UD.TEAM, UD.ACTIVE_FLAG ");
      sbSqlQuery.append(" FROM RESOURCE_MATRIX RM RIGHT JOIN USER_DETAILS UD ");
      sbSqlQuery.append(" ON RM.USER_ID = UD.USER_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(RM.DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', LAST_NAME) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strDate);
         arlResourceMatrix = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResourceMatrix;
   }

   public void saveResourceCount(String strDate, int intLiveBugsCount, int intRQACount,
                                 int intFeatureCount, int intQACount, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT COUNT(REF_NO) ");
      sbSqlQuery.append(" FROM RESOURCE_COUNT ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      ArrayList<String> arlCount;
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      if (arlCount != null && !arlCount.isEmpty())
      {
         int intCount = Integer.parseInt(arlCount.get(0));
         intParam = 0;
         if (intCount > 0)
         {
            sbSqlQuery = new StringBuilder("UPDATE RESOURCE_COUNT SET");
            sbSqlQuery.append(" LIVE_BUGS = ?, ");
            sbSqlQuery.append(" RQA_BUGS = ?, ");
            sbSqlQuery.append(" SEPA = ?, ");
            sbSqlQuery.append(" QA = ? ");
            sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
            sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setInt(++intParam, intLiveBugsCount);
               objPreparedStatement.setInt(++intParam, intRQACount);
               objPreparedStatement.setInt(++intParam, intFeatureCount);
               objPreparedStatement.setInt(++intParam, intQACount);
               objPreparedStatement.setString(++intParam, strDate);
               BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
               BaseDAO.connCommit(objDBConnection);
            }
            catch (Exception objException)
            {
               BaseDAO.connRollback(objDBConnection);
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }
         }
         else
         {
            sbSqlQuery = new StringBuilder("INSERT INTO RESOURCE_COUNT ");
            sbSqlQuery.append("(DATE, LIVE_BUGS, RQA_BUGS, SEPA, QA) VALUES ");
            sbSqlQuery.append("(?, ?, ?, ?, ?)");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setString(++intParam, AppUtil.convertDate(strDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
               objPreparedStatement.setInt(++intParam, intLiveBugsCount);
               objPreparedStatement.setInt(++intParam, intRQACount);
               objPreparedStatement.setInt(++intParam, intFeatureCount);
               objPreparedStatement.setInt(++intParam, intQACount);
               BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
               BaseDAO.connCommit(objDBConnection);
            }
            catch (Exception objException)
            {
               BaseDAO.connRollback(objDBConnection);
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public void saveResourceMatrix(String strUserId, String strDate, String strLiveBugsFlag, String strComments,
                                  Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT COUNT(REF_NO) ");
      sbSqlQuery.append(" FROM RESOURCE_MATRIX ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(1, strDate);
         objPreparedStatement.setString(2, strUserId);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      if (arlCount != null && !arlCount.isEmpty())
      {
         int intCount = Integer.parseInt(arlCount.get(0));
         intParam = 0;
         if (intCount > 0)
         {
            sbSqlQuery = new StringBuilder("UPDATE RESOURCE_MATRIX SET");
            sbSqlQuery.append(" LIVE_BUGS = ?, ");
            sbSqlQuery.append(" COMMENTS = ? ");
            sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
            sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
            sbSqlQuery.append(" AND USER_ID = ? ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setString(++intParam, strLiveBugsFlag);
               objPreparedStatement.setString(++intParam, strComments);
               objPreparedStatement.setString(++intParam, strDate);
               objPreparedStatement.setString(++intParam, strUserId);
               BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
               BaseDAO.connCommit(objDBConnection);
            }
            catch (Exception objException)
            {
               BaseDAO.connRollback(objDBConnection);
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }
         }
         else
         {
            sbSqlQuery = new StringBuilder("INSERT INTO RESOURCE_MATRIX ");
            sbSqlQuery.append("(USER_ID, DATE, LIVE_BUGS, COMMENTS) VALUES ");
            sbSqlQuery.append("(?, ?, ?, ?)");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setString(++intParam, strUserId);
               objPreparedStatement.setString(++intParam, AppUtil.convertDate(strDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
               objPreparedStatement.setString(++intParam, strLiveBugsFlag);
               objPreparedStatement.setString(++intParam, strComments);
               BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
               BaseDAO.connCommit(objDBConnection);
            }
            catch (Exception objException)
            {
               BaseDAO.connRollback(objDBConnection);
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public ArrayList<String> getResourceCount(String strDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResourceMatrix;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT LIVE_BUGS, RQA_BUGS, SEPA, QA ");
      sbSqlQuery.append(" FROM RESOURCE_COUNT ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') = ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strDate);
         arlResourceMatrix = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResourceMatrix;
   }

   public ArrayList<String> getCommonSettings(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlUserAccessRights;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT OOOSLA_HIGHLIGHT,DB_BACKUP,ETES_REMINDER, ");
      sbSqlQuery.append(" HIGH_PRIORITY_NOT_STARTED, OOSLA_NOT_STARTED, RQA_ETA_REMINDER, STATUS_UPDATE_REMINDER, OPEN_TICKETS_PRIORITY, TICKETS_PROGRESS_CHECK, ");
      sbSqlQuery.append(" ETA_BUGS, ETA_CRITICAL_TASKS, ETA_MAJOR_TASKS, ETA_TOPAGEING_TASKS, RESCHEDULE_CROSSED_ETA, ETA_INTERNAL_TICKETS, ");
      sbSqlQuery.append(" ETA_RQA_BUGS, TOPAGEING_THRESHOLD, ETA_RESCHEDULE_MAX, DAILY_STATUS ");
      sbSqlQuery.append(" FROM COMMON_SETTINGS ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlUserAccessRights = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlUserAccessRights;
   }

   public String saveCommonSettings(String OOSLAHighLight, String DBBackup, String ETESReminder, String HighPriorityNotStarted, String OOSLANotStarted, String RQAETAReminder,
                                    String StatusUpdateReminder, String OpenTicketsPriority, String TicketsProgressCheck, String ETABugs, String ETACriticalTasks,
                                    String ETAMajorTasks, String ETATopAgeingTasks, String RescheduleCrossedETA, String ETAInteranlTickets, String ETARQABugs, String TopAgeingThreshold,
                                    String ETARescheduleMax, String DailyStatus, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);

      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE COMMON_SETTINGS SET");
      sbSqlQuery.append(" OOOSLA_HIGHLIGHT = ?, ");
      sbSqlQuery.append(" DB_BACKUP = ?, ");
      sbSqlQuery.append(" ETES_REMINDER = ?, ");
      sbSqlQuery.append(" HIGH_PRIORITY_NOT_STARTED = ?, ");
      sbSqlQuery.append(" OOSLA_NOT_STARTED = ?, ");
      sbSqlQuery.append(" RQA_ETA_REMINDER = ?, ");
      sbSqlQuery.append(" STATUS_UPDATE_REMINDER = ?, ");
      sbSqlQuery.append(" OPEN_TICKETS_PRIORITY = ?, ");
      sbSqlQuery.append(" TICKETS_PROGRESS_CHECK = ?, ");
      sbSqlQuery.append(" ETA_BUGS = ?, ");
      sbSqlQuery.append(" ETA_CRITICAL_TASKS = ?, ");
      sbSqlQuery.append(" ETA_MAJOR_TASKS = ?, ");
      sbSqlQuery.append(" ETA_TOPAGEING_TASKS = ?, ");
      sbSqlQuery.append(" RESCHEDULE_CROSSED_ETA = ?, ");
      sbSqlQuery.append(" ETA_INTERNAL_TICKETS = ?, ");
      sbSqlQuery.append(" ETA_RQA_BUGS = ?, ");
      sbSqlQuery.append(" TOPAGEING_THRESHOLD = ?, ");
      sbSqlQuery.append(" ETA_RESCHEDULE_MAX = ?, ");
      sbSqlQuery.append(" DAILY_STATUS = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, OOSLAHighLight);
         objPreparedStatement.setString(++intParam, DBBackup);
         objPreparedStatement.setString(++intParam, ETESReminder);
         objPreparedStatement.setString(++intParam, HighPriorityNotStarted);
         objPreparedStatement.setString(++intParam, OOSLANotStarted);
         objPreparedStatement.setString(++intParam, RQAETAReminder);
         objPreparedStatement.setString(++intParam, StatusUpdateReminder);
         objPreparedStatement.setString(++intParam, OpenTicketsPriority);
         objPreparedStatement.setString(++intParam, TicketsProgressCheck);
         objPreparedStatement.setString(++intParam, ETABugs);
         objPreparedStatement.setString(++intParam, ETACriticalTasks);
         objPreparedStatement.setString(++intParam, ETAMajorTasks);
         objPreparedStatement.setString(++intParam, ETATopAgeingTasks);
         objPreparedStatement.setString(++intParam, RescheduleCrossedETA);
         objPreparedStatement.setString(++intParam, ETAInteranlTickets);
         objPreparedStatement.setString(++intParam, ETARQABugs);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(TopAgeingThreshold));
         objPreparedStatement.setInt(++intParam, Integer.parseInt(ETARescheduleMax));
         objPreparedStatement.setString(++intParam, DailyStatus);

         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         BaseDAO.connCommit(objDBConnection);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

}
