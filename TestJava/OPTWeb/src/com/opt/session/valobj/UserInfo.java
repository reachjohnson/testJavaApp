package com.opt.session.valobj;

import java.io.Serializable;

/**
 * This class is used to store and retrieve user related info
 */

public class UserInfo implements Serializable
{
   private String strUserId;
   private String strUserName;
   private boolean blnAdmin;
   private boolean blnActive;
   private boolean blnAssignee;
   private String strTeam;
   private boolean blnAccessRestricted;
   private String strLastLogin;

   public UserInfo(String strUserId, String strUserName, boolean blnActive, boolean blnAdmin, boolean blnAssignee, String strTeam, boolean blnAccessRestricted, String strLastLogin)
   {
      this.strUserId = strUserId;
      this.strUserName = strUserName;
      this.blnActive = blnActive;
      this.blnAdmin = blnAdmin;
      this.blnAssignee = blnAssignee;
      this.strTeam = strTeam;
      this.blnAccessRestricted = blnAccessRestricted;
      this.strLastLogin = strLastLogin;
   }

   public String getUserId()
   {
      return strUserId;
   }

   public String getUserName()
   {
      return strUserName;
   }

   public boolean getActive()
   {
      return blnActive;
   }

   public boolean getAdmin()
   {
      return blnAdmin;
   }

   public boolean getAssignee()
   {
      return blnAssignee;
   }

   public String getTeam()
   {
      return strTeam;
   }

   public boolean isBlnAccessRestricted()
   {
      return blnAccessRestricted;
   }

   public void setBlnAccessRestricted(boolean blnAccessRestricted)
   {
      this.blnAccessRestricted = blnAccessRestricted;
   }

   public String getLastLogin()
   {
      return strLastLogin;
   }
}
