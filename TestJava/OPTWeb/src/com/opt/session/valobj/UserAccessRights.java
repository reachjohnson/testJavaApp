package com.opt.session.valobj;

import java.io.Serializable;

public class UserAccessRights implements Serializable
{
   private boolean writeAccess = false;
   private boolean createTask = false;
   private boolean createBug = false;
   private boolean createInternalTicket = false;
   private boolean modifyCurrentTickets = false;
   private boolean reopenClosedTickets = false;
   private boolean updateLeavePlans = false;
   private boolean holidayCalendar = false;
   private boolean addUser = false;
   private boolean modifyUser = false;
   private boolean accessRights = false;
   private boolean ticketsStatus = false;
   private boolean currentStatus = false;
   private boolean currentTickets = false;
   private boolean closedTickets = false;
   private boolean SLAMissedTickets = false;
   private boolean resourceAllocation = false;
   private boolean SLAReports = false;
   private boolean SLAMetrics = false;
   private boolean currentInternalTickets = false;
   private boolean closedInternalTickets = false;
   private boolean viewLeavePlans = false;
   private boolean listOfHolidays = false;
   private boolean teamContacts = false;
   private boolean domainPOC = false;
   private boolean createRQABug = false;
   private boolean reopenRQABug = false;
   private boolean modifyRQATickets = false;
   private boolean RQADailyReport = false;
   private boolean RQACurrentStatus = false;
   private boolean RQAMetrics = false;
   private boolean RQASettings = false;
   private boolean ResourceMatrix = false;
   private boolean ITAdminChanges = false;
   private boolean CommonSettings = false;
   private boolean UpdateClosedTicket = false;
   private boolean TeamFeedback = false;
   private boolean ReopenedTickets = false;
   private boolean CodeFixReleases = false;

   public UserAccessRights(boolean writeAccess, boolean createTask, boolean createBug, boolean createInternalTicket, boolean modifyCurrentTickets, boolean reopenClosedTickets,
                           boolean updateLeavePlans, boolean holidayCalendar, boolean addUser, boolean modifyUser, boolean accessRights, boolean ticketsStatus,
                           boolean currentStatus, boolean currentTickets, boolean closedTickets, boolean SLAMissedTickets, boolean resourceAllocation,
                           boolean SLAReports, boolean SLAMetrics, boolean currentInternalTickets, boolean closedInternalTickets, boolean viewLeavePlans,
                           boolean listOfHolidays, boolean teamContacts, boolean domainPOC, boolean createRQABug,
                           boolean reopenRQABug, boolean modifyRQATickets, boolean RQADailyReport,
                           boolean RQACurrentStatus, boolean RQAMetrics, boolean RQASettings, boolean ResourceMatrix, boolean ITAdminChanges, boolean CommonSettings,
                           boolean UpdateClosedTicket, boolean TeamFeedback, boolean ReopenedTickets, boolean CodeFixReleases)
   {
      this.writeAccess = writeAccess;
      this.createTask = createTask;
      this.createBug = createBug;
      this.createInternalTicket = createInternalTicket;
      this.modifyCurrentTickets = modifyCurrentTickets;
      this.reopenClosedTickets = reopenClosedTickets;
      this.updateLeavePlans = updateLeavePlans;
      this.holidayCalendar = holidayCalendar;
      this.addUser = addUser;
      this.modifyUser = modifyUser;
      this.accessRights = accessRights;
      this.ticketsStatus = ticketsStatus;
      this.currentStatus = currentStatus;
      this.currentTickets = currentTickets;
      this.closedTickets = closedTickets;
      this.SLAMissedTickets = SLAMissedTickets;
      this.resourceAllocation = resourceAllocation;
      this.SLAReports = SLAReports;
      this.SLAMetrics = SLAMetrics;
      this.currentInternalTickets = currentInternalTickets;
      this.closedInternalTickets = closedInternalTickets;
      this.viewLeavePlans = viewLeavePlans;
      this.listOfHolidays = listOfHolidays;
      this.teamContacts = teamContacts;
      this.domainPOC = domainPOC;
      this.createRQABug = createRQABug;
      this.reopenRQABug = reopenRQABug;
      this.modifyRQATickets = modifyRQATickets;
      this.RQADailyReport = RQADailyReport;
      this.RQACurrentStatus = RQACurrentStatus;
      this.RQAMetrics = RQAMetrics;
      this.RQASettings = RQASettings;
      this.ResourceMatrix = ResourceMatrix;
      this.ITAdminChanges = ITAdminChanges;
      this.CommonSettings = CommonSettings;
      this.UpdateClosedTicket = UpdateClosedTicket;
      this.TeamFeedback = TeamFeedback;
      this.ReopenedTickets = ReopenedTickets;
      this.CodeFixReleases = CodeFixReleases;
   }

