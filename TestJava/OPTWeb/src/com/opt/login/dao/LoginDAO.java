package com.opt.login.dao;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.Connection;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class LoginDAO
{
   Logger objLogger = Logger.getLogger(LoginDAO.class.getName());

   public ArrayList<String> validateLogin(ArrayList<String> arlLoginDetails, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlLoginCount;
      ArrayList<String> arlUserDetails = new ArrayList<>();
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID FROM USER_DETAILS ");
      sbSqlQuery.append("WHERE USER_ID = ? ");
      sbSqlQuery.append("AND PASSWORD = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, arlLoginDetails.get(0));
         objPreparedStatement.setString(++intParam, arlLoginDetails.get(1));
         arlLoginCount = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      if (arlLoginCount != null && !arlLoginCount.isEmpty())
      {
         if (!arlLoginCount.get(0).equals(""))
         {
            intParam = 0;
            ArrayList<String> arlTemp;
            arlUserDetails.add("ValidateSuccess");
            sbSqlQuery = new StringBuilder("SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) AS EMPNAME, ACTIVE_FLAG, ADMIN_FLAG, ASSIGNEE_FLAG, TEAM ");
            sbSqlQuery.append("FROM USER_DETAILS ");
            sbSqlQuery.append("WHERE USER_ID = ? ");
            sbSqlQuery.append("AND ACTIVE_FLAG = 'Y'");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setString(++intParam, arlLoginCount.get(0));
               arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            }
            catch (Exception objException)
            {
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }

            if (arlTemp != null && !arlTemp.isEmpty())
            {
               arlUserDetails.add("UserActive");
               arlUserDetails.addAll(arlTemp);
            }
            else
            {
               arlUserDetails.add("UserInActive");
            }
         }
      }
      else
      {
         arlUserDetails.add("InvalidUseridPassword");
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlUserDetails;
   }

   public ArrayList<String> getUserAccessRights(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlUserAccessRights;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT WRITE_ACCESS,CREATE_TASK,CREATE_BUG,CREATE_INTERNAL_TICKET,MODIFY_CURRENT_TICKETS,REOPEN_CLOSED_TICKETS,");
      sbSqlQuery.append("UPDATE_LEAVE_PLANS,HOLIDAY_CALENDAR,ADD_USER,MODIFY_USER,ACCESS_RIGHTS, TICKETS_STATUS,CURRENT_STATUS,CURRENT_TICKETS,CLOSED_TICKETS,SLA_MISSED_TICKETS,RESOURCE_ALLOCATION,");
      sbSqlQuery.append("SLA_REPORTS,SLA_METRICS,CURRENT_INTERNAL_TICKETS,CLOSED_INTERNAL_TICKETS,VIEW_LEAVE_PLANS,LIST_OF_HOLIDAYS,TEAM_CONTACTS,DOMAIN_POC, ");
      sbSqlQuery.append(" CREATE_RQA_BUG, REOPEN_RQA_BUG, MODIFY_RQA_TICKETS, RQA_DAILY_REPORT, RQA_CURRENT_STATUS, RQA_METRICS, RQA_SETTINGS, RESOURCE_MATRIX, IT_ADMIN_CHANGES, COMMON_SETTINGS, ");
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

   public void changePassword(String strLoginId, String strPassword, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE USER_DETAILS SET ");
      sbSqlQuery.append("PASSWORD = ? ");
      sbSqlQuery.append("WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strPassword);
         objPreparedStatement.setString(++intParam, strLoginId);
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

   public String getLastLoginTime(String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strLastLoginTime = "";
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT DATE_FORMAT(MAX(LOGIN_TIME), '%d/%b/%Y %h:%i %p') LAST_LOGIN FROM LOGIN_HISTORY ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strLastLoginTime = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      intParam = 0;
      sbSqlQuery = new StringBuilder("INSERT INTO LOGIN_HISTORY ");
      sbSqlQuery.append("(USER_ID, LOGIN_TIME) VALUES ");
      sbSqlQuery.append("(?, ?)");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
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
      return strLastLoginTime;
   }
}