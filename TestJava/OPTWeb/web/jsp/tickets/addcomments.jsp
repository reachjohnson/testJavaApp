<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    ArrayList<String> arlCommentsHistory = (ArrayList) request.getAttribute("CommentsHistory");
    ArrayList<String> arlOpenTicketIdDescAssigneeStatus = (ArrayList) request.getAttribute("OpenTicketIdDescAssigneeStatus");
    String[] strCommentsCategory = null;
    String strTicketStatus = arlOpenTicketIdDescAssigneeStatus.get(3);
    if (strTicketStatus.equalsIgnoreCase(AppConstants.NOT_STARTED))
    {
        strCommentsCategory = AppConstants.NOT_STARTED_COMMENTS_CATEGORY;
    }
    else if (strTicketStatus.equalsIgnoreCase(AppConstants.ON_HOLD))
    {
        strCommentsCategory = AppConstants.ON_HOLD_COMMENTS_CATEGORY;
    }
    else if (strTicketStatus.equalsIgnoreCase(AppConstants.IN_PROGRESS))
    {
        strCommentsCategory = AppConstants.IN_PROGRESS_COMMENTS_CATEGORY;
    }
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function AddComments() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (document.OPTForm.CommentsCategory.options.selectedIndex == 0) {
                    OPTDialog("Select Comments Category", document.OPTForm.CommentsCategory);
                    return;
                }
                if (isEmpty(document.OPTForm.CurrentStatus.value)) {
                    OPTDialog("Enter Current Status", document.OPTForm.CurrentStatus);
                    return;
                }
                document.OPTForm.action = "addComments";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.CommentsCategory.focus();
            document.OPTForm.hidRefNo.value = parent.opener.OPTForm.hidRefNo.value;
            document.OPTForm.hidTicketId.value = parent.opener.OPTForm.hidTicketId.value;
            document.OPTForm.hidPreWorkingDay.value = parent.opener.OPTForm.hidPreWorkingDay.value;
            var fromPage = "<%= strFromPage %>";
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "COMMENTSADDED") {
                showFullPageMask(true);
                jAlert("Comments Updated Successfully", JavaScriptInfo, function (retval) {
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
    <input type="hidden" name="hidAssignee">
    <input type="hidden" name="hidFromPage" value="<%= strFromPage %>">
    <input type="hidden" name="hidPreWorkingDay">
    <%@ include file="../common/popup_banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Add Comments
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
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Comments Category</td>
                                    <td width="82%">
                                        <select name="CommentsCategory" class="txtgrey">
                                            <option value="">Select</option>
                                            <%
                                                for (String CommentsCategory : strCommentsCategory)
                                                {
                                            %>
                                            <option value="<%= CommentsCategory %>"><%= CommentsCategory %>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtgreybold">Current Status</td>
                                    <td width="82%">
                            <textarea name="CurrentStatus" rows="6" cols="120" maxlength="7490"
                                      class="txtgrey"></textarea>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:AddComments()" id="buttonId0"/>
            </td>
            <td width="50%" align="left" valign="middle">&nbsp;
                <input type="button" class="myButton" value="Close" onclick="JavaScript:frmClose()" id="buttonId1"/>
            </td>
        </tr>
    </table>
    <%@ include file="../common/onespace.jsp" %>
    <%@ include file="../common/line.jsp" %>
    <%@ include file="../common/onespace.jsp" %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Comments History
                        </td>
                    </tr>
                </table>
                <%
                    if (arlCommentsHistory != null && !arlCommentsHistory.isEmpty())
                    {
                %>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="15%" class="txtblackbold12">Comments Category</td>
                        <td width="55%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Comments Date</td>
                        <td width="15%" class="txtblackbold12">Comments By</td>
                    </tr>
                    <%
                        int intCounter = 0;
                        String strBackGround = "";

                        for (int iCount = 0; iCount < arlCommentsHistory.size(); iCount += 4)
                        {
                            if (intCounter % 2 == 0)
                            {
                                strBackGround = "LightGreyBandStyle";
                            }
                            else
                            {
                                strBackGround = "DarkGreyBandStyle";
                            }

                    %>
                    <tr class="<%= strBackGround %>">
                        <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlCommentsHistory.get(iCount + 3) %></pre>
                        </td>
                    </tr>
                    <%
                            intCounter++;
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
    <%@ include file="../common/onespace.jsp" %>

</form>
</body>
</html>