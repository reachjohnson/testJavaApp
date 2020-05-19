package com.opt.exception;

public class DAOException extends com.opt.exception.base.NestingException
{
   public DAOException()
   {
      super();
   }

   public DAOException(String strMessage, Exception nestedException)
   {
      super(strMessage, nestedException);
   }
}
