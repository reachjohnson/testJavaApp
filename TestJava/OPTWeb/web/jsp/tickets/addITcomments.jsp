<%@ include file="../common/noCache.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String strResult = AppUtil.checkNull((String) request.getAttribute("RESULT"));
    ArrayList<String> arlITCommentsHistory = (ArrayList) request.getAttribute("ITCommentsHistory");
    ArrayList<String> arlInternalTicketDetails = (ArrayList) request.getAttribute("InternalTicketDetails");
%>
<html>
<head>
    <title><%= AppConstants.APPTITLE %>
    </title>

    <script type="text/javascript" language="JavaScript">

        function addITcomments() {
            if (document.OPTForm.formsubmit.value == "submitted") {
                return;
            }
            else {
                if (isEmpty(document.OPTForm.Comments.value)) {
                    OPTDialog("Enter Comments", document.OPTForm.Comments);
                    return;
                }

                document.OPTForm.action = "addITComments";
                frmWriteSubmit();
            }
        }

        function displaymessage() {
            document.OPTForm.Comments.focus();
            var varResult = "<%= strResult.toUpperCase() %>";
            if (varResult == "COMMENTSADDED") {
                showFullPageMask(true);
                jAlert("Comments Updated Successfully", JavaScriptInfo, function (retval) {
                    showFullPageMask(false);
                    if (retval) {
                        parent.opener.showFullPageMask(false);
                        parent.opener.OPTForm.action = "homePage";
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
    <input type="hidden" name="hidRefNo" value="<%= arlInternalTicketDetails.get(0) %>">
    <input type="hidden" name="hidTicketId" value="<%= arlInternalTicketDetails.get(1) %>">
    <input type="hidden" name="hidTicketDesc" value="<%= arlInternalTicketDetails.get(2) %>">
    <input type="hidden" name="hidActionRequired" value="<%= arlInternalTicketDetails.get(3) %>">
    <input type="hidden" name="CreatedBy" value="<%= arlInternalTicketDetails.get(5) %>">
    <input type="hidden" name="CreatedByName" value="<%= arlInternalTicketDetails.get(6) %>">
    <input type="hidden" name="Assignee" value="<%= arlInternalTicketDetails.get(7) %>">
    <input type="hidden" name="AssigneeName" value="<%= arlInternalTicketDetails.get(8) %>">
    <%@ include file="../common/popup_banner.jsp" %>

    <table width="90%" cellpadding=1 cellspacing=1 border=0 align="center" class="TableBorder1Pix">
        <tr>
            <td width="100%" valign="top">
                <table width="100%" align="left">
                    <tr>
                        <td width="100%" class="DarkBlueBandStyleText14">&nbsp;Add Comments For Internal Ticket
                        </td>
                    </tr>
                </table>
                <table width="100%" cellpadding=0 cellspacing=0 border=0 align="center">
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Ticket ID</td>
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(1) %>
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
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(2) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Action Required</td>
                                    <td width="82%"><pre class="txtblue"><%= arlInternalTicketDetails.get(3) %></pre>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" class="txtgrey" colspan="2">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="18%" class="txtbluebold">Start Date</td>
                                    <td width="82%" class="txtblue"><%= arlInternalTicketDetails.get(4) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Created By</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(6) %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="txtgrey">
                            <table width="100%" cellpadding=2 cellspacing=1 border=0 align="center">
                                <tr class="LightBandStyle">
                                    <td width="36%" class="txtbluebold">Assignee</td>
                                    <td width="64%" class="txtblue"><%= arlInternalTicketDetails.get(8) %>
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
                <input type="button" class="myButton" value="Save" onclick="JavaScript:addITcomments()" id="buttonId0"/>
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
                    if (arlITCommentsHistory != null && !arlITCommentsHistory.isEmpty())
                    {
                %>

                <table width="100%" cellpadding=2 cellspacing=2 border=0>
                    <tr class="LightBlueBandStyle">
                        <td width="65%" class="txtblackbold12">Comments</td>
                        <td width="15%" class="txtblackbold12">Comments By</td>
                        <td width="20%" class="txtblackbold12">Comments Date</td>
                    </tr>
                    <%
                        for (int iCount = 0; iCount < arlITCommentsHistory.size(); iCount += 3)
                        {
                    %>
                    <tr class="LightBandStyle">
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount + 2) %></pre>
                        </td>
                        <td><pre class="txtgrey"><%= arlITCommentsHistory.get(iCount + 1) %></pre>
                        </td>
                    </tr>
                    <%
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