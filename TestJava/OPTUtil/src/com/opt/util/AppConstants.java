package com.opt.util;

/**
 * This class is refrenced for the static variable declared for use through out the application.
 */

public class AppConstants
{
   public static final String APPTITLE = "Online Payments Tickets";
   public static final String EMAIL_APPTITLE = "OPT";
   public static final String PROCESS_STARTED = "Process Started";
   public static final String PROCESS_FINISHED = "Process Finished";
   public static final String SUCCESS = "SUCCESS";
   public static final String SESSION_DATA = "SessionData";
   public static final String DB_JNDI = "java:/opt";
   public static final String DEFAULT_PASSWORD = "password";
   public static final String APPLICATIONURL = "http://L-MAA-00800543:1234/OPT";
   public static final String JIRAURL = "https://jira.testcompany.com/browse/";
   public static final String MAIL_SERVER_IP = "10.184.246.116";
   public static final String[] MAIL_SERVER_IPS = {"10.184.198.24", "10.184.246.116", "10.184.246.117", "10.185.198.24", "10.185.246.116", "10.185.246.117"};
   public static final String EMAIL_SUFFIX = "@testcompanycorp.com";
   public static final String EMAIL_FROM = "OnlinePaymentsTickets";
   public static final String MAILLOGINADDSUBJECT = "Your Credentials Added In " + APPTITLE + " Application";
   public static final String MAILPASSWORDRESETSUBJECT = "Your Password Has Been Reset In " + APPTITLE + " Application";
   public static final String CREDENTIALSACTIVESUBJECT = "Your Credentials Activated In " + APPTITLE + " Application";
   public static final String CREDENTIALSINACTIVESUBJECT = "Your Credentials Deactivated In " + APPTITLE + " Application";
   public static final String SYSTEMADMINS_ERROR = "mariajohnson";
   public static final String[] TEAM_DL = {"mariajohnson", "ppanigrahy"};
   public static final String LEAD_TEAM_DL = "mariajohnson@testcompanycorp.com, ppanigrahy@testcompanycorp.com";
   public static final String MSO_TEAM_DL = "DL-PP-PCE-MSO@testcompany.com";
   public static final String FEEDBACK_APPRECIATION = "Appreciation";
   public static final String APPRECIATION_EMAIL = "Appreciation Email";
   public static final String FEEDBACK_EMAIL = "Feedback Observation Email";

   public static final String APPRECIATION_MAIL_HEADER = "We appreciate you for the good work.";
   public static final String FEEDBACK_EMAIL_HEADER = "Please take care of the below feedback.";

   public static final String ERRORNOTIFYSUBJECT = "Error Notification";
   public static final String SPACE = " ";
   public static final String ACTION_TICKET_DETAILS = "Ticket Details";
   public static final String ACTION_UPDATE = "Update";
   public static final String ACTION_REASSIGN = "Re-Assign";
   public static final String ACTION_START_PROGRESS = "Start Progress";
   public static final String ACTION_STOP_PROGRESS = "Stop Progress";
   public static final String ACTION_DELETE_TICKET = "Delete";
   public static final String ACTION_CLOSE_TICKET = "Close";
   public static final String ACTION_ADD_COMMENTS = "Add Comments";

   public static final String[] TICKET_MODULE = {"EC", "WPS", "DG", "AP", "Acquiring", "Billing Product", "Mobile", "Others"};
   public static final String[] TICKET_PRIORITY = {"Critical", "Major", "Normal", "Minor", "Trivial"};
   public static final String[] TICKET_TYPE = {"Customer Impact", "Non-Customer Impact"};
   public static final String[] TICKET_RAISED_BY = {"SRE", "MTS", "QA", "MS Team", "Others"};

