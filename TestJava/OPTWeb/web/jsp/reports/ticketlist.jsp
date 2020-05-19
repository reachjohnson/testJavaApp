<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlTasksDetails = (ArrayList) request.getAttribute("TasksDetails");
    ArrayList<String> arlBugsDetails = (ArrayList) request.getAttribute("BugsDetails");
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    String strDashBoardName = AppUtil.checkNull((String) request.getAttribute("DashBoardName"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    String strTicketStatus = AppUtil.checkNull((String) request.getAttribute("TicketStatus"));
    String strTicketPriority = AppUtil.checkNull((String) request.getAttribute("TicketPriority"));
    String strTicketModule = AppUtil.checkNull((String) request.getAttribute("TicketModule"));
    String strSLAMissedTicketsCount = AppUtil.checkNull((String) request.getAttribute("SLAMissedTicketsCount"));
    String strNearingSLATicketsCount = AppUtil.checkNull((String) request.getAttribute("NearingSLATicketsCount"));
    ArrayList<String> arlNearingSLATickets = (ArrayList) request.getAttribute("NearingSLATickets");
    ArrayList<Object> arlOpenTicketsETAHistory = (ArrayList) request.getAttribute("OpenTicketsETAHistory");
    int intTaskCount = 0;
    int intBugsCount = 0;
    int intTotalCount = 0;
    String strSelected = "";
    String strSLAMissedStyle = "";
    String strTicketETA = "";
    String strTicketETATBDTAG = "<span class=\"blink_me\" style=\"font-weight: bold;color: #FF0000;\">ETA - TBD</span>";
    if (arlTasksDetails != null && !arlTasksDetails.isEmpty())
    {
        intTaskCount = arlTasksDetails.size() / 13;
    }

    if (arlBugsDetails != null && !arlBugsDetails.isEmpty())
    {
        intBugsCount = arlBugsDetails.size() / 13;
    }
    intTotalCount = intTaskCount + intBugsCount;
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function ticketList() {
            document.OPTForm.action = "TicketList";
            frmReadSubmit();
        }
        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }

        function CTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                ticketList();
            }
        }

        function blinker() {
            $('.blink_me').fadeOut(500);
            $('.blink_me').fadeIn(500);
        }
        setInterval(blinker, 1000); //Runs every second

    </script>
