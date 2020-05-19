<%@ page import="com.opt.util.AppConstants" %>
<style type="text/css">
    .container {
        background: #ccc;
    }

    body {
        font: 13px/20px Verdana, Tahoma, sans-serif;
        color: #404040;
        margin: 0;
    }

    .logos > p#csc_logo {
        float: right;
        margin: 10px 25px;
    }

    .logos > p#testcompany_logo {
        float: left;
        margin: 5px 25px;
    }

    h3 {
        text-align: center;
    }

    .banner {
        height: 98px;
        padding-top: 20px;
    }

    #user_id > span#user_id_label {
        margin-right: 23px;
    }

    .banner > p#user_name, p#user_id {
        margin-left: 30px;
        margin-bottom: 0;
    }

    p#user_id, p#user_name {
        margin-top: 0;
    }

    h2 {
        text-align: center;
        margin-top: 14px;
        margin-bottom: 0;
        font-size: 20px;
    }
</style>
<input type="hidden" name="ACCESS" value="RESTRICTED">
<input type="hidden" name="formsubmit">

<div class="container">
    <div class="banner">
        <h2 id="online_payment_tickets">
            <%= AppConstants.APPTITLE %>
        </h2>

        <div class="logos">
            <p id="testcompany_logo">
                <img src="images/testcompany_logo_new.gif"/>
            </p>

            <p id="csc_logo">
                <img src="images/csclogo.gif"/>
            </p>
        </div>
    </div>
</div>
<%@ include file="../common/onespace.jsp" %>