   public boolean isWriteAccess()
   {
      return writeAccess;
   }

   public boolean isCreateTask()
   {
      return createTask;
   }

   public boolean isCreateBug()
   {
      return createBug;
   }

   public boolean isCreateInternalTicket()
   {
      return createInternalTicket;
   }

   public boolean isModifyCurrentTickets()
   {
      return modifyCurrentTickets;
   }

   public boolean isReopenClosedTickets()
   {
      return reopenClosedTickets;
   }

   public boolean isUpdateLeavePlans()
   {
      return updateLeavePlans;
   }

   public boolean isHolidayCalendar()
   {
      return holidayCalendar;
   }

   public boolean isAddUser()
   {
      return addUser;
   }

   public boolean isModifyUser()
   {
      return modifyUser;
   }

   public boolean isAccessRights()
   {
      return accessRights;
   }

   public boolean isTicketsStatus()
   {
      return ticketsStatus;
   }

   public boolean isCurrentStatus()
   {
      return currentStatus;
   }

   public boolean isCurrentTickets()
   {
      return currentTickets;
   }

   public boolean isClosedTickets()
   {
      return closedTickets;
   }

   public boolean isSLAMissedTickets()
   {
      return SLAMissedTickets;
   }

   public boolean isResourceAllocation()
   {
      return resourceAllocation;
   }

   public boolean isSLAReports()
   {
      return SLAReports;
   }

   public boolean isSLAMetrics()
   {
      return SLAMetrics;
   }

   public boolean isCurrentInternalTickets()
   {
      return currentInternalTickets;
   }

   public boolean isClosedInternalTickets()
   {
      return closedInternalTickets;
   }

   public boolean isViewLeavePlans()
   {
      return viewLeavePlans;
   }

   public boolean isListOfHolidays()
   {
      return listOfHolidays;
   }

   public boolean isTeamContacts()
   {
      return teamContacts;
   }

   public boolean isDomainPOC()
   {
      return domainPOC;
   }

   public boolean isCreateRQABug()
   {
      return createRQABug;
   }

   public boolean isReopenRQABug()
   {
      return reopenRQABug;
   }

   public boolean isModifyRQATickets()
   {
      return modifyRQATickets;
   }

   public boolean isRQADailyReport()
   {
      return RQADailyReport;
   }

   public boolean isRQACurrentStatus()
   {
      return RQACurrentStatus;
   }

   public boolean isRQAMetrics()
   {
      return RQAMetrics;
   }

   public boolean isRQASettings()
   {
      return RQASettings;
   }

   public boolean isResourceMatrix()
   {
      return ResourceMatrix;
   }

   public boolean isITAdminChanges()
   {
      return ITAdminChanges;
   }

   public boolean isCommonSettings()
   {
      return CommonSettings;
   }

   public boolean isUpdateClosedTicket()
   {
      return UpdateClosedTicket;
   }

   public boolean isTeamFeedback()
   {
      return TeamFeedback;
   }

   public boolean isReopenedTickets()
   {
      return ReopenedTickets;
   }

   public boolean isCodeFixReleases()
   {
      return CodeFixReleases;
   }
}
