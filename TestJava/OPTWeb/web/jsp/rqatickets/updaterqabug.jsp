<%@ include file="../common/noCache.jsp" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%
    ArrayList<String> arlRQABugDetails = (ArrayList) request.getAttribute("RQABugDetails");

    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("ModifyRQATicketAssignee"));

    String strSelected = "";
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function updateRQABug() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
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
                if (isEmpty(document.OPTForm.UpdateRQABugETADate.value)) {
                    OPTDialog("Enter ETA Date", document.OPTForm.UpdateRQABugETADate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.UpdateRQABugETADate.value, SystemDate, "DD/MM/YYYY", "LESSER")) {
                    OPTDialog("ETA Date should not be lesser than Current Date", document.OPTForm.UpdateRQABugETADate);
                    return false;
                }

                document.OPTForm.action = "updateRQABug";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.BugDescription.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("RQA Bug Updated Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        var fromPage = "<%= strFromPage %>";

                        if (fromPage == "HomePage") {
                            document.OPTForm.action = "homePage";
                        }
                        else if (fromPage == "ModifyRQATickets") {
                            document.OPTForm.action = "loadModifyRQATickets";
                        }
                        frmReadSubmit();
                    }
                });
            }
        }
    </script>

</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= arlRQABugDetails.get(0) %>">
    <input type="hidden" name="hidTicketId" value="<%= arlRQABugDetails.get(3) %>">
    <input type="hidden" name="hidUserName" value="<%= strUserName %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="ModifyRQATicketAssignee" value="<%= strAssignee %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update RQA Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">RQA Phase</td>
                                    <td width="64%" class="txtgrey"><%= arlRQABugDetails.get(1) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">RQA Cycle</td>
                                    <td width="64%" class="txtgrey"><%= arlRQABugDetails.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Bug Id</td>
                                    <td width="82%" class="txtgrey"><%= arlRQABugDetails.get(3) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Bug Description</td>
                                    <td width="82%">
                        <textarea name="BugDescription" rows="6" cols="120" maxlength="490"
                                  class="txtgrey"><%= arlRQABugDetails.get(4) %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Bug Priority</td>
                                    <td width="82%">
                                        <select name="BugPriority" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugPriority : AppConstants.RQA_TICKET_PRIORITY)
                                                {
                                                    strSelected = "";
                                                    if (arlRQABugDetails.get(5).equalsIgnoreCase(strBugPriority))
                                                    {
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
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Bug Module</td>
                                    <td width="64%">
                                        <select name="BugModule" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugModule : AppConstants.RQA_TICKET_MODULE)
                                                {
                                                    strSelected = "";
                                                    if (arlRQABugDetails.get(6).equalsIgnoreCase(strBugModule))
                                                    {
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
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Bug Raised By</td>
                                    <td width="64%">
                                        <select name="BugRaisedBy" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugRaisedBy : AppConstants.RQA_BUG_RAISED_BY)
                                                {
                                                    strSelected = "";
                                                    if (arlRQABugDetails.get(7).equalsIgnoreCase(strBugRaisedBy))
                                                    {
                                                        strSelected = "selected";
                                                    }
                                            %>
                                            <option value="<%= strBugRaisedBy %>" <%= strSelected%>><%= strBugRaisedBy %>
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
                                    <td width="18%" class="txtgreybold">Assignee</td>
                                    <td width="82%" class="txtgrey"><%= arlRQABugDetails.get(8) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Received Date</td>
                                    <td width="64%" class="txtgrey"><%= arlRQABugDetails.get(9) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">ETA Date</td>
                                    <td width="64%" class="txtgrey">
                                        <input type="text" name="UpdateRQABugETADate" value="<%= arlRQABugDetails.get(10) %>"
                                               size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)">
                                        <a href="javascript:showCal('Calendar16','dd/mm/yyyy');"><img src="images/cal.gif"
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:updateRQABug()" id="buttonId0"/>
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