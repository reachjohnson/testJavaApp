package com.opt.tickets.dao;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class RQATicketsDAO
{
   Logger objLogger = Logger.getLogger(RQATicketsDAO.class.getName());

   public void saveRQABug(String strRQAPhase, String strRQACycle, String strBugId, String strBugDescription, String strBugPriority,
                          String strBugModule, String strAssignee, String sbReceivedDate, String strBugRaisedBy, String strAssigneeName,
                          String strUserName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO RQA_TICKET_DETAILS ");
      sbSqlQuery.append("(RQA_PHASE, RQA_CYCLE, TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_MODULE, ");
      sbSqlQuery.append("ASSIGNEE, RECEIVED_DATE, TICKET_STATUS, TICKET_RAISED_BY, TICKET_INFLOW_TYPE) ");
      sbSqlQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strRQAPhase);
         objPreparedStatement.setString(++intParam, strRQACycle);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strBugId);
         objPreparedStatement.setString(++intParam, strBugDescription);
         objPreparedStatement.setString(++intParam, strBugPriority);
         objPreparedStatement.setString(++intParam, strBugModule);
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(sbReceivedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         objPreparedStatement.setString(++intParam, strBugRaisedBy);
         objPreparedStatement.setString(++intParam, AppConstants.TICKET_INFLOW_NEW_RQA_BUG);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      intParam = 0;
      sbSqlQuery = new StringBuilder("SELECT REF_NO ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      sbSqlQuery.append(" AND TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strBugId);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arltemp != null && !arltemp.isEmpty())
         {
            String strRefNo = arltemp.get(0);
            createRQATicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_NEW, AppConstants.TICKET_NEW_COMMENTS + strAssigneeName, objDBConnection);
         }
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

   public boolean checkRQATicketExists(String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      boolean blnTicketExists = false;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS");
      sbSqlQuery.append(" WHERE TICKET_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arltemp != null && !arltemp.isEmpty())
         {
            int intCount = Integer.parseInt(arltemp.get(0));
            blnTicketExists = (intCount > 0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return blnTicketExists;
   }


   private void createRQATicketActivityHistory(String strRefno, String strTicketId, String strUserName, String strCreationType,
                                               String strCreationComments, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO RQA_TICKET_ACTIVITY_HISTORY ");
      sbSqlQuery.append("(REF_NO, TICKET_ID, TICKET_ACTIVITY_TYPE, COMMENTS, ACTIVITY_BY, ACTIVITY_DATE) ");
      sbSqlQuery.append("VALUES(?, ?, ?, ?, ?, ?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, strCreationType);
         objPreparedStatement.setString(++intParam, strCreationComments);
         objPreparedStatement.setString(++intParam, strUserName);
         objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public ArrayList<String> getRQATicketDetailsForView(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQATicketDetailsForView;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.RQA_PHASE, TD.RQA_CYCLE, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY,  DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y') AS RECEIVEDDATE, ");
      sbSqlQuery.append(" TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, TD.TICKET_STATUS, DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') AS ACTUALENDDATE, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.REF_NO = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlRQATicketDetailsForView = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQATicketDetailsForView;
   }

   public ArrayList<String> getRQATicketActivityHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQATicketActivityHistory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TAH.TICKET_ACTIVITY_TYPE, TAH.COMMENTS,  ");
      sbSqlQuery.append(" DATE_FORMAT(TAH.ACTIVITY_DATE, '%d/%b/%Y %h:%i:%s %p') AS ACTIVITY_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_TICKET_ACTIVITY_HISTORY TAH, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TAH.ACTIVITY_BY = ASS.USER_ID ");
      sbSqlQuery.append(" AND TAH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TAH.REF_NO = ? ");
      sbSqlQuery.append(" ORDER BY TAH.TICKET_ACTIVITY_HISTORY_REF_NO DESC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         arlRQATicketActivityHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQATicketActivityHistory;
   }

   public ArrayList<String> getRQACommentsHistory(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQACommentsCategory;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT CH.COMMENTS,  ");
      sbSqlQuery.append(" DATE_FORMAT(CH.COMMENTS_DATE, '%d/%b/%Y %h:%i:%s %p') AS COMMENTS_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_COMMENTS_HISTORY CH, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE CH.COMMENTS_BY = ASS.USER_ID ");
      sbSqlQuery.append(" AND CH.TICKET_ID = ? ");
      sbSqlQuery.append(" AND CH.REF_NO = ? ");
      sbSqlQuery.append(" ORDER BY CH.COMMENTS_HISTORY_REF_NO DESC ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         arlRQACommentsCategory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQACommentsCategory;
   }

   public ArrayList<String> getRQATicketDetailsForUpdate(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQATicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.RQA_PHASE, TD.RQA_CYCLE, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
      sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y') ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS ASS ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
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

   public void updateRQABug(String strRefNo, String strBugId, String strBugDescription, String strBugPriority, String strBugModule,
                            String strBugRaisedBy, String strETADate, String strUserName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE RQA_TICKET_DETAILS SET ");
      sbSqlQuery.append("TICKET_DESC = ?, ");
      sbSqlQuery.append("TICKET_PRIORITY = ?, ");
      sbSqlQuery.append("TICKET_MODULE = ?, ");
      sbSqlQuery.append("TICKET_RAISED_BY = ?, ");
      sbSqlQuery.append("PLANNED_END_DATE = ? ");
      sbSqlQuery.append("WHERE TICKET_ID = ? ");
      sbSqlQuery.append("AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strBugDescription);
         objPreparedStatement.setString(++intParam, strBugPriority);
         objPreparedStatement.setString(++intParam, strBugModule);
         objPreparedStatement.setString(++intParam, strBugRaisedBy);
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(strETADate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, strBugId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         createRQATicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_UPDATE, AppConstants.TICKET_UPDATE_COMMENTS, objDBConnection);
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

   public ArrayList<String> getRQAOpenTicketIdDescAssigneeStatus(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlOpenTicketIdDescAssigneeStatus;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_STATUS, ");
      sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.REF_NO = ? AND TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         arlOpenTicketIdDescAssigneeStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlOpenTicketIdDescAssigneeStatus;
   }

   public void addRQAComments(String strRefno, String strTicketId, String strComments,
                              String strUserId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO RQA_COMMENTS_HISTORY ");
      sbSqlQuery.append("(REF_NO, TICKET_ID, COMMENTS, COMMENTS_BY, COMMENTS_DATE) ");
      sbSqlQuery.append(" VALUES (?,?,?,?,?) ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setString(++intParam, strComments);
         objPreparedStatement.setString(++intParam, strUserId);
         objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
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

   public void reAssignRQATicket(String strRefNo, String strTicketId, String strAssignee, String strCommentsForReAssign, String strUserName, String strAssigneeName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE RQA_TICKET_DETAILS SET ");
      sbSqlQuery.append("ASSIGNEE = ? ");
      sbSqlQuery.append("WHERE TICKET_ID = ? ");
      sbSqlQuery.append("AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);

         StringBuffer sbComments = new StringBuffer(AppConstants.TICKET_REASSIGN_COMMENTS).append(strAssigneeName).append(AppConstants.HYPHEN).append(AppConstants.SPACE).append(strCommentsForReAssign);
         createRQATicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_REASSIGNED, sbComments.toString(), objDBConnection);
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

   public String getRQATicketDesc(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      String strTicketDescription = "";
      StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_DESC ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append(" WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         if (arlTemp != null && !arlTemp.isEmpty())
         {
            strTicketDescription = arlTemp.get(0);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strTicketDescription;
   }

   public void closeRQABug(String strRefno, String strBugId, String strBugResolution, String strMovedDomain,
                           String strBugRootCause, String strComments, String strUserName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("UPDATE RQA_TICKET_DETAILS SET ");
      sbSqlQuery.append("TICKET_STATUS = ?, ");
      sbSqlQuery.append("TICKET_RESOLUTION = ?, ");
      sbSqlQuery.append("MOVED_DOMAIN = ?, ");
      sbSqlQuery.append("TICKET_ROOT_CAUSE = ?, ");
      sbSqlQuery.append("COMMENTS = ?, ");
      sbSqlQuery.append("ACTUAL_END_DATE = ? ");
      sbSqlQuery.append("WHERE TICKET_ID = ? ");
      sbSqlQuery.append("AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
         objPreparedStatement.setString(++intParam, strBugResolution);
         objPreparedStatement.setString(++intParam, strMovedDomain);
         objPreparedStatement.setString(++intParam, strBugRootCause);
         objPreparedStatement.setString(++intParam, strComments);
         objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
         objPreparedStatement.setString(++intParam, strBugId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
         createRQATicketActivityHistory(strRefno, strBugId, strUserName, AppConstants.TICKET_CLOSED, AppConstants.TICKET_CLOSE_COMMENTS, objDBConnection);
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

   public ArrayList<String> getRQATicketDetailsForAdmin(String strAssignee, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlRQATicketDetails;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT RTD.REF_NO, RTD.TICKET_ID, RTD.TICKET_DESC, RTD.TICKET_CATEGORY, RTD.TICKET_PRIORITY, ");
      sbSqlQuery.append("RTD.TICKET_STATUS, DATE_FORMAT(RTD.RECEIVED_DATE, '%d/%b/%Y'), RTD.ASSIGNEE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
      sbSqlQuery.append("FROM RQA_TICKET_DETAILS RTD, USER_DETAILS ASS  ");
      sbSqlQuery.append("WHERE TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND RTD.ASSIGNEE = ASS.USER_ID ");
      if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
      {
         sbSqlQuery.append(" AND RTD.ASSIGNEE = ? ");
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

   public ArrayList<String> getOpenRQATicketsAssignees(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlAssignees;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
      sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ?  ");
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

   public void deleteRQATicket(String strRefNo, String strTicketId, String strUserName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("DELETE FROM RQA_TICKET_ACTIVITY_HISTORY ");
      sbSqlQuery.append("WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      intParam = 0;
      sbSqlQuery = new StringBuilder("DELETE FROM RQA_COMMENTS_HISTORY ");
      sbSqlQuery.append("WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      intParam = 0;
      sbSqlQuery = new StringBuilder("DELETE FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append("WHERE REF_NO = ? ");
      sbSqlQuery.append(" AND TICKET_ID = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         objPreparedStatement.setString(++intParam, strTicketId);
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      createRQATicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_DELETED, AppConstants.TICKET_DELETE_COMMENTS, objDBConnection);
      BaseDAO.connCommit(objDBConnection);
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   public ArrayList<String> getRQATicketDetailsForReopen(String strTicketId, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      ArrayList<String> arlTicketDetailsForReopen = null;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD ");
      sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
      sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
      sbSqlQuery.append(" AND TD.TICKET_ID NOT IN (SELECT TICKET_ID FROM RQA_TICKET_DETAILS WHERE TICKET_ID = ? AND TICKET_STATUS != ? ) ");
      sbSqlQuery.append(" GROUP BY TD.TICKET_ID ");
      ArrayList<String> arlRefNo;
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
         sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, ");
         sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, ");
         sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), ");
         sbSqlQuery.append(" TD.RQA_PHASE, TD.RQA_CYCLE, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
         sbSqlQuery.append(" FROM RQA_TICKET_DETAILS TD, USER_DETAILS UD ");
         sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
         sbSqlQuery.append(" AND TD.REF_NO = ? ");
         sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
         sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
         try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
         {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlTicketDetailsForReopen = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketDetailsForReopen;
   }

   public void reopenRQATicket(String strRefNo, String strTicketId, String strReopenComments, String strAssignee,
                               String strNewBugRecDate, String strReasonForReopen, String strRQAPhase, String strRQACycle,
                               String strUserName, Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      int intParam = 0;
      StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO RQA_TICKET_DETAILS ");
      sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_MODULE, ");
      sbSqlQuery.append("ASSIGNEE, RECEIVED_DATE, TICKET_STATUS, TICKET_RAISED_BY, TICKET_INFLOW_TYPE, REOPEN_REASON, RQA_PHASE, RQA_CYCLE) ");
      sbSqlQuery.append("SELECT ?, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_MODULE, ?, ?, ?, TICKET_RAISED_BY, ?, ?, ?, ?  ");
      sbSqlQuery.append(" FROM RQA_TICKET_DETAILS ");
      sbSqlQuery.append("WHERE TICKET_ID = ? ");
      sbSqlQuery.append("AND REF_NO = ? ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         objPreparedStatement.setString(++intParam, AppConstants.BUG);
         objPreparedStatement.setString(++intParam, strAssignee);
         objPreparedStatement.setString(++intParam, strNewBugRecDate);
         objPreparedStatement.setString(++intParam, AppConstants.OPEN);
         objPreparedStatement.setString(++intParam, AppConstants.RQA_TICKET_REOPEN);
         objPreparedStatement.setString(++intParam, strReasonForReopen);
         objPreparedStatement.setString(++intParam, strRQAPhase);
         objPreparedStatement.setString(++intParam, strRQACycle);
         objPreparedStatement.setString(++intParam, strTicketId);
         objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
         BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
      }
      catch (Exception objException)
      {
         BaseDAO.connRollback(objDBConnection);
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }

      intParam = 0;
      sbSqlQuery = new StringBuilder("SELECT REF_NO ");
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
            String strNewRefNo = arltemp.get(0);
            createRQATicketActivityHistory(strNewRefNo, strTicketId, strUserName, AppConstants.RQA_TICKET_REOPEN, strReopenComments, objDBConnection);
         }
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

   public ArrayList<String> getRQAResolutionTypes(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQAResolutionTypes;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT RESOLUTION_TYPE FROM RQA_RESOLUTION_TYPES ORDER BY RESOLUTION_TYPE ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlRQAResolutionTypes = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQAResolutionTypes;
   }

   public ArrayList<String> getRQAResolutionSettings(Connection objDBConnection) throws DAOException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlRQAResolutionSettings;
      StringBuilder sbSqlQuery = new StringBuilder("SELECT RESOL_TRIAGED, RESOL_LEGACY_FIXED, RESOL_FEAT_CODE_IMP_FIXED, RESOL_NONFEAT_CODE_IMP_FIXED FROM RQA_SETTINGS ");
      try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString()))
      {
         arlRQAResolutionSettings = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlRQAResolutionSettings;
   }
}