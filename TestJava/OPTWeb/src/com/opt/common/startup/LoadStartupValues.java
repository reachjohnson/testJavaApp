package com.opt.common.startup;

import com.opt.base.dao.BaseDAO;
import com.opt.common.dao.CommonDAO;
import com.opt.common.util.AppSettings;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.util.ArrayList;

public class LoadStartupValues extends HttpServlet
{
   Logger objLogger = Logger.getLogger(LoadStartupValues.class.getName());

   public void init()
   {
      try (Connection objDBConnection = BaseDAO.openConnection(AppConstants.DB_JNDI))
      {
         ArrayList<String> arlStartUpValues = new CommonDAO().getStartUpValues(objDBConnection);
         if (arlStartUpValues != null && !arlStartUpValues.isEmpty())
         {
            new AppSettings(arlStartUpValues.get(0).equalsIgnoreCase("Y"), arlStartUpValues.get(1).equalsIgnoreCase("Y"), arlStartUpValues.get(2).equalsIgnoreCase("Y"),
                    arlStartUpValues.get(3).equalsIgnoreCase("Y"), arlStartUpValues.get(4).equalsIgnoreCase("Y"), arlStartUpValues.get(5).equalsIgnoreCase("Y"),
                    arlStartUpValues.get(6).equalsIgnoreCase("Y"), arlStartUpValues.get(7).equalsIgnoreCase("Y"), arlStartUpValues.get(8).equalsIgnoreCase("Y"),
                    arlStartUpValues.get(9).equalsIgnoreCase("Y"), arlStartUpValues.get(10).equalsIgnoreCase("Y"), arlStartUpValues.get(11).equalsIgnoreCase("Y"),
                    arlStartUpValues.get(12).equalsIgnoreCase("Y"), arlStartUpValues.get(13).equalsIgnoreCase("Y"), arlStartUpValues.get(14).equalsIgnoreCase("Y"),
                    arlStartUpValues.get(15).equalsIgnoreCase("Y"), Integer.parseInt(arlStartUpValues.get(16)), Integer.parseInt(arlStartUpValues.get(17)),
                    arlStartUpValues.get(18).equalsIgnoreCase("Y"));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
      }
   }
}
