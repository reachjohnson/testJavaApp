<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function addLocation() {
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
                document.WebForm.action = "AddLocation";
                frmWriteSubmit();
            }
        }

        function updateLocation(LocationRefNo) {
            document.WebForm.hidRefNo.value = LocationRefNo;
            MM_openBrWindow("loadUpdateLocationDummy", 300, 700);
        }

        function displaymessage() {
            document.WebForm.LocationName.focus();
            var varResult = "<%= Result %>";
            if (varResult == "SUCCESS") {
                confirmMessage("Location Name Added", function (caption) {
                    document.WebForm.LocationName.focus();
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
<body onload="displaymessage();" onfocus="FocusModalWin()" onClick="FocusModalWin()" onUnLoad="CloseModalWin()">
<form name="WebForm" method="post">
    <% include ../common/banner.ejs %>
    <% include ../common/onespace.ejs %>
    <input type="hidden" name="hidRefNo">

    <div id="TabeleDiv">
        <table width="99%" align="center">
            <tr>
                <td width="50%">
                    <table width="100%" cellpadding=0 cellspacing=0 border=0 class="TableBorder1Pix">
                        <tr>
                            <td width="100%" valign="top">
                                <table width="100%">
                                    <tr>
                                        <td width="100%" class="DarkBlueBandStyleText13">Add New Location
                                        </td>
                                    </tr>
                                </table>
                                <table width="100%" cellpadding=3 cellspacing=0 border="1"
                                       style="border-collapse:collapse;" class="BlueBandStyleShadow">
                                    <tr>
                                        <td class="txtblackbold12">Location Name</td>
                                        <td><input type="text" name="LocationName" size="40" maxlength="29"
                                                   class="txtblack12"
                                                   value="<%= LocationName %>">
                                        </td>
                                        <td><input type="button" class="myButton" value="Add"
                                                   onclick="JavaScript:addLocation()" id="buttonId0"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="50%">&nbsp;</td>
            </tr>
        </table>
        <% include ../common/onespace.ejs %>

        <table width="99%" align="center">
            <tr>
                <td width="30%" valign="top">
                    <%
                    if(ActiveLocations.length > 0)
                    {
                    %>
                    <table width="100%">
                        <tr>
                            <td>
                                <table width="100%" cellpadding=0 cellspacing=0 border=0 class="TableBorder1Pix">
                                    <tr>
                                        <td width="100%" valign="top">
                                            <table width="100%">
                                                <tr>
                                                    <td width="100%" class="DarkBlueBandStyleText13">Active
                                                        Locations
                                                    </td>
                                                </tr>
                                            </table>
                                            <table width="100%" cellpadding=3 cellspacing=1 id="mainTable">
                                                <tr class="LightBlueBandStyle">
                                                    <td width="4%" class="txtblackbold12" align="center">Update</td>
                                                    <td width="16%" class="txtblackbold12">Location Name</td>
                                                </tr>
                                                <%
                                                    var intCounter = 0;
                                                    var BackGround = "";
                                                        for (var iCount in ActiveLocations)
                                                {
                                                    if (intCounter % 2 == 0) {
                                                        BackGround = "LightGreyBandStyle";
                                                    }
                                                    else {
                                                        BackGround = "DarkGreyBandStyle";
                                                    }
                                                %>
                                                <tr class="<%= BackGround %>">
                                                    <td align="center" valign="middle">
                                                        <table>
                                                            <tr>
                                                                <td valign="middle"><a
                                                                            href="JavaScript:updateLocation('<%= ActiveLocations[iCount].REF_NO %>')">
                                                                        <img src="images/update_new.jpg" border="0"
                                                                             class="tooltip" title="Update Location"></a></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td class="txtblack12"><%= ActiveLocations[iCount].LOCATION_NAME %>
                                                    </td>
                                                </tr>
                                                <%
                                                    intCounter++;
                                                }
                                                %>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <%
                    }
                    %>
                </td>
                <td width="5%" valign="top">&nbsp;</td>
                <td width="30%" valign="top">
                    <%
                    if(InActiveLocations.length > 0)
                    {
                    %>
                    <table width="100%">
                        <tr>
                            <td>
                                <table width="100%" cellpadding=0 cellspacing=0 border=0 class="TableBorder1Pix">
                                    <tr>
                                        <td width="100%" valign="top">
                                            <table width="100%">
                                                <tr>
                                                    <td width="100%" class="DarkBlueBandStyleText13">InActive
                                                        Locations
                                                    </td>
                                                </tr>
                                            </table>
                                            <table width="100%" cellpadding=3 cellspacing=1 id="mainTable">
                                                <tr class="LightBlueBandStyle">
                                                    <td width="4%" class="txtblackbold12" align="center">Update</td>
                                                    <td width="16%" class="txtblackbold12">Location Name</td>
                                                </tr>
                                                <%
                                                    var intCounter = 0;
                                                    var BackGround = "";
                                                        for (var iCount in InActiveLocations)
                                                {
                                                    if (intCounter % 2 == 0) {
                                                        BackGround = "LightGreyBandStyle";
                                                    }
                                                    else {
                                                        BackGround = "DarkGreyBandStyle";
                                                    }
                                                %>
                                                <tr class="<%= BackGround %>">
                                                    <td align="center" valign="middle">
                                                        <table>
                                                            <tr>
                                                                <td valign="middle"><a
                                                                            href="JavaScript:updateLocation('<%= InActiveLocations[iCount].REF_NO %>')">
                                                                        <img src="images/update_new.jpg" border="0"
                                                                             class="tooltip" title="Update Location"></a></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td class="txtblack12"><%= InActiveLocations[iCount].LOCATION_NAME %>
                                                    </td>
                                                </tr>
                                                <%
                                                    intCounter++;
                                                }
                                                %>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <%
                    }
                    %>
                </td>
                <td width="35%" valign="top">&nbsp;</td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
