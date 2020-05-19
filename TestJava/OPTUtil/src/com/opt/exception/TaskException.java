package com.opt.exception;

/**
 * This class is used as a user defined exception class for handling exception arising in tasks.
 */

public class TaskException extends com.opt.exception.base.NestingException
{

   /**
    * This is a default constructor.
    */
   public TaskException()
   {
      super();
   }

   /**
    * This is a constructor which takes two parameters.
    *
    * @param strMessage      represents the Error Message.
    * @param nestedException represents the Exception Object.
    */

   public TaskException(String strMessage, Exception nestedException)
   {
      super(strMessage, nestedException);
   }
}
