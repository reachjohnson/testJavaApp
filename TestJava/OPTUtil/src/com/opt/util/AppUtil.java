package com.opt.util;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class AppUtil
{
   static Logger objLogger = Logger.getLogger(AppUtil.class.getName());

   public static String getCurrentDate(String strOutputFormat)
   {
      Calendar curdateins = Calendar.getInstance();
      Date CurrentDate = curdateins.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat(strOutputFormat);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentDateDDMMYYYYHHMMAMPM()
   {
      Calendar curdateins = Calendar.getInstance();
      Date CurrentDate = curdateins.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentDate()
   {
      Calendar curdateins = Calendar.getInstance();
      Date CurrentDate = curdateins.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATE_TIME_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentDate_MySql()
   {
      Calendar curdateins = Calendar.getInstance();
      Date CurrentDate = curdateins.getTime();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATETIME_FORMAT_MYSQL);
      return sdf.format(CurrentDate);
   }


   private static Date getCurrentDatePrivate()
   {
      Calendar curdateins = Calendar.getInstance();
      Date CurrentDate = curdateins.getTime();
      return CurrentDate;
   }

   public static String getCurrentDay()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DAY_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentMonth()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_MONTH_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentYear()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_YEAR_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentHour()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_HOUR_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String getCurrentMins()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_MINS_FORMAT);
      int intCurrentMins = Integer.parseInt(sdf.format(CurrentDate));

      intCurrentMins = intCurrentMins - (intCurrentMins % 5);
      String strCurrentMins = "";
      if (intCurrentMins > 55)
      {
         intCurrentMins = 55;
      }
      if (Integer.toString(intCurrentMins).length() == 1)
      {
         strCurrentMins = "0" + Integer.toString(intCurrentMins);
      }
      else
      {
         strCurrentMins = Integer.toString(intCurrentMins);
      }
      return strCurrentMins;
   }

   public static String getCurrentAmPm()
   {
      Date CurrentDate = getCurrentDatePrivate();
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_AMPM_FORMAT);
      return sdf.format(CurrentDate);
   }

   public static String convertDate(String strDate, String strInputFormat, String strOutputFormat) throws Exception
   {
      try
      {
         Calendar objCalendar = Calendar.getInstance();
         Date inputDate = new SimpleDateFormat(strInputFormat).parse(strDate);
         objCalendar.setTime(inputDate);
         Date outputDate = objCalendar.getTime();
         return new SimpleDateFormat(strOutputFormat).format(outputDate);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }

   }


   public static String checkNull(String strData)
   {
      if (null == strData || ((strData.trim()).equals("")) || ((strData.trim()).equals("null")))
      {
         strData = "";
      }
      return strData.trim();
   }

   public static String checkNullSingleQuote(String strData)
   {
      if (null == strData || ((strData.trim()).equals("")) || ((strData.trim()).equals("null")))
      {
         strData = "";
      }
      return replaceSingleQuote(strData.trim());
   }

   public static String replaceSingleQuote(String strInput)
   {
      String retvalue = strInput;

      try
      {

         if (strInput.indexOf("'") != -1)
         {
            StringBuffer hold = new StringBuffer();
            char charInput;
            for (int i = 0; i < strInput.length(); i++)
            {
               if ((charInput = strInput.charAt(i)) == '\'')
                  hold.append("\\'");
               else
                  hold.append(charInput);
            }
            retvalue = hold.toString();
         }

      }
      catch (Exception e)
      {
      }
      return retvalue;

   }

   public static String getEncriptedString(String str, int val) throws Exception
   {
      String encstr = "";
      try
      {
         String mainstr = new String("0123456789 \\`<>?/{}()!@#$%|~^&*_+-=[],.;:\"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@#$%|~^&*");
         String origStr = str;
         for (int i = 0; i < origStr.length(); i++)
         {
            String tempstr = origStr.substring(i, i + 1);
            int index = mainstr.indexOf(tempstr);
            int code = index ^ val;
            encstr += mainstr.substring(code, code + 1);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return encstr;
   }

   public static String getBusinessDate(String strDate, int intNoOfDays, ArrayList<String> arlHolidayCalendar) throws Exception
   {
      Date dateTemp;
      String strDateTemp;
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM);
      SimpleDateFormat NewFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
      Date nextBusinessDay = null;
      try
      {
         Date NewDate = sdf.parse(strDate);

         Calendar calendar = Calendar.getInstance();
         calendar.setTime(NewDate);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         if (dayOfWeek == Calendar.SATURDAY)
         {
            calendar.add(Calendar.DATE, -1);
         }
         else if (dayOfWeek == Calendar.SUNDAY)
         {
            calendar.add(Calendar.DATE, -2);
         }
         if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
         {
            nextBusinessDay = calendar.getTime();
            strDate = NewFormat.format(nextBusinessDay.getTime());
            strDate += " 07:00 PM";
            NewDate = sdf.parse(strDate);
            calendar.setTime(NewDate);
         }
         dateTemp = calendar.getTime();
         strDateTemp = NewFormat.format(dateTemp);
         if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty() && arlHolidayCalendar.contains(strDateTemp))
         {
            calendar.setTime(getNextWorkingDay(calendar, arlHolidayCalendar));
         }

         for (int intCount = 0; intCount < intNoOfDays; intCount++)
         {
            calendar.setTime(getNextWorkingDay(calendar, arlHolidayCalendar));
         }
         nextBusinessDay = calendar.getTime();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return sdf.format(nextBusinessDay);
   }

   public static Date getNextWorkingDay(Calendar calendar, ArrayList<String> arlHolidayCalendar) throws Exception
   {
      Date nextWorkingday;
      boolean blnContinue = false;
      try
      {
         calendar.add(Calendar.DATE, 1);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

         if (dayOfWeek == Calendar.SATURDAY)
         {
            blnContinue = true;
         }
         else if (dayOfWeek == Calendar.SUNDAY)
         {
            blnContinue = true;
         }
         else
         {
            Date dateTemp = calendar.getTime();
            SimpleDateFormat NewFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            String strDateTemp = NewFormat.format(dateTemp);
            if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty() && arlHolidayCalendar.contains(strDateTemp))
            {
               blnContinue = true;
            }
         }
         if (blnContinue)
         {
            getNextWorkingDay(calendar, arlHolidayCalendar);
         }
         nextWorkingday = calendar.getTime();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return nextWorkingday;
   }

   public static boolean isTodayWorkingDay(ArrayList<String> arlHolidayCalendar) throws Exception
   {
      boolean blnTodayWorkingDay = true;
      try
      {
         SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
         String strToday = getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
         Date NewDate = dateFormat.parse(strToday);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(NewDate);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         if (dayOfWeek == Calendar.SATURDAY)
         {
            blnTodayWorkingDay = false;
         }
         else if (dayOfWeek == Calendar.SUNDAY)
         {
            blnTodayWorkingDay = false;
         }
         else if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty() && arlHolidayCalendar.contains(strToday))
         {
            blnTodayWorkingDay = false;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return blnTodayWorkingDay;
   }

   public static Date getPreviousWorkingDay(Calendar calendar, ArrayList<String> arlHolidayCalendar) throws Exception
   {
      Date nextWorkingday;
      boolean blnContinue = false;
      try
      {
         calendar.add(Calendar.DATE, -1);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

         if (dayOfWeek == Calendar.SATURDAY)
         {
            blnContinue = true;
         }
         else if (dayOfWeek == Calendar.SUNDAY)
         {
            blnContinue = true;
         }
         else
         {
            Date dateTemp = calendar.getTime();
            SimpleDateFormat NewFormat = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
            String strDateTemp = NewFormat.format(dateTemp);
            if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty() && arlHolidayCalendar.contains(strDateTemp))
            {
               blnContinue = true;
            }
         }
         if (blnContinue)
         {
            getPreviousWorkingDay(calendar, arlHolidayCalendar);
         }
         nextWorkingday = calendar.getTime();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return nextWorkingday;
   }

   public static boolean isWorkingDay(String strDate, String strDateFormat, ArrayList<String> arlHolidayCalendar) throws Exception
   {
      boolean blnWorkingDay = true;
      try
      {
         SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
         Date NewDate = dateFormat.parse(strDate);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(NewDate);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         if (dayOfWeek == Calendar.SATURDAY)
         {
            blnWorkingDay = false;
         }
         else if (dayOfWeek == Calendar.SUNDAY)
         {
            blnWorkingDay = false;
         }
         else if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty() && arlHolidayCalendar.contains(strDate))
         {
            blnWorkingDay = false;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return blnWorkingDay;
   }

   public static boolean isWeekDay(String strDate, String strDateFormat) throws Exception
   {
      boolean blnWeekDay = true;
      try
      {
         SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
         Date NewDate = dateFormat.parse(strDate);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(NewDate);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

         if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
         {
            blnWeekDay = false;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return blnWeekDay;
   }

   public static ArrayList<Object> getWeekAndMonthDates(String strFromDate, String strToDate, String strDateFormat) throws Exception
   {
      ArrayList<Object> arlWeekAndMonthDates = new ArrayList<>();
      ArrayList<Date> arlWeekFromToDates = new ArrayList<>();
      ArrayList<Date> arlMonthFromToDates = new ArrayList<>();
      try
      {
         SimpleDateFormat objDateFormat = new SimpleDateFormat(strDateFormat);
         String strCurrentDateDDMMYYYY = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
         Calendar objCalendarCurrentDate = Calendar.getInstance();
         objCalendarCurrentDate.setTime(objDateFormat.parse(strCurrentDateDDMMYYYY));
         Date objCurrentDate = objCalendarCurrentDate.getTime();

         Calendar objCalendarFirstWeek = Calendar.getInstance();
         Date objFirstWeekDate = objDateFormat.parse(strFromDate);
         objCalendarFirstWeek.setTime(objFirstWeekDate);
         int intFromDayOfWeek = objCalendarFirstWeek.get(Calendar.DAY_OF_WEEK);
         if (intFromDayOfWeek == Calendar.SUNDAY)
         {
            objCalendarFirstWeek.add(Calendar.DATE, -6);
         }
         else
         {
            objCalendarFirstWeek.add(Calendar.DATE, 8 - intFromDayOfWeek - 6);
         }
         objFirstWeekDate = objCalendarFirstWeek.getTime();

         Calendar objCalendarLastWeek = Calendar.getInstance();
         Date objLastWeekDate = objDateFormat.parse(strToDate);
         objCalendarLastWeek.setTime(objLastWeekDate);
         int intToDayOfWeek = objCalendarLastWeek.get(Calendar.DAY_OF_WEEK);
         if (intToDayOfWeek != Calendar.SUNDAY)
         {
            objCalendarLastWeek.add(Calendar.DATE, 8 - intToDayOfWeek);
         }
         objLastWeekDate = objCalendarLastWeek.getTime();

         arlWeekAndMonthDates.add(objFirstWeekDate);
         arlWeekAndMonthDates.add(objLastWeekDate);

         boolean blnContinue = true;
         Calendar objCalendarWeekFromDate = Calendar.getInstance();
         objCalendarWeekFromDate.setTime(objCalendarFirstWeek.getTime());
         Date objWeekFromDate;
         Date objWeekToDate;

         do
         {
            objWeekFromDate = objCalendarWeekFromDate.getTime();
            objCalendarWeekFromDate.add(Calendar.DATE, 6);
            objWeekToDate = objCalendarWeekFromDate.getTime();

            if (objWeekToDate.compareTo(objLastWeekDate) <= 0)
            {
               if (objWeekToDate.compareTo(objCurrentDate) > 0)
               {
                  objWeekToDate = objCurrentDate;
                  blnContinue = false;
               }
               arlWeekFromToDates.add(objWeekFromDate);
               arlWeekFromToDates.add(objWeekToDate);
               objCalendarWeekFromDate.add(Calendar.DATE, 1);
            }
            else
            {
               blnContinue = false;
            }
         }
         while (blnContinue);
         arlWeekAndMonthDates.add(arlWeekFromToDates);

         Calendar objCalendarFirstMonth = Calendar.getInstance();
         Date objFirstMonthDate = objDateFormat.parse(strFromDate);
         objCalendarFirstMonth.setTime(objFirstMonthDate);
         objCalendarFirstMonth.set(Calendar.DAY_OF_MONTH, objCalendarFirstMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
         objFirstMonthDate = objCalendarFirstMonth.getTime();

         Calendar objCalendarLastMonth = Calendar.getInstance();
         Date objLastMonthDate = objDateFormat.parse(strToDate);
         objCalendarLastMonth.setTime(objLastMonthDate);
         objCalendarLastMonth.set(Calendar.DAY_OF_MONTH, objCalendarLastMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
         objLastMonthDate = objCalendarLastMonth.getTime();

         arlWeekAndMonthDates.add(objFirstMonthDate);
         arlWeekAndMonthDates.add(objLastMonthDate);

         Calendar objCalendarMonthFromDate = Calendar.getInstance();
         objCalendarMonthFromDate.setTime(objCalendarFirstMonth.getTime());

         Date objMonthFromDate;
         Date objMonthToDate;
         blnContinue = true;
         do
         {
            objMonthFromDate = objCalendarMonthFromDate.getTime();
            objCalendarMonthFromDate.set(Calendar.DAY_OF_MONTH, objCalendarMonthFromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            objMonthToDate = objCalendarMonthFromDate.getTime();

            if (objMonthToDate.compareTo(objLastMonthDate) <= 0)
            {
               if (objMonthToDate.compareTo(objCurrentDate) > 0)
               {
                  objMonthToDate = objCurrentDate;
                  blnContinue = false;
               }
               arlMonthFromToDates.add(objMonthFromDate);
               arlMonthFromToDates.add(objMonthToDate);
               objCalendarMonthFromDate.add(Calendar.DATE, 1);
            }
            else
            {
               blnContinue = false;
            }
         }
         while (blnContinue);
         arlWeekAndMonthDates.add(arlMonthFromToDates);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
      return arlWeekAndMonthDates;
   }

   public static String getMondayDate(String InputDate, String strDateFormat) throws Exception
   {
      try
      {
         SimpleDateFormat objDateFormat = new SimpleDateFormat(strDateFormat);
         Calendar objCalendar = Calendar.getInstance();
         Date objDate = objDateFormat.parse(InputDate);
         objCalendar.setTime(objDate);
         int intDayOfWeek = objCalendar.get(Calendar.DAY_OF_WEEK);
         if (intDayOfWeek == Calendar.SUNDAY)
         {
            objCalendar.add(Calendar.DATE, -6);
         }
         else
         {
            objCalendar.add(Calendar.DATE, 8 - intDayOfWeek - 6);
         }
         return objDateFormat.format(objCalendar.getTime());
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
   }

   public static String getMonthStartDate(String InputDate, String strDateFormat) throws Exception
   {
      try
      {
         SimpleDateFormat objDateFormat = new SimpleDateFormat(strDateFormat);
         Calendar objCalendar = Calendar.getInstance();
         Date objDate = objDateFormat.parse(InputDate);
         objCalendar.setTime(objDate);
         objCalendar.set(Calendar.DAY_OF_MONTH, objCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
         return objDateFormat.format(objCalendar.getTime());
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
   }

}
