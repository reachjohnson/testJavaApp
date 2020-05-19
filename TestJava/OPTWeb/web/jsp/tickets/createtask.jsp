<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlTaskReference = (ArrayList) request.getAttribute("TaskReference");
    ArrayList<String> arlTaskDetails = (ArrayList) request.getAttribute("TaskDetails");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelected = "";

    String strNewTaskId = "";
    String strNewTaskDescription = "";
    String strNewTaskPriority = "";
    String strNewTaskType = "";
    String strNewTaskModule = "";
    String strNewAssignee = "";
    String strCreateTaskRecDate = "";
    String strNewReceivedHour = "";
    String strNewReceivedMinute = "";
    String strNewReceived24Hours = "";
    String strNewTaskRaisedBy = "";
    String strNewTaskReference = "";
    String strNewTaskCreatedDate = "";

    if (arlTaskDetails != null && !arlTaskDetails.isEmpty()) {
        strNewTaskId = AppUtil.checkNull(arlTaskDetails.get(0));
        strNewTaskDescription = AppUtil.checkNull(arlTaskDetails.get(1));
        strNewTaskPriority = AppUtil.checkNull(arlTaskDetails.get(2));
        strNewTaskType = AppUtil.checkNull(arlTaskDetails.get(3));
        strNewTaskModule = AppUtil.checkNull(arlTaskDetails.get(4));
        strNewAssignee = AppUtil.checkNull(arlTaskDetails.get(5));
        strCreateTaskRecDate = AppUtil.checkNull(arlTaskDetails.get(6));
        strNewReceivedHour = AppUtil.checkNull(arlTaskDetails.get(7));
        strNewReceivedMinute = AppUtil.checkNull(arlTaskDetails.get(8));
        strNewReceived24Hours = AppUtil.checkNull(arlTaskDetails.get(9));
        strNewTaskRaisedBy = AppUtil.checkNull(arlTaskDetails.get(10));
        strNewTaskCreatedDate = AppUtil.checkNull(arlTaskDetails.get(11));
    }

    if (strNewAssignee.isEmpty()) {
        strNewAssignee = strUserId;
    }

    if (strCreateTaskRecDate.isEmpty()) {
        strCreateTaskRecDate = strCurrentDateDDMMYYYY;
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

        function saveTask() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.TaskId.value)) {
                    OPTDialog("Enter Task Id", document.OPTForm.TaskId);
                    return;
                }
                if (/[^a-zA-Z0-9\-]/.test(document.OPTForm.TaskId.value)) {
                    OPTDialog("Special Characters Not Allowed in Task Id", document.OPTForm.TaskId);
                    return;
                }

                if (isEmpty(document.OPTForm.TaskDescription.value)) {
                    OPTDialog("Enter Task Description", document.OPTForm.TaskDescription);
                    return;
                }
                if (document.OPTForm.TaskPriority.options.selectedIndex == 0) {
                    OPTDialog("Select Task Priority", document.OPTForm.TaskPriority);
                    return;
                }
                if (document.OPTForm.TaskModule.options.selectedIndex == 0) {
                    OPTDialog("Select Task Module", document.OPTForm.TaskModule);
                    return;
                }
                if (document.OPTForm.TaskRaisedBy.options.selectedIndex == 0) {
                    OPTDialog("Select Task Raised By", document.OPTForm.TaskRaisedBy);
                    return;
                }
                if (document.OPTForm.Assignee.options.selectedIndex == 0) {
                    OPTDialog("Select Assignee", document.OPTForm.Assignee);
                    return;
                }
                if (isEmpty(document.OPTForm.CreateTaskRecDate.value)) {
                    OPTDialog("Enter Received Date", document.OPTForm.CreateTaskRecDate);
                    return;
                }

                var enteredDate = getDefaultDateFormat(document.OPTForm.CreateTaskRecDate.value, "DD/MM/YYYY");
                var enteredDateTime = enteredDate + " " + document.OPTForm.ReceivedHour.value + ":" + document.OPTForm.ReceivedMinute.value + " " + document.OPTForm.Received24Hours.value;
                var enteredDateValue = Date.parse(enteredDateTime);
                var systemDateValue = Date.parse(new Date());
                if (enteredDateValue > systemDateValue) {
                    OPTDialog("Received Date Should Not Be Greater Than Current Date", document.OPTForm.CreateTaskRecDate);
                    return;
                }

                if (isEmpty(document.OPTForm.CreatedTaskDate.value)) {
                    OPTDialog("Enter Created Date", document.OPTForm.CreatedTaskDate);
                    return;
                }
                var createdDate = getDefaultDateFormat(document.OPTForm.CreatedTaskDate.value, "DD/MM/YYYY");
                var createdDateValue = Date.parse(createdDate);
                if (createdDateValue > systemDateValue) {
                    OPTDialog("Created Date Should Not Be Greater Than Current Date", document.OPTForm.CreatedTaskDate);
                    return;
                }

                if (enteredDateValue < createdDateValue) {
                    OPTDialog("Received Date Should Not Be Lesser Than Created Date", document.OPTForm.CreateTaskRecDate)
                    return;
                }
                document.OPTForm.action = "saveTask";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.TaskId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jConfirm("Task Created Successfully.\nDo you want to create another Task?", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                    else {
                        document.OPTForm.TaskId.focus();
                    }
                });

            }
            else if (varResult == "TASKEXISTS") {
                OPTDialog("Task Id already exists", document.OPTForm.TaskId);
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Create New Task
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=3 border="0">
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Task Id</td>
                        <td width="82%" colspan="3">
                            <input type="text" name="TaskId" size="30" maxlength="30" class="txtgrey"
                                   value="<%= strNewTaskId %>">
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Task Description</td>
                        <td width="82%" colspan="3">
                                        <textarea name="TaskDescription" rows="3" cols="120" maxlength="490"
                                                  class="txtgrey"><%= strNewTaskDescription %></textarea>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Task Priority</td>
                        <td width="32%">
                            <select name="TaskPriority" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strTaskPriority : AppConstants.TICKET_PRIORITY) {
                                        strSelected = "";
                                        if (strNewTaskPriority.equalsIgnoreCase(strTaskPriority)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strTaskPriority %>" <%= strSelected %>><%= strTaskPriority %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                        <td width="18%" class="txtgreybold">Task Module</td>
                        <td width="32%">
                            <select name="TaskModule" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strTaskModule : AppConstants.TICKET_MODULE) {
                                        strSelected = "";
                                        if (strNewTaskModule.equalsIgnoreCase(strTaskModule)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strTaskModule %>" <%= strSelected %>><%= strTaskModule %>
                                </option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td width="18%" class="txtgreybold">Task Raised By</td>
                        <td width="32%">
                            <select name="TaskRaisedBy" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    for (String strTaskRaisedBy : AppConstants.TICKET_RAISED_BY) {
                                        strSelected = "";
                                        if (strNewTaskRaisedBy.equalsIgnoreCase(strTaskRaisedBy)) {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strTaskRaisedBy %>" <%= strSelected %>><%= strTaskRaisedBy %>
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
                            <input type="text" name="CreateTaskRecDate" value="<%= strCreateTaskRecDate %>"
                                   size="10"
                                   maxlength="20" class="txtgrey"
                                   onblur="checkValidDateForObject(this)">
                            <a href="javascript:showCal('Calendar3','dd/mm/yyyy');"><img
                                    src="images/cal.gif" width="20"
                                    height="13" border="0"
                                    alt="Pick a date"></a>
                        </td>
                        <td width="18%" class="txtgreybold">Created Date</td>
                        <td width="32%">
                            <input type="text" name="CreatedTaskDate" value="<%= strNewTaskCreatedDate %>"
                                   size="10"
                                   maxlength="20" class="txtgrey"
                                   onblur="checkValidDateForObject(this)">
                            <a href="javascript:showCal('Calendar10','dd/mm/yyyy');"><img
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
</form>
</body>
</html>