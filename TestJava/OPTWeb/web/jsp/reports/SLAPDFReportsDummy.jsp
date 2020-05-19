<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.SLAReportsFromDate.value = parent.opener.OPTForm.SLAReportsFromDate.value;
            document.OPTForm.SLAReportsToDate.value = parent.opener.OPTForm.SLAReportsToDate.value;
            document.OPTForm.action = "GenerateSLAReportsPDF";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="SLAReportsFromDate">
    <input type="hidden" name="SLAReportsToDate">
</form>
</body>
</html>