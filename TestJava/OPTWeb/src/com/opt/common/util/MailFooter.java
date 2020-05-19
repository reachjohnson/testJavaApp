package com.opt.common.util;

import com.opt.util.AppConstants;

public class MailFooter
{
   public static String getMailFooter()
   {
      String strMessageBody = "<table width=\"40%\" cellpadding=0 cellspacing=0 border=1 align=\"left\">\n" +
              "\n" +
              "    <tr>\n" +
              "        <td>\n" +
              "            <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
              "                <tr bgcolor=\"#B0E0E6\">\n" +
              "                    <td width=\"20%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">OPT URL</td>\n" +
              "                    <td width=\"80%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">" + AppConstants.APPLICATIONURL + "\n" +
              "                    </td>\n" +
              "                </tr>\n" +
              "            </table>\n" +
              "        </td>\n" +
              "    </tr>\n" +
              "    <tr>\n" +
              "        <td>\n" +
              "            <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
              "                <tr bgcolor=\"#B0E0E6\">\n" +
              "                    <td width=\"100%\" colspan=\"2\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">" + AppConstants.MAIL_LBL_BROWSER_USE + "</td>\n" +
              "                    </td>\n" +
              "                </tr>\n" +
              "            </table>\n" +
              "        </td>\n" +
              "    </tr>\n" +
              "\n" +
              "    <tr>\n" +
              "        <td>\n" +
              "            <table width=\"100%\" cellpadding=2 cellspacing=1 border=0 align=\"center\">\n" +
              "                <tr bgcolor=\"#B0E0E6\">\n" +
              "                    <td width=\"100%\" colspan=\"2\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">" + AppConstants.MAIL_LBL_AUTO_MAIL + "</td>\n" +
              "                    </td>\n" +
              "                </tr>\n" +
              "            </table>\n" +
              "        </td>\n" +
              "    </tr>\n" +
              "\n";

      strMessageBody += "</table>\n\n";
      return strMessageBody;
   }
}
