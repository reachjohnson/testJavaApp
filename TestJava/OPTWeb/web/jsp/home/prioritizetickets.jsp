<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    int intCounter = 0;
    String strBackGround = "";

    ArrayList<String> arlTicketsForCommentsUpdate = (ArrayList) request.getAttribute("TicketsForCommentsUpdate");
    String strPrevWorkingDate = AppUtil.checkNull((String) request.getAttribute("CommentsDate"));
    ArrayList<String> arlInternalTicketsForETAUpdate = (ArrayList) request.getAttribute("InternalTicketsForETAUpdate");
    ArrayList<String> arlRQATicketsForETAUpdate = (ArrayList) request.getAttribute("RQATicketsForETAUpdate");
    ArrayList<Object> arlOpenTicketsETAHistory = (ArrayList) request.getAttribute("OpenTicketsETAHistory");

    ArrayList<String> arlBugsForETAUpdate = (ArrayList) request.getAttribute("BugsForETAUpdate");
    ArrayList<String> arlCriticalTasksForETAUpdate = (ArrayList) request.getAttribute("CriticalTasksForETAUpdate");
    ArrayList<String> arlMajorTasksForETAUpdate = (ArrayList) request.getAttribute("MajorTasksForETAUpdate");
    ArrayList<String> arlTopAgeingTasksForETAUpdate = (ArrayList) request.getAttribute("TopAgeingTasksForETAUpdate");
    ArrayList<String> arlETACrossedTickets = (ArrayList) request.getAttribute("ETACrossedTickets");

    String strTicketETA;
    StringBuffer sbETAHistory;
    StringBuffer sbETAHistoryDisplay;
    int intETAUpdatedCount = 0;
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function updateTicket(TicketCategory, RefNo, TicketId, ETAUpdatedCount, ETAHistoryDisplay) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            if (ETA_RESCHEDULE_MAX > 0 && ETAUpdatedCount >= ETA_RESCHEDULE_MAX) {
                OPTDialog("ETA already scheduled " + ETAUpdatedCount + " times for this " + TicketCategory + ".  Please reach out to your Lead.\n\n" + ETAHistoryDisplay);
            }
            else {
                if (TicketCategory == "Task") {
                    document.OPTForm.action = "loadUpdateTask";
                }
                if (TicketCategory == "Bug") {
                    document.OPTForm.action = "loadUpdateBug";
                }
                frmWriteSubmit();
            }
        }

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }

        function updateComments(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadAddCommentsDummy?ACCESS=RESTRICTED", 600, 1300);
        }

        function updateInternalTicket(RefNo, TicketId, Assignee) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidOriginalAssignee.value = Assignee;
            document.OPTForm.action = "loadUpdateIT";
            frmWriteSubmit();
        }

        function updateRQATicket(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.action = "loadRQAUpdateBug";
            frmWriteSubmit();
        }

    </script>

