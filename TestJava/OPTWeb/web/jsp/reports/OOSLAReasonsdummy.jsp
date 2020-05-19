<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.FromDate.value = parent.opener.OPTForm.FromDate.value;
            document.OPTForm.ToDate.value = parent.opener.OPTForm.ToDate.value;
            document.OPTForm.OOSLAReasonsAction.value = parent.opener.OPTForm.OOSLAReasonsAction.value;
            document.OPTForm.action = "loadOOSLAReasons";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="hidFromPage">
    <input type="hidden" name="FromDate">
    <input type="hidden" name="ToDate">
    <input type="hidden" name="OOSLAReasonsAction">
</form>

</body>
</html>