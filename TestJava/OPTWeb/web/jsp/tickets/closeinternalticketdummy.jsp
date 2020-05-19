<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.hidRefNo.value = parent.opener.OPTForm.hidRefNo.value;
            document.OPTForm.hidTicketId.value = parent.opener.OPTForm.hidTicketId.value;
            document.OPTForm.action = "loadCloseInternalTicket";
            frmReadSubmit();
        }
    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/popup_banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">
</form>
</body>
</html>