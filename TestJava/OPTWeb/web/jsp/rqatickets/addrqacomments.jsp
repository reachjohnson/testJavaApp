<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    String strFromPage = AppUtil.checkNull((String) request.getAttribute("FromPage"));
    ArrayList<String> arlRQACommentsCategory = (ArrayList) request.getAttribute("RQACommentsCategory");
    ArrayList<String> arlOpenTicketIdDescAssigneeStatus = (ArrayList) request.getAttribute("OpenTicketIdDescAssigneeStatus");
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
                if (isEmpty(document.OPTForm.Comments.value)) {
                    OPTDialog("Enter Comments", document.OPTForm.Comments);
                    return;
                }

                document.OPTForm.action = "addRQAComments";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.Comments.focus();
            document.OPTForm.hidRefNo.value = parent.opener.OPTForm.hidRefNo.value;
            document.OPTForm.hidTicketId.value = parent.opener.OPTForm.hidTicketId.value;
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
                        else if (fromPage == "ModifyRQATickets") {
                            parent.opener.OPTForm.action = "loadModifyRQATickets";
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
    <%@ include file="../common/popup_banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Add Comments For RQA Bug
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Id</td>
                                    <td width="82%" class="txtblue"><%= arlOpenTicketIdDescAssigneeStatus.get(0) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Bug Description</td>
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
                                    <td width="36%" class="txtbluebold">Bug Status</td>
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
                                    <td width="18%" class="txtgreybold">Comments</td>
                                    <td width="82%">
                            <textarea name="Comments" rows="6" cols="120" maxlength="4990"
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
    <%@ include file="../common/spacebefore.jsp" %>

    <%
        if (arlRQACommentsCategory != null && !arlRQACommentsCategory.isEmpty())
        {
    %>
    <table width="99%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;RQA Bug Comments History
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBandStyle">
                        <td width="70%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Comments Date</td>
                        <td width="15%" class="txtblackbold12">Comments By</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlRQACommentsCategory.size(); iCount += 3)
                        {
                    %>
                    <tr class="LightBandStyle">
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount + 1) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlRQACommentsCategory.get(iCount + 2) %></pre>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
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