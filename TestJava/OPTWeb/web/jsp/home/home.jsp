<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    int intOpenInternalTickets = 0;
    int intOpenRQABugs = 0;
    String strClosedRQABugs = "NA";

    ArrayList<String> arlTicketDetails = (ArrayList) request.getAttribute("TicketDetails");
    String strSLAMissedTicketsAvailable = AppUtil.checkNull((String) request.getAttribute("SLAMissedTicketsAvailable"));
    String strSLAMissedTicketsCount = AppUtil.checkNull((String) request.getAttribute("SLAMissedTicketsCount"));
    String strNearingSLATicketsCount = AppUtil.checkNull((String) request.getAttribute("NearingSLATicketsCount"));
    ArrayList<String> arlNearingSLATickets = (ArrayList) request.getAttribute("NearingSLATickets");
    ArrayList<String> arlClosedTasksBugsCountsCurrentMonth = (ArrayList) request.getAttribute("ClosedTasksBugsCountsCurrentMonth");
    ArrayList<String> arlOpenTicketsCount = (ArrayList) request.getAttribute("OpenTicketsCount");
    ArrayList<String> arlClosedTasksBugsCountsCurrentWeek = (ArrayList) request.getAttribute("ClosedTasksBugsCountsCurrentWeek");
    ArrayList<String> arlInternalTicketDetails = (ArrayList) request.getAttribute("InternalTicketDetails");
    ArrayList<String> arlInternalTicketDetailsCreatedByYou = (ArrayList) request.getAttribute("InternalTicketDetailsCreatedByYou");
    ArrayList<String> arlRQATicketDetails = (ArrayList) request.getAttribute("RQATicketDetails");
    String strRQACurrentPhase = AppUtil.checkNull((String) request.getAttribute("RQACurrentPhase"));
    String strTeamSummary = AppUtil.checkNull((String) request.getAttribute("TeamSummary"));
    ArrayList<Object> arlOpenTicketsETAHistory = (ArrayList) request.getAttribute("OpenTicketsETAHistory");
    String strReopenTicketsCount = AppUtil.checkNull((String) request.getAttribute("ReopenTicketsCount"));
    String strReopenedCountTitle = "";

    String strAssignee = "";
    if (strTeamSummary.equalsIgnoreCase("YES"))
    {
        strAssignee = AppConstants.ALL_ASSIGNEES;
        strReopenedCountTitle = "Reopened Tickets closed by Team";
    }
    else
    {
        strAssignee = strUserId;
        strReopenedCountTitle = "Reopened Tickets closed by you";
    }

    if (strRQACurrentPhase != null && strRQACurrentPhase.trim().length() > 0)
    {
        ArrayList<String> RQAClosedBugsCounts = (ArrayList) request.getAttribute("RQAClosedBugsCounts");
        strClosedRQABugs = RQAClosedBugsCounts.get(0);
    }

    if (arlInternalTicketDetails != null && !arlInternalTicketDetails.isEmpty())
    {
        intOpenInternalTickets = arlInternalTicketDetails.size() / 9;
    }
    if (arlRQATicketDetails != null && !arlRQATicketDetails.isEmpty())
    {
        intOpenRQABugs = arlRQATicketDetails.size() / 10;
    }

    String strTicketETA;

    String strSLAMissedStyle = "";
    boolean blnTasksAvailable = false;
    boolean blnBugsAvailable = false;
    boolean blnTasksNotStartedAvailable = false;
    boolean blnTasksInProgressAvailable = false;
    boolean blnTasksOnHoldAvailable = false;
    boolean blnBugsNotStartedAvailable = false;
    boolean blnBugsInProgressAvailable = false;
    boolean blnBugsOnHoldAvailable = false;

    int intTaskNotStarted = 0;
    int intTaskInProgress = 0;
    int intTaskOnHold = 0;

    int intBugNotStarted = 0;
    int intBugInProgress = 0;
    int intBugOnHold = 0;

    int intMajorTasks = 0;
    int intCriticalTasks = 0;
    int intTopAgeingTasks = 0;
    int intOtherTasks = 0;

    ArrayList<String> arlBugsList = new ArrayList<String>();
    ArrayList<String> arlMajorTasksList = new ArrayList<String>();
    ArrayList<String> arlCriticalTasksList = new ArrayList<String>();
    ArrayList<String> arlTopAgeingTasksList = new ArrayList<String>();
    ArrayList<String> arlOtherTasksList = new ArrayList<String>();
    if (arlTicketDetails != null && !arlTicketDetails.isEmpty())
    {
        for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 15)
        {
            if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.NOT_STARTED))
                {
                    intTaskNotStarted++;
                }
                else if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                {
                    intTaskInProgress++;
                }
                else if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.ON_HOLD))
                {
                    intTaskOnHold++;
                }
                if (arlTicketDetails.get(iCount + 4).equalsIgnoreCase("Major"))
                {
                    intMajorTasks++;
                    arlMajorTasksList.add(arlTicketDetails.get(iCount));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 1));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 2));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 4));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 14));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 12));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 13));
                    arlMajorTasksList.add(arlTicketDetails.get(iCount + 6));
                }
                else if (arlTicketDetails.get(iCount + 4).equalsIgnoreCase("Critical"))
                {
                    intCriticalTasks++;
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 1));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 2));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 4));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 14));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 12));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 13));
                    arlCriticalTasksList.add(arlTicketDetails.get(iCount + 6));
                }
                else if (arlTicketDetails.get(iCount + 4).equalsIgnoreCase("Normal") || arlTicketDetails.get(iCount + 4).equalsIgnoreCase("Minor") || arlTicketDetails.get(iCount + 4).equalsIgnoreCase("Trivial"))
                {
                    if (Integer.parseInt(arlTicketDetails.get(iCount + 13)) < AppSettings.getTopAgeingThreshold())
                    {
                        intTopAgeingTasks++;
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 1));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 2));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 4));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 14));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 12));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 13));
                        arlTopAgeingTasksList.add(arlTicketDetails.get(iCount + 6));
                    }
                    else if (Integer.parseInt(arlTicketDetails.get(iCount + 13)) >= AppSettings.getTopAgeingThreshold())
                    {
                        intOtherTasks++;
                        arlOtherTasksList.add(arlTicketDetails.get(iCount));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 1));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 2));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 4));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 14));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 12));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 13));
                        arlOtherTasksList.add(arlTicketDetails.get(iCount + 6));
                    }
                }
            }
            else if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                arlBugsList.add(arlTicketDetails.get(iCount));
                arlBugsList.add(arlTicketDetails.get(iCount + 1));
                arlBugsList.add(arlTicketDetails.get(iCount + 2));
                arlBugsList.add(arlTicketDetails.get(iCount + 4));
                arlBugsList.add(arlTicketDetails.get(iCount + 14));
                arlBugsList.add(arlTicketDetails.get(iCount + 12));
                arlBugsList.add(arlTicketDetails.get(iCount + 13));
                arlBugsList.add(arlTicketDetails.get(iCount + 6));

                if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.NOT_STARTED))
                {
                    intBugNotStarted++;
                }
                else if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                {
                    intBugInProgress++;
                }
                else if (arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.ON_HOLD))
                {
                    intBugOnHold++;
                }
            }
        }
    }

    if (intTaskNotStarted + intTaskInProgress + intTaskOnHold > 0)
    {
        blnTasksAvailable = true;
    }
    if (intBugNotStarted + intBugInProgress + intBugOnHold > 0)
    {
        blnBugsAvailable = true;
    }

    if (intTaskNotStarted > 0)
    {
        blnTasksNotStartedAvailable = true;
    }

    if (intTaskInProgress > 0)
    {
        blnTasksInProgressAvailable = true;
    }

    if (intTaskOnHold > 0)
    {
        blnTasksOnHoldAvailable = true;
    }

    if (intBugNotStarted > 0)
    {
        blnBugsNotStartedAvailable = true;
    }

    if (intBugInProgress > 0)
    {
        blnBugsInProgressAvailable = true;
    }

    if (intBugOnHold > 0)
    {
        blnBugsOnHoldAvailable = true;
    }
    String strMonthStartDate = AppUtil.getMonthStartDate(strCurrentDateDDMMYYYY, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function setActionRefNoTicketId(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidAction.value = ActionName;
        }

        function ImageSubmitAction(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidSubmitAction.value = ActionName;
            submitHomePage();
        }


        function submitTaskNotStarted() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskNotStarted") {
                    OPTDialog("Select Task");
                    return;
                }
                if (document.OPTForm.TaskNotStartedAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskNotStartedAction.value;
            submitHomePage();
        }
        function submitTaskInProgress() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskInProgress") {
                    OPTDialog("Select Task");
                    return;
                }
                if (document.OPTForm.TaskInProgressAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskInProgressAction.value;
            submitHomePage();
        }

        function submitTaskOnHold() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskOnHold") {
                    OPTDialog("Select Task");
                    return;
                }
                if (document.OPTForm.TaskOnHoldAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskOnHoldAction.value;
            submitHomePage();
        }

        function submitBugNotStarted() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugNotStarted") {
                    OPTDialog("Select Bug");
                    return;
                }
                if (document.OPTForm.BugNotStartedAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.BugNotStartedAction.value;
            submitHomePage();
        }
        function submitBugInProgress() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugInProgress") {
                    OPTDialog("Select Bug");
                    return;
                }
                if (document.OPTForm.BugInProgressAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.BugInProgressAction.value;
            submitHomePage();
        }

        function submitBugOnHold() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugOnHold") {
                    OPTDialog("Select Bug");
                    return;
                }
                if (document.OPTForm.BugOnHoldAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.BugOnHoldAction.value;
            submitHomePage();
        }

        function submitHomePage() {
            if (document.OPTForm.hidSubmitAction.value == "loadReAssignTicket") {
                showFullPageMask(true);
                MM_openBrWindow("ReAssignTicketDummy?ACCESS=RESTRICTED", 550, 1100);
            }
            else if (document.OPTForm.hidSubmitAction.value == "deleteTicket") {
                showFullPageMask(true);
                jConfirm("Are you sure you want to delete this Ticket '" + document.OPTForm.hidTicketId.value + "'", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = document.OPTForm.hidSubmitAction.value;
                        frmWriteSubmit();
                    }
                });
            }
            else if (document.OPTForm.hidSubmitAction.value == "loadStopProgressTicket") {
                showFullPageMask(true);
                MM_openBrWindow("StopProgressTicketDummy?ACCESS=RESTRICTED", 450, 850);
            }
            else if (document.OPTForm.hidSubmitAction.value == "loadTicketDetails") {
                showFullPageMask(true);
                MM_openBrWindow("loadTicketDetails?ACCESS=RESTRICTED", 700, 1300);
            }
            else if (document.OPTForm.hidSubmitAction.value == "AddComments") {
                showFullPageMask(true);
                MM_openBrWindow("loadAddCommentsDummy?ACCESS=RESTRICTED", 600, 1300);
            }
            else {
                document.OPTForm.action = document.OPTForm.hidSubmitAction.value;
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            var varResult = "<%= strResult.toUpperCase() %>";
            var TeamName = "<%= strTeam %>";
            var MgmtTeam = "<%= AppConstants.MGMT_TEAM %>";
            var SLAMissed = "<%= strSLAMissedTicketsAvailable.toUpperCase().trim() %>";
            if (varResult == "TICKETDELETED") {
                OPTDialog("Ticket Deleted Successfully");
            }
            else if (varResult == "TICKETINPROGRESS") {
                OPTDialog("Ticket Moved to In Progress");
            }
            if (OOSLA_HIGHLIGHT == "true") {
                if (SLAMissed == "AVAILABLE" && TeamName != MgmtTeam) {
                    OPTDialog("You have OOSLA Tickets In Your Name HighLighted In Red Color.\nPlease Make Sure You Close Soon.", null);
                }
            }
        }
        function blinker() {
            $('.blink_me').fadeOut(500);
            $('.blink_me').fadeIn(500);
        }
        setInterval(blinker, 1000); //Runs every second

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadTicketDetails?ACCESS=RESTRICTED", 700, 1300);
        }

        function submitOtherLinks(actionval) {
            document.OPTForm.action = actionval;
            frmReadSubmit();
        }

        function submitClosedTickets(actionval, submitType) {
            if (submitType == "Week") {
                document.OPTForm.ClosedTicketFromDate.value = "";
                document.OPTForm.ClosedTicketToDate.value = "";
            }
            document.OPTForm.action = actionval;
            frmReadSubmit();
        }

        function TeamSummary() {
            document.OPTForm.hidTeamSummary.value = "YES";
            document.OPTForm.action = "homePage";
            frmReadSubmit();
        }
        function homePage() {
            document.OPTForm.hidTeamSummary.value = "";
            document.OPTForm.action = "homePage";
            frmReadSubmit();
        }

        function viewRQATicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadRQATicketDetails?ACCESS=RESTRICTED", 700, 1300);
        }
        function InternalTicketDetails(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadInternalTicketDetailsDummy?ACCESS=RESTRICTED", 600, 1300);
        }

        function updateInternalTicket(RefNo, TicketId, Assignee) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidOriginalAssignee.value = Assignee;
            document.OPTForm.action = "loadUpdateIT";
            frmWriteSubmit();
        }

        function ClosureTrend(TeamSummaryValue) {
            document.OPTForm.hidTeamSummary.value = TeamSummaryValue;
            showFullPageMask(true);
            MM_openBrWindow("ClosureTrendDummy?ACCESS=RESTRICTED", 650, 1300);
        }


        function setRQAActionRefNoTicketId(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidAction.value = ActionName;
        }


        function RQAImageSubmitAction(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidSubmitAction.value = ActionName;
            submitRQAHomePage();
        }

        function submitRQATickets() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "RQATickets") {
                    OPTDialog("Select Bug");
                    return;
                }
                if (document.OPTForm.RQATicketsAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.RQATicketsAction.value;
            submitRQAHomePage();
        }

        function submitRQAHomePage() {
            if (document.OPTForm.hidSubmitAction.value == "loadRQAReAssignBug") {
                showFullPageMask(true);
                MM_openBrWindow("ReAssignRQATicketDummy?ACCESS=RESTRICTED", 550, 1100);
            }
            else if (document.OPTForm.hidSubmitAction.value == "loadRQABugDetails") {
                showFullPageMask(true);
                MM_openBrWindow("loadRQATicketDetails?ACCESS=RESTRICTED", 700, 1300);
            }
            else if (document.OPTForm.hidSubmitAction.value == "RQAAddComments") {
                showFullPageMask(true);
                MM_openBrWindow("loadAddRQACommentsDummy?ACCESS=RESTRICTED", 600, 1300);
            }
            else {
                document.OPTForm.action = document.OPTForm.hidSubmitAction.value;
                frmWriteSubmit();
            }
        }

        function AddComments(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadAddITCommentsDummy?ACCESS=RESTRICTED", 600, 1300);
        }

        function CloseInternalTicket(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadCloseInternalTicketDummy?ACCESS=RESTRICTED", 600, 1300);
        }

        function ViewOpenTickets(TicketStatus) {
            document.OPTForm.TicketStatus.value = TicketStatus;
            document.OPTForm.action = "TicketList";
            frmReadSubmit();
        }

        function ViewReopenedTickets() {
            document.OPTForm.action = "loadReopenedTickets";
            frmReadSubmit();
        }


    </script>

