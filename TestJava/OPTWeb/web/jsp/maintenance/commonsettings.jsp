<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.util.AppUtil" %>
<%@ page errorPage="../common/errorpage.jsp" %>
<%@ include file="../common/noCache.jsp" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlCommonSettings = (ArrayList) request.getAttribute("CommonSettings");
    String strSelected = "";
    boolean blnDataAvailable = false;
    if (arlCommonSettings != null && !arlCommonSettings.isEmpty())
    {
        blnDataAvailable = true;
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function saveCommonSettings() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                document.OPTForm.action = "saveCommonSettings";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.ETAForBugs.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "MODIFIED") {
                OPTDialog("Settings Updated Successfully", document.OPTForm.ETAForBugs);
            }
        }

    </script>
</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Update Common Settings
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=2 border=0 align="left">
                                <tr>
                                    <td width="22%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0 style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Mandatory Updates</td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="80%" class="txtgreybold">&nbsp;ETA For Bugs</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(9).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="20%" align="left"><input type="checkbox" name="ETAForBugs" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA For Critical Tasks</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(10).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="ETAForCriticalTasks" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA For Major Tasks</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(11).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="ETAForMajorTasks" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA For Top Ageing Tasks</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(12).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="ETAForTopAgeingTasks" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Re-schedule For Crossed ETA</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(13).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="RescheduleForCrossedETA" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA For Internal Tickets</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(14).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="ETAForInternalTickets" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA For RQA Bugs</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(15).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="ETAForRQABugs" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Daily Status Update</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(18).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td align="left"><input type="checkbox" name="DailyStatus" <%= strSelected %> >
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0 style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Automated Email</td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="80%" class="txtgreybold">&nbsp;Database Backup</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(1).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="20%"><input type="checkbox" name="DBBackup" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETES Reminder</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(2).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="ETESReminder" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;High Priority Not Started</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(3).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="HighPriorityNotStarted" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;OOSLA Not Started</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(4).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="OOSLANotStarted" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;RQA ETA Reminder</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(5).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="RQAETAReminder" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Status Update Reminder</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(6).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="StatusUpdateReminder" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Open Tickets Priority</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(7).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="OpenTicketsPriority" <%= strSelected %> ></td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Tickets Progress Check</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(8).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td><input type="checkbox" name="TicketsProgressCheck" <%= strSelected %> ></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="top">
                                        <table width="100%" cellpadding=0 cellspacing=0 style="border: 1px solid #666666">
                                            <tr>
                                                <td width="100%" colspan="2" class="DarkBlueBandStyleText13">Other Settings</td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td width="70%" class="txtgreybold">&nbsp;OOSLA HighLight</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable && arlCommonSettings.get(0).equalsIgnoreCase("Y"))
                                                    {
                                                        strSelected = "checked";
                                                    }
                                                %>
                                                <td width="30%" align="left"><input type="checkbox" name="OOSLAHighLight" <%= strSelected %> >
                                                </td>
                                            </tr>
                                            <tr class="DarkGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;Top Ageing Threshold</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable)
                                                    {
                                                        strSelected = arlCommonSettings.get(16);
                                                    }
                                                %>
                                                <td align="left"><input type="text" size="3" maxlength="3" name="TopAgeingThreshold" value="<%= strSelected %>">
                                                </td>
                                            </tr>
                                            <tr class="LightGreyBandStyle">
                                                <td class="txtgreybold">&nbsp;ETA Reschedule Max</td>
                                                <%
                                                    strSelected = "";
                                                    if (blnDataAvailable)
                                                    {
                                                        strSelected = arlCommonSettings.get(17);
                                                    }
                                                %>
                                                <td align="left"><input type="text" size="3" maxlength="3" name="ETARescheduleMax" value="<%= strSelected %>">
                                                </td>
                                            </tr>

                                        </table>
                                    </td>
                                    <td width="18%" valign="top">&nbsp;</td>
                                    <td width="20%" valign="top">&nbsp;</td>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:saveCommonSettings()"
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
