package com.opt.jms.util;

import com.opt.base.dao.BaseDAO;
import com.opt.exception.TaskException;
import com.opt.tickets.dao.RQATicketsDAO;
import com.opt.tickets.dao.TicketsDAO;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

public class ConstructInternalTicket
{
   Logger objLogger = Logger.getLogger(ConstructInternalTicket.class.getName());

   public String ConstructInternalTicket(String strRefno, String strTicketId) throws Exception
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
              "</style>";

      try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
      {
         TicketsDAO objTicketsDAO = new TicketsDAO();
         ArrayList<String> arlITCommentsHistory = objTicketsDAO.getITCommentsHistory(strRefno, objDBConnection);
         ArrayList<String> arlInternalTicketDetails = objTicketsDAO.getInternalTaskForUpdate(strRefno, strTicketId, objDBConnection);

         strLiveTicketBody += "    <table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" valign=\"top\">\n" +
                 "                <table width=\"100%\" align=\"left\">\n" +
                 "                    <tr>\n" +
                 "                        <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Internal Ticket Details\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n";
         strLiveTicketBody += "    <table width=\"100%\" cellpadding=0 cellspacing=0 border=0 align=\"center\">\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"18%\" class=\"txtbluebold\">Ticket ID</td>\n" +
                 "                        <td width=\"82%\" class=\"txtblue\">" + arlInternalTicketDetails.get(1) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"18%\" class=\"txtbluebold\">Ticket Description</td>\n" +
                 "                        <td width=\"82%\" class=\"txtblue\"><pre class=\"txtblue\">" + arlInternalTicketDetails.get(2) + "</pre>\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        <tr>\n" +
                 "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"18%\" class=\"txtbluebold\">Action Required</td>\n" +
                 "                        <td width=\"82%\" class=\"txtblue\"><pre class=\"txtblue\">" + arlInternalTicketDetails.get(3) + "</pre>\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Start Date</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlInternalTicketDetails.get(4) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">ETA</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlInternalTicketDetails.get(9) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        \n";
         if (arlInternalTicketDetails.get(11).equalsIgnoreCase(AppConstants.COMPLETED))
         {
            strLiveTicketBody += "        \n" +
                    "        <tr>\n" +
                    "            <td width=\"100%\" class=\"txtgrey\" colspan=\"2\">\n" +
                    "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                    "                    <tr class=\"LightBandStyle\">\n" +
                    "                        <td width=\"18%\" class=\"txtbluebold\">End Date</td>\n" +
                    "                        <td width=\"82%\" class=\"txtblue\">" + arlInternalTicketDetails.get(10) + "\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        \n";
         }
         strLiveTicketBody += "        \n" +
                 "        <tr>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Created By</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlInternalTicketDetails.get(6) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "            <td width=\"50%\" class=\"txtgrey\">\n" +
                 "                <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
                 "                    <tr class=\"LightBandStyle\">\n" +
                 "                        <td width=\"36%\" class=\"txtbluebold\">Assignee</td>\n" +
                 "                        <td width=\"64%\" class=\"txtblue\">" + arlInternalTicketDetails.get(8) + "\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "    </table>\n";


         strLiveTicketBody += "            </td>" +
                 "        </tr>" +
                 "      </table>";

         strLiveTicketBody += "<br/><br/>";

         if (arlITCommentsHistory != null && !arlITCommentsHistory.isEmpty())
         {
            strLiveTicketBody += "<table width=\"100%\" cellpadding=1 cellspacing=1 border=0 align=\"center\" class=\"TableBorder1Pix\">\n" +
                    "    <tr>\n" +
                    "        <td width=\"100%\" valign=\"top\">\n" +
                    "            <table width=\"100%\" align=\"left\">\n" +
                    "                <tr>\n" +
                    "                    <td width=\"100%\" class=\"DarkBlueBandStyleText14\">&nbsp;Internal Ticket Comments History\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "            \n";
            if (arlITCommentsHistory != null && !arlITCommentsHistory.isEmpty())
            {
               strLiveTicketBody += "            \n" +
                       "\n" +
                       "            <table width=\"100%\" cellpadding=2 cellspacing=2 border=0\">\n" +
                       "                <tr class=\"LightBlueBandStyle\">\n" +
                       "                    <td width=\"75%\" class=\"txtblackbold12\">Comments</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Comments Date</td>\n" +
                       "                    <td width=\"15%\" class=\"txtblackbold12\">Comments By</td>\n" +
                       "                </tr>\n" +
                       "                \n";
               int intCounter = 0;
               String strBackGround = "";
               String strComments;
               for (int iCount = 0; iCount < arlITCommentsHistory.size(); iCount += 3)
               {
                  if (intCounter % 2 == 0)
                  {
                     strBackGround = "LightGreyBandStyle";
                  }
                  else
                  {
                     strBackGround = "DarkGreyBandStyle";
                  }
                  strComments = arlITCommentsHistory.get(iCount);
                  strComments.replace("\n", "<br/>");
                  strLiveTicketBody += "                \n" +
                          "                <tr class=\"" + strBackGround + "\">\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + strComments + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlITCommentsHistory.get(iCount + 1) + "</pre>\n" +
                          "                    </td>\n" +
                          "                    <td class=\"txtgrey\"><pre class=\"txtblue\">" + arlITCommentsHistory.get(iCount + 2) + "</pre>\n" +
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
