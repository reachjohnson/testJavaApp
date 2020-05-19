package com.opt.session.valobj;

import java.io.Serializable;

public class EmailData implements Serializable
{
   private String[] strToAddress;
   private String[] strCCAddress;
   private String[] strBCCAddress;
   private String strSubject;
   private String[] strMessageHeader;
   private String strTicketType;
   private String[] strMessageBody;
   private boolean blnConstructBody;

   public EmailData(String[] strToAddress, String[] strCCAddress, String[] strBCCAddress, String strSubject, String[] strMessageHeader, String strTicketType, String[] strMessageBody, boolean blnConstructBody)
   {
      this.strToAddress = strToAddress;
      this.strCCAddress = strCCAddress;
      this.strBCCAddress = strBCCAddress;
      this.strSubject = strSubject;
      this.strMessageHeader = strMessageHeader;
      this.strTicketType = strTicketType;
      this.strMessageBody = strMessageBody;
      this.blnConstructBody = blnConstructBody;
   }

   public String[] getToAddress()
   {
      return strToAddress;
   }

   public String[] getCCAddress()
   {
      return strCCAddress;
   }

   public String[] getBCCAddress()
   {
      return strBCCAddress;
   }

   public String getSubject()
   {
      return strSubject;
   }

   public String[] getMessageHeader()
   {
      return strMessageHeader;
   }

   public String getTicketType()
   {
      return strTicketType;
   }

   public String[] getMessageBody()
   {
      return strMessageBody;
   }

   public boolean isConstructBody()
   {
      return blnConstructBody;
   }
}
