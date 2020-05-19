<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("ModifyITAssignee"));
    ArrayList<String> arlITAssignees = (ArrayList) request.getAttribute("ITAssignees");
    ArrayList<String> arlITDetails = (ArrayList) request.getAttribute("ITDetails");

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function loadITAdminChanges() {
            document.OPTForm.action = "loadITAdminChanges";
            frmReadSubmit();
        }

        function AddComments(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadAddITCommentsDummy?ACCESS=RESTRICTED", 600, 1300);
        }

        function updateDeleteInternalTicket(ActionVal, RefNo, TicketId, Assignee) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            document.OPTForm.hidOriginalAssignee.value = Assignee;
            if (ActionVal == "Update") {
                document.OPTForm.action = "loadUpdateIT";
                frmWriteSubmit();
            }
            else if (ActionVal == "Delete") {
                showFullPageMask(true);
                jConfirm("Are you sure you want to delete this Ticket '" + document.OPTForm.hidTicketId.value + "'", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "loadDeleteIT";
                        frmWriteSubmit();
                    }
                });
            }

        }

        function MCTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                loadITAdminChanges();
            }
        }

        function displaymessage() {
            document.OPTForm.ModifyITAssignee.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "TICKETDELETED") {
                OPTDialog("Internal Ticket Deleted Successfully", document.OPTForm.ModifyITAssignee);
            }
        }

    </script>

</head>
<body onload="displaymessage()" onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidFromPage" value="MODIFYINTERNALTICKETS">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidAction">
    <input type="hidden" name="hidSubmitAction">
    <input type="hidden" name="hidOriginalAssignee">

    <table width="99%" align="center">
        <tr>
            <td width="25%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Internal Tickets - Admin Changes
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
                                        <select name="ModifyITAssignee" class="txtgrey" onkeypress="MCTtrackEnterKey(event)">
                                            <%
                                                String strSelected = "";
                                                if (strAssignee.equalsIgnoreCase(AppConstants.ALL_ASSIGNEES))
                                                    strSelected = "selected";
                                            %>
                                            <option value="<%= AppConstants.ALL_ASSIGNEES %>" <%= strSelected %>><%= AppConstants.ALL_ASSIGNEES %>
                                            </option>
                                            <%
                                                if (arlITAssignees != null && !arlITAssignees.isEmpty())
                                                {
                                                    for (int intCount = 0; intCount < arlITAssignees.size(); intCount += 2)
                                                    {
                                                        strSelected = "";
                                                        if (strAssignee.equalsIgnoreCase(arlITAssignees.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlITAssignees.get(intCount) %>" <%= strSelected %>><%= arlITAssignees.get(intCount + 1) %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search"
                                               onclick="JavaScript:loadITAdminChanges()" id="buttonId0"/>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Internal Tickets
                            (<%= arlITDetails.size() / 7 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlITDetails != null && !arlITDetails.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                    <tr class="LightBlueBandStyle">
                        <td width="7" class="txtblackbold12">Action</td>
                        <td width="15%" class="txtblackbold12">Ticket ID</td>
                        <td width="26%" class="txtblackbold12">Ticket Description</td>
                        <td width="35%" class="txtblackbold12">Action Required</td>
                        <td width="12%" class="txtblackbold12">Assignee</td>
                        <td width="6%" class="txtblackbold12">ETA</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlITDetails.size(); iCount += 7)
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
                        <td>
                            <table >
                                <tr>
                                    <td valign="middle"><a href="JavaScript:updateDeleteInternalTicket('Update', '<%= arlITDetails.get(iCount) %>', '<%= arlITDetails.get(iCount+1) %>', '<%= arlITDetails.get(iCount + 6) %>')"
                                                           class="link"><img src="images/update_new.jpg" border="0" title="Update Internal Ticket"></a></td>
                                    <td valign="middle"><a href="JavaScript:updateDeleteInternalTicket('Delete', '<%= arlITDetails.get(iCount) %>', '<%= arlITDetails.get(iCount+1) %>', '<%= arlITDetails.get(iCount + 6) %>')"
                                                           class="link"><img src="images/closeicon_new.png" border="0"
                                                                             title="Delete Internal Ticket"></a></td>
                                    <td valign="middle"><a href="JavaScript:AddComments('<%= arlITDetails.get(iCount) %>', '<%= arlITDetails.get(iCount+1) %>')">
                                        <img src="images/comments_new.jpg" border="0" title="Add Comments"></a></td>
                                </tr>
                            </table>
                        </td>
                        <td><pre class="txtgrey"><%= arlITDetails.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITDetails.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITDetails.get(iCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITDetails.get(iCount + 5) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITDetails.get(iCount + 4) %></pre>
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
