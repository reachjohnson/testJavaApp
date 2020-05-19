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

public class SLAReport
{
   Logger objLogger = Logger.getLogger(SLAReport.class.getName());
   String strFromDate;
   String strToDate;

   ArrayList<String> arlCounts;
   ArrayList<String> arlReceivedTasks;
   ArrayList<String> arlReceivedBugs;
   ArrayList<String> arlClosedTasks;
   ArrayList<String> arlClosedBugs;
   ArrayList<String> arlCurrentOpenTasks;
   ArrayList<String> arlCurrentOpenBugs;

   private HashMap<Integer, String> hpReportParameters;
   private int intReportNumbers;
   private OutputStream out;

   public SLAReport(String strFromDate, String strToDate, ArrayList<String> arlCounts, ArrayList<String> arlReceivedTasks,
                    ArrayList<String> arlReceivedBugs, ArrayList<String> arlClosedTasks, ArrayList<String> arlClosedBugs,
                    ArrayList<String> arlCurrentOpenTasks, ArrayList<String> arlCurrentOpenBugs, int intReportNumbers, HashMap<Integer, String> hpReportParameters,
                    OutputStream out) throws Exception
   {
      try
      {
         this.strFromDate = strFromDate;
         this.strToDate = strToDate;
         this.arlCounts = arlCounts;
         this.arlReceivedTasks = arlReceivedTasks;
         this.arlReceivedBugs = arlReceivedBugs;
         this.arlClosedTasks = arlClosedTasks;
         this.arlClosedBugs = arlClosedBugs;
         this.arlCurrentOpenTasks = arlCurrentOpenTasks;
         this.arlCurrentOpenBugs = arlCurrentOpenBugs;
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
                 .title(Templates.createTitleComponent("Online Payments Reports for the period " + strFromDate + " To " + strToDate))
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
         if (strReportName.equalsIgnoreCase("Counts"))
         {
            report.setTemplate(Templates.reportTemplate)
                    .title(cmp.text("Summary").setStyle(Templates.bold10LeftStyle));
            report.addColumn(col.column("Category", "category", type.stringType()).setStyle(Templates.columnStyle8String));
            report.addColumn(col.column("Tasks", "tasks", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Bugs", "bugs", type.stringType()).setStyle(Templates.columnStyle8Number));
            report.addColumn(col.column("Total", "total", type.stringType()).setStyle(Templates.columnStyle8Number));
         }
         if (strReportName.equalsIgnoreCase("ReceivedTasks"))
         {
            if (arlReceivedTasks != null && !arlReceivedTasks.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Received Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Received Date", "receiveddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Received Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("ReceivedBugs"))
         {
            if (arlReceivedBugs != null && !arlReceivedBugs.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Received Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Received Date", "receiveddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Received Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("ClosedTasks"))
         {
            if (arlClosedTasks != null && !arlClosedTasks.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Closed Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("Actual End Date", "actualenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Closed Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("ClosedBugs"))
         {
            if (arlClosedBugs != null && !arlClosedBugs.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Closed Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("Actual End Date", "actualenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Closed Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("CurrentOpenTasks"))
         {
            if (arlCurrentOpenTasks != null && !arlCurrentOpenTasks.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Current Open Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Received Date", "receiveddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Current Open Tasks").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
         }
         if (strReportName.equalsIgnoreCase("CurrentOpenBugs"))
         {
            if (arlCurrentOpenBugs != null && !arlCurrentOpenBugs.isEmpty())
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Current Open Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("S.No", "sno", type.integerType()).setStyle(Templates.columnStyle8String).setWidth(13));
               report.addColumn(col.column(" Ticket Id", "ticketid", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Ticket Description", "ticketdescription", type.stringType()).setStyle(Templates.columnStyle8String));
               report.addColumn(col.column("Ticket Priority", "ticketpriority", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
               report.addColumn(col.column("Received Date", "receiveddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
               report.addColumn(col.column("SLA End Date", "slaenddate", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(40));
            }
            else
            {
               report.setTemplate(Templates.reportTemplate)
                       .title(cmp.text("Current Open Bugs").setStyle(Templates.bold10LeftStyle));
               report.addColumn(col.column("No Data Available", "nodata", type.stringType()).setStyle(Templates.columnStyle8String).setWidth(35));
            }
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
         if (strReportName.equalsIgnoreCase("Counts"))
         {
            String[] columns = new String[4];
            columns[0] = "category";
            columns[1] = "tasks";
            columns[2] = "bugs";
            columns[3] = "total";
            dataSource = new DRDataSource(columns);

            for (int intCount = 0; intCount < arlCounts.size(); intCount += 4)
            {
               dataSource.add(arlCounts.get(intCount), arlCounts.get(intCount + 1), arlCounts.get(intCount + 2), arlCounts.get(intCount + 3));
            }
         }
         if (strReportName.equalsIgnoreCase("ReceivedTasks"))
         {
            if (arlReceivedTasks != null && !arlReceivedTasks.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "receiveddate";
               columns[5] = "slaenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlReceivedTasks.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlReceivedTasks.get(intCount + 1), arlReceivedTasks.get(intCount + 2),
                          arlReceivedTasks.get(intCount + 3), arlReceivedTasks.get(intCount + 4), arlReceivedTasks.get(intCount + 5));
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
         if (strReportName.equalsIgnoreCase("ReceivedBugs"))
         {
            if (arlReceivedBugs != null && !arlReceivedBugs.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "receiveddate";
               columns[5] = "slaenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlReceivedBugs.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlReceivedBugs.get(intCount + 1), arlReceivedBugs.get(intCount + 2),
                          arlReceivedBugs.get(intCount + 3), arlReceivedBugs.get(intCount + 4), arlReceivedBugs.get(intCount + 5));
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
         if (strReportName.equalsIgnoreCase("ClosedTasks"))
         {
            if (arlClosedTasks != null && !arlClosedTasks.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "slaenddate";
               columns[5] = "actualenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlClosedTasks.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlClosedTasks.get(intCount + 1), arlClosedTasks.get(intCount + 2),
                          arlClosedTasks.get(intCount + 3), arlClosedTasks.get(intCount + 4), arlClosedTasks.get(intCount + 5));
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
         if (strReportName.equalsIgnoreCase("ClosedBugs"))
         {
            if (arlClosedBugs != null && !arlClosedBugs.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "slaenddate";
               columns[5] = "actualenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlClosedBugs.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlClosedBugs.get(intCount + 1), arlClosedBugs.get(intCount + 2),
                          arlClosedBugs.get(intCount + 3), arlClosedBugs.get(intCount + 4), arlClosedBugs.get(intCount + 5));
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
         if (strReportName.equalsIgnoreCase("CurrentOpenTasks"))
         {
            if (arlCurrentOpenTasks != null && !arlCurrentOpenTasks.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "receiveddate";
               columns[5] = "slaenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlCurrentOpenTasks.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlCurrentOpenTasks.get(intCount + 1), arlCurrentOpenTasks.get(intCount + 2),
                          arlCurrentOpenTasks.get(intCount + 3), arlCurrentOpenTasks.get(intCount + 4), arlCurrentOpenTasks.get(intCount + 5));
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
         if (strReportName.equalsIgnoreCase("CurrentOpenBugs"))
         {
            if (arlCurrentOpenBugs != null && !arlCurrentOpenBugs.isEmpty())
            {
               String[] columns = new String[6];
               columns[0] = "sno";
               columns[1] = "ticketid";
               columns[2] = "ticketdescription";
               columns[3] = "ticketpriority";
               columns[4] = "receiveddate";
               columns[5] = "slaenddate";
               dataSource = new DRDataSource(columns);
               int intSlNo = 0;
               for (int intCount = 0; intCount < arlCurrentOpenBugs.size(); intCount += 6)
               {
                  dataSource.add(++intSlNo, " " + arlCurrentOpenBugs.get(intCount + 1), arlCurrentOpenBugs.get(intCount + 2),
                          arlCurrentOpenBugs.get(intCount + 3), arlCurrentOpenBugs.get(intCount + 4), arlCurrentOpenBugs.get(intCount + 5));
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

         return dataSource;
      }
   }
}
