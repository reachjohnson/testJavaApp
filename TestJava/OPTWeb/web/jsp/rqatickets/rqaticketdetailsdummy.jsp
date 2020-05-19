<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.RefNo.value = parent.opener.OPTForm.hidRefNo.value;
            document.OPTForm.TicketId.value = parent.opener.OPTForm.hidTicketId.value;
            document.OPTForm.action = "viewRQATicketDetails";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="RefNo">
    <input type="hidden" name="TicketId">
</form>
</body>
</html>