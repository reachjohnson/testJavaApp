package com.opt.base.dao;

import com.opt.exception.DAOException;

import javax.naming.InitialContext;
import java.sql.*;
import javax.sql.DataSource;
import java.util.ArrayList;

import com.opt.util.AppUtil;
import org.apache.log4j.Logger;

public class BaseDAO
{
   static Logger objLogger = Logger.getLogger(BaseDAO.class.getName());

   public static Connection openConnection(String strJNDI) throws DAOException
   {
      Connection objDBConnection;
      try
      {
         InitialContext intialContext = new InitialContext();
         DataSource datasource = (DataSource) intialContext.lookup(strJNDI);
         objDBConnection = datasource.getConnection();
         objDBConnection.setAutoCommit(false);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      return objDBConnection;
   }

   public static ArrayList<String> execPreparedStmtQuery(PreparedStatement objPreparedStatement) throws DAOException
   {
      ArrayList<String> objList;
      try (ResultSet objResultSetDB = objPreparedStatement.executeQuery())
      {
         ResultSetMetaData rsmd = objResultSetDB.getMetaData();
         int iNumCols = rsmd.getColumnCount();
         objList = new ArrayList<>(iNumCols);
         while (objResultSetDB.next())
         {
            for (int iCount = 1; iCount <= iNumCols; iCount++)
            {
               objList.add(AppUtil.checkNull(objResultSetDB.getString(iCount)));
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
      return objList;
   }

   public static void execPreparedStmtUpdate(PreparedStatement objPreparedStatement) throws DAOException
   {
      try
      {
         objPreparedStatement.executeUpdate();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
   }

   public static void connCommit(Connection objDBConnection) throws DAOException
   {
      try
      {
         objDBConnection.commit();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
   }

   public static void connRollback(Connection objDBConnection) throws DAOException
   {
      try
      {
         objDBConnection.rollback();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new DAOException(objException.getMessage(), objException);
      }
   }
}
