<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList arlLeavePlans = (ArrayList) request.getAttribute("LEAVEPLANS");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function SaveLeavePlans() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.LeavePlansFromDate.value)) {
                    OPTDialog("Enter From Date", document.OPTForm.LeavePlansFromDate);
                    return;
                }
                if (isEmpty(document.OPTForm.LeavePlansToDate.value)) {
                    OPTDialog("Enter To Date", document.OPTForm.LeavePlansToDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.LeavePlansFromDate.value, SystemDate, "DD/MM/YYYY", "LESSEROREQUAL")) {
                    OPTDialog("Leave From date should be greater than system date", document.OPTForm.LeavePlansFromDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.LeavePlansToDate.value, SystemDate, "DD/MM/YYYY", "LESSEROREQUAL")) {
                    OPTDialog("Leave To date should be greater than system date", document.OPTForm.LeavePlansToDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.LeavePlansToDate.value, document.OPTForm.LeavePlansFromDate.value, "DD/MM/YYYY", "LESSER")) {
                    OPTDialog("Leave To date should be greater or equal to Leave From date", document.OPTForm.LeavePlansToDate);
                    return;
                }
                if (isEmpty(document.OPTForm.LeaveReason.value)) {
                    OPTDialog("Enter Reason For Leave", document.OPTForm.LeaveReason);
                    return;
                }
                document.OPTForm.action = "saveLeavePlans";
                frmWriteSubmit();
            }
        }

        function frmCancelThis() {
            showFullPageMask(true);
            jConfirm("Are You Sure to Reset Data", JavaScriptConf, function (retval) {
                showFullPageMask(false);
                if (retval) {
                    document.OPTForm.reset();
                    document.OPTForm.hidAction.value = "Add";
                }
            });
        }

        function displaymessage() {
            document.OPTForm.hidAction.value = "Add";
            document.OPTForm.LeavePlansFromDate.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "ADDED") {
                OPTDialog("Leave Plan Added successfully", document.OPTForm.LeavePlansFromDate);
            }
            else if (varResult == "CANCELLED") {
                OPTDialog("Leave Plan Cancelled", document.OPTForm.LeavePlansFromDate);
            }
        }

        function CancelLeavePlan(varLeaveRefNo) {
            showFullPageMask(true);
            jConfirm("Are You Sure to Cancel Leave Plans", JavaScriptConf, function (retval) {
                showFullPageMask(false);
                if (retval) {
                    document.OPTForm.hidAction.value = "Cancel";
                    document.OPTForm.hidLeaveRefNo.value = varLeaveRefNo;
                    document.OPTForm.action = "saveLeavePlans";
                    frmWriteSubmit();
                }
            });
        }

    </script>
</head>
<body onload="displaymessage();">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidLeaveRefNo">
    <input type="hidden" name="hidAction">

    <table width="99%" align="center">
        <tr>
            <td width="70%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Leave Plans
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">From Date</td>
                                                <td width="64%"><input type="text" name="LeavePlansFromDate" size="10" maxlength="10"
                                                                       class="txtgrey"
                                                                       onblur="checkValidDateForObject(this)">
                                                    <a href="javascript:showCal('Calendar4','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                                                 height="13" border="0"
                                                                                                                 alt="Pick a date"></a><span
                                                            class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">To Date</td>
                                                <td width="64%"><input type="text" name="LeavePlansToDate" size="10" maxlength="20"
                                                                       class="txtgrey" onblur="checkValidDateForObject(this)">
                                                    <a href="javascript:showCal('Calendar5','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                                                 height="13" border="0"
                                                                                                                 alt="Pick a date"></a><span
                                                            class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%" class="txtgrey" colspan="2">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="18%" class="txtgreybold">Reason</td>
                                                <td width="82%"><input type="text" name="LeaveReason" size="110" maxlength="300"
                                                                       class="txtgrey">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="30%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table cellpadding=2 cellspacing=1 border=0 width="99%" align="center">
        <tr>
            <td width="60%">
                <table cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr>
                        <td width="50%" align="right" valign="middle">&nbsp;
                            <input type="button" class="myButton" value="Save" onclick="JavaScript:SaveLeavePlans()" id="buttonId0"/>
                        </td>
                        <td width="50%" align="left" valign="middle">&nbsp;
                            <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancelThis()"/>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="40%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
    <%
        if (arlLeavePlans != null && !arlLeavePlans.isEmpty())
        {
    %>
    <table width="99%" align="center">
        <tr>
            <td width="70%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Leave Plans
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="15%" class="txtblackbold12">From Date</td>
                                    <td width="15%" class="txtblackbold12">To Date</td>
                                    <td width="50%" class="txtblackbold12">Reason for Leave</td>
                                    <td width="20%" class="txtblackbold12">Cancel Leave</td>
                                </tr>
                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";
                                    for (int iCount = 0; iCount < arlLeavePlans.size(); iCount += 4)
                                    {
                                        if (intCounter % 2 == 0)
                                        {
                                            strBackGround = "LightGreyBandStyle";
                                        }
                                        else
                                        {
                                            strBackGround = "DarkGreyBandStyle";
                                        }
                                %>
                                <tr class="<%= strBackGround %>">
                                    <td class="txtgrey"><%= arlLeavePlans.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlLeavePlans.get(iCount + 2) %>
                                    </td>

                                    <td class="txtgrey"><%= arlLeavePlans.get(iCount + 3) %>
                                    </td>
                                    <td class="txtgrey"><a
                                            href="javascript:CancelLeavePlan('<%= (String) arlLeavePlans.get(iCount) %>');"
                                            class="link">Cancel</a>
                                    </td>
                                </tr>
                                <%
                                        intCounter++;
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="30%"></td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
