<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlInternalTickets = (ArrayList) request.getAttribute("InternalTickets");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function InternalTicketDetails(RefNo, TicketId) {
            document.OPTForm.hidRefNo.value = RefNo;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadInternalTicketDetailsDummy?ACCESS=RESTRICTED", 600, 1300);
        }
    </script>
</head>
<body onFocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Open Internal Tickets
                            (<%= arlInternalTickets.size() / 8 %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlInternalTickets != null && !arlInternalTickets.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="7%" class="txtblackbold12">Ticket ID</td>
                        <td width="35%" class="txtblackbold12">Action Required</td>
                        <td width="13%" class="txtblackbold12">Assignee</td>
                        <td width="5%" class="txtblackbold12">ETA</td>
                        <td width="15%" class="txtblackbold12">Start Date</td>
                        <td width="10%" class="txtblackbold12">Comments</td>
                        <td width="12%" class="txtblackbold12">Created By</td>

                    </tr>
                    <%
                        int intSerialNo = 0;
                        int intCounter = 0;
                        String strBackGround = "";


                        for (int iCount = 0; iCount < arlInternalTickets.size(); iCount += 8)
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
                        <td class="txtgrey"><%= ++intSerialNo %>
                        <td title="<%= arlInternalTickets.get(iCount + 1) %>"><pre class="txtgrey"><%= arlInternalTickets.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTickets.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTickets.get(iCount + 3) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTickets.get(iCount + 7) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlInternalTickets.get(iCount + 4) %></pre>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:InternalTicketDetails('<%= arlInternalTickets.get(iCount + 6) %>', '<%= arlInternalTickets.get(iCount) %>')">
                            <pre class="txtgrey">Comments History</pre></a></td>
                        <td><pre class="txtgrey"><%= arlInternalTickets.get(iCount + 5) %></pre>
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
</form>
</body>
</html>
