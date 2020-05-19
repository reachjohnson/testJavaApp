package com.opt.reports.dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class ReportsDAO
{
   Logger objLogger = Logger.getLogger(ReportsDAO.class.getName());

   public ArrayList<String> getOpenTicketsAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
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

   public ArrayList<String> getAllAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
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


   public ArrayList<String> getTicketList(String strTicketCategory, String strAssignee, String strTicketStatus, String strTicketPriority, String strTicketModule, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketDetails;
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" TD.TICKET_STATUS, DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), ");
      sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), DATE_FORMAT(TD.ETA, '%d/%b/%Y'), ");
      sbSqlQuery.append(" CASE TD.TICKET_PRIORITY ");
      sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
      sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
      sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
      sbSqlQuery.append(" END AS AGEING ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");

      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
      }

      if (!strTicketStatus.equalsIgnoreCase(AppConstants.ALL_STATUS))
      {
         sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      }
      else
      {
         sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
      }
      if (!strTicketPriority.equalsIgnoreCase(AppConstants.ALL_PRIORITY))
      {
         sbSqlQuery.append(" AND TD.TICKET_PRIORITY = ? ");
      }
      if (!strTicketModule.equalsIgnoreCase(AppConstants.ALL_MODULE))
      {
         sbSqlQuery.append(" AND TD.TICKET_MODULE = ? ");
      }

      sbSqlQuery.append(" AND TD.TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" ORDER BY TD.TICKET_PRIORITY, AGEING ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         if (!strTicketStatus.equalsIgnoreCase(AppConstants.ALL_STATUS))
         {
            objPreparedStatement.setString(++intParam, strTicketStatus);
         }
         else
         {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         }
         if (!strTicketPriority.equalsIgnoreCase(AppConstants.ALL_PRIORITY))
         {
            objPreparedStatement.setString(++intParam, strTicketPriority);
         }
         if (!strTicketModule.equalsIgnoreCase(AppConstants.ALL_MODULE))
         {
            objPreparedStatement.setString(++intParam, strTicketModule);
         }

         objPreparedStatement.setString(++intParam, strTicketCategory);
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

   public ArrayList<String> getClosedTicketList(String strAssignee, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedTicketList;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" ORDER BY CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), TD.ACTUAL_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         arlClosedTicketList = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedTicketList;
   }

   public ArrayList<String> getSLAMissedTickets(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" TD.TICKET_STATUS, DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS  ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
      if (strAssignee.length() > 0)
      {
         sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TD.ACTUAL_END_DATE IS NULL ");
      sbSqlQuery.append(" ORDER BY TD.PLANNED_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         if (strAssignee.length() > 0)
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

   public ArrayList<String> getTicketsForResourceAllocation(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ASSIGNEE, TICKET_CATEGORY, TICKET_STATUS, STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS IN (?, ?, ? ) ");
      sbSqlQuery.append(" ORDER BY ASSIGNEE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
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

   public ArrayList<String> getCurrentLeave(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID FROM LEAVE_PLANS ");
      sbSqlQuery.append(" WHERE STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%b/%Y'), '%d/%b/%Y') BETWEEN STR_TO_DATE(DATE_FORMAT(FROM_DATE, '%d/%b/%Y'), '%d/%b/%Y') AND STR_TO_DATE(DATE_FORMAT(TO_DATE, '%d/%b/%Y'), '%d/%b/%Y') ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
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

   public ArrayList<String> getLeavePlansList(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlEmpLeavePlans;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), DATE_FORMAT(LP.FROM_DATE, '%d/%b/%Y'), DATE_FORMAT(LP.TO_DATE, '%d/%b/%Y'), LP.LEAVE_REASON  ");
      sbSqlQuery.append(" FROM LEAVE_PLANS LP, USER_DETAILS ASS");
      sbSqlQuery.append(" WHERE ASS.USER_ID = LP.USER_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%b/%Y'), '%d/%b/%Y') <= STR_TO_DATE(DATE_FORMAT(TO_DATE, '%d/%b/%Y'), '%d/%b/%Y') ");
      sbSqlQuery.append(" ORDER BY LP.FROM_DATE ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
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

   public ArrayList<String> getTeamAllocation(String strTeam, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlUserIds;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT USER_ID FROM USER_DETAILS  ");
      sbSqlQuery.append(" WHERE TEAM = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTeam);
         arlUserIds = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlUserIds;
   }

   public ArrayList<String> getTicketStatus(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketStatus;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, TICKET_ID, TICKET_DESC, TICKET_CATEGORY, TICKET_STATUS, ");
      sbSqlQuery.append(" DATE_FORMAT(RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(PLANNED_END_DATE, '%d/%b/%Y'), STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_STATUS IN (?, ?, ?) ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
         objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
         objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
         objPreparedStatement.setString(++intParam, strAssignee);
         arlTicketStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketStatus;
   }

   public ArrayList<String> getCurrentTicketsForStatus(String strTicketStatus, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlNotStartedTicketsCurrentStatus;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), TD.TICKET_DESC, TD.TICKET_CATEGORY ");
      sbSqlQuery.append(" FROM USER_DETAILS UD1, TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD1.USER_ID ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME) ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketStatus);
         arlNotStartedTicketsCurrentStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlNotStartedTicketsCurrentStatus;
   }

   public ArrayList<String> getCurrentStatusFromCommentsHistory(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCurrentStatusFromCommentsHistory = new ArrayList<>();
      ArrayList<String> arlMaxCommentsDate;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(CH.UPDATED_DATE) FROM COMMENTS_HISTORY CH ");
      sbSqlQuery.append(" WHERE CH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND CH.REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlMaxCommentsDate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      if (arlMaxCommentsDate != null && !arlMaxCommentsDate.isEmpty())
      {
         intParam = 0;
         String strMaxCommentsDate = arlMaxCommentsDate.get(0);
         sbSqlQuery = new StringBuilder("SELECT CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), DATE_FORMAT(CH.UPDATED_DATE, '%d/%b/%Y %h:%i:%s %p'), CONCAT(CH.COMMENTS_CATEGORY, ' - ', CH.COMMENTS) ");
         sbSqlQuery.append(" FROM COMMENTS_HISTORY CH, USER_DETAILS UD ");
         sbSqlQuery.append(" WHERE CH.TICKET_ID = ? ");
         sbSqlQuery.append(" AND CH.REF_NO = ? ");
         sbSqlQuery.append(" AND CH.COMMENTS_BY = UD.USER_ID ");
         sbSqlQuery.append(" AND CH.UPDATED_DATE = ? ");
         try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
         {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strMaxCommentsDate);
            arlCurrentStatusFromCommentsHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentStatusFromCommentsHistory;
   }

   public ArrayList<String> getCurrentStatusFromTicketHistory(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCurrentStatusFromTicketHistory = new ArrayList<>();
      ArrayList<String> arlMaxCommentsDate;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TH.END_DATE) FROM TICKET_HISTORY TH ");
      sbSqlQuery.append(" WHERE TH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TH.REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlMaxCommentsDate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      if (arlMaxCommentsDate != null && !arlMaxCommentsDate.isEmpty())
      {
         String strMaxCommentsDate = arlMaxCommentsDate.get(0);
         intParam = 0;
         sbSqlQuery = new StringBuilder("SELECT CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), DATE_FORMAT(TH.END_DATE, '%d/%b/%Y %h:%i %p'), TH.COMMENTS ");
         sbSqlQuery.append(" FROM TICKET_HISTORY TH, USER_DETAILS UD ");
         sbSqlQuery.append(" WHERE TH.TICKET_ID = ? ");
         sbSqlQuery.append(" AND TH.REF_NO = ? ");
         sbSqlQuery.append(" AND TH.ASSIGNEE = UD.USER_ID ");
         sbSqlQuery.append(" AND TH.END_DATE = ? ");
         try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
         {
            objPreparedStatement.setString(1, strTicketId);
            objPreparedStatement.setInt(2, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(3, strMaxCommentsDate);
            arlCurrentStatusFromTicketHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCurrentStatusFromTicketHistory;
   }

   public ArrayList<String> getInternalTickets(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlInternalTickets;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), DATE_FORMAT(ITD.START_DATE, '%d/%b/%Y %h:%i %p'), CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), ITD.REF_NO, DATE_FORMAT(ITD.ETA, '%d/%b/%Y') ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1, USER_DETAILS UD2 ");
      sbSqlQuery.append(" WHERE ITD.TICKET_STATUS  = 'Open' ");
      sbSqlQuery.append(" AND ITD.CREATED_BY = UD1.USER_ID ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = UD2.USER_ID ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), ITD.START_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlInternalTickets = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlInternalTickets;
   }

   public ArrayList<String> getClosedInternalTickets(String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlClosedInternalTickets;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), DATE_FORMAT(ITD.START_DATE, '%d/%b/%Y %h:%i %p'), DATE_FORMAT(ITD.END_DATE, '%d/%b/%Y %h:%i %p'), CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), ITD.REF_NO ");
      sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1, USER_DETAILS UD2 ");
      sbSqlQuery.append(" WHERE ITD.CREATED_BY = UD1.USER_ID ");
      sbSqlQuery.append(" AND ITD.ASSIGNEE = UD2.USER_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(ITD.END_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND TICKET_STATUS = 'Completed' ");
      sbSqlQuery.append(" ORDER BY CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), ITD.START_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         arlClosedInternalTickets = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlClosedInternalTickets;
   }

   public ArrayList<String> getOpenTicketsCount(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlOpenTicketsCount;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT ");
      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS BUGSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS CRITICALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS MAJORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS NORMALTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS MINORTASKSCOUNT, ");

      sbSqlQuery.append(" (SELECT COUNT(TICKET_ID) ");
      sbSqlQuery.append(" FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_CATEGORY = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS != ? ");
      sbSqlQuery.append(" AND TICKET_PRIORITY = ? ");
      sbSqlQuery.append(" AND ASSIGNEE = ? ) AS TRIVIALTASKSCOUNT ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strAssignee);

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Critical");
         objPreparedStatement.setString(++intParam, strAssignee);

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Major");
         objPreparedStatement.setString(++intParam, strAssignee);

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Normal");
         objPreparedStatement.setString(++intParam, strAssignee);

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Minor");
         objPreparedStatement.setString(++intParam, strAssignee);

         objPreparedStatement.setString(++intParam, AppConstants.TASK);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, "Trivial");
         objPreparedStatement.setString(++intParam, strAssignee);

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

   public ArrayList<String> getReopenedTicketList(String strAssignee, String strFromDate, String strToDate, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlReopenedTicketList;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND STR_TO_DATE(DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), '%d/%m/%Y') BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND ");
      sbSqlQuery.append(" STR_TO_DATE(?, '%d/%m/%Y') ");
      sbSqlQuery.append(" AND REF_NO IN ");
      sbSqlQuery.append(" (SELECT REOPEN_REF_NO FROM TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE TICKET_INFLOW_TYPE LIKE '%Re-Opened%' ");
      sbSqlQuery.append(" AND REOPEN_REASON != ? ) ");

      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
      }
      sbSqlQuery.append(" ORDER BY CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), TD.ACTUAL_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strFromDate);
         objPreparedStatement.setString(++intParam, strToDate);
         objPreparedStatement.setString(++intParam, AppConstants.REOPEN_REASON_HELPING_OTHER_DOMAIN);
         if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
         {
            objPreparedStatement.setString(++intParam, strAssignee);
         }

         arlReopenedTicketList = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlReopenedTicketList;
   }

   public ArrayList<String> getCodeFixReleases(String strReleaseVersion, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlCodeFixReleases;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
      sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, STR_TO_DATE(DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') > STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND RELEASE_VERSION = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS  = ? ");
      sbSqlQuery.append(" ORDER BY CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), TD.ACTUAL_END_DATE ASC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strReleaseVersion);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         arlCodeFixReleases = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlCodeFixReleases;
   }

}