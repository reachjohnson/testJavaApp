<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlRQATicketDetailsForView = (ArrayList) request.getAttribute("RQATicketDetailsForView");
    ArrayList<String> arlRQATicketActivityHistory = (ArrayList) request.getAttribute("RQATicketActivityHistory");
    ArrayList<String> arlRQACommentsCategory = (ArrayList) request.getAttribute("RQACommentsCategory");
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bug Details
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">RQA Phase</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(0) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">RQA Cycle</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(1) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" colspan="2" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Id</td>
                                    <td width="82%" class="txtblue"><%= arlRQATicketDetailsForView.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" colspan="2" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Description</td>
                                    <td width="82%" class="txtblue"><%= arlRQATicketDetailsForView.get(3) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Priority</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(4) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Type</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(5) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Raised By</td>
                                    <td width="82%" class="txtblue"><%= arlRQATicketDetailsForView.get(6) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <%
                        if (arlRQATicketDetailsForView.get(12).equalsIgnoreCase(AppConstants.COMPLETED))
                        {
                    %>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Bug Resolution</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(8) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Moved Team</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(9) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Root Cause</td>
                                    <td width="82%" class="txtblue"><%= arlRQATicketDetailsForView.get(10) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Comments</td>
                                    <td width="82%"><pre class="txtblue"><%= arlRQATicketDetailsForView.get(11) %></pre>
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
                                    <td width="36%" class="txtbluebold">Received Date</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(7) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Actual End Date</td>
                                    <td width="64%" class="txtblue"><%= arlRQATicketDetailsForView.get(13) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Assignee</td>
                                    <td width="82%" class="txtblue"><%= arlRQATicketDetailsForView.get(14) %>
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
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bug Comments History
                        </td>
                    </tr>
                </table>
                <%
                    if (arlRQACommentsCategory != null && !arlRQACommentsCategory.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="70%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Comments Date</td>
                        <td width="15%" class="txtblackbold12">Comments By</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlRQACommentsCategory.size(); iCount += 3)
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
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount + 2) %></pre>
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
    <%@ include file="../common/onespace.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bug Activity History
                        </td>
                    </tr>
                </table>
                <%
                    if (arlRQATicketActivityHistory != null && !arlRQATicketActivityHistory.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="10%" class="txtblackbold12">Activity Type</td>
                        <td width="55%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Activity Date</td>
                        <td width="20%" class="txtblackbold12">Activity By</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlRQATicketActivityHistory.size(); iCount += 4)
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
                        <td><pre class="txtgrey"><%= arlRQATicketActivityHistory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQATicketActivityHistory.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQATicketActivityHistory.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQATicketActivityHistory.get(iCount + 3) %></pre>
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