<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlDomainNames = (ArrayList) request.getAttribute("DomainNames");
    ArrayList<String> arlBugDetailsForClosing = (ArrayList) request.getAttribute("BugDetailsForClosing");
    String strBugToMs = AppUtil.checkNull((String) request.getAttribute("EXISTINGBUGTOMS"));
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strModCurrTickAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));
    String strFixingAssigneeName = AppUtil.checkNull((String) request.getAttribute("FixingAssigneeName"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlReleaseVersions = (ArrayList) request.getAttribute("ReleaseVersions");

    ArrayList<String> arlBugResolutionTypes = new ArrayList<String>();
    String strRefNo = "";
    String strBugId = "";
    String strBugDescription = "";
    String strBugCategory = "";
    String strBugPriority = "";
    String strBugType = "";
    String strBugModule = "";
    String strBugRaisedBy = "";
    String strAssignee = "";
    String strReceivedDate = "";
    String strSLAEndDate = "";
    String strSLAMissed = "";
    String strTeamWorking = "";
    if (arlBugDetailsForClosing != null && !arlBugDetailsForClosing.isEmpty())
    {
        strRefNo = AppUtil.checkNull(arlBugDetailsForClosing.get(0));
        strBugId = AppUtil.checkNull(arlBugDetailsForClosing.get(1));
        strBugDescription = AppUtil.checkNull(arlBugDetailsForClosing.get(2));
        strBugPriority = AppUtil.checkNull(arlBugDetailsForClosing.get(3));
        strBugType = AppUtil.checkNull(arlBugDetailsForClosing.get(4));
        strBugModule = AppUtil.checkNull(arlBugDetailsForClosing.get(5));
        strBugRaisedBy = AppUtil.checkNull(arlBugDetailsForClosing.get(6));
        strAssignee = AppUtil.checkNull(arlBugDetailsForClosing.get(7));
        strReceivedDate = AppUtil.checkNull(arlBugDetailsForClosing.get(8));
        strSLAEndDate = AppUtil.checkNull(arlBugDetailsForClosing.get(9));
        strSLAMissed = AppUtil.checkNull(arlBugDetailsForClosing.get(10));
        strTeamWorking = AppUtil.checkNull(arlBugDetailsForClosing.get(11));

        for (String strReolutionType : AppConstants.BUG_RESOLUTION)
        {
            arlBugResolutionTypes.add(strReolutionType);
        }
        if (strTeamWorking.equalsIgnoreCase(AppConstants.TRIAGING_TEAM))
        {
            arlBugResolutionTypes.remove(AppConstants.CODE_FIXED_DELIVERED);
        }

        if (strTeamWorking.equalsIgnoreCase(AppConstants.FIXING_TEAM))
        {
            arlBugResolutionTypes.remove(AppConstants.BUG_FIX_EXPECTED);
        }
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        var SLAMissed = "<%= strSLAMissed %>";

        function BugResolutionOnChange() {
            if (document.OPTForm.BugResolution.value == document.OPTForm.BugMoved.value) {
                document.OPTForm.MovedDomain.disabled = false;
            }
            else {
                document.OPTForm.MovedDomain.selectedIndex = 0;
                document.OPTForm.MovedDomain.disabled = true;
            }
            if (document.OPTForm.TeamWorking.value == "FIXING") {
                if (document.OPTForm.BugResolution.value == document.OPTForm.CodeFixed.value) {
                    document.OPTForm.CodeReviewComments.disabled = false;
                    document.OPTForm.BugRootCause.disabled = false;
                    document.OPTForm.ReleaseVersion.disabled = false;
                }
                else {
                    document.OPTForm.CodeReviewComments.selectedIndex = 0;
                    document.OPTForm.CodeReviewComments.disabled = true;
                    document.OPTForm.BugRootCause.selectedIndex = 0;
                    document.OPTForm.BugRootCause.disabled = true;
                    document.OPTForm.ReleaseVersion.selectedIndex = 0;
                    document.OPTForm.ReleaseVersion.disabled = true;
                }
            }
            if (document.OPTForm.BugResolution.value == document.OPTForm.FixExpectedByMSTeam.value) {
                document.OPTForm.FixingAssignee.disabled = false;
            }
            else {
                document.OPTForm.FixingAssignee.selectedIndex = 0;
                document.OPTForm.FixingAssignee.disabled = true;
            }

            if (document.OPTForm.BugResolution.value == document.OPTForm.BugMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Have you already moved this Bug to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strBugId %>', '<%= strBugId %>')
                    }
                });
            }
        }

        function DomainOnChange() {
            if (document.OPTForm.BugResolution.value == document.OPTForm.BugMoved.value &&
                    document.OPTForm.MovedDomain.selectedIndex > 0) {
                showFullPageMask(true);
                jConfirm('Have you already moved this Bug to - "' + document.OPTForm.MovedDomain.value + '" in JIRA?', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strBugId %>', '<%= strBugId %>')
                    }
                });
            }
        }


        function closeBug() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.BugResolution.options.selectedIndex == 0) {
                    OPTDialog("Select Bug Resolution", document.OPTForm.BugResolution);
                    return;
                }
                if (document.OPTForm.BugResolution.value == document.OPTForm.BugMoved.value) {
                    if (document.OPTForm.MovedDomain.options.selectedIndex == 0) {
                        OPTDialog("Select Moved Domain", document.OPTForm.MovedDomain);
                        return;
                    }
                }
                if (document.OPTForm.TeamWorking.value == "FIXING") {
                    if (document.OPTForm.BugResolution.value == document.OPTForm.CodeFixed.value) {
                        if (document.OPTForm.BugRootCause.options.selectedIndex == 0) {
                            OPTDialog("Select Bug Root Cause", document.OPTForm.BugRootCause);
                            return;
                        }
                        if (document.OPTForm.CodeReviewComments.options.selectedIndex == 0) {
                            OPTDialog("Select Code Review Comments Received or Not", document.OPTForm.CodeReviewComments);
                            return;
                        }
                        if (document.OPTForm.ReleaseVersion.options.selectedIndex == 0) {
                            OPTDialog("Select Release Version for this code fix", document.OPTForm.ReleaseVersion);
                            return;
                        }
                    }
                }
                if (document.OPTForm.BugSeverity.options.selectedIndex == 0) {
                    OPTDialog("Select Bug Severity", document.OPTForm.BugSeverity);
                    return;
                }
                if (OOSLA_HIGHLIGHT && SLAMissed == "1") {
                    if (isEmpty(document.OPTForm.OOSLAJustification.value)) {
                        OPTDialog("Enter OOSLA Justification", document.OPTForm.OOSLAJustification);
                        return;
                    }
                }
                if (isEmpty(document.OPTForm.BugComments.value)) {
                    OPTDialog("Enter Closing Comments", document.OPTForm.BugComments);
                    return;
                }
                if (document.OPTForm.BugResolution.value == document.OPTForm.FixExpectedByMSTeam.value) {
                    if (document.OPTForm.FixingAssignee.options.selectedIndex == 0) {
                        OPTDialog("Select Assignee For Fixing", document.OPTForm.FixingAssignee);
                        return;
                    }
                }
                showFullPageMask(true);
                jConfirm('All JIRA changes done for this Task ? - "' + document.OPTForm.hidBugId.value + '"', JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        openJIRAWindow('<%= AppConstants.JIRAURL + strBugId %>', '<%= strBugId %>')
                    }
                    else {
                        document.OPTForm.action = "closeBug";
                        frmWriteSubmit();
                    }
                });

            }
        }

        function displaymessage() {
            document.OPTForm.BugResolution.focus();

            var varResult = "<%= strResult.toUpperCase() %>";
            var bugtoms = "<%= strBugToMs.toUpperCase() %>";
            var fixingAssigneeName = "<%= strFixingAssigneeName %>"
            var fromPage = "<%= strFromPage.toUpperCase() %>";

            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Bug Closed Successfully", JavaScriptInfo, function (retval) {
                    if (bugtoms == "EXISTINGBUGTOMS") {
                        jAlert("Another Bug was created for Fixing Team and assigned to \"" + fixingAssigneeName + "\"", JavaScriptInfo, function (retval) {
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
                    OPTDialog("This Bug is Already OOSLA. Enter Proper Justification In Comments.", document.OPTForm.BugResolution);
                }
            }
        }
    </script>


</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidBugId" value="<%= strBugId %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="Assignee" value="<%= strModCurrTickAssignee %>">
    <input type="hidden" name="TeamWorking" value="<%= strTeamWorking.toUpperCase() %>">
    <input type="hidden" name="closepage" value="Bug">
    <input type="hidden" name="BugMoved" value="<%= AppConstants.BUG_MOVED %>">
    <input type="hidden" name="CodeFixed" value="<%= AppConstants.CODE_FIXED_DELIVERED %>">
    <input type="hidden" name="FixExpectedByMSTeam" value="<%= AppConstants.BUG_FIX_EXPECTED %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Close Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Id</td>
                                    <td width="82%" class="txtblue"><%= strBugId %>
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
                                    <td width="82%" class="txtblue"><%= strBugDescription %>
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
                                    <td width="64%" class="txtblue"><%= strBugPriority %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Type</td>
                                    <td width="64%" class="txtblue"><%= strBugType %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Module</td>
                                    <td width="64%" class="txtblue"><%= strBugModule %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Raised By</td>
                                    <td width="64%" class="txtblue"><%= strBugRaisedBy %>
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
                                    <td width="36%" class="txtgreybold">Bug Resolution</td>
                                    <td width="64%" class="txtgrey">
                                        <select name="BugResolution" class="txtgrey"
                                                onchange="JavaScript:BugResolutionOnChange()">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugResolution : arlBugResolutionTypes)
                                                {
                                            %>
                                            <option value="<%= strBugResolution %>"><%= strBugResolution %>
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
                    <%
                        if (strTeamWorking.equalsIgnoreCase(AppConstants.FIXING_TEAM))
                        {
                    %>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Bug Root Cause</td>
                                    <td width="64%" class="txtgrey">
                                        <select name="BugRootCause" class="txtgrey" disabled="true">
                                            <option value="">Select</option>
                                            <%
                                                for (String strRootCause : AppConstants.BUG_ROOTCAUSE)
                                                {
                                            %>
                                            <option value="<%= strRootCause %>"><%= strRootCause %>
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
                                    <td width="36%" class="txtgreybold">Code Review Comments Received</td>
                                    <td width="64%" class="txtgrey">
                                        <select name="CodeReviewComments" class="txtgrey" disabled="true">
                                            <option value="">Select</option>
                                            <option value="Yes">Yes</option>
                                            <option value="No">No</option>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border="0" align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Release Version</td>
                                    <td width="82%">
                                        <select name="ReleaseVersion" class="txtgrey" disabled="true">
                                            <option value="">Select</option>
                                            <%
                                                for (String strReleaseVersion : arlReleaseVersions)
                                                {
                                            %>
                                            <option value="<%= strReleaseVersion %>"><%= strReleaseVersion %>
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
                        }
                    %>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border="0" align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Bug Severity</td>
                                    <td width="82%">
                                        <select name="BugSeverity" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugSeverity : AppConstants.TICKET_SEVERITY)
                                                {
                                            %>
                                            <option value="<%= strBugSeverity %>"><%= strBugSeverity %>
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
                        if (AppSettings.isOooslaHighlight() && strSLAMissed.equalsIgnoreCase("1"))
                        {
                            strClass = OOSLA_RED_TEXT;
                            intTextAreaLength -= 29990;
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
                            <textarea name="BugComments" rows="8" cols="120" maxlength="<%= intTextAreaLength %>"
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:closeBug()" id="buttonId0"/>
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