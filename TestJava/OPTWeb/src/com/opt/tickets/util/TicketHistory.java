package com.opt.tickets.util;

import com.opt.common.dao.CommonDAO;
import com.opt.exception.TaskException;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.util.AppConstants;
import com.opt.util.CalculateBusinessHours;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class TicketHistory
{
   Logger objLogger = Logger.getLogger(TicketHistory.class.getName());
   double dblTotalDaysSpent = 0;

   public ArrayList<String> getTicketHistory(String strRefno, String strTicketId, Connection objDBConnection) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      ArrayList<String> arlTicketHistory = new ArrayList<>();
      try
      {
         ArrayList<String> arlTicketHistoryTemp = new TicketsDAO().getTicketHistory(strRefno, strTicketId, objDBConnection);

         if (arlTicketHistoryTemp != null && !arlTicketHistoryTemp.isEmpty())
         {
            CalculateBusinessHours objCalculateBusinessHours;
            String strStartDate;
            String strEndDate;
            String strTotalHours;
            String strTotalMins;
            int intTotalHours = 0;
            int intTotalMinutes = 0;

            for (int iCount = 0; iCount < arlTicketHistoryTemp.size(); iCount += 5)
            {
               strTotalHours = "";
               strTotalMins = "";
               strStartDate = arlTicketHistoryTemp.get(iCount);
               strEndDate = arlTicketHistoryTemp.get(iCount + 1);
               if (strStartDate.length() > 0 && strEndDate.length() > 0)
               {
                  objCalculateBusinessHours = new CalculateBusinessHours();
                  objCalculateBusinessHours.execute(strStartDate, strEndDate, new CommonDAO().getHolidayCalendarForCalc(objDBConnection));
                  intTotalHours += objCalculateBusinessHours.getTotalHours();
                  intTotalMinutes += objCalculateBusinessHours.getTotalMinutes();
                  strTotalHours = String.valueOf(objCalculateBusinessHours.getTotalHours());
                  strTotalMins = String.valueOf(objCalculateBusinessHours.getTotalMinutes());
               }
               arlTicketHistory.add(strStartDate);
               arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 4));
               arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 2));
               arlTicketHistory.add(arlTicketHistoryTemp.get(iCount + 3));
               arlTicketHistory.add(strTotalHours);
               arlTicketHistory.add(strTotalMins);
            }
            intTotalMinutes += intTotalHours * 60;
            dblTotalDaysSpent = Double.valueOf(intTotalMinutes) / Double.valueOf((24 * 60));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return arlTicketHistory;
   }

   public String getTotalDaysSpent()
   {
      DecimalFormat twoDForm = new DecimalFormat("#0.00");
      return twoDForm.format(dblTotalDaysSpent);
   }
}
