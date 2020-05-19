<%
    if (intOpenRQABugs > 0)
    {
%>
<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <%
                if (arlRQATicketDetails != null && !arlRQATicketDetails.isEmpty())
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bugs (<%= intOpenRQABugs%>)</td>
                </tr>
            </table>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="12%" class="txtblackbold12">Bug ID</td>
                    <td width="5%" class="txtblackbold12">&nbsp;</td>
                    <td width="52%" class="txtblackbold12">Bug Description</td>
                    <td width="10%" class="txtblackbold12">Bug Priority</td>
                    <td width="8%" class="txtblackbold12">Received Date</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    String strETA = "";

                    for (int iCount = 0; iCount < arlRQATicketDetails.size(); iCount += 10)
                    {
                        if (intCounter % 2 == 0)
                        {
                            strBackGround = "LightGreyBandStyle";
                        }
                        else
                        {
                            strBackGround = "DarkGreyBandStyle";
                        }
                        if (arlRQATicketDetails.get(iCount + 8).trim().length() > 0)
                        {
                            strETA = arlRQATicketDetails.get(iCount + 8);
                        }
                        else
                        {
                            strETA = "Yet To Start";
                        }
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setRQAActionRefNoTicketId('RQATickets','<%= arlRQATicketDetails.get(iCount) %>', '<%= arlRQATicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlRQATicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td align="center"><a
                            href="JavaScript:RQAImageSubmitAction('loadCloseRQABug', '<%= arlRQATicketDetails.get(iCount) %>', '<%= arlRQATicketDetails.get(iCount +1) %>')">
                        <img src="images/closeicon_new.png" border="0" title="Close RQA Bug"></a>&nbsp;
                        <a href="JavaScript:RQAImageSubmitAction('RQAAddComments', '<%= arlRQATicketDetails.get(iCount) %>', '<%= arlRQATicketDetails.get(iCount +1) %>')">
                            <img src="images/comments_new.jpg" border="0" title="Add Comments"></a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlRQATicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 6) %>
                    </td>
                    <td class="txtgrey"><%= strETA %>
                    </td>
                </tr>
                <%
                        intCounter++;
                    }
                %>
                <tr>
                    <td colspan="7">
                        <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                            <tr>
                                <td width="5%" class="txtblackbold12 LightBlueBandStyle">Action</td>
                                <td width="12%" align="left" class="txtgreybold LightBlueBandStyle">
                                    <select name="RQATicketsAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadRQABugDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <option value="loadRQAUpdateBug"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <option value="loadCloseRQABug"><%= AppConstants.ACTION_CLOSE_TICKET%>
                                        </option>
                                        <option value="RQAAddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                        <option value="loadRQAReAssignBug"><%= AppConstants.ACTION_REASSIGN%>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitRQATickets()" id="SendButton7"/>
                                </td>
                                <td width="73%">&nbsp
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
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