   public static final String TRIAGING_TEAM = "Triaging";
   public static final String MGMT_TEAM = "Management";
   public static final String FIXING_TEAM = "Fixing";
   public static final String FEATURE_TEAM = "Feature";
   public static final String QA_TEAM = "QA";
   public static final String RQA_TEAM = "RQA";
   public static final String TICKET_INFLOW_NEW_TASK = "New Task";
   public static final String TICKET_INFLOW_NEW_RQA_BUG = "New RQA Bug";
   public static final String RQA_TICKET_REOPEN = "RQA Bug Re-Opened";
   public static final String TICKET_INFLOW_EXISTING_BUG = "Existing Bug";
   public static final String TICKET_INFLOW_TASK_CONVERTED_BUG = "Task Converted To Bug";

   public static final String[] TASK_RESOLUTION = {"Enquiry Clarified", "Converted To Bug - Fix Expected by MS Team", "Converted To Bug - Analyzed and Moved To Other Team",
           "Enquiry Task - Moved To Other Team", "Task - Moved To Other Team", "Moved As Improvement", "Duplicate - Already Fixed", "Duplicate - Task Already Exists", "Duplicate - Bug Already Exists",
           "Fix Not Required By Product", "Invalid - Expected Behavior", "Invalid - Won't Fix", "Invalid - Not A Bug", "Not Reproducible"};

   public static final String[] BUG_RESOLUTION = {"Fix Expected by MS Team", "Analyzed and Moved To Other Team",
           "Moved As Improvement", "Duplicate - Already Fixed", "Duplicate - Bug Already Exists", "Code Fixed and Delivered",
           "Fix Not Required By Product", "Invalid - Expected Behavior", "Invalid - Won't Fix", "Invalid - Not A Bug", "Not Reproducible"};


   public static final String TASK_CONVERTED_BUG_FIX_EXPECTED = "Converted To Bug - Fix Expected by MS Team";
   public static final String BUG_FIX_EXPECTED = "Fix Expected by MS Team";

   public static final String TASK_CONVERTED_BUG_MOVED = "Converted To Bug - Analyzed and Moved To Other Team";
   public static final String BUG_MOVED = "Analyzed and Moved To Other Team";
   public static final String CODE_FIXED_DELIVERED = "Code Fixed and Delivered";

   public static final String ENQUIRY_TASK_MOVED = "Enquiry Task - Moved To Other Team";
   public static final String TASK_MOVED = "Task - Moved To Other Team";
   public static final String MOVED_TO_IMPROVEMENT = "Moved As Improvement";

   public static final String RESOLUTION_INVALID = "Invalid";
   public static final String RESOLUTION_DUPLICATE = "Duplicate";

   public static final String HYPHEN = "-";

   public static final String TASK = "Task";
   public static final String BUG = "Bug";
   public static final String OPEN = "Open";
   public static final String NOT_STARTED = "Not Started";
   public static final String IN_PROGRESS = "In Progress";
   public static final String ON_HOLD = "On Hold";
   public static final String COMPLETED = "Completed";

   public static final int TASK_SLA_DAYS = 4;
   public static final int BUG_SLA_DAYS = 4;
   public static final int EXISTING_BUG_SLA_DAYS = 8;

   public static final String JAVA_DATE_TIME_FORMAT = "dd/MMMM/yyyy hh:mm a";
   public static final String JAVA_DATE_FORMAT = "dd/MMMM/yyyy";

   public static final String JAVA_DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
   public static final String JAVA_DATE_FORMAT_MM_DD_YYYY = "MM-dd-yyyy";
   public static final String JAVA_DATE_FORMAT_DD_MMM_YYYY = "dd/MMM/yyyy";
   public static final String JAVA_DATE_FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy hh:mm a";

   public static final String JAVA_DATETIME_FORMAT_MYSQL = "yyyy-MM-dd HH:mm:ss";
   public static final String JAVA_DATE_FORMAT_MYSQL = "yyyy-MM-dd";

