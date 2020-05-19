<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Ticket Progress History
                    </td>
                </tr>
            </table>
            <%
                if (arlTicketHistory != null && !arlTicketHistory.isEmpty())
                {
            %>

            <table width="18%" cellpadding=1 cellspacing=2 border=0>
                <tr class="LightBandStyle">
                    <td width="80%" class="txtbluebold">Total No.Of Days Spent :</td>
                    <td width="20%" class="txtblue"><%= strTotalDaysSpent %>
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="15%" class="txtblackbold12">Start Date</td>
                    <td width="15%" class="txtblackbold12">End Date</td>
                    <td width="15%" class="txtblackbold12">Assignee</td>
                    <td width="45%" class="txtblackbold12">Comments</td>
                    <td width="5%" class="txtblackbold12">Hours</td>
                    <td width="5%" class="txtblackbold12">Minutes</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    for (int iCount = 0; iCount < arlTicketHistory.size(); iCount += 6)
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
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount + 1) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount + 2) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount + 3) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount + 4) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketHistory.get(iCount + 5) %></pre>
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
            <%@ include file="../../common/nodataavailable.jsp" %>
            <%
                }
            %>
        </td>
    </tr>
</table>
<%@ include file="../../common/onespace.jsp" %>

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
                if (arlCommentsHistory != null && !arlCommentsHistory.isEmpty())
                {
            %>

            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="15%" class="txtblackbold12">Comments Category</td>
                    <td width="55%" class="txtblackbold12">Comments</td>
                    <td width="15%" class="txtblackbold12">Comments Date</td>
                    <td width="15%" class="txtblackbold12">Comments By</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";
                    String strComments;

                    for (int iCount = 0; iCount < arlCommentsHistory.size(); iCount += 4)
                    {
                        if (intCounter % 2 == 0)
                        {
                            strBackGround = "LightGreyBandStyle";
                        }
                        else
                        {
                            strBackGround = "DarkGreyBandStyle";
                        }

                        strComments = arlCommentsHistory.get(iCount + 1);
                        strComments.replace("\n", "<br>");
                %>
                <tr class="<%= strBackGround %>">
                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 1) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 2) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 3) %></pre>
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
            <%@ include file="../../common/nodataavailable.jsp" %>
            <%
                }
            %>
        </td>
    </tr>
</table>
<%@ include file="../../common/onespace.jsp" %>

<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Ticket Activity History
                    </td>
                </tr>
            </table>
            <%
                if (arlTicketActivityHistory != null && !arlTicketActivityHistory.isEmpty())
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0 >
                <tr class="LightBlueBandStyle">
                    <td width="10%" class="txtblackbold12">Activity Type</td>
                    <td width="55%" class="txtblackbold12">Comments</td>
                    <td width="15%" class="txtblackbold12">Activity Date</td>
                    <td width="20%" class="txtblackbold12">Activity By</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    for (int iCount = 0; iCount < arlTicketActivityHistory.size(); iCount += 4)
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
                    <td><pre class="txtgrey"><%= arlTicketActivityHistory.get(iCount) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketActivityHistory.get(iCount + 1) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketActivityHistory.get(iCount + 2) %></pre>
                    </td>
                    <td><pre class="txtgrey"><%= arlTicketActivityHistory.get(iCount + 3) %></pre>
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
            <%@ include file="../../common/nodataavailable.jsp" %>
            <%
                }
            %>
        </td>
    </tr>
</table>
<%@ include file="../../common/onespace.jsp" %>
