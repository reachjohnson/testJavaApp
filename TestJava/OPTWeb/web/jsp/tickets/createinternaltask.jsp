\
<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlOpenTicketIdsAndDesc = (ArrayList) request.getAttribute("OpenTicketIdsAndDesc");
    ArrayList<String> arlInternalTaskDetails = (ArrayList) request.getAttribute("InternalTaskDetails");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelected = "";

    String strOpenTicketRefNo = "";
    String strNewAssignee = "";
    String strActionRequired = "";

    if (arlInternalTaskDetails != null && !arlInternalTaskDetails.isEmpty())
    {
        strOpenTicketRefNo = AppUtil.checkNull(arlInternalTaskDetails.get(0));
        strNewAssignee = AppUtil.checkNull(arlInternalTaskDetails.get(1));
        strActionRequired = AppUtil.checkNull(arlInternalTaskDetails.get(2));
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function stopRKey(evt) {
            var evt = (evt) ? evt : ((event) ? event : null);
            var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
            if ((evt.keyCode == 13) && (node.type == "text")) {
                return false;
            }
        }

        document.onkeypress = stopRKey;

        function saveTask() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.OpenTicket.options.selectedIndex == 0) {
                    OPTDialog("Select Open Tickets", document.OPTForm.OpenTicket);
                    return;
                }
                if (document.OPTForm.OpenTicket.value == "Others") {
                    if (isEmpty(document.OPTForm.ITTicketIdOthers.value)) {
                        OPTDialog("Enter Ticket Id (For Others)", document.OPTForm.ITTicketIdOthers);
                        return;
                    }
                    if (/[^a-zA-Z0-9\-_]/.test(document.OPTForm.ITTicketIdOthers.value)) {
                        OPTDialog("Special Characters Not Allowed in Ticket Id", document.OPTForm.ITTicketIdOthers);
                        return;
                    }
                }

                if (document.OPTForm.Assignee.options.selectedIndex == 0) {
                    OPTDialog("Select Assignee", document.OPTForm.Assignee);
                    return;
                }
                if (isEmpty(document.OPTForm.ActionRequired.value)) {
                    OPTDialog("Enter Action Required", document.OPTForm.ActionRequired);
                    return;
                }
                if (!isEmpty(document.OPTForm.ITTicketIdOthers.value)) {
                    if (isEmpty(document.OPTForm.ITETADate.value)) {
                        OPTDialog("Enter ETA", document.OPTForm.ITETADate);
                        return;
                    }
                }

                if (!isEmpty(document.OPTForm.ITETADate.value)) {
                    if (!compareTwoDates(document.OPTForm.ITETADate.value, SystemDate, "DD/MM/YYYY", "LESSER")) {
                        OPTDialog("ETA Date should be equal to or greater than system date", document.OPTForm.ITETADate);
                        return;
                    }
                }
                if (document.OPTForm.OpenTicket.value == "Others") {
                    document.OPTForm.OpenTicketId.value = document.OPTForm.ITTicketIdOthers.value;
                    document.OPTForm.OpenTicketDesc.value = "Other Internal Ticket";
                }
                document.OPTForm.action = "saveInternalTask";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.OpenTicket.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Internal Ticket Created Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                });
            }
        }
        function assignopenticketrefno(obj) {
            if (document.OPTForm.OpenTicket.value == "Others") {
                document.OPTForm.ITTicketIdOthers.disabled = false;
            }
            else {
                document.OPTForm.ITTicketIdOthers.value = "";
                document.OPTForm.ITTicketIdOthers.disabled = true;
                var selectedIndex = obj.options.selectedIndex;
                var TicketIdObj = document.OPTForm.elements["TicketId" + selectedIndex];
                var TicketDescObj = document.OPTForm.elements["TicketDesc" + selectedIndex];
                document.OPTForm.OpenTicketRefno.value = TicketIdObj.value;
                document.OPTForm.OpenTicketId.value = TicketIdObj.value;
                document.OPTForm.OpenTicketDesc.value = TicketDescObj.value;
            }
        }

    </script>


