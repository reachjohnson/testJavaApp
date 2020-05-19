<table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" align="left">
                <tr>
                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Ticket Details
                    </td>
                </tr>
            </table>

            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <%
                        String strClass1 = "txtgreenbold15";
                        String strClass2 = "txtgreen15";
                        if (strSLAMissed.equalsIgnoreCase("1"))
                        {
                            strClass1 = "txtredbold15";
                            strClass2 = "txtred15";
                        }
                    %>
                    <td width="100%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="7%" class="<%= strClass1 %>">Status</td>
                                <td width="9%" class="<%= strClass2 %>"><%= arlViewTicketDetails.get(16) %>
                                </td>
                                <td width="84%" bgcolor="#FFFFFF" align="left">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Category</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(1) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Id</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(2) %>
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
                                <td width="82%" class="txtblue"><%= arlViewTicketDetails.get(3) %>
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
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(4) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Type</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(5) %>
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
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(6) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Raised By</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(7) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    if (arlViewTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
                    {
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ticket Resolution</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(11) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Moved Team</td>
                                <td width="64%" class="txtblue"><%= arlViewTicketDetails.get(12) %>
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
                                <td width="82%" class="txtblue"><%= arlViewTicketDetails.get(13) %>
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
                                <td width="82%"><pre class="txtblue"><%= arlViewTicketDetails.get(14) %></pre>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    }
                %>

                <%
                    String strClass = "txtblue";
                    if (strSLAMissed.equalsIgnoreCase("1"))
                    {
                        strClass = OOSLA_DETAILS_RED_TEXT;
                    }
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Received Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(8) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Created Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(18) %>
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
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(9) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">ETA</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(19) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%
                    if (arlViewTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
                    {
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Actual End Date</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(15) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Release Version</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(21) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <%
                }
                else
                {
                %>
                <tr>
                    <td width="50%" class="txtgrey">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="36%" class="txtbluebold">Ageing</td>
                                <td width="64%" class="<%= strClass %>"><%= arlViewTicketDetails.get(20) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="50%" class="txtgrey">&nbsp;</td>
                </tr>
                <%
                    }
                %>

                <tr>
                    <td width="100%" class="txtgrey" colspan="2">
                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                            <tr class="LightBandStyle">
                                <td width="18%" class="txtbluebold">Assignee</td>
                                <td width="82%" class="txtblue"><%= arlViewTicketDetails.get(17) %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
