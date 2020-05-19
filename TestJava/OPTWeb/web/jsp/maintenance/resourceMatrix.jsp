<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlResourceMatrix = (ArrayList) request.getAttribute("ResourceMatrix");
    ArrayList<String> arlResourceCount = (ArrayList) request.getAttribute("ResourceCount");
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strResourceMatrixDate = AppUtil.checkNull((String) request.getAttribute("ResourceMatrixDate"));
    String strSelected = "";
    int intActiveResourcesCount = 0;

    int intLiveBugsCount = 0;
    int intRQACount = 0;
    int intFeatureCount = 0;
    int intQACount = 0;

    int intLiveBugsResourcecCount = 0;
    int intRQAResourcecCount = 0;
    int intFeatureResourcecCount = 0;
    int intQAResourcecCount = 0;

    int intCounter = 0;
    int intSerailNo = 0;

    String strBackGround = "";
    String strResourceId;
    String strResourceName;
    String strLiveBugs;
    String strComments;
    String strResourceTeam;
    String strActiveFlag;
    if (arlResourceMatrix != null && !arlResourceMatrix.isEmpty())
    {
        for (int iCount = 0; iCount < arlResourceMatrix.size(); iCount += 6)
        {
            strLiveBugs = arlResourceMatrix.get(iCount + 2);
            strResourceTeam = arlResourceMatrix.get(iCount + 4);
            strActiveFlag = arlResourceMatrix.get(iCount + 5);
            if (strActiveFlag.equalsIgnoreCase("Y"))
            {
                if ((strResourceTeam.equalsIgnoreCase(AppConstants.TRIAGING_TEAM) || strResourceTeam.equalsIgnoreCase(AppConstants.FIXING_TEAM)))
                {
                    intActiveResourcesCount++;
                    intLiveBugsResourcecCount++;
                    if (strLiveBugs.equalsIgnoreCase("Y"))
                    {
                        intLiveBugsCount++;
                    }
                }
                if (strResourceTeam.equalsIgnoreCase(AppConstants.RQA_TEAM))
                {
                    intActiveResourcesCount++;
                    intRQAResourcecCount++;
                    if (strLiveBugs.equalsIgnoreCase("Y"))
                    {
                        intLiveBugsCount++;
                    }
                    else
                    {
                        intRQACount++;
                    }
                }
                if (strResourceTeam.equalsIgnoreCase(AppConstants.QA_TEAM))
                {
                    intActiveResourcesCount++;
                    intQAResourcecCount++;
                    if (strLiveBugs.equalsIgnoreCase("Y"))
                    {
                        intLiveBugsCount++;
                    }
                    else
                    {
                        intQACount++;
                    }
                }
                if (strResourceTeam.equalsIgnoreCase(AppConstants.FEATURE_TEAM))
                {
                    intActiveResourcesCount++;
                    intFeatureResourcecCount++;
                    if (strLiveBugs.equalsIgnoreCase("Y"))
                    {
                        intLiveBugsCount++;
                    }
                    else
                    {
                        intFeatureCount++;
                    }
                }
            }
        }
    }

    if (arlResourceCount != null && !arlResourceCount.isEmpty())
    {
        intLiveBugsCount = Integer.parseInt(arlResourceCount.get(0));
        intRQACount = Integer.parseInt(arlResourceCount.get(1));
        intFeatureCount = Integer.parseInt(arlResourceCount.get(2));
        intQACount = Integer.parseInt(arlResourceCount.get(3));
    }

