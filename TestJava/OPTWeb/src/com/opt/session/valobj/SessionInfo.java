package com.opt.session.valobj;

public class SessionInfo implements java.io.Serializable
{
   private UserInfo objUserInfo;
   private UserAccessRights objUserAccessRights;

   public void setUserInfo(UserInfo objUserInfo)
   {
      this.objUserInfo = objUserInfo;
   }

   public UserInfo getUserInfo()
   {
      return this.objUserInfo;
   }

   public void setUserAccessRights(UserAccessRights objUserAccessRights)
   {
      this.objUserAccessRights = objUserAccessRights;
   }

   public UserAccessRights getUserAccessRights()
   {

      return objUserAccessRights;
   }
}
