<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlRQAPhases = (ArrayList) request.getAttribute("RQAPhases");
    String strRQAPhase = AppUtil.checkNull((String) request.getAttribute("SelectedRQAPhase"));
    String strSelected = "";

    ArrayList<String> arlRQABugsCount = (ArrayList) request.getAttribute("RQABugsCount");
    ArrayList<String> arlRQABugsResolution = (ArrayList) request.getAttribute("RQABugsResolution");
    ArrayList<String> arlRQABugsMovedTeams = (ArrayList) request.getAttribute("RQABugsMovedTeams");
    ArrayList<String> arlRQAListOfBugs = (ArrayList) request.getAttribute("RQAListOfBugs");
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
                loadRQAMetrics();
            }
        }

        function viewRQATicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadRQATicketDetails?ACCESS=RESTRICTED", 700, 1300);
        }

        function loadRQAMetrics() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.RQAPhase.options.selectedIndex == 0) {
                    OPTDialog("Select RQA Phase", document.OPTForm.RQAPhase);
                    return;
                }
                document.OPTForm.action = "loadRQAMetrics";
                frmReadSubmit();
            }
        }
    </script>
</head>
<body onload="document.OPTForm.RQAPhase.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" align="center">
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Select RQA Phase to Generate Metrics
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
                                                <td width="40%" class="txtgreybold">&nbsp;&nbsp;RQA Phases</td>
                                                <td width="20%">
                                                    <select name="RQAPhase" class="txtgrey" onkeypress="CTtrackEnterKey(event)">
                                                        <option value="">Select</option>
                                                        <%
                                                            if (arlRQAPhases != null && !arlRQAPhases.isEmpty())
                                                            {
                                                                for (String rqaPhase : arlRQAPhases)
                                                                {
                                                                    strSelected = "";
                                                                    if (strRQAPhase.equalsIgnoreCase(rqaPhase))
                                                                    {
                                                                        strSelected = "selected";
                                                                    }
                                                        %>
                                                        <option value="<%= rqaPhase %>" <%= strSelected %>><%= rqaPhase %>
                                                        </option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                                <td width="40%" class="txtgreybold">
                                                    <input type="button" class="myButton" value="Search" onclick="JavaScript:loadRQAMetrics()"
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
            <td width="70%">&nbsp;</td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strRQAPhase.length() > 0)
        {
    %>
    <table width="99%" align="center">
        <tr>
            <td width="20%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Summary
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=1 cellspacing=1 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="90%" class="txtgreybold">No.Of Received Bugs</td>
                                    <td width="10%" class="txtgrey" align="center"><%= arlRQABugsCount.get(0) %>
                                    </td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="90%" class="txtgreybold">No.Of Closed Bugs</td>
                                    <td width="10%" class="txtgrey" align="center"><%= arlRQABugsCount.get(1) %>
                                    </td>
                                </tr>
                                <tr class="LightBlueBandStyle">
                                    <td width="90%" class="txtgreybold">No.Of Current Open Bugs</td>
                                    <td width="10%" class="txtgrey" align="center"><%= arlRQABugsCount.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="80%"></td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" align="center">
        <%
            if (arlRQABugsResolution != null && !arlRQABugsResolution.isEmpty())
            {
        %>
        <td width="30%" valign="top">
            <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                <tr>
                    <td width="100%" valign="top">
                        <table width="100%">
                            <tr>
                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs Resolution Types
                                </td>
                            </tr>
                        </table>
                        <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                            <tr class="LightBlueBandStyle">
                                <td width="80%" class="txtblackbold12">Resolution Type</td>
                                <td width="20%" class="txtblackbold12" align="center">Count</td>
                            </tr>
                            <%
                                int intCounter = 0;
                                String strBackGround = "";

                                for (int intCount = 0; intCount < arlRQABugsResolution.size(); intCount += 2)
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
                                <td class="txtgrey"><%= arlRQABugsResolution.get(intCount) %>
                                </td>
                                <td class="txtgrey" align="center"><%= arlRQABugsResolution.get(intCount + 1) %>
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
        <%
        }
        else
        {
        %>
        <td width="30%" valign="top">&nbsp;</td>
        <%
            }
        %>
        <td width="5%">&nbsp;</td>
        <%
            if (arlRQABugsMovedTeams != null && !arlRQABugsMovedTeams.isEmpty())
            {
        %>
        <td width="20%" valign="top">
            <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                <tr>
                    <td width="100%" valign="top">
                        <table width="100%">
                            <tr>
                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Moved To Other Teams
                                </td>
                            </tr>
                        </table>
                        <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                            <tr class="LightBlueBandStyle">
                                <td width="80%" class="txtblackbold12">Moved Team</td>
                                <td width="20%" class="txtblackbold12" align="center">Count</td>
                            </tr>
                            <%
                                int intCounter = 0;
                                String strBackGround = "";

                                for (int intCount = 0; intCount < arlRQABugsMovedTeams.size(); intCount += 2)
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
                                <td class="txtgrey"><%= arlRQABugsMovedTeams.get(intCount) %>
                                </td>
                                <td class="txtgrey" align="center"><%= arlRQABugsMovedTeams.get(intCount + 1) %>
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
        <%
        }
        else
        {
        %>
        <td width="20%" valign="top">&nbsp;</td>
        <%
            }
        %>
        <td width="45%">&nbsp;</td>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (strRQAPhase != null && strRQAPhase.length() > 0)
        {
    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Bugs
                            (<%= arlRQAListOfBugs.size() / 18 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlRQAListOfBugs != null && !arlRQAListOfBugs.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="11%" class="txtblackbold12">Bug ID</td>
                        <td width="35%" class="txtblackbold12">Bug Description</td>
                        <td width="8%" class="txtblackbold12">Bug Priority</td>
                        <td width="17%" class="txtblackbold12">Bug Resolution</td>
                        <td width="10%" class="txtblackbold12">Completed Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlRQAListOfBugs.size(); iCount += 18)
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
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlRQAListOfBugs.get(iCount + 16) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQAListOfBugs.get(iCount + 2) %>', '<%= arlRQAListOfBugs.get(iCount + 2) %>')"
                                class="link"><%= arlRQAListOfBugs.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQAListOfBugs.get(iCount + 2) %>', '<%= arlRQAListOfBugs.get(iCount + 2) %>')"
                                class="link"><%= arlRQAListOfBugs.get(iCount + 3) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewRQATicketDetails('<%= arlRQAListOfBugs.get(iCount) %>','<%= arlRQAListOfBugs.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlRQAListOfBugs.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlRQAListOfBugs.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlRQAListOfBugs.get(iCount + 13) %>
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