</head>
<body onload="displaymessage()" onfocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidFromPage" value="HomePage">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidAction">
    <input type="hidden" name="hidSubmitAction">
    <input type="hidden" name="hidTeamSummary">
    <input type="hidden" name="hidOriginalAssignee">
    <input type="hidden" name="ClosedTicketFromDate" value="<%= strMonthStartDate %>">
    <input type="hidden" name="ClosedTicketToDate" value="<%= strCurrentDateDDMMYYYY %>">
    <input type="hidden" name="ReopenedTicketFromDate" value="06/10/2014">
    <input type="hidden" name="ReopenedTicketToDate" value="<%= strCurrentDateDDMMYYYY %>">
    <input type="hidden" name="Assignee" value="<%= strAssignee %>">
    <input type="hidden" name="hidPreWorkingDay">
    <input type="hidden" name="TicketStatus">
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="2%">&nbsp;</td>
            <%
                if (!strTeamSummary.equalsIgnoreCase("YES"))
                {
            %>
            <td width="23%" align="left">
                <a href="JavaScript:ClosureTrend('')" class="myButton" id="buttonId0">Click here for Your Closure
                    Trend</a>
            </td>
            <td width="50%" class="headerblue1" align="center">Your Tickets Dashboard
            </td>
            <td width="23%" align="right">
                <a href="JavaScript:TeamSummary()" class="myButton" id="buttonId1">Click here for Team Summary</a>
            </td>
            <%
            }
            else
            {
            %>
            <td width="23%" align="left">
                <a href="JavaScript:ClosureTrend('YES')" class="myButton" id="buttonId2">Click here for Team Closure
                    Trend</a>
            </td>
            <td width="50%" class="headerblue1" align="center">Team Dashboard and Summary</td>
            <td width="23%" align="right">
                <a href="JavaScript:homePage()" class="myButton" id="buttonId3">Click here for Your Dashboard</a>
            </td>
            <%
                }
            %>
            <td width="2%">&nbsp;</td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%@ include file="../common/line.jsp" %>
    <table width="99%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="46%" align="left">
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                    <tr class="LightBlueBandStyle">
                        <td width="32%" class="txtfirbrickbold13" rowspan="2">Summary</td>
                        <td width="8%" class="txtfirbrickbold13" rowspan="2" align="center">Bugs</td>
                        <td width="50%" colspan="6" class="txtfirbrickbold13" align="center">Tasks</td>
                        <td width="10%" class="txtgreenbold13" rowspan="2" align="center">SUM</td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtblackbold12" align="center">Critical</td>
                        <td class="txtblackbold12" align="center">Major</td>
                        <td class="txtblackbold12" align="center">Normal</td>
                        <td class="txtblackbold12" align="center">Minor</td>
                        <td class="txtblackbold12" align="center">Trivial</td>
                        <td class="txtfirbrickbold13" align="center">Total</td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Current Open</td>
                        <td class="txtgreybold" align="center"><%= arlOpenTicketsCount.get(0) %>
                        </td>

                        <td class="txtgrey" align="center"><%= arlOpenTicketsCount.get(1) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlOpenTicketsCount.get(2) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlOpenTicketsCount.get(3) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlOpenTicketsCount.get(4) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlOpenTicketsCount.get(5) %>
                        </td>
                        <td class="txtgreybold"
                            align="center"><%= Integer.parseInt(arlOpenTicketsCount.get(1)) +
                                Integer.parseInt(arlOpenTicketsCount.get(2)) +
                                Integer.parseInt(arlOpenTicketsCount.get(3)) +
                                Integer.parseInt(arlOpenTicketsCount.get(4)) +
                                Integer.parseInt(arlOpenTicketsCount.get(5)) %>
                        </td>
                        <td class="txtgreybold" align="center"><%= Integer.parseInt(arlOpenTicketsCount.get(1)) +
                                Integer.parseInt(arlOpenTicketsCount.get(2)) +
                                Integer.parseInt(arlOpenTicketsCount.get(3)) +
                                Integer.parseInt(arlOpenTicketsCount.get(4)) +
                                Integer.parseInt(arlOpenTicketsCount.get(0)) +
                                Integer.parseInt(arlOpenTicketsCount.get(5))%>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Closed For This Week</td>
                        <td class="txtgreybold"
                            align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(0) %>
                        </td>
                        <td class="txtgrey"
                            align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(1) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(2) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(3) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(4) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentWeek.get(5) %>
                        </td>
                        <td class="txtgreybold"
                            align="center"><%= Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(1)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(2)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(3)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(4)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(5)) %>
                        </td>
                        <td class="txtgreybold" align="center"><a
                                href="javascript:submitClosedTickets('closedTicketList', 'Week')"><%= Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(1)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(2)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(3)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(4)) + Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(0)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentWeek.get(5))%>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Closed For This Month</td>
                        <td class="txtgreybold"
                            align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(0) %>
                        </td>
                        <td class="txtgrey"
                            align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(1) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(2) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(3) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(4) %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlClosedTasksBugsCountsCurrentMonth.get(5) %>
                        </td>
                        <td class="txtgreybold"
                            align="center"><%= Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(1)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(2)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(3)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(4)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(5))%>
                        </td>
                        <td class="txtgreybold" align="center"><a
                                href="javascript:submitClosedTickets('closedTicketList', 'Month')"><%= Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(1)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(2)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(3)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(4)) + Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(0)) +
                                Integer.parseInt(arlClosedTasksBugsCountsCurrentMonth.get(5))%>
                        </a>
                        </td>
                    </tr>

                    <tr class="LightBlueBandStyle">
                        <td colspan="9">
                            <table width="100%" cellpadding=2 cellspacing=2>
                                <tr>
                                    <td width="50%" class="txtredbold13"><%= strReopenedCountTitle %></td>
                                    <td width="5%" align="center" class="txtgrey12"><a href="javascript:ViewReopenedTickets()"><%= strReopenTicketsCount %></a></td>
                                    <td width="45%">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                </table>
            </td>

            <td width="1%">&nbsp;</td>

            <td width="14%">
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                    <tr class="LightBlueBandStyle">
                        <td width="100%" align="center" class="txtfirbrickbold13" colspan="2">Tickets Priority</td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td width="80%" class="txtgreybold">Bugs</td>
                        <td width="20%" class="txtgreybold"
                            align="center"><%= intBugNotStarted + intBugInProgress + intBugOnHold %>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Critical Tasks</td>
                        <td class="txtgreybold" align="center"><%= intCriticalTasks %>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Major Tasks</td>
                        <td class="txtgreybold" align="center"><%= intMajorTasks %>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Top Ageing Tasks</td>
                        <td class="txtgreybold" align="center"><%= intTopAgeingTasks %>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Other Tasks</td>
                        <td class="txtgreybold" align="center"><%= intOtherTasks %>
                        </td>
                    </tr>
                </table>
            </td>

            <td width="1%">&nbsp;</td>

            <td width="17%">
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                    <tr class="LightBlueBandStyle">
                        <td width="54%" class="txtfirbrickbold13">Tickets Status</td>
                        <td width="23%" class="txtfirbrickbold13" align="center">Tasks</td>
                        <td width="23%" class="txtfirbrickbold13" align="center">Bugs</td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Not Started</td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.NOT_STARTED %>')"><%= intTaskNotStarted %>
                        </a>
                        </td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.NOT_STARTED %>')"><%= intBugNotStarted %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">In Progress</td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.IN_PROGRESS %>')"><%= intTaskInProgress %>
                        </a>
                        </td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.IN_PROGRESS %>')"><%= intBugInProgress %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">On Hold</td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.ON_HOLD %>')"><%= intTaskOnHold %>
                        </a>
                        </td>
                        <td class="txtgrey" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.ON_HOLD %>')"><%= intBugOnHold %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtgreybold">Total</td>
                        <td class="txtgreybold"
                            align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.ALL_STATUS %>')"><%= intTaskNotStarted + intTaskInProgress + intTaskOnHold%>
                        </a>
                        </td>
                        <td class="txtgreybold" align="center"><a
                                href="javascript:ViewOpenTickets('<%= AppConstants.ALL_STATUS %>')"><%= intBugNotStarted + intBugInProgress + intBugOnHold %>
                        </a>
                        </td>
                    </tr>
                </table>
            </td>

            <td width="1%">&nbsp;</td>

            <td width="20%" align="right">
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                    <tr class="LightBlueBandStyle">
                        <td width="90%" class="txtfirbrickbold13">RQA<%= " " + strRQACurrentPhase %> Open Bugs</td>
                        <td width="10%" class="txtfirbrickbold13" align="center"><a
                                href="javascript:submitOtherLinks('loadRQACurrentStatus')"><%= intOpenRQABugs %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td width="90%" class="txtfirbrickbold13">RQA<%= " " + strRQACurrentPhase %> Closed Bugs</td>
                        <td width="10%" class="txtfirbrickbold13" align="center"><a
                                href="javascript:submitOtherLinks('loadRQACurrentStatus')"><%= strClosedRQABugs %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td width="90%" class="txtfirbrickbold13">Open Internal Tickets</td>
                        <td width="10%" class="txtfirbrickbold13" align="center"><a
                                href="javascript:submitOtherLinks('CurrentInternalTickets')"><%= intOpenInternalTickets %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td width="90%" class="txtredbold13">Out Of SLA Tickets</td>
                        <td width="10%" class="txtredbold13" align="center"><a
                                href="javascript:submitOtherLinks('SLAMissedTickets')"><%= strSLAMissedTicketsCount %>
                        </a>
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td width="90%" class="txtorangebold13">Nearing Out Of SLA Tickets</td>
                        <td width="10%" class="txtorangebold13" align="center"><%= strNearingSLATicketsCount %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/line.jsp" %>
    <%@ include file="../common/twospace.jsp" %>

    <%
        if (!strTeamSummary.equalsIgnoreCase("YES"))
        {
    %>
    <%@ include file="../home/tasks.jsp" %>
    <%@ include file="../home/bugs.jsp" %>
    <%@ include file="../home/rqabugs.jsp" %>
    <%@ include file="../home/internaltickets.jsp" %>
    <%
        }
    %>

    <%
        if (strTeamSummary.equalsIgnoreCase("YES"))
        {
    %>
    <%@ include file="../home/bugssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/criticaltaskssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/majortaskssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/topagintaskssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/othertaskssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/rqabugssummary.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../home/internalticketssummary.jsp" %>
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>
</form>
</body>
</html>
