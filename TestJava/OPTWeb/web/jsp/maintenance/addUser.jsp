<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlUserDetails = (ArrayList) request.getAttribute("USERDETAILS");
    String strNewUserId = "";
    String strFirstName = "";
    String strLastName = "";
    String strAdminFlag = "";
    String strActiveFlag = "";
    String strIsAssigneeFlag = "";
    String strResourceTeam = "";
    boolean blnNewAdmin = false;
    boolean blnNewActive = true;
    boolean blnIsAssignee = true;

    if (arlUserDetails != null && !arlUserDetails.isEmpty())
    {
        strNewUserId = arlUserDetails.get(0);
        strFirstName = arlUserDetails.get(1);
        strLastName = arlUserDetails.get(2);
        strAdminFlag = arlUserDetails.get(3);
        strActiveFlag = arlUserDetails.get(4);
        strIsAssigneeFlag = arlUserDetails.get(5);
        strResourceTeam = arlUserDetails.get(6);
        if (strAdminFlag.equalsIgnoreCase("Y"))
        {
            blnNewAdmin = true;
        }
        else
        {
            blnNewAdmin = false;
        }
        if (strActiveFlag.equalsIgnoreCase("Y"))
        {
            blnNewActive = true;
        }
        else
        {
            blnNewActive = false;
        }
        if (strIsAssigneeFlag.equalsIgnoreCase("Y"))
        {
            blnIsAssignee = true;
        }
        else
        {
            blnIsAssignee = false;
        }
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function addUser() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.UserId.value)) {
                    OPTDialog("Please enter UserId", document.OPTForm.UserId);
                    return;
                }
                if (/[^a-zA-Z0-9 ]/.test(document.OPTForm.UserId.value)) {
                    OPTDialog("Special Characters Not Allowed in User Id", document.OPTForm.UserId);
                    return;
                }

                if (isEmpty(document.OPTForm.FirstName.value)) {
                    OPTDialog("Please enter First Name", document.OPTForm.FirstName);
                    return;
                }
                if (/[^a-zA-Z ]/.test(document.OPTForm.FirstName.value)) {
                    OPTDialog("Special Characters and Numbers Not Allowed in First Name", document.OPTForm.FirstName);
                    return;
                }
                if (isEmpty(document.OPTForm.LastName.value)) {
                    OPTDialog("Please enter Last Name", document.OPTForm.LastName);
                    return;
                }
                if (/[^a-zA-Z ]/.test(document.OPTForm.LastName.value)) {
                    OPTDialog("Special Characters and Numbers Not Allowed in Last Name", document.OPTForm.LastName);
                    return;
                }
                if (document.OPTForm.Team.selectedIndex == 0) {
                    OPTDialog("Select Team", document.OPTForm.Team);
                    return;
                }
                document.OPTForm.action = "addUser";
                frmWriteSubmit();
            }
        }

        function setValues() {
            <%
            if(blnNewAdmin)
            {
            %>
            document.OPTForm.Admin[0].checked = true;
            document.OPTForm.Admin[1].checked = false;
            <%
            }
            else
            {
            %>
            document.OPTForm.Admin[0].checked = false;
            document.OPTForm.Admin[1].checked = true;

            <%
            }
            %>
            <%
            if(blnNewActive)
            {
            %>
            document.OPTForm.Active[0].checked = true;
            document.OPTForm.Active[1].checked = false;
            <%
            }
            else
            {
            %>
            document.OPTForm.Active[0].checked = false;
            document.OPTForm.Active[1].checked = true;

            <%
            }
            %>
            <%
            if(blnIsAssignee)
            {
            %>
            document.OPTForm.IsAssignee[0].checked = true;
            document.OPTForm.IsAssignee[1].checked = false;
            <%
            }
            else
            {
            %>
            document.OPTForm.IsAssignee[0].checked = false;
            document.OPTForm.IsAssignee[1].checked = true;

            <%
            }
            %>
        }

        function displaymessage() {
            document.OPTForm.UserId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "ADDED") {
                OPTDialog("User details added successfully");
            }
            else if (varResult == "USERIDEXISTS") {
                OPTDialog("UserId already exists", document.OPTForm.UserId);
            }
        }

    </script>
</head>
<body onload="setValues(); displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Add New User
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;User Id</td>
                                    <td><input type="text" name="UserId" size="35" maxlength="30" class="txtgrey" value="<%= strNewUserId %>">
                                        <span class="txtgrey">&nbsp;(testcompany User Id - Please do not add " @testcompany.com ")</span></td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;First Name</td>
                                    <td><input type="text" name="FirstName" size="50" maxlength="100" class="txtgrey"
                                               value="<%= strFirstName %>"></td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Last Name</td>
                                    <td><input type="text" name="LastName" size="50" maxlength="100" class="txtgrey" value="<%= strLastName %>">
                                    </td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Admin</td>
                                    <td>
                                        <input type="radio" name="Admin" value="Y"><span class="txtgrey">Yes</span>&nbsp;
                                        <input type="radio" name="Admin" value="N"><span class="txtgrey">No</span>
                                    </td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Active</td>
                                    <td>
                                        <input type="radio" name="Active" value="Y"><span class="txtgrey">Yes</span>&nbsp;
                                        <input type="radio" name="Active" value="N"><span class="txtgrey">No</span>
                                    </td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Is Assignee</td>
                                    <td>
                                        <input type="radio" name="IsAssignee" value="Y"><span class="txtgrey">Yes</span>&nbsp;
                                        <input type="radio" name="IsAssignee" value="N"><span class="txtgrey">No</span>
                                    </td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Team</td>
                                    <td>
                                        <select name="Team" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                String strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.TRIAGING_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.TRIAGING_TEAM %>" <%= strSelected %>><%= AppConstants.TRIAGING_TEAM %>
                                            </option>
                                            <%
                                                strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.FIXING_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.FIXING_TEAM %>" <%= strSelected %>><%= AppConstants.FIXING_TEAM %>
                                            </option>
                                            <%
                                                strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.FEATURE_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.FEATURE_TEAM %>" <%= strSelected %>><%= AppConstants.FEATURE_TEAM %>
                                            </option>
                                            <%
                                                strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.QA_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.QA_TEAM %>" <%= strSelected %>><%= AppConstants.QA_TEAM %>
                                            </option>
                                            <%
                                                strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.RQA_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.RQA_TEAM %>" <%= strSelected %>><%= AppConstants.RQA_TEAM %>
                                            </option>
                                            <%
                                                strSelected = "";
                                                if (strResourceTeam.equalsIgnoreCase(AppConstants.MGMT_TEAM))
                                                {
                                                    strSelected = "selected";
                                                }
                                            %>
                                            <option value="<%= AppConstants.MGMT_TEAM %>" <%= strSelected %>><%= AppConstants.MGMT_TEAM %>
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="40%">&nbsp;</td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:addUser()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" id="buttonId1" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
</form>
</body>
</html>
