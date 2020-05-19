<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    ArrayList<String> arlAssignees = (ArrayList) request.getAttribute("Assignees");
    ArrayList<String> arlITDetails = (ArrayList) request.getAttribute("ITDetails");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelected = "";
    String strRefNo = "";
    String strTicketId = "";
    String strOriginalAssignee = "";
    String strTicketDesc = "";
    String strActionRequired = "";
    String strETA = "";
    if (arlITDetails != null && !arlITDetails.isEmpty())
    {
        strRefNo = arlITDetails.get(0);
        strTicketId = arlITDetails.get(1);
        strOriginalAssignee = arlITDetails.get(7);
        strTicketDesc = arlITDetails.get(2);
        strActionRequired = arlITDetails.get(3);
        strETA = arlITDetails.get(9);
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
                if (document.OPTForm.UpdateITAssignee.options.selectedIndex == 0) {
                    OPTDialog("Select Assignee", document.OPTForm.UpdateITAssignee);
                    return;
                }
                if (isEmpty(document.OPTForm.ActionRequired.value)) {
                    OPTDialog("Enter Action Required", document.OPTForm.ActionRequired);
                    return;
                }
                if (!isEmpty(document.OPTForm.ITETADate.value)) {
                    if (!compareTwoDates(document.OPTForm.ITETADate.value, SystemDate, "DD/MM/YYYY", "LESSER")) {
                        OPTDialog("ETA Date should be equal to or greater than system date", document.OPTForm.ITETADate);
                        return;
                    }
                }
                document.OPTForm.action = "UpdateIT";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.UpdateITAssignee.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            var fromPage = "<%= strFromPage.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Internal Ticket Updated Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        if (fromPage == "HOMEPAGE") {
                            document.OPTForm.action = "homePage";
                        }
                        else if (fromPage == "MODIFYINTERNALTICKETS") {
                            document.OPTForm.action = "loadITAdminChanges";
                        }
                        frmReadSubmit();
                    }
                });
            }
        }
    </script>


</head>
<body onload="JavaScript:displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefno" value="<%=strRefNo %>">
    <input type="hidden" name="hidTicketId" value="<%=strTicketId %>">
    <input type="hidden" name="hidOriginalAssignee" value="<%=strOriginalAssignee %>">
    <input type="hidden" name="hidTicketDesc" value="<%=strTicketDesc %>">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Internal Ticket
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Ticket Id</td>
                                    <td width="80%" class="txtgrey"><%= strTicketId %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Ticket Description</td>
                                    <td width="80%" class="txtgrey"><%= strTicketDesc %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Assignee</td>
                                    <td width="80%">
                                        <select name="UpdateITAssignee" class="txtgrey">
                                            <option value="">Select</option>
                                            <option value="<%= AppConstants.ALL_ASSIGNEES %>" <%= strSelected %>><%= AppConstants.ALL_ASSIGNEES %>
                                                    <%
                                    if (arlAssignees != null && !arlAssignees.isEmpty())
                                    {
                                        for (int intCount = 0; intCount < arlAssignees.size(); intCount += 2)
                                        {
                                            strSelected = "";
                                            if (strOriginalAssignee.equalsIgnoreCase(arlAssignees.get(intCount)))
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
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Action Required</td>
                                    <td width="80%">
                            <textarea name="ActionRequired" rows="6" cols="120" maxlength="998"
                                      class="txtgrey"><%= strActionRequired %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">ETA</td>
                                    <td width="80%">
                                        <input type="text" name="ITETADate" size="10"
                                               maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)" value="<%= strETA %>">
                                        <a href="javascript:showCal('Calendar20','dd/mm/yyyy');"><img src="images/cal.gif"
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