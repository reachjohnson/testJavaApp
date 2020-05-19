package com.opt.reports.PDFReports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import com.opt.exception.TaskException;
import com.opt.reports.common.Templates;
import com.opt.util.AppConstants;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SLAMetrics
{
   Logger objLogger = Logger.getLogger(SLAMetrics.class.getName());
   String strFromDate;
   String strToDate;
   ArrayList<String> arlCurrentOpenTasksCount;
   ArrayList<String> arlCurrentOpenBugsCount;

   ArrayList<String> arlTasksStatusCount;
   ArrayList<String> arlBugsStatusCount;
   ArrayList<String> arlTasksResolutioncount;
   ArrayList<String> arlTasksMovedTeamscount;
   ArrayList<String> arlBugsResolutioncount;
   ArrayList<String> arlBugsMovedTeamscount;
   ArrayList<String> arlTasksSLA;
   ArrayList<String> arlBugsSLA;

   private HashMap<Integer, String> hpReportParameters;
   private int intReportNumbers;
   private OutputStream out;

   public SLAMetrics(String strFromDate, String strToDate, ArrayList<String> arlCurrentOpenTasksCount, ArrayList<String> arlCurrentOpenBugsCount,
                     ArrayList<String> arlTasksStatusCount, ArrayList<String> arlBugsStatusCount,
                     ArrayList<String> arlTasksResolutioncount, ArrayList<String> arlTasksMovedTeamscount,
                     ArrayList<String> arlBugsResolutioncount, ArrayList<String> arlBugsMovedTeamscount,
                     ArrayList<String> arlTasksSLA, ArrayList<String> arlBugsSLA,
                     int intReportNumbers, HashMap<Integer, String> hpReportParameters, OutputStream out) throws Exception
   {
      try
      {
         this.strFromDate = strFromDate;
         this.strToDate = strToDate;
         this.arlCurrentOpenTasksCount = arlCurrentOpenTasksCount;
         this.arlCurrentOpenBugsCount = arlCurrentOpenBugsCount;

         this.arlTasksStatusCount = arlTasksStatusCount;
         this.arlBugsStatusCount = arlBugsStatusCount;
         this.arlTasksResolutioncount = arlTasksResolutioncount;
         this.arlTasksMovedTeamscount = arlTasksMovedTeamscount;
         this.arlBugsResolutioncount = arlBugsResolutioncount;
         this.arlBugsMovedTeamscount = arlBugsMovedTeamscount;
         this.arlTasksSLA = arlTasksSLA;
         this.arlBugsSLA = arlBugsSLA;

         this.intReportNumbers = intReportNumbers;
         this.hpReportParameters = hpReportParameters;
         this.out = out;
         Build();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
   }

   private void Build() throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
              .setDataSource(new SubreportDataSourceExpression());
      try
      {
         report()
                 .title(Templates.createTitleComponent("Online Payments - Metrics Report for the period " + strFromDate + " To " + strToDate))
                 .detail(
                         subreport,
                         cmp.verticalGap(20))
                 .pageFooter(Templates.footerComponent)
                 .setDataSource(createDataSource())
                 .toPdf(out);
         out.flush();
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   private JRDataSource createDataSource()
   {
      return new JREmptyDataSource(intReportNumbers);
   }

   private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder>
   {
      private static final long serialVersionUID = 1L;

      @Override
      public JasperReportBuilder evaluate(ReportParameters reportParameters)
      {
         String strReportName = hpReportParameters.get(reportParameters.getReportRowNumber());
         JasperReportBuilder report = report();
         if (strReportName.equalsIgnoreCase("CurrentOpenTasksCount"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Current Open Tasks").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Module", "Module", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[0], AppConstants.TICKET_PRIORITY[0], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[1], AppConstants.TICKET_PRIORITY[1], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[2], AppConstants.TICKET_PRIORITY[2], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[3], AppConstants.TICKET_PRIORITY[3], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[4], AppConstants.TICKET_PRIORITY[4], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Total", "Total", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         if (strReportName.equalsIgnoreCase("CurrentOpenBugsCount"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Current Open Bugs").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Module", "Module", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[0], AppConstants.TICKET_PRIORITY[0], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[1], AppConstants.TICKET_PRIORITY[1], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[2], AppConstants.TICKET_PRIORITY[2], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[3], AppConstants.TICKET_PRIORITY[3], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[4], AppConstants.TICKET_PRIORITY[4], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Total", "Total", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         if (strReportName.equalsIgnoreCase("TasksStatusCount"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Tasks Status").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Module", "Module", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[0], AppConstants.TICKET_PRIORITY[0], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[1], AppConstants.TICKET_PRIORITY[1], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[2], AppConstants.TICKET_PRIORITY[2], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[3], AppConstants.TICKET_PRIORITY[3], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[4], AppConstants.TICKET_PRIORITY[4], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Total", "Total", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         if (strReportName.equalsIgnoreCase("TasksResolutioncount"))
         {
            if (arlTasksResolutioncount != null && !arlTasksResolutioncount.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Tasks Resolution Types").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("Resolution Type", "ResolutionType", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column(" Count", "Count", type.stringType()).setStyle(Templates.columnStyle8Number).setWidth(15));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Tasks Resolution Types").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("TasksMovedTeamscount"))
         {
            if (arlTasksMovedTeamscount != null && !arlTasksMovedTeamscount.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Tasks - Analyzed and Moved to Other Teams").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("Moved Team", "MovedTeam", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column(" Count", "Count", type.stringType()).setStyle(Templates.columnStyle8Number).setWidth(15));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Tasks - Analyzed and Moved to Other Teams").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("TasksSLA"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Tasks SLA").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Priority", "Priority", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column("In SLA", "InSLA", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Out Of SLA", "OutOfSLA", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Average No.Of Days", "AverageNoOfDays", type.stringType()).setStyle(Templates.columnStyle8Number));
         }

         if (strReportName.equalsIgnoreCase("BugsStatusCount"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Bugs Status").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Module", "Module", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[0], AppConstants.TICKET_PRIORITY[0], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[1], AppConstants.TICKET_PRIORITY[1], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[2], AppConstants.TICKET_PRIORITY[2], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[3], AppConstants.TICKET_PRIORITY[3], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column(AppConstants.TICKET_PRIORITY[4], AppConstants.TICKET_PRIORITY[4], type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Total", "Total", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         if (strReportName.equalsIgnoreCase("BugsResolutioncount"))
         {
            if (arlBugsResolutioncount != null && !arlBugsResolutioncount.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Bugs Resolution Types").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("Resolution Type", "ResolutionType", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column(" Count", "Count", type.stringType()).setStyle(Templates.columnStyle8Number).setWidth(15));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Bugs Resolution Types").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("BugsMovedTeamscount"))
         {
            if (arlBugsMovedTeamscount != null && !arlBugsMovedTeamscount.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Bugs - Analyzed and Moved to Other Teams").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("Moved Team", "MovedTeam", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column(" Count", "Count", type.stringType()).setStyle(Templates.columnStyle8Number).setWidth(15));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Bugs - Analyzed and Moved to Other Teams").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }

         if (strReportName.equalsIgnoreCase("BugsSLA"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Bugs SLA").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Priority", "Priority", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column("In SLA", "InSLA", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Out Of SLA", "OutOfSLA", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Average No.Of Days", "AverageNoOfDays", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         return report;
      }
   }

   private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource>
   {
      private static final long serialVersionUID = 1L;

      @Override
      public JRDataSource evaluate(ReportParameters reportParameters)
      {
         String strReportName = hpReportParameters.get(reportParameters.getReportRowNumber());
         DRDataSource dataSource = null;
         if (strReportName.equalsIgnoreCase("CurrentOpenTasksCount"))
         {
            String[] columns = new String[7];
            columns[0] = "Module";
            columns[1] = AppConstants.TICKET_PRIORITY[0];
            columns[2] = AppConstants.TICKET_PRIORITY[1];
            columns[3] = AppConstants.TICKET_PRIORITY[2];
            columns[4] = AppConstants.TICKET_PRIORITY[3];
            columns[5] = AppConstants.TICKET_PRIORITY[4];
            columns[6] = "Total";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlCurrentOpenTasksCount.size(); intCount += 7)
            {
               dataSource.add(arlCurrentOpenTasksCount.get(intCount), arlCurrentOpenTasksCount.get(intCount + 1),
                       arlCurrentOpenTasksCount.get(intCount + 2), arlCurrentOpenTasksCount.get(intCount + 3),
                       arlCurrentOpenTasksCount.get(intCount + 4), arlCurrentOpenTasksCount.get(intCount + 5),
                       arlCurrentOpenTasksCount.get(intCount + 6));
            }
         }
         if (strReportName.equalsIgnoreCase("CurrentOpenBugsCount"))
         {
            String[] columns = new String[7];
            columns[0] = "Module";
            columns[1] = AppConstants.TICKET_PRIORITY[0];
            columns[2] = AppConstants.TICKET_PRIORITY[1];
            columns[3] = AppConstants.TICKET_PRIORITY[2];
            columns[4] = AppConstants.TICKET_PRIORITY[3];
            columns[5] = AppConstants.TICKET_PRIORITY[4];
            columns[6] = "Total";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlCurrentOpenBugsCount.size(); intCount += 7)
            {
               dataSource.add(arlCurrentOpenBugsCount.get(intCount), arlCurrentOpenBugsCount.get(intCount + 1),
                       arlCurrentOpenBugsCount.get(intCount + 2), arlCurrentOpenBugsCount.get(intCount + 3),
                       arlCurrentOpenBugsCount.get(intCount + 4), arlCurrentOpenBugsCount.get(intCount + 5),
                       arlCurrentOpenBugsCount.get(intCount + 6));
            }
         }
         if (strReportName.equalsIgnoreCase("TasksStatusCount"))
         {
            String[] columns = new String[7];
            columns[0] = "Module";
            columns[1] = AppConstants.TICKET_PRIORITY[0];
            columns[2] = AppConstants.TICKET_PRIORITY[1];
            columns[3] = AppConstants.TICKET_PRIORITY[2];
            columns[4] = AppConstants.TICKET_PRIORITY[3];
            columns[5] = AppConstants.TICKET_PRIORITY[4];
            columns[6] = "Total";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlTasksStatusCount.size(); intCount += 7)
            {
               dataSource.add(arlTasksStatusCount.get(intCount), arlTasksStatusCount.get(intCount + 1),
                       arlTasksStatusCount.get(intCount + 2), arlTasksStatusCount.get(intCount + 3),
                       arlTasksStatusCount.get(intCount + 4), arlTasksStatusCount.get(intCount + 5),
                       arlTasksStatusCount.get(intCount + 6));
            }
         }
         if (strReportName.equalsIgnoreCase("TasksResolutioncount"))
         {
            if (arlTasksResolutioncount != null && !arlTasksResolutioncount.isEmpty())
            {
               String[] columns = new String[2];
               columns[0] = "ResolutionType";
               columns[1] = "Count";
               dataSource = new DRDataSource(columns);
               for (int intCount = 0; intCount < arlTasksResolutioncount.size(); intCount += 2)
               {
                  dataSource.add(arlTasksResolutioncount.get(intCount), arlTasksResolutioncount.get(intCount + 1));
               }
            }
            else
            {
               String[] columns = new String[1];
               columns[0] = "nodata";
               dataSource = new DRDataSource(columns);
               dataSource.add(" ");
            }
         }
         if (strReportName.equalsIgnoreCase("TasksMovedTeamscount"))
         {
            if (arlTasksMovedTeamscount != null && !arlTasksMovedTeamscount.isEmpty())
            {
               String[] columns = new String[2];
               columns[0] = "MovedTeam";
               columns[1] = "Count";
               dataSource = new DRDataSource(columns);
               for (int intCount = 0; intCount < arlTasksMovedTeamscount.size(); intCount += 2)
               {
                  dataSource.add(arlTasksMovedTeamscount.get(intCount), arlTasksMovedTeamscount.get(intCount + 1));
               }
            }
            else
            {
               String[] columns = new String[1];
               columns[0] = "nodata";
               dataSource = new DRDataSource(columns);
               dataSource.add(" ");
            }
         }
         if (strReportName.equalsIgnoreCase("TasksSLA"))
         {
            String[] columns = new String[4];
            columns[0] = "Priority";
            columns[1] = "InSLA";
            columns[2] = "OutOfSLA";
            columns[3] = "AverageNoOfDays";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlTasksSLA.size(); intCount += 4)
            {
               dataSource.add(arlTasksSLA.get(intCount), arlTasksSLA.get(intCount + 1),
                       arlTasksSLA.get(intCount + 2), arlTasksSLA.get(intCount + 3));
            }
         }

         if (strReportName.equalsIgnoreCase("BugsStatusCount"))
         {
            String[] columns = new String[7];
            columns[0] = "Module";
            columns[1] = AppConstants.TICKET_PRIORITY[0];
            columns[2] = AppConstants.TICKET_PRIORITY[1];
            columns[3] = AppConstants.TICKET_PRIORITY[2];
            columns[4] = AppConstants.TICKET_PRIORITY[3];
            columns[5] = AppConstants.TICKET_PRIORITY[4];
            columns[6] = "Total";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlBugsStatusCount.size(); intCount += 7)
            {
               dataSource.add(arlBugsStatusCount.get(intCount), arlBugsStatusCount.get(intCount + 1),
                       arlBugsStatusCount.get(intCount + 2), arlBugsStatusCount.get(intCount + 3),
                       arlBugsStatusCount.get(intCount + 4), arlBugsStatusCount.get(intCount + 5),
                       arlBugsStatusCount.get(intCount + 6));
            }
         }
         if (strReportName.equalsIgnoreCase("BugsResolutioncount"))
         {
            if (arlBugsResolutioncount != null && !arlBugsResolutioncount.isEmpty())
            {
               String[] columns = new String[2];
               columns[0] = "ResolutionType";
               columns[1] = "Count";
               dataSource = new DRDataSource(columns);
               for (int intCount = 0; intCount < arlBugsResolutioncount.size(); intCount += 2)
               {
                  dataSource.add(arlBugsResolutioncount.get(intCount), arlBugsResolutioncount.get(intCount + 1));
               }
            }
            else
            {
               String[] columns = new String[1];
               columns[0] = "nodata";
               dataSource = new DRDataSource(columns);
               dataSource.add(" ");
            }
         }
         if (strReportName.equalsIgnoreCase("BugsMovedTeamscount"))
         {
            if (arlBugsMovedTeamscount != null && !arlBugsMovedTeamscount.isEmpty())
            {
               String[] columns = new String[2];
               columns[0] = "MovedTeam";
               columns[1] = "Count";
               dataSource = new DRDataSource(columns);
               for (int intCount = 0; intCount < arlBugsMovedTeamscount.size(); intCount += 2)
               {
                  dataSource.add(arlBugsMovedTeamscount.get(intCount), arlBugsMovedTeamscount.get(intCount + 1));
               }
            }
            else
            {
               String[] columns = new String[1];
               columns[0] = "nodata";
               dataSource = new DRDataSource(columns);
               dataSource.add(" ");
            }
         }
         if (strReportName.equalsIgnoreCase("BugsSLA"))
         {
            String[] columns = new String[4];
            columns[0] = "Priority";
            columns[1] = "InSLA";
            columns[2] = "OutOfSLA";
            columns[3] = "AverageNoOfDays";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlBugsSLA.size(); intCount += 4)
            {
               dataSource.add(arlBugsSLA.get(intCount), arlBugsSLA.get(intCount + 1),
                       arlBugsSLA.get(intCount + 2), arlBugsSLA.get(intCount + 3));
            }
         }

         return dataSource;
      }
   }
}
