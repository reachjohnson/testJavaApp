package com.opt.common.dao;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class CommonDAO
{
   Logger objLogger = Logger.getLogger(CommonDAO.class.getName());

   public String getAssigneeName(String strUserId, Connection objDBConnection) throws DAOException
   {
      String strAssigneeName = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(FIRST_NAME, ' ', LAST_NAME), USER_ID FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strAssigneeName = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strAssigneeName;
   }

   public String getResourceName(String strUserId, Connection objDBConnection) throws DAOException
   {
      String strResourceName = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(FIRST_NAME, ' ', LAST_NAME) FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE USER_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strResourceName = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strResourceName;
   }

   public String getTicketAssignee(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strAssignee = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ASSIGNEE FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strAssignee = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strAssignee;
   }

   public String getTicketCreatedBy(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strCreatedBy = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ACTIVITY_BY ");
      sbSqlQuery.append(" FROM TICKET_ACTIVITY_HISTORY ");
      sbSqlQuery.append(" WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_ACTIVITY_TYPE IN (?, ?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_NEW);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_REOPEN);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strCreatedBy = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strCreatedBy;
   }

   public String getRQABugCreatedBy(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strCreatedBy = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ACTIVITY_BY ");
      sbSqlQuery.append(" FROM RQA_TICKET_ACTIVITY_HISTORY ");
      sbSqlQuery.append(" WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_ACTIVITY_TYPE IN (?, ?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_NEW);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_REOPEN);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strCreatedBy = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strCreatedBy;
   }

   public String getRQATicketAssignee(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strTicketAssignee = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ASSIGNEE FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strTicketAssignee = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strTicketAssignee;
   }

   public String getTicketRefNoForNotStarted(String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strRefNo = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO ");
      sbSqlQuery.append(" FROM TICKET_DETAILS");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arltemp != null && !arltemp.isEmpty())
         {
            strRefNo = arltemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

   public String getRQATicketRefNoForNotStarted(String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strRefNo = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arltemp != null && !arltemp.isEmpty())
         {
            strRefNo = arltemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

   public ArrayList<String> getHolidayCalendar(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlHolidays;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, DATE_FORMAT(HOLIDAY_DATE, '%d/%b/%Y'), HOLIDAY_NAME FROM HOLIDAY_CALENDAR");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%b/%Y'), '%d/%b/%Y') <= STR_TO_DATE(DATE_FORMAT(HOLIDAY_DATE, '%d/%b/%Y'), '%d/%b/%Y') ");
      sbSqlQuery.append(" ORDER BY HOLIDAY_DATE ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlHolidays = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlHolidays;
   }

   public ArrayList<String> getHolidayCalendarForCalc(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlHolidayCalendar;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(HOLIDAY_DATE, '%d/%m/%Y') FROM HOLIDAY_CALENDAR");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlHolidayCalendar = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlHolidayCalendar;
   }

   public ArrayList<String> getAllAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) ");
      sbSqlQuery.append(" FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE ACTIVE_FLAG = 'Y' ");
      sbSqlQuery.append(" AND ASSIGNEE_FLAG = 'Y' ");
      sbSqlQuery.append(" ORDER BY CONCAT(FIRST_NAME, ' ', LAST_NAME) ASC ");
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

   public ArrayList<String> getAllAssigneesForInternalTickets(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) ");
      sbSqlQuery.append(" FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE ACTIVE_FLAG = 'Y' ");
      sbSqlQuery.append(" AND TEAM != ? ");
      sbSqlQuery.append(" ORDER BY CONCAT(FIRST_NAME, ' ', LAST_NAME) ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.MGMT_TEAM);
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


   public ArrayList<String> getTeamContacts(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTeamContacts;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT NAME, CONTACT_NO, CORP_ID, CSC_ID FROM TEAM_CONTACTS ORDER BY NAME ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTeamContacts = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTeamContacts;
   }

   public ArrayList<String> getDomainPOCNames(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlDomainPOCNames;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT DOMAIN, DL_ID FROM DOMAIN_CONTACTS ORDER BY DOMAIN ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlDomainPOCNames = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlDomainPOCNames;
   }

   public ArrayList<String> getDomainPOCDetails(String strDomainName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlDomainPOCDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT POC_NAME, CORP_ID, POC_CATEGORY FROM DOMAIN_CONTACTS ");
      sbSqlQuery.append(" WHERE DOMAIN = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strDomainName);
         arlDomainPOCDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlDomainPOCDetails;
   }

   public ArrayList<String> getSearchTicketDetails(String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlSearchTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY,  DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y') AS RECEIVEDDATE, DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') AS SLAENDDATE, ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') AS ACTUALENDDATE, TD.TICKET_STATUS, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         arlSearchTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      if (arlSearchTicketDetails == null || arlSearchTicketDetails.isEmpty())
      {
         ArrayList<String> arlRefNo;
         intParam = 0;
         sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
         sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
         sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
         sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
         sbSqlQuery.append(" AND TD.TICKET_ID NOT IN (SELECT TICKET_ID FROM TICKET_DETAILS WHERE TICKET_ID = ? AND TICKET_STATUS != ? ) ");
         sbSqlQuery.append(" GROUP BY TD.TICKET_ID ");

         try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
         {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlRefNo = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
         }
         if (arlRefNo != null && !arlRefNo.isEmpty())
         {
            intParam = 0;
            String strRefNo = arlRefNo.get(0);
            sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
            sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY,  DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y') AS RECEIVEDDATE, DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') AS SLAENDDATE, ");
            sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y'), ");
            sbSqlQuery.append(" TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') AS ACTUALENDDATE, TD.TICKET_STATUS, ");
            sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
            sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
            sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
            sbSqlQuery.append(" AND TD.REF_NO = ? ");
            sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
            sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
            {
               objPreparedStatement.setString(++intParam, strTicketId);
               objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
               objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
               arlSearchTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            }
            catch (Exception objException)
            {
               objLogger.error(objException, objException.fillInStackTrace());
               throw new DAOException(objException.getMessage(), objException);
            }
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlSearchTicketDetails;
   }

   public ArrayList<String> getTriagingSearchTicketDetails(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlSearchTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO), TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY,  DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y') AS RECEIVEDDATE, DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') AS SLAENDDATE, ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') AS ACTUALENDDATE, TD.TICKET_STATUS, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.REF_NO = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlSearchTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlSearchTicketDetails;
   }

   public ArrayList<String> getSearchTicketHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketHistory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(TH.START_DATE, '%d/%b/%Y %h:%i %p') AS STARTDATE, ");
      sbSqlQuery.append(" DATE_FORMAT(IFNULL(TH.END_DATE, NOW()), '%d/%b/%Y %h:%i %p'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), TH.COMMENTS, ");
      sbSqlQuery.append(" IFNULL(DATE_FORMAT(TH.END_DATE, '%d/%b/%Y %h:%i %p'), 'As Of Now') ");
      sbSqlQuery.append(" FROM TICKET_HISTORY TH, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TH.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND TH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TH.REF_NO = ? ");
      sbSqlQuery.append(" ORDER BY TH.TICKET_HISTORY_REF_NO DESC");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         arlTicketHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketHistory;
   }

   public ArrayList<String> getSearchTicketActivityHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketHistory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TAH.TICKET_ACTIVITY_TYPE, TAH.COMMENTS,  ");
      sbSqlQuery.append(" DATE_FORMAT(TAH.ACTIVITY_DATE, '%d/%b/%Y %h:%i:%s %p') AS ACTIVITY_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_ACTIVITY_HISTORY TAH, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TAH.ACTIVITY_BY = ASS.USER_ID ");
      sbSqlQuery.append(" AND TAH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TAH.REF_NO = ? ");
      sbSqlQuery.append(" ORDER BY TAH.TICKET_ACTIVITY_HISTORY_REF_NO DESC");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         arlTicketHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketHistory;
   }

   public ArrayList<String> getSearchCommentsHistory(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCommentsCategory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CH.COMMENTS_CATEGORY, CH.COMMENTS,  ");
      sbSqlQuery.append(" DATE_FORMAT(CH.UPDATED_DATE, '%d/%b/%Y %h:%i:%s %p') AS COMMENTS_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append(" FROM COMMENTS_HISTORY CH, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE CH.COMMENTS_BY = ASS.USER_ID ");
      sbSqlQuery.append(" AND CH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND CH.REF_NO = ? ");
      sbSqlQuery.append(" ORDER BY CH.COMMENTS_HISTORY_REF_NO DESC");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlCommentsCategory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCommentsCategory;
   }

   public String getSearchTriagingRefNo(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strTriagingRefNo = "";
      ArrayList<String> arlTemp;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TRIAGING_REF_NO ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strTriagingRefNo = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strTriagingRefNo;
   }

   public String getRQACurrentPhase(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strRQACurrentPhase = "";
      ArrayList<String> arlTemp;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CURRENT_PHASE FROM RQA_SETTINGS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strRQACurrentPhase = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRQACurrentPhase;
   }

   public String getRQAMailCutOffTime(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strMailCutOffTime = "";
      ArrayList<String> arlTemp;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAIL_CUTOFF_TIME FROM RQA_SETTINGS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strMailCutOffTime = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strMailCutOffTime;
   }

   public ArrayList<String> getRQAMailSettings(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQAMailSettings;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAIL_TO_TRIGGER, MAIL_CUTOFF_TIME, MAIL_TO_IDS, MAIL_CC_IDS, MAIL_SUBJECT, MAIL_HEADER, CURRENT_PHASE, RQA_POC_MAIL_IDS FROM RQA_SETTINGS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlRQAMailSettings = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQAMailSettings;
   }

   public int getCurrentOpenCount(String strAsOnDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCount;
      int intCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') <= ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND (ACTUAL_END_DATE IS NULL OR ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y')) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
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

   public String getITCreatedBy(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strCreatedBy = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CREATED_BY FROM INTERNAL_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strCreatedBy = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strCreatedBy;
   }

   public int getClosedTicketsForGivenDates(String strAssignee, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      int intClosedCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      sbSqlQuery.append(" AND TICKET_RESOLUTION != ? ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.BUG_FIX_EXPECTED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(5, strAssignee);
         }
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            intClosedCount = Integer.parseInt(arlTemp.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intClosedCount;
   }

   public int getClosedITForGivenDates(String strAssignee, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      int intClosedCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            intClosedCount = Integer.parseInt(arlTemp.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intClosedCount;
   }

   public int getClosedRQABugsForGivenDates(String strAssignee, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      int intClosedCount = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND (STR_TO_DATE(DATE_FORMAT(ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')) ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND ASSIGNEE = ? ");
      }
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            intClosedCount = Integer.parseInt(arlTemp.get(0));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return intClosedCount;
   }

   public ArrayList<String> getOpenTicketsETAHistory(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlOpenTicketsETAHistory;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT TD.REF_NO, TD.TICKET_ID, TAH.COMMENTS ");
      sbSqlQuery.append(" FROM TICKET_ACTIVITY_HISTORY TAH, TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TAH.REF_NO = TD.REF_NO ");
      sbSqlQuery.append(" AND TAH.TICKET_ID = TD.TICKET_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TD.ETA IS NOT NULL ");
      sbSqlQuery.append(" AND TAH.TICKET_ACTIVITY_TYPE = 'ETA Updated' ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
      }

      sbSqlQuery.append(" ORDER BY TAH.TICKET_ACTIVITY_HISTORY_REF_NO ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }
         arlOpenTicketsETAHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOpenTicketsETAHistory;
   }

   public ArrayList<String> getStartUpValues(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlStartUpValues = new ArrayList<>();
      StringBuilder sbSqlQuery = new StringBuilder("SELECT OOOSLA_HIGHLIGHT,DB_BACKUP,ETES_REMINDER, ");
      sbSqlQuery.append(" HIGH_PRIORITY_NOT_STARTED, OOSLA_NOT_STARTED, RQA_ETA_REMINDER, STATUS_UPDATE_REMINDER, OPEN_TICKETS_PRIORITY, TICKETS_PROGRESS_CHECK, ");
      sbSqlQuery.append(" ETA_BUGS, ETA_CRITICAL_TASKS, ETA_MAJOR_TASKS, ETA_TOPAGEING_TASKS, RESCHEDULE_CROSSED_ETA, ETA_INTERNAL_TICKETS, ");
      sbSqlQuery.append(" ETA_RQA_BUGS, TOPAGEING_THRESHOLD, ETA_RESCHEDULE_MAX, DAILY_STATUS ");
      sbSqlQuery.append(" FROM COMMON_SETTINGS ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlStartUpValues = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlStartUpValues;
   }

   public ArrayList<String> getSearchedLveTickets(String strTicketDescription, String strTicketRootCause, String strTicketComments, String strTicketResolution,
                                                  Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlSearchedLiveTickets;
      StringBuilder sbSqlQuery = new StringBuilder(" SELECT REF_NO, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_RESOLUTION, TICKET_INFLOW_TYPE ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY IN (?, ?) ");
      if(strTicketDescription.length() > 0)
      {
         sbSqlQuery.append(" AND TICKET_DESC LIKE ? ");
      }
      if(strTicketRootCause.length() > 0)
      {
         sbSqlQuery.append(" AND TICKET_ROOT_CAUSE LIKE ? ");
      }
      if(strTicketComments.length() > 0)
      {
         sbSqlQuery.append(" AND COMMENTS LIKE ? ");
      }
      if(strTicketResolution.length() > 0)
      {
         sbSqlQuery.append(" AND TICKET_RESOLUTION = ? ");
      }

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         if(strTicketDescription.length() > 0)
         {
            objPreparedStatement.setString(++intParam, "%"+strTicketDescription+"%");
         }
         if(strTicketRootCause.length() > 0)
         {
            objPreparedStatement.setString(++intParam, "%"+strTicketRootCause+"%");
         }
         if(strTicketComments.length() > 0)
         {
            objPreparedStatement.setString(++intParam, "%"+strTicketComments+"%");
         }
         if(strTicketResolution.length() > 0)
         {
            objPreparedStatement.setString(++intParam, strTicketResolution);
         }

         arlSearchedLiveTickets = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlSearchedLiveTickets;
   }

   public ArrayList<String> getFeedbackCategory(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTeamContacts;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CATEGORY FROM FEEDBACK_CATEGORY ORDER BY CATEGORY ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlTeamContacts = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTeamContacts;
   }

   public ArrayList<String> getResourceForFeedback(Connection objDBConnection) throws DAOException
   {
      ArrayList<String> arlResourceForFeedback = null;
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID, CONCAT(FIRST_NAME, ' ', LAST_NAME) FROM USER_DETAILS ");
      sbSqlQuery.append(" WHERE TEAM != ? ");
      sbSqlQuery.append(" AND ACTIVE_FLAG = 'Y' ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.MGMT_TEAM);
         arlResourceForFeedback = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlResourceForFeedback;
   }

   public void UpdateTeamFeedback(String strUserId, String strFeedbackDate, String strFeedbackCategory,
                               String strFeedbackComments, String strFeedbackBy, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TEAM_FEEDBACK ");
      sbSqlQuery.append("(USER_ID, FEEDBACK_DATE, FEEDBACK_CATEGORY, FEEDBACK_COMMENTS, FEEDBACK_BY) VALUES ");
      sbSqlQuery.append("(?, ?, ?, ?, ?)");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strUserId);
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strFeedbackDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, strFeedbackCategory);
         objPreparedStatement.setString(++intParam, strFeedbackComments);
         objPreparedStatement.setString(++intParam, strFeedbackBy);
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

   public ArrayList<String> getTeamFeedbackDetails(String strResource, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      ArrayList<String> arlTeamFeedbackDetails = null;
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DATE_FORMAT(TF.FEEDBACK_DATE, '%d/%m/%Y'), TF.FEEDBACK_CATEGORY, TF.FEEDBACK_COMMENTS, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TEAM_FEEDBACK TF, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(TF.FEEDBACK_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      if(!strResource.equalsIgnoreCase("All Resources"))
      {
         sbSqlQuery.append(" AND USER_ID = ? ");
      }
      sbSqlQuery.append(" AND TF.USER_ID = UD.USER_ID ");
      sbSqlQuery.append(" ORDER BY TF.USER_ID, TF.FEEDBACK_DATE DESC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         if(!strResource.equalsIgnoreCase("All Resources"))
         {
            objPreparedStatement.setString(++intParam, strResource);
         }
         arlTeamFeedbackDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTeamFeedbackDetails;
   }

   public String getLiveTicketRefNo(String strTicketId, Connection objDBConnection) throws DAOException
   {
      String strRefNo = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetailsForReopen = null;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if(arlTemp != null && !arlTemp.isEmpty())
         {
            strRefNo = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

   public String getRQATicketRefNo(String strTicketId, Connection objDBConnection) throws DAOException
   {
      String strRefNo = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetailsForReopen = null;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if(arlTemp != null && !arlTemp.isEmpty())
         {
            strRefNo = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

   public String getInternalTicketRefNo(String strTicketId, Connection objDBConnection) throws DAOException
   {
      String strRefNo = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetailsForReopen = null;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(REF_NO) ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if(arlTemp != null && !arlTemp.isEmpty())
         {
            strRefNo = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

   public String getSearchLiveTicketRefNo(String strTicketId, Connection objDBConnection) throws DAOException
   {
      String strRefNo = "";
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetailsForReopen = null;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");

      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if(arlTemp != null && !arlTemp.isEmpty())
         {
            strRefNo = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strRefNo;
   }

}