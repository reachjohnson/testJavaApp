<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlDomainNames = (ArrayList) request.getAttribute("DomainNames");
    ArrayList<String> arlTaskDetailsForClosing = (ArrayList) request.getAttribute("TaskDetailsForClosing");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strBugToMs = AppUtil.checkNull((String) request.getAttribute("NEWBUGTOMS"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strModCurrTickAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    String strFixingAssigneeName = AppUtil.checkNull((String) request.getAttribute("FixingAssigneeName"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    String[] strTaskResolutionTypes = {};
    String strRefNo = "";
    String strTaskId = "";
    String strTaskDescription = "";
    String strTaskCategory = "";
    String strTaskPriority = "";
    String strTaskType = "";
    String strTaskModule = "";
    String strTaskRaisedBy = "";
    String strAssignee = "";
    String strReceivedDate = "";
    String strSLAEndDate = "";
    String strSLAMissed = "";
    if (arlTaskDetailsForClosing != null && !arlTaskDetailsForClosing.isEmpty())
    {
        strRefNo = AppUtil.checkNull(arlTaskDetailsForClosing.get(0));
        strTaskId = AppUtil.checkNull(arlTaskDetailsForClosing.get(1));
        strTaskDescription = AppUtil.checkNull(arlTaskDetailsForClosing.get(2));
        strTaskPriority = AppUtil.checkNull(arlTaskDetailsForClosing.get(3));
        strTaskType = AppUtil.checkNull(arlTaskDetailsForClosing.get(4));
        strTaskModule = AppUtil.checkNull(arlTaskDetailsForClosing.get(5));
        strTaskRaisedBy = AppUtil.checkNull(arlTaskDetailsForClosing.get(6));
        strAssignee = AppUtil.checkNull(arlTaskDetailsForClosing.get(7));
        strReceivedDate = AppUtil.checkNull(arlTaskDetailsForClosing.get(8));
        strSLAEndDate = AppUtil.checkNull(arlTaskDetailsForClosing.get(9));
        strSLAMissed = AppUtil.checkNull(arlTaskDetailsForClosing.get(10));

        strTaskResolutionTypes = AppConstants.TASK_RESOLUTION;
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        var TaskOtherTeams = "<%= AppConstants.TASK_OTHER_TEAMS %>";
        var SLAMissed = "<%= strSLAMissed %>";

        function TaskResolutionOnChange() {
            if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMoved.value ||
                    document.OPTForm.TaskResolution.value == document.OPTForm.EnquiryTaskMoved.value ||
                    document.OPTForm.TaskResolution.value == document.OPTForm.TaskMovedOtherTeam.value) {
                document.OPTForm.MovedDomain.disabled = false;
            }
            else {
                document.OPTForm.MovedDomain.selectedIndex = 0;
                document.OPTForm.MovedDomain.disabled = true;
            }
            if (document.OPTForm.TaskResolution.value == document.OPTForm.FixExpectedByMSTeam.value) {
                document.OPTForm.FixingAssignee.disabled = false;
            }
            else {
                document.OPTForm.FixingAssignee.selectedIndex = 0;
                document.OPTForm.FixingAssignee.disabled = true;
            }

            if (document.OPTForm.TaskResolution.value == document.OPTForm.EnquiryTaskMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Are you sure "This is Enquiry Task moved to - ' + document.OPTForm.MovedDomain.value + '"', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        document.OPTForm.TaskResolution.selectedIndex = 0;
                        document.OPTForm.MovedDomain.selectedIndex = 0;
                        document.OPTForm.MovedDomain.disabled = true;
                        document.OPTForm.TaskResolution.focus();
                    }
                });
            }
            if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMovedOtherTeam.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0 &&
                    TaskOtherTeams.indexOf(document.OPTForm.MovedDomain.value) < 0) {
                OPTDialog('Task can be moved only to "' + TaskOtherTeams + '"', document.OPTForm.MovedDomain);
                document.OPTForm.MovedDomain.selectedIndex = 0;
                return;
            }

            if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Have you already converted to Bug and moved to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strTaskId %>', '<%= strTaskId %>')
                    }
                });
            }
        }

        function DomainOnChange() {
            if (document.OPTForm.TaskResolution.value == document.OPTForm.EnquiryTaskMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Are you sure "This is Enquiry Task moved to - ' + document.OPTForm.MovedDomain.value + '"', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        document.OPTForm.TaskResolution.selectedIndex = 0;
                        document.OPTForm.MovedDomain.selectedIndex = 0;
                        document.OPTForm.MovedDomain.disabled = true;
                        document.OPTForm.TaskResolution.focus();
                    }
                });
            }
            if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMovedOtherTeam.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0 &&
                    TaskOtherTeams.indexOf(document.OPTForm.MovedDomain.value) < 0) {
                OPTDialog('Task can be moved only to "' + TaskOtherTeams + '"', document.OPTForm.MovedDomain);
                document.OPTForm.MovedDomain.selectedIndex = 0;
                return;
            }

            if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Have you already converted to Bug and moved to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strTaskId %>', '<%= strTaskId %>')
                    }
                });
            }
        }


        function closeTask() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.TaskResolution.options.selectedIndex == 0) {
                    OPTDialog("Select Task Resolution", document.OPTForm.TaskResolution);
                    return;
                }
                if (document.OPTForm.TaskResolution.value == document.OPTForm.TaskMoved.value ||
                        document.OPTForm.TaskResolution.value == document.OPTForm.EnquiryTaskMoved.value ||
                        document.OPTForm.TaskResolution.value == document.OPTForm.TaskMovedOtherTeam.value) {
                    if (document.OPTForm.MovedDomain.options.selectedIndex == 0) {
                        OPTDialog("Select Moved Domain", document.OPTForm.MovedDomain);
                        return;
                    }
                }
                if (document.OPTForm.TaskSeverity.options.selectedIndex == 0) {
                    OPTDialog("Select Task Severity", document.OPTForm.TaskSeverity);
                    return;
                }

                if (OOSLA_HIGHLIGHT && SLAMissed == "1") {
                    if (isEmpty(document.OPTForm.OOSLAJustification.value)) {
                        OPTDialog("Enter OOSLA Justification", document.OPTForm.OOSLAJustification);
                        return;
                    }
                }
                if (isEmpty(document.OPTForm.TaskComments.value)) {
                    OPTDialog("Enter Closing Comments", document.OPTForm.TaskComments);
                    return;
                }

                if (document.OPTForm.TaskResolution.value == document.OPTForm.FixExpectedByMSTeam.value) {
                    if (document.OPTForm.FixingAssignee.options.selectedIndex == 0) {
                        OPTDialog("Select Assignee For Fixing (Converting Bug)", document.OPTForm.FixingAssignee);
                        return;
                    }
                }
                showFullPageMask(true);
                jConfirm('All JIRA changes done for this Task ? - "' + document.OPTForm.hidTaskId.value + '"', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strTaskId %>', '<%= strTaskId %>')
                    }
                    else {
                        document.OPTForm.action = "closeTask";
                        frmWriteSubmit();
                    }
                });
            }
        }

        function displaymessage() {
            document.OPTForm.TaskResolution.focus();

            var varResult = "<%= strResult.toUpperCase() %>";
            var bugtoms = "<%= strBugToMs.toUpperCase() %>";
            var fixingAssigneeName = "<%= strFixingAssigneeName %>"
            var fromPage = "<%= strFromPage.toUpperCase() %>";

            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Task Closed Successfully", JavaScriptInfo, function (retval) {
                    if (bugtoms == "NEWBUGTOMS") {
                        jAlert("New Bug was created for Fixing Team and assigned to \"" + fixingAssigneeName + "\"", JavaScriptInfo, function (retval) {
                            showFullPageMask(false);
                            if (fromPage == "HOMEPAGE") {
                                document.OPTForm.action = "homePage";
                            }
                            else if (fromPage == "MODIFYCURRENTTICKETS") {
                                document.OPTForm.action = "loadModifyCurrentTickets";
                            }
                            frmReadSubmit();
                        });
                    }
                    else {
                        showFullPageMask(false);
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
                    OPTDialog("This Task is Already OOSLA. Enter Proper Justification In Comments.", document.OPTForm.TaskResolution);
                }
            }
        }
    </script>


