<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlUserList = (ArrayList) request.getAttribute("USERLIST");
    ArrayList<String> arlUserAccessRights = (ArrayList) request.getAttribute("UserAccessRights");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelectedUserId = AppUtil.checkNull((String) request.getAttribute("AccessRightsUserId"));
    String strSelected = "";
    boolean blnDataAvailable = false;
    if (arlUserAccessRights != null && !arlUserAccessRights.isEmpty())
    {
        blnDataAvailable = true;
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function saveAccessRights() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                document.OPTForm.action = "saveAccessRights";
                frmWriteSubmit();
            }
        }

        function getAccessRights() {
            document.OPTForm.action = "loadAccessRights";
            frmReadSubmit();
        }

        function displaymessage() {
            document.OPTForm.AccessRightsUserId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "MODIFIED") {
                OPTDialog("Access Rights Updated Successfully", document.OPTForm.AccessRightsUserId);
            }
        }

    </script>
</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="99%" align="center">
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Select User to Update Access
                                        Rights
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="left">
                                <tr class="LightBandStyle">
                                    <td width="30%" class="txtgreybold">Select User</td>
                                    <td width="70%">
                                        <select name="AccessRightsUserId" class="txtgrey"
                                                onchange="JavaScript:getAccessRights()">
                                            <option value="">Select</option>
                                            <%
                                                if (arlUserList != null && !arlUserList.isEmpty())
                                                {
                                                    for (int intCount = 0; intCount < arlUserList.size(); intCount += 2)
                                                    {
                                                        strSelected = "";
                                                        if (strSelectedUserId.equalsIgnoreCase(arlUserList.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlUserList.get(intCount) %>" <%= strSelected %>><%= arlUserList.get(intCount + 1) %>
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
                    </tr>
                </table>
            </td>
            <td width="70%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        if (strSelectedUserId != null && strSelectedUserId.length() > 0)
        {
    %>

    <table width="99%" align="center">
        <tr>
            <td width="20%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Application Write Access
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="80%" class="txtgreybold">Write Access</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(0).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="20%"><input type="checkbox"
                                                                       name="WriteAccess" <%= strSelected %> >
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
            <td width="80%">&nbsp;
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Menu Items and Automated Email
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                                <tr>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0
                                               style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Tickets =>
                                                    Live Tickets
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;Create Task</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(1).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%" align="left"><input type="checkbox"
                                                                                    name="CreateTask" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Create Bug</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(2).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="CreateBug" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Reopen Ticket</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(5).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="ReopenClosedTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Update Closed Ticket</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(41).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="UpdateClosedTicket" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Admin Changes</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(4).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="ModifyCurrentTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Tickets =>
                                                    RQA Tickets
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Create Bug</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(25).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="CreateRQABug" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Reopen Bug</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(26).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="ReopenRQABug" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Admin Changes</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(27).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox"
                                                                        name="ModifyRQATickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Tickets =>
                                                    Internal Tickets
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Create Ticket</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(3).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="CreateInternalTicket" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Admin Changes</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(33).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="InternalTicketAdmin" <%= strSelected %> ></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0
                                               style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Calendar
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;Update Leave</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(6).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%"><input type="checkbox"
                                                                       name="UpdateLeavePlans" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Holiday Calendar</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(7).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="HolidayCalendar" <%= strSelected %> >
                                                </td>
                                            </tr>

                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Maintenance
                                                    => User
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Add User</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(8).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="AddUser" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Modify User</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(9).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ModifyUser" <%= strSelected %> ></td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Maintenance
                                                    => Settings
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Access Rights</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(10).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="AccessRights" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;RQA Settings</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(31).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="RQASettings" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Common Settings</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(40).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="CommonSettings" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="15%" class="txtgreybold">&nbsp;Resource Matrix</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(32).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="2%"><input type="checkbox"
                                                                      name="ResourceMatrix" <%= strSelected %> ></td>
                                            </tr>
                                        </table>
                                    </td>

                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0
                                               style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Reports
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;Resource Allocation</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(16).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%"><input type="checkbox"
                                                                       name="ResourceAllocation" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Reports =>
                                                    Live Tickets
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Tickets Status</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(11).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="TicketsStatus" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Current Status</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(12).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="CurrentStatus" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Open Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(13).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="CurrentTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Closed Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(14).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ClosedTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Reopened Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(43).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ReopenedTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;OOSLA Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(15).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="SLAMissedTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;SLA Reports</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(17).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="SLAReports" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;SLA Metrics</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(18).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="SLAMetrics" <%= strSelected %> ></td>
                                            </tr>

                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Code Fix Releases</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(44).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="CodeFixReleases" <%= strSelected %> >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0
                                               style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Reports =>
                                                    RQA Tickets
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;Daily Report</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(28).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%"><input type="checkbox"
                                                                       name="RQADailyReport" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Current Status</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(29).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="RQACurrentStatus" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Metrics</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(30).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="RQAMetrics" <%= strSelected %> ></td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Reports =>
                                                    Internal Tickets
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Open Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(19).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="CurrentInternalTickets" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Closed Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(20).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="ClosedInternalTickets" <%= strSelected %> ></td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Reports =>
                                                    Leave and Holiday
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;View Leave Plans</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(21).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ViewLeavePlans" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;List of Holidays</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(22).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ListOfHolidays" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Common</td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Team Contacts</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(23).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="TeamContacts" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Domain POC</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(24).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="DomainPOC" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Team Feedback</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(42).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="TeamFeedback" <%= strSelected %> ></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0
                                               style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Automated
                                                    Email
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;High Priority Not Started</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(34).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%"><input type="checkbox"
                                                                       name="HighPriorityNotStarted" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;OOSLA Not Started</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(35).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="OOSLANotStarted" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;RQA ETA Reminder</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(36).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="RQAETAReminder" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Status Update Reminder</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(37).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="StatusUpdateReminder" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Open Tickets Priority</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(38).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="OpenTicketsPriority" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Tickets Progress Check</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlUserAccessRights.get(39).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox"
                                                           name="TicketsProgressCheck" <%= strSelected %> ></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveAccessRights()"
                       id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