   public static final String JAVA_DAY_FORMAT = "dd";
   public static final String JAVA_MONTH_FORMAT = "MMMM";
   public static final String JAVA_YEAR_FORMAT = "yyyy";
   public static final String JAVA_HOUR_FORMAT = "hh";
   public static final String JAVA_MINS_FORMAT = "mm";
   public static final String JAVA_AMPM_FORMAT = "a";

   public static final String TICKET_NEW = "New";
   public static final String TICKET_REOPEN = "Re-Opened";
   public static final String TICKET_UPDATE = "Updated";
   public static final String CLOSED_TICKET_UPDATE = "Closed Ticket Updated";
   public static final String TICKET_ETA_UPDATE = "ETA Updated";
   public static final String TICKET_ETA_DATE = "ETA Date : ";
   public static final String TICKET_DELETED = "Deleted";
   public static final String TICKET_CLOSED = "Closed";
   public static final String TICKET_REASSIGNED = "ReAssigned";
   public static final String TICKET_STOP = "Stopped";
   public static final String TICKET_START = "Started";

   public static final String TICKET_NEW_COMMENTS = "New Ticket Created - Assigned To : ";
   public static final String TICKET_DELETE_COMMENTS = "This Ticket Deleted";
   public static final String TICKET_UPDATE_COMMENTS = "This Ticket Updated";
   public static final String TICKET_STOP_COMMENTS = "This Ticket Stopped";
   public static final String TICKET_STOP_COMMENTS_FOR_CLOSING = "This Ticket Stopped For Closing";
   public static final String TICKET_START_COMMENTS = "This Ticket Started";
   public static final String TICKET_CLOSE_COMMENTS = "This Ticket Closed";
   public static final String TICKET_REASSIGN_COMMENTS = "Ticket Re-Assigned to ";
   public static final String SESSION_EXPIRED = "SessionExpire";
   public static final String ALL_ASSIGNEES = "All Assignees";
   public static final String INVALID_ACCESS = "InvalidAccess";
   public static final String GOAHEAD = "GoAhead";
   public static final String ALL_STATUS = "All Status";
   public static final String ALL_PRIORITY = "All Priority";
   public static final String ALL_MODULE = "All Module";

   public static final String MANAGER_NAME = "Maria Johnson";
   public static final String MANAGER_ID = "mariajohnson";
   public static final String LEAVEPLANSSUBJECT = "Leave Plans - Notification";
   public static final String LEAVECANCELPLANSSUBJECT = "Leave Plans - Cancelled Notification";

   public static final String QA = "QA";
   public static final String EXCEL_FOLDER = "ExcelData";
   public static final String CHART_IMAGE_FOLDER = "ChartImages";

   public static final String ACQUIRING = "Acquiring";
   public static final String AP = "Adaptive Payments";
   public static final String BILLING_PRODUCT = "Billing Product";
   public static final String MS_TEAM = "MS Team";

   public static final String[] TICKET_SEVERITY = {"Very Complex", "Complex", "Medium", "Low", "Very Low"};

   public static final String[] BUG_ROOTCAUSE = {"Legacy Issue", "Recently Introduced By Feature", "Database Issue"};

   public static final String[] REOPEN_REASONS = {"Code Fix Rejected", "Ticket ReAssigned Back", "Ticket Re-opened", "Test Case Failure", "Assigned back for helping Other Domain Code Fix"};
   public static final String REOPEN_REASON_HELPING_OTHER_DOMAIN = "Assigned back for helping Other Domain Code Fix";

   public static final String[] NOT_STARTED_COMMENTS_CATEGORY = {"Not Started"};
   public static final String[] IN_PROGRESS_COMMENTS_CATEGORY = {"Analysis", "Reproducing Issue", "Testing", "Others"};
   public static final String[] ON_HOLD_COMMENTS_CATEGORY = {"Blocked", "On Hold", "Waiting For Response", "Others"};

