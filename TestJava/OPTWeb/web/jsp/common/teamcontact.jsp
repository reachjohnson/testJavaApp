<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlTeamContacts = (ArrayList) request.getAttribute("TeamContacts");
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
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Team Contacts
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="40%" class="txtblackbold13">Name</td>
                                    <td width="20%" class="txtblackbold13">Contact Number</td>
                                    <td width="20%" class="txtblackbold13">CORP Id</td>
                                    <td width="20%" class="txtblackbold13">CSC Id</td>
                                </tr>
                                <%
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    for (int intCount = 0; intCount < arlTeamContacts.size(); intCount += 4)
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
                                    <td class="txtgrey"><%= arlTeamContacts.get(intCount) %>
                                    </td>
                                    <td class="txtgrey"><%= arlTeamContacts.get(intCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlTeamContacts.get(intCount + 2) %>
                                    </td>
                                    <td class="txtgrey"><%= arlTeamContacts.get(intCount + 3) %>
                                    </td>
                                </tr>
                                <%
                                        intCounter++;
                                    }
                                %>
                            </table>
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
