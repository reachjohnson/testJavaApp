<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strReopenTicketId = AppUtil.checkNull((String) request.getAttribute("ReopenTicketId"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlRQATicketDetailsForReopen = (ArrayList) request.getAttribute("RQATicketDetailsForReopen");
    String strRefNo = "";
    String strTicketCategory = "";
    String strTicketId = "";
    if (arlRQATicketDetailsForReopen != null && !arlRQATicketDetailsForReopen.isEmpty())
    {
        strRefNo = arlRQATicketDetailsForReopen.get(0);
        strTicketCategory = arlRQATicketDetailsForReopen.get(1);
        strTicketId = arlRQATicketDetailsForReopen.get(2);
    }
    if (strResult.equalsIgnoreCase(AppConstants.SUCCESS))
    {
        arlRQATicketDetailsForReopen = null;
        strReopenTicketId = "";
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function ROTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                loadRQATicketDetailsForReopen();
            }
        }

        function loadRQATicketDetailsForReopen() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.TicketId.value)) {
                    OPTDialog("Enter Bug Id", document.OPTForm.TicketId);
                    return;
                }
                if (/[^a-zA-Z0-9\-]/.test(document.OPTForm.TicketId.value)) {
                    OPTDialog("Special Characters Not Allowed in Ticket Id", document.OPTForm.TicketId);
                    return;
                }
                document.OPTForm.action = "loadReopenRQATicket";
                frmReadSubmit();
            }
        }

        function reopenTicket() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.ReasonForReopen.options.selectedIndex == 0) {
                    OPTDialog("Select Reason For Reopen", document.OPTForm.ReasonForReopen);
                    return;
                }
                if (document.OPTForm.Assignee.options.selectedIndex == 0) {
                    OPTDialog("Select Assignee for Reopening this Ticket", document.OPTForm.Assignee);
                    return;
                }
                if (isEmpty(document.OPTForm.ReopenComments.value)) {
                    OPTDialog("Enter Comments", document.OPTForm.ReopenComments);
                    return;
                }
                document.OPTForm.action = "ReopenRQATicket";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.TicketId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                document.OPTForm.TicketId.value = "";
                OPTDialog("RQA Bug Re-Opened Successfully", document.OPTForm.TicketId);
            }
        }
    </script>
</head>
<body onload="JavaScript:displaymessage()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidTicketId" value="<%= strTicketId %>">

    <table width="99%">
        <tr>
            <td width="2%">&nbsp;</td>
            <td width="55%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Search Closed RQA Bug to
                                        Re-Open
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">&nbsp;&nbsp;Bug Id</td>
                                    <td width="40%"><input type="text" name="TicketId" size="30" maxlength="30"
                                                           class="txtgrey"
                                                           value="<%= strReopenTicketId %>"
                                                           onkeypress="ROTtrackEnterKey(event)">
                                    </td>
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search"
                                               onclick="JavaScript:loadRQATicketDetailsForReopen()" id="buttonId0"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="43%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strReopenTicketId.length() > 0)
        {
    %>
    <table width="99%">
        <tr>
            <td width="2%">&nbsp;</td>
            <td width="90%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Closed RQA Bug Details to
                                        Re-Open
                                    </td>
                                </tr>
                            </table>
                            <%
                                if (arlRQATicketDetailsForReopen != null && !arlRQATicketDetailsForReopen.isEmpty())
                                {
                            %>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">RQA Phase</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(14) %>
                                                    <input type="hidden" name="hidRQAPhase"
                                                           value="<%= arlRQATicketDetailsForReopen.get(14) %>">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">RQA Cycle</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(15) %>
                                                    <input type="hidden" name="hidRQACycle"
                                                           value="<%= arlRQATicketDetailsForReopen.get(15) %>">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Id</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(2) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="100%" colspan="2" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtbluebold">Bug Description</td>
                                                <td width="82%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(3) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Priority</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(4) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Module</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(5) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Raised By</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(6) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Resolution</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(7) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Moved Team</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(8) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Bug Root Cause</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(9) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtbluebold">Comments</td>
                                                <td width="82%"><pre class="txtblue"><%= arlRQATicketDetailsForReopen.get(10) %></pre>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Received Date</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(11) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtbluebold">Planned End Date</td>
                                                <td width="64%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(12) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtbluebold">Actual End Date</td>
                                                <td width="82%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(13) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtbluebold">Assignee</td>
                                                <td width="82%"
                                                    class="txtblue"><%= arlRQATicketDetailsForReopen.get(16) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Reason For Reopen</td>
                                                <td width="64%" class="txtblue">
                                                    <select name="ReasonForReopen" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strReopenReason : AppConstants.REOPEN_REASONS)
                                                            {
                                                        %>
                                                        <option value="<%= strReopenReason %>"><%= strReopenReason %>
                                                        </option>
                                                        <%
                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Assignee For Reopening</td>
                                                <td width="64%">
                                                    <select name="Assignee" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            if (arlAssignees != null && !arlAssignees.isEmpty())
                                                            {
                                                                for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                                                                {
                                                        %>
                                                        <option value="<%= arlAssignees.get(intCount) %>"><%= arlAssignees.get(intCount + 1) %>
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

                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtgreybold">Comments</td>
                                                <td width="82%">
                    <textarea name="ReopenComments" rows="6" cols="120" maxlength="4990"
                              class="txtgrey"></textarea>
                                                </td>
                                            </tr>
                                        </table>
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
                        </td>
                    </tr>
                </table>
            </td>
            <td width="8%">&nbsp;</td>
        </tr>

            <%
        if (arlRQATicketDetailsForReopen != null && !arlRQATicketDetailsForReopen.isEmpty())
        {
    %>

        <%@ include file="../common/onespace.jsp" %>
        <table cellpadding=2 cellspacing=1 border=0 align="center">
            <tr>
                <td width="50%" align="right" valign="middle">&nbsp;
                    <input type="button" class="myButton" value="Save" onclick="JavaScript:reopenTicket()"
                           id="buttonId1"/>
                </td>
                <td width="50%" align="left" valign="middle">&nbsp;
                    <input type="button" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"
                           id="buttonId2"/>
                </td>
            </tr>
        </table>
        <%@ include file="../common/spacebefore.jsp" %>
            <%
            }
        }
    %>
</form>
</body>
</html>
