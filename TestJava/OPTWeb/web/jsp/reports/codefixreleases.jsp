<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../common/noCache.jsp" %>
<%
    ArrayList<String> arlCodeFixReleases = (ArrayList) request.getAttribute("CodeFixReleases");
    ArrayList<String> arlReleaseVersions = (ArrayList) request.getAttribute("ReleaseVersions");
    String strSelectedReleaseVersion = AppUtil.checkNull((String) request.getAttribute("SelectedReleaseVersion"));

    int intBugCount = 0;

    if (arlCodeFixReleases != null && !arlCodeFixReleases.isEmpty())
    {
        intBugCount = arlCodeFixReleases.size() / 12;
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>
    <script type="text/javascript" language="JavaScript">
        function CTtrackEnterKey(keyevent) {
            if (window.event)
                key = window.event.keyCode;

            else if (keyevent)
                key = keyevent.which;
            var keychar = "";
            keychar = String.fromCharCode(key);

            if (key == 13) {
                loadCodeFixReleases();
            }
        }


        function loadCodeFixReleases() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.ReleaseVersion.options.selectedIndex == 0) {
                    OPTDialog("Select Release Version", document.OPTForm.ReleaseVersion);
                    return;
                }
                document.OPTForm.action = "loadCodeFixReleases";
                frmReadSubmit();
            }
        }

        function viewTicketDetails(Refno, TicketId) {
            document.OPTForm.hidRefNo.value = Refno;
            document.OPTForm.hidTicketId.value = TicketId;
            showFullPageMask(true);
            MM_openBrWindow("loadClosedTicketDetails?ACCESS=RESTRICTED", 500, 1300);
        }
    </script>
</head>
<body onload="document.OPTForm.ReleaseVersion.focus()" onFocus="FocusModalWin()" onClick="FocusModalWin()"
      onUnLoad="CloseModalWin()">
<form name="OPTForm" method="post">
    <%@ include file="../common/banner.jsp" %>
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">

    <table width="99%" align="center">
        <tr>
            <td width="40%">
                <table width="100%" cellpadding=1 cellspacing=1 border=0 align="left" class="TableBorder1Pix">
                    <tr>
                        <td width="100%">
                            <table width="100%" align="left">
                                <tr>
                                    <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Select Release Version to
                                        View Planned Bugs
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%">
                            <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                                <tr>
                                    <td width="80%" class="txtgrey" valign="middle">
                                        <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                            <tr class="LightBandStyle">
                                                <td width="40%" class="txtgreybold">Release Version</td>
                                                <td width="60%">
                                                    <select name="ReleaseVersion" class="txtgrey" onkeypress="CTtrackEnterKey(event)">
                                                        <option value="">Select</option>
                                                        <%
                                                            String strSelected = "";
                                                            for (String strReleaseVer : arlReleaseVersions)
                                                            {
                                                                strSelected = "";
                                                                if (strReleaseVer.equalsIgnoreCase(strSelectedReleaseVersion))
                                                                {
                                                                    strSelected = "selected";
                                                                }
                                                        %>
                                                        <option value="<%= strReleaseVer %>" <%= strSelected %>><%= strReleaseVer %>
                                                        </option>
                                                        <%

                                                            }
                                                        %>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td width="20%" valign="middle">
                                        <table cellpadding=2 cellspacing=1 border=0 align="center" width="100%">
                                            <tr>
                                                <td width="100%" align="center" valign="middle">&nbsp;
                                                    <input type="button" class="myButton" value="Search"
                                                           onclick="JavaScript:loadCodeFixReleases()"
                                                           id="buttonId0"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="60%">&nbsp;</td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>

    <%
        if (strSelectedReleaseVersion.length() > 0)
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;List of Bugs (<%= intBugCount %>)
                        </td>
                    </tr>
                </table>
                <%
                    if (arlCodeFixReleases != null && !arlCodeFixReleases.isEmpty())
                    {
                %>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="3%" class="txtblackbold12">S.No</td>
                        <td width="15%" class="txtblackbold12">Assignee</td>
                        <td width="10%" class="txtblackbold12">Ticket ID</td>
                        <td width="34%" class="txtblackbold12">Ticket Description</td>
                        <td width="7%" class="txtblackbold12">Ticket Priority</td>
                        <td width="17%" class="txtblackbold12">Ticket Resolution</td>
                        <td width="7%" class="txtblackbold12">SLA End Date</td>
                        <td width="7%" class="txtblackbold12">Closed Date</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        int intSerialNo = 0;
                        for (int iCount = 0; iCount < arlCodeFixReleases.size(); iCount += 12)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                            if (arlCodeFixReleases.get(iCount + 3).equalsIgnoreCase(AppConstants.BUG))
                            {
                    %>
                    <tr class="<%= strBackGround %>">
                        <td class="txtgrey" align="right"><%= ++intSerialNo %>
                        </td>
                        <td class="txtgrey"><%= arlCodeFixReleases.get(iCount + 9) %>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCodeFixReleases.get(iCount + 1) %>', '<%= arlCodeFixReleases.get(iCount + 1) %>')"
                                class="link"><%= arlCodeFixReleases.get(iCount + 1) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:openJIRAWindow('<%= AppConstants.JIRAURL + arlCodeFixReleases.get(iCount + 1) %>', '<%= arlCodeFixReleases.get(iCount + 1) %>')"
                                class="link"><%= arlCodeFixReleases.get(iCount + 2) %>
                        </a>
                        </td>
                        <td class="txtgrey"><a
                                href="JavaScript:viewTicketDetails('<%= arlCodeFixReleases.get(iCount) %>','<%= arlCodeFixReleases.get(iCount + 1) %>')"
                                title="Ticket Details"><%= arlCodeFixReleases.get(iCount + 4) %>
                        </a>
                        </td>
                        <td class="txtgrey"><%= arlCodeFixReleases.get(iCount + 10) %>
                        </td>

                        <td class="txtgrey"><%= arlCodeFixReleases.get(iCount + 7) %>
                        </td>
                        <td class="txtgrey"><%= arlCodeFixReleases.get(iCount + 8) %>
                        </td>
                    </tr>
                    <%
                                intCounter++;
                            }
                        }
                    %>
                </table>
                <%
                }
                else
                {
                %>
                <%@ include file="../common/nodataavailable.jsp" %>
                <%
                    }
                %>
            </td>
        </tr>
    </table>
    <%
        }
    %>
    <%@ include file="../common/twospace.jsp" %>

</form>
</body>
</html>
