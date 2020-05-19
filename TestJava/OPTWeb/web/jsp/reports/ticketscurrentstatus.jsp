<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlNotStartedTicketsCurrentStatus = (ArrayList) request.getAttribute("NotStartedTicketsCurrentStatus");
    ArrayList<String> arlInProgressTicketsCurrentStatus = (ArrayList) request.getAttribute("InProgressTicketsCurrentStatus");
    ArrayList<String> arlOnHoldTicketsCurrentStatus = (ArrayList) request.getAttribute("OnHoldTicketsCurrentStatus");

    int intTaskNotStarted = 0;
    int intTaskInProgress = 0;
    int intTaskOnHold = 0;

    int intBugNotStarted = 0;
    int intBugInProgress = 0;
    int intBugOnHold = 0;

    if (arlNotStartedTicketsCurrentStatus != null && !arlNotStartedTicketsCurrentStatus.isEmpty())
    {
        for (int intCount = 0; intCount < arlNotStartedTicketsCurrentStatus.size(); intCount += 7)
        {
            if (arlNotStartedTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                intTaskNotStarted++;
            }
            else if (arlNotStartedTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                intBugNotStarted++;
            }

        }
    }
    if (arlInProgressTicketsCurrentStatus != null && !arlInProgressTicketsCurrentStatus.isEmpty())
    {
        for (int intCount = 0; intCount < arlInProgressTicketsCurrentStatus.size(); intCount += 7)
        {
            if (arlInProgressTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                intTaskInProgress++;
            }
            else if (arlInProgressTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                intBugInProgress++;
            }

        }
    }
    if (arlOnHoldTicketsCurrentStatus != null && !arlOnHoldTicketsCurrentStatus.isEmpty())
    {
        for (int intCount = 0; intCount < arlOnHoldTicketsCurrentStatus.size(); intCount += 7)
        {
            if (arlOnHoldTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.TASK))
            {
                intTaskOnHold++;
            }
            else if (arlOnHoldTicketsCurrentStatus.get(intCount + 3).equalsIgnoreCase(AppConstants.BUG))
            {
                intBugOnHold++;
            }

        }
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function TicketCurrentStatusExcel() {
            showFullPageMask(true);
            MM_openBrWindow('loadTicketCurrentStatusExcel?ACCESS=RESTRICTED', 500, 1300);
        }


    </script>
