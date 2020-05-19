<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.hidTeamSummary.value = parent.opener.OPTForm.hidTeamSummary.value;
            parent.opener.OPTForm.hidTeamSummary.value = "";
            document.OPTForm.action = "ClosureTrend";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="hidFromPage">
    <input type="hidden" name="hidTeamSummary">
</form>
</body>
</html>