<%
    if ((arlInternalTicketDetailsCreatedByYou != null && !arlInternalTicketDetailsCreatedByYou.isEmpty()) || (arlInternalTicketDetails != null && !arlInternalTicketDetails.isEmpty()))
    {
%>
<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <%
                if (arlInternalTicketDetails != null && !arlInternalTicketDetails.isEmpty())
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Internal Tickets Assigned To You
                        (<%= intOpenInternalTickets%>)
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="10%" class="txtblackbold12">Ticket ID</td>
                    <td width="7%" class="txtblackbold12">Action</td>
                    <td width="25%" class="txtblackbold12">Ticket Description</td>
                    <td width="28%" class="txtblackbold12">Action Required</td>
                    <td width="7%" class="txtblackbold12">ETA</td>
                    <td width="13%" class="txtblackbold12">Start Date</td>
                    <td width="15%" class="txtblackbold12">Created By</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";
                    String strTicketDesc = "";
                    for (int iCount = 0; iCount < arlInternalTicketDetails.size(); iCount += 9)
                    {
                        strTicketDesc = arlInternalTicketDetails.get(iCount + 2);
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
                    <td>
                        <%
                            if (!strTicketDesc.equalsIgnoreCase("Other Internal Ticket"))
                            {
                        %>
                        <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlInternalTicketDetails.get(iCount + 1) %>', '<%= arlInternalTicketDetails.get(iCount + 1) %>')"
                           class="link"><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 1) %></pre>
                        </a>
                        <%
                        }
                        else
                        {
                        %>
                        <pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 1) %></pre>
                        <%
                            }
                        %>
                    </td>
                    <td align="center" valign="middle">
                        <table>
                            <tr>
                                <td valign="middle"><a href="JavaScript:updateInternalTicket('<%= arlInternalTicketDetails.get(iCount) %>', '<%= arlInternalTicketDetails.get(iCount+1) %>', '<%= arlInternalTicketDetails.get(iCount+8) %>')">
                                    <img src="images/update_new.jpg" border="0" title="Update Internal Ticket"></a></td>
                                <td valign="middle"><a href="JavaScript:CloseInternalTicket('<%= arlInternalTicketDetails.get(iCount) %>', '<%= arlInternalTicketDetails.get(iCount+1) %>')">
                                    <img src="images/closeicon_new.png" border="0" title="Close Internal Ticket"></a></td>
                                <td valign="middle"><a href="JavaScript:AddComments('<%= arlInternalTicketDetails.get(iCount) %>', '<%= arlInternalTicketDetails.get(iCount+1) %>')">
                                    <img src="images/comments_new.jpg" border="0" title="Add Comments"></a></td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <%
                            if (!strTicketDesc.equalsIgnoreCase("Other Internal Ticket"))
                            {
                        %>
                        <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlInternalTicketDetails.get(iCount + 1) %>', '<%= arlInternalTicketDetails.get(iCount + 1) %>')"
                           class="link"><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 2) %></pre>
                        </a>
                        <%
                        }
                        else
                        {
                        %>
                        <pre class="txtgrey"><%= strTicketDesc %></pre>
                        <%
                            }
                        %>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 3) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 6) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 4) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 5) %></pre>
                    </td>
                </tr>
                <%
                        intCounter++;
                    }
                %>
            </table>
            <%
                }
            %>
            <%
                if (arlInternalTicketDetailsCreatedByYou != null && !arlInternalTicketDetailsCreatedByYou.isEmpty())
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Internal Tickets Created By You
                        (<%= arlInternalTicketDetailsCreatedByYou.size() / 8%>)
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="10%" class="txtblackbold12">Ticket ID</td>
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="20%" class="txtblackbold12">Ticket Description</td>
                    <td width="25%" class="txtblackbold12">Action Required</td>
                    <td width="7%" class="txtblackbold12">ETA</td>
                    <td width="13%" class="txtblackbold12">Start Date</td>
                    <td width="15%" class="txtblackbold12">Assignee</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";
                    String strTicketDesc = "";
                    for (int iCount = 0; iCount < arlInternalTicketDetailsCreatedByYou.size(); iCount += 8)
                    {
                        strTicketDesc = arlInternalTicketDetailsCreatedByYou.get(iCount + 2);
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
                    <td>
                        <%
                            if (!strTicketDesc.equalsIgnoreCase("Other Internal Ticket"))
                            {
                        %>
                        <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %>', '<%= arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %>')"
                           class="link"><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %></pre>
                        </a>
                        <%
                        }
                        else
                        {
                        %>
                        <pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %></pre>
                        <%
                            }
                        %>
                    </td>
                    <td align="center">
                        <table>
                            <tr>
                                <td valign="middle"><a href="JavaScript:updateInternalTicket('<%= arlInternalTicketDetailsCreatedByYou.get(iCount) %>', '<%= arlInternalTicketDetailsCreatedByYou.get(iCount+1) %>', '<%= arlInternalTicketDetailsCreatedByYou.get(iCount + 7) %>')">
                                    <img src="images/update_new.jpg" border="0" title="Update Internal Ticket"></a></td>
                                <td valign="middle"><a href="JavaScript:AddComments('<%= arlInternalTicketDetailsCreatedByYou.get(iCount) %>', '<%= arlInternalTicketDetailsCreatedByYou.get(iCount+1) %>')">
                                    <img src="images/comments_new.jpg" border="0" title="Add Comments"></a></td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <%
                            if (!strTicketDesc.equalsIgnoreCase("Other Internal Ticket"))
                            {
                        %>
                        <a href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %>', '<%= arlInternalTicketDetailsCreatedByYou.get(iCount + 1) %>')"
                           class="link"><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 2) %></pre>
                        </a>
                        <%
                        }
                        else
                        {
                        %>
                        <pre class="txtgrey"><%= strTicketDesc %></pre>
                        <%
                            }
                        %>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 3) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 6) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 4) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetailsCreatedByYou.get(iCount + 5) %></pre>
                    </td>
                </tr>
                <%
                        intCounter++;
                    }
                %>
            </table>
            <%
                }
            %>
        </td>
    </tr>
</table>
<%@ include file="../common/twospace.jsp" %>
<%
    }
%>