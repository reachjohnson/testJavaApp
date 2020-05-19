<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlResourceNames = (ArrayList) request.getAttribute("ResourceNames");
    ArrayList<String> arlFeedbackCategory = (ArrayList) request.getAttribute("FeedbackCategory");
    ArrayList<String> arlTeamFeedbacks = (ArrayList) request.getAttribute("TeamFeedbacks");

    String strFeedbackFromDate = AppUtil.checkNull((String) request.getAttribute("FeedbackFromDate"));
    String strFeedbackToDate = AppUtil.checkNull((String) request.getAttribute("FeedbackToDate"));
    String strResource = AppUtil.checkNull((String) request.getAttribute("Resource"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function CTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                getTeamFeedbackDetails();
            }
        }


        function UpdateTeamFeedback() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.UserId.options.selectedIndex == 0) {
                    OPTDialog("Select Resource Name", document.OPTForm.UserId);
                    return;
                }
                if (isEmpty(document.OPTForm.FeedbackDate.value)) {
                    OPTDialog("Please enter Feedback Date", document.OPTForm.FeedbackDate);
                    return;
                }
                var systemDateValue = Date.parse(new Date());
                var feedbackDate = getDefaultDateFormat(document.OPTForm.FeedbackDate.value, "DD/MM/YYYY");
                var feedbackDateValue = Date.parse(feedbackDate);

                if (feedbackDateValue > systemDateValue) {
                    OPTDialog("Feedback Date Should Not Be Greater Than Current Date", document.OPTForm.FeedbackDate);
                    return;
                }
                if (document.OPTForm.FeedbackCategory.options.selectedIndex == 0) {
                    OPTDialog("Select Feedback Category", document.OPTForm.FeedbackCategory);
                    return;
                }
                if (isEmpty(document.OPTForm.FeedbackComments.value)) {
                    OPTDialog("Please enter Feedback Comments", document.OPTForm.FeedbackComments);
                    return;
                }
                document.OPTForm.action = "UpdateTeamFeedback";
                frmWriteSubmit();
            }
        }

        function getTeamFeedbackDetails() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.FeedbackFromDate.value)) {
                    OPTDialog("Please enter From Date", document.OPTForm.FeedbackFromDate);
                    return;
                }
                var systemDateValue = Date.parse(new Date());
                var feedbackFromDate = getDefaultDateFormat(document.OPTForm.FeedbackFromDate.value, "DD/MM/YYYY");
                var feedbackFromDateValue = Date.parse(feedbackFromDate);
                if (feedbackFromDateValue > systemDateValue) {
                    OPTDialog("From Date Should Not Be Greater Than Current Date", document.OPTForm.FeedbackDate);
                    return;
                }
                if (isEmpty(document.OPTForm.FeedbackToDate.value)) {
                    OPTDialog("Please enter To Date", document.OPTForm.FeedbackToDate);
                    return;
                }
                var feedbackToDate = getDefaultDateFormat(document.OPTForm.FeedbackToDate.value, "DD/MM/YYYY");
                var feedbackToDateValue = Date.parse(feedbackToDate);
                if (feedbackToDateValue > systemDateValue) {
                    OPTDialog("To Date Should Not Be Greater Than Current Date", document.OPTForm.FeedbackToDate);
                    return;
                }
                if (feedbackFromDateValue > feedbackToDateValue) {
                    OPTDialog("From Date Should Not Be Greater Than To Date", document.OPTForm.FeedbackFromDate);
                    return;
                }
                if (document.OPTForm.Resource.options.selectedIndex == 0) {
                    OPTDialog("Select Resource", document.OPTForm.Resource);
                    return;
                }
                document.OPTForm.action = "getTeamFeedbackDetails";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.UserId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "SUCCESS") {
                OPTDialog("Feedback Updated successfully");
            }
        }

    </script>
