package com.opt.servlets;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet
{
   public void init()
   {
      String prefix = getServletContext().getRealPath("/");
      String file = getInitParameter("log4j-init-file");
      ServletContext context = getServletContext();
      System.setProperty("appRootPath", context.getRealPath("/"));
      if (file != null)
      {
         PropertyConfigurator.configure(prefix + file);
      }
      else
      {
         System.out.println("Log4J Is not configured for your Application: " + prefix + file);
      }
   }
}
