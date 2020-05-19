<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlUserDetails = (ArrayList) request.getAttribute("USERDETAILS");
    ArrayList<String> arlUserList = (ArrayList) request.getAttribute("USERLIST");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strSelectedUserId = AppUtil.checkNull((String) request.getAttribute("UserId"));
    String strFirstName = "";
    String strLastName = "";
    String strAdminFlag = "";
    String strActiveFlag = "";
    String strIsAssigneeFlag = "";
    String strResourceTeam = "";
    boolean blnNewAdmin = false;
    boolean blnNewActive = true;
    boolean blnIsAssignee = true;
    int intRecordsCount = 0;
    String strSelected = "";

    if (arlUserDetails != null && !arlUserDetails.isEmpty())
    {
        intRecordsCount = arlUserDetails.size();
        strFirstName = arlUserDetails.get(1);
        strLastName = arlUserDetails.get(2);
        strActiveFlag = arlUserDetails.get(3);
        strAdminFlag = arlUserDetails.get(4);
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

        function modifyUser() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
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

                document.OPTForm.action = "modifyUser";
                frmWriteSubmit();
            }
        }

        function getUserDetails() {
            document.OPTForm.action = "loadModifyUser";
            frmReadSubmit();
        }

        function setValues() {
            var recordsCount = <%= intRecordsCount %>;
            if (recordsCount > 0) {
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
        }

        function displaymessage() {
            document.OPTForm.UserId.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "MODIFIED") {
                OPTDialog("User Details modified successfully", document.OPTForm.UserId);
            }
        }

    </script>
</head>
<body onload="JavaScript:setValues(); displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="OldActiveFlag" value="<%= strActiveFlag %>">

    <table width="99%" align="center">
        <tr>
            <td width="30%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Select User to Modify
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=1 border=0>
                                <tr class="LightBandStyle">
                                    <td width="30%" class="txtgreybold">Select User</td>
                                    <td width="70%">
                                        <select name="UserId" class="txtgrey" onchange="JavaScript:getUserDetails()">
                                            <option value="">Select</option>
                                            <%
                                                if (arlUserList != null && !arlUserList.isEmpty())
                                                {
                                                    for (int intCount = 0; intCount < arlUserList.size(); intCount += 2)
                                                    {
                                                        strSelected = "";
                                                        if (strSelectedUserId.equalsIgnoreCase(arlUserList.get(intCount)))
                                                        {
                                                            strSelected = "selected";
                                                        }
                                            %>
                                            <option value="<%= arlUserList.get(intCount) %>" <%= strSelected %>><%= arlUserList.get(intCount + 1) %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="70%">&nbsp;</td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>
    <%
        if (arlUserDetails != null && !arlUserDetails.isEmpty())
        {
    %>

    <table width="99%" align="center">
        <tr>
            <td width="60%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
                    <tr>
                        <td width="100%" valign="top">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;User Details to Modify
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;User Id</td>
                                    <td class="txtgrey"><%= strSelectedUserId %>
                                    </td>
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
                                    <td class="txtgreybold">&nbsp;Reset Password</td>
                                    <td>
                                        <input type="checkbox" name="ResetPassword">
                                    </td>
                                </tr>
                                <tr class="LightBandStyle">
                                    <td class="txtgreybold">&nbsp;Team</td>
                                    <td>
                                        <select name="Team" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                strSelected = "";
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:modifyUser()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
    <%
        }
    %>
</form>
</body>
</html>
