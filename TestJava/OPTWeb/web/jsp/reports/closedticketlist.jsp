<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlTicketDetails = (ArrayList) request.getAttribute("TicketDetails");
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    String strDashBoardName = AppUtil.checkNull((String) request.getAttribute("DashBoardName"));
    String strFromDate = AppUtil.checkNull((String) request.getAttribute("FromDate"));
    String strToDate = AppUtil.checkNull((String) request.getAttribute("ToDate"));

    if (strFromDate.length() == 0)
    {
        strFromDate = strCurrentDateDDMMYYYY;
    }
    if (strToDate.length() == 0)
    {
        strToDate = strCurrentDateDDMMYYYY;
    }

    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    String strSelected = "";
    String strSLAMissedStyle = "";

    int intTaskCount = 0;
    int intBugsCount = 0;
    int intTotalCount = 0;
    if (arlTicketDetails != null && !arlTicketDetails.isEmpty())
    {
        for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 12)
        {
            if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                intTaskCount++;
            }
        }
        for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 12)
        {
            if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                intBugsCount++;
            }
        }
    }
    intTotalCount = intTaskCount + intBugsCount;

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function CTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                closedTicketList();
            }
        }


        function closedTicketList() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.ClosedTicketFromDate.value)) {
                    OPTDialog("Enter From Date", document.OPTForm.ClosedTicketFromDate);
                    return;
                }
                if (isEmpty(document.OPTForm.ClosedTicketToDate.value)) {
                    OPTDialog("Enter To Date", document.OPTForm.ClosedTicketToDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.ClosedTicketFromDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than current date", document.OPTForm.ClosedTicketFromDate);
                    return;
                }
                if (!compareTwoDates(RestrictedFromDate, document.OPTForm.ClosedTicketFromDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog(FromDateMessage, document.OPTForm.ClosedTicketFromDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.ClosedTicketToDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("To date should not be greater than current date", document.OPTForm.ClosedTicketToDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.ClosedTicketFromDate.value, document.OPTForm.ClosedTicketToDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than To Date", document.OPTForm.ClosedTicketFromDate);
                    return;
                }
                document.OPTForm.action = "closedTicketList";
                frmReadSubmit();
            }
        }

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadClosedTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }
    </script>
</head>
<body onload="document.OPTForm.ClosedTicketFromDate.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidAssignee" value="<%= strDashBoardName %>">
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Filters to View Closed Tickets
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="25%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="25%" class="txtgreybold">From Date</td>
                                                <td width="75%">
                                                    <input type="text" name="ClosedTicketFromDate" size="10" maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)" value="<%= strFromDate %>"
                                                           onkeypress="CTtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar1','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                                                 height="13" border="0"
                                                                                                                 alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="25%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="25%" class="txtgreybold">To Date</td>
                                                <td width="75%">
                                                    <input type="text" name="ClosedTicketToDate" size="10" maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)" value="<%= strToDate %>"
                                                           onkeypress="CTtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar2','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                                                 height="13" border="0"
                                                                                                                 alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Assignee</td>
                                                <td width="60%">
                                                    <select name="Assignee" class="txtgrey" onkeypress="CTtrackEnterKey(event)">
                                                        <%
                                                            strSelected = "";
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
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="10%" valign="middle">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="100%" align="center" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search" onclick="JavaScript:closedTicketList()"
                                                           id="buttonId0"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <%
                                        if (strDashBoardName.length() > 0)
                                        {
                                    %>
                                    <td width="20%" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=2 class="TableBorder1Pix">
                                            <tr>
                                                <td colspan="3" width="100%" class="DarkBlueBandStyleText14">&nbsp;Summary
                                                </td>
                                            </tr>
                                            <tr class="LightBlueBandStyle">
                                                <td width="5%" class="txtblackbold12" align="center">Tasks</td>
                                                <td width="5%" class="txtblackbold12" align="center">Bugs</td>
                                                <td width="5%" class="txtblackbold12" align="center">Total</td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgrey" align="center"><%= intTaskCount %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= intBugsCount %>
                                                </td>
                                                <td class="txtgreybold" align="center"><%= intTotalCount %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <%
                                    }
                                    else
                                    {
                                    %>
                                    <td width="20%" valign="middle">&nbsp;</td>
                                    <%
                                        }
                                    %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strDashBoardName.length() > 0)
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Tasks
                            (<%= intTaskCount%>)
                        </td>
                    </tr>
                </table>
                <%
                    if (intTaskCount > 0)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="15%" class="txtblackbold12">Assignee</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="34%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="17%" class="txtblackbold12">Ticket Resolution</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                        <td width="7%" class="txtblackbold12">Closed Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 12)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strSLAMissedStyle = OOSLA_GREY_TEXT;
                            if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK))
                            {
                                if (arlTicketDetails.get(iCount + 11).equalsIgnoreCase("1"))
                                {
                                    strSLAMissedStyle = OOSLA_RED_TEXT;
                                }
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTicketDetails.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTicketDetails.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlTicketDetails.get(iCount) %>','<%= arlTicketDetails.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlTicketDetails.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 10) %>
                        </td>
                        <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 8) %>
                        </td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                </table>
                <%
                }
                else
                {
                %>
                <%@ include file="../common/nodataavailable.jsp" %>
                <%
                    }
                %>
            </td>
        </tr>
    </table>

    <%@ include file="../common/twospace.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Bugs
                            (<%= intBugsCount%>)
                        </td>
                    </tr>
                </table>
                <%
                    if (intBugsCount > 0)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="15%" class="txtblackbold12">Assignee</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="34%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="17%" class="txtblackbold12">Ticket Resolution</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                        <td width="7%" class="txtblackbold12">Closed Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 12)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strSLAMissedStyle = OOSLA_GREY_TEXT;
                            if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
                            {
                                if (arlTicketDetails.get(iCount + 11).equalsIgnoreCase("1"))
                                {
                                    strSLAMissedStyle = OOSLA_RED_TEXT;
                                }
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTicketDetails.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTicketDetails.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlTicketDetails.get(iCount) %>','<%= arlTicketDetails.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlTicketDetails.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 10) %>
                        </td>

                        <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlTicketDetails.get(iCount + 8) %>
                        </td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                </table>
                <%
                }
                else
                {
                %>
                <%@ include file="../common/nodataavailable.jsp" %>
                <%
                    }
                %>
            </td>
        </tr>
    </table>
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
