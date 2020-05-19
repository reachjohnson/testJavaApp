package com.opt.common.util;

public class AppSettings
{
   private static boolean OOOSLA_HIGHLIGHT;
   private static boolean DBBackup;
   private static boolean ETESReminder;
   private static boolean HighPriorityNotStarted;
   private static boolean OOSLANotStarted;
   private static boolean RQAETAReminder;
   private static boolean StatusUpdateReminder;
   private static boolean OpenTicketsPriority;
   private static boolean TicketsProgressCheck;
   private static boolean ETABugs;
   private static boolean ETACriticalTasks;
   private static boolean ETAMajorTasks;
   private static boolean ETATopAgeingTasks;
   private static boolean RescheduleCrossedETA;
   private static boolean ETAInteranlTickets;
   private static boolean ETARQABugs;
   private static int TopAgeingThreshold;
   private static int ETARescheduleMax;
   private static boolean DailyStatus;

   public AppSettings(boolean OOOSLA_HIGHLIGHT, boolean DBBackup, boolean ETESReminder, boolean HighPriorityNotStarted, boolean OOSLANotStarted,
                      boolean RQAETAReminder, boolean StatusUpdateReminder, boolean OpenTicketsPriority, boolean TicketsProgressCheck,
                      boolean ETABugs, boolean ETACriticalTasks, boolean ETAMajorTasks, boolean ETATopAgeingTasks, boolean RescheduleCrossedETA,
                      boolean ETAInteranlTickets, boolean ETARQABugs, int TopAgeingThreshold, int ETARescheduleMax, boolean DailyStatus)
   {
      this.OOOSLA_HIGHLIGHT = OOOSLA_HIGHLIGHT;
      this.DBBackup = DBBackup;
      this.ETESReminder = ETESReminder;
      this.HighPriorityNotStarted = HighPriorityNotStarted;
      this.OOSLANotStarted = OOSLANotStarted;
      this.RQAETAReminder = RQAETAReminder;
      this.StatusUpdateReminder = StatusUpdateReminder;
      this.OpenTicketsPriority = OpenTicketsPriority;
      this.TicketsProgressCheck = TicketsProgressCheck;
      this.ETABugs = ETABugs;
      this.ETACriticalTasks = ETACriticalTasks;
      this.ETAMajorTasks = ETAMajorTasks;
      this.ETATopAgeingTasks = ETATopAgeingTasks;
      this.RescheduleCrossedETA = RescheduleCrossedETA;
      this.ETAInteranlTickets = ETAInteranlTickets;
      this.ETARQABugs = ETARQABugs;
      this.TopAgeingThreshold = TopAgeingThreshold;
      this.ETARescheduleMax = ETARescheduleMax;
      this.DailyStatus = DailyStatus;
   }

   public static boolean isOooslaHighlight()
   {
      return OOOSLA_HIGHLIGHT;
   }

   public static void setOooslaHighlight(boolean oooslaHighlight)
   {
      OOOSLA_HIGHLIGHT = oooslaHighlight;
   }

   public static boolean isDBBackup()
   {
      return DBBackup;
   }

   public static void setDBBackup(boolean DBBackup)
   {
      AppSettings.DBBackup = DBBackup;
   }

   public static boolean isETESReminder()
   {
      return ETESReminder;
   }

   public static void setETESReminder(boolean ETESReminder)
   {
      AppSettings.ETESReminder = ETESReminder;
   }

   public static boolean isHighPriorityNotStarted()
   {
      return HighPriorityNotStarted;
   }

   public static void setHighPriorityNotStarted(boolean highPriorityNotStarted)
   {
      HighPriorityNotStarted = highPriorityNotStarted;
   }

   public static boolean isOOSLANotStarted()
   {
      return OOSLANotStarted;
   }

   public static void setOOSLANotStarted(boolean OOSLANotStarted)
   {
      AppSettings.OOSLANotStarted = OOSLANotStarted;
   }

   public static boolean isRQAETAReminder()
   {
      return RQAETAReminder;
   }

   public static void setRQAETAReminder(boolean RQAETAReminder)
   {
      AppSettings.RQAETAReminder = RQAETAReminder;
   }

   public static boolean isStatusUpdateReminder()
   {
      return StatusUpdateReminder;
   }

   public static void setStatusUpdateReminder(boolean statusUpdateReminder)
   {
      StatusUpdateReminder = statusUpdateReminder;
   }

   public static boolean isOpenTicketsPriority()
   {
      return OpenTicketsPriority;
   }

   public static void setOpenTicketsPriority(boolean openTicketsPriority)
   {
      OpenTicketsPriority = openTicketsPriority;
   }

   public static boolean isTicketsProgressCheck()
   {
      return TicketsProgressCheck;
   }

   public static void setTicketsProgressCheck(boolean ticketsProgressCheck)
   {
      TicketsProgressCheck = ticketsProgressCheck;
   }

   public static boolean isETABugs()
   {
      return ETABugs;
   }

   public static void setETABugs(boolean ETABugs)
   {
      AppSettings.ETABugs = ETABugs;
   }

   public static boolean isETACriticalTasks()
   {
      return ETACriticalTasks;
   }

   public static void setETACriticalTasks(boolean ETACriticalTasks)
   {
      AppSettings.ETACriticalTasks = ETACriticalTasks;
   }

   public static boolean isETAMajorTasks()
   {
      return ETAMajorTasks;
   }

   public static void setETAMajorTasks(boolean ETAMajorTasks)
   {
      AppSettings.ETAMajorTasks = ETAMajorTasks;
   }

   public static boolean isETATopAgeingTasks()
   {
      return ETATopAgeingTasks;
   }

   public static void setETATopAgeingTasks(boolean ETATopAgeingTasks)
   {
      AppSettings.ETATopAgeingTasks = ETATopAgeingTasks;
   }

   public static boolean isRescheduleCrossedETA()
   {
      return RescheduleCrossedETA;
   }

   public static void setRescheduleCrossedETA(boolean rescheduleCrossedETA)
   {
      RescheduleCrossedETA = rescheduleCrossedETA;
   }

   public static boolean isETAInteranlTickets()
   {
      return ETAInteranlTickets;
   }

   public static void setETAInteranlTickets(boolean ETAInteranlTickets)
   {
      AppSettings.ETAInteranlTickets = ETAInteranlTickets;
   }

   public static boolean isETARQABugs()
   {
      return ETARQABugs;
   }

   public static void setETARQABugs(boolean ETARQABugs)
   {
      AppSettings.ETARQABugs = ETARQABugs;
   }

   public static int getTopAgeingThreshold()
   {
      return TopAgeingThreshold;
   }

   public static void setTopAgeingThreshold(int topAgeingThreshold)
   {
      TopAgeingThreshold = topAgeingThreshold;
   }

   public static int getETARescheduleMax()
   {
      return ETARescheduleMax;
   }

   public static void setETARescheduleMax(int ETARescheduleMax)
   {
      AppSettings.ETARescheduleMax = ETARescheduleMax;
   }

   public static boolean isDailyStatus()
   {
      return DailyStatus;
   }

   public static void setDailyStatus(boolean dailyStatus)
   {
      DailyStatus = dailyStatus;
   }
}
