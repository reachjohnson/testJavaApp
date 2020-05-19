<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlTicketDetails = (ArrayList) request.getAttribute("TicketDetails");
    String strSLAMissedTicketsCount = AppUtil.checkNull((String) request.getAttribute("SLAMissedTicketsCount"));
    String strNearingSLATicketsCount = AppUtil.checkNull((String) request.getAttribute("NearingSLATicketsCount"));
    ArrayList<String> arlNearingSLATickets = (ArrayList) request.getAttribute("NearingSLATickets");

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
            }
            else if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
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
        function submitTaskNotStarted() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskNotStarted") {
                    OPTDialog("Select Task")
                    return;
                }
                if (document.OPTForm.TaskNotStartedAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskNotStartedAction.value;
                document.OPTForm.action = document.OPTForm.TaskNotStartedAction.value;
                submitModifyCurrentTickets();
            }
        }
        function submitTaskInProgress() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskInProgress") {
                    OPTDialog("Select Task")
                    return;
                }
                if (document.OPTForm.TaskInProgressAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskInProgressAction.value;
                submitModifyCurrentTickets();
            }
        }

        function submitTaskOnHold() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "TaskOnHold") {
                    OPTDialog("Select Task")
                    return;
                }
                if (document.OPTForm.TaskOnHoldAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.TaskOnHoldAction.value;
                submitModifyCurrentTickets();
            }
        }

        function submitBugNotStarted() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugNotStarted") {
                    OPTDialog("Select Bug")
                    return;
                }
                if (document.OPTForm.BugNotStartedAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.BugNotStartedAction.value;
                submitModifyCurrentTickets();
            }
        }
        function submitBugInProgress() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugInProgress") {
                    OPTDialog("Select Bug")
                    return;
                }
                if (document.OPTForm.BugInProgressAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.BugInProgressAction.value;
                submitModifyCurrentTickets();
            }
        }

        function submitBugOnHold() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "BugOnHold") {
                    OPTDialog("Select Bug")
                    return;
                }
                if (document.OPTForm.BugOnHoldAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
                document.OPTForm.hidSubmitAction.value = document.OPTForm.BugOnHoldAction.value;
                submitModifyCurrentTickets();
            }
        }

        function modifycurrenttickets() {
            document.OPTForm.action = "loadModifyCurrentTickets";
            frmReadSubmit();
        }

        function MCTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                modifycurrenttickets();
            }
        }


        function submitModifyCurrentTickets() {
            if (document.OPTForm.hidSubmitAction.value == "loadReAssignTicket") {
                showFullPageMask(true);
                MM_openBrWindow("ReAssignTicketDummy?ACCESS=RESTRICTED", 450, 1100);
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
            document.OPTForm.Assignee.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "TICKETDELETED") {
                OPTDialog("Ticket Deleted Successfully", document.OPTForm.Assignee);
            }
            else if (varResult == "TICKETINPROGRESS") {
                OPTDialog("Ticket Moved to In Progress", document.OPTForm.Assignee);
            }
        }
    </script>
</head>
<body onload="displaymessage()" onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidFromPage" value="ModifyCurrentTickets">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidAction">
    <input type="hidden" name="hidSubmitAction">
    <input type="hidden" name="hidPreWorkingDay">

    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" colspan="3">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Live Tickets - Admin Changes
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="45%" class="txtgrey" valign="middle">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="15%" class="txtgreybold">&nbsp;&nbsp;Assignee</td>
                                    <td width="65%">
                                        <select name="Assignee" class="txtgrey" onkeypress="MCTtrackEnterKey(event)">
                                            <%
                                                String strSelected = "";
                                                if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
                                                    strSelected = "selected";
                                            %>
                                            <option value="<%= AppConstants.ALL_ASSIGNEES %>" <%= strSelected %>><%= AppConstants.ALL_ASSIGNEES %>
                                            </option>
                                            <%
                                                if (arlAssignees != null && !arlAssignees.isEmpty())
                                                {
                                                    for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                                                    {
                                                        strSelected = "";
                                                        if (strAssignee.equalsIgnoreCase(arlAssignees.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlAssignees.get(intCount) %>" <%= strSelected %>><%= arlAssignees.get(intCount + 1) %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search"
                                               onclick="JavaScript:modifycurrenttickets()" id="buttonId0"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="5%">&nbsp;</td>
                        <%
                            if (Integer.parseInt(strSLAMissedTicketsCount) > 0 || Integer.parseInt(strNearingSLATicketsCount) > 0)
                            {
                        %>
                        <td width="50%" valign="middle">
                            <table width="100%" cellpadding=3 cellspacing=3 class="tableborder1pix" align="left">
                                <%
                                    if (Integer.parseInt(strSLAMissedTicketsCount) > 0)
                                    {
                                %>
                                <tr class="lightbandstyle">
                                    <td width="19%" class="txtredbold14">Out Of SLA Tickets</td>
                                    <td width="1%" class="txtredbold14"><%= strSLAMissedTicketsCount%>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                                <%
                                    if (Integer.parseInt(strNearingSLATicketsCount) > 0)
                                    {
                                %>
                                <tr class="lightbandstyle">
                                    <td width="19%" class="txtorangebold14">Nearing Out Of SLA Tickets</td>
                                    <td width="1%" class="txtorangebold14"><%= strNearingSLATicketsCount %>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </table>
                        </td>
                        <%
                        }
                        else
                        {
                        %>
                        <td width="50%">&nbsp;</td>
                        <%
                            }
                        %>
                    </tr>
                </table>
            </td>
            <td width="40%">&nbsp;</td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strAssignee.length() > 0)
        {
    %>
    <%@ include file="../tickets/admintasks.jsp" %>
    <%@ include file="../common/twospace.jsp" %>
    <%@ include file="../tickets/adminbugs.jsp" %>
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>
</form>
</body>
</html>
