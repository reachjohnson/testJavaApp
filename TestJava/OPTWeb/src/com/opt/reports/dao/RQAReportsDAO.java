package com.opt.reports.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class RQAReportsDAO
{
   Logger objLogger = Logger.getLogger(RQAReportsDAO.class.getName());

   public ArrayList<String> getRQACurrentStatus(String strCurrentRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQACurrentStatus;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), ");
      sbSqlQuery.append(" TD.RQA_PHASE, TD.RQA_CYCLE, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), TD.TICKET_STATUS ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strCurrentRQAPhase);
         arlRQACurrentStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQACurrentStatus;
   }

   public ArrayList<String> getRQAListOfBugs(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQACurrentStatus;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), ");
      sbSqlQuery.append(" TD.RQA_PHASE, TD.RQA_CYCLE, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), TD.TICKET_STATUS ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strRQAPhase);
         arlRQACurrentStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQACurrentStatus;
   }

   public int getRQAOpeningBalance(String strCurrentRQAPhase, String strDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTemp;
      int intOpeningBalance = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE RQA_PHASE = ? ");
      sbSqlQuery.append(" AND RECEIVED_DATE <= STR_TO_DATE(?, '%Y-%m-%d %T') ");
      sbSqlQuery.append(" AND (ACTUAL_END_DATE IS NULL OR ACTUAL_END_DATE > STR_TO_DATE(?, '%Y-%m-%d %T')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strCurrentRQAPhase);
         objPreparedStatement.setString(++intParam, strDate);
         objPreparedStatement.setString(++intParam, strDate);
         arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            intOpeningBalance = Integer.parseInt(arlTemp.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intOpeningBalance;
   }

   public ArrayList<String> getRQANewBugs(String strCurrentRQAPhase, String strPrevDate, String strToday, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQANewBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), TD.TICKET_STATUS, DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.RECEIVED_DATE > STR_TO_DATE(?, '%Y-%m-%d %T') ");
      sbSqlQuery.append(" AND TD.RECEIVED_DATE <= STR_TO_DATE(?, '%Y-%m-%d %T') ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strCurrentRQAPhase);
         objPreparedStatement.setString(++intParam, strPrevDate);
         objPreparedStatement.setString(++intParam, strToday);
         arlRQANewBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQANewBugs;
   }

   public ArrayList<String> getRQAResolvedBugs(String strCurrentRQAPhase, String strPrevDate, String strToday, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQAResolvedBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.ACTUAL_END_DATE > STR_TO_DATE(?, '%Y-%m-%d %T') ");
      sbSqlQuery.append(" AND TD.ACTUAL_END_DATE <= STR_TO_DATE(?, '%Y-%m-%d %T') ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strCurrentRQAPhase);
         objPreparedStatement.setString(++intParam, strPrevDate);
         objPreparedStatement.setString(++intParam, strToday);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         arlRQAResolvedBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQAResolvedBugs;
   }

   public ArrayList<String> getRQACurrentOpenBugs(String strCurrentRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQACurrentOpenBugs;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y %h:%i %p'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strCurrentRQAPhase);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         arlRQACurrentOpenBugs = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQACurrentOpenBugs;
   }

   public ArrayList<String> getRQAPhases(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT RQA_PHASE ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ORDER BY RQA_PHASE");
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

   public int getReceivedRQABugsCount(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strRQAPhase);
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

   public int getClosedRQABugsCount(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strRQAPhase);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
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

   public int getCurrentOpenRQABugsCount(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TD.TICKET_ID) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strRQAPhase);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
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

   public ArrayList<String> getRQABugsResolution(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlResolutionTypes;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_RESOLUTION, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" GROUP BY TD.TICKET_RESOLUTION ");
      sbSqlQuery.append(" ORDER BY TD.TICKET_RESOLUTION ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strRQAPhase);
         arlResolutionTypes = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResolutionTypes;
   }

   public ArrayList<String> getRQABugsMovedTeams(String strRQAPhase, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlMovedTeams;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.MOVED_DOMAIN, COUNT(TD.TICKET_ID)  ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD");
      sbSqlQuery.append(" WHERE TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.RQA_PHASE = ? ");
      sbSqlQuery.append(" AND TD.MOVED_DOMAIN IS NOT NULL AND TD.MOVED_DOMAIN != '' ");
      sbSqlQuery.append(" GROUP BY TD.MOVED_DOMAIN ");
      sbSqlQuery.append(" ORDER BY TD.MOVED_DOMAIN ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strRQAPhase);
         arlMovedTeams = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlMovedTeams;
   }
}