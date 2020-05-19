package com.opt.exception.base;

import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * This class is used as a user defined exception class which is extended in all the othe exception classes.
 */

public class NestingException extends Exception
{
   private Throwable nestedException;
   private String stackTraceString;

   /**
    * This is method is used to generate stack trace.
    */
   static public String generateStackTraceString(Throwable t)
   {
      StringWriter s = new StringWriter();
      t.printStackTrace(new PrintWriter(s));
      return s.toString();
   }

   /**
    * This is a default constructor.
    */

   public NestingException()
   {
   }

   /**
    * This is a constructor which takes one parameters.
    *
    * @param strMessage represents the Error Message.
    */

   public NestingException(String strMessage)
   {
      super(strMessage);
   }

   /**
    * This is a constructor which takes one parameters.
    *
    * @param nestedException represents the Throwable Object.
    */

   public NestingException(Throwable nestedException)
   {
      this.nestedException = nestedException;
      nestedException.printStackTrace();
      stackTraceString = generateStackTraceString(nestedException);
   }

   /**
    * This is a constructor which takes two parameters.
    *
    * @param strMessage      represents the Error Message.
    * @param nestedException represents the Throwable Object.
    */

   public NestingException(String strMessage, Throwable nestedException)
   {
      this(strMessage);
      nestedException.printStackTrace();
      this.nestedException = nestedException;
      stackTraceString = generateStackTraceString(nestedException);
   }

   /**
    * This method acts as a getter method for nestedException Variable.
    */

   public Throwable getNestedException()
   {
      return nestedException;
   }

   /**
    * This method acts as a getter method for getting the stack trace for the Exception occurred.
    */

   public String getStackTraceString()
   {
      if (nestedException == null)
         return null;
      StringBuffer traceBuffer = new StringBuffer();
      if (nestedException instanceof NestingException)
      {
         traceBuffer.append(((NestingException) nestedException).getStackTraceString());
         traceBuffer.append("-------- nested by:\n");
      }
      traceBuffer.append(stackTraceString);
      return traceBuffer.toString();
   }

   /**
    * This method is used for getting the Error Message.
    */

   public String getMessage()
   {
      String superMsg = super.getMessage();
      if (getNestedException() == null)
         return superMsg;
      StringBuffer theMsg = new StringBuffer();
      String nestedMsg = getNestedException().getMessage();
      if (nestedMsg == null)
         nestedMsg = "";
      if (superMsg != null)
         theMsg.append(superMsg).append(": ").append(nestedMsg);
      else
         theMsg.append(nestedMsg);
      return theMsg.toString();
   }

   /**
    * This method is used for converting the exception into a string.
    */

   public String toString()
   {
      StringBuffer theMsg = new StringBuffer(super.toString());
      if (getNestedException() != null)
         theMsg.append("; \n\t---> nested ").append(getNestedException());
      return theMsg.toString();
   }
}