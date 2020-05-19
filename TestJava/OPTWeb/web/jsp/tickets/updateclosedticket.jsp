<%@ page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strUpdateClosedTicketId = AppUtil.checkNull((String) request.getAttribute("UpdateClosedTicketId"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlDomainNames = (ArrayList) request.getAttribute("DomainNames");
    ArrayList<String> arlReleaseVersions = (ArrayList) request.getAttribute("ReleaseVersions");
    ArrayList<String> arlClosedTicketDetailsForUpdate = (ArrayList) request.getAttribute("ClosedTicketDetailsForUpdate");
    String strRefNo = "";
    String strTicketCategory = "";
    String strTicketId = "";
    String strTicketReceivedDate = "";
    String strAssignee = "";
    String strAssigneeName = "";
    if (arlClosedTicketDetailsForUpdate != null && !arlClosedTicketDetailsForUpdate.isEmpty())
    {
        strRefNo = arlClosedTicketDetailsForUpdate.get(0);
        strTicketCategory = arlClosedTicketDetailsForUpdate.get(3);
        strTicketId = arlClosedTicketDetailsForUpdate.get(1);
        strTicketReceivedDate = arlClosedTicketDetailsForUpdate.get(23);
        strAssignee = arlClosedTicketDetailsForUpdate.get(7);
        strAssigneeName = arlClosedTicketDetailsForUpdate.get(22);
    }
    if (strResult.equalsIgnoreCase(AppConstants.SUCCESS))
    {
        arlClosedTicketDetailsForUpdate = null;
        strUpdateClosedTicketId = "";
    }

    List<String> lstResolutionTypes = new ArrayList<String>();
    if(arlClosedTicketDetailsForUpdate != null && !arlClosedTicketDetailsForUpdate.isEmpty())
    {
        if(arlClosedTicketDetailsForUpdate.get(3).equalsIgnoreCase(AppConstants.TASK))
        {
            lstResolutionTypes = Arrays.asList(AppConstants.TASK_RESOLUTION);
        }
        if(arlClosedTicketDetailsForUpdate.get(3).equalsIgnoreCase(AppConstants.BUG))
        {
            lstResolutionTypes = Arrays.asList(AppConstants.BUG_RESOLUTION);
        }
    }
    ArrayList<String> arlResolutionTypes = new ArrayList<String>();
    arlResolutionTypes.addAll(lstResolutionTypes);
    if(arlClosedTicketDetailsForUpdate != null && !arlClosedTicketDetailsForUpdate.isEmpty())
    {
        if(arlClosedTicketDetailsForUpdate.get(3).equalsIgnoreCase(AppConstants.TASK))
        {
            arlResolutionTypes.remove(AppConstants.TASK_CONVERTED_BUG_FIX_EXPECTED);
        }
        if(arlClosedTicketDetailsForUpdate.get(3).equalsIgnoreCase(AppConstants.BUG))
        {
            arlResolutionTypes.remove(AppConstants.BUG_FIX_EXPECTED);
        }
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
                loadClosedTicketDetailsForUpdate();
            }
        }

        var TaskOtherTeams = "<%= AppConstants.TASK_OTHER_TEAMS %>";

        function TicketResolutionOnChange() {
            if(document.OPTForm.hidTicketCategory.value == document.OPTForm.hidTask.value)
            {
                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.EnquiryTaskMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.TaskMovedOtherTeam.value) {
                    document.OPTForm.MovedDomain.disabled = false;
                }
                else {
                    document.OPTForm.MovedDomain.selectedIndex = 0;
                    document.OPTForm.MovedDomain.disabled = true;
                }

                if (document.OPTForm.TicketResolution.value == document.OPTForm.EnquiryTaskMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Are you sure "This is Enquiry Task moved to - ' + document.OPTForm.MovedDomain.value + '"', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            document.OPTForm.TicketResolution.selectedIndex = 0;
                            document.OPTForm.MovedDomain.selectedIndex = 0;
                            document.OPTForm.MovedDomain.disabled = true;
                            document.OPTForm.TicketResolution.focus();
                        }
                    });
                }
                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMovedOtherTeam.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0 &&
                        TaskOtherTeams.indexOf(document.OPTForm.MovedDomain.value) < 0) {
                    OPTDialog('Task can be moved only to "' + TaskOtherTeams + '"', document.OPTForm.MovedDomain);
                    document.OPTForm.MovedDomain.selectedIndex = 0;
                    return;
                }

                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Have you already converted to Bug and moved to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            openJIRAWindow('<%= AppConstants.JIRAURL + strTicketId %>', '<%= strTicketId %>')
                        }
                    });
                }
            }

            if(document.OPTForm.hidTicketCategory.value == document.OPTForm.hidBug.value)
            {
                if (document.OPTForm.TicketResolution.value == document.OPTForm.BugMoved.value) {
                    document.OPTForm.MovedDomain.disabled = false;
                }
                else {
                    document.OPTForm.MovedDomain.selectedIndex = 0;
                    document.OPTForm.MovedDomain.disabled = true;
                }
                if (document.OPTForm.TicketResolution.value == document.OPTForm.CodeFixed.value) {
                    document.OPTForm.TicketRootCause.disabled = false;
                    document.OPTForm.ReleaseVersion.disabled = false;
                }
                else {
                    document.OPTForm.TicketRootCause.selectedIndex = 0;
                    document.OPTForm.TicketRootCause.disabled = true;
                    document.OPTForm.ReleaseVersion.selectedIndex = 0;
                    document.OPTForm.ReleaseVersion.disabled = true;
                }
                if (document.OPTForm.TicketResolution.value == document.OPTForm.BugMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Have you already moved this Bug to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            openJIRAWindow('<%= AppConstants.JIRAURL + strTicketId %>', '<%= strTicketId %>')
                        }
                    });
                }
            }
        }

        function DomainOnChange() {
            if(document.OPTForm.hidTicketCategory.value == document.OPTForm.hidTask.value)
            {
                if (document.OPTForm.TicketResolution.value == document.OPTForm.EnquiryTaskMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Are you sure "This is Enquiry Task moved to - ' + document.OPTForm.MovedDomain.value + '"', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            document.OPTForm.TicketResolution.selectedIndex = 0;
                            document.OPTForm.MovedDomain.selectedIndex = 0;
                            document.OPTForm.MovedDomain.disabled = true;
                            document.OPTForm.TicketResolution.focus();
                        }
                    });
                }
                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMovedOtherTeam.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0 &&
                        TaskOtherTeams.indexOf(document.OPTForm.MovedDomain.value) < 0) {
                    OPTDialog('Task can be moved only to "' + TaskOtherTeams + '"', document.OPTForm.MovedDomain);
                    document.OPTForm.MovedDomain.selectedIndex = 0;
                    return;
                }

                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Have you already converted to Bug and moved to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            openJIRAWindow('<%= AppConstants.JIRAURL + strTicketId %>', '<%= strTicketId %>')
                        }
                    });
                }
            }

            if(document.OPTForm.hidTicketCategory.value == document.OPTForm.hidBug.value)
            {
                if (document.OPTForm.TicketResolution.value == document.OPTForm.BugMoved.value &&
                        document.OPTForm.MovedDomain.selectedIndex > 0) {
                    showFullPageMask(true);
                    jConfirm('Have you already moved this Bug to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                        showFullPageMask(false);
                        if (!retval) {
                            openJIRAWindow('<%= AppConstants.JIRAURL + strTicketId %>', '<%= strTicketId %>')
                        }
                    });
                }
            }

        }

        function loadClosedTicketDetailsForUpdate() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.TicketId.value)) {
                    OPTDialog("Enter Ticket Id", document.OPTForm.TicketId);
                    return;
                }
                if (/[^a-zA-Z0-9\-]/.test(document.OPTForm.TicketId.value)) {
                    OPTDialog("Special Characters Not Allowed in Ticket Id", document.OPTForm.TicketId);
                    return;
                }
                document.OPTForm.action = "loadUpdateClosedTicket";
                frmReadSubmit();
            }
        }

        function UpdateClosedTicket() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.TicketDescription.value)) {
                    OPTDialog("Enter Ticket Description", document.OPTForm.TicketDescription);
                    return;
                }

                if (document.OPTForm.TicketPriority.options.selectedIndex == 0) {
                    OPTDialog("Select Ticket Priority", document.OPTForm.TicketPriority);
                    return;
                }

                if (document.OPTForm.TicketModule.options.selectedIndex == 0) {
                    OPTDialog("Select Ticket Module", document.OPTForm.TicketModule);
                    return;
                }

                if (document.OPTForm.TicketRaisedBy.options.selectedIndex == 0) {
                    OPTDialog("Select Ticket Raised By", document.OPTForm.TicketRaisedBy);
                    return;
                }

                if (document.OPTForm.TicketResolution.options.selectedIndex == 0) {
                    OPTDialog("Select Task Resolution", document.OPTForm.TicketResolution);
                    return;
                }

                if (document.OPTForm.TicketResolution.value == document.OPTForm.TaskMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.EnquiryTaskMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.BugMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.TaskMovedOtherTeam.value) {
                    if (document.OPTForm.MovedDomain.options.selectedIndex == 0) {
                        OPTDialog("Select Moved Domain", document.OPTForm.MovedDomain);
                        return;
                    }
                }

                if (document.OPTForm.TicketResolution.value == document.OPTForm.CodeFixed.value) {
                    if (document.OPTForm.TicketRootCause.options.selectedIndex == 0) {
                        OPTDialog("Select Ticket Root Cause", document.OPTForm.TicketRootCause);
                        return;
                    }
                    if (document.OPTForm.ReleaseVersion.options.selectedIndex == 0) {
                        OPTDialog("Select Release Version for this code fix", document.OPTForm.ReleaseVersion);
                        return;
                    }
                }

                if (isEmpty(document.OPTForm.ClosingComments.value)) {
                    OPTDialog("Enter Closing Comments", document.OPTForm.ClosingComments);
                    return;
                }

                if (isEmpty(document.OPTForm.TicketCreatedDate.value)) {
                    OPTDialog("Enter Ticket Created Date", document.OPTForm.TicketCreatedDate);
                    return;
                }
                var systemDateValue = Date.parse(new Date());
                var createdDate = getDefaultDateFormat(document.OPTForm.TicketCreatedDate.value, "DD/MM/YYYY");
                var createdDateValue = Date.parse(createdDate);
                var receivedDate = getDefaultDateFormat(document.OPTForm.TicketReceivedDate.value, "DD/MM/YYYY");
                var receivedDateValue = Date.parse(receivedDate);

                if (createdDateValue > systemDateValue) {
                    OPTDialog("Created Date Should Not Be Greater Than Current Date", document.OPTForm.TicketCreatedDate);
                    return;
                }
                if (createdDateValue > receivedDateValue) {
                    OPTDialog("Created Date Should Not Be Greater Than Received Date", document.OPTForm.TicketCreatedDate);
                    return;
                }
                document.OPTForm.action = "UpdateClosedTicket";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.TicketId.focus();
            if (!isEmpty(document.OPTForm.TicketId.value))
            {
                if(document.OPTForm.TicketResolution.value == document.OPTForm.TaskMovedOtherTeam.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.TaskMoved.value ||
                        document.OPTForm.TicketResolution.value == document.OPTForm.BugMoved.value)
                {
                    document.OPTForm.MovedDomain.disabled = false;
                }
                if (document.OPTForm.TicketResolution.value == document.OPTForm.CodeFixed.value) {
                    document.OPTForm.TicketRootCause.disabled = false;
                    document.OPTForm.ReleaseVersion.disabled = false;
                }
            }

            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Closed Ticket Updated Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                });
            }
        }
    </script>
