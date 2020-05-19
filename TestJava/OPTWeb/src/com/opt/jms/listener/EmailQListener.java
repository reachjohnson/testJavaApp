package com.opt.jms.listener;

import com.opt.jms.receiver.EmailQReceiver;
import com.opt.util.AppConstants;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import javax.servlet.http.HttpServlet;

public class EmailQListener extends HttpServlet
{
   public final static String JNDI_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";

   public void init()
   {
      try
      {
         InitialContext objInitialContext = getInitialContext();
         EmailQReceiver objEmailQReceiver = new EmailQReceiver();
         objEmailQReceiver.init(objInitialContext, AppConstants.EMAILQ_NAME);
      }
      catch (Exception objException)
      {
         objException.printStackTrace();
      }
   }

   private static InitialContext getInitialContext() throws NamingException
   {
      Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
      env.put(Context.PROVIDER_URL, AppConstants.EMAILQ_REMOTE_URL);
      env.put(Context.SECURITY_PRINCIPAL, AppConstants.EMAILQ_USER);
      env.put(Context.SECURITY_CREDENTIALS, AppConstants.EMAILQ_PASSWORD);
      return new InitialContext(env);
   }

}
