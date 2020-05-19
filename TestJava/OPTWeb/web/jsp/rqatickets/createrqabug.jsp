<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlBugDetails = (ArrayList) request.getAttribute("BugDetails");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strCurrentRQAPhase = AppUtil.checkNull((String) request.getAttribute("CurrentRQAPhase"));
    boolean blnRQACurrentPhase = strCurrentRQAPhase.length() > 0;
    String strSelected = "";

    String strNewRQACycle = "";
    String strNewBugId = "";
    String strNewBugDescription = "";
    String strNewBugPriority = "";
    String strNewBugModule = "";
    String strNewAssignee = "";
    String strCreateBugRecDate = "";
    String strNewReceivedHour = "";
    String strNewReceivedMinute = "";
    String strNewReceived24Hours = "";
    String strNewBugRaisedBy = "";

    if (arlBugDetails != null && !arlBugDetails.isEmpty())
    {
        strNewRQACycle = AppUtil.checkNull(arlBugDetails.get(0));
        strNewBugId = AppUtil.checkNull(arlBugDetails.get(1));
        strNewBugDescription = AppUtil.checkNull(arlBugDetails.get(2));
        strNewBugPriority = AppUtil.checkNull(arlBugDetails.get(3));
        strNewBugModule = AppUtil.checkNull(arlBugDetails.get(4));
        strNewAssignee = AppUtil.checkNull(arlBugDetails.get(5));
        strCreateBugRecDate = AppUtil.checkNull(arlBugDetails.get(6));
        strNewReceivedHour = AppUtil.checkNull(arlBugDetails.get(7));
        strNewReceivedMinute = AppUtil.checkNull(arlBugDetails.get(8));
        strNewReceived24Hours = AppUtil.checkNull(arlBugDetails.get(9));
        strNewBugRaisedBy = AppUtil.checkNull(arlBugDetails.get(10));
    }

    if (strNewAssignee.isEmpty())
    {
        strNewAssignee = strUserId;
    }

    if (strCreateBugRecDate.isEmpty())
    {
        strCreateBugRecDate = strCurrentDateDDMMYYYY;
    }

    if (strNewReceivedHour.isEmpty())
    {
        strNewReceivedHour = strCurrentHour;
    }

    if (strNewReceivedMinute.isEmpty())
    {
        strNewReceivedMinute = strCurrentMinute;
    }

    if (strNewReceived24Hours.isEmpty())
    {
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

        function saveRQABug() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.RQACycle.options.selectedIndex == 0) {
                    OPTDialog("Select RQA Cycle", document.OPTForm.RQACycle);
                    return;
                }
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
                if (isEmpty(document.OPTForm.CreateRQABugRecDate.value)) {
                    OPTDialog("Enter Received Date", document.OPTForm.CreateRQABugRecDate);
                    return;
                }
                var enteredDate = getDefaultDateFormat(document.OPTForm.CreateRQABugRecDate.value, "DD/MM/YYYY");
                enteredDateTime = enteredDate + " " + document.OPTForm.ReceivedHour.value + ":" + document.OPTForm.ReceivedMinute.value + " " + document.OPTForm.Received24Hours.value;
                var enteredDateValue = Date.parse(enteredDateTime);
                var systemDateValue = Date.parse(new Date());
                if (enteredDateValue > systemDateValue) {
                    OPTDialog("Received Date Should Not Be Greater Than Current Date", document.OPTForm.CreateRQABugRecDate)
                    return;
                }

                document.OPTForm.action = "saveRQABug";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            var RQACurrentPhase = <%= blnRQACurrentPhase %>;
            if (!RQACurrentPhase) {
                showFullPageMask(true);
                jAlert("No Current RQA Phase Found. Update the RQA Phase in RQA Settings.", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                });
            }
            else {
                document.OPTForm.RQACycle.focus();
            }

            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jConfirm("RQA Bug Created Successfully.\nDo you want to create another RQA Bug?", JavaScriptConf, function (retval) {
                    showFullPageMask(false);
                    if (!retval) {
                        document.OPTForm.action = "homePage";
                        frmReadSubmit();
                    }
                    else {
                        document.OPTForm.RQACycle.focus();
                    }
                });
            }
            else if (varResult == "BUGEXISTS") {
                OPTDialog("RQA Bug Id already exists", document.OPTForm.BugId);
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
    <%@ include file="../common/onespace.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Create RQA Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">RQA Phase</td>
                                    <td width="64%" class="txtgrey"><%= strCurrentRQAPhase %>
                                        <input type="hidden" name="hidRQAPhase" value="<%= strCurrentRQAPhase %>">
                                        <input type="text" name="dummy1" style="visibility:hidden;">
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">RQA Cycle</td>
                                    <td width="64%">
                                        <select name="RQACycle" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String strRQACycle : AppConstants.RQA_CYCLES)
                                                {
                                                    strSelected = "";
                                                    if (strNewRQACycle.equalsIgnoreCase(strRQACycle))
                                                    {
                                                        strSelected = "selected";
                                                    }
                                            %>
                                            <option value="<%= strRQACycle %>" <%= strSelected %>><%= strRQACycle %>
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
                                    <td width="18%" class="txtgreybold">Bug Id</td>
                                    <td width="82%">
                                        <input type="text" name="BugId" size="30" maxlength="30" class="txtgrey"
                                               value="<%= strNewBugId %>">
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
                        <textarea name="BugDescription" rows="3" cols="120" maxlength="490"
                                  class="txtgrey"><%= strNewBugDescription %></textarea>
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
                                                for (String strBugRaisedBy : AppConstants.RQA_BUG_RAISED_BY)
                                                {
                                                    strSelected = "";
                                                    if (strNewBugRaisedBy.equalsIgnoreCase(strBugRaisedBy))
                                                    {
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
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Assignee</td>
                                    <td width="64%">
                                        <select name="Assignee" class="txtgrey">
                                            <option value="">Select</option>
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
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtgreybold">Received Date</td>
                                    <td width="64%">
                                        <input type="text" name="CreateRQABugRecDate" value="<%= strCreateBugRecDate %>"
                                               size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)">
                                        <a href="javascript:showCal('Calendar17','dd/mm/yyyy');"><img
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveRQABug()" id="buttonId0"/>
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