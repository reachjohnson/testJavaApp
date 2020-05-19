package com.opt.reports.dao;

import com.opt.base.dao.BaseDAO;
import com.opt.exception.DAOException;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class SLAReportsDAO
{
   Logger objLogger = Logger.getLogger(SLAReportsDAO.class.getName());

   public int getReceivedTasksCount(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intCount, AppConstants.TASK);
         objPreparedStatement.setString(++intCount, strFromDate);
         objPreparedStatement.setString(++intCount, strToDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getReceivedBugsCount(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getClosedTasksCount(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getClosedBugsCount(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getCurrentOpenTasksCount(String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getCurrentOpenBugsCount(String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }


   public ArrayList<String> getReceivedTasks(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlReceivedTasks;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlReceivedTasks = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlReceivedTasks;
   }

   public ArrayList<String> getReceivedBugs(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlReceivedBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlReceivedBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlReceivedBugs;
   }

   public ArrayList<String> getClosedTasks(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedTasks;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlClosedTasks = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedTasks;
   }

   public ArrayList<String> getClosedBugs(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlClosedBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedBugs;
   }

   public ArrayList<String> getCurrentOpenTasks(String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCurrentOpenTasks;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCurrentOpenTasks = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenTasks;
   }

   public ArrayList<String> getCurrentOpenBugs(String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCurrentOpenBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCurrentOpenBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenBugs;
   }

   public int getCurrentOpenCountByModulePriority(String strTicketCategory, String strTicketModule, String strTicketPriority, String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_MODULE = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketModule);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getCurrentOpenOOSLACountByPriority(String strTicketCategory, String strTicketPriority, String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND (BACKLOG IS NULL OR TRIM(BACKLOG) = '') ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(?, '%d/%m/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getReceivedCountByPriority(String strTicketCategory, String strTicketPriority, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);

         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getClosedCountByPriority(String strTicketCategory, String strTicketPriority, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
      {
         sbSqlQuery.append(" AND TD.TICKET_RESOLUTION != ? ");
      }*/
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
         {
            objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         }*/
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public int getCurrentOpenCountByPriority(String strTicketCategory, String strTicketPriority, String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty())
         {
            intCount = Integer.parseInt(arlCount.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intCount;
   }

   public String getInSLACountByPriority(String strTicketCategory, String strTicketPriority, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      String strCount = "0";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT SUM(STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y')) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '')");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);

         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlCount != null && !arlCount.isEmpty() && arlCount.get(0).length() > 0)
         {
            strCount = arlCount.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strCount;
   }

   public String getOutSLACountByPriority(String strTicketCategory, String strTicketPriority, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      String strCount = "0";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT SUM(STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y')) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '')");
      /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
      {
         sbSqlQuery.append(" AND TD.TICKET_RESOLUTION != ? ");
      }*/
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
         {
            objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         }*/
         arlCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);

         if (arlCount != null && !arlCount.isEmpty() && arlCount.get(0).length() > 0)
         {
            strCount = arlCount.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strCount;
   }

   public ArrayList<String> getResolutionDatesByPriority(String strTicketCategory, String strTicketPriority, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResolutionDatesByPriority;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(MIN(TH.ACTIVITY_DATE), '%d/%b/%Y %h:%i %p'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y %h:%i %p') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, TICKET_ACTIVITY_HISTORY TH ");
      sbSqlQuery.append(" WHERE TD.REF_NO = TH.REF_NO ");
      sbSqlQuery.append(" AND TD.TICKET_ID = TH.TICKET_ID ");
      sbSqlQuery.append(" AND TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TD.BACKLOG IS NULL ");
      sbSqlQuery.append(" AND TH.TICKET_ACTIVITY_TYPE = ? ");
      sbSqlQuery.append(" GROUP BY TH.REF_NO ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strTicketPriority);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_START);
         arlResolutionDatesByPriority = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResolutionDatesByPriority;
   }

   public ArrayList<String> getTaskResolutionTypesCount(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResolutionTypesCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_RESOLUTION, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      //sbSqlQuery.append(" AND TICKET_RESOLUTION != ? ");
      sbSqlQuery.append(" AND NOT (TICKET_RESOLUTION = ? AND MOVED_DOMAIN IN (?,?,?) )");
      sbSqlQuery.append(" GROUP BY TD.TICKET_RESOLUTION ");
      sbSqlQuery.append(" ORDER BY TD.TICKET_RESOLUTION ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         //objPreparedStatement.setString(++intParam, AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED);
         objPreparedStatement.setString(++intParam, AppConstants.TASK_CONVERTED_BUG_MOVED);
         objPreparedStatement.setString(++intParam, AppConstants.AP);
         objPreparedStatement.setString(++intParam, AppConstants.ACQUIRING);
         objPreparedStatement.setString(++intParam, AppConstants.BILLING_PRODUCT);
         arlResolutionTypesCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResolutionTypesCount;
   }

   public ArrayList<String> getResolutionTypesCount(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResolutionTypesCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_RESOLUTION, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
      {
         sbSqlQuery.append(" AND TD.TICKET_RESOLUTION != ? ");
      }*/
      sbSqlQuery.append(" GROUP BY TD.TICKET_RESOLUTION ");
      sbSqlQuery.append(" ORDER BY TD.TICKET_RESOLUTION ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
         {
            objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         }*/
         arlResolutionTypesCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResolutionTypesCount;
   }


   public ArrayList<String> getConvertedBugsMovedTeamsCount(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlMovedTeamsCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.MOVED_DOMAIN, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.MOVED_DOMAIN IS NOT NULL AND TD.MOVED_DOMAIN != '' ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TICKET_RESOLUTION = ? ");
      sbSqlQuery.append(" AND MOVED_DOMAIN NOT IN (?,?,?) ");
      sbSqlQuery.append(" GROUP BY TD.MOVED_DOMAIN ");
      sbSqlQuery.append(" ORDER BY TD.MOVED_DOMAIN ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.TASK_CONVERTED_BUG_MOVED);
         objPreparedStatement.setString(++intParam, AppConstants.AP);
         objPreparedStatement.setString(++intParam, AppConstants.ACQUIRING);
         objPreparedStatement.setString(++intParam, AppConstants.BILLING_PRODUCT);

         arlMovedTeamsCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlMovedTeamsCount;
   }

   public ArrayList<String> getTasksOthersMovedTeamsCount(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlMovedTeamsCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.MOVED_DOMAIN, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.MOVED_DOMAIN IS NOT NULL AND TD.MOVED_DOMAIN != '' ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append("  AND TICKET_RESOLUTION IN (?, ?) ");
      sbSqlQuery.append(" GROUP BY TD.MOVED_DOMAIN ");
      sbSqlQuery.append(" ORDER BY TD.MOVED_DOMAIN ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.ENQUIRY_TASK_MOVED);
         objPreparedStatement.setString(++intParam, AppConstants.TASK_MOVED);
         arlMovedTeamsCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlMovedTeamsCount;
   }


   public ArrayList<String> getBugsMovedTeamsCount(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlMovedTeamsCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.MOVED_DOMAIN, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.MOVED_DOMAIN IS NOT NULL AND TD.MOVED_DOMAIN != '' ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" GROUP BY TD.MOVED_DOMAIN ");
      sbSqlQuery.append(" ORDER BY TD.MOVED_DOMAIN ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlMovedTeamsCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlMovedTeamsCount;
   }


   public ArrayList<String> getClosedOOSLATicketIds(String strTicketCategory, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedOOSLATicketIds;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), TD.COMMENTS, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y')  ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
      {
         sbSqlQuery.append(" AND TD.TICKET_RESOLUTION != ? ");
      }*/
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         /*if (strTicketCategory.equalsIgnoreCase(AppConstants.BUG))
         {
            objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         }*/
         arlClosedOOSLATicketIds = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedOOSLATicketIds;
   }

   public ArrayList<String> getCurrentOpenOOSLATicketIds(String strTicketCategory, String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCurrentOpenOOSLATicketIds;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), TD.COMMENTS, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND (TD.BACKLOG IS NULL OR TRIM(TD.BACKLOG) = '') ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketCategory);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlCurrentOpenOOSLATicketIds = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentOpenOOSLATicketIds;
   }

   public ArrayList<String> getTicketProgressHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketProgressHistory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ' - ', DATE_FORMAT(TH.END_DATE, '%d/%b/%Y %h:%i %p'), ' - ', TH.COMMENTS) ");
      sbSqlQuery.append(" FROM TICKET_HISTORY TH , TICKET_DETAILS TD, USER_DETAILS UD  ");
      sbSqlQuery.append(" WHERE TH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TH.REF_NO = ? ");
      sbSqlQuery.append(" AND TH.REF_NO = TD.REF_NO ");
      sbSqlQuery.append(" AND TH.TICKET_ID = TD.TICKET_ID ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" ORDER BY TH.END_DATE DESC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         arlTicketProgressHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketProgressHistory;
   }

   public ArrayList<String> getTicketCommentsHistory(String strRefno, String strTicketId, String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketCommentsHistory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ' - ', DATE_FORMAT(UPDATED_DATE, '%d/%b/%Y %h:%i:%s %p'), ' - ', CONCAT(COMMENTS_CATEGORY, ' - ', COMMENTS)) ");
      sbSqlQuery.append(" FROM COMMENTS_HISTORY, USER_DETAILS UD  ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND REF_NO = ? ");
      sbSqlQuery.append(" AND COMMENTS_BY = UD.USER_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(UPDATED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" ORDER BY COMMENTS_HISTORY_REF_NO DESC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         objPreparedStatement.setString(++intParam, strAsOnDate);
         arlTicketCommentsHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketCommentsHistory;
   }

   public ArrayList<String> getTeams(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTeams;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE UD.USER_ID IN (SELECT DISTINCT ASSIGNEE FROM TICKET_DETAILS ) ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTeams = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTeams;
   }

   public ArrayList<String> getBugsInflowFromTasks(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlBugsInflowFromTasks;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MOVED_DOMAIN ");
      sbSqlQuery.append(" FROM TICKET_DETAILS   ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (TICKET_RESOLUTION = ? OR ");
      sbSqlQuery.append(" TICKET_RESOLUTION = ?) ");
      sbSqlQuery.append(" ORDER BY MOVED_DOMAIN ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED);
         objPreparedStatement.setString(++intParam, AppConstants.TASK_CONVERTED_BUG_MOVED);
         arlBugsInflowFromTasks = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlBugsInflowFromTasks;
   }

   public ArrayList<String> getTicketHistoryForMetrics(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketHistoryForMetrics;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TH.ASSIGNEE, TD.TEAM_WORKING, DATE_FORMAT(TH.START_DATE, '%d/%b/%Y %h:%i %p') AS STARTDATE, DATE_FORMAT(TH.END_DATE,  '%d/%b/%Y %h:%i %p') ");
      sbSqlQuery.append(" FROM TICKET_HISTORY TH, TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE ((STR_TO_DATE(DATE_FORMAT(TH.START_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) OR ");
      sbSqlQuery.append(" (STR_TO_DATE(DATE_FORMAT(TH.END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) OR ");
      sbSqlQuery.append(" (STR_TO_DATE(DATE_FORMAT(TH.START_DATE, '%d/%m/%Y'), '%d/%m/%Y') < ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND TH.END_DATE IS NULL) OR ");
      sbSqlQuery.append(" (STR_TO_DATE(DATE_FORMAT(TH.START_DATE, '%d/%m/%Y'), '%d/%m/%Y') < ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(DATE_FORMAT(TH.END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y'))) ");
      sbSqlQuery.append(" AND TH.START_DATE IS NOT NULL ");
      sbSqlQuery.append(" AND TH.TICKET_ID = TD.TICKET_ID ");
      sbSqlQuery.append(" AND TH.REF_NO = TD.REF_NO ");
      sbSqlQuery.append(" ORDER BY TH.ASSIGNEE, TH.TICKET_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlTicketHistoryForMetrics = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketHistoryForMetrics;
   }

   public ArrayList<String> getClosedTicketsForGivenDates(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedTicketsForGivenDates;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y') AS END_DATE, ASSIGNEE ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      //sbSqlQuery.append(" AND TICKET_RESOLUTION != ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         //objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         arlClosedTicketsForGivenDates = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedTicketsForGivenDates;
   }

   public ArrayList<String> getReceivedTicketsForGivenDates(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlReceivedTicketsForGivenDates;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y') AS RECEIVED_DATE, ASSIGNEE ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE (STR_TO_DATE(DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlReceivedTicketsForGivenDates = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlReceivedTicketsForGivenDates;
   }

   public ArrayList<String> getResourceCountForGivenDates(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResourceCountForGivenDates;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(DATE, '%d/%m/%Y') AS DATE, LIVE_BUGS ");
      sbSqlQuery.append(" FROM RESOURCE_COUNT ");
      sbSqlQuery.append(" WHERE (STR_TO_DATE(DATE_FORMAT(DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlResourceCountForGivenDates = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResourceCountForGivenDates;
   }

   public ArrayList<String> getActiveAssigneesForGivenDates(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlActiveAssigneesForGivenDates;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) AS ASSIGNEE_NAME ");
      sbSqlQuery.append(" FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE ACTIVE_FLAG = 'Y'  ");
      sbSqlQuery.append(" AND ASSIGNEE_FLAG = 'Y'  ");
      sbSqlQuery.append(" ORDER BY ASSIGNEE_NAME");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlActiveAssigneesForGivenDates = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlActiveAssigneesForGivenDates;
   }

   public ArrayList<String> getResourceMatrix(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlResourceMatrix;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT RM.USER_ID, DATE_FORMAT(RM.DATE, '%d/%m/%Y'), RM.LIVE_BUGS ");
      sbSqlQuery.append(" FROM RESOURCE_MATRIX RM, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE RM.USER_ID = UD.USER_ID ");
      sbSqlQuery.append(" AND UD.ACTIVE_FLAG = 'Y' ");
      sbSqlQuery.append(" AND UD.ASSIGNEE_FLAG = 'Y' ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(RM.DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      sbSqlQuery.append(" ORDER BY UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
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

}
