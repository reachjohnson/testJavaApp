package com.opt.util;

import com.opt.exception.TaskException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class WebUtil
{
   Logger objLogger = Logger.getLogger(WebUtil.class.getName());

   public String checkSession(HttpServletRequest objRequest, boolean blnCheckMethod)
   {
      String strForward;
      if (null == objRequest.getSession(false))
      {
         strForward = AppConstants.SESSION_EXPIRED;
      }
      else if (null == (objRequest.getSession(false)).getAttribute(AppConstants.SESSION_DATA))
      {
         strForward = AppConstants.SESSION_EXPIRED;
      }
      else if (AppUtil.checkNull(objRequest.getParameter("ACCESS")).length() == 0 || (blnCheckMethod && objRequest.getMethod().equalsIgnoreCase("GET")))
      {
         strForward = AppConstants.INVALID_ACCESS;
      }
      else
      {
         strForward = AppConstants.GOAHEAD;
      }
      return strForward;
   }

   public ArrayList<Object> populatedETAHistory(ArrayList<String> arlOpenTicketsETAHistory) throws Exception
   {
      ArrayList<Object> arlPopulatedETAHistory = new ArrayList<>();
      try
      {
         if (arlOpenTicketsETAHistory != null && !arlOpenTicketsETAHistory.isEmpty())
         {
            String strOldRefno = "";
            String strOldTicketId = "";
            String strNewRefNo = "";
            String strNewTicketId = "";
            String strETA = "";
            HashSet<Date> hsETADates = new HashSet<>();
            SimpleDateFormat objDateFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            for (int iCount = 0; iCount < arlOpenTicketsETAHistory.size(); iCount += 3)
            {
               strNewRefNo = arlOpenTicketsETAHistory.get(iCount);
               strNewTicketId = arlOpenTicketsETAHistory.get(iCount + 1);
               strETA = arlOpenTicketsETAHistory.get(iCount + 2).substring(11, 21);

               if (iCount == 0)
               {
                  strOldRefno = strNewRefNo;
                  strOldTicketId = strNewTicketId;
                  hsETADates.add(objDateFormat.parse(strETA));
               }
               else
               {
                  if (strNewRefNo.equalsIgnoreCase(strOldRefno) && strNewTicketId.equalsIgnoreCase(strOldTicketId))
                  {
                     hsETADates.add(objDateFormat.parse(strETA));
                  }
                  else
                  {
                     arlPopulatedETAHistory.add(strOldRefno);
                     arlPopulatedETAHistory.add(strOldTicketId);
                     List<Date> arlTemp = new ArrayList<>(hsETADates);
                     Collections.sort(arlTemp);
                     arlPopulatedETAHistory.add(arlTemp);

                     hsETADates = new HashSet<>();
                     hsETADates.add(objDateFormat.parse(strETA));
                  }
                  strOldRefno = strNewRefNo;
                  strOldTicketId = strNewTicketId;
               }
               if (iCount == arlOpenTicketsETAHistory.size() - 3)
               {
                  arlPopulatedETAHistory.add(strOldRefno);
                  arlPopulatedETAHistory.add(strOldTicketId);
                  List<Date> arlTemp = new ArrayList<>(hsETADates);
                  Collections.sort(arlTemp);
                  arlPopulatedETAHistory.add(arlTemp);
               }
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      return arlPopulatedETAHistory;
   }
}
