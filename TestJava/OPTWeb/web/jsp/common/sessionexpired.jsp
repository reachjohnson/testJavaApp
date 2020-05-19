<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<link href="styles/styleie.css?ts=<%= String.valueOf(new Date().getTime()) %>" rel="stylesheet" type="text/css"/>
<html>
<head>
    <div id="spinnerContainer" style="position:fixed;top:50%;left:50%">
    </div>
    <script language="javascript" src="/js/spin.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
    <script type="text/javascript">
        var opts = {
            lines: 15, // The number of lines to draw
            length: 70, // The length of each line
            width: 25, // The line thickness
            radius: 75, // The radius of the inner circle
            corners: 1, // Corner roundness (0..1)
            rotate: 0, // The rotation offset
            color: '#000', // #rgb or #rrggbb
            speed: 0.75, // Rounds per second
            trail: 50, // Afterglow percentage
            shadow: false, // Whether to render a shadow
            hwaccel: false, // Whether to use hardware acceleration
            className: 'spinner', // The CSS class to assign to the spinner
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            top: 'auto', // Top position relative to parent in px
            left: 'auto' // Left position relative to parent in px
        };
    </script>

    <script language="javascript" src="/js/common.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>

    <script type="text/javascript" language="JavaScript">
        function submitForm() {
            document.OPTForm.action = "Login";
            frmReadSubmit();
        }

        function trackEnterKey(e) {
            if (window.event)
                key = window.event.keyCode;

            else if (e)
                key = e.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                submitForm();
            }
        }

    </script>

</head>

<body onKeyPress="trackEnterKey(event)">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <%@ include file="../common/banner1.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="alertred" align="center">Your Session Expired</td>
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

    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="alertred" align="center">Press ENTER or Click&nbsp;<a href="JavaScript:submitForm();">here</a>&nbsp;to
                login again
            </td>
        </tr>
    </table>

</form>
</body>
</html>
