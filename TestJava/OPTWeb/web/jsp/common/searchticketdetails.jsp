<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlViewTicketDetails = null;
    ArrayList<String> arlTicketHistory = null;
    ArrayList<String> arlTicketActivityHistory = null;
    ArrayList<String> arlCommentsHistory = null;
    String strTotalDaysSpent = "";

    ArrayList<String> arlTriagingTicketDetails = null;
    ArrayList<String> arlTriagingTicketHistory = null;
    ArrayList<String> arlTriagingTicketActivityHistory = null;
    ArrayList<String> arlTriagingCommentsHistory = null;
    String strTriagingTotalDaysSpent = "";


    String strSLAMissed = "";
    String strTicketStatus = "";
    String strTriagingSLAMissed = "";
    String strTriagingTicketStatus = "";

    String strTicketCompleted = "";

    String strSearchTicketId = AppUtil.checkNull((String) request.getAttribute("SearchTicketId"));
    String strTicketDetailsAvailable = AppUtil.checkNull((String) request.getAttribute("TicketDetailsAvailable"));
    if (strTicketDetailsAvailable.equalsIgnoreCase("AVAILABLE"))
    {
        arlViewTicketDetails = (ArrayList) request.getAttribute("ViewTicketDetails");
        arlTicketHistory = (ArrayList) request.getAttribute("TicketHistory");
        arlTicketActivityHistory = (ArrayList) request.getAttribute("TicketActivityHistory");
        arlCommentsHistory = (ArrayList) request.getAttribute("CommentsHistory");
        strTotalDaysSpent = AppUtil.checkNull((String) request.getAttribute("TotalDaysSpent"));

        arlTriagingTicketDetails = (ArrayList) request.getAttribute("TriagingTicketDetails");
        arlTriagingTicketHistory = (ArrayList) request.getAttribute("TriagingTicketHistory");
        arlTriagingTicketActivityHistory = (ArrayList) request.getAttribute("TriagingTicketActivityHistory");
        arlTriagingCommentsHistory = (ArrayList) request.getAttribute("TriagingCommentsHistory");
        strTriagingTotalDaysSpent = AppUtil.checkNull((String) request.getAttribute("TriagingTotalDaysSpent"));

        if (strTicketStatus.equalsIgnoreCase(AppConstants.COMPLETED))
        {
            strSLAMissed = arlViewTicketDetails.get(18);
        }
        else
        {
            strSLAMissed = arlViewTicketDetails.get(10);
        }
        strTicketStatus = arlViewTicketDetails.get(16);
        strTriagingSLAMissed = "";
        strTriagingTicketStatus = "";

        if (arlTriagingTicketDetails != null && !arlTriagingTicketDetails.isEmpty())
        {
            strTriagingTicketStatus = arlTriagingTicketDetails.get(16);
            if (strTriagingTicketStatus.equalsIgnoreCase(AppConstants.COMPLETED))
            {
                strTriagingSLAMissed = arlTriagingTicketDetails.get(18);
            }
            else
            {
                strTriagingSLAMissed = arlTriagingTicketDetails.get(10);
            }
        }

        strTicketCompleted = AppConstants.COMPLETED;

    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function displayMessage() {
            var SLAMissed = "<%= strSLAMissed %>";
            var ticketStatus = "<%= strTicketStatus %>";
            var ticketCompleted = "<%= strTicketCompleted %>";
            if (OOSLA_HIGHLIGHT == "true") {
                if (SLAMissed == "1" && ticketStatus != ticketCompleted) {
                    OPTDialog("This Ticket is Already OOSLA")
                }
            }
        }
    </script>

</head>
<body onload="displayMessage()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/banner.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="txtblackbold14">&nbsp;&nbsp;Search Ticket</td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>

    <%
        if (strTicketDetailsAvailable.equalsIgnoreCase("AVAILABLE"))
        {
    %>
    <%@ include file="../common/tickets/ticketdetails.jsp" %>
    <%@ include file="../common/onespace.jsp" %>
    <%@ include file="../common/tickets/ticketshistory.jsp" %>
    <%@ include file="../common/onespace.jsp" %>


    <%
        if (arlTriagingTicketDetails != null && !arlTriagingTicketDetails.isEmpty())
        {
    %>
    <%@ include file="../common/tickets/triagingticketdetails.jsp" %>
    <%@ include file="../common/onespace.jsp" %>
    <%@ include file="../common/tickets/triagedticketshistory.jsp" %>
    <%@ include file="../common/onespace.jsp" %>
    <%
        }
    %>
    <%
    }
    else
    {
    %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="alertred" align="center">Ticket Searched : <%= strSearchTicketId %>
            </td>
        </tr>
        <tr>
            <td align="center">&nbsp;</td>
        </tr>
        <tr>
            <td class="alertred" align="center">No Data Found</td>
        </tr>
    </table>
    <%
        }
    %>


</form>
</body>
</html>