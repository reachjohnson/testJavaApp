<%@ page import="com.opt.util.AppUtil" %>
<%@ page import="com.opt.util.AppConstants" %>
<%@ page import="java.util.Date" %>
<%
    String strInvalidLogin = AppUtil.checkNull((String) request.getAttribute("InvalidLogin"));
    String strUserIncactive = AppUtil.checkNull((String) request.getAttribute("UserInActive"));
%>
<html>
<head runat="server">
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
    <style type="text/css">
        .container {
            margin: 95px auto;
            width: 640px;
        }

        .login {
            position: relative;
            margin: 0 auto;
            padding: 20px 20px 20px;
            width: 310px;
        }

        .login p.submit {
            text-align: right;
        }

        .login-help {
            margin: 20px 0;
            font-size: 11px;
            color: white;
            text-align: center;
            text-shadow: 0 1px #2a85a1;
            display: none;
        }

        .login-help a {
            color: #cce7fa;
            text-decoration: none;
        }

        .login-help a:hover {
            text-decoration: underline;
        }

        :-moz-placeholder {
            color: #c9c9c9 !important;
            font-size: 13px;
        }

        ::-webkit-input-placeholder {
            color: #ccc;
            font-size: 13px;
        }

        .login:before {
            content: '';
            position: absolute;
            top: -8px;
            right: -8px;
            bottom: -8px;
            left: -8px;
            z-index: -1;
            background: rgba(0, 0, 0, 0.08);
            border-radius: 4px;
        }

        .login h1 {
            background: none repeat scroll 0 0 #f3f3f3;
            border-color: #cfcfcf;
            border-radius: 3px 3px 37px 37px;
            border-style: solid;
            border-width: 4px 38px;
            /*box-shadow: 0 1px whitesmoke;*/
            color: #555;
            font-size: 15px;
            font-weight: bold;
            line-height: 40px;
            margin: -20px -20px 21px;
            text-align: center;
            text-shadow: 0 1px white;
        }

        .login p {
            margin: 20px 9px 0;
        }

        .login p:first-child {
            margin-top: 0;
        }

        .login input[type=text], .login input[type=password] {
            width: 278px;
        }

        .login p.remember_me {
            float: left;
            line-height: 31px;
            display: none;
        }

        .login p.remember_me label {
            font-size: 12px;
            color: #363636;
            cursor: pointer;
        }

        .login p.remember_me input {
            position: relative;
            bottom: 1px;
            margin-right: 4px;
            vertical-align: middle;
        }

        input {
            font-family: 'Lucida Grande', Tahoma, Verdana, sans-serif;
            font-size: 14px;
        }

        input[type=text], input[type=password] {
            margin: 5px;
            padding: 0 10px;
            width: 200px;
            height: 34px;
            color: #404040;
            background: white;
            border: 1px solid;
            border-color: #c4c4c4 #d1d1d1 #d4d4d4;
            border-radius: 2px;
            outline: 5px solid #eff4f7;
            -moz-outline-radius: 3px;
            -webkit-box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.12);
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.12);
        }

        input[type=text]:focus, input[type=password]:focus {
            border-color: #7dc9e2;
            outline-color: #dceefc;
            outline-offset: 0;
        }

        input[type=button] {
            padding: 0 18px;
            height: 29px;
            font-size: 12px;
            font-weight: bold;
            color: #527881;
            text-shadow: 0 1px #e3f1f1;
            background: #cde5ef;
            border: 1px solid;
            border-color: #b4ccce #b3c0c8 #9eb9c2;
            border-radius: 16px;
            outline: 0;
            -webkit-box-sizing: content-box;
            -moz-box-sizing: content-box;
            box-sizing: content-box;
            background-image: -webkit-linear-gradient(top, #edf5f8, #cde5ef);
            background-image: -moz-linear-gradient(top, #edf5f8, #cde5ef);
            background-image: -o-linear-gradient(top, #edf5f8, #cde5ef);
            background-image: linear-gradient(to bottom, #edf5f8, #cde5ef);
            -webkit-box-shadow: inset 0 1px white, 0 1px 2px rgba(0, 0, 0, 0.15);
            box-shadow: inset 0 1px white, 0 1px 2px rgba(0, 0, 0, 0.15);
        }

        input[type=submit]:active {
            background: #cde5ef;
            border-color: #9eb9c2 #b3c0c8 #b4ccce;
            -webkit-box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
            box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
        }

        input[type=checkbox],
        input[type=radio] {
            border: 1px solid #c0c0c0;
            margin: 0 0.1em 0 0;
            padding: 0;
            font-size: 16px;
            line-height: 1em;
            width: 1.25em;
            height: 1.25em;
            background: #fff;
            background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#ededed), to(#fbfbfb));

            -webkit-appearance: none;
            -webkit-box-shadow: 1px 1px 1px #fff;
            -webkit-border-radius: 0.25em;
            vertical-align: text-top;
            display: inline-block;

        }

        input[type=radio] {
            -webkit-border-radius: 2em; /* Make radios round */
        }

        input[type=checkbox]:checked::after {
            content: "✔";
            display: block;
            text-align: center;
            font-size: 16px;
            height: 16px;
            line-height: 18px;
        }

        input[type=radio]:checked::after {
            content: "●";
            display: block;
            height: 16px;
            line-height: 15px;
            font-size: 20px;
            text-align: center;
        }

        select {
            border: 1px solid #D0D0D0;
            background: url(http://www.quilor.com/i/select.png) no-repeat right center, -webkit-gradient(linear, 0% 0%, 0% 100%, from(#fbfbfb), to(#ededed));
            background: -moz-linear-gradient(19% 75% 90deg, #ededed, #fbfbfb);
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            color: #444;
        }

        body {
            font: 13px/20px "Lucida Grande", Tahoma, Verdana, sans-serif;
            color: #404040;
            background: #ccc;
        }

        .logos > p#csc_logo {
            float: left;
            margin: 8px 36px;
        }

        .logos > p#testcompany_logo {
            float: right;
            margin: 0;
        }

        .logos {
            height: 50px;
            background-color: #fff;
        }

        .title {
            margin: 58px 185px;
        }

        h3 {
            text-align: center;
        }
    </style>

    <title><%= AppConstants.APPTITLE %>
    </title>
    <link href="styles/Style.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" language="JavaScript">
        function trackEnterKey(e) {
            if (window.event)
                key = window.event.keyCode;

            else if (e)
                key = e.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                Login();
            }
        }

        function onLoadFun() {
            document.OPTForm.LoginID.focus();
        }
        function changePassword() {
            document.OPTForm.action = "changePassword";
            submit();
        }
        function Login() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.LoginID.value)) {
                    alert("Please enter UserId");
                    document.OPTForm.LoginID.focus();
                    return false;
                }
                if (isEmpty(document.OPTForm.Password.value)) {
                    alert("Please enter Password");
                    document.OPTForm.Password.focus();
                    return false;
                }
                document.OPTForm.action = "validateLogin";
                frmLoginSubmit();
            }
        }

        function cancelLogin() {
            document.OPTForm.reset();
        }

        function frmDisplayErrorMessage() {
            var varInvalidLogin = "<%= strInvalidLogin.toUpperCase() %>";
            var varUserIncactive = "<%= strUserIncactive.toUpperCase() %>";
            if (varInvalidLogin == "INVALIDLOGIN") {
                alert(("UserId / Password is incorrect"));
                document.OPTForm.LoginID.focus();
                return false;
            }
            else if (varUserIncactive == "USERINACTIVE") {
                alert(("User is not active"));
                document.OPTForm.LoginID.focus();
                return false;
            }

        }
    </script>