</head>
<body onload="JavaScript:displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="OpenTicketRefno">
    <input type="hidden" name="OpenTicketId">
    <input type="hidden" name="OpenTicketDesc">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Create Internal Ticket
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Ticket Id (For Live Tickets)</td>
                                    <td width="80%">
                                        <select name="OpenTicket" class="txtgrey"
                                                onchange="assignopenticketrefno(this)">
                                            <option value="">Select</option>
                                            <%
                                                if (arlOpenTicketIdsAndDesc != null && !arlOpenTicketIdsAndDesc.isEmpty())
                                                {
                                                    String strOpenTicketIdsAndDesc = "";
                                                    for (int intCount = 0; intCount < arlOpenTicketIdsAndDesc.size(); intCount += 3)
                                                    {
                                                        strOpenTicketIdsAndDesc = arlOpenTicketIdsAndDesc.get(intCount + 1) + " - " + arlOpenTicketIdsAndDesc.get(intCount + 2);
                                                        if (strOpenTicketIdsAndDesc.length() > 125)
                                                        {
                                                            strOpenTicketIdsAndDesc = strOpenTicketIdsAndDesc.substring(0, 124);
                                                        }
                                                        strSelected = "";
                                                        if (strOpenTicketRefNo.equalsIgnoreCase(arlOpenTicketIdsAndDesc.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlOpenTicketIdsAndDesc.get(intCount) %>" <%= strSelected %>><%= strOpenTicketIdsAndDesc %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                            <option value="Others">Others</option>
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
                                    <td width="20%" class="txtgreybold">Ticket Id (For Others)</td>
                                    <td width="80%">
                                        <input type="text" name="ITTicketIdOthers" size="30"
                                               maxlength="30" class="txtgrey" disabled="true">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Assignee</td>
                                    <td width="80%">
                                        <select name="Assignee" class="txtgrey">
                                            <option value="">Select</option>
                                            <option value="<%= AppConstants.ALL_ASSIGNEES %>" <%= strSelected %>><%= AppConstants.ALL_ASSIGNEES %>
                                                    <%
                                    if (arlAssignees != null && !arlAssignees.isEmpty())
                                    {
                                        for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                                        {
                                            strSelected = "";
                                            if (strNewAssignee.equalsIgnoreCase(arlAssignees.get(intCount)))
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
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Action Required</td>
                                    <td width="80%">
                            <textarea name="ActionRequired" rows="6" cols="120" maxlength="998"
                                      class="txtgrey"><%= strActionRequired %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">ETA</td>
                                    <td width="80%">
                                        <input type="text" name="ITETADate" size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)">
                                        <a href="javascript:showCal('Calendar20','dd/mm/yyyy');"><img
                                                src="images/cal.gif"
                                                width="20"
                                                height="13" border="0"
                                                alt="Pick a date"></a>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveTask()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Reset" onclick="JavaScript:frmCancel()" id="buttonId1"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
    <%
        int intCounter = 1;
        if (arlOpenTicketIdsAndDesc != null && !arlOpenTicketIdsAndDesc.isEmpty())
        {
            for (int intCount = 0; intCount < arlOpenTicketIdsAndDesc.size(); intCount += 3)
            {

    %>
    <input type="hidden" name="TicketId<%= intCounter %>" value="<%= arlOpenTicketIdsAndDesc.get(intCount + 1) %>">
    <input type="hidden" name="TicketDesc<%= intCounter %>" value="<%= arlOpenTicketIdsAndDesc.get(intCount + 2) %>">
    <%
                intCounter++;
            }
        }
    %>
    <input type="hidden" name="TicketId<%= intCounter %>" value="Others">
    <input type="hidden" name="TicketDesc<%= intCounter %>" value="Others">

</form>
</body>
</html>