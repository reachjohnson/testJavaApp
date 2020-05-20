<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function UpdateLocation() {
            if (document.WebForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.WebForm.LocationName.value)) {
                    warningMessage("Enter Location Name", function (caption) {
                        document.WebForm.LocationName.focus();
                    });
                    return;
                }
                document.WebForm.action = "UpdateLocation";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.WebForm.hidRefNo.value = parent.opener.WebForm.hidRefNo.value;
            document.WebForm.LocationName.focus();
            var varResult = "<%= Result %>";
            if (varResult == "SUCCESS") {
                confirmMessage("Location Details Updated", function (caption) {
                    parent.opener.WebForm.action = "loadAddLocation";
                    parent.opener.WebForm.submit();
                    window.close();
                });
            }
            else if (varResult == "EXISTS") {
                errorMessage("Location Name already exists", function (caption) {
                    document.WebForm.LocationName.focus();
                });
            }
        }

    </script>
</head>
<body onload="displaymessage();">
<form name="WebForm" method="post">
    <% include ../common/popup_banner.ejs %>
    <div id="TabeleDiv">
        <input type="hidden" name="hidRefNo">
        <input type="hidden" name="hidOldLocationName" value="<%= LocationDetails[0].LOCATION_NAME %>">
        <table width="99%" align="center">
            <tr>
                <td width="70%">
                    <table width="100%" cellpadding=0 cellspacing=0 border=0 class="TableBorder1Pix">
                        <tr>
                            <td width="100%" valign="top">
                                <table width="100%">
                                    <tr>
                                        <td width="100%" class="DarkBlueBandStyleText13">Update Location
                                        </td>
                                    </tr>
                                </table>
                                <table width="100%" cellpadding=3 cellspacing=0 border="1"
                                       style="border-collapse:collapse;" class="BlueBandStyleShadow">
                                    <tr>
                                        <td class="txtblackbold12">Location Name</td>
                                        <td><input type="text" name="LocationName" size="35" maxlength="30"
                                                   class="txtblack12"
                                                   value="<%= LocationDetails[0].LOCATION_NAME %>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="txtblackbold12">Active</td>
                                        <%
                                        var activeChecked = "";
                                        var inActiveChecked = "";
                                        if (LocationDetails[0].ACTIVE == "Y") {
                                            activeChecked = "checked";
                                            inActiveChecked = "";
                                        }
                                        else {
                                            activeChecked = "";
                                            inActiveChecked = "checked";
                                        }
                                        %>
                                        <td><input type="radio" name="ActiveFlag" id="radio1" class="css-radiobutton"
                                                   value="Y" <%= activeChecked %> />
                                            <label for="radio1" class="css-radiolabel radGroup3"></label>
                                            <span class="txtblackbold12"><label for="radio1">Yes</label></span>&nbsp;&nbsp;
                                            <input type="radio" name="ActiveFlag" id="radio2" class="css-radiobutton"
                                                   value="N" <%= inActiveChecked %>/>
                                            <label for="radio2" class="css-radiolabel radGroup3"></label>
                                            <span class="txtblackbold12"><label for="radio2">No</label></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2"><input type="button" class="myButton"
                                                                              value="Save"
                                                                              onclick="JavaScript:UpdateLocation()"
                                                                              id="buttonId0"/>&nbsp;<input type="button"
                                                                                                           class="myButton"
                                                                                                           id="buttonId1"
                                                                                                           value="Close"
                                                                                                           onclick="JavaScript:frmClose()"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="30%">&nbsp;</td>
            </tr>
        </table>
        <% include ../common/onespace.ejs %>
    </div>
</form>
</body>
</html>
