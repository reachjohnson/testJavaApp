<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlCounts = (ArrayList) request.getAttribute("Counts");

    ArrayList<String> arlReceivedTasks = (ArrayList) request.getAttribute("ReceivedTasks");
    ArrayList<String> arlReceivedBugs = (ArrayList) request.getAttribute("ReceivedBugs");
    ArrayList<String> arlClosedTasks = (ArrayList) request.getAttribute("ClosedTasks");
    ArrayList<String> arlClosedBugs = (ArrayList) request.getAttribute("ClosedBugs");
    ArrayList<String> arlCurrentOpenTasks = (ArrayList) request.getAttribute("CurrentOpenTasks");
    ArrayList<String> arlCurrentOpenBugs = (ArrayList) request.getAttribute("CurrentOpenBugs");

    boolean blnDisplayHeading = false;
    String strFromDate = AppUtil.checkNull((String) request.getAttribute("FromDate"));
    String strToDate = AppUtil.checkNull((String) request.getAttribute("ToDate"));
    if (strFromDate.length() > 0 && strToDate.length() > 0)
    {
        blnDisplayHeading = true;
    }

    if (strFromDate.length() == 0)
    {
        strFromDate = strCurrentDateDDMMYYYY;
    }
    if (strToDate.length() == 0)
    {
        strToDate = strCurrentDateDDMMYYYY;
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function validateFields() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.SLAReportsFromDate.value)) {
                    OPTDialog("Enter From Date", document.OPTForm.SLAReportsFromDate);
                    return false;
                }
                if (isEmpty(document.OPTForm.SLAReportsToDate.value)) {
                    OPTDialog("Enter To Date", document.OPTForm.SLAReportsToDate);
                    return false;
                }
                if (!compareTwoDates(document.OPTForm.SLAReportsFromDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than current date", document.OPTForm.SLAReportsFromDate);
                    return false;
                }
                if (!compareTwoDates(RestrictedFromDate, document.OPTForm.SLAReportsFromDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog(FromDateMessage, document.OPTForm.SLAReportsFromDate);
                    return;
                }

                if (!compareTwoDates(document.OPTForm.SLAReportsToDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("To date should not be greater than current date", document.OPTForm.SLAReportsToDate);
                    return false;
                }
                if (!compareTwoDates(document.OPTForm.SLAReportsFromDate.value, document.OPTForm.SLAReportsToDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than To Date", document.OPTForm.SLAReportsFromDate);
                    return false;
                }
                return true;
            }
        }
        function GenerateSLAReport() {
            if (validateFields()) {
                document.OPTForm.action = "loadSLAReports";
                frmReadSubmit();
            }
        }

        function SLARtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                GenerateSLAReport();
            }
        }


        function GenerateSLAPDFReport() {
            if (validateFields()) {
                showFullPageMask(true);
                MM_openBrWindow('loadSLAReportsPDF?ACCESS=RESTRICTED', 500, 1300);
            }
        }

    </script>
