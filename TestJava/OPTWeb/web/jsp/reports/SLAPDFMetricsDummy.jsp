<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.SLAMetricsFromDate.value = parent.opener.OPTForm.SLAMetricsFromDate.value;
            document.OPTForm.SLAMetricsToDate.value = parent.opener.OPTForm.SLAMetricsToDate.value;
            document.OPTForm.action = "GenerateSLAMetricsPDF";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="SLAMetricsFromDate">
    <input type="hidden" name="SLAMetricsToDate">
</form>
</body>
</html>