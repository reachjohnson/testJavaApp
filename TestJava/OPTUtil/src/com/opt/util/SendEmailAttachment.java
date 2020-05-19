package com.opt.util;

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendEmailAttachment
{
   Logger objLogger = Logger.getLogger(SendEmailAttachment.class.getName());

   public void sendEmail(String fromAddress, String toAddress, String ccAddress, String bccAddress, String strSubject, String strMessage, String strFilePath)
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      Properties props = new Properties();
      props.put("mail.smtp.host", AppConstants.MAIL_SERVER_IP);
      Session session = Session.getInstance(props);
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
         MimeBodyPart mbp1 = new MimeBodyPart();
         mbp1.setText(strMessage);

         // create the second message part
         MimeBodyPart mbp2 = new MimeBodyPart();

         // attach the file to the message
         FileDataSource fds = new FileDataSource(strFilePath);
         mbp2.setDataHandler(new DataHandler(fds));
         mbp2.setFileName(fds.getName());

         // create the Multipart and add its parts to it
         Multipart mp = new MimeMultipart();
         mp.addBodyPart(mbp1);
         mp.addBodyPart(mbp2);

         // add the Multipart to the message
         message.setContent(mp);
         //Transport.send(message);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}
