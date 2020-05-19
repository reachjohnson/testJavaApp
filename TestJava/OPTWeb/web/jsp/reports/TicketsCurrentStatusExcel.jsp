<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strFilename = AppUtil.checkNull((String) request.getAttribute("EXCELFILEPATH"));
    request.setAttribute("EXCELFILEPATH", strFilename);
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        function frmSetValues() {
            document.OPTForm.action = "DownloadExcel?EXCELFILEPATH=" + document.OPTForm.FilePath.value;
            document.OPTForm.submit();
        }

    </script>


</head>
<body onload="JavaScript:frmSetValues()">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <input type="hidden" name="FilePath" value="<%= strFilename %>">
    <%@ include file="../common/popup_banner.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="headergreen18" align="center">Excel Uploaded Successfully....</td>
        </tr>
    </table>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>

    </table>

    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td width="100%" align="center" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Close" onclick="JavaScript:frmClose()"/>
            </td>
        </tr>
    </table>


</form>
</body>
</html>