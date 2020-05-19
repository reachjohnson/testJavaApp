<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<Object> arlOpenTicketsCount = (ArrayList) request.getAttribute("OpenTicketsCount");
    ArrayList<String> arlTriagingResourceAllocation = (ArrayList) request.getAttribute("TRIAGINGRESOURCEALLOCATION");
    ArrayList<String> arlFixingResourceAllocation = (ArrayList) request.getAttribute("FIXINGRESOURCEALLOCATION");
    ArrayList<String> arlFeatureResourceAllocation = (ArrayList) request.getAttribute("FEATURERESOURCEALLOCATION");
    ArrayList<String> arlQAResourceAllocation = (ArrayList) request.getAttribute("QARESOURCEALLOCATION");
    ArrayList<String> arlRQAResourceAllocation = (ArrayList) request.getAttribute("RQAESOURCEALLOCATION");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
</head>
<body onload="displaymessage();document.OPTForm.HolidayDate.focus();">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidHolidayRefNo">
    <input type="hidden" name="hidAction">
    <%
        if (arlOpenTicketsCount != null && !arlOpenTicketsCount.isEmpty())
        {
    %>
    <table width="99%" align="center">
        <tr>
            <td width="50%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Resource Allocation Summary
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="38%" class="txtfirbrickbold13" rowspan="2">Summary</td>
                                    <td width="8%" class="txtfirbrickbold13" rowspan="2" align="center">Bugs</td>
                                    <td width="45%" colspan="6" class="txtfirbrickbold13" align="center">Tasks</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td class="txtblackbold12" align="center">Critical</td>
                                    <td class="txtblackbold12" align="center">Major</td>
                                    <td class="txtblackbold12" align="center">Normal</td>
                                    <td class="txtblackbold12" align="center">Minor</td>
                                    <td class="txtblackbold12" align="center">Trivial</td>
                                    <td class="txtfirbrickbold13" align="center">Total</td>
                                </tr>
                                <%
                                    String strAssigneeName = "";
                                    ArrayList<String> arlOpenTicketsCountByAssignee = new ArrayList<String>();
                                    int intBugsCount = 0;
                                    int intCriticalTasksCount = 0;
                                    int intMajorTasksCount = 0;
                                    int intNormalTasksCount = 0;
                                    int intMinorTasksCount = 0;
                                    int intTrivialTasksCount = 0;

                                    int intBugsTotal = 0;
                                    int intMajorTasksTotal = 0;
                                    int intCriticalTasksTotal = 0;
                                    int intNormalTasksTotal = 0;
                                    int intMinorTasksTotal = 0;
                                    int intTrivialTasksTotal = 0;
                                    int intTasksTotal = 0;
                                    int intBGCounter = 0;
                                    String strBackGround = "";

                                    for (int intCounter = 0; intCounter < arlOpenTicketsCount.size(); intCounter += 2)
                                    {
                                        if (intBGCounter % 2 == 0)
                                        {
                                            strBackGround = "LightGreyBandStyle";
                                        }
                                        else
                                        {
                                            strBackGround = "DarkGreyBandStyle";
                                        }

                                        strAssigneeName = (String) arlOpenTicketsCount.get(intCounter);
                                        arlOpenTicketsCountByAssignee = (ArrayList) arlOpenTicketsCount.get(intCounter + 1);
                                        intBugsCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(0));
                                        intCriticalTasksCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(1));
                                        intMajorTasksCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(2));
                                        intNormalTasksCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(3));
                                        intMinorTasksCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(4));
                                        intTrivialTasksCount = Integer.parseInt(arlOpenTicketsCountByAssignee.get(5));
                                        intBugsTotal += intBugsCount;
                                        intCriticalTasksTotal += intCriticalTasksCount;
                                        intMajorTasksTotal += intMajorTasksCount;
                                        intNormalTasksTotal += intNormalTasksCount;
                                        intMinorTasksTotal += intMinorTasksCount;
                                        intTrivialTasksTotal += intTrivialTasksCount;

                                %>
                                <tr class="<%= strBackGround %>">
                                    <td class="txtgreybold"><%= strAssigneeName %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intBugsCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intCriticalTasksCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intMajorTasksCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intNormalTasksCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intMinorTasksCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intTrivialTasksCount %>
                                    </td>
                                    <td class="txtgreybold"
                                        align="center"><%= intCriticalTasksCount + intMajorTasksCount + intNormalTasksCount + intMinorTasksCount + intTrivialTasksCount %>
                                    </td>
                                </tr>
                                <%
                                        intBGCounter++;
                                    }
                                %>

                                <tr class="LightBlueBandStyle">
                                    <td class="txtfirbrickbold13">Overall Total</td>
                                    <td class="txtfirbrickbold13" align="center"><%= intBugsTotal %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intCriticalTasksTotal %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intMajorTasksTotal %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intNormalTasksTotal %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intMinorTasksTotal %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intTrivialTasksTotal %>
                                    </td>
                                    <td class="txtfirbrickbold13"
                                        align="center"><%= intCriticalTasksTotal + intMajorTasksTotal + intNormalTasksTotal + intMinorTasksTotal + intTrivialTasksTotal %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="50%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (arlTriagingResourceAllocation != null && !arlTriagingResourceAllocation.isEmpty())
        {
            int intNoOfRows = arlTriagingResourceAllocation.size() / 11;
    %>
    <table width="99%" align="center">
        <tr>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Triaging Team Resource
                                        Allocation As Of Today
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtfirbrickbold13" rowspan="2">Assignee</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Total</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Tasks Status
                                    </td>
                                    <td width="5%" class="txtgreenbold" rowspan="<%= intNoOfRows + 2 %>">&nbsp;</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Bug Status</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Leave</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                </tr>
                                <%
                                    String strClass = "txtgrey";

                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int iCount = 0; iCount < arlTriagingResourceAllocation.size(); iCount += 11)
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
                                    <td class="txtgrey"><%= arlTriagingResourceAllocation.get(iCount) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 10) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 3) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlTriagingResourceAllocation.get(iCount + 4).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 4) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 5) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 6) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 7) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlTriagingResourceAllocation.get(iCount + 8).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 8) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlTriagingResourceAllocation.get(iCount + 9) %>
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
            </td>
            <td width="10%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (arlFixingResourceAllocation != null && !arlFixingResourceAllocation.isEmpty())
        {
            int intNoOfRows = arlTriagingResourceAllocation.size() / 11;
    %>
    <table width="99%" align="center">
        <tr>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Fixing Team Resource
                                        Allocation As Of Today
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtfirbrickbold13" rowspan="2">Assignee</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Total</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Tasks Status
                                    </td>
                                    <td width="5%" class="txtgreenbold" rowspan="<%= intNoOfRows + 2 %>">&nbsp;</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Bug Status</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Leave</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                </tr>
                                <%
                                    String strClass = "txtgrey";
                                    int intCounter = 0;
                                    String strBackGround = "";


                                    for (int iCount = 0; iCount < arlFixingResourceAllocation.size(); iCount += 11)
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
                                    <td class="txtgrey"><%= arlFixingResourceAllocation.get(iCount) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 10) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 3) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlFixingResourceAllocation.get(iCount + 4).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 4) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 5) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 6) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 7) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlFixingResourceAllocation.get(iCount + 8).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 8) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFixingResourceAllocation.get(iCount + 9) %>
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
            </td>
            <td width="10%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (arlRQAResourceAllocation != null && !arlRQAResourceAllocation.isEmpty())
        {
            int intNoOfRows = arlTriagingResourceAllocation.size() / 11;
    %>
    <table width="99%" align="center">
        <tr>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Team Resource Allocation
                                        As Of Today
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtfirbrickbold13" rowspan="2">Assignee</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Total</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Tasks Status
                                    </td>
                                    <td width="5%" class="txtgreenbold" rowspan="<%= intNoOfRows + 2 %>">&nbsp;</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Bug Status</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Leave</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                </tr>
                                <%
                                    String strClass = "txtgrey";
                                    int intCounter = 0;
                                    String strBackGround = "";


                                    for (int iCount = 0; iCount < arlRQAResourceAllocation.size(); iCount += 11)
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
                                    <td class="txtgrey"><%= arlRQAResourceAllocation.get(iCount) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 10) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 3) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlRQAResourceAllocation.get(iCount + 4).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlRQAResourceAllocation.get(iCount + 4) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 5) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 6) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 7) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlRQAResourceAllocation.get(iCount + 8).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlRQAResourceAllocation.get(iCount + 8) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlRQAResourceAllocation.get(iCount + 9) %>
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
            </td>
            <td width="10%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (arlFeatureResourceAllocation != null && !arlFeatureResourceAllocation.isEmpty())
        {
            int intNoOfRows = arlTriagingResourceAllocation.size() / 11;
    %>
    <table width="99%" align="center">
        <tr>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Feature Team Resource
                                        Allocation As Of Today
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtfirbrickbold13" rowspan="2">Assignee</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Total</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Tasks Status
                                    </td>
                                    <td width="5%" class="txtgreenbold" rowspan="<%= intNoOfRows + 2 %>">&nbsp;</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Bug Status</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Leave</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                </tr>
                                <%
                                    String strClass = "txtgrey";
                                    int intCounter = 0;
                                    String strBackGround = "";


                                    for (int iCount = 0; iCount < arlFeatureResourceAllocation.size(); iCount += 11)
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
                                    <td class="txtgrey"><%= arlFeatureResourceAllocation.get(iCount) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 10) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 3) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlFeatureResourceAllocation.get(iCount + 4).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 4) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 5) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 6) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 7) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlFeatureResourceAllocation.get(iCount + 8).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 8) %>
                                    </td>
                                    <td class="txtgrey"
                                        align="center"><%= arlFeatureResourceAllocation.get(iCount + 9) %>
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
            </td>
            <td width="10%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (arlQAResourceAllocation != null && !arlQAResourceAllocation.isEmpty())
        {
            int intNoOfRows = arlTriagingResourceAllocation.size() / 11;
    %>
    <table width="99%" align="center">
        <tr>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;QA Team Resource Allocation
                                        As Of Today
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtfirbrickbold13" rowspan="2">Assignee</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Total</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Tasks Status
                                    </td>
                                    <td width="5%" class="txtgreenbold" rowspan="<%= intNoOfRows + 2 %>">&nbsp;</td>
                                    <td width="29%" colspan="4" class="txtfirbrickbold13" align="center">Bug Status</td>
                                    <td width="6%" class="txtfirbrickbold13" rowspan="2" align="center">Leave</td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                    <td width="7%" class="txtblackbold12" align="center">Not Started</td>
                                    <td width="7%" class="txtblackbold12" align="center">In Progress</td>
                                    <td width="7%" class="txtblackbold12" align="center">On Hold</td>
                                    <td width="8%" class="txtblackbold12" align="center">OOSLA</td>
                                </tr>
                                <%
                                    String strClass = "txtgrey";
                                    int intCounter = 0;
                                    String strBackGround = "";


                                    for (int iCount = 0; iCount < arlQAResourceAllocation.size(); iCount += 11)
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
                                    <td class="txtgrey"><%= arlQAResourceAllocation.get(iCount) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 10) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 3) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlQAResourceAllocation.get(iCount + 4).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlQAResourceAllocation.get(iCount + 4) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 5) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 6) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 7) %>
                                    </td>
                                    <%
                                        strClass = "txtgrey";
                                        if (!arlQAResourceAllocation.get(iCount + 8).equalsIgnoreCase("0"))
                                        {
                                            strClass = "txtredbold";
                                        }
                                    %>
                                    <td class="<%= strClass %>"
                                        align="center"><%= arlQAResourceAllocation.get(iCount + 8) %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= arlQAResourceAllocation.get(iCount + 9) %>
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
            </td>
            <td width="10%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%@ include file="../common/onespace.jsp" %>

</form>
</body>
</html>
