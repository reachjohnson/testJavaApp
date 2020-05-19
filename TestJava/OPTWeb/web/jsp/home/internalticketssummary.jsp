<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;List of Internal Tickets
                        (<%= intOpenInternalTickets %>)
                    </td>
                </tr>
            </table>
            <%
                if (arlInternalTicketDetails != null && !arlInternalTicketDetails.isEmpty())
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="3%" class="txtblackbold12">S.No</td>
                    <td width="10%" class="txtblackbold12">Ticket ID</td>
                    <td width="20%" class="txtblackbold12">Ticket Description</td>
                    <td width="26%" class="txtblackbold12">Action Required</td>
                    <td width="7%" class="txtblackbold12">ETA</td>
                    <td width="10%" class="txtblackbold12">Comments</td>
                    <td width="12%" class="txtblackbold12">Start Date</td>
                    <td width="12%" class="txtblackbold12">Assignee</td>
                </tr>
                <%
                    int intCounter = 0;
                    int intSerialNo = 0;
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
                    <td class="txtgrey" align="right"><%= ++intSerialNo %>
                    </td>
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
                    <td><a href="JavaScript:InternalTicketDetails('<%= arlInternalTicketDetails.get(iCount) %>', '<%= arlInternalTicketDetails.get(iCount+1) %>')">
                        <pre class="txtgrey">Comments History</pre></a>
                    </td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 4) %></pre></td>
                    <td><pre class="txtgrey"><%= arlInternalTicketDetails.get(iCount + 7) %></pre></td>
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
