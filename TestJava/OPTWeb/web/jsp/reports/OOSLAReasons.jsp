<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlTasksOOSLAReasons = (ArrayList) request.getAttribute("TasksOOSLAReasons");
    ArrayList<String> arlBugsOOSLAReasons = (ArrayList) request.getAttribute("BugsOOSLAReasons");

    ArrayList<String> arlCurrentOpenTasksOOSLAReasons = (ArrayList) request.getAttribute("CurrentOpenTasksOOSLAReasons");
    ArrayList<String> arlCurrentOpenBugsOOSLAReasons = (ArrayList) request.getAttribute("CurrentOpenBugsOOSLAReasons");
    String strMessageToDisplay = AppUtil.checkNull((String) request.getAttribute("MessageToDisplay"));
    String strOOSLAReasonsAction = AppUtil.checkNull((String) request.getAttribute("OOSLAReasonsAction"));

    int intSerialNo = 0;
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

    </script>


</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="header">&nbsp;&nbsp;<%= strMessageToDisplay %>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>

    <%
        if (strOOSLAReasonsAction.equalsIgnoreCase("ClosedTasks"))
        {
            if (arlTasksOOSLAReasons != null && !arlTasksOOSLAReasons.isEmpty())
            {
    %>

    <%
        for (int intCount = 0; intCount < arlTasksOOSLAReasons.size(); intCount += 7)
        {
    %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightBlueBandStyle">
                        <td width="10%" class="txtblackbold12">Ticket Id</td>
                        <td width="90%"
                            class="txtblack"><%= ++intSerialNo + " - " + arlTasksOOSLAReasons.get(intCount) %>
                        </td>
                    </tr>
                </table>
            </td>
            <!--        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Received Date</td>
                    <td width="64%" class="txtblue"><%//= arlTasksOOSLAReasons.get(intCount + 2) %>
                    </td>
                </tr>
            </table>
        </td>
-->
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Ticket Desc</td>
                        <td width="90%" class="txtblue"><%= arlTasksOOSLAReasons.get(intCount + 1) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Assignee</td>
                        <td width="90%" class="txtblue"><%= arlTasksOOSLAReasons.get(intCount + 6) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!--
    <tr>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">SLA End Date</td>
                    <td width="64%" class="txtblue"><%//= arlTasksOOSLAReasons.get(intCount + 3) %>
                    </td>
                </tr>
            </table>
        </td>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Actual End Date</td>
                    <td width="64%" class="txtblue"><%//= arlTasksOOSLAReasons.get(intCount + 4) %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
-->
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="DarkGreyBandStyle">
                        <td width="10%" class="txtbluebold">Comments</td>
                        <td width="90%"><pre class="txtblue"><%= arlTasksOOSLAReasons.get(intCount + 5) %></pre>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%
        }
    %>

    <%
    }
    else
    {
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="Left">
        <tr>
            <td class="txtblackbold13">&nbsp;No Data Available</td>
        </tr>
    </table>

    <%
            }
        }
    %>


    <%
        if (strOOSLAReasonsAction.equalsIgnoreCase("ClosedBugs"))
        {

            if (arlBugsOOSLAReasons != null && !arlBugsOOSLAReasons.isEmpty())
            {
    %>
    <%
        for (int intCount = 0; intCount < arlBugsOOSLAReasons.size(); intCount += 7)
        {
    %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightBlueBandStyle">
                        <td width="10%" class="txtblackbold12">Ticket Id</td>
                        <td width="90%"
                            class="txtblack"><%= ++intSerialNo + " - " + arlBugsOOSLAReasons.get(intCount) %>
                        </td>
                    </tr>
                </table>
            </td>
            <!--        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Received Date</td>
                    <td width="64%" class="txtblue"><%//= arlBugsOOSLAReasons.get(intCount + 2) %>
                    </td>
                </tr>
            </table>
        </td>
-->
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Ticket Desc</td>
                        <td width="90%" class="txtblue"><%= arlBugsOOSLAReasons.get(intCount + 1) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Assignee</td>
                        <td width="90%" class="txtblue"><%= arlBugsOOSLAReasons.get(intCount + 6) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!--
    <tr>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">SLA End Date</td>
                    <td width="64%" class="txtblue"><%//= arlBugsOOSLAReasons.get(intCount + 3) %>
                    </td>
                </tr>
            </table>
        </td>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Actual End Date</td>
                    <td width="64%" class="txtblue"><%//= arlBugsOOSLAReasons.get(intCount + 4) %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
-->
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="DarkGreyBandStyle">
                        <td width="10%" class="txtbluebold">Comments</td>
                        <td width="90%"><pre class="txtblue"><%= arlBugsOOSLAReasons.get(intCount + 5) %></pre>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%
        }
    %>

    <%
    }
    else
    {
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="Left">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td class="txtblackbold13">&nbsp;No Data Available</td>
        </tr>
    </table>

    <%
            }
        }
    %>


    <%
        if (strOOSLAReasonsAction.equalsIgnoreCase("OpenTasks"))
        {

            if (arlCurrentOpenTasksOOSLAReasons != null && !arlCurrentOpenTasksOOSLAReasons.isEmpty())
            {
    %>

    <%
        for (int intCount = 0; intCount < arlCurrentOpenTasksOOSLAReasons.size(); intCount += 7)
        {
    %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightBlueBandStyle">
                        <td width="10%" class="txtblackbold12">Ticket Id</td>
                        <td width="90%"
                            class="txtblack"><%= ++intSerialNo + " - " + arlCurrentOpenTasksOOSLAReasons.get(intCount) %>
                        </td>
                    </tr>
                </table>
            </td>
            <!--        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Received Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenTasksOOSLAReasons.get(intCount + 2) %>
                    </td>
                </tr>
            </table>
        </td>
-->
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Ticket Desc</td>
                        <td width="90%" class="txtblue"><%= arlCurrentOpenTasksOOSLAReasons.get(intCount + 1) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Assignee</td>
                        <td width="90%" class="txtblue"><%= arlCurrentOpenTasksOOSLAReasons.get(intCount + 6) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!--
    <tr>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">SLA End Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenTasksOOSLAReasons.get(intCount + 3) %>
                    </td>
                </tr>
            </table>
        </td>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Actual End Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenTasksOOSLAReasons.get(intCount + 4) %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
-->
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="DarkGreyBandStyle">
                        <td width="10%" class="txtbluebold">Comments</td>
                        <td width="90%"><pre class="txtblue"><%= arlCurrentOpenTasksOOSLAReasons.get(intCount + 5) %></pre>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%
        }
    %>

    <%
    }
    else
    {
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="Left">
        <tr>
            <td class="txtblackbold13">&nbsp;No Data Available</td>
        </tr>
    </table>

    <%
            }
        }
    %>


    <%
        if (strOOSLAReasonsAction.equalsIgnoreCase("OpenBugs"))
        {

            if (arlCurrentOpenBugsOOSLAReasons != null && !arlCurrentOpenBugsOOSLAReasons.isEmpty())
            {
    %>

    <%
        for (int intCount = 0; intCount < arlCurrentOpenBugsOOSLAReasons.size(); intCount += 7)
        {
    %>

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightBlueBandStyle">
                        <td width="10%" class="txtblackbold12">Ticket Id</td>
                        <td width="90%"
                            class="txtblack"><%= ++intSerialNo + " - " + arlCurrentOpenBugsOOSLAReasons.get(intCount) %>
                        </td>
                    </tr>
                </table>
            </td>
            <!--        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Received Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenBugsOOSLAReasons.get(intCount + 2) %>
                    </td>
                </tr>
            </table>
        </td>
-->
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Ticket Desc</td>
                        <td width="90%" class="txtblue"><%= arlCurrentOpenBugsOOSLAReasons.get(intCount + 1) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="LightGreyBandStyle">
                        <td width="10%" class="txtbluebold">Assignee</td>
                        <td width="90%" class="txtblue"><%= arlCurrentOpenBugsOOSLAReasons.get(intCount + 6) %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!--
    <tr>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">SLA End Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenBugsOOSLAReasons.get(intCount + 3) %>
                    </td>
                </tr>
            </table>
        </td>
        <td width="50%" class="txtgrey">
            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                <tr class="LightBandStyle">
                    <td width="36%" class="txtbluebold">Actual End Date</td>
                    <td width="64%" class="txtblue"><%//= arlCurrentOpenBugsOOSLAReasons.get(intCount + 4) %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
-->
        <tr>
            <td width="100%" class="txtgrey" colspan="2">
                <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                    <tr class="DarkGreyBandStyle">
                        <td width="10%" class="txtbluebold">Comments</td>
                        <td width="90%"><pre class="txtblue"><%= arlCurrentOpenBugsOOSLAReasons.get(intCount + 5) %></pre>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>

    <%
        }
    %>

    <%
    }
    else
    {
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="Left">
        <tr>
            <td class="txtblackbold13">&nbsp;No Data Available</td>
        </tr>
    </table>

    <%
            }
        }
    %>


    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td width="100%" align="center" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Close" onclick="JavaScript:frmClose()"/>
            </td>
        </tr>
    </table>

</form>
</body>
</html>