   public static final String MAIL_LBL_BROWSER_USE = "Please Use Chrome or FireFox For Better UI Experience";
   public static final String MAIL_LBL_AUTO_MAIL = "This Is Auto Generated Email. Please Do Not Reply.";
   public static final String MAIL_LBL_HI = "Hi ";

   public static final String MAIL_LBL_JIRA_LINK_START = "<a href=\"https://jira.testcompany.com/browse/";
   public static final String MAIL_LBL_JIRA_LINK_CLOSE = "\">";
   public static final String MAIL_LBL_JIRA_LINK_END = "</a>";

   public static final String MAIL_LBL_NEXT_LINE = "<br/>";
   public static final String MAIL_LBL_DOT = ".";

   public static final String MAIL_LBL_FONT_START = "<p><font size =\"2.5\" face=\"verdana, arial, helvetica\">";
   public static final String MAIL_LBL_FONT_END = "</font></p>";

   public static final String[] RQA_TICKET_MODULE = {"EC", "WPS", "DG", "AP", "Acquiring", "Billing Product", "Mobile", "Others"};
   public static final String[] RQA_TICKET_PRIORITY = {"Blocker", "Critical", "Major", "Normal", "Minor ", "Trivial"};
   public static final String[] RQA_BUG_RAISED_BY = {"SRE", "MTS", "QA", "MS Team", "Others"};

   public static final String[] RQA_CYCLES = {"Cycle 1", "Cycle 2", "Cycle 3", "Cycle 4", "Thorttle", "LTS"};

   public static final String TASK_OTHER_TEAMS = "Gateway, Payments";

   public static final String EMAILQ_USER = "Johnson";
   public static final String EMAILQ_PASSWORD = "Password1!";
   public static final String EMAILQ_REMOTE_URL = "remote://L-MAA-00800543:4447";
   public static final String EMAILQ_NAME = "EmailQ";

   public static final String RESOL_ACTUAL_BUGS = "Actual Bugs";
   public static final String RESOL_OTHER_TEAM_BUGS = "Other Team Bugs";
   public static final String RESOL_IMPROVEMENTS = "Improvements";
   public static final String RESOL_INVALID = "Invalid";
   public static final String RESOL_DUPLICATE = "Duplicate";
   public static final String RESOL_OTHERS = "Others";
   public static final String RESOL_CODEFIX = "Code Fix";

   public static final String MAIL_ID_SEPERATOR = ",";

   public static final String TICKET_CAT_LABEL = "Ticket Category";
   public static final String TICKET_ID_LABEL = "Ticket Id";
   public static final String TICKET_DESC_LABEL = "Ticket Description";
   public static final String TICKET_PRIORITY_LABEL = "Priority";
   public static final String TICKET_COMMENTS_LABEL = "Comments";
   public static final String TICKET_REOPEN_REASON_LABEL = "Reopen Reason";
   public static final String TICKET_RESOL_LABEL = "Resolution";
   public static final String TICKET_ACTION_REQ_LABEL = "Action Required";
   public static final String TICKET_ETA_LABEL = "ETA";

   public static final String JAVA_SCRIPT_ALERT = APPTITLE + " - Alert";
   public static final String JAVA_SCRIPT_CONFIRMATION = APPTITLE + " - Confirmation";
   public static final String JAVA_SCRIPT_INFORMATION = APPTITLE + " - Information";
   public static final String LBL_CURRENT_STATUS = "CURRENT STATUS : ";
   public static final String LBL_NEXT_ACTION = "NEXT ACTION : ";
   public static final String CLOSING_TICKET_COMMENTS = "Closing Comments : ";
   public static final String OOSLA_REASON = "OOSLA Reason : ";

   public static final String TICKET_TYPE_LIVE = "LiveTicket";
   public static final String TICKET_TYPE_RQA = "RQATicket";
   public static final String TICKET_TYPE_INTERNAL = "InternalTicket";
   public static final String TICKET_TYPE_OTHERS = "Others";

}
