<script type="text/javascript" language="JavaScript">
    function trackEnterKey(keyevent) {
        if (window.event)
            key = window.event.keyCode;

        else if (keyevent)
            key = keyevent.which;
        var keychar = "";
        keychar = String.fromCharCode(key);

        if (key == 13) {
            if (!isEmpty(document.OPTForm.searchticktid.value)) {
                document.OPTForm.action = "loadSearchTicket";
                frmReadSubmit();
            }
            else {
                keyevent.preventDefault();
                return false;
            }
        }
    }
</script>

<style>
    body {
        color: #c2c2c2;
        margin: 100px 0px 0px 0px;
    }

    div#topdiv {
        position: fixed;
        top: 0px;
        left: 0px;
        width: 100%;
        background: #c2c2c2;
        padding-top: 10px;
    }

    .bannerLabel {
        border: 1px ridge gray;
    }
</style>
<input type="hidden" name="ACCESS" value="RESTRICTED">
<input type="hidden" name="formsubmit">

<div id="topdiv">
    <table width="98%" align="center" border="0" cellspacing="0" cellpadding="0 ">
        <tr>
            <td height='40px' width="5%" align="left" valign="top">
                <img src="images/testcompany_logo_new.gif"/>
            </td>
            <td height='40px' width="5%">
                &nbsp;
            </td>
            <td height='40px' width="25%" valign="middle">
                <table width="100%" align="center" style="border: 1px solid #666666">
                    <tr>
                        <td height='20px' width="27%" class="txtblackbold11" style="border-bottom: 1px solid #666666;">
                            User Name
                        </td>
                        <td height='20px' width="73%" class="txtblack11"
                            style="border-bottom: 1px solid #666666;"><%= strUserName %>
                        </td>
                    </tr>
                    <tr>
                        <td height='20px' class="txtblackbold11">Last Login</td>
                        <td height='20px' class="txtblack11"><%= strLastLoginTime %>
                        </td>
                    </tr>
                </table>
            </td>
            <td height='40px' width="30%" align="center" valign="middle"
                style="font-family: verdana, arial, helvetica; font-size: 18px; font-weight: bold; color: black;">
                <%= AppConstants.APPTITLE %>
            </td>
            <td height='40px' width="2%">
                &nbsp;
            </td>
            <td height='40px' width="25%" valign="middle">
                <table width="100%" border="0" cellpadding=2 cellspacing=2>
                    <tr>
                        <td align="right">
                            <input type="text" style="border: 2px solid red;" name="searchticktid" size="20" maxlength="20" placeholder="Search By Ticket Id" onkeypress="trackEnterKey(event)">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" style="font-family: verdana, arial, helvetica; font-size: 12px; font-weight: bold; color: black;">
                            <input type="button" id="buttonId" onclick="JavaScript:menuSubmitForm('loadSearchCriteria')" value="Advanced Search" class="myButton" style="padding:2px 8px;">
                        </td>
                    </tr>
                </table>
            </td>
            <td height='40px' width="5%" align="right" valign="top">
                <img src="images/csclogo.gif"/>
            </td>
        </tr>
        <tr>
            <td height='40px' width="10%" valign="middle" colspan="7">
                <%@ include file="../common/menu.jsp" %>
            </td>
        </tr>
    </table>
</div>

<%@ include file="../common/onespace.jsp" %>