<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strToday = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY);
    ArrayList<String> arlRQACurrentStatus = (ArrayList) request.getAttribute("RQACurrentStatus");
    String strRQACurrentPhase = AppUtil.checkNull((String) request.getAttribute("RQACurrentPhase"));
    boolean blnRQACurrentPhase = strRQACurrentPhase.length() > 0;
    String strOpeningBalance = AppUtil.checkNull((String) request.getAttribute("OpeningBalance"));
    ArrayList<String> arlRQANewBugs = (ArrayList) request.getAttribute("RQANewBugs");
    ArrayList<String> arlRQAResolvedBugs = (ArrayList) request.getAttribute("RQAResolvedBugs");
    ArrayList<String> arlRQACurrentOpenBugs = (ArrayList) request.getAttribute("RQACurrentOpenBugs");
    ArrayList<String> arlRQAResolutionSettings = (ArrayList) request.getAttribute("RQAResolutionSettings");

    String strResolutionBugMoved = "";
    if (arlRQAResolutionSettings != null && !arlRQAResolutionSettings.isEmpty())
    {
        strResolutionBugMoved = arlRQAResolutionSettings.get(0);
    }


    int intClosingBalance = 0;
    if (arlRQACurrentOpenBugs != null && !arlRQACurrentOpenBugs.isEmpty())
    {
        intClosingBalance = arlRQACurrentOpenBugs.size() / 6;
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
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
        }

    </script>

</head>
<body onload="displaymessage()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA <%= strRQACurrentPhase %> - Daily Report</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=1>
                    <tr>
                        <td width="90%" class="DarkBlueBandStyleText14">Opening Balance On <%= strToday %>
                        </td>
                        <td width="10%" class="CreamBandStyle" align="center"><%= strOpeningBalance %>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="75%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table width="99%" cellpadding=1 cellspacing=1 border=1 align="center">
        <tr class="DarkBlueBandStyleText14">
            <td width="100%" colspan="6" align="center">New Bugs</td>
        </tr>
        <tr class="LightBlueBandStyleText">
            <td width="5%">Sl.No</td>
            <td width="10%">Bug Id</td>
            <td width="58%">Bug Description</td>
            <td width="7">Priority</td>
            <td width="20%">Assigned To</td>
        </tr>
        <%
            if (arlRQANewBugs != null && !arlRQANewBugs.isEmpty())
            {
                int intSlNo = 1;
                for (int intCount = 0; intCount < arlRQANewBugs.size(); intCount += 6)
                {
        %>
        <tr class="WhiteBandStyle12">
            <td><%= intSlNo++ %>
            </td>
            <td><%= arlRQANewBugs.get(intCount) %>
            </td>
            <td><%= arlRQANewBugs.get(intCount + 1) %>
            </td>
            <td><%= arlRQANewBugs.get(intCount + 2) %>
            </td>
            <td><%= arlRQANewBugs.get(intCount + 3) %>
            </td>
        </tr>
        <%
            }
        }
        else
        {
        %>
        <tr class="WhiteBandStyle12">
            <td colspan="6" align="center" class="txtblackbold14">No Data Available</td>
        </tr>
        <%
            }
        %>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table width="99%" cellpadding=1 cellspacing=1 border=1 align="center">
        <tr class="DarkBlueBandStyleText14">
            <td width="100%" colspan="6" align="center">Resolved</td>
        </tr>
        <tr class="LightBlueBandStyleText">
            <td width="5%">Sl.No</td>
            <td width="10%">Bug Id</td>
            <td width="45%">Bug Description</td>
            <td width="7">Priority</td>
            <td width="15%">Assigned To</td>
            <td width="18%">Resolution</td>
        </tr>
        <%
            if (arlRQAResolvedBugs != null && !arlRQAResolvedBugs.isEmpty())
            {
                int intSlNo = 1;
                String strTicketResolution;
                String strMovedDomain;
                for (int intCount = 0; intCount < arlRQAResolvedBugs.size(); intCount += 6)
                {
                    strTicketResolution = arlRQAResolvedBugs.get(intCount + 4);
                    strMovedDomain = arlRQAResolvedBugs.get(intCount + 5);
                    if (strTicketResolution.equalsIgnoreCase(strResolutionBugMoved))
                    {
                        strTicketResolution += " - " + strMovedDomain;
                    }

        %>
        <tr class="WhiteBandStyle12">
            <td><%= intSlNo++ %>
            </td>
            <td><%= arlRQAResolvedBugs.get(intCount) %>
            </td>
            <td><%= arlRQAResolvedBugs.get(intCount + 1) %>
            </td>
            <td><%= arlRQAResolvedBugs.get(intCount + 2) %>
            </td>
            <td><%= arlRQAResolvedBugs.get(intCount + 3) %>
            </td>
            <td><%=  strTicketResolution %>
            </td>
        </tr>
        <%
            }
        }
        else
        {
        %>
        <tr class="WhiteBandStyle12">
            <td colspan="6" align="center" class="txtblackbold14">No Data Available</td>
        </tr>
        <%
            }
        %>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table width="95%" cellpadding=1 cellspacing=1 border=1>
        <tr class="DarkBlueBandStyleText14">
            <td width="100%" colspan="7" align="center">Current Open Bugs</td>
        </tr>
        <tr class="LightBlueBandStyleText">
            <td width="5%">Sl.No</td>
            <td width="10%">Bug Id</td>
            <td width="40%">Bug Description</td>
            <td width="6">Priority</td>
            <td width="15%">Assigned To</td>
            <td width="14%">Received</td>
            <td width="10%">ETA</td>
        </tr>
        <%
            if (arlRQACurrentOpenBugs != null && !arlRQACurrentOpenBugs.isEmpty())
            {
                int intSlNo = 1;
                String strETA = "";
                for (int intCount = 0; intCount < arlRQACurrentOpenBugs.size(); intCount += 6)
                {
                    if (arlRQACurrentOpenBugs.get(intCount + 5).length() > 0)
                    {
                        strETA = arlRQACurrentOpenBugs.get(intCount + 5);
                    }
                    else
                    {
                        strETA = "Yet To Start";
                    }
        %>
        <tr class="WhiteBandStyle12">
            <td><%= intSlNo++ %>
            </td>
            <td><%= arlRQACurrentOpenBugs.get(intCount) %>
            </td>
            <td><%= arlRQACurrentOpenBugs.get(intCount + 1) %>
            </td>
            <td><%= arlRQACurrentOpenBugs.get(intCount + 2) %>
            </td>
            <td><%= arlRQACurrentOpenBugs.get(intCount + 3) %>
            </td>
            <td><%= arlRQACurrentOpenBugs.get(intCount + 4) %>
            </td>
            <td><%= strETA %>
            </td>
        </tr>
        <%
            }
        }
        else
        {
        %>
        <tr class="WhiteBandStyle12">
            <td colspan="7" align="center" class="txtblackbold14">No Data Available</td>
        </tr>
        <%
            }
        %>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0>
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=1>
                    <tr>
                        <td width="90%" class="DarkBlueBandStyleText14">Closing Balance On <%= strToday %>
                        </td>
                        <td width="10%" class="CreamBandStyle" align="center"><%= intClosingBalance %>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="75%">&nbsp;</td>
        </tr>
    </table>

</form>
</body>
</html>
