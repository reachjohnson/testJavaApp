<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<Object> arlDomainPOCDetails = (ArrayList) request.getAttribute("DomainPOCDetails");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
</head>
<body>
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner.jsp" %>
    <%
        if (arlDomainPOCDetails != null && !arlDomainPOCDetails.isEmpty())
        {
    %>

    <table width="99%" align="center">
        <tr>
            <td width="80%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Domain POC and DL
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0>
                                <tr class="LightBlueBandStyle">
                                    <td width="15%" class="txtblackbold13">Domain</td>
                                    <td width="25%" class="txtblackbold13">POC Name</td>
                                    <td width="15%" class="txtblackbold13">CORP Id</td>
                                    <td width="20%" class="txtblackbold13">Category</td>
                                    <td width="25%" class="txtblackbold13">DL Name</td>
                                </tr>
                                <%
                                    ArrayList<String> arlDomainPOCContacts;
                                    int intCounter = 0;
                                    String strBackGround = "";

                                    int intNoOfRows = 0;
                                    for (int intFirstCount = 0; intFirstCount < arlDomainPOCDetails.size(); intFirstCount += 3)
                                    {
                                        arlDomainPOCContacts = (ArrayList) arlDomainPOCDetails.get(intFirstCount + 1);
                                %>

                                <%
                                    intNoOfRows = arlDomainPOCContacts.size() / 3;
                                    for (int intSecondCount = 0; intSecondCount < arlDomainPOCContacts.size(); intSecondCount += 3)
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
                                    <%
                                        if (intSecondCount == 0)
                                        {
                                    %>
                                    <td class="txtgreybold"
                                        rowspan="<%= intNoOfRows %>"><%= arlDomainPOCDetails.get(intFirstCount) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                    <td class="txtgrey"><%= arlDomainPOCContacts.get(intSecondCount) %>
                                    </td>
                                    <td class="txtgrey"><%= arlDomainPOCContacts.get(intSecondCount + 1) %>
                                    </td>
                                    <td class="txtgrey"><%= arlDomainPOCContacts.get(intSecondCount + 2) %>
                                    </td>
                                    <%
                                        if (intSecondCount == 0)
                                        {
                                    %>
                                    <td class="txtgreybold"
                                        rowspan="<%= intNoOfRows %>"><%= arlDomainPOCDetails.get(intFirstCount + 2) %>
                                    </td>
                                    <%
                                        }
                                    %>
                                </tr>

                                <%
                                    }
                                %>
                                <%
                                        intCounter++;
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="20%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/twospace.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