%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">
        var activeResourcesCount = <%= intActiveResourcesCount %>;
        var TriagingTeam = "<%= AppConstants.TRIAGING_TEAM %>";
        var FixingTeam = "<%= AppConstants.FIXING_TEAM %>";
        var RQATeam = "<%= AppConstants.RQA_TEAM %>";
        var FeatureTeam = "<%= AppConstants.FEATURE_TEAM %>";
        var QATeam = "<%= AppConstants.QA_TEAM %>";

        function saveResourceMatrix() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.LiveBugsCount.value)) {
                    OPTDialog("Enter Live Bugs Count", document.OPTForm.LiveBugsCount);
                    return;
                }
                if (isEmpty(document.OPTForm.RQACount.value)) {
                    OPTDialog("Enter RQA Team Count", document.OPTForm.RQACount);
                    return;
                }
                if (isEmpty(document.OPTForm.FeatureCount.value)) {
                    OPTDialog("Enter Feature Team Count", document.OPTForm.FeatureCount);
                    return;
                }
                if (isEmpty(document.OPTForm.QACount.value)) {
                    OPTDialog("Enter QA Team Count", document.OPTForm.QACount);
                    return;
                }

                var objLiveBugsFlag;
                var objComments;
                var objResourceName;
                for (var count = 0; count < activeResourcesCount; count++) {
                    objLiveBugsFlag = document.getElementById("LiveBugsFlag" + count);
                    objComments = document.getElementById("Comments" + count);
                    objResourceName = document.getElementById("ResourceName" + count);
                    if (!objLiveBugsFlag.checked && isEmpty(objComments.value)) {
                        OPTDialog('"' + objResourceName.value + '" is not into Live Bugs. Enter Comments', objComments);
                        return;
                    }
                }

                document.OPTForm.action = "saveResourceMatrix";
                frmWriteSubmit();
            }
        }

        function getAccessRights() {
            document.OPTForm.action = "loadResourceMatrix";
            frmReadSubmit();
        }

        function displaymessage() {
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "MODIFIED") {
                OPTDialog("Resource Matrix Updated Successfully", document.OPTForm.LiveBugsCount);
            }
        }

        function updateLiveBugsCount(objVal, counterval) {
            var LiveCount = document.OPTForm.LiveBugsCount.value;
            objComments = document.getElementById("Comments" + counterval);
            if (objVal.checked) {
                LiveCount++;
                objComments.value = "LiveBugs";
            }
            else {
                LiveCount--;
                objComments.value = "";
            }
            document.OPTForm.LiveBugsCount.value = LiveCount;
        }

        function updateRQACount(objVal, counterval) {
            var LiveCount = document.OPTForm.LiveBugsCount.value;
            var RQACount = document.OPTForm.RQACount.value;
            objComments = document.getElementById("Comments" + counterval);
            if (objVal.checked) {
                LiveCount++;
                RQACount--;
                objComments.value = "LiveBugs";
            }
            else {
                LiveCount--;
                RQACount++;
                objComments.value = "RQA";
            }
            document.OPTForm.LiveBugsCount.value = LiveCount;
            document.OPTForm.RQACount.value = RQACount;
        }

        function updateFeatureCount(objVal, counterval) {
            var LiveCount = document.OPTForm.LiveBugsCount.value;
            var FeatureCount = document.OPTForm.FeatureCount.value;
            objComments = document.getElementById("Comments" + counterval);
            if (objVal.checked) {
                LiveCount++;
                FeatureCount--;
                objComments.value = "LiveBugs";
            }
            else {
                LiveCount--;
                FeatureCount++;
                objComments.value = "SEPA Development";
            }
            document.OPTForm.LiveBugsCount.value = LiveCount;
            document.OPTForm.FeatureCount.value = FeatureCount;
        }

        function updateQACount(objVal, counterval) {
            var LiveCount = document.OPTForm.LiveBugsCount.value;
            var QACount = document.OPTForm.QACount.value;
            objComments = document.getElementById("Comments" + counterval);
            if (objVal.checked) {
                LiveCount++;
                QACount--;
                objComments.value = "LiveBugs";
            }
            else {
                LiveCount--;
                QACount++
                objComments.value = "Testing Effort in SEPA and RQA";
            }
            document.OPTForm.LiveBugsCount.value = LiveCount;
            document.OPTForm.QACount.value = QACount;
        }

    </script>
