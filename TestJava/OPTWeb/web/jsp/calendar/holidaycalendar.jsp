<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList arlHolidayCalendar = (ArrayList) request.getAttribute("HOLIDAYCALENDAR");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strHolidayDate = AppUtil.checkNull((String) request.getAttribute("HOLIDAYDATE"));
    String strHolidayName = AppUtil.checkNull((String) request.getAttribute("HOLIDAYNAME"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function saveHolidayCalendar() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.HolidayDate.value)) {
                    OPTDialog("Enter Holiday Date", document.OPTForm.HolidayDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.HolidayDate.value, SystemDate, "DD/MM/YYYY", "LESSEROREQUAL")) {
                    OPTDialog("Holiday Date should be greater than system date", document.OPTForm.HolidayDate);
                    return;
                }

                if (isEmpty(document.OPTForm.HolidayName.value)) {
                    OPTDialog("Enter Holiday Name", document.OPTForm.HolidayName);
                    return;
                }
                document.OPTForm.action = "saveHolidayCalendar";
                frmWriteSubmit();
            }
        }

        function frmCancelThis() {
            showFullPageMask(true);
            jConfirm("Are you sure you want to clear all the data", JavaScriptConf, function (retval) {
                showFullPageMask(false);
                if (retval) {
                    document.OPTForm.reset();
                    document.OPTForm.hidAction.value = "Add";
                }
            });
        }

        function displaymessage() {
            document.OPTForm.hidAction.value = "Add";
            document.OPTForm.HolidayDate.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "ADDED") {
                OPTDialog("Holiday Added Successfully", document.OPTForm.HolidayDate);
            }
            else if (varResult == "DELETED") {
                OPTDialog("Holiday Deleted Successfully", document.OPTForm.HolidayDate);
            }
            else if (varResult == "HOLIDAYEXISTS") {
                OPTDialog("This Date Already Exists", document.OPTForm.HolidayDate);
            }
        }

        function deleteHolidayCalendar(varHolidayRefNo) {
            showFullPageMask(true);
            jConfirm("Are you sure you want to delete", JavaScriptConf, function (retval) {
                showFullPageMask(false);
                if (retval) {
                    document.OPTForm.hidAction.value = "Delete";
                    document.OPTForm.hidHolidayRefNo.value = varHolidayRefNo;
                    document.OPTForm.action = "saveHolidayCalendar";
                    frmWriteSubmit();
                }
            });
        }
    </script>
</head>
<body onload="displaymessage();">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidHolidayRefNo">
    <input type="hidden" name="hidAction">

    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Holiday Calendar
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="50%" class="txtgrey">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="36%" class="txtgreybold">Holiday Date</td>
                                                <td width="64%"><input type="text" name="HolidayDate" size="10"
                                                                       maxlength="10" class="txtgrey"
                                                                       onblur="checkValidDateForObject(this)"
                                                                       value="<%= strHolidayDate %>">
                                                    <a href="javascript:showCal('Calendar6','dd/mm/yyyy');"><img
                                                            src="images/cal.gif" width="20"
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
                                                <td width="36%" class="txtgreybold">Holiday Name</td>
                                                <td width="64%"><input type="text" name="HolidayName" size="30"
                                                                       maxlength="30" class="txtgrey"
                                                                       value="<%= strHolidayName %>">
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
            <td width="60%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 width="99%" align="center">
        <tr>
            <td width="60%">
                <table cellpadding=2 cellspacing=1 border=0 width="100%" align="center">
                    <tr>
                        <td width="50%" align="right" valign="middle">&nbsp;
                            <input type="button" class="myButton" value="Save" onclick="JavaScript:saveHolidayCalendar()"
                                   id="buttonId0"/>
                        </td>
                        <td width="50%" align="left" valign="middle">&nbsp;
                            <input type="button" id="buttonId1" class="myButton" value="Reset"
                                   onclick="JavaScript:frmCancelThis()"/>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="40%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>

    <%
        if (arlHolidayCalendar != null && !arlHolidayCalendar.isEmpty())
        {
    %>
    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Holidays
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="40%" class="txtblackbold12">Holiday Date</td>
                                    <td width="40%" class="txtblackbold12">Holiday Name</td>
                                    <td width="20%" class="txtblackbold12">Delete Holiday</td>
                                </tr>
                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";
                                    for (int iCount = 0; iCount < arlHolidayCalendar.size(); iCount += 3)
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
                                    <td class="txtgrey"><%= arlHolidayCalendar.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlHolidayCalendar.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey"><a
                                            href="javascript:deleteHolidayCalendar('<%= (String) arlHolidayCalendar.get(iCount) %>');"
                                            class="link">Delete</a>
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
            <td width="50%"></td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
