package com.opt.calendar.dao;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class CalendarDAO
{
   Logger objLogger = Logger.getLogger(CalendarDAO.class.getName());

   public ArrayList<String> getLeavePlans(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlEmpLeavePlans;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, DATE_FORMAT(FROM_DATE, '%d/%b/%Y'), DATE_FORMAT(TO_DATE, '%d/%b/%Y'), LEAVE_REASON  ");
      sbSqlQuery.append("FROM LEAVE_PLANS ");
      sbSqlQuery.append("WHERE USER_ID = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%b/%Y'), '%d/%b/%Y') <= STR_TO_DATE(DATE_FORMAT(TO_DATE, '%d/%b/%Y'), '%d/%b/%Y') ");
      sbSqlQuery.append(" ORDER BY FROM_DATE ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         arlEmpLeavePlans = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlEmpLeavePlans;
   }

   public ArrayList<String> getLeavePlansForRefNo(String strLeaveRefNo, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlEmpLeavePlans;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(FROM_DATE, '%d/%b/%Y'), DATE_FORMAT(TO_DATE, '%d/%b/%Y'), LEAVE_REASON  ");
      sbSqlQuery.append(" FROM LEAVE_PLANS ");
      sbSqlQuery.append(" WHERE REF_NO = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strLeaveRefNo));
         arlEmpLeavePlans = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlEmpLeavePlans;
   }

   public void addLeavePlans(String strUserId, String strLeaveFromDate, String strLeaveToDate, String strLeaveReason, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO LEAVE_PLANS ");
      sbSqlQuery.append("(USER_ID, FROM_DATE, TO_DATE, LEAVE_REASON) ");
      sbSqlQuery.append(" VALUES(?, ?, ?, ?) ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strLeaveFromDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strLeaveToDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, strLeaveReason);
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

   public void deleteLeavePlans(String strRefNo, String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("DELETE FROM LEAVE_PLANS WHERE USER_ID = ? AND REF_NO = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         ;
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
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

   public boolean checkHolidayCalendarExists(String strHolidayDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      boolean blnHolidayCalendarExists = false;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT REF_NO FROM HOLIDAY_CALENDAR ");
      sbSqlQuery.append(" WHERE HOLIDAY_DATE = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strHolidayDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            blnHolidayCalendarExists = true;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return blnHolidayCalendarExists;
   }

   public void addHolidayCalendar(String strHolidayDate, String strHolidayName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO HOLIDAY_CALENDAR ");
      sbSqlQuery.append("( HOLIDAY_DATE, HOLIDAY_NAME ) ");
      sbSqlQuery.append("VALUES(?, ?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strHolidayDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, strHolidayName);
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

   public void deleteHolidayCalendar(String strRefNo, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("DELETE FROM HOLIDAY_CALENDAR WHERE REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
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
}