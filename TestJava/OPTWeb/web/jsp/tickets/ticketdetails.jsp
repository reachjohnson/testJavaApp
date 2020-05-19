<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    ArrayList<String> arlViewTicketDetails = (ArrayList) request.getAttribute("ViewTicketDetails");
    ArrayList<String> arlTicketHistory = (ArrayList) request.getAttribute("TicketHistory");
    ArrayList<String> arlTicketActivityHistory = (ArrayList) request.getAttribute("TicketActivityHistory");
    ArrayList<String> arlCommentsHistory = (ArrayList) request.getAttribute("CommentsHistory");
    String strTotalDaysSpent = AppUtil.checkNull((String) request.getAttribute("TotalDaysSpent"));

    ArrayList<String> arlTriagingTicketDetails = (ArrayList) request.getAttribute("TriagingTicketDetails");
    ArrayList<String> arlTriagingTicketHistory = (ArrayList) request.getAttribute("TriagingTicketHistory");
    ArrayList<String> arlTriagingTicketActivityHistory = (ArrayList) request.getAttribute("TriagingTicketActivityHistory");
    ArrayList<String> arlTriagingCommentsHistory = (ArrayList) request.getAttribute("TriagingCommentsHistory");
    String strTriagingTotalDaysSpent = AppUtil.checkNull((String) request.getAttribute("TriagingTotalDaysSpent"));


    String strSLAMissed = arlViewTicketDetails.get(10);
    String strTicketStatus = arlViewTicketDetails.get(16);
    String strTriagingSLAMissed = "";
    String strTriagingTicketStatus = "";

    if (arlTriagingTicketDetails != null && !arlTriagingTicketDetails.isEmpty())
    {
        strTriagingSLAMissed = arlTriagingTicketDetails.get(10);
        strTriagingTicketStatus = arlTriagingTicketDetails.get(16);
    }

    String strTicketCompleted = AppConstants.COMPLETED;
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
                    OPTDialog("This Ticket is Already OOSLA");
                }
            }
        }
    </script>

</head>
<body onload="displayMessage()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>

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

    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="100%" align="center" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Close" onclick="JavaScript:frmClose()" id="buttonId1"/>
            </td>
        </tr>
    </table>

</form>
</body>
</html>