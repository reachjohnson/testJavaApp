<table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
    <tr>
        <td width="100%" valign="top">
            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                <tr>
                    <td class="DarkBlueBandStyleText14" align="left">&nbsp;List of RQA Bugs (<%= intOpenRQABugs %>
                        )
                    </td>
                </tr>
            </table>
            <%
                if (arlRQATicketDetails != null && !arlRQATicketDetails.isEmpty())
                {
            %>
            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                <tr class="LightBlueBandStyle">
                    <td width="3%" class="txtblackbold12">S.No</td>
                    <td width="16%" class="txtblackbold12">Assignee</td>
                    <td width="12%" class="txtblackbold12">Bug ID</td>
                    <td width="43%" class="txtblackbold12">Bug Description</td>
                    <td width="10%" class="txtblackbold12">Bug Priority</td>
                    <td width="8%" class="txtblackbold12">Received Date</td>
                    <td width="8%" class="txtblackbold12">ETA</td>
                </tr>
                <%
                    int intCounter = 0;
                    int intSerialNo = 0;
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
                    <td class="txtgrey" align="right"><%= ++intSerialNo %>
                    </td>
                    <td class="txtgrey"><%= arlRQATicketDetails.get(iCount + 9) %>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlRQATicketDetails.get(iCount + 1) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlRQATicketDetails.get(iCount + 1) %>', '<%= arlRQATicketDetails.get(iCount + 1) %>')"
                            class="link"><%= arlRQATicketDetails.get(iCount + 2) %>
                    </a>
                    </td>
                    <td class="txtgrey"><a
                            href="JavaScript:viewRQATicketDetails('<%= arlRQATicketDetails.get(iCount) %>','<%= arlRQATicketDetails.get(iCount + 1) %>')"
                            title="Ticket Details"><%= arlRQATicketDetails.get(iCount + 4) %>
                    </a>
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
