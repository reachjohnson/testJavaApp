<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlDomainNames = (ArrayList) request.getAttribute("DomainNames");
    ArrayList<String> arlRQABugDetailsForClosing = (ArrayList) request.getAttribute("RQABugDetailsForClosing");
    ArrayList<String> arlRQAResolutionTypes = (ArrayList) request.getAttribute("RQAResolutionTypes");
    ArrayList<String> arlRQAResolutionSettings = (ArrayList) request.getAttribute("RQAResolutionSettings");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strModCurrTickAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));

    ArrayList<String> arlBugResolutionTypes = new ArrayList<String>();
    String strRefNo = "";
    String strRQAPhase = "";
    String strRQACycle = "";
    String strBugId = "";
    String strBugDescription = "";
    String strBugPriority = "";
    String strBugType = "";
    String strBugModule = "";
    String strBugRaisedBy = "";
    String strAssignee = "";
    String strReceivedDate = "";
    String strSLAEndDate = "";
    if (arlRQABugDetailsForClosing != null && !arlRQABugDetailsForClosing.isEmpty())
    {
        strRefNo = AppUtil.checkNull(arlRQABugDetailsForClosing.get(0));
        strRQAPhase = AppUtil.checkNull(arlRQABugDetailsForClosing.get(1));
        strRQACycle = AppUtil.checkNull(arlRQABugDetailsForClosing.get(2));
        strBugId = AppUtil.checkNull(arlRQABugDetailsForClosing.get(3));
        strBugDescription = AppUtil.checkNull(arlRQABugDetailsForClosing.get(4));
        strBugPriority = AppUtil.checkNull(arlRQABugDetailsForClosing.get(5));
        strBugModule = AppUtil.checkNull(arlRQABugDetailsForClosing.get(6));
        strBugRaisedBy = AppUtil.checkNull(arlRQABugDetailsForClosing.get(7));
        strAssignee = AppUtil.checkNull(arlRQABugDetailsForClosing.get(8));
        strReceivedDate = AppUtil.checkNull(arlRQABugDetailsForClosing.get(9));
        strSLAEndDate = AppUtil.checkNull(arlRQABugDetailsForClosing.get(10));
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function BugResolutionOnChange() {
            if (document.OPTForm.BugResolution.value == document.OPTForm.BugMoved.value) {
                document.OPTForm.MovedDomain.disabled = false;
            }
            else {
                document.OPTForm.MovedDomain.selectedIndex = 0;
                document.OPTForm.MovedDomain.disabled = true;
            }
            if (document.OPTForm.BugResolution.value == document.OPTForm.LegacyCodeFixed.value ||
                    document.OPTForm.BugResolution.value == document.OPTForm.NonFeatCodeImpCodeFixed.value ||
                    document.OPTForm.BugResolution.value == document.OPTForm.FeatCodeImpCodeFixed.value) {
                document.OPTForm.BugRootCause.disabled = false;
            }
            else {
                document.OPTForm.BugRootCause.selectedIndex = 0;
                document.OPTForm.BugRootCause.disabled = true;
            }
        }

        function closeRQABug() {
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
                if (document.OPTForm.BugResolution.value == document.OPTForm.LegacyCodeFixed.value ||
                        document.OPTForm.BugResolution.value == document.OPTForm.NonFeatCodeImpCodeFixed.value ||
                        document.OPTForm.BugResolution.value == document.OPTForm.FeatCodeImpCodeFixed.value) {
                    if (document.OPTForm.BugRootCause.options.selectedIndex == 0) {
                        OPTDialog("Enter Bug Root Cause", document.OPTForm.BugRootCause);
                        return;
                    }
                }

                if (isEmpty(document.OPTForm.BugComments.value)) {
                    OPTDialog("Enter Comments", document.OPTForm.BugComments);
                    return;
                }
                document.OPTForm.action = "closeRQABug";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.BugResolution.focus();
            var fromPage = "<%= strFromPage %>";
            var varResult = "<%= strResult.toUpperCase() %>";

            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("RQA Bug Closed Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
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
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo" value="<%= strRefNo %>">
    <input type="hidden" name="hidBugId" value="<%= strBugId %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="Assignee" value="<%= strModCurrTickAssignee %>">
    <input type="hidden" name="closepage" value="Bug">
    <input type="hidden" name="BugMoved" value="<%= arlRQAResolutionSettings.get(0) %>">
    <input type="hidden" name="LegacyCodeFixed" value="<%= arlRQAResolutionSettings.get(1) %>">
    <input type="hidden" name="FeatCodeImpCodeFixed" value="<%= arlRQAResolutionSettings.get(2) %>">
    <input type="hidden" name="NonFeatCodeImpCodeFixed" value="<%= arlRQAResolutionSettings.get(3) %>">

    <%@ include file="../common/onespace.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Close RQA Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">RQA Phase</td>
                                    <td width="64%" class="txtblue"><%= strRQAPhase %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">RQA Cycle</td>
                                    <td width="64%" class="txtblue"><%= strRQACycle %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

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
                                    <td width="36%" class="txtbluebold">Bug Module</td>
                                    <td width="64%" class="txtblue"><%= strBugModule %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Raised By</td>
                                    <td width="82%" class="txtblue"><%= strBugRaisedBy %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Received Date</td>
                                    <td width="64%" class="txtblue"><%= strReceivedDate %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">SLA End Date</td>
                                    <td width="64%" class="txtblue"><%= strSLAEndDate %>
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
                                                for (String strBugResolution : arlRQAResolutionTypes)
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
                                        <select name="MovedDomain" class="txtgrey" disabled="true">
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
                                    <td width="18%" class="txtgreybold">Bug Root Cause</td>
                                    <td width="82%" class="txtgrey">
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
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Comments
                                    </td>
                                    <td width="82%">
                            <textarea name="BugComments" rows="8" cols="120" maxlength="4990"
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:closeRQABug()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Reset" onclick="JavaScript:frmCancel()" id="buttonId1" />
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>

</form>
</body>
</html>