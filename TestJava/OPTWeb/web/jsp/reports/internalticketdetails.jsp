<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlITCommentsHistory = (ArrayList) request.getAttribute("ITCommentsHistory");
    ArrayList<String> arlInternalTicketDetails = (ArrayList) request.getAttribute("InternalTicketDetails");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>


</head>
<body>
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Internal Ticket Details
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Ticket ID</td>
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(1) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Ticket Description</td>
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Action Required</td>
                                    <td width="82%"><pre class="txtblue"><%= arlInternalTicketDetails.get(3) %></pre>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Start Date</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(4) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">ETA</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(9) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%
                        if(arlInternalTicketDetails.get(11).equalsIgnoreCase(AppConstants.COMPLETED))
                        {
                    %>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">End Date</td>
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(10) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Created By</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(6) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Assignee</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(8) %>
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

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Comments History
                        </td>
                    </tr>
                </table>
                <%
                    if (arlITCommentsHistory != null && !arlITCommentsHistory.isEmpty())
                    {
                        int intCounter = 0;
                        String strBackGround = "";
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="65%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Comments By</td>
                        <td width="20%" class="txtblackbold12">Comments Date</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlITCommentsHistory.size(); iCount += 3)
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
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount + 1) %></pre>
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
    <%@ include file="../common/twospace.jsp" %>

    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="100%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Close" onclick="JavaScript:frmClose()"/>
            </td>
        </tr>
    </table>

</form>
</body>
</html>