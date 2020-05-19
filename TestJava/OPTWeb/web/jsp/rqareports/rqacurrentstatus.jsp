<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlRQACurrentStatus = (ArrayList) request.getAttribute("RQACurrentStatus");
    String strRQACurrentPhase = AppUtil.checkNull((String) request.getAttribute("RQACurrentPhase"));
    boolean blnRQACurrentPhase = strRQACurrentPhase.length() > 0;

    int intOpenBugsCount = 0;
    int intClosedBugsCount = 0;
    int intTotalCount = 0;

    if (arlRQACurrentStatus != null && !arlRQACurrentStatus.isEmpty())
    {
        for (int intCount = 0; intCount < arlRQACurrentStatus.size(); intCount += 18)
        {
            if (arlRQACurrentStatus.get(intCount + 17).equalsIgnoreCase(AppConstants.OPEN))
            {
                intOpenBugsCount++;
            }
            else if (arlRQACurrentStatus.get(intCount + 17).equalsIgnoreCase(AppConstants.COMPLETED))
            {
                intClosedBugsCount++;
            }
        }
    }
    intTotalCount = intOpenBugsCount + intClosedBugsCount;
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
        function viewRQATicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadRQATicketDetails?ACCESS=RESTRICTED", 700, 1300);
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

        function displaymessage() {
            var RQACurrentPhase = <%= blnRQACurrentPhase %>;
            if (!RQACurrentPhase) {
                showFullPageMask(true);
                jAlert("No Current RQA Phase Found. Update the RQA Phase in RQA Settings.", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                });
            }
        }

    </script>
</head>
<body onload="displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA <%= strRQACurrentPhase %> Current Status</td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>

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
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="5%" class="txtblackbold12" align="center">Open Bugs</td>
                                    <td width="5%" class="txtblackbold12" align="center">Closed Bugs</td>
                                    <td width="5%" class="txtblackbold12" align="center">Total</td>
                                </tr>
                                <tr class="LightGreyBandStyle">
                                    <td class="txtgrey" align="center"><%= intOpenBugsCount %>
                                    </td>
                                    <td class="txtgrey" align="center"><%= intClosedBugsCount %>
                                    </td>
                                    <td class="txtgreybold" align="center"><%= intTotalCount %>
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

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Open Bugs
                        </td>
                    </tr>
                </table>
                <%
                    if (intOpenBugsCount > 0)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="16%" class="txtblackbold12">Assignee</td>
                        <td width="11%" class="txtblackbold12">Bug ID</td>
                        <td width="44%" class="txtblackbold12">Bug Description</td>
                        <td width="10%" class="txtblackbold12">Bug Priority</td>
                        <td width="8%" class="txtblackbold12">Received Date</td>
                        <td width="8%" class="txtblackbold12">ETA</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";
                        int intSerialNo = 0;
                        String strETADate = "";
                        for (int iCount = 0; iCount < arlRQACurrentStatus.size(); iCount += 18)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }
                            if (arlRQACurrentStatus.get(iCount + 17).equalsIgnoreCase(AppConstants.OPEN))
                            {
                                if (arlRQACurrentStatus.get(iCount + 12).length() > 0)
                                {
                                    strETADate = arlRQACurrentStatus.get(iCount + 12);
                                }
                                else
                                {
                                    strETADate = "Yet To Start";
                                }
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlRQACurrentStatus.get(iCount + 16) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQACurrentStatus.get(iCount + 2) %>', '<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                class="link"><%= arlRQACurrentStatus.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQACurrentStatus.get(iCount + 2) %>', '<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                class="link"><%= arlRQACurrentStatus.get(iCount + 3) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewRQATicketDetails('<%= arlRQACurrentStatus.get(iCount) %>','<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlRQACurrentStatus.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlRQACurrentStatus.get(iCount + 11) %>
                        </td>
                        <td class="txtgrey"><%= strETADate %>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Closed Bugs
                        </td>
                    </tr>
                </table>
                <%
                    if (intClosedBugsCount > 0)
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
                        for (int iCount = 0; iCount < arlRQACurrentStatus.size(); iCount += 18)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }
                            if (arlRQACurrentStatus.get(iCount + 17).equalsIgnoreCase(AppConstants.COMPLETED))
                            {
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlRQACurrentStatus.get(iCount + 16) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQACurrentStatus.get(iCount + 2) %>', '<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                class="link"><%= arlRQACurrentStatus.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQACurrentStatus.get(iCount + 2) %>', '<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                class="link"><%= arlRQACurrentStatus.get(iCount + 3) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewRQATicketDetails('<%= arlRQACurrentStatus.get(iCount) %>','<%= arlRQACurrentStatus.get(iCount + 2) %>')"
                                title="Ticket Details"><%= arlRQACurrentStatus.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlRQACurrentStatus.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlRQACurrentStatus.get(iCount + 13) %>
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
</form>
</body>
</html>
