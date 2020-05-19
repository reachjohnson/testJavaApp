<%@ include file="../common/noCache.jsp" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%
    ArrayList<String> arlTaskDetails = (ArrayList) request.getAttribute("TaskDetails");

    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));

    String strSelected;

    String strRefNo = "";
    String strNewTaskId = "";
    String strNewTaskDescription = "";
    String strNewTaskPriority = "";
    String strNewTaskType = "";
    String strNewTaskModule = "";
    String strNewTaskRaisedBy = "";
    String strNewAssignee = "";
    String strNewReceivedDate = "";
    String strNewSLAEndDate = "";
    String strSLAMissed = "";
    String strETA = "";
    if (arlTaskDetails != null && !arlTaskDetails.isEmpty())
    {
        strRefNo = AppUtil.checkNull(arlTaskDetails.get(0));
        strNewTaskId = AppUtil.checkNull(arlTaskDetails.get(1));
        strNewTaskDescription = AppUtil.checkNull(arlTaskDetails.get(2));
        strNewTaskPriority = AppUtil.checkNull(arlTaskDetails.get(3));
        strNewTaskType = AppUtil.checkNull(arlTaskDetails.get(4));
        strNewTaskModule = AppUtil.checkNull(arlTaskDetails.get(5));
        strNewTaskRaisedBy = AppUtil.checkNull(arlTaskDetails.get(6));
        strNewAssignee = AppUtil.checkNull(arlTaskDetails.get(7));
        strNewReceivedDate = AppUtil.checkNull(arlTaskDetails.get(8));
        strNewSLAEndDate = AppUtil.checkNull(arlTaskDetails.get(9));
        strSLAMissed = AppUtil.checkNull(arlTaskDetails.get(10));
        strETA = AppUtil.checkNull(arlTaskDetails.get(12));
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function updateTask() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
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
                if (!isEmpty(document.OPTForm.TaskETADate.value)) {
                    if (!compareTwoDates(document.OPTForm.TaskETADate.value, SystemDate, "DD/MM/YYYY", "LESSER")) {
                        OPTDialog("Task ETA Date should be equal to or greater than system date", document.OPTForm.TaskETADate);
                        return;
                    }
                }
                if (isEmpty(document.OPTForm.ReasonForUpdate.value)) {
                    OPTDialog("Enter Reason For Update", document.OPTForm.ReasonForUpdate);
                    return;
                }

                document.OPTForm.action = "updateTask";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.TaskDescription.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            var SLAMissed = "<%= strSLAMissed %>";
            var fromPage = "<%= strFromPage.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Task Updated Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        if (fromPage == "HOMEPAGE") {
                            document.OPTForm.action = "homePage";
                        }
                        else if (fromPage == "MODIFYCURRENTTICKETS") {
                            document.OPTForm.action = "loadModifyCurrentTickets";
                        }
                        frmReadSubmit();
                    }
                });
            }
            if (OOSLA_HIGHLIGHT == "true") {
                if (SLAMissed == "1") {
                    OPTDialog("This Task is Already OOSLA", document.OPTForm.TaskDescription);
                }
            }
        }
    </script>


</head>
<body onload="JavaScript:displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidTaskId" value="<%= strNewTaskId %>">
    <input type="hidden" name="hidUserName" value="<%= strUserName %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="Assignee" value="<%= strAssignee %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Task
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Task Id</td>
                                    <td width="82%" class="txtgreybold"><%= strNewTaskId %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Task Description</td>
                                    <td width="82%">
                    <textarea name="TaskDescription" rows="6" cols="120" maxlength="490"
                              class="txtgrey"><%= strNewTaskDescription %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Task Priority</td>
                                    <td width="64%">
                                        <select name="TaskPriority" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskPriority : AppConstants.TICKET_PRIORITY)
                                                {
                                                    strSelected = "";
                                                    if (strNewTaskPriority.equalsIgnoreCase(strTaskPriority))
                                                    {
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
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Task Type</td>
                                    <td width="64%">
                                        <select name="TaskType" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskType : AppConstants.TICKET_TYPE)
                                                {
                                                    strSelected = "";
                                                    if (strNewTaskType.equalsIgnoreCase(strTaskType))
                                                    {
                                                        strSelected = "selected";
                                                    }
                                            %>
                                            <option value="<%= strTaskType %>" <%= strSelected %>><%= strTaskType %>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Task Module</td>
                                    <td width="64%">
                                        <select name="TaskModule" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskModule : AppConstants.TICKET_MODULE)
                                                {
                                                    strSelected = "";
                                                    if (strNewTaskModule.equalsIgnoreCase(strTaskModule))
                                                    {
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
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Task Raised By</td>
                                    <td width="64%">
                                        <select name="TaskRaisedBy" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskRaisedBy : AppConstants.TICKET_RAISED_BY)
                                                {
                                                    strSelected = "";
                                                    if (strNewTaskRaisedBy.equalsIgnoreCase(strTaskRaisedBy))
                                                    {
                                                        strSelected = "selected";
                                                    }
                                            %>
                                            <option value="<%= strTaskRaisedBy %>" <%= strSelected%>><%= strTaskRaisedBy %>
                                            </option>
                                            <%
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
                                    <td width="18%" class="txtgreybold">Task ETA</td>
                                    <td width="82%" class="txtgrey">
                                        <input type="text" name="TaskETADate" value="<%= strETA %>" size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)">
                                        <a href="javascript:showCal('Calendar18','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                                      width="20"
                                                                                                      height="13" border="0"
                                                                                                      alt="Pick a date"></a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>


                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Assignee</td>
                                    <td width="82%" class="txtgrey"><%= strNewAssignee %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%
                        String strClass = "txtgrey";
                        if (strSLAMissed.equalsIgnoreCase("1"))
                        {
                            strClass = OOSLA_RED_TEXT;
                        }
                    %>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Received Date</td>
                                    <td width="64%" class="<%= strClass %>"><%= strNewReceivedDate %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">SLA End Date</td>
                                    <td width="64%" class="<%= strClass %>"><%= strNewSLAEndDate %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Reason For Update</td>
                                    <td width="82%">
                    <textarea name="ReasonForUpdate" rows="6" cols="120" maxlength="4900"
                              class="txtgrey"></textarea>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:updateTask()" id="buttonId0"/>
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