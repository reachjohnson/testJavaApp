<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList arlListOfHolidays = (ArrayList) request.getAttribute("LISTOFHOLIDAYS");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
</head>
<body>
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidHolidayRefNo">
    <input type="hidden" name="hidAction">

    <table width="99%" align="center">
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Holidays
                                    </td>
                                </tr>
                            </table>
                            <%
                                if (arlListOfHolidays != null && !arlListOfHolidays.isEmpty())
                                {
                            %>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="10%" class="txtblackbold12">Holiday Date</td>
                                    <td width="20%" class="txtblackbold12">Holiday Name</td>
                                </tr>
                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int iCount = 0; iCount < arlListOfHolidays.size(); iCount += 3)
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
                                    <td class="txtgrey"><%= arlListOfHolidays.get(iCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlListOfHolidays.get(iCount + 2) %>
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
            <td width="70%"></td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>
</form>
</body>
</html>