</head>
<body onload="displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Create Team Feedback
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="center">
                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Resource Name</td>
                        <td>
                            <select name="UserId" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    if (arlResourceNames != null && !arlResourceNames.isEmpty())
                                    {
                                        for (int intCount = 0; intCount < arlResourceNames.size(); intCount += 2)
                                        {
                                %>
                                <option value="<%= arlResourceNames.get(intCount) %>"><%= arlResourceNames.get(intCount + 1) %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Feedback Date</td>
                        <td>
                            <input type="text" name="FeedbackDate" size="10"
                                   maxlength="20" class="txtgrey"
                                   onblur="checkValidDateForObject(this)">
                            <a href="javascript:showCal('Calendar23','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                          width="20"
                                                                                          height="13" border="0"
                                                                                          alt="Pick a date"></a>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Feedback Category</td>
                        <td>
                            <select name="FeedbackCategory" class="txtgrey">
                                <option value="">Select</option>
                                <%
                                    if (arlFeedbackCategory != null && !arlFeedbackCategory.isEmpty())
                                    {
                                        for (String strFeedbackCategory : arlFeedbackCategory)
                                        {
                                %>
                                <option value="<%= strFeedbackCategory %>"><%= strFeedbackCategory %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Feedback Comments</td>
                        <td>
                <textarea name="FeedbackComments" rows="6" cols="120" maxlength="2990"
                          class="txtgrey"></textarea>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:UpdateTeamFeedback()"
                       id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%@ include file="../common/line.jsp" %>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Team Feedback Details Search
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="25%" class="txtgrey" valign="middle">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="25%" class="txtgreybold">From Date</td>
                                    <td width="75%">
                                        <input type="text" name="FeedbackFromDate" size="10" maxlength="20"
                                               class="txtgrey"
                                               onblur="checkValidDateForObject(this)" value="<%= strFeedbackFromDate %>"
                                               onkeypress="CTtrackEnterKey(event)">
                                        <a href="javascript:showCal('Calendar24','dd/mm/yyyy');"><img
                                                src="images/cal.gif"
                                                width="20"
                                                height="13" border="0"
                                                alt="Pick a date"></a><span
                                            class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="25%" class="txtgrey" valign="middle">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="25%" class="txtgreybold">To Date</td>
                                    <td width="75%">
                                        <input type="text" name="FeedbackToDate" size="10" maxlength="20"
                                               class="txtgrey"
                                               onblur="checkValidDateForObject(this)" value="<%= strFeedbackToDate %>"
                                               onkeypress="CTtrackEnterKey(event)">
                                        <a href="javascript:showCal('Calendar25','dd/mm/yyyy');"><img
                                                src="images/cal.gif"
                                                width="20"
                                                height="13" border="0"
                                                alt="Pick a date"></a><span
                                            class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="20%" class="txtgrey" valign="middle">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="40%" class="txtgreybold">Resource</td>
                                    <td width="60%">
                                        <select name="Resource" class="txtgrey" onkeypress="CTtrackEnterKey(event)">
                                            <option value="">Select</option>
                                            <option value="All Resources">All Resources</option>
                                            <%
                                                String strSelected = "";
                                                if (arlResourceNames != null && !arlResourceNames.isEmpty())
                                                {
                                                    for (int intCount = 0; intCount < arlResourceNames.size(); intCount += 2)
                                                    {
                                                        strSelected = "";
                                                        if (strResource.equalsIgnoreCase(arlResourceNames.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlResourceNames.get(intCount) %>" <%= strSelected %>><%= arlResourceNames.get(intCount + 1) %>
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
                        <td width="10%" valign="middle">
                            <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                <tr>
                                    <td width="100%" align="center" valign="middle">&nbsp;
                                        <input type="button" class="myButton" value="Search"
                                               onclick="JavaScript:getTeamFeedbackDetails()"
                                               id="buttonId2"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="20%" valign="middle">&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strFeedbackFromDate.length() > 0 && strFeedbackToDate.length() > 0)
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Team Feedback Details
                        </td>
                    </tr>
                </table>

                <%
                    if (arlTeamFeedbacks != null && !arlTeamFeedbacks.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="15%" class="txtblackbold12">Resource Name</td>
                        <td width="10%" class="txtblackbold12">Feedback Date</td>
                        <td width="12%" class="txtblackbold12">Feedback Category</td>
                        <td width="60%" class="txtblackbold12">Feedback Comments</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlTeamFeedbacks.size(); iCount += 4)
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
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlTeamFeedbacks.get(iCount + 3) %>
                        </td>
                        <td class="txtgrey"><%= arlTeamFeedbacks.get(iCount) %>
                        </td>
                        <td class="txtgrey"><%= arlTeamFeedbacks.get(iCount + 1) %>
                        </td>
                        <td class="txtgrey"><%= arlTeamFeedbacks.get(iCount + 2) %>
                        </td>
                    </tr>
                    <%
                            intCounter++;
                        }
                    %>
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
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>
</form>
</body>
</html>
