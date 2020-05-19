<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList arlViewLeavePlans = (ArrayList) request.getAttribute("VIEWLEAVEPLANS");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
</head>
<body>
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Leave Plans
                                    </td>
                                </tr>
                            </table>
                            <%
                                if (arlViewLeavePlans != null && !arlViewLeavePlans.isEmpty())
                                {
                            %>

                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="25%" class="txtblackbold12">Name</td>
                                    <td width="15%" class="txtblackbold12">From Date</td>
                                    <td width="15%" class="txtblackbold12">To Date</td>
                                    <td width="50%" class="txtblackbold12">Reason for Leave</td>
                                </tr>
                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int iCount = 0; iCount < arlViewLeavePlans.size(); iCount += 4)
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
                                    <td class="txtgrey"><%= arlViewLeavePlans.get(iCount) %>
                                    </td>
                                    <td class="txtgrey"><%= arlViewLeavePlans.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlViewLeavePlans.get(iCount + 2) %>
                                    </td>
                                    <td class="txtgrey"><%= arlViewLeavePlans.get(iCount + 3) %>
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
            </td>
            <td width="40%"></td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
