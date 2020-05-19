<table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Ticket Details - Triaging Team Analysis,
                        Progress and Activity
                        History
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Category</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(1) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Id</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(2) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td width="100%" colspan="2" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="18%" class="txtbluebold">Ticket Description</td>
                                <td width="82%" class="txtblue"><%= arlTriagingTicketDetails.get(3) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Priority</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(4) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Type</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(5) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Module</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(6) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Raised By</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(7) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    if (arlTriagingTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
                    {
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Resolution</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(11) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Moved Team</td>
                                <td width="64%" class="txtblue"><%= arlTriagingTicketDetails.get(12) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td width="100%" class="txtgrey" colspan="2">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="18%" class="txtbluebold">Ticket Root Cause</td>
                                <td width="82%" class="txtblue"><%= arlTriagingTicketDetails.get(13) %>
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
                                <td width="82%"><pre class="txtblue"><%= arlTriagingTicketDetails.get(14) %></pre>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    }
                %>

                <%
                    strClass = "txtblue";
                    if (strTriagingSLAMissed.equalsIgnoreCase("1"))
                    {
                        strClass = OOSLA_DETAILS_RED_TEXT;
                    }
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Received Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlTriagingTicketDetails.get(8) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Created Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlTriagingTicketDetails.get(18) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">SLA End Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlTriagingTicketDetails.get(9) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">ETA</td>
                                <td width="64%" class="<%= strClass %>"><%= arlTriagingTicketDetails.get(19) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    if (arlTriagingTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
                    {
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Actual End Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlTriagingTicketDetails.get(15) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">&nbsp;</td>
                    <%
                        }
                    %>
                </tr>

                <tr>
                    <td width="100%" class="txtgrey" colspan="2">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="18%" class="txtbluebold">Assignee</td>
                                <td width="82%" class="txtblue"><%= arlTriagingTicketDetails.get(17) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
