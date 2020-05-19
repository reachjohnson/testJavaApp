package com.opt.home.dao;

import com.opt.common.util.AppSettings;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class HomeDAO
{
   Logger objLogger = Logger.getLogger(HomeDAO.class.getName());

   public ArrayList<String> getTicketDetails(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketDetails;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, TICKET_ID, TICKET_DESC, TICKET_CATEGORY, TICKET_PRIORITY, TICKET_TYPE, ");
      sbSqlQuery.append("TICKET_STATUS, DATE_FORMAT(RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(PLANNED_END_DATE, '%d/%b/%Y'), ");
      sbSqlQuery.append(" ASSIGNEE, STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), TEAM_WORKING, DATE_FORMAT(ETA, '%d/%b/%Y'), ");
      sbSqlQuery.append(" CASE TICKET_PRIORITY ");
      sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), CREATED_DATE) ");
      sbSqlQuery.append(" END AS AGEING, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TICKET_STATUS IN (?, ?, ? )");
      sbSqlQuery.append(" AND ASSIGNEE = UD.USER_ID ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" ORDER BY ASSIGNEE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketDetails;
   }

   public ArrayList<String> getInternalTicketDetails(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlInternalTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.REF_NO, ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, DATE_FORMAT(ITD.START_DATE, '%d/%b/%Y %h:%i %p'), CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), DATE_FORMAT(ITD.ETA, '%d/%b/%Y'), ");
      sbSqlQuery.append(" CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), ITD.ASSIGNEE ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1, USER_DETAILS UD2");
      sbSqlQuery.append(" WHERE ITD.TICKET_STATUS = 'Open' ");
      sbSqlQuery.append(" AND ITD.CREATED_BY = UD1.USER_ID ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = UD2.USER_ID ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" ORDER BY ITD.START_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlInternalTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlInternalTicketDetails;
   }

   public ArrayList<String> getInternalTicketDetailsCreatedByYou(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlInternalTicketDetailsCreatedByYou;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.REF_NO, ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, DATE_FORMAT(ITD.START_DATE, '%d/%b/%Y %h:%i %p'), CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), DATE_FORMAT(ITD.ETA, '%d/%b/%Y'), ITD.ASSIGNEE ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE ITD.TICKET_STATUS = 'Open' ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND CREATED_BY = ? ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE != ITD.CREATED_BY ");
      sbSqlQuery.append(" ORDER BY ITD.START_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         arlInternalTicketDetailsCreatedByYou = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlInternalTicketDetailsCreatedByYou;
   }

   public ArrayList<String> getSLAMissingTickets(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlSLAMissingTicketDetails;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_ID, ");
      sbSqlQuery.append(" DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y') ");
      sbSqlQuery.append("FROM TICKET_DETAILS ");
      sbSqlQuery.append("WHERE TICKET_STATUS != ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" AND ACTUAL_END_DATE IS NULL ");
      sbSqlQuery.append(" ORDER BY PLANNED_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlSLAMissingTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlSLAMissingTicketDetails;
   }

   public ArrayList<String> getRQATicketDetails(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQATicketDetails;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, TICKET_ID, TICKET_DESC, TICKET_CATEGORY, TICKET_PRIORITY, ");
      sbSqlQuery.append("TICKET_STATUS, DATE_FORMAT(RECEIVED_DATE, '%d/%b/%Y'), ASSIGNEE, DATE_FORMAT(PLANNED_END_DATE, '%d/%b/%Y'), CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append("FROM RQA_TICKET_DETAILS, USER_DETAILS UD ");
      sbSqlQuery.append("WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = UD.USER_ID ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" ORDER BY PLANNED_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlRQATicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQATicketDetails;
   }

   public ArrayList<String> getOpenTicketsCount(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlOpenTicketsCount;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ");
      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS BUGSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS CRITICALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MAJORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS NORMALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MINORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS TRIVIALTASKSCOUNT ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Critical");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Major");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Normal");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Minor");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Trivial");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         arlOpenTicketsCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOpenTicketsCount;
   }

   public ArrayList<String> getClosedTasksBugsCountsCurrentMonth(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlClosedTasksBugsCountsCurrentMonth;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ");
      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS BUGSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS CRITICALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MAJORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS NORMALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MINORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(NOW() ,'01/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS TRIVIALTASKSCOUNT ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Critical");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Major");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Normal");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Minor");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Trivial");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         arlClosedTasksBugsCountsCurrentMonth = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedTasksBugsCountsCurrentMonth;
   }

   public ArrayList<String> getClosedTasksBugsCountsCurrentWeek(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlClosedTasksBugsCountsCurrentWeek;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ");
      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS BUGSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS CRITICALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MAJORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS NORMALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS MINORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(DATE_FORMAT(ADDDATE(CURDATE(), INTERVAL - WEEKDAY(CURDATE()) DAY) ,'%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND  STR_TO_DATE(DATE_FORMAT(NOW() ,'%d/%m/%Y'), '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ?  ");
      }
      sbSqlQuery.append(" ) AS TRIVIALTASKSCOUNT ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Critical");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Major");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Normal");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Minor");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Trivial");
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlClosedTasksBugsCountsCurrentWeek = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedTasksBugsCountsCurrentWeek;
   }

   public ArrayList<String> getRQAClosedBugsCounts(String strRQAPhase, String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQAClosedBugsCounts;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" AND RQA_PHASE = ?  ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         objPreparedStatement.setString(++intParam, strRQAPhase);
         arlRQAClosedBugsCounts = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQAClosedBugsCounts;
   }

   public ArrayList<String> getTicketsForETAUpdate(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketsForETAUpdate;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT REF_NO, TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, DATE_FORMAT(ETA, '%d/%b/%Y'), ");
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
      sbSqlQuery.append(" AND ( ");
      sbSqlQuery.append(" (TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '')) OR ");
      sbSqlQuery.append(" (TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '')) OR ");
      sbSqlQuery.append(" (TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '')) OR ");
      sbSqlQuery.append(" (TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND 90 - DATEDIFF(NOW(), CREATED_DATE) < ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '')) OR ");
      sbSqlQuery.append(" (TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY IN (?, ?) ");
      sbSqlQuery.append(" AND 120 - DATEDIFF(NOW(), CREATED_DATE) < ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '')) OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ETA, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" ) ");
      sbSqlQuery.append(" ORDER BY TICKET_CATEGORY, TICKET_PRIORITY, AGEING ASC  ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, "Critical");
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, "Major");
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, "Normal");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, "Minor");
         objPreparedStatement.setString(++intParam, "Trivial");
         objPreparedStatement.setInt(++intParam, AppSettings.getTopAgeingThreshold());

         arlTicketsForETAUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketsForETAUpdate;
   }

   public ArrayList<String> getInternalTicketsForETAUpdate(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlInternalTicketsForETAUpdate;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT ITD.REF_NO, ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, DATE_FORMAT(ITD.ETA, '%d/%b/%Y'), CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME) ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1 ");
      sbSqlQuery.append(" WHERE ITD.TICKET_STATUS  != ? ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = UD1.USER_ID ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = ? ");
      sbSqlQuery.append(" AND (ETA IS NULL OR ETA = '' OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ETA, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" ) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strAssignee);
         arlInternalTicketsForETAUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlInternalTicketsForETAUpdate;
   }


   public ArrayList<String> getInProgressOnHoldTickets(String strAssignee, String strPrevWorkingDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlInProgressOnHoldTickets;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT DISTINCT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, DATE_FORMAT(TD.ETA, '%d/%b/%Y'), TD.TICKET_STATUS ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, TICKET_ACTIVITY_HISTORY TAH  ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS IN (?) ");
      sbSqlQuery.append(" AND TD.REF_NO = TAH.REF_NO ");
      sbSqlQuery.append(" AND TD.TICKET_ID = TAH.TICKET_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TAH.ACTIVITY_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= STR_TO_DATE(?, '%d/%m/%Y')  ");
      sbSqlQuery.append(" AND TAH.TICKET_ACTIVITY_TYPE = ? ");
      sbSqlQuery.append(" ORDER BY TICKET_STATUS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         //objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
         objPreparedStatement.setString(++intParam, strPrevWorkingDate);
         objPreparedStatement.setString(++intParam, "Started");
         arlInProgressOnHoldTickets = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlInProgressOnHoldTickets;
   }

   public boolean isLatestCommentsNotAvailable(String strRefNo, String strTicketId, String strCurrentWorkingDate, String strPrevWorkingDate, String strAssignee, Connection objDBConnection) throws Exception
   {
      boolean blnLatestNotCommentsAvailable = true;
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCommentsAvailable;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT COUNT(CH.TICKET_ID)  ");
      sbSqlQuery.append(" FROM COMMENTS_HISTORY CH ");
      sbSqlQuery.append(" WHERE CH.REF_NO = ? ");
      sbSqlQuery.append(" AND CH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(CH.COMMENTS_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, strPrevWorkingDate);
         objPreparedStatement.setString(++intParam, strCurrentWorkingDate);
         arlCommentsAvailable = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCommentsAvailable != null && !arlCommentsAvailable.isEmpty() && Integer.parseInt(arlCommentsAvailable.get(0)) > 0)
         {
            blnLatestNotCommentsAvailable = false;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return blnLatestNotCommentsAvailable;

   }

   public ArrayList<String> getRQATicketsForETAUpdate(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQATicketsForETAUpdate;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT RTD.REF_NO, RTD.TICKET_ID, RTD.TICKET_DESC, RTD.TICKET_CATEGORY, RTD.TICKET_PRIORITY, ");
      sbSqlQuery.append("RTD.TICKET_STATUS, DATE_FORMAT(RTD.RECEIVED_DATE, '%d/%b/%Y'), RTD.ASSIGNEE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), DATE_FORMAT(RTD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append("FROM RQA_TICKET_DETAILS RTD, USER_DETAILS ASS  ");
      sbSqlQuery.append("WHERE TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ");
      sbSqlQuery.append(" AND RTD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND (PLANNED_END_DATE IS NULL OR PLANNED_END_DATE = '' OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" ) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strAssignee);
         arlRQATicketsForETAUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQATicketsForETAUpdate;
   }

   public int getReopenTicketsCount(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      int intReopenTicketsCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE REF_NO IN ");
      sbSqlQuery.append(" (SELECT REOPEN_REF_NO FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_INFLOW_TYPE LIKE '%Re-Opened%' ");
      sbSqlQuery.append(" AND REOPEN_REASON != ? ) ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.REOPEN_REASON_HELPING_OTHER_DOMAIN);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            intReopenTicketsCount = Integer.parseInt(arlTemp.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intReopenTicketsCount;
   }

}