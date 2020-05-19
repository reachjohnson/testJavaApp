package com.opt.login.action;

import com.opt.base.dao.BaseDAO;
import com.opt.exception.TaskException;
import com.opt.session.valobj.UserAccessRights;
import com.opt.util.AppUtil;
import com.opt.util.AppConstants;
import com.opt.login.dao.LoginDAO;
import com.opt.session.valobj.SessionInfo;
import com.opt.session.valobj.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import com.opt.util.WebUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

public class LoginAction extends ActionSupport
{
   Logger objLogger = Logger.getLogger(LoginAction.class.getName());
   WebUtil objWebUtil = new WebUtil();

   public String OPT() throws Exception
   {
      try
      {
         objLogger.info(AppConstants.PROCESS_STARTED);
         objLogger.info(AppConstants.PROCESS_FINISHED);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      return AppConstants.SUCCESS;
   }

   public String loadLogin() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         HttpServletRequest objRequest = ServletActionContext.getRequest();
         HttpSession objSession = objRequest.getSession(false);
         if (objSession != null)
         {
            objSession.invalidate();
         }

      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return AppConstants.SUCCESS;
   }

   public String validateLogin() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward;
      if (AppUtil.checkNull(objRequest.getParameter("ACCESS")).length() == 0)
         strForward = AppConstants.SESSION_EXPIRED;
      else
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            HttpSession objSession = objRequest.getSession(true);
            SessionInfo objSessionInfo = new SessionInfo();
            objSession.setAttribute(AppConstants.SESSION_DATA, objSessionInfo);

            String strLoginId = AppUtil.checkNull(objRequest.getParameter("LoginID"));
            String strPassword = AppUtil.replaceSingleQuote(AppUtil.getEncriptedString(AppUtil.checkNull(objRequest.getParameter("Password")), 5));
            ArrayList<String> arlLoginDetails = new ArrayList<>();
            arlLoginDetails.add(strLoginId);
            arlLoginDetails.add(strPassword);
            LoginDAO objLoginDAO = new LoginDAO();
            ArrayList<String> arlUserDetails = objLoginDAO.validateLogin(arlLoginDetails, objDBConnection);