</head>
<body onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>

    <table width="99%" align="center">
        <tr>
            <td width="25%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tickets Current Status&nbsp;
                                        <%
                                            if ((arlNotStartedTicketsCurrentStatus != null && !arlNotStartedTicketsCurrentStatus.isEmpty()) ||
                                                    (arlInProgressTicketsCurrentStatus != null && !arlInProgressTicketsCurrentStatus.isEmpty()) ||
                                                    (arlOnHoldTicketsCurrentStatus != null && !arlOnHoldTicketsCurrentStatus.isEmpty()))
                                            {
                                        %>
                                        <a href="JavaScript:TicketCurrentStatusExcel()"><img src="images/excel.jpg" border="0" alt=""></a>
                                        <%

                                            }
                                        %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="75%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" align="center">
        <tr>
            <td width="35%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Summary
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                                <tr class="LightBlueBandStyle">
                                    <td width="20%" class="txtblackbold12">Category</td>
                                    <td width="20%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="20%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="20%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="20%" class="txtblackbold12" align="center">Total</td>
                                </tr>

                                <tr class="LightGreyBandStyle">
                                    <td class="txtgrey">Tasks</td>
                                    <td class="txtgrey" align="center"><%= intTaskNotStarted %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTaskInProgress %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTaskOnHold %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTaskNotStarted + intTaskInProgress + intTaskOnHold %>
                                    </td>
                                </tr>

                                <tr class="DarkGreyBandStyle">
                                    <td class="txtgrey">Bugs</td>
                                    <td class="txtgrey" align="center"><%= intBugNotStarted %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intBugInProgress %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intBugOnHold %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intBugNotStarted + intBugInProgress + intBugOnHold %>
                                    </td>
                                </tr>

                                <tr class="LightGreyBandStyle">
                                    <td class="txtgrey">Total</td>
                                    <td class="txtgrey" align="center"><%= intTaskNotStarted + intBugNotStarted %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTaskInProgress + intBugInProgress %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTaskOnHold + intBugOnHold %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= intBugNotStarted + intBugInProgress + intBugOnHold + intTaskNotStarted + intTaskInProgress + intTaskOnHold %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="65%"></td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tickets - Not Started
                        </td>
                    </tr>
                </table>
                <%
                    if (arlNotStartedTicketsCurrentStatus != null && !arlNotStartedTicketsCurrentStatus.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 style="table-layout: fixed;">
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="5%" class="txtblackbold12">Category</td>
                        <td width="14%" class="txtblackbold12">Assignee</td>
                        <td width="14%" class="txtblackbold12">Comments By</td>
                        <td width="10%" class="txtblackbold12">Comments Date</td>
                        <td width="44%" class="txtblackbold12">Comments</td>
                    </tr>


                    <%
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String strBackGround = "";
                        String strTicketDescription;

                        for (int intCount = 0; intCount < arlNotStartedTicketsCurrentStatus.size(); intCount += 7)
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
                        <%
                            strTicketDescription = arlNotStartedTicketsCurrentStatus.get(intCount + 2);
                            strTicketDescription = strTicketDescription.replaceAll("\"", "");
                        %>
                        <td title="<%= strTicketDescription %>">
                            <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlNotStartedTicketsCurrentStatus.get(intCount) %>', '<%= arlNotStartedTicketsCurrentStatus.get(intCount) %>')"
                               class="link"><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount) %></pre>
                            </a>
                        </td>
                        <td><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount + 4) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount + 5) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlNotStartedTicketsCurrentStatus.get(intCount + 6) %></pre>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tickets - In Progress
                        </td>
                    </tr>
                </table>
                <%
                    if (arlInProgressTicketsCurrentStatus != null && !arlInProgressTicketsCurrentStatus.isEmpty())
                    {
                        int intCounter = 0;
                        String strBackGround = "";

                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 style="table-layout: fixed;">
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="5%" class="txtblackbold12">Category</td>
                        <td width="14%" class="txtblackbold12">Assignee</td>
                        <td width="14%" class="txtblackbold12">Comments By</td>
                        <td width="10%" class="txtblackbold12">Comments Date</td>
                        <td width="44%" class="txtblackbold12">Comments</td>
                    </tr>


                    <%
                        int intSerialNo = 0;
                        String strTicketDescription;
                        for (int intCount = 0; intCount < arlInProgressTicketsCurrentStatus.size(); intCount += 7)
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
                        <%
                            strTicketDescription = arlInProgressTicketsCurrentStatus.get(intCount + 2);
                            strTicketDescription = strTicketDescription.replaceAll("\"", "");
                        %>
                        <td title="<%= strTicketDescription %>">
                            <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlInProgressTicketsCurrentStatus.get(intCount) %>', '<%= arlInProgressTicketsCurrentStatus.get(intCount) %>')"
                               class="link"><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount) %></pre>
                            </a>
                        </td>
                        <td><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount + 4) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount + 5) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInProgressTicketsCurrentStatus.get(intCount + 6) %></pre>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tickets - On Hold
                        </td>
                    </tr>
                </table>
                <%
                    if (arlOnHoldTicketsCurrentStatus != null && !arlOnHoldTicketsCurrentStatus.isEmpty())
                    {
                        int intCounter = 0;
                        String strBackGround = "";
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 style="table-layout: fixed;">
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="5%" class="txtblackbold12">Category</td>
                        <td width="14%" class="txtblackbold12">Assignee</td>
                        <td width="14%" class="txtblackbold12">Comments By</td>
                        <td width="10%" class="txtblackbold12">Comments Date</td>
                        <td width="44%" class="txtblackbold12">Comments</td>
                    </tr>


                    <%
                        int intSerialNo = 0;
                        String strTicketDescription;
                        for (int intCount = 0; intCount < arlOnHoldTicketsCurrentStatus.size(); intCount += 7)
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
                        <%
                            strTicketDescription = arlOnHoldTicketsCurrentStatus.get(intCount + 2);
                            strTicketDescription = strTicketDescription.replaceAll("\"", "");
                        %>
                        <td title="<%= strTicketDescription %>">
                            <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlOnHoldTicketsCurrentStatus.get(intCount) %>', '<%= arlOnHoldTicketsCurrentStatus.get(intCount) %>')"
                               class="link"><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount) %></pre>
                            </a>
                        </td>
                        <td><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount + 4) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount + 5) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlOnHoldTicketsCurrentStatus.get(intCount + 6) %></pre>
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

</form>
</body>
</html>
