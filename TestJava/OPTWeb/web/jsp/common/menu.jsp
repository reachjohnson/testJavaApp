<link rel="stylesheet" type="text/css" href="/styles/menu/menu.css?ts=<%= String.valueOf(new Date().getTime()) %>">
<%
    if (!objSessionInfo.getUserInfo().isBlnAccessRestricted())
    {
%>
<nav id="menu-wrap">
    <ul id="menu">
        <li><a href="JavaScript:menuSubmitForm('homePage')">Home</a>
        <li>
            <a href="#">Tickets</a>
            <ul>
                <li>
                    <a href="#">Live Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCreateTask())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCreateTask')">Create Task</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:noAccessMessage()">Create Task</a></li>
                        <%
                            }
                        %>


                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCreateBug())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCreateBug')">Create Bug</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:noAccessMessage()">Create Bug</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isReopenClosedTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadReopenTicket')">Reopen Ticket</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Reopen Ticket</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isUpdateClosedTicket())
                            {
                        %>
                        <li><a href="JavaScript:JavaScript:menuSubmitForm('loadUpdateClosedTicket')">Update Closed
                            Ticket</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Update Closed Ticket</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isModifyCurrentTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadModifyCurrentTickets')">Admin Changes</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Admin Changes</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <li>
                    <a href="#">RQA Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCreateRQABug())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCreateRQABug')">Create Bug</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Create Bug</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isReopenRQABug())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadReopenRQATicket')">Reopen Bug</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Reopen Bug</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isModifyRQATickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadModifyRQATickets')">Admin Changes</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Admin Changes</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <li>
                    <a href="#">Internal Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCreateInternalTicket())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCreateInternalTask')">Create Ticket</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Create Ticket</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isITAdminChanges())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadITAdminChanges')">Admin Changes</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Admin Changes</a></li>
                        <%
                            }
                        %>

                    </ul>
                </li>
            </ul>
        </li>
        <li>
            <a href="#">Calendar</a>
            <ul>
                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isUpdateLeavePlans())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('loadLeavePlans')">Update Leave</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Update Leave</a></li>
                <%
                    }
                %>

                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isHolidayCalendar())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('loadHolidayCalendar')">Holiday Calendar</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Holiday Calendar</a></li>
                <%
                    }
                %>
            </ul>
        </li>
        <li>
            <a href="#">Maintenance</a>
            <ul>
                <li>
                    <a href="#">User</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isAddUser())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadAddUser')">Add User</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Add User</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isModifyUser())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadModifyUser')">Modify User</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Modify User</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <li>
                    <a href="#">Settings</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isAccessRights())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadAccessRights')">Access Rights/Email</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Access Rights</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isRQASettings())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadRQASettings')">RQA Settings</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">RQA Settings</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCommonSettings())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCommonSettings')">Common Settings</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Common Settings</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isResourceMatrix())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadResourceMatrix')">Resource Matrix</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Resource Matrix</a></li>
                        <%
                            }
                        %>

                    </ul>
                </li>
            </ul>
        </li>

        <li>
            <a href="#">Reports</a>
            <ul>
                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isResourceAllocation())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('ResourceAllocation')">Resource Allocation</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Resource Allocation</a></li>
                <%
                    }
                %>
                <li>
                    <a href="#">Live Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isTicketsStatus())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadTicketStatus')">Tickets Status</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Tickets Status</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCurrentStatus())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('TicketCurrentStatus')">Current Status</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Current Status</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCurrentTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('TicketList')">Open Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Open Tickets</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isClosedTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('closedTicketList')">Closed Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Closed Tickets</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isReopenedTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadReopenedTickets')">Reopened Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Reopened Tickets</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isSLAMissedTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('SLAMissedTickets')">OOSLA Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">OOSLA Tickets</a></li>
                        <%
                            }
                        %>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isSLAReports())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadSLAReports')">SLA Reports</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">SLA Reports</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isSLAMetrics())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadSLAMetrics')">SLA Metrics</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">SLA Metrics</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCodeFixReleases())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadCodeFixReleases')">Code Fix Releases</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Code Fix Releases</a></li>
                        <%
                            }
                        %>

                    </ul>
                </li>
                <li>
                    <a href="#">RQA Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isRQADailyReport())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadRQADailyReport')">Daily Report</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Daily Report</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isRQACurrentStatus())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadRQACurrentStatus')">Current Status</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Current Status</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isRQAMetrics())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('loadRQAMetrics')">Metrics</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Metrics</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <li>
                    <a href="#">Internal Tickets</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isCurrentInternalTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('CurrentInternalTickets')">Open Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Open Tickets</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isClosedInternalTickets())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('ClosedInternalTickets')">Closed Tickets</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">Closed Tickets</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>
                <li>
                    <a href="#">Leave and Holiday</a>
                    <ul>
                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isViewLeavePlans())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('viewLeavePlans')">View Leave Plans</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">View Leave Plans</a></li>
                        <%
                            }
                        %>

                        <%
                            if (blnAdmin || objSessionInfo.getUserAccessRights().isListOfHolidays())
                            {
                        %>
                        <li><a href="JavaScript:menuSubmitForm('ListOfHolidays')">List Of Holidays</a></li>
                        <%
                        }
                        else
                        {
                        %>
                        <li><a href="JavaScript:JavaScript:noAccessMessage()">List Of Holidays</a></li>
                        <%
                            }
                        %>
                    </ul>
                </li>

            </ul>
        </li>

        <li>
            <a href="#">Common</a>
            <ul>
                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isTeamContacts())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('loadTeamContacts')">Team Contacts</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Team Contacts</a></li>
                <%
                    }
                %>

                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isDomainPOC())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('loadDomainContacts')">Domain POC</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Domain POC</a></li>
                <%
                    }
                %>

                <%
                    if (blnAdmin || objSessionInfo.getUserAccessRights().isTeamFeedback())
                    {
                %>
                <li><a href="JavaScript:menuSubmitForm('loadTeamFeedback')">Team Feedback</a></li>
                <%
                }
                else
                {
                %>
                <li><a href="JavaScript:JavaScript:noAccessMessage()">Team Feedback</a></li>
                <%
                    }
                %>
            </ul>
        </li>
        <li style="border-right: 0px !important; box-shadow: none"><a
                href="JavaScript:menuSubmitForm('Login')">Logout</a></li>
    </ul>
</nav>
<%
}
else
{
%>
<table width="10%" cellpadding=0 cellspacing=0 border=0 align="center">
    <tr>
        <td width="50%" align="center">
            <input type="button" class="myButton" value="Home" onclick="JavaScript:menuSubmitForm('homePage')"
                   id="buttonIdHome"/>
        </td>
        <td width="50%" align="center">
            <input type="button" class="myButton" value="Logout" onclick="JavaScript:menuSubmitForm('Login');"
                   id="buttonIdLogout"/>
        </td>
    </tr>
</table>

<%
    }
%>