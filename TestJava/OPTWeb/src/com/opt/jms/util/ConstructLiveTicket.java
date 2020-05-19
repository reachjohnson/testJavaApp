package com.opt.jms.util;

import com.opt.base.dao.BaseDAO;
import com.opt.exception.TaskException;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.tickets.util.TicketHistory;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

public class ConstructLiveTicket
{
   Logger objLogger = Logger.getLogger(ConstructLiveTicket.class.getName());

   public String ConstructLiveTicket(String strRefno, String strTicketId) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strLiveTicketBody = "<style type=\"text/css\">\n" +
              "pre {\n" +
              "    white-space: pre-wrap;\n" +
              "    white-space: -moz-pre-wrap;\n" +
              "    white-space: -pre-wrap;\n" +
              "    white-space: -o-pre-wrap;\n" +
              "    word-wrap: break-word;\n" +
              "    -ms-word-break: break-all;\n" +
              "    word-break: break-all;\n" +
              "    word-break: break-word;\n" +
              "    -webkit-hyphens: auto;\n" +
              "    -moz-hyphens: auto;\n" +
              "    -ms-hyphens: auto;\n" +
              "    hyphens: auto;\n" +
              "}\n" +
              ".txtblue {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    color: #003399;\n" +
              "}\n"+
              ".TableBorder1Pix {\n" +
              "    border: 1px solid #666666\n" +
              "}\n"+
              ".DarkBlueBandStyleText14 {\n" +
              "    background-color: #0061a7;\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 14px;\n" +
              "    font-weight: bold;\n" +
              "    color: #FFFFFF;\n" +
              "}\n"+
              ".txtblue {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    color: #003399;\n" +
              "}\n" +
              "\n" +
              ".txtbluebold {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    font-weight: bold;\n" +
              "    color: #003399;\n" +
              "}\n"+
              ".txtgrey {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    color: #666666;\n" +
              "}\n"+
              ".LightBandStyle {\n" +
              "    background-color: #f2f2f2;\n" +
              "}\n"+
              ".txtgreen15 {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 15px;\n" +
              "    color: #009900;\n" +
              "}\n"+
              ".LightBlueBandStyle {\n" +
              "    background-color: #B0E0E6;\n" +
              "}\n"+
              ".txtblackbold12 {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    font-weight: bold;\n" +
              "    color: black;\n" +
              "}\n"+
              ".LightGreyBandStyle {\n" +
              "    background-color: #f2f2f2;\n" +
              "}\n" +
              "\n" +
              ".DarkGreyBandStyle {\n" +
              "    background-color: #DCDCDC;\n" +
              "}\n"+
              ".txtgrey12 {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 12px;\n" +
              "    font-weight: bold;\n" +
              "    color: #666666;\n" +
              "}\n"+
              ".txtgreenbold15 {\n" +
              "    font-family: verdana, arial, helvetica;\n" +
              "    font-size: 15px;\n" +
              "    font-weight: bold;\n" +
              "    color: #009900;\n" +
              "}\n"+
              "</style>";

