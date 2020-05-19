<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strFromLogin = AppUtil.checkNull((String) request.getAttribute("FROMLOGIN"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.action = "homePage";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <input type="hidden" name="ACCESS" value="RESTRICTED">
    <input type="hidden" name="formsubmit">
    <input type="hidden" name="FROMLOGIN" value="<%= strFromLogin %>">
</form>
</body>
</html>