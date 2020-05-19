<%@ include file="../../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strAssigneeName = AppUtil.checkNull((String) request.getAttribute("AssigneeName"));
    String strClosureTrendChartPath = AppUtil.checkNull((String) request.getAttribute("ClosureTrendChartPath"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
</head>
<body>
<%@ include file="../../common/popup_banner.jsp" %>
<table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
    <tr>
        <td class="header">&nbsp;&nbsp;Weekly Closure Trend - Closed Tickets By <%= strAssigneeName %>
        </td>
    </tr>
</table>
<%@ include file="../../common/spaceafter.jsp" %>

<table width="90%" cellpadding=0 cellspacing=0 border=0 align="left">
    <tr>
        <td><img src="<%= strClosureTrendChartPath%>">
        </td>
    </tr>
</table>


<%@ include file="../../common/spacebefore.jsp" %>
<table cellpadding=2 cellspacing=1 border=0 align="center">
    <tr>
        <td width="100%" align="center" valign="middle">&nbsp;
            <input type="button" class="myButton" id="buttonId1" value="Close" onclick="JavaScript:frmClose()"/>
        </td>
    </tr>
</table>

</body>
</html>