            if ((arlUserDetails.get(0)).equalsIgnoreCase("InvalidUseridPassword"))
            {
               objRequest.setAttribute("InvalidLogin", "InvalidLogin");
               strForward = "validateLoginFailure";
            }
            else if ((arlUserDetails.get(1)).equalsIgnoreCase("UserInActive"))
            {
               objRequest.setAttribute("UserInActive", "UserInActive");
               strForward = "validateLoginFailure";
            }
            else
            {
               if (AppUtil.checkNull(objRequest.getParameter("Password")).equalsIgnoreCase(AppConstants.DEFAULT_PASSWORD))
               {
                  strForward = "changePassword";
                  objRequest.setAttribute("LOGINID", strLoginId);
               }
               else
               {
                  String strLastLoginTime = objLoginDAO.getLastLoginTime(arlUserDetails.get(2), objDBConnection);
                  UserInfo objUserInfo = new UserInfo(arlUserDetails.get(2), arlUserDetails.get(3),
                          arlUserDetails.get(4).equalsIgnoreCase("Y"), arlUserDetails.get(5).equalsIgnoreCase("Y"),
                          arlUserDetails.get(6).equalsIgnoreCase("Y"), arlUserDetails.get(7), false, strLastLoginTime);
                  objSessionInfo.setUserInfo(objUserInfo);

                  UserAccessRights objUserAccessRights = null;
                  ArrayList<String> arlUserAccessRights = objLoginDAO.getUserAccessRights(strLoginId, objDBConnection);
                  if (arlUserAccessRights != null && !arlUserAccessRights.isEmpty())
                  {
                     objUserAccessRights = new UserAccessRights(arlUserAccessRights.get(0).equalsIgnoreCase("Y"), arlUserAccessRights.get(1).equalsIgnoreCase("Y"), arlUserAccessRights.get(2).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(3).equalsIgnoreCase("Y"), arlUserAccessRights.get(4).equalsIgnoreCase("Y"), arlUserAccessRights.get(5).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(6).equalsIgnoreCase("Y"), arlUserAccessRights.get(7).equalsIgnoreCase("Y"), arlUserAccessRights.get(8).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(9).equalsIgnoreCase("Y"), arlUserAccessRights.get(10).equalsIgnoreCase("Y"), arlUserAccessRights.get(11).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(12).equalsIgnoreCase("Y"), arlUserAccessRights.get(13).equalsIgnoreCase("Y"), arlUserAccessRights.get(14).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(15).equalsIgnoreCase("Y"), arlUserAccessRights.get(16).equalsIgnoreCase("Y"), arlUserAccessRights.get(17).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(18).equalsIgnoreCase("Y"), arlUserAccessRights.get(19).equalsIgnoreCase("Y"), arlUserAccessRights.get(20).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(21).equalsIgnoreCase("Y"), arlUserAccessRights.get(22).equalsIgnoreCase("Y"), arlUserAccessRights.get(23).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(24).equalsIgnoreCase("Y"), arlUserAccessRights.get(25).equalsIgnoreCase("Y"), arlUserAccessRights.get(26).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(27).equalsIgnoreCase("Y"), arlUserAccessRights.get(28).equalsIgnoreCase("Y"), arlUserAccessRights.get(29).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(30).equalsIgnoreCase("Y"), arlUserAccessRights.get(31).equalsIgnoreCase("Y"), arlUserAccessRights.get(32).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(33).equalsIgnoreCase("Y"), arlUserAccessRights.get(34).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(35).equalsIgnoreCase("Y"), arlUserAccessRights.get(36).equalsIgnoreCase("Y"), arlUserAccessRights.get(37).equalsIgnoreCase("Y"),
                             arlUserAccessRights.get(38).equalsIgnoreCase("Y"));
                  }
                  objSessionInfo.setUserAccessRights(objUserAccessRights);

                  objSession.setMaxInactiveInterval(21600);
                  strForward = "validateLoginSuccess";
                  objRequest.setAttribute("FROMLOGIN", "FROMLOGIN");
               }
            }
         }
         catch (Exception objException)
         {
            objRequest.setAttribute("LOGINPAGE", "YES");
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }

   public String changePassword() throws Exception
   {
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try
         {
            objLogger.info(AppConstants.PROCESS_STARTED);
            objLogger.info(AppConstants.PROCESS_FINISHED);
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objRequest.setAttribute("LOGINPAGE", "YES");
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      return strForward;
   }

   public String saveChangePassword() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      HttpServletRequest objRequest = ServletActionContext.getRequest();
      String strForward = objWebUtil.checkSession(objRequest, true);
      if (strForward.equalsIgnoreCase(AppConstants.GOAHEAD))
      {
         try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
         {
            LoginDAO objLoginDAO = new LoginDAO();
            String strLoginId = AppUtil.checkNull(objRequest.getParameter("LoginID"));
            String strPassword = AppUtil.replaceSingleQuote(AppUtil.getEncriptedString(AppUtil.checkNull(objRequest.getParameter("OldPassword")), 5));
            ArrayList<String> arlLoginDetails = new ArrayList<>();
            arlLoginDetails.add(strLoginId);
            arlLoginDetails.add(strPassword);
            ArrayList arlUserDetails = objLoginDAO.validateLogin(arlLoginDetails, objDBConnection);
            if (((String) arlUserDetails.get(0)).equalsIgnoreCase("InvalidUseridPassword"))
            {
               objRequest.setAttribute("InvalidLogin", "InvalidLogin");
            }
            else if (((String) arlUserDetails.get(1)).equalsIgnoreCase("UserInActive"))
            {
               objRequest.setAttribute("UserInActive", "UserInActive");
            }
            else
            {
               String strNewPassword = AppUtil.replaceSingleQuote(AppUtil.getEncriptedString(AppUtil.checkNull(objRequest.getParameter("NewPassword")), 5));
               objLoginDAO.changePassword(strLoginId, strNewPassword, objDBConnection);
               objRequest.setAttribute("ChangePasswordSuccess", AppConstants.SUCCESS);
            }
            strForward = AppConstants.SUCCESS;
         }
         catch (Exception objException)
         {
            objRequest.setAttribute("LOGINPAGE", "YES");
            objLogger.error(objException, objException.fillInStackTrace());
            throw new TaskException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strForward;
   }
}