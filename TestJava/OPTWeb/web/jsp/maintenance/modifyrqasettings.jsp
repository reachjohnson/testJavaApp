<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlRQASettings = (ArrayList) request.getAttribute("RQASettings");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function updateRQASettings() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                document.OPTForm.action = "updateRQASettings";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.CurrentRQAPhase.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "UPDATED") {
                OPTDialog("RQA Settings Updated successfully");
            }
        }

    </script>
</head>
<body onload="displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update RQA Settings
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="center">
                                <tr>
                                    <td class="LightBandStyle" width="65%" class="txtgreybold">&nbsp;<span
                                            class="txtgreybold">Current RQA Phase</span></td>
                                    <td class="LightBandStyle" width="35%"><input type="text" name="CurrentRQAPhase"
                                                                                  size="10" maxlength="10"
                                                                                  class="txtgrey"
                                                                                  value="<%= arlRQASettings.get(0) %>">
                                        <span class="txtgrey">&nbsp;(###.#)</span>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Auto Email Report</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <%
                                            String strChecked = "";
                                            if (arlRQASettings.get(1).equalsIgnoreCase("Y"))
                                            {
                                                strChecked = "checked";
                                            }
                                        %>
                                        <input type="radio" name="AutoEmail" value="Y" <%= strChecked %>><span
                                            class="txtgrey">Yes</span>&nbsp;
                                        <%
                                            strChecked = "";
                                            if (arlRQASettings.get(1).equalsIgnoreCase("N"))
                                            {
                                                strChecked = "checked";
                                            }
                                        %>

                                        <input type="radio" name="AutoEmail" value="N" <%= strChecked%>><span
                                            class="txtgrey">No</span>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Auto Email CutOff Time</span>
                                    </td>
                                    <td class="LightBandStyle"><input type="text" name="EmailCutOffTime" size="10"
                                                                      maxlength="10"
                                                                      class="txtgrey"
                                                                      value="<%= arlRQASettings.get(2) %>">
                                        <span class="txtgrey">&nbsp;(HH:MM AM)</span>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Email To Ids</span>
                                    </td>
                                    <td class="LightBandStyle">
                <textarea name="EmailToIds" rows="4" cols="120" maxlength="4990"
                          class="txtgrey"><%= arlRQASettings.get(3) %></textarea>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Email CC Ids</span>
                                    </td>
                                    <td class="LightBandStyle">
                <textarea name="EmailCCIds" rows="4" cols="120" maxlength="4990"
                          class="txtgrey"><%= arlRQASettings.get(4) %></textarea>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Email Subject</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="EmailSubject" size="100" maxlength="200"
                                               class="txtgrey"
                                               value="<%= arlRQASettings.get(5) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Email Header</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="EmailHeader" size="100" maxlength="200" class="txtgrey"
                                               value="<%= arlRQASettings.get(6) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Bug Resolution Triaged</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="BugResolTriaged" size="100" maxlength="100"
                                               class="txtgrey"
                                               value="<%= arlRQASettings.get(7) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Bug Resolution Legacy Bug Fixed</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="BugResolLegacyBugFixed" size="100" maxlength="100"
                                               class="txtgrey"
                                               value="<%= arlRQASettings.get(8) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Bug Resolution Feature Code Impact Bug Fixed</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="BugResolFeatCodeImpactFixed" size="100" maxlength="100"
                                               class="txtgrey"
                                               value="<%= arlRQASettings.get(9) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">Bug Resolution Non Feature Code Impact Bug Fixed</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="BugResolNonFeatCodeImpactFixed" size="100"
                                               maxlength="100" class="txtgrey"
                                               value="<%= arlRQASettings.get(10) %>">
                                    </td>
                                </tr>

                                <tr>
                                    <td class="LightBandStyle" class="txtgreybold">&nbsp;<span class="txtgreybold">RQA POC Mail Ids</span>
                                    </td>
                                    <td class="LightBandStyle">
                                        <input type="text" name="RQAPOCMailIds" size="50" maxlength="50" class="txtgrey"
                                               value="<%= arlRQASettings.get(11) %>">
                                        <span class="txtgrey">&nbsp;(Mail Id/Mail Id)</span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:updateRQASettings()"
                       id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" id="buttonId1" class="myButton" value="Reset" onclick="JavaScript:frmCancel()"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
</form>
</body>
</html>