</head>
<body onload="onLoadFun(); frmDisplayErrorMessage();">
<form name="OPTForm" method="post">
    <input type="hidden" name="ACCESS" value="RESTRICTED">
    <input type="hidden" name="formsubmit">

    <div class="logos">
        <p id="csc_logo">
            <img src="images/csclogo.gif"/>
        </p>

        <p id="testcompany_logo">
            <img src="images/testcompany_logo_new.gif"/>
        </p>
    </div>
    <div class="title">
        <h1 id="online_payment_tickets">
            <%= AppConstants.APPTITLE %>
        </h1>

        <h3 id="mso_team">
            Managed Services Team
        </h3>
    </div>
    <div class="container">
        <div class="login">
            <h1>Login</h1>

            <form method="post" action="">
                <p><input type="text" name="LoginID" value="" placeholder="Username" size="20" maxlength="30"
                          onKeyPress="trackEnterKey(event)"></p>

                <p><input type="password" name="Password" value="" placeholder="Password" size="20" maxlength="20"
                          onKeyPress="trackEnterKey(event)"></p>

                <p class="remember_me">
                    <label>
                        <label>
                            <input type="checkbox" name="remember_me" id="remember_me">
                            Remember me on this computer
                        </label>
                    </label>
                </p>

                <p class="submit">
                    <input type="button" name="btnLogin" value="Login" onclick="JavaScript:Login()">
                    <input type="button" name="btnReset" value="Reset" onclick="JavaScript:cancelLogin()">
                </p>
            </form>
        </div>

        <div class="login-help">
            <p>Forgot your password? <a href="#">Click here to reset it</a>.</p>
        </div>
    </div>
</form>
</body>
</html>