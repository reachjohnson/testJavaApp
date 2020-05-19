<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlBugDetails = (ArrayList) request.getAttribute("BugDetails");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelected = "";

    String strNewBugId = "";
    String strNewBugDescription = "";
    String strNewBugPriority = "";
    String strNewBugType = "";
    String strNewBugModule = "";
    String strNewAssignee = "";
    String strCreateBugRecDate = "";
    String strNewReceivedHour = "";
    String strNewReceivedMinute = "";
    String strNewReceived24Hours = "";
    String strNewBugRaisedBy = "";
    String strNewBugCreatedDate = "";

    if (arlBugDetails != null && !arlBugDetails.isEmpty()) {
        strNewBugId = AppUtil.checkNull(arlBugDetails.get(0));
        strNewBugDescription = AppUtil.checkNull(arlBugDetails.get(1));
        strNewBugPriority = AppUtil.checkNull(arlBugDetails.get(2));
        strNewBugType = AppUtil.checkNull(arlBugDetails.get(3));
        strNewBugModule = AppUtil.checkNull(arlBugDetails.get(4));
        strNewAssignee = AppUtil.checkNull(arlBugDetails.get(5));
        strCreateBugRecDate = AppUtil.checkNull(arlBugDetails.get(6));
        strNewReceivedHour = AppUtil.checkNull(arlBugDetails.get(7));
        strNewReceivedMinute = AppUtil.checkNull(arlBugDetails.get(8));
        strNewReceived24Hours = AppUtil.checkNull(arlBugDetails.get(9));
        strNewBugRaisedBy = AppUtil.checkNull(arlBugDetails.get(10));
        strNewBugCreatedDate = AppUtil.checkNull(arlBugDetails.get(11));
    }

    if (strNewAssignee.isEmpty()) {
        strNewAssignee = strUserId;
    }

    if (strCreateBugRecDate.isEmpty()) {
        strCreateBugRecDate = strCurrentDateDDMMYYYY;
    }

    if (strNewReceivedHour.isEmpty()) {
        strNewReceivedHour = strCurrentHour;
    }

    if (strNewReceivedMinute.isEmpty()) {
        strNewReceivedMinute = strCurrentMinute;
    }

    if (strNewReceived24Hours.isEmpty()) {
        strNewReceived24Hours = strCurrent24Hours;
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

        function saveBug() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.BugId.value)) {
                    OPTDialog("Enter Bug Id", document.OPTForm.BugId);
                    return;
                }
                if (/[^a-zA-Z0-9\-]/.test(document.OPTForm.BugId.value)) {
                    OPTDialog("Special Characters Not Allowed in Bug Id", document.OPTForm.BugId);
                    return;
                }

                if (isEmpty(document.OPTForm.BugDescription.value)) {
                    OPTDialog("Enter Bug Description", document.OPTForm.BugDescription);
                    return;
                }
                if (document.OPTForm.BugPriority.options.selectedIndex == 0) {
                    OPTDialog("Select Bug Priority", document.OPTForm.BugPriority);
                    return;
                }
                if (document.OPTForm.BugModule.options.selectedIndex == 0) {
                    OPTDialog("Select Bug Module", document.OPTForm.BugModule);
                    return;
                }
                if (document.OPTForm.BugRaisedBy.options.selectedIndex == 0) {
                    OPTDialog("Select Bug Raised By", document.OPTForm.BugRaisedBy);
                    return;
                }
                if (document.OPTForm.Assignee.options.selectedIndex == 0) {
                    OPTDialog("Select Assignee", document.OPTForm.Assignee);
                    return;
                }
                if (isEmpty(document.OPTForm.CreateBugRecDate.value)) {
                    OPTDialog("Enter Received Date", document.OPTForm.CreateBugRecDate);
                    return;
                }
                var enteredDate = getDefaultDateFormat(document.OPTForm.CreateBugRecDate.value, "DD/MM/YYYY");
                enteredDateTime = enteredDate + " " + document.OPTForm.ReceivedHour.value + ":" + document.OPTForm.ReceivedMinute.value + " " + document.OPTForm.Received24Hours.value;
                var enteredDateValue = Date.parse(enteredDateTime);
                var systemDateValue = Date.parse(new Date());
                if (enteredDateValue > systemDateValue) {
                    OPTDialog("Received Date Should Not Be Greater Than Current Date", document.OPTForm.CreateBugRecDate)
                    return;
                }

                if (isEmpty(document.OPTForm.CreatedBugDate.value)) {
                    OPTDialog("Enter Created Date", document.OPTForm.CreatedBugDate);
                    return;
                }
                var createdDate = getDefaultDateFormat(document.OPTForm.CreatedBugDate.value, "DD/MM/YYYY");
                var createdDateValue = Date.parse(createdDate);
                if (createdDateValue > systemDateValue) {
                    OPTDialog("Created Date Should Not Be Greater Than Current Date", document.OPTForm.CreatedBugDate);
                    return;
                }
                if (enteredDateValue < createdDateValue) {
                    OPTDialog("Received Date Should Not Be Lesser Than Created Date", document.OPTForm.CreateBugRecDate);
                    return;
                }
                document.OPTForm.action = "saveBug";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.BugId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jConfirm("Bug Created Successfully.\nDo you want to create another Bug?", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                    else {
                        document.OPTForm.BugId.focus();
                    }
                });
            }
            else if (varResult == "BUGEXISTS") {
                OPTDialog("Bug Id already exists", document.OPTForm.BugId);
            }

        }

    </script>