</head>
<body onload="displaymessage()" onfocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidFromPage" value="HomePage">
    <input type="hidden" name="hidOriginalAssignee">
    <input type="hidden" name="hidPreWorkingDay" value="<%= strPrevWorkingDate %>">

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="txtblackbold12" align="left">&nbsp;Note : You will be redirected To Your Dashboard Once You
                Completed The Below Actions.
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%
        if (arlBugsForETAUpdate != null && !arlBugsForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Bugs</td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Ageing</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlBugsForETAUpdate.size(); iCount += 7)
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
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateTicket('<%= arlBugsForETAUpdate.get(iCount + 1) %>', '<%= arlBugsForETAUpdate.get(iCount) %>',
                    '<%= arlBugsForETAUpdate.get(iCount + 2) %>', '0', '')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlBugsForETAUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlBugsForETAUpdate.get(iCount + 2) %>', '<%= arlBugsForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlBugsForETAUpdate.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlBugsForETAUpdate.get(iCount + 2) %>', '<%= arlBugsForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlBugsForETAUpdate.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlBugsForETAUpdate.get(iCount) %>','<%= arlBugsForETAUpdate.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlBugsForETAUpdate.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlBugsForETAUpdate.get(iCount + 5) %>
                        </td>
                        <td class="txtgrey"><%= arlBugsForETAUpdate.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlCriticalTasksForETAUpdate != null && !arlCriticalTasksForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Critical Tasks</td>
                    </tr>
                </table>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Ageing</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlCriticalTasksForETAUpdate.size(); iCount += 7)
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
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateTicket('<%= arlCriticalTasksForETAUpdate.get(iCount + 1) %>', '<%= arlCriticalTasksForETAUpdate.get(iCount) %>', '<%= arlCriticalTasksForETAUpdate.get(iCount + 2) %>', '0', '')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlCriticalTasksForETAUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCriticalTasksForETAUpdate.get(iCount + 2) %>', '<%= arlCriticalTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlCriticalTasksForETAUpdate.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCriticalTasksForETAUpdate.get(iCount + 2) %>', '<%= arlCriticalTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlCriticalTasksForETAUpdate.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlCriticalTasksForETAUpdate.get(iCount) %>','<%= arlCriticalTasksForETAUpdate.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlCriticalTasksForETAUpdate.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlCriticalTasksForETAUpdate.get(iCount + 5) %>
                        </td>
                        <td class="txtgrey"><%= arlCriticalTasksForETAUpdate.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlMajorTasksForETAUpdate != null && !arlMajorTasksForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Major Tasks</td>
                    </tr>
                </table>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Ageing</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlMajorTasksForETAUpdate.size(); iCount += 7)
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
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateTicket('<%= arlMajorTasksForETAUpdate.get(iCount + 1) %>', '<%= arlMajorTasksForETAUpdate.get(iCount) %>', '<%= arlMajorTasksForETAUpdate.get(iCount + 2) %>', '0', '')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlMajorTasksForETAUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlMajorTasksForETAUpdate.get(iCount + 2) %>', '<%= arlMajorTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlMajorTasksForETAUpdate.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlMajorTasksForETAUpdate.get(iCount + 2) %>', '<%= arlMajorTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlMajorTasksForETAUpdate.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlMajorTasksForETAUpdate.get(iCount) %>','<%= arlMajorTasksForETAUpdate.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlMajorTasksForETAUpdate.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlMajorTasksForETAUpdate.get(iCount + 5) %>
                        </td>
                        <td class="txtgrey"><%= arlMajorTasksForETAUpdate.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlTopAgeingTasksForETAUpdate != null && !arlTopAgeingTasksForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Top Ageing Tasks
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Ageing</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlTopAgeingTasksForETAUpdate.size(); iCount += 7)
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
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateTicket('<%= arlTopAgeingTasksForETAUpdate.get(iCount + 1) %>', '<%= arlTopAgeingTasksForETAUpdate.get(iCount) %>', '<%= arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>', '0', '')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>', '<%= arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>', '<%= arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlTopAgeingTasksForETAUpdate.get(iCount) %>','<%= arlTopAgeingTasksForETAUpdate.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 5) %>
                        </td>
                        <td class="txtgrey"><%= arlTopAgeingTasksForETAUpdate.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlETACrossedTickets != null && !arlETACrossedTickets.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Tickets Which
                            Crossed ETA
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Ageing</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlETACrossedTickets.size(); iCount += 7)
                        {
                            strTicketETA = "";
                            sbETAHistory = new StringBuffer();
                            sbETAHistoryDisplay = new StringBuffer();
                            intETAUpdatedCount = 0;

                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strTicketETA = arlETACrossedTickets.get(iCount + 5);
                            if (strTicketETA != null && strTicketETA.length() > 0 && arlOpenTicketsETAHistory != null && !arlOpenTicketsETAHistory.isEmpty())
                            {
                                ArrayList<Date> arlETADates = new ArrayList();
                                int intSlNo = 0;
                                SimpleDateFormat ETA_DISPLAY_FORMAT = new SimpleDateFormat(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
                                sbETAHistory.append("title=\"");
                                for (int intETACount = 0; intETACount < arlOpenTicketsETAHistory.size(); intETACount += 3)
                                {
                                    if (arlETACrossedTickets.get(iCount).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount)) &&
                                            arlETACrossedTickets.get(iCount + 2).equalsIgnoreCase((String) arlOpenTicketsETAHistory.get(intETACount + 1)))
                                    {
                                        arlETADates = (ArrayList<Date>) arlOpenTicketsETAHistory.get(intETACount + 2);
                                        for (Date objTemp : arlETADates)
                                        {
                                            sbETAHistory.append(ETA_DISPLAY_FORMAT.format(objTemp)).append("\n");
                                            sbETAHistoryDisplay.append(++intSlNo).append(") ").append(ETA_DISPLAY_FORMAT.format(objTemp)).append("<br>");
                                        }
                                        intETAUpdatedCount = arlETADates.size();
                                    }
                                }
                                sbETAHistory.append("\"");
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateTicket('<%= arlETACrossedTickets.get(iCount + 1) %>', '<%= arlETACrossedTickets.get(iCount) %>',
                    '<%= arlETACrossedTickets.get(iCount + 2) %>', '<%= intETAUpdatedCount%>', '<%= sbETAHistoryDisplay.toString() %>')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlETACrossedTickets.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlETACrossedTickets.get(iCount + 2) %>', '<%= arlETACrossedTickets.get(iCount + 2) %>')"
                                class="link"><%= arlETACrossedTickets.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlETACrossedTickets.get(iCount + 2) %>', '<%= arlETACrossedTickets.get(iCount + 2) %>')"
                                class="link"><%= arlETACrossedTickets.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlETACrossedTickets.get(iCount) %>','<%= arlETACrossedTickets.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlETACrossedTickets.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey" <%= sbETAHistory %>><%= strTicketETA %>
                        </td>
                        <td class="txtgrey"><%= arlETACrossedTickets.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>


    <%
        if (arlTicketsForCommentsUpdate != null && !arlTicketsForCommentsUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Daily Status Not Updated On
                            <%= AppUtil.convertDate(strPrevWorkingDate, AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY, AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY) %>
                            For Below Tickets
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="10%" class="txtblackbold12">Ticket Category</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="52%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Task Priority</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                        <td width="5%" class="txtblackbold12">Status</td>
                    </tr>
                    <%
                        intCounter = 0;
                        strBackGround = "";

                        for (int iCount = 0; iCount < arlTicketsForCommentsUpdate.size(); iCount += 7)
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
                        <td class="txtgrey" align="center"><a
                                href="JavaScript:updateComments('<%= arlTicketsForCommentsUpdate.get(iCount) %>', '<%= arlTicketsForCommentsUpdate.get(iCount + 2) %>')"
                                class="link"><img src="images/update_new.jpg" border="0">
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTicketsForCommentsUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketsForCommentsUpdate.get(iCount + 2) %>', '<%= arlTicketsForCommentsUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlTicketsForCommentsUpdate.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketsForCommentsUpdate.get(iCount + 2) %>', '<%= arlTicketsForCommentsUpdate.get(iCount + 2) %>')"
                                class="link"><%= arlTicketsForCommentsUpdate.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlTicketsForCommentsUpdate.get(iCount) %>','<%= arlTicketsForCommentsUpdate.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlTicketsForCommentsUpdate.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlTicketsForCommentsUpdate.get(iCount + 5) %>
                        </td>
                        <td class="txtgrey"><%= arlTicketsForCommentsUpdate.get(iCount + 6) %>
                        </td>

                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlInternalTicketsForETAUpdate != null && !arlInternalTicketsForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left" >
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below Internal
                            Tickets Which Crossed ETA / ETA Not Updated
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="15%" class="txtblackbold12">Ticket ID</td>
                        <td width="24%" class="txtblackbold12">Ticket Description</td>
                        <td width="52%" class="txtblackbold12">Action Required</td>
                        <td width="6%" class="txtblackbold12">ETA</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlInternalTicketsForETAUpdate.size(); iCount += 6)
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
                        <td class="txtgrey" align="center">
                            <a href="JavaScript:updateInternalTicket('<%= arlInternalTicketsForETAUpdate.get(iCount) %>', '<%= arlInternalTicketsForETAUpdate.get(iCount+1) %>', '<%= arlInternalTicketsForETAUpdate.get(iCount + 5) %>')"
                               class="link"><img src="images/update_new.jpg" border="0"></a>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTicketsForETAUpdate.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTicketsForETAUpdate.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTicketsForETAUpdate.get(iCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTicketsForETAUpdate.get(iCount + 4) %></pre>
                        </td>
                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

    <%
        if (arlRQATicketsForETAUpdate != null && !arlRQATicketsForETAUpdate.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%%" cellpadding=0 cellspacing=0 border=0 align="left">
                    <tr>
                        <td class="DarkBlueBandStyleText14" align="left">&nbsp;Update ETA For Below RQA Tickets
                            Which Crossed ETA / ETA Not Updated
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">Update</td>
                        <td width="15%" class="txtblackbold12">Bug ID</td>
                        <td width="52%" class="txtblackbold12">Bug Description</td>
                        <td width="10%" class="txtblackbold12">Bug Priority</td>
                        <td width="10%" class="txtblackbold12">Received Date</td>
                        <td width="10%" class="txtblackbold12">ETA</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlRQATicketsForETAUpdate.size(); iCount += 10)
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
                        <td class="txtgrey" align="center">
                            <a href="JavaScript:updateRQATicket('<%= arlRQATicketsForETAUpdate.get(iCount) %>', '<%= arlRQATicketsForETAUpdate.get(iCount+1) %>')"
                               class="link"><img src="images/update_new.jpg" border="0"></a>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketsForETAUpdate.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketsForETAUpdate.get(iCount + 2) %>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketsForETAUpdate.get(iCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketsForETAUpdate.get(iCount + 6) %>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketsForETAUpdate.get(iCount + 9) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <%
        }
    %>

</form>
</body>
</html>
