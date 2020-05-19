<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlSLAMissedTickets = (ArrayList) request.getAttribute("SLAMissedTickets");
    boolean blnTasksAvailable = false;
    boolean blnBugsAvailable = false;
    String strSelected = "";
    int intTaskCount = 0;
    int intBugsCount = 0;
    int intTotalCount = 0;
    if (arlSLAMissedTickets != null && !arlSLAMissedTickets.isEmpty())
    {
        for (int iCount = 0; iCount < arlSLAMissedTickets.size(); iCount += 10)
        {
            if (arlSLAMissedTickets.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                intTaskCount++;
            }
        }
        for (int iCount = 0; iCount < arlSLAMissedTickets.size(); iCount += 10)
        {
            if (arlSLAMissedTickets.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                intBugsCount++;
            }
        }
    }
    if (intTaskCount > 0)
    {
        blnTasksAvailable = true;
    }
    if (intBugsCount > 0)
    {
        blnBugsAvailable = true;
    }
    intTotalCount = intTaskCount + intBugsCount;
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadSLAMissedTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }
    </script>
</head>
<body onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidTaskRefNo">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" class="DarkBlueBandStyleText14">List of OOSLA Tickets</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" align="center">
        <tr>
            <td width="15%">
                <table width="100%" cellpadding=2 cellspacing=2 class="TableBorder1Pix" align="left">
                    <tr>
                        <td colspan="3" width="100%" class="DarkBlueBandStyleText14">&nbsp;Summary
                        </td>
                    </tr>
                    <tr class="LightBlueBandStyle">
                        <td class="txtblackbold12" align="center">Tasks</td>
                        <td class="txtblackbold12" align="center">Bugs</td>
                        <td class="txtblackbold12" align="center">Total</td>
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
            <td width="85%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (blnTasksAvailable || blnBugsAvailable)
        {
    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Tasks
                        </td>
                    </tr>
                </table>
                <%
                    if (blnTasksAvailable)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="46%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Ticket Status</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String strBackGround = "";


                        for (int iCount = 0; iCount < arlSLAMissedTickets.size(); iCount += 10)
                        {
                            if (arlSLAMissedTickets.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK))
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
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSLAMissedTickets.get(iCount + 1) %>', '<%= arlSLAMissedTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSLAMissedTickets.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSLAMissedTickets.get(iCount + 1) %>', '<%= arlSLAMissedTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSLAMissedTickets.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlSLAMissedTickets.get(iCount) %>','<%= arlSLAMissedTickets.get(iCount + 1) %>')"><%= arlSLAMissedTickets.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 6) %>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 8) %>
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
    <%@ include file="../common/onespace.jsp" %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Bugs
                        </td>
                    </tr>
                </table>
                <%
                    if (blnBugsAvailable)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="46%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="7%" class="txtblackbold12">Ticket Status</td>
                        <td width="7%" class="txtblackbold12">Received Date</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                    </tr>
                    <%
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlSLAMissedTickets.size(); iCount += 10)
                        {
                            if (arlSLAMissedTickets.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
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
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSLAMissedTickets.get(iCount + 1) %>', '<%= arlSLAMissedTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSLAMissedTickets.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSLAMissedTickets.get(iCount + 1) %>', '<%= arlSLAMissedTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSLAMissedTickets.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlSLAMissedTickets.get(iCount) %>','<%= arlSLAMissedTickets.get(iCount + 1) %>')"><%= arlSLAMissedTickets.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 6) %>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlSLAMissedTickets.get(iCount + 8) %>
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
    else
    {
    %>
    <%@ include file="../common/nodataavailable.jsp" %>
    <%
        }

    %>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