      try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
      {
         TicketsDAO objTicketsDAO = new TicketsDAO();
         ArrayList<String> arlViewTicketDetails = objTicketsDAO.getTicketDetailsForView(strRefno, strTicketId, objDBConnection);
         TicketHistory objTicketHistory = new TicketHistory();
         ArrayList<String> arlTicketHistory = objTicketHistory.getTicketHistory(strRefno, strTicketId, objDBConnection);
         String strTotalDaysSpent = objTicketHistory.getTotalDaysSpent();
         ArrayList<String> arlTicketActivityHistory = objTicketsDAO.getTicketActivityHistory(strRefno, strTicketId, objDBConnection);
         ArrayList<String> arlCommentsHistory = objTicketsDAO.getCommentsHistory(strRefno, strTicketId, objDBConnection);

         strLiveTicketBody += "    <table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" valign=\"top\">\n" +
                 "                <table width=\"100%\" align=\"left\">\n" +
                 "                    <tr>\n" +
                 "                        <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Ticket Details\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n";
         strLiveTicketBody += "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"7%\" class=\"txtgreenbold15\">Status</td>\n" +
                 "                        <td width=\"9%\" class=\"txtgreen15\">" + arlViewTicketDetails.get(16) + "\n" +
                 "                        </td>\n" +
                 "                        <td width=\"84%\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "    </table>\n" +
                 "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Category</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(1) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Id</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlViewTicketDetails.get(2) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlViewTicketDetails.get(2) + AppConstants.MAIL_LBL_JIRA_LINK_END + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" colspan=\"2\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"18%\" class=\"txtbluebold\">Ticket Description</td>\n" +
                 "                        <td width=\"82%\" class=\"txtblue\">" + AppConstants.MAIL_LBL_JIRA_LINK_START + arlViewTicketDetails.get(2) + AppConstants.MAIL_LBL_JIRA_LINK_CLOSE + arlViewTicketDetails.get(3) + AppConstants.MAIL_LBL_JIRA_LINK_END + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Priority</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(4) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Type</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(5) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Module</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(6) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Raised By</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(7) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n";

         if (arlViewTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
         {
            strLiveTicketBody += "        \n" +
                    "        <tr>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"36%\" class=\"txtbluebold\">Ticket Resolution</td>\n" +
                    "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(11) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"36%\" class=\"txtbluebold\">Moved Team</td>\n" +
                    "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(12) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"18%\" class=\"txtbluebold\">Ticket Root Cause</td>\n" +
                    "                        <td width=\"82%\" class=\"txtblue\">" + arlViewTicketDetails.get(13) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"18%\" class=\"txtbluebold\">Comments</td>\n" +
                    "                        <td width=\"82%\" class=\"txtblue\"><pre class=\"txtblue\">" + arlViewTicketDetails.get(14) + "</pre>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "\n";
         }

         strLiveTicketBody += "        \n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Received Date</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(8) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Created Date</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(18) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">SLA End Date</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(9) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">ETA</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(19) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "\n";
         if (arlViewTicketDetails.get(16).equalsIgnoreCase(AppConstants.COMPLETED))
         {
            strLiveTicketBody += "        \n" +
                    "        <tr>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"36%\" class=\"txtbluebold\">Actual End Date</td>\n" +
                    "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(15) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"36%\" class=\"txtbluebold\">Release Version</td>\n" +
                    "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(21) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n";
         }
         else
         {

            strLiveTicketBody += "        <tr>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"36%\" class=\"txtbluebold\">Ageing</td>\n" +
                    "                        <td width=\"64%\" class=\"txtblue\">" + arlViewTicketDetails.get(20) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "            <td width=\"50%\" class=\"txtgrey\">&nbsp;</td>\n" +
                    "        </tr>\n";

         }

         strLiveTicketBody += "\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"18%\" class=\"txtbluebold\">Assignee</td>\n" +
                 "                        <td width=\"82%\" class=\"txtblue\">" + arlViewTicketDetails.get(17) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "    </table>\n";
         strLiveTicketBody += "            </td>" +
                 "        </tr>" +
                 "      </table>" +
                 "<br/><br/>";


         if (arlTicketHistory != null && !arlTicketHistory.isEmpty())
         {
            strLiveTicketBody += "<table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                    "    <tr>\n" +
                    "        <td width=\"100%\" valign=\"top\">\n" +
                    "            <table width=\"100%\" align=\"left\">\n" +
                    "                <tr>\n" +
                    "                    <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Ticket Progress History\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            \n";
            if (arlTicketHistory != null && !arlTicketHistory.isEmpty())
            {
               strLiveTicketBody += "            \n" +
                       "\n" +
                       "            <table width=\"100%\" cellpadding=1 cellspacing=2 border=0>\n" +
                       "                <tr class=\"LightBandStyle\">\n" +
                       "                    <td width=\"20%\" class=\"txtbluebold\">Total No.Of Days Spent :</td>\n" +
                       "                    <td width=\"5%\" class=\"txtblue\">" + strTotalDaysSpent + "\n" +
                       "                    </td>\n" +
                       "                    <td width=\"75%\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td>\n" +
                       "                </tr>\n" +
                       "            </table>\n" +
                       "\n" +
                       "            <table width=\"100%\" cellpadding=2 cellspacing=2 border=0\">\n" +
                       "                <tr class=\"LightBlueBandStyle\">\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Start Date</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">End Date</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Assignee</td>\n" +
                       "                    <td width=\"45%\" class=\"txtblackbold12\">Comments</td>\n" +
                       "                    <td width=\"5%\" class=\"txtblackbold12\">Hours</td>\n" +
                       "                    <td width=\"5%\" class=\"txtblackbold12\">Minutes</td>\n" +
                       "                </tr>\n";
               int intCounter = 0;
               String strBackGround = "";
               for (int iCount = 0; iCount < arlTicketHistory.size(); iCount += 6)
               {
                  if (intCounter % 2 == 0)
                  {
                     strBackGround = "LightGreyBandStyle";
                  }
                  else
                  {
                     strBackGround = "DarkGreyBandStyle";
                  }
                  strLiveTicketBody += "                \n" +
                          "                <tr class=\"" + strBackGround + "\">\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount + 1) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount + 2) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount + 3) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount + 4) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketHistory.get(iCount + 5) + "</pre>\n" +
                          "                    </td>\n" +
                          "                </tr>\n" +
                          "               \n";
                  intCounter++;
               }
               strLiveTicketBody += "                \n" +
                       "            </table>\n" +
                       "            \n";
            }
            else
            {
               strLiveTicketBody += "            <table width=\"100%\">\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td align=\"left\" class=\"txtgrey12\">&nbsp;No Data Available</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "</table>\n";

            }
            strLiveTicketBody += "            </td>" +
                    "        </tr>" +
                    "      </table>";

            strLiveTicketBody += "<br/><br/>";
         }

         if (arlCommentsHistory != null && !arlCommentsHistory.isEmpty())
         {
            strLiveTicketBody += "<table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                    "    <tr>\n" +
                    "        <td width=\"100%\" valign=\"top\">\n" +
                    "            <table width=\"100%\" align=\"left\">\n" +
                    "                <tr>\n" +
                    "                    <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Comments History\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            \n";
            if (arlCommentsHistory != null && !arlCommentsHistory.isEmpty())
            {
               strLiveTicketBody += "            \n" +
                       "\n" +
                       "            <table width=\"100%\" cellpadding=2 cellspacing=2 border=0\">\n" +
                       "                <tr class=\"LightBlueBandStyle\">\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Comments Category</td>\n" +
                       "                    <td width=\"55%\" class=\"txtblackbold12\">Comments</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Comments Date</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Comments By</td>\n" +
                       "                </tr>\n" +
                       "                \n";
               int intCounter = 0;
               String strBackGround = "";
               String strComments;
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
                  strComments = arlCommentsHistory.get(iCount + 1);
                  strComments.replace("\n", "<br/>");
                  strLiveTicketBody += "                \n" +
                          "                <tr class=\"" + strBackGround + "\">\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlCommentsHistory.get(iCount) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlCommentsHistory.get(iCount + 1) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlCommentsHistory.get(iCount + 2) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlCommentsHistory.get(iCount + 3) + "</pre>\n" +
                          "                    </td>\n" +
                          "                </tr>\n" +
                          "                \n";
                          intCounter++;
               }

               strLiveTicketBody += "            </table>\n" +
                       "            \n";
            }
            else
            {
               strLiveTicketBody += "            <table width=\"100%\">\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td align=\"left\" class=\"txtgrey12\">&nbsp;No Data Available</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "</table>\n";

            }
            strLiveTicketBody += "            </td>" +
                    "        </tr>" +
                    "      </table>";

            strLiveTicketBody += "<br/><br/>";
         }

         if (arlTicketActivityHistory != null && !arlTicketActivityHistory.isEmpty())
         {
            strLiveTicketBody += "<table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                    "    <tr>\n" +
                    "        <td width=\"100%\" valign=\"top\">\n" +
                    "            <table width=\"100%\" align=\"left\">\n" +
                    "                <tr>\n" +
                    "                    <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Ticket Activity History\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            \n";
            if (arlTicketActivityHistory != null && !arlTicketActivityHistory.isEmpty())
            {
               strLiveTicketBody += "            \n" +
                       "            <table width=\"100%\" cellpadding=2 cellspacing=2 border=0\">\n" +
                       "                <tr class=\"LightBlueBandStyle\">\n" +
                       "                    <td width=\"10%\" class=\"txtblackbold12\">Activity Type</td>\n" +
                       "                    <td width=\"55%\" class=\"txtblackbold12\">Comments</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Activity Date</td>\n" +
                       "                    <td width=\"20%\" class=\"txtblackbold12\">Activity By</td>\n" +
                       "                </tr>\n";
               int intCounter = 0;
               String strBackGround = "";
               for (int iCount = 0; iCount < arlTicketActivityHistory.size(); iCount += 4)
               {
                  if (intCounter % 2 == 0)
                  {
                     strBackGround = "LightGreyBandStyle";
                  }
                  else
                  {
                     strBackGround = "DarkGreyBandStyle";
                  }
                  strLiveTicketBody += "                \n" +
                          "                <tr class=\"" + strBackGround + "\">\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketActivityHistory.get(iCount) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketActivityHistory.get(iCount + 1) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketActivityHistory.get(iCount + 2) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlTicketActivityHistory.get(iCount + 3) + "</pre>\n" +
                          "                    </td>\n" +
                          "                </tr>\n" +
                          "                \n";
                  intCounter++;
               }
               strLiveTicketBody += "                \n" +
                       "            </table>\n" +
                       "            \n";
            }
            else
            {
               strLiveTicketBody += "            <table width=\"100%\">\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td align=\"left\" class=\"txtgrey12\">&nbsp;No Data Available</td>\n" +
                       "    </tr>\n" +
                       "    <tr>\n" +
                       "        <td class=\"txtgrey12\">&nbsp;</td>\n" +
                       "    </tr>\n" +
                       "</table>\n";

            }
            strLiveTicketBody += "            \n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "</table>\n";

            strLiveTicketBody += "<br/><br/>";
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strLiveTicketBody;
   }
}
