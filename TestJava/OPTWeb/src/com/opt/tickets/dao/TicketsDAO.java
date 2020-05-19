package com.opt.tickets.dao;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.opt.exception.DAOException;
import com.opt.base.dao.BaseDAO;
import com.opt.util.AppConstants;

public class TicketsDAO {
    Logger objLogger = Logger.getLogger(TicketsDAO.class.getName());

    public ArrayList<String> getOpenTicketsAssignees(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlAssignees;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
        sbSqlQuery.append(" WHERE TD.ASSIGNEE = UD.USER_ID ");
        sbSqlQuery.append(" AND TD.TICKET_STATUS IN (?, ?, ? ) ");
        sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
            objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
            arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlAssignees;
    }

    public ArrayList<String> getAssigneesForInternalTasks(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlAssignees;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        sbSqlQuery.append(" FROM USER_DETAILS UD WHERE UD.TEAM = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.QA);
            arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlAssignees;
    }

    public void saveTask(String strTaskId, String strTaskDescription, String strTaskPriority, String strTaskType,
                         String strTaskModule, String strAssignee, String sbReceivedDate, String strPlannedEndDate, String strTaskRaisedBy,
                         String strTaskCreatedDate, String strAssigneeName, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TICKET_DETAILS ");
        sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ");
        sbSqlQuery.append("ASSIGNEE, RECEIVED_DATE, PLANNED_END_DATE, TICKET_STATUS, TICKET_RAISED_BY, TEAM_WORKING, TICKET_INFLOW_TYPE, CREATED_DATE) ");
        sbSqlQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.TASK);
            objPreparedStatement.setString(++intParam, strTaskId);
            objPreparedStatement.setString(++intParam, strTaskDescription);
            objPreparedStatement.setString(++intParam, strTaskPriority);
            objPreparedStatement.setString(++intParam, strTaskType);
            objPreparedStatement.setString(++intParam, strTaskModule);
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(sbReceivedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strPlannedEndDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, strTaskRaisedBy);
            objPreparedStatement.setString(++intParam, AppConstants.TRIAGING_TEAM);
            objPreparedStatement.setString(++intParam, AppConstants.TICKET_INFLOW_NEW_TASK);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strTaskCreatedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("SELECT REF_NO ");
        sbSqlQuery.append(" FROM TICKET_DETAILS");
        sbSqlQuery.append(" WHERE TICKET_ID = ? ");
        sbSqlQuery.append(" AND TICKET_STATUS = ? ");
        sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTaskId);
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, AppConstants.TASK);
            ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arltemp != null && !arltemp.isEmpty()) {
                String strRefNo = arltemp.get(0);
                createTicketActivityHistory(strRefNo, strTaskId, strUserName, AppConstants.TICKET_NEW, AppConstants.TICKET_NEW_COMMENTS + strAssigneeName, objDBConnection);
            }
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void saveBug(String strBugId, String strBugDescription, String strBugPriority, String strBugType,
                        String strBugModule, String strAssignee, String sbReceivedDate, String strPlannedEndDate, String strBugRaisedBy,
                        String strBugCreatedDate, String strAssigneeName, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TICKET_DETAILS ");
        sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ");
        sbSqlQuery.append("ASSIGNEE, RECEIVED_DATE, PLANNED_END_DATE, TICKET_STATUS, TICKET_RAISED_BY, TEAM_WORKING, TICKET_INFLOW_TYPE, CREATED_DATE) ");
        sbSqlQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.BUG);
            objPreparedStatement.setString(++intParam, strBugId);
            objPreparedStatement.setString(++intParam, strBugDescription);
            objPreparedStatement.setString(++intParam, strBugPriority);
            objPreparedStatement.setString(++intParam, strBugType);
            objPreparedStatement.setString(++intParam, strBugModule);
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(sbReceivedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strPlannedEndDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, strBugRaisedBy);
            objPreparedStatement.setString(++intParam, AppConstants.FIXING_TEAM);
            objPreparedStatement.setString(++intParam, AppConstants.TICKET_INFLOW_EXISTING_BUG);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strBugCreatedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("SELECT REF_NO ");
        sbSqlQuery.append(" FROM TICKET_DETAILS");
        sbSqlQuery.append(" WHERE TICKET_ID = ? ");
        sbSqlQuery.append(" AND TICKET_STATUS = ? ");
        sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strBugId);
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, AppConstants.BUG);
            ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arltemp != null && !arltemp.isEmpty()) {
                String strRefNo = arltemp.get(0);
                createTicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_NEW, AppConstants.TICKET_NEW_COMMENTS + strAssigneeName, objDBConnection);
            }
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getTicketDetailsForUpdate(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlTicketDetails;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
        sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), ");
        sbSqlQuery.append("STR_TO_DATE(DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), TEAM_WORKING, DATE_FORMAT(TD.ETA, '%d/%m/%Y') ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
        sbSqlQuery.append(" WHERE TD.ASSIGNEE = ASS.USER_ID ");
        sbSqlQuery.append(" AND TD.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TD.REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            arlTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketDetails;
    }

    public void updateTask(String strRefNo, String strOldTaskId, String strTaskId, String strTaskDescription, String strTaskPriority, String strTaskType,
                           String strTaskModule, String strTaskRaisedBy, String strTaskETADate, String strReasonForUpdate, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_ID = ?, ");
        sbSqlQuery.append("TICKET_DESC = ?, ");
        sbSqlQuery.append("TICKET_PRIORITY = ?, ");
        sbSqlQuery.append("TICKET_TYPE = ?, ");
        sbSqlQuery.append("TICKET_MODULE = ?, ");
        sbSqlQuery.append("TICKET_RAISED_BY = ?, ");
        sbSqlQuery.append("ETA = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTaskId);
            objPreparedStatement.setString(++intParam, strTaskDescription);
            objPreparedStatement.setString(++intParam, strTaskPriority);
            objPreparedStatement.setString(++intParam, strTaskType);
            objPreparedStatement.setString(++intParam, strTaskModule);
            objPreparedStatement.setString(++intParam, strTaskRaisedBy);
            if (strTaskETADate != null && strTaskETADate.length() > 0) {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strTaskETADate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
            } else {
                objPreparedStatement.setString(++intParam, null);
            }
            objPreparedStatement.setString(++intParam, strOldTaskId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));

            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            if (strTaskETADate != null && strTaskETADate.length() > 0) {
                createTicketActivityHistory(strRefNo, strTaskId, strUserName, AppConstants.TICKET_ETA_UPDATE, AppConstants.TICKET_ETA_DATE + strTaskETADate, objDBConnection);
            }
            createTicketActivityHistory(strRefNo, strTaskId, strUserName, AppConstants.TICKET_UPDATE, strReasonForUpdate, objDBConnection);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void updateBug(String strRefNo, String strOldBugId, String strBugId, String strBugDescription, String strBugPriority, String strBugType,
                          String strBugModule, String strBugRaisedBy, String strBugETADate, String strReasonForUpdate, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_ID = ?, ");
        sbSqlQuery.append("TICKET_DESC = ?, ");
        sbSqlQuery.append("TICKET_PRIORITY = ?, ");
        sbSqlQuery.append("TICKET_TYPE = ?, ");
        sbSqlQuery.append("TICKET_MODULE = ?, ");
        sbSqlQuery.append("TICKET_RAISED_BY = ?, ");
        sbSqlQuery.append("ETA = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strBugId);
            objPreparedStatement.setString(++intParam, strBugDescription);
            objPreparedStatement.setString(++intParam, strBugPriority);
            objPreparedStatement.setString(++intParam, strBugType);
            objPreparedStatement.setString(++intParam, strBugModule);
            objPreparedStatement.setString(++intParam, strBugRaisedBy);
            if (strBugETADate != null && strBugETADate.length() > 0) {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strBugETADate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
            } else {
                objPreparedStatement.setString(++intParam, null);
            }
            objPreparedStatement.setString(++intParam, strOldBugId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));

            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            if (strBugETADate != null && strBugETADate.length() > 0) {
                createTicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_ETA_UPDATE, AppConstants.TICKET_ETA_DATE + strBugETADate, objDBConnection);
            }
            createTicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_UPDATE, strReasonForUpdate, objDBConnection);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public boolean checkTicketExists(String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        boolean blnTicketExists = false;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) ");
        sbSqlQuery.append(" FROM TICKET_DETAILS");
        sbSqlQuery.append(" WHERE TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arltemp != null && !arltemp.isEmpty()) {
                int intCount = Integer.parseInt(arltemp.get(0));
                blnTicketExists = (intCount > 0);
            }
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return blnTicketExists;
    }

    public void deleteTicket(String strRefNo, String strTicketId, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("DELETE FROM TICKET_ACTIVITY_HISTORY ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("DELETE FROM COMMENTS_HISTORY ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("DELETE FROM TICKET_HISTORY ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("DELETE FROM TICKET_DETAILS ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_DELETED, AppConstants.TICKET_DELETE_COMMENTS, objDBConnection);
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void reAssignTicket(String strRefNo, String strTicketId, String strAssignee, String strCommentsForReAssign, String strUserName, String strAssigneeName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("ASSIGNEE = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            StringBuffer sbComments = new StringBuffer(AppConstants.TICKET_REASSIGN_COMMENTS).append(strAssigneeName).append(AppConstants.HYPHEN).append(AppConstants.SPACE).append(strCommentsForReAssign);
            createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_REASSIGNED, sbComments.toString(), objDBConnection);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void startProgressTicket(String strRefNo, String strTicketId, String strAssignee, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TICKET_HISTORY( ");
        sbSqlQuery.append("REF_NO, TICKET_ID, ASSIGNEE, START_DATE) ");
        sbSqlQuery.append("VALUES(?, ?, ?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_STATUS = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        sbSqlQuery.append(" AND ASSIGNEE = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strAssignee);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_START, AppConstants.TICKET_START_COMMENTS, objDBConnection);
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void stopProgressTicket(String strRefNo, String strTicketId, String strCommentsForStopProgress, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE TICKET_HISTORY SET ");
        sbSqlQuery.append("END_DATE = ?, ");
        sbSqlQuery.append("COMMENTS = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        sbSqlQuery.append("AND END_DATE IS NULL");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, strCommentsForStopProgress);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_STATUS = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_STOP, AppConstants.TICKET_STOP_COMMENTS, objDBConnection);
        BaseDAO.connCommit(objDBConnection);

        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getTicketDetailsForView(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlTicketDetailsForView;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
        sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY,  DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y') AS RECEIVEDDATE, DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y') AS SLAENDDATE, ");
        sbSqlQuery.append(" STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), ");
        sbSqlQuery.append(" TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y') AS ACTUALENDDATE, TD.TICKET_STATUS, ");
        sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ");
        sbSqlQuery.append(" DATE_FORMAT(TD.CREATED_DATE, '%d/%b/%Y') AS CREATEDDATE, DATE_FORMAT(TD.ETA, '%d/%b/%Y') AS ETADATE,  ");
        sbSqlQuery.append(" CASE TD.TICKET_PRIORITY ");
        sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), CREATED_DATE) ");
        sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), CREATED_DATE) ");
        sbSqlQuery.append(" END AS AGEING, RELEASE_VERSION ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
        sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TD.REF_NO = ? ");
        sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            arlTicketDetailsForView = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketDetailsForView;
    }

    public void closeTask(String strRefno, String strTaskId, String strTaskResolution, String strMovedDomain,
                          String strTaskRootCause, String strComments, String strNewBugRecDate, String strNewBugPlannedEndDate, String strTaskSeverity, String strFixingAssignee,
                          String strFixingAssigneeName, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) FROM TICKET_HISTORY ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        sbSqlQuery.append(" AND END_DATE IS NULL");
        ArrayList<String> arlTemp;
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTaskId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        if (arlTemp != null && !arlTemp.isEmpty() && Integer.parseInt(arlTemp.get(0)) > 0) {
            intParam = 0;
            sbSqlQuery = new StringBuilder("UPDATE TICKET_HISTORY SET ");
            sbSqlQuery.append("END_DATE = ?, ");
            sbSqlQuery.append("COMMENTS = ? ");
            sbSqlQuery.append("WHERE TICKET_ID = ? ");
            sbSqlQuery.append("AND REF_NO = ? ");
            sbSqlQuery.append(" AND END_DATE IS NULL");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppConstants.TICKET_STOP_COMMENTS_FOR_CLOSING);
                objPreparedStatement.setString(++intParam, strTaskId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
                createTicketActivityHistory(strRefno, strTaskId, strUserName, AppConstants.TICKET_STOP, AppConstants.TICKET_STOP_COMMENTS, objDBConnection);
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_STATUS = ?, ");
        sbSqlQuery.append("TICKET_RESOLUTION = ?, ");
        sbSqlQuery.append("MOVED_DOMAIN = ?, ");
        sbSqlQuery.append("TICKET_ROOT_CAUSE = ?, ");
        sbSqlQuery.append("COMMENTS = ?, ");
        sbSqlQuery.append("ACTUAL_END_DATE = ?, ");
        sbSqlQuery.append("TICKET_SEVERITY = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, strTaskResolution);
            objPreparedStatement.setString(++intParam, strMovedDomain);
            objPreparedStatement.setString(++intParam, strTaskRootCause);
            objPreparedStatement.setString(++intParam, strComments);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, strTaskSeverity);
            objPreparedStatement.setString(++intParam, strTaskId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            createTicketActivityHistory(strRefno, strTaskId, strUserName, AppConstants.TICKET_CLOSED, AppConstants.TICKET_CLOSE_COMMENTS, objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        if (strTaskResolution.equalsIgnoreCase(AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED)) {
            intParam = 0;
            sbSqlQuery = new StringBuilder("INSERT INTO TICKET_DETAILS ");
            sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ");
            sbSqlQuery.append("RECEIVED_DATE, PLANNED_END_DATE, TICKET_STATUS, TICKET_RAISED_BY, TEAM_WORKING, TICKET_INFLOW_TYPE, TRIAGING_REF_NO, CREATED_DATE, ASSIGNEE) ");
            sbSqlQuery.append("SELECT ?,  TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ?, ?, ?, TICKET_RAISED_BY, ?, ?, ?, CREATED_DATE, ? ");
            sbSqlQuery.append(" FROM TICKET_DETAILS ");
            sbSqlQuery.append(" WHERE TICKET_ID = ? ");
            sbSqlQuery.append(" AND REF_NO = ? ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, AppConstants.BUG);
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugRecDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugPlannedEndDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
                objPreparedStatement.setString(++intParam, AppConstants.FIXING_TEAM);
                objPreparedStatement.setString(++intParam, AppConstants.TICKET_INFLOW_TASK_CONVERTED_BUG);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                objPreparedStatement.setString(++intParam, strFixingAssignee);
                objPreparedStatement.setString(++intParam, strTaskId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }

            intParam = 0;
            sbSqlQuery = new StringBuilder("SELECT REF_NO ");
            sbSqlQuery.append(" FROM TICKET_DETAILS");
            sbSqlQuery.append(" WHERE TICKET_ID = ? ");
            sbSqlQuery.append(" AND TICKET_STATUS = ? ");
            sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, strTaskId);
                objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
                objPreparedStatement.setString(++intParam, AppConstants.BUG);
                ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
                if (arltemp != null && !arltemp.isEmpty()) {
                    String strRefNo = arltemp.get(0);
                    createTicketActivityHistory(strRefNo, strTaskId, strUserName, AppConstants.TICKET_NEW, AppConstants.TICKET_NEW_COMMENTS + strFixingAssigneeName, objDBConnection);
                }
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }

        }
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void closeBug(String strRefno, String strBugId, String strBugResolution, String strMovedDomain,
                         String strBugRootCause, String strComments, String strNewBugRecDate, String strNewBugPlannedEndDate,
                         String strCodeReviewComments, String strBugSeverity, String strFixingAssignee,
                         String strFixingAssigneeName, String strReleaseVersion, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT COUNT(*) FROM TICKET_HISTORY ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        sbSqlQuery.append(" AND END_DATE IS NULL");
        ArrayList<String> arlTemp;
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strBugId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        if (arlTemp != null && !arlTemp.isEmpty() && Integer.parseInt(arlTemp.get(0)) > 0) {
            intParam = 0;
            sbSqlQuery = new StringBuilder("UPDATE TICKET_HISTORY SET ");
            sbSqlQuery.append("END_DATE = ?, ");
            sbSqlQuery.append("COMMENTS = ? ");
            sbSqlQuery.append("WHERE TICKET_ID = ? ");
            sbSqlQuery.append("AND REF_NO = ? ");
            sbSqlQuery.append(" AND END_DATE IS NULL");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppConstants.TICKET_STOP_COMMENTS_FOR_CLOSING);
                objPreparedStatement.setString(++intParam, strBugId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
                createTicketActivityHistory(strRefno, strBugId, strUserName, AppConstants.TICKET_STOP, AppConstants.TICKET_STOP_COMMENTS, objDBConnection);
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_STATUS = ?, ");
        sbSqlQuery.append("TICKET_RESOLUTION = ?, ");
        sbSqlQuery.append("MOVED_DOMAIN = ?, ");
        sbSqlQuery.append("TICKET_ROOT_CAUSE = ?, ");
        sbSqlQuery.append("COMMENTS = ?, ");
        sbSqlQuery.append("ACTUAL_END_DATE = ? , ");
        sbSqlQuery.append("CODE_REVIEW_COMMENTS = ?, ");
        sbSqlQuery.append("TICKET_SEVERITY = ?, ");
        sbSqlQuery.append("RELEASE_VERSION = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, strBugResolution);
            objPreparedStatement.setString(++intParam, strMovedDomain);
            objPreparedStatement.setString(++intParam, strBugRootCause);
            objPreparedStatement.setString(++intParam, strComments);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, strCodeReviewComments);
            objPreparedStatement.setString(++intParam, strBugSeverity);
            objPreparedStatement.setString(++intParam, strReleaseVersion);
            objPreparedStatement.setString(++intParam, strBugId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            createTicketActivityHistory(strRefno, strBugId, strUserName, AppConstants.TICKET_CLOSED, AppConstants.TICKET_CLOSE_COMMENTS, objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        if (strBugResolution.equalsIgnoreCase(AppConstants.BUG_FIX_EXPECTED)) {
            intParam = 0;
            sbSqlQuery = new StringBuilder("INSERT INTO TICKET_DETAILS ");
            sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ");
            sbSqlQuery.append("RECEIVED_DATE, PLANNED_END_DATE, TICKET_STATUS, TICKET_RAISED_BY, TEAM_WORKING, TICKET_INFLOW_TYPE, TRIAGING_REF_NO, CREATED_DATE, ASSIGNEE) ");
            sbSqlQuery.append("SELECT ?,  TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ?, ?, ?, TICKET_RAISED_BY, ?, ?, ?, CREATED_DATE, ? ");
            sbSqlQuery.append(" FROM TICKET_DETAILS ");
            sbSqlQuery.append("WHERE TICKET_ID = ? ");
            sbSqlQuery.append("AND REF_NO = ? ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, AppConstants.BUG);
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugRecDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugPlannedEndDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
                objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
                objPreparedStatement.setString(++intParam, AppConstants.FIXING_TEAM);
                objPreparedStatement.setString(++intParam, AppConstants.TICKET_INFLOW_EXISTING_BUG);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                objPreparedStatement.setString(++intParam, strFixingAssignee);
                objPreparedStatement.setString(++intParam, strBugId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
                BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
            intParam = 0;
            sbSqlQuery = new StringBuilder("SELECT REF_NO ");
            sbSqlQuery.append(" FROM TICKET_DETAILS");
            sbSqlQuery.append(" WHERE TICKET_ID = ? ");
            sbSqlQuery.append(" AND TICKET_STATUS = ? ");
            sbSqlQuery.append(" AND TICKET_CATEGORY = ? ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, strBugId);
                objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
                objPreparedStatement.setString(++intParam, AppConstants.BUG);
                ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
                if (arltemp != null && !arltemp.isEmpty()) {
                    String strRefNo = arltemp.get(0);
                    createTicketActivityHistory(strRefNo, strBugId, strUserName, AppConstants.TICKET_NEW, AppConstants.TICKET_NEW_COMMENTS + strFixingAssigneeName, objDBConnection);
                }
            } catch (Exception objException) {
                BaseDAO.connRollback(objDBConnection);
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
        }
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void reopenTicket(String strRefNo, String strTicketCategory, String strTicketId, String strReopenComments, String strAssignee,
                             String strNewBugRecDate, String strNewBugPlannedEndDate, String strReasonForReopen, String strTeamWorking,
                             String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TICKET_DETAILS ");
        sbSqlQuery.append("(TICKET_CATEGORY, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ");
        sbSqlQuery.append("ASSIGNEE, RECEIVED_DATE, PLANNED_END_DATE, TICKET_STATUS, TICKET_RAISED_BY, TEAM_WORKING, TICKET_INFLOW_TYPE, REOPEN_REASON, TRIAGING_REF_NO, CREATED_DATE, REOPEN_REF_NO) ");
        sbSqlQuery.append("SELECT ?, TICKET_ID, TICKET_DESC, TICKET_PRIORITY, TICKET_TYPE, TICKET_MODULE, ?, ?, ?, ?, TICKET_RAISED_BY, ?, CONCAT(?, ' ', ?), ?, TRIAGING_REF_NO, CREATED_DATE, ? ");
        sbSqlQuery.append(" FROM TICKET_DETAILS ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketCategory);
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugRecDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strNewBugPlannedEndDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, strTeamWorking);
            objPreparedStatement.setString(++intParam, AppConstants.TICKET_REOPEN);
            objPreparedStatement.setString(++intParam, strTicketCategory);
            objPreparedStatement.setString(++intParam, strReasonForReopen);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("SELECT REF_NO ");
        sbSqlQuery.append(" FROM TICKET_DETAILS");
        sbSqlQuery.append(" WHERE TICKET_ID = ? ");
        sbSqlQuery.append(" AND TICKET_STATUS = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            ArrayList<String> arltemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arltemp != null && !arltemp.isEmpty()) {
                String strNewRefNo = arltemp.get(0);
                createTicketActivityHistory(strNewRefNo, strTicketId, strUserName, AppConstants.TICKET_REOPEN, strReopenComments, objDBConnection);
            }
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getTicketHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException {
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
        sbSqlQuery.append(" ORDER BY TH.TICKET_HISTORY_REF_NO DESC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            arlTicketHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketHistory;
    }

    public String getTriagingRefNo(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        String strTriagingRefNo = "";
        ArrayList<String> arlTemp;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TRIAGING_REF_NO ");
        sbSqlQuery.append(" FROM TICKET_DETAILS ");
        sbSqlQuery.append(" WHERE TICKET_ID = ? ");
        sbSqlQuery.append(" AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arlTemp != null && !arlTemp.isEmpty()) {
                strTriagingRefNo = arlTemp.get(0);
            }
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return strTriagingRefNo;
    }

    public ArrayList<String> getTicketActivityHistory(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlTicketActivityHistory;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TAH.TICKET_ACTIVITY_TYPE, TAH.COMMENTS,  ");
        sbSqlQuery.append(" DATE_FORMAT(TAH.ACTIVITY_DATE, '%d/%b/%Y %h:%i:%s %p') AS ACTIVITY_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
        sbSqlQuery.append(" FROM TICKET_ACTIVITY_HISTORY TAH, USER_DETAILS ASS ");
        sbSqlQuery.append(" WHERE TAH.ACTIVITY_BY = ASS.USER_ID ");
        sbSqlQuery.append(" AND TAH.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TAH.REF_NO = ? ");
        sbSqlQuery.append(" ORDER BY TAH.TICKET_ACTIVITY_HISTORY_REF_NO DESC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            arlTicketActivityHistory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketActivityHistory;
    }

    private void createTicketActivityHistory(String strRefno, String strTicketId, String strUserName, String strCreationType,
                                             String strCreationComments, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO TICKET_ACTIVITY_HISTORY ");
        sbSqlQuery.append("(REF_NO, TICKET_ID, TICKET_ACTIVITY_TYPE, COMMENTS, ACTIVITY_BY, ACTIVITY_DATE) ");
        sbSqlQuery.append("VALUES(?, ?, ?, ?, ?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, strCreationType);
            objPreparedStatement.setString(++intParam, strCreationComments);
            objPreparedStatement.setString(++intParam, strUserName);
            objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getDomainNames(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        ArrayList<String> arlDomainNames;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT DOMAIN_NAME FROM DOMAIN_NAMES ORDER BY DOMAIN_NAME ASC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            arlDomainNames = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlDomainNames;
    }

    public ArrayList<String> getTicketDetailsForModifyByAdmin(String strAssignee, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlTicketDetails;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
        sbSqlQuery.append(" TD.TICKET_STATUS, DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), ");
        sbSqlQuery.append(" TD.ASSIGNEE, STR_TO_DATE(DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%m/%Y'), '%d/%m/%Y') < STR_TO_DATE(DATE_FORMAT(NOW(), '%d/%m/%Y'), '%d/%m/%Y'), ");
        sbSqlQuery.append(" CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME), TEAM_WORKING, DATE_FORMAT(TD.ETA, '%d/%b/%Y'), ");
        sbSqlQuery.append(" CASE TD.TICKET_PRIORITY ");
        sbSqlQuery.append(" WHEN 'Blocker' THEN 20 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Critical' THEN 20 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Major' THEN 40 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
        sbSqlQuery.append(" WHEN 'Normal' THEN 90 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
        sbSqlQuery.append(" ELSE 120 - DATEDIFF(NOW(), TD.CREATED_DATE) ");
        sbSqlQuery.append(" END AS AGEING ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS ASS ");
        sbSqlQuery.append("WHERE TD.TICKET_STATUS IN (?, ?, ?) ");
        sbSqlQuery.append(" AND TD.ASSIGNEE = ASS.USER_ID ");
        if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
            sbSqlQuery.append(" AND TD.ASSIGNEE = ? ");
        }
        sbSqlQuery.append(" ORDER BY TD.PLANNED_END_DATE ASC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.NOT_STARTED);
            objPreparedStatement.setString(++intParam, AppConstants.IN_PROGRESS);
            objPreparedStatement.setString(++intParam, AppConstants.ON_HOLD);
            if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
                objPreparedStatement.setString(++intParam, strAssignee);
            }
            arlTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketDetails;
    }

    public ArrayList<String> getTicketDetailsForReopen(String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlTicketDetailsForReopen = null;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
        sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
        sbSqlQuery.append(" AND TD.TICKET_ID NOT IN (SELECT TICKET_ID FROM TICKET_DETAILS WHERE TICKET_ID = ? AND TICKET_STATUS != ? ) ");
        sbSqlQuery.append(" GROUP BY TD.TICKET_ID ");
        ArrayList<String> arlRefNo;
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlRefNo = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        if (arlRefNo != null && !arlRefNo.isEmpty()) {
            intParam = 0;
            String strRefNo = arlRefNo.get(0);
            sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_CATEGORY, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_PRIORITY, TD.TICKET_TYPE,  ");
            sbSqlQuery.append(" TD.TICKET_MODULE, TD.TICKET_RAISED_BY, TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, ");
            sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), ");
            sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
            sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
            sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
            sbSqlQuery.append(" AND TD.REF_NO = ? ");
            sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
            sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, strTicketId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
                objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
                arlTicketDetailsForReopen = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            } catch (Exception objException) {
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlTicketDetailsForReopen;
    }

    public void addComments(String strRefno, String strTicketId, String strCommentsCategory, String strComments, String strCommentsDate,
                            String strUserId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO COMMENTS_HISTORY ");
        sbSqlQuery.append("(REF_NO, TICKET_ID, COMMENTS_CATEGORY, COMMENTS, COMMENTS_BY, COMMENTS_DATE, UPDATED_DATE) ");
        sbSqlQuery.append(" VALUES (?,?,?,?,?,?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, strCommentsCategory);
            objPreparedStatement.setString(++intParam, strComments);
            objPreparedStatement.setString(++intParam, strUserId);
            objPreparedStatement.setString(++intParam, strCommentsDate);
            objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void addITComments(String strRefno, String strTicketId, String strComments,
                              String strUserId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO IT_COMMENTS_HISTORY ");
        sbSqlQuery.append("(REF_NO, TICKET_ID, COMMENTS, COMMENTS_BY, COMMENTS_DATE) ");
        sbSqlQuery.append(" VALUES (?,?,?,?,?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, strComments);
            objPreparedStatement.setString(++intParam, strUserId);
            objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getCommentsHistory(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlCommentsCategory;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT CH.COMMENTS_CATEGORY, CH.COMMENTS,  ");
        sbSqlQuery.append(" DATE_FORMAT(CH.UPDATED_DATE, '%d/%b/%Y %h:%i:%s %p') AS COMMENTS_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
        sbSqlQuery.append(" FROM COMMENTS_HISTORY CH, USER_DETAILS ASS ");
        sbSqlQuery.append(" WHERE CH.COMMENTS_BY = ASS.USER_ID ");
        sbSqlQuery.append(" AND CH.TICKET_ID = ? ");
        sbSqlQuery.append(" AND CH.REF_NO = ? ");
        sbSqlQuery.append(" ORDER BY CH.COMMENTS_HISTORY_REF_NO DESC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            arlCommentsCategory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlCommentsCategory;
    }

    public ArrayList<String> getITCommentsHistory(String strRefNo, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlCommentsCategory;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT CH.COMMENTS,  ");
        sbSqlQuery.append(" DATE_FORMAT(CH.COMMENTS_DATE, '%d/%b/%Y %h:%i:%s %p') AS COMMENTS_DATE, CONCAT(ASS.FIRST_NAME, ' ', ASS.LAST_NAME) ");
        sbSqlQuery.append(" FROM IT_COMMENTS_HISTORY CH, USER_DETAILS ASS ");
        sbSqlQuery.append(" WHERE CH.COMMENTS_BY = ASS.USER_ID ");
        sbSqlQuery.append(" AND CH.REF_NO = ? ");
        sbSqlQuery.append(" ORDER BY CH.IT_COMMENTS_HISTORY_REF_NO DESC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            arlCommentsCategory = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlCommentsCategory;
    }


    public ArrayList<String> getOpenTicketIdsAndDesc(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlOpenTicketIdsAndDesc;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT REF_NO, TICKET_ID, TICKET_DESC ");
        sbSqlQuery.append(" FROM TICKET_DETAILS ");
        sbSqlQuery.append(" WHERE TICKET_STATUS != ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlOpenTicketIdsAndDesc = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlOpenTicketIdsAndDesc;
    }

    public String getTicketDesc(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        String strTicketDescription = "";
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_DESC ");
        sbSqlQuery.append(" FROM TICKET_DETAILS ");
        sbSqlQuery.append(" WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            ArrayList<String> arlTemp = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            if (arlTemp != null && !arlTemp.isEmpty()) {
                strTicketDescription = arlTemp.get(0);
            }
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return strTicketDescription;
    }

    public void saveInternalTask(String strOpenTicketId, String strOpenTicketDesc, String strAssignee, String strActionRequired,
                                 String strITETADate, String strCreatedBy,
                                 Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("INSERT INTO INTERNAL_TICKET_DETAILS ");
        sbSqlQuery.append("(TICKET_ID, TICKET_DESC, ASSIGNEE, ACTION_REQUIRED, CREATED_BY, START_DATE, TICKET_STATUS, ETA) ");
        sbSqlQuery.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strOpenTicketId);
            objPreparedStatement.setString(++intParam, strOpenTicketDesc);
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, strActionRequired);
            objPreparedStatement.setString(++intParam, strCreatedBy);
            objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
            objPreparedStatement.setString(++intParam, "Open");
            if (strITETADate != null && strITETADate.length() > 0) {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strITETADate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
            } else {
                objPreparedStatement.setString(++intParam, null);
            }

            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void updateInternalTicket(String strRefNo, String strTicketId, String strAssignee, String strActionRequired, String strETA,
                                     String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE INTERNAL_TICKET_DETAILS SET ");
        sbSqlQuery.append("ASSIGNEE = ?, ");
        sbSqlQuery.append("ACTION_REQUIRED = ?, ");
        sbSqlQuery.append("ETA = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strAssignee);
            objPreparedStatement.setString(++intParam, strActionRequired);
            if (strETA.length() == 0) {
                objPreparedStatement.setString(++intParam, null);
            } else {
                objPreparedStatement.setString(++intParam, AppUtil.convertDate(strETA, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
            }

            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public void deleteInternalTicket(String strRefNo, String strTicketId, String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("DELETE FROM IT_COMMENTS_HISTORY ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("DELETE FROM INTERNAL_TICKET_DETAILS ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        sbSqlQuery.append(" AND TICKET_ID = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.TICKET_DELETED, AppConstants.TICKET_DELETE_COMMENTS, objDBConnection);
        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }


    public ArrayList<String> getOpenITAssignees(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlAssignees;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT DISTINCT UD.USER_ID, CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD ");
        sbSqlQuery.append(" WHERE ITD.ASSIGNEE = UD.USER_ID ");
        sbSqlQuery.append(" AND ITD.TICKET_STATUS != ? ");
        sbSqlQuery.append(" ORDER BY CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlAssignees = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlAssignees;
    }

    public ArrayList<String> getITDetailsForAdmin(String strAssignee, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlInternalTaskForUpdate;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.REF_NO, ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, DATE_FORMAT(ITD.ETA, '%d/%b/%Y'), CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), ITD.ASSIGNEE ");
        sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1 ");
        sbSqlQuery.append(" WHERE ITD.TICKET_STATUS  != ? ");
        sbSqlQuery.append(" AND ITD.ASSIGNEE = UD1.USER_ID ");
        if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
            sbSqlQuery.append(" AND ITD.ASSIGNEE = ? ");
        }
        sbSqlQuery.append(" ORDER BY ITD.ASSIGNEE ASC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
                objPreparedStatement.setString(++intParam, strAssignee);
            }
            arlInternalTaskForUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlInternalTaskForUpdate;
    }

    public ArrayList<String> getInternalTaskForUpdate(String strRefno, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlInternalTaskForUpdate;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT ITD.REF_NO, ITD.TICKET_ID, ITD.TICKET_DESC, ITD.ACTION_REQUIRED, DATE_FORMAT(ITD.START_DATE, '%d/%b/%Y %h:%i %p'), ITD.CREATED_BY, CONCAT(UD1.FIRST_NAME, ' ', UD1.LAST_NAME), ITD.ASSIGNEE, CONCAT(UD2.FIRST_NAME, ' ', UD2.LAST_NAME), DATE_FORMAT(ITD.ETA, '%d/%m/%Y'), ");
        sbSqlQuery.append("DATE_FORMAT(ITD.END_DATE, '%d/%b/%Y %h:%i %p'), ITD.TICKET_STATUS ");
        sbSqlQuery.append(" FROM INTERNAL_TICKET_DETAILS ITD, USER_DETAILS UD1, USER_DETAILS UD2 ");
        sbSqlQuery.append(" WHERE ITD.REF_NO  = ? ");
        sbSqlQuery.append(" AND ITD.TICKET_ID  = ? ");
        sbSqlQuery.append(" AND ITD.CREATED_BY = UD1.USER_ID ");
        sbSqlQuery.append(" AND ITD.ASSIGNEE = UD2.USER_ID ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefno));
            objPreparedStatement.setString(++intParam, strTicketId);
            arlInternalTaskForUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlInternalTaskForUpdate;
    }

    public void closeInternalTicket(String strRefNo, String strTicketId, String strComments, String strUserId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE INTERNAL_TICKET_DETAILS SET ");
        sbSqlQuery.append(" TICKET_STATUS = ?, ");
        sbSqlQuery.append(" END_DATE = ? ");
        sbSqlQuery.append("WHERE REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(AppUtil.getCurrentDate(), AppConstants.JAVA_DATE_TIME_FORMAT, AppConstants.JAVA_DATETIME_FORMAT_MYSQL));
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }

        intParam = 0;
        sbSqlQuery = new StringBuilder("INSERT INTO IT_COMMENTS_HISTORY ");
        sbSqlQuery.append("(REF_NO, TICKET_ID, COMMENTS, COMMENTS_BY, COMMENTS_DATE) ");
        sbSqlQuery.append(" VALUES (?,?,?,?,?) ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, strComments);
            objPreparedStatement.setString(++intParam, strUserId);
            objPreparedStatement.setString(++intParam, AppUtil.getCurrentDate_MySql());
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }


        BaseDAO.connCommit(objDBConnection);
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getOpenTicketIdDescAssigneeStatus(String strRefNo, String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlOpenTicketIdDescAssigneeStatus;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_STATUS, ");
        sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME) ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
        sbSqlQuery.append(" WHERE TD.REF_NO = ? AND TD.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
        sbSqlQuery.append(" AND TD.TICKET_STATUS != ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlOpenTicketIdDescAssigneeStatus = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlOpenTicketIdDescAssigneeStatus;
    }

    public ArrayList<String> getSLAMissingTickets(String strAssignee, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlSLAMissingTicketDetails;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT TICKET_ID, ");
        sbSqlQuery.append(" DATE_FORMAT(PLANNED_END_DATE, '%d/%m/%Y') ");
        sbSqlQuery.append("FROM TICKET_DETAILS ");
        sbSqlQuery.append("WHERE TICKET_STATUS != ? ");
        if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
            sbSqlQuery.append(" AND ASSIGNEE = ? ");
        }
        sbSqlQuery.append(" AND ACTUAL_END_DATE IS NULL ");
        sbSqlQuery.append(" ORDER BY PLANNED_END_DATE ASC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            if (!strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES)) {
                objPreparedStatement.setString(++intParam, strAssignee);
            }
            arlSLAMissingTicketDetails = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlSLAMissingTicketDetails;
    }

    public ArrayList<String> getClosedTicketDetailsForUpdate(String strTicketId, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        ArrayList<String> arlClosedTicketDetailsForUpdate = null;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT MAX(TD.REF_NO) ");
        sbSqlQuery.append(" FROM TICKET_DETAILS TD ");
        sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
        sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
        sbSqlQuery.append(" AND TD.TICKET_ID NOT IN (SELECT TICKET_ID FROM TICKET_DETAILS WHERE TICKET_ID = ? AND TICKET_STATUS != ? ) ");
        sbSqlQuery.append(" GROUP BY TD.TICKET_ID ");
        ArrayList<String> arlRefNo;
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
            arlRefNo = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        if (arlRefNo != null && !arlRefNo.isEmpty()) {
            intParam = 0;
            String strRefNo = arlRefNo.get(0);
            sbSqlQuery = new StringBuilder("SELECT TD.REF_NO, TD.TICKET_ID, TD.TICKET_DESC, TD.TICKET_CATEGORY, TD.TICKET_PRIORITY, TD.TICKET_TYPE, ");
            sbSqlQuery.append(" TD.TICKET_MODULE, TD.ASSIGNEE, TD.TICKET_STATUS, TD.TICKET_RESOLUTION, TD.MOVED_DOMAIN, TD.TICKET_ROOT_CAUSE, TD.COMMENTS, ");
            sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.PLANNED_END_DATE, '%d/%b/%Y'), DATE_FORMAT(TD.ACTUAL_END_DATE, '%d/%b/%Y'), ");
            sbSqlQuery.append(" TD.TICKET_RAISED_BY, DATE_FORMAT(TD.CREATED_DATE, '%d/%m/%Y'), TD.CODE_REVIEW_COMMENTS, TD.TICKET_SEVERITY, TD.REOPEN_REASON, DATE_FORMAT(TD.ETA, '%d/%b/%Y'), ");
            sbSqlQuery.append(" CONCAT(UD.FIRST_NAME, ' ', UD.LAST_NAME), ");
            sbSqlQuery.append(" DATE_FORMAT(TD.RECEIVED_DATE, '%d/%m/%Y'), RELEASE_VERSION ");
            sbSqlQuery.append(" FROM TICKET_DETAILS TD, USER_DETAILS UD ");
            sbSqlQuery.append(" WHERE TD.TICKET_ID = ? ");
            sbSqlQuery.append(" AND TD.REF_NO = ? ");
            sbSqlQuery.append(" AND TD.TICKET_STATUS = ? ");
            sbSqlQuery.append(" AND TD.ASSIGNEE = UD.USER_ID ");
            try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
                objPreparedStatement.setString(++intParam, strTicketId);
                objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
                objPreparedStatement.setString(++intParam, AppConstants.COMPLETED);
                arlClosedTicketDetailsForUpdate = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
            } catch (Exception objException) {
                objLogger.error(objException, objException.fillInStackTrace());
                throw new DAOException(objException.getMessage(), objException);
            }
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlClosedTicketDetailsForUpdate;
    }

    public void UpdateClosedTicket(String strRefNo, String strTicketId, String strTicketDescription, String strTicketPriority, String strTicketType,
                                   String strTicketModule, String strTicketRaisedBy, String strTicketResolution, String strMovedDomain, String strTicketRootCause,
                                   String strClosingComments, String strTicketCreatedDate, String strReleaseVersion,
                                   String strUserName, Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        int intParam = 0;
        StringBuilder sbSqlQuery = new StringBuilder("UPDATE TICKET_DETAILS SET ");
        sbSqlQuery.append("TICKET_DESC = ?, ");
        sbSqlQuery.append("TICKET_PRIORITY = ?, ");
        sbSqlQuery.append("TICKET_TYPE = ?, ");
        sbSqlQuery.append("TICKET_MODULE = ?, ");
        sbSqlQuery.append("TICKET_RAISED_BY = ?, ");
        sbSqlQuery.append("TICKET_RESOLUTION = ?, ");
        sbSqlQuery.append("MOVED_DOMAIN = ?, ");
        sbSqlQuery.append("TICKET_ROOT_CAUSE = ?, ");
        sbSqlQuery.append("COMMENTS = ?, ");
        sbSqlQuery.append("CREATED_DATE = ?, ");
        sbSqlQuery.append("RELEASE_VERSION = ? ");
        sbSqlQuery.append("WHERE TICKET_ID = ? ");
        sbSqlQuery.append("AND REF_NO = ? ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            objPreparedStatement.setString(++intParam, strTicketDescription);
            objPreparedStatement.setString(++intParam, strTicketPriority);
            objPreparedStatement.setString(++intParam, strTicketType);
            objPreparedStatement.setString(++intParam, strTicketModule);
            objPreparedStatement.setString(++intParam, strTicketRaisedBy);
            objPreparedStatement.setString(++intParam, strTicketResolution);
            objPreparedStatement.setString(++intParam, strMovedDomain);
            objPreparedStatement.setString(++intParam, strTicketRootCause);
            objPreparedStatement.setString(++intParam, strClosingComments);
            objPreparedStatement.setString(++intParam, AppUtil.convertDate(strTicketCreatedDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_MYSQL));
            objPreparedStatement.setString(++intParam, strReleaseVersion);
            objPreparedStatement.setString(++intParam, strTicketId);
            objPreparedStatement.setInt(++intParam, Integer.parseInt(strRefNo));
            BaseDAO.execPreparedStmtUpdate(objPreparedStatement);
            createTicketActivityHistory(strRefNo, strTicketId, strUserName, AppConstants.CLOSED_TICKET_UPDATE, AppConstants.CLOSED_TICKET_UPDATE, objDBConnection);
            BaseDAO.connCommit(objDBConnection);
        } catch (Exception objException) {
            BaseDAO.connRollback(objDBConnection);
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
    }

    public ArrayList<String> getReleaseVersions(Connection objDBConnection) throws DAOException {
        objLogger.info(AppConstants.PROCESS_STARTED);
        ArrayList<String> arlReleaseVersions;
        StringBuilder sbSqlQuery = new StringBuilder("SELECT RELEASE_VERSION FROM RELEASE_VERSIONS WHERE ACTIVE_FLAG = 'Y' ORDER BY REF_NO ASC ");
        try (PreparedStatement objPreparedStatement = objDBConnection.prepareStatement(sbSqlQuery.toString())) {
            arlReleaseVersions = BaseDAO.execPreparedStmtQuery(objPreparedStatement);
        } catch (Exception objException) {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new DAOException(objException.getMessage(), objException);
        }
        objLogger.info(AppConstants.PROCESS_FINISHED);
        return arlReleaseVersions;
    }

}