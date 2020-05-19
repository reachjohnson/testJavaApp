<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.opt.charts.LineChart" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlCurrentOpenTasksCount = (ArrayList) request.getAttribute("CurrentOpenTasksCount");
    ArrayList<String> arlCurrentOpenBugsCount = (ArrayList) request.getAttribute("CurrentOpenBugsCount");

    ArrayList<String> arlCurrentOpenOOLSATasksCount = (ArrayList) request.getAttribute("CurrentOpenOOLSATasksCount");
    ArrayList<String> arlCurrentOpenOOLSABugsCount = (ArrayList) request.getAttribute("CurrentOpenOOLSABugsCount");

    ArrayList<String> arlTasksStatusCount = (ArrayList) request.getAttribute("TasksStatusCount");
    ArrayList<String> arlBugsStatusCount = (ArrayList) request.getAttribute("BugsStatusCount");
    ArrayList<String> arlTasksResolutioncount = (ArrayList) request.getAttribute("TasksResolutioncount");
    ArrayList<String> arlConvertedBugsMovedTeamsCount = (ArrayList) request.getAttribute("ConvertedBugsMovedTeamsCount");
    ArrayList<String> arlTasksOthersMovedTeamsCount = (ArrayList) request.getAttribute("TasksOthersMovedTeamsCount");
    ArrayList<String> arlBugsResolutioncount = (ArrayList) request.getAttribute("BugsResolutioncount");
    ArrayList<String> arlBugsMovedTeamscount = (ArrayList) request.getAttribute("BugsMovedTeamscount");
    ArrayList<String> arlTasksSLA = (ArrayList) request.getAttribute("TasksSLA");
    ArrayList<String> arlBugsSLA = (ArrayList) request.getAttribute("BugsSLA");
    ArrayList<String> arlResourceUtilization = (ArrayList) request.getAttribute("ResourceUtilization");
    ArrayList<String> arlBugsInflowFromTasks = (ArrayList) request.getAttribute("BugsInflowFromTasks");


    String strFromDate = AppUtil.checkNull((String) request.getAttribute("FromDate"));
    String strToDate = AppUtil.checkNull((String) request.getAttribute("ToDate"));
    ArrayList<ArrayList<String>> arlWeekAndMonthSummary = (ArrayList) request.getAttribute("WeekAndMonthSummary");

    String strWeekSummaryChartPath = AppUtil.checkNull((String) request.getAttribute("WeekSummaryChartPath"));
    String strWeekResourceClosureChartPath = AppUtil.checkNull((String) request.getAttribute("WeekResourceClosureChartPath"));
    String strTaskResolutionChartPath = AppUtil.checkNull((String) request.getAttribute("TaskResolutionChartPath"));
    String strBugResolutionChartPath = AppUtil.checkNull((String) request.getAttribute("BugResolutionChartPath"));
    String strTaskResolutionWeekSummaryChartPath = AppUtil.checkNull((String) request.getAttribute("TaskResolutionWeekSummaryChartPath"));
    String strBugResolutionWeekSummaryChartPath = AppUtil.checkNull((String) request.getAttribute("BugResolutionWeekSummaryChartPath"));

    ArrayList<String> arlWeekSummary = new ArrayList<String>();
    ArrayList<String> arlMonthSummary = new ArrayList<String>();
    ArrayList<String> arlWeekDisplayDates = new ArrayList<String>();
    ArrayList<String> arlWeekResourceSummary = new ArrayList<String>();
    ArrayList<String> arlMonthDisplayDates = new ArrayList<String>();
    ArrayList<String> arlMonthResourceSummary = new ArrayList<String>();

    int intWeekSummaryCount = 0;
    int intMonthSummaryCount = 0;
    int intWeekResourceSummaryCount = 0;
    int intMonthResourceSummaryCount = 0;

    if (arlWeekAndMonthSummary != null && !arlWeekAndMonthSummary.isEmpty())
    {
        arlWeekSummary = arlWeekAndMonthSummary.get(0);
        arlMonthSummary = arlWeekAndMonthSummary.get(1);
        arlWeekDisplayDates = arlWeekAndMonthSummary.get(2);
        arlWeekResourceSummary = arlWeekAndMonthSummary.get(3);
        arlMonthDisplayDates = arlWeekAndMonthSummary.get(4);
        arlMonthResourceSummary = arlWeekAndMonthSummary.get(5);
        if (arlWeekSummary != null && !arlWeekSummary.isEmpty())
        {
            intWeekSummaryCount = arlWeekSummary.size() / 7;
        }
        if (arlMonthSummary != null && !arlMonthSummary.isEmpty())
        {
            intMonthSummaryCount = arlMonthSummary.size() / 7;
        }
        if (arlWeekResourceSummary != null && !arlWeekResourceSummary.isEmpty())
        {
            intWeekResourceSummaryCount = arlWeekDisplayDates.size();
        }
        if (arlMonthResourceSummary != null && !arlMonthResourceSummary.isEmpty())
        {
            intMonthResourceSummaryCount = arlMonthDisplayDates.size();
        }
    }

    boolean blnDisplayHeading = false;
    if (strFromDate.length() > 0 && strToDate.length() > 0)
    {
        blnDisplayHeading = true;
    }

    if (strFromDate.length() == 0)
    {
        strFromDate = strCurrentDateDDMMYYYY;
    }
    if (strToDate.length() == 0)
    {
        strToDate = strCurrentDateDDMMYYYY;
    }

    int intBugsInflowToSystem = 0;
    if (arlBugsInflowFromTasks != null && !arlBugsInflowFromTasks.isEmpty())
    {
        for (int intCount = 0; intCount < arlBugsInflowFromTasks.size(); intCount += 2)
        {
            intBugsInflowToSystem += Integer.parseInt(arlBugsInflowFromTasks.get(intCount + 1));
        }
    }

    int intCounter = 0;
    String strBackGround = "";
    String strTxtBackGround = "";
    String strCriticalOOSLA = "";
    String strMajorOOSLA = "";
    String strNormalOOSLA = "";
    String strMinorOOSLA = "";
    String strTrivialOOSLA = "";
    String strTotalOOSLA = "";

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function validateFields() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.SLAMetricsFromDate.value)) {
                    OPTDialog("Enter From Date", document.OPTForm.SLAMetricsFromDate);
                    return false;
                }
                if (isEmpty(document.OPTForm.SLAMetricsToDate.value)) {
                    OPTDialog("Enter To Date", document.OPTForm.SLAMetricsToDate);
                    return false;
                }
                if (!compareTwoDates(document.OPTForm.SLAMetricsFromDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than current date", document.OPTForm.SLAMetricsFromDate);
                    return false;
                }
                if (!compareTwoDates(RestrictedFromDate, document.OPTForm.SLAMetricsFromDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog(FromDateMessage, document.OPTForm.SLAMetricsFromDate);
                    return;
                }
                if (!compareTwoDates(document.OPTForm.SLAMetricsToDate.value, SystemDate, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("To date should not be greater than current date", document.OPTForm.SLAMetricsToDate);
                    return false;
                }
                if (!compareTwoDates(document.OPTForm.SLAMetricsFromDate.value, document.OPTForm.SLAMetricsToDate.value, "DD/MM/YYYY", "GREATER")) {
                    OPTDialog("From date should not be greater than To Date", document.OPTForm.SLAMetricsFromDate);
                    return false;
                }
                return true;
            }
        }
        function GenerateSLAMetrics() {
            if (validateFields()) {
                document.OPTForm.action = "loadSLAMetrics";
                frmReadSubmit();
            }
        }

        function SLAMtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                GenerateSLAMetrics();
            }
        }


        function GenerateSLAMetricsPDF() {
            if (validateFields()) {
                showFullPageMask(true);
                MM_openBrWindow('loadSLAMetricsPDF?ACCESS=RESTRICTED', 500, 1300);
            }
        }

        function openOOSLAReasons(OOSLAReasonsAction) {
            showFullPageMask(true);
            document.OPTForm.OOSLAReasonsAction.value = OOSLAReasonsAction;
            MM_openBrWindow("loadOOSLAReasonsDummy?ACCESS=RESTRICTED", 600, 1300);
        }

    </script>
