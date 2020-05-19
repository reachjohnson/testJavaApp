<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    ArrayList<String> arlOpenTicketIdDescAssigneeStatus = (ArrayList) request.getAttribute("OpenTicketIdDescAssigneeStatus");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function stopProgressTicket() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.commentsforstopprogress.value)) {
                    OPTDialog("Enter Reason for Stopping Progress", document.OPTForm.commentsforstopprogress);
                    return;
                }
                document.OPTForm.action = "stopProgressTicket";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.commentsforstopprogress.focus();
            document.OPTForm.hidRefNo.value = parent.opener.OPTForm.hidRefNo.value;
            document.OPTForm.hidTicketId.value = parent.opener.OPTForm.hidTicketId.value;
            var fromPage = "<%= strFromPage %>";

            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "TICKETONHOLD") {
                showFullPageMask(true);
                jAlert("Ticket Moved to On-Hold", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        parent.opener.showFullPageMask(false);
                        if (fromPage == "HomePage") {
                            parent.opener.OPTForm.action = "homePage";
                        }
                        else if (fromPage == "ModifyCurrentTickets") {
                            parent.opener.OPTForm.action = "loadModifyCurrentTickets";
                        }
                        parent.opener.OPTForm.submit();
                        window.close();
                    }
                });
            }
        }
    </script>
</head>
<body onload="JavaScript:displaymessage();">
<form name="OPTForm" method="post" action="" enctype="multipart/form-data">
    <input type="hidden" name="hidRefNo">
    <input type="hidden" name="hidTicketId">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <%@ include file="../common/popup_banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Comments - Reason for Stopping Progress
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Ticket Category</td>
                                    <td width="64%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Ticket Id</td>
                                    <td width="64%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(0) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Ticket Description</td>
                                    <td width="82%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(1) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Ticket Status</td>
                                    <td width="64%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(3) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Assignee</td>
                                    <td width="64%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(4) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <td width="100%" class="txtgrey">
                        <textarea name="commentsforstopprogress" rows="12" cols="105" maxlength="4990" class="txtgrey"></textarea>
                    </td>
                </table>

            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <table cellpadding=2 cellspacing=1 border=0 align="center">
        <tr>
            <td width="50%" align="right" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Save" onclick="JavaScript:stopProgressTicket()"
                       id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Close" onclick="JavaScript:frmClose()" id="buttonId1"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/spacebefore.jsp" %>
</form>
</body>
</html>