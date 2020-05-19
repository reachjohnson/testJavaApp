package com.opt.util;

import org.apache.log4j.Logger;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail
{
   Logger objLogger = Logger.getLogger(SendEmail.class.getName());

   public void sendEmail(String fromAddress, String toAddress, String ccAddress, String bccAddress, String strSubject, String strMessage)
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      Properties props;
      boolean blnTransportConnect = true;
      int IPCounter = 0;
      int MailServersLength = AppConstants.MAIL_SERVER_IPS.length;
      Exception objException = new Exception();
      while(blnTransportConnect)
      {
         props = new Properties();
         props.put("mail.smtp.host", AppConstants.MAIL_SERVER_IPS[IPCounter]);
         Session session = Session.getInstance(props);
            if (IPCounter >= MailServersLength) {
               blnTransportConnect = false;
               objLogger.error(objException, objException.fillInStackTrace());
            }
            else
            {
               try
               {
                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress(fromAddress));
                  message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
                  if (ccAddress != null && ccAddress.length() > 0)
                  {
                     message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress));
                  }
                  if (bccAddress != null && bccAddress.length() > 0)
                  {
                     message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccAddress));
                  }

                  message.setSubject(strSubject);
                  message.setContent(strMessage, "text/html");
                  //Transport.send(message);
                  blnTransportConnect = false;
               }
               catch (Exception thisException)
               {
                  IPCounter++;
                  blnTransportConnect = true;
                  objException = thisException;
               }
            }

      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }
}
 
