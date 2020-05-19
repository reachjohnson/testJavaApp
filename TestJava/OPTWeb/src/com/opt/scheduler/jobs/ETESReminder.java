package com.opt.scheduler.jobs;

import com.opt.common.util.AppSettings;
import com.opt.common.util.MailFooter;
import com.opt.jms.sender.EmailQSender;
import com.opt.session.valobj.EmailData;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.SendEmail;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ETESReminder implements Job
{
   Logger objLogger = Logger.getLogger(ETESReminder.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         if (AppSettings.isETESReminder())
         {
            String[] strToEmailIds = {AppConstants.MSO_TEAM_DL};
            String strMailSubject = "ETES Reminder : Please Submit ETES before 12 PM";
            String[] strMessageHeader = {"Team", "Please Submit ETES before 12 PM"};
            new EmailQSender().send(new EmailData(strToEmailIds, null, null, strMailSubject,
                    strMessageHeader, AppConstants.TICKET_TYPE_OTHERS, null, false));
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new JobExecutionException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}