</head>
<body onload="document.OPTForm.SLAReportsFromDate.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Filters to Generate SLA Reports
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="99%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="28%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="35%" class="txtgreybold">From Date</td>
                                                <td width="65%">
                                                    <input type="text" name="SLAReportsFromDate" size="10"
                                                           maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)"
                                                           value="<%= strFromDate %>"
                                                           onkeypress="SLARtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar7','dd/mm/yyyy');"><img
                                                            src="images/cal.gif" width="20"
                                                            height="13" border="0"
                                                            alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="28%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="35%" class="txtgreybold">To Date</td>
                                                <td width="65%">
                                                    <input type="text" name="SLAReportsToDate" size="10" maxlength="20"
                                                           class="txtgrey"
                                                           onblur="checkValidDateForObject(this)"
                                                           value="<%= strToDate %>"
                                                           onkeypress="SLARtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar8','dd/mm/yyyy');"><img
                                                            src="images/cal.gif" width="20"
                                                            height="13" border="0"
                                                            alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="16%" valign="middle">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="80%" align="right" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search"
                                                           onclick="JavaScript:GenerateSLAReport()" id="buttonId0"/>&nbsp;&nbsp;
                                                </td>
                                                <td width="20%" align="left" valign="bottom">&nbsp;
                                                    <a href="JavaScript:GenerateSLAPDFReport()"><img
                                                            src="images/pdf-icon.png" border="0"
                                                            alt=""></a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="2%" valign="middle">&nbsp;</td>
                                    <%
                                        if (blnDisplayHeading)
                                        {
                                    %>
                                    <td width="24%" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=2 class="TableBorder1Pix"
                                               align="left">
                                            <tr>
                                                <td colspan="4" width="100%" class="DarkBlueBandStyleText14">&nbsp;Summary
                                                </td>
                                            </tr>
                                            <tr class="LightBlueBandStyle">
                                                <td width="40%" class="txtblackbold12">Category</td>
                                                <td width="20%" class="txtblackbold12" align="center">Tasks</td>
                                                <td width="20%" class="txtblackbold12" align="center">Bugs</td>
                                                <td width="20%" class="txtblackbold12" align="center">Total</td>
                                            </tr>

                                            <%
                                                int intCounter1 = 0;
                                                String strBackGround1 = "";

                                                for (int intCount = 0; intCount < arlCounts.size(); intCount += 4)
                                                {
                                                    if (intCounter1 % 2 == 0)
                                                    {
                                                        strBackGround1 = "LightGreyBandStyle";
                                                    }
                                                    else
                                                    {
                                                        strBackGround1 = "DarkGreyBandStyle";
                                                    }

                                            %>
                                            <tr class="<%= strBackGround1 %>">
                                                <td class="txtgrey"><%= arlCounts.get(intCount) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlCounts.get(intCount + 1) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlCounts.get(intCount + 2) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlCounts.get(intCount + 3) %>
                                                </td>
                                            </tr>
                                            <%
                                                    intCounter1++;
                                                }
                                            %>
                                        </table>
                                    </td>
                                    <%
                                    }
                                    else
                                    {
                                    %>
                                    <td width="24%" valign="middle">&nbsp;</td>
                                    <%
                                        }
                                    %>
                                    <td width="2%" valign="middle">&nbsp;</td>
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
        if (blnDisplayHeading)
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Received Tasks
                            (<%= arlReceivedTasks.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlReceivedTasks != null && !arlReceivedTasks.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlReceivedTasks.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlReceivedTasks.get(intCount+1) %>', '<%= arlReceivedTasks.get(intCount+1) %>')"
                                class="link"><%= arlReceivedTasks.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlReceivedTasks.get(intCount+1) %>', '<%= arlReceivedTasks.get(intCount+1) %>')"
                                class="link"><%= arlReceivedTasks.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlReceivedTasks.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlReceivedTasks.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlReceivedTasks.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Received Bugs
                            (<%= arlReceivedBugs.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlReceivedBugs != null && !arlReceivedBugs.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlReceivedBugs.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlReceivedBugs.get(intCount+1) %>', '<%= arlReceivedBugs.get(intCount+1) %>')"
                                class="link"><%= arlReceivedBugs.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlReceivedBugs.get(intCount+1) %>', '<%= arlReceivedBugs.get(intCount+1) %>')"
                                class="link"><%= arlReceivedBugs.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlReceivedBugs.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlReceivedBugs.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlReceivedBugs.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Closed Tasks
                            (<%= arlClosedTasks.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlClosedTasks != null && !arlClosedTasks.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                        <td width="8%" class="txtblackbold12">Actual End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlClosedTasks.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlClosedTasks.get(intCount+1) %>', '<%= arlClosedTasks.get(intCount+1) %>')"
                                class="link"><%= arlClosedTasks.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlClosedTasks.get(intCount+1) %>', '<%= arlClosedTasks.get(intCount+1) %>')"
                                class="link"><%= arlClosedTasks.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlClosedTasks.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlClosedTasks.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlClosedTasks.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Closed Tasks
                            (<%= arlClosedBugs.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlClosedBugs != null && !arlClosedBugs.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                        <td width="8%" class="txtblackbold12">Actual End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlClosedBugs.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlClosedBugs.get(intCount+1) %>', '<%= arlClosedBugs.get(intCount+1) %>')"
                                class="link"><%= arlClosedBugs.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlClosedBugs.get(intCount+1) %>', '<%= arlClosedBugs.get(intCount+1) %>')"
                                class="link"><%= arlClosedBugs.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlClosedBugs.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlClosedBugs.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlClosedBugs.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Current Open Tasks
                            (<%= arlCurrentOpenTasks.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlCurrentOpenTasks != null && !arlCurrentOpenTasks.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlCurrentOpenTasks.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCurrentOpenTasks.get(intCount+1) %>', '<%= arlCurrentOpenTasks.get(intCount+1) %>')"
                                class="link"><%= arlCurrentOpenTasks.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCurrentOpenTasks.get(intCount+1) %>', '<%= arlCurrentOpenTasks.get(intCount+1) %>')"
                                class="link"><%= arlCurrentOpenTasks.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlCurrentOpenTasks.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlCurrentOpenTasks.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlCurrentOpenTasks.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Current Open Bugs
                            (<%= arlCurrentOpenBugs.size() / 6 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlCurrentOpenBugs != null && !arlCurrentOpenBugs.isEmpty())
                    {
                        int intSerialNo = 0;
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="65%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int intCount = 0; intCount < arlCurrentOpenBugs.size(); intCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCurrentOpenBugs.get(intCount+1) %>', '<%= arlCurrentOpenBugs.get(intCount+1) %>')"
                                class="link"><%= arlCurrentOpenBugs.get(intCount + 1) %>
                        </a></td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCurrentOpenBugs.get(intCount+1) %>', '<%= arlCurrentOpenBugs.get(intCount+1) %>')"
                                class="link"><%= arlCurrentOpenBugs.get(intCount + 2) %>
                        </a></td>
                        <td class="txtgrey"><%= arlCurrentOpenBugs.get(intCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlCurrentOpenBugs.get(intCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlCurrentOpenBugs.get(intCount + 5) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
    <%
        }
    %>
</form>
</body>
</html>