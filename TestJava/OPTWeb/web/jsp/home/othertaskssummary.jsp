<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <%
                        if (intOtherTasks > 0)
                        {
                    %>
                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;List of Other Tasks (<%= intOtherTasks %>
                        )
                    </td>
                    <%
                    }
                    else
                    {
                    %>
                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;List of Other Tasks</td>
                    <%
                        }
                    %>
                </tr>
            </table>
            <%
                if (intOtherTasks > 0)
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="3%" class="txtblackbold12">S.No</td>
                    <td width="10%" class="txtblackbold12">Ticket ID</td>
                    <td width="45%" class="txtblackbold12">Ticket Description</td>
                    <td width="8%" class="txtblackbold12">Ticket Priority</td>
                    <td width="14%" class="txtblackbold12">Assignee</td>
                    <td width="7%" class="txtblackbold12">Status</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                    <td width="5%" class="txtblackbold12" align="center">Ageing</td>
                </tr>
                <%
                    int intCounter = 0;
                    String strBackGround = "";
                    String strTicketETATBDTAG = "<span class=\"blink_me\" style=\"font-weight: bold;color: #FF0000;\">ETA - TBD</span>";

                    int intSerialNo = 0;
                    for (int iCount = 0; iCount < arlOtherTasksList.size(); iCount += 8)
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
                    <td class="txtgrey" align="right"><%= ++intSerialNo %>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlOtherTasksList.get(iCount + 1) %>', '<%= arlOtherTasksList.get(iCount + 1) %>')"
                            class="link"><%= arlOtherTasksList.get(iCount + 1) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlOtherTasksList.get(iCount + 1) %>', '<%= arlOtherTasksList.get(iCount + 1) %>')"
                            class="link"><%= arlOtherTasksList.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:viewTicketDetails('<%= arlOtherTasksList.get(iCount) %>','<%= arlOtherTasksList.get(iCount + 1) %>')"
                            title="Ticket Details"><%= arlOtherTasksList.get(iCount + 3) %>
                    </a>
                    </td>
                    <td class="txtgrey"><%= arlOtherTasksList.get(iCount + 4) %>
                    </td>
                    <td class="txtgrey"><%= arlOtherTasksList.get(iCount + 7) %>
                    </td>
                    <td class="txtgrey"><%= arlOtherTasksList.get(iCount + 5) %>
                    </td>
                    <td class="txtgrey" align="center"><%= arlOtherTasksList.get(iCount + 6) %>
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