</head>
<body onload="displaymessage()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidTicketId" value="<%= strTicketId %>">
    <input type="hidden" name="hidTicketCategory" value="<%= strTicketCategory %>">
    <input type="hidden" name="Assignee" value="<%= strAssignee %>">
    <input type="hidden" name="AssigneeName" value="<%= strAssigneeName %>">
    <input type="hidden" name="hidTask" value="<%= AppConstants.TASK %>">
    <input type="hidden" name="hidBug" value="<%= AppConstants.BUG %>">
    <input type="hidden" name="TaskMoved" value="<%= AppConstants.TASK_CONVERTED_BUG_MOVED %>">
    <input type="hidden" name="EnquiryTaskMoved" value="<%= AppConstants.ENQUIRY_TASK_MOVED %>">
    <input type="hidden" name="TaskMovedOtherTeam" value="<%= AppConstants.TASK_MOVED %>">
    <input type="hidden" name="BugMoved" value="<%= AppConstants.BUG_MOVED %>">
    <input type="hidden" name="CodeFixed" value="<%= AppConstants.CODE_FIXED_DELIVERED %>">
    <input type="hidden" name="FixExpectedByMSTeam" value="<%= AppConstants.BUG_FIX_EXPECTED %>">
    <input type="hidden" name="TicketReceivedDate" value="<%= strTicketReceivedDate %>">

    <table width="99%">
        <tr>
            <td width="2%">&nbsp;</td>
            <td width="55%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Search Closed Ticket to Update
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">&nbsp;&nbsp;Ticket Id</td>
                                    <td width="40%"><input type="text" name="TicketId" size="30" maxlength="30" class="txtgrey"
                                                           value="<%= strUpdateClosedTicketId %>" onkeypress="ROTtrackEnterKey(event)">
                                    </td>
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search"
                                               onclick="JavaScript:loadClosedTicketDetailsForUpdate()" id="buttonId0"/>
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
        if (strUpdateClosedTicketId.length() > 0)
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
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Closed Ticket Details For Update
                                    </td>
                                </tr>
                            </table>
                            <%
                                if (arlClosedTicketDetailsForUpdate != null && !arlClosedTicketDetailsForUpdate.isEmpty())
                                {
                            %>
                            <table width="100%" cellpadding=0 cellspacing=1 border=0 align="center">
                                <tr>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Id</td>
                                                <td width="64%" class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(1) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Category</td>
                                                <td width="64%" class="txtgrey">
                                                    <%= arlClosedTicketDetailsForUpdate.get(3) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="100%" colspan="2">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtgreybold">Ticket Description</td>
                                                <td width="82%" class="txtgrey">
                            <textarea name="TicketDescription" rows="6" cols="120" maxlength="4990"
                                      class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(2) %></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Priority</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketPriority" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            String strSelected;
                                                            for (String strTaskPriority : AppConstants.TICKET_PRIORITY)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(4).equalsIgnoreCase(strTaskPriority))
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
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Type</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketType" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strTaskType : AppConstants.TICKET_TYPE)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(5).equalsIgnoreCase(strTaskType))
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
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Module</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketModule" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strTaskModule : AppConstants.TICKET_MODULE)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(6).equalsIgnoreCase(strTaskModule))
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
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Raised By</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketRaisedBy" class="txtgrey">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strTaskRaisedBy : AppConstants.TICKET_RAISED_BY)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(16).equalsIgnoreCase(strTaskRaisedBy))
                                                                {
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
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Assignee</td>
                                                <td width="64%" class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(22) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Received Date</td>
                                                <td width="64%" class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(13) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Planned End Date</td>
                                                <td width="64%" class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(14) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Actual End Date</td>
                                                <td width="64%" class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(15) %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Resolution</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketResolution" class="txtgrey"
                                                            onchange="JavaScript:TicketResolutionOnChange()">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strTaskResolution : arlResolutionTypes)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(9).equalsIgnoreCase(strTaskResolution))
                                                                {
                                                                    strSelected = "selected";
                                                                }
                                                        %>
                                                        <option value="<%= strTaskResolution %>" <%= strSelected%>><%= strTaskResolution %>
                                                        </option>
                                                        <%

                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Moved Domain</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="MovedDomain" class="txtgrey" disabled="true" onchange="DomainOnChange();">
                                                        <option value="">Select</option>
                                                        <%
                                                            if (arlDomainNames != null && !arlDomainNames.isEmpty())
                                                            {
                                                                for (String strDomainName : arlDomainNames)
                                                                {
                                                                    strSelected = "";
                                                                    if (arlClosedTicketDetailsForUpdate.get(10).equalsIgnoreCase(strDomainName))
                                                                    {
                                                                        strSelected = "selected";
                                                                    }
                                                        %>
                                                        <option value="<%= strDomainName %>" <%= strSelected %>><%= strDomainName %>
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
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Ticket Root Cause</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="TicketRootCause" class="txtgrey" disabled="true">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strRootCause : AppConstants.BUG_ROOTCAUSE)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(11).equalsIgnoreCase(strRootCause))
                                                                {
                                                                    strSelected = "selected";
                                                                }
                                                        %>
                                                        <option value="<%= strRootCause %>" <%= strSelected %>><%= strRootCause %>
                                                        </option>
                                                        <%

                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Release Version</td>
                                                <td width="64%" class="txtgrey">
                                                    <select name="ReleaseVersion" class="txtgrey" disabled="true">
                                                        <option value="">Select</option>
                                                        <%
                                                            for (String strReleaseVersion : arlReleaseVersions)
                                                            {
                                                                strSelected = "";
                                                                if (arlClosedTicketDetailsForUpdate.get(24).equalsIgnoreCase(strReleaseVersion))
                                                                {
                                                                    strSelected = "selected";
                                                                }
                                                        %>
                                                        <option value="<%= strReleaseVersion %>" <%= strSelected %>><%= strReleaseVersion %>
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
                                    <td width="100%" colspan="2">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtgreybold">Closing Comments</td>
                                                <td width="82%" class="txtgrey">
                            <textarea name="ClosingComments" rows="8" cols="120" maxlength="4990"
                                      class="txtgrey"><%= arlClosedTicketDetailsForUpdate.get(12) %></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td width="100%" colspan="2">
                                        <table width="100%">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtgreybold">Ticket Created Date</td>
                                                <td width="82%" class="txtgrey">
                                                    <input type="text" name="TicketCreatedDate" value="<%= arlClosedTicketDetailsForUpdate.get(17) %>" size="10"
                                                           maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)">
                                                    <a href="javascript:showCal('Calendar22','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                                                  width="20"
                                                                                                                  height="13" border="0"
                                                                                                                  alt="Pick a date"></a>
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
    </table>

    <%
        if (arlClosedTicketDetailsForUpdate != null && !arlClosedTicketDetailsForUpdate.isEmpty())
        {
    %>
    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:UpdateClosedTicket()" id="buttonId1"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Reset" onclick="JavaScript:frmCancel()" id="buttonId2"/>
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