</head>
<body onload="JavaScript:displaymessage();" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="ResourceMatrixCount" value="<%= intActiveResourcesCount %>">

    <table width="99%" align="center">
        <tr>
            <td width="35%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Input Date to Update Resource Team Allocation
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="left">
                                <tr class="LightBandStyle">
                                    <td width="20%" class="txtgreybold">Select Date</td>
                                    <td width="60%">
                                        <input type="text" name="ResourceMatrixDate" size="10" maxlength="20" class="txtgrey"
                                               onblur="checkValidDateForObject(this)" value="<%= strResourceMatrixDate %>"
                                               onkeypress="CTtrackEnterKey(event)">
                                        <a href="javascript:showCal('Calendar21','dd/mm/yyyy');"><img src="images/cal.gif" width="20"
                                                                                                      height="13" border="0"
                                                                                                      alt="Pick a date"></a><span
                                            class="txtgrey">&nbsp;(DD/MM/YYYY)</span>

                                    </td>
                                    <td width="20%" class="txtgreybold">
                                        <input type="button" class="myButton" value="Search" onclick="JavaScript:getAccessRights()"
                                               id="buttonIdReopenSearch"/>
                                    </td>

                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="65%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (intActiveResourcesCount > 0)
        {
    %>
    <%@ include file="../common/line.jsp" %>
    <table width="65%" cellpadding=2 cellspacing=2 border=0 align="center">
        <tr class="LightBlueBandStyle">
            <td width="15%" class="txtblackbold12">Live Bugs Team</td>
            <td width="5%" class="txtblackbold12"><input type="text" name="LiveBugsCount" size="2" maxlength="2"
                                                         class="txtgrey" value="<%= intLiveBugsCount %>"></td>
            <td width="5%" bgcolor="#FFFFFF">&nbsp;</td>

            <td width="15%" class="txtblackbold12">RQA Team</td>
            <td width="5%" class="txtblackbold12"><input type="text" name="RQACount" size="2" maxlength="2"
                                                         class="txtgrey" value="<%= intRQACount %>"></td>
            <td width="5%" bgcolor="#FFFFFF">&nbsp;</td>

            <td width="15%" class="txtblackbold12">Feature Team</td>
            <td width="5%" class="txtblackbold12"><input type="text" name="FeatureCount" size="2" maxlength="2"
                                                         class="txtgrey" value="<%= intFeatureCount %>"></td>
            <td width="5%" bgcolor="#FFFFFF">&nbsp;</td>

            <td width="15%" class="txtblackbold12">QA Team</td>
            <td width="5%" class="txtblackbold12"><input type="text" name="QACount" size="2" maxlength="2"
                                                         class="txtgrey" value="<%= intQACount %>"></td>
            <td width="5%" bgcolor="#FFFFFF">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/line.jsp" %>
    <table width="100%" cellpadding=2 cellspacing=2 border=0>
        <tr>
            <td width="100%">&nbsp;</td>
        </tr>
    </table>

    <table width="98%" cellpadding=2 cellspacing=2 border=0 align="center">
        <tr>
            <td width="49%" valign="top">
                <table width="100%" cellpadding=2 cellspacing=2 style="border: 1px solid #666666">
                    <tr>
                        <td width="100%" colspan="4" class="DarkBlueBandStyleText13">&nbsp;Live Bugs Team</td>
                    </tr>
                    <%
                        if (intLiveBugsResourcecCount > 0)
                        {
                    %>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="37%" class="txtblackbold12">Resource Name</td>
                        <td width="10%" class="txtblackbold12" align="center">Live Bugs</td>
                        <td width="50%" class="txtblackbold12">Comments</td>
                    </tr>
                    <%
                        intCounter = 0;
                        intSerailNo = 0;
                        for (int iCount = 0; iCount < arlResourceMatrix.size(); iCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strResourceId = arlResourceMatrix.get(iCount);
                            strResourceName = arlResourceMatrix.get(iCount + 1);
                            strLiveBugs = arlResourceMatrix.get(iCount + 2);
                            strComments = arlResourceMatrix.get(iCount + 3);
                            strResourceTeam = arlResourceMatrix.get(iCount + 4);
                            strActiveFlag = arlResourceMatrix.get(iCount + 5);
                            if ((strResourceTeam.equalsIgnoreCase(AppConstants.TRIAGING_TEAM) || strResourceTeam.equalsIgnoreCase(AppConstants.FIXING_TEAM)) && strActiveFlag.equalsIgnoreCase("Y"))
                            {
                    %>
                    <tr class="<%= strBackGround%>">
                        <input type="hidden" name="ResourceId<%= intCounter %>" value="<%= strResourceId %>">
                        <input type="hidden" name="ResourceName<%= intCounter %>" id="ResourceName<%= intCounter %>"
                               value="<%= strResourceName %>">
                        <td class="txtgrey"><%= ++intSerailNo %>
                        </td>
                        <td class="txtgrey"><%= strResourceName %>
                        </td>
                        <%
                            strSelected = "";
                            if (strLiveBugs.equalsIgnoreCase("Y"))
                            {
                                strSelected = "checked";
                            }
                        %>
                        <td class="txtgrey" align="center"><input type="checkbox"
                                                                  name="LiveBugsFlag<%= intCounter %>"
                                                                  id="LiveBugsFlag<%= intCounter %>" <%= strSelected %>
                                                                  onclick="updateLiveBugsCount(this, <%= intCounter %>)">
                        </td>
                        <td class="txtgrey"><input type="text" name="Comments<%= intCounter %>"
                                                   id="Comments<%= intCounter %>"
                                                   size="50" maxlength="100"
                                                   class="txtgrey" value="<%= strComments %>"></td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                    <%
                    }
                    else
                    {
                    %>
                    <tr>
                        <td width="100%" class="txtgrey">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">No Data Available</td>
                    </tr>
                    <%
                        }
                    %>

                    <tr>
                        <td width="100%" colspan="4" class="DarkBlueBandStyleText13">&nbsp;RQA Team</td>
                    </tr>
                    <%
                        if (intRQAResourcecCount > 0)
                        {
                    %>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="37%" class="txtblackbold12">Resource Name</td>
                        <td width="10%" class="txtblackbold12" align="center">Live Bugs</td>
                        <td width="50%" class="txtblackbold12">Comments</td>
                    </tr>
                    <%
                        intSerailNo = 0;
                        for (int iCount = 0; iCount < arlResourceMatrix.size(); iCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strResourceId = arlResourceMatrix.get(iCount);
                            strResourceName = arlResourceMatrix.get(iCount + 1);
                            strLiveBugs = arlResourceMatrix.get(iCount + 2);
                            strComments = arlResourceMatrix.get(iCount + 3);
                            strResourceTeam = arlResourceMatrix.get(iCount + 4);
                            strActiveFlag = arlResourceMatrix.get(iCount + 5);
                            if (strResourceTeam.equalsIgnoreCase(AppConstants.RQA_TEAM) && strActiveFlag.equalsIgnoreCase("Y"))
                            {
                    %>
                    <tr class="<%= strBackGround%>">
                        <input type="hidden" name="ResourceId<%= intCounter %>" value="<%= strResourceId %>">
                        <input type="hidden" name="ResourceName<%= intCounter %>" id="ResourceName<%= intCounter %>"
                               value="<%= strResourceName %>">
                        <td class="txtgrey"><%= ++intSerailNo %>
                        </td>
                        <td class="txtgrey"><%= strResourceName %>
                        </td>
                        <%
                            strSelected = "";
                            if (strLiveBugs.equalsIgnoreCase("Y"))
                            {
                                strSelected = "checked";
                            }
                            if (strLiveBugs.length() == 0 && strComments.length() == 0)
                            {
                                strComments = "RQA";
                            }
                        %>
                        <td class="txtgrey" align="center"><input type="checkbox"
                                                                  name="LiveBugsFlag<%= intCounter %>"
                                                                  id="LiveBugsFlag<%= intCounter %>" <%= strSelected %>
                                                                  onclick="updateRQACount(this, <%= intCounter %>)">
                        </td>
                        <td class="txtgrey"><input type="text" name="Comments<%= intCounter %>"
                                                   id="Comments<%= intCounter %>"
                                                   size="50" maxlength="100"
                                                   class="txtgrey" value="<%= strComments %>"></td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                    <%
                    }
                    else
                    {
                    %>
                    <tr>
                        <td width="100%" class="txtgrey">&nbsp;</td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey">No Data Available</td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </td>
            <td width="2%" valign="top">&nbsp;
            </td>
            <td width="49%" valign="top">
                <table width="100%" cellpadding=2 cellspacing=2 style="border: 1px solid #666666">
                    <tr>
                        <td width="100%" colspan="4" class="DarkBlueBandStyleText13">&nbsp;Feature Team</td>
                    </tr>
                    <%
                        if (intFeatureResourcecCount > 0)
                        {
                    %>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="37%" class="txtblackbold12">Resource Name</td>
                        <td width="10%" class="txtblackbold12" align="center">Live Bugs</td>
                        <td width="50%" class="txtblackbold12">Comments</td>
                    </tr>
                    <%
                        intSerailNo = 0;
                        for (int iCount = 0; iCount < arlResourceMatrix.size(); iCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strResourceId = arlResourceMatrix.get(iCount);
                            strResourceName = arlResourceMatrix.get(iCount + 1);
                            strLiveBugs = arlResourceMatrix.get(iCount + 2);
                            strComments = arlResourceMatrix.get(iCount + 3);
                            strResourceTeam = arlResourceMatrix.get(iCount + 4);
                            strActiveFlag = arlResourceMatrix.get(iCount + 5);
                            if (strResourceTeam.equalsIgnoreCase(AppConstants.FEATURE_TEAM) && strActiveFlag.equalsIgnoreCase("Y"))
                            {
                    %>
                    <tr class="<%= strBackGround%>">
                        <input type="hidden" name="ResourceId<%= intCounter %>" value="<%= strResourceId %>">
                        <input type="hidden" name="ResourceName<%= intCounter %>" id="ResourceName<%= intCounter %>"
                               value="<%= strResourceName %>">
                        <td class="txtgrey"><%= ++intSerailNo %>
                        </td>
                        <td class="txtgrey"><%= strResourceName %>
                        </td>
                        <%
                            strSelected = "";
                            if (strLiveBugs.equalsIgnoreCase("Y"))
                            {
                                strSelected = "checked";
                            }
                            if (strLiveBugs.length() == 0 && strComments.length() == 0)
                            {
                                strComments = "SEPA Development";
                            }
                        %>
                        <td class="txtgrey" align="center"><input type="checkbox"
                                                                  name="LiveBugsFlag<%= intCounter %>"
                                                                  id="LiveBugsFlag<%= intCounter %>" <%= strSelected %>
                                                                  onclick="updateFeatureCount(this, <%= intCounter %>)">
                        </td>
                        <td class="txtgrey"><input type="text" name="Comments<%= intCounter %>"
                                                   id="Comments<%= intCounter %>"
                                                   size="50" maxlength="100"
                                                   class="txtgrey" value="<%= strComments %>"></td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                    <%
                    }
                    else
                    {
                    %>
                    <tr>
                        <td width="100%" class="txtgrey">&nbsp;</td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey">No Data Available</td>
                    </tr>
                    <%
                        }
                    %>

                    <tr>
                        <td width="100%" colspan="4" class="DarkBlueBandStyleText13">&nbsp;QA Team</td>
                    </tr>
                    <%
                        if (intQAResourcecCount > 0)
                        {
                    %>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="37%" class="txtblackbold12">Resource Name</td>
                        <td width="10%" class="txtblackbold12" align="center">Live Bugs</td>
                        <td width="50%" class="txtblackbold12">Comments</td>
                    </tr>
                    <%
                        intSerailNo = 0;
                        for (int iCount = 0; iCount < arlResourceMatrix.size(); iCount += 6)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            strResourceId = arlResourceMatrix.get(iCount);
                            strResourceName = arlResourceMatrix.get(iCount + 1);
                            strLiveBugs = arlResourceMatrix.get(iCount + 2);
                            strComments = arlResourceMatrix.get(iCount + 3);
                            strResourceTeam = arlResourceMatrix.get(iCount + 4);
                            strActiveFlag = arlResourceMatrix.get(iCount + 5);
                            if (strResourceTeam.equalsIgnoreCase(AppConstants.QA_TEAM) && strActiveFlag.equalsIgnoreCase("Y"))
                            {
                    %>
                    <tr class="<%= strBackGround%>">
                        <input type="hidden" name="ResourceId<%= intCounter %>" value="<%= strResourceId %>">
                        <input type="hidden" name="ResourceName<%= intCounter %>" id="ResourceName<%= intCounter %>"
                               value="<%= strResourceName %>">
                        <td class="txtgrey"><%= ++intSerailNo %>
                        </td>
                        <td class="txtgrey"><%= strResourceName %>
                        </td>
                        <%
                            strSelected = "";
                            if (strLiveBugs.equalsIgnoreCase("Y"))
                            {
                                strSelected = "checked";
                            }
                            if (strLiveBugs.length() == 0 && strComments.length() == 0)
                            {
                                strComments = "Testing Effort";
                            }
                        %>
                        <td class="txtgrey" align="center"><input type="checkbox"
                                                                  name="LiveBugsFlag<%= intCounter %>"
                                                                  id="LiveBugsFlag<%= intCounter %>" <%= strSelected %>
                                                                  onclick="updateQACount(this, <%= intCounter %>)">
                        </td>
                        <td class="txtgrey"><input type="text" name="Comments<%= intCounter %>"
                                                   id="Comments<%= intCounter %>"
                                                   size="50" maxlength="100"
                                                   class="txtgrey" value="<%= strComments %>"></td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                    <%
                    }
                    else
                    {
                    %>
                    <tr>
                        <td width="100%" class="txtgrey">&nbsp;</td>
                    </tr>

                    <tr>
                        <td width="100%" class="txtgrey">No Data Available</td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spaceafter.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveResourceMatrix()"
                       id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%
    }
    else
    {
    %>
    <table width="90%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td width="100%" class="txtgrey">&nbsp;</td>
        </tr>

        <tr>
            <td width="100%" class="txtgrey">No Data Available</td>
        </tr>
    </table>
    <%
        }
    %>
    <%@ include file="../common/spacebefore.jsp" %>
    <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
</form>
</body>
</html>
