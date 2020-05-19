<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strLoginPage = AppUtil.checkNull((String) request.getAttribute("LOGINPAGE"));
%>
<html>
<head>
    <title>Global Error Handler</title>
</head>
<body>
<form name="OPTForm" method="post" action="">
    <%
        if (strLoginPage.equalsIgnoreCase("YES"))
        {
    %>
    <%@ include file="../common/banner1.jsp" %>
    <%
    }
    else
    {
    %>
    <%@ include file="../common/banner.jsp" %>
    <%
        }
    %>
    <input type="hidden" name="hidSubmitAction">

    <table width="95%" cellpadding=0 cellspacing=0 border=0 align="left">
        <tr>
            <td class="alertred">Global Error Handler</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td class="txtredbold"><s:property value="exception.stackTrace"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td class="txtredbold"><s:property value="exception.message"/></td>
        </tr>

    </table>
</form>
</body>
</html>
