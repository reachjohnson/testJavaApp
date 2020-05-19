package com.opt.common.action;

import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.session.valobj.SessionInfo;
import com.opt.util.AppConstants;
import com.opt.util.SendEmail;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ErrorProcessor extends ActionSupport
{
   Logger objLogger = Logger.getLogger(ErrorProcessor.class.getName());
   private Exception exception;

   @Override
   public String execute()
   {
      try
      {
         HttpServletRequest objRequest = ServletActionContext.getRequest();
         HttpSession objSession = objRequest.getSession(false);
         SessionInfo objSessionInfo = (SessionInfo) objSession.getAttribute(AppConstants.SESSION_DATA);
         StackTraceElement objStackTraceElement[] = getException().getStackTrace();
         StringBuilder sbErrorMessage = new StringBuilder(objStackTraceElement[0].getClassName()).append(".").append(objStackTraceElement[0].getMethodName()).append("(Line Number : ").append(objStackTraceElement[0].getLineNumber()).append(")").append("<br><br>");
         sbErrorMessage.append(getException().toString() + "<br><br><br><br>");
         String[] strToEmailIds = {AppConstants.SYSTEMADMINS_ERROR};
         String[] strCCMailIds = {objSessionInfo.getUserInfo().getUserId()};
         String strMailSubject = AppConstants.ERRORNOTIFYSUBJECT;
         String[] strMessageHeader = {"System Admin", "The following error/exception triggered for the below user"};
         String[] strMessageBody = {
                 "User Id", objSessionInfo.getUserInfo().getUserId(),
                 "User Name", objSessionInfo.getUserInfo().getUserName(),
                 "Error/Exception", sbErrorMessage.toString()
         };
         new EmailQSender().send(new EmailData(strToEmailIds, strCCMailIds, null, strMailSubject,
                 strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, strMessageBody, true));
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      return "error";
   }

   public void setException(Exception exceptionHolder)
   {
      this.exception = exceptionHolder;
   }

   public Exception getException()
   {
      return exception;
   }
}