</head>
<body onload="document.OPTForm.Assignee.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidAssignee" value="<%= strDashBoardName %>">
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Filters to View Current Open Tickets
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="25%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="25%" class="txtgreybold">Assignee</td>
                                                <td width="75%">
                                                    <select name="Assignee" class="txtgrey"
                                                            onkeypress="CTtrackEnterKey(event)">
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
                                    <td width="20%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Ticket Status</td>
                                                <td width="60%">
                                                    <select name="TicketStatus" class="txtgrey"
                                                            onkeypress="CTtrackEnterKey(event)">
                                                        <%
                                                            strSelected = "";
                                                            if (strTicketStatus.equalsIgnoreCase(AppConstants.ALL_STATUS))
                                                                strSelected = "selected";
                                                        %>
                                                        <option value="<%= AppConstants.ALL_STATUS %>" <%= strSelected %>><%= AppConstants.ALL_STATUS %>
                                                                <%
                                strSelected = "";
                                if (strTicketStatus.equalsIgnoreCase(AppConstants.NOT_STARTED))
                                    strSelected = "selected";
                            %>
                                                        <option value="<%= AppConstants.NOT_STARTED %>" <%= strSelected %>><%= AppConstants.NOT_STARTED %>
                                                                <%
                                strSelected = "";
                                if (strTicketStatus.equalsIgnoreCase(AppConstants.IN_PROGRESS))
                                    strSelected = "selected";
                            %>
                                                        <option value="<%= AppConstants.IN_PROGRESS %>" <%= strSelected %>><%= AppConstants.IN_PROGRESS %>
                                                                <%
                                strSelected = "";
                                if (strTicketStatus.equalsIgnoreCase(AppConstants.ON_HOLD))
                                    strSelected = "selected";
                            %>
                                                        <option value="<%= AppConstants.ON_HOLD %>" <%= strSelected %>><%= AppConstants.ON_HOLD %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Ticket Priority</td>
                                                <td width="60%">
                                                    <select name="TicketPriority" class="txtgrey"
                                                            onkeypress="CTtrackEnterKey(event)">
                                                        <%
                                                            strSelected = "";
                                                            if (strTicketPriority.equalsIgnoreCase(AppConstants.ALL_PRIORITY))
                                                            {
                                                                strSelected = "selected";
                                                            }

                                                        %>
                                                        <option value="<%= AppConstants.ALL_PRIORITY %>" <%= strSelected %>><%= AppConstants.ALL_PRIORITY %>
                                                                <%
                                for (String strTaskPriority : AppConstants.TICKET_PRIORITY)
                                {
                                    strSelected = "";
                                    if (strTicketPriority.equalsIgnoreCase(strTaskPriority))
                                    {
                                        strSelected = "selected";
                                    }
                            %>
                                                        <option value="<%= strTaskPriority %>" <%= strSelected %>><%= strTaskPriority %>
                                                        </option>
                                                        <%
                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Ticket Module</td>
                                                <td width="60%">
                                                    <select name="TicketModule" class="txtgrey"
                                                            onkeypress="CTtrackEnterKey(event)">
                                                        <%
                                                            strSelected = "";
                                                            if (strTicketPriority.equalsIgnoreCase(AppConstants.ALL_MODULE))
                                                            {
                                                                strSelected = "selected";
                                                            }

                                                        %>
                                                        <option value="<%= AppConstants.ALL_MODULE %>" <%= strSelected %>><%= AppConstants.ALL_MODULE %>
                                                                <%
                                for (String strTicketMod : AppConstants.TICKET_MODULE)
                                {
                                    strSelected = "";
                                    if (strTicketMod.equalsIgnoreCase(strTicketModule))
                                    {
                                        strSelected = "selected";
                                    }
                            %>
                                                        <option value="<%= strTicketMod %>" <%= strSelected %>><%= strTicketMod %>
                                                        </option>
                                                        <%
                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="10%">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="100%" align="center" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search"
                                                           onclick="JavaScript:ticketList()"
                                                           id="buttonId0"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="5%">&nbsp;</td>
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
    <table width="99%" cellpadding=1 cellspacing=1 align="center">
        <tr>
            <td width="20%" valign="middle">
                <table width="100%" cellpadding=2 cellspacing=2 align="left" class="TableBorder1Pix">
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
            <td width="5%">&nbsp;</td>
            <td width="25%" valign="middle">
                <%
                    if (Integer.parseInt(strSLAMissedTicketsCount) > 0 || Integer.parseInt(strNearingSLATicketsCount) > 0)
                    {
                %>
                <table width="100%" cellpadding=3 cellspacing=3 class="TableBorder1Pix">
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
                <%
                    }
                %>
            </td>
            <td width="50%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

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
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="30%" class="txtblackbold12">Ticket Description</td>
                        <td width="8%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Ticket Status</td>
                        <td width="8%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                        <td width="8%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12" align="center">Ageing</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        StringBuffer sbETAHistory;
                        for (int iCount = 0; iCount < arlTasksDetails.size(); iCount += 13)
                        {
                            sbETAHistory = new StringBuffer();
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strSLAMissedStyle = OOSLA_GREY_TEXT;
                            if (arlTasksDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTasksDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                            strTicketETA = arlTasksDetails.get(iCount + 11);
                            if (strTicketETA != null && strTicketETA.length() > 0)
                            {
                                ArrayList<Date> arlETADates = new ArrayList();
                                SimpleDateFormat ETA_DISPLAY_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
                                sbETAHistory.append("title=\"");
                                for (int intETACount = 0; intETACount < arlOpenTicketsETAHistory.size(); intETACount += 3)
                                {
                                    if (arlTasksDetails.get(iCount).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount)) &&
                                            arlTasksDetails.get(iCount + 1).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount + 1)))
                                    {
                                        arlETADates = (ArrayList<Date>) arlOpenTicketsETAHistory.get(intETACount + 2);
                                        for (Date objTemp : arlETADates)
                                        {
                                            sbETAHistory.append(ETA_DISPLAY_FORMAT.format(objTemp)).append("\n");
                                        }
                                    }
                                }
                                sbETAHistory.append("\"");
                            }
                            if ((arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Blocker") ||
                                    arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Critical") ||
                                    arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Major")) && (strTicketETA == null || strTicketETA.trim().length() == 0))
                            {
                                strTicketETA = strTicketETATBDTAG;
                            }

                            if ((arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Normal") ||
                                    arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Minor") ||
                                    arlTasksDetails.get(iCount + 4).equalsIgnoreCase("Trivial")) && Integer.parseInt(arlTasksDetails.get(iCount + 12)) < AppSettings.getTopAgeingThreshold() && (strTicketETA == null || strTicketETA.trim().length() == 0))
                            {
                                strTicketETA = strTicketETATBDTAG;
                            }

                            if (arlTasksDetails.get(iCount + 11) != null && arlTasksDetails.get(iCount + 11).length() > 0)
                            {
                                SimpleDateFormat ETA_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
                                SimpleDateFormat CURRENT_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
                                Date ETADate = ETA_DATE_FORMAT.parse(arlTasksDetails.get(iCount + 11));
                                Date CurrentDate = CURRENT_DATE_FORMAT.parse(strCurrentDateDDMMYYYY);
                                if (CurrentDate.compareTo(ETADate) > 0)
                                {
                                    strTicketETA = "<span class=\"blink_me\" style=\"font-weight: bold;color: #FF0000;\">" + strTicketETA + "</span>";
                                }

                            }


                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlTasksDetails.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTasksDetails.get(iCount + 1) %>', '<%= arlTasksDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTasksDetails.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTasksDetails.get(iCount + 1) %>', '<%= arlTasksDetails.get(iCount + 1) %>')"
                                class="link"><%= arlTasksDetails.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlTasksDetails.get(iCount) %>','<%= arlTasksDetails.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlTasksDetails.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTasksDetails.get(iCount + 6) %>
                        </td>
                        <td class="txtgrey"><%= arlTasksDetails.get(iCount + 7) %>
                        </td>
                        <td class="<%= strSLAMissedStyle %>"><%= arlTasksDetails.get(iCount + 8) %>
                        </td>
                        <td class="txtgrey" <%= sbETAHistory %>><%= strTicketETA %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlTasksDetails.get(iCount + 12) %>
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
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="30%" class="txtblackbold12">Ticket Description</td>
                        <td width="8%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Ticket Status</td>
                        <td width="8%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">SLA End Date</td>
                        <td width="8%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12" align="center">Ageing</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";
                        StringBuffer sbETAHistory;

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlBugsDetails.size(); iCount += 13)
                        {
                            sbETAHistory = new StringBuffer();
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strSLAMissedStyle = OOSLA_GREY_TEXT;
                            if (arlBugsDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlBugsDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                            strTicketETA = arlBugsDetails.get(iCount + 11);
                            if (strTicketETA != null && strTicketETA.length() > 0)
                            {
                                ArrayList<Date> arlETADates = new ArrayList();
                                SimpleDateFormat ETA_DISPLAY_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);

                                sbETAHistory.append("title=\"");
                                for (int intETACount = 0; intETACount < arlOpenTicketsETAHistory.size(); intETACount += 3)
                                {
                                    if (arlBugsDetails.get(iCount).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount)) &&
                                            arlBugsDetails.get(iCount + 1).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount + 1)))
                                    {
                                        arlETADates = (ArrayList<Date>) arlOpenTicketsETAHistory.get(intETACount + 2);
                                        for (Date objTemp : arlETADates)
                                        {
                                            sbETAHistory.append(ETA_DISPLAY_FORMAT.format(objTemp)).append("\n");
                                        }
                                    }
                                }
                                sbETAHistory.append("\"");
                            }

                            if (strTicketETA == null || strTicketETA.trim().length() == 0)
                            {
                                strTicketETA = strTicketETATBDTAG;
                            }

                            if (arlBugsDetails.get(iCount + 11) != null && arlBugsDetails.get(iCount + 11).length() > 0)
                            {
                                SimpleDateFormat ETA_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
                                SimpleDateFormat CURRENT_DATE_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);
                                Date ETADate = ETA_DATE_FORMAT.parse(arlBugsDetails.get(iCount + 11));
                                Date CurrentDate = CURRENT_DATE_FORMAT.parse(strCurrentDateDDMMYYYY);
                                if (CurrentDate.compareTo(ETADate) > 0)
                                {
                                    strTicketETA = "<span class=\"blink_me\" style=\"font-weight: bold;color: #FF0000;\">" + strTicketETA + "</span>";
                                }

                            }
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlBugsDetails.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlBugsDetails.get(iCount + 1) %>', '<%= arlBugsDetails.get(iCount + 1) %>')"
                                class="link"><%= arlBugsDetails.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlBugsDetails.get(iCount + 1) %>', '<%= arlBugsDetails.get(iCount + 1) %>')"
                                class="link"><%= arlBugsDetails.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlBugsDetails.get(iCount) %>','<%= arlBugsDetails.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlBugsDetails.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlBugsDetails.get(iCount + 6) %>
                        </td>
                        <td class="txtgrey"><%= arlBugsDetails.get(iCount + 7) %>
                        </td>
                        <td class="<%= strSLAMissedStyle %>"><%= arlBugsDetails.get(iCount + 8) %>
                        </td>
                        <td class="txtgrey" <%= sbETAHistory %>><%= strTicketETA %>
                        </td>
                        <td class="txtgrey" align="center"><%= arlBugsDetails.get(iCount + 12) %>
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
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
