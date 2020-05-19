<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<Object> arlTicketsSummary = (ArrayList) request.getAttribute("TicketsSummary");
    String strDashBoardName = AppUtil.checkNull((String) request.getAttribute("DashBoardName"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    String strSelected = "";
    String strSLAMissedStyle = "";

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function TicketStatus() {
            document.OPTForm.action = "TicketStatus";
            frmReadSubmit();
        }

        function TStrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                TicketStatus();
            }
        }

    </script>
</head>
<body onload="document.OPTForm.Assignee.focus()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidAssignee" value="<%= strDashBoardName %>">
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" align="center">
        <tr>
            <td width="40%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Select Assignee for Current Tickets Status
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                                <tr>
                                    <td width="100%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="15%" class="txtgreybold">&nbsp;&nbsp;Assignee</td>
                                                <td width="40%">
                                                    <select name="Assignee" class="txtgrey" onkeypress="TStrackEnterKey(event)">
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
                                                <td width="45%" class="txtgreybold">
                                                    <input type="button" class="myButton" value="Search" onclick="JavaScript:TicketStatus()"
                                                           id="buttonId0"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="60%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strDashBoardName.length() > 0)
        {
    %>

    <%
        if (arlTicketsSummary != null && !arlTicketsSummary.isEmpty())
        {
            for (int intFirstCount = 0; intFirstCount < arlTicketsSummary.size(); intFirstCount += 2)
            {
                String strAssigneeName = (String) arlTicketsSummary.get(intFirstCount);
                ArrayList arlIndividualTickets = (ArrayList) arlTicketsSummary.get(intFirstCount + 1);
    %>

    <%
        for (int intSecondCount = 0; intSecondCount < arlIndividualTickets.size(); intSecondCount += 9)
        {
            ArrayList arlCommentsHistory = (ArrayList) arlIndividualTickets.get(intSecondCount + 7);
            ArrayList arlTicketHistory = (ArrayList) arlIndividualTickets.get(intSecondCount + 8);
            strSLAMissedStyle = OOSLA_GREY_TEXT;
            if (((String) arlIndividualTickets.get(intSecondCount + 6)).equalsIgnoreCase("1"))
            {
                strSLAMissedStyle = OOSLA_RED_TEXT;
            }

    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr>
                                    <td class="DarkBlueBandStyleText14">Ticket Details - <%= strAssigneeName %>
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0>
                                <tr>
                                    <td width="15%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Ticket Id</td>
                                                <td width="60%" class="txtgrey"><a
                                                        href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlIndividualTickets.get(intSecondCount) %>', '<%= arlIndividualTickets.get(intSecondCount) %>')"
                                                        class="link"><%= arlIndividualTickets.get(intSecondCount) %>
                                                </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="85%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="15%" class="txtgreybold">Ticket Description</td>
                                                <td width="85%" class="txtgrey"><a
                                                        href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlIndividualTickets.get(intSecondCount) %>', '<%= arlIndividualTickets.get(intSecondCount) %>')"
                                                        class="link"><%= arlIndividualTickets.get(intSecondCount + 1) %>
                                                </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0>
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="30%" class="txtgreybold">Ticket Category</td>
                                                <td width="70%"
                                                    class="txtgrey"><%= arlIndividualTickets.get(intSecondCount + 2) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="30%" class="txtgreybold">Ticket Status</td>
                                                <td width="70%"
                                                    class="txtgrey"><%= arlIndividualTickets.get(intSecondCount + 3) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="30%" class="txtgreybold">Received Date</td>
                                                <td width="70%"
                                                    class="txtgrey"><%= arlIndividualTickets.get(intSecondCount + 4) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                            <tr class="LightBandStyle">
                                                <td width="30%" class="txtgreybold">SLA End Date</td>
                                                <td width="70%"
                                                    class="<%= strSLAMissedStyle %>"><%= arlIndividualTickets.get(intSecondCount + 5) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr>
                                    <td class="DarkBlueBandStyleText14">Comments History</td>
                                </tr>
                            </table>
                            <%
                                if (arlCommentsHistory != null && !arlCommentsHistory.isEmpty())
                                {
                            %>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="15%" class="txtblackbold12">Comments Category</td>
                                    <td width="55%" class="txtblackbold12">Comments</td>
                                    <td width="15%" class="txtblackbold12">Comments Date</td>
                                    <td width="15%" class="txtblackbold12">Comments By</td>
                                </tr>

                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int intThirdCount = 0; intThirdCount < arlCommentsHistory.size(); intThirdCount += 4)
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
                                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(intThirdCount) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(intThirdCount + 1) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(intThirdCount + 2) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(intThirdCount + 3) %></pre>
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

                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr>
                                    <td class="DarkBlueBandStyleText14">Ticket Progress History</td>
                                </tr>
                            </table>
                            <%
                                if (arlTicketHistory != null && !arlTicketHistory.isEmpty())
                                {
                            %>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="15%" class="txtblackbold12">Start Date</td>
                                    <td width="15%" class="txtblackbold12">End Date</td>
                                    <td width="15%" class="txtblackbold12">Assignee</td>
                                    <td width="55%" class="txtblackbold12">Comments</td>
                                </tr>


                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int intFourthCount = 0; intFourthCount < arlTicketHistory.size(); intFourthCount += 5)
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
                                    <td><pre class="txtgrey"><%= arlTicketHistory.get(intFourthCount) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlTicketHistory.get(intFourthCount + 4) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlTicketHistory.get(intFourthCount + 2) %></pre>
                                    </td>
                                    <td><pre class="txtgrey"><%= arlTicketHistory.get(intFourthCount + 3) %></pre>
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
            </td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>
    <%
        }
    %>
    <%
        }
    %>

    <%
    }
    else
    {
    %>
    <%@ include file="../common/nodataavailable.jsp" %>
    <%
        }
    %>
    <%
        }
    %>


</form>
</body>
</html>
