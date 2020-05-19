package com.opt.common.action;

import com.opt.base.dao.BaseDAO;
import com.opt.exception.TaskException;
import com.opt.home.dao.HomeDAO;
import com.opt.util.AppConstants;
import com.opt.session.valobj.SessionInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

public class SessionExpire extends ActionSupport
{
   Logger objLogger = Logger.getLogger(SessionExpire.class.getName());

   public String expireSession() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);

      try
      {
         //
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

   public String invalidAccess() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);

      try
      {
         //
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

}