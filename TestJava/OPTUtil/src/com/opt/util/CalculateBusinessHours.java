package com.opt.util;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalculateBusinessHours
{
   static Logger objLogger = Logger.getLogger(AppUtil.class.getName());
   private final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_TIME_FORMAT);
   private int intTotalHours;
   private int intTotalMinutes;

   public void execute(String strStartDate, String strEndDate, ArrayList<String> arlHolidays) throws Exception
   {
      try
      {
         Date beg = DEFAULT_DATE_FORMAT.parse(strStartDate);
         Date end = DEFAULT_DATE_FORMAT.parse(strEndDate);
         int intTempMinutes = interval(beg, end, new IncludeAllDateRule(), Calendar.MINUTE, arlHolidays);
         int intTotalDays = interval(beg, end, new IncludeAllDateRule(), Calendar.DATE, arlHolidays);
         int intWeekDays = interval(beg, end, new ExcludeWeekendDateRule(), Calendar.DATE, arlHolidays);
         int intDiffDays = intTotalDays - intWeekDays;
         int intTempHours = (int) intTempMinutes / 60;
         if (intDiffDays > 1)
         {
            intTotalHours = intTempHours - intDiffDays * 24;
         }
         else
         {
            intTotalHours = intTempHours;
         }
         intTotalMinutes = intTempMinutes % 60;
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new Exception(objException.getMessage(), objException);
      }
   }

   public int getTotalHours()
   {
      return intTotalHours;
   }

   public int getTotalMinutes()
   {
      return intTotalMinutes;
   }

   public int interval(Date beg, Date end, DateRule dateRule, int intervalType, ArrayList<String> arlHolidays)
   {
      boolean blnCheckCondition = true;
      Calendar executionCalendar = Calendar.getInstance();
      Date executionDate = executionCalendar.getTime();
      int dayOfWeek = executionCalendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
      {
         blnCheckCondition = false;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(beg);
      int count = 0;
      Date now = calendar.getTime();
      while (now.before(end))
      {
         calendar.add(intervalType, 1);
         now = calendar.getTime();
         if (!blnCheckCondition || dateRule.include(now, arlHolidays))
         {
            ++count;
         }
      }
      return count;
   }
}

interface DateRule
{
   boolean include(Date d, ArrayList<String> arlHolidays);
}

class ExcludeWeekendDateRule implements DateRule
{
   public boolean include(Date d, ArrayList<String> arlHolidays)
   {
      boolean include = true;

      Calendar calendar = Calendar.getInstance();

      calendar.setTime(d);
      Date dateTemp = calendar.getTime();
      String strDateTemp = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY).format(dateTemp);

      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

      if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY) || (arlHolidays != null && !arlHolidays.isEmpty() && arlHolidays.contains(strDateTemp)))
      {
         include = false;
      }
      return include;
   }
}

class IncludeAllDateRule implements DateRule
{
   public boolean include(Date d, ArrayList<String> arlHolidays)
   {
      return true;
   }
}