</head>
<body onload="JavaScript:displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="ReceivedHour" value="<%= strNewReceivedHour%>">
    <input type="hidden" name="ReceivedMinute" value="<%= strNewReceivedMinute%>">
    <input type="hidden" name="Received24Hours" value="<%= strNewReceived24Hours%>">

    <table width="90%" cellpadding=0 cellspacing=0 border=1 align="center" class="TableBorder1Pix" style="border-collapse:collapse;">
        <tr>
            <td width="100%">
                <table width="100%">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Create New Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=3 border="0">
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Bug Id</td>
                        <td width="82%" colspan="3">
                            <input type="text" name="BugId" size="30" maxlength="30" class="txtgrey"
                                   value="<%= strNewBugId %>">
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Bug Description</td>
                        <td width="82%" colspan="3">
                                        <textarea name="BugDescription" rows="3" cols="120" maxlength="490"
                                                  class="txtgrey"><%= strNewBugDescription %></textarea>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Bug Priority</td>
                        <td width="32%">
                            <select name="BugPriority" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strBugPriority : AppConstants.TICKET_PRIORITY) {
                                        strSelected = "";
                                        if (strNewBugPriority.equalsIgnoreCase(strBugPriority)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strBugPriority %>" <%= strSelected %>><%= strBugPriority %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                        <td width="18%" class="txtgreybold">Bug Module</td>
                        <td width="32%">
                            <select name="BugModule" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strBugModule : AppConstants.TICKET_MODULE) {
                                        strSelected = "";
                                        if (strNewBugModule.equalsIgnoreCase(strBugModule)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strBugModule %>" <%= strSelected %>><%= strBugModule %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Bug Raised By</td>
                        <td width="32%">
                            <select name="BugRaisedBy" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strBugRaisedBy : AppConstants.TICKET_RAISED_BY) {
                                        strSelected = "";
                                        if (strNewBugRaisedBy.equalsIgnoreCase(strBugRaisedBy)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strBugRaisedBy %>" <%= strSelected %>><%= strBugRaisedBy %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                        <td width="18%" class="txtgreybold">Assignee</td>
                        <td width="32%">
                            <select name="Assignee" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    if (arlAssignees != null && !arlAssignees.isEmpty()) {
                                        for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2) {
                                            strSelected = "";
                                            if (strNewAssignee.equalsIgnoreCase(arlAssignees.get(intCount))) {
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
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Received Date</td>
                        <td width="32%">
                            <input type="text" name="CreateBugRecDate" value="<%= strCreateBugRecDate %>" size="10"
                                   maxlength="20" class="txtgrey"
                                   onblur="checkValidDateForObject(this)">
                            <a href="javascript:showCal('Calendar9','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                         height="13" border="0"
                                                                                         alt="Pick a date"></a>
                        </td>
                        <td width="18%" class="txtgreybold">Created Date</td>
                        <td width="32%">
                            <input type="text" name="CreatedBugDate" value="<%= strNewBugCreatedDate %>" size="10"
                                   maxlength="20" class="txtgrey"
                                   onblur="checkValidDateForObject(this)">
                            <a href="javascript:showCal('Calendar11','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                          width="20"
                                                                                          height="13" border="0"
                                                                                          alt="Pick a date"></a>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveBug()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Reset" onclick="JavaScript:frmCancel()" id="buttonId1"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
</form>
</body>
</html>