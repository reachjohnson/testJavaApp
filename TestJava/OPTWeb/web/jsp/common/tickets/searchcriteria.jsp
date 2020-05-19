<%@ page import="com.opt.util.AppUtil" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="../../common/errorpage.jsp" %>
<%@ include file="../../common/noCache.jsp" %>
<%
    ArrayList<String> arlSearchedLiveTickets = (ArrayList) request.getAttribute("SearchedLiveTickets");
    String strTicketsSearched = AppUtil.checkNull((String) request.getAttribute("TicketsSearched"));
    String strTicketDescription = AppUtil.checkNull((String) request.getAttribute("TicketDescription"));
    String strTicketRootCause = AppUtil.checkNull((String) request.getAttribute("TicketRootCause"));
    String strTicketComments = AppUtil.checkNull((String) request.getAttribute("TicketComments"));
    String strTicketResolution = AppUtil.checkNull((String) request.getAttribute("TicketResolution"));

    List<String> lstTaskResolutionTypes = Arrays.asList(AppConstants.TASK_RESOLUTION);
    List<String> lstBugResolutionTypes = Arrays.asList(AppConstants.BUG_RESOLUTION);
    Set<String> setResolutionTypes = new HashSet<String>();
    ArrayList<String> arlResolutionTypes = new ArrayList<String>();
    setResolutionTypes.addAll(lstTaskResolutionTypes);
    setResolutionTypes.addAll(lstBugResolutionTypes);
    arlResolutionTypes.addAll(setResolutionTypes);
    Collections.sort(arlResolutionTypes);
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function searchLiveTickets() {
            if (isEmpty(document.OPTForm.TicketDescription.value) &&
                    isEmpty(document.OPTForm.TicketRootCause.value) &&
                    isEmpty(document.OPTForm.TicketComments.value) && document.OPTForm.TicketResolution.selectedIndex == 0)
            {
                OPTDialog("Enter atleast one search criteria", document.OPTForm.TicketDescription);
                return;
            }
            document.OPTForm.action = "searchLiveTickets";
            frmReadSubmit();
        }

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }


    </script>
</head>
<body onload="document.OPTForm.TicketDescription.focus();">
<form name="OPTForm" method="post">
    <%@ include file="../../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Live Tickets - Advanced Search
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=2 cellspacing=2 border=0 align="center">

                    <tr class="LightBandStyle">
                        <td width="20%" class="txtgreybold">&nbsp;Ticket Description</td>
                        <td width="50%"><textarea name="TicketDescription" rows="6" cols="120" maxlength="490"
                                                  class="txtgrey"><%= strTicketDescription %></textarea>
                        </td>
                        <td width="30%" bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>

                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Ticket Root Cause</td>
                        <td><input type="text" name="TicketRootCause" size="100" maxlength="300" class="txtgrey" value="<%= strTicketRootCause %>">
                        </td>
                        <td bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>

                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Closing Comments</td>
                        <td><textarea name="TicketComments" rows="4" cols="120" maxlength="4990" class="txtgrey"><%= strTicketComments %></textarea>
                        </td>
                        <td bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>
                    <tr class="LightBandStyle">
                        <td class="txtgreybold">&nbsp;Ticket Resolution</td>
                        <td>
                            <select name="TicketResolution" class="txtgrey"
                                    onchange="JavaScript:TaskResolutionOnChange()">
                                <option value="">Select</option>
                                <%
                                    String strSelected = "";
                                    for (String strValue : arlResolutionTypes)
                                    {
                                        strSelected = "";
                                        if(strValue.equalsIgnoreCase(strTicketResolution))
                                        {
                                            strSelected = "selected";
                                        }
                                %>
                                <option value="<%= strValue %>" <%= strSelected %>><%= strValue %>
                                </option>
                                <%

                                    }
                                %>
                            </select>

                        </td>
                        <td bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="100%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Search" onclick="JavaScript:searchLiveTickets()"
                       id="buttonId0"/>
            </td>
        </tr>
    </table>
    <%@ include file="../../common/spacebefore.jsp" %>

    <%
        if (strTicketsSearched.equalsIgnoreCase("YES"))
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <%
                        String strTicketsCount = "";
                        if (arlSearchedLiveTickets != null && arlSearchedLiveTickets.size() > 0)
                        {
                            strTicketsCount = "(" + arlSearchedLiveTickets.size()/6 + ")";
                        }
                    %>
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Tickets <%= strTicketsCount %></td>
                    </tr>
                </table>
                <%
                    if (arlSearchedLiveTickets != null && arlSearchedLiveTickets.size() > 0)
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="45%" class="txtblackbold12">Ticket Description</td>
                        <td width="10%" class="txtblackbold12">Ticket Priority</td>
                        <td width="20%" class="txtblackbold12">Ticket Resolution</td>
                        <td width="12%" class="txtblackbold12">Ticket Inflow Type</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlSearchedLiveTickets.size(); iCount += 6)
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
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSearchedLiveTickets.get(iCount + 1) %>', '<%= arlSearchedLiveTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSearchedLiveTickets.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlSearchedLiveTickets.get(iCount + 1) %>', '<%= arlSearchedLiveTickets.get(iCount + 1) %>')"
                                class="link"><%= arlSearchedLiveTickets.get(iCount + 2) %>
                        </a>
                        </td>

                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlSearchedLiveTickets.get(iCount) %>','<%= arlSearchedLiveTickets.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlSearchedLiveTickets.get(iCount + 3) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlSearchedLiveTickets.get(iCount + 4) %>
                        </td>
                        <td class="txtgrey"><%= arlSearchedLiveTickets.get(iCount + 5) %>
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
                <table width="100%">
                    <tr>
                        <td class="txtgrey12">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="left" class="txtgrey12">&nbsp;Refine Your Search Criteria</td>
                    </tr>
                    <tr>
                        <td class="txtgrey12">&nbsp;</td>
                    </tr>
                </table>

                <%
                    }
                %>
            </td>
        </tr>
    </table>
    <%
        }
    %>

</form>
</body>
</html>
