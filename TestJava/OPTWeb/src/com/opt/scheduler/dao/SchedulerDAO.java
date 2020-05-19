package com.opt.scheduler.dao;

import com.opt.common.util.AppSettings;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class SchedulerDAO
{
   Logger objLogger = Logger.getLogger(SchedulerDAO.class.getName());

   public ArrayList<String> getTicketsProgressAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketsProgressAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), UD.USER_ID  ");
      sbSqlQuery.append(" FROM USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE UD.ACTIVE_FLAG = 'Y'  ");
      sbSqlQuery.append(" AND UD.ASSIGNEE_FLAG = 'Y' ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.TICKETS_PROGRESS_CHECK = 'Y' ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTicketsProgressAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketsProgressAssignees;
   }

   public ArrayList<String> getOpenTicketsAssigneesForStatusUpdate(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.STATUS_UPDATE_REMINDER = 'Y' ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS IN (?, ?, ? ) ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
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

   public ArrayList<String> getOpenTicketsAssigneesForTicketsPriority(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.OPEN_TICKETS_PRIORITY = 'Y' ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS IN (?, ?, ? ) ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
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

   public ArrayList<String> getOOSLANotStartedAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlOOSLANotStartedAssignees;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.OOSLA_NOT_STARTED = 'Y' ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlOOSLANotStartedAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOOSLANotStartedAssignees;
   }

   public ArrayList<String> getHighPrioritiesNotStartedAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlOOSLANotStartedAssignees;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.HIGH_PRIORITY_NOT_STARTED = 'Y' ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND ( TICKET_CATEGORY = ? OR ");
      sbSqlQuery.append(" TICKET_PRIORITY = ? OR ");
      sbSqlQuery.append(" (TICKET_PRIORITY = ? AND 90 - DATEDIFF(NOW(), CREATED_DATE) < ?) OR ");
      sbSqlQuery.append(" ((TICKET_PRIORITY = ? OR TICKET_PRIORITY = ?) AND 120 - DATEDIFF(NOW(), CREATED_DATE) < ?)) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, "Major");
         objPreparedStatement.setString(++intParam, "Normal");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());
         objPreparedStatement.setString(++intParam, "Minor");
         objPreparedStatement.setString(++intParam, "Trivial");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());
         arlOOSLANotStartedAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOOSLANotStartedAssignees;
   }

   public ArrayList<String> getRQAOpenTicketsAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD, USER_ACCESS_RIGHTS UA ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.USER_ID = UA.USER_ID ");
      sbSqlQuery.append(" AND UA.RQA_ETA_REMINDER = 'Y' ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
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

   public boolean getTicketsProgressCheck(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTemp;
      boolean blnTicketsProgressCheck = false;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE ASSIGNEE = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            if (Integer.parseInt(arlTemp.get(0)) == 0)
            {
               blnTicketsProgressCheck = true;
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return blnTicketsProgressCheck;
   }

   public ArrayList<String> getOOSLANotStartedTickets(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketsProgressAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_ID, TICKET_DESC ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, strAssignee);
         arlTicketsProgressAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketsProgressAssignees;
   }

   public ArrayList<String> getTicketsPriorityETA(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketsProgressAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, DATE_FORMAT(ETA, '%d/%b/%Y'), ");
      sbSqlQuery.append(" CASE TICKET_PRIORITY ");
      sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" END AS AGEING ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ");
      sbSqlQuery.append(" ORDER BY TICKET_CATEGORY, TICKET_PRIORITY, AGEING ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strAssignee);
         arlTicketsProgressAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketsProgressAssignees;
   }

   public ArrayList<String> getHighPrioritiesNotStarted(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketsProgressAssignees;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, DATE_FORMAT(ETA, '%d/%b/%Y'), ");
      sbSqlQuery.append(" CASE TICKET_PRIORITY ");
      sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" END AS AGEING ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ");
      sbSqlQuery.append(" AND ( TICKET_CATEGORY = ? OR ");
      sbSqlQuery.append(" TICKET_PRIORITY = ? OR ");
      sbSqlQuery.append(" (TICKET_PRIORITY = ? AND 90 - DATEDIFF(NOW(), CREATED_DATE) < ?) OR ");
      sbSqlQuery.append(" ((TICKET_PRIORITY = ? OR TICKET_PRIORITY = ?) AND 120 - DATEDIFF(NOW(), CREATED_DATE) < ?))");
      sbSqlQuery.append(" ORDER BY TICKET_CATEGORY, TICKET_PRIORITY, AGEING ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, "Major");
         objPreparedStatement.setString(++intParam, "Normal");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());
         objPreparedStatement.setString(++intParam, "Minor");
         objPreparedStatement.setString(++intParam, "Trivial");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());
         arlTicketsProgressAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketsProgressAssignees;
   }
}