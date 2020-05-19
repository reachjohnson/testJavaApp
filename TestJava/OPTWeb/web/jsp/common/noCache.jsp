<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.opt.util.AppUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.opt.session.valobj.SessionInfo" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.opt.util.AppConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.opt.common.util.AppSettings" %>

<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<link href="styles/styleie.css?ts=<%= String.valueOf(new Date().getTime()) %>" rel="stylesheet" type="text/css"/>
<link href="styles/jquery.alerts.css?ts=<%= String.valueOf(new Date().getTime()) %>" rel="stylesheet" type="text/css"/>
<script language="javascript" src="/js/jquery.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/jquery.ui.draggable.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/jquery.alerts.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/common.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/popupwindow.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/cal3.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script language="javascript" src="/js/cal_conf2.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>

<div id="fullPageMask" class="accessAid">
    <div id="pageMask" style="height: 812px; width: 1135px;"></div>
</div>


<div id="spinnerContainer" style="position:fixed;top:50%;left:50%">
</div>
<script language="javascript" src="/js/spin.js?ts=<%= String.valueOf(new Date().getTime()) %>"></script>
<script type="text/javascript">
    var opts = {
        lines: 15, // The number of lines to draw
        length: 70, // The length of each line
        width: 25, // The line thickness
        radius: 75, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        color: '#000', // #rgb or #rrggbb
        speed: 0.75, // Rounds per second
        trail: 50, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: false, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: 'auto', // Top position relative to parent in px
        left: 'auto' // Left position relative to parent in px
    };
</script>
<%
    SessionInfo objSessionInfo = (SessionInfo) session.getAttribute(AppConstants.SESSION_DATA);

    String strUserId = "";
    String strUserName = "";
    boolean blnActive = false;
    boolean blnAdmin = false;
    boolean blnWriteAccess = false;
    String strTeam = "";
    String strLastLoginTime = "";
    if (objSessionInfo.getUserInfo() != null)
    {
        strUserId = objSessionInfo.getUserInfo().getUserId();
        strUserName = objSessionInfo.getUserInfo().getUserName();
        blnActive = objSessionInfo.getUserInfo().getActive();
        blnAdmin = objSessionInfo.getUserInfo().getAdmin();
        blnWriteAccess = objSessionInfo.getUserAccessRights().isWriteAccess();
        strTeam = objSessionInfo.getUserInfo().getTeam();
        strLastLoginTime = objSessionInfo.getUserInfo().getLastLogin();
    }
    String strCurrentDate = AppUtil.getCurrentDate();
    String strCurrentDay = AppUtil.getCurrentDay();
    String strCurrentMonth = AppUtil.getCurrentMonth();
    String strCurrentYear = AppUtil.getCurrentYear();
    String strCurrentHour = AppUtil.getCurrentHour();
    String strCurrentMinute = AppUtil.getCurrentMins();
    String strCurrent24Hours = AppUtil.getCurrentAmPm();
    String strCurrentDateDDMMYYYY = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MM_YYYY);

    String OOSLA_GREY_TEXT = "";
    String OOSLA_RED_TEXT = "";
    String OOSLA_DETAILS_RED_TEXT = "";
    String OOSLA_ORANGE_TEXT = "";

    if (AppSettings.isOooslaHighlight())
    {
        OOSLA_GREY_TEXT = "txtgrey";
        OOSLA_RED_TEXT = "txtredbold";
        OOSLA_DETAILS_RED_TEXT = "txtredbold";
        OOSLA_ORANGE_TEXT = "txtorangebold";
    }
    else
    {
        OOSLA_GREY_TEXT = "txtgrey";
        OOSLA_RED_TEXT = "txtgrey";
        OOSLA_DETAILS_RED_TEXT = "txtblue";
        OOSLA_ORANGE_TEXT = "txtgrey";
    }

%>
<script type="text/javascript" language="JavaScript">
    var SystemDate = "<%= strCurrentDateDDMMYYYY %>";
    var writeAccess = <%= blnWriteAccess %>;
    var RestrictedFromDate = "06/10/2014";
    var FromDateMessage = 'From Date can not be less than "06 Oct 2014"';
    var JavaScriptAlert = "<%= AppConstants.JAVA_SCRIPT_ALERT %>";
    var JavaScriptConf = "<%= AppConstants.JAVA_SCRIPT_CONFIRMATION %>";
    var JavaScriptInfo = "<%= AppConstants.JAVA_SCRIPT_INFORMATION %>";

    var OOSLA_HIGHLIGHT = <%= AppSettings.isOooslaHighlight() %>;
    var ETA_RESCHEDULE_MAX = "<%= AppSettings.getETARescheduleMax() %>";

</script>


