package com.opt.jms.sender;

import com.opt.session.valobj.EmailData;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class EmailQSender
{
   Logger objLogger = Logger.getLogger(EmailQSender.class.getName());
   public final static String JNDI_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
   public final static String JMS_FACTORY = "jms/RemoteConnectionFactory";

   public void send(EmailData objEmailData)
   {
      InitialContext objInitialContext = null;
      QueueConnectionFactory qconFactory = null;
      QueueConnection qcon = null;
      QueueSession qsession = null;
      Queue queue = null;
      QueueSender qsender = null;

      try
      {
         objInitialContext = getInitialContext();
         qconFactory = (QueueConnectionFactory) objInitialContext.lookup(JMS_FACTORY);
         qcon = qconFactory.createQueueConnection(AppConstants.EMAILQ_USER, AppConstants.EMAILQ_PASSWORD);
         qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
         queue = (Queue) objInitialContext.lookup(AppConstants.EMAILQ_NAME);
         qsender = qsession.createSender(queue);
         ObjectMessage objMessage = qsession.createObjectMessage();
         qcon.start();
         objMessage.setObject(objEmailData);
         qsender.send(objMessage);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      finally
      {
         try
         {
            if (qsender != null)
            {
               qsender.close();
            }
            if (qsession != null)
            {
               qsession.close();
            }
            if (qcon != null)
            {
               qcon.close();
            }
            if (objInitialContext != null)
            {
               objInitialContext.close();
            }
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
         }
      }
   }

   private InitialContext getInitialContext()
   {
      InitialContext objInitialContext = null;
      try
      {
         Hashtable<String, String> env = new Hashtable<>();
         env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
         env.put(Context.PROVIDER_URL, AppConstants.EMAILQ_REMOTE_URL);
         env.put(Context.SECURITY_PRINCIPAL, AppConstants.EMAILQ_USER);
         env.put(Context.SECURITY_CREDENTIALS, AppConstants.EMAILQ_PASSWORD);
         objInitialContext = new InitialContext(env);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
      return objInitialContext;
   }
}