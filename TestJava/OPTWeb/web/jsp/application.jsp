<%@ page import="com.opt.util.AppConstants" %>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title></head>
<script language="javascript">

    function newWin() {
        var params = 'width=' + screen.width;
        params += ', height=' + screen.height;
        params += ', top=0, left=0'
        var url = 'Login';
        newWindow = window.open(url, '_self', 'resizable=no,status=yes,toolbar=no,menubar=no,location=no,scrollbars=auto,copyhistory=no,' + params);
        return false;
    }
</script>

<body onLoad=newWin();>
</body>
</html>