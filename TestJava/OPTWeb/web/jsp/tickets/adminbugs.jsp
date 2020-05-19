<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">

            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs - Not Started
                        (<%= intBugNotStarted%>)
                    </td>
                </tr>
            </table>
            <%
                if (blnBugsNotStartedAvailable)
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="15" class="txtblackbold12">Assignee</td>
                    <td width="10%" class="txtblackbold12">Bug ID</td>
                    <td width="27%" class="txtblackbold12">Bug Description</td>
                    <td width="7%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Bug Priority</td>
                    <td width="8%" class="txtblackbold12">Received Date</td>
                    <td width="8%" class="txtblackbold12">SLA End Date</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                    <td width="5%" align="center" class="txtblackbold12">Ageing</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 15)
                    {
                        if (intCounter % 2 == 0)
                        {
                            strBackGround = "LightGreyBandStyle";
                        }
                        else
                        {
                            strBackGround = "DarkGreyBandStyle";
                        }

                        strSLAMissedStyle = OOSLA_GREY_TEXT;
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.NOT_STARTED))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('BugNotStarted','<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11) %>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 12) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 14) %>
                    </td>
                </tr>
                <%
                            intCounter++;
                        }
                    }
                %>
                <tr>
                    <td colspan="10">
                        <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                            <tr>
                                <td width="5%" class="txtblackbold12 LightBlueBandStyle">Action</td>
                                <td width="12%" align="left" class="txtgreybold LightBlueBandStyle">
                                    <select name="BugNotStartedAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <!--<option value="loadTaskDetails"><%//= AppConstants.ACTION_TASK_DETAILS%>
                </option>-->
                                        <option value="loadUpdateBug"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <option value="deleteTicket"><%= AppConstants.ACTION_DELETE_TICKET%>
                                        </option>
                                        <option value="loadReAssignTicket"><%= AppConstants.ACTION_REASSIGN%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitBugNotStarted()"
                                           id="SendButton4"/>
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
            else
            {
            %>
            <%@ include file="../common/nodataavailable.jsp" %>
            <%
                }
            %>

            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs - In Progress
                        (<%= intBugInProgress%>)
                    </td>
                </tr>
            </table>
            <%
                if (blnBugsInProgressAvailable)
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="15" class="txtblackbold12">Assignee</td>
                    <td width="10%" class="txtblackbold12">Bug ID</td>
                    <td width="27%" class="txtblackbold12">Bug Description</td>
                    <td width="7%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Bug Priority</td>
                    <td width="8%" class="txtblackbold12">Received Date</td>
                    <td width="8%" class="txtblackbold12">SLA End Date</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                    <td width="5%" align="center" class="txtblackbold12">Ageing</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 15)
                    {
                        if (intCounter % 2 == 0)
                        {
                            strBackGround = "LightGreyBandStyle";
                        }
                        else
                        {
                            strBackGround = "DarkGreyBandStyle";
                        }

                        strSLAMissedStyle = OOSLA_GREY_TEXT;
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('BugInProgress','<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11) %>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 12) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 14) %>
                    </td>
                </tr>
                <%
                            intCounter++;
                        }
                    }
                %>
                <tr>
                    <td colspan="10">
                        <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                            <tr>
                                <td width="5%" class="txtblackbold12 LightBlueBandStyle">Action</td>
                                <td width="12%" align="left" class="txtgreybold LightBlueBandStyle">
                                    <select name="BugInProgressAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <!--<option value="loadTaskDetails"><%//= AppConstants.ACTION_TASK_DETAILS%>
                </option>-->
                                        <option value="loadUpdateBug"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <option value="deleteTicket"><%= AppConstants.ACTION_DELETE_TICKET%>
                                        </option>
                                        <option value="loadStopProgressTicket"><%= AppConstants.ACTION_STOP_PROGRESS%>
                                        </option>
                                        <option value="loadCloseBug"><%= AppConstants.ACTION_CLOSE_TICKET%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitBugInProgress()"
                                           id="SendButton5"/>
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
            else
            {
            %>
            <%@ include file="../common/nodataavailable.jsp" %>
            <%
                }
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Bugs - On Hold (<%= intBugOnHold%>)</td>
                </tr>
            </table>
            <%
                if (blnBugsOnHoldAvailable)
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="15" class="txtblackbold12">Assignee</td>
                    <td width="10%" class="txtblackbold12">Bug ID</td>
                    <td width="27%" class="txtblackbold12">Bug Description</td>
                    <td width="7%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Bug Priority</td>
                    <td width="8%" class="txtblackbold12">Received Date</td>
                    <td width="8%" class="txtblackbold12">SLA End Date</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                    <td width="5%" align="center" class="txtblackbold12">Ageing</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";

                    for (int iCount = 0; iCount < arlTicketDetails.size(); iCount += 15)
                    {
                        if (intCounter % 2 == 0)
                        {
                            strBackGround = "LightGreyBandStyle";
                        }
                        else
                        {
                            strBackGround = "DarkGreyBandStyle";
                        }

                        strSLAMissedStyle = OOSLA_GREY_TEXT;
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.ON_HOLD))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('BugOnHold','<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11) %>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 12) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 14) %>
                    </td>
                </tr>
                <%
                            intCounter++;
                        }
                    }
                %>
                <tr>
                    <td colspan="10">
                        <table width="100%" cellpadding=0 cellspacing=0 border=0 align="left">
                            <tr>
                                <td width="5%" class="txtblackbold12 LightBlueBandStyle">Action</td>
                                <td width="12%" align="left" class="txtgreybold LightBlueBandStyle">
                                    <select name="BugOnHoldAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <!--<option value="loadTaskDetails"><%//= AppConstants.ACTION_TASK_DETAILS%>
                </option>-->
                                        <option value="loadUpdateBug"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <option value="deleteTicket"><%= AppConstants.ACTION_DELETE_TICKET%>
                                        </option>
                                        <option value="loadReAssignTicket"><%= AppConstants.ACTION_REASSIGN%>
                                        </option>
                                        <option value="loadCloseBug"><%= AppConstants.ACTION_CLOSE_TICKET%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitBugOnHold()"
                                           id="SendButton6"/>
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
