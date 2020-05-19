<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlClosedInternalTickets = (ArrayList) request.getAttribute("ClosedInternalTickets");
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
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function CITtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                ClosedInternalTickets();
            }
        }


        function ClosedInternalTickets() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.InternalTicketFromDate.value)) {
                    OPTDialog("Enter From Date", document.OPTForm.InternalTicketFromDate);
                    return;
                }
                if (isEmpty(document.OPTForm.InternalTicketToDate.value)) {
                    OPTDialog("Enter To Date", document.OPTForm.InternalTicketToDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.InternalTicketFromDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than current date", document.OPTForm.InternalTicketFromDate);
                    return false;
                }
                if (!compareTwoDates(RestrictedFromDate, document.OPTForm.InternalTicketFromDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog(FromDateMessage, document.OPTForm.InternalTicketFromDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.InternalTicketToDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("To date should not be greater than current date", document.OPTForm.InternalTicketToDate);
                    return false;
                }
                if (!compareTwoDates(document.OPTForm.InternalTicketFromDate.value, document.OPTForm.InternalTicketToDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than To Date", document.OPTForm.InternalTicketFromDate);
                    return false;
                }
                document.OPTForm.action = "ClosedInternalTickets";
                frmReadSubmit();
            }
        }

        function InternalTicketDetails(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadInternalTicketDetailsDummy?ACCESS=RESTRICTED", 600, 1300);
        }


    </script>
</head>
<body onload="document.OPTForm.InternalTicketFromDate.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Filters to View Closed Internal
                            Tickets
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="26%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="25%" class="txtgreybold">From Date</td>
                                                <td width="75%">
                                                    <input type="text" name="InternalTicketFromDate" size="10"
                                                           maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)"
                                                           value="<%= strFromDate %>"
                                                           onkeypress="CITtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar12','dd/mm/yyyy');"><img
                                                            src="images/cal.gif"
                                                            width="20"
                                                            height="13" border="0"
                                                            alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="26%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="25%" class="txtgreybold">To Date</td>
                                                <td width="75%">
                                                    <input type="text" name="InternalTicketToDate" size="10"
                                                           maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)"
                                                           value="<%= strToDate %>"
                                                           onkeypress="CITtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar13','dd/mm/yyyy');"><img
                                                            src="images/cal.gif"
                                                            width="20"
                                                            height="13" border="0"
                                                            alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="14%" valign="middle">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="100%" align="right" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search"
                                                           onclick="JavaScript:ClosedInternalTickets()" id="buttonId0"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="24%" valign="middle">&nbsp;</td>
                                    <td width="10%" valign="middle">&nbsp;</td>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Closed Internal Tickets
                            From <%= strFromDate %>
                            To <%= strToDate %>
                        </td>
                    </tr>
                </table>
                <%
                    if (arlClosedInternalTickets != null && !arlClosedInternalTickets.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="35%" class="txtblackbold12">Action Required</td>
                        <td width="13%" class="txtblackbold12">Assignee</td>
                        <td width="10%" class="txtblackbold12">Start Date</td>
                        <td width="10%" class="txtblackbold12">End Date</td>
                        <td width="10%" class="txtblackbold12">Comments</td>
                        <td width="12%" class="txtblackbold12">Created By</td>

                    </tr>
                    <%
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String strBackGround = "";


                        for (int iCount = 0; iCount < arlClosedInternalTickets.size(); iCount += 8)
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
                        <td title="<%= arlClosedInternalTickets.get(iCount + 1) %>"><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount) %>
                        </td>
                        <td><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount + 4) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount + 5) %></pre>
                        </td>
                        <td><a href="JavaScript:InternalTicketDetails('<%= arlClosedInternalTickets.get(iCount + 7) %>', '<%= arlClosedInternalTickets.get(iCount) %>')">
                            <pre class="txtgrey">Comments History</pre></a></td>
                        <td><pre class="txtgrey"><%= arlClosedInternalTickets.get(iCount + 6) %></pre>
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