</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidTaskId" value="<%= strTaskId %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="Assignee" value="<%= strModCurrTickAssignee %>">
    <input type="hidden" name="closepage" value="Task">
    <input type="hidden" name="TaskMoved" value="<%= AppConstants.TASK_CONVERTED_BUG_MOVED %>">
    <input type="hidden" name="EnquiryTaskMoved" value="<%= AppConstants.ENQUIRY_TASK_MOVED %>">
    <input type="hidden" name="TaskMovedOtherTeam" value="<%= AppConstants.TASK_MOVED %>">
    <input type="hidden" name="FixExpectedByMSTeam" value="<%= AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Close Task
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Task Id</td>
                                    <td width="82%" class="txtblue"><%= strTaskId %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" colspan="2" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Task Description</td>
                                    <td width="82%" class="txtblue"><%= strTaskDescription %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Task Priority</td>
                                    <td width="64%" class="txtblue"><%= strTaskPriority %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Task Type</td>
                                    <td width="64%" class="txtblue"><%= strTaskType %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Task Module</td>
                                    <td width="64%" class="txtblue"><%= strTaskModule %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Task Raised By</td>
                                    <td width="64%" class="txtblue"><%= strTaskRaisedBy %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%
                        String strClass = "txtblue";
                        if (strSLAMissed.equalsIgnoreCase("1"))
                        {
                            strClass = OOSLA_RED_TEXT;
                        }
                    %>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Received Date</td>
                                    <td width="64%" class="<%= strClass %>"><%= strReceivedDate %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">SLA End Date</td>
                                    <td width="64%" class="<%= strClass %>"><%= strSLAEndDate %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Task Resolution</td>
                                    <td width="64%" class="txtgrey">
                                        <select name="TaskResolution" class="txtgrey"
                                                onchange="JavaScript:TaskResolutionOnChange()">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskResolution : strTaskResolutionTypes)
                                                {
                                            %>
                                            <option value="<%= strTaskResolution %>"><%= strTaskResolution %>
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
                                    <td width="36%" class="txtgreybold">Moved Team</td>
                                    <td width="64%" class="txtgrey">
                                        <select name="MovedDomain" class="txtgrey" disabled="true" onchange="DomainOnChange();">
                                            <option value="">Select</option>
                                            <%
                                                if (arlDomainNames != null && !arlDomainNames.isEmpty())
                                                {
                                                    for (String strDomainName : arlDomainNames)
                                                    {
                                            %>
                                            <option value="<%= strDomainName %>"><%= strDomainName %>
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
                                    <td width="18%" class="txtgreybold">Task Severity</td>
                                    <td width="82%" class="txtgrey">
                                        <select name="TaskSeverity" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strTaskSeverity : AppConstants.TICKET_SEVERITY)
                                                {
                                            %>
                                            <option value="<%= strTaskSeverity %>"><%= strTaskSeverity %>
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

                    <%
                        int intTextAreaLength = 4990;
                        strClass = "txtgreybold";
                        if (AppSettings.isOooslaHighlight() &&  strSLAMissed.equalsIgnoreCase("1"))
                        {
                            strClass = OOSLA_RED_TEXT;
                            intTextAreaLength -= 2990;
                    %>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">

                                    <td width="18%" class="txtgreybold">OOSLA Justification
                                    </td>
                                    <td width="82%">
                            <textarea name="OOSLAJustification" rows="5" cols="120" maxlength="2990"
                                      class="txtgrey"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">

                                    <td width="18%" class="txtgreybold">Closing Comments
                                    </td>
                                    <td width="82%">
                            <textarea name="TaskComments" rows="8" cols="120" maxlength="<%= intTextAreaLength %>"
                                      class="txtgrey"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Assignee For Fixing</td>
                                    <td width="82%" class="txtgrey">
                                        <select name="FixingAssignee" class="txtgrey" disabled="true">
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
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:closeTask()" id="buttonId0"/>
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