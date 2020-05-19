<%@ include file="../common/noCache.jsp" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%
    ArrayList<String> arlBugDetails = (ArrayList) request.getAttribute("BugDetails");

    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    String strAssignee = AppUtil.checkNull((String) request.getAttribute("Assignee"));

    String strSelected = "";

    String strRefNo = "";
    String strNewBugId = "";
    String strNewBugDescription = "";
    String strNewBugPriority = "";
    String strNewBugType = "";
    String strNewBugModule = "";
    String strNewBugRaisedBy = "";
    String strNewAssignee = "";
    String strNewReceivedDate = "";
    String strNewSLAEndDate = "";
    String strSLAMissed = "";
    String strETA = "";

    if (arlBugDetails != null && !arlBugDetails.isEmpty())
    {
        strRefNo = AppUtil.checkNull(arlBugDetails.get(0));
        strNewBugId = AppUtil.checkNull(arlBugDetails.get(1));
        strNewBugDescription = AppUtil.checkNull(arlBugDetails.get(2));
        strNewBugPriority = AppUtil.checkNull(arlBugDetails.get(3));
        strNewBugType = AppUtil.checkNull(arlBugDetails.get(4));
        strNewBugModule = AppUtil.checkNull(arlBugDetails.get(5));
        strNewBugRaisedBy = AppUtil.checkNull(arlBugDetails.get(6));
        strNewAssignee = AppUtil.checkNull(arlBugDetails.get(7));
        strNewReceivedDate = AppUtil.checkNull(arlBugDetails.get(8));
        strNewSLAEndDate = AppUtil.checkNull(arlBugDetails.get(9));
        strSLAMissed = AppUtil.checkNull(arlBugDetails.get(10));
        strETA = AppUtil.checkNull(arlBugDetails.get(12));
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function updateBug() {
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
                if (!isEmpty(document.OPTForm.BugETADate.value)) {
                    if (!compareTwoDates(document.OPTForm.BugETADate.value, SystemDate, "DD/MM/YYYY", "LESSER")) {
                        OPTDialog("Bug ETA Date should be equal to or greater than system date", document.OPTForm.BugETADate);
                        return;
                    }
                }
                if (isEmpty(document.OPTForm.ReasonForUpdate.value)) {
                    OPTDialog("Enter Reason For Update", document.OPTForm.ReasonForUpdate);
                    return;
                }

                document.OPTForm.action = "updateBug";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.BugDescription.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            var SLAMissed = "<%= strSLAMissed %>";
            var fromPage = "<%= strFromPage.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Bug Updated Successfully", JavaScriptInfo, function (retval) {
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
                    OPTDialog("This Bug is Already OOSLA", document.OPTForm.BugDescription);
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
    <input type="hidden" name="hidBugId" value="<%= strNewBugId %>">
    <input type="hidden" name="hidUserName" value="<%= strUserName %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="Assignee" value="<%= strAssignee %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Bug Id</td>
                                    <td width="82%" class="txtgreybold"><%= strNewBugId %>
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
                              class="txtgrey"><%= strNewBugDescription %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Bug Priority</td>
                                    <td width="64%">
                                        <select name="BugPriority" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugPriority : AppConstants.TICKET_PRIORITY)
                                                {
                                                    strSelected = "";
                                                    if (strNewBugPriority.equalsIgnoreCase(strBugPriority))
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
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Bug Type</td>
                                    <td width="64%">
                                        <select name="BugType" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strBugType : AppConstants.TICKET_TYPE)
                                                {
                                                    strSelected = "";
                                                    if (strNewBugType.equalsIgnoreCase(strBugType))
                                                    {
                                                        strSelected = "selected";
                                                    }
                                            %>
                                            <option value="<%= strBugType %>" <%= strSelected %>><%= strBugType %>
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
                                                for (String strBugModule : AppConstants.TICKET_MODULE)
                                                {
                                                    strSelected = "";
                                                    if (strNewBugModule.equalsIgnoreCase(strBugModule))
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
                                                for (String strBugRaisedBy : AppConstants.TICKET_RAISED_BY)
                                                {
                                                    strSelected = "";
                                                    if (strNewBugRaisedBy.equalsIgnoreCase(strBugRaisedBy))
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
                                    <td width="18%" class="txtgreybold">Bug ETA</td>
                                    <td width="82%" class="txtgrey">
                                        <input type="text" name="BugETADate" value="<%= strETA %>" size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)">
                                        <a href="javascript:showCal('Calendar19','dd/mm/yyyy');"><img src="images/cal.gif"
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:updateBug()" id="buttonId0"/>
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