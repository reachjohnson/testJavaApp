<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("ModifyRQATicketAssignee"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlRQATicketDetails = (ArrayList) request.getAttribute("RQATicketDetails");

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function modifyrqatickets() {
            document.OPTForm.action = "loadModifyRQATickets";
            frmReadSubmit();
        }
        function MCTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                modifyrqatickets();
            }
        }
        function setRQAActionRefNoTicketId(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidAction.value = ActionName;
        }


        function RQAImageSubmitAction(ActionName, RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidSubmitAction.value = ActionName;
            submitModifyRQATickets();
        }

        function submitRQATickets() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.hidAction.value != "RQATickets") {
                    OPTDialog("Select Bug");
                    return;
                }
                if (document.OPTForm.RQATicketsAction.options.selectedIndex == 0) {
                    OPTDialog("Select Action");
                    return;
                }
            }
            document.OPTForm.hidSubmitAction.value = document.OPTForm.RQATicketsAction.value;
            submitModifyRQATickets();
        }

        function submitModifyRQATickets() {
            if (document.OPTForm.hidSubmitAction.value == "loadRQAReAssignBug") {
                showFullPageMask(true);
                MM_openBrWindow("ReAssignRQATicketDummy?ACCESS=RESTRICTED", 450, 1100);
            }
            else if (document.OPTForm.hidSubmitAction.value == "deleteRQATicket") {
                showFullPageMask(true);
                jConfirm("Are you sure you want to delete this Bug '" + document.OPTForm.hidTicketId.value + "'", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = document.OPTForm.hidSubmitAction.value;
                        frmWriteSubmit();
                    }
                });
            }
            else if (document.OPTForm.hidSubmitAction.value == "loadRQABugDetails") {
                showFullPageMask(true);
                MM_openBrWindow("loadRQATicketDetails?ACCESS=RESTRICTED", 700, 1300);
            }
            else if (document.OPTForm.hidSubmitAction.value == "RQAAddComments") {
                showFullPageMask(true);
                MM_openBrWindow("loadAddRQACommentsDummy?ACCESS=RESTRICTED", 600, 1300);
            }
            else {
                document.OPTForm.action = document.OPTForm.hidSubmitAction.value;
                frmWriteSubmit();
            }
        }
        function displaymessage() {
            document.OPTForm.ModifyRQATicketAssignee.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "TICKETDELETED") {
                OPTDialog("RQA Bug Deleted Successfully", document.OPTForm.ModifyRQATicketAssignee);
            }
        }

    </script>

</head>
<body onload="displaymessage()" onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidFromPage" value="ModifyRQATickets">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidAction">
    <input type="hidden" name="hidSubmitAction">

    <table width="99%" align="center">
        <tr>
            <td width="25%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bugs - Admin Changes
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="15%" class="txtgreybold">&nbsp;&nbsp;Assignee</td>
                                    <td width="65%">
                                        <select name="ModifyRQATicketAssignee" class="txtgrey" onkeypress="MCTtrackEnterKey(event)">
                                            <%
                                                String strSelected = "";
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
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search" onclick="JavaScript:modifyrqatickets()"
                                               id="buttonId0"/>
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

    <%
        if (strAssignee.length() > 0)
        {
    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">

                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bugs
                            (<%= arlRQATicketDetails.size() / 9 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlRQATicketDetails != null && !arlRQATicketDetails.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="5%" class="txtblackbold12">For Action</td>
                        <td width="10%" class="txtblackbold12">Assignee</td>
                        <td width="15%" class="txtblackbold12">Bug ID</td>
                        <td width="50%" class="txtblackbold12">Bug Description</td>
                        <td width="10%" class="txtblackbold12">Bug Priority</td>
                        <td width="10%" class="txtblackbold12">Received Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlRQATicketDetails.size(); iCount += 9)
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
                        <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                                   onclick="JavaScript:setRQAActionRefNoTicketId('RQATickets','<%= arlRQATicketDetails.get(iCount) %>', '<%= arlRQATicketDetails.get(iCount +1) %>')">
                        </td>
                        <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 8) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlRQATicketDetails.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                                class="link"><%= arlRQATicketDetails.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 6) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
                    <tr class="LightBlueBandStyle">
                        <td colspan="10">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                                <tr>
                                    <td width="5%" class="txtblackbold12">Action</td>
                                    <td width="12%" align="left" class="txtgreybold">
                                        <select name="RQATicketsAction" class="txtgreybold">
                                            <option value="">Select</option>
                                            <option value="loadRQABugDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                            </option>
                                            <option value="loadRQAUpdateBug"><%= AppConstants.ACTION_UPDATE%>
                                            </option>
                                            <option value="loadCloseRQABug"><%= AppConstants.ACTION_CLOSE_TICKET%>
                                            </option>
                                            <option value="RQAAddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                            </option>
                                            <option value="loadRQAReAssignBug"><%= AppConstants.ACTION_REASSIGN%>
                                            </option>
                                            <option value="deleteRQATicket"><%= AppConstants.ACTION_DELETE_TICKET%>
                                            </option>
                                        </select>
                                    </td>
                                    <td width="10%">
                                        <input type="button" class="myButton" value="Submit"
                                               onclick="JavaScript:submitRQATickets()" id="SendButton7"/>
                                    </td>
                                    <td width="73%">&nbsp
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
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
