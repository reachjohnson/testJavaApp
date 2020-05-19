<%@ include file="../common/noCache.jsp" %>
<%
    String strInvalidLogin = AppUtil.checkNull((String) request.getAttribute("InvalidLogin"));
    String strUserIncactive = AppUtil.checkNull((String) request.getAttribute("UserInActive"));
    String strChangePassword = AppUtil.checkNull((String) request.getAttribute("ChangePasswordSuccess"));
    String strLoginIdForChangePwd = AppUtil.checkNull((String) request.getAttribute("LOGINID"));
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">

        function trackEnterKeyCP(e) {
            if (window.event)
                key = window.event.keyCode;

            else if (e)
                key = e.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                Changepassword();
            }
        }

        function Changepassword() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.LoginID.value)) {
                    OPTDialog("Please enter Login ID", document.OPTForm.LoginID);
                    return;
                }
                if (isEmpty(document.OPTForm.OldPassword.value)) {
                    OPTDialog("Please enter Old Password", document.OPTForm.OldPassword);
                    return;
                }
                if (isEmpty(document.OPTForm.NewPassword.value)) {
                    OPTDialog("Please enter New Password", document.OPTForm.NewPassword);
                    return;
                }
                if (isEmpty(document.OPTForm.ConfirmPassword.value)) {
                    OPTDialog("Please confirm New Password", document.OPTForm.ConfirmPassword);
                    return;
                }

                if ((document.OPTForm.NewPassword.value).toLowerCase() == 'password') {
                    OPTDialog("Please enter different password", document.OPTForm.NewPassword);
                    return;
                }

                if (trim(document.OPTForm.NewPassword.value) != trim(document.OPTForm.ConfirmPassword.value)) {
                    OPTDialog("New Password and Confirm Password should be same", document.OPTForm.ConfirmPassword);
                    return;
                }
                if (trim(document.OPTForm.OldPassword.value) == trim(document.OPTForm.NewPassword.value)) {
                    OPTDialog("Old Password and New Password can not be same", document.OPTForm.NewPassword);
                    return;
                }
                document.OPTForm.action = "saveChangePassword";
                frmLoginSubmit();
            }
        }

        function cancel() {
            document.OPTForm.action = "Login";
            frmLoginSubmit();
        }

        function frmDisplayErrorMessage() {
            var varInvalidLogin = "<%= strInvalidLogin.toUpperCase() %>";
            var varUserIncactive = "<%= strUserIncactive.toUpperCase() %>";
            var varChangePassword = "<%= strChangePassword.toUpperCase() %>";
            if (varInvalidLogin == "INVALIDLOGIN") {
                OPTDialog("Login ID / Password is incorrect", document.OPTForm.LoginID);
                return false;
            }
            else if (varUserIncactive == "USERINACTIVE") {
                OPTDialog("User is not active", document.OPTForm.LoginID);
                return false;
            }
            else if (varChangePassword == "SUCCESS") {
                showFullPageMask(true);
                jAlert("Password has been changed successfully.\nYou will be redirected to login page.", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        document.OPTForm.action = "Login";
                        frmLoginSubmit();
                    }
                });
            }
        }

        function setFocusChangePwd() {
            var LoginId = "<%= strLoginIdForChangePwd %>";
            if (LoginId.length > 0) {
                document.OPTForm.OldPassword.focus();
            }
            else {
                document.OPTForm.LoginID.focus();
            }
        }

    </script>
</head>
<body onload="setFocusChangePwd();frmDisplayErrorMessage();">
<form name="OPTForm" method="post" action="">
    <%@ include file="../common/banner1.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td class="header">Change Password</td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>
    <table width="40%" cellpadding=2 cellspacing=1 border=0 align="center">
        <tr class="LightBandStyle">
            <td width="40%" class="txtgreybold">User ID</td>
            <td width="60%"><input type="text" name="LoginID" size="20" maxlength="30" class="txtgrey"
                                   onKeyPress="trackEnterKeyCP(event)" value="<%= strLoginIdForChangePwd %>"></td>
        </tr>
        <tr class="LightBandStyle">
            <td class="txtgreybold">Old Password</td>
            <td><input type="password" name="OldPassword" size="20" maxlength="20" class="txtgrey"
                       onKeyPress="trackEnterKeyCP(event)"></td>
        </tr>
        <tr class="LightBandStyle">
            <td class="txtgreybold">New Password</td>
            <td><input type="password" name="NewPassword" size="20" maxlength="20" class="txtgrey"
                       onKeyPress="trackEnterKeyCP(event)"></td>
        </tr>
        <tr class="LightBandStyle">
            <td class="txtgreybold">Confirm Password</td>
            <td><input type="password" name="ConfirmPassword" size="20" maxlength="20" class="txtgrey"
                       onKeyPress="trackEnterKeyCP(event)"></td>
        </tr>
        <tr>
            <table cellpadding=2 cellspacing=1 border=0 align="center">
                <tr>
                    <td width="50%" align="right">&nbsp;
                        <input type="button" class="myButton" value="Save" onclick="JavaScript:Changepassword()"
                               id="buttonId" onKeyPress="trackEnterKeyCP(event)"/>
                    </td>
                    <td width="50%" align="left">&nbsp;
                        <input type="button" class="myButton" value="Cancel" onclick="JavaScript:cancel()"/>
                </tr>
            </table>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
</form>
</body>
</html>
