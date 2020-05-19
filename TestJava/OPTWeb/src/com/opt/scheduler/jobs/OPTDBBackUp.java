package com.opt.scheduler.jobs;

import com.opt.common.util.AppSettings;
import com.opt.util.AppConstants;
import com.opt.util.AppUtil;
import com.opt.util.SendEmailAttachment;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OPTDBBackUp implements Job
{
   Logger objLogger = Logger.getLogger(OPTDBBackUp.class.getName());

   public void execute(JobExecutionContext context)
           throws JobExecutionException
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      if (AppSettings.isDBBackup())
      {
         try
         {
            String strFilePath = "C:\\OPT_Project\\OPTDBBackup\\";

            String strBatchFilePath = strFilePath + "OPTBackUp.cmd";
            Process objProcess = Runtime.getRuntime().exec(strBatchFilePath);

            objProcess.waitFor();

            InputStream in = objProcess.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int c;
            while ((c = in.read()) != -1)
            {
               baos.write(c);
            }

            in.close();
            baos.close();

            byte[] buffer = new byte[1024];
            String strCurrentDate = AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_MM_DD_YYYY);
            StringBuilder sbZipFileName = new StringBuilder("OPTDump-").append(strCurrentDate).append(".zip");
            StringBuilder sbSqlFileName = new StringBuilder("OPTDump-").append(strCurrentDate).append(".sql");
            FileOutputStream fos = new FileOutputStream(strFilePath + sbZipFileName.toString());
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(sbSqlFileName.toString());
            zos.putNextEntry(ze);
            FileInputStream objFileInpStr = new FileInputStream(strFilePath + sbSqlFileName.toString());

            int len;
            while ((len = objFileInpStr.read(buffer)) > 0)
            {
               zos.write(buffer, 0, len);
            }
            objFileInpStr.close();
            zos.closeEntry();
            zos.close();

            String strToMailId = AppConstants.LEAD_TEAM_DL;
            StringBuilder sbMailSubject = new StringBuilder(AppConstants.EMAIL_APPTITLE).append(" : DataBase BackUp (").append(AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY)).append(")");
            StringBuilder sbMessage = new StringBuilder(AppConstants.EMAIL_APPTITLE).append(" : DataBase Backup As On ").append(AppUtil.getCurrentDate(AppConstants.JAVA_DATE_FORMAT_DD_MMM_YYYY)).append(".");
            new SendEmailAttachment().sendEmail(AppConstants.EMAIL_FROM + AppConstants.EMAIL_SUFFIX, strToMailId, null, null, sbMailSubject.toString(), sbMessage.toString(), strFilePath + sbZipFileName.toString());
         }
         catch (Exception objException)
         {
            objLogger.error(objException, objException.fillInStackTrace());
            throw new JobExecutionException(objException.getMessage(), objException);
         }
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }
}
