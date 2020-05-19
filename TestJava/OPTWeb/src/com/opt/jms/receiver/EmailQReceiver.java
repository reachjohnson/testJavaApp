package com.opt.jms.receiver;

import com.opt.common.util.MailFooter;
import com.opt.jms.util.ConstructInternalTicket;
import com.opt.jms.util.ConstructLiveTicket;
import com.opt.jms.util.ConstructRQATicket;
import com.opt.session.valobj.EmailData;
import com.opt.util.AppConstants;
import com.opt.util.SendEmail;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class EmailQReceiver implements MessageListener
{
   Logger objLogger = Logger.getLogger(EmailQReceiver.class.getName());
   public final static String JMS_FACTORY = "jms/RemoteConnectionFactory";

   public void onMessage(Message objMessage)
   {
      try
      {
         EmailData objEmailData = (EmailData) ((ObjectMessage) objMessage).getObject();
         String strTicketType = objEmailData.getTicketType();
         String strMessageHeader = constructMessageHeader(objEmailData.getMessageHeader());
         String strMessageBody = "";
         if (strTicketType.equalsIgnoreCase(AppConstants.TICKET_TYPE_LIVE))
         {
            String[] strRefNoTicketId = objEmailData.getMessageBody();
            strMessageBody = new ConstructLiveTicket().ConstructLiveTicket(strRefNoTicketId[0], strRefNoTicketId[1]);
            strMessageBody += MailFooter.getMailFooter();
         }
         else if (strTicketType.equalsIgnoreCase(AppConstants.TICKET_TYPE_RQA))
         {
            String[] strRefNoTicketId = objEmailData.getMessageBody();
            strMessageBody = new ConstructRQATicket().ConstructRQATicket(strRefNoTicketId[0], strRefNoTicketId[1]);
            strMessageBody += MailFooter.getMailFooter();
         }
         else  if (strTicketType.equalsIgnoreCase(AppConstants.TICKET_TYPE_INTERNAL))
         {
            String[] strRefNoTicketId = objEmailData.getMessageBody();
            strMessageBody = new ConstructInternalTicket().ConstructInternalTicket(strRefNoTicketId[0], strRefNoTicketId[1]);
            strMessageBody += MailFooter.getMailFooter();
         }
         else
         {
            strMessageBody = constructMessageBody(objEmailData.getMessageBody(), objEmailData.isConstructBody());
         }
         if (objEmailData != null)
         {
            String[] strCCMailIds = objEmailData.getCCAddress();
            List<String> arlCCMailIds = new ArrayList<>();

            if (strCCMailIds != null)
            {
               arlCCMailIds.addAll(Arrays.asList(strCCMailIds));
            }
            List<String> CCMailidsList = Arrays.asList(AppConstants.TEAM_DL);
            arlCCMailIds.addAll(CCMailidsList);

            HashSet<String> hsTemp = new HashSet<>();
            hsTemp.addAll(arlCCMailIds);
            arlCCMailIds.clear();
            arlCCMailIds.addAll(hsTemp);
            strCCMailIds = arrangeCCMailIds(objEmailData.getToAddress(), arlCCMailIds);

            new SendEmail().sendEmail(AppConstants.EMAIL_FROM + AppConstants.EMAIL_SUFFIX, constructMailIds(objEmailData.getToAddress()), constructMailIds(strCCMailIds),
                    constructMailIds(objEmailData.getBCCAddress()), constructSubject(objEmailData.getSubject()),
                    strMessageHeader + strMessageBody);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
   }

   public void init(Context ctx, String queueName)
   {
      QueueConnectionFactory qconFactory;
      QueueConnection qcon;
      QueueSession qsession;
      Queue queue;
      QueueReceiver qreceiver;

      try
      {
         qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
         qcon = qconFactory.createQueueConnection(AppConstants.EMAILQ_USER, AppConstants.EMAILQ_PASSWORD);
         qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
         queue = (Queue) ctx.lookup(queueName);
         qreceiver = qsession.createReceiver(queue);
         qreceiver.setMessageListener(this);
         qcon.start();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      finally
      {
         try
         {
            if (ctx != null)
            {
               ctx.close();
            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
         }
      }
   }

   private String[] arrangeCCMailIds(String[] strToMailIds, List<String> arlCCMailIds)
   {

      try
      {
         for (String strToMailId : strToMailIds)
         {
            if (arlCCMailIds.contains(strToMailId))
            {
               arlCCMailIds.remove(strToMailId);
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      if (arlCCMailIds != null && !arlCCMailIds.isEmpty())
      {
         return arlCCMailIds.toArray(new String[arlCCMailIds.size()]);
      }
      else
      {
         return null;
      }
   }

   private String constructMailIds(String[] strMailIds)
   {
      StringBuilder sbMailIds = new StringBuilder();
      try
      {
         if (strMailIds != null && strMailIds.length > 0)
         {
            int intCounter = 0;
            for (String strMailId : strMailIds)
            {
               if (!sbMailIds.toString().contains(strMailId))
               {
                  if (intCounter > 0)
                  {
                     sbMailIds.append(AppConstants.MAIL_ID_SEPERATOR);
                  }
                  sbMailIds.append(strMailId);
                  if (!strMailId.contains("@"))
                  {
                     sbMailIds.append(AppConstants.EMAIL_SUFFIX);
                  }
               }
               intCounter++;
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      if (sbMailIds.length() > 0)
      {
         return sbMailIds.toString();
      }
      else
      {
         return null;
      }
   }

   private String constructSubject(String strSubject)
   {
      StringBuilder sbSubject = new StringBuilder();
      try
      {
         if (strSubject != null && strSubject.length() > 0)
         {
            sbSubject.append(AppConstants.EMAIL_APPTITLE).append(" : ").append(strSubject);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      return sbSubject.toString();
   }

   private String constructMessageHeader(String[] strMessageHeader)
   {
      StringBuilder sbMessageHeader = new StringBuilder();
      try
      {
         if (strMessageHeader != null && strMessageHeader.length > 0)
         {
            sbMessageHeader.append(AppConstants.MAIL_LBL_FONT_START).append(AppConstants.MAIL_LBL_HI).append(strMessageHeader[0]).append(",").append(AppConstants.MAIL_LBL_NEXT_LINE).append(AppConstants.MAIL_LBL_NEXT_LINE);
            sbMessageHeader.append(strMessageHeader[1]).append(AppConstants.MAIL_LBL_DOT).append(AppConstants.MAIL_LBL_FONT_END);
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      return sbMessageHeader.toString();
   }

   private String constructMessageBody(String[] strMessageBody, boolean blnConstructBody)
   {
      String strNewMessageBody = "";
      try
      {
         if (blnConstructBody)
         {
            StringBuilder sbMessageBody = new StringBuilder();
            if (strMessageBody != null && strMessageBody.length > 0)
            {
               String strLabel;
               String strValue;
               String strTicketId = "";
               sbMessageBody.append("<table width=\"100%\" cellpadding=2 cellspacing=2 border=1 align=\"left\">\n");
               for (int iCount = 0; iCount < strMessageBody.length; iCount += 2)
               {
                  strLabel = strMessageBody[iCount];
                  strValue = strMessageBody[iCount + 1];
                  if (strLabel.equalsIgnoreCase(AppConstants.TICKET_ID_LABEL))
                  {
                     strTicketId = strValue;
                  }
                  sbMessageBody.append("<tr>\n");
                  sbMessageBody.append("<td>\n");
                  sbMessageBody.append("<table width=\"100%\" cellpadding=2 cellspacing=2 border=0 align=\"center\">\n");
                  sbMessageBody.append("<tr bgcolor=\"#f2f2f2\">\n");
                  sbMessageBody.append("<td width=\"15%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;font-weight: bold;color: #666666;\">").append(strLabel).append("</td>\n");
                  if (strLabel.equalsIgnoreCase(AppConstants.TICKET_ID_LABEL) || strLabel.equalsIgnoreCase(AppConstants.TICKET_DESC_LABEL))
                  {
                     sbMessageBody.append("<td width=\"85%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">").append(AppConstants.MAIL_LBL_JIRA_LINK_START).append(strTicketId).append(AppConstants.MAIL_LBL_JIRA_LINK_CLOSE).append(strValue).append(AppConstants.MAIL_LBL_JIRA_LINK_END).append("\n");
                  }
                  else
                  {
                     sbMessageBody.append("<td width=\"85%\" style=\"font-family: verdana, arial, helvetica;font-size: 13px;color: #666666;\">").append(strValue).append("\n");
                  }
                  sbMessageBody.append("</td>\n");
                  sbMessageBody.append("</tr>\n");
                  sbMessageBody.append("</table>\n");
                  sbMessageBody.append("</td>\n");
                  sbMessageBody.append("</tr>\n\n");
               }
               sbMessageBody.append("</table><br/><br/>\n\n");
               sbMessageBody.append(MailFooter.getMailFooter());
            }
            strNewMessageBody = sbMessageBody.toString();
         }
         else
         {
            if (strMessageBody != null && strMessageBody.length > 0)
            {
               strNewMessageBody = strMessageBody[0] + MailFooter.getMailFooter();
            }
            else
            {
               strNewMessageBody = MailFooter.getMailFooter();
            }
         }

      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      return strNewMessageBody;
   }
}