</head>
<body onload="document.OPTForm.SLAMetricsFromDate.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="FromDate" value="<%= strFromDate %>">
    <input type="hidden" name="ToDate" value="<%= strToDate %>">
    <input type="hidden" name="OOSLAReasonsAction">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Filters to Generate SLA Metrics
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="99%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="30%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="35%" class="txtgreybold">From Date</td>
                                                <td width="65%">
                                                    <input type="text" name="SLAMetricsFromDate" size="10" maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)" value="<%= strFromDate %>"
                                                           onkeypress="SLAMtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar14','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                                                  width="20"
                                                                                                                  height="13" border="0"
                                                                                                                  alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="30%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="35%" class="txtgreybold">To Date</td>
                                                <td width="65%">
                                                    <input type="text" name="SLAMetricsToDate" size="10" maxlength="20" class="txtgrey"
                                                           onblur="checkValidDateForObject(this)" value="<%= strToDate %>"
                                                           onkeypress="SLAMtrackEnterKey(event)">
                                                    <a href="javascript:showCal('Calendar15','dd/mm/yyyy');"><img src="images/cal.gif"
                                                                                                                  width="20"
                                                                                                                  height="13" border="0"
                                                                                                                  alt="Pick a date"></a><span
                                                        class="txtgrey">&nbsp;(DD/MM/YYYY)</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="18%" valign="middle">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="80%" align="right" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search"
                                                           onclick="JavaScript:GenerateSLAMetrics()"
                                                           id="buttonId0"/>
                                                </td>
                                                <td width="20%" align="left" valign="bottom">&nbsp;
                                                    <a href="JavaScript:GenerateSLAMetricsPDF()"><img src="images/pdf-icon.png" border="0"
                                                                                                      alt=""></a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="12%" valign="middle">&nbsp;</td>
                                    <td width="10%" valign="middle">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (blnDisplayHeading)
        {
    %>
    <table width="90%" cellpadding=0 cellspacing=0 align="center">
        <tr>
            <!-- For Tasks -->
            <td width="47%" valign="top">
                <table width="100%" cellpadding=0 cellspacing=0>
                    <!-- Open Tasks Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Open Tasks
                                                    (<%= strToDate %>)
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="30%" class="txtblackbold12">Module</td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[0]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[1]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[2]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[3]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[4]%>
                                                </td>
                                                <td width="10%" class="txtblackbold12" align="center">Total</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                strTxtBackGround = "";
                                                strCriticalOOSLA = "";
                                                strMajorOOSLA = "";
                                                strNormalOOSLA = "";
                                                strMinorOOSLA = "";
                                                strTrivialOOSLA = "";
                                                strTotalOOSLA = "";

                                                for (int intCount = 0; intCount < arlCurrentOpenTasksCount.size(); intCount += 7)
                                                {
                                                    if (intCounter % 2 == 0)
                                                    {
                                                        strBackGround = "LightGreyBandStyle";
                                                    }
                                                    else
                                                    {
                                                        strBackGround = "DarkGreyBandStyle";
                                                    }
                                                    if (arlCurrentOpenTasksCount.get(intCount).equalsIgnoreCase("Total"))
                                                    {
                                                        strBackGround = "LightBlueBandStyle";
                                                        strTxtBackGround = "txtblackbold12";

                                                        if (!arlCurrentOpenOOLSATasksCount.get(0).equalsIgnoreCase("0"))
                                                        {
                                                            strCriticalOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(0) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSATasksCount.get(1).equalsIgnoreCase("0"))
                                                        {
                                                            strMajorOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(1) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSATasksCount.get(2).equalsIgnoreCase("0"))
                                                        {
                                                            strNormalOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(2) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSATasksCount.get(3).equalsIgnoreCase("0"))
                                                        {
                                                            strMinorOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(3) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSATasksCount.get(4).equalsIgnoreCase("0"))
                                                        {
                                                            strTrivialOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(4) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSATasksCount.get(5).equalsIgnoreCase("0"))
                                                        {
                                                            strTotalOOSLA = "(" + arlCurrentOpenOOLSATasksCount.get(5) + ")";
                                                        }
                                                    }
                                                    else
                                                    {
                                                        strTxtBackGround = "txtgrey";
                                                    }
                                            %>
                                            <tr class="<%= strBackGround %>">
                                                <td class="<%= strTxtBackGround %>"><%= arlCurrentOpenTasksCount.get(intCount) %>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 1) %><span
                                                        class="txtredbold"><%= strCriticalOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 2) %><span
                                                        class="txtredbold"><%= strMajorOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 3) %><span
                                                        class="txtredbold"><%= strNormalOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 4) %><span
                                                        class="txtredbold"><%= strMinorOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 5) %><span
                                                        class="txtredbold"><%= strTrivialOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenTasksCount.get(intCount + 6) %><span
                                                        class="txtredbold"><%= strTotalOOSLA %></span>
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
                    </tr>
                    <!-- Open Tasks End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Tasks Status Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks Status
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="30%" class="txtblackbold12">Tasks</td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[0]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[1]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[2]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[3]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[4]%>
                                                </td>
                                                <td width="10%" class="txtblackbold12" align="center">Total</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlTasksStatusCount.size(); intCount += 7)
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
                                                <td class="txtgrey"><%= arlTasksStatusCount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 1) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 2) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 3) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 4) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 5) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksStatusCount.get(intCount + 6) %>
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
                    </tr>
                    <!-- Tasks Status End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Tasks Resolution Types Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks Resolution
                                                    Types
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlTasksResolutioncount != null && !arlTasksResolutioncount.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Resolution Type</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgrey">Converted To Bug - MS Team, AP, Acquiring, BP</td>
                                                <td class="txtgrey" align="center"><%= intBugsInflowToSystem %>
                                                </td>
                                            </tr>

                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlTasksResolutioncount.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlTasksResolutioncount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksResolutioncount.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Tasks Resolution Types End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Tasks Resolution Chart Start -->
                    <%
                        if (strTaskResolutionChartPath.length() > 0)
                        {
                    %>
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" align="center"><img
                                                        src="<%= strTaskResolutionChartPath %>">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>
                    <%
                        }
                    %>
                    <!-- Tasks Resolution Chart End -->

                    <!-- Bugs Inflow From Tasks Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Actual Bugs
                                                    Inflow To The System From Tasks
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlBugsInflowFromTasks != null && !arlBugsInflowFromTasks.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Team</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlBugsInflowFromTasks.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlBugsInflowFromTasks.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsInflowFromTasks.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Bugs Inflow From Tasks End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Converted To Bugs Moved Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Converted To Bug
                                                    - Analyzed and Moved To Other Team
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlConvertedBugsMovedTeamsCount != null && !arlConvertedBugsMovedTeamsCount.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Moved Team</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlConvertedBugsMovedTeamsCount.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlConvertedBugsMovedTeamsCount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlConvertedBugsMovedTeamsCount.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Converted To Bugs Moved End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Other Tasks Moved Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Other Tasks -
                                                    Analyzed and Moved to Other Teams
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlTasksOthersMovedTeamsCount != null && !arlTasksOthersMovedTeamsCount.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Moved Team</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlTasksOthersMovedTeamsCount.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlTasksOthersMovedTeamsCount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlTasksOthersMovedTeamsCount.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Other Tasks Moved End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Tasks SLA Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks SLA
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="20%" class="txtblackbold12">Priority</td>
                                                <td width="15%" class="txtblackbold12" align="center">In SLA</td>
                                                <td width="20%" class="txtblackbold12" align="center">Out Of SLA</td>
                                                <td width="55%" class="txtblackbold12" align="center">Average No.Of
                                                    Days
                                                </td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                for (int intCount = 0; intCount < arlTasksSLA.size(); intCount += 4)
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
                                                <td class="txtgrey"><%= arlTasksSLA.get(intCount) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlTasksSLA.get(intCount + 1) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlTasksSLA.get(intCount + 2) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlTasksSLA.get(intCount + 3) %>
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
                    </tr>
                    <!-- Tasks SLA End -->
                </table>

            </td>
            <!-- For Tasks END-->

            <!-- Space In Between Two Tables Start -->
            <td width="6%">&nbsp;</td>
            <!-- Space In Between Two Tables End-->

            <!-- For Bugs -->
            <td width="47%" valign="top">
                <table width="100%" cellpadding=0 cellspacing=0>
                    <!-- Open Bugs Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Open Bugs
                                                    (<%= strToDate %>)
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="30%" class="txtblackbold12">Module</td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[0]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[1]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[2]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[3]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[4]%>
                                                </td>
                                                <td width="10%" class="txtblackbold12" align="center">Total</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                strTxtBackGround = "";
                                                strBackGround = "";
                                                strTxtBackGround = "";
                                                strCriticalOOSLA = "";
                                                strMajorOOSLA = "";
                                                strNormalOOSLA = "";
                                                strMinorOOSLA = "";
                                                strTrivialOOSLA = "";
                                                strTotalOOSLA = "";

                                                for (int intCount = 0; intCount < arlCurrentOpenBugsCount.size(); intCount += 7)
                                                {
                                                    if (intCounter % 2 == 0)
                                                    {
                                                        strBackGround = "LightGreyBandStyle";
                                                    }
                                                    else
                                                    {
                                                        strBackGround = "DarkGreyBandStyle";
                                                    }
                                                    if (arlCurrentOpenBugsCount.get(intCount).equalsIgnoreCase("Total"))
                                                    {
                                                        strBackGround = "LightBlueBandStyle";
                                                        strTxtBackGround = "txtblackbold12";

                                                        if (!arlCurrentOpenOOLSABugsCount.get(0).equalsIgnoreCase("0"))
                                                        {
                                                            strCriticalOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(0) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSABugsCount.get(1).equalsIgnoreCase("0"))
                                                        {
                                                            strMajorOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(1) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSABugsCount.get(2).equalsIgnoreCase("0"))
                                                        {
                                                            strNormalOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(2) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSABugsCount.get(3).equalsIgnoreCase("0"))
                                                        {
                                                            strMinorOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(3) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSABugsCount.get(4).equalsIgnoreCase("0"))
                                                        {
                                                            strTrivialOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(4) + ")";
                                                        }
                                                        if (!arlCurrentOpenOOLSABugsCount.get(5).equalsIgnoreCase("0"))
                                                        {
                                                            strTotalOOSLA = "(" + arlCurrentOpenOOLSABugsCount.get(5) + ")";
                                                        }

                                                    }
                                                    else
                                                    {
                                                        strTxtBackGround = "txtgrey";
                                                    }
                                            %>
                                            <tr class="<%= strBackGround %>">
                                                <td class="<%= strTxtBackGround %>"><%= arlCurrentOpenBugsCount.get(intCount) %>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 1) %><span
                                                        class="txtredbold"><%= strCriticalOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 2) %><span
                                                        class="txtredbold"><%= strMajorOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 3) %><span
                                                        class="txtredbold"><%= strNormalOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 4) %><span
                                                        class="txtredbold"><%= strMinorOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 5) %><span
                                                        class="txtredbold"><%= strTrivialOOSLA %></span>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlCurrentOpenBugsCount.get(intCount + 6) %><span
                                                        class="txtredbold"><%= strTotalOOSLA %></span>
                                                </td>
                                            </tr>
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
                    </tr>
                    <!-- Open Bugs End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Bugs Status Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs Status
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="30%" class="txtblackbold12">Bugs</td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[0]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[1]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[2]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[3]%>
                                                </td>
                                                <td width="12%" class="txtblackbold12"
                                                    align="center"><%= AppConstants.TICKET_PRIORITY[4]%>
                                                </td>
                                                <td width="10%" class="txtblackbold12" align="center">Total</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlBugsStatusCount.size(); intCount += 7)
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
                                                <td class="txtgrey"><%= arlBugsStatusCount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 1) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 2) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 3) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 4) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 5) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsStatusCount.get(intCount + 6) %>
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
                    </tr>
                    <!-- Bugs Status End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Bugs Resolution Types Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs Resolution
                                                    Types
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlBugsResolutioncount != null && !arlBugsResolutioncount.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Resolution Type</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";

                                                for (int intCount = 0; intCount < arlBugsResolutioncount.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlBugsResolutioncount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsResolutioncount.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Bugs Resolution Types End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <%
                        if (strBugResolutionChartPath.length() > 0)
                        {
                    %>
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" align="center"><img
                                                        src="<%= strBugResolutionChartPath %>">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>
                    <%
                        }
                    %>

                    <!-- Bugs Moved Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs - Analyzed
                                                    and Moved to Other Teams
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlBugsMovedTeamscount != null && !arlBugsMovedTeamscount.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="70%" class="txtblackbold12">Moved Team</td>
                                                <td width="30%" class="txtblackbold12" align="center">Count</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                for (int intCount = 0; intCount < arlBugsMovedTeamscount.size(); intCount += 2)
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
                                                <td class="txtgrey"><%= arlBugsMovedTeamscount.get(intCount) %>
                                                </td>
                                                <td class="txtgrey"
                                                    align="center"><%= arlBugsMovedTeamscount.get(intCount + 1) %>
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
                        </td>
                    </tr>
                    <!-- Bugs Moved End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Bugs SLA Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs SLA
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="20%" class="txtblackbold12">Priority</td>
                                                <td width="15%" class="txtblackbold12" align="center">In SLA</td>
                                                <td width="20%" class="txtblackbold12" align="center">Out Of SLA</td>
                                                <td width="55%" class="txtblackbold12" align="center">Average No.Of
                                                    Days
                                                </td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                for (int intCount = 0; intCount < arlBugsSLA.size(); intCount += 4)
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
                                                <td class="txtgrey"><%= arlBugsSLA.get(intCount) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlBugsSLA.get(intCount + 1) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlBugsSLA.get(intCount + 2) %>
                                                </td>
                                                <td class="txtgrey" align="center"><%= arlBugsSLA.get(intCount + 3) %>
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
                    </tr>
                    <!-- Bugs SLA End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- Resource Utilization Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Resource
                                                    Utilization
                                                </td>
                                            </tr>
                                        </table>
                                        <%
                                            if (arlResourceUtilization != null && !arlResourceUtilization.isEmpty())
                                            {
                                        %>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                            <tr class="LightBlueBandStyle">
                                                <td width="5%" class="txtblackbold12">S.No</td>
                                                <td width="65%" class="txtblackbold12">Resource Name</td>
                                                <td width="15%" class="txtblackbold12" align="center">Triaging (%)</td>
                                                <td width="15%" class="txtblackbold12" align="center">Fixing (%)</td>
                                            </tr>
                                            <%
                                                intCounter = 0;
                                                strBackGround = "";
                                                strTxtBackGround = "";
                                                String strSlNo = "";

                                                for (int intCount = 0; intCount < arlResourceUtilization.size(); intCount += 3)
                                                {
                                                    if (intCounter % 2 == 0)
                                                    {
                                                        strBackGround = "LightGreyBandStyle";
                                                    }
                                                    else
                                                    {
                                                        strBackGround = "DarkGreyBandStyle";
                                                    }
                                                    if (arlResourceUtilization.get(intCount).equalsIgnoreCase("Total"))
                                                    {
                                                        strBackGround = "LightBlueBandStyle";
                                                        strTxtBackGround = "txtblackbold12";
                                                        strSlNo = "";
                                                    }
                                                    else
                                                    {
                                                        strTxtBackGround = "txtgrey";
                                                        strSlNo = "" + (intCounter + 1);
                                                    }
                                            %>
                                            <tr class="<%= strBackGround %>">
                                                <td class="<%= strTxtBackGround %>" align="right"><%= strSlNo %>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"><%= arlResourceUtilization.get(intCount) %>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlResourceUtilization.get(intCount + 1) %>
                                                </td>
                                                <td class="<%= strTxtBackGround %>"
                                                    align="center"><%= arlResourceUtilization.get(intCount + 2) %>
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
                        </td>
                    </tr>
                    <!-- Resource Utilization End -->

                    <tr>
                        <td width="100%">&nbsp;</td>
                    </tr>

                    <!-- OOSLA Reasons Link Start -->
                    <tr>
                        <td width="100%">
                            <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center"
                                   class="TableBorder1Pix">
                                <tr>
                                    <td width="100%" valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td width="100%" class="DarkBlueBandStyleText14">&nbsp;OOSLA Reasons
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%" cellpadding=2 cellspacing=2 border=1>
                                            <tr class="LightBlueBandStyle">
                                                <td width="100%" class="txtblackbold12">
                                                    <a href="JavaScript:openOOSLAReasons('OpenTasks')"
                                                       title="Ticket Details">Open Tasks (<%= strToDate %>)</a>
                                                </td>
                                            </tr>
                                            <tr class="LightBlueBandStyle">
                                                <td width="100%" class="txtblackbold12">
                                                    <a href="JavaScript:openOOSLAReasons('ClosedTasks')"
                                                       title="Ticket Details">Closed Tasks (<%= strFromDate %>
                                                        To <%= strToDate %>
                                                        )</a>
                                                </td>
                                            </tr>
                                            <tr class="LightBlueBandStyle">
                                                <td width="100%" class="txtblackbold12">
                                                    <a href="JavaScript:openOOSLAReasons('OpenBugs')"
                                                       title="Ticket Details">Open Bugs (<%= strToDate %>)</a>
                                                </td>
                                            </tr>
                                            <tr class="LightBlueBandStyle">
                                                <td width="100%" class="txtblackbold12">
                                                    <a href="JavaScript:openOOSLAReasons('ClosedBugs')"
                                                       title="Ticket Details">Closed Bugs (<%= strFromDate %>
                                                        To <%= strToDate %>
                                                        )</a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!-- OOSLA Reasons Link Start -->
                </table>
            </td>
            <!-- For Bugs END-->
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>
    <%@ include file="../common/line.jsp" %>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (intWeekSummaryCount > 0)
        {
            String strHeadingClass = "";
            String strDataClass = "";
            int intCountWidth = 0;
            int intHeadingWidth = 10;
            String strHeading1 = "";
            String strHeading2 = "";
            String strHeading3 = "";

            if (intWeekSummaryCount < 20)
            {
                strHeadingClass = "txtblackbold12";
                strDataClass = "txtblack12";
                intCountWidth = 4;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
                strHeading3 = "Average Inflow/Day";
            }
            else if (intWeekSummaryCount < 30)
            {
                strHeadingClass = "txtblackbold11";
                strDataClass = "txtblack11";
                intCountWidth = 3;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
                strHeading3 = "Average Inflow/Day";
            }
            else if (intWeekSummaryCount < 40)
            {
                strHeadingClass = "txtblackbold10";
                strDataClass = "txtblack10";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
                strHeading3 = "Average<br>Inflow/Day";
            }
            else
            {
                strHeadingClass = "txtblackbold9";
                strDataClass = "txtblack9";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
                strHeading3 = "Average<br>Inflow/Day";
            }
            int intTableWidth = (intWeekSummaryCount * intCountWidth) + intHeadingWidth;
            int intWidhPerc = (100 - intHeadingWidth) / intWeekSummaryCount;
    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="left">
        <tr>
            <td width="1%">&nbsp;</td>
            <td width="98%">
                <table width="<%= intTableWidth %>%" cellpadding=1 cellspacing=1 border=0 align="left"
                       class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;Weekly Summary</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=2>
                                <tr class="LightBlueBandStyle">
                                    <td width="<%= intHeadingWidth %>%" height="40" class="<%= strHeadingClass %>">
                                        &nbsp;</td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td width="<%= intWidhPerc %>%" height="40" class="<%= strHeadingClass %>"
                                        align="center"><%= arlWeekSummary.get(iCount) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="LightGreyBandStyle">
                                    <td class="<%= strDataClass %>">Received</td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 1) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>">Closed</td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 2) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>">Open</td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 6) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading3 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 3) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="LightGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading1 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 4) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading2 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlWeekSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>" align="center"><%= arlWeekSummary.get(iCount + 5) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="1%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (intMonthSummaryCount > 0)
        {
            String strHeadingClass = "";
            String strDataClass = "";
            int intCountWidth = 0;
            int intHeadingWidth = 10;
            String strHeading1 = "";
            String strHeading2 = "";
            String strHeading3 = "";

            if (intMonthSummaryCount < 20)
            {
                strHeadingClass = "txtblackbold12";
                strDataClass = "txtblack12";
                intCountWidth = 4;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
                strHeading3 = "Average Inflow/Day";
            }
            else if (intMonthSummaryCount < 30)
            {
                strHeadingClass = "txtblackbold11";
                strDataClass = "txtblack11";
                intCountWidth = 3;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
                strHeading3 = "Average Inflow/Day";
            }
            else if (intMonthSummaryCount < 40)
            {
                strHeadingClass = "txtblackbold10";
                strDataClass = "txtblack10";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
                strHeading3 = "Average<br>Inflow/Day";
            }
            else
            {
                strHeadingClass = "txtblackbold9";
                strDataClass = "txtblack9";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
                strHeading3 = "Average<br>Inflow/Day";
            }
            int intTableWidth = (intMonthSummaryCount * intCountWidth) + intHeadingWidth;
            int intWidhPerc = (100 - intHeadingWidth) / intMonthSummaryCount;
    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="left">
        <tr>
            <td width="1%">&nbsp;</td>
            <td width="98%">
                <table width="<%= intTableWidth %>%" cellpadding=1 cellspacing=1 border=0 align="left"
                       class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;Monthly Summary</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=2>
                                <tr class="LightBlueBandStyle">
                                    <td width="<%= intHeadingWidth %>%" height="40" class="<%= strHeadingClass %>">
                                        &nbsp;</td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td width="<%= intWidhPerc %>%" height="40" class="<%= strHeadingClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="LightGreyBandStyle">
                                    <td class="<%= strDataClass %>">Received</td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 1) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>">Closed</td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 2) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>">Open</td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 6) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading3 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 3) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="LightGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading1 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 4) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <tr class="DarkGreyBandStyle">
                                    <td class="<%= strDataClass %>"><%= strHeading2 %>
                                    </td>
                                    <%
                                        for (int iCount = 0; iCount < arlMonthSummary.size(); iCount += 7)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthSummary.get(iCount + 5) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="1%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (intWeekResourceSummaryCount > 0)
        {
            String strHeadingClass = "";
            String strDataClass = "";
            int intCountWidth = 0;
            int intHeadingWidth = 10;
            String strHeading1 = "";
            String strHeading2 = "";

            if (intWeekResourceSummaryCount < 20)
            {
                strHeadingClass = "txtblackbold12";
                strDataClass = "txtblack12";
                intCountWidth = 4;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
            }
            else if (intWeekResourceSummaryCount < 30)
            {
                strHeadingClass = "txtblackbold11";
                strDataClass = "txtblack11";
                intCountWidth = 3;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
            }
            else if (intWeekResourceSummaryCount < 40)
            {
                strHeadingClass = "txtblackbold10";
                strDataClass = "txtblack10";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
            }
            else
            {
                strHeadingClass = "txtblackbold9";
                strDataClass = "txtblack9";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
            }
            int intTableWidth = (intWeekResourceSummaryCount * intCountWidth) + intHeadingWidth;
            int intWidhPerc = (100 - intHeadingWidth) / intWeekResourceSummaryCount;

            int intNoOfDataRows = arlWeekResourceSummary.size() / intWeekResourceSummaryCount;

    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="left">
        <tr>
            <td width="1%">&nbsp;</td>
            <td width="98%">
                <table width="<%= intTableWidth %>%" cellpadding=1 cellspacing=1 border=0 align="left"
                       class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;Weekly Summary - By
                                        Resource
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=2>
                                <tr class="LightBlueBandStyle">
                                    <%
                                        for (int iCount = 0; iCount < arlWeekDisplayDates.size(); iCount++)
                                        {
                                    %>
                                    <td width="<%= intWidhPerc %>%" height="40" class="<%= strHeadingClass %>"
                                        align="center"><%= arlWeekDisplayDates.get(iCount) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <%
                                    int intRowCounter = 0;
                                    int intDataIncrementer = 0;
                                    while (intRowCounter < intNoOfDataRows)
                                    {
                                %>
                                <tr class="LightGreyBandStyle">
                                    <%
                                        int intDataCounter = 0;
                                        while (intDataCounter < intWeekResourceSummaryCount)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlWeekResourceSummary.get(intDataIncrementer++) %>
                                    </td>
                                    <%
                                            intDataCounter++;
                                        }
                                    %>
                                </tr>
                                <%
                                        intRowCounter++;
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="1%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>

    <%
        if (intMonthResourceSummaryCount > 0)
        {
            String strHeadingClass = "";
            String strDataClass = "";
            int intCountWidth = 0;
            int intHeadingWidth = 10;
            String strHeading1 = "";
            String strHeading2 = "";

            if (intMonthResourceSummaryCount < 20)
            {
                strHeadingClass = "txtblackbold12";
                strDataClass = "txtblack12";
                intCountWidth = 4;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
            }
            else if (intMonthResourceSummaryCount < 30)
            {
                strHeadingClass = "txtblackbold11";
                strDataClass = "txtblack11";
                intCountWidth = 3;
                strHeading1 = "Average Resource";
                strHeading2 = "Average Closed";
            }
            else if (intMonthResourceSummaryCount < 40)
            {
                strHeadingClass = "txtblackbold10";
                strDataClass = "txtblack10";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
            }
            else
            {
                strHeadingClass = "txtblackbold9";
                strDataClass = "txtblack9";
                intCountWidth = 2;
                strHeading1 = "Average<br>Resource";
                strHeading2 = "Average<br>Closed";
            }
            int intTableWidth = (intMonthResourceSummaryCount * intCountWidth) + intHeadingWidth;
            int intWidhPerc = (100 - intHeadingWidth) / intMonthResourceSummaryCount;

            int intNoOfDataRows = arlMonthResourceSummary.size() / intMonthResourceSummaryCount;

    %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="left">
        <tr>
            <td width="1%">&nbsp;</td>
            <td width="98%">
                <table width="<%= intTableWidth %>%" cellpadding=1 cellspacing=1 border=0 align="left"
                       class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;Monthly Summary - By
                                        Resource
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=2>
                                <tr class="LightBlueBandStyle">
                                    <%
                                        for (int iCount = 0; iCount < arlMonthDisplayDates.size(); iCount++)
                                        {
                                    %>
                                    <td width="<%= intWidhPerc %>%" height="40" class="<%= strHeadingClass %>"
                                        align="center"><%= arlMonthDisplayDates.get(iCount) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <%
                                    int intRowCounter = 0;
                                    int intDataIncrementer = 0;
                                    while (intRowCounter < intNoOfDataRows)
                                    {
                                %>
                                <tr class="LightGreyBandStyle">
                                    <%
                                        int intDataCounter = 0;
                                        while (intDataCounter < intMonthResourceSummaryCount)
                                        {
                                    %>
                                    <td class="<%= strDataClass %>"
                                        align="center"><%= arlMonthResourceSummary.get(intDataIncrementer++) %>
                                    </td>
                                    <%
                                            intDataCounter++;
                                        }
                                    %>
                                </tr>
                                <%
                                        intRowCounter++;
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="1%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0>
        <tr>
            <td width="5%">&nbsp;
            </td>
            <td width="90%">
                <table cellpadding=0 cellspacing=0 border=1 style="border-style: solid; border-color: #f2f2f2"
                       align="left">
                    <tr>
                        <td><img src="<%= strWeekSummaryChartPath%>">
                        </td>
                    </tr>
                </table>
            </td>
            <td width="5%">&nbsp;
            </td>
        </tr>
    </table>

    <%@ include file="../common/twospace.jsp" %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0>
        <tr>
            <td width="5%">&nbsp;
            </td>
            <td width="90%">
                <table cellpadding=0 cellspacing=0 border=1 style="border-style: solid; border-color: #f2f2f2"
                       align="left">
                    <tr>
                        <td><img src="<%= strWeekResourceClosureChartPath%>">
                        </td>
                    </tr>
                </table>
            </td>
            <td width="5%">&nbsp;
            </td>
        </tr>
    </table>

    <%@ include file="../common/twospace.jsp" %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0>
        <tr>
            <td width="5%">&nbsp;
            </td>
            <td width="90%">
                <table cellpadding=0 cellspacing=0 border=1 style="border-style: solid; border-color: #f2f2f2"
                       align="left">
                    <tr>
                        <td><img src="<%= strTaskResolutionWeekSummaryChartPath%>">
                        </td>
                    </tr>
                </table>
            </td>
            <td width="5%">&nbsp;
            </td>
        </tr>
    </table>

    <%@ include file="../common/twospace.jsp" %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0>
        <tr>
            <td width="5%">&nbsp;
            </td>
            <td width="90%">
                <table cellpadding=0 cellspacing=0 border=1 style="border-style: solid; border-color: #f2f2f2"
                       align="left">
                    <tr>
                        <td><img src="<%= strBugResolutionWeekSummaryChartPath%>">
                        </td>
                    </tr>
                </table>
            </td>
            <td width="5%">&nbsp;
            </td>
        </tr>
    </table>

    <%@ include file="../common/twospace.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
