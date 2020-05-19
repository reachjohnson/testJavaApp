<%
    if (blnTasksAvailable)
    {
%>
<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <%
                if (blnTasksNotStartedAvailable)
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks - Not Started
                        (<%= intTaskNotStarted%>)
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="12%" class="txtblackbold12">Task ID</td>
                    <td width="5%" class="txtblackbold12">&nbsp;</td>
                    <td width="32%" class="txtblackbold12">Task Description</td>
                    <td width="6%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Priority</td>
                    <td width="10%" class="txtblackbold12">Received Date</td>
                    <td width="10%" class="txtblackbold12">SLA End Date</td>
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
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.NOT_STARTED))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                            strTicketETA = arlTicketDetails.get(iCount + 12);

                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('TaskNotStarted', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td align="center"><a
                            href="JavaScript:ImageSubmitAction('startProgressTicket', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                        <img src="images/start_new.png" border="0" title="Start Progress"></a>&nbsp;
                        <a href="JavaScript:ImageSubmitAction('AddComments', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                            <img src="images/comments_new.jpg" border="0" title="Add Comments"></a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11).toUpperCase() %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= strTicketETA %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
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
                                    <select name="TaskNotStartedAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <option value="loadUpdateTask"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <!--<option value="deleteTicket"><%//=AppConstants.ACTION_DELETE_TICKET%>
                    </option>-->
                                        <option value="loadReAssignTicket"><%= AppConstants.ACTION_REASSIGN%>
                                        </option>
                                        <option value="startProgressTicket"><%= AppConstants.ACTION_START_PROGRESS%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitTaskNotStarted()"
                                           id="SendButton1"/>
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

            <%
                if (blnTasksInProgressAvailable)
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks - In Progress
                        (<%= intTaskInProgress%>)
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="12%" class="txtblackbold12">Task ID</td>
                    <td width="5%" class="txtblackbold12">&nbsp;</td>
                    <td width="32%" class="txtblackbold12">Task Description</td>
                    <td width="6%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Priority</td>
                    <td width="10%" class="txtblackbold12">Received Date</td>
                    <td width="10%" class="txtblackbold12">SLA End Date</td>
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
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.IN_PROGRESS))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                            strTicketETA = arlTicketDetails.get(iCount + 12);
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('TaskInProgress','<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td align="center"><a
                            href="JavaScript:ImageSubmitAction('loadStopProgressTicket', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                        <img src="images/stop_new.png" border="0" title="Stop Progress"></a>&nbsp;
                        <a href="JavaScript:ImageSubmitAction('AddComments', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                            <img src="images/comments_new.jpg" border="0" title="Add Comments"></a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11).toUpperCase() %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= strTicketETA %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
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
                                    <select name="TaskInProgressAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <option value="loadUpdateTask"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <!--<option value="deleteTicket"><%//= AppConstants.ACTION_DELETE_TICKET%>
                    </option>-->
                                        <option value="loadStopProgressTicket"><%= AppConstants.ACTION_STOP_PROGRESS%>
                                        </option>
                                        <option value="loadCloseTask"><%= AppConstants.ACTION_CLOSE_TICKET%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitTaskInProgress()"
                                           id="SendButton2"/>
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

            <%
                if (blnTasksOnHoldAvailable)
                {
            %>
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Tasks - On Hold (<%= intTaskOnHold%>)
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="5%" class="txtblackbold12">Action</td>
                    <td width="12%" class="txtblackbold12">Task ID</td>
                    <td width="5%" class="txtblackbold12">&nbsp;</td>
                    <td width="32%" class="txtblackbold12">Task Description</td>
                    <td width="6%" class="txtblackbold12">Team</td>
                    <td width="7%" class="txtblackbold12">Task Priority</td>
                    <td width="10%" class="txtblackbold12">Received Date</td>
                    <td width="10%" class="txtblackbold12">SLA End Date</td>
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
                        if (arlTicketDetails.get(iCount + 3).equalsIgnoreCase(AppConstants.TASK) && arlTicketDetails.get(iCount + 6).equalsIgnoreCase(AppConstants.ON_HOLD))
                        {
                            if (arlTicketDetails.get(iCount + 10).equalsIgnoreCase("1"))
                            {
                                strSLAMissedStyle = OOSLA_RED_TEXT;
                            }
                            else if (Integer.parseInt(strNearingSLATicketsCount) > 0 && arlNearingSLATickets.contains(arlTicketDetails.get(iCount + 1)))
                            {
                                strSLAMissedStyle = OOSLA_ORANGE_TEXT;
                            }
                            strTicketETA = arlTicketDetails.get(iCount + 12);
                %>
                <tr class="<%= strBackGround %>">
                    <td class="txtgrey"><input type="radio" name="ActionRefNoTicketId" value="Y"
                                               onclick="JavaScript:setActionRefNoTicketId('TaskOnHold','<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td align="center"><a
                            href="JavaScript:ImageSubmitAction('startProgressTicket', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                        <img src="images/start_new.png" border="0" title="Start Progress"></a>&nbsp;
                        <a href="JavaScript:ImageSubmitAction('AddComments', '<%= arlTicketDetails.get(iCount) %>', '<%= arlTicketDetails.get(iCount +1) %>')">
                            <img src="images/comments_new.jpg" border="0" title="Add Comments"></a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlTicketDetails.get(iCount + 1) %>', '<%= arlTicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlTicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 11).toUpperCase() %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlTicketDetails.get(iCount + 7) %>
                    </td>
                    <td class="<%= strSLAMissedStyle %>"><%= arlTicketDetails.get(iCount + 8) %>
                    </td>
                    <td class="txtgrey"><%= strTicketETA %>
                    </td>
                    <td align="center" class="txtgrey"><%= arlTicketDetails.get(iCount + 13) %>
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
                                    <select name="TaskOnHoldAction" class="txtgreybold">
                                        <option value="">Select</option>
                                        <option value="loadTicketDetails"><%= AppConstants.ACTION_TICKET_DETAILS%>
                                        </option>
                                        <option value="loadUpdateTask"><%= AppConstants.ACTION_UPDATE%>
                                        </option>
                                        <!--<option value="deleteTicket"><%//= AppConstants.ACTION_DELETE_TICKET%>
                    </option>-->
                                        <option value="loadReAssignTicket"><%= AppConstants.ACTION_REASSIGN%>
                                        </option>
                                        <option value="startProgressTicket"><%= AppConstants.ACTION_START_PROGRESS%>
                                        </option>
                                        <option value="AddComments"><%= AppConstants.ACTION_ADD_COMMENTS %>
                                        </option>
                                    </select>
                                </td>
                                <td width="10%">
                                    <input type="button" class="myButton" value="Submit"
                                           onclick="JavaScript:submitTaskOnHold()"
                                           id="SendButton3